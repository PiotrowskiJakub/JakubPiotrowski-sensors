package com.piotrowski.sensors.viewholders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;


import butterknife.Bind;
import butterknife.ButterKnife;
import com.piotrowski.sensors.R;
import com.piotrowski.sensors.items.ExpandableItem;

public class ExpandableViewHolder extends RecyclerView.ViewHolder {

    @Bind(android.R.id.text1)
    public TextView text;
    @Bind(R.id.expandable)
    public ExpandableItem expandable;
    @Bind(R.id.values)
    public TextView values;

    public ExpandableViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }
}
