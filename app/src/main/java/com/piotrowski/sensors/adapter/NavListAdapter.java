package com.piotrowski.sensors.adapter;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.view.View;
import android.widget.AdapterView;

import com.piotrowski.sensors.MainActivity;
import com.piotrowski.sensors.R;
import com.piotrowski.sensors.SensorFragment;
import com.piotrowski.sensors.TelephonyManagerFragment;

public class NavListAdapter implements AdapterView.OnItemClickListener {

    private Context context;

    public NavListAdapter(Context context) {
        this.context = context;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Fragment fragment = null;
        if(position == 0) {
            fragment = SensorFragment.newInstance();
        } else {
            fragment = TelephonyManagerFragment.newInstance();
        }

        FragmentManager fragmentManager = ((MainActivity) context).getFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
