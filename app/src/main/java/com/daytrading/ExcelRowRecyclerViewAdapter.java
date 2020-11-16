package com.daytrading;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ExcelRowRecyclerViewAdapter extends RecyclerView.Adapter<ExcelRowRecyclerViewAdapter.ExcelRowViewHolder> {

    private Context context;
    private List<ExcelRowData> list;

    public ExcelRowRecyclerViewAdapter(Context context, List<ExcelRowData> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ExcelRowViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.excel_row_layout, parent, false);
        return new ExcelRowViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ExcelRowViewHolder holder, int position) {

        ExcelRowData data = list.get(position);

        holder.typeTV.setText(data.getType());
        holder.stockTV.setText(data.getStock());
        holder.entryTV.setText(data.getEntry());
        holder.exitTV.setText(data.getExit());
        holder.qtyTV.setText(data.getQty());
        holder.PndLTV.setText(data.getPndL());
        holder.PndLPerTV.setText("("+data.getPndLPer()+"%)");

        if(data.getType().toLowerCase().equals("short")){
            holder.typeTV.setBackground(context.getResources().getDrawable(R.drawable.red_solid));
        }else if(data.getType().toLowerCase().equals("long")){
            holder.typeTV.setBackground(context.getResources().getDrawable(R.drawable.green_solid));
        }

        if(data.getPndLType().toLowerCase().equals("profit")){
            String s = "+";
            holder.PndLTV.setText(s+data.getPndL());
            holder.PndLTV.setTextColor(context.getResources().getColor(R.color.colorAccent));
            holder.PndLPerTV.setTextColor(context.getResources().getColor(R.color.colorAccent));

        }else if(data.getPndLType().toLowerCase().equals("loss")){
            String s = "-";
            holder.PndLTV.setText(s+data.getPndL());
            holder.PndLTV.setTextColor(context.getResources().getColor(R.color.red));
            holder.PndLPerTV.setTextColor(context.getResources().getColor(R.color.red));
        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ExcelRowViewHolder extends RecyclerView.ViewHolder{

        TextView typeTV, stockTV, entryTV, exitTV, qtyTV, PndLTV, PndLPerTV;

        public ExcelRowViewHolder(@NonNull View itemView) {
            super(itemView);

            typeTV = (TextView)itemView.findViewById(R.id.excel_type);
            stockTV = (TextView)itemView.findViewById(R.id.excel_stock);
            entryTV = (TextView)itemView.findViewById(R.id.excel_entry);
            exitTV = (TextView)itemView.findViewById(R.id.excel_exit);
            qtyTV = (TextView)itemView.findViewById(R.id.excel_qty);
            PndLTV = (TextView)itemView.findViewById(R.id.excel_PndL);
            PndLPerTV = (TextView)itemView.findViewById(R.id.excel_PndLPer);
        }
    }
}
