package com.daytrading;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ExcelRowRecyclerViewAdapter extends RecyclerView.Adapter<ExcelRowRecyclerViewAdapter.ExcelRowViewHolder> {

    private Context context;
    private List<ExcelRowData> list;
    private ExcelRowClickListener excelRowClickListener;

    public ExcelRowRecyclerViewAdapter(Context context, List<ExcelRowData> list, ExcelRowClickListener excelRowClickListener) {
        this.context = context;
        this.list = list;
        this.excelRowClickListener = excelRowClickListener;
    }

    @NonNull
    @Override
    public ExcelRowViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.excel_row_layout, parent, false);
        return new ExcelRowViewHolder(view, excelRowClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ExcelRowViewHolder holder, int position) {

        ExcelRowData data = list.get(position);

        holder.typeTV.setText(data.getType());
        holder.stockTV.setText(data.getStock());
        holder.entryTV.setText(data.getEntry());
        holder.exitTV.setText(data.getExit());
        holder.qtyTV.setText(data.getQty());
        holder.PndLPerTV.setText("("+data.getPndLPer()+"%)");
        holder.date.setText(" "+data.getDate()+" ");

        Log.i("P&L without BR", data.getPndLwithoutBr());
        String PndLFinal = "";
        try{
            //Parse only, if Number is available
            Double.parseDouble(data.getPndLwithoutBr());
            //Than, set PndL without Br
            PndLFinal = data.getPndLwithoutBr();
        }catch (Exception e){
            //Otherwise, set PndL
            PndLFinal = data.getPndL();
        }


        if(data.getType().toLowerCase().equals("short")){
            holder.typeTV.setBackground(context.getResources().getDrawable(R.drawable.red_solid));
        }else if(data.getType().toLowerCase().equals("long")){
            holder.typeTV.setBackground(context.getResources().getDrawable(R.drawable.green_solid));
        }

        if(data.getPndLType().toLowerCase().equals("profit")){
            String s = "+";
            holder.PndLTV.setText(s+PndLFinal);
            holder.PndLTV.setTextColor(context.getResources().getColor(R.color.colorAccent));
            holder.PndLPerTV.setTextColor(context.getResources().getColor(R.color.colorAccent));

        }else if(data.getPndLType().toLowerCase().equals("loss")){
            String s = "-";
            holder.PndLTV.setText(s+PndLFinal);
            holder.PndLTV.setTextColor(context.getResources().getColor(R.color.red));
            holder.PndLPerTV.setTextColor(context.getResources().getColor(R.color.red));
        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ExcelRowViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView typeTV, stockTV, entryTV, exitTV, qtyTV, PndLTV, PndLPerTV, date;
        ImageView deleteBtn, editBtn;
        ExcelRowClickListener excelRowClickListener;

        public ExcelRowViewHolder(@NonNull View itemView, ExcelRowClickListener excelRowClickListener) {
            super(itemView);

            typeTV = (TextView)itemView.findViewById(R.id.excel_type);
            stockTV = (TextView)itemView.findViewById(R.id.excel_stock);
            entryTV = (TextView)itemView.findViewById(R.id.excel_entry);
            exitTV = (TextView)itemView.findViewById(R.id.excel_exit);
            qtyTV = (TextView)itemView.findViewById(R.id.excel_qty);
            PndLTV = (TextView)itemView.findViewById(R.id.excel_PndL);
            PndLPerTV = (TextView)itemView.findViewById(R.id.excel_PndLPer);
            date = (TextView)itemView.findViewById(R.id.excel_date);
            deleteBtn = (ImageView)itemView.findViewById(R.id.excel_delete_btn);
            editBtn = (ImageView)itemView.findViewById(R.id.excel_edit_btn);

            this.excelRowClickListener = excelRowClickListener;

            //Click Events
            deleteBtn.setOnClickListener(this);
            editBtn.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

            switch (v.getId()){

                case R.id.excel_delete_btn:
                    excelRowClickListener.onClick(deleteBtn, getAdapterPosition(), "DELETE");
                    break;

                case R.id.excel_edit_btn:
                    excelRowClickListener.onClick(editBtn, getAdapterPosition(), "EDIT");
                    break;
            }

        }
    }
}
