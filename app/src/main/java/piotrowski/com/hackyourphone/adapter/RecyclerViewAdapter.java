package piotrowski.com.hackyourphone.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.ViewGroup;


import java.util.List;

import piotrowski.com.hackyourphone.databinders.DataBinder;
import piotrowski.com.hackyourphone.databinders.SensorDataBinder;
import piotrowski.com.hackyourphone.databinders.SimpleCardDataBinder;
import piotrowski.com.hackyourphone.databinders.SimpleTextDataBinder;
import piotrowski.com.hackyourphone.items.IntentItem;
import piotrowski.com.hackyourphone.items.Section;
import piotrowski.com.hackyourphone.items.SensorItem;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private OnClickListener listener;
    protected SparseArray<DataBinder> mDataBinders=new SparseArray<>();
    public List<Object> elems;

    public RecyclerViewAdapter(List<Object> elems) {
        this.elems = elems;
        mDataBinders.put(1, new SensorDataBinder(this));
        mDataBinders.put(2, new SimpleTextDataBinder());
        mDataBinders.put(3, new SimpleCardDataBinder());
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        DataBinder dataBinder = mDataBinders.get(viewType);
        if(dataBinder==null) throw new IllegalStateException("No data binder for view type: "+viewType);
        RecyclerView.ViewHolder viewHolder = dataBinder.onCreateViewHolder(LayoutInflater.from(parent.getContext()), parent);
        viewHolder.itemView.setOnClickListener(v -> {
            int i = viewHolder.getAdapterPosition();
            if (i != -1 && listener != null) listener.onItemClick(i, viewHolder);
        });
        viewHolder.itemView.setOnLongClickListener(v -> {
            int i = viewHolder.getAdapterPosition();
            if(i!=-1 && listener!=null) return listener.onItemLongClick(i, viewHolder);
            else return false;
        });
        return viewHolder;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        int viewType = getItemViewType(position);
        DataBinder dataBinder = mDataBinders.get(viewType);
        if(dataBinder==null) throw new IllegalStateException("No data binder for view type: "+viewType);
        dataBinder.onBindViewHolder(elems.get(position), holder, position);
    }

    @Override
    public int getItemViewType(int position) {
        Object o = elems.get(position);
        if(o instanceof SensorItem) return 1;
        else if(o instanceof Section) return 2;
        else if(o instanceof IntentItem) return 3;
        else return super.getItemViewType(position);
    }

    @Override
    public int getItemCount() {
        return elems.size();
    }

    public void setListener(OnClickListener listener) {
        this.listener = listener;
    }
}
