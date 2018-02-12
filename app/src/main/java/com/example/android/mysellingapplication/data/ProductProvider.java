package com.example.android.mysellingapplication.data;

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
import android.widget.Toast;


public class ProductProvider extends ContentProvider {
    public static final String LOG_TAG = ProductProvider.class.getSimpleName();

    private static final int PRODUCTS = 1;

    private static final int PRODUCTS_ID = 2;
    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        sUriMatcher.addURI(PrContract.CONTENT_AUTHORITY,PrContract.PATH_PRODUCTS,PRODUCTS);
        sUriMatcher.addURI(PrContract.CONTENT_AUTHORITY,PrContract.PATH_PRODUCTS + "/#",PRODUCTS_ID);

    }

    private PrDbHelper myPrDbHelper;

    @Override
    public boolean onCreate() {
        myPrDbHelper = new PrDbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri,@Nullable String[] projection,@Nullable String selection,@Nullable String[] selectionArgs,@Nullable String sortOrder) {
        SQLiteDatabase database = myPrDbHelper.getReadableDatabase();
        Cursor cursor;
        int match = sUriMatcher.match(uri);

        switch (match) {

            case PRODUCTS:

                cursor = database.query(PrContract.productsEntry.TABLE_NAME,projection,null,null,null,null,sortOrder);
                break;
            case PRODUCTS_ID:

                selection = PrContract.productsEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                cursor = database.query(PrContract.productsEntry.TABLE_NAME,projection,selection,selectionArgs,null,null,sortOrder);

                break;
            default:


                throw new IllegalArgumentException("Cannot query unknown " + uri);


        }

        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case PRODUCTS:
                return PrContract.productsEntry.CONTENT_LIST_TYPE;
            case PRODUCTS_ID:
                return PrContract.productsEntry.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalStateException("Unknown URI " + uri + " with match " + match);
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri,@Nullable ContentValues contentValues) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case PRODUCTS:
                return insertProducts(uri,contentValues);
            default:
                throw new IllegalArgumentException("Insertion is not supported for " + uri);
        }
    }

    private Uri insertProducts(Uri uri,ContentValues values) {
        SQLiteDatabase database = myPrDbHelper.getWritableDatabase();
        long id = database.insert(PrContract.productsEntry.TABLE_NAME,null,values);
        Integer quantity = values.getAsInteger(PrContract.productsEntry.COLUMN_QUANTITY);
        if (quantity < 0) {
            throw new IllegalArgumentException("quantity can not be less than zero");
        }
        String supplierName = values.getAsString(PrContract.productsEntry.COLUMN_SUPPLIER_NAME);
        if (supplierName == null) {
            throw new IllegalArgumentException("supplier name  can not be null");
        }
        String supplierEmail = values.getAsString(PrContract.productsEntry.COLUMN_SUPPLIER_EMAIL);
        if (supplierEmail == null) {
            throw new IllegalArgumentException("supplier email can not be null");
        }
        String supplierPhone = values.getAsString(PrContract.productsEntry.COLUMN_SUPPLIER_PHONE_NUMBER);
        if (supplierPhone == null) {
            throw new IllegalArgumentException("supplier phone number can not be null");
        }
        String productName = values.getAsString(PrContract.productsEntry.COLUMN_PRODUCT_NAME);
        if (productName == null) {
            throw new IllegalArgumentException("product name can not be null");
        }
        Integer price = values.getAsInteger(PrContract.productsEntry.COLUMN_PRICE);
        if (price < 0) {
            throw new IllegalArgumentException("price requires valid number");
        }
        if (id == -1) {
            Log.e("Row","row cannot be equals to -1" + uri);
            Toast.makeText(getContext(),"error row !=-1",Toast.LENGTH_SHORT).show();
            return null;
        }

        return ContentUris.withAppendedId(uri,id);
    }


    @Override
    public int delete(@NonNull Uri uri,@Nullable String selection,@Nullable String[] selectionArgs) {
        SQLiteDatabase database = myPrDbHelper.getWritableDatabase();

        final int match = sUriMatcher.match(uri);
        switch (match) {
            case PRODUCTS:
                return database.delete(PrContract.productsEntry.TABLE_NAME,selection,selectionArgs);
            case PRODUCTS_ID:
                selection = PrContract.productsEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                return database.delete(PrContract.productsEntry.TABLE_NAME,selection,selectionArgs);
            default:
                throw new IllegalArgumentException("Deletion is not supported for " + uri);
        }
    }

    @Override
    public int update(@NonNull Uri uri,@Nullable ContentValues contentValues,@Nullable String selection,@Nullable String[] selectionArgs) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case PRODUCTS:
                return updateProdect(uri,contentValues,selection,selectionArgs);
            case PRODUCTS_ID:

                selection = PrContract.productsEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                return updateProdect(uri,contentValues,selection,selectionArgs);
            default:
                throw new IllegalArgumentException("Update is not supported for " + uri);
        }

    }

    private int updateProdect(Uri uri,ContentValues values,String selection,String[] selectionArgs) {
        if (values.containsKey(PrContract.productsEntry.COLUMN_PRODUCT_NAME)) {
            String name = values.getAsString(PrContract.productsEntry.COLUMN_PRODUCT_NAME);
            if (name == null) {
                throw new IllegalArgumentException("Product requires a name");
            }
        }
        if (values.containsKey(PrContract.productsEntry.COLUMN_PRICE)) {
            Integer price = values.getAsInteger(PrContract.productsEntry.COLUMN_PRICE);
            if (price == null) {
                throw new IllegalArgumentException("Pet requires valid gender");
            }

        }
        if (values.containsKey(PrContract.productsEntry.COLUMN_QUANTITY)) {
            Integer quantity = values.getAsInteger(PrContract.productsEntry.COLUMN_QUANTITY);
            if (quantity != null && quantity < 0) {
                throw new IllegalArgumentException("Pet requires valid weight");
            }
        }
        //supemail
        if (values.containsKey(PrContract.productsEntry.COLUMN_SUPPLIER_EMAIL)) {
            String supplierEmail = values.getAsString(PrContract.productsEntry.COLUMN_SUPPLIER_EMAIL);
            if (supplierEmail == null) {
                throw new IllegalArgumentException("Product requires a name");
            }
        }
        //supnumber
        if (values.containsKey(PrContract.productsEntry.COLUMN_SUPPLIER_PHONE_NUMBER)) {
            String supplierNumber = values.getAsString(PrContract.productsEntry.COLUMN_SUPPLIER_PHONE_NUMBER);
            if (supplierNumber == null) {
                throw new IllegalArgumentException("Product requires a name");
            }
        }
        //suppname
        if (values.containsKey(PrContract.productsEntry.COLUMN_SUPPLIER_NAME)) {
            String supplierName = values.getAsString(PrContract.productsEntry.COLUMN_SUPPLIER_NAME);
            if (supplierName == null) {
                throw new IllegalArgumentException("Product requires a name");
            }
        }
        if (values.size() == 0) {
            return 0;
        }

        SQLiteDatabase database = myPrDbHelper.getWritableDatabase();

        int rowsUpdated = database.update(PrContract.productsEntry.TABLE_NAME,values,selection,selectionArgs);
        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri,null);
        }

        return rowsUpdated;
    }
}
