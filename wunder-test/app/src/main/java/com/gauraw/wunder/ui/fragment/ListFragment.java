

package com.gauraw.wunder.ui.fragment;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ListFragment extends Fragment {

    private static final String TAG = "ListFragment";
    public ArrayList<Placemark> carsList = new ArrayList<>();

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private CarShowcaseAdapter carsShowcaseAdapter;

    public static ListFragment newInstance() {
        ListFragment fragment = new ListFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_list, container, false);

        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);

        if(getActivity()!=null){
            mLayoutManager = new LinearLayoutManager(getActivity());
        }
        recyclerView.setLayoutManager(mLayoutManager);

        //TODO Fetch Locations.json
        new GetCarsList().execute();

        return view;

    }

    private class GetCarsList extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //TODO beautiful custom progress dialog,
            //TODO load URL first, then fragments
            if(getActivity()!=null){
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

            if(getActivity()!=null){
                carsShowcaseAdapter = new CarShowcaseAdapter(carsList, getActivity());
                recyclerView.setAdapter(carsShowcaseAdapter);
            }
        }
    }
}
