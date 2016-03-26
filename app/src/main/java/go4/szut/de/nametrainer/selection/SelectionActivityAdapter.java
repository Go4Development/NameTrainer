package go4.szut.de.nametrainer.selection;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import go4.szut.de.nametrainer.R;
import go4.szut.de.nametrainer.util.Util;

/**
 * Created by Michelé on 26.03.2016.
 */
public class SelectionActivityAdapter extends BaseAdapter{

    private LayoutInflater inflater;
    private ArrayList<String> groupsToSelect;
    private SelectionActivity selectionActivity;

    public SelectionActivityAdapter(SelectionActivity context){
        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        groupsToSelect = Util.createArrayListWithNonSense(22);
        selectionActivity = context;
    }

    @Override
    public int getCount() {
        return groupsToSelect.size();
    }

    @Override
    public Object getItem(int position) {
        return groupsToSelect.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null) {
            convertView = inflater.inflate(R.layout.activity_selection_textview, parent, false);
        }

        TextView textView = (TextView) convertView;
        textView.setText(groupsToSelect.get(position));
        textView.setOnClickListener(selectionActivity);
        return convertView;
    }
}
