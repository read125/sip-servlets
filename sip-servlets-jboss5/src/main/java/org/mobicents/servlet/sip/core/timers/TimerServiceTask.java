/*
 * JBoss, Home of Professional Open Source
 * Copyright 2008, Red Hat Middleware LLC, and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.mobicents.servlet.sip.core.timers;

import java.io.Serializable;
import java.util.concurrent.ScheduledFuture;

import javax.servlet.sip.ServletTimer;
import javax.servlet.sip.SipApplicationSession;

import org.jboss.web.tomcat.service.session.ClusteredSipManager;
import org.jboss.web.tomcat.service.session.distributedcache.spi.OutgoingDistributableSessionData;
import org.mobicents.servlet.sip.core.session.MobicentsSipApplicationSession;
import org.mobicents.timers.TimerTask;

/**
 * @author jean.deruelle@gmail.com
 *
 */
public class TimerServiceTask extends TimerTask implements ServletTimer {
	
	ServletTimerImpl servletTimer;	
	TimerServiceTaskData data;
	
	/**
	 * @param data
	 */
	public TimerServiceTask(ClusteredSipManager<? extends OutgoingDistributableSessionData> sipManager, ServletTimerImpl servletTimerImpl, TimerServiceTaskData data) {
		super(data);
		this.data = data;
		if(servletTimerImpl == null) {
			MobicentsSipApplicationSession sipApplicationSession = sipManager.getSipApplicationSession(data.getKey(), false);
			this.servletTimer = new ServletTimerImpl(data.getData(), data.getDelay(), sipApplicationSession.getSipContext().getListeners().getTimerListener(), sipApplicationSession);						
		} else {
			this.servletTimer = servletTimerImpl;
			data.setData(servletTimerImpl.getInfo());
			data.setDelay(servletTimerImpl.getDelay());
			data.setKey(((MobicentsSipApplicationSession)servletTimerImpl.getApplicationSession()).getKey());
		}
	}

	/* (non-Javadoc)
	 * @see org.mobicents.timers.TimerTask#run()
	 */
	@Override
	public void run() {		
		servletTimer.run();
	}

	public void cancel() {
		servletTimer.cancel();
	}

	public SipApplicationSession getApplicationSession() {		
		return servletTimer.getApplicationSession();
	}

	@Override
	protected void setScheduledFuture(ScheduledFuture<?> scheduledFuture) {
		servletTimer.setFuture(scheduledFuture);
		super.setScheduledFuture(scheduledFuture);
	}
	
	public String getId() {
		return data.getTaskID().toString();
	}

	public Serializable getInfo() { 
		return data.getData();
	}

	public long getTimeRemaining() {		
		return servletTimer.getTimeRemaining();
	}

	public long scheduledExecutionTime() {
		return servletTimer.scheduledExecutionTime();
	}	
}
