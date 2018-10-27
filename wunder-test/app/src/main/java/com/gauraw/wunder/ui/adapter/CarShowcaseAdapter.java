package com.gauraw.wunder.ui.adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;

import com.gauraw.wunder.R;
import com.gauraw.wunder.custom.font.CustomTextViewBold;
import com.gauraw.wunder.custom.font.CustomTextViewRegular;
import com.gauraw.wunder.model.pojos.Placemark;
import com.gauraw.wunder.ui.fragment.MapsFragment;

import java.util.ArrayList;
import java.util.List;

public class CarShowcaseAdapter extends RecyclerView.Adapter<CarShowcaseAdapter.ViewHolder> {

    private List<Placemark> mPlacemarks;
    private Context context;

    public CarShowcaseAdapter(ArrayList<Placemark> placemark, Context context) {
        this.mPlacemarks = placemark;
        this.context = context;
    }

    @Override
    public CarShowcaseAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cars_listing, parent, false);
        return new CarShowcaseAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final CarShowcaseAdapter.ViewHolder holder, final int position) {
        Placemark placemark = mPlacemarks.get(position);
        holder.name.setText(mPlacemarks.get(position).getName());
        holder.address.setText(mPlacemarks.get(position).getAddress());
        holder.engineType.setText(mPlacemarks.get(position).getEngineType());
        if (mPlacemarks.get(position).getExterior().equalsIgnoreCase("GOOD")) {
            holder.exterior.setImageResource(R.drawable.ic_thumb_up_black_24dp);
        } else {
            holder.exterior.setImageResource(R.drawable.ic_thumb_down_black_24dp);
        }

        if (mPlacemarks.get(position).getInterior().equalsIgnoreCase("GOOD")) {
            holder.interior.setImageResource(R.drawable.ic_thumb_up_black_24dp);
        } else {
            holder.interior.setImageResource(R.drawable.ic_thumb_down_black_24dp);
        }

        holder.fuel.setText("" + mPlacemarks.get(position).getFuel());
        holder.vin.setText(mPlacemarks.get(position).getVin());
        //holder.z.setText(String.valueOf(mPlacemarks.get(position).getZ()));
        //holder.latitude.setText(String.valueOf(mPlacemarks.get(position).getLatitude()));
        //holder.longitude.setText(String.valueOf(mPlacemarks.get(position).getLongitude()));

        blinkText(holder.name);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Bundle bundle = new Bundle();
                bundle.putString("name", mPlacemarks.get(position).getName());
                bundle.putDouble("latitude", mPlacemarks.get(position).getLatitude());
                bundle.putDouble("longitude", mPlacemarks.get(position).getLongitude());
                bundle.putDouble("z", mPlacemarks.get(position).getZ());

                MapsFragment mapsFragment = new MapsFragment();
                mapsFragment.setArguments(bundle);

                ((AppCompatActivity) context).getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.container, mapsFragment)
                        .commit();

            }
        });
    }

    private void blinkText(final View view) {

        Animation anim = new AlphaAnimation(0.0f, 1.0f);
        anim.setDuration(500); //You can manage the time of the blink with this parameter
        anim.setStartOffset(20);
        anim.setRepeatMode(Animation.REVERSE);
        anim.setRepeatCount(Animation.INFINITE);
        view.startAnimation(anim);

    }

    @Override
    public int getItemCount() {
        if (mPlacemarks != null) {
            return mPlacemarks.size();
        } else {
            return 0;
        }
    }

    public Placemark getPlacemark(int position) {
        return mPlacemarks.get(position);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private CustomTextViewBold name;
        private CustomTextViewRegular address, vin, engineType, fuel;//, latitude, longitude, z;
        private AppCompatImageView interior, exterior;

        ViewHolder(View itemView) {
            super(itemView);
            name = (CustomTextViewBold) itemView.findViewById(R.id.name);
            address = (CustomTextViewRegular) itemView.findViewById(R.id.address);
            vin = (CustomTextViewRegular) itemView.findViewById(R.id.vin);
            engineType = (CustomTextViewRegular) itemView.findViewById(R.id.engineType);
            fuel = (CustomTextViewRegular) itemView.findViewById(R.id.fuel);
            exterior = (AppCompatImageView) itemView.findViewById(R.id.exterior);
            interior = (AppCompatImageView) itemView.findViewById(R.id.interior);
            //latitude = (CustomTextViewRegular) itemView.findViewById(R.id.latitude);
            //longitude = (CustomTextViewRegular) itemView.findViewById(R.id.longitude);
            //z = (CustomTextViewRegular) itemView.findViewById(R.id.z);
        }

    }
}