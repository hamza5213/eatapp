package Adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

import Fragments.OwnerFoodDisplay;
import Fragments.OwnerOrders;
import Fragments.OwnersOnTheWayOrders;

/**
 * Created by hamza on 09-Jul-18.
 */

public class ViewPagerAdapter extends FragmentPagerAdapter {
    Context context;
    String[] tabTitles={"Order Pending","Orders on the way","FoodItems"};
    ArrayList<Fragment> fragments;



    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
        fragments=new ArrayList<>();
        fragments.add(new OwnerOrders());
        fragments.add(new OwnersOnTheWayOrders());
        fragments.add(new OwnerFoodDisplay());
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles[position];
    }

    public Fragment getFragment(int position)
    {
        return fragments.get(position);
    }


}
