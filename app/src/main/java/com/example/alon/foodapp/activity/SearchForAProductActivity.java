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
import android.widget.ListView;

import com.example.alon.foodapp.classes.Product;
import com.example.alon.foodapp.R;
import com.example.alon.foodapp.CustomAdapter.SearchProductCustomAdapter;

import java.util.ArrayList;


public class SearchForAProductActivity extends ActionBarActivity {

    ArrayList<Product> allSnowProduct;
    private ArrayList<Product> allProduct,allNewProduct;
    SearchProductCustomAdapter SPCAAdapter;
    private String ips,edit,fromWhereICome,allProFromServer;
    private int pos;
    private ListView lv;
    private EditText inputSearch;
    private String[] ProFromServer;
    private static String TAG = "Search For A Product Activity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_for_product_layout);

        Log.d(TAG, "start get all");
        getAll();
        Log.d(TAG, "end get all and start setAllProduct");
        setAllProduct();
        Log.d(TAG, "end get set All Product");

        //set all the pro for user too see
        for(int i=0 ;i<allSnowProduct.size();i++)
        {
            Log.d(TAG, "allProduct size = " + allProduct.size());
            for(int j=0 ;j<allProduct.size();j++)
            {
                Log.d(TAG, "start i = " + i + " j = " + j);
                if(allSnowProduct.get(i)!= null && allProduct.get(j)!=null)
                {
                    Log.d(TAG, "temp.get(i) = " + allSnowProduct.get(i));
                    Log.d(TAG, "allProduct.get(j) " + allProduct.get(j));
                    if (allSnowProduct.get(i).getName().equals(allProduct.get(j).getName()))
                    {
                        Log.d(TAG, "before remove from temp " + allSnowProduct.get(i) + " that is in allProduct ");
                        Log.d(TAG, "temp size = " + allSnowProduct.size());
                        allSnowProduct.get(i).setName("no");
                        Log.d(TAG, "remove was Done");
                    }
                }
                Log.d(TAG, "end i = " + i + " j = " + j);
            }
        }

        //if user come from pro type list in need to delete from another array
        if(fromWhereICome.equals("type"))
        {
            for(int i=0 ;i<allSnowProduct.size();i++)
            {
                Log.d(TAG, "allProduct size = " + allProduct.size());
                for(int j=0 ;j<allNewProduct.size();j++)
                {
                    Log.d(TAG, "start i = " + i + " j = " + j);
                    if(allSnowProduct.get(i)!= null && allNewProduct.get(j)!=null)
                    {
                        Log.d(TAG, "temp.get(i) = " + allSnowProduct.get(i));
                        Log.d(TAG, "allProduct.get(j) " + allNewProduct.get(j));
                        if (allSnowProduct.get(i).getName().equals(allNewProduct.get(j).getName()))
                        {
                            Log.d(TAG, "before remove from temp " + allSnowProduct.get(i) + " that is in allProduct ");
                            Log.d(TAG, "temp size = " + allSnowProduct.size());
                            allSnowProduct.get(i).setName("no");
                            Log.d(TAG, "remove was Done");
                        }
                    }
                    Log.d(TAG, "end i = " + i + " j = " + j);
                }
            }
        }



        //remove all the product that were already selected
        for(int i=allSnowProduct.size()-1;i>=0;i--)
        {
            if(allSnowProduct.get(i).getName().equals("no"))
                allSnowProduct.remove(i);
        }

        lv = (ListView) findViewById(R.id.SFPL_list_view);
        inputSearch = (EditText) findViewById(R.id.inputSearchP);

        // Adding items to listview

        SPCAAdapter = new SearchProductCustomAdapter(SearchForAProductActivity.this, R.layout.search_item_layout,allSnowProduct);
        //adapter = new ArrayAdapter<String>(this, R.layout.list_item, R.id.product_name, products);
        lv.setItemsCanFocus(false);
        lv.setAdapter(SPCAAdapter);

        //if user Typing someting
        inputSearch.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                // When user changed the Text
                ArrayList<Product> temp = new ArrayList<Product>();
                int textLength = inputSearch.getText().length();
                temp.clear();
                for (int i = 0; i < allSnowProduct.size(); i++)
                {
                    if (textLength <= allSnowProduct.get(i).getName().length())
                    {
                        if(inputSearch.getText().toString().equalsIgnoreCase(
                                (String)
                                        allSnowProduct.get(i).getName().subSequence(0,
                                                textLength)))
                        {
                            temp.add(allSnowProduct.get(i));
                        }
                    }
                }
                lv.setAdapter(new SearchProductCustomAdapter(SearchForAProductActivity.this, R.layout.search_item_layout, temp));


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
        //if the user click on item
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                int i;
                boolean flag=false;

