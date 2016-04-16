package go4.szut.de.nametrainer.game;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import go4.szut.de.nametrainer.R;
import go4.szut.de.nametrainer.util.Util;

/**
 * Created by Rene on 24.03.2016.
 */
public class GameActivity extends AppCompatActivity implements GameSetupBuilder.OnGameModeListener {

    //layout id for game mode for assigning letters to blank spaces in order to build a persons name
    public static final int GAME_MODE_LETTER_ASSIGNING = R.layout.activity_game_mode1;
    public static final int GAME_MODE_LETTER_ASSIGNING_IDENTIFIER = 0;

    //layout id for game mode for assigning names to blank spaces in order to match a name to a picture
    public static final int GAME_MODE_NAME_ASSIGNING = R.layout.activity_game_mode2;
    public static final int GAME_MODE_NAME_ASSIGNING_IDENTIFIER = 1;

    //key for retrieving game group object from intent extras
    public static final String GAME_GROUP_OBJECT = "game_group";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new GameSetupBuilder(this);
    }


    @Override
    public void onLetterAssigningGameMode() {
        Toast.makeText(this, "Letter", Toast.LENGTH_LONG).show();
        setContentView(GAME_MODE_LETTER_ASSIGNING);
        Util.D.l(this, "Letter Assigning Mode");
    }

    @Override
    public void onNameAssigningGameMode() {
        Toast.makeText(this, "Name", Toast.LENGTH_LONG).show();
        setContentView(GAME_MODE_NAME_ASSIGNING);
        Util.D.l(this, "Name Assigning Mode");
    }
}
