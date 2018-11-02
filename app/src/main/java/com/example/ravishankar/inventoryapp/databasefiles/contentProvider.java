package com.example.ravishankar.inventoryapp.databasefiles;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.ravishankar.inventoryapp.databasefiles.InventoryContract.InventoryDataColumns;

public class contentProvider extends ContentProvider {

    public static final int BOOK = 100;
    public static final int BOOK_ID = 101;

    private static UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        uriMatcher.addURI(InventoryContract.CONTENT_AUTHORITY, InventoryContract.PATH, BOOK);
        uriMatcher.addURI(InventoryContract.CONTENT_AUTHORITY, InventoryContract.PATH + "/#", BOOK_ID);
    }

    private DBHelper mDBHelper;

    @Override
    public boolean onCreate() {
        mDBHelper = new DBHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(@NonNull Uri uri, String[] projection, String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        SQLiteDatabase database = mDBHelper.getReadableDatabase();
        Cursor cursor;
        int match = uriMatcher.match(uri);
        switch (match) {
            case BOOK: {
                cursor = database.query(InventoryDataColumns.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            }
            case BOOK_ID: {
                selection = InventoryDataColumns._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                cursor = database.query(InventoryDataColumns.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            }
            default:
                throw new IllegalArgumentException("Cannot query unknown URI " + uri);
        }
        if (getContext() != null)
            cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        switch (uriMatcher.match(uri)) {
            case BOOK:
                return InventoryDataColumns.CONTENT_LIST_TYPE;
            case BOOK_ID:
                return InventoryDataColumns.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalStateException("Unknown URI " + uri + " with match " + uriMatcher.match(uri));
        }
    }

    @Override
    public Uri insert(@NonNull Uri uri, ContentValues values) {
        int match = uriMatcher.match(uri);
        switch (match) {
            case BOOK:
                return insertBook(uri, values);
            default:
                throw new IllegalArgumentException("Insertion is not supported for " + uri);
        }
    }

    public Uri insertBook(Uri uri, ContentValues contentValues) {
        SQLiteDatabase database = mDBHelper.getWritableDatabase();
        long id = database.insert(InventoryDataColumns.TABLE_NAME, null, contentValues);
        if (id == -1) {
            Log.e("Row Insertion", "Failed to insert row for " + uri);
            return null;
        }
        if (getContext() != null)
            getContext().getContentResolver().notifyChange(uri, null);
        return ContentUris.withAppendedId(uri, id);
    }

    @Override
    public int delete(@NonNull Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase database = mDBHelper.getWritableDatabase();
        final int match = uriMatcher.match(uri);
        switch (match) {
            case BOOK: {
                return database.delete(InventoryDataColumns.TABLE_NAME, selection, selectionArgs);
            }
            case BOOK_ID: {
                selection = InventoryDataColumns._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                int rowsDeleted = database.delete(InventoryDataColumns.TABLE_NAME, selection, selectionArgs);
                if (rowsDeleted != 0 && getContext() != null)
                    getContext().getContentResolver().notifyChange(uri, null);
                return rowsDeleted;
            }
            default:
                throw new IllegalArgumentException("Deletion is not supported for " + uri);
        }
    }

    @Override
    public int update(@NonNull Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        final int match = uriMatcher.match(uri);
        switch (match) {
            case BOOK: {
                return updateBook(uri, values, selection, selectionArgs);
            }

            case BOOK_ID: {
                selection = InventoryDataColumns._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                return updateBook(uri, values, selection, selectionArgs);
            }
            default:
                throw new IllegalArgumentException("Update is not supported for " + uri);
        }
    }

    private int updateBook(Uri uri, ContentValues contentValues, String selection, String[] selectionArgs) {
        if (contentValues.containsKey(InventoryDataColumns.PRODUCT_NAME)) {
            String name = contentValues.getAsString(InventoryDataColumns.PRODUCT_NAME);
            if (name == null)
                throw new IllegalArgumentException("Book requires a name");
        }

        SQLiteDatabase database = mDBHelper.getWritableDatabase();
        int updateNumber = database.update(InventoryDataColumns.TABLE_NAME, contentValues, selection, selectionArgs);
        if (updateNumber != 0 && getContext() != null)
            getContext().getContentResolver().notifyChange(uri, null);
        return updateNumber;
    }
}
