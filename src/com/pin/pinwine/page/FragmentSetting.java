package com.pin.pinwine.page;

import com.pin.pinwine.R;
import com.pin.pinwine.activity.LoginActivity;
import com.pin.pinwine.activity.SettingActivity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class FragmentSetting extends Fragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_setting, container, false);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		initView();
	}

	private void initView()
	{
		RelativeLayout rl = (RelativeLayout)getActivity().findViewById(R.id.re_myinfo);
		rl.getBackground().setAlpha(100);
		
		RelativeLayout rl_setting = (RelativeLayout)getActivity().findViewById(R.id.rl_setting);
		rl_setting.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				startActivity(new Intent(getActivity(), SettingActivity.class));
			}
		});
		
		TextView btLogin = (TextView)getActivity().findViewById(R.id.tv_login);
		btLogin.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				startActivity(new Intent(getActivity(), LoginActivity.class));
			}
		});
	}
}
