package go4.szut.de.nametrainer.groupeditor;

import android.content.DialogInterface;

/**
 * Created by Rene on 03.04.2016.
 */
public abstract class CustomDialogOnClickListener implements DialogInterface.OnClickListener {

    public Object object;

    public CustomDialogOnClickListener(Object object) {
        this.object = object;
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        this.onClick(dialog, which, object);
    }

    public abstract void onClick(DialogInterface dialog, int which, Object object);

}
