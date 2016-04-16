package com.piotrowski.sensors;

import android.Manifest;
import android.app.Fragment;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.telephony.TelephonyManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;


public class TelephonyManagerFragment extends Fragment{

    @Bind(R.id.list)
    ListView list;
    private ArrayAdapter<String> listAdapter;

    public static TelephonyManagerFragment newInstance() {
        TelephonyManagerFragment fragment = new TelephonyManagerFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ((MainActivity)getActivity()).setActivityTitle(getResources().getString(R.string.tm_option));
        View view = inflater.inflate(R.layout.fragment_telephonymanager, container, false);
        ButterKnife.bind(this, view);

        listAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, getPhoneInfo());
        list.setAdapter(listAdapter);

        return view;
    }

    private List<String> getPhoneInfo() {
        List<String> tmInfoList = new ArrayList<>();
        final TelephonyManager tm=(TelephonyManager)getActivity().getSystemService(Context.TELEPHONY_SERVICE);
        tmInfoList.add("IMEI Number: " + tm.getDeviceId());
        tmInfoList.add("SubscriberID: " + tm.getDeviceId());
        tmInfoList.add("Sim Serial Number: " + tm.getSimSerialNumber());
        tmInfoList.add("Network Country ISO: " + tm.getNetworkCountryIso());
        tmInfoList.add("SIM Country ISO: " + tm.getSimCountryIso());
        tmInfoList.add("Software Version: " + tm.getDeviceSoftwareVersion());
        tmInfoList.add("Voice Mail Number: " + tm.getVoiceMailNumber());

        // Phone type
        String phoneType = null;
        switch (tm.getPhoneType())
        {
            case (TelephonyManager.PHONE_TYPE_CDMA):
                phoneType = "CDMA";
                break;
            case (TelephonyManager.PHONE_TYPE_GSM):
                phoneType = "GSM";
                break;
            case (TelephonyManager.PHONE_TYPE_NONE):
                phoneType = "NONE";
                break;
        }
        tmInfoList.add("Phone Network Type: " + phoneType);

        //getting information if phone is in roaming
        tmInfoList.add("In Roaming?: " +  tm.isNetworkRoaming());


        return tmInfoList;
    }
}
