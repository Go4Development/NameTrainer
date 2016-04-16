package go4.szut.de.nametrainer.groupeditor;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.util.AttributeSet;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.Collections;

import go4.szut.de.nametrainer.R;
import go4.szut.de.nametrainer.util.Util;

/**
 * Created by ramazan on 25.03.2016.
 */
public class CustomHorizontalScrollView extends HorizontalScrollView {

    //holds the linear layout of this CustomHorizontalScrollVie
    private LinearLayout portraitLinearLayout;

    //holds the timestamp of the last scroll update
    private long lastScrollUpdate = -1;

    //adapter that holds data
    private HorizontalScrollViewAdapter adapter;


    public CustomHorizontalScrollView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }


    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if (lastScrollUpdate == -1) {
            onScrollStart();
            postDelayed(new ScrollStateHandler(), 10);
        }
        lastScrollUpdate = System.currentTimeMillis();
    }

    public void setAdapter(HorizontalScrollViewAdapter adapter) {
        this.adapter = adapter;
        portraitLinearLayout = (LinearLayout)findViewById(R.id.portrait_linearlayout);
        attachListViewItems();
    }

    private void attachListViewItems() {
        if(adapter != null) {
            for (int i = 0; i < adapter.getSize(); i++) {
                HorizontalScrollViewItem item = adapter.getHorizontalScrollViewItemAt(i);
                portraitLinearLayout.addView(item);
                if (item.getGalleryImageView().getDrawable() == null){
                    item.getGalleryImageView()
                            .setImageBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.ic_add_black_48dp));
                }
                Util.D.l(this,"Width: " + (item.getGalleryImageView().getDrawable() == null));


            }


        }
    }

    public void update() {
        portraitLinearLayout.removeAllViews();
        attachListViewItems();
    }

    /**
     *
     */
    private void onScrollStart() {
        //nothing
    }

    /**
     *
     */
    private void onScrollEnd() {
        ArrayList<Integer> differenceToScreenCenter = new ArrayList<Integer>();
        Integer screenCenter = getWidth() / 2;

        for(int i = 0; i < adapter.getSize(); i++) {
            HorizontalScrollViewItem item = adapter.getHorizontalScrollViewItemAt(i);
            item.setHighlightOff();
            differenceToScreenCenter.add(Math.abs(screenCenter - (item.getPosition() + (item.getWidth() / 2))));
        }
        if(adapter.getSize() != 0){
            int minIndex = differenceToScreenCenter.indexOf(Collections.min(differenceToScreenCenter));
            adapter.getHorizontalScrollViewItemAt(minIndex).setHighlightOn();
        }


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
