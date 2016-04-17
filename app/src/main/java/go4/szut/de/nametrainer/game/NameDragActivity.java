package go4.szut.de.nametrainer.game;

/**
 * Created by ramazan on 17.04.16.
 */

import android.app.Activity;
import android.content.ClipData;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.GridLayout;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.DragShadowBuilder;
import android.view.View.OnDragListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import go4.szut.de.nametrainer.R;
import go4.szut.de.nametrainer.database.Member;
//needs an ArrayList<Members> in
// Intent(setParcelableArrayListExtra() method
// and the PARCELABLE_KEY as key)
//atleast 6 Member are acquired
//still Work in progress
public class NameDragActivity extends Activity {

    public static String PARCELABLE_KEY = "members";

    ArrayList<Member> members;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drag);

        members = getIntent().getParcelableArrayListExtra(PARCELABLE_KEY);

        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.initial_droptarget);
        GridLayout targetContainer = (GridLayout) findViewById(R.id.game_gridlayout);

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

    private final class ImageTouchListener implements OnTouchListener {
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                ClipData data = ClipData.newPlainText("", "");
                DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(view);
                view.startDrag(data, shadowBuilder, view, 0);
                view.setVisibility(View.INVISIBLE);
                return true;
            } else {
                return false;
            }
        }
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
