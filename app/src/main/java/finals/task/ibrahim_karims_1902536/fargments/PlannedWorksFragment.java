//ibrahim_karim_s1902536
package finals.task.ibrahim_karims_1902536.fargments;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import finals.task.ibrahim_karims_1902536.R;
import finals.task.ibrahim_karims_1902536.api.FetchApis;
import finals.task.ibrahim_karims_1902536.api.RunnerClass;
import finals.task.ibrahim_karims_1902536.bottomsheet.BottomDialogFragment;
import finals.task.ibrahim_karims_1902536.databinding.FragmentPlannedBinding;
import finals.task.ibrahim_karims_1902536.modelclasses.WorksModel;

public class PlannedWorksFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {
    private FragmentPlannedBinding layoutBinding;
    private static ArrayList<WorksModel> arrayListPlanned = new ArrayList<>();
    //private static IncidentsMainAdapter incidentsAdapter1;
    private String[] desDate;
    private String[] startDateSplit, endDateSplit;
    private String startDate, endDate;
    private GoogleMap map;
    public Double mLat, mLong;
    private String flag = "current";
    private boolean searchFlag = false;
    ArrayList<WorksModel> filteredList;
    private ArrayList<LatLng> locationArrayListPlanned = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        layoutBinding = FragmentPlannedBinding.inflate(inflater, container, false);

