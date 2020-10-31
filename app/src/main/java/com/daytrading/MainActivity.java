package com.daytrading;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.ContextCompat;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private EditText investmentET, rptPercentageET, entryPriceET, exitPrice, stopLossET;
    private TextView output, changeBtn;
    private Button getBtn;

    private RiskManagement riskManagement;

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

        changeBtn.setVisibility(View.INVISIBLE);

        //CLICK EVENTS
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
                        }

                        //CAPITAL
                        if(!capital.getText().toString().trim().matches("")){
                            output.setText(
                                    riskManagement.toString03(Long.valueOf(capital.getText().toString().trim()))
                            );
                        }

                    alertDialog.dismiss();

                }catch (Exception e){
                    e.printStackTrace();
                    Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}