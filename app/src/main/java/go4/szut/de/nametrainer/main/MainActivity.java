package go4.szut.de.nametrainer.main;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import go4.szut.de.nametrainer.R;
import go4.szut.de.nametrainer.options.OptionsActivity;
import go4.szut.de.nametrainer.selection.SelectionActivity;
import go4.szut.de.nametrainer.sharing.SharingActivity;

public class MainActivity extends AppCompatActivity {

    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /** Called when the user clicks the Send button */
    public void startSelectionActivtiy(View view) {
        intent = new Intent(this, SelectionActivity.class);

    }

    /** Called when the user clicks the Send button */
    public void startSharingActivity(View view) {
        intent = new Intent(this, SharingActivity.class);

    }

    /** Called when the user clicks the Send button */
    public void startOptionsActivity(View view) {
        intent = new Intent(this, OptionsActivity.class);

    }

    /** Called when the user clicks the Send button */
    public void exit(View view) {
        finish();

    }
}
