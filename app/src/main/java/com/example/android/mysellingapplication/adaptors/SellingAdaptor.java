package com.example.android.mysellingapplication.adaptors;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.example.android.mysellingapplication.R;
import com.example.android.mysellingapplication.data.PrContract;

/**
 * Created by Recodedharran on 2/7/2018.
 */

public class SellingAdaptor  extends CursorAdapter {
    private Context ctx;
    public SellingAdaptor(Context context,Cursor c) {
        super(context, c, 0 /* flags */);
        this.ctx = context;

    }

    @Override
    public View newView(Context context,Cursor cursor,ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.list, parent, false);

    }

    @Override
    public void bindView(View view,Context context,Cursor cursor) {

        TextView textViewPrice = (TextView) view.findViewById(R.id.Price);
        TextView textViewProductName = (TextView) view.findViewById(R.id.productName);
        final TextView textViewQuantity = (TextView) view.findViewById(R.id.quantity);


        int nameColumnIndex = cursor.getColumnIndex(PrContract.productsEntry.COLUMN_PRODUCT_NAME);
        int priceColumnIndex = cursor.getColumnIndex(PrContract.productsEntry.COLUMN_PRICE);
        int quantityColumnIndex = cursor.getColumnIndex(PrContract.productsEntry.COLUMN_QUANTITY);
        final String name = cursor.getString(nameColumnIndex);

        Integer price = cursor.getInt(priceColumnIndex);
        final Integer[] quantity = {cursor.getInt(quantityColumnIndex)};
        textViewPrice.setText(price + "");
        textViewProductName.setText(name);
        textViewQuantity.setText(quantity[0] + "");
        Button reduceButton = (Button) view.findViewById(R.id.sell_me);
        reduceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (quantity[0] > 0) {
                    quantity[0] -= 1;
                    ContentValues datas = new ContentValues();
                    datas.put(PrContract.productsEntry.COLUMN_QUANTITY, quantity[0]);
                    ctx.getContentResolver().update(PrContract.productsEntry.CONTENT_URI, datas, PrContract.productsEntry.COLUMN_PRODUCT_NAME + "=?", new String[]{name});
                    textViewQuantity.setText(quantity[0] + "");

                }

            }
        });
    }
}
