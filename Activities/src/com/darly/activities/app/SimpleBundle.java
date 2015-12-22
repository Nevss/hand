/**
 * 上午9:55:35
 * @author zhangyh2
 * $
 * SimpleBundle.java
 * TODO
 */
package com.darly.activities.app;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

/**
 * @author zhangyh2 SimpleBundle $ 上午9:55:35 TODO
 */
public class SimpleBundle implements BundleActivator {

	private ServiceRegistration m_reg = null;
	private BundleContext mcontext = null;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext
	 * )
	 */
	@Override
	public void start(BundleContext context) throws Exception {
		// TODO Auto-generated method stub
		System.err.println("OOP_1 start："
				+ context.getBundle().getBundleId());
		this.mcontext = context;
		BundleContextFactory.getInstance().setBundleContext(context);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	@Override
	public void stop(BundleContext context) throws Exception {
		// TODO Auto-generated method stub
		System.err.println("OOP_1 stop："
				+ context.getBundle().getBundleId());
	}

	public ServiceRegistration getM_reg() {
		return m_reg;
	}

	public void setM_reg(ServiceRegistration m_reg) {
		this.m_reg = m_reg;
	}

	public BundleContext getMcontext() {
		return mcontext;
	}

	public void setMcontext(BundleContext mcontext) {
		this.mcontext = mcontext;
	}

}
