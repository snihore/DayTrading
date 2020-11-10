package com.daytrading;

import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import org.w3c.dom.Text;

public class BottomSheetDialog extends BottomSheetDialogFragment {

    private EditText entry;
    private EditText exitOrSL;
    private String label;

    public BottomSheetDialog(EditText entry, EditText exitOrSL, String label) {
        this.entry = entry;
        this.exitOrSL = exitOrSL;
        this.label = label;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable
            ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.percentage_layout,
                container, false);

        SeekBar seekBar = v.findViewById(R.id.percentage_seekbar);
        Button doneBtn = (Button)v.findViewById(R.id.done_btn_bs);
        final EditText percentageEditText = (EditText)v.findViewById(R.id.percentage_edit_text_bs);
        final TextView changePoint = (TextView) v.findViewById(R.id.point_change_bs);
        TextView entryPrice = (TextView)v.findViewById(R.id.entry_price_bs);
        final TextView exitOrStopLoss = (TextView) v.findViewById(R.id.exit_price_bs);
        final TextView buyTag = (TextView)v.findViewById(R.id.buy_tag_btn_bs);
        final TextView sellTag = (TextView)v.findViewById(R.id.sell_tag_btn_bs);

        final String[] tradeType = {""};
        final double[] res = {0.0};

        try{

            if(entry != null){
                if(entry.getText().toString().trim().matches("")){
                    entryPrice.setText(
                            "Entry Price\nRs. 0.0"
                    );
                }else{
                    entryPrice.setText(
                            "Entry Price\nRs. "+entry.getText().toString().trim()
                    );
                }
            }

            if(exitOrSL != null && label != null && !label.matches("")){
                if(exitOrSL.getText().toString().trim().matches("")){
                    exitOrStopLoss.setText(
                            label+"\nRs. 0.0"
                    );
                }else{
                    exitOrStopLoss.setText(
                            label+"\nRs. "+exitOrSL.getText().toString().trim()
                    );
                }
            }

            buyTag.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    buyTag.setBackground(getResources().getDrawable(R.drawable.green_solid));
                    buyTag.setTextColor(Color.parseColor("#ffffff"));
                    sellTag.setBackground(getResources().getDrawable(R.drawable.red_border));
                    sellTag.setTextColor(Color.RED);
                    tradeType[0] = "BUY";
                }
            });

            sellTag.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    buyTag.setBackground(getResources().getDrawable(R.drawable.green_border));
                    buyTag.setTextColor(getResources().getColor(R.color.colorAccent));
                    sellTag.setBackground(getResources().getDrawable(R.drawable.red_solid));
                    sellTag.setTextColor(Color.parseColor("#ffffff"));
                    tradeType[0] = "SELL";
                }
            });


            seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    double value = ((double) progress / 10.0);
//                    Log.d("TAG", "onProgressChanged: "+value);

                    percentageEditText.setText(String.valueOf(value));

                    if(entry != null && !entry.getText().toString().trim().matches("") &&
                        !percentageEditText.getText().toString().trim().matches("")){

                        PercentageCalc calc = new PercentageCalc(
                                Double.parseDouble(entry.getText().toString().trim()),
                                Double.parseDouble(percentageEditText.getText().toString().trim()),
                                tradeType[0]);

                        changePoint.setText(String.valueOf(calc.getPointChange()));

                        if(label != null && label.equals("Exit Price")){

                            exitOrStopLoss.setText(
                                    label+"\nRs. "+calc.getExit()
                            );

                            res[0] = calc.getExit();


                        }else if(label != null && label.equals("Stop Loss")){

                            exitOrStopLoss.setText(
                                    label+"\nRs. "+calc.getStopLoss()
                            );

                            res[0] = calc.getStopLoss();
                        }
                    }

                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {}
                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {}
            });

            percentageEditText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                    if(entry != null && !entry.getText().toString().trim().matches("") &&
                            !s.toString().matches("")){

                        PercentageCalc calc = new PercentageCalc(
                                Double.parseDouble(entry.getText().toString().trim()),
                                Double.parseDouble(s.toString()),
                                tradeType[0]);

                        changePoint.setText(String.valueOf(calc.getPointChange()));

                        if(label != null && label.equals("Exit Price")){

                            exitOrStopLoss.setText(
                                    label+"\nRs. "+calc.getExit()
                            );

                            res[0] = calc.getExit();


                        }else if(label != null && label.equals("Stop Loss")){

                            exitOrStopLoss.setText(
                                    label+"\nRs. "+calc.getStopLoss()
                            );

                            res[0] = calc.getStopLoss();
                        }
                    }

                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });

            doneBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    exitOrSL.setText(String.valueOf(res[0]));
                    dismiss();
                }
            });

        }catch (Exception e){
            e.printStackTrace();
        }

        return v;
    }
}
