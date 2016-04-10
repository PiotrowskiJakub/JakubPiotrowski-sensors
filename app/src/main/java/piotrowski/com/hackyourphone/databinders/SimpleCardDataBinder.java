package piotrowski.com.hackyourphone.databinders;


import piotrowski.com.hackyourphone.R;

public class SimpleCardDataBinder extends SimpleTextDataBinder {

    @Override
    protected int getLayoutResId() {
        return R.layout.simple_card_item;
    }
}
