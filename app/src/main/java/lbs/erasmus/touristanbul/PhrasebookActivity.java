package lbs.erasmus.touristanbul;

import android.app.ExpandableListActivity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.Toast;
import java.util.ArrayList;


/**
 * Created by HP on 03/04/2014.
 */
public class PhrasebookActivity extends ExpandableListActivity implements ExpandableListView.OnChildClickListener {

    ArrayList<String> groupItem = new ArrayList<String>();
    ArrayList<Object> childItem = new ArrayList<Object>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ExpandableListView expandList = getExpandableListView();
        expandList.setDividerHeight(2);
        expandList.setGroupIndicator(null);
        expandList.setClickable(true);

        // 1st level data
        setGroupData();

        // 2nd level data
        setChildGroupData();

        ParentLevelAdapter parentAdapter = new ParentLevelAdapter(groupItem, childItem);
        parentAdapter.setInflater((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE), this);
        getExpandableListView().setAdapter(parentAdapter);
        expandList.setOnChildClickListener(this);
    }

    public void setGroupData() {
        groupItem.add(getResources().getString(R.string.phrasebook1_basic_greetings));
        groupItem.add(getResources().getString(R.string.phrasebook2_asking_for_help_and_directions));
        groupItem.add(getResources().getString(R.string.phrasebook3_how_to_introduce_yourself));
        groupItem.add(getResources().getString(R.string.phrasebook4_wish_someone_something));
        groupItem.add(getResources().getString(R.string.phrasebook5_solving_a_misunderstanding));
        groupItem.add(getResources().getString(R.string.phrasebook6_expressions_and_words));
    }

    public void setChildGroupData() {
        /**
         * Add phrases for "Basic Greetings"
         */
        ArrayList<String> child = new ArrayList<String>();
        child.add(getResources().getString(R.string.phrasebook1_basic_greetings_child1_en));
        child.add(getResources().getString(R.string.phrasebook1_basic_greetings_child1_tr));
        child.add(getResources().getString(R.string.phrasebook1_basic_greetings_child2_en));
        child.add(getResources().getString(R.string.phrasebook1_basic_greetings_child2_tr));
        child.add(getResources().getString(R.string.phrasebook1_basic_greetings_child3_en));
        child.add(getResources().getString(R.string.phrasebook1_basic_greetings_child3_tr));
        child.add(getResources().getString(R.string.phrasebook1_basic_greetings_child4_en));
        child.add(getResources().getString(R.string.phrasebook1_basic_greetings_child4_tr));
        child.add(getResources().getString(R.string.phrasebook1_basic_greetings_child5_en));
        child.add(getResources().getString(R.string.phrasebook1_basic_greetings_child5_tr));
        child.add(getResources().getString(R.string.phrasebook1_basic_greetings_child6_en));
        child.add(getResources().getString(R.string.phrasebook1_basic_greetings_child6_tr));
        child.add(getResources().getString(R.string.phrasebook1_basic_greetings_child7_en));
        child.add(getResources().getString(R.string.phrasebook1_basic_greetings_child7_tr));
        child.add(getResources().getString(R.string.phrasebook1_basic_greetings_child8_en));
        child.add(getResources().getString(R.string.phrasebook1_basic_greetings_child8_tr));
        child.add(getResources().getString(R.string.phrasebook1_basic_greetings_child9_en));
        child.add(getResources().getString(R.string.phrasebook1_basic_greetings_child9_tr));
        child.add(getResources().getString(R.string.phrasebook1_basic_greetings_child10_en));
        child.add(getResources().getString(R.string.phrasebook1_basic_greetings_child10_tr));
        child.add(getResources().getString(R.string.phrasebook1_basic_greetings_child11_en));
        child.add(getResources().getString(R.string.phrasebook1_basic_greetings_child11_tr));
        child.add(getResources().getString(R.string.phrasebook1_basic_greetings_child12_en));
        child.add(getResources().getString(R.string.phrasebook1_basic_greetings_child12_tr));
        child.add(getResources().getString(R.string.phrasebook1_basic_greetings_child13_en));
        child.add(getResources().getString(R.string.phrasebook1_basic_greetings_child13_tr));
        child.add(getResources().getString(R.string.phrasebook1_basic_greetings_child14_en));
        child.add(getResources().getString(R.string.phrasebook1_basic_greetings_child14_tr));
        child.add(getResources().getString(R.string.phrasebook1_basic_greetings_child15_en));
        child.add(getResources().getString(R.string.phrasebook1_basic_greetings_child15_tr));
        child.add(getResources().getString(R.string.phrasebook1_basic_greetings_child16_en));
        child.add(getResources().getString(R.string.phrasebook1_basic_greetings_child16_tr));
        child.add(getResources().getString(R.string.phrasebook1_basic_greetings_child17_en));
        child.add(getResources().getString(R.string.phrasebook1_basic_greetings_child17_tr));
        childItem.add(child);

        /**
         * Add phrases for "Asking for help and directions"
         */
        child = new ArrayList<String>();
        child.add(getResources().getString(R.string.phrasebook2_asking_for_help_and_directions_child1_en));
        child.add(getResources().getString(R.string.phrasebook2_asking_for_help_and_directions_child1_tr));
        child.add(getResources().getString(R.string.phrasebook2_asking_for_help_and_directions_child2_en));
        child.add(getResources().getString(R.string.phrasebook2_asking_for_help_and_directions_child2_tr));
        child.add(getResources().getString(R.string.phrasebook2_asking_for_help_and_directions_child3_en));
        child.add(getResources().getString(R.string.phrasebook2_asking_for_help_and_directions_child3_tr));
        child.add(getResources().getString(R.string.phrasebook2_asking_for_help_and_directions_child4_en));
        child.add(getResources().getString(R.string.phrasebook2_asking_for_help_and_directions_child4_tr));
        child.add(getResources().getString(R.string.phrasebook2_asking_for_help_and_directions_child5_en));
        child.add(getResources().getString(R.string.phrasebook2_asking_for_help_and_directions_child5_tr));
        child.add(getResources().getString(R.string.phrasebook2_asking_for_help_and_directions_child6_en));
        child.add(getResources().getString(R.string.phrasebook2_asking_for_help_and_directions_child6_tr));
        child.add(getResources().getString(R.string.phrasebook2_asking_for_help_and_directions_child7_en));
        child.add(getResources().getString(R.string.phrasebook2_asking_for_help_and_directions_child7_tr));
        child.add(getResources().getString(R.string.phrasebook2_asking_for_help_and_directions_child8_en));
        child.add(getResources().getString(R.string.phrasebook2_asking_for_help_and_directions_child8_tr));
        child.add(getResources().getString(R.string.phrasebook2_asking_for_help_and_directions_child9_en));
        child.add(getResources().getString(R.string.phrasebook2_asking_for_help_and_directions_child9_tr));
        child.add(getResources().getString(R.string.phrasebook2_asking_for_help_and_directions_child10_en));
        child.add(getResources().getString(R.string.phrasebook2_asking_for_help_and_directions_child10_tr));
        child.add(getResources().getString(R.string.phrasebook2_asking_for_help_and_directions_child11_en));
        child.add(getResources().getString(R.string.phrasebook2_asking_for_help_and_directions_child11_tr));
        child.add(getResources().getString(R.string.phrasebook2_asking_for_help_and_directions_child12_en));
        child.add(getResources().getString(R.string.phrasebook2_asking_for_help_and_directions_child12_tr));
        childItem.add(child);

        /**
         * Add phrases for "How to introduce Yourself"
         */
        child = new ArrayList<String>();
        child.add(getResources().getString(R.string.phrasebook3_how_to_introduce_yourself_child1_en));
        child.add(getResources().getString(R.string.phrasebook3_how_to_introduce_yourself_child1_tr));
        child.add(getResources().getString(R.string.phrasebook3_how_to_introduce_yourself_child2_en));
        child.add(getResources().getString(R.string.phrasebook3_how_to_introduce_yourself_child2_tr));
        child.add(getResources().getString(R.string.phrasebook3_how_to_introduce_yourself_child3_en));
        child.add(getResources().getString(R.string.phrasebook3_how_to_introduce_yourself_child3_tr));
        child.add(getResources().getString(R.string.phrasebook3_how_to_introduce_yourself_child4_en));
        child.add(getResources().getString(R.string.phrasebook3_how_to_introduce_yourself_child4_tr));
        child.add(getResources().getString(R.string.phrasebook3_how_to_introduce_yourself_child5_en));
        child.add(getResources().getString(R.string.phrasebook3_how_to_introduce_yourself_child5_tr));
        child.add(getResources().getString(R.string.phrasebook3_how_to_introduce_yourself_child6_en));
        child.add(getResources().getString(R.string.phrasebook3_how_to_introduce_yourself_child6_tr));
        child.add(getResources().getString(R.string.phrasebook3_how_to_introduce_yourself_child7_en));
        child.add(getResources().getString(R.string.phrasebook3_how_to_introduce_yourself_child7_tr));
        child.add(getResources().getString(R.string.phrasebook3_how_to_introduce_yourself_child8_en));
        child.add(getResources().getString(R.string.phrasebook3_how_to_introduce_yourself_child8_tr));
        child.add(getResources().getString(R.string.phrasebook3_how_to_introduce_yourself_child9_en));
        child.add(getResources().getString(R.string.phrasebook3_how_to_introduce_yourself_child9_tr));
        child.add(getResources().getString(R.string.phrasebook3_how_to_introduce_yourself_child10_en));
        child.add(getResources().getString(R.string.phrasebook3_how_to_introduce_yourself_child10_tr));
        child.add(getResources().getString(R.string.phrasebook3_how_to_introduce_yourself_child11_en));
        child.add(getResources().getString(R.string.phrasebook3_how_to_introduce_yourself_child11_tr));
        child.add(getResources().getString(R.string.phrasebook3_how_to_introduce_yourself_child12_en));
        child.add(getResources().getString(R.string.phrasebook3_how_to_introduce_yourself_child12_tr));
        child.add(getResources().getString(R.string.phrasebook3_how_to_introduce_yourself_child13_en));
        child.add(getResources().getString(R.string.phrasebook3_how_to_introduce_yourself_child13_tr));
        child.add(getResources().getString(R.string.phrasebook3_how_to_introduce_yourself_child14_en));
        child.add(getResources().getString(R.string.phrasebook3_how_to_introduce_yourself_child14_tr));
        child.add(getResources().getString(R.string.phrasebook3_how_to_introduce_yourself_child15_en));
        child.add(getResources().getString(R.string.phrasebook3_how_to_introduce_yourself_child15_tr));
        child.add(getResources().getString(R.string.phrasebook3_how_to_introduce_yourself_child16_en));
        child.add(getResources().getString(R.string.phrasebook3_how_to_introduce_yourself_child16_tr));
        child.add(getResources().getString(R.string.phrasebook3_how_to_introduce_yourself_child17_en));
        child.add(getResources().getString(R.string.phrasebook3_how_to_introduce_yourself_child17_tr));
        child.add(getResources().getString(R.string.phrasebook3_how_to_introduce_yourself_child18_en));
        child.add(getResources().getString(R.string.phrasebook3_how_to_introduce_yourself_child18_tr));
        child.add(getResources().getString(R.string.phrasebook3_how_to_introduce_yourself_child19_en));
        child.add(getResources().getString(R.string.phrasebook3_how_to_introduce_yourself_child19_tr));
        child.add(getResources().getString(R.string.phrasebook3_how_to_introduce_yourself_child20_en));
        child.add(getResources().getString(R.string.phrasebook3_how_to_introduce_yourself_child20_tr));
        child.add(getResources().getString(R.string.phrasebook3_how_to_introduce_yourself_child21_en));
        child.add(getResources().getString(R.string.phrasebook3_how_to_introduce_yourself_child21_tr));
        child.add(getResources().getString(R.string.phrasebook3_how_to_introduce_yourself_child22_en));
        child.add(getResources().getString(R.string.phrasebook3_how_to_introduce_yourself_child22_tr));
        child.add(getResources().getString(R.string.phrasebook3_how_to_introduce_yourself_child23_en));
        child.add(getResources().getString(R.string.phrasebook3_how_to_introduce_yourself_child23_tr));
        childItem.add(child);

        /**
         * Add phrases for "Wish Someone Something"
         */
        child = new ArrayList<String>();
        child.add(getResources().getString(R.string.phrasebook4_wish_someone_something_child1_en));
        child.add(getResources().getString(R.string.phrasebook4_wish_someone_something_child1_tr));
        child.add(getResources().getString(R.string.phrasebook4_wish_someone_something_child2_en));
        child.add(getResources().getString(R.string.phrasebook4_wish_someone_something_child2_tr));
        child.add(getResources().getString(R.string.phrasebook4_wish_someone_something_child3_en));
        child.add(getResources().getString(R.string.phrasebook4_wish_someone_something_child3_tr));
        child.add(getResources().getString(R.string.phrasebook4_wish_someone_something_child4_en));
        child.add(getResources().getString(R.string.phrasebook4_wish_someone_something_child4_tr));
        child.add(getResources().getString(R.string.phrasebook4_wish_someone_something_child5_en));
        child.add(getResources().getString(R.string.phrasebook4_wish_someone_something_child5_tr));
        child.add(getResources().getString(R.string.phrasebook4_wish_someone_something_child6_en));
        child.add(getResources().getString(R.string.phrasebook4_wish_someone_something_child6_tr));
        child.add(getResources().getString(R.string.phrasebook4_wish_someone_something_child7_en));
        child.add(getResources().getString(R.string.phrasebook4_wish_someone_something_child7_tr));
        child.add(getResources().getString(R.string.phrasebook4_wish_someone_something_child8_en));
        child.add(getResources().getString(R.string.phrasebook4_wish_someone_something_child8_tr));
        child.add(getResources().getString(R.string.phrasebook4_wish_someone_something_child9_en));
        child.add(getResources().getString(R.string.phrasebook4_wish_someone_something_child9_tr));
        child.add(getResources().getString(R.string.phrasebook4_wish_someone_something_child10_en));
        child.add(getResources().getString(R.string.phrasebook4_wish_someone_something_child10_tr));
        childItem.add(child);

        /**
         * Add phrases for "Solving a Misunderstanding"
         */
        child = new ArrayList<String>();
        child.add(getResources().getString(R.string.phrasebook5_solving_a_misunderstanding_child1_en));
        child.add(getResources().getString(R.string.phrasebook5_solving_a_misunderstanding_child1_tr));
        child.add(getResources().getString(R.string.phrasebook5_solving_a_misunderstanding_child2_en));
        child.add(getResources().getString(R.string.phrasebook5_solving_a_misunderstanding_child2_tr));
        child.add(getResources().getString(R.string.phrasebook5_solving_a_misunderstanding_child3_en));
        child.add(getResources().getString(R.string.phrasebook5_solving_a_misunderstanding_child3_tr));
        child.add(getResources().getString(R.string.phrasebook5_solving_a_misunderstanding_child4_en));
        child.add(getResources().getString(R.string.phrasebook5_solving_a_misunderstanding_child4_tr));
        child.add(getResources().getString(R.string.phrasebook5_solving_a_misunderstanding_child5_en));
        child.add(getResources().getString(R.string.phrasebook5_solving_a_misunderstanding_child5_tr));
        child.add(getResources().getString(R.string.phrasebook5_solving_a_misunderstanding_child6_en));
        child.add(getResources().getString(R.string.phrasebook5_solving_a_misunderstanding_child6_tr));
        child.add(getResources().getString(R.string.phrasebook5_solving_a_misunderstanding_child7_en));
        child.add(getResources().getString(R.string.phrasebook5_solving_a_misunderstanding_child7_tr));
        child.add(getResources().getString(R.string.phrasebook5_solving_a_misunderstanding_child8_en));
        child.add(getResources().getString(R.string.phrasebook5_solving_a_misunderstanding_child8_tr));
        child.add(getResources().getString(R.string.phrasebook5_solving_a_misunderstanding_child9_en));
        child.add(getResources().getString(R.string.phrasebook5_solving_a_misunderstanding_child9_tr));
        child.add(getResources().getString(R.string.phrasebook5_solving_a_misunderstanding_child10_en));
        child.add(getResources().getString(R.string.phrasebook5_solving_a_misunderstanding_child10_tr));
        child.add(getResources().getString(R.string.phrasebook5_solving_a_misunderstanding_child11_en));
        child.add(getResources().getString(R.string.phrasebook5_solving_a_misunderstanding_child11_tr));
        child.add(getResources().getString(R.string.phrasebook5_solving_a_misunderstanding_child12_en));
        child.add(getResources().getString(R.string.phrasebook5_solving_a_misunderstanding_child12_tr));
        child.add(getResources().getString(R.string.phrasebook5_solving_a_misunderstanding_child13_en));
        child.add(getResources().getString(R.string.phrasebook5_solving_a_misunderstanding_child13_tr));
        child.add(getResources().getString(R.string.phrasebook5_solving_a_misunderstanding_child14_en));
        child.add(getResources().getString(R.string.phrasebook5_solving_a_misunderstanding_child14_tr));
        child.add(getResources().getString(R.string.phrasebook5_solving_a_misunderstanding_child15_en));
        child.add(getResources().getString(R.string.phrasebook5_solving_a_misunderstanding_child15_tr));
        child.add(getResources().getString(R.string.phrasebook5_solving_a_misunderstanding_child16_en));
        child.add(getResources().getString(R.string.phrasebook5_solving_a_misunderstanding_child16_tr));
        childItem.add(child);

        /**
         * Add phrases for "Expressions and Words"
         */
        child = new ArrayList<String>();
        child.add(getResources().getString(R.string.phrasebook6_expressions_and_words_child1_en));
        child.add(getResources().getString(R.string.phrasebook6_expressions_and_words_child1_tr));
        child.add(getResources().getString(R.string.phrasebook6_expressions_and_words_child2_en));
        child.add(getResources().getString(R.string.phrasebook6_expressions_and_words_child2_tr));
        child.add(getResources().getString(R.string.phrasebook6_expressions_and_words_child3_en));
        child.add(getResources().getString(R.string.phrasebook6_expressions_and_words_child3_tr));
        child.add(getResources().getString(R.string.phrasebook6_expressions_and_words_child4_en));
        child.add(getResources().getString(R.string.phrasebook6_expressions_and_words_child4_tr));
        child.add(getResources().getString(R.string.phrasebook6_expressions_and_words_child5_en));
        child.add(getResources().getString(R.string.phrasebook6_expressions_and_words_child5_tr));
        child.add(getResources().getString(R.string.phrasebook6_expressions_and_words_child6_en));
        child.add(getResources().getString(R.string.phrasebook6_expressions_and_words_child6_tr));
        child.add(getResources().getString(R.string.phrasebook6_expressions_and_words_child7_en));
        child.add(getResources().getString(R.string.phrasebook6_expressions_and_words_child7_tr));
        child.add(getResources().getString(R.string.phrasebook6_expressions_and_words_child8_en));
        child.add(getResources().getString(R.string.phrasebook6_expressions_and_words_child8_tr));
        child.add(getResources().getString(R.string.phrasebook6_expressions_and_words_child9_en));
        child.add(getResources().getString(R.string.phrasebook6_expressions_and_words_child9_tr));
        child.add(getResources().getString(R.string.phrasebook6_expressions_and_words_child10_tr));
        child.add(getResources().getString(R.string.phrasebook6_expressions_and_words_child10_en));
        child.add(getResources().getString(R.string.phrasebook6_expressions_and_words_child11_tr));
        child.add(getResources().getString(R.string.phrasebook6_expressions_and_words_child11_en));
        child.add(getResources().getString(R.string.phrasebook6_expressions_and_words_child12_tr));
        child.add(getResources().getString(R.string.phrasebook6_expressions_and_words_child12_en));
        child.add(getResources().getString(R.string.phrasebook6_expressions_and_words_child13_tr));
        child.add(getResources().getString(R.string.phrasebook6_expressions_and_words_child13_en));
        child.add(getResources().getString(R.string.phrasebook6_expressions_and_words_child14_tr));
        child.add(getResources().getString(R.string.phrasebook6_expressions_and_words_child14_en));
        child.add(getResources().getString(R.string.phrasebook6_expressions_and_words_child15_tr));
        child.add(getResources().getString(R.string.phrasebook6_expressions_and_words_child15_en));
        child.add(getResources().getString(R.string.phrasebook6_expressions_and_words_child16_tr));
        child.add(getResources().getString(R.string.phrasebook6_expressions_and_words_child16_en));
        child.add(getResources().getString(R.string.phrasebook6_expressions_and_words_child17_en));
        child.add(getResources().getString(R.string.phrasebook6_expressions_and_words_child17_tr));
        child.add(getResources().getString(R.string.phrasebook6_expressions_and_words_child18_en));
        child.add(getResources().getString(R.string.phrasebook6_expressions_and_words_child18_tr));
        child.add(getResources().getString(R.string.phrasebook6_expressions_and_words_child19_en));
        child.add(getResources().getString(R.string.phrasebook6_expressions_and_words_child19_tr));
        child.add(getResources().getString(R.string.phrasebook6_expressions_and_words_child20_en));
        child.add(getResources().getString(R.string.phrasebook6_expressions_and_words_child20_tr));
        child.add(getResources().getString(R.string.phrasebook6_expressions_and_words_child21_en));
        child.add(getResources().getString(R.string.phrasebook6_expressions_and_words_child21_tr));
        child.add(getResources().getString(R.string.phrasebook6_expressions_and_words_child22_en));
        child.add(getResources().getString(R.string.phrasebook6_expressions_and_words_child22_tr));
        child.add(getResources().getString(R.string.phrasebook6_expressions_and_words_child23_en));
        child.add(getResources().getString(R.string.phrasebook6_expressions_and_words_child23_tr));
        childItem.add(child);
    }

    @Override
    public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
        Toast.makeText(PhrasebookActivity.this, "Clicked On Child", Toast.LENGTH_SHORT).show();
        return true;
    }
}
