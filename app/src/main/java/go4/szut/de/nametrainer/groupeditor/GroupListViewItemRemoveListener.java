package go4.szut.de.nametrainer.groupeditor;

import android.content.Context;
import android.view.View;

/**
 * Created by Rene on 31.03.2016.
 */
public class GroupListViewItemRemoveListener implements View.OnLongClickListener {

    private Context context;

    public GroupListViewItemRemoveListener(Context context) {
        this.context = context;
    }

    @Override
    public boolean onLongClick(View v) {
        //TODO Hier AlertDialog zum Bearbeiten und Löschen bauen
        return false;
    }
}
