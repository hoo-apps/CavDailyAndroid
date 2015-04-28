package org.hooapps.cavdaily;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
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
    ListView navDrawer;
    private NavDrawerAdapter navDrawerAdapter;
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
        navDrawer = (ListView) this.findViewById(R.id.left_drawer);
        toolbar = (Toolbar) this.findViewById(R.id.toolbar);

        // Configure the adapter for the Nav Drawer
        ArrayList<String> navDrawerItems = new ArrayList<>(Arrays.asList(
                getResources().getStringArray(R.array.nav_drawer_items)
        ));
        navDrawerAdapter = new NavDrawerAdapter(this, navDrawerItems);
        navDrawer.setAdapter(navDrawerAdapter);
        navDrawer.setOnItemClickListener(new NavDrawerListener());

        if (toolbar != null) {
            toolbar.setTitle(navDrawerAdapter.getItem(0));
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

    private class NavDrawerListener implements  ListView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            Fragment frag;

            switch (position) {
                // Top Stories
                case 0: frag = createListFragment(CavDailyFeedService.TOP);
                    break;
                // News
                case 1: frag = createListFragment(CavDailyFeedService.NEWS);
                    break;
                // Sports
                case 2: frag = createListFragment(CavDailyFeedService.SPORTS);
                    break;
                // Opinion
                case 3: frag = createListFragment(CavDailyFeedService.OPINION);
                    break;
                // Life
                case 4: frag = createListFragment(CavDailyFeedService.AE);
                    break;
                // A&E
                case 5: frag = createListFragment(CavDailyFeedService.LIFE);
                    break;
                // Focus
                case 6: frag = createListFragment(CavDailyFeedService.FOCUS);
                    break;
                // H&S
                case 7: frag = createListFragment(CavDailyFeedService.HS);
                    break;
                // Multimedia
                case 8: frag = createMediaListFragment(CavDailyFeedService.MULTIMEDIA);
                    break;
                case 9: frag = new PaperLocationFragment();
                    break;
                default:
                    frag = createListFragment(CavDailyFeedService.NEWS);
                    break;
            }

            swapListFragment(frag);
            getSupportActionBar().setTitle(navDrawerAdapter.getItem(position));
            navDrawer.setItemChecked(position, true);
            drawerLayout.closeDrawers();
        }
    }

    private static class NavDrawerAdapter extends BaseAdapter {

        private Context context;
        private List<String> items;

        private NavDrawerAdapter(Context context, List<String> items) {
            this.context = context;
            this.items = items;
        }

        @Override
        public int getCount() {
            return items.size();
        }

        @Override
        public String getItem(int position) {
            return items.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(R.layout.view_nav_drawer_item, parent, false);
                convertView.setTag(new ViewHolder(convertView));
            }
            ViewHolder holder = (ViewHolder) convertView.getTag();
            String title = getItem(position);
            holder.title.setText(title);
            return convertView;
        }

        private static class ViewHolder {
            TextView title;

            public ViewHolder(View v) {
                title = (TextView) v.findViewById(R.id.title);
            }
        }
    }
}
