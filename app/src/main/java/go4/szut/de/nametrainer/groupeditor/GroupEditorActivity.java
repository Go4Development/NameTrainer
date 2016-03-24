package go4.szut.de.nametrainer.groupeditor;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import go4.szut.de.nametrainer.R;

/**
 * Created by Rene on 24.03.2016.
 */
public class GroupEditorActivity extends AppCompatActivity {

    //holds a bunch of horizontal positioned images of students of the current selected group
    private HorizontalScrollView portraitScrollView;
    private LinearLayout portraitLinearLayout;

    private ListView groupListView;
    private GroupEditorListViewAdapter groupListViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_groupeditor);

        portraitScrollView = (HorizontalScrollView)findViewById(R.id.portrait_scrollview);

        portraitLinearLayout = (LinearLayout)findViewById(R.id.portrait_linearlayout);

        groupListView = (ListView)findViewById(R.id.group_listview);
        groupListViewAdapter = new GroupEditorListViewAdapter();

        //groupListView.setAdapter(groupListViewAdapter);

        for(int i = 0; i < 10; i++) {
            final GroupListViewItem item = new GroupListViewItem(this, "Hans Vadder", "HV" + i);
            item.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Toast.makeText(GroupEditorActivity.this, item.getGalleryName(), Toast.LENGTH_LONG).show();
                }
            });
            portraitLinearLayout.addView(item);
        }


    }
}