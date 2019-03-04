package com.example.brent.films.Class;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.example.brent.films.Model.Film;
import com.example.brent.films.R;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.List;

public class MoviesGridView extends BaseAdapter {
    private Context mContext;
    private List<Film> films;

    public MoviesGridView(Context c, List<Film> films) {
        mContext = c;
        this.films = films;
    }

    public int getCount() {
        return films.size();
    }

    public Film getItem(int position) {
        return films.get(position);
    }

    public long getItemId(int position) {
        return getItem(position).getID();
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView mImageView;

        if (convertView == null) {
            mImageView = new ImageView(mContext);
            GridView.LayoutParams params = new GridView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            mImageView.setLayoutParams(params);
            mImageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
            mImageView.setAdjustViewBounds(true);
            mImageView.setBackgroundResource(R.drawable.background_image_film_grid);
        } else {
            mImageView = (ImageView) convertView;
        }

        Bitmap bm = null;
        try {
            bm = Methodes.getBitmapFromAsset(mContext, "films/" + films.get(position).getID() + ".jpg");
            bm = Methodes.getRoundedCornerBitmap(bm, 15);
            mImageView.setImageBitmap(bm);
        } catch (IOException e) {
            mImageView.setImageResource(R.drawable.no_image);
            e.printStackTrace();
        }

        convertView = mImageView;
        return mImageView;
    }
}
