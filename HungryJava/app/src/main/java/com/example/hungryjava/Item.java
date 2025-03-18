package com.example.hungryjava;

public class Item {
    private String name;
    private int quantity;

    public Item(String name, int quantity)
    {
        this.name = name;
        this.quantity = quantity;
    }

    public void setQuantity(int newAmount)
    {
        this.quantity=newAmount;
    }

    public int getQuantity()
    {
        return quantity;
    }

    public String getName()
    {
        return name;
    }
}
