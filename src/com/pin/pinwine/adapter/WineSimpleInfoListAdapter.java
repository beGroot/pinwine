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
import android.widget.TextView;

public class WineSimpleInfoListAdapter extends BaseAdapter {
	
	private LayoutInflater mInflater;
	private List<String> infoList;
	private Context mContext;
	
	@Override
	public boolean areAllItemsEnabled() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isEnabled(int position) {
		// TODO Auto-generated method stub
		return false;
	}

	public WineSimpleInfoListAdapter(Context context, List<String> mData) {
		this.mContext=context;
		this.mInflater = LayoutInflater.from(context);
		this.infoList = mData;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return infoList.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return infoList.get(arg0);
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
			v = mInflater.inflate(R.layout.wine_simple_info_list_item,null);
			holder.tv=(TextView)v.findViewById(R.id.tv_wine_info);
			holder.tv.setText(infoList.get(p));
			v.setTag(holder);
		}else{
			holder = (ViewHolder) v.getTag();
		}
		
		return v;
	}
	
	public final class ViewHolder {
		public TextView tv;
	}

}
