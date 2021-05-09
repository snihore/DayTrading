package com.daytrading;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.poi.hssf.record.EscherAggregate;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;

public class FuturesAndOptionsActivity extends AppCompatActivity {

    private EditText investmentET, rptPercentageET, buyAtET, lotSizeET, requiredMarginTv;
    private TextView sellAtTv, stopLossAtTv, buyAtTag, rptPercentageTag;
    private ImageView backBtn, addLotSize, minusLotSize;

    private long investment;
    private double rptPercentage;
    private double buyAt;

    boolean lotSizeActive = false, buyAtActive = false, changeLotSize = false, marginChange = false;

    private double fixedLoss, fixedProfit, totalRequiredMargin, sellAt, sellAtPer, stopLossAt, stopLossAtPer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_futures_and_options);

        //Init Views
        backBtn = (ImageView)findViewById(R.id.fno_back_btn);
        investmentET = (EditText)findViewById(R.id.fno_investmentET);
        rptPercentageET = (EditText)findViewById(R.id.fno_rptPercentageET);
        lotSizeET = (EditText)findViewById(R.id.fno_lotSizeET);
        buyAtET = (EditText)findViewById(R.id.fno_buyAtET);
        sellAtTv = (TextView)findViewById(R.id.fno_sell_at);
        stopLossAtTv = (TextView)findViewById(R.id.fno_stop_loss_at);
        requiredMarginTv = (EditText) findViewById(R.id.fno_required_margin);
        buyAtTag = (TextView)findViewById(R.id.fno_buy_at_tag);
        rptPercentageTag = (TextView)findViewById(R.id.fno_rptPercentage_tag);
        addLotSize = (ImageView)findViewById(R.id.fno_add_lotsize);
        minusLotSize = (ImageView)findViewById(R.id.fno_minus_lotsize);


        try{

            int lotSize = Conf.getLotSize(getApplicationContext());

            Intent intent = getIntent();

            if(intent != null){

                buyAt = intent.getDoubleExtra("buyAt", 0.0);

                rptPercentage = intent.getDoubleExtra("rptPercentage", 0.0);

                investment = intent.getLongExtra("investment", 0);

                buyAtTag.setText("Buy At (at Max. "+(investment/lotSize)+")");

                calculationHandle(lotSize, investment, rptPercentage, buyAt);

                rptPercentageTag.setText("RPT ("+fixedLoss+" L, "+fixedProfit+" P)");
            }

        }catch (Exception e){
            e.printStackTrace();
        }

        //Click Events
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        lotSizeET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                try{

                    String str = s.toString();
                    String str02 = buyAtET.getText().toString().trim();

                    if(!str.matches("") && !str02.matches("")){
                        int newLotSize = Integer.parseInt(str);

                        double newBuyAt = Double.parseDouble(str02);

                        lotSizeActive = true;
                        calculationHandle(newLotSize, investment, rptPercentage, newBuyAt);
                        lotSizeActive = false;

                        if(!changeLotSize){
                            Conf.setLotSize(getApplicationContext(), newLotSize);
                        }else{
                            changeLotSize = false;
                        }
                        buyAtTag.setText("Buy At (at Max. "+(investment/newLotSize)+")");

                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        buyAtET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                try{

                    String str = s.toString();
                    String str02 = lotSizeET.getText().toString().trim();

                    if(!str.matches("") && !str02.matches("")){
                        int newLotSize = Integer.parseInt(str02);

                        double newBuyAt = Double.parseDouble(str);

                        buyAtActive = true;
                        calculationHandle(newLotSize, investment, rptPercentage, newBuyAt);
                        buyAtActive = false;

                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        requiredMarginTv.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //Given: Margin Required
                try{
                    if(!buyAtActive){
                        double margin = Double.parseDouble(s.toString());

                        double buyAt = margin/Integer.parseInt(lotSizeET.getText().toString().trim());

                        buyAt = round(buyAt, 2);

                        marginChange = true;
                        buyAtET.setText(String.valueOf(buyAt));
                        marginChange = false;
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        addLotSize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Add Multiple Lot Size ...
                try{

                    int ls = Integer.parseInt(lotSizeET.getText().toString().trim());

                    int baseLotSize = Conf.getLotSize(getApplicationContext());
                    ls += baseLotSize;

                    changeLotSize = true;
                    lotSizeET.setText(String.valueOf(ls));

                }catch (Exception e){}
            }
        });

        minusLotSize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Minus Multiple Lot Size ...
                try{
                    int ls = Integer.parseInt(lotSizeET.getText().toString().trim());

                    int baseLotSize = Conf.getLotSize(getApplicationContext());

                    if(ls > baseLotSize){

                        ls -= baseLotSize;

                        changeLotSize = true;
                        lotSizeET.setText(String.valueOf(ls));
                    }


                }catch (Exception e){}
            }
        });

    }

    private void calculationHandle(int ls, long i, double rptp, double bat) {

        try{
//            Log.i("ERROR", "0");
            if(bat != 0.0 && rptp != 0.0 && i != 0 && ls != 0){

//                Log.i("ERROR", "1");
                OptionsHandle optionsHandle = new OptionsHandle(i, rptp, ls, bat);
//                Log.i("ERROR", "2");
                Map<String, Double> map = optionsHandle.calculte();
//                Log.i("ERROR", "3");
                if(map != null){
//                    Log.i("ERROR", "4");
                    fixedLoss = map.get("fixedLoss");
                    fixedProfit = map.get("fixedProfit");
                    totalRequiredMargin = map.get("totalRequiredCapital");
                    sellAt = map.get("sellAt");
                    sellAtPer = map.get("sellAtPer");
                    stopLossAt = map.get("stopLossAt");
                    stopLossAtPer = map.get("stopLossAtPer");
//                    Log.i("ERROR", "5");
                    //Set Views ...
                    investmentET.setText(String.valueOf(i));
                    rptPercentageET.setText(String.valueOf(rptp));

//                    Log.i("ERROR", "5_5");
//                    Log.i("ERROR", "LotSizeActive "+lotSizeActive);
                    if(!lotSizeActive){
                        lotSizeET.setText(String.valueOf(ls));
                    }
//                    Log.i("ERROR", "5_5_5");
//                    Log.i("ERROR", "BuyAtActive "+buyAtActive);
                    if(!buyAtActive){
                        buyAtET.setText(String.valueOf(bat));
                    }
//                    Log.i("ERROR", "6");
//                    Log.i("ERROR", "MarginChange "+marginChange);
                    if(!marginChange){
                        requiredMarginTv.setText(String.valueOf(totalRequiredMargin));
                    }
//                    Log.i("ERROR", "6_6");

                    sellAtTv.setText(String.valueOf(sellAt+" ("+sellAtPer+"%)"));
                    stopLossAtTv.setText(String.valueOf(stopLossAt+" ("+stopLossAtPer+"%)"));
//                    Log.i("ERROR", "7");
                }
//                Log.i("ERROR", "8");
            }
        }catch (Exception e){
//            Log.i("ERROR", "9");
            e.printStackTrace();
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
//            Log.i("ERROR", "10");
        }
    }

    private static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = BigDecimal.valueOf(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

}