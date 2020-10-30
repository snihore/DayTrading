package com.daytrading;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private EditText investmentET, rptPercentageET, entryPriceET, exitPrice, stopLossET;
    private TextView output;
    private Button getBtn;

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

        //CLICK EVENTS
        getBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try{
                    RiskManagement riskManagement = new RiskManagement(
                            Long.valueOf(investmentET.getText().toString().trim()),
                            Double.valueOf(rptPercentageET.getText().toString().trim()),
                            Double.valueOf(entryPriceET.getText().toString().trim()),
                            Double.valueOf(exitPrice.getText().toString().trim()),
                            Double.valueOf(stopLossET.getText().toString().trim())
                    );

                    output.setText(riskManagement.toString());
                }catch (Exception e){
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }

            }
        });

    }
}