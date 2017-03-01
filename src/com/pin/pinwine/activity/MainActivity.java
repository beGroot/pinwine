package com.pin.pinwine.activity;

import java.util.List;
import java.util.Random;

import net.tsz.afinal.core.AsyncTask;

import com.pin.pinwine.R;
import com.pin.pinwine.adapter.WinePicListAdapter;
import com.pin.pinwine.constant.ConstantUtil;
import com.pin.pinwine.manager.NetworkDetector;
import com.pin.pinwine.manager.WinePicHttpUtil;
import com.pin.pinwine.model.WineModel;
import com.pin.pinwine.page.FragmentCommunity;
import com.pin.pinwine.page.FragmentHome;
import com.pin.pinwine.page.FragmentSetting;
import com.pin.pinwine.view.CircularProgress;
import com.pin.pinwine.view.XListView;
import com.pin.pinwine.view.XListView.IXListViewListener;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


public class MainActivity extends BaseActivity /*implements OnClickListener, IXListViewListener*/{
	
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
	
	private Fragment[] fragments;
	private int index;
	private int currentTabIndex;
	private ImageView[] imagebuttons;
    private TextView[] textviews;
    
    private FragmentHome homepage;
    private FragmentCommunity commpage;
    private FragmentSetting settingpage;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initView();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	private void initView()
	{
		homepage = new FragmentHome();
		commpage = new FragmentCommunity();
		settingpage = new FragmentSetting();
		fragments = new Fragment[] { homepage, commpage, settingpage };
		imagebuttons = new ImageView[3];
        imagebuttons[0] = (ImageView) findViewById(R.id.ib_home);
        imagebuttons[1] = (ImageView) findViewById(R.id.ib_comm);
        imagebuttons[2] = (ImageView) findViewById(R.id.ib_setting);
        imagebuttons[0].setSelected(true);
        
        textviews = new TextView[3];
        textviews[0] = (TextView) findViewById(R.id.tv_home);
        textviews[1] = (TextView) findViewById(R.id.tv_comm);
        textviews[2] = (TextView) findViewById(R.id.tv_setting);
        textviews[0].setTextColor(0xFF8B2252);
        
        getSupportFragmentManager().beginTransaction()
	        .add(R.id.fragment_container, homepage)
	        .add(R.id.fragment_container, commpage)
	        .add(R.id.fragment_container, settingpage)
	        .hide(commpage).hide(settingpage)
	        .show(homepage).commit();
	}
	
	// 初始化xListView
		/*private void initXListView() {
			lv_common.setPullLoadEnable(true);
			lv_common.setPullRefreshEnable(true);
			lv_common.setXListViewListener(this);
		}

		private void checknetworkAndshowData() {
			network = NetworkDetector.detect(MainActivity.this);
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
					adapter = new WinePicListAdapter(MainActivity.this, wineList, wWidth, wHeight);
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
	}*/
	
	public void onTabClicked(View view) {
        switch (view.getId()) {
        case R.id.re_home:
            index = 0;
            break;
        case R.id.re_comm:
            index = 1;
            break;
        case R.id.re_setting:
            index = 2;
            break;

        }

        if (currentTabIndex != index) {
            FragmentTransaction trx = getSupportFragmentManager()
                    .beginTransaction();
            trx.hide(fragments[currentTabIndex]);
            if (!fragments[index].isAdded()) {
                trx.add(R.id.fragment_container, fragments[index]);
            }
            trx.show(fragments[index]).commit();
        }
        imagebuttons[currentTabIndex].setSelected(false);
        // 把当前tab设为选中状态
        imagebuttons[index].setSelected(true);
        textviews[currentTabIndex].setTextColor(0xFF9A9A9A);
        textviews[index].setTextColor(0xFF8B2252);
        currentTabIndex = index;
    }

}
