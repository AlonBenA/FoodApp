package com.example.alon.foodapp.activity;


import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import com.example.alon.foodapp.CustomAdapter.recipeCustomAdapter;
import com.example.alon.foodapp.R;
import com.example.alon.foodapp.classes.Product;
import com.example.alon.foodapp.classes.Recipe;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

public class recipesListActivity extends ActionBarActivity {
    ArrayList<Recipe> allRecipe;
    recipeCustomAdapter RCAdapter;
    ListView listView;
    private String ips,all,from;
    private ArrayList<Product> allProduct;
    private String message,ms;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recipes_list_layout);
        listView = (ListView) findViewById(R.id.recipeListView);
        int i;
        Intent fromUser = getIntent();
        ips = fromUser.getExtras().getString("ips");
        //all the recipes
        all = fromUser.getExtras().getString("Recipes");
        //where the rec come from
        from = ""+fromUser.getExtras().getString("where");
        //if user list pro in need to get Pro also and then set all the recipes
        if(from.equals("user"))
        {
            Bundle b = getIntent().getExtras();
            if(b!=null)
            {
                allProduct = (ArrayList<Product>)b.getSerializable("allUserProduct");
                if(allProduct==null)
                    allProduct = new ArrayList<>();
            }

            String[] ba;
            if (all.equals("")) {
                Toast.makeText(recipesListActivity.this, "אין מתכונים עם המוצרים שבחרת", Toast.LENGTH_LONG).show();
            } else
            {
                ba = all.split("@");
                allRecipe = new ArrayList<Recipe>();

                for (i = 0; i < ba.length; i += 2)
                {
                    allRecipe.add(new Recipe(ba[i], ""));
                }


                RCAdapter = new recipeCustomAdapter(recipesListActivity.this, R.layout.recipe_row, allRecipe, ba, ips, all,allProduct);
                listView.setItemsCanFocus(false);
                listView.setAdapter(RCAdapter);
            }
        }
        else if(from.equals("Search"))
        {
            //if by search name in need to get Pro also and then set all the recipes
            String[] ba;

            if (all.equals("")) {
                Toast.makeText(recipesListActivity.this, "אין מתכונים עם המוצרים שבחרת", Toast.LENGTH_LONG).show();
            } else
            {
                ba = all.split("@");

                allRecipe = new ArrayList<Recipe>();
                for (i = 0; i < ba.length; i += 2)
                {
                    allRecipe.add(new Recipe(ba[i], ""));

                }

                RCAdapter = new recipeCustomAdapter(recipesListActivity.this, R.layout.recipe_row, allRecipe, ba, ips, all);
                listView.setItemsCanFocus(false);
                listView.setAdapter(RCAdapter);
            }


        }


        //if user click on item it get the code of the recipe and send it to server
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View v, final int position, long id) {
                //Toast.makeText(recipesListActivity.this, "List View Clicked:" + position, Toast.LENGTH_LONG).show();

                String[] b = all.split("@");
                message = ""+ b[(position*2)+1];
                SendMessage sendMessageTask = new SendMessage();
                sendMessageTask.execute();
                ms = null;

            }
        });
    }

    //go to main activity
    public void toStart(View view) {
        Intent toStart = new Intent(this,MainActivity.class);
        startActivity(toStart);
        this.finish();
    }
    //class that ask the server for the recipe
    private class SendMessage extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute()
        {

        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                Socket client = new Socket(ips, 10000); // connect to the server

                ObjectOutputStream outToClient =
                        new ObjectOutputStream(client.getOutputStream());
                outToClient.flush();

                ObjectInputStream inFromClient =
                        new ObjectInputStream(client.getInputStream());

                outToClient.writeObject("r"+message); // write the message to output stream
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
        //when it get the the recipe it go to ho do do recipe
            Intent seeRecipe = new Intent(recipesListActivity.this,howToDoRecipe.class);
            seeRecipe.putExtra("re",ms);
            seeRecipe.putExtra("ips",ips);
            seeRecipe.putExtra("where",from);
            seeRecipe.putExtra("Recipes", all);

            if(from.equals("user")) {
                seeRecipe.putExtra("allUserProduct", allProduct);
            }

            recipesListActivity.this.startActivity(seeRecipe);
            recipesListActivity.this.finish();
        }
    }

}
