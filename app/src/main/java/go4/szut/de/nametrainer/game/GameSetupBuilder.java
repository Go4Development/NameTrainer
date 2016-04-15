package go4.szut.de.nametrainer.game;

import go4.szut.de.nametrainer.database.DataSource;

/**
 * Created by Rene on 08.04.2016.
 */
public class GameSetupBuilder {

    private GameActivity activity;
    private OnGameModeListener gameModeListener;
    private DataSource source;

    public GameSetupBuilder(GameActivity activity) {
        this.activity = activity;
        this.gameModeListener = activity;
        source = DataSource.getDataSourceInstance(activity);
    }


    public interface OnGameModeListener {
        public void onLetterAssigningGameMode();
        public void onNameAssigningGameMode();
    }


}
