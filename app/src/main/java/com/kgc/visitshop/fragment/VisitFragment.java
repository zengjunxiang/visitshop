package com.kgc.visitshop.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kgc.visitshop.R;
import com.kgc.visitshop.utils.LogUtils;

/**
 * 拜访
 */
public class VisitFragment extends BaseFragment {
    private View mView;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_visit, container, false);
        LogUtils.i(TAG,"拜访界面加载..........");
        return mView;
    }
}
