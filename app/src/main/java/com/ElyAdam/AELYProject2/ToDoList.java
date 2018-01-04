package com.ElyAdam.AELYProject2;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Owner on 11/3/2015.
 */
public class ToDoList implements Serializable {

    private String mListName;
    private String mItem;
    private ArrayList<String> mItemsInList;

    public ToDoList(String mListName, String mItem) {
        this.mItem = mItem;
        this.mListName = mListName;
        this.mItemsInList = new ArrayList<>();
    }

    public ArrayList<String> getItemsInList() {
        return mItemsInList;
    }

    public String getListName() {
        return mListName;
    }

    public void setListName(String mListName) {
        this.mListName = mListName;
    }

    public String getItem() {
        return mItem;
    }

    public void setItem(String mItem) {
        this.mItem = mItem;
    }

    public void putNewItem(String newItem) {
        mItemsInList.add(newItem);
    }

    public String toString() {
        return mItem;
    }
}