                ArrayList<Product> temp = new ArrayList<Product>();
                int textLength = inputSearch.getText().length();
                temp.clear();
                for (i = 0; i < allSnowProduct.size(); i++)
                {
                    if (textLength <= allSnowProduct.get(i).getName().length())
                    {
                        if(inputSearch.getText().toString().equalsIgnoreCase(
                                (String)
                                        allSnowProduct.get(i).getName().subSequence(0,
                                                textLength)))
                        {
                            temp.add(allSnowProduct.get(i));
                        }
                    }
                }

                if(fromWhereICome.equals("user"))
                {
                    //if there is copy of the Product it will not add it
                    for (i = 0; i < allProduct.size(); i++) {
                        if (allSnowProduct.get(position).getName().equals(allProduct.get(i).getName())) {
                            flag = true;
                        }
                    }

                    //if there is copy of the Product it will not add it
                    if (flag)
                    {
                        Intent backToUser = new Intent(SearchForAProductActivity.this, userListActivity.class);
                        backToUser.putExtra("allUserProduct", allProduct);
                        backToUser.putExtra("start", "no");
                        backToUser.putExtra("ips", ips);
                        startActivity(backToUser);
                        SearchForAProductActivity.this.finish();
                    }
                    else
                    {

                        //else it see if it need to add the product or Modify an existing product
                        if (edit.equals("yes")) {
                            allProduct.set(pos, allSnowProduct.get(position));
                        } else {
                            allProduct.add(temp.get(position));
                        }

                        Intent backToUser = new Intent(SearchForAProductActivity.this, userListActivity.class);
                        backToUser.putExtra("allUserProduct", allProduct);
                        backToUser.putExtra("start", "no");
                        backToUser.putExtra("ips", ips);
                        startActivity(backToUser);
                        SearchForAProductActivity.this.finish();
                    }
                }
                else
                {
                        flag=false;
                        //if there is copy of the Product it will not add it
                        for(i=0;i<allNewProduct.size();i++)
                    {
                        if(allSnowProduct.get(position).getName().equals(allNewProduct.get(i).getName()))
                        {
                            flag = true;
                        }
                    }
                            if(flag)
                            {
                            Intent backToTypeList = new Intent(SearchForAProductActivity.this, Product_Type_list_Activity.class);
                            backToTypeList.putExtra("allUserProduct", allProduct);
                            backToTypeList.putExtra("newProduct", allNewProduct);
                            backToTypeList.putExtra("ProFromServer",allProFromServer);
                            backToTypeList.putExtra("ips", ips);
                            startActivity(backToTypeList);
                            SearchForAProductActivity.this.finish();
                            }
                            else
                            {
                            allNewProduct.add(temp.get(position));
                            Intent backToTypeList = new Intent(SearchForAProductActivity.this, Product_Type_list_Activity.class);
                            backToTypeList.putExtra("allUserProduct", allProduct);
                            backToTypeList.putExtra("newProduct", allNewProduct);
                            backToTypeList.putExtra("ProFromServer",allProFromServer);
                            backToTypeList.putExtra("ips", ips);
                            startActivity(backToTypeList);
                            SearchForAProductActivity.this.finish();
                            }


                }

            }
        });

    }
//set all the pro for user to see
    public void setAllProduct()
    {
        int i=0;
        allSnowProduct = new ArrayList<>();
        Log.d(TAG, ""+allProFromServer);
        ProFromServer = allProFromServer.split("@");
        Log.d(TAG, "ProFromServer.length: "+ProFromServer.length);
        while(i<ProFromServer.length)
        {
            allSnowProduct.add(new Product(ProFromServer[i+1],0.0,"units"));
            Log.d(TAG, "add: " + ProFromServer[i+1]+ " i = " + i);
            i+=2;
        }

        Log.d(TAG, "end: setAllProduct " );


    }
//get all the info needed
    public void getAll()
    {
        Intent fromBefore = getIntent();
        Bundle b = getIntent().getExtras();
        if(b!=null)
        {
            allProduct = (ArrayList<Product>)b.getSerializable("allUserProduct");
        }
        ips =  fromBefore.getExtras().getString("ips");
        edit = fromBefore.getExtras().getString("edit");
        fromWhereICome = fromBefore.getExtras().getString("where");

        if(fromWhereICome.equals("type"))
        {
            allNewProduct = (ArrayList<Product>)b.getSerializable("allNewProduct");
        }

        if(edit.equals("yes"))
        {
            pos = fromBefore.getExtras().getInt("edit number");
        }

        allProFromServer = fromBefore.getExtras().getString("ProFromServer");
    }


    @Override
    public void onBackPressed() {

        Intent backToTypeList = new Intent(SearchForAProductActivity.this, Product_Type_list_Activity.class);
        backToTypeList.putExtra("allUserProduct", allProduct);
        backToTypeList.putExtra("newProduct", allNewProduct);
        backToTypeList.putExtra("ProFromServer", allProFromServer);
        backToTypeList.putExtra("start", "no");
        backToTypeList.putExtra("ips", ips);
        startActivity(backToTypeList);
        SearchForAProductActivity.this.finish();
    }
}
