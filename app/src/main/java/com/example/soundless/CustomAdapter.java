/**
 * Soundless - Application that automatically changes the profile of the phone based on the Google Calendar events.

 * Team Adroit -
 *  Computer Science:
 *    Joseph Daniels 
 *    Kshipra Kode 
 *    Oladipupo Eke
 *    Mark Pileggi
 *  Designer:
 *    Monica Williams

 * Date : 19th May 2016

 **/

package com.example.soundless;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

//Class that defines the custom layout of the Events in the calendar
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


        rowView = inflater.inflate(R.layout.content_listview, null);
        ImageView imageIcon = (ImageView) rowView.findViewById(R.id.iconCalender);
        //Depending on the date, the events are given an individual icon
        switch(date){
            case 1:
                imageIcon.setImageResource(R.drawable.cal_01);
                break;
            case 2:
                imageIcon.setImageResource(R.drawable.cal_02);
                break;
            case 3:
                imageIcon.setImageResource(R.drawable.cal_03);
                break;
            case 4:
                imageIcon.setImageResource(R.drawable.cal_04);
                break;
            case 5:
                imageIcon.setImageResource(R.drawable.cal_05);
                break;
            case 6:
                imageIcon.setImageResource(R.drawable.cal_06);
                break;
            case 7:
                imageIcon.setImageResource(R.drawable.cal_07);
                break;
            case 8:
                imageIcon.setImageResource(R.drawable.cal_08);
                break;
            case 9:
                imageIcon.setImageResource(R.drawable.cal_09);
                break;
            case 10:
                imageIcon.setImageResource(R.drawable.cal_10);
                break;
            case 11:
                imageIcon.setImageResource(R.drawable.cal_11);
                break;
            case 12:
                imageIcon.setImageResource(R.drawable.cal_12);
                break;
            case 13:
                imageIcon.setImageResource(R.drawable.cal_13);
                break;
            case 14:
                imageIcon.setImageResource(R.drawable.cal_14);
                break;
            case 15:
                imageIcon.setImageResource(R.drawable.cal_15);
                break;
            case 16:
                imageIcon.setImageResource(R.drawable.cal_16);
                break;
            case 17:
                imageIcon.setImageResource(R.drawable.cal_17);
                break;
            case 18:
                imageIcon.setImageResource(R.drawable.cal_18);
                break;
            case 19:
                imageIcon.setImageResource(R.drawable.cal_19);
                break;
            case 20:
                imageIcon.setImageResource(R.drawable.cal_20);
                break;
            case 21:
                imageIcon.setImageResource(R.drawable.cal_21);
                break;
            case 22:
                imageIcon.setImageResource(R.drawable.cal_22);
                break;
            case 23:
                imageIcon.setImageResource(R.drawable.cal_23);
                break;
            case 24:
                imageIcon.setImageResource(R.drawable.cal_24);
                break;
            case 25:
                imageIcon.setImageResource(R.drawable.cal_25);
                break;
            case 26:
                imageIcon.setImageResource(R.drawable.cal_26);
                break;
            case 27:
                imageIcon.setImageResource(R.drawable.cal_27);
                break;
            case 28:
                imageIcon.setImageResource(R.drawable.cal_28);
                break;
            case 29:
                imageIcon.setImageResource(R.drawable.cal_29);
                break;
            case 30:
                imageIcon.setImageResource(R.drawable.cal_30);
                break;
            case 31:
                imageIcon.setImageResource(R.drawable.cal_31);
                break;

        }

        //Setting the events Name, Location, Timings
        holder.name=(TextView) rowView.findViewById(R.id.EventName);
        holder.location=(TextView) rowView.findViewById(R.id.EventLocation);
        holder.time=(TextView) rowView.findViewById(R.id.EventTime);
        holder.timeEnd=(TextView) rowView.findViewById(R.id.EventTimeEnd);
        holder.name.setText(eventNames.get(position));

        if(eventTime.get(position)!=null)
        holder.time.setText("From: " + eventTime.get(position));
        holder.location.setText(eventLocations.get(position));
        holder.timeEnd.setText("To: " + eventTimeEnd.get(position));

        rowView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        return rowView;
    }

}