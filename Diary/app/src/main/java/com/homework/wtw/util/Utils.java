/**
 * 
 */
package com.homework.wtw.util;

import android.text.TextUtils;

import com.amap.api.location.AMapLocation;

import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * 辅助工具类
 * @创建时间： 2015年11月24日 上午11:46:50
 * @项目名称： AMapLocationDemo2.x
 * @author hongming.wang
 * @文件名称: Utils.java
 * @类型名称: Utils
 */
public class Utils {
	/**
	 *  开始定位
	 */
	public final static int MSG_LOCATION_START = 0;
	/**
	 * 定位完成
	 */
	public final static int MSG_LOCATION_FINISH = 1;
	/**
	 * 停止定位
	 */
	public final static int MSG_LOCATION_STOP= 2;
	
	public final static String KEY_URL = "URL";
	public final static String URL_H5LOCATION = "file:///android_asset/location.html";
	public synchronized static String getCity(AMapLocation location){
		if(null == location) {
			return null;
		}
		if(location.getErrorCode() == 0){
			if (location.getProvince().equals(location.getCity()))
				return location.getCity();
			else
				return location.getProvince()+location.getCity();
		} else {
			//定位失败
			return null;
		}
	}
	
	private static SimpleDateFormat sdf = null;
	public synchronized static String formatUTC(long l, String strPattern) {
		if (TextUtils.isEmpty(strPattern)) {
			strPattern = "yyyy-MM-dd HH:mm:ss";
		}
		if (sdf == null) {
			try {
				sdf = new SimpleDateFormat(strPattern, Locale.CHINA);
			} catch (Throwable e) {
			}
		} else {
			sdf.applyPattern(strPattern);
		}
		return sdf == null ? "NULL" : sdf.format(l);
	}
}
