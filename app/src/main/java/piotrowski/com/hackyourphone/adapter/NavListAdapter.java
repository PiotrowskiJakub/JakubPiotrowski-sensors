package piotrowski.com.hackyourphone.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import piotrowski.com.hackyourphone.MainActivity;
import piotrowski.com.hackyourphone.R;
import piotrowski.com.hackyourphone.items.IntentItem;

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
