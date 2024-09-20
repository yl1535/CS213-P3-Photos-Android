package com.example.photos;

import androidx.annotation.NonNull;

import java.io.Serializable;

/**
 * This class implements the tags functionality for photos
 * @author Yue Luo
 * @author Nicole Le
 */
public class Tags implements Serializable{
    private static final long serialVersionUID = 1535L;
    public enum TagName{
        person,
        location;
    }

    private TagName TagName;

    private String TagValue;

    public Tags(TagName tagName, String TagValue){
        this.TagName = tagName;
        this.TagValue = TagValue;
    }

    public TagName getTagName(){
        return TagName;
    }

    public void setTagName(TagName newName){
        TagName = newName;
    }

    public String getTagValue(){
        return TagValue;
    }

    public void setTagValue(String newValue){
        TagValue = newValue;
    }

    @NonNull
    @Override
    public String toString(){
        return "TagName: " + TagName.toString() + ", TagValue: " + TagValue.toLowerCase();
    }

    public String toStringNTLC(){
        return "TagName: " + TagName.toString() + ", TagValue: " + TagValue;
    }

    public boolean ifFit(Tags tag){
        return this.TagName.equals(tag.getTagName()) && ifSubStringof(tag.getTagValue().toLowerCase(), this.TagValue.toLowerCase());
    }

    public boolean ifSubStringof(String shorter, String longer){
        if(shorter.length() > longer.length()) return false;
        for(int i=0;i<longer.length()+1;i++){
            if(shorter.equals(longer.substring(0,i))) return true;
        }
        return false;
    }
}
