package com.ebr163.support;

import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by mac1 on 25.10.16.
*/
public class ItemClickSupport {
    private final RecyclerView recyclerView;
    private ItemClickSupport.OnItemClickListener onItemClickListener;
    private ItemClickSupport.OnItemLongClickListener onItemLongClickListener;

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            if (ItemClickSupport.this.onItemClickListener != null) {
                RecyclerView.ViewHolder holder = ItemClickSupport.this.recyclerView.getChildViewHolder(v);
                ItemClickSupport.this.onItemClickListener.onItemClicked(ItemClickSupport.this.recyclerView, holder.getAdapterPosition(), v);
            }

        }
    };

    private View.OnLongClickListener onLongClickListener = new View.OnLongClickListener() {
        public boolean onLongClick(View v) {
            if (ItemClickSupport.this.onItemLongClickListener != null) {
                RecyclerView.ViewHolder holder = ItemClickSupport.this.recyclerView.getChildViewHolder(v);
                return ItemClickSupport.this.onItemLongClickListener.onItemLongClicked(ItemClickSupport.this.recyclerView, holder.getAdapterPosition(), v);
            } else {
                return false;
            }
        }
    };

    private RecyclerView.OnChildAttachStateChangeListener attachListener = new RecyclerView.OnChildAttachStateChangeListener() {
        public void onChildViewAttachedToWindow(View view) {
            if (ItemClickSupport.this.onItemClickListener != null) {
                view.setOnClickListener(ItemClickSupport.this.onClickListener);
            }

            if (ItemClickSupport.this.onItemLongClickListener != null) {
                view.setOnLongClickListener(ItemClickSupport.this.onLongClickListener);
            }

        }

        public void onChildViewDetachedFromWindow(View view) {
        }
    };

    private ItemClickSupport(RecyclerView recyclerView) {
        this.recyclerView = recyclerView;
        this.recyclerView.setTag(R.id.item_click_support, this);
        this.recyclerView.addOnChildAttachStateChangeListener(this.attachListener);
    }

    public static ItemClickSupport addTo(RecyclerView view) {
        ItemClickSupport support = (ItemClickSupport) view.getTag(R.id.item_click_support);
        if (support == null) {
            support = new ItemClickSupport(view);
        }

        return support;
    }

    public static ItemClickSupport removeFrom(RecyclerView view) {
        ItemClickSupport support = (ItemClickSupport) view.getTag(R.id.item_click_support);
        if (support != null) {
            support.detach(view);
        }

        return support;
    }

    public ItemClickSupport setOnItemClickListener(ItemClickSupport.OnItemClickListener listener) {
        this.onItemClickListener = listener;
        return this;
    }

    public ItemClickSupport setOnItemLongClickListener(ItemClickSupport.OnItemLongClickListener listener) {
        this.onItemLongClickListener = listener;
        return this;
    }

    private void detach(RecyclerView view) {
        view.removeOnChildAttachStateChangeListener(this.attachListener);
        view.setTag(R.id.item_click_support, null);
    }

    public interface OnItemLongClickListener {
        boolean onItemLongClicked(RecyclerView recyclerView, int position, View view);
    }

    public interface OnItemClickListener {
        void onItemClicked(RecyclerView recyclerView, int position, View view);
    }
}
