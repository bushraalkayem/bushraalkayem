package com.example.android.mysellingapplication;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.example.android.mysellingapplication.data.PrContract;

public class EditActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        int getPosition = getIntent().getIntExtra("position",0);
        String[] projection = {PrContract.productsEntry._ID,PrContract.productsEntry.COLUMN_QUANTITY,PrContract.productsEntry.COLUMN_PRODUCT_NAME,PrContract.productsEntry.COLUMN_PRICE,
                PrContract.productsEntry.COLUMN_SUPPLIER_NAME,PrContract.productsEntry.COLUMN_SUPPLIER_EMAIL,PrContract.productsEntry.COLUMN_SUPPLIER_PHONE_NUMBER};
        Cursor cursor = getContentResolver().query(PrContract.productsEntry.CONTENT_URI,projection,null,null,null);
        cursor.moveToPosition(getPosition);

        final EditText editTextPrice = findViewById(R.id.text_price1_edit);
        final EditText editTextProduct = findViewById(R.id.text_name1_edit);
        final EditText editTextQuantity = findViewById(R.id.text_quantity_tow_edit);
        final EditText editTextSupplierName = findViewById(R.id.text_Supplier_Name1_edit);
        final EditText editTextSupplierEmail = findViewById(R.id.text_email1_edit);
        final EditText editTextSupplierPhone = findViewById(R.id.phone_edit);

        int nameColumnIndex = cursor.getColumnIndex(PrContract.productsEntry.COLUMN_PRODUCT_NAME);
        int priceColumnIndex = cursor.getColumnIndex(PrContract.productsEntry.COLUMN_PRICE);
        int quantityColumnIndex = cursor.getColumnIndex(PrContract.productsEntry.COLUMN_QUANTITY);
        int supplierNameColumnIndex = cursor.getColumnIndex(PrContract.productsEntry.COLUMN_SUPPLIER_NAME);
        int supplierEmailColumnIndex = cursor.getColumnIndex(PrContract.productsEntry.COLUMN_SUPPLIER_EMAIL);
        int supplierPhoneColumnIndex = cursor.getColumnIndex(PrContract.productsEntry.COLUMN_SUPPLIER_PHONE_NUMBER);
        String supplierName = cursor.getString(supplierNameColumnIndex);
        final String supplierEmail = cursor.getString(supplierEmailColumnIndex);
        final String supplierPhone = cursor.getString(supplierPhoneColumnIndex);
        final String name = cursor.getString(nameColumnIndex);
        Integer price = cursor.getInt(priceColumnIndex);
        final Integer[] quantity = {cursor.getInt(quantityColumnIndex)};
        editTextPrice.setText(price + "");
        editTextProduct.setText(name);
        editTextQuantity.setText(quantity[0] + "");
        editTextSupplierName.setText(supplierName);
        editTextSupplierEmail.setText(supplierEmail);
        editTextSupplierPhone.setText(supplierPhone);

        Button increaseEditButton = findViewById(R.id.increase_edit);
        Button decreaseEditButton = findViewById(R.id.decrease_edit);
        ImageView deleteEditButton = findViewById(R.id.delete_edit);
        ImageView callEditButton = findViewById(R.id.detailes_call_edit);
        ImageView saveEditButton = findViewById(R.id.checked_edit);
        ImageView back = findViewById(R.id.arrow_edit);

        saveEditButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editTextQuantity.getText().toString().isEmpty() || editTextPrice.getText().toString().isEmpty() || editTextProduct.getText().toString().isEmpty() ||
                        editTextSupplierName.getText().toString().isEmpty() ||
                        editTextSupplierPhone.getText().toString().isEmpty()) {
                    Toast.makeText(EditActivity.this,"You need to fill everywhere.",Toast.LENGTH_SHORT).show();
                } else if (isValidEmail(editTextSupplierEmail.getText().toString().trim()) == false) {
                    Toast.makeText(EditActivity.this,"You need to right your mail address correctly.",Toast.LENGTH_SHORT).show();

                } else {
                    ContentValues values = new ContentValues();
                    values.put(PrContract.productsEntry.COLUMN_QUANTITY,editTextQuantity.getText().toString());
                    values.put(PrContract.productsEntry.COLUMN_PRICE,editTextPrice.getText().toString());
                    values.put(PrContract.productsEntry.COLUMN_PRODUCT_NAME,editTextProduct.getText().toString());
                    values.put(PrContract.productsEntry.COLUMN_SUPPLIER_NAME,editTextSupplierName.getText().toString());
                    values.put(PrContract.productsEntry.COLUMN_SUPPLIER_EMAIL,editTextSupplierEmail.getText().toString());
                    values.put(PrContract.productsEntry.COLUMN_SUPPLIER_PHONE_NUMBER,editTextSupplierPhone.getText().toString());
                    getContentResolver().update(PrContract.productsEntry.CONTENT_URI,values,PrContract.productsEntry.COLUMN_PRODUCT_NAME + "=?",new String[]{name});
                    finish();

                }

            }
        });

        deleteEditButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder1 = new AlertDialog.Builder(EditActivity.this);
                builder1.setMessage("Are you sure this record will be deleted.");
                builder1.setCancelable(true);

                builder1.setPositiveButton(
                        "Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                MainActivity.getInstance().delete(name);
                                finish();
                                dialog.cancel();
                            }
                        });
                builder1.setNegativeButton(
                        "No",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alert = builder1.create();
                alert.show();

            }
        });
        increaseEditButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                quantity[0] += 1;
                editTextQuantity.setText(quantity[0] + "");

            }
        });
        decreaseEditButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (quantity[0] > 0) {
                    quantity[0] -= 1;
                    editTextQuantity.setText(quantity[0] + "");
                }
            }
        });
        callEditButton.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("MissingPermission")
            @Override
            public void onClick(View v) {
                if (supplierEmail.isEmpty() == false) {

                    String uri = "tel:" + supplierPhone.trim();
                    Intent intent = new Intent(Intent.ACTION_DIAL);
                    intent.setData(Uri.parse(uri));
                    startActivity(intent);
                }
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2 = new Intent(EditActivity.this,DetailesActivity.class);
                startActivity(intent2);
            }
        });
    }

    public final static boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }


}