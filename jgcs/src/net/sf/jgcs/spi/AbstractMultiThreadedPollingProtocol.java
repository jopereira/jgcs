
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

/**
 * This class defines a AbstractMultiThreadedPollingProtocol.
 * It enriches the AbstractProtocol class with a multi threaded mechanism
 * to poll I/O for several sessions.
 * 
 * @author Jose Pereira
 * @version 1.0
 */
public abstract class AbstractMultiThreadedPollingProtocol<
		P extends AbstractMultiThreadedPollingProtocol<P,DS,CS,G>,
		DS extends AbstractDataSession<P,DS,CS,G>,
		CS extends AbstractControlSession<P,DS,CS,G>,
		G extends GroupConfiguration>
		extends AbstractProtocol<P,DS,CS,G> {

	private ExecutorService pool;
	
	protected void boot() {
		if (pool!=null)
			return;
		super.boot();
		pool = Executors.newCachedThreadPool(new ThreadFactory() {
			public Thread newThread(Runnable r) {
				Thread t=new Thread(r);
				t.setDaemon(true);
				return t;
			}
		});
	}
	
	protected void startReader(ProtocolReader<?> worker){
		pool.execute(worker);
	}
	
	public abstract class ProtocolReader<C> implements Runnable {
		private GroupConfiguration group;
		private C channel;
		public void run(){
			read();
			pool.execute(this);
		}
		public void setChannel(C c){
			channel = c;
		}
		public C getChannel(){
			return channel;
		}
		public GroupConfiguration getGroup() {
			return group;
		}
		public void setGroup(GroupConfiguration group) {
			this.group = group;
		}
		public void setFields(GroupConfiguration g, C c){
			group = g;
			channel = c;
		}
		public abstract void read();
	}

}
