package lbs.erasmus.touristanbul;

import android.app.ExpandableListActivity;
import android.content.Context;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;

import java.util.ArrayList;

/**
 * Created by patmonsi on 24/03/14.
 */


public class DosAndDontsActivity extends ExpandableListActivity implements OnChildClickListener {

    ArrayList<String> groupItem = new ArrayList<String>();
    ArrayList<Object> childItem = new ArrayList<Object>();
    private TextToSpeech mTTS;

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
        groupItem.add("When visiting a mosque");
        groupItem.add("When travelling during Ramadan");
        groupItem.add("When visiting traditional rural areas");
        groupItem.add("When interacting with Turks");
        groupItem.add("When talking to Turks");
        groupItem.add("When eating in a restaurant");
        groupItem.add("When invited to someone’s home");
        groupItem.add("When shopping at a bazaar");
        groupItem.add("When taking photos");
    }

    public void setChildGroupData() {
        /**
         * Add Do\s and Don\'ts For "When visiting a mosque"
         */
        ArrayList<String> child = new ArrayList<String>();
        child.add(getResources().getString(R.string.when_visiting_a_mosque_child1));
        child.add(getResources().getString(R.string.when_visiting_a_mosque_child2));
        child.add(getResources().getString(R.string.when_visiting_a_mosque_child3));
        childItem.add(child);

        /**
         * Add Do\s and Don\'ts For "When travelling during Ramadan"
         */
        child = new ArrayList<String>();
        child.add(getResources().getString(R.string.when_travelling_during_ramadan_child1));
        child.add(getResources().getString(R.string.when_travelling_during_ramadan_child2));
        childItem.add(child);
        /**
         * Add Do\s and Don\'ts For "When visiting traditional rural areas"
         */
        child = new ArrayList<String>();
        child.add(getResources().getString(R.string.when_visiting_traditional_rural_areas_child1));
        child.add(getResources().getString(R.string.when_visiting_traditional_rural_areas_child2));
        child.add(getResources().getString(R.string.when_visiting_traditional_rural_areas_child3));
        child.add(getResources().getString(R.string.when_visiting_traditional_rural_areas_child4));
        childItem.add(child);
        /**
         * Add Do\s and Don\'ts For "When interacting with Turks"
         */
        child = new ArrayList<String>();
        child.add(getResources().getString(R.string.when_interacting_with_turks_child1));
        child.add(getResources().getString(R.string.when_interacting_with_turks_child2));
        childItem.add(child);
        /**
         * Add Do\s and Don\'ts For "When talking to Turks"
         */
        child = new ArrayList<String>();
        child.add(getResources().getString(R.string.when_talking_to_turks_child1));
        child.add(getResources().getString(R.string.when_talking_to_turks_child2));
        childItem.add(child);
        /**
         * Add Do\s and Don\'ts For "When eating in a restaurant"
         */
        child = new ArrayList<String>();
        child.add(getResources().getString(R.string.when_eating_in_a_restaurant_child1));
        child.add(getResources().getString(R.string.when_eating_in_a_restaurant_child2));
        child.add(getResources().getString(R.string.when_eating_in_a_restaurant_child3));
        child.add(getResources().getString(R.string.when_eating_in_a_restaurant_child4));
        childItem.add(child);
        /**
         * Add Do\s and Don\'ts For "When invited to someone’s home"
         */
        child = new ArrayList<String>();
        child.add(getResources().getString(R.string.when_invited_to_someones_home_child1));
        child.add(getResources().getString(R.string.when_invited_to_someones_home_child2));
        child.add(getResources().getString(R.string.when_invited_to_someones_home_child3));
        child.add(getResources().getString(R.string.when_invited_to_someones_home_child4));
        childItem.add(child);
        /**
         * Add Do\s and Don\'ts For "When shopping at a bazaar"
         */
        child = new ArrayList<String>();
        child.add(getResources().getString(R.string.when_shopping_at_a_bazaar_child1));
        child.add(getResources().getString(R.string.when_shopping_at_a_bazaar_child2));
        child.add(getResources().getString(R.string.when_shopping_at_a_bazaar_child3));
        childItem.add(child);
        /**
         * Add Do\s and Don\'ts For "When taking photos"
         */
        child = new ArrayList<String>();
        child.add(getResources().getString(R.string.when_taking_photos_child1));
        child.add(getResources().getString(R.string.when_taking_photos_child2));
        childItem.add(child);

    }



    @Override
    public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
    //    Toast.makeText(DosAndDontsActivity.this, "Clicked On Child", Toast.LENGTH_SHORT).show();
        return true;
    }
}
