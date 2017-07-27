package com.kgc.visitshop.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.kgc.visitshop.MainActivity;
import com.kgc.visitshop.R;
import com.kgc.visitshop.adapters.InfoListBaseAdapter;
import com.kgc.visitshop.adapters.TaskListBaseAdapter;
import com.kgc.visitshop.bean.AnnImageResult;
import com.kgc.visitshop.bean.AnnImgs;
import com.kgc.visitshop.bean.InfoResult;
import com.kgc.visitshop.bean.InfoResultBody;
import com.kgc.visitshop.bean.Task;
import com.kgc.visitshop.bean.TaskBody;
import com.kgc.visitshop.net.OkHttpManager;
import com.kgc.visitshop.utils.Constant;
import com.kgc.visitshop.utils.SharePreUtil;
import com.squareup.picasso.Picasso;

import org.litepal.crud.DataSupport;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.Request;

/**
 * 首页--默认展示
 */

public class HomeFragment extends BaseFragment implements View.OnClickListener {

    private View view;//根布局
    private RelativeLayout progress, home_fragment_task;
    private TextView task_more, info_more;
    private TextView sort_shop, sort_visit, sort_train;
    private ViewPager vp;
    private LinearLayout rl_http_failed;
    private RecyclerView task_lv, info_lv;
    private InfoListBaseAdapter info_adapter;
    private TaskListBaseAdapter task_adapter;
    private MainActivity mainActivity;
    private List<View> views;//轮播图展示图片view
    private MViewpager vp_adapter;//viewpager适配器
    private Timer timer;//计时器
    private LinearLayout layout;//轮播图下标集合
    private int count = 0;//轮播图当前下标
    public final int GetImags = 1014;//获取广告图返回码
    private final int AnnFaild = 1011;//获取广告图失败返回码

