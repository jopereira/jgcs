
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

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

import net.sf.jgcs.GroupConfiguration;
import net.sf.jgcs.JGCSException;

/**
 * Data session that directly polls I/O. This is often used for simple
 * protocols that do not have associated membership sessions. It probably should
 * not be used with AbstractPollingProtocol.
 * @author Jose Pereira
 */
public abstract class AbstractPollingDataSession<
		P extends AbstractProtocol<P,DS,CS,G>,
		DS extends AbstractPollingDataSession<P,DS,CS,G>,
		CS extends AbstractControlSession<P,DS,CS,G>,
		G extends GroupConfiguration>
		extends AbstractDataSession<P,DS,CS,G> {
	protected ExecutorService pool;
	protected Runnable task;

	protected AbstractPollingDataSession() {
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
		} catch (IOException e) {
			if (isClosed())
				return;
			JGCSException je=new JGCSException("I/O exception", e);
			notifyExceptionListeners(je);			
			return;
		}
		if (!isClosed())
			pool.execute(task);
	}

	protected void cleanup() {
		super.cleanup();
		pool.shutdown();
	}
	
	protected abstract void read() throws IOException;
}
