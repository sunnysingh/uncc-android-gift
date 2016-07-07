package com.mad.sunny.afinal;

/**
 * Name: Sunny Singh
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by sunny on 6/28/16.
 */
public class GiftAdapter extends ArrayAdapter<Gift> {

    List<Gift> mData;
    Context mContext;
    int mResource;

    public GiftAdapter(Context context, int resource, List<Gift> objects) {
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

        ImageView imageViewGift = (ImageView) convertView.findViewById(R.id.imageViewGift);
        TextView textViewGift = (TextView) convertView.findViewById(R.id.textViewGift);
        TextView textViewGiftPrice = (TextView) convertView.findViewById(R.id.textViewGiftPrice);

        Gift gift = mData.get(position);
        String giftPriceText = "$"+gift.getPrice();

        Picasso.with(mContext).load(gift.getImageUrl()).into(imageViewGift);
        textViewGift.setText(gift.getGift());
        textViewGiftPrice.setText(giftPriceText);

        return convertView;
    }
}
