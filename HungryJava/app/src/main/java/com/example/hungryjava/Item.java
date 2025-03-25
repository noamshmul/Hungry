package com.example.hungryjava;

public class Item {
    private String name;
    private double quantity;

    public Item(String name, double quantity)
    {
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
}
