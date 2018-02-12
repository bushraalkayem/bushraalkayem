package com.example.android.mysellingapplication;

import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.android.mysellingapplication.adaptors.SellingAdaptor;
import com.example.android.mysellingapplication.data.PrContract;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    SellingAdaptor myAdapter;
    ListView listView;
    public static MainActivity instance;

    public static MainActivity getInstance() {
        return instance;
    }
    public void delete(String name) {
        getContentResolver().delete(PrContract.productsEntry.CONTENT_URI, PrContract.productsEntry.COLUMN_PRODUCT_NAME + "=?", new String[]{name});
        getLoaderManager().restartLoader(0, null,this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        instance = this;
        listView = (ListView) findViewById(R.id.list_view);
        getLoaderManager().initLoader(0, null, this);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,AddActivity.class);
                startActivity(intent);
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent openDetail = new Intent(MainActivity.this, DetailesActivity.class);
                openDetail.putExtra("positionDetail", position);
                startActivity(openDetail);
            }
        });

    }
    protected void onResume() {

        super.onResume();
        getLoaderManager().restartLoader(0, null, this);
    }


    private void deleteAllproject() {
     getContentResolver().delete(PrContract.productsEntry.CONTENT_URI, null, null);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        TextView textViewEmpty = (TextView) findViewById(R.id.eror);
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_delete) {
            deleteAllproject();
            listView.setVisibility(View.GONE);
            textViewEmpty.setVisibility(View.VISIBLE);
            return true;
        }else {
            listView.setVisibility(View.VISIBLE);
            textViewEmpty.setVisibility(View.GONE);}


        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id,Bundle args) {
        String[] projection = {PrContract.productsEntry._ID, PrContract.productsEntry.COLUMN_QUANTITY, PrContract.productsEntry.COLUMN_PRODUCT_NAME, PrContract.productsEntry.COLUMN_PRICE};
        return new CursorLoader(this,PrContract.productsEntry.CONTENT_URI, projection, null, null, null);

    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader,Cursor data) {
        data.moveToFirst();
        TextView textViewEmpty = (TextView) findViewById(R.id.eror);
        if (data.getCount() == 0) {
            listView.setVisibility(View.GONE);
            textViewEmpty.setVisibility(View.VISIBLE);
        } else {
            listView.setVisibility(View.VISIBLE);
            textViewEmpty.setVisibility(View.GONE);
            myAdapter=new SellingAdaptor(this,data);
            listView.setAdapter(myAdapter);
        }
    }
    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        myAdapter.swapCursor(null);
    }
}
