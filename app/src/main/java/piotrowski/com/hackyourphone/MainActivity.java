package piotrowski.com.hackyourphone;

import android.content.res.Configuration;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import piotrowski.com.hackyourphone.adapter.ItemVisibilityScrollListener;
import piotrowski.com.hackyourphone.adapter.NavListAdapter;
import piotrowski.com.hackyourphone.adapter.RecyclerViewAdapter;
import piotrowski.com.hackyourphone.adapter.OnClickListener;
import piotrowski.com.hackyourphone.items.SensorItem;
import rx.Observable;

public class MainActivity extends AppCompatActivity implements OnClickListener, SensorEventListener, ItemVisibilityScrollListener.ItemVisibilityChange {

    private final String TAG = getClass().getName();
    private int RATE = 1000000;
    private SensorManager sensorManager;

    @Bind(R.id.navList)
    ListView navList;
    private ArrayAdapter<String> navAdapter;

    @Bind(R.id.list)
    RecyclerView list;
    private RecyclerViewAdapter listAdapter;

    private ActionBarDrawerToggle mDrawerToggle;
    @Bind(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;
    private String mActivityTitle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        addDrawerItems();
        navList.setOnItemClickListener(new NavListAdapter(this));

        // set drawer toogle
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        mActivityTitle = getTitle().toString();
        setupDrawer();

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        list.setLayoutManager(layoutManager);
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
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

    private void addDrawerItems() {
        String[] menuArray = { "Sensors", "Telephony manager", "Localization and status" };
        navAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, menuArray);
        navList.setAdapter(navAdapter);
    }

    private void setupDrawer() {
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                R.string.drawer_open, R.string.drawer_close) {

            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getSupportActionBar().setTitle("Navigation");
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                getSupportActionBar().setTitle(mActivityTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

        };

        mDrawerToggle.setDrawerIndicatorEnabled(true);
        mDrawerLayout.setDrawerListener(mDrawerToggle);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPostCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onPostCreate(savedInstanceState, persistentState);
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }
}
