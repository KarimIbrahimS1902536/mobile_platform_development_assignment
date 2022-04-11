//ibrahim_karim_s1902536
package finals.task.ibrahim_karims_1902536.bottomsheet;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import finals.task.ibrahim_karims_1902536.R;
import finals.task.ibrahim_karims_1902536.databinding.FragmentBottomDialogBinding;

public class BottomDialogFragment extends BottomSheetDialogFragment {
    public static final String TAG = "ActionBottomDialog";
    private FragmentBottomDialogBinding layoutBinding;

    public static BottomDialogFragment newInstance() {
        return new BottomDialogFragment();
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        layoutBinding = FragmentBottomDialogBinding.inflate(inflater, container, false);

        String title = this.getArguments().getString("mtitle");
        String description = this.getArguments().getString("mDes");
        String link = this.getArguments().getString("mLink");

        layoutBinding.tvTitle.setText(title);
        layoutBinding.tvDescription.setText(description);
        layoutBinding.tvLink.setText(link);

        String startDate = null, endDate = null;
        if (description.contains("Start Date:")) {
            String[] startDateSplit, endDateSplit;
            String[] desDate;
            desDate = description.split("<br />");
            startDateSplit = desDate[0].split("Start Date:");
            startDateSplit = startDateSplit[1].split("-");
            endDateSplit = desDate[1].split("End Date:");
            endDateSplit = endDateSplit[1].split("-");
            startDate = startDateSplit[0];
            endDate = endDateSplit[0];

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(" EEEE, dd MMM yyyy ");
            try {
                Date date1 = simpleDateFormat.parse(startDate);
                Date date2 = simpleDateFormat.parse(endDate);

                long diff = dateDifference(date1, date2);
                if (diff == 0) {

                } else if (diff <= 3) {
                    layoutBinding.tvTitle.setBackgroundColor(getResources().getColor(R.color.orange));
                } else {
                    layoutBinding.tvTitle.setBackgroundColor(getResources().getColor(R.color.red));
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }

        }

        return layoutBinding.getRoot();
    }

    public long dateDifference(Date startDate, Date endDate) {
        long different = endDate.getTime() - startDate.getTime();
        long secondsInMilli = 1000;
        long minutesInMilli = secondsInMilli * 60;
        long hoursInMilli = minutesInMilli * 60;
        long daysInMilli = hoursInMilli * 24;
        long elapsedDays = different / daysInMilli;
        return elapsedDays;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}