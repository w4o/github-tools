package utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.alibaba.fastjson.JSON;

public class ExUtils {

	public static Ex buildEx(String str) {

		Pattern p = Pattern.compile("^\\{.*\\}$");
		Matcher m = p.matcher(str);
		if (m.find()) {
			return JSON.parseObject(str, Ex.class);
		}
		return null;
	}

}
