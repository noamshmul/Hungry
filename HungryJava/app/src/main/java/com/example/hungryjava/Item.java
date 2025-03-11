package com.example.hungryjava;

public class Item {
    private String name;
    private Double quantity;

    public Item(String name, Double quantity)
    {
        this.name = name;
        this.quantity = quantity;
    }

    public void setQuantity(Double newAmount)
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
