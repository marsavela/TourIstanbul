package lbs.erasmus.touristanbul.fragments;

import android.app.Fragment;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import lbs.erasmus.touristanbul.AssetProvider;
import lbs.erasmus.touristanbul.R;

/**
 * Created by SergiuDaniel on 9/03/14.
 */
public class AttractionsFragment extends Fragment implements AdapterView.OnItemClickListener {

    public AttractionsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Set fragment_attractions.xml to be the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_attractions, container, false);

        // Find the {@link GridView} that was already defined in the XML layout
        GridView gridView = (GridView) rootView.findViewById(R.id.grid);

        // Initialize the adapter with all the attractions. Set the adapter on the {@link GridView}.
        gridView.setAdapter(new AttractionsAdapter(inflater, createAllAttractions()));

        // Set a click listener for each attraction in the grid
        gridView.setOnItemClickListener(this);

        return rootView;
    }

    /**
     * Generate the list of all attractions.
     * @return The list of attractions.
     */
    private List<Attraction> createAllAttractions() {
        // You can add a title, subtitle, and a photo (in the assets directory).
        List<Attraction> attractions = new ArrayList<Attraction>();
        attractions.add(new Attraction("Topkapi Palace" /* title */,
                "Take a stroll in the flower garden" /* subtitle */, "topkapi.jpg" /* image */));
        attractions.add(new Attraction("Süleymaniye Mosque",
                "Locals come here to eat kuru fasuliye, the Turkish take on baked beans, in a street" +
                        " once haunted by opium addicts.", "suleymaniye.jpg"
        ));
        attractions.add(new Attraction("Aya Sofya",
                "Drive out to the vista point and watch the sunrise at 6am", "sophia.jpg"));
        attractions.add(new Attraction("Turkish and Islamic Arts Museum",
                "Don't leave without trying a thick black Turkish coffee in the pretty cafe in the" +
                        " grounds.", "islamicmuseum.jpg"
        ));
        attractions.add(new Attraction("Galata Tower",
                "Watery Istanbul is a city that cries out to be viewed from on high", "galata.jpg"));
        attractions.add(new Attraction("Basilica Cistern",
                "Your favorite meal cooked by yours truly", "cistern.jpg"));
        attractions.add(new Attraction("Chora Church",
                "Like Aya Sofya, it has made the journey from Byzantine church to Ottoman mosque.", "chora.jpg"));
        attractions.add(new Attraction("Blue Mosque",
                "One of only a handful of mosques in the world to boast six minarets.", "bluemosque.jpg"));
        attractions.add(new Attraction("Ayasofya Hürrem Sultan Hamam",
                "Think acres of marble, the sound of running water echoing around stupendous domes", "ayasofya.jpg"));
        attractions.add(new Attraction("Istanbul Archaeology Museums",
                "Walk to Istanbul's three-in-one equivalent of the British Museum via the grounds " +
                        "of Topkapi Palace.", "archeology.jpg"
        ));
        return attractions;
    }

    /**
     * Callback method for a when a attraction is clicked. A new share intent is created with the
     * attraction title. Then the user can select which app to share the content of the attraction with.
     *
     * @param parent The AdapterView where the click happened.
     * @param view The view within the AdapterView that was clicked (this
     *            will be a view provided by the adapter).
     * @param position The position of the view in the adapter.
     * @param id The row id of the item that was clicked.
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        // Find attraction that was clicked based off of position in adapter
        Attraction attraction = (Attraction) parent.getItemAtPosition(position);

        //TODO Hay que enlazar con la actividad correspondiente a las fichas de las atracciones
        if(attraction != null) {
            Toast.makeText(getActivity(), attraction.mTitle, Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(getActivity(), "ERROR RARO :-)", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Adapter for grid of attractions.
     */
    private static class AttractionsAdapter extends BaseAdapter {

        private LayoutInflater mInflater;
        private List<Attraction> mAllAttractions;

        /**
         * Constructs a new {@link lbs.erasmus.touristanbul.fragments.AttractionsFragment.AttractionsAdapter}.
         *
         * @param inflater to create new views
         * @param attractionList for list of all attractions to be displayed
         */
        public AttractionsAdapter(LayoutInflater inflater, List<Attraction> attractionList) {
            if (attractionList == null) {
                throw new IllegalStateException("Can't have null list of attractions");
            }
            mAllAttractions = attractionList;
            mInflater = inflater;
        }

        @Override
        public int getCount() {
            return mAllAttractions.size();
        }

        @Override
        public Attraction getItem(int position) {
            return mAllAttractions.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View result = convertView;
            if (result == null) {
                result = mInflater.inflate(R.layout.grid_item, parent, false);
            }

            // Try to get view cache or create a new one if needed
            ViewCache viewCache = (ViewCache) result.getTag();
            if (viewCache == null) {
                viewCache = new ViewCache(result);
                result.setTag(viewCache);
            }

            // Fetch item
            Attraction attraction = getItem(position);

            // Bind the data
            viewCache.mTitleView.setText(attraction.mTitle);
            viewCache.mSubtitleView.setText(attraction.mSubtitle);
            viewCache.mImageView.setImageURI(attraction.mImageUri);

            return result;
        }
    }

    /**
     * Cache of views in the grid item view to make recycling of views quicker. This avoids
     * additional {@link View#findViewById(int)} calls after the {@link ViewCache} is first
     * created for a view. See
     * {@link AttractionsAdapter#getView(int position, View convertView, ViewGroup parent)}.
     */
    private static class ViewCache {

        /** View that displays the title of the attraction */
        private final TextView mTitleView;

        /** View that displays the subtitle of the attraction */
        private final TextView mSubtitleView;

        /** View that displays the image associated with the attraction */
        private final ImageView mImageView;

        /**
         * Constructs a new {@link ViewCache}.
         *
         * @param view which contains children views that should be cached.
         */
        private ViewCache(View view) {
            mTitleView = (TextView) view.findViewById(R.id.title);
            mSubtitleView = (TextView) view.findViewById(R.id.subtitle);
            mImageView = (ImageView) view.findViewById(R.id.image);
        }
    }

    /**
     * Model object for attraction.
     */
    private static class Attraction {

        /** Title of the attraction. */
        private final String mTitle;

        /** Description of the attraction. */
        private final String mSubtitle;

        /** Content URI of the image for the attraction. */
        private final Uri mImageUri;

        /**
         * Constructs a new {@link Attraction}.
         *
         * @param titleString is the title
         * @param subtitleString is the description
         * @param imageAssetFilePath is the file path from the application's assets folder for
         *                           the image associated with this attraction
         */
        private Attraction(String titleString, String subtitleString, String imageAssetFilePath) {
            mTitle = titleString;
            mSubtitle = subtitleString;
            mImageUri = Uri.parse("content://" + AssetProvider.CONTENT_URI + "/" +
                    imageAssetFilePath);
        }
    }
}