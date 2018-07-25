package com.example.alon.foodapp.classes;

import java.util.ArrayList;

/**
 * Created by Alon on 08/05/2015.
 */
public class ProductType {
    ArrayList<Product> allP;
    String name;

    public ProductType() {
        allP = new ArrayList<>();
    }


    public ArrayList<Product> getAllP() {
        return allP;
    }

    public void setAllP(ArrayList<Product> allP) {
        this.allP = allP;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void addProduct(Product p) {
    allP.add(p);
}
}
