
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
import net.sf.jgcs.JGCSException;

public abstract class AbstractBlockSession<
		P extends AbstractProtocol<P,DS,CS,G>,
		DS extends AbstractDataSession<P,DS,CS,G>,
		CS extends AbstractBlockSession<P,DS,CS,G>,
		G extends GroupConfiguration>
		extends AbstractMembershipSession<P,DS,CS,G> implements BlockSession {

	private BlockListener blkListener;
	private boolean joined = false;
	
	public void setBlockListener(BlockListener listener) throws JGCSException {
		boot();
		if(joined && listener == null)
			throw new JGCSException("Cannot unset block listener while in the group.");
		blkListener = listener;
	}

	protected synchronized boolean hasAllListeners(){
		return super.hasAllListeners() && blkListener != null;
	}

	protected synchronized void notifyBlock() {
		if(blkListener != null)
			blkListener.onBlock();
		else {
			notifyExceptionListeners(new JGCSException("BlockListener not registered. A BlockListener should be registered, "+
				"otherwise the view could not change."));
		}
	}

	protected synchronized void setJoined(boolean joined) {
		this.joined = joined;
	}
}
