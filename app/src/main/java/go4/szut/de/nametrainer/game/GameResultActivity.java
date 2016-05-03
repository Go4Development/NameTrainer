package go4.szut.de.nametrainer.game;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import go4.szut.de.nametrainer.R;

/**
 * Created by Rene on 16.04.2016.
 */
public class GameResultActivity extends AppCompatActivity {

    /**
     * identifier for this activity
     */
    public static final int IDENTIFIER = 666;

    public static final String PLAY_AGAIN_DATA = "play_data";
    public static final String RIGHT_MATCHES_VALUE = "right_matches";
    public static final String WRONG_MATCHES_VALUE = "wrong_matches";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_result);
    }



}
