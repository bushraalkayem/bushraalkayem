package com.example.android.mysellingapplication;

import android.content.ContentValues;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.android.mysellingapplication.data.PrContract;

public class AddActivity extends AppCompatActivity {
    EditText editTextProductName;
    EditText editTextPrice;
    EditText editTextQuantity;
    EditText editTextSupplierName;
    EditText editTextSupplierEmail;
    EditText editTextSupplierPhone;
    ImageView plus;
    ImageView arrow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        Toolbar toolbar = (Toolbar) findViewById(R.id.add_activity_toolbar);
        setSupportActionBar(toolbar);
        editTextProductName = (EditText) findViewById(R.id.text_add_name1);
        editTextPrice = (EditText) findViewById(R.id.text_add_price1);
        editTextQuantity = (EditText) findViewById(R.id.text_add_quantity_tow);
        editTextSupplierName = (EditText) findViewById(R.id.text_add_Supplier_Name1);
        editTextSupplierEmail = (EditText) findViewById(R.id.text_add_email1);
        editTextSupplierPhone = (EditText) findViewById(R.id.add_Phone);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        plus = (ImageView) findViewById(R.id.add_cheked_image_view);
        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertData();
                finish();
            }
        });
        arrow = (ImageView) findViewById(R.id.add_return_image_viev);
        arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });
    }

    private void insertData() {
        if (editTextPrice.getText().toString().trim().isEmpty() || editTextProductName.getText().toString().trim().isEmpty() || editTextQuantity.getText().toString().trim().isEmpty() || editTextSupplierName.getText().toString().trim().isEmpty() || editTextSupplierPhone.getText().toString().trim().isEmpty()) {
            Toast.makeText(this,"All information is needed.",Toast.LENGTH_SHORT).show();

        } else if (isValidEmail(editTextSupplierEmail.getText().toString().trim()) == false) {
            Toast.makeText(this,"You need to right your mail address correctly.",Toast.LENGTH_SHORT).show();

        } else {

            ContentValues datas = new ContentValues();
            datas.put(PrContract.productsEntry.COLUMN_PRICE,Integer.parseInt(editTextPrice.getText().toString()));
            datas.put(PrContract.productsEntry.COLUMN_PRODUCT_NAME,editTextProductName.getText().toString());
            datas.put(PrContract.productsEntry.COLUMN_QUANTITY,Integer.parseInt(editTextQuantity.getText().toString()));
            datas.put(PrContract.productsEntry.COLUMN_SUPPLIER_NAME,editTextSupplierName.getText().toString());
            datas.put(PrContract.productsEntry.COLUMN_SUPPLIER_EMAIL,editTextSupplierEmail.getText().toString());
            datas.put(PrContract.productsEntry.COLUMN_SUPPLIER_PHONE_NUMBER,editTextSupplierPhone.getText().toString());
            getContentResolver().insert(PrContract.productsEntry.CONTENT_URI,datas);
            finish();
        }
    }

    public final static boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }
}
