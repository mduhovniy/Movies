package info.duhovniy.maxim.movies.ui;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import info.duhovniy.maxim.movies.R;
import info.duhovniy.maxim.movies.db.DBConstants;
import info.duhovniy.maxim.movies.db.Movie;

import static info.duhovniy.maxim.movies.R.drawable.ic_menu;

public class MainActivity extends AppCompatActivity implements EditFragment.onEditMovie {

    private DrawerLayout drawerLayout;
    private ViewPager viewPager;
    private SharedPreferences mPref;
    private TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mPref = PreferenceManager.getDefaultSharedPreferences(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.mToolbar);
        setSupportActionBar(toolbar);

        final ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setHomeAsUpIndicator(ic_menu);
            ab.setDisplayHomeAsUpEnabled(true);
        }

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        NavigationView navView = (NavigationView) findViewById(R.id.navigation_view);
        if (navView != null) {

            setupDrawerContent(navView);
        }

        viewPager = (ViewPager) findViewById(R.id.tab_viewpager);
        if (viewPager != null) {
            setupViewPager(viewPager);
        }

        tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(viewPager);

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        handleSearchIntent(getIntent());

    }

    @Override
    protected void onNewIntent(Intent intent) {

        handleSearchIntent(intent);
    }

    private void handleSearchIntent(Intent intent) {

        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {

            tabLayout.setTabsFromPagerAdapter(viewPager.getAdapter());
            viewPager.setCurrentItem(0);

            SearchFragment sf = (SearchFragment) ((ViewPagerAdapter) viewPager.getAdapter())
                    .getItem(0);
            sf.setQuery(intent.getStringExtra(SearchManager.QUERY));
            sf.showSearch();
        }
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(new SearchFragment(), getString(R.string.search_header));
        adapter.addFrag(new EditFragment(), getString(R.string.edit_header));
        adapter.addFrag(new DBFragment(), getString(R.string.base_header));
        viewPager.setAdapter(adapter);
    }

    private void setupDrawerContent(NavigationView navigationView) {

        View header = navigationView.inflateHeaderView(R.layout.drawer_header);

        TextView user = (TextView) header.findViewById(R.id.user_name);
        TextView base = (TextView) header.findViewById(R.id.user_base);
        ImageView avatar = (ImageView) header.findViewById(R.id.avatar);

        avatar.setImageResource(R.drawable.web_hi_res_512);
        user.setText(mPref.getString(DBConstants.USER_NAME_MARKER, DBConstants.DEFAULT_USER_NAME));
        base.setText(mPref.getString(DBConstants.TABLE_NAME_MARKER, DBConstants.DEFAULT_TABLE_NAME));

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                menuItem.setChecked(true);

                switch (menuItem.getItemId()) {
                    case R.id.drawer_search:
                        viewPager.setCurrentItem(0);
                        break;
                    case R.id.drawer_edit:
                        viewPager.setCurrentItem(1);
                        break;
                    case R.id.drawer_base:
                        viewPager.setCurrentItem(2);
                        break;
                }

                drawerLayout.closeDrawers();
                return true;
            }
        });
    }

    @Override
    public void editMovie(Movie movie) {
        viewPager.setCurrentItem(1);

        EditFragment ef = (EditFragment) ((ViewPagerAdapter) viewPager.getAdapter()).getItem(1);
        ef.setOmdbID(movie.getOmdbId());
        ef.fillEditFields();
    }

    static class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFrag(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        // Associate searchable configuration with the SearchView
        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView =
                (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {
            case R.id.action_settings:
                Intent intent = new Intent(this, SettingsPreference.class);
                startActivity(intent);

                return true;
            case android.R.id.home:
                if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    drawerLayout.closeDrawer(GravityCompat.START);
                } else {
                    drawerLayout.openDrawer(GravityCompat.START);
                }

                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
