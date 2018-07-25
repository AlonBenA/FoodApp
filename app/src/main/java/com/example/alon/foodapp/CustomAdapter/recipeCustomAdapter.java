package com.example.alon.foodapp.CustomAdapter;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.alon.foodapp.R;
import com.example.alon.foodapp.activity.whatMissToOrder;
import com.example.alon.foodapp.classes.Product;
import com.example.alon.foodapp.classes.Recipe;

public class recipeCustomAdapter extends ArrayAdapter<Recipe> {

    Context context;
    int layoutResourceId;
    private ArrayList<Recipe> data = new ArrayList<Recipe>();
    private String[] all;
    private Socket client;
    private String message,ips,ms,where ;
    private String allS,TAG = "recipeCustomAdapter";
    private ArrayList<Product> allProduct;

    public recipeCustomAdapter(Context context, int layoutResourceId,ArrayList<Recipe> data,String[] all,String ips,String allS)
    {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
        this.all = all;
        this.ips = ips;
        this.allS = allS;
        where = "Search";
    }
    public recipeCustomAdapter(Context context, int layoutResourceId,ArrayList<Recipe> data,String[] all,String ips,String allS,ArrayList<Product> allProduct)
    {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
        this.all = all;
        this.ips = ips;
        this.allS = allS;
        this.allProduct = allProduct;
        where = "user";
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent)
    {
        View row = convertView;
        ProductHolder holder = null;
        String sub;

        if (row == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);

            holder = new ProductHolder();
            holder.textName = (TextView) row.findViewById(R.id.recipesNameView);
          //  holder.RecipeExplain = (TextView) row.findViewById(R.id.RecipeTextView);
            //holder.btnRecipe = (Button) row.findViewById(R.id.seeRecipeButton);
            holder.btnMissPro = (ImageButton) row.findViewById(R.id.seeMissProButton);

            row.setTag(holder);
        } else {
            holder = (ProductHolder) row.getTag();
        }
        Recipe recipe = data.get(position);

        if(recipe.getName().charAt(0) =='%')
        {
            sub = recipe.getName().substring(1);
            holder.textName.setText(sub);
           // holder.textName.setTextColor(0xffff0000);
        }
        else
        {
        holder.textName.setText(recipe.getName());
            holder.btnMissPro.setVisibility(View.INVISIBLE);

        }

      //  holder.RecipeExplain.setText(recipe.getExplain());
/*
        holder.btnRecipe.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {


                message = ""+all[(position*2)+1];
                SendMessage sendMessageTask = new SendMessage();
                sendMessageTask.execute();
                ms = null;

            }
        });
*/
        //send the server the all the user pro and get back all the miss pro
        holder.btnMissPro.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Recipe recipe = data.get(position);

                if(recipe.getName().charAt(0) =='%')
                {
                    Log.d(TAG,"enter by %");
                    message = ""+all[(position*2)+1];
                    Log.d(TAG,"add "+ all[(position*2)+1]);
                    Log.d(TAG,"allProduct.size() = "+  allProduct.size());

                    for(int i=0;i<allProduct.size();i++)
                        message+=allProduct.get(i).toTXT()+'@';
                    Log.d(TAG,"add Product");

                //    Toast.makeText(context,message , Toast.LENGTH_LONG).show();
                    SendMessagePro sendMessageTask = new SendMessagePro();
                    Log.d(TAG,"set sendMessageTask");
                    sendMessageTask.execute();
                    Log.d(TAG, "sendMessageTask execute");
                    ms = null;
                    Log.d(TAG, "ms = null");
                }
                else
                {
                    Toast.makeText(context, "אין מוצרים חסרים ",
                            Toast.LENGTH_LONG).show();
                }
            }
        });

        return row;
    }

    static class ProductHolder {
        TextView textName;
        TextView RecipeExplain;
        Button btnRecipe;
        ImageButton btnMissPro;
    }


    //SEE THE MISS PRO
    private class SendMessagePro extends AsyncTask<Void, Void, Void>
    {

        @Override
        protected void onPreExecute()
        {
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                client = new Socket(ips, 10000); // connect to the server

                ObjectOutputStream outToClient =
                        new ObjectOutputStream(client.getOutputStream());
                outToClient.flush();

                ObjectInputStream inFromClient =
                        new ObjectInputStream(client.getInputStream());
                Log.d(TAG, "send "+ message);
                outToClient.writeObject("m" + message); // write the message to output stream
                outToClient.flush();

                Object b = inFromClient.readObject();
                Log.d(TAG, "get Object");
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
        //go to what miss
        Intent SeeMiss = new Intent(context,whatMissToOrder.class);
        SeeMiss.putExtra("re",ms);
        SeeMiss.putExtra("ips",ips);
        SeeMiss.putExtra("Recipes",allS);
            if(where.equals("user"))
            {
                SeeMiss.putExtra("allUserProduct", allProduct);
            }
        context.startActivity(SeeMiss);
        ((Activity) context).finish();

        }
    }
}
