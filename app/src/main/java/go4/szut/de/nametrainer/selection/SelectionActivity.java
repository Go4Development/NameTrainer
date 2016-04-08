package go4.szut.de.nametrainer.selection;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import go4.szut.de.nametrainer.R;

/**
 * Created by raven on 25.03.2016.
 */
public class SelectionActivity extends AppCompatActivity implements View.OnClickListener{

    private ListView listView;

    public SelectionActivity(){

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selection);

        listView = (ListView)findViewById(R.id.group_listview);
        SelectionActivityAdapter adapter = new SelectionActivityAdapter(this);
        listView.setAdapter(adapter);
        Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    @Override
    public void onClick(View v) {
        TextView textView = (TextView)v;
        //Toast
        Toast.makeText(this, textView.getText(), Toast.LENGTH_LONG).show();
    }
}
