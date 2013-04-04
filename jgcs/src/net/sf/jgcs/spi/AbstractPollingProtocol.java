
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

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

import net.sf.jgcs.GroupConfiguration;
import net.sf.jgcs.GroupException;

/**
 * 
 * This class defines a AbstractPollingProtocol.
 * It enriches the AbstractProtocol class with a mechanism
 * to poll I/O for several sessions.
 * 
 * @author Jose Pereira
 * @version 1.0
 */
public abstract class AbstractPollingProtocol<
		P extends AbstractPollingProtocol<P,DS,CS,G>,
		DS extends AbstractDataSession<P,DS,CS,G>,
		CS extends AbstractControlSession<P,DS,CS,G>,
		G extends GroupConfiguration>
		extends AbstractProtocol<P,DS,CS,G> {
	private ExecutorService pool;
	private Runnable task;

	protected AbstractPollingProtocol() {
		pool = Executors.newFixedThreadPool(1, new ThreadFactory() {
			public Thread newThread(Runnable r) {
				Thread t=new Thread(r);
				t.setDaemon(true);
				return t;
			}
		});
		task = new Runnable() {
			public void run() {
				poll();
			}
		};
	}
	
	/**
	 * Start polling for input. This should be executed after the object is
	 * fully initialized and before any invocations. Most likely, as the last
	 * operation of the constructor in a derived concrete class.
	 */
	protected void boot() {
		pool.execute(task);		
	}
	
	private void poll() {
		if (isClosed())
			return;
		try {
			read();
		} catch (GroupException e) {
			if (isClosed())
				return;
			GroupException je=new GroupException("I/O exception", e);
			notifyExceptionListeners(je);			
			return;
		}
		pool.execute(task);
	}
	
	protected abstract void read() throws GroupException;

	@Override
	protected synchronized void cleanup() {
		pool.shutdown();
		super.cleanup();
	}
}
