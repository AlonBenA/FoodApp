package com.example.alon.foodapp.activity;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.example.alon.foodapp.classes.Product;
import com.example.alon.foodapp.CustomAdapter.ProductCustomAdapter;
import com.example.alon.foodapp.R;


/**
 * Created by Alon on 20/04/2015.
 */
public class userListActivity extends Activity {
    private ArrayList<Product> allProduct; //all the Products that user Choose
    private ProductCustomAdapter PCAdapter; // the Adapter that show the Products
    private ListView listView;
    private Socket client; // the way to connect to Server
    private String message,ips,ms ;
    private String TAG = "userListActivity";
    private static ProgressBar pb; // is the user wait for Reply from server
    private SharedPreferences settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_list_layout);
        pb = (ProgressBar) findViewById(R.id.UserProgressBar);
        pb.setVisibility(View.INVISIBLE);
        settings = getPreferences(MODE_PRIVATE);



        Intent fromStart = getIntent();
        ips = fromStart.getExtras().getString("ips");
        String start = "" + fromStart.getExtras().getString("start");
        //get allProduct

        Bundle b = getIntent().getExtras();
        if(b!=null)
        {
            allProduct = (ArrayList<Product>)b.getSerializable("allUserProduct");
            if(allProduct==null)
                allProduct = new ArrayList<>();
            //check that there is no double from the same Product
            if(allProduct.size()>0 )
            {
                for(int i=0;i<allProduct.size()-1;i++)
                {
                    for(int j=i+1;j<allProduct.size();j++)
                    {
                        if(allProduct.get(i).getName().equals(allProduct.get(j).getName()))
                        {
                            allProduct.remove(j);
                        }
                    }
                }
            }
        }

        if(CheckPreference() && start.equals("start"))
        {
            getLastPro();
        }


        //set the list
        listView = (ListView) findViewById(R.id.userListView);
        PCAdapter = new ProductCustomAdapter(userListActivity.this, R.layout.row,allProduct,ips);
        listView.setItemsCanFocus(false);
        listView.setAdapter(PCAdapter);

        //if the item was click print on screen
        listView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View v,final int position, long id) {
                //Toast.makeText(userListActivity.this,"List View Clicked:" + position, Toast.LENGTH_LONG).show();
            }
        });

    }
    //there is way to Connect to the Internet
    protected boolean isOnline()
    {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        } else {
            return false;
        }
    }

    public void getLastPro()
    {
        int i;
        SharedPreferences.Editor editor = settings.edit();
        String lastMs = settings.getString("lastPro","");
        String[] Pro = lastMs.split("@");

        for(i=0;i<Pro.length;i+=3)
        {
            allProduct.add(new Product(Pro[i],0.0,Pro[i+2]));
        }
    }

    //Check if the user enter other ip before
    public boolean CheckPreference()
    {
        String flag = settings.getString("flag","no");
        if(flag.equals("no"))
        {
            return false;
        }

        return true;
    }

    //if the user want to Check under the products that was added
    public void toRecipesList(View view) {

        //if there is no Product don't send
        if(allProduct.size()==0)
        {
            Toast.makeText(this, "אתה צריך לבחור לפחות מוצר אחד",
                    Toast.LENGTH_LONG).show();
        }
        else if(!isOnline())
        {
            //if there is no Internet don't send
            Toast.makeText(this, "אתה לא יכול להתחבר לאינטרנט",
                    Toast.LENGTH_LONG).show();
        }
        else {
    //if there is Internet and there is at least one Product send Message to server about the Product that
            SendMessage sendMessageTask = new SendMessage();
            sendMessageTask.execute();
            ms = null;
        }
    }
    //get the Products from the server
    public void addNewProducts(View view)
    {

        //if there is no Internet don't send
        if(!isOnline())
        {
            Toast.makeText(this, "You can not connect",
                    Toast.LENGTH_LONG).show();
        }
        else
        {
            //if there is Internet and ask the server what Product
            SendMessagePro sendMessageTask = new SendMessagePro();
            sendMessageTask.execute();
            ms = null;
        }
    }

    //if the user click on delete all button
    public void deleteAll(View view)
    {
        for(int i=allProduct.size()-1;i>=0;i--) allProduct.remove(i);
        allProduct = new ArrayList<>();
        PCAdapter = new ProductCustomAdapter(userListActivity.this, R.layout.row,allProduct,ips);
        listView.setAdapter(PCAdapter);
    }


    //back thread get the Recipes
    private class SendMessage extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute()
        {
            int i;
            String MessToSave = "";
            pb.setVisibility(View.VISIBLE); // snow the user
            CheckBox cb = (CheckBox) findViewById(R.id.onlyFullCheckBox);

            if(!cb.isChecked())
            {
                message = "h"; // full and half
            }
            else
            {
                message = "l"; // full
            }

            for(i=0;i<allProduct.size();i++)
            {
                message += allProduct.get(i).toTXT()+"@";
                MessToSave += allProduct.get(i).getName()+"@";
                MessToSave += allProduct.get(i).getQuantity()+"@";
                MessToSave += allProduct.get(i).getUnit()+"@";
            }

            SharedPreferences.Editor editor = editor = settings.edit();
            editor.putString("lastPro",MessToSave);
            editor.putString("flag","yes");
            editor.commit();
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                client = new Socket(ips, 10000); // connect to the server
                ObjectOutputStream outToClient = new ObjectOutputStream(client.getOutputStream());
                outToClient.flush();
                ObjectInputStream inFromClient = new ObjectInputStream(client.getInputStream());
                outToClient.writeObject(""+message); // write the message to output stream
                outToClient.flush();


                Object b = inFromClient.readObject();

                if(b instanceof String)
                {
                    ms = (String) b;
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
        protected void onPostExecute(Void aVoid) {
            pb.setVisibility(View.INVISIBLE);

            Intent RecipesList = new Intent(userListActivity.this, recipesListActivity.class);
            Log.d(TAG,"send Recipes to next Activity");
            RecipesList.putExtra("Recipes", ms);
            Log.d(TAG,"send Recipes to next Activity");
            RecipesList.putExtra("ips", ips);
            Log.d(TAG,"send ips to next Activity RecipesList "+ips);
            RecipesList.putExtra("allUserProduct", allProduct);
            Log.d(TAG,"send allProduct ");
            RecipesList.putExtra("where", "user");
            Log.d(TAG,"send that it come from user list ");
            startActivity(RecipesList);
            userListActivity.this.finish();
        }
    }


    //back thread get the Products
    private class SendMessagePro extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute()
        {
            pb.setVisibility(View.VISIBLE);
            message = "";
            Log.d(TAG,"ips is " + ips);

        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                client = new Socket(ips, 10000); // connect to the server
                ObjectOutputStream outToClient = new ObjectOutputStream(client.getOutputStream());
                outToClient.flush();
                ObjectInputStream inFromClient = new ObjectInputStream(client.getInputStream());
                outToClient.writeObject("p"+message); // write the message to output stream
                outToClient.flush();


                Object b = inFromClient.readObject();

                if(b instanceof String)
                {
                    ms = (String) b;
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
            pb.setVisibility(View.INVISIBLE);
            ArrayList<Product> allNewProduct = new ArrayList<>();
            Log.d(TAG,"Create allNewProduct  ");
            Intent addNew = new Intent(userListActivity.this,Product_Type_list_Activity.class);
            Log.d(TAG,"Create addNew Intent ");
            addNew.putExtra("allUserProduct", allProduct);
            Log.d(TAG,"send allProduct to next Activity");
            addNew.putExtra("newProduct", allNewProduct);
            Log.d(TAG,"send allNewProduct to next Activity");
            addNew.putExtra("ips" ,ips);
            Log.d(TAG,"send ips to next Activity");
            addNew.putExtra("ProFromServer",ms);
            Log.d(TAG,"send Products From Server to next Activity " + ms);
            startActivity(addNew);
            Log.d(TAG,"start Product_Type_list_Activity Activity ");
            userListActivity.this.finish();
            Log.d(TAG,"end user List Activity Activity ");

        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent toStart = new Intent(userListActivity.this, MainActivity.class);
        startActivity(toStart);
        userListActivity.this.finish();
    }
}
