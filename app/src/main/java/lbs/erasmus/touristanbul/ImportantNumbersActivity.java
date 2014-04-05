package lbs.erasmus.touristanbul;

import android.app.ExpandableListActivity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by patmonsi on 24/03/14.
 */
public class ImportantNumbersActivity extends ExpandableListActivity implements ExpandableListView.OnChildClickListener {

    ArrayList<String> groupItem = new ArrayList<String>();
    ArrayList<Object> childItem = new ArrayList<Object>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ExpandableListView expandbleLis = getExpandableListView();
        expandbleLis.setDividerHeight(2);
        expandbleLis.setGroupIndicator(null);
        expandbleLis.setClickable(true);

        // 1st level data
        setGroupData();

        // 2nd level data
        setChildGroupData();

        ParentLevelAdapter parentAdapter = new ParentLevelAdapter(groupItem, childItem);
        parentAdapter.setInflater((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE), this);
        getExpandableListView().setAdapter(parentAdapter);
        expandbleLis.setOnChildClickListener(this);
    }

    public void setGroupData() {
        groupItem.add(getResources().getString(R.string.important_numbers1));
        groupItem.add(getResources().getString(R.string.important_numbers2));
        groupItem.add(getResources().getString(R.string.important_numbers3));
        groupItem.add(getResources().getString(R.string.important_numbers4));
        groupItem.add(getResources().getString(R.string.important_numbers5));
        groupItem.add(getResources().getString(R.string.important_numbers6));
        groupItem.add(getResources().getString(R.string.important_numbers7));
        groupItem.add(getResources().getString(R.string.important_numbers8));
        groupItem.add(getResources().getString(R.string.important_numbers9));
        groupItem.add(getResources().getString(R.string.important_numbers10));
        groupItem.add(getResources().getString(R.string.important_numbers11));
        groupItem.add(getResources().getString(R.string.important_numbers12));
        groupItem.add(getResources().getString(R.string.important_numbers13));
        groupItem.add(getResources().getString(R.string.important_numbers14));
        groupItem.add(getResources().getString(R.string.important_numbers15));
    }

    public void setChildGroupData() {
        /**
         * Add ambulance number
         */
        ArrayList<String> child = new ArrayList<String>();
        child.add(getResources().getString(R.string.important_numbers_ambulance));
        childItem.add(child);

        /**
         * Add fire number
         */
        child = new ArrayList<String>();
        child.add(getResources().getString(R.string.important_numbers_fire));
        childItem.add(child);
        /**
         * Add police number
         */
        child = new ArrayList<String>();
        child.add(getResources().getString(R.string.important_numbers_police));
        childItem.add(child);
        /**
         * Add coastguard number
         */
        child = new ArrayList<String>();
        child.add(getResources().getString(R.string.important_numbers_coastguard));
        childItem.add(child);
        /**
         * Add
         */
        child = new ArrayList<String>();
        child.add(getResources().getString(R.string.important_numbers_forest));
        childItem.add(child);


        child = new ArrayList<String>();
        child.add(getResources().getString(R.string.important_numbers_missing));
        childItem.add(child);


        child = new ArrayList<String>();
        child.add(getResources().getString(R.string.important_numbers_chilean_consulate));
        childItem.add(child);

        child = new ArrayList<String>();
        child.add(getResources().getString(R.string.important_numbers_portuguese_consulate));
        childItem.add(child);

        child = new ArrayList<String>();
        child.add(getResources().getString(R.string.important_numbers_brazilian_consulate));
        childItem.add(child);

        child = new ArrayList<String>();
        child.add(getResources().getString(R.string.important_numbers_brazilian_consulate));
        childItem.add(child);


        child = new ArrayList<String>();
        child.add(getResources().getString(R.string.important_numbers_colombian_consulate));
        childItem.add(child);

        child = new ArrayList<String>();
        child.add(getResources().getString(R.string.important_numbers_costa_rican_consulate));
        childItem.add(child);


        child = new ArrayList<String>();
        child.add(getResources().getString(R.string.important_numbers_dominican));
        childItem.add(child);


        child = new ArrayList<String>();
        child.add(getResources().getString(R.string.important_numbers_ecuadorian));
        childItem.add(child);


        child = new ArrayList<String>();
        child.add(getResources().getString(R.string.important_numbers_salvadorean));
        childItem.add(child);
    }

    @Override
    public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
        //Toast.makeText(ImportantNumbersActivity.this, "Clicked On Child", Toast.LENGTH_SHORT).show();
        return true;
    }
}
