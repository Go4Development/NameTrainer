package go4.szut.de.nametrainer.groupeditor;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.Collections;

import go4.szut.de.nametrainer.R;

/**
 * Created by ramazan on 25.03.2016.
 */
public class CustomHorizontalScrollView extends HorizontalScrollView {

    //holds the linear layout of this CustomHorizontalScrollVie
    private LinearLayout portraitLinearLayout;

    //holds the timestamp of the last scroll update
    private long lastScrollUpdate = -1;

    private ArrayList<HorizontalScrollViewItem> horizontalScrollViewItems;

    public CustomHorizontalScrollView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }


    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if (lastScrollUpdate == -1) {
            onScrollStart();
            postDelayed(new ScrollStateHandler(), 100);
        }
        lastScrollUpdate = System.currentTimeMillis();
    }

    public void setPortraitItems(ArrayList<HorizontalScrollViewItem> horizontalScrollViewItems) {
        this.horizontalScrollViewItems = horizontalScrollViewItems;
        portraitLinearLayout = (LinearLayout)findViewById(R.id.portrait_linearlayout);
        attachListViewItems();
    }

    private void attachListViewItems() {
        for(HorizontalScrollViewItem item : horizontalScrollViewItems) {
            portraitLinearLayout.addView(item);
        }
    }

    /**
     *
     */
    private void onScrollStart() {
        //TODO Hm?
    }

    /**
     *
     */
    private void onScrollEnd() {
        ArrayList<Integer> differenceToScreenCenter = new ArrayList<Integer>();
        Integer screenCenter = getWidth() / 2;
        for(HorizontalScrollViewItem item : horizontalScrollViewItems){
            item.setHighlightOff();
            differenceToScreenCenter.add(Math.abs(screenCenter - (
                    item.getPosition() + (item.getWidth() / 2))));
        }
        int minIndex = differenceToScreenCenter.indexOf(Collections.min(differenceToScreenCenter));
        horizontalScrollViewItems.get(minIndex).setHighlightOn();

    }

    private class ScrollStateHandler implements Runnable {

        @Override
        public void run() {
            long currentTime = System.currentTimeMillis();
            if ((currentTime - lastScrollUpdate) > 100) {
                lastScrollUpdate = -1;
                onScrollEnd();
            } else {
                postDelayed(this, 100);
            }
        }
    }

}
