package com.example.android.mysellingapplication.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;


public class PrContract {
    PrContract() {
    }

    public static final String CONTENT_AUTHORITY = "com.example.android.mysellingapplication";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String PATH_PRODUCTS = "products";


    public static final class productsEntry implements BaseColumns {
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI,PATH_PRODUCTS);

        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_PRODUCTS;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_PRODUCTS;

        public final static String TABLE_NAME = "PRODUCTS";
        public final static String _ID = BaseColumns._ID;
        public static final String COLUMN_PRODUCT_NAME = "productName";
        public static final String COLUMN_PRICE = "price";
        public static final String COLUMN_QUANTITY = "quantity";
        public static final String COLUMN_SUPPLIER_NAME = "supplierName";
        public static final String COLUMN_SUPPLIER_EMAIL = "supplierEmail";
        public static final String COLUMN_SUPPLIER_PHONE_NUMBER = "supplierPhoneNumber";
        public static final String SQL_CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " ("
                + _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_PRODUCT_NAME + " TEXT , "
                + COLUMN_PRICE + " INTEGER NOT NULL DEFAULT 0, "
                + COLUMN_QUANTITY + " INTEGER NOT NULL DEFAULT 0, "
                + COLUMN_SUPPLIER_NAME + " TEXT , "
                + COLUMN_SUPPLIER_EMAIL + " TEXT , "
                + COLUMN_SUPPLIER_PHONE_NUMBER + " TEXT );";

    }
}
