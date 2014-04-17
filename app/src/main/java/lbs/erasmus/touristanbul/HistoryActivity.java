package lbs.erasmus.touristanbul;

import android.app.Activity;
import android.app.ListActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;

import java.util.ArrayList;

/**
 * Created by patmonsi on 24/03/14.
 */

public class HistoryActivity extends ListActivity {
    ArrayList<String> groupItem = new ArrayList();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setGroupData();
        setContentView(R.layout.activity_history);

        setListAdapter(new ArrayAdapter(this, android.R.layout.simple_list_item_1, groupItem));
    }


    public void setGroupData() {
        groupItem.add("HISTORY");
        groupItem.add(getResources().getString(R.string.history0));
        groupItem.add(getResources().getString(R.string.history1));
        groupItem.add(getResources().getString(R.string.history2));
        groupItem.add(getResources().getString(R.string.history3));
        groupItem.add(getResources().getString(R.string.history4));
        groupItem.add(getResources().getString(R.string.history5));
        groupItem.add(getResources().getString(R.string.history6));
        groupItem.add(getResources().getString(R.string.history7));
        groupItem.add(getResources().getString(R.string.history8));
        groupItem.add(getResources().getString(R.string.history9));
        groupItem.add(getResources().getString(R.string.history10));
        groupItem.add(getResources().getString(R.string.history11));
        groupItem.add(getResources().getString(R.string.history12));
        groupItem.add(getResources().getString(R.string.history13));
        groupItem.add(getResources().getString(R.string.history14));
    }
}