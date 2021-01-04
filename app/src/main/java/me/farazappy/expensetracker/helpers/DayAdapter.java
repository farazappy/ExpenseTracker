package me.farazappy.expensetracker.helpers;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import me.farazappy.expensetracker.R;
import me.farazappy.expensetracker.models.Day;

public class DayAdapter extends RecyclerView.Adapter<DayAdapter.DayViewHolder> {

    public interface OnClickListener {
        void onClick(Day day);
    }

    private Context context;
    private List<Day> days;
    private OnClickListener onClickListener;

    private static String TAG = DayAdapter.class.getSimpleName();



    public class DayViewHolder extends RecyclerView.ViewHolder {

        TextView dayName;
        View itemView;

        public DayViewHolder(View itemView) {
            super(itemView);
            this.dayName = itemView.findViewById(R.id.dayName);
            this.itemView = itemView;
        }
    }

    public DayAdapter(Context context, List<Day> days, OnClickListener onClickListener) {
        this.context = context;
        this.days = days;
        this.onClickListener = onClickListener;
    }

    @NonNull
    @Override
    public DayViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.day_item, viewGroup, false);

        return new DayViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull DayViewHolder dayViewHolder, int i) {
        final Day day = days.get(i);

        dayViewHolder.dayName.setText(day.getName());
        dayViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickListener.onClick(day);
            }
        });
    }

    @Override
    public int getItemCount() {
        return days.size();
    }

}
