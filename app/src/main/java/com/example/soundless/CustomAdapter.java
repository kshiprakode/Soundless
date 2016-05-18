package com.example.soundless;

import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class CustomAdapter extends BaseAdapter{
    List<String> eventNames;
    List<String> eventTime;
    List<String> eventTimeEnd;
    List<String> eventLocations;
    Context context;
    Typeface customFont;

    private static LayoutInflater inflater=null;
    public CustomAdapter(com.example.soundless.MainActivity mainActivity, List<String> eventNames1, List<String> eventTime1,List<String> eventTime2, List<String> eventLocation1) {
        // TODO Auto-generated constructor stub
        eventNames=eventNames1;
        context=mainActivity;
        eventTime=eventTime1;
        eventTimeEnd=eventTime2;
        eventLocations=eventLocation1;
        customFont = Typeface.createFromAsset(mainActivity.getAssets(),"fonts/IrmaTextRoundStd-SemiBold copy.ttf");
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
        TextView name,time,location,timeEnd;
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        Holder holder=new Holder();

        View rowView;
        int date = Integer.parseInt(eventTime.get(position).substring(8, 10));
        Log.d("date",String.valueOf(date));

        rowView = inflater.inflate(R.layout.content_listview, null);
        ImageView imageIcon = (ImageView) rowView.findViewById(R.id.iconCalender);
        switch(date){
            case 1:
                imageIcon.setImageResource(R.drawable.cal_01);
                break;
            case 2:
                imageIcon.setImageResource(R.drawable.cal_01);
                break;
            case 3:
                imageIcon.setImageResource(R.drawable.cal_01);
                break;
            case 4:
                imageIcon.setImageResource(R.drawable.cal_01);
                break;
            case 5:
                imageIcon.setImageResource(R.drawable.cal_01);
                break;
            case 6:
                imageIcon.setImageResource(R.drawable.cal_01);
                break;
            case 7:
                imageIcon.setImageResource(R.drawable.cal_01);
                break;
            case 8:
                imageIcon.setImageResource(R.drawable.cal_01);
                break;
            case 9:
                imageIcon.setImageResource(R.drawable.cal_01);
                break;
            case 10:
                imageIcon.setImageResource(R.drawable.cal_01);
                break;
            case 11:
                imageIcon.setImageResource(R.drawable.cal_01);
                break;
            case 12:
                imageIcon.setImageResource(R.drawable.cal_01);
                break;
            case 13:
                imageIcon.setImageResource(R.drawable.cal_01);
                break;
            case 14:
                imageIcon.setImageResource(R.drawable.cal_01);
                break;
            case 15:
                imageIcon.setImageResource(R.drawable.cal_01);
                break;
            case 16:
                imageIcon.setImageResource(R.drawable.cal_01);
                break;
            case 17:
                imageIcon.setImageResource(R.drawable.cal_01);
                break;
            case 18:
                imageIcon.setImageResource(R.drawable.cal_01);
                break;
            case 19:
                imageIcon.setImageResource(R.drawable.cal_01);
                break;
            case 20:
                imageIcon.setImageResource(R.drawable.cal_01);
                break;
            case 21:
                imageIcon.setImageResource(R.drawable.cal_01);
                break;
            case 22:
                imageIcon.setImageResource(R.drawable.cal_01);
                break;
            case 23:
                imageIcon.setImageResource(R.drawable.cal_01);
                break;
            case 24:
                imageIcon.setImageResource(R.drawable.cal_01);
                break;
            case 25:
                imageIcon.setImageResource(R.drawable.cal_01);
                break;
            case 26:
                imageIcon.setImageResource(R.drawable.cal_01);
                break;
            case 27:
                imageIcon.setImageResource(R.drawable.cal_01);
                break;
            case 28:
                imageIcon.setImageResource(R.drawable.cal_01);
                break;
            case 29:
                imageIcon.setImageResource(R.drawable.cal_01);
                break;
            case 30:
                imageIcon.setImageResource(R.drawable.cal_01);
                break;
            case 31:
                imageIcon.setImageResource(R.drawable.cal_01);
                break;

        }
        holder.name=(TextView) rowView.findViewById(R.id.EventName);
        holder.location=(TextView) rowView.findViewById(R.id.EventLocation);
        holder.time=(TextView) rowView.findViewById(R.id.EventTime);
        holder.timeEnd=(TextView) rowView.findViewById(R.id.EventTimeEnd);
        holder.name.setText(eventNames.get(position));

        if(eventTime.get(position)!=null)
        holder.time.setText(eventTime.get(position));
        holder.location.setText(eventLocations.get(position));
        holder.timeEnd.setText(eventTimeEnd.get(position));

        rowView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Toast.makeText(context, "You Clicked " + eventNames.get(position), Toast.LENGTH_LONG).show();
            }
        });
        return rowView;
    }

}