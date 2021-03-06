package com.darly.activities.ui.qinjia.util;

import java.util.Comparator;

import com.darly.activities.ui.qinjia.bean.GotyeUserProxy;


/**
 * 
 * @author xiaanming
 * 
 */
public class PinyinComparator implements Comparator<GotyeUserProxy> {

	@Override
	public int compare(GotyeUserProxy o1, GotyeUserProxy o2) {
		if (o1.firstChar.equals("@") || o2.firstChar.equals("#")) {
			return -1;
		} else if (o1.firstChar.equals("#") || o2.firstChar.equals("@")) {
			return 1;
		} else {
			return o1.firstChar.compareTo(o2.firstChar);
		}
	}

}
