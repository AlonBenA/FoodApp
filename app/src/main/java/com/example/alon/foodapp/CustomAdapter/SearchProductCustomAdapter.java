package com.example.alon.foodapp.CustomAdapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.alon.foodapp.R;
import com.example.alon.foodapp.classes.Product;

import java.util.ArrayList;

//set all the pro in Search by name pro
public class SearchProductCustomAdapter  extends ArrayAdapter<Product> {

    Context context;
    int layoutResourceId;
    ArrayList<Product> data = new ArrayList<>();

    public SearchProductCustomAdapter(Context context, int layoutResourceId,ArrayList<Product> data)
    {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
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
            holder.textName = (TextView) row.findViewById(R.id.SIL_product_name);
            row.setTag(holder);
        } else {
            holder = (ProductHolder) row.getTag();
        }
        Product product = data.get(position);
        holder.textName.setText(product.getName());


        return row;
    }

    static class ProductHolder {
        TextView textName;
    }
}
