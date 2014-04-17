package lbs.erasmus.touristanbul;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by SergiuDaniel on 17/04/2014.
 * Adapter for grid of attractions.
 */
 public class DrawerAdapter extends ArrayAdapter<String> {

    private final Context context;
    private final String[] values;
    /**
     * Constructs a new {@link lbs.erasmus.touristanbul.fragments.AttractionsFragment.AttractionsAdapter}.
     *
     * @param context to create new views
     * @param values for list of all attractions to be displayed
     */
    public DrawerAdapter(Context context, String[] values) {
        super(context, R.layout.fragment_navigation_row, values);
        this.context = context;
        this.values = values;
    }

    @Override
    public int getCount() {
        return values.length;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.fragment_navigation_row, parent, false);
        TextView textView = (TextView) rowView.findViewById(R.id.label);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.icon);
        textView.setText(values[position]);

        switch (position) {
            case 0:
                imageView.setImageResource(android.R.drawable.ic_menu_mapmode);
                break;
            case 1:
                imageView.setImageResource(android.R.drawable.ic_menu_myplaces);
                break;
            case 2:
                imageView.setImageResource(android.R.drawable.ic_menu_preferences);
                break;
            case 3:
                imageView.setImageResource(R.drawable.ic_menu_notifications);
                break;
            case 4:
                imageView.setImageResource(R.drawable.ic_menu_star);
                break;
            default:
                imageView.setImageResource(android.R.drawable.ic_menu_delete);
                break;
        }
        // Change the icon for Windows and iPhone
        String s = values[position];
        if (s.startsWith("Windows7") || s.startsWith("iPhone")
                || s.startsWith("Solaris")) {
            imageView.setImageResource(R.drawable.no);
        } else {
            //imageView.setImageResource(R.drawable.yes);
        }

        return rowView;
    }
}