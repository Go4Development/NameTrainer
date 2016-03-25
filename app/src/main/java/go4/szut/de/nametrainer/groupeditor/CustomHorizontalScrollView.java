package go4.szut.de.nametrainer.groupeditor;

import android.content.Context;
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

    //holds the linear layout of this CustomHorizontalScrollView
    private LinearLayout portraitLinearLayout;

    //holds the timestamp of the last scroll update
    private long lastScrollUpdate = -1;

    private ArrayList<GroupListViewItem> groupListViewItemList;

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

    /**
     * Sets the groupListViewItemList
     * @param groupListViewItemList - the groupListViewItemList
     */
    public void setGroupViewItemList(ArrayList<GroupListViewItem> groupListViewItemList) {
        this.groupListViewItemList = groupListViewItemList;
        portraitLinearLayout = (LinearLayout)findViewById(R.id.portrait_linearlayout);
        attachListViewItems();
    }

    private void attachListViewItems() {
        for(GroupListViewItem item : groupListViewItemList) {
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
        for(GroupListViewItem groupListViewItem : groupListViewItemList){
            groupListViewItem.setHighlightOff();
            differenceToScreenCenter.add(Math.abs(screenCenter - (
                    groupListViewItem.getPosition() + (groupListViewItem.getWidth() / 2))));
        }
        int minIndex = differenceToScreenCenter.indexOf(Collections.min(differenceToScreenCenter));
        groupListViewItemList.get(minIndex).setHighlightOn();

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
