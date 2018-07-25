package com.example.alon.foodapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.alon.foodapp.classes.Product;
import com.example.alon.foodapp.CustomAdapter.ProductFromTypeCustomAdapter;
import com.example.alon.foodapp.R;
import java.util.ArrayList;

import static android.widget.Toast.*;


public class product_from_type_Activity extends ActionBarActivity  {

    private ArrayList<Product> allProduct;
    private ArrayList<Product> allNewProduct;
    private ArrayList<Product> ProductToSnow;
    private ArrayList<Product> allNewTypeProduct;
    private String ips,allProFromServer,name;
    private static String TAG = "product from type Activity";
    // List view
    private ListView lv;
    // Listview Adapter
    //ArrayAdapter<String> adapter;
    // Search EditText
    private EditText inputSearch;
    // ArrayList for Listview
    private ProductFromTypeCustomAdapter PFTAdapter;
    private ArrayList<Product> temp;
    private ImageView nameImageView;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_from_type_layout);
        Log.d(TAG, "before all");
        getAll();
        Log.d(TAG, "get all");

        nameImageView = (ImageView) findViewById(R.id.nameImageView);
        //set the pic on the top
        setPic();
        //set the array that the user will see
        temp = new ArrayList<Product>();
        Log.d(TAG,"set temp");
        lv = (ListView) findViewById(R.id.pftl_listView);
        Log.d(TAG,"set lv");
        inputSearch = (EditText) findViewById(R.id.inputSearch);
        Log.d(TAG,"set EditText inputSearch ");
        //set the Array were the user will add the product
        allNewTypeProduct = new ArrayList<>();
        Log.d(TAG,"set allNewTypeProduct ArrayList");

        //TextView nameView = (TextView) findViewById(R.id.textView3);
        //nameView.setText(name);

        //add to temp all the Product to show
        for(int i=0;i<ProductToSnow.size();i++)
        {
            temp.add(new Product(ProductToSnow.get(i)));
        }
        Log.d(TAG,"add all Product to temp");

        //set all the product that were already selected to no show
        for(int i=0 ;i<temp.size();i++)
        {
            Log.d(TAG, "allProduct size = " + allProduct.size());
            for(int j=0 ;j<allProduct.size();j++)
            {
                Log.d(TAG, "start i = " + i + " j = " + j);
                if(temp.get(i)!= null && allProduct.get(j)!=null)
                {
                    Log.d(TAG, "temp.get(i) = " + temp.get(i));
                    Log.d(TAG, "allProduct.get(j) " + allProduct.get(j));
                    if (temp.get(i).getName().equals(allProduct.get(j).getName()))
                    {
                        Log.d(TAG, "before remove from temp " + temp.get(i) + " that is in allProduct ");
                        Log.d(TAG, "temp size = " + temp.size());
                        temp.get(i).setName("no");
                        Log.d(TAG, "remove was Done");
                    }
                }
                Log.d(TAG, "end i = " + i + " j = " + j);
            }
        }

        //set all the product that were already selected to no show
            Log.d(TAG, "allNewProduct temp size = " + temp.size());
            for(int i=0 ; i < temp.size();i++)
            {
                for(int j=0 ;j< allNewProduct.size();j++)
                {
                    if(temp.get(i)!=null && allNewProduct.get(j)!=null)
                    {
                        if (temp.get(i).getName().equals(allNewProduct.get(j).getName())) {
                            Log.d(TAG, "before remove from temp " + temp.get(i).getName() + " that is in allNewProduct ");
                            temp.get(i).setName("no");
                            Log.d(TAG, "temp size = " + temp.size());
                        }
                    }
                    Log.d(TAG, "i = " + i + " j = " + j);
                }
            }
        //remove all the product that were already selected
        for(int i=temp.size()-1;i>=0;i--)
        {
            if(temp.get(i).getName().equals("no"))
                temp.remove(i);
        }


    // Adding items to listview
        Log.d(TAG, "before PFTAdapter");
        PFTAdapter = new ProductFromTypeCustomAdapter(product_from_type_Activity.this, R.layout.product_from_type_item_layout,allNewTypeProduct,temp,allProduct,allNewProduct);
        Log.d(TAG, "set PFTAdapter");
        lv.setItemsCanFocus(false);
        Log.d(TAG, "set set Items Can Focus");
        lv.setAdapter(PFTAdapter);
        Log.d(TAG, "set Adapter PFTAdapter with temp");
        inputSearch.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                // When user changed the Text
                ArrayList<Product> tempS = new ArrayList<Product>();
                int textLength = inputSearch.getText().length();
                tempS.clear();
                for (int i = 0; i < temp.size(); i++)
                {
                    if (textLength <= temp.get(i).getName().length())
                    {
                        if(inputSearch.getText().toString().equalsIgnoreCase(
                                (String)
                                        temp.get(i).getName().subSequence(0,
                                                textLength)))
                        {
                            tempS.add(temp.get(i));
                        }
                    }
                }
                lv.setAdapter(new ProductFromTypeCustomAdapter(product_from_type_Activity.this, R.layout.product_from_type_item_layout, allNewTypeProduct,tempS,allProduct,allNewProduct));
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                          int arg3) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub
            }
        });
        //when the user click on item
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View v,final int position, long id)
            {
                TextView textName = (TextView) v.findViewById(R.id.PFTIL_product_name);
                String toShow = "add " + textName.getText().toString();
                //show Toast , add the product and remove from the user eye
                Toast.makeText(product_from_type_Activity.this, toShow, Toast.LENGTH_SHORT).show();
                allNewTypeProduct.add(new Product(textName.getText().toString(), 0.0, "units"));

                for(int i=0;i<temp.size();i++)
                {
                    for (int j = 0; j < allNewTypeProduct.size(); j++) {
                        if (temp.get(i).getName().equals(allNewTypeProduct.get(j).getName())) {
                            temp.remove(i);
                        }
                    }
                }
                //if the user search for item it will still show what he was looking for
                ArrayList<Product> tempS = new ArrayList<Product>();
                int textLength = inputSearch.getText().length();
                tempS.clear();
                for (int i = 0; i < temp.size(); i++)
                {
                    if (textLength <= temp.get(i).getName().length())
                    {
                        if(inputSearch.getText().toString().equalsIgnoreCase(
                                (String)
                                        temp.get(i).getName().subSequence(0,
                                                textLength)))
                        {
                            tempS.add(temp.get(i));
                        }
                    }
                }
                lv.setAdapter(new ProductFromTypeCustomAdapter(product_from_type_Activity.this, R.layout.product_from_type_item_layout, allNewTypeProduct,tempS,allProduct,allNewProduct));

            }

        });

    }
        //get all the info needed
    public void getAll()
    {
        Intent edit = getIntent();
        Bundle b = getIntent().getExtras();
        if(b!=null)
        {
            allProduct = (ArrayList<Product>)b.getSerializable("allUserProduct");
            ProductToSnow = (ArrayList<Product>)b.getSerializable("ProductToSnow");
            allNewProduct = (ArrayList<Product>)b.getSerializable("allNewProduct");
        }
        ips = edit.getExtras().getString("ips");
        name = edit.getExtras().getString("name");
        allProFromServer = edit.getExtras().getString("ProFromServer");
    }

    public void returnAndNotAdd(View v)
    {
        Intent backToUser = new Intent(this,Product_Type_list_Activity.class);
        backToUser.putExtra("allUserProduct", allProduct);
        backToUser.putExtra("newProduct", allNewProduct);
        backToUser.putExtra("ProFromServer",allProFromServer);
        backToUser.putExtra("ips", ips);
        startActivity(backToUser);
        this.finish();
    }


    @Override
    public void onBackPressed() {
        Intent backToUser = new Intent(this,Product_Type_list_Activity.class);
        backToUser.putExtra("allUserProduct", allProduct);
        backToUser.putExtra("newProduct", allNewProduct);
        backToUser.putExtra("ProFromServer",allProFromServer);
        backToUser.putExtra("ips", ips);
        startActivity(backToUser);
        this.finish();
    }
