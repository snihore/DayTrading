package com.daytrading;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.res.AssetManager;
import android.os.Bundle;
import android.util.Log;
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
import java.util.Iterator;
import java.util.List;

public class ExcelDataActivity extends AppCompatActivity {

    private static final String TAG = "ExcelDataActivity";
    boolean readFirstTime = true;
    private RecyclerView recyclerView;
    private List<ExcelRowData> list;
    private ExcelRowRecyclerViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_excel_data);

        recyclerView = (RecyclerView)findViewById(R.id.excel_data_rv);
        list = new ArrayList<>();

        try{
            adapter = new ExcelRowRecyclerViewAdapter(getApplicationContext(), list);
            recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

            read();
        }catch (Exception e){
            e.printStackTrace();
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }





    private void read(){

    try{

        InputStream myInput = new FileInputStream(new File(Common.excelFileLoc(getApplicationContext())+"day_trading_excel_file.xls"));
        
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
                String stock1="", type1="", entryPrice1="", exitPrice1 = "", quantity1="", PndL1="", PndLType="", PndLPer="", PndLB="";
                while (cellIter.hasNext()) {
                    HSSFCell myCell = (HSSFCell) cellIter.next();
                    if (colno==0){
                        stock1 = myCell.toString();
                    }else if (colno==1){
                        type1 = myCell.toString();
                    }else if (colno==2){
                        entryPrice1 = myCell.toString();
                    }else if(colno==3){
                        exitPrice1 = myCell.toString();
                    }else if(colno==4){
                        quantity1 = myCell.toString();
                    }else if(colno==5){
                        PndL1 = myCell.toString();
                    }else if(colno==6){
                        PndLType = myCell.toString();
                    }else if(colno==7){
                        PndLPer = myCell.toString();
                    }else if(colno==8){
                        PndLB = myCell.toString();
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
                        PndLType
                ));

                adapter.notifyDataSetChanged();

            }
            rowno++;
        }

        myWorkBook.close();
        myFileSystem.close();
        myInput.close();

    }catch (FileNotFoundException e){
        e.printStackTrace();

        Log.d(TAG, ".xls File Creation ...");
        //Create NEW .xls File
        createExcelFile();
    } catch (Exception e) {
        e.printStackTrace();
        Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
    }
    }

    private void write(String stock, String type, String entryPrice, String exitPrice, String quantity, String PndL){

        try{

            InputStream myInput = new FileInputStream(new File(Common.excelFileLoc(getApplicationContext())+"day_trading_excel_file.xls"));

            // Create a POI File System object
            POIFSFileSystem myFileSystem = new POIFSFileSystem(myInput);
            // Create a workbook using the File System
            HSSFWorkbook myWorkBook = new HSSFWorkbook(myFileSystem);

            // Get the first sheet from workbook
            HSSFSheet hssfSheet = myWorkBook.getSheetAt(0);

            int lastRow = hssfSheet.getLastRowNum();

            Row row = hssfSheet.createRow(lastRow+1);

            //Stock
            Cell cell0 = row.createCell(0);
            cell0.setCellValue(stock);

            //Type
            Cell cell1 = row.createCell(1);
            cell1.setCellValue(type);

            //Entry Price
            Cell cell2 = row.createCell(2);
            cell2.setCellValue(entryPrice);

            //Exit Price
            Cell cell3 = row.createCell(3);
            cell3.setCellValue(exitPrice);

            //Quantity
            Cell cell4 = row.createCell(4);
            cell4.setCellValue(quantity);

            //Profit and Loss
            Cell cell5 = row.createCell(5);
            cell5.setCellValue(PndL);

            OutputStream outputStream = new FileOutputStream(new File(Common.excelFileLoc(getApplicationContext())+"day_trading_excel_file.xls"));
            myWorkBook.write(outputStream);

            myWorkBook.close();
            myFileSystem.close();
            myInput.close();
            outputStream.close();



        }catch (FileNotFoundException e){
            e.printStackTrace();

        }catch (Exception e){
            e.printStackTrace();
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void delete(int rowIndex){

        try {

            InputStream myInput = new FileInputStream(new File(Common.excelFileLoc(getApplicationContext()) + "day_trading_excel_file.xls"));

            // Create a POI File System object
            POIFSFileSystem myFileSystem = new POIFSFileSystem(myInput);
            // Create a workbook using the File System
            HSSFWorkbook myWorkBook = new HSSFWorkbook(myFileSystem);

            // Get the first sheet from workbook
            HSSFSheet hssfSheet = myWorkBook.getSheetAt(0);

            Row row = hssfSheet.getRow(rowIndex);

            if(row != null){

                hssfSheet.removeRow(row);

                OutputStream outputStream = new FileOutputStream(new File(Common.excelFileLoc(getApplicationContext())+"day_trading_excel_file.xls"));
                myWorkBook.write(outputStream);

                outputStream.close();

            }else {
                Toast.makeText(this, "Row Index not found", Toast.LENGTH_SHORT).show();
            }

            myWorkBook.close();
            myFileSystem.close();
            myInput.close();

        }catch (Exception e){
            e.printStackTrace();
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }


    }

    private void createExcelFile() {

        try{

            HSSFWorkbook hssfWorkbook = new HSSFWorkbook();

            HSSFSheet hssfSheet =  hssfWorkbook.createSheet("DayTradingJournal");

            Row row = hssfSheet.createRow(0);

            //Stock
            Cell cell0 = row.createCell(0);
            cell0.setCellValue("Stock"); // TCS, UPL, INFY, ICICI etc.

            //Type
            Cell cell1 = row.createCell(1);
            cell1.setCellValue("Type"); // Long or Short

            //Entry Price
            Cell cell2 = row.createCell(2);
            cell2.setCellValue("Entry Price");

            //Exit Price
            Cell cell3 = row.createCell(3);
            cell3.setCellValue("Exit Price");

            //Quantity
            Cell cell4 = row.createCell(4);
            cell4.setCellValue("Quantity");

            //Profit and Loss
            Cell cell5 = row.createCell(5);
            cell5.setCellValue("P&L"); // Points

            //P&L Type
            Cell cell6 = row.createCell(6);
            cell6.setCellValue("P&L Type"); // Profit or Loss

            //P&L Percentage
            Cell cell7 = row.createCell(7);
            cell7.setCellValue("P&L Percentage");

            //P&L Without Brokerages
            Cell cell8 = row.createCell(8);
            cell8.setCellValue("P&L without Brokerages");

            OutputStream outputStream = new FileOutputStream(new File(Common.excelFileLoc(getApplicationContext())+"day_trading_excel_file.xls"));
            hssfWorkbook.write(outputStream);

            if(readFirstTime){
                read();
                readFirstTime = false;
            }

            hssfWorkbook.close();
            outputStream.close();

            Toast.makeText(this, "Excel file created at \""+Common.excelFileLoc(getApplicationContext())+"\"", Toast.LENGTH_SHORT).show();


        }catch (Exception e){
            e.printStackTrace();
        }
    }
}