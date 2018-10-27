package com.gauraw.wunder.ui.fragment;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.gauraw.wunder.R;
import com.gauraw.wunder.model.pojos.Placemark;
import com.gauraw.wunder.ui.adapter.CarShowcaseAdapter;
import com.gauraw.wunder.utils.HttpHandler;
import com.gauraw.wunder.utils.Utils;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MapsFragment extends Fragment implements OnMapReadyCallback {

    private GoogleMap mMap;
    private double latitude, longitude, z;
    private String name;
    private static final String TAG = "ListFragment";
    public ArrayList<Placemark> carsList = new ArrayList<>();

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private CarShowcaseAdapter carsShowcaseAdapter;

    public static MapsFragment newInstance() {
        Bundle args = new Bundle();
        MapsFragment fragment = new MapsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_maps, container, false);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            Log.e(TAG, "onViewCreated if");
            name = bundle.getString("name", null);
            latitude = bundle.getDouble("latitude", 0);
            longitude = bundle.getDouble("longitude", 0);
            z = bundle.getDouble("z", 0);
        } else {
            Log.e(TAG, "onViewCreated else");
        }
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.getUiSettings().setMapToolbarEnabled(true);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.setBuildingsEnabled(true);
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap.setTrafficEnabled(true);

        if (latitude != 0.0 && longitude != 0.0 && name != null) {
            LatLng latLng = new LatLng(latitude, longitude);

            if (getActivity() != null) {
                mMap.addMarker(new MarkerOptions().position(latLng).title(name).draggable(true)
                        .icon(bitmapDescriptorFromVector(getActivity(), R.drawable.ic_local_taxi_black_24dp)));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                Log.e(TAG, "onMapReady if");

            }

        } else {
            Log.e(TAG, "onMapReady else");
            new GetCarsList().execute();

        }
    }


    private BitmapDescriptor bitmapDescriptorFromVector(Context context, int vectorResId) {
        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorResId);
        vectorDrawable.setBounds(0, 0, vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight());
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

    private class GetCarsList extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //TODO beautiful custom progress dialog,
            //TODO load URL first, then fragments
            if (getActivity() != null) {
                Toast.makeText(getActivity(), getResources().getString(R.string.getCarsAsyncPreload), Toast.LENGTH_LONG).show();
            }
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();
            String jsonStr = sh.makeServiceCall(Utils.serverUrl);

            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);

                    //[]-->Placemarks
                    JSONArray cars = jsonObj.getJSONArray("placemarks");

                    for (int i = 0; i < cars.length(); i++) {
                        JSONObject c = cars.getJSONObject(i);
                        String name = c.getString("name");
                        String address = c.getString("address");
                        String vin = c.getString("vin");
                        String engineType = c.getString("engineType");
                        int fuel = c.getInt("fuel");
                        String exterior = c.getString("exterior");
                        String interior = c.getString("interior");


                        //  node is JSON Object
                        JSONArray coordinates = c.getJSONArray("coordinates");

                        double latitude = coordinates.getDouble(0);
                        double longitude = coordinates.getDouble(1);
                        double z = coordinates.getDouble(2);

                        Placemark placemark = new Placemark();
                        placemark.setName(name);
                        placemark.setAddress(address);
                        placemark.setVin(vin);
                        placemark.setEngineType(engineType);
                        placemark.setFuel(fuel);
                        placemark.setExterior(exterior);
                        placemark.setInterior(interior);

                        placemark.setLatitude(latitude);
                        placemark.setLongitude(longitude);
                        placemark.setZ(z);

                        // adding contact to contact list
                        carsList.add(placemark);
                    }
                } catch (final JSONException e) {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());
                    if (getActivity() != null) {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getActivity(),
                                        "Json parsing error: " + e.getMessage(),
                                        Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                }

            } else {
                Log.e(TAG, "Couldn't get json from server.");
                if (getActivity() != null) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getActivity(),
                                    "Couldn't get json from server. Check LogCat for possible errors!",
                                    Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            Log.e(TAG, "" + carsList.size());

            if (carsList != null && carsList.size() > 0) {
                for (int i = 0; i < carsList.size(); i++) {
                    LatLng latLng = new LatLng(carsList.get(i).getLatitude(), carsList.get(i).getLongitude());

                    if (mMap != null) {
                        Log.e(TAG, "onPOstExecute");
                        mMap.addMarker(new MarkerOptions().position(latLng).title(carsList.get(i).getName()).draggable(true)
                                .icon(bitmapDescriptorFromVector(getActivity(), R.drawable.ic_local_taxi_black_24dp)));

                        if (i == 0) {
                            mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
                        }
                    }
                }
            }
        }
    }
}

