/**
 * 下午2:13:54
 * @author zhangyh2
 * $
 * PlugsModel.java
 * TODO
 */
package com.darly.oop.ui.plug;

import org.osgi.framework.Bundle;

/**
 * @author zhangyh2 PlugsModel $ 下午2:13:54 TODO
 */
public class PlugsModel {

	public boolean install;

	public Bundle bundle;

	public String name;

	public PlugsModel(boolean install, Bundle bundle, String name) {
		this.install = install;
		this.bundle = bundle;
		this.name = name;
	}

}
