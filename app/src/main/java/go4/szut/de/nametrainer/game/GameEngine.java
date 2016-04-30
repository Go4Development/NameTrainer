package go4.szut.de.nametrainer.game;

import android.content.Intent;

import java.util.ArrayList;
import java.util.Random;

import go4.szut.de.nametrainer.database.DataSource;
import go4.szut.de.nametrainer.database.Group;
import go4.szut.de.nametrainer.database.Member;
import go4.szut.de.nametrainer.util.Util;

/**
 * Created by Rene on 08.04.2016.
 */
public class GameEngine {

    /**
     * key for retrieving game group object from intent extras
     */
    public static final String GAME_GROUP_OBJECT = "game_group";

    public static final int MEMBERS_COUNT = 6;

    /**
     * holds the game activity instance
     */
    private GameActivity activity;
    private Intent gameActivityIntent;

    /**
     * holds the listener for game mode selected events
     */
    private OnGameModeListener gameModeListener;
    private OnCompleteListener completeListener;

    /**
     * holds the source to database
     */
    private DataSource source;

    /**
     * holds the game group object
     */
    private Group group;
    private ArrayList<Member> members;

    /**
     * GameSetupBuilder sets up the game mode the user is going to play
     * @param activity - the game activity
     */
    public GameEngine(GameActivity activity) {
        this.activity = activity;

        this.gameModeListener = activity;
        this.completeListener = activity;

        source = DataSource.getDataSourceInstance(activity);
        gameActivityIntent = activity.getIntent();

        loadGameData();
    }

    private void loadGameData() {
        group = retrieveGameGroupObject();
        source.open();
        members = source.getMembers(group.getId());
        Util.D.l(this, members.size() + "Members of Group " + group.getName() + " loaded!");
        source.close();

        members = testGenerateRandomMembers();
        gameModeListener.onNameAssigningGameMode(members);
    }

    private Group retrieveGameGroupObject() {
        return gameActivityIntent.getParcelableExtra(GAME_GROUP_OBJECT);
    }

    private ArrayList<Member> testGenerateRandomMembers() {
        ArrayList<Member> randomMembers = new ArrayList<Member>();
        Random r = new Random();
        for(int i = 0; i < MEMBERS_COUNT; i++) {
            randomMembers.add(members.get(r.nextInt(members.size())));
        }
        return randomMembers;
    }

    private void startGameMode() {
        Random r = new Random();
        int mode = r.nextInt(2);
        switch(mode) {
            case GameActivity.GAME_MODE_LETTER_ASSIGNING_IDENTIFIER:
                gameModeListener.onLetterAssigningGameMode(null);
                break;
            case GameActivity.GAME_MODE_NAME_ASSIGNING_IDENTIFIER:
                gameModeListener.onNameAssigningGameMode(null);
                break;
        }
    }

    public void start() {

    }


    /**
     * OnGameModeListener class invokes the setup of a specific
     * game mode that is randomly chosen by the GameEngine
     */
    public interface OnGameModeListener {
        public void onLetterAssigningGameMode(Member member);
        public void onNameAssigningGameMode(ArrayList<Member> members);
    }

    /**
     * OnCompleteListener class invokes the event of onComplete
     * if a game of a selected group is completed
     */
    public interface OnCompleteListener {
        public void onComplete(Group group);
    }


}
