package com.example.simpletodo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ArrayList<String> items;
    ArrayAdapter<String> itemsAdapter;
    ListView lvItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Initialize the Items, then the adapter, then the ListView
        readItems();
        itemsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, items);
        //Set the ListView to be the one we defined in the xml
        lvItems = (ListView) findViewById(R.id.Lvitems);
        //Set the adapter as the as our itemsAdapter
        lvItems.setAdapter(itemsAdapter);

        //add random data
        //items.add("EGG");
        //items.add("MORE EGG");

        setupListViewListener();

        //TODO: Load previous data from filesystem

    }
    public void onAddItem(View v) {
        EditText etNewItem = (EditText) findViewById(R.id.etNewItem);
        String itemText = etNewItem.getText().toString();
        itemsAdapter.add(itemText);
        etNewItem.setText("");

        //Update the file
        writeItems();

        Toast.makeText(getApplicationContext(), "Item added to list", Toast.LENGTH_SHORT).show();
        Log.i("MainActivity", "Item added to list: " + items.get(items.size() - 1));

        //TODO: add item to file for storing the list

    }

    private void setupListViewListener(){
        //On long click delete the item clicked
        Log.i("MainActivity", "Setting up listener on the ListView");
        lvItems.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener(){

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Log.i("MainActivity", "Removing item from list: " + items.get(position));
                Toast.makeText(getApplicationContext(), "Item removed from list", Toast.LENGTH_SHORT).show();
                items.remove(position);

                //Tell the adapter that we made a change so that it knows to update
                itemsAdapter.notifyDataSetChanged();

                //Update the file
                writeItems();

                return true;
            }
        });
    }

    private File getDataFile(){
        return new File(getFilesDir(), "todo.txt");
    }

    private void readItems(){
        try {
            items = new ArrayList<>(FileUtils.readLines(getDataFile(), Charset.defaultCharset()));
        }
        //Catch the exception that would be thrown if the file does not exist
        catch (IOException e) {
            //Log the exception
            Log.e("MainActivity", "Failed to read file from getDataFile()");
            //If we don't get the list from the file make a new one
            items = new ArrayList<>();
        }
    }

    private void writeItems(){
        try {
            FileUtils.writeLines(getDataFile(), items);
        }
        //Catch a write error
        catch (IOException e){
            Log.e("MainActivity", "Failed to write file getDataFile()");
        }

    }

    //TODO: Add a way to store the list to a file and open the list on app startup

}
