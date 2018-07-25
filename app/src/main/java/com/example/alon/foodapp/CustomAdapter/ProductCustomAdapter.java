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
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
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
import com.example.alon.foodapp.activity.Product_Type_list_Activity;
import com.example.alon.foodapp.activity.SearchForAProductActivity;
import com.example.alon.foodapp.activity.userListActivity;
import com.example.alon.foodapp.classes.Product;

/**
 * Created by Alon on 21/04/2015.
 */
public class ProductCustomAdapter extends ArrayAdapter<Product>  {

    Context context;
    int layoutResourceId;
    ArrayList<Product> data = new ArrayList<Product>();
    String ips,ms;
    private Socket client;
    int thePosition;

    public ProductCustomAdapter(Context context, int layoutResourceId,ArrayList<Product> data,String ips)
    {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
        this.ips = ips;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent)
     {
        View row = convertView;
        ProductHolder holder = null;

        if (row == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);

            holder = new ProductHolder();
            holder.textName = (TextView) row.findViewById(R.id.NameView);
           // holder.textUnit = (TextView) row.findViewById(R.id.UnitView);
           // holder.textQuantity = (TextView) row.findViewById(R.id.QuantityView);
            //holder.btnEdit = (Button) row.findViewById(R.id.button1);
            holder.btnDelete = (ImageButton) row.findViewById(R.id.button2);
            row.setTag(holder);
        } else {
            holder = (ProductHolder) row.getTag();
        }
        Product product = data.get(position);
        holder.textName.setText(product.getName());
        //holder.textUnit.setText(product.getUnit());
      //  holder.textQuantity.setText(product.getStringQuantity());
       //  holder.textUnit.setText("");
       //  holder.textQuantity.setText("");


         /*
        holder.btnEdit.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v)
            {
            //    Toast.makeText(context, "Edit button Clicked",
            //            Toast.LENGTH_LONG).show();
            //edit Existing product

                    thePosition = position;
                    SendMessagePro sendMessageTask = new SendMessagePro();
                    sendMessageTask.execute();
                    ms = null;
            }
        });

        */
         //if the user click the delete button it delete the pro from the list
        holder.btnDelete.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                //Toast.makeText(context, "Delete button Clicked ",
                 //       Toast.LENGTH_LONG).show();
                if(data.size()==1){
                    data = new ArrayList<Product>();
                }
                else
                data.remove(position);
                //do Refresh
                Intent next = new Intent(context,userListActivity.class);
                next.putExtra("allUserProduct", data);
                next.putExtra("ips", ips);
                context.startActivity(next);
                ((Activity) context).finish();
            }
        });

        return row;
    }

    static class ProductHolder
    {
        TextView textName;
        TextView textUnit;
       TextView textQuantity;
        //Button btnEdit;
        ImageButton btnDelete;
    }

    //get all pro from server
    private class SendMessagePro extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute()
        {
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                client = new Socket(ips, 10000); // connect to the server
                ObjectOutputStream outToClient = new ObjectOutputStream(client.getOutputStream());
                outToClient.flush();
                ObjectInputStream inFromClient = new ObjectInputStream(client.getInputStream());
                outToClient.writeObject("p"); // write the message to output stream
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
            Intent next = new Intent(context,SearchForAProductActivity.class);
            next.putExtra("allUserProduct", data);
            next.putExtra("where", "user");
            next.putExtra("edit","yes");
            next.putExtra("edit number",thePosition);
            next.putExtra("ProFromServer",ms);
            next.putExtra("ips",ips);
            context.startActivity(next);
            ((Activity) context).finish();
        }
    }


}





