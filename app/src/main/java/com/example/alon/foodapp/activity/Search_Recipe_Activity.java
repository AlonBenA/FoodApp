package com.example.alon.foodapp.activity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.example.alon.foodapp.R;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Created by Alon on 13/05/2015.
 */
public class Search_Recipe_Activity extends ActionBarActivity {
    private EditText NameOfRecipeEditText;
    private Socket client;
    private String messageTo,ips,messageFrom;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_recipe_layout);
        getAll();

        NameOfRecipeEditText = (EditText) findViewById(R.id.SRL_editText);

    }
    //get all the info needed
    protected  void getAll()
    {
        Intent fromBefore = getIntent();
        ips = fromBefore.getExtras().getString("ips");

    }
    //see if there is internet connection
    protected boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        } else {
            return false;
        }
    }
    //Check if the user search word and if there is internet connection is there is so it ask the server
    public void startSearch(View view)
    {
        if(NameOfRecipeEditText.getText().toString().equals(""))
        {
            Toast.makeText(this, "You need to add name of Recipe",
                    Toast.LENGTH_LONG).show();
        }
        else if(!isOnline())
        {
            Toast.makeText(this, "You can not connect",
                    Toast.LENGTH_LONG).show();
        }
        else
        {
            SendMessage sendMessageTask = new SendMessage();
            messageFrom = null;
            sendMessageTask.execute();
        }


    }

    public void onBackPressed()
    {
        super.onBackPressed();
        Intent toStart = new Intent(Search_Recipe_Activity.this, MainActivity.class);
        startActivity(toStart);
        Search_Recipe_Activity.this.finish();
    }
    //send the word to the sever and wait for answer
    private class SendMessage extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute()
        {
            //pb.setVisibility(View.VISIBLE);

            messageTo = NameOfRecipeEditText.getText().toString();
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                client = new Socket(ips, 10000); // connect to the server
                ObjectOutputStream outToClient = new ObjectOutputStream(client.getOutputStream());
                outToClient.flush();
                ObjectInputStream inFromClient = new ObjectInputStream(client.getInputStream());
                outToClient.writeObject("s"+messageTo); // write the message to output stream
                outToClient.flush();


                Object b = inFromClient.readObject();

                if(b instanceof String)
                {
                    messageFrom = (String) b;
                }


                outToClient.close();
                inFromClient.close();
                client.close(); // closing the connection

            } catch (UnknownHostException e) {

                e.printStackTrace();

            } catch (IOException e) {

                e.printStackTrace();
            }catch (ClassNotFoundException e)
            {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid)
        {
            //pb.setVisibility(View.INVISIBLE);
    //if there is no recipe by name tell the user, if there is it go to recipe list and show all the recipes with that word in
        if(messageFrom.equals(""))
        {
            Toast.makeText(Search_Recipe_Activity.this, "there is no recipe in that name",
                    Toast.LENGTH_LONG).show();
        }
         else
        {
            Intent seeRecipe = new Intent(Search_Recipe_Activity.this, recipesListActivity.class);
            seeRecipe.putExtra("Recipes", messageFrom);
            seeRecipe.putExtra("ips", ips);
            seeRecipe.putExtra("where", "Search");
            startActivity(seeRecipe);
            Search_Recipe_Activity.this.finish();
        }
        }
    }
}
