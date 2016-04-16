package go4.szut.de.nametrainer.game;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;

import go4.szut.de.nametrainer.R;
import go4.szut.de.nametrainer.database.Member;
import go4.szut.de.nametrainer.util.Util;

/**
 * Created by Rene on 24.03.2016.
 */
public class GameActivity extends AppCompatActivity implements
        GameEngine.OnGameModeListener, GameEngine.OnCompleteListener {

    //layout id for game mode for assigning letters to blank spaces in order to build a persons name
    public static final int GAME_MODE_LETTER_ASSIGNING = R.layout.activity_game_mode1;
    public static final int GAME_MODE_LETTER_ASSIGNING_IDENTIFIER = 0;

    //layout id for game mode for assigning names to blank spaces in order to match a name to a picture
    public static final int GAME_MODE_NAME_ASSIGNING = R.layout.activity_game_mode2;
    public static final int GAME_MODE_NAME_ASSIGNING_IDENTIFIER = 1;


    private GameEngine engine;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        engine = new GameEngine(this);
    }


    @Override
    public void onLetterAssigningGameMode(Member menber) {
        Util.D.l(this, "Engine has chosen : Letter Assigning Mode");
        setContentView(GAME_MODE_LETTER_ASSIGNING);
    }

    @Override
    public void onNameAssigningGameMode(ArrayList<Member> members) {
        Util.D.l(this, "Engine has chosen : Name Assigning Mode");
        setContentView(GAME_MODE_NAME_ASSIGNING);
    }

    @Override
    public void onComplete() {

    }

}
