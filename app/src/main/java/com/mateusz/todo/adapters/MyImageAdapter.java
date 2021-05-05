package com.mateusz.todo.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import java.util.List;

public class MyImageAdapter extends BaseAdapter {

    private Context context;
    private List<Bitmap> images;

    public MyImageAdapter(Context context, List<Bitmap> images) {
        this.context = context;
        this.images = images;
    }

    @Override
    public int getCount() {
        return this.images.size();
    }

    @Override
    public Object getItem(int position) {
        return this.images.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if (convertView == null) {
            imageView = new ImageView(context);
            imageView.setScaleType(ImageView.ScaleType.CENTER);
            imageView.setPadding(15, 15, 15, 15);
            
        } else {
            imageView = (ImageView) convertView;
        }
        imageView.setImageBitmap(this.images.get(position));
        return imageView;
    }
}
