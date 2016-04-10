package piotrowski.com.hackyourphone.adapter;

import android.support.v7.widget.RecyclerView;

public interface OnClickListener {
    void onItemClick(int i, RecyclerView.ViewHolder view);
    boolean onItemLongClick(int i, RecyclerView.ViewHolder view);
}
