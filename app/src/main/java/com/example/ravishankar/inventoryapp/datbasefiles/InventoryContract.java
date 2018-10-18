package com.example.ravishankar.inventoryapp.datbasefiles;

import android.provider.BaseColumns;

public final class InventoryContract {
    private InventoryContract() {

    }
    public class InventoryDataColumns implements BaseColumns{
        public final static String TABLE_NAME = "Books";
        public final static String _ID = BaseColumns._ID;
        public static final String PRODUCT_NAME="BOOK_NAME";
        public static final String PRICE="PRICE";
        public static final String QUANTITY = "quantity";
        public static final String SUPPLIER_NAME="SUPPLIER_NAME";
        public static final String SUPPLIER_PHONE_NUMBER="SUPPLIER_PHONE_NUMBER";
    }

}
