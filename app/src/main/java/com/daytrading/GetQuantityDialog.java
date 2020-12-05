package com.daytrading;

import android.app.Dialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class GetQuantityDialog extends DialogFragment {

    public static final String TAG = "example_dialog";

    private  String entryPrice;
    private String investment;
    private String rptPercentage;
    private RecyclerView recyclerView;

    public GetQuantityDialog(String investment, String rptPercentage, String entryPrice) {
        this.entryPrice = entryPrice;
        this.investment = investment;
        this.rptPercentage = rptPercentage;
    }

    public static GetQuantityDialog display(FragmentManager fragmentManager, String investment, String rptPercentage, String entryPrice) {
        GetQuantityDialog dialog = new GetQuantityDialog(investment, rptPercentage, entryPrice);
        dialog.show(fragmentManager, TAG);
        return dialog;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.AppTheme_FullScreenDialog);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.get_quantity_dialog, container, false);

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final ImageView closeBtn = (ImageView) view.findViewById(R.id.get_quantity_close_btn);
        final EditText entryPriceET = (EditText) view.findViewById(R.id.get_quantity_set_entry_price);
        recyclerView = (RecyclerView) view.findViewById(R.id.get_quantity_rv);

        if(entryPrice != null && !entryPrice.matches("")){
            entryPriceET.setText(entryPrice);
        }

        //Init
        getQuantity(entryPrice);

        entryPriceET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String entry = s.toString();

                getQuantity(entry);

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        //Click Events
        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            dialog.getWindow().setLayout(width, height);
        }
    }

    private void getQuantity(String entry){

        try{
            GetQuantityCalc getQuantityCalc = new GetQuantityCalc(
                    getContext(),
                    Long.parseLong(investment),
                    Double.parseDouble(rptPercentage),
                    Double.parseDouble(entry),
                    2,
                    "BUY"
            );

            List<Long> quantites = getQuantityCalc.getQuantites();
            List<Double> stopLossPercentages = getQuantityCalc.getStopLossPercentages();

            List<GetQuantityData> list = new ArrayList<>();

            for(int i=0; i<quantites.size(); i++){
                list.add(new GetQuantityData(
                        String.valueOf(stopLossPercentages.get(i)),
                        String.valueOf(quantites.get(i))
                ));
            }

            GetQuantityShowItemAdapter adapter = new GetQuantityShowItemAdapter(getContext(), list);
            recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
