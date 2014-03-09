package lbs.erasmus.touristanbul;

import android.app.ActionBar;
import android.app.Activity;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;

import lbs.erasmus.touristanbul.fragments.AttractionsFragment;
import lbs.erasmus.touristanbul.fragments.InformationFragment;
import lbs.erasmus.touristanbul.fragments.MapFragment;
import lbs.erasmus.touristanbul.fragments.ToolsFragment;

public class MainActivity extends Activity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {
    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // Update the main content by replacing fragments
        FragmentManager fragmentManager = getFragmentManager();

        //TODO Administrar bien los fragments, ya que así se creará uno nuevo cada vez [creo]
        switch (position + 1) {
            case 1:
                mTitle = getString(R.string.title_map);
                fragmentManager.beginTransaction()
                        .replace(R.id.container, new MapFragment())
                        .commit();
                break;
            case 2:
                mTitle = getString(R.string.title_attractions);
                fragmentManager.beginTransaction()
                        .replace(R.id.container, new AttractionsFragment())
                        .commit();
                break;
            case 3:
                mTitle = getString(R.string.title_tools);
                fragmentManager.beginTransaction()
                        .replace(R.id.container, new ToolsFragment())
                        .commit();
                break;
            case 4:
                mTitle = getString(R.string.title_information);
                fragmentManager.beginTransaction()
                        .replace(R.id.container, new InformationFragment())
                        .commit();
                break;
        }
    }

    public void restoreActionBar() {
        ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.main, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