//add all the new pro and go back to the list
    public void addAllNew(View v)
    {
        Intent backToUser = new Intent(this,Product_Type_list_Activity.class);

        for(int i=0;i<allNewTypeProduct.size();i++)
            allNewProduct.add(allNewTypeProduct.get(i));

        backToUser.putExtra("allUserProduct", allProduct);
        backToUser.putExtra("newProduct", allNewProduct);
        backToUser.putExtra("ProFromServer",allProFromServer);
        backToUser.putExtra("ips", ips);
        startActivity(backToUser);
        this.finish();
    }

//set the pic onn the top
    void setPic()
    {
        Log.d(TAG, "set pic f " + name);
        if(name.equals("meat"))
        {
            Log.d(TAG, "set pic to meat");
            nameImageView.setImageResource(R.drawable.meat);
        }
        else if(name.equals("Legumes"))
        {
            Log.d(TAG, "set pic to kitniot");
            nameImageView.setImageResource(R.drawable.kitniot);
        }
        else if(name.equals("Milk"))
        {
            Log.d(TAG, "set pic to milk");
            nameImageView.setImageResource(R.drawable.milk);
        }
        else if(name.equals("Vegetable"))
        {
            Log.d(TAG, "set pic to veg");
            nameImageView.setImageResource(R.drawable.veg);
        }
        else if(name.equals("Genera"))
        {
            Log.d(TAG, "set pic to klaly");
            nameImageView.setImageResource(R.drawable.klaly);
        }
        else if(name.equals("Fruit"))
        {
            Log.d(TAG, "set pic to prot");
            nameImageView.setImageResource(R.drawable.prot);
        }
        else if(name.equals("Spices"))
        {
            Log.d(TAG, "set pic to tavlinim");
            nameImageView.setImageResource(R.drawable.tavlinim);
        }
        else if(name.equals("Fish"))
        {
            Log.d(TAG, "set pic to fish");
            nameImageView.setImageResource(R.drawable.fish);
        }
        else if(name.equals("drink"))
        {
            Log.d(TAG, "set pic to drink");
            nameImageView.setImageResource(R.drawable.drink);
        }
    }
}
