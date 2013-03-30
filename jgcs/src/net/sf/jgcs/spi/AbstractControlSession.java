
/*
 * JGCS - Group Communication Service.
 * Copyright (C) 2006 Nuno Carvalho, Universidade de Lisboa
 *
 * jgcs@lasige.di.fc.ul.pt
 *
 * Departamento de Informatica, Universidade de Lisboa
 * Bloco C6, Faculdade de Ciências, Campo Grande, 1749-016 Lisboa, Portugal.
 *
 * See COPYING for licensing details.
 */
package net.sf.jgcs.spi;

import net.sf.jgcs.ControlSession;
import net.sf.jgcs.ExceptionListener;
import net.sf.jgcs.GroupConfiguration;
import net.sf.jgcs.JGCSException;

/**
 * 
 * This class defines a AbstractControlSession. It partially implements the ControlSession interface
 * by handling the listeners. It provides methods for notification of listeners
 * that can be used by final implementations.
 * 
 * @author <a href="mailto:nunomrc@di.fc.ul.pt">Nuno Carvalho</a>
 * @version 1.0
 */
public abstract class AbstractControlSession<
	P extends AbstractProtocol<P,DS,CS,G>,
	DS extends AbstractDataSession<P,DS,CS,G>,
	CS extends AbstractControlSession<P,DS,CS,G>,
	G extends GroupConfiguration>
			implements ControlSession {
	protected P protocol;
	protected DS dataSession;
	protected G group;

	private ExceptionListener exceptionListener;
	
	/**
	 * Initializes any variables needed by this session.
	 *
	 */
	protected void boot() {
	}

	/**
	 * Sets the exception listener.
	 */
	public synchronized void setExceptionListener(ExceptionListener listener) {
		boot();
		exceptionListener = listener;		
	}

	/**
	 * Verifies listener registratioVerifies listener registration.
	 * @return true if all listeners are registered
	 */
	protected synchronized boolean hasAllListeners(){
		return exceptionListener != null;
	}

	/**
	 * Notifies the exception listener.
	 * @param exception the exception to notify.
	 */
	protected void notifyExceptionListeners(JGCSException exception) {
		if(exceptionListener != null)
			exceptionListener.onException(exception);
	}
}
