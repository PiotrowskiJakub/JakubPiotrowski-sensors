package com.piotrowski.sensors;


import android.app.Fragment;
import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import android.location.LocationListener;

import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.Bind;
import butterknife.ButterKnife;

public class LocalizationFragment extends Fragment implements LocationListener {

    public static final long UPDATE_INTERVAL_IN_MILLISECONDS = 1000;
    private LocationManager locationManager;
    private double latitude, longitude;
    private Criteria criteria;
    private String bestProvider;
    private SimpleDateFormat dateFormat;

    @Bind(R.id.latitude_text)
    TextView mLatitudeTextView;
    @Bind(R.id.longitude_text)
    TextView mLongitudeTextView;
    @Bind(R.id.last_update_time_text)
    TextView mLastUpdateTimeTextView;

    private String mLatitudeLabel, mLongitudeLabel, mLastUpdateTimeLabel;

    protected String mLastUpdateTime;

    public static LocalizationFragment newInstance() {
        LocalizationFragment fragment = new LocalizationFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ((MainActivity) getActivity()).setActivityTitle(getResources().getString(R.string.localization_option));
        View view = inflater.inflate(R.layout.fragment_localization, container, false);
        ButterKnife.bind(this, view);

        mLatitudeLabel = getResources().getString(R.string.latitude_label);
        mLongitudeLabel = getResources().getString(R.string.longitude_label);
        mLastUpdateTimeLabel = getResources().getString(R.string.last_update_time_label);
        dateFormat = new SimpleDateFormat("HH:mm:ss");

        getLocation();

        return view;
    }

    protected void getLocation() {
            locationManager = (LocationManager)  this.getActivity().getSystemService(Context.LOCATION_SERVICE);
            criteria = new Criteria();
            bestProvider = String.valueOf(locationManager.getBestProvider(criteria, true)).toString();

            Location location = locationManager.getLastKnownLocation(bestProvider);
            if (location != null) {
                latitude = location.getLatitude();
                longitude = location.getLongitude();
                setUIFields();
                locationManager.requestLocationUpdates(bestProvider, UPDATE_INTERVAL_IN_MILLISECONDS, 0, this);
            }
    }

    @Override
    public void onLocationChanged(Location location) {
        latitude = location.getLatitude();
        longitude = location.getLongitude();
        setUIFields();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public void onStop() {
        super.onStop();
        locationManager.removeUpdates(this);
    }

    private void setUIFields() {
        mLatitudeTextView.setText(mLatitudeLabel + ": " + latitude);
        mLongitudeTextView.setText(mLongitudeLabel + ": " + longitude);
        mLastUpdateTimeTextView.setText(mLastUpdateTimeLabel + ": " + dateFormat.format(new Date()));
    }
}
