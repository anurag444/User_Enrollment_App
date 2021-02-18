package com.example.internship_enrollment_app;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.example.internship_enrollment_app.Adapter.ViewPagerAdapter;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class MainActivity extends AppCompatActivity {

    private ViewPager2 viewPager;
    private TabLayout tabs;
    private ViewPagerAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewPager = findViewById(R.id.view_pager);
        tabs = findViewById(R.id.tabs);
        adapter = new ViewPagerAdapter(this);


        viewPager.setAdapter(adapter);
        viewPager.setUserInputEnabled(true);
        new TabLayoutMediator(tabs, viewPager,
                (tab, position) -> {
                    switch (position) {
                        case 0:
                            tab.setText("Users");
                            break;
                        case 1:
                            tab.setText("Enroll");
                            break;
                    }
                }).attach();


    }
}