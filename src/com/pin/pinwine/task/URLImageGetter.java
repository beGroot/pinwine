package com.pin.pinwine.task;

import java.net.URL;
import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.text.Html.ImageGetter;
import android.view.Display;
import android.widget.TextView;

public class URLImageGetter implements ImageGetter {
	TextView textView;
	Context context;
	int width;
	int height;

	public URLImageGetter(Context contxt, TextView textView, int w, int h) {
		this.context = contxt;
		this.textView = textView;
		this.width = w;
		this.height = h;
	}

	@Override
	public Drawable getDrawable(String paramString) {
		URLDrawable urlDrawable = new URLDrawable(context);

		ImageGetterAsyncTask getterTask = new ImageGetterAsyncTask(urlDrawable);
		getterTask.execute(paramString);
		return urlDrawable;
	}

	public class ImageGetterAsyncTask extends AsyncTask<String, Void, Drawable> {
		URLDrawable urlDrawable;

		public ImageGetterAsyncTask(URLDrawable drawable) {
			this.urlDrawable = drawable;
		}

		@Override
		protected void onPostExecute(Drawable result) {
			if (result != null) {
				urlDrawable.drawable = result;
				//System.out.println("result height:"+result.getBounds().bottom);
				//URLImageGetter.this.textView.setMinHeight(URLImageGetter.this.textView.getHeight()+result.getBounds().bottom);
				URLImageGetter.this.textView.requestLayout();
				URLImageGetter.this.textView.invalidate();
			}
		}

		@Override
		protected Drawable doInBackground(String... params) {
			String source = params[0];
			return fetchDrawable(source);
		}

		public Drawable fetchDrawable(String url) {
			Drawable drawable = null;
			URL Url;
			try {
				Url = new URL(url);
				drawable = Drawable.createFromStream(Url.openStream(), "");
			} catch (Exception e) {
				e.printStackTrace();
				//System.out.println("������������������������������������");
				return null;
			}
			// ����������ͼƬ
			/*Rect bounds = getDefaultImageBounds(context);
			int newwidth = bounds.width();
			int newheight = bounds.height();
			double factor = 1;
			double fx = (double) drawable.getIntrinsicWidth()/ (double) newwidth;
			double fy = (double) drawable.getIntrinsicHeight()/ (double) newheight;
			factor = fx > fy ? fx : fy;
			if (factor < 1)
				factor = 1;
			newwidth = (int) (drawable.getIntrinsicWidth() / factor);
			newheight = (int) (drawable.getIntrinsicHeight() / factor);*/
			
			int newwidth = width*3/4;
			double fx = (double)newwidth/ (double)drawable.getIntrinsicWidth();
			int newheight = (int)((double)drawable.getIntrinsicHeight()*fx);
			int left = (width - newwidth)/2;
			//System.out.println("new widht:"+newwidth+", new height:"+newheight);
			drawable.setBounds(left, 0, left+newwidth, newheight);
			return drawable;
		}
	}

	// Ԥ��ͼƬ��߱���Ϊ 4:3
	@SuppressWarnings("deprecation")
	public Rect getDefaultImageBounds(Context context) {
		Display display = ((Activity) context).getWindowManager()
				.getDefaultDisplay();
		int width = display.getWidth();
		int height = (int) (width * 3 / 4);
		Rect bounds = new Rect(0, 0, width, height);
		return bounds;
	}
}