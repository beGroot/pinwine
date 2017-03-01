package com.pin.pinwine.manager;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.TextUtils;
import android.util.DisplayMetrics;

import com.pin.pinwine.constant.ConstantUtil;
import com.pin.pinwine.model.WineModel;

public class WinePicHttpUtil {

	@SuppressLint("SimpleDateFormat")
	public static String transferLongToDate(Long millSec) {
		SimpleDateFormat sdf = new SimpleDateFormat("yy-MM-dd HH:mm:ss");
		Date date = new Date(millSec);
		return sdf.format(date);
	}
	
	public float convertPixelsToDp(Context ctx, float px) {
		DisplayMetrics metrics = ctx.getResources().getDisplayMetrics();
		float dp = px / (metrics.densityDpi / 160f);
		return dp;

	}

	public static int convertDpToPixelInt(Context context, float dp) {

		DisplayMetrics metrics = context.getResources().getDisplayMetrics();
		int px = (int) (dp * (metrics.densityDpi / 160f));
		return px;
	}
	
	public static float convertDpToPixel(Context context, float dp) {

		DisplayMetrics metrics = context.getResources().getDisplayMetrics();
		float px = (float) (dp * (metrics.densityDpi / 160f));
		return px;
	}

	/**
	 * 图片列表API接口-天狗开放阅图分类列表接口
	 * @param httpUrl
	 * @param page
	 * @param rows
	 * @return
	 */
	public static List<WineModel> requestPicList(String httpUrl,String page, String rows,String ids) {
		List<WineModel> listItems = new ArrayList<WineModel>();
		BufferedReader reader = null;
		String result = null;
		StringBuffer sbf = new StringBuffer();
		httpUrl = ConstantUtil.SERVER_URL + "/scoresys/CuisineCfg?method=6";
		try {
			URL url = new URL(httpUrl);
			HttpURLConnection connection = (HttpURLConnection) url .openConnection();
			connection.setRequestMethod("GET");
			connection.connect();
			InputStream is = connection.getInputStream();
			reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
			String strRead = null;
			while ((strRead = reader.readLine()) != null) {
				sbf.append(strRead);
				sbf.append("\r\n");
			}
			reader.close();
			result = sbf.toString();
			if (!TextUtils.isEmpty(result)) {
				//JSONObject jObj = new JSONObject(result);
				//String tngou = jObj.getString("tngou");
				JSONArray jsonArray = new JSONArray(/*tngou*/result);
				String id;
				String time;
				String img;
				String title;
				for (int i = 0; i < jsonArray.length(); i++) {
					/*JSONObject jsonObject = (JSONObject)jsonArray.get(i);
					id = jsonObject.getString("id");
					time = jsonObject.getString("time");
					img = jsonObject.getString("img");
					title = jsonObject.getString("title");*/

					WineModel model = new WineModel();
					model.setImg(ConstantUtil.SERVER_URL + "/scoresys/img/" + jsonArray.getString(i));
					listItems.add(model);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return listItems;
	}
	
	/**
	 * 根据id获取详情的列表
	 * @param httpUrl
	 * @param ids
	 * @return
	 */
	public static List<WineModel> requestPicDetailsList(String httpUrl,String ids) {
		List<WineModel> listItems = new ArrayList<WineModel>();
		BufferedReader reader = null;
		String result = null;
		StringBuffer sbf = new StringBuffer();
		httpUrl = httpUrl + "?id=" + ids;
		try {
			URL url = new URL(httpUrl);
			HttpURLConnection connection = (HttpURLConnection) url .openConnection();
			connection.setRequestMethod("GET");
			connection.connect();
			InputStream is = connection.getInputStream();
			reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
			String strRead = null;
			while ((strRead = reader.readLine()) != null) {
				sbf.append(strRead);
				sbf.append("\r\n");
			}
			reader.close();
			result = sbf.toString();
			if (!TextUtils.isEmpty(result)) {
				JSONObject jObj = new JSONObject(result);
				String tngou = jObj.getString("list");
				JSONArray jsonArray = new JSONArray(tngou);
				String id;
				String img;
				for (int i = 0; i < jsonArray.length(); i++) {
					JSONObject jsonObject = jsonArray.getJSONObject(i);
					id = jsonObject.getString("id");
					img = jsonObject.getString("src");

					WineModel model = new WineModel();
					model.setImg(ConstantUtil.HEAD_IMG + img);
					listItems.add(model);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return listItems;
	}
	
	/**
	 * 获取图片分类的接口
	 * @param httpUrl
	 * @return
	 */
	public static List<WineModel> requestPicClassfiy(String httpUrl) {
		List<WineModel> listItems = new ArrayList<WineModel>();
		BufferedReader reader = null;
		String result = null;
		StringBuffer sbf = new StringBuffer();
		try {
			URL url = new URL(httpUrl);
			HttpURLConnection connection = (HttpURLConnection) url .openConnection();
			connection.setRequestMethod("GET");
			connection.connect();
			InputStream is = connection.getInputStream();
			reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
			String strRead = null;
			while ((strRead = reader.readLine()) != null) {
				sbf.append(strRead);
				sbf.append("\r\n");
			}
			reader.close();
			result = sbf.toString();
			if (!TextUtils.isEmpty(result)) {
				JSONArray jsonArray = new JSONArray(result);
				String id;
				String title;
				for (int i = 0; i < jsonArray.length(); i++) {
					JSONObject jsonObject = jsonArray.getJSONObject(i);
					id = jsonObject.getString("id");
					title = jsonObject.getString("title");

					WineModel model = new WineModel();
					listItems.add(model);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return listItems;
	}
}
