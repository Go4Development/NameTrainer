package go4.szut.de.nametrainer.game;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayout;
import android.view.DragEvent;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

import go4.szut.de.nametrainer.R;
import go4.szut.de.nametrainer.database.Group;
import go4.szut.de.nametrainer.database.Member;
import go4.szut.de.nametrainer.main.MainActivity;
import go4.szut.de.nametrainer.util.CustomAlertDialog;
import go4.szut.de.nametrainer.util.Util;

/**
 * Created by Rene on 24.03.2016.
 */
public class GameActivity extends AppCompatActivity implements
        GameEngine.OnEngineListener {

    //layout id for game mode for assigning letters to blank spaces in order to build a persons name
    public static final int GAME_MODE_LETTER_ASSIGNING = R.layout.activity_game_mode1;
    public static final int GAME_MODE_LETTER_ASSIGNING_IDENTIFIER = 0;

    //layout id for game mode for assigning names to blank spaces in order to match a name to a picture
    public static final int GAME_MODE_NAME_ASSIGNING = R.layout.activity_game_mode2;
    public static final int GAME_MODE_NAME_ASSIGNING_IDENTIFIER = 1;

    //holds the game engine's instance that retrieves the necessary data from database in order to build up
    //the game mode that the user is going to play
    private GameEngine engine;

    private LinearLayout gameLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        engine = new GameEngine(this);
        //starts the engine in order to select a random game mode
        engine.start();

        gameLayout = (LinearLayout)findViewById(R.id.game_layout);
        gameLayout.setOnDragListener(new View.OnDragListener() {
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
    public void onLetterAssigningGameMode(Member member) {
        Util.D.l(this, "Engine has chosen : Letter Assigning Mode");
        setContentView(GAME_MODE_LETTER_ASSIGNING);
        char[] firstname = member.getFirstname().toUpperCase().toCharArray();
        char[] lastname = member.getSurname().toUpperCase().toCharArray();
        char[] scrambledChars = Util.Helper.scrambleChars(1, 20, member.getFirstname(), member.getSurname());

        LinearLayout firstnameContainer = (LinearLayout) findViewById(R.id.game_mode_one_firstname_container);
        LinearLayout lastnameContainer = (LinearLayout) findViewById(R.id.game_mode_one_lastname_container);
        GridLayout initialContainer = (GridLayout) findViewById(R.id.game_mode_one_initial_container);
        initialContainer.setTag(R.string.letter_count_tag_key, (firstname.length + lastname.length));
        initialContainer.setOnDragListener(engine.getLetterAssigningModeOnDragListener());
        ImageView imageView = (ImageView) findViewById(R.id.game_mode_one_imageview);
        ImageLoader.getInstance().displayImage(member.getImagePath(), imageView);

        for(int i = 0; i < scrambledChars.length; i++){
            int[] dpValues = Util.Dis.dp(this, 5, 40, 2);
            LayoutInflater layoutInflater =  ((LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE));
            TextView textView = (TextView) layoutInflater.inflate(R.layout.activity_game_mode1_draggable_letter, null);
            textView.setOnTouchListener(engine.getTouchListener());
            textView.setText(String.valueOf(scrambledChars[i]));
            textView.setTextColor(Color.argb(255, 255, 255, 255));
            textView.setTag(R.string.letter_tag_key, String.valueOf(scrambledChars[i]));
            textView.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.shape_letter, null));

            GridLayout.Spec row = GridLayout.spec(i / 10, 1);
            GridLayout.Spec colspan = GridLayout.spec(i % 10, 1);
            GridLayout.LayoutParams gridLayoutParam = new GridLayout.LayoutParams(row, colspan);
            gridLayoutParam.setMargins(dpValues[2],dpValues[2],dpValues[2],dpValues[2]);

            if(initialContainer != null)
                initialContainer.addView(textView,gridLayoutParam);

            LinearLayout dropTarget = (LinearLayout) layoutInflater.inflate(R.layout.activity_game_mode1_droptarget, null);
            dropTarget.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.shape, null));
            dropTarget.setOnDragListener(engine.getLetterAssigningModeOnDragListener());
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(dpValues[1],dpValues[1]);
            layoutParams.gravity = Gravity.CENTER;
            layoutParams.setMargins(dpValues[0],dpValues[0], 0, 0);

            if(i < firstname.length + lastname.length) {
                if(i < firstname.length) {
                    if(firstnameContainer != null) {
                        firstnameContainer.addView(dropTarget, layoutParams);
                        dropTarget.setTag(R.string.letter_tag_key, firstname[i]);
                    }
                } else {
                    if(lastnameContainer != null) {
                        lastnameContainer.addView(dropTarget, layoutParams);
                        dropTarget.setTag(R.string.letter_tag_key, lastname[i - firstname.length]);
                    }
                }
            }
        }
    }


    @Override
    public void onNameAssigningGameMode(ArrayList<Member> members) {
        Util.D.l(this, "Engine has chosen : Name Assigning Mode");
        setContentView(GAME_MODE_NAME_ASSIGNING);
        
        //retrieves layouts from xml layout definition
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.initial_droptarget);
        GridLayout targetContainer = (GridLayout) findViewById(R.id.game_gridlayout);

        //sets the DragListener to the linear layout
        if(linearLayout != null)
            linearLayout.setOnDragListener(engine.getNameAssigningModeOnDragListener());

        for (int i = 0; i < linearLayout.getChildCount(); i++){
            Member member = members.get(i);
            ImageView image = (ImageView) linearLayout.getChildAt(i);
            ImageLoader.getInstance().displayImage(member.getImagePath(), image);
            image.setOnTouchListener(engine.getTouchListener());
            image.setTag(R.string.member_tag, member.getId());
            LinearLayout dropTarget = (LinearLayout) targetContainer.getChildAt(i);
            dropTarget.getChildAt(0).setOnDragListener(engine.getNameAssigningModeOnDragListener());
            dropTarget.getChildAt(0).setTag(R.string.member_tag, member.getId());
            TextView name = (TextView) dropTarget.getChildAt(1);
            name.setText(member.getFullName());
        }
        
    }

    @Override
    public void onComplete(Group group, int rightMatches, int wrongMatches) {
        Util.D.l(this, "onComplete");
        Intent gameResultActivityIntent = new Intent(this, GameResultActivity.class);
        gameResultActivityIntent.putExtra(GameResultActivity.PLAY_AGAIN_DATA, group);
        gameResultActivityIntent.putExtra(GameResultActivity.RIGHT_MATCHES_VALUE, rightMatches);
        gameResultActivityIntent.putExtra(GameResultActivity.WRONG_MATCHES_VALUE, wrongMatches);
        startActivityForResult(gameResultActivityIntent, GameResultActivity.IDENTIFIER);
    }

    @Override
    public void onStageCompleted() {
        CustomAlertDialog dialog = new CustomAlertDialog(this);
        dialog.setTitle("Stage Completed");
        dialog.setPositiveButtonTitle("Weiter");
        dialog.setOptionSelectionListener(new CustomAlertDialog.SimpleOnOptionSelectionListener() {
            @Override
            public void onPositive(CustomAlertDialog.Interface i) {
                engine.next();
                i.close(null);
            }

            @Override
            public void onNegative(CustomAlertDialog.Interface i) {

            }
        });
        dialog.show();
    }

}
