package com.example.soundless;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.google.api.client.util.DateTime;

import java.util.List;

public class CustomAdapter extends BaseAdapter{
    List<String> eventNames;
    List<DateTime> eventTime;
    List<String> eventLocations;
    Context context;
    int [] imageId;

    private static LayoutInflater inflater=null;
    public CustomAdapter(com.example.soundless.MainActivity mainActivity, List<String> eventNames1, List<DateTime> eventTime1, List<String> eventLocation1) {
        // TODO Auto-generated constructor stub
        eventNames=eventNames1;
        context=mainActivity;
        eventTime=eventTime1;
        eventLocations=eventLocation1;
        inflater = ( LayoutInflater )context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return eventLocations.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    public class Holder
    {
        TextView name,time,location;
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        Holder holder=new Holder();
        View rowView;
        rowView = inflater.inflate(R.layout.content_listview, null);
        holder.name=(TextView) rowView.findViewById(R.id.EventName);
        holder.location=(TextView) rowView.findViewById(R.id.EventLocation);
        holder.time=(TextView) rowView.findViewById(R.id.EventTime);

        holder.name.setText(eventNames.get(position));
        holder.time.setText(eventTime.get(position).toString());
        holder.location.setText(eventLocations.get(position));

        rowView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                //Toast.makeText(context, "You Clicked "+eventNames.get(position), Toast.LENGTH_LONG).show();
            }
        });
        return rowView;
    }

}