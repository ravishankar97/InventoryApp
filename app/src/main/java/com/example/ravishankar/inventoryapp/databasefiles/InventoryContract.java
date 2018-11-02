package com.example.ravishankar.inventoryapp.databasefiles;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;


public final class InventoryContract {
    static final String CONTENT_AUTHORITY = "com.example.ravishankar.inventoryapp";
    private static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    static final String PATH = "Books";


    private InventoryContract() {

    }

    public static final class InventoryDataColumns implements BaseColumns {

        public final static Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH);
        static final String CONTENT_LIST_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH;
        static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH;

        final static String TABLE_NAME = "Books";
        public final static String _ID = BaseColumns._ID;
        public static final String PRODUCT_NAME = "BOOK_NAME";
        public static final String PRICE = "PRICE";
        public static final String QUANTITY = "quantity";
        public static final String SUPPLIER_NAME = "SUPPLIER_NAME";
        public static final String SUPPLIER_PHONE_NUMBER = "SUPPLIER_PHONE_NUMBER";


    }

}
