package com.example.olinestore;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.olinestore.Fragments.LoginFragment;
import com.example.olinestore.Fragments.RegisterFragment;

public class TabHandler extends FragmentStateAdapter {
    private int tabsAmount = 3;

    public TabHandler(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0:
                return new LoginFragment();
            case 1:
                return new RegisterFragment();
            default:
                return new LoginFragment();
        }
    }

    @Override
    public int getItemCount() {
        return this.tabsAmount;
    }
}
