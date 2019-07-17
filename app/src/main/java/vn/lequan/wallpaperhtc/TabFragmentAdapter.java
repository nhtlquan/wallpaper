package vn.lequan.wallpaperhtc;

import android.annotation.SuppressLint;
import android.content.Context;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;


@SuppressLint("DefaultLocale")
public class TabFragmentAdapter extends FragmentPagerAdapter {

    private ArrayList<Fragment> fragments;
    private List<String> titles;
    public static final boolean upperCase = false;

    @Deprecated
    public TabFragmentAdapter(FragmentManager fm, ArrayList<Fragment> fragments, List<String> titles) {
        super(fm);
        this.fragments = fragments;
        this.titles = titles;
    }

    public TabFragmentAdapter(Context context, FragmentManager fm, LinkedHashMap<String, Fragment> mapFragments) {
        super(fm);
        fragments = new ArrayList<Fragment>();
        titles = new ArrayList<String>();
        for (String map : mapFragments.keySet()) {
            this.titles.add(map);
            this.fragments.add(mapFragments.get(map));
        }
    }

    public TabFragmentAdapter(Context context, FragmentManager fm, LinkedHashMap<String, Fragment> mapFragments,
                              boolean flag) {
        super(fm);
        fragments = new ArrayList<Fragment>();
        titles = new ArrayList<String>();
        for (String map : mapFragments.keySet()) {
            this.titles.add(map);
            this.fragments.add(mapFragments.get(map));
        }
    }


    @Override
    public Fragment getItem(int position) {
        return fragments.get(position % titles.size());
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return upperCase ? titles.get(position).toUpperCase() : titles.get(position);
    }

}
