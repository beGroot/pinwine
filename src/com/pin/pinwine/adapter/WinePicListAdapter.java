package com.pin.pinwine.adapter;

import java.util.List;

import net.tsz.afinal.FinalBitmap;

import com.pin.pinwine.R;
import com.pin.pinwine.model.WineModel;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ImageView;

public class WinePicListAdapter extends BaseAdapter {
	
	private LayoutInflater mInflater;
	private List<WineModel> wineList;
	private Context mContext;
	private FinalBitmap fb;
	private int wWidth;
	private int wHeight;
	
	public WinePicListAdapter(Context context, List<WineModel> mData, int width, int height) {
		this.mContext=context;
		this.mInflater = LayoutInflater.from(context);
		this.wineList = mData;
		fb = FinalBitmap.create(context);//³õÊ¼»¯FinalBitmapÄ£¿é
		fb.configLoadingImage(R.drawable.b_bgpic);
		wWidth = width;
		wHeight = height;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return wineList.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return wineList.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int p, View v, ViewGroup arg2) {
		// TODO Auto-generated method stub
		ViewHolder holder = null;
		if (v == null) {
			holder = new ViewHolder();
			v = mInflater.inflate(R.layout.wine_pic_list_item,null);
			holder.picView=(ImageView)v.findViewById(R.id.pic);
			v.setTag(holder);
		}else{
			holder = (ViewHolder) v.getTag();
		}
		
		//Log.v("----lyyo---", mContext.getString(R.string.app_name));
		fb.configBitmapMaxWidth(wWidth);	
		fb.display(holder.picView,wineList.get(p).getImg());
		return v;
	}
	
	public final class ViewHolder {
		public ImageView picView;
	}

}
