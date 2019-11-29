package com.example.music;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class SongAdapter extends BaseAdapter {

    private Context context;
    public ArrayList<Song> arraySong;
    public TextView txtSTT, txtTitle;

    public SongAdapter(Context context, ArrayList<Song> arraySong) {
        this.context = context;
        this.arraySong = arraySong;
    }

    @Override
    public int getCount() {
        return arraySong.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.song_row, null);

        txtSTT = (TextView) convertView.findViewById(R.id.textViewSTT);
        txtTitle = (TextView) convertView.findViewById(R.id.textViewTenBai);

        Song song = arraySong.get(position);
        txtTitle.setText(song.getTitle());
        txtSTT.setText(position+1+"");

        return convertView;
    }

}
