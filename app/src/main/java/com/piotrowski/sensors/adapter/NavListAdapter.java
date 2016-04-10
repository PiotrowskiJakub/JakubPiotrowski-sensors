package com.piotrowski.sensors.adapter;

import android.content.Context;
import android.view.View;
import android.widget.AdapterView;

import com.piotrowski.sensors.MainActivity;
import com.piotrowski.sensors.R;
import com.piotrowski.sensors.items.IntentItem;

public class NavListAdapter implements AdapterView.OnItemClickListener {

    private Context context;

    public NavListAdapter(Context context) {
        this.context = context;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        context.startActivity(IntentItem.builder()
                .context(context)
                .activityClass(MainActivity.class)
                .build().getIntent());
    }
}