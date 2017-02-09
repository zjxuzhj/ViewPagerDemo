package zhj.viewpagerdemo.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import zhj.viewpagerdemo.Fragment1;
import zhj.viewpagerdemo.Fragment2;
import zhj.viewpagerdemo.Fragment3;
import zhj.viewpagerdemo.Fragment4;
import zhj.viewpagerdemo.Fragment5;

/**
 * Created by HongJay on 2016/8/11.
 */
public class MyFragmentPagerAdapter extends FragmentPagerAdapter{
    private List<Fragment> fragments;
    public MyFragmentPagerAdapter(FragmentManager fm,List<Fragment> fragments) {
        super(fm);
        this.fragments=fragments;
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }
}
