仿照着知乎写了一套UI界面，结合着以前学的知识，一天就撸了出来，其实也没啥东西，就是有些没接触的地方踩了坑。

效果展示

![效果展示](http://upload-images.jianshu.io/upload_images/1877523-e272b9221121a6f0.gif?imageMogr2/auto-orient/strip)

 
### 涉及知识点
--- 
1. 最基础的viewpager编写可以参看这篇[超简单ViewPager控件实现Demo](http://www.jianshu.com/p/04c635fec2f0)
1. tablayout+viewpager实现的过程可以参看这篇[Material Design学习：TabLayout+Viewpager制作一个标签页](http://www.jianshu.com/p/51f3a17df49d)
1. recycleview的实现可以参看这篇[RecyclerView的使用简介](http://www.jianshu.com/p/a5f90af3e2e4)
1. viewpager需要添加为recycleview的头布局才能在recycleview上滑的时候上滑，但是recycleview没有添加头布局方法，具体的实现是通过加载不同item的方式。
可以参看我写的[RecycleView加载不同类型的Item](http://www.jianshu.com/p/05dd0315ef41)
 
#### 页面分析

![页面分析](http://upload-images.jianshu.io/upload_images/1877523-6c221790d79bf57f.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

 
#### 实现思路
---
1. 外层的viewpager页面，通过点击页面底部的RadioButton来进行页面切换（禁止此viewpager响应滑动事件），配合fragment实现。
2. 中间层的viewpager页面，通过和tablayout绑定，配合fragment，实现顶部页签。
3. 顶层的viewpager页面，就是个简单的轮播图，循环播放不想写了。底部使用了viewpagerindicator开源库实现小圆点指示器。
4. 添加依赖
compile 'com.android.support:design:24.2.0'
compile 'com.android.support:cardview-v7:24.2.0'
compile 'com.android.support:recyclerview-v7:24.2.0'
viewpagerindicator开源库可以从我的项目中下载

#### 1. 外层viewpager的布局实现
--- 
- 布局代码实现
```
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout
    xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:orientation="vertical">
 
    <zhj.viewpagerdemo.view.NoScrollViewPager
        android:id="@+id/vp_main"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:layout_weight="1"/>
 
    <RadioGroup
        android:id="@+id/rg_group"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:orientation="horizontal"
        android:paddingTop="5dp">
 
        <RadioButton
            android:id="@+id/rb_home"
            style="@style/BottomTabStyle"
            android:checked="true"
            android:drawableBottom="@drawable/btn_tab_home_selector"/>
 
        <RadioButton
            android:id="@+id/rb_news"
            style="@style/BottomTabStyle"
            android:drawableBottom="@drawable/btn_tab_news_selector"/>
 
        <RadioButton
            android:id="@+id/rb_service"
            style="@style/BottomTabStyle"
            android:drawableBottom="@drawable/btn_tab_service_selector"/>
 
        <RadioButton
            android:id="@+id/rb_gov"
            style="@style/BottomTabStyle"
            android:drawableBottom="@drawable/btn_tab_gov_selector"/>
 
        <RadioButton
            android:id="@+id/rb_setting"
            style="@style/BottomTabStyle"
            android:drawableBottom="@drawable/btn_tab_setting_selector"/>
    </RadioGroup>
</LinearLayout>
```
- 禁止滑动的viewpager实现，继承viewpager
```
public class NoScrollViewPager extends ViewPager {
    public NoScrollViewPager(Context context) {
        super(context);
    }
 
    public NoScrollViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
 
    //表示事件是否拦截，返回false表示不拦截
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return false;
    }

    //重写onTouchEvent事件，什么都不用做
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return false;
    }
}
```

#### 2. 外层viewpager的代码实现
---
```
public class MainActivity extends AppCompatActivity {
    private MyFragmentPagerAdapter myFragmentPagerAdapter;
    private RadioGroup rgGroup;
    private List<Fragment> fragments;
    private ViewPager mViewPager;
 
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);  //隐藏掉系统原先的导航栏
        setContentView(R.layout.activity_main);
        mViewPager = (ViewPager) findViewById(R.id.vp_main);
        //新建fragment集合对象，传递给FragmentPagerAdapter
        fragments=new ArrayList<Fragment>();
        fragments.add(new Fragment1());
        fragments.add(new Fragment2());
        fragments.add(new Fragment3());
        fragments.add(new Fragment4());
        fragments.add(new Fragment5());
        myFragmentPagerAdapter = new MyFragmentPagerAdapter(getSupportFragmentManager(),fragments);
        mViewPager.setAdapter(myFragmentPagerAdapter);
 
        rgGroup = (RadioGroup) findViewById(R.id.rg_group);
        rgGroup.check(R.id.rb_home);
        //当点击底部按钮时切换页面
        rgGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (i == R.id.rb_home) {
                    mViewPager.setCurrentItem(0, false);//去掉切换页面的动画
                } else if (i == R.id.rb_news) {
                    mViewPager.setCurrentItem(1, false);
                } else if (i == R.id.rb_service) {
                    mViewPager.setCurrentItem(2, false);
                } else if (i == R.id.rb_gov) {
                    mViewPager.setCurrentItem(3, false);
                } else if (i == R.id.rb_setting) {
                    mViewPager.setCurrentItem(4, false);
                }
            }
        });
        //防止频繁的销毁视图
        mViewPager.setOffscreenPageLimit(4);
    }
}
```
> 在大部分时候，项目中的ViewPager会和Fragment同时出现，每一个ViewPager的页面就是一个Fragment。
Android提供了一些专门的适配器来让ViewPager与Fragment一起工作，也就是FragmentPagerAdapter与FragmentStatePagerAdapter。

>FragmentPagerAdapter继承自PagerAdapter ，主要用来展示多个Fragment页面，并且每一个Fragment都会被保存在fragment manager中。 
FragmentPagerAdapter最适用于那种少量且相对静态的页面，例如几个tab页。每一个用户访问过的fragment都会被保存在内存中，尽管他的视图层级可能会在不可见时被销毁。这可能导致大量的内存因为fragment实例能够拥有任意数量的状态。对于较多的页面集合，更推荐使用FragmentStatePagerAdapter。 
当使用FragmentPagerAdapter的时候对应的ViewPager必须拥有一个有效的ID集。 
FragmentPagerAdapter的派生类只需要实现getItem(int)和getCount()即可。

- FragmentPagerAdapter适配器的实现
```
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
```
里面的几个fragment都是简单布局，就不贴了。主要讲解下fragment1，因为它里面包含着另外两个viewpager。
 
#### 3. 中间层的viewpager的布局实现
---
- 一个简单的tablayout配合viewpager使用，对tablayout的样式做了设定。

```
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
              xmlns:tools="http://schemas.android.com/tools"
              xmlns:app="http://schemas.android.com/apk/res-auto"
              android:layout_width="match_parent"
              android:layout_height="match_parent"
              android:orientation="vertical">
    <android.support.design.widget.TabLayout
        android:id="@+id/tab_main"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:background="#1e8ae8"
        app:tabGravity="fill"
        app:tabIndicatorColor="#fff"
        app:tabIndicatorHeight="4dp"
        app:tabMode="fixed"
        app:tabSelectedTextColor="#fff"
        app:tabTextColor="#97c8f4"
        >
    </android.support.design.widget.TabLayout>
 
    <android.support.v4.view.ViewPager
        android:id="@+id/vp_menu_pager"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        >
    </android.support.v4.view.ViewPager>
 
</LinearLayout>
```
#### 4. 中间层的viewpager的代码实现
---
```
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
 
    //初始化数据
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
```
- 中间层的viewpager的FragmentPagerAdapter适配器
```
public class MyTabFragmentPagerAdapter extends FragmentPagerAdapter {
    private String[] mTitles = new String[]{"推荐", "圆桌", "热门","收藏"};
    private List<Fragment> fragments;
 
    public MyTabFragmentPagerAdapter(FragmentManager fm,List<Fragment> fragments) {
        super(fm);
        this.fragments=fragments;
    }
 
    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }
 
    @Override
    public int getCount() {
        return mTitles.length;
    }
 
    //用来设置tab的标题
    @Override
    public CharSequence getPageTitle(int position) {
        return mTitles[position];
    }
}
```

#### 5. 顶层的viewpager的实现
---
- 在布局只是在fragment中放入一个recycleview，而轮播的viewpager是作为头布局加入recycleview中的。

- 下面是头布局的代码
```
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
              xmlns:app="http://schemas.android.com/apk/res-auto"
              android:layout_width="match_parent"
              android:layout_height="wrap_content"
              android:orientation="vertical">
 
    <android.support.v4.view.ViewPager
        android:id="@+id/vp_tab_headview"
        android:layout_width="match_parent"
        android:layout_height="180dp">
    </android.support.v4.view.ViewPager>
 
    <RelativeLayout
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:layout_alignParentBottom="true"
        android:background="#fff">
 
        <RelativeLayout
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:layout_centerInParent="true">
            //在布局中添加viewpagerindicator，通过自定义属性设置好外观
            <com.viewpagerindicator.CirclePageIndicator
                android:id="@+id/indicator"
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                android:padding="10dip"
                app:fillColor="#f00"
                app:pageColor="#e0e0e0"
                app:radius="4dp"
                app:strokeWidth="0dp"/>
        </RelativeLayout>
 
    </RelativeLayout>
</LinearLayout>
```
- 下面是TabFragment1 的逻辑代码
```
//省略viewpager的adapter，因为不重要
public class TabFragment1 extends Fragment {
    private RecyclerView mRecyclerView;
    private List<String> mDatas;
    private ArrayList<ImageView> imageList;
 
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tab_fragment1, container, false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycle);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        initdata();//初始化数据
        mRecyclerView.setAdapter(new MyAdapter(mDatas));
        return view;
    }
 
    private void initdata() {
        //初始化recycleview的数据
        mDatas = new ArrayList<String>();
        for (int i = 0; i < 45; i++) {
            mDatas.add("item" + i);
        }
        //初始化viewpager的数据
        int[] imageResIDs = {R.drawable.a, R.drawable.b, R.drawable.c};
        imageList = new ArrayList<ImageView>();
        for (int i = 0; i < imageResIDs.length; i++) {
            ImageView image = new ImageView(getActivity());
            image.setBackgroundResource(imageResIDs[i]);
            imageList.add(image);
        }
    }
 
    class MyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        private List<String> mDatas;
        private static final int HEAD_VIEW = 0;//头布局
        private static final int BODY_VIEW = 1;//内容布局
        private MyPagerAdapter mPagerAdapter = new MyPagerAdapter();
 
        //创建构造参数，用来接受数据集
        public MyAdapter(List<String> datas) {
            this.mDatas = datas;
        }
 
        //创建ViewHolder
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            if (viewType == HEAD_VIEW) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.headview_recycleview, parent, false);
                MyHeadViewHolder viewHolder = new MyHeadViewHolder(view);
                return viewHolder;
            }
            if (viewType == BODY_VIEW) {
                //加载布局文件
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_recycle, parent, false);
                MyBodyViewHolder viewHolder = new MyBodyViewHolder(view);
                return viewHolder;
            }
            return null;
        }
 
        //绑定ViewHolder
        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            //将数据填充到具体的view中
            if (holder instanceof MyHeadViewHolder) {
                ((MyHeadViewHolder) holder).mViewPager.setAdapter(mPagerAdapter);
                ((MyHeadViewHolder) holder).indicator.onPageSelected(0);
                ((MyHeadViewHolder) holder).indicator.setViewPager(((MyHeadViewHolder) holder).mViewPager);
                ((MyHeadViewHolder) holder).indicator.setSnap(true);
            }
            if (holder instanceof MyBodyViewHolder) {
                ((MyBodyViewHolder) holder).tv.setText(mDatas.get(position-1));
            }
        }
 
        @Override
        public int getItemCount() {
            return mDatas.size() + 1;
        }
 
        //如果是第一项，则加载头布局
        @Override
        public int getItemViewType(int position) {
            if (position == 0) {
                return HEAD_VIEW;
            } else {
                return BODY_VIEW;
            }
        }
    }
 
    //头布局的viewholder
    class MyHeadViewHolder extends RecyclerView.ViewHolder {
        ViewPager mViewPager;
        CirclePageIndicator indicator; //定义indicator
 
        public MyHeadViewHolder(View itemView) {
            super(itemView);
            mViewPager = (ViewPager) itemView.findViewById(R.id.vp_tab_headview);
            indicator = (CirclePageIndicator) itemView.findViewById(R.id.indicator);
        }
    }
 
    class MyBodyViewHolder extends RecyclerView.ViewHolder {
        TextView tv;
 
        public MyBodyViewHolder(View itemView) {
            super(itemView);
            tv = (TextView) itemView.findViewById(R.id.recycle_tv);
        }
    }
}
```
- ViewPager+Fragment配合使用中的问题

![切换后页面被销毁](http://upload-images.jianshu.io/upload_images/1877523-88eefff3c77774c4.gif?imageMogr2/auto-orient/strip)

如图，切换的时候可能导致页面被销毁。
- 解决方案：防止频繁的销毁视图，可以setOffscreenPageLimit(2)或者重写PagerAdaper的destroyItem方法为空。



这里是[项目地址](https://github.com/zjxuzhj/ViewPagerDemo)。

参考
http://blog.csdn.net/never_cxb/article/details/50520270
http://www.open-open.com/lib/view/open1431174803882.html
http://www.imooc.com/article/2742