    private String userid;//用户id
    private Boolean isLoad;//是否登录
    private List<InfoResultBody> info_list;
    private List<TaskBody> task_list;
    private int mShowSize = 3;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home, container, false);
        initView();//初始化UI组件
        initdata();//初始化数据
        return view;
    }

    /**
     * 初始化控件加载
     */
    private void initView() {
        vp = (ViewPager) view.findViewById(R.id.fragment_img_viewpager);
        home_fragment_task = (RelativeLayout) view.findViewById(R.id.home_fragment_task);
        layout = (LinearLayout) view.findViewById(R.id.fragment_point_subscript);
        rl_http_failed = (LinearLayout) view.findViewById(R.id.rl_http_failed);
        rl_http_failed.setOnClickListener(this);
        progress = (RelativeLayout) view.findViewById(R.id.message_fregment_progress);
        task_more = (TextView) view.findViewById(R.id.fragment_home_task_more);
        task_more.setOnClickListener(this);
        info_more = (TextView) view.findViewById(R.id.fragment_home_info_more);
        info_more.setOnClickListener(this);
        sort_shop = (TextView) view.findViewById(R.id.fragment_sort_shop);
        sort_shop.setOnClickListener(this);
        sort_visit = (TextView) view.findViewById(R.id.fragment_sort_visit);
        sort_visit.setOnClickListener(this);
        sort_train = (TextView) view.findViewById(R.id.fragment_sort_train);
        sort_train.setOnClickListener(this);
        layout = (LinearLayout) view.findViewById(R.id.fragment_point_subscript);
        task_lv = (RecyclerView) view.findViewById(R.id.fragment_home_task_list);
        info_lv = (RecyclerView) view.findViewById(R.id.fragment_home_info_list);

        task_lv.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        info_lv.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));

        task_lv.setNestedScrollingEnabled(false);
        info_lv.setNestedScrollingEnabled(false);
    }

    /**
     * 初始化数据展示
     */
    private void initdata() {
        userid = SharePreUtil.GetShareString(mActivity, "userid");
        if (TextUtils.isEmpty(userid)) {
            isLoad = false;
            Toast.makeText(mContext, R.string.please_login, Toast.LENGTH_SHORT).show();
        } else {
            isLoad = true;
        }
        if (views == null) {
            views = new ArrayList<View>();
        }
        vp_adapter = new MViewpager();
        vp.setAdapter(vp_adapter);
        //添加界面滚动监听
        vp.addOnPageChangeListener(vp_adapter);
        progress.setVisibility(View.VISIBLE);
        //首页轮播图获取
        OkHttpManager.getInstance().getNet(Constant.Announcement, new OkHttpManager.ResultCallback() {
            @Override
            public void onFailed(Request request, IOException e) {
                getAnnFailure();
            }

            @Override
            public void onSuccess(String response) {
                getAnnSuccess(response);
            }
        });
        //请求资讯
        OkHttpManager.getInstance().getNet(Constant.Info + "?pagenum=" + 1 + "&type=" + 0, new OkHttpManager.ResultCallback() {
            @Override
            public void onFailed(Request request, IOException e) {
                getInfoFailed();
            }

            @Override
            public void onSuccess(String response) {
                getInfoSuccess(response);
            }
        });
        if (isLoad) {
            home_fragment_task.setVisibility(View.VISIBLE);
            //登录后请求任务
            OkHttpManager.getInstance().getNet(Constant.Task + "?pagenum=" + 1, new OkHttpManager.ResultCallback() {
                @Override
                public void onFailed(Request request, IOException e) {
                    getTaskFailed();
                }

                @Override
                public void onSuccess(String response) {
                    getTaskSuccess(response);
                }
            });
        }
    }

    /**
     * 从服务端获取任务信息失败
     * 此时展示数据库缓存数据
     */
    private void getTaskFailed() {
        progress.setVisibility(View.GONE);
        rl_http_failed.setVisibility(View.VISIBLE);
        //展示保存数据
        task_list = DataSupport.findAll(TaskBody.class);
        if (task_list != null) {
            task_adapter = new TaskListBaseAdapter(mContext, task_list, mShowSize);
            task_lv.setAdapter(task_adapter);
        }
    }

    /**
     * 从服务端获取任务信息成功
     */
    private void getTaskSuccess(String response) {
        progress.setVisibility(View.GONE);
        Gson gson = new Gson();
        Task task = gson.fromJson(response, Task.class);
        //适配资讯列表
        if (task.getBody() != null) {
            task_list = task.getBody();
            task_adapter = new TaskListBaseAdapter(mContext, task_list, mShowSize);
            task_lv.setAdapter(task_adapter);
            //从数据库清除数据保存
            DataSupport.deleteAll(TaskBody.class);
            //添加新数据到数据库
            DataSupport.saveAll(task.getBody());
        }
    }

    /**
     * 从服务端获取资讯信息失败
     * 此时展示数据库缓存数据
     */
    private void getInfoFailed() {
        progress.setVisibility(View.GONE);
        rl_http_failed.setVisibility(View.VISIBLE);
        //展示保存数据
        info_list = DataSupport.findAll(InfoResultBody.class);
        if (info_list != null) {
            info_adapter = new InfoListBaseAdapter(mContext, info_list, mShowSize);
            info_lv.setAdapter(info_adapter);
        }
    }

    /**
     * 从服务端获取资讯信息成功
     */
    private void getInfoSuccess(String response) {
        progress.setVisibility(View.GONE);
        Gson gson1 = new Gson();
        InfoResult info = gson1.fromJson(response, InfoResult.class);
        //适配资讯列表
        if (info.getBody() != null) {
            info_list = info.getBody();
            info_adapter = new InfoListBaseAdapter(mContext, info_list, mShowSize);
            info_lv.setAdapter(info_adapter);
            //从数据库清除数据保存
            DataSupport.deleteAll(InfoResultBody.class);
            //添加新数据到数据库
            DataSupport.saveAll(info.getBody());
        }
    }

    /**
     * 从服务端获取公告信息失败
     * 此时展示数据库缓存数据
     */
    private void getAnnFailure() {
        //从数据库中获取数据
        List<AnnImgs> imgs_dblist = DataSupport.findAll(AnnImgs.class);
        if (imgs_dblist != null) {
            updateAnnShow(imgs_dblist);
        }
    }


    /**
     * 从服务端获取公告信息成功
     */
    private void getAnnSuccess(String resultImgs) {
        //服务端返回有效数据则展示，没有不做处理
        if (resultImgs != null && !"".equals(resultImgs)) {
            Gson gson = new Gson();
            AnnImageResult air = gson.fromJson(resultImgs, AnnImageResult.class);
            List<AnnImgs> imgs_list = air.getBody();
            if (imgs_list == null) {
                imgs_list = new ArrayList<AnnImgs>();
            }
            updateAnnShow(imgs_list);
            //更新缓存
            if (imgs_list.size() > 0) {
                //从数据库清除数据保存
                DataSupport.deleteAll(AnnImgs.class);
                //添加新数据到数据库
                DataSupport.saveAll(imgs_list);
            }
        }
    }

    /**
     * 根据公告图片地址动态更新界面
     *
     * @param imgs_dblist
     */
    private void updateAnnShow(List<AnnImgs> imgs_dblist) {
        views.clear();
        //动态创建轮播展示view
        for (int i = 0; i < imgs_dblist.size(); i++) {
            ImageView img = new ImageView(mActivity);
            img.setScaleType(ImageView.ScaleType.CENTER_CROP);
            //通过网络地址显示图片
            Picasso.with(mActivity)
                    .load(Constant.BaseUrl + imgs_dblist.get(i).getImgUrl())
                    .into(img);
            views.add(img);
        }
        //更新界面显示
        vp_adapter.notifyDataSetChanged();
        //添加指示器下标点
        initPoint();
        //开启任务计时器
        if (timer == null) {
            timer = new Timer();
            timer.schedule(task, 0, 3000);
        }
    }

    @Override
    public void onClick(View v) {

    }

    //创建viewpager适配器
    class MViewpager extends PagerAdapter implements ViewPager.OnPageChangeListener {
        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        @Override
        public int getCount() {
            return views.size();
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(views.get(position));
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(views.get(position));
            return views.get(position);
        }

        /**
         * viewpager滑动监听，动态更改指示下标的选中状态
         *
         * @param position
         */
        @Override
        public void onPageSelected(int position) {
            for (int i = 0; i < layout.getChildCount(); i++) {
                ImageView image = (ImageView) layout.getChildAt(i);
                if (i == position) {
                    image.setSelected(true);
                } else {
                    image.setSelected(false);
                }
            }
        }

        @Override
        public void onPageScrollStateChanged(int arg0) {
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }
    }

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case Constant.Scroll://接收滚动消息，并执行
                    vp.setCurrentItem(count);
                    break;
                default:
                    break;
            }
        }
    };

    /**
     * 创建图片变化下标图标
     */
    public void initPoint() {
        //清除所有指示下标
        layout.removeAllViews();
        for (int i = 0; i < views.size(); i++) {
            ImageView img = new ImageView(mActivity);
            //添加下标圆点参数
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            params.leftMargin = 5;
            params.rightMargin = 5;
            img.setLayoutParams(params);
            img.setImageResource(R.drawable.sns_v2_page_point);
            if (i == 0) {
                img.setSelected(true);
            }
            layout.addView(img);
        }
    }

    // 创建记时器发送图片轮播消息
    TimerTask task = new TimerTask() {
        @Override
        public void run() {
            if (count == views.size()) {
                count = 0;
            } else {
                count = count + 1;
            }
            mHandler.sendEmptyMessage(Constant.Scroll);
        }
    };
}
