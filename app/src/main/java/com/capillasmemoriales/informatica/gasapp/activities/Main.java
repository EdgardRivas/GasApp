package com.capillasmemoriales.informatica.gasapp.activities;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.capillasmemoriales.informatica.gasapp.activities.fragments.DetailsFragment;
import com.capillasmemoriales.informatica.gasapp.activities.fragments.ScheduleFragment;
import com.capillasmemoriales.informatica.gasapp.adapters.TabViewPagerAdapter;
import com.capillasmemoriales.informatica.gasapp.R;

public class Main extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.viewPager);

        tabLayout.setupWithViewPager(viewPager);
        setUpViewPager(viewPager);
    }

    private void setUpViewPager(ViewPager viewPager) {
        TabViewPagerAdapter tabViewPagerAdapter = new TabViewPagerAdapter(getSupportFragmentManager());
        tabViewPagerAdapter.addFragment(new DetailsFragment(), getString(R.string.tab));
        //tabViewPagerAdapter.addFragment(new ScheduleFragment(), getString(R.string.schedule));
        viewPager.setAdapter(tabViewPagerAdapter);
    }
}