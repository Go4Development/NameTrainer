package go4.szut.de.nametrainer.util;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v4.util.ArrayMap;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.Set;

import go4.szut.de.nametrainer.database.DataSource;

/**
 * Created by Rene on 01.04.2016.
 */
public class CustomAlertDialog implements DialogInterface.OnClickListener, View.OnClickListener {

    /**
     * constant for no selection
     */
    public static final int NO_SELECTION = Integer.MAX_VALUE;

    /**
     * holds the context of the activity
     */
    private Context context;

    /**
     * holds the alert dialog builder
     */
    private AlertDialog.Builder dialogBuilder;

    /**
     * holds the view of the dialog
     */
    private View dialogView;

    /**
     * holds the onOptionSelectionListener that is the callback for events
     */
    private OnOptionSelectionListener optionSelectionListener;

    /**
     * holds the onUpdateListener that is the callback for an update event
     */
    private OnUpdateListener updateListener;
    private int updateIdentifier;

    /**
     * holds the title for the positive button of the alert dialog
     */
    private String positiveButtonTitle;

    /**
     * holds the title for the negative button of the alert dialog
     */
    private String negativeButtonTitle;

    /**
     * holds views matched to keys, its like a Java HashMap
     */
    private SparseArray<View> views;

    /**
     * is a place holder for an object that might be important for callbacks
     */
    private Object callback;

    /**
     * is a place holder for an adapter that might be important for lists, etc.
     */
    private Object adapter;

    /**
     * is a place holder for a certain value to store in dialog in order to pass
     * it to the listener while an event
     */
    private Object value;

    /**
     * holds a data source instance that has access to database
     */
    private DataSource source;

    /**
     * Takes the context of an activity.
     * @param context - the context
     */
    public CustomAlertDialog(Context context) {
        this.context = context;
        dialogBuilder = new AlertDialog.Builder(context);
        views = new SparseArray<View>();
        source = DataSource.getDataSourceInstance(context);
    }

    /**
     * Sets the title of the positive button.
     * @param positiveButtonTitle - title of the button
     */
    public void setPositiveButtonTitle(String positiveButtonTitle) {
        this.positiveButtonTitle = positiveButtonTitle;
    }

    /**
     * Sets the title of the positive button.
     * @param positiveButtonTitleId - id that points to a resource string
     */
    public void setPositiveButtonTitle(int positiveButtonTitleId) {
        this.positiveButtonTitle = context.getResources().getString(positiveButtonTitleId);
    }

    /**
     * Sets the title of the negative button.
     * @param negativeButtonTitle - the title of the button
     */
    public void setNegativeButtonTitle(String negativeButtonTitle) {
        this.negativeButtonTitle = negativeButtonTitle;
    }

    /**
     * Sets the title of the negative button.
     * @param negativeButtonTitleId - id that points to a resource string
     */
    public void setNegativeButtonTitle(int negativeButtonTitleId) {
        this.negativeButtonTitle = context.getResources().getString(negativeButtonTitleId);
    }

    /**
     * Sets the title of the dialog.
     * @param title - the title of the dialog
     */
    public void setTitle(String title) {
        dialogBuilder.setTitle(title);
    }

    /**
     * Sets the dialog view to the passed one.
     * @param dialogView - the dialog view
     */
    public void setDialogView(View dialogView) {
        this.dialogView = dialogView;
    }

