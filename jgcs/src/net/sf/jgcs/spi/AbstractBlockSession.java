/*
 * JGCS - Group Communication Service.
 * Copyright (C) 2006 Nuno Carvalho, Universidade de Lisboa
 *
 * jgcs@lasige.di.fc.ul.pt
 *
 * Departamento de Informatica, Universidade de Lisboa
 * Bloco C6, Faculdade de Ciencias, Campo Grande, 1749-016 Lisboa, Portugal.
 *
 * See COPYING for licensing details.
 */

package net.sf.jgcs.spi;

import net.sf.jgcs.BlockListener;
import net.sf.jgcs.BlockSession;
import net.sf.jgcs.GroupConfiguration;
import net.sf.jgcs.GroupException;

public abstract class AbstractBlockSession<
		P extends AbstractProtocol<P,DS,CS,G>,
		DS extends AbstractDataSession<P,DS,CS,G>,
		CS extends AbstractBlockSession<P,DS,CS,G>,
		G extends GroupConfiguration>
		extends AbstractMembershipSession<P,DS,CS,G> implements BlockSession {

	private BlockListener blkListener;
	
	public void setBlockListener(BlockListener listener) throws GroupException {
		try {
			lock.lock();
			blkListener = listener;
		} finally {
			lock.unlock();
		}
	}

	protected boolean hasAllListeners(){
		return super.hasAllListeners() && blkListener != null;
	}

	protected void notifyBlock() {
		/* Avoid NPE but invoke callback outside the lock.
		 * This means that there can be callbacks after close(),
		 * but avoids deadlocks.
		 */
		BlockListener listener = null;
		try {
			lock.lock();
			listener = blkListener;		
		} finally {
			lock.unlock();
		}
		if(listener != null)
			listener.onBlock();
		else
			notifyExceptionListeners(new GroupException("BlockListener not registered. A BlockListener should be registered, "+
				"otherwise the view could not change."));
	}
	
	
	protected void cleanup() {
		super.cleanup();
		blkListener = null;
	}

}
