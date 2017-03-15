package com.homework.wtw.util;

import com.amap.api.location.AMapLocation;

/**
 * 辅助工具类
 * @文件名称: Utils.java
 * @类型名称: Utils
 */
public class Utils {
	public synchronized static String getCity(AMapLocation location, int type){
		if(null == location) {
			return null;
		}
		if(location.getErrorCode() == 0 && type == 0){
			if (location.getProvince().equals(location.getCity()))
				return location.getCity();
			else
				return location.getProvince()+location.getCity();
		} else if (location.getErrorCode() == 0 && type == 1) {
			return location.getCity();
		}else {
			//定位失败
			return null;
		}
	}
}
