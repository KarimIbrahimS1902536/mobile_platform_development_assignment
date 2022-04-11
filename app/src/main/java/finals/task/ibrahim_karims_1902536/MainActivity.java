//ibrahim_karim_s1902536
package finals.task.ibrahim_karims_1902536;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import finals.task.ibrahim_karims_1902536.adapter.FragmentAdapter;
import finals.task.ibrahim_karims_1902536.databinding.ActivityMainBinding;
import finals.task.ibrahim_karims_1902536.fargments.PlannedWorksFragment;
import finals.task.ibrahim_karims_1902536.fargments.RoadWorksFragment;
import finals.task.ibrahim_karims_1902536.modelclasses.WorksModel;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private int mPosition = 0;
    private SearchView searchView;
    private ActivityMainBinding layoutBinding;
    private MenuItem ivSearch;
    private ArrayList<WorksModel> arrayList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        layoutBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(layoutBinding.getRoot());

        settingTabLayout();
        settingViewPager();

    }

    private void settingTabLayout() {
        layoutBinding.tabLayout.addTab(layoutBinding.tabLayout.newTab().setText("Current"));
        layoutBinding.tabLayout.addTab(layoutBinding.tabLayout.newTab().setText("Road Works"));
        layoutBinding.tabLayout.addTab(layoutBinding.tabLayout.newTab().setText("Planned"));
        layoutBinding.tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
    }

    private void settingViewPager() {
        final FragmentAdapter adapter = new FragmentAdapter(this, getSupportFragmentManager(),
                layoutBinding.tabLayout.getTabCount());
        layoutBinding.viewPager.setAdapter(adapter);

        layoutBinding.viewPager.addOnPageChangeListener(new
                TabLayout.TabLayoutOnPageChangeListener(layoutBinding.tabLayout));

        layoutBinding.tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                layoutBinding.viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);

        ivSearch = menu.findItem(R.id.action_search);
        searchView = (SearchView) ivSearch.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (mPosition == 1) {
                    //RoadWorksFragment.filter(newText);
                } else if (mPosition == 2) {
                    //PlannedWorksFragment.filter(newText);
                }
                return false;
            }
        });

        ivSearch.setOnActionExpandListener(new MenuItem.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                return true;
            }
        });


        return super.onCreateOptionsMenu(menu);
    }
}