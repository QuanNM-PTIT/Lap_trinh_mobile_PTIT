package com.example.planetsapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class MyCustomAdapter extends ArrayAdapter<Planet>
{
    private ArrayList<Planet> planets;
    private Context context;

    public MyCustomAdapter(ArrayList<Planet> planets, Context context)
    {
        super(context, R.layout.list_item, planets);
        this.planets = planets;
        this.context = context;
    }

    private static class ViewHolder
    {
        private ImageView planetImage;
        private TextView planetName;
        private TextView moonCount;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent)
    {
        Planet planet = getItem(position);

        ViewHolder viewHolder;
        final View result;

        if (convertView == null)
        {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.list_item, parent, false);
            viewHolder.planetName = (TextView) convertView.findViewById(R.id.planetName);
            viewHolder.moonCount = (TextView) convertView.findViewById(R.id.moonCount);
            viewHolder.planetImage = (ImageView) convertView.findViewById(R.id.planetImage);

            result = convertView;

            convertView.setTag(viewHolder);
        }
        else
        {
            viewHolder = (ViewHolder) convertView.getTag();
            result = convertView;
        }

        viewHolder.planetName.setText(planet.getName());
        viewHolder.moonCount.setText(planet.getMoonCount());
        viewHolder.planetImage.setImageResource(planet.getImage());

        return convertView;
    }
}
