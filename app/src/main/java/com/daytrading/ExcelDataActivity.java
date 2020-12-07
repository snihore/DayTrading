package com.daytrading;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.res.AssetManager;
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

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.poifs.filesystem.POIFSWriterEvent;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class ExcelDataActivity extends AppCompatActivity {

    private static String FILE_NAME = "day_trading_excel_file_v2.xls";

    private static final String TAG = "ExcelDataActivity";
    private TextView status, PndLBtn;
    private RecyclerView recyclerView;
    private List<ExcelRowData> list, copyList;
    private ExcelRowRecyclerViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_excel_data);

        recyclerView = (RecyclerView)findViewById(R.id.excel_data_rv);
        status = (TextView)findViewById(R.id.status);
        PndLBtn = (TextView)findViewById(R.id.p_nd_l_btn);
        list = new ArrayList<>();

        try{
            read();
        }catch (Exception e){
            e.printStackTrace();
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        //Click Events
        PndLBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ProfitAndLossActivity.class);
                startActivity(intent);
            }
        });
    }


    private void read(){

    try{

        InputStream myInput = new FileInputStream(new File(Common.excelFileLoc(getApplicationContext())+FILE_NAME));
        
        // Create a POI File System object
        POIFSFileSystem myFileSystem = new POIFSFileSystem(myInput);
        
        // Create a workbook using the File System
        HSSFWorkbook myWorkBook = new HSSFWorkbook(myFileSystem);

        // Get the first sheet from workbook
        HSSFSheet mySheet = myWorkBook.getSheet("DayTradingJournal");

        // We now need something to iterate through the cells.
        Iterator<Row> rowIter = mySheet.rowIterator();
        int rowno =0;

        while (rowIter.hasNext()) {
            Log.d(TAG, " row no "+ rowno);
            HSSFRow myRow = (HSSFRow) rowIter.next();
            if(rowno != 0 ) {
                Iterator<Cell> cellIter = myRow.cellIterator();
                int colno =0;
                String stock1="", type1="", entryPrice1="", exitPrice1 = "", quantity1="", PndL1="", PndLType="", PndLPer="", PndLB="", date="";
                while (cellIter.hasNext()) {
                    HSSFCell myCell = (HSSFCell) cellIter.next();
                    if (colno==0){
                        stock1 = myCell.toString();
//                        Log.i("Stock", myCell.toString()+"--->");
                    }else if (colno==1){
                        type1 = myCell.toString();
//                        Log.i("Type", myCell.toString()+"--->");
                    }else if (colno==2){
                        entryPrice1 = myCell.toString();
//                        Log.i("Entry", myCell.toString()+"--->");
                    }else if(colno==3){
                        exitPrice1 = myCell.toString();
//                        Log.i("Exit", myCell.toString()+"--->");
                    }else if(colno==4){
                        quantity1 = myCell.toString();
//                        Log.i("QTY", myCell.toString()+"--->");
                    }else if(colno==5){
                        PndL1 = myCell.toString();
//                        Log.i("P&L", myCell.toString()+"--->");
                    }else if(colno==6){
                        PndLType = myCell.toString();
//                        Log.i("P&L Type", myCell.toString()+"--->");
                    }else if(colno==7){
                        PndLPer = myCell.toString();
//                        Log.i("P&L %", myCell.toString()+"--->");
                    }else if(colno==8){
                        PndLB = myCell.toString();
//                        Log.i("P&L without Br.", myCell.toString()+"--->");
                    }else if(colno==9){
                        date = myCell.toString();
//                        Log.i("Date", myCell.toString()+"--->"+date);
                    }
                    colno++;
                    Log.d(TAG, " Index :" + myCell.getColumnIndex() + " -- " + myCell.toString());

                }

                list.add(new ExcelRowData(
                        stock1,
                        type1,
                        entryPrice1,
                        exitPrice1,
                        quantity1,
                        PndL1,
                        PndLPer,
                        PndLType,
                        date,
                        PndLB
                ));


            }
            rowno++;
        }

        copyList = new ArrayList<>(list);
        reverseListOrder();

        adapter = new ExcelRowRecyclerViewAdapter(getApplicationContext(), list, new ExcelRowClickListener() {
            @Override
            public void onClick(View view, int position, String type) {
                switch (type){

                    case "DELETE":
                        openDeleteDialog(position);
                        break;

                    case "EDIT":
//                        Toast.makeText(ExcelDataActivity.this, "Edit "+list.get(position).getStock(), Toast.LENGTH_SHORT).show();
                        openEditDialog(position);
                        break;
                }
            }
        });
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        if(list.size()==0){
            status.setVisibility(View.VISIBLE);
        }else {
            status.setVisibility(View.INVISIBLE);
        }

        myWorkBook.close();
        myFileSystem.close();
        myInput.close();

    }catch (Exception e) {
        e.printStackTrace();
        Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
    }
    }

    private void openEditDialog(int position) {

        //before inflating the custom alert dialog layout, we will get the current activity viewgroup
        ViewGroup viewGroup = findViewById(android.R.id.content);

        //then we will inflate the custom alert dialog xml that we created
        View dialogView = LayoutInflater.from(ExcelDataActivity.this).inflate(R.layout.excel_row_edit_dialog, viewGroup, false);

        //Now we need an AlertDialog.Builder object
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        //setting the view of the builder to our custom view that we already inflated
        builder.setView(dialogView);

        //finally creating the alert dialog and displaying it
        final AlertDialog alertDialog = builder.create();
        alertDialog.show();

        //Handle Views
        final EditText editText = (EditText)dialogView.findViewById(R.id.excel_row_edit_et);
        TextView title = (TextView)dialogView.findViewById(R.id.excel_row_edit_title);
        TextView cancelBtn = (TextView)dialogView.findViewById(R.id.excel_row_edit_cancel_btn);
        TextView saveBtn = (TextView)dialogView.findViewById(R.id.excel_row_edit_save_btn);

        try{

            if(copyList == null){
                alertDialog.dismiss();
                Toast.makeText(this, "Excel Data Not Found !!", Toast.LENGTH_SHORT).show();
                return;
            }

            title.setText(list.get(position).getStock());

            int editIndex = -1;
            editIndex = copyList.indexOf(list.get(position));

            //Click events
            cancelBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alertDialog.dismiss();
                }
            });

            final int finalEditIndex = editIndex;
            saveBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(finalEditIndex < 0 || finalEditIndex>=copyList.size()){
                        alertDialog.dismiss();
                        Toast.makeText(getApplicationContext(), "Not Updated", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    String PndLwithoutBr = editText.getText().toString().trim();

                    if(PndLwithoutBr == null || PndLwithoutBr.matches("")){
                        alertDialog.dismiss();
                        Toast.makeText(getApplicationContext(), "Not Updated", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    ExcelHandle excelHandle = new ExcelHandle(getApplicationContext());
                    excelHandle.update(finalEditIndex+1, PndLwithoutBr);

                    alertDialog.dismiss();
                    list.clear();
                    copyList.clear();
                    read();

                }
            });

        }catch (Exception e){
            e.printStackTrace();
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }

    private void openDeleteDialog(int position) {

        //before inflating the custom alert dialog layout, we will get the current activity viewgroup
        ViewGroup viewGroup = findViewById(android.R.id.content);

        //then we will inflate the custom alert dialog xml that we created
        View dialogView = LayoutInflater.from(ExcelDataActivity.this).inflate(R.layout.excel_row_delete_dialog, viewGroup, false);

        //Now we need an AlertDialog.Builder object
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        //setting the view of the builder to our custom view that we already inflated
        builder.setView(dialogView);

        //finally creating the alert dialog and displaying it
        final AlertDialog alertDialog = builder.create();
        alertDialog.show();

        //Handle Views
        TextView title = (TextView)dialogView.findViewById(R.id.excel_row_delete_dialog_title);
        TextView cancel = (TextView)dialogView.findViewById(R.id.excel_row_delete_dialog_cancel_btn);
        TextView delete = (TextView)dialogView.findViewById(R.id.excel_row_delete_dialog_delete_btn);

        try{

            if(copyList == null){
                alertDialog.dismiss();
                Toast.makeText(this, "Excel Delete Data Not Found !!", Toast.LENGTH_SHORT).show();
                return;
            }

            title.setText(list.get(position).getStock());

            int deleteIndex = -1;
            deleteIndex = copyList.indexOf(list.get(position));

            final int finalDeleteIndex = deleteIndex;
            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(finalDeleteIndex < 0 || finalDeleteIndex>=copyList.size()){
                        alertDialog.dismiss();
                        Toast.makeText(getApplicationContext(), "Not Deleted", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    ExcelHandle excelHandle = new ExcelHandle(getApplicationContext());

                    excelHandle.delete(finalDeleteIndex+1);
                    Log.i("DELETED ROW INDEX", String.valueOf(finalDeleteIndex));
                    alertDialog.dismiss();
                    list.clear();
                    copyList.clear();
                    read();

                }
            });

            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alertDialog.dismiss();
                }
            });



        }catch (Exception e){
            e.printStackTrace();
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }



    }

    private void reverseListOrder() {

        if(list == null || list.size() < 2){
            return;
        }

        List<ExcelRowData> reverseList = new ArrayList<>();

        for(int i=list.size()-1; i>=0; i--){
            reverseList.add(list.get(i));
        }

        list = new ArrayList<>(reverseList);
    }



}