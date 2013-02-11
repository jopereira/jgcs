
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

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

/**
 * 
 * This class defines a AbstractPollingProtocol.
 * It enriches the AbstractProtocol class with a mechanism
 * to poll I/O for several sessions.
 * 
 * @author Jose Pereira
 * @version 1.0
 */
public abstract class AbstractPollingProtocol extends AbstractProtocol {
	private ExecutorService pool;
	private Runnable task;
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
		read();
		pool.execute(task);
	}
	
	protected abstract void read();
}
