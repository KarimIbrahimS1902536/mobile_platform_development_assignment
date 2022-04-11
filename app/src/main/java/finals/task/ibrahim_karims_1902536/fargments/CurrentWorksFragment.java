//ibrahim_karim_s1902536
package finals.task.ibrahim_karims_1902536.fargments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import finals.task.ibrahim_karims_1902536.R;
import finals.task.ibrahim_karims_1902536.api.FetchApis;
import finals.task.ibrahim_karims_1902536.api.RunnerClass;
import finals.task.ibrahim_karims_1902536.bottomsheet.BottomDialogFragment;
import finals.task.ibrahim_karims_1902536.databinding.FragmentCurrentBinding;
import finals.task.ibrahim_karims_1902536.modelclasses.WorksModel;

public class CurrentWorksFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {
    private FragmentCurrentBinding layoutBinding;
    private ArrayList<WorksModel> arrayList = new ArrayList<>();
    private GoogleMap map;
    private String mTitle, mDes, mLink, mDate;
    private Double mLat, mLong;
    private String flag = "current";
    private ArrayList<LatLng> locationArrayList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        layoutBinding = FragmentCurrentBinding.inflate(inflater, container, false);

        clickListener();
        fetchData();
        return layoutBinding.getRoot();
    }

    private void clickListener() {
        layoutBinding.ivRefresh.setOnClickListener(view -> {
            Toast.makeText(requireContext(), "Refreshed", Toast.LENGTH_SHORT).show();
            fetchData();
        });
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

    private void fetchData() {
        RunnerClass runnerClass1 = new RunnerClass();
        runnerClass1.executeAsync(new FetchApis("https://trafficscotland.org/rss/feeds/currentincidents.aspx"), (data) -> {
            layoutBinding.pbCurrent.setVisibility(View.GONE);
            if (data != null) {
                arrayList = data;
                //setAdapter(arrayList);

                for (int i = 0; i < arrayList.size(); i++) {
                    String mLatLong = arrayList.get(i).getLocPoints();
                    mLat = Double.parseDouble(mLatLong.split(" ")[0]);
                    mLong = Double.parseDouble(mLatLong.split(" ")[1]);
                    LatLng mlocation = new LatLng(mLat, mLong);
                    locationArrayList.add(mlocation);
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

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        map = googleMap;
        map.setOnMarkerClickListener(this);

        //LatLng latLng = new LatLng(-33.00, 72.34);
        try {
            for (int i = 0; i < locationArrayList.size(); i++) {
                map.addMarker(new MarkerOptions().position(locationArrayList.get(i))
                        .title(arrayList.get(i).getTitle())
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
                map.animateCamera(CameraUpdateFactory.zoomTo(15f));
                map.moveCamera(CameraUpdateFactory.newLatLng(locationArrayList.get(i)));
            }
        } catch (Exception ex){
            //body
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        map.clear();
    }

    @Override
    public boolean onMarkerClick(@NonNull Marker marker) {
        LatLng pos = marker.getPosition();
        String[] strSplit = pos.toString().split("\\(");
        String[] strSplitAgain = strSplit[1].split("\\)");
        String[] strSplitFinal = strSplitAgain[0].split(",");
        String lat = strSplitFinal[0];
        String lang = strSplitFinal[1];

        for (int i = 0; i < arrayList.size(); i++) {
            String mLatLong = arrayList.get(i).getLocPoints();
            String LatFromList = mLatLong.split(" ")[0];
            String LangFromList = mLatLong.split(" ")[1];
            if (lat.equals(LatFromList) && lang.equals(LangFromList)) {
                showBottomSheet(arrayList.get(i).getTitle(),
                        arrayList.get(i).getDescription(),
                        arrayList.get(i).getLink(),
                        arrayList.get(i).getPublishDate());
            }
        }
        return false;
    }
}