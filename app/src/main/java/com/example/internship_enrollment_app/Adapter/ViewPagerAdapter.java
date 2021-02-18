package com.example.internship_enrollment_app.Adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.internship_enrollment_app.Fragments.EnrollFragment;
import com.example.internship_enrollment_app.Fragments.UserFragment;

public class ViewPagerAdapter extends FragmentStateAdapter {


    public ViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new UserFragment();
            case 1:
                return new EnrollFragment();
        }
        return null;
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
