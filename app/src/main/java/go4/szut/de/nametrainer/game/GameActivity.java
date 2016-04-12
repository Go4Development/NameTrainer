package go4.szut.de.nametrainer.game;

import android.content.ClipData;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import go4.szut.de.nametrainer.R;

/**
 * Created by Rene on 24.03.2016.
 */
public class GameActivity extends AppCompatActivity implements View.OnTouchListener,
    View.OnDragListener {

    //layout id for game mode for assigning letters to blank spaces in order to build a persons name
    public static final int GAME_MODE_LETTER_ASSIGNING = R.layout.activity_game_mode1;
    //layout id for game mode for assigning names to blank spaces in order to match a name to a picture
    public static final int GAME_MODE_NAME_ASSIGNING = R.layout.activity_game_mode2;

    //setup builder decides which game mode the player is going to play and builds up the corresponding views
    private GameSetupBuilder setupBuilder;

    //shapes for the blank spaces where the draggable object should be put in
    private Drawable normalBlankShape;
    private Drawable enterBlankShape;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupBuilder = new GameSetupBuilder(this);
    }

    @Override
    public boolean onDrag(View v, DragEvent event) {
        int action = event.getAction();
        switch (action) {
            case DragEvent.ACTION_DRAG_STARTED:
                break;
             case DragEvent.ACTION_DRAG_ENTERED:
                v.setBackground(normalBlankShape);
                break;
            case DragEvent.ACTION_DRAG_EXITED:
                v.setBackground(enterBlankShape);
                break;
            case DragEvent.ACTION_DROP:
                View view = (View) event.getLocalState();
                ViewGroup owner = (ViewGroup) view.getParent();
                owner.removeView(view);
                LinearLayout container = (LinearLayout) v;
                container.addView(view);
                view.setVisibility(View.VISIBLE);
                break;
            case DragEvent.ACTION_DRAG_ENDED:
                v.setBackground(normalBlankShape);
            default:
                break;
        }
        return true;
    }


    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if(event.getAction() == MotionEvent.ACTION_DOWN) {
            ClipData data = ClipData.newPlainText("", "");
            View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(v);
            v.startDrag(data, shadowBuilder, v, 0);
            v.setVisibility(View.VISIBLE);
            return true;
        } else {
            return false;
        }
    }


}
