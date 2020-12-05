package com.daytrading;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.zip.Inflater;

public class GetQuantityShowItemAdapter extends RecyclerView.Adapter<GetQuantityShowItemAdapter.ViewHolder> {

    private Context context;
    private List<GetQuantityData> list;

    public GetQuantityShowItemAdapter(Context context, List<GetQuantityData> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.get_quantity_show_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.quantity.setText(list.get(position).getQuantity());
        holder.stopLoss.setText(list.get(position).getStopLoss());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView stopLoss, quantity;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            stopLoss = (TextView)itemView.findViewById(R.id.get_quantity_show_item_stoploss);
            quantity = (TextView)itemView.findViewById(R.id.get_quantity_show_item_quantity);
        }
    }
}
