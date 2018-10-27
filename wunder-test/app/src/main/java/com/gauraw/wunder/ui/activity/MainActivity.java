package com.gauraw.wunder.ui.activity;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableStringBuilder;
import android.view.MenuItem;

import com.gauraw.wunder.R;
import com.gauraw.wunder.custom.font.CustomTypefaceSpan;
import com.gauraw.wunder.ui.fragment.ListFragment;
import com.gauraw.wunder.ui.fragment.MapsFragment;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "MainActivity";
    public static ArrayList<HashMap<String, String>> carsList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        //TODO create a splash screen

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigationView);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        Typeface typeFace = Typeface.createFromAsset(this.getAssets(), "fonts/GlacialIndifference-Bold.ttf");
        CustomTypefaceSpan typefaceSpan = new CustomTypefaceSpan("", typeFace);
        //change typeface for Bottom tabs!

        for (int i = 0; i < navigation.getMenu().size(); i++) {
            MenuItem menuItem = navigation.getMenu().getItem(i);
            SpannableStringBuilder spannableTitle = new SpannableStringBuilder(menuItem.getTitle());
            spannableTitle.setSpan(typefaceSpan, 0, spannableTitle.length(), 0);
            menuItem.setTitle(spannableTitle);
        }

        //Manually displaying the first fragment - one time only

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, ListFragment.newInstance());
        transaction.commit();

        //TODO load url in Activity rather than in each fragment.
    }

    public BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment = null;
            switch (item.getItemId()) {
                case R.id.navigation_list:
                    fragment = ListFragment.newInstance();
                    break;
                case R.id.navigation_maps:
                    fragment = MapsFragment.newInstance();
                    break;
            }

            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.container, fragment);
            transaction.commit();
            return true;
        }


    };

}
