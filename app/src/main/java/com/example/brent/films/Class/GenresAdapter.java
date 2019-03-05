package com.example.brent.films.Class;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.brent.films.Model.Acteur;
import com.example.brent.films.Model.Tag;
import com.example.brent.films.R;

import java.io.IOException;
import java.util.List;

public class GenresAdapter extends BaseAdapter {
    private Context mContext;
    private List<Tag> tags;

    private List<Tag> onzichtbareTags;

    GenresDAO dao;

    public GenresAdapter(Context c) {
        mContext = c;
        dao = FilmsDb.getDatabase(c).genresDAO();

        this.tags = DAC.Tags;
        this.tags.addAll(dao.getOwnTags());

        this.onzichtbareTags = dao.getExistingTags();
    }

    public int getCount() {
        return tags.size();
    }

    public Tag getItem(int position) {
        return tags.get(position);
    }

    public long getItemId(int position) {
        return getItem(position).getID();
    }

    private class ViewHolder {
        TextView lblGenre;
        LinearLayout llEigenGenre;
        ImageView btnEditGenre;
        ImageView btnDeleteGenre;

        LinearLayout llBestaandGenre;
        ToggleButton btnToggleZichtbaarheid;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.lst_genre_item, null);

            viewHolder = new ViewHolder();
            viewHolder.lblGenre = convertView.findViewById(R.id.lblGenre);
            viewHolder.llEigenGenre = convertView.findViewById(R.id.llEigenGenre);
            viewHolder.btnEditGenre = convertView.findViewById(R.id.btnEditGenre);
            viewHolder.btnDeleteGenre = convertView.findViewById(R.id.btnDeleteGenre);
            viewHolder.llBestaandGenre = convertView.findViewById(R.id.llBestaandGenre);
            viewHolder.btnToggleZichtbaarheid = convertView.findViewById(R.id.tglVisibility);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.lblGenre.setText(tags.get(position).getNaam());
        if (tags.get(position).getID() >= 100){
            viewHolder.llBestaandGenre.setVisibility(View.GONE);
            viewHolder.llEigenGenre.setVisibility(View.VISIBLE);
        }else{
            viewHolder.llEigenGenre.setVisibility(View.GONE);
            viewHolder.llBestaandGenre.setVisibility(View.VISIBLE);

            boolean isZichtbaar = !onzichtbareTags.contains(tags.get(position));
            viewHolder.btnToggleZichtbaarheid.setChecked(isZichtbaar);
            viewHolder.btnToggleZichtbaarheid.setTag(isZichtbaar);
        }

        viewHolder.btnToggleZichtbaarheid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((ToggleButton)v).isChecked()){
                    dao.deleteById(tags.get(position).getID());
                    v.setTag(false);
                    Toast.makeText(mContext, "Verwijderd", Toast.LENGTH_SHORT).show();
                }else{
                    dao.insert(tags.get(position));
                    v.setTag(false);
                    Toast.makeText(mContext, "Toegevoegd", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return convertView;
    }
}
