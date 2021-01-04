package me.farazappy.expensetracker.helpers;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import me.farazappy.expensetracker.R;
import me.farazappy.expensetracker.models.Item;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemViewHolder> {

    public interface OnClickListener {
        void onClick(View view, Item item);
    }

    private Context context;
    private List<Item> items;
    private OnClickListener onClickListener;

    private static String TAG = DayAdapter.class.getSimpleName();



    public class ItemViewHolder extends RecyclerView.ViewHolder {

        TextView itemName, itemCost;
        ImageView overflowMenu;
        View itemView;

        public ItemViewHolder(View itemView) {
            super(itemView);
            this.itemName = itemView.findViewById(R.id.itemName);
            this.itemCost = itemView.findViewById(R.id.itemCost);
            this.overflowMenu = itemView.findViewById(R.id.overflowMenu);
            this.itemView = itemView;
        }
    }

    public ItemAdapter(Context context, List<Item> items, OnClickListener onClickListener) {
        this.context = context;
        this.items = items;
        this.onClickListener = onClickListener;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_item, viewGroup, false);

        return new ItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final ItemViewHolder dayViewHolder, int i) {
        final Item item = items.get(i);

        dayViewHolder.itemName.setText(item.getName());
        dayViewHolder.itemCost.setText("₹" + item.getCost());
        dayViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickListener.onClick(dayViewHolder.overflowMenu, item);
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

}
