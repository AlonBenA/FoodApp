package com.example.alon.foodapp.classes;

import java.io.Serializable;


public class Recipe implements Serializable {
    String name;
    String Explain;

    public Recipe(String name,String Explain)
    {
        super();
        this.name = name;
        this.Explain = Explain;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getExplain() {
        return Explain;
    }

    public void setExplain(String explain) {
        Explain = explain;
    }

    @Override
    public String toString() {
        return "Recipe: " +
                "name = " + name + "," + Explain + " ";

    }
}
