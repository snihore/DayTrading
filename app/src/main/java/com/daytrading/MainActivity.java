package com.daytrading;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private EditText investmentET, rptPercentageET, entryPriceET, exitPrice, stopLossET;
    private TextView output, changeBtn, saveBtn;
    private Button getBtn;
    private ImageView exitPercntageBtn, stopLossPercentageBtn, takeScreenshot, stockJournal;

    private RiskManagement riskManagement;
    private Map<String, String> excelEntryMap = new HashMap<>();

    public long getInvestment(){
        if(investmentET != null && !investmentET.getText().toString().trim().matches("")){
            try{

                return Long.valueOf(investmentET.getText().toString().trim());

            }catch (Exception e){
                e.printStackTrace();
                return 0;
            }
        }
        return 0;
    }

    public double getRptPercentage(){
        if(rptPercentageET != null && !rptPercentageET.getText().toString().trim().matches("")){
            try{

                return Double.valueOf(rptPercentageET.getText().toString().trim());

            }catch (Exception e){
                e.printStackTrace();
                return 0.0;
            }
        }
        return 0.0;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //INIT VIEWS
        investmentET = (EditText)findViewById(R.id.investment_et);
        rptPercentageET = (EditText)findViewById(R.id.rpt_per_et);
        entryPriceET = (EditText)findViewById(R.id.entry_price_et);
        exitPrice = (EditText)findViewById(R.id.exit_price_et);
        stopLossET = (EditText)findViewById(R.id.stop_loss_et);
        output = (TextView)findViewById(R.id.output);
        getBtn = (Button)findViewById(R.id.get_btn);
        changeBtn = (TextView)findViewById(R.id.change_btn);
        exitPercntageBtn = (ImageView)findViewById(R.id.exit_percentage_btn);
        stopLossPercentageBtn = (ImageView)findViewById(R.id.stop_loss_percentage_btn);
        takeScreenshot = (ImageView)findViewById(R.id.take_screenshot);
        stockJournal = (ImageView)findViewById(R.id.stock_journal);
        saveBtn = (TextView)findViewById(R.id.save_btn);

        changeBtn.setVisibility(View.INVISIBLE);

        //CLICK EVENTS
        takeScreenshot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!entryPriceET.getText().toString().trim().matches("") &&
                        !investmentET.getText().toString().trim().matches("") &&
                        !rptPercentageET.getText().toString().trim().matches("") &&
                        !exitPrice.getText().toString().trim().matches("") &&
                        !stopLossET.getText().toString().trim().matches("")){
                    Screenshot.takeScreenshot(MainActivity.this);
                }
            }
        });

        getBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try{
                    riskManagement = new RiskManagement(
                            Long.valueOf(investmentET.getText().toString().trim()),
                            Double.valueOf(rptPercentageET.getText().toString().trim()),
                            Double.valueOf(entryPriceET.getText().toString().trim()),
                            Double.valueOf(exitPrice.getText().toString().trim()),
                            Double.valueOf(stopLossET.getText().toString().trim())
                    );

                    output.setText(riskManagement.toString());

                    //Save Btn Enable
                    saveBtn.setVisibility(View.VISIBLE);
                    //Set Map
                    excelEntryMap.put("entry", entryPriceET.getText().toString().trim());
                    excelEntryMap.put("exit1", exitPrice.getText().toString().trim());
                    excelEntryMap.put("exit2", stopLossET.getText().toString().trim());
                    excelEntryMap.put("qty", String.valueOf(riskManagement.expectedQuantity()));
                    excelEntryMap.put("PndL1", String.valueOf(riskManagement.totalProfit()));
                    excelEntryMap.put("PndL2", String.valueOf(riskManagement.totalLoss()));
                    excelEntryMap.put("PndLPer1", String.valueOf(riskManagement.totalProfitPercentage()));
                    excelEntryMap.put("PndLPer2", String.valueOf(riskManagement.totalLossPercentage()));


                    openRecommendedDialog(riskManagement, Long.valueOf(investmentET.getText().toString().trim()));

                    changeBtn.setVisibility(View.VISIBLE);
                }catch (Exception e){
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }

            }
        });

        changeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialog();
            }
        });

        stopLossPercentageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!entryPriceET.getText().toString().trim().matches("") &&
                        !investmentET.getText().toString().trim().matches("") &&
                        !rptPercentageET.getText().toString().trim().matches("")){
                    BottomSheetDialog bottomSheet = new BottomSheetDialog(MainActivity.this, entryPriceET, stopLossET, "Stop Loss");
                    bottomSheet.show(getSupportFragmentManager(),
                            "PercentageBottomSheet");
                }


            }
        });
        exitPercntageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(entryPriceET != null && !entryPriceET.getText().toString().trim().matches("")){
                    BottomSheetDialog bottomSheet = new BottomSheetDialog(MainActivity.this, entryPriceET, exitPrice, "Exit Price");
                    bottomSheet.show(getSupportFragmentManager(),
                            "PercentageBottomSheet");
                }


            }
        });

        stockJournal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ExcelDataActivity.class);
                startActivity(intent);

            }
        });

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openExcelEntryDialog();
            }
        });

    }

    private void openRecommendedDialog(final RiskManagement riskManagement, long investment) {

        //before inflating the custom alert dialog layout, we will get the current activity viewgroup
        ViewGroup viewGroup = findViewById(android.R.id.content);

        //then we will inflate the custom alert dialog xml that we created
        View dialogView = LayoutInflater.from(MainActivity.this).inflate(R.layout.recommended_dialog_layout, viewGroup, false);

        ImageView closeBtn = (ImageView) dialogView.findViewById(R.id.recommended_close_btn);
        TextView quantityTV = (TextView)dialogView.findViewById(R.id.recommended_quantity);
        TextView capitalTV = (TextView)dialogView.findViewById(R.id.recommended_capital);
        Button setBtn = (Button)dialogView.findViewById(R.id.recommended_set_btn);

        //Now we need an AlertDialog.Builder object
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        //setting the view of the builder to our custom view that we already inflated
        builder.setView(dialogView);

        //finally creating the alert dialog and displaying it
        final AlertDialog alertDialog = builder.create();
        alertDialog.show();

        //SET VALUES

        //also check expected margin capital capital
        if(riskManagement.expectedCapitalWithMargin() > Long.valueOf(investmentET.getText().toString().trim())){

            setBtn.setVisibility(View.VISIBLE);

            final long quantity = riskManagement.expectedQuantity03(investment);

            long marginCapital = riskManagement.expectedCapitalWithMargin02(quantity);

            quantityTV.setText(String.valueOf(quantity));
            capitalTV.setText(String.valueOf(marginCapital));

            setBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    output.setText(riskManagement.toString02(quantity));
                    //Save Btn Enable
                    saveBtn.setVisibility(View.VISIBLE);
                    //Set Map
                    excelEntryMap.put("entry", entryPriceET.getText().toString().trim());
                    excelEntryMap.put("exit1", exitPrice.getText().toString().trim());
                    excelEntryMap.put("exit2", stopLossET.getText().toString().trim());
                    excelEntryMap.put("qty", String.valueOf(quantity));
                    excelEntryMap.put("PndL1", String.valueOf(riskManagement.totalProfit02(quantity)));
                    excelEntryMap.put("PndL2", String.valueOf(riskManagement.totalLoss02(quantity)));
                    excelEntryMap.put("PndLPer1", String.valueOf(riskManagement.totalProfitPercentage02(quantity)));
                    excelEntryMap.put("PndLPer2", String.valueOf(riskManagement.totalLossPercentage02(quantity)));

                    alertDialog.dismiss();

                }
            });

        }else{

            setBtn.setVisibility(View.INVISIBLE);

            long quantity = riskManagement.expectedQuantity();

            long marginCapital = riskManagement.expectedCapitalWithMargin();

            quantityTV.setText(String.valueOf(quantity));
            capitalTV.setText(String.valueOf(marginCapital));
        }


        //CLICK EVENTS
        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
    }

    private void openDialog() {

        //before inflating the custom alert dialog layout, we will get the current activity viewgroup
        ViewGroup viewGroup = findViewById(android.R.id.content);

        //then we will inflate the custom alert dialog xml that we created
        View dialogView = LayoutInflater.from(MainActivity.this).inflate(R.layout.change_dialog, viewGroup, false);

        //Title and URL
        final EditText quantity = (EditText)dialogView.findViewById(R.id.change_quantity);
        final EditText capital = (EditText)dialogView.findViewById(R.id.change_capital);
        AppCompatButton btn = (AppCompatButton)dialogView.findViewById(R.id.change_dialog_btn);

        //Now we need an AlertDialog.Builder object
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        //setting the view of the builder to our custom view that we already inflated
        builder.setView(dialogView);

        //finally creating the alert dialog and displaying it
        final AlertDialog alertDialog = builder.create();
        alertDialog.show();

        //CLICK EVENTS
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    if(riskManagement != null)

                        //QUANTITY
                        if(!quantity.getText().toString().trim().matches("")){
                            output.setText(
                                    riskManagement.toString02(Long.valueOf(quantity.getText().toString().trim()))

                            );
                            //Save Btn Enable
                            saveBtn.setVisibility(View.VISIBLE);
                            //Set Map
                            excelEntryMap.put("entry", entryPriceET.getText().toString().trim());
                            excelEntryMap.put("exit1", exitPrice.getText().toString().trim());
                            excelEntryMap.put("exit2", stopLossET.getText().toString().trim());
                            excelEntryMap.put("qty", String.valueOf(
                                    Long.valueOf(quantity.getText().toString().trim())
                            ));
                            excelEntryMap.put("PndL1", String.valueOf(riskManagement.totalProfit02(
                                    Long.valueOf(quantity.getText().toString().trim())
                            )));
                            excelEntryMap.put("PndL2", String.valueOf(riskManagement.totalLoss02(
                                    Long.valueOf(quantity.getText().toString().trim())
                            )));
                            excelEntryMap.put("PndLPer1", String.valueOf(riskManagement.totalProfitPercentage02(
                                    Long.valueOf(quantity.getText().toString().trim())
                            )));
                            excelEntryMap.put("PndLPer2", String.valueOf(riskManagement.totalLossPercentage02(
                                    Long.valueOf(quantity.getText().toString().trim())
                            )));

                        }

                        //CAPITAL
                        if(!capital.getText().toString().trim().matches("")){
                            output.setText(
                                    riskManagement.toString03(Long.valueOf(capital.getText().toString().trim()))
                            );
                            //Save Btn Enable
                            saveBtn.setVisibility(View.VISIBLE);
                            //Set Map
                            excelEntryMap.put("entry", entryPriceET.getText().toString().trim());
                            excelEntryMap.put("exit1", exitPrice.getText().toString().trim());
                            excelEntryMap.put("exit2", stopLossET.getText().toString().trim());
                            excelEntryMap.put("qty", String.valueOf(riskManagement.expectedQuantity03(
                                    Long.valueOf(capital.getText().toString().trim())
                            )));
                            excelEntryMap.put("PndL1", String.valueOf(riskManagement.totalProfit03(
                                    Long.valueOf(capital.getText().toString().trim())
                            )));
                            excelEntryMap.put("PndL2", String.valueOf(riskManagement.totalLoss03(
                                    Long.valueOf(capital.getText().toString().trim())
                            )));
                            excelEntryMap.put("PndLPer1", String.valueOf(riskManagement.totalProfitPercentage03(
                                    Long.valueOf(capital.getText().toString().trim())
                            )));
                            excelEntryMap.put("PndLPer2", String.valueOf(riskManagement.totalLossPercentage03(
                                    Long.valueOf(capital.getText().toString().trim())
                            )));
                        }

                    alertDialog.dismiss();

                }catch (Exception e){
                    e.printStackTrace();
                    Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void openExcelEntryDialog() {

        //before inflating the custom alert dialog layout, we will get the current activity viewgroup
        ViewGroup viewGroup = findViewById(android.R.id.content);

        //then we will inflate the custom alert dialog xml that we created
        View dialogView = LayoutInflater.from(MainActivity.this).inflate(R.layout.excel_entry_dialog, viewGroup, false);

        //Now we need an AlertDialog.Builder object
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        //setting the view of the builder to our custom view that we already inflated
        builder.setView(dialogView);

        //finally creating the alert dialog and displaying it
        final AlertDialog alertDialog = builder.create();
        alertDialog.show();

        //VIEWS
        final EditText editText = (EditText)dialogView.findViewById(R.id.excel_entry_stock_name);
        final TextView longType = (TextView)dialogView.findViewById(R.id.excel_entry_long_type);
        final TextView shortType = (TextView)dialogView.findViewById(R.id.excel_entry_short_type);
        final TextView profit = (TextView)dialogView.findViewById(R.id.excel_entry_profit);
        final TextView loss = (TextView)dialogView.findViewById(R.id.excel_entry_loss);
        Button saveBtn = (Button)dialogView.findViewById(R.id.excel_entry_save_btn);

        final int[] tradeType = {0};
        final int[] profitAndLoss = {0};

        //Toggle
        //1.
        longType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                longType.setBackground(getResources().getDrawable(R.drawable.green_solid));
                longType.setTextColor(Color.parseColor("#ffffff"));
                shortType.setBackground(getResources().getDrawable(R.drawable.red_border));
                shortType.setTextColor(Color.RED);
                tradeType[0] = 1;
            }
        });

        shortType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                longType.setBackground(getResources().getDrawable(R.drawable.green_border));
                longType.setTextColor(getResources().getColor(R.color.colorAccent));
                shortType.setBackground(getResources().getDrawable(R.drawable.red_solid));
                shortType.setTextColor(Color.parseColor("#ffffff"));
                tradeType[0] = 2;
            }
        });

        //2.
        profit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                profit.setBackground(getResources().getDrawable(R.drawable.green_solid));
                profit.setTextColor(Color.parseColor("#ffffff"));
                loss.setBackground(getResources().getDrawable(R.drawable.red_border));
                loss.setTextColor(Color.RED);
                profitAndLoss[0] = 1;
            }
        });

        loss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                profit.setBackground(getResources().getDrawable(R.drawable.green_border));
                profit.setTextColor(getResources().getColor(R.color.colorAccent));
                loss.setBackground(getResources().getDrawable(R.drawable.red_solid));
                loss.setTextColor(Color.parseColor("#ffffff"));
                profitAndLoss[0] = 2;
            }
        });


        //CLICK EVENTS
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String entry = "", exit = "", qty = "", PndL = "", PndLPer = "", PndLType = "", type = "", name = "";

                try{

                    if(tradeType[0] == 1 && profitAndLoss[0] == 1){
                        entry = excelEntryMap.get("entry");
                        exit = excelEntryMap.get("exit1");
                        qty = excelEntryMap.get("qty");
                        PndL = excelEntryMap.get("PndL1");
                        PndLPer = excelEntryMap.get("PndLPer1");
                        PndLType = "Profit";
                        type = "Long";
                        name = editText.getText().toString().trim();

                    }else if(tradeType[0] == 2 && profitAndLoss[0] == 1){
                        entry = excelEntryMap.get("entry");
                        exit = excelEntryMap.get("exit1");
                        qty = excelEntryMap.get("qty");
                        PndL = excelEntryMap.get("PndL1");
                        PndLPer = excelEntryMap.get("PndLPer1");
                        PndLType = "Profit";
                        type = "Short";
                        name = editText.getText().toString().trim();

                    }else if(tradeType[0] == 1 && profitAndLoss[0] == 2){
                        entry = excelEntryMap.get("entry");
                        exit = excelEntryMap.get("exit2");
                        qty = excelEntryMap.get("qty");
                        PndL = excelEntryMap.get("PndL2");
                        PndLPer = excelEntryMap.get("PndLPer2");
                        PndLType = "Loss";
                        type = "Long";
                        name = editText.getText().toString().trim();

                    }else if(tradeType[0] == 2 && profitAndLoss[0] == 2){
                        entry = excelEntryMap.get("entry");
                        exit = excelEntryMap.get("exit2");
                        qty = excelEntryMap.get("qty");
                        PndL = excelEntryMap.get("PndL2");
                        PndLPer = excelEntryMap.get("PndLPer2");
                        PndLType = "Loss";
                        type = "Short";
                        name = editText.getText().toString().trim();
                    }

                    if(!entry.matches("") &&
                            !exit.matches("") &&
                            !qty.matches("") &&
                            !PndL.matches("") &&
                            !PndLPer.matches("") &&
                            !PndLType.matches("") &&
                            !type.matches("") &&
                            !name.matches("")){

                        //Save
//                        Log.d(TAG, "EXCEL SAVE: "+entry+" "+exit+" "+qty+" "+PndL+" "+PndLPer+" "+PndLType+" "+type+" "+name);

                        ExcelHandle excelHandle = new ExcelHandle(getApplicationContext());
                        excelHandle.write(name, type, entry, exit, qty, PndL, PndLPer, PndLType);
                    }else {
                        Toast.makeText(MainActivity.this, "not saved", Toast.LENGTH_SHORT).show();
                    }

                }catch (Exception e){
                    e.printStackTrace();
                    Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }

                alertDialog.dismiss();

                //Set Map
                excelEntryMap.put("entry", "");
                excelEntryMap.put("exit1", "");
                excelEntryMap.put("exit2", "");
                excelEntryMap.put("qty", "");
                excelEntryMap.put("PndL1", "");
                excelEntryMap.put("PndL2", "");
                excelEntryMap.put("PndLPer1", "");
                excelEntryMap.put("PndLPer2", "");
            }
        });


    }

    private void filePermissions(){

        //check READ and WRITE Permissions
        int READ_PERMISSION = 0;
        int WRITE_PERNISSION = 0;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            READ_PERMISSION = checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE);
            WRITE_PERNISSION = checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            String[] strings = {
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
            };

            if(READ_PERMISSION != PackageManager.PERMISSION_GRANTED || WRITE_PERNISSION != PackageManager.PERMISSION_GRANTED){
                requestPermissions(strings, 1786);
            }else{
                //Access Storage

            }
        }else{
            //Access Storage

        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == 1786 && grantResults != null){

            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED
                && grantResults[1] == PackageManager.PERMISSION_GRANTED){
                //ACCESS STORAGE

            }

        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        filePermissions();
    }

    public void openScreenshot(File imageFile) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        Uri uri = Uri.fromFile(imageFile);
        intent.setDataAndType(uri, "image/*");
        startActivity(intent);
    }
}