package com.example.android.quakereport;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static com.example.android.quakereport.R.id.location;
import static com.example.android.quakereport.R.id.offset;

/**
 * Created by Katary on 11/21/2017.
 */

public class EarthquakeAdapter extends ArrayAdapter<Earthquake> {

    public EarthquakeAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<Earthquake> objects) {
        super(context, 0, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItemView = convertView;
        Earthquake item = getItem(position);

        if (listItemView == null){
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
        }

        TextView magnitudeView = (TextView) listItemView.findViewById(R.id.magnitude);

        DecimalFormat formatter = new DecimalFormat("0.0");
        String output = formatter.format(item.getMagnitude());
        magnitudeView.setText(output);


        TextView locationView = (TextView) listItemView.findViewById(location);
        TextView offsetView = (TextView) listItemView.findViewById(R.id.offset);
        String originalLocation = item.getLocation();
        String offset;
        String  location;

        if(originalLocation.contains(" of ")) {
            String[] locationArray = originalLocation.split(" of ");
            offset = locationArray[0] + " of";
            location= locationArray[1];
        }
        else{
            offset = "Near of";
            location = originalLocation;
        }

        locationView.setText(location);
        offsetView.setText(offset);

        TextView dateView = (TextView) listItemView.findViewById(R.id.date);
        TextView timeView = (TextView) listItemView.findViewById(R.id.time);

        Date date = new Date(item.getTime());
        SimpleDateFormat dateFormatter = new SimpleDateFormat("MMM dd, yyyy");
        SimpleDateFormat timeFormatter = new SimpleDateFormat("h:mm a");
        String displayDate = dateFormatter.format(date);
        String displayTime = timeFormatter.format(date);

        dateView.setText(displayDate);

        timeView.setText(displayTime);

        return listItemView;
    }
}
