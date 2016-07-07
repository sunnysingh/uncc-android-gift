package com.mad.sunny.afinal;

/**
 * Name: Sunny Singh
 */

import java.util.ArrayList;

/**
 * Created by sunny on 6/28/16.
 */
public class Person {

    private String name;
    private int budget;
    private int spent;
    private int giftsBought;
    private ArrayList<Gift> gifts;

    public ArrayList<Gift> getGifts() {
        return gifts;
    }

    public void setGifts(ArrayList<Gift> gifts) {
        this.gifts = gifts;
    }

    public int getSpent() {
        return spent;
    }

    public void setSpent(int spent) {
        this.spent = spent;
    }

    public int getGiftsBought() {
        return giftsBought;
    }

    public void setGiftsBought(int giftsBought) {
        this.giftsBought = giftsBought;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getBudget() {
        return budget;
    }

    public void setBudget(int budget) {
        this.budget = budget;
    }

    // Creates a friendly key for Firebase
    public static String generateKey(String name) {
        return name.toLowerCase().replaceAll("[^a-zA-Z]+", "");
    }
}
