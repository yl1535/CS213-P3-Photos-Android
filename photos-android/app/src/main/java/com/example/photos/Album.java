package com.example.photos;

import androidx.annotation.NonNull;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * This class is to display the albums of photos.
 * @author Yue Luo
 * @author Nicole Le
 */
public class Album implements Serializable{
    private static final long serialVersionUID = 1535L;
    private ArrayList<EachPhoto> Contains;
    private String name;

    public Album(ArrayList<EachPhoto> Contains, String name){
        this.Contains = Contains;
        this.name = name;
    }

    public Album(String name){
        this.Contains = new ArrayList<EachPhoto>();
        this.name = name;
    }

    public ArrayList<EachPhoto> getContains(){
        return Contains;
    }

    public void setContains(ArrayList<EachPhoto> newContains){
        Contains = newContains;
    }

    public String getName(){
        return name;
    }

    public void setName(String newName){
        name = newName;
    }

    @NonNull
    @Override
    public String toString(){
        return name;
    }

    public String toStringFull(){
        return "Name: "+name+"\nContains "+Contains.size()+" Photos";
    }
}
