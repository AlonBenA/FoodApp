package com.example.alon.foodapp.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.example.alon.foodapp.classes.Product;
import com.example.alon.foodapp.classes.ProductType;
import com.example.alon.foodapp.R;

import java.util.ArrayList;


public class Product_Type_list_Activity extends ActionBarActivity {

    private ArrayList<ProductType> allProductType;
    private ArrayList<Product> allProduct;
    private ArrayList<Product> allNewProduct;
    private ListView lv;
    private String ips, allProFromServer, TAG = "Product_Type_list_Activity";
    private String[] ProFromServer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_type_list_layout);
        allProductType = new ArrayList<>();
        getAll();
        setAllProductType();

        //set all the buttons

        Button theButton = (Button)findViewById(R.id.milkButton);
        theButton.setVisibility(View.VISIBLE);
        theButton.setBackgroundColor(Color.TRANSPARENT);

        Button theButton2 = (Button)findViewById(R.id.fishButton);
        theButton2.setVisibility(View.VISIBLE);
        theButton2.setBackgroundColor(Color.TRANSPARENT);

        Button theButton3 = (Button)findViewById(R.id.MeatButton);
        theButton3.setVisibility(View.VISIBLE);
        theButton3.setBackgroundColor(Color.TRANSPARENT);

        Button theButton4 = (Button)findViewById(R.id.fruitsButton);
        theButton4.setVisibility(View.VISIBLE);
        theButton4.setBackgroundColor(Color.TRANSPARENT);

        Button theButton5 = (Button)findViewById(R.id.generalButton);
        theButton5.setVisibility(View.VISIBLE);
        theButton5.setBackgroundColor(Color.TRANSPARENT);


        Button theButton6 = (Button)findViewById(R.id.VegetableButton);
        theButton6.setVisibility(View.VISIBLE);
        theButton6.setBackgroundColor(Color.TRANSPARENT);


        Button theButton7 = (Button)findViewById(R.id.legumesButton);
        theButton7.setVisibility(View.VISIBLE);
        theButton7.setBackgroundColor(Color.TRANSPARENT);


        Button theButton8 = (Button)findViewById(R.id.StaffButton);
        theButton8.setVisibility(View.VISIBLE);
        theButton8.setBackgroundColor(Color.TRANSPARENT);


        Button theButton9 = (Button)findViewById(R.id.spicesButton);
        theButton9.setVisibility(View.VISIBLE);
        theButton9.setBackgroundColor(Color.TRANSPARENT);

        Button theButton10 = (Button)findViewById(R.id.ptl_add);
        theButton10.setVisibility(View.VISIBLE);
        theButton10.setBackgroundColor(Color.TRANSPARENT);

        Button theButton11 = (Button)findViewById(R.id.ptl_Search);
        theButton11.setVisibility(View.VISIBLE);
        theButton11.setBackgroundColor(Color.TRANSPARENT);



    }
    //get all the info needed
    public void getAll() {
        Intent fromBefore = getIntent();
        Bundle b = getIntent().getExtras();
        if (b != null) {
            allProduct = (ArrayList<Product>) b.getSerializable("allUserProduct");
            Log.d(TAG, "get allProduct ");
            allNewProduct = (ArrayList<Product>) b.getSerializable("newProduct");
            Log.d(TAG, "get allNewProduct ");
        }
        ips = fromBefore.getExtras().getString("ips");
        Log.d(TAG, "get ips " + ips);
        allProFromServer = fromBefore.getExtras().getString("ProFromServer");
        Log.d(TAG, "get allProFromServer " + allProFromServer);
        ProFromServer = allProFromServer.split("@");
    }

    //set all the Product Type that there is and get all the product to each type
    public void setAllProductType() {
        int i, j;
        i = 0;

        allProductType.add(new ProductType());
        allProductType.add(new ProductType());
        allProductType.add(new ProductType());
        allProductType.add(new ProductType());
        allProductType.add(new ProductType());
        allProductType.add(new ProductType());
        allProductType.add(new ProductType());
        allProductType.add(new ProductType());
        allProductType.add(new ProductType());

        allProductType.get(0).setName("בשר");
        allProductType.get(1).setName("דגנים/קיטניות");
        allProductType.get(2).setName("חלבי");
        allProductType.get(3).setName("ירקות");
        allProductType.get(4).setName("כללי");
        allProductType.get(5).setName("פירות");
        allProductType.get(6).setName("תבלינים");
        allProductType.get(7).setName("דגים");
        allProductType.get(8).setName("משקאות");

        Log.d(TAG, "set by  "+allProFromServer);
        while (i < ProFromServer.length) {
            for (j = 0; j < allProductType.size(); j++) {
                if (ProFromServer[i].equals(allProductType.get(j).getName())) {
                    allProductType.get(j).addProduct(new Product(ProFromServer[i + 1], 0.0, "units"));
                }
            }

            i += 2;
        }


    }


    public void notAddTheNew(View view) {
        Intent backToUser = new Intent(this, userListActivity.class);
        backToUser.putExtra("allUserProduct", allProduct);
        backToUser.putExtra("start", "no");
        backToUser.putExtra("ips", ips);
        startActivity(backToUser);
        this.finish();
    }

    @Override
    public void onBackPressed() {
        Intent backToUser = new Intent(this, userListActivity.class);
        backToUser.putExtra("allUserProduct", allProduct);
        backToUser.putExtra("start", "no");
        backToUser.putExtra("ips", ips);
        startActivity(backToUser);
        this.finish();
    }
    //add all the new pro and go back to the user list
    public void AddTheNew(View view) {
        int i;

        for (i = 0; i < allNewProduct.size(); i++) {
            allProduct.add(allNewProduct.get(i));
        }

        Intent backToUser = new Intent(this, userListActivity.class);
        backToUser.putExtra("allUserProduct", allProduct);
        backToUser.putExtra("start", "no");
        backToUser.putExtra("ips", ips);
        startActivity(backToUser);
        this.finish();
    }
    //go to the activity add pro by search
    public void AddTheNewBySearch(View view) {
        Intent GotoSearch = new Intent(this, SearchForAProductActivity.class);
        GotoSearch.putExtra("allUserProduct", allProduct);
        GotoSearch.putExtra("allNewProduct", allNewProduct);
        Log.d(TAG, "send: " + allProFromServer);
        GotoSearch.putExtra("ProFromServer", allProFromServer);
        GotoSearch.putExtra("ips", ips);
        GotoSearch.putExtra("where", "type");
        GotoSearch.putExtra("edit", "no");
        startActivity(GotoSearch);
        this.finish();
    }

    //see all the meat pro
    public void seeMeat(View view)
    {
        Intent toSnow = new Intent(Product_Type_list_Activity.this, product_from_type_Activity.class);
        toSnow.putExtra("allUserProduct", allProduct);
        toSnow.putExtra("ProductToSnow", allProductType.get(0).getAllP());
        toSnow.putExtra("allNewProduct", allNewProduct);
        toSnow.putExtra("ProFromServer", allProFromServer);
        toSnow.putExtra("name", "meat");
        toSnow.putExtra("ips", ips);
        startActivity(toSnow);
        Product_Type_list_Activity.this.finish();
    }
    //see all the Legumes pro
    public void seeLegumes(View view)
    {
        Intent toSnow = new Intent(Product_Type_list_Activity.this, product_from_type_Activity.class);
        toSnow.putExtra("allUserProduct", allProduct);
        toSnow.putExtra("ProductToSnow", allProductType.get(1).getAllP());
        toSnow.putExtra("allNewProduct", allNewProduct);
        toSnow.putExtra("ProFromServer", allProFromServer);
        toSnow.putExtra("name", "Legumes");
        toSnow.putExtra("ips", ips);
        startActivity(toSnow);
        Product_Type_list_Activity.this.finish();
    }

    //see all the Milk pro
    public void seeMilk(View view)
    {
        Intent toSnow = new Intent(Product_Type_list_Activity.this, product_from_type_Activity.class);
        toSnow.putExtra("allUserProduct", allProduct);
        toSnow.putExtra("ProductToSnow", allProductType.get(2).getAllP());
        toSnow.putExtra("allNewProduct", allNewProduct);
        toSnow.putExtra("ProFromServer", allProFromServer);
        toSnow.putExtra("ips", ips);
        toSnow.putExtra("name", "Milk");
        startActivity(toSnow);
        Product_Type_list_Activity.this.finish();
    }
    //see all the Vegetable pro
    public void seeVegetable(View view)
    {
        Intent toSnow = new Intent(Product_Type_list_Activity.this, product_from_type_Activity.class);
        toSnow.putExtra("allUserProduct", allProduct);
        toSnow.putExtra("ProductToSnow", allProductType.get(3).getAllP());
        toSnow.putExtra("allNewProduct", allNewProduct);
        toSnow.putExtra("ProFromServer", allProFromServer);
        toSnow.putExtra("name", "Vegetable");
        toSnow.putExtra("ips", ips);
        startActivity(toSnow);
        Product_Type_list_Activity.this.finish();
    }
    //see all the General pro
    public void seeGeneral(View view)
    {
        Intent toSnow = new Intent(Product_Type_list_Activity.this, product_from_type_Activity.class);
        toSnow.putExtra("allUserProduct", allProduct);
        toSnow.putExtra("ProductToSnow", allProductType.get(4).getAllP());
        toSnow.putExtra("allNewProduct", allNewProduct);
        toSnow.putExtra("ProFromServer", allProFromServer);
        toSnow.putExtra("name", "Genera");
        toSnow.putExtra("ips", ips);
        startActivity(toSnow);
        Product_Type_list_Activity.this.finish();
    }

    //see all the Fruit pro
    public void seeFruit(View view)
    {
        Intent toSnow = new Intent(Product_Type_list_Activity.this, product_from_type_Activity.class);
        toSnow.putExtra("allUserProduct", allProduct);
        toSnow.putExtra("ProductToSnow", allProductType.get(5).getAllP());
        toSnow.putExtra("allNewProduct", allNewProduct);
        toSnow.putExtra("ProFromServer", allProFromServer);
        toSnow.putExtra("name", "Fruit");
        toSnow.putExtra("ips", ips);
        startActivity(toSnow);
        Product_Type_list_Activity.this.finish();
    }
    //see all the Spices pro
    public void seeSpices(View view)
    {
        Intent toSnow = new Intent(Product_Type_list_Activity.this, product_from_type_Activity.class);
        toSnow.putExtra("allUserProduct", allProduct);
        toSnow.putExtra("ProductToSnow", allProductType.get(6).getAllP());
        toSnow.putExtra("allNewProduct", allNewProduct);
        toSnow.putExtra("ProFromServer", allProFromServer);
        toSnow.putExtra("name", "Spices");
        toSnow.putExtra("ips", ips);
        startActivity(toSnow);
        Product_Type_list_Activity.this.finish();
    }
    //see all the Fish pro
    public void seeFish(View view)
    {
        Intent toSnow = new Intent(Product_Type_list_Activity.this, product_from_type_Activity.class);
        toSnow.putExtra("allUserProduct", allProduct);
        toSnow.putExtra("ProductToSnow", allProductType.get(7).getAllP());
        toSnow.putExtra("allNewProduct", allNewProduct);
        toSnow.putExtra("ProFromServer", allProFromServer);
        toSnow.putExtra("name", "Fish");
        toSnow.putExtra("ips", ips);
        startActivity(toSnow);
        Product_Type_list_Activity.this.finish();
    }

    //see all the drink pro
    public void seeStaff(View view)
    {
        Intent toSnow = new Intent(Product_Type_list_Activity.this, product_from_type_Activity.class);
        toSnow.putExtra("allUserProduct", allProduct);
        toSnow.putExtra("ProductToSnow", allProductType.get(8).getAllP());
        toSnow.putExtra("allNewProduct", allNewProduct);
        toSnow.putExtra("ProFromServer", allProFromServer);
        toSnow.putExtra("ips", ips);
        toSnow.putExtra("name", "drink");
        startActivity(toSnow);
        Product_Type_list_Activity.this.finish();
    }
}
