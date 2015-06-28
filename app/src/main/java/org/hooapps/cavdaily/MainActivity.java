package org.hooapps.cavdaily;

import android.content.Context;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.hooapps.cavdaily.api.CavDailyFeedService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class MainActivity extends ActionBarActivity {

    DrawerLayout drawerLayout;
    NavigationView mNavigationView;
    private ActionBarDrawerToggle drawerToggle;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(savedInstanceState == null){
            // Set the fragment to initially load the news feed
            Bundle bundle = new Bundle();
            bundle.putString(CategoryFragment.ARG_CATEGORY, CavDailyFeedService.TOP);

            // Set the arguments for the ListFragment
            Fragment frag = new CategoryFragment();
            frag.setArguments(bundle);

            // Add the fragment to the container
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.add(R.id.container, frag);
            ft.commit();
        }

        // Load the views
        drawerLayout = (DrawerLayout) this.findViewById(R.id.drawer_layout);
        mNavigationView = (NavigationView) this.findViewById(R.id.navigation_view);
        mNavigationView.setNavigationItemSelectedListener(menuItem -> {
            Fragment frag;
            switch (menuItem.getItemId()) {
                case R.id.nav_top: frag = createListFragment(CavDailyFeedService.TOP);
                    break;
                case R.id.nav_news:frag = createListFragment(CavDailyFeedService.NEWS);
                    break;
                case R.id.nav_sports: frag = createListFragment(CavDailyFeedService.SPORTS);
                    break;
                case R.id.nav_opinion: frag = createListFragment(CavDailyFeedService.OPINION);
                    break;
                case R.id.nav_arts_and_ent: frag = createListFragment(CavDailyFeedService.AE);
                    break;
                case R.id.nav_life: frag = createListFragment(CavDailyFeedService.LIFE);
                    break;
                case R.id.nav_focus: frag = createListFragment(CavDailyFeedService.FOCUS);
                    break;
                case R.id.nav_health_and_sci: frag = createListFragment(CavDailyFeedService.HS);
                    break;
                case R.id.nav_multimedia: frag = createListFragment(CavDailyFeedService.MULTIMEDIA);
                    break;
                case R.id.nav_find_a_paper: frag = new PaperLocationFragment();
                    break;
                case R.id.nav_about: frag = new AboutFragment();
                    break;
                default: frag = createListFragment(CavDailyFeedService.TOP);
                    break;
            }
            menuItem.setChecked(true);
            swapListFragment(frag);
            toolbar.setTitle(menuItem.getTitle());
            drawerLayout.closeDrawers();
            return true;
        });
        toolbar = (Toolbar) this.findViewById(R.id.toolbar);

        if (toolbar != null) {
            setSupportActionBar(toolbar);
        }

        // Bind the DrawerToggle with the Drawer
        drawerToggle = new ActionBarDrawerToggle(
                this,
                drawerLayout,
                toolbar,
                R.string.drawer_open,
                R.string.drawer_closed
        ) {
        @Override
        public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }

        @Override
        public void onDrawerOpened(View drawerView) {
            super.onDrawerOpened(drawerView);
        }
        };
        drawerLayout.setDrawerListener(drawerToggle);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    private Fragment createListFragment(String category) {
        // Create a new ListFrag with the correct args
        Bundle bundle = new Bundle();
        bundle.putString(CategoryFragment.ARG_CATEGORY, category);
        Fragment frag = new CategoryFragment();
        frag.setArguments(bundle);
        return frag;
    }

    private Fragment createMediaListFragment(String category) {
        // Create a new MediaListFrag with the correct args
        Bundle bundle = new Bundle();
        bundle.putString(MediaListFragment.ARG_CATEGORY, category);
        Fragment frag = new MediaListFragment();
        frag.setArguments(bundle);
        return frag;
    }

    private void swapListFragment(Fragment frag) {
        // Swap the fragment
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.container, frag);
        ft.commit();
    }
}
