package lbs.erasmus.touristanbul.fragments;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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

import lbs.erasmus.touristanbul.AttractionActivity;
import lbs.erasmus.touristanbul.DAOAttractions;
import lbs.erasmus.touristanbul.R;
import lbs.erasmus.touristanbul.domain.Attraction;

/**
 * Created by SergiuDaniel on 9/03/14.
 */
public class AttractionsFragment extends Fragment implements AdapterView.OnItemClickListener {

    private AttractionsAdapter attractionsAdapter;
    private ArrayList<Attraction> attractionArrayList;
    private GridView gridView;
    private DAOAttractions daoAttractions;

    public AttractionsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Set fragment_attractions.xml to be the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_attractions, container, false);

        // Find the {@link GridView} that was already defined in the XML layout
        gridView = (GridView) rootView.findViewById(R.id.grid);

        Bundle extras = getArguments();
        daoAttractions = new DAOAttractions(this.getActivity());
        //attractionArrayList = new ArrayList<Attraction>();
        Log.v("VERBOSE", "VAlor de los extras " + extras.toString() + extras.size());
        if (extras != null) {
            Log.v("VERBOSE", "Hay extras");
             attractionArrayList = extras.getParcelableArrayList("Attractions");
           // attractionArrayList = daoAttractions.getAttractions();
            // Initialize the adapter with all the attractions. Set the adapter on the {@link GridView}.
            //gridView.setAdapter(new AttractionsAdapter(inflater, createAllAttractions()));
            attractionsAdapter = new AttractionsAdapter(inflater, attractionArrayList);
            gridView.setAdapter(attractionsAdapter);

            // Set a click listener for each attraction in the grid
            gridView.setOnItemClickListener(this);

        }

        return rootView;
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

        if(attraction != null) {
            //Toast.makeText(getActivity(), attraction.mTitle, Toast.LENGTH_LONG).show();
            Intent i = new Intent(getActivity(), AttractionActivity.class);
            i.putExtra("Attraction", attraction);
            startActivity(i);
        } else {
            Toast.makeText(getActivity(), "ERROR RARO :-)", Toast.LENGTH_LONG).show();
        }
    }

    public void setAttractions(ArrayList<Attraction> mAttractionsList) {
        //Log.v("VERBOSE", "datos del primer name del resultado 2: " + mAttractionsList.get(0).getTitle());
        attractionArrayList = mAttractionsList;
        //Log.v("VERBOSE", "datos del primer name del resultado 3: " + attractionArrayList.get(0).getTitle());
        updateAdapter(attractionArrayList);
    }

    public void updateAdapter(ArrayList<Attraction> list) {
        if (attractionsAdapter != null)
            attractionsAdapter.updateList(list);
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
            viewCache.mTitleView.setText(attraction.getTitle());
            viewCache.mSubtitleView.setText(attraction.getSubtitle());
            viewCache.mImageView.setImageURI(attraction.getImageUri());

            return result;
        }

        public void updateList(ArrayList<Attraction> newlist) {
            mAllAttractions.clear();
            mAllAttractions.addAll(newlist);
            this.notifyDataSetChanged();
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
}