    /**
     * Sets the dialog view to the defined one of the passed resources id.
     * @param dialogViewId - the resource id for dialog view layout
     */
    public void setDialogView(int dialogViewId) {
        this.dialogView = ((LayoutInflater)context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(dialogViewId, null);
    }

    /**
     * Finds a view by id that is a child of the dialog view.
     * @param id - the id of the view
     * @return the view instance that belongs to the passed id, otherwise the result is null
     */
    public View findViewById(int id) {
        if(dialogView != null) {
            return dialogView.findViewById(id);
        }
        return null;
    }

    /**
     * Casts a view to the given type that is found by the passed view.
     * @param type - the class object of the class that the view should be casted in
     * @param id - the id that belongs to the searched view
     * @param <T> - its the generic type of the class that type is an instance of
     * @return the casted view, otherwise the result is null
     */
    public <T> T getView(Class<T> type, int id) {
        View view = views.get(id);
        if(view != null) {
            return type.cast(view);
        }
        return null;
    }

    /**
     * Adds a view to the list.
     * @param viewId - the id of the view that is a child of the dialog view
     * @return the found view by passed id, otherwise the result is null
     */
    public View addView(int viewId) {
        View view = findViewById(viewId);
        if(view != null) {
            views.put(viewId, view);
            return view;
        }
        return null;
    }

    /**
     * Adds a view to the list.
     * @param viewId - the id that the passed view should be found with
     * @param view - the view
     * @return the view that is stored to the list
     */
    public View addView(int viewId, View view) {
        if(view != null) {
            views.put(viewId, view);
        }
        return view;
    }

    /**
     * Adds a view to the list and sets an onClick listener to
     * the passed view.
     * @param viewId - the id that the passed view should be found with
     * @param view - the view
     */
    public void addViewIncludingOnClick(int viewId, View view) {
        if(view != null) {
            view.setOnClickListener(this);
            views.put(viewId, view);
        }
    }

    /**
     * Adds an onClick listener to a view that is already stored
     * in the list.
     * @param viewId - the id that belongs to the view for onClick listener
     */
    public void addViewIncludingOnClick(int viewId) {
        View view = addView(viewId);
        if(view != null) {
            view.setOnClickListener(this);
        }
    }

    /**
     * Sets the onOptionSelectionListener for this custom dialog.
     * @param optionSelectionListener - the listener
     */
    public void setOptionSelectionListener(OnOptionSelectionListener optionSelectionListener) {
        this.optionSelectionListener = optionSelectionListener;
    }

    /**
     * Sets the onUpdateListener for this custom dialog.
     * @param updateListener - the listener
     */
    public void setUpdateListener(int updateIdentifier, OnUpdateListener updateListener) {
        this.updateListener = updateListener;
        this.updateIdentifier = updateIdentifier;
    }

    /**
     * Sets the place holder value to the passed one.
     * @param value - a value
     */
    public void setValue(Object value) {
        this.value = value;
    }

    /**
     * Sets the place holder adapter to the passed one.
     * @param adapter - an adapter
     */
    public void setAdapter(Object adapter) {
        this.adapter = adapter;
    }

    /**
     * Sets the place holder callback to the passed one.
     * @param callback - a callback
     */
    public void setCallback(Object callback) {
        this.callback = callback;
    }

    /**
     * Sets an array adapter to the dialog, if a collection of options in a list is needed.
     * @param selectionLayoutId - the layout id of the dialog
     * @param selectionStringArrayId - the id to a resource array
     * @return the created array adapter
     */
    public ArrayAdapter<String> setArrayAdapter(int selectionLayoutId, int selectionStringArrayId) {
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(context, selectionLayoutId,
                context.getResources().getStringArray(selectionStringArrayId));
        dialogBuilder.setAdapter(arrayAdapter, this);
        return arrayAdapter;
    }

    private void reopenDialog(ArrayMap<Integer, String> bundle) {
        Set<Integer> keySet = bundle.keySet();
        for(Integer key : keySet) {
            EditText editText = getView(EditText.class, key);
            if(editText != null) {
                String value = bundle.get(key);
                editText.setText(value);
            }
        }
        show();
    }

    /**
     * Shows the dialog.
     */
    public void show() {
        if(dialogView != null) {
            ViewGroup viewGroup = (ViewGroup)dialogView.getParent();
            if(viewGroup != null) {
                viewGroup.removeView(dialogView);
            }
            dialogBuilder.setView(dialogView);
        }
        dialogBuilder.setCancelable(true);
        if(positiveButtonTitle != null) {
            dialogBuilder.setPositiveButton(positiveButtonTitle, this);
        }
        if(negativeButtonTitle != null) {
            dialogBuilder.setNegativeButton(negativeButtonTitle, this);
        }
        dialogBuilder.show();
    }


    /**
     * The onClick method of the standard alert dialog that will be called
     * if an event is trigged by the user.
     * @param dialog - the dialog interface
     * @param which - indicator which options is selected
     */
    @Override
    public void onClick(final DialogInterface dialog, final int which) {
        /*
            Creation of the own interface that is passed to the callbacks
            of the own listener. Values that should be available for access to
            some other classes get wrapped in methods for return.
         */
        Interface anInterface = new Interface() {

            @Override
            public <T extends View> T getView(Class<T> type, int viewId) {
                return type.cast(views.get(viewId));
            }

            @Override
            public View getViewAt(int index) {
                return views.get(index);
            }

            @Override
            public View getClickedView() {
                return null;
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
            public <T> T getAdapter(Class<T> type) {
                return type.cast(adapter);
            }

            @Override
            public Object getValue() {
                return value;
            }

            @Override
            public boolean hasValue() {
                return value != null;
            }

            @Override
            public Object getCallback() {
                return callback;
            }

            @Override
            public boolean hasCallback() {
                return callback != null;
            }

            @Override
            public void close(Object data) {
                dialog.dismiss();
                if(updateListener != null) {
                    updateListener.update(updateIdentifier, data);
                }
            }

            @Override
            public Delayed reopen(ArrayMap<Integer, String> bundle) {
                return new Delayed(bundle);
            }

            public int getSelection() {
                return which;
            }

            public DataSource getDataSource() {
                return source;
            }

        };

        /*
          Triggers the different methods of the own listener. The indicator
          which decides which option should be triggered and passes the
          interface for access to values of this custom dialog instance.
         */
        switch (which) {
            case DialogInterface.BUTTON_NEGATIVE:
                if (optionSelectionListener != null) {
                    optionSelectionListener.onNegativeSelection(anInterface);
                }
                break;
            case DialogInterface.BUTTON_POSITIVE:
                if (optionSelectionListener != null) {
                    optionSelectionListener.onPositiveSelection(anInterface);
                }
                break;
            case DialogInterface.BUTTON_NEUTRAL:
                if (optionSelectionListener != null) {
                    optionSelectionListener.onNeutralSelection(anInterface);
                }
                break;
        }

        /*
         * Is triggered at anytime to ensure a callback if no either of
         * the previous methods was called.
         */
        if (optionSelectionListener != null) {
            optionSelectionListener.onDefaultSelection(anInterface);
        }
    }

    /**
     * The onClick method of an onClick listener for a view.
     * @param v - the source view where the onClick event is coming from
     */
    @Override
    public void onClick(final View v) {

         /*
            Creation of the own interface that is passed to the callbacks
            of the own listener. Values that should be available for access to
            some other classes get wrapped in methods for return.
         */
            Interface anInterface = new Interface() {

                @Override
                public <T extends View> T getView(Class<T> type, int viewId) {
                    return type.cast(views.get(viewId));
                }

                @Override
                public View getViewAt(int index) {
                    return views.get(index);
                }

                @Override
                public View getClickedView() {
                    return v;
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
                public <T> T getAdapter(Class<T> type) {
                    return type.cast(adapter);
                }

                @Override
                public Object getValue() {
                    return value;
                }

                @Override
                public boolean hasValue() {
                    return value != null;
                }

                @Override
                public Object getCallback() {
                    return callback;
                }

                @Override
                public boolean hasCallback() {
                    return callback != null;
                }

                @Override
                public void close(Object data) {

                }

                @Override
                public Delayed reopen(ArrayMap<Integer, String> bundle) {
                    return new Delayed(bundle);
                }

                public int getSelection() {
                    return NO_SELECTION;
                }

                public DataSource getDataSource() {
                    return source;
                }

            };

        /*
            Is triggered for own callback in the onOptionSelectedListener
            when an onClick event of a certain view occurs.
         */
            if (optionSelectionListener != null) {
                optionSelectionListener.onViewOnClick(anInterface);
            }
    }



    /**
     * Definition of the onOptionSelectionListener that is used for
     * callback of this custom dialog instance.
     */
    public interface OnOptionSelectionListener {
        public void onPositiveSelection(Interface i);
        public void onNegativeSelection(Interface i);
        public void onNeutralSelection(Interface i);
        public void onDefaultSelection(Interface i);
        public void onViewOnClick(Interface i);
    }

    /**
     * Definition of the Interface that handles the access to
     * the values of this custom dialog instance.
     */
    public interface Interface {
        public <T extends View> T getView(Class<T> type, int viewId);
        public View getViewAt(int index);
        public int getViewCount();
        public Object getAdapter();
        public <T> T getAdapter(Class<T> type);
        public Object getValue();
        public boolean hasValue();
        public Object getCallback();
        public boolean hasCallback();
        public Delayed reopen(ArrayMap<Integer, String> bundle);
        public void close(Object data);
        public int getSelection();
        public DataSource getDataSource();
        public View getClickedView();
    }


    public class Delayed implements Runnable {

        private ArrayMap<Integer, String> bundle;

        public Delayed(ArrayMap<Integer, String> bundle) {
            this.bundle = bundle;
        }

        @Override
        public void run() {
            reopenDialog(bundle);
        }
    }

    public interface OnUpdateListener {

        public void update(int updateIdentifier, Object data);

    }

    /**
     * Simple Implementation of onOptionSelectionListener
     */
    public static abstract class AdvancedSimpleOnOptionSelectionListener
            implements OnOptionSelectionListener {

        public abstract void onPositive(Interface i);
        public abstract void onNegative(Interface i);
        public abstract void onClick(Interface i);

        @Override
        public void onPositiveSelection(Interface i) {
            onPositive(i);
        }

        @Override
        public void onNegativeSelection(Interface i) {
            onNegative(i);
        }

        @Override
        public void onDefaultSelection(Interface i) {

        }

        @Override
        public void onNeutralSelection(Interface i) {

        }

        @Override
        public void onViewOnClick(Interface i) {
            onClick(i);
        }
    }

    public static abstract class SimpleOnOptionSelectionListener
        implements OnOptionSelectionListener {

        public abstract void onPositive(Interface i);
        public abstract void onNegative(Interface i);

        @Override
        public void onPositiveSelection(Interface i) {
            onPositive(i);
        }

        @Override
        public void onNegativeSelection(Interface i) {
            onNegative(i);
        }

        @Override
        public void onNeutralSelection(Interface i) {

        }

        @Override
        public void onDefaultSelection(Interface i) {

        }

        @Override
        public void onViewOnClick(Interface i) {

        }
    }


    public static abstract class DefaultSelectionListener
        implements OnOptionSelectionListener {

        public abstract void onDefault(Interface i);

        @Override
        public void onPositiveSelection(Interface i) {

        }

        @Override
        public void onNegativeSelection(Interface i) {

        }

        @Override
        public void onNeutralSelection(Interface i) {

        }

        @Override
        public void onDefaultSelection(Interface i) {
            onDefault(i);
        }

        @Override
        public void onViewOnClick(Interface i) {

        }
    }

}
