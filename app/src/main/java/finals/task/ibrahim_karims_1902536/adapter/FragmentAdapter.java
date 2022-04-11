//ibrahim_karim_s1902536
package finals.task.ibrahim_karims_1902536.adapter;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import finals.task.ibrahim_karims_1902536.fargments.CurrentWorksFragment;
import finals.task.ibrahim_karims_1902536.fargments.PlannedWorksFragment;
import finals.task.ibrahim_karims_1902536.fargments.RoadWorksFragment;

public class FragmentAdapter extends FragmentPagerAdapter {

    int totalTabs;

    public FragmentAdapter(Context context, FragmentManager fm, int totalTabs) {
        super(fm);
        this.totalTabs = totalTabs;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new CurrentWorksFragment();
            case 1:
                return new RoadWorksFragment();
            case 2:
                return new PlannedWorksFragment();
            default:
                return null;
        }
    }
    @Override
    public int getCount() {
        return totalTabs;
    }
}