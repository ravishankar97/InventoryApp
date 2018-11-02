package com.example.ravishankar.inventoryapp;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ravishankar.inventoryapp.databasefiles.InventoryContract.InventoryDataColumns;

public class InventoryCursorAdapter extends CursorAdapter {

    InventoryCursorAdapter(Context context, Cursor cursor) {
        super(context, cursor, 0);

    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
    }

    @Override
    public void bindView(View view, final Context context, Cursor cursor) {
        TextView productName = view.findViewById(R.id.book_name_id);
        TextView supplierName = view.findViewById(R.id.author_id);
        TextView price = view.findViewById(R.id.price_id);
        final TextView quantity = view.findViewById(R.id.quantity_id);
        Button availableButton = view.findViewById(R.id.available_button);


        int productColumnIndex = cursor.getColumnIndex(InventoryDataColumns.PRODUCT_NAME);
        int supplierColumnIndex = cursor.getColumnIndex(InventoryDataColumns.SUPPLIER_NAME);
        int priceColumnIndex = cursor.getColumnIndex(InventoryDataColumns.PRICE);
        int quantityColumnIndex = cursor.getColumnIndex(InventoryDataColumns.QUANTITY);
        final int rowId = cursor.getInt(cursor.getColumnIndex(InventoryDataColumns._ID));

        final String quantityAvailable = String.valueOf(cursor.getInt(quantityColumnIndex));

        productName.setText(cursor.getString(productColumnIndex));
        supplierName.setText(cursor.getString(supplierColumnIndex));
        String priceString = context.getString(R.string.rupees_syntax) + cursor.getString(priceColumnIndex);
        price.setText(priceString);
        quantity.setText(cursor.getString(quantityColumnIndex));


        availableButton.setOnClickListener(new View.OnClickListener() {
            Toast toast;
            @Override
            public void onClick(View v) {
                Uri uri = ContentUris.withAppendedId(InventoryDataColumns.CONTENT_URI, rowId);
                if(toast!=null)
                    toast.cancel();
                if (Integer.parseInt((quantityAvailable)) > 0) {
                    ContentValues values = new ContentValues();
                    values.put(InventoryDataColumns.QUANTITY, Integer.parseInt(quantityAvailable) - 1);
                    context.getContentResolver().update(uri, values, null, null);
                } else {

                    toast = Toast.makeText(context, R.string.unavailable_text, Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        });
    }
}
