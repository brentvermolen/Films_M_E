package com.example.brent.films.Class;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.brent.films.Model.ActeurFilm;
import com.example.brent.films.Model.Film;
import com.example.brent.films.R;

import java.io.IOException;
import java.util.List;

public class ActorsGridView extends BaseAdapter {
    private Context mContext;
    private List<ActeurFilm> acteurs;

    public ActorsGridView(Context c, List<ActeurFilm> acteurs) {
        mContext = c;
        this.acteurs = acteurs;
    }

    public int getCount() {
        return acteurs.size();
    }

    public ActeurFilm getItem(int position) {
        return acteurs.get(position);
    }

    public long getItemId(int position) {
        return getItem(position).getActeurID();
    }

    private class ViewHolder {
        public ImageView imgPoster;
        public TextView lblActeur;
        public TextView lblRol;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.inflater_acteur_poster, null);

            viewHolder = new ViewHolder();
            viewHolder.imgPoster = convertView.findViewById(R.id.imgPoster);
            viewHolder.lblActeur = convertView.findViewById(R.id.lblActeur);
            viewHolder.lblRol = convertView.findViewById(R.id.lblRol);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Bitmap bm = null;
        try {
            bm = Methodes.getBitmapFromAsset(mContext, "acteurs/" + acteurs.get(position).getActeurID() + ".jpg");
            bm = Methodes.getRoundedCornerBitmap(bm, 15);
            viewHolder.imgPoster.setImageBitmap(bm);
        } catch (IOException e) {
            viewHolder.imgPoster.setImageResource(R.drawable.no_image);
            e.printStackTrace();
        }

        viewHolder.lblActeur.setText(acteurs.get(position).getActeur().getNaam());
        viewHolder.lblRol.setText(acteurs.get(position).getKarakter());

        return convertView;
    }
}
