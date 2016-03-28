package go4.szut.de.nametrainer.selection;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import go4.szut.de.nametrainer.R;

/**
 * Created by raven on 25.03.2016.
 */
public class SelectionActivity extends Activity implements View.OnClickListener{

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


    }

    @Override
    public void onClick(View v) {
        TextView textView = (TextView)v;
        //Toast
        Toast.makeText(this, textView.getText(), Toast.LENGTH_LONG).show();
    }
}
