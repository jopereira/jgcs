/*
 * Spread implementation of JGCS - Group Communication Service.
 * Copyright (C) 2006,2013 Jose' Orlando Pereira
 *
 * http://github.com/jopereira/jgcs
 * 
 * See COPYING for licensing details.
 */
	  
package net.sf.jgcs.spread;

import net.sf.jgcs.GroupException;
import net.sf.jgcs.Protocol;
import net.sf.jgcs.ProtocolFactory;
import net.sf.jgcs.spread.jni.Mailbox;

public class SpProtocolFactory implements ProtocolFactory {
	
	private static final long serialVersionUID = 2L;
	
	private String daemonAddress = "4803@localhost";
	private String processName;
	private String mailboxClass = "net.sf.jgcs.spread.jni.SpMailbox";

	public Protocol createProtocol() throws GroupException {
		Mailbox mb;
		try {
			mb = (Mailbox) Class.forName(mailboxClass).newInstance();
		} catch (Exception e) {
			throw new GroupException("cannot create mailbox", e);
		}
		return new SpProtocol(mb, daemonAddress, processName);
	}

	public String getDaemonAddress() {
		return daemonAddress;
	}

	/**
	 * Address of local daemon. The default is 4803@localhost
	 * and usually is the right thing to use.
	 * @param daemonAddress address of daemon
	 */
	public void setDaemonAddress(String daemonAddress) {
		this.daemonAddress = daemonAddress;
	}

	public String getProcessName() {
		return processName;
	}

	/**
	 * Set local process name. This must be unique in
	 * each Spread daemon. When using plain Spread, a
	 * null value will automatically generate an unique
	 * name.
	 * @param processName process name
	 */
	public void setProcessName(String processName) {
		this.processName = processName;
	}

	public String getMailboxClass() {
		return mailboxClass;
	}

	/**
	 * The name of the native binding to use. These is either
	 * net.sf.jgcs.spread.jni.SpMailbox, to use plain Spread, or 
	 * net.sf.jgcs.spread.jni.FlMailbox, to use FlushSpread.
	 * The default is plain Spread.
	 * @param mailboxClass binding to use
	 */
	public void setMailboxClass(String mailboxClass) {
		this.mailboxClass = mailboxClass;
	}
}
