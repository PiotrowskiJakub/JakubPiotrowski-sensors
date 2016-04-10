package piotrowski.com.hackyourphone.databinders;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import piotrowski.com.hackyourphone.R;
import piotrowski.com.hackyourphone.adapter.RecyclerViewAdapter;
import piotrowski.com.hackyourphone.items.SensorItem;
import piotrowski.com.hackyourphone.viewholders.ExpandableViewHolder;

public class SensorDataBinder implements DataBinder<SensorItem,ExpandableViewHolder> {

    private RecyclerViewAdapter mAdapter;

    public SensorDataBinder(RecyclerViewAdapter adapter) {
        mAdapter = adapter;
    }

    @Override
    public ExpandableViewHolder onCreateViewHolder(LayoutInflater inflater, ViewGroup parent) {
        ExpandableViewHolder holder = new ExpandableViewHolder(inflater.inflate(R.layout.expandable_item, parent, false));

        return holder;
    }

    @Override
    public void onBindViewHolder(SensorItem elem, ExpandableViewHolder holder, int position) {
        holder.text.setText(elem.getSensor().getName());
        holder.expandable.setExpanded(false, elem.isRegistered());
        if(elem.isRegistered()) {
            float[] values = elem.getValues();
            StringBuilder builder=new StringBuilder();
            for(int i=0; i<values.length; i++) {
                if(i!=values.length-1) builder.append(String.format("%.4f",values[i])+", ");
                else builder.append(values[i]);
            }
            holder.values.setText("Values: " + builder.toString());
        } else {
            holder.values.setText("");
        }
    }
}
