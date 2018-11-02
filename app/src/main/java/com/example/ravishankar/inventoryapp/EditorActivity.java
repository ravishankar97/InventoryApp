package com.example.ravishankar.inventoryapp;

import android.app.AlertDialog;
import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.ravishankar.inventoryapp.databasefiles.InventoryContract.InventoryDataColumns;

public class EditorActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int EXISTING_LOADER = 0;
    EditText EditBookNameText;
    EditText EditSupplierNameText;
    EditText EditQuantityText;
    EditText EditPriceText;
    EditText EditPhoneNumberText;
    Button SaveButton;
    Button orderButton;
    Button IncreaseQuantityButton;
    Button DecreaseQuantityButton;
    private Uri mCurrentItemUri;
    private String phoneNumberData;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);
        SaveButton = findViewById(R.id.update_button);
        IncreaseQuantityButton = findViewById(R.id.increase_quantity);
        DecreaseQuantityButton = findViewById(R.id.decrease_quantity);
        EditBookNameText = findViewById(R.id.book_name_edit_text);
        EditSupplierNameText = findViewById(R.id.supplier_name_edit_text);
        EditQuantityText = findViewById(R.id.quantity_edit_text);
        EditPriceText = findViewById(R.id.price_edit_text);
        orderButton = findViewById(R.id.order_button);
        EditPhoneNumberText = findViewById(R.id.phone_edit_text);
        EditQuantityText.setText("0");

        Intent intent = getIntent();
        mCurrentItemUri = intent.getData();

        if (mCurrentItemUri == null) {
            setTitle(R.string.editor_activity_title_add_item);
            invalidateOptionsMenu();
            SaveButton.setText(R.string.adding_entry_button_text);

        } else {
            setTitle(R.string.editor_activity_title_edit_pet);
            SaveButton.setText(R.string.updating_entry_button_text);
            getLoaderManager().initLoader(EXISTING_LOADER, null, this);
        }

        if (mCurrentItemUri != null) {
            EditBookNameText.setFocusable(false);
            EditQuantityText.setFocusable(false);
            EditPhoneNumberText.setFocusable(false);
            EditSupplierNameText.setFocusable(false);
            EditPriceText.setFocusable(false);
        }


        IncreaseQuantityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(EditQuantityText.getText()))
                    EditQuantityText.setText(String.valueOf(Integer.parseInt(EditQuantityText.getText().toString()) + 1));
                else
                    EditQuantityText.setText("0");
            }
        });

        DecreaseQuantityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ((!TextUtils.isEmpty(EditQuantityText.getText())) && (!EditQuantityText.getText().toString().equals("0")))
                    EditQuantityText.setText(String.valueOf(Integer.parseInt(EditQuantityText.getText().toString()) - 1));
                else
                    EditQuantityText.setText("0");
            }
        });

        SaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveEntry();
            }
        });

        orderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent phoneIntent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phoneNumberData));
                startActivity(phoneIntent);
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        if (mCurrentItemUri == null) {
            MenuItem editMenuItem = menu.findItem(R.id.edit_icon_id);
            MenuItem deleteMenuItem = menu.findItem(R.id.delete_icon_id);
            editMenuItem.setVisible(false);
            deleteMenuItem.setVisible(false);


        } else {
            SaveButton.setVisibility(View.GONE);
            DecreaseQuantityButton.setVisibility(View.GONE);
            IncreaseQuantityButton.setVisibility(View.GONE);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.edit_icon_id: {
                SaveButton.setVisibility(View.VISIBLE);
                orderButton.setVisibility(View.GONE);
                DecreaseQuantityButton.setVisibility(View.VISIBLE);
                IncreaseQuantityButton.setVisibility(View.VISIBLE);
                showTextsAsEditable();
                break;
            }
            case android.R.id.home:
                super.onBackPressed();

            case R.id.delete_icon_id: {
                showDeleteConfirmationDialog();
                break;
            }
        }
        return true;
    }

    private void showDeleteConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.delete_dialog_message);
        builder.setPositiveButton(R.string.delete_message, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deleteBook();
            }
        });
        builder.setNegativeButton(R.string.cancel_message, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (dialog != null)
                    dialog.dismiss();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void deleteBook() {
        if (mCurrentItemUri != null) {
            int rowDeleted = getContentResolver().delete(mCurrentItemUri, null, null);

            if (rowDeleted == 0) {
                Toast.makeText(this, R.string.delete_message_failed_toast_message, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, R.string.delete_book_successful_toast_msg, Toast.LENGTH_SHORT).show();
            }
        }
        finish();
    }

    private void showTextsAsEditable() {
        EditBookNameText.setFocusableInTouchMode(true);
        EditQuantityText.setFocusableInTouchMode(true);
        EditPhoneNumberText.setFocusableInTouchMode(true);
        EditSupplierNameText.setFocusableInTouchMode(true);
        EditPriceText.setFocusableInTouchMode(true);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projection = {
                InventoryDataColumns._ID,
                InventoryDataColumns.PRODUCT_NAME,
                InventoryDataColumns.SUPPLIER_NAME,
                InventoryDataColumns.PRICE,
                InventoryDataColumns.QUANTITY,
                InventoryDataColumns.SUPPLIER_PHONE_NUMBER};
        return new CursorLoader(this, mCurrentItemUri, projection, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (data == null || data.getCount() < 1) {
            return;
        }

        if (data.moveToFirst()) {
            int bookNameColumnIndex = data.getColumnIndex(InventoryDataColumns.PRODUCT_NAME);
            int supplierNameColumnIndex = data.getColumnIndex(InventoryDataColumns.SUPPLIER_NAME);
            int quantityColumnIndex = data.getColumnIndex(InventoryDataColumns.QUANTITY);
            int priceColumnIndex = data.getColumnIndex(InventoryDataColumns.PRICE);
            int phoneNumberColumnIndex = data.getColumnIndex(InventoryDataColumns.SUPPLIER_PHONE_NUMBER);

            EditBookNameText.setText(data.getString(bookNameColumnIndex));
            EditSupplierNameText.setText(data.getString(supplierNameColumnIndex));
            EditQuantityText.setText(String.valueOf(data.getInt(quantityColumnIndex)));
            EditPriceText.setText(String.valueOf(data.getInt(priceColumnIndex)));
            EditPhoneNumberText.setText(data.getString(phoneNumberColumnIndex));

        }

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        EditBookNameText.setText("");
        EditSupplierNameText.setText("");
        EditQuantityText.setText("");
        EditPriceText.setText("");
        EditPhoneNumberText.setText("");
    }

    private void saveEntry() {
        EditBookNameText.setFocusable(true);
        EditQuantityText.setFocusable(true);
        EditPhoneNumberText.setFocusable(true);
        EditSupplierNameText.setFocusable(true);
        EditPriceText.setFocusable(true);

        String productNameData = EditBookNameText.getText().toString().trim();
        String supplierNameData = EditSupplierNameText.getText().toString().trim();
        String quantityData = (EditQuantityText.getText().toString().trim());
        String priceData = (EditPriceText.getText().toString().trim());
        phoneNumberData = EditPhoneNumberText.getText().toString().trim();

        if (EditBookNameText.getText().toString().length() <= 0) {
            EditBookNameText.setError(getString(R.string.enter_book_name_error_msg));
            return;
        } else
            EditBookNameText.setError(null);
        if (EditSupplierNameText.getText().toString().length() <= 0) {
            EditSupplierNameText.setError(getString(R.string.enter_supplier_name_error_msg));
            return;
        } else
            EditSupplierNameText.setError(null);

        if (EditPriceText.getText().toString().trim().length() <= 0) {
            EditPriceText.setError(getString(R.string.enter_price_error_msg));
            return;
        } else
            EditPriceText.setError(null);

        if ((EditPhoneNumberText.getText().toString().length()) != 10) {
            EditPhoneNumberText.setError(getString(R.string.enter_phne_number_error_msg));
            return;
        }

        ContentValues values = new ContentValues();
        values.put(InventoryDataColumns.PRODUCT_NAME, productNameData);
        values.put(InventoryDataColumns.SUPPLIER_NAME, supplierNameData);
        values.put(InventoryDataColumns.PRICE, Integer.parseInt((priceData)));
        values.put(InventoryDataColumns.QUANTITY, Integer.parseInt(quantityData));
        values.put(InventoryDataColumns.SUPPLIER_PHONE_NUMBER, phoneNumberData);

        if (mCurrentItemUri == null) {
            Uri tempUri = getContentResolver().insert(InventoryDataColumns.CONTENT_URI, values);
            if (tempUri == null) {
                Toast.makeText(this, R.string.failed_inserting_book, Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, R.string.insert_book_successful, Toast.LENGTH_SHORT).show();
            }
        } else {
            int rowsAffected = getContentResolver().update(mCurrentItemUri, values, null, null);
            if (rowsAffected == 0) {
                Toast.makeText(this, R.string.update_book_failed, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, R.string.update_book_success, Toast.LENGTH_SHORT).show();
            }
        }
        finish();
    }
}


