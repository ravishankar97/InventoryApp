package com.example.ravishankar.inventoryapp.datbasefiles;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.ravishankar.inventoryapp.datbasefiles.InventoryContract.InventoryDataColumns;

public class DBHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME="Inventory.db";
    private static final int DATABASE_VERSION=1;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME,null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE= "CREATE TABLE  " + InventoryDataColumns.TABLE_NAME + " ("
                + InventoryDataColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + InventoryDataColumns.PRODUCT_NAME + " TEXT NOT NULL, "
                + InventoryDataColumns.PRICE + " INTEGER NOT NULL, "
                + InventoryDataColumns.QUANTITY + " INTEGER NOT NULL, "
                + InventoryDataColumns.SUPPLIER_NAME + " TEXT NOT NULL, "
                + InventoryDataColumns.SUPPLIER_PHONE_NUMBER + " INTEGER NOT NULL" +");";
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
