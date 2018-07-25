package com.example.alon.foodapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import com.example.alon.foodapp.R;
import com.example.alon.foodapp.classes.Product;
import java.util.ArrayList;

/**
 * Created by Alon on 26/05/2015.
 */
public class whatMissToOrder extends ActionBarActivity {

    private TextView id;
    private String ips;
    private String allS,where;
    private ListView howToList;
    private ArrayList<Product> allProduct;
    String TAG = "what Miss To Order";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.what_miss_to_order_layout);
        howToList = (ListView) findViewById(R.id.whatMissListView);

        Intent fromAll = getIntent();
        //get all info
        String[] howTo = fromAll.getExtras().getString("re").split("@");
        ips =   fromAll.getExtras().getString("ips");
        allS = fromAll.getExtras().getString("Recipes");
        //get all pro of the user
        Bundle b = getIntent().getExtras();
        if(b!=null)
        {
            allProduct = (ArrayList<Product>)b.getSerializable("allUserProduct");
            if(allProduct==null)
                allProduct = new ArrayList<>();
        }
        //show the user what he missing
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, howTo);

        howToList.setAdapter(adapter);
    }

   public void backTotoRecipeList(View v)
   {
       Intent toRecipeList = new Intent(whatMissToOrder.this,recipesListActivity.class);
       toRecipeList.putExtra("ips", ips);
       toRecipeList.putExtra("Recipes", allS);
       toRecipeList.putExtra("allUserProduct", allProduct);
       toRecipeList.putExtra("where", "user");
       startActivity(toRecipeList);
       this.finish();
   }


    @Override
    public void onBackPressed()
    {
        Intent toRecipeList = new Intent(whatMissToOrder.this,recipesListActivity.class);
        toRecipeList.putExtra("ips", ips);
        toRecipeList.putExtra("Recipes", allS);
        toRecipeList.putExtra("allUserProduct", allProduct);
        toRecipeList.putExtra("where", "user");
        startActivity(toRecipeList);
        this.finish();
    }

}
