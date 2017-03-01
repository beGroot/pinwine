package com.pin.pinwine.activity;

import java.util.ArrayList;
import java.util.List;

import net.tsz.afinal.FinalBitmap;

import com.pin.pinwine.R;
import com.pin.pinwine.R.layout;
import com.pin.pinwine.R.menu;
import com.pin.pinwine.adapter.RelativePaperListAdapter;
import com.pin.pinwine.adapter.WineSimpleInfoListAdapter;
import com.pin.pinwine.constant.ConstantUtil;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class WineInfoActivity extends Activity {
	
	private FinalBitmap fb;
	private int wWidth;
	private WineSimpleInfoListAdapter infoAdapter;
	private RelativePaperListAdapter paperAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_wine_info);
		fb = FinalBitmap.create(this.getApplicationContext());
		fb.configLoadingImage(R.drawable.b_bgpic);
		
		DisplayMetrics  dm = new DisplayMetrics(); 
		this.getWindowManager().getDefaultDisplay().getMetrics(dm);
		wWidth = dm.widthPixels;
		
		
		
		initView();
	}
	
	private void initView()
	{
		ImageView iv_wine = (ImageView)findViewById(R.id.iv_wineinfo_img);
		String img_path = ConstantUtil.SERVER_URL + "/scoresys/img/5.jpg";
		fb.configBitmapMaxWidth(wWidth);	
		fb.display(iv_wine, img_path);
		
		ListView lv_info = (ListView)findViewById(R.id.lv_wineinfo_infos);
		List<String> infoList = new ArrayList<String>();
		infoList.add("酒类型：红葡萄酒");
		infoList.add("产区：法国>沃恩-罗曼尼");
		infoList.add("酒庄：罗曼尼•康帝酒庄");
		infoList.add("葡萄品种：黑皮诺");
		infoList.add("风格：凝练 余味悠长");
		infoList.add("年份：2009");
		infoAdapter = new WineSimpleInfoListAdapter(this,infoList);
		lv_info.setAdapter(infoAdapter);
		
		TextView tv_seemore = (TextView)findViewById(R.id.tv_wineinfo_seemore);
		tv_seemore.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				clickSeeMore();
			}
		});
		
		
		ListView lv_papers = (ListView)findViewById(R.id.lv_relative_papers);
		List<String> paperList = new ArrayList<String>();
		paperList.add("教你如何鉴别红酒的真伪");
		paperList.add("红葡萄酒与白葡萄酒的前世今生");
		paperList.add("红酒与食物的搭配");
		paperList.add("漫步帝王酒庄，探访红酒故乡");
		paperList.add("浓厚醇香，品味无穷");
		paperList.add("呼朋引友一起周末小酌品味人生");
		paperList.add("中国的红酒赤霞珠的由来和如何品尝");
		paperAdapter = new RelativePaperListAdapter(this,paperList);
		lv_papers.setAdapter(paperAdapter);
		lv_papers.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View view, int arg2,
					long arg3) {
			}
		});
	}
	
	public void clickSeeMore()
	{
		startActivity(new Intent(this, WineDetailActivity.class));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		//getMenuInflater().inflate(R.menu.pine_info, menu);
		return true;
	}

}
