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
		String html="<html><body><img src=\""+ConstantUtil.SERVER_URL+"/scoresys/img/11.jpg\"/>\u3000\u3000�˿�Ʋ����ǹ���ҫ��������-����԰�����������������Ѿƣ����г������Ҳ����ģ�ֻ�����ر�ĵط��Ż���������Ӱ�����ǰ�����ӵ�еģ�ֻ�������̲������ܵ�������Ѿơ�<br>"  
                +"<img src=\""+ConstantUtil.SERVER_URL+"/scoresys/img/12.jpg\"/>\u3000\u3000������.���۾�ׯ��Domaine de La Romanee-Conti�����������Ϊ��DRC�����ǲ��޵ڣ�Burgundy��������ׯ֮һ������ӵ��������?���ۣ�Romanee-Conti��������ϣ��La Tache���������ؼ�����԰���������ؼ�����԰�����汤��Richebourg����ʥά����Romanee-Saint-Vivant������ɪ����Echezeaux���ʹ���ɪ����Grands Echezeaux���ȶ���԰�ء��þ�ׯ���������ѾƱ���Ϊ���������������Ѿ�֮һ���Ժ��ء����Ӻ��ʹ����ơ���ׯ���Ѿ�ͨ��ѡ�ò������͵�����������Ϊԭ�ϣ�����������ʱ��Ҫ�����ϸ���ѡ����Ȼ������ա���԰���Լ���������ȶ��Ǹþ�ׯ�ɹ����ؾ���<br>"  
                +"<img src=\""+ConstantUtil.SERVER_URL+"/scoresys/img/13.jpg\"/>\u3000\u3000��Ƥŵ��Pinot Noir�������Ʒ������޵ڣ�Burgundy�������ѾƵ�ΨһƷ�֣������ڵ�Ƥŵ��������������������Ӧ����ǿ������ɱ��ͣ�������������ĳ�ϼ�飨Cabernet Sauvignon����ȣ���Ƥŵ��һ����Ҫ������ֲ�ߺ����ʦ�����չ˵�����Ʒ�֡�һ�����޵ڵ���Ʒ�ʽϼѵĺ�Ƥŵ�����Ѿƣ�������������ױȵĸй����ܡ�����Ϊ��ˣ�������ش󲿷�Ұ�Ĳ��������Ѿ��������Ƕ���һ���鲻��������Ʒ���Ӱ��мӡ����ܸ�Ʒ������ԭ���ر��ֲ����ȶ�����Ŀǰ���˼������ȵĵط����������������������������ֲ��</body></html>";  

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
	                  drawable = Drawable.createFromStream(url.openStream(), "");  //��ȡ��·ͼƬ  
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