        clickListener();
        fetchData();
        editTextListener();
        return layoutBinding.getRoot();
    }

    private void clickListener() {
        layoutBinding.ivRefresh.setOnClickListener(view -> {
            Toast.makeText(requireContext(), "Refreshed", Toast.LENGTH_SHORT).show();
            fetchData();
        });
        layoutBinding.tvPlanJourney.setOnClickListener(v -> {
            try {
                showDateDialog(requireActivity());
            } catch (ParseException e) {
                e.printStackTrace();
            }
        });

    }

    private void editTextListener() {
        layoutBinding.etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence query, int start, int before, int count) {
                filter(query.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void filter(String text) {
        ArrayList<WorksModel> filteredList = new ArrayList<>();

        for (WorksModel item : arrayListPlanned) {
            if (item.getTitle() != null) {
                if (item.getTitle().toLowerCase().contains(text.toLowerCase())) {
                    filteredList.add(item);
                }
            }
        }

        //incidentsAdapter.filterList(filteredList);
        locationArrayListPlanned.clear();
        for (int i = 0; i < filteredList.size(); i++) {
            String mLatLong = filteredList.get(i).getLocPoints();
            mLat = Double.parseDouble(mLatLong.split(" ")[0]);
            mLong = Double.parseDouble(mLatLong.split(" ")[1]);
            LatLng mlocation = new LatLng(mLat, mLong);
            locationArrayListPlanned.add(mlocation);
        }

        map.clear();
        SupportMapFragment mFrag = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
        assert mFrag != null;
        mFrag.getMapAsync(this);
    }

    private void fetchData() {
        RunnerClass runnerClass2 = new RunnerClass();
        runnerClass2.executeAsync(new FetchApis("https://trafficscotland.org/rss/feeds/plannedroadworks.aspx"), (data) -> {

            if (data != null) {
                arrayListPlanned = data;

                for (int i = 0; i < arrayListPlanned.size(); i++) {
                    String mLatLong = arrayListPlanned.get(i).getLocPoints();
                    mLat = Double.parseDouble(mLatLong.split(" ")[0]);
                    mLong = Double.parseDouble(mLatLong.split(" ")[1]);
                    LatLng mlocation = new LatLng(mLat, mLong);
                    locationArrayListPlanned.add(mlocation);
                }

                SupportMapFragment mFrag = (SupportMapFragment) getChildFragmentManager()
                        .findFragmentById(R.id.map);
                assert mFrag != null;
                mFrag.getMapAsync(this);
            }else {
                Toast.makeText(requireContext(), "No Internet", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void showDateDialog(Activity activity) throws ParseException {
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.getWindow().setGravity(Gravity.CENTER);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.date_dialog);

        TextView tvSubmit = dialog.findViewById(R.id.tv_submit);
        TextView tvCancel = dialog.findViewById(R.id.tv_cancel);
        ImageView ivCancel = dialog.findViewById(R.id.iv_cancel);
        DatePicker datePicker = dialog.findViewById(R.id.date_picker);


        tvSubmit.setOnClickListener(view -> {
            searchFlag = true;
            int day = datePicker.getDayOfMonth();
            int month = (datePicker.getMonth() + 1);
            int year = datePicker.getYear();

            //tvDate.setText(year + "-" + month + "-" + day);
            Toast.makeText(activity, "" + year + "-" + month + "-" + day, Toast.LENGTH_SHORT).show();

            String dateselect = +year + "-" + month + "-" + day;
            String inputFormat = "yyyy-MM-dd";
            String OutPutFormat = "EEEE, dd MMM yyyy";
            String formats1 = null;
            try {
                SimpleDateFormat formatter5 = new SimpleDateFormat("yyyy-MM-dd");

                long startDateSelectLong, endDateSelectLong;
                Date dateStart = formatter5.parse(dateselect);
                Date dateEnd = formatter5.parse("2022-3-28");
                startDateSelectLong = dateStart.getTime();
                endDateSelectLong = dateEnd.getTime();

                getListBetweenTwoDates(startDateSelectLong, endDateSelectLong);
            } catch (ParseException e) {
                e.printStackTrace();
            }


            dialog.dismiss();
        });

        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        ivCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void getListBetweenTwoDates(long DatePassed, long endDatePassed) {
        filteredList = new ArrayList<>();
        for (int i = 0; i < arrayListPlanned.size(); i++) {
            desDate = arrayListPlanned.get(i).getDescription().split("<br />");
            startDateSplit = desDate[0].split("Start Date:");
            startDateSplit = startDateSplit[1].split("-");

            endDateSplit = desDate[1].split("End Date:");
            endDateSplit = endDateSplit[1].split("-");

            startDate = startDateSplit[0];
            endDate = endDateSplit[0];

            try {
                //SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                //Date dateStart = sdf.parse(startDate);
                //Date dateEnd = sdf.parse(endDate);

                SimpleDateFormat inputFormat = new SimpleDateFormat(" EEEE, d MMM yyyy ");
                SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd");

                Date dateStart = inputFormat.parse(startDate);
                startDate = outputFormat.format(dateStart);

                Date dateEnd = inputFormat.parse(endDate);
                endDate = outputFormat.format(dateEnd);

                long startDateLong = dateStart.getTime();
                long endDateLong = dateEnd.getTime();

                if (DatePassed >= startDateLong && DatePassed <= endDateLong) {
                    filteredList.add(arrayListPlanned.get(i));
                }

            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        //incidentsAdapter1.filterList(filteredList);
        locationArrayListPlanned.clear();
        for (int i = 0; i < filteredList.size(); i++) {
            String mLatLong = filteredList.get(i).getLocPoints();
            mLat = Double.parseDouble(mLatLong.split(" ")[0]);
            mLong = Double.parseDouble(mLatLong.split(" ")[1]);
            LatLng mlocation = new LatLng(mLat, mLong);
            locationArrayListPlanned.add(mlocation);
        }

        map.clear();
        SupportMapFragment mFrag = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
        assert mFrag != null;
        mFrag.getMapAsync(this);
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        map = googleMap;
        map.setOnMarkerClickListener(this);

        try {
            for (int i = 0; i < locationArrayListPlanned.size(); i++) {
                // below line is use to add marker to each location of our array list.
                if (searchFlag) {
                    map.addMarker(new MarkerOptions().position(locationArrayListPlanned.get(i))
                            .title(filteredList.get(i).getTitle())
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
                } else {
                    map.addMarker(new MarkerOptions().position(locationArrayListPlanned.get(i))
                            .title(arrayListPlanned.get(i).getTitle())
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
                }
                // below lin is use to zoom our camera on map.
                map.animateCamera(CameraUpdateFactory.zoomTo(15f));
                // below line is use to move our camera to the specific location.
                map.moveCamera(CameraUpdateFactory.newLatLng(locationArrayListPlanned.get(i)));
            }
        } catch (Exception ex) {
            //body
        }
        layoutBinding.pbCurrentPlanned.setVisibility(View.GONE);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        map.clear();
    }

    public void showBottomSheet(String title, String des, String link, String date) {
        Bundle bundle = new Bundle();
        bundle.putString("mDate", date);
        bundle.putString("mtitle", title);
        bundle.putString("mDes", des);
        bundle.putString("mLink", link);
        BottomDialogFragment bottomDialogFragment = BottomDialogFragment.newInstance();
        bottomDialogFragment.show(getChildFragmentManager(), BottomDialogFragment.TAG);
        bottomDialogFragment.setArguments(bundle);
    }

    @Override
    public boolean onMarkerClick(@NonNull Marker marker) {
        LatLng pos = marker.getPosition();
        String[] strSplit = pos.toString().split("\\(");
        String[] strSplitAgain = strSplit[1].split("\\)");
        String[] strSplitFinal = strSplitAgain[0].split(",");
        String lat = strSplitFinal[0];
        String lang = strSplitFinal[1];

        for (int i = 0; i < arrayListPlanned.size(); i++) {
            String mLatLong = arrayListPlanned.get(i).getLocPoints();
            String LatFromList = mLatLong.split(" ")[0];
            String LangFromList = mLatLong.split(" ")[1];
            if (lat.equals(LatFromList) && lang.equals(LangFromList)) {
                showBottomSheet(arrayListPlanned.get(i).getTitle(),
                        arrayListPlanned.get(i).getDescription(),
                        arrayListPlanned.get(i).getLink(),
                        arrayListPlanned.get(i).getPublishDate());
            }
        }
        return false;
    }
}