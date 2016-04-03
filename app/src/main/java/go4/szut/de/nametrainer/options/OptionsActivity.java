package go4.szut.de.nametrainer.options;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.CompoundButton;
import android.widget.Toast;
import android.widget.ToggleButton;

import go4.szut.de.nametrainer.R;
import go4.szut.de.nametrainer.util.Util;

/**
 * Created by Michele on 24.03.2016.
 */
public class OptionsActivity extends Activity {

    private boolean soundEnabled;
    private boolean lastNameEnabled;
    private ToggleButton soundButton;
    private ToggleButton lastNameButton;

    public OptionsActivity(){
        //may initialisize something to save settings
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options);

        soundButton = (ToggleButton) findViewById(R.id.toggleButtonSound);
        soundEnabled = soundButton.isChecked();

        //soundButton.setChecked(-- load settings --);

        soundButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    switchSoundState();
                } else {
                    switchSoundState();
                }
                Log.d("options","State of Sound got changed");
            }

        });
        lastNameButton = (ToggleButton) findViewById(R.id.toggleButtonLastName);
        lastNameEnabled = lastNameButton.isChecked();
        //lastNameButton.setChecked(--load settings-);

        lastNameButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    switchLastNameState();
                } else {
                    switchLastNameState();
                }
                Log.d("options","State of lastName changed");
            }
        });

    }

    public boolean switchSoundState(){

        //Need to enable or disable Sound somehow

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

    public void saveSettings(){
        //Save settings at this Point ha ha ha
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        saveSettings();
    }

    @Override
    protected void onStop() {
        super.onStop();
        saveSettings();
    }

    public boolean isSoundEnabled() {
        return soundEnabled;
    }

    public boolean isLastNameEnabled() {
        return lastNameEnabled;
    }

}
