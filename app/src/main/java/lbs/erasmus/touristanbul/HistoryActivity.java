package lbs.erasmus.touristanbul;


import android.app.Activity;
import android.app.ListActivity;
import android.os.Bundle;

import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;


public class HistoryActivity extends Activity {
    ArrayList<String> groupItem = new ArrayList();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setGroupData();
        setContentView(R.layout.activity_history);
        TextView textView1 = (TextView) findViewById(R.id.Text1);
        textView1.setText(groupItem.get(0));
        TextView textView2 = (TextView) findViewById(R.id.Text2);
        textView2.setText(groupItem.get(1));
        TextView textView3 = (TextView) findViewById(R.id.Text3);
        textView3.setText(groupItem.get(2));
        TextView textView4 = (TextView) findViewById(R.id.Text4);
        textView4.setText(groupItem.get(3));
        TextView textView5 = (TextView) findViewById(R.id.Text5);
        textView5.setText(groupItem.get(4));
        TextView textView6 = (TextView) findViewById(R.id.Text6);
        textView6.setText(groupItem.get(5));
        TextView textView7 = (TextView) findViewById(R.id.Text7);
        textView7.setText(groupItem.get(6));
        TextView textView8 = (TextView) findViewById(R.id.Text8);
        textView8.setText(groupItem.get(7));
        TextView textView9 = (TextView) findViewById(R.id.Text9);
        textView9.setText(groupItem.get(8));
        TextView textView10 = (TextView) findViewById(R.id.Text10);
        textView10.setText(groupItem.get(9));
        TextView textView11 = (TextView) findViewById(R.id.Text11);
        textView11.setText(groupItem.get(10));
        TextView textView12 = (TextView) findViewById(R.id.Text12);
        textView12.setText(groupItem.get(11));
        TextView textView13 = (TextView) findViewById(R.id.Text13);
        textView13.setText(groupItem.get(12));
        TextView textView14 = (TextView) findViewById(R.id.Text14);
        textView14.setText(groupItem.get(13));
        TextView textView15 = (TextView) findViewById(R.id.Text15);
        textView15.setText(groupItem.get(14));
        TextView textView16 = (TextView) findViewById(R.id.Text16);
        textView16.setText(groupItem.get(15));
        TextView textView17 = (TextView) findViewById(R.id.Text17);
        textView17.setText(groupItem.get(16));
        TextView textView18 = (TextView) findViewById(R.id.Text18);
        textView18.setText(groupItem.get(17));
        TextView textView19 = (TextView) findViewById(R.id.Text19);
        textView19.setText(groupItem.get(18));

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
        groupItem.add(getResources().getString(R.string.history15));
        groupItem.add(getResources().getString(R.string.history16));
        groupItem.add(getResources().getString(R.string.history17));
    }
}

