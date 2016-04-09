package go4.szut.de.nametrainer.groupeditor;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;

import java.util.ArrayList;

/**
 * Created by Rene on 01.04.2016.
 */
public class CustomAlertDialog implements DialogInterface.OnClickListener {

    private Context context;

    private AlertDialog.Builder dialogBuilder;
    private View dialogView;

    private OnOptionSelectionListener optionSelectionListener;

    private String positiveButtonTitle;
    private String negativeButtonTitle;

    private ArrayList<View> views;
    private Object adapter;
    private Object value;

    public CustomAlertDialog(Context context) {
        this.context = context;
        dialogBuilder = new AlertDialog.Builder(context);
        views = new ArrayList<View>();
    }

    public void setPositiveButtonTitle(String positiveButtonTitle) {
        this.positiveButtonTitle = positiveButtonTitle;
    }

    public void setPositiveButtonTitle(int positiveButtonTitleId) {
        this.positiveButtonTitle = context.getResources().getString(positiveButtonTitleId);
    }

    public void setNegativeButtonTitle(String negativeButtonTitle) {
        this.negativeButtonTitle = negativeButtonTitle;
    }

    public void setNegativeButtonTitle(int negativeButtonTitleId) {
        this.negativeButtonTitle = context.getResources().getString(negativeButtonTitleId);
    }

    public void setDialogView(View dialogView) {
        this.dialogView = dialogView;
    }

    public void setDialogView(int dialogViewId) {
        this.dialogView = ((LayoutInflater)context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(dialogViewId, null);
    }

    public Object findViewById(int id) {
        if(dialogView != null) {
            return dialogView.findViewById(id);
        }
        return null;
    }

    public void addView(View view) {
        views.add(view);
    }

    public void setOptionSelectionListener(OnOptionSelectionListener optionSelectionListener) {
        this.optionSelectionListener = optionSelectionListener;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public void setAdapter(Object adapter) {
        this.adapter = adapter;
    }

    public void show() {
        dialogBuilder.setView(dialogView);
        dialogBuilder.setCancelable(true);
        dialogBuilder.setPositiveButton(positiveButtonTitle, this);
        dialogBuilder.setNegativeButton(negativeButtonTitle, this);
        dialogBuilder.show();
    }


    @Override
    public void onClick(final DialogInterface dialog, final int which) {
        CustomDialogInterface customDialogInterface = new CustomDialogInterface() {

            @Override
            public ArrayList<View> getViews() {
                return views;
            }

            @Override
            public View getViewAt(int index) {
                return views.get(index);
            }

            @Override
            public int getViewCount() {
                return views.size();
            }

            @Override
            public Object getAdapter() {
                return adapter;
            }

            @Override
            public Object getValue() {
                return value;
            }

            @Override
            public void close() {
                dialog.dismiss();
            }

            public int getSelection() {
                return which;
            }

        };

        switch(which) {
            case DialogInterface.BUTTON_NEGATIVE:
                if(optionSelectionListener != null) {
                    optionSelectionListener.onNegativeSelection(customDialogInterface);
                }
                break;
            case DialogInterface.BUTTON_POSITIVE:
                if(optionSelectionListener != null) {
                    optionSelectionListener.onPositiveSelection(customDialogInterface);
                }
                break;
            case DialogInterface.BUTTON_NEUTRAL:
                if(optionSelectionListener != null) {
                    optionSelectionListener.onNeutralSelection(customDialogInterface);
                }
                break;
        }

        if(optionSelectionListener != null) {
            optionSelectionListener.onDefaultSelection(customDialogInterface);
        }
    }

    public interface OnOptionSelectionListener {
        public void onPositiveSelection(CustomDialogInterface customDialogInterface);
        public void onNegativeSelection(CustomDialogInterface customDialogInterface);
        public void onNeutralSelection(CustomDialogInterface customDialogInterface);
        public void onDefaultSelection(CustomDialogInterface customDialogInterface);
    }

    public interface CustomDialogInterface {
        public ArrayList<View> getViews();
        public View getViewAt(int index);
        public int getViewCount();
        public Object getAdapter();
        public Object getValue();
        public void close();
        public int getSelection();
    }

    /**
     * Created by Rene on 03.04.2016.
     */
    public abstract static class CustomDialogOnClickListener implements DialogInterface.OnClickListener {

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
}
