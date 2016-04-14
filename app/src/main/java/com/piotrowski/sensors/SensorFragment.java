package com.piotrowski.sensors;

import android.app.Fragment;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.piotrowski.sensors.adapter.ItemVisibilityScrollListener;
import com.piotrowski.sensors.adapter.OnClickListener;
import com.piotrowski.sensors.adapter.RecyclerViewAdapter;
import com.piotrowski.sensors.items.SensorItem;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Observable;


public class SensorFragment extends Fragment implements OnClickListener, SensorEventListener, ItemVisibilityScrollListener.ItemVisibilityChange {
    private final String TAG = getClass().getName();
    private int RATE = 1000000;
    private SensorManager sensorManager;

    @Bind(R.id.list)
    RecyclerView list;
    private RecyclerViewAdapter listAdapter;

    public static SensorFragment newInstance() {
        SensorFragment fragment = new SensorFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ((MainActivity)getActivity()).setActivityTitle(getResources().getString(R.string.sensors_option));
        View view = inflater.inflate(R.layout.fragment_sensor, container, false);
        ButterKnife.bind(this, view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        list.setLayoutManager(layoutManager);
        sensorManager = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);
        List<Sensor> sensorList = sensorManager.getSensorList(Sensor.TYPE_ALL);
        ArrayList<Object> sensorsArray = new ArrayList<>();
        Observable.from(sensorList)
                .distinct(Sensor::getName)
                .map(SensorItem::new)
                .toList()
                .toBlocking()
                .subscribe(sensorsArray::addAll);
        listAdapter = new RecyclerViewAdapter(sensorsArray);
        list.setAdapter(listAdapter);
        listAdapter.setListener(this);
        list.addOnScrollListener(new ItemVisibilityScrollListener(layoutManager, this));

        return view;
    }

    @Override
    public void onItemClick(int i, RecyclerView.ViewHolder vh) {
        Object o = listAdapter.elems.get(i);
        if (o instanceof SensorItem) onItemClick(i, vh, (SensorItem) o);
    }


    private void onItemClick(int i, RecyclerView.ViewHolder vh, SensorItem item) {
        Observable.from(listAdapter.elems)
                .filter(o1 -> o1 instanceof SensorItem)
                .map(o2 -> (SensorItem) o2)
                .filter(sensorItem -> sensorItem.isRegistered() && !sensorItem.equals(item))
                .toBlocking()
                .subscribe(sensorItem1 -> {
                    sensorManager.unregisterListener(this, sensorItem1.getSensor());
                    Log.d(TAG, "Unregistered: " + sensorItem1.getSensor().getName());
                    sensorItem1.setRegistered(false);
                    int index = listAdapter.elems.indexOf(sensorItem1);
                    listAdapter.notifyItemChanged(index);
                });
        item.toggleRegistered();
        if (item.isRegistered()) {
            sensorManager.registerListener(this, item.getSensor(), RATE);
            Log.d(TAG, "Registered: " + item.getSensor().getName());
        } else {
            sensorManager.unregisterListener(this, item.getSensor());
            Log.d(TAG, "Unregistered: " + item.getSensor().getName());
        }
        listAdapter.notifyItemChanged(i);
    }

    @Override
    public boolean onItemLongClick(int i, RecyclerView.ViewHolder vh) {
        return false;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        int i = -1;
        for (Object o : listAdapter.elems) {
            i++;
            if (o instanceof SensorItem) {
                SensorItem item = (SensorItem) o;
                if (!event.sensor.getName().equals(item.getSensor().getName())) continue;
                item.setValues(event.values);
                listAdapter.notifyDataSetChanged();
                break;
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    public void onDestroy() {
        sensorManager.unregisterListener(this);
        super.onDestroy();
    }

    @Override
    public void onItemHidden(int position) {
        Log.d(TAG, "Item hidden: " + position);
        Object o = listAdapter.elems.get(position);
        if (o instanceof SensorItem) {
            SensorItem sensorItem = (SensorItem) o;
            sensorManager.unregisterListener(this, sensorItem.getSensor());
            Log.d(TAG, "Unregistered: " + sensorItem.getSensor().getName());
        }
    }

    @Override
    public void onItemShown(int position) {
        Log.d(TAG, "Item shown: " + position);
        Object o = listAdapter.elems.get(position);
        if (o instanceof SensorItem) {
            SensorItem sensorItem = (SensorItem) o;
            if (sensorItem.isRegistered()) {
                sensorManager.registerListener(this, sensorItem.getSensor(), RATE);
                Log.d(TAG, "Registered: " + sensorItem.getSensor().getName());
            }
        }
    }
}
