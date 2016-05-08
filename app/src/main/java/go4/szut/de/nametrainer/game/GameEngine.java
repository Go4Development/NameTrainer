package go4.szut.de.nametrainer.game;

import android.content.ClipData;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.GridLayout;
import android.text.method.Touch;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.Random;

import go4.szut.de.nametrainer.R;
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
    private OnEngineListener engineListener;

    private NameAssigningModeOnDragListener nameAssigningModeOnDragListener;
    private LetterAssigningModeOnDragListener letterAssigningModeOnDragListener;
    private TouchListener touchListener;

    /**
     * holds the source to database
     */
    private DataSource source;

    /**
     * holds the stage count
     */
    private int stageCount;

    /**
     * holds the game group object
     */
    private Group group;
    private ArrayList<Member> memberPool;

    private int stageIndex;
    private ArrayList<Integer> stagePattern;

    private int totalWrongMatches;
    private int totalRightMatches;
    private int wrongMatches;
    private int rightMatches;

    /**
     * GameSetupBuilder sets up the game mode the user is going to play
     * @param activity - the game activity
     */
    public GameEngine(GameActivity activity) {

        this.activity = activity;

        this.engineListener = activity;

        nameAssigningModeOnDragListener = new NameAssigningModeOnDragListener(this, activity);
        letterAssigningModeOnDragListener = new LetterAssigningModeOnDragListener(this, activity);
        touchListener = new TouchListener();

        source = DataSource.getDataSourceInstance(activity);
        gameActivityIntent = activity.getIntent();

        loadGameData();
    }

    private void loadGameData() {
        group = retrieveGameGroupObject();
        source.open();
        memberPool = source.getMembers(group.getId());
        Util.D.l(this, memberPool.size() + "Members of Group " + group.getName() + " loaded!");
        source.close();
        stagePattern = generateStagePattern();
    }

    private Group retrieveGameGroupObject() {
        return gameActivityIntent.getParcelableExtra(GAME_GROUP_OBJECT);
    }

    private ArrayList<Integer> generateStagePattern() {
        ArrayList<Integer> stagePattern = new ArrayList<Integer>();
        int totalMemberCount = memberPool.size();
        int nameAssigningModeCount = totalMemberCount / MEMBERS_COUNT;
        int remainMemberCount = totalMemberCount % MEMBERS_COUNT;
        if(totalMemberCount != MEMBERS_COUNT && remainMemberCount == 0) {
            nameAssigningModeCount--;
            remainMemberCount = MEMBERS_COUNT;
        }
        int stageCount = nameAssigningModeCount + remainMemberCount;
        for(int i = 0; i < stageCount; i++) {
            if(nameAssigningModeCount != 0) {
                stagePattern.add(GameActivity.GAME_MODE_NAME_ASSIGNING_IDENTIFIER);
                nameAssigningModeCount--;
            }
            if(totalMemberCount != 0) {
                stagePattern.add(GameActivity.GAME_MODE_LETTER_ASSIGNING_IDENTIFIER);
                totalMemberCount--;
            }
        }
        return stagePattern;
    }

    private ArrayList<Member> pickRandomMembers(int mode) {
        Random random = new Random();
        ArrayList<Member> randomMembers = new ArrayList<Member>();
        int memberCount = (mode == GameActivity.GAME_MODE_NAME_ASSIGNING_IDENTIFIER) ? MEMBERS_COUNT : 1;
        if(memberPool.size() != 0) {
            for(int i = 0; i < memberCount; i++) {
                Member randomMember = memberPool.remove(random.nextInt(memberPool.size()));
                randomMembers.add(randomMember);
            }
        }
        return randomMembers;
    }

    private void startGameMode(int mode) {
        ArrayList<Member> members;
        switch(mode) {
            case GameActivity.GAME_MODE_LETTER_ASSIGNING_IDENTIFIER:
                members = pickRandomMembers(mode);
                engineListener.onLetterAssigningGameMode(members.get(0));
                break;
            case GameActivity.GAME_MODE_NAME_ASSIGNING_IDENTIFIER:
                members = pickRandomMembers(mode);
                engineListener.onNameAssigningGameMode(members);
                break;
        }
    }


    public void start() {
        stageIndex = 0;
        startGameMode(stagePattern.get(stageIndex));
    }

    public void next() {
        //TODO check inputs
        if(stageIndex + 1 < stagePattern.size()) {
            stageIndex++;
            startGameMode(stagePattern.get(stageIndex));
        } else {
            engineListener.onComplete(group, totalRightMatches, totalWrongMatches);
        }
    }

    public NameAssigningModeOnDragListener getNameAssigningModeOnDragListener() {
        return nameAssigningModeOnDragListener;
    }

    public LetterAssigningModeOnDragListener getLetterAssigningModeOnDragListener() {
        return letterAssigningModeOnDragListener;
    }

    public TouchListener getTouchListener() {
        return touchListener;
    }


    /**
     * OnGameModeListener class invokes the setup of a specific
     * game mode that is randomly chosen by the GameEngine
     */
    public interface OnEngineListener {
        public void onLetterAssigningGameMode(Member member);
        public void onNameAssigningGameMode(ArrayList<Member> members);
        public void onComplete(Group group, int wrongMatches, int rightMatches);
        public void onStageCompleted();
    }

    private static class NameAssigningModeOnDragListener implements View.OnDragListener {

        private GameActivity activity;
        private GameEngine engine;

        private final Drawable enterShape;
        private final Drawable normalShape;
        private final Drawable rightShape;

        public NameAssigningModeOnDragListener(GameEngine engine, GameActivity activity) {
            this.activity = activity;
            this.engine = engine;
            enterShape = ResourcesCompat.getDrawable(activity.getResources(), R.drawable.shape_droptarget, null);
            normalShape = ResourcesCompat.getDrawable(activity.getResources(), R.drawable.shape, null);
            rightShape = ResourcesCompat.getDrawable(activity.getResources(), R.drawable.shape_rightdroptarget, null);
        }


        @Override
        public boolean onDrag(View v, DragEvent event) {
            ViewGroup dropTarget = (ViewGroup) v;
            View initialDropTarget = activity.findViewById(R.id.initial_droptarget);
            switch (event.getAction()) {
                case DragEvent.ACTION_DRAG_STARTED:
                    // do nothing
                    break;
                case DragEvent.ACTION_DRAG_ENTERED:
                    if(dropTarget.getChildCount() == 0 && v != initialDropTarget)
                        v.setBackground(enterShape);
                    break;
                case DragEvent.ACTION_DRAG_EXITED:
                    if(dropTarget.getChildCount() == 0 && v != initialDropTarget)
                        v.setBackground(normalShape);
                    break;
                case DragEvent.ACTION_DROP:
                    if(dropTarget.getChildCount() > 0 && v != initialDropTarget) {
                        View view = (View) event.getLocalState();
                        view.setVisibility(View.VISIBLE);
                    } else {
                        View view = (View) event.getLocalState();
                        ViewGroup owner = (ViewGroup) view.getParent();
                        owner.removeView(view);
                        LinearLayout container = (LinearLayout) v;
                        container.addView(view);
                        view.setVisibility(View.VISIBLE);
                        if(view.getTag(R.string.member_tag) == v.getTag(R.string.member_tag)){
                            view.setOnTouchListener(null);
                            container.setBackground(rightShape);
                            engine.rightMatches++;
                            engine.totalRightMatches++;
                            if(engine.rightMatches == 6) {
                                engine.engineListener.onStageCompleted();
                            }
                        } else {
                            engine.wrongMatches++;
                            engine.totalWrongMatches++;
                        }
                    }
                    break;
                case DragEvent.ACTION_DRAG_ENDED:
                    if(dropTarget.getChildCount() == 0 && v != initialDropTarget)
                        v.setBackground(normalShape);
                default:
                    break;
            }
            return true;
        }

    }

    private static class LetterAssigningModeOnDragListener implements View.OnDragListener {

        private GameEngine engine;
        private GameActivity activity;

        private final Drawable enterShape;
        private final Drawable normalShape;
        private final Drawable rightShape;

        public LetterAssigningModeOnDragListener(GameEngine engine, GameActivity activity) {
            this.engine = engine;
            this.activity = activity;

            enterShape = ResourcesCompat.getDrawable(activity.getResources(), R.drawable.shape_droptarget, null);
            normalShape = ResourcesCompat.getDrawable(activity.getResources(), R.drawable.shape, null);
            rightShape = ResourcesCompat.getDrawable(activity.getResources(), R.drawable.shape_rightdroptarget, null);
        }

        @Override
        public boolean onDrag(View v, DragEvent event) {
            ViewGroup dropTarget = (ViewGroup)v;
            ViewGroup initialDropTarget = (ViewGroup)activity.findViewById(R.id.game_mode_one_initial_container);
            ViewGroup initialDropTargetChildOne = (ViewGroup) initialDropTarget.getChildAt(0);
            ViewGroup initialDropTargetChildTwo = (ViewGroup) initialDropTarget.getChildAt(1);
            switch (event.getAction()) {
                case DragEvent.ACTION_DRAG_STARTED:
                    //do nothing
                    break;
                case DragEvent.ACTION_DRAG_ENTERED:
                    if(dropTarget.getChildCount() == 0 && (v != initialDropTargetChildOne) && (v != initialDropTargetChildTwo))
                        v.setBackground(enterShape);
                    break;
                case DragEvent.ACTION_DRAG_EXITED:
                    if(dropTarget.getChildCount() == 0 && (v != initialDropTargetChildOne) && (v != initialDropTargetChildTwo))
                        v.setBackground(normalShape);
                    break;
                case DragEvent.ACTION_DROP:
                    if(dropTarget.getChildCount() > 0 && (v != initialDropTargetChildOne) && (v != initialDropTargetChildTwo)) {
                        View view = (View) event.getLocalState();
                        view.setVisibility(View.VISIBLE);
                    } else {
                        View view = (View) event.getLocalState();
                        ViewGroup owner = (ViewGroup) view.getParent();
                        owner.removeView(view);
                        ViewGroup container = (ViewGroup) v;
                        if(v == initialDropTargetChildOne && initialDropTargetChildOne.getChildCount() == 10){

                            initialDropTargetChildTwo.addView(view);

                        } else if(v == initialDropTargetChildTwo && initialDropTargetChildTwo.getChildCount() == 10){

                            initialDropTargetChildOne.addView(view);

                        } else{

                            container.addView(view);

                        }
                        view.setVisibility(View.VISIBLE);
                        if((v != initialDropTargetChildOne) && (v != initialDropTargetChildTwo)) {
                            if (view.getTag(R.string.letter_tag_key).toString().equals(
                                    v.getTag(R.string.letter_tag_key).toString())) {
                                view.setOnTouchListener(null);
                                container.setBackground(rightShape);
                                engine.rightMatches++;
                                engine.totalRightMatches++;
                            if (engine.rightMatches == Integer.parseInt(
                                    initialDropTarget.getTag(R.string.letter_count_tag_key).toString())) {
                                engine.engineListener.onStageCompleted();
                                engine.rightMatches = 0;
                            }
                            } else {
                                engine.wrongMatches++;
                                engine.totalWrongMatches++;
                            }
                        }
                    }
                    break;
                case DragEvent.ACTION_DRAG_ENDED:
                    if(dropTarget.getChildCount() == 0 && (v != initialDropTargetChildOne) && (v != initialDropTargetChildTwo))
                        v.setBackground(normalShape);
                    break;
                default:
                    break;
            }
            return true;
        }
    }

    private static class TouchListener implements View.OnTouchListener {
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if(motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                ClipData data = ClipData.newPlainText("", "");
                View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(view);
                view.startDrag(data, shadowBuilder, view, 0);
                view.setVisibility(View.INVISIBLE);
                return true;
            } else {
                return false;
            }
        }
    }


}
