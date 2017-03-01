package com.pin.pinwine.page;

import java.util.List;
import java.util.Random;

import net.tsz.afinal.core.AsyncTask;

import com.pin.pinwine.R;
import com.pin.pinwine.activity.LoginActivity;
import com.pin.pinwine.activity.WineInfoActivity;
import com.pin.pinwine.adapter.WinePicListAdapter;
import com.pin.pinwine.constant.ConstantUtil;
import com.pin.pinwine.manager.NetworkDetector;
import com.pin.pinwine.manager.WinePicHttpUtil;
import com.pin.pinwine.model.WineModel;
import com.pin.pinwine.view.CircularProgress;
import com.pin.pinwine.view.XListView;
import com.pin.pinwine.view.XListView.IXListViewListener;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.AdapterView.OnItemClickListener;

public class FragmentHome extends Fragment implements OnClickListener, IXListViewListener {

	private LinearLayout netexceptionLayout, nodataLayout;
	private CircularProgress progressBar;
	private int operation_type = -1;
	private List<WineModel> wineList;
	private WinePicListAdapter adapter;
	private XListView lv_common;
	private boolean network = false;
	private int randNum = 0;
	private boolean flag=false;
	private int page = 1;
	private String id;
	private int rows = 10;
	private int wWidth = 0;
	private int wHeight = 0;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_home, container, false);
	}
	
	
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		
		initView();
		initXListView();
		checknetworkAndshowData();
		
	}



	private void initView()
	{
		
		Random rand = new Random();
		randNum = rand.nextInt(8);// /生成0--7的随机数
		netexceptionLayout = (LinearLayout) getView().findViewById(R.id.netexception);
		nodataLayout = (LinearLayout) getView().findViewById(R.id.nodata);
		progressBar = (CircularProgress) getView().findViewById(R.id.progressbar);
		lv_common = (XListView) getView().findViewById(R.id.listView);
		lv_common.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View view, int arg2,
					long arg3) {
				startActivity(new Intent(getActivity(), WineInfoActivity.class));
			}
		});
		
		netexceptionLayout.setOnClickListener(this);
		nodataLayout.setOnClickListener(this);
		
		operation_type = ConstantUtil.OPERATION_TYPE_LOAD;
		
		DisplayMetrics  dm = new DisplayMetrics(); 
		getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
		wWidth = dm.widthPixels;
	    wHeight = dm.heightPixels;
	
	}



	// 刷新完 停止下拉刷新
			private void onLoad() {
				lv_common.stopRefresh();
				lv_common.stopLoadMore();
				lv_common.setRefreshTime(getResources().getString(R.string.ganggangstr));
			}

		@Override
		public void onRefresh() {
			// TODO Auto-generated method stub
			rows = 10;
			page = 1;
			operation_type = ConstantUtil.OPERATION_TYPE_REFRESH;
			checknetworkAndshowData();
			onLoad();
		}

		@Override
		public void onLoadMore() {
			// TODO Auto-generated method stub
			rows = 10;
			page += 1;
			operation_type = ConstantUtil.OPERATION_TYPE_LOAD_MORE;
			checknetworkAndshowData();
			onLoad();
		}

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			int id = v.getId();
			switch (id) {
			case R.id.netexception:
				checknetworkAndshowData();
				break;
			case R.id.nodata:
				checknetworkAndshowData();
				break;
			default:
				break;
			}
		}
		
		// 初始化xListView
				private void initXListView() {
					lv_common.setPullLoadEnable(true);
					lv_common.setPullRefreshEnable(true);
					lv_common.setXListViewListener(this);
				}
		
		private void checknetworkAndshowData() {
			network = NetworkDetector.detect(getActivity());
			if (operation_type == ConstantUtil.OPERATION_TYPE_REFRESH) {// 下拉刷新
				Random rand = new Random();
				randNum = rand.nextInt(7);// /生成0--7的随机数
			}
			System.out.println("network: "+network);
			if (network) {
				netexceptionLayout.setVisibility(View.GONE);
				if(flag){
					WinePicListTask task = new WinePicListTask();
					task.execute(ConstantUtil.PIC_LIST_URL, page + "", rows + "", id);
				}else{
					WinePicListTask task = new WinePicListTask();
					task.execute(ConstantUtil.PIC_LIST_URL, page + "", rows + "", randNum + "");
				}
			} else {
				netexceptionLayout.setVisibility(View.VISIBLE);
			}
		}
		
		private class WinePicListTask extends AsyncTask<String, Object, List<WineModel>> {

			@Override
			protected List<WineModel> doInBackground(String... params) {
				return WinePicHttpUtil.requestPicList(params[0], params[1], params[2],params[3]);
			}

			@Override
			protected void onPostExecute(List<WineModel> listItems) {
				progressBar.setVisibility(View.GONE);
				if (listItems != null && listItems.size() != 0) {
					nodataLayout.setVisibility(View.GONE);
					if (operation_type == ConstantUtil.OPERATION_TYPE_LOAD || operation_type == ConstantUtil.OPERATION_TYPE_REFRESH) {
						wineList = listItems;
					} else if (operation_type == ConstantUtil.OPERATION_TYPE_LOAD_MORE) {
						if (wineList != null) {
							wineList.addAll(wineList.size(), listItems);
							if (listItems.size() < 10) {
								//ToastUtil.show(MainActivity.this,R.string.nodatashow);
							}
						}
					}
					if (wineList != null && wineList.size() != 0) {
						adapter = new WinePicListAdapter(getActivity(), wineList, wWidth, wHeight);
						lv_common.setAdapter(adapter);
						lv_common.setSelection(wineList.size() - listItems.size());
					}
				} else {
					if (operation_type == ConstantUtil.OPERATION_TYPE_LOAD_MORE || operation_type == ConstantUtil.OPERATION_TYPE_REFRESH) {
						//ToastUtil.show(MainActivity.this, R.string.nodatashow);
					} else {
						nodataLayout.setVisibility(View.VISIBLE);
					}
				}
			}

			@Override
			protected void onPreExecute() {
				progressBar.setVisibility(View.VISIBLE);
			}
		}
	
}
