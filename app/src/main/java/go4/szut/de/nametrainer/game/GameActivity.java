package go4.szut.de.nametrainer.game;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;

import go4.szut.de.nametrainer.R;
import go4.szut.de.nametrainer.database.Group;
import go4.szut.de.nametrainer.database.Member;
import go4.szut.de.nametrainer.main.MainActivity;
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

    //holds the game engine's instance that retrieves the necessary data from database in order to build up
    //the game mode that the user is going to play
    private GameEngine engine;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        engine = new GameEngine(this);
        //starts the engine in order to select a random game mode
        engine.start();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK) {
            //if the result is coming from GameResultActivity and has data in order
            //to play the same selected group again, this method will restart the game
            //activity with the already selected group
            if(requestCode == GameResultActivity.IDENTIFIER) {
                if(data != null && data.hasExtra(GameResultActivity.PLAY_AGAIN_DATA)) {
                    Group group = data.getParcelableExtra(GameResultActivity.PLAY_AGAIN_DATA);
                    Intent gameActivityIntent = new Intent(this, GameActivity.class);
                    gameActivityIntent.putExtra(GameEngine.GAME_GROUP_OBJECT, group);
                    startActivity(gameActivityIntent);
                } else {
                    // may call: finish(); to stop the current running activity
                    Intent mainActivityIntent = new Intent(this, MainActivity.class);
                    startActivity(mainActivityIntent);
                }
            }
        }
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

        //retrieves layouts from xml layout definition
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.initial_droptarget);
        GridLayout targetContainer = (GridLayout) findViewById(R.id.game_gridlayout);

        //sets the DragListener to the linear layout
        linearLayout.setOnDragListener(new DropTargetListener());

        for (int i = 0; i < linearLayout.getChildCount(); i++){
            Member member = members.get(i);
            ImageView image = (ImageView) linearLayout.getChildAt(i);
            image.setOnTouchListener(new ImageTouchListener());
            image.setTag(R.string.member_tag,member.getId());
            LinearLayout dropTarget = (LinearLayout) targetContainer.getChildAt(i);
            dropTarget.getChildAt(0).setOnDragListener(new DropTargetListener());
            dropTarget.getChildAt(0).setTag(R.string.member_tag,member.getId());
            TextView name = (TextView) dropTarget.getChildAt(1);
            name.setText(member.getFullName());
        }

        findViewById(R.id.game_layout).setOnDragListener(new OnDragListener() {
            @Override
            public boolean onDrag(View v, DragEvent event) {
                if(event.getAction() == DragEvent.ACTION_DROP){
                    View view = (View) event.getLocalState();
                    view.setVisibility(View.VISIBLE);
                }
                return true;
            }
        });

    }

    @Override
    public void onComplete(Group group) {
        Util.D.l(this, "onComplete");
        Intent gameResultActivityIntent = new Intent(this, GameResultActivity.class);
        gameResultActivityIntent.putExtra(GameResultActivity.PLAY_AGAIN_DATA, group);
        startActivityForResult(gameResultActivityIntent, GameResultActivity.IDENTIFIER);
    }

    class DropTargetListener implements OnDragListener {
        Drawable enterShape = ResourcesCompat.getDrawable(getResources(), R.drawable.shape_droptarget, null);
        Drawable normalShape =  ResourcesCompat.getDrawable(getResources(), R.drawable.shape, null);
        Drawable rightShape = ResourcesCompat.getDrawable(getResources(), R.drawable.shape_rightdroptarget,null);
        Drawable wrongShape = ResourcesCompat.getDrawable(getResources(), R.drawable.shape_wrongtarget,null);

        @Override
        public boolean onDrag(View v, DragEvent event) {
            ViewGroup dropTarget = (ViewGroup) v;
            switch (event.getAction()) {
                case DragEvent.ACTION_DRAG_STARTED:
                    // do nothing
                    break;
                case DragEvent.ACTION_DRAG_ENTERED:
                    if(dropTarget.getChildCount() == 0)
                        v.setBackground(enterShape);
                    break;
                case DragEvent.ACTION_DRAG_EXITED:
                    if(dropTarget.getChildCount() == 0)
                        v.setBackground(normalShape);
                    break;
                case DragEvent.ACTION_DROP:
                    if(( (ViewGroup) v).getChildCount() > 0 && v != findViewById(R.id.initial_droptarget)) {
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
                            v.setBackground(rightShape);
                        }else {
                            v.setBackground(wrongShape);
                        }
                    }
                    break;
                case DragEvent.ACTION_DRAG_ENDED:
                    if(dropTarget.getChildCount() == 0)
                        v.setBackground(normalShape);
                default:
                    break;
            }
            return true;
        }
    }

}