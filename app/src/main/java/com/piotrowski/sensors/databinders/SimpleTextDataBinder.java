package com.piotrowski.sensors.databinders;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.piotrowski.sensors.items.Titled;
import com.piotrowski.sensors.viewholders.SimpleTextViewHolder;


public class SimpleTextDataBinder implements DataBinder<Titled, SimpleTextViewHolder> {

    @Override
    public SimpleTextViewHolder onCreateViewHolder(LayoutInflater inflater, ViewGroup parent) {
        View view = inflater.inflate(getLayoutResId(), parent, false);
        return new SimpleTextViewHolder(view);
    }

    @Override
    public void onBindViewHolder(Titled elem, SimpleTextViewHolder holder, int position) {
        holder.text.setText(elem.getTitle());
    }

    protected int getLayoutResId() {
        return android.R.layout.simple_list_item_1;
    }
}
