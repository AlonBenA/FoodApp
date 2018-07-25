package com.example.alon.foodapp.classes;


import java.io.Serializable;

/**
 * Created by Alon on 21/04/2015.
 */
public class Product implements Serializable {
    private String ProductName;
    private Double Quantity;
    private String Unit;

    public Product(String name,Double Quantity,String Unit)
    {
        super();
        this.ProductName = name;
        this.Quantity = Quantity;
        this.Unit =Unit;
    }

    public Product(Product a)
    {
        this.ProductName = a.getName();
        this.Quantity = a.getQuantity();
        this.Unit = a.getUnit();
    }


    public String toString() {
        return "Name=" + ProductName + ", Quantity=" + Quantity
                + ", Unit=" + Unit + "";
    }

    public String getName() {

        return ProductName;
    }

    public void setName(String ProductName) {

        this.ProductName = ProductName;
    }

    public String getStringQuantity() {
        return ""+Quantity;
    }

    public Double getQuantity() {
        return Quantity;
    }


    public void setQuantity(Double quantity) {
        Quantity = quantity;
    }

    public String getUnit() {
        return Unit;
    }

    public void setUnit(String unit) {
        Unit = unit;
    }

    public String toTXT() {
        return ProductName + ":" + Quantity
                + ":" + Unit + "";
    }



}
