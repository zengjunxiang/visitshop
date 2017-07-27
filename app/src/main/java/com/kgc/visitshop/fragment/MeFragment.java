package com.kgc.visitshop.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bdqn.settingitemlibrary.SetItemView;
import com.kgc.visitshop.R;

/**
 * 个人中心-主界面
 */
public class MeFragment extends BaseFragment {
    private View mView;
    private SetItemView mMeItem;
    private SetItemView mAboutItem;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //加载布局
        mView = inflater.inflate(R.layout.fragment_me, container, false);
        initView();

        return mView;
    }

    /**
     * 初始化控件信息
     */
    private void initView() {
        mMeItem = (SetItemView) mView.findViewById(R.id.rl_me);
        mAboutItem = (SetItemView) mView.findViewById(R.id.rl_about);

        mMeItem.setmOnSetItemClick(new SetItemView.OnSetItemClick() {
            @Override
            public void click() {
                Toast.makeText(mActivity, "点击了个人资料", Toast.LENGTH_SHORT).show();
            }
        });
        mAboutItem.setmOnSetItemClick(new SetItemView.OnSetItemClick() {
            @Override
            public void click() {
                Toast.makeText(mActivity, "点击了关于", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
