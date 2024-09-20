package com.example.photos;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import java.util.ArrayList;

public class ImageAdapter extends BaseAdapter{

    private Context imageContext;
    private ArrayList<String> imageFilePaths;
    private int imageSize;

    public ImageAdapter(Context context, ArrayList<String> imageFilePaths){
        this.imageContext = context;
        this.imageFilePaths = imageFilePaths;
    }

    public void setImageSize(int size){
        this.imageSize = size;
    }

    public int getCount(){
        return imageFilePaths.size();
    }

    public Object getItem(int position){
        return imageFilePaths.get(position);
    }

    public long getItemId(int position){
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent){
        ImageView imageView;
        if(convertView == null){
            imageView = new ImageView(imageContext);
            imageView.setLayoutParams(new ViewGroup.LayoutParams(imageSize, imageSize));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        }
        else{
            imageView = (ImageView) convertView;
        }
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = false;
        Bitmap bitmap = BitmapFactory.decodeFile(imageFilePaths.get(position), options);
        imageView.setImageBitmap(bitmap);
        return imageView;
    }

}
