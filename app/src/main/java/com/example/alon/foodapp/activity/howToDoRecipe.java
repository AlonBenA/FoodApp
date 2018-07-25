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
 * Created by Alon on 20/04/2015.
 */
public class howToDoRecipe extends ActionBarActivity {

    private TextView id;
    private String ips;
    private String allS,where;
    private ListView howToList;
    ArrayList<Product> allProduct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.how_to_do_recipe_layout);
        Intent fromAll = getIntent();
        id = (TextView) findViewById(R.id.theidtextView);
        howToList = (ListView) findViewById(R.id.howToDolistView);

        id.setText("");
        //get all the info from last Activity
        allS = fromAll.getExtras().getString("Recipes");
        String[] howTo = fromAll.getExtras().getString("re").split("@");
        ips =   fromAll.getExtras().getString("ips");
        where =   fromAll.getExtras().getString("where");
        //if it come from user it need to get the allProduct also
        if(where.equals("user")) {


            Bundle b = getIntent().getExtras();
            if(b!=null)
            {
                allProduct = (ArrayList<Product>)b.getSerializable("allUserProduct");
                if(allProduct==null)
                    allProduct = new ArrayList<>();
            }

        }
        //set the Adapter and show the how to make the recipe
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, howTo);

        howToList.setAdapter(adapter);
    }

    public void returnToRecipeList(View view) {
        //if user send back the product
        if(where.equals("user"))
        {
            Intent next = new Intent(this, recipesListActivity.class);
            next.putExtra("ips", ips);
            next.putExtra("allUserProduct",allProduct);
            next.putExtra("where","user");
            next.putExtra("Recipes", allS);
            startActivity(next);
            this.finish();
        }
        else
        {
            Intent next = new Intent(this, recipesListActivity.class);
            next.putExtra("ips", ips);
            next.putExtra("Recipes", allS);
            next.putExtra("where", "Search");
            startActivity(next);
            this.finish();
        }
    }

    @Override
    public void onBackPressed() {
        //if user send back the product
        if(where.equals("user"))
        {
            Intent next = new Intent(this, recipesListActivity.class);
            next.putExtra("ips", ips);
            next.putExtra("allUserProduct",allProduct);
            next.putExtra("where","user");
            next.putExtra("Recipes", allS);
            startActivity(next);
            this.finish();
        }
        else
        {
            Intent next = new Intent(this, recipesListActivity.class);
            next.putExtra("ips", ips);
            next.putExtra("Recipes", allS);
            next.putExtra("where", "Search");
            startActivity(next);
            this.finish();
        }

    }

}
