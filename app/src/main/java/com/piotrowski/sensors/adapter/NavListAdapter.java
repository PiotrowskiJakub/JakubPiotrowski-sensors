package com.piotrowski.sensors.adapter;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.view.View;
import android.widget.AdapterView;

import com.piotrowski.sensors.MainActivity;
import com.piotrowski.sensors.R;
import com.piotrowski.sensors.SensorFragment;
import com.piotrowski.sensors.items.IntentItem;

public class NavListAdapter implements AdapterView.OnItemClickListener {

    private Context context;

    public NavListAdapter(Context context) {
        this.context = context;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        FragmentManager fragmentManager = ((MainActivity) context).getFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.fragment_container, SensorFragment.newInstance());
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
