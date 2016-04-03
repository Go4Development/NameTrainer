package go4.szut.de.nametrainer.options;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.CompoundButton;
import android.widget.ToggleButton;

import go4.szut.de.nametrainer.R;

/**
 * Created by Michele on 24.03.2016.
 */
public class OptionsActivity extends Activity {

    //Button States
    private boolean soundEnabled;
    private boolean lastNameEnabled;

   //Button
    private ToggleButton soundButton;
    private ToggleButton lastNameButton;

    //Shared-Prefereces Key-Value
    private SharedPreferences spref;
    private SharedPreferences.Editor sprefEdit;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options);

        spref = this.getPreferences(Context.MODE_PRIVATE);

        soundButton = (ToggleButton) findViewById(R.id.toggleButtonSound);
        boolean soundState = spref.getBoolean(getResources().getString(R.string.sound_state_key), false);
        soundEnabled = soundState;
        soundButton.setChecked(soundState);


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
        boolean lastNameState = spref.getBoolean(getResources().getString(R.string.lastName_state_key), false);
        lastNameEnabled = lastNameState;
        lastNameButton.setChecked(lastNameState);

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

        if(soundEnabled){
            soundEnabled = false;
            soundButton.setChecked(soundEnabled);
        } else{
            soundEnabled = true;
            soundButton.setChecked(soundEnabled);
        }

        sprefEdit = spref.edit();
        sprefEdit.putBoolean(getResources().getString(R.string.sound_state_key), soundEnabled);
        sprefEdit.commit();
        return soundEnabled;
    }

    public boolean switchLastNameState(){

        if(lastNameEnabled){
            lastNameEnabled = false;
            lastNameButton.setChecked(lastNameEnabled);
        }else{
            lastNameEnabled = true;
            lastNameButton.setChecked(lastNameEnabled);
        }
        sprefEdit = spref.edit();
        sprefEdit.putBoolean(getResources().getString(R.string.lastName_state_key), lastNameEnabled);
        sprefEdit.commit();
        return lastNameEnabled;
    }

    public void saveSettings(){
        sprefEdit = spref.edit();
        sprefEdit.putBoolean(getResources().getString(R.string.sound_state_key), isSoundEnabled());
        sprefEdit.putBoolean(getResources().getString(R.string.lastName_state_key), isLastNameEnabled());
        sprefEdit.commit();
    }

    @Override
    protected void onDestroy() {
        saveSettings();
        super.onDestroy();
    }

    @Override
    protected void onStop() {
        saveSettings();
        super.onStop();
    }

    public boolean isSoundEnabled() {
        return soundEnabled;
    }

    public boolean isLastNameEnabled() {
        return lastNameEnabled;
    }

}
