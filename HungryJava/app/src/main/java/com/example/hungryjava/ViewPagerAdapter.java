package com.example.hungryjava;


import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.ArrayList;
import java.util.List;

public class ViewPagerAdapter extends FragmentStateAdapter {

    public ViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    private final List<Fragment> fragmentList = new ArrayList<>();
    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                Fragment inventoryFragment = new InventoryFragment();
                fragmentList.add(inventoryFragment);
                return inventoryFragment;
            case 1:
                Fragment homeFragment = new HomeFragment();
                fragmentList.add(homeFragment);
                return homeFragment;
            case 2:
                Fragment catalogFragment = new CatalogFragment();
                fragmentList.add(catalogFragment);
                return catalogFragment;
            default:
                return new HomeFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }

    public Fragment getFragment(int position) {
        return fragmentList.get(position);
    }

}
