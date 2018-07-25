package com.example.alon.foodapp.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.alon.foodapp.classes.Product;
import com.example.alon.foodapp.R;

import java.util.ArrayList;


public class MainActivity extends ActionBarActivity {
    private ArrayList<Product> allProduct; // all the product
    private EditText IP; // the ip the user enter
    private SharedPreferences settings; // the cash memory class
    private String lastIP; // the last ip that was used
    private static String TAG = "MainActivity ";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "start MainActivity");
        //set Invisible Buttons
        Button theButton = (Button)findViewById(R.id.startButton);
        theButton.setVisibility(View.VISIBLE);
        theButton.setBackgroundColor(Color.TRANSPARENT);

        Button theButton2 = (Button)findViewById(R.id.AM_button);
        theButton2.setVisibility(View.VISIBLE);
        theButton2.setBackgroundColor(Color.TRANSPARENT);


        allProduct = new ArrayList<Product>();

        //
        IP = (EditText) findViewById(R.id.IPEditText);

        settings = getPreferences(MODE_PRIVATE);
        //if where is ip that was used it will get it if not it will get the Default ip
        if(CheckPreference())
        {
            Log.d(TAG, "DefaultIP");
            DefaultIP();
            lastIP = IP.getText().toString();
            Log.d(TAG, "lastIP = " + lastIP);
        }
        else{
            lastIP  =  settings.getString("IP","123");
            Log.d(TAG, "lastIP = " + lastIP);
            if(lastIP != null)
            IP.setText(lastIP);
        }
//

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    //get to the user List
    //go to the next Activity of user product
    public void nextActivity(View view)
    {
        Intent next = new Intent(this,userListActivity.class);


        SharedPreferences.Editor editor = editor = settings.edit();

        //save the ip
        //if the ip is equals to the last ip that was enter it do no save it else it is

        if(IP.getText().toString().equals(lastIP))
        {

        }
        else
        {
            editor.putString("IP", IP.getText().toString());
            editor.commit();
        }

//
        next.putExtra("allUserProduct", allProduct); // send all the products
        next.putExtra("ips", IP.getText().toString()); // send the server ip
        //next.putExtra("ips", "82.81.6.42");
        next.putExtra("start", "start");
        startActivity(next);
        this.finish();
    }


// set the server Default IP
    public void DefaultIP()
    {
        IP.setText("82.81.6.42");
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("IP","82.81.6.42");
        editor.putString("flag","yes");
        editor.commit();
    }
    //Check if the user enter other ip before
    public boolean CheckPreference()
    {
        String flag = settings.getString("flag","no");
        if(flag.equals("no"))
        {
            return true;
        }

        return false;
    }
    // go to the Activity of Search Recipe by name
    public void lookByRecipe(View v)
    {
        Intent next = new Intent(this,Search_Recipe_Activity.class);

        SharedPreferences.Editor editor = settings.edit();

        if(IP.getText().toString().equals(lastIP))
        {

        }
        else
        {
            editor.putString("IP", IP.getText().toString());
            editor.commit();
        }


        next.putExtra("ips", IP.getText().toString());
        //next.putExtra("ips", "82.81.6.42");
        startActivity(next);
        this.finish();
    }


}
