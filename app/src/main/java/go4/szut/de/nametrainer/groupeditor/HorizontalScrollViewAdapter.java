package go4.szut.de.nametrainer.groupeditor;

import android.content.Context;

import java.util.ArrayList;

import go4.szut.de.nametrainer.database.DataSource;
import go4.szut.de.nametrainer.database.Group;
import go4.szut.de.nametrainer.database.Member;

/**
 * Created by Rene on 31.03.2016.
 */
public class HorizontalScrollViewAdapter {

    private GroupEditorActivity activity;
    private Group group;
    private DataSource source;

    private ArrayList<Member> members;
    private ArrayList<HorizontalScrollViewItem> items;

    private CustomHorizontalScrollView scrollView;
    private HorizontalScrollViewItemListener listener;

    public HorizontalScrollViewAdapter(GroupEditorActivity activity, CustomHorizontalScrollView scrollView, Group group) {
        this.activity = activity;
        this.group = group;
        this.scrollView = scrollView;

        listener = new HorizontalScrollViewItemListener(activity, this);


        source = DataSource.getDataSourceInstance(activity);
        source.open();
        members = source.getMembers(group.getId());
        source.close();

        items = createHorizontalScrollViewItems();


    }

    public HorizontalScrollViewItem getHorizontalScrollViewItemAt(int index) {
        return items.get(index);
    }

    public int getSize() {
        return items.size();
    }

    public void update() {
        source = DataSource.getDataSourceInstance(activity);
        source.open();
        members = source.getMembers(group.getId());
        source.close();
        items = createHorizontalScrollViewItems();
        scrollView.update();
    }

    public ArrayList<HorizontalScrollViewItem> createHorizontalScrollViewItems() {
        ArrayList<HorizontalScrollViewItem> items = new ArrayList<HorizontalScrollViewItem>();
        for(Member member : members) {
            HorizontalScrollViewItem item = new HorizontalScrollViewItem(activity, member);
            item.setOnLongClickListener(listener);
            items.add(item);
        }
        return items;
    }

    public HorizontalScrollViewItemListener getListener() {
        return listener;
    }



}
