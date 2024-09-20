package com.example.photos;

import androidx.annotation.NonNull;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * This is the class for each individual photo within an album 
 * @author Yue Luo
 * @author Nicole Le
 */

public class EachPhoto implements Serializable{
    private static final long serialVersionUID = 1535L;
    private String PhotoPath;
    private ArrayList<Tags> tags = new ArrayList<Tags>();
    private int width;
    private int height;

    public EachPhoto(String PhotoPath){
        this.PhotoPath = PhotoPath;
        tags = new ArrayList<Tags>();
    }

    public String getPath(){
        return PhotoPath;
    }

    public void setPath(String newPath){
        PhotoPath = newPath;
    }

    public ArrayList<Tags> getTags(){
        return tags;
    }

    public void setTags(ArrayList<Tags> newTags){
        tags = newTags;
    }

    public int getWidth(){
        return width;
    }

    public int getHeight(){
        return height;
    }

    public void setSize(int width, int height){
        this.width = width;
        this.height = height;
    }

    @NonNull
    @Override
    public String toString(){
        return PhotoPath;
    }
}
