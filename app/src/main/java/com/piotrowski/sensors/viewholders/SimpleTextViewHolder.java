package com.piotrowski.sensors.viewholders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;

public class SimpleTextViewHolder extends RecyclerView.ViewHolder {
    @Bind(android.R.id.text1)
    public TextView text;


    public SimpleTextViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }
}
