package zhj.viewpagerdemo.tabfragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.viewpagerindicator.CirclePageIndicator;

import java.util.ArrayList;
import java.util.List;

import zhj.viewpagerdemo.R;

/**
 * Created by HongJay on 2016/8/11.
 */
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

        initdata();
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
                ((MyBodyViewHolder) holder).tv.setText(mDatas.get(position));
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
        CirclePageIndicator indicator;

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

    class MyPagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return imageList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(imageList.get(position));
            return imageList.get(position);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return Integer.toString(position);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }
}




