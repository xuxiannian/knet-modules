package cn.knet.domain.utils;

import org.apache.commons.lang.StringUtils;

import java.util.HashMap;
import java.util.Map;

public class RegionUtils {
	
	public static String getRegionbyPinyin(String pinyin) {
		Map<String,String> map = new HashMap<String,String>();
		map.put("Beijing","北京");
		map.put("Shanghai","上海");
		map.put("Tianjin","天津");
		map.put("Chongqing","重庆");
		map.put("Xianggang","香港");
		map.put("Aomen","澳门");
		map.put("Anhui","安徽");
		map.put("Fujian","福建");
		map.put("Guangdong","广东");
		map.put("Guangxi","广西");
		map.put("Guizhou","贵州");
		map.put("Gansu","甘肃");
		map.put("Hainan","海南");
		map.put("Hebei","河北");
		map.put("Henan","河南");
		map.put("Hubei","湖北");
		map.put("Hunan","湖南");
		map.put("Jilin","吉林");
		map.put("Jiangsu","江苏");
		map.put("Jiangxi","江西");
		map.put("Liaoning","辽宁");
		map.put("Ningxia","宁夏");
		map.put("Qinghai","青海");
		map.put("Shanxi","陕西");
		map.put("Shanxi","山西");
		map.put("Shandong","山东");
		map.put("Sichuan","四川");
		map.put("Taiwan","台湾");
		map.put("Xizang","西藏");
		map.put("Xinjiang","新疆");
		map.put("Yunnan","云南");
		map.put("Zhejiang","浙江");
		map.put("Heilongjiang","黑龙江");
		map.put("Neimenggu","内蒙古");
		String result = map.get(pinyin);
		if(StringUtils.isBlank(result)) {
			result ="其他";
		}
		return result;
		
	}

}
