package com.example.alon.foodapp.CustomAdapter;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.example.alon.foodapp.R;
import com.example.alon.foodapp.classes.Product;
import java.util.ArrayList;


/**
 * Created by Alon on 08/05/2015.
 */
public class ProductFromTypeCustomAdapter extends ArrayAdapter<Product> {

    Context context;
    int layoutResourceId;
    ArrayList<Product> showData = new ArrayList<Product>();
    ArrayList<Product> recentData = new ArrayList<Product>();
    ArrayList<Product> allProduct = new ArrayList<Product>();
    ArrayList<Product> allNewProduct = new ArrayList<Product>();
    private static String TAG = "Product From Type Custom Adapter";

    public ProductFromTypeCustomAdapter(Context context, int layoutResourceId,ArrayList<Product> recentData,ArrayList<Product> showData,ArrayList<Product> allProduct, ArrayList<Product> allNewProduct)
    {
        super(context, layoutResourceId, showData);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.recentData = recentData;
        this.showData = showData;
        if(this.showData == null) this.showData = new ArrayList<>();
        this.allProduct = allProduct;
        this.allNewProduct = allNewProduct;
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
            holder.textName = (TextView) row.findViewById(R.id.PFTIL_product_name);
            row.setTag(holder);

        } else {
            holder = (ProductHolder) row.getTag();
        }
        if(showData.get(position) !=null)
        {
            Product product = showData.get(position);
            Log.d(TAG, "set product to " + showData.get(position).getName());
            holder.textName.setText(product.getName());
            Log.d(TAG, "set textName to " + product.getName());
        }

        return row;
    }



    static class ProductHolder {
        TextView textName;
    }
}
