package go4.szut.de.nametrainer.game;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import go4.szut.de.nametrainer.R;
import go4.szut.de.nametrainer.database.Group;
import go4.szut.de.nametrainer.main.MainActivity;

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

    private TextView totalScoreValueTextView;
    private TextView wrongMatchesTextView;
    private TextView playAgainGroupNameTextView;

    private Group playAgainData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_result);

        totalScoreValueTextView = (TextView)findViewById(R.id.total_score_value_textview);
        wrongMatchesTextView = (TextView)findViewById(R.id.wrong_matches_value_textview);


        int rightMatches = getIntent().getIntExtra(RIGHT_MATCHES_VALUE, 0);
        int wrongMatches = getIntent().getIntExtra(WRONG_MATCHES_VALUE, 0);

        totalScoreValueTextView.setText(String.valueOf(((rightMatches / (1 + wrongMatches)) * 100)));
        wrongMatchesTextView.setText(String.valueOf(wrongMatches));

        playAgainData = getIntent().getParcelableExtra(PLAY_AGAIN_DATA);

        playAgainGroupNameTextView = (TextView)findViewById(R.id.play_again_group_name_textview);
        playAgainGroupNameTextView.setText(playAgainData.getName());

    }

    @Override
    public void onBackPressed() {

    }

    public void onPlayAgain(View view) {
        //if playAgainData has a value the GameActivity will be forced to reopen the GameActivity with this group
        putIntentData();
        finish();
    }

    public void onBackToMenu(View view) {
        playAgainData = null; //if playAgainData is null the GameActivity will be forced to open MainActivity
        putIntentData();
        finish();
    }

    private void putIntentData() {
        Intent data = new Intent();
        data.putExtra(PLAY_AGAIN_DATA, playAgainData);
        setResult(RESULT_OK, data);
    }

}
