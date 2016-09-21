package zhj.viewpagerdemo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.viewpagerindicator.TabPageIndicator;

import java.util.ArrayList;
import java.util.List;

import zhj.viewpagerdemo.adapter.MyTabFragmentPagerAdapter;
import zhj.viewpagerdemo.tabfragment.TabFragment1;
import zhj.viewpagerdemo.tabfragment.TabFragment2;
import zhj.viewpagerdemo.tabfragment.TabFragment3;
import zhj.viewpagerdemo.tabfragment.TabFragment4;

/**
 * Created by HongJay on 2016/8/11.
 */
public class Fragment1 extends Fragment {

    private ViewPager mViewPager;
    private MyTabFragmentPagerAdapter mMyTabFragmentPagerAdapter;
    private List<Fragment> fragments;
    private TabLayout.Tab one;
    private TabLayout.Tab two;
    private TabLayout.Tab three;
    private TabLayout.Tab four;
    private TabLayout mTabLayout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment1, container, false);
        mTabLayout = (TabLayout) view.findViewById(R.id.tab_main);
        mViewPager = (ViewPager) view.findViewById(R.id.vp_menu_pager);
        initdata();
        return view;
    }

    private void initdata() {
        fragments=new ArrayList<Fragment>();
        fragments.add(new TabFragment1());
        fragments.add(new TabFragment2());
        fragments.add(new TabFragment3());
        fragments.add(new TabFragment4());
        mMyTabFragmentPagerAdapter = new MyTabFragmentPagerAdapter(getActivity().getSupportFragmentManager()
        ,fragments);
        mViewPager.setOffscreenPageLimit(4);
        mViewPager.setAdapter(mMyTabFragmentPagerAdapter);
        //将TabLayout和ViewPager绑定在一起，使双方各自的改变都能直接影响另一方，解放了开发人员对双方变动事件的监听
        mTabLayout.setupWithViewPager(mViewPager);

        //指定Tab的位置
        one = mTabLayout.getTabAt(0);
        two = mTabLayout.getTabAt(1);
        three = mTabLayout.getTabAt(2);
        four = mTabLayout.getTabAt(3);
    }
}
