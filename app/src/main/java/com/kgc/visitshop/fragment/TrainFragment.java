package com.kgc.visitshop.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kgc.visitshop.R;
import com.kgc.visitshop.utils.LogUtils;


/**
 * 培训
 */
public class TrainFragment extends BaseFragment{

	private View view;


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_train, container, false);
		LogUtils.i(TAG,"培训界面加载..........");
		return view;
	}
}
