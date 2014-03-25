package lbs.erasmus.touristanbul;

/**
 * Created by patmonsi on 24/03/14.
 */

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckedTextView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

@SuppressWarnings("unchecked")
public class ParentLevelAdapter extends BaseExpandableListAdapter {

    public ArrayList<String> groupItem, tempChild;
    public ArrayList<Object> Childtem = new ArrayList<Object>();

    public LayoutInflater minflater;
    public Activity activity;

    public ParentLevelAdapter(ArrayList<String> grList, ArrayList<Object> childItem) {
        groupItem = grList;
        this.Childtem = childItem;
    }

    public void setInflater(LayoutInflater mInflater, Activity act) {
        this.minflater = mInflater;
        activity = act;
    }

    public Object getChild(int groupPosition, int childPosition) {
        return null;
    }

    public long getChildId(int groupPosition, int childPosition) {
        return 0;
    }

    public View getChildView(int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        tempChild = (ArrayList<String>) Childtem.get(groupPosition);
        TextView text = null;
        if (convertView == null) {
            convertView = minflater.inflate(R.layout.childrow, null);
        }
        text = (TextView) convertView.findViewById(R.id.textView1);
        text.setText(tempChild.get(childPosition));
        convertView.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                Toast.makeText(activity, tempChild.get(childPosition), Toast.LENGTH_SHORT).show();
            }
        });
        // Change the icon Do\'s and Don\'ts
        ImageView imageView = (ImageView) convertView.findViewById(R.id.childImage);
        String s = tempChild.get(childPosition);
        if (s.startsWith("Do ")) {
            imageView.setImageResource(R.drawable.yes);
        } else {
            imageView.setImageResource(R.drawable.no);
        }
        return convertView;
    }

    public int getChildrenCount(int groupPosition) {
        return ((ArrayList<String>) Childtem.get(groupPosition)).size();
    }

    public Object getGroup(int groupPosition) {
        return null;
    }

    public int getGroupCount() {
        return groupItem.size();
    }

    @Override
    public void onGroupCollapsed(int groupPosition) {
        super.onGroupCollapsed(groupPosition);
    }

    @Override
    public void onGroupExpanded(int groupPosition) {
        super.onGroupExpanded(groupPosition);
    }

    public long getGroupId(int groupPosition) {
        return 0;
    }

    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = minflater.inflate(R.layout.grouprow, null);
        }
        ((CheckedTextView) convertView).setText(groupItem.get(groupPosition));
        ((CheckedTextView) convertView).setChecked(isExpanded);
        return convertView;
    }

    public boolean hasStableIds() {
        return false;
    }

    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }

}
