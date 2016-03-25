package go4.szut.de.nametrainer.groupeditor;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.widget.HorizontalScrollView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by ramazan on 25.03.2016.
 */
public class CustomHorizontalScrollView extends HorizontalScrollView {
    private long lastScrollUpdate = -1;
    private ArrayList<GroupListViewItem> GLVIList;

    public CustomHorizontalScrollView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
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

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if (lastScrollUpdate == -1) {
            onScrollStart();
            postDelayed(new ScrollStateHandler(), 100);
        }

        lastScrollUpdate = System.currentTimeMillis();
    }

    private void onScrollStart() {
        // do something NYEHAHAHA
    }

    private void onScrollEnd() {
        ArrayList<Integer> differenceToScreenCenter = new ArrayList<Integer>();
        Integer screenCenter = getWidth()/2;
        for(GroupListViewItem i : GLVIList){
            i.setHighlightOff();
            differenceToScreenCenter.add(Math.abs(screenCenter - (i.getPosition() + (i.getWidth() / 2))));
        }
        int minIndex = differenceToScreenCenter.indexOf(Collections.min(differenceToScreenCenter));
        //Toast.makeText(this.getContext(),String.valueOf(minIndex),Toast.LENGTH_LONG).show();

        GLVIList.get(minIndex).setHighlightOn();

    }
    public void setItemList(ArrayList<GroupListViewItem> GLVIList) {
        this.GLVIList = GLVIList;
    }

}
