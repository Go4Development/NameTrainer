package go4.szut.de.nametrainer.game;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import go4.szut.de.nametrainer.R;

/**
 * Created by Rene on 24.03.2016.
 */
public class GameActivity extends AppCompatActivity implements GameSetupBuilder.OnGameModeListener {

    //layout id for game mode for assigning letters to blank spaces in order to build a persons name
    public static final int GAME_MODE_LETTER_ASSIGNING = R.layout.activity_game_mode1;
    //layout id for game mode for assigning names to blank spaces in order to match a name to a picture
    public static final int GAME_MODE_NAME_ASSIGNING = R.layout.activity_game_mode2;

    //key for retrieving game group object from intent extras
    public static final String GAME_GROUP_OBJECT = "game_group";

    //setup builder decides which game mode the player is going to play and builds up the corresponding views
    private GameSetupBuilder setupBuilder;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupBuilder = new GameSetupBuilder(this);
    }


    @Override
    public void onLetterAssigningGameMode() {

    }

    @Override
    public void onNameAssigningGameMode() {

    }
}
