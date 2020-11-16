package com.daytrading;

import android.content.Context;
import android.widget.Toast;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

public class ExcelHandle {

    private Context context;

    public ExcelHandle(Context context) {
        this.context = context;
    }

    public void write(
            String stock,
            String type,
            String entryPrice,
            String exitPrice,
            String quantity,
            String PndL,
            String PndLPer,
            String PndLType){

        try{

            InputStream myInput = new FileInputStream(new File(Common.excelFileLoc(context)+"day_trading_excel_file.xls"));

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

            //Profit and Loss type
            Cell cell6 = row.createCell(6);
            cell6.setCellValue(PndLType);

            //Profit and Loss Percentage
            Cell cell7 = row.createCell(7);
            cell7.setCellValue(PndLPer);

            OutputStream outputStream = new FileOutputStream(new File(Common.excelFileLoc(context)+"day_trading_excel_file.xls"));
            myWorkBook.write(outputStream);

            myWorkBook.close();
            myFileSystem.close();
            myInput.close();
            outputStream.close();

            Toast.makeText(context, "saved", Toast.LENGTH_SHORT).show();



        }catch (FileNotFoundException e){
            e.printStackTrace();

            Toast.makeText(context, "Open the journal tab first and you will able to save the entry", Toast.LENGTH_SHORT).show();

        }catch (Exception e){
            e.printStackTrace();
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}
