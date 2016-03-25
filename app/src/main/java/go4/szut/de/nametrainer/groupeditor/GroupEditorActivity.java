package go4.szut.de.nametrainer.groupeditor;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import go4.szut.de.nametrainer.R;
import go4.szut.de.nametrainer.util.Util;

/**
 * Created by Rene on 24.03.2016.
 */
public class GroupEditorActivity extends AppCompatActivity
    implements View.OnClickListener {

    //holds a bunch of horizontal positioned images of students of the current selected group
    private CustomHorizontalScrollView portraitScrollView;
    //the inner layout of the portraitScrollView
    private LinearLayout portraitLinearLayout;

    private ArrayList<GroupListViewItem> GLVIList;

    //holds a list of groups containing multiple students
    private ListView groupListView;
    //holds the data for the groupListView
    private GroupEditorListViewAdapter groupListViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //TODO Hier Datenbank Anfrage machen, um die benötigten Daten zu laden

        //sets the layout to the specified "groupeditor layout"
        setContentView(R.layout.activity_groupeditor);

        //hides the ActionBar of AppCompatActivity class
        Util.hideActionBar(this);

        portraitScrollView = (CustomHorizontalScrollView)findViewById(R.id.portrait_scrollview);
        portraitLinearLayout = (LinearLayout)findViewById(R.id.portrait_linearlayout);

        groupListViewAdapter = new GroupEditorListViewAdapter();
        groupListView = (ListView)findViewById(R.id.group_listview);
        //groupListView.setAdapter(groupListViewAdapter);

        GLVIList = new ArrayList<GroupListViewItem>();

        for(int i = 0; i < 10; i++) {
            GroupListViewItem item = new GroupListViewItem(this, "Hans Vadder", "HV" + i);
            item.setOnClickListener(this);
            portraitLinearLayout.addView(item);
            GLVIList.add(item);
        }

        portraitScrollView.setItemList(GLVIList);

    }

    @Override
    public void onClick(View v) {
        GroupListViewItem item = (GroupListViewItem)v;
        //Toast.makeText(this, String.valueOf(item.getPosition()), Toast.LENGTH_LONG).show();
    }
    public void onScrollEnd(){

    }
}