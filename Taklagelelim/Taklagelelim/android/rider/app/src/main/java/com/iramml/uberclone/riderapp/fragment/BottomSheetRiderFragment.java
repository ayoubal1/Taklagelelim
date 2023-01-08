package com.iramml.uberclone.riderapp.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.iramml.uberclone.riderapp.common.Common;
import com.iramml.uberclone.riderapp.R;
import com.iramml.uberclone.riderapp.common.ConfigApp;
import com.iramml.uberclone.riderapp.retrofit.IGoogleAPI;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BottomSheetRiderFragment extends BottomSheetDialogFragment {
    String mLocation, mDestination;
    boolean isTapOnMap;
    IGoogleAPI mService;
    TextView txtCalculate, txtLocation, txtDestination;

    public static BottomSheetRiderFragment newInstance(String location, String destination, boolean isTapOnMap,double markLat,double markLng,double currentLat,double currentLng){
        BottomSheetRiderFragment fragment=new BottomSheetRiderFragment();
        Bundle args=new Bundle();
        args.putString("location", location);
        args.putDouble("markLat", markLat);
        args.putDouble("markLng", markLng);
        args.putDouble("currentLat", currentLat);
        args.putDouble("currentLng", currentLng);
        args.putString("destination", destination);
        args.putBoolean("isTapOnMap", isTapOnMap);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        assert getArguments() != null;
        mLocation=getArguments().getString("location");
        mDestination=getArguments().getString("destination");
        isTapOnMap=getArguments().getBoolean("isTapOnMap");
    }

    @SuppressLint("SetTextI18n")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view0=inflater.inflate(R.layout.bottom_sheet_rider, container, false);
        txtLocation=(TextView)view0.findViewById(R.id.txtLocation);
        txtDestination=(TextView)view0.findViewById(R.id.txtDestination);
        txtCalculate=(TextView)view0.findViewById(R.id.txtCalculate);
        assert getArguments() != null;
        //txtDestination.setText( getArguments().getString("markLat")+" "+ getArguments().getString("markLng"));
        //txtLocation.setText( getArguments().getString("currentLat")+" "+ getArguments().getString("currentLng"));
        mService=Common.getGoogleService();
        getPrice(mLocation, mDestination);
        double distance= distFrom(getArguments().getDouble("markLat"),getArguments().getDouble("markLng"),getArguments().getDouble("currentLat"),getArguments().getDouble("currentLng"));
        distance*=5;
        int price = (int) distance;
        String euro = "\u20ac";
        txtCalculate.setText(price+" "+euro);
            //from place fragment
            txtLocation.setText(mLocation.toString());
            txtDestination.setText(mDestination.toString());


        return view0;
    }

    private void getPrice(String mLocation, String mDestination) {
        try {
            String requestUrl = "https://maps.googleapis.com/maps/api/directions/json?mode=driving&" +
                    "transit_routing_preference=less_driving&" +
                    "origin=" + mLocation + "&" +
                    "destination=" + mDestination + "&" +
                    "key=" + ConfigApp.GOOGLE_API_KEY;
            Log.d("LINK_ROUTES", requestUrl);
            mService.getPath(requestUrl).enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {

                    /*JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(response.body().toString());
                        JSONArray routes = jsonObject.getJSONArray("routes");

                        JSONObject object = routes.getJSONObject(0);
                        JSONArray legs = object.getJSONArray("legs");

                        JSONObject legsObject = legs.getJSONObject(0);

                        JSONObject distance = legsObject.getJSONObject("distance");
                        String distanceText = distance.getString("text");
                        double distanceValue = Double.parseDouble(distanceText.replaceAll("[^0-9\\\\.]", ""));

                        JSONObject time = legsObject.getJSONObject("duration");
                        String timeText = time.getString("text");
                        int timeValue = Integer.parseInt(timeText.replaceAll("\\D+", ""));

                        @SuppressLint("DefaultLocale") String finalCalculate = String.format("%s + %s = $%.2f", Common.getPrice(distanceValue, timeValue));
                        txtCalculate.setText(finalCalculate);


                            String startAddress = legsObject.getString("start_address");
                            String endAddress = legsObject.getString("end_address");

                            txtLocation.setText(startAddress);
                            txtDestination.setText(endAddress);


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }*/


                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    Log.d("ERROR", Objects.requireNonNull(t.getMessage()));
                }
            });
        } catch (Exception ex) {
            Toast.makeText(getContext(),"hataaaaaaa",Toast.LENGTH_SHORT).show();
            ex.printStackTrace();
        }
    }
    public static double distFrom(double lat1, double lng1, double lat2, double lng2) {
        double earthRadius = 6371.0 ; // miles (or 6371.0 kilometers) miles = 3958.75
        double dLat = Math.toRadians(lat2-lat1);
        double dLng = Math.toRadians(lng2-lng1);
        double sindLat = Math.sin(dLat / 2);
        double sindLng = Math.sin(dLng / 2);
        double a = Math.pow(sindLat, 2) + Math.pow(sindLng, 2)
                * Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2));
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        double dist = earthRadius * c;

        return dist;
    }
}
