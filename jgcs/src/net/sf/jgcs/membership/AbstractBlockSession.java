
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
package net.sf.jgcs.membership;

import net.sf.jgcs.JGCSException;


public abstract class AbstractBlockSession extends AbstractMembershipSession implements BlockSession {
	private BlockListener blkListener;
	private boolean joined = false;
	
	protected void boot() {
//		if (blkListeners!=null)
//			return;
		super.boot();
	}
	
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
