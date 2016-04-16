package go4.szut.de.nametrainer.selection;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;

import go4.szut.de.nametrainer.R;
import go4.szut.de.nametrainer.database.Group;
import go4.szut.de.nametrainer.game.GameActivity;
import go4.szut.de.nametrainer.game.GameEngine;
import go4.szut.de.nametrainer.groupeditor.GroupEditorActivity;
import go4.szut.de.nametrainer.options.OptionsActivity;

/**
 * Created by raven on 25.03.2016.
 */
public class SelectionActivity extends AppCompatActivity implements View.OnClickListener {

    private ListView listView;
    private Intent intent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selection);

        listView = (ListView)findViewById(R.id.group_listview);
        SelectionActivityAdapter adapter = new SelectionActivityAdapter(this);
        listView.setAdapter(adapter);

        //Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    @Override
    public void onClick(View v) {
        LinearLayout linearLayout = (LinearLayout)v;
        Group group = (Group)linearLayout.getTag();
        Intent gameIntent = new Intent(this, GameActivity.class);
        gameIntent.putExtra(GameEngine.GAME_GROUP_OBJECT, group);
        startActivity(gameIntent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch(id) {
            case R.id.selection_settings_item:
                intent = new Intent(this, OptionsActivity.class);
                startActivity(intent);
                return true;
            case R.id.selection_editor_item:
                intent = new Intent(this, GroupEditorActivity.class);
                startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_selection_activity, menu);
        return true;
    }


}
