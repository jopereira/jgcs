
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
package net.sf.jgcs;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;


/**
 * Data session that directly polls I/O. This is often used for simple
 * protocols that do not have associated control sessions. It probably should
 * not be used with AbstractPollingProtocol.
 * @author Jose Pereira
 */
public abstract class AbstractPollingDataSession extends AbstractDataSession {
	private boolean closed;
	protected ExecutorService pool;
	protected Runnable task;

	protected AbstractPollingDataSession(AbstractProtocol protocol, GroupConfiguration group) {
		super(protocol, group);
	}
	
	protected void boot() {
		if (task!=null)
			return;
		super.boot();
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
		pool.execute(task);		
	}
	
	private void poll() {
		Message msg;
		try {
			msg=read();
		} catch (IOException e) {
			JGCSException je=new JGCSException("I/O exception", e);
			notifyExceptionListeners(je);			
			close();
			return;
		}
		notifyMessageListeners(msg);
		pool.execute(task);
	}

	public synchronized void close() {
		if (closed)
			return;
		super.close();
		closed=true;
		cleanup();
		pool.shutdown();
	}

	protected abstract Message read() throws IOException;
	protected abstract void cleanup();
}
