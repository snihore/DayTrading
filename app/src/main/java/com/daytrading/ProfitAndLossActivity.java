package com.daytrading;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ProfitAndLossActivity extends AppCompatActivity {

    private ImageView backBtn;
    private TextView monthShow, profitShow, lossShow, netShow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profit_and_loss);

        backBtn = (ImageView)findViewById(R.id.PndL_back_btn);
        monthShow = (TextView)findViewById(R.id.month_show);
        profitShow = (TextView)findViewById(R.id.profit_show);
        lossShow = (TextView)findViewById(R.id.loss_show);
        netShow = (TextView)findViewById(R.id.net_show);

        if(!getMonth().matches("")){
            monthShow.setText(getMonth());

            //Main Task
            ExcelHandle excelHandle = new ExcelHandle(getApplicationContext());
            List<PndLData> list = excelHandle.getPndLDataFromExcel(getMonth());

            if(list != null && list.size() > 0){
                profitShow.setText(String.valueOf(round(getTotalProfit(list), 2)));
                lossShow.setText(String.valueOf(round(getTotalLoss(list), 2)));
                netShow.setText(String.valueOf(round(getNetPndL(list), 2)));
                if(getNetPndL(list) > 0){
                    netShow.setTextColor(getApplicationContext().getResources().getColor(R.color.colorAccent));
                }else if(getNetPndL(list) < 0){
                    netShow.setTextColor(getApplicationContext().getResources().getColor(R.color.red));
                }

            }
        }else {
            monthShow.setText("---");
        }


        //Click Events
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }

    private double getNetPndL(List<PndLData> list) {

        double total = 0.0;

        for(int i=0; i<list.size(); i++){

            try{

                PndLData data = list.get(i);

                String type = data.getPndLType();
                double PndL = Double.parseDouble(data.getPndL());

                if(type.equals("Loss")){
                    total -= PndL;
                }else if(type.equals("Profit")){
                    total += PndL;
                }


            }catch (Exception e){
//                e.printStackTrace();
            }
        }

        return total;
    }

    private double getTotalLoss(List<PndLData> list) {

        double total = 0.0;

        for(int i=0; i<list.size(); i++){

            try{

                PndLData data = list.get(i);

                String type = data.getPndLType();
                double PndL = Double.parseDouble(data.getPndL());

                if(type.equals("Loss")){
                    total += PndL;
                }


            }catch (Exception e){
//                e.printStackTrace();
            }
        }

        return total;
    }

    private double getTotalProfit(List<PndLData> list) {

        double total = 0.0;

        for(int i=0; i<list.size(); i++){

            try{

                PndLData data = list.get(i);

                String type = data.getPndLType();
                double PndL = Double.parseDouble(data.getPndL());

                if(type.equals("Profit")){
                    total += PndL;
                }


            }catch (Exception e){
//                e.printStackTrace();
            }
        }

        return total;
    }

    private String getMonth(){

        try{

            DateFormat dateFormat = new SimpleDateFormat("MMM");
            Date date = new Date();
            Log.d("Month",dateFormat.format(date));

            return dateFormat.format(date);

        }catch (Exception e){
            return "";
        }
    }

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = BigDecimal.valueOf(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
}