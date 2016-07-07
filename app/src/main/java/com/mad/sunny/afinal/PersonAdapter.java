package com.mad.sunny.afinal;

/**
 * Name: Sunny Singh
 */

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by sunny on 6/28/16.
 */
public class PersonAdapter extends ArrayAdapter<Person> {

    List<Person> mData;
    Context mContext;
    int mResource;

    public PersonAdapter(Context context, int resource, List<Person> objects) {
        super(context, resource, objects);

        this.mData = objects;
        this.mContext = context;
        this.mResource = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(mResource, parent, false);
        }

        TextView textViewPersonName = (TextView) convertView.findViewById(R.id.textViewPersonName);
        TextView textViewPersonCost = (TextView) convertView.findViewById(R.id.textViewPersonCost);
        TextView textViewPersonBoughtGiftsCount = (TextView) convertView.findViewById(R.id.textViewBoughtGiftsCount);

        Person person = mData.get(position);
        String personCostText = "$" + person.getSpent() + "/$" + person.getBudget();
        String giftsBoughtText = person.getGiftsBought()+" gifts bought";
        if (person.getGiftsBought() == 1) {
            giftsBoughtText = "1 gift bought";
        }

        textViewPersonName.setText(person.getName());
        textViewPersonCost.setText(personCostText);
        textViewPersonBoughtGiftsCount.setText(giftsBoughtText);

        // Set cost color
        if (person.getSpent() == 0) {
            textViewPersonCost.setTextColor(Color.parseColor("#757575"));
        } else if (person.getSpent() == person.getBudget()) {
            textViewPersonCost.setTextColor(Color.parseColor("#43A047"));
        } else {
            textViewPersonCost.setTextColor(Color.parseColor("#e53935"));
        }

        return convertView;
    }
}
