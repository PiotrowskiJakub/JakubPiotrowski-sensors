package com.piotrowski.sensors.databinders;


import com.piotrowski.sensors.R;

public class SimpleCardDataBinder extends SimpleTextDataBinder {

    @Override
    protected int getLayoutResId() {
        return R.layout.simple_card_item;
    }
}
