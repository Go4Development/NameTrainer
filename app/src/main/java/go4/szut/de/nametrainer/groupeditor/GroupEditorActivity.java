package go4.szut.de.nametrainer.groupeditor;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;

import java.util.ArrayList;

import go4.szut.de.nametrainer.R;
import go4.szut.de.nametrainer.util.Util;

/**
 * Created by Rene on 24.03.2016.
 */
public class GroupEditorActivity extends AppCompatActivity
    implements View.OnClickListener, View.OnLongClickListener {

    //holds a bunch of horizontal positioned images of students of the current selected group
    private CustomHorizontalScrollView portraitScrollView;
    //the inner layout of the portraitScrollView
    private LinearLayout portraitLinearLayout;

    private ArrayList<GroupListViewItem> groupListViewItemList;

    //holds a list of groups containing multiple students
    private ListView groupListView;
    //holds the data for the groupListView
    private GroupEditorListViewAdapter groupListViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //sets the layout to the specified "groupeditor layout"
        setContentView(R.layout.activity_groupeditor);

        //hides the ActionBar of AppCompatActivity class
        Util.hideActionBar(this);

        /**
         * Load Data - Start
         */

        groupListViewAdapter = new GroupEditorListViewAdapter(this);
        groupListViewItemList = new ArrayList<GroupListViewItem>();

        for(int i = 0; i < 10; i++) {
            GroupListViewItem item = new GroupListViewItem(this, "Hans Vadder", "Vorname" + i, "Nachname" + i);
            item.setOnClickListener(this);
            item.setOnLongClickListener(this);
            groupListViewItemList.add(item);
        }

        /**
         * Load Data - End
         */

        //portrait stuff
        portraitScrollView = (CustomHorizontalScrollView)findViewById(R.id.portrait_scrollview);

        //grouplist stuff
        groupListView = (ListView)findViewById(R.id.group_listview);
        groupListView.setAdapter(groupListViewAdapter);

        //sets the list containing the GroupListViewItems
        portraitScrollView.setGroupViewItemList(groupListViewItemList);

    }

    @Override
    public void onClick(View v) {
        GroupListViewItem item = (GroupListViewItem)v;

    }

    @Override
    public boolean onLongClick(View v) {
        GroupListViewItem item = (GroupListViewItem)v;
        return true;
    }
}