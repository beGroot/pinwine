package com.pin.pinwine.activity;

import java.net.URL;
import java.util.List;

import net.tsz.afinal.core.AsyncTask;

import com.pin.pinwine.R;
import com.pin.pinwine.R.layout;
import com.pin.pinwine.R.menu;
import com.pin.pinwine.adapter.WinePicListAdapter;
import com.pin.pinwine.constant.ConstantUtil;
import com.pin.pinwine.manager.WinePicHttpUtil;
import com.pin.pinwine.model.WineModel;
import com.pin.pinwine.task.URLImageGetter;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.text.Html.ImageGetter;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

public class WineDetailActivity extends Activity {

	private TextView tv_detail;
	private int wWidth;
	private int wHeight;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_wine_detail);

        initView();
	}
	
	private void initView()
	{
		DisplayMetrics  dm = new DisplayMetrics(); 
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		wWidth = dm.widthPixels;
	    wHeight = dm.heightPixels;
	    
		tv_detail = (TextView)findViewById(R.id.tv_wine_detail);
		URLImageGetter ReviewImgGetter = new URLImageGetter(WineDetailActivity.this, tv_detail, wWidth, wHeight);
		String html="<html><body><img src=\""+ConstantUtil.SERVER_URL+"/scoresys/img/11.jpg\"/>\u3000\u3000此款酒产自星光闪耀的罗曼尼-康帝园，是世界上最昂贵的葡萄酒，在市场上是找不到的，只有在特别的地方才会有它的身影。它是百万富翁拥有的，只有亿万富翁才能享受得起的葡萄酒。<br>"  
                +"<img src=\""+ConstantUtil.SERVER_URL+"/scoresys/img/12.jpg\"/>\u3000\u3000罗曼尼.康帝酒庄（Domaine de La Romanee-Conti），常被简称为“DRC”，是勃艮第（Burgundy）著名酒庄之一，不仅拥有罗曼尼?康帝（Romanee-Conti）和拉塔希（La Tache）这两个特级葡萄园，在其它特级葡萄园如里奇堡（Richebourg）、圣维旺（Romanee-Saint-Vivant）、依瑟索（Echezeaux）和大依瑟索（Grands Echezeaux）等都有园地。该酒庄生产的葡萄酒被认为是世界上最昂贵的葡萄酒之一，以厚重、复杂和耐存著称。酒庄葡萄酒通常选用产量极低的老藤葡萄作为原料，葡萄在酿造时还要经过严格挑选。当然，晚采收、好园地以及精心酿造等都是该酒庄成功的秘诀。<br>"  
                +"<img src=\""+ConstantUtil.SERVER_URL+"/scoresys/img/13.jpg\"/>\u3000\u3000黑皮诺（Pinot Noir）是酿制法国勃艮第（Burgundy）红葡萄酒的唯一品种，其所在的皮诺家族因它而得名。与适应能力强，管理成本低，而与个性鲜明的赤霞珠（Cabernet Sauvignon）相比，黑皮诺是一个需要葡萄种植者和酿酒师精心照顾的葡萄品种。一杯勃艮第地区品质较佳的黑皮诺红葡萄酒，会带给人无与伦比的感官享受。正因为如此，世界各地大部分野心勃勃的葡萄酒生产者们对这一性情不定的葡萄品种钟爱有加。尽管该品种在其原产地表现并不稳定，但目前除了极其炎热的地方，它几乎在世界各产酒区均有种植。</body></html>";  

		tv_detail.setText(Html.fromHtml(html, ReviewImgGetter, null));
		//tv_detail.setGravity(Gravity);
	}
	
	public ImageGetter getImageGetterInstance() {  
	    ImageGetter imgGetter = new Html.ImageGetter() {  
	        @Override  
	        public Drawable getDrawable(String source) {  
	              Drawable drawable = null;  
	              URL url;    
	              try {    
	            	  System.out.println("image in html:"+source);
	                  url = new URL(source);    
	                  drawable = Drawable.createFromStream(url.openStream(), "");  //获取网路图片  
	                  System.out.println("create image end");
	              } catch (Exception e) { 
	            	  System.out.println("oh no.....");
	            	  e.printStackTrace();
	                  return null;    
	              }    
	              System.out.println("width:"+drawable.getIntrinsicWidth()+", height:"+drawable.getIntrinsicHeight());
	              drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());  
	              return drawable;   
	        }  
	    };  
	    return imgGetter;  
	} 

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.pine_detail, menu);
		return true;
	}

}
