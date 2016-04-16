package go4.szut.de.nametrainer.game;

import android.content.Intent;

import go4.szut.de.nametrainer.database.DataSource;
import go4.szut.de.nametrainer.database.Group;
import go4.szut.de.nametrainer.util.Util;

/**
 * Created by Rene on 08.04.2016.
 */
public class GameSetupBuilder {

    /**
     * holds the game activity instance
     */
    private GameActivity activity;
    private Intent gameActivityIntent;

    /**
     * holds the listener for game mode selected events
     */
    private OnGameModeListener gameModeListener;

    /**
     * holds the source to database
     */
    private DataSource source;

    /**
     * holds the game group object
     */
    private Group group;

    /**
     * GameSetupBuilder sets up the game mode the user is going to play
     * @param activity - the game activity
     */
    public GameSetupBuilder(GameActivity activity) {
        this.activity = activity;
        gameActivityIntent = activity.getIntent();
        this.gameModeListener = activity;
        source = DataSource.getDataSourceInstance(activity);

        group = retrieveGameGroupObject();

    }

    private Group retrieveGameGroupObject() {
        return gameActivityIntent.getParcelableExtra(GameActivity.GAME_GROUP_OBJECT);
    }


    /**
     * OnGameModeListener class invokes the setup of a specific
     * game mode that is randomly chosen by the GameSetupBuilder
     */
    public interface OnGameModeListener {
        public void onLetterAssigningGameMode();
        public void onNameAssigningGameMode();
    }


}
