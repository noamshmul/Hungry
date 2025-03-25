package com.example.hungryjava;

public class Item {
    private String name;
    private long id;
    private double quantity;

    public Item(String name, double quantity, long id)
    {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
    }

    public void setQuantity(double newAmount)
    {
        this.quantity=newAmount;
    }

    public double getQuantity()
    {
        return quantity;
    }

    public String getName()
    {
        return name;
    }

    public long getID()
    {
        return id;
    }
}
