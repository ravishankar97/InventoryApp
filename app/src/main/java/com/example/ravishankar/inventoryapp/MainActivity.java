package com.example.ravishankar.inventoryapp;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.ravishankar.inventoryapp.datbasefiles.DBHelper;
import com.example.ravishankar.inventoryapp.datbasefiles.InventoryContract.InventoryDataColumns;

public class MainActivity extends AppCompatActivity {

    Button insertButton;
    private DBHelper mDbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        insertButton = findViewById(R.id.button);

        insertButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertData();
            }
        });
        mDbHelper = new DBHelper(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        queryData();
    }

    private void queryData() {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        String[] projectionColumns = {
                InventoryDataColumns._ID,
                InventoryDataColumns.PRODUCT_NAME,
                InventoryDataColumns.PRICE,
                InventoryDataColumns.QUANTITY,
                InventoryDataColumns.SUPPLIER_NAME,
                InventoryDataColumns.SUPPLIER_PHONE_NUMBER
        };

        Cursor resultSet = db.query(InventoryDataColumns.TABLE_NAME, projectionColumns,
                null,
                null,
                null,
                null,
                null);

        TextView textView = findViewById(R.id.textView);


        try {
            textView.setText("\n");
            int idColumnIndex = resultSet.getColumnIndex(InventoryDataColumns._ID);
            int nameColumnIndex = resultSet.getColumnIndex(InventoryDataColumns.PRODUCT_NAME);
            int priceColumnIndex = resultSet.getColumnIndex(InventoryDataColumns.PRICE);
            int quantityColumnIndex = resultSet.getColumnIndex(InventoryDataColumns.QUANTITY);
            int supplierNameColumnIndex = resultSet.getColumnIndex(InventoryDataColumns.SUPPLIER_NAME);
            int supplierPhoneColumnIndex = resultSet.getColumnIndex(InventoryDataColumns.SUPPLIER_PHONE_NUMBER);

            while (resultSet.moveToNext()) {

                int productID = resultSet.getInt(idColumnIndex);
                String productName = resultSet.getString(nameColumnIndex);
                int price = resultSet.getInt(priceColumnIndex);
                int quantity = resultSet.getInt(quantityColumnIndex);
                String supplierName = resultSet.getString(supplierNameColumnIndex);
                int supplierPhoneNumber = resultSet.getInt(supplierPhoneColumnIndex);

                textView.append(("\n" + productID + "-" + productName + "-" + quantity + "-" + price + "-" + supplierName + "-" + supplierPhoneNumber));
            }
        } finally {
            resultSet.close();
        }
    }

    private void insertData() {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(InventoryDataColumns.PRODUCT_NAME, "HarryPotter");
        values.put(InventoryDataColumns.PRICE, 425);
        values.put(InventoryDataColumns.QUANTITY, 23);
        values.put(InventoryDataColumns.SUPPLIER_NAME, "J.K.Rowling");
        values.put(InventoryDataColumns.SUPPLIER_PHONE_NUMBER, 987654321);
        db.insert(InventoryDataColumns.TABLE_NAME, null, values);
        queryData();

    }
}
