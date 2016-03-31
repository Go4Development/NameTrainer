package go4.szut.de.nametrainer.groupeditor;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Toast;

import go4.szut.de.nametrainer.R;
import go4.szut.de.nametrainer.database.DataSource;
import go4.szut.de.nametrainer.database.Member;

/**
 * Created by Rene on 31.03.2016.
 */
public class HorizontalScrollViewItemListener implements View.OnLongClickListener {

    //option identifier for editing a member
    private static final int DIALOG_OPTION_EDIT = 0;
    //option identifier for deleting a member
    private static final int DIALOG_OPTION_DELETE = 1;

    private Context context;
    private ArrayAdapter<String> horizontalScrollViewItemDialogAdapter;

    private HorizontalScrollViewAdapter adapter;

    private DataSource source;

    public HorizontalScrollViewItemListener(Context context, HorizontalScrollViewAdapter adapter) {
        this.context = context;
        this.adapter = adapter;

        horizontalScrollViewItemDialogAdapter = new ArrayAdapter<String>(context,
                android.R.layout.select_dialog_item,
                context.getResources().getStringArray(R.array.groupeditor_item_dialog_options));

        source = DataSource.getDataSourceInstance(context);
    }

    @Override
    public boolean onLongClick(View v) {
        HorizontalScrollViewItem item = (HorizontalScrollViewItem)v;
        final Member member = item.getMember();

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(member.getFullName());
        builder.setCancelable(true);
        builder.setAdapter(horizontalScrollViewItemDialogAdapter, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DIALOG_OPTION_EDIT:
                        onEdit(member);
                        break;
                    case DIALOG_OPTION_DELETE:
                        onDelete(member);
                        break;
                }
            }
        });
        builder.show();
        return false;
    }

    private void onEdit(final Member member) {

        DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch(which) {
                    case DialogInterface.BUTTON_NEGATIVE:
                        Toast.makeText(context, "Button Negative", Toast.LENGTH_LONG).show();
                        break;
                    case DialogInterface.BUTTON_POSITIVE:
                        Toast.makeText(context, "Button Positive Edit", Toast.LENGTH_LONG).show();
                        break;
                }
            }
        };


        AlertDialog.Builder builder = new AlertDialog.Builder(context)
                .setCancelable(true)
                .setPositiveButton(context.getResources().getString(R.string.groupeditor_add_action_posbutton), listener)
                .setNegativeButton(context.getResources().getString(R.string.groupeditor_add_action_negbutton), listener)
                .setView(R.layout.activity_groupeditor_portraititem_dialog);

        AlertDialog dialog = builder.show();

        EditText firstnameEditText = (EditText)dialog.findViewById(R.id.dialog_firstname);
        firstnameEditText.setText(member.getFirstname());

        EditText surnameEditText = (EditText)dialog.findViewById(R.id.dialog_surname);
        surnameEditText.setText(member.getSurname());

        //here image f

    }

    private void onDelete(Member member) {
        source.open();
        source.deleteMember(member);
        source.close();
        adapter.update();
    }

}
