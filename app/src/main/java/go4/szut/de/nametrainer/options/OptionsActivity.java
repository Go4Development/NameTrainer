package go4.szut.de.nametrainer.options;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.CompoundButton;
import android.widget.Switch;

import go4.szut.de.nametrainer.R;

/**
 * Created by Michele on 24.03.2016.
 */
public class OptionsActivity extends AppCompatActivity {

    private boolean soundEnabled;
    private boolean lastNameEnabled;
    private Switch soundButton;
    private Switch lastNameButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options);
        Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        soundButton = (Switch) findViewById(R.id.toggleButtonSound);
        soundEnabled = soundButton.isChecked();
        soundButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    switchSoundState();
                } else {
                    switchSoundState();
                }
            }
        });
        lastNameButton = (Switch) findViewById(R.id.toggleButtonLastName);
        lastNameEnabled= lastNameButton.isChecked();
        lastNameButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    switchLastNameState();
                } else {
                    switchLastNameState();
                }
            }
        });
        //addListenerOnButton();

    }

    public boolean switchSoundState(){

        //Need to enable or diable Sound somehow

        if(soundEnabled){
            soundEnabled = false;
            soundButton.setChecked(soundEnabled);
        } else{
            soundEnabled = true;
            soundButton.setChecked(soundEnabled);
        }
        return soundEnabled;
    }

    public boolean switchLastNameState(){

        //Need to save this in global varibale for future requests

        if(lastNameEnabled){
            lastNameEnabled = false;
            lastNameButton.setChecked(lastNameEnabled);
        }else{
            lastNameEnabled = true;
            lastNameButton.setChecked(lastNameEnabled);
        }
        return lastNameEnabled;
    }

    public boolean isSoundEnabled() {
        return soundEnabled;
    }

    public boolean isLastNameEnabled() {
        return lastNameEnabled;
    }

}
