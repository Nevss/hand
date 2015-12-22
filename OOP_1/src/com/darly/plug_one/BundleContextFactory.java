/**
 * 上午10:18:51
 * @author zhangyh2
 * $
 * a.java
 * TODO
 */
package com.darly.plug_one;

import org.osgi.framework.BundleContext;

/**
 * @author zhangyh2 a $ 上午10:18:51 TODO
 */
public class BundleContextFactory {
	private static BundleContextFactory _instance = null;
	private BundleContext mcontext = null;

	public static BundleContextFactory getInstance() {
		try {
			if (_instance == null) {
				_instance = new BundleContextFactory();
			}
			BundleContextFactory localBundleContextFactory = _instance;
			return localBundleContextFactory;
		} finally {
		}
	}

	public BundleContext getBundleContext() {
		return this.mcontext;
	}

	public void setBundleContext(BundleContext paramBundleContext) {
		this.mcontext = paramBundleContext;
	}
}