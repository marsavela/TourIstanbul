package lbs.erasmus.touristanbul;

import android.app.ExpandableListActivity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by patmonsi on 24/03/14.
 */


public class DosAndDontsActivity extends ExpandableListActivity implements OnChildClickListener {

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
        child.add("Do remove your shoes before going inside a mosque.Women must cover hair and dress modestly. Men should not wear shorts.");
        child.add("Don’t talk loudly – it’s a place of worship. Try to avoid visiting a mosque on a Friday since it is a day of prayer.");
        child.add("Don’t walk directly in front of someone who’s praying or, according to their belief, his prayer won’t be counted.");
        childItem.add(child);

        /**
         * Add Do\s and Don\'ts For "When travelling during Ramadan"
         */
        child = new ArrayList<String>();
        child.add("Don’t offer people food, drink or even a smoke during the day. The month of Ramadan is the Islamic month of fasting, when devout Muslims refrain from eating and drinking from dawn until dusk. Show some courtesy while eating in public during the day.");
        child.add("Do be aware that it might be difficult to find food during the day in remote villages. Shop ahead.");
        childItem.add(child);
        /**
         * Add Do\s and Don\'ts For "When visiting traditional rural areas"
         */
        child = new ArrayList<String>();
        child.add("Do wear more conservative clothing outside of large cities. Shorts and short sleeves are still acceptable, though.");
        child.add("Do be aware that in rural areas where people are more conservative, men and women are expected to behave very modestly when showing any form of affection toward the opposite sex.");
        child.add("Don’t be offended, if you are a female travelling with a male companion, when Turkish men talk only to your companion. They are not being rude but rather protective of your honour.");
        child.add("Don’t, if you are a male, talk or sit next to a single, young Turkish woman. She might feel threatened.");
        childItem.add(child);
        /**
         * Add Do\s and Don\'ts For "When interacting with Turks"
         */
        child = new ArrayList<String>();
        child.add("Do learn gestures of the head for “yes” and “no”, which can be very confusing. “Yes” is a downward nod of the head and no is an upward nod of the head while raising eyebrows and clicking the tongue. Shaking your head might not be understood as “no”. Try to learn the Turkish words evet (yes) and hayir (no).");
        child.add("Don’t make the “OK” sign with your thumb and forefinger. It’s considered obscene in Turkey.");
        childItem.add(child);
        /**
         * Add Do\s and Don\'ts For "When talking to Turks"
         */
        child = new ArrayList<String>();
        child.add("Do be polite. Turkish customs place great emphasis on manners and politeness. It might come in handy to memorize some phrases or at least the words tesekkür ederim (thank you), lütfen (please) and pardon (excuse me). ");
        child.add("Don’t berate Kemal Atatürk. Turkish people have a deep respect for the Founding Father of the Turkish Republic, and they wouldn’t tolerate even a harmless joke about him, much less any form of derogatory discussion about his achievements.");
        childItem.add(child);
        /**
         * Add Do\s and Don\'ts For "When eating in a restaurant"
         */
        child = new ArrayList<String>();
        child.add("Do tolerate that abstinence from alcohol is greatly encouraged in Turkey as in many other Muslim countries. Although some Turks consume an occasional glass of wine – being drunk is considered a disgrace.");
        child.add("Don’t blow your nose or pick your teeth while sitting in a restaurant, cafe or bar. Turkish people find this just as impolite as burping openly. Go to the bathroom if you have to blow your nose or if something is stuck between your teeth.");
        child.add("Do be aware that smoking while eating is common and you shouldn’t request your dining partners to stop.");
        child.add("Don’t attempt to split the bill – it’s not traditionally done in Turkey. Pay for the whole meal if you initiated the invitation. If someone else pays, be sure to invite that person out to a meal in return before you leave Turkey.");
        childItem.add(child);
        /**
         * Add Do\s and Don\'ts For "When invited to someone’s home"
         */
        child = new ArrayList<String>();
        child.add("Do take off your shoes before entering a home.");
        child.add("Do bring a modest gift. Keep in mind that many Muslims adhere to an abstinent lifestyle – a bottle of alcohol is thus not an ideal gift. Giving money is considered rude.");
        child.add("Don’t show the bottom of your feet to anyone when sitting down. Since it is quite common to sit on cushions or carpets on the floor, try to sit cross-legged or with your legs to one side.");
        child.add("Do finish your plate. If you leave food on your plate, it is seen as a sign that you didn’t like the dish and risks offending your host.");
        childItem.add(child);
        /**
         * Add Do\s and Don\'ts For "When shopping at a bazaar"
         */
        child = new ArrayList<String>();
        child.add("Do bargain. It’s regarded as a polite gesture and a form of dialogue to negotiate the price before buying. But:");
        child.add("Don’t start to bargain if you’re not willing to buy the product in question. If the merchant gives you a price and you tell him yours in return, you have automatically entered into a business transaction.");
        child.add("Don’t buy stones or fossils. They are considered cultural artefacts and it’s illegal to export them. You could be forced to pay a fine or even end up in jail if caught going through customs with stones or fossils in your luggage.");
        childItem.add(child);
        /**
         * Add Do\s and Don\'ts For "When taking photos"
         */
        child = new ArrayList<String>();
        child.add("Don’t take pictures of people without asking permission. Older people, in particular, are afraid of the camera and consider it “the evil eye”.");
        child.add("Don’t take pictures of any woman wearing a veil, people praying, or any kind of military facility.");
        childItem.add(child);

    }

    @Override
    public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
        Toast.makeText(DosAndDontsActivity.this, "Clicked On Child", Toast.LENGTH_SHORT).show();
        return true;
    }
}
