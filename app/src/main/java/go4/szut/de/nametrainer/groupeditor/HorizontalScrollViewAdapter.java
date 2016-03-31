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

    private Context context;
    private Group group;
    private DataSource source;

    private ArrayList<Member> members;
    private ArrayList<HorizontalScrollViewItem> items;

    private CustomHorizontalScrollView scrollView;
    private HorizontalScrollViewItemListener listener;

    public HorizontalScrollViewAdapter(Context context, CustomHorizontalScrollView scrollView, Group group) {
        this.context = context;
        this.group = group;
        this.scrollView = scrollView;

        listener = new HorizontalScrollViewItemListener(context, this);

        members = new ArrayList<Member>();
        members.add(Member.toMember(1, "Hans", "Vadder", "Test"));
        members.add(Member.toMember(1, "Hansa", "Vaddera", "Test"));
        members.add(Member.toMember(1, "Hansas", "Vadderas", "Test"));
        members.add(Member.toMember(1, "Hans", "Vadder", "Test"));
        members.add(Member.toMember(1, "Hansa", "Vaddera", "Test"));
        members.add(Member.toMember(1, "Hansas", "Vadderas", "Test"));

        items = createHorizontalScrollViewItems();

       /* source = DataSource.getDataSourceInstance(context);
        source.open();
        members = source.getMembers(group.getId());
        source.close();
        */
    }

    public HorizontalScrollViewItem getHorizontalScrollViewItemAt(int index) {
        return items.get(index);
    }

    public int getSize() {
        return items.size();
    }

    public void update() {
        source = DataSource.getDataSourceInstance(context);
        source.open();
        members = source.getMembers(group.getId());
        source.close();
        createHorizontalScrollViewItems();
        scrollView.update();
    }

    public ArrayList<HorizontalScrollViewItem> createHorizontalScrollViewItems() {
        ArrayList<HorizontalScrollViewItem> items = new ArrayList<HorizontalScrollViewItem>();
        for(Member member : members) {
            HorizontalScrollViewItem item = new HorizontalScrollViewItem(context, member);
            item.setOnLongClickListener(listener);
            items.add(item);
        }
        return items;
    }



}
