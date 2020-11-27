package com.daytrading;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
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
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Iterator;

public class ExcelHandle {

    private static String FILE_NAME = "day_trading_excel_file_v2.xls";

    public static void checkExcelFile(Context context){

        try{

            InputStream myInput = new FileInputStream(new File(Common.excelFileLoc(context)+FILE_NAME));

            // Create a POI File System object
            POIFSFileSystem myFileSystem = new POIFSFileSystem(myInput);

            // Create a workbook using the File System
            HSSFWorkbook myWorkBook = new HSSFWorkbook(myFileSystem);

            myWorkBook.close();
            myFileSystem.close();
            myInput.close();

        }catch (FileNotFoundException e){
            e.printStackTrace();

            //Create File
            ExcelHandle handle = new ExcelHandle(context);
            handle.createExcelFile();

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private Context context;

    public ExcelHandle(Context context) {
        this.context = context;
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

            //Date
            Cell cell9 = row.createCell(9);
            cell9.setCellValue("Date");

            OutputStream outputStream = new FileOutputStream(new File(Common.excelFileLoc(context)+FILE_NAME));
            hssfWorkbook.write(outputStream);

            hssfWorkbook.close();
            outputStream.close();

            Toast.makeText(context, "Excel file created at \""+Common.excelFileLoc(context)+"\"", Toast.LENGTH_SHORT).show();


        }catch (Exception e){
            e.printStackTrace();
        }
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

            InputStream myInput = new FileInputStream(new File(Common.excelFileLoc(context)+FILE_NAME));

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
            cell0.setCellValue(stock.toUpperCase());

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

            //Profit and Loss Without Brokerage
            Cell cell8 = row.createCell(8);
            cell8.setCellValue("--");

            //Date
            Cell cell9 = row.createCell(9);
            cell9.setCellValue(getDateAndTime());

            OutputStream outputStream = new FileOutputStream(new File(Common.excelFileLoc(context)+FILE_NAME));
            myWorkBook.write(outputStream);

            myWorkBook.close();
            myFileSystem.close();
            myInput.close();
            outputStream.close();

            Toast.makeText(context, "saved", Toast.LENGTH_SHORT).show();



        }catch (FileNotFoundException e){
            e.printStackTrace();

            Toast.makeText(context, "Excel File Not Found !!!", Toast.LENGTH_SHORT).show();

        }catch (Exception e){
            e.printStackTrace();
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public void delete(int rowIndex){

        try {

            InputStream myInput = new FileInputStream(new File(Common.excelFileLoc(context) + FILE_NAME));

            // Create a POI File System object
            POIFSFileSystem myFileSystem = new POIFSFileSystem(myInput);
            // Create a workbook using the File System
            HSSFWorkbook myWorkBook = new HSSFWorkbook(myFileSystem);

            // Get the first sheet from workbook
            HSSFSheet hssfSheet = myWorkBook.getSheetAt(0);

            Row row = hssfSheet.getRow(rowIndex);

            if(row != null){

                int lastRowNum = hssfSheet.getLastRowNum();

                if(rowIndex == lastRowNum){
                    hssfSheet.removeRow(row);
                }else{
                    hssfSheet.shiftRows(rowIndex+1, lastRowNum, -1);

                    if(hssfSheet.getRow(lastRowNum) != null){
                        hssfSheet.removeRow(hssfSheet.getRow(lastRowNum));
                    }
                }


                OutputStream outputStream = new FileOutputStream(new File(Common.excelFileLoc(context)+FILE_NAME));
                myWorkBook.write(outputStream);

                outputStream.close();

                Toast.makeText(context, "Deleted", Toast.LENGTH_SHORT).show();

            }else {
                Toast.makeText(context, "Row Index not found", Toast.LENGTH_SHORT).show();
            }

            myWorkBook.close();
            myFileSystem.close();
            myInput.close();

        }catch (Exception e){
            e.printStackTrace();
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
        }


    }


    private String getDateAndTime() {

        try{

            Calendar c = Calendar.getInstance();
            SimpleDateFormat dateformat = new SimpleDateFormat("dd-MMM-yyyy HH:mm");
            String datetime = dateformat.format(c.getTime());

            return datetime;

        }catch (Exception e){
            e.printStackTrace();
            return "";
        }
    }


}
