package com.example.ravishankar.inventoryapp;

import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.ravishankar.inventoryapp.databasefiles.InventoryContract.InventoryDataColumns;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int STATIC_LOADER_INTEGER = 0;

    private InventoryCursorAdapter mCursorAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView listView = findViewById(R.id.list_view);
        FloatingActionButton fab = findViewById(R.id.fab);

        View emptyView = findViewById(R.id.empty_view);
        listView.setEmptyView(emptyView);
        mCursorAdapter = new InventoryCursorAdapter(this, null);
        listView.setAdapter(mCursorAdapter);
        getLoaderManager().initLoader(STATIC_LOADER_INTEGER, null, this);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this, EditorActivity.class);

                Uri currentItemUri = ContentUris.withAppendedId(InventoryDataColumns.CONTENT_URI, id);
                intent.setData(currentItemUri);
                startActivity(intent);
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, EditorActivity.class);
                startActivity(intent);
            }
        });

    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        String[] projection = {InventoryDataColumns._ID,
                InventoryDataColumns.PRODUCT_NAME,
                InventoryDataColumns.PRICE,
                InventoryDataColumns.SUPPLIER_NAME,
                InventoryDataColumns.SUPPLIER_PHONE_NUMBER,
                InventoryDataColumns.QUANTITY};

        return new CursorLoader(this, InventoryDataColumns.CONTENT_URI, projection, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        mCursorAdapter.swapCursor(data);

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mCursorAdapter.swapCursor(null);

    }
}
