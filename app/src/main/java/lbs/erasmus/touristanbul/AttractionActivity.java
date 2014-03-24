package lbs.erasmus.touristanbul;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageView;

import lbs.erasmus.touristanbul.domain.Attraction;
import lbs.erasmus.touristanbul.fadingactionbar.FadingActionBarHelper;

/**
 * Created by sergiu on 24/03/14.
 */
public class AttractionActivity extends Activity {

    Attraction mAttraction;
    //TextView mTitleView;
    ImageView mAttractionImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FadingActionBarHelper helper = new FadingActionBarHelper()
                .actionBarBackground(R.drawable.ab_background)
                .headerLayout(R.layout.atraction_header)
                .contentLayout(R.layout.activity_scrollview);
        setContentView(helper.createView(this));


        mAttraction = getIntent().getExtras().getParcelable("Attraction");
        /*mTitleView = (TextView) findViewById(R.id.title);
        mTitleView.setText(mAttraction.getTitle());*/
        mAttractionImageView = (ImageView) findViewById(R.id.image_header);
        mAttractionImageView.setImageURI(mAttraction.getImageUri());

        getActionBar().setTitle(mAttraction.getTitle());

        helper.initActionBar(this);
    }

}