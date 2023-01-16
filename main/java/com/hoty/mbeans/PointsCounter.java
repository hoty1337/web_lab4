package com.hoty.mbeans;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.LocalBean;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.management.*;
import java.lang.management.ManagementFactory;

@Singleton
@LocalBean
@Startup
public class PointsCounter extends NotificationBroadcasterSupport implements PointsCounterMBean {
	private int allPointsCounter = 0;
	private int outOfAreaPointsCounter = 0;
	private int sequenceNum = 0;

	private MBeanServer platformMBeanServer;
	private ObjectName objName = null;

	@PostConstruct
	public void registerInJMX() {
		try {
			objName = new ObjectName("mbeans:type=" + this.getClass().getName());
			platformMBeanServer = ManagementFactory.getPlatformMBeanServer();
			platformMBeanServer.registerMBean(this, objName);
		} catch (Exception e) {
			throw new IllegalStateException("There is a problem with register Monitoring into JMX: " + e);
		}
	}

	@Override
	public int getAllPointsCounter() {
		return allPointsCounter;
	}

	@Override
	public int getOutOfAreaPointsCounter() {
		return outOfAreaPointsCounter;
	}

	public void changeCounters(double x, double y, double r, boolean result) {
		allPointsCounter++;
		if(!result) {
			outOfAreaPointsCounter++;
		}
		double rate = 100/r;
		double xRate = x*rate;
		double yRate = y*rate;
		if(Math.abs(xRate) > 150 || Math.abs(yRate) > 150) {
			sendNotification(new Notification("out_of_coordinate_plane", this, sequenceNum++, System.currentTimeMillis(), "You missed coordinate plane"));
		}
	}

	@PreDestroy
	public void unregisterFromJMX() {
		try {
			platformMBeanServer.unregisterMBean(objName);
		} catch (Exception e) {
			throw new IllegalStateException("There is a problem with unregister Monitoring into JMX: " + e);
		}
	}
}
