
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

import net.sf.jgcs.GroupConfiguration;
import net.sf.jgcs.Membership;
import net.sf.jgcs.MembershipID;
import net.sf.jgcs.MembershipListener;
import net.sf.jgcs.MembershipSession;
import net.sf.jgcs.NotJoinedException;


public abstract class AbstractMembershipSession<
		P extends AbstractProtocol<P,DS,CS,G>,
		DS extends AbstractDataSession<P,DS,CS,G>,
		CS extends AbstractMembershipSession<P,DS,CS,G>,
		G extends GroupConfiguration>
	extends AbstractControlSession<P,DS,CS,G> implements MembershipSession {

	private MembershipListener membListener;	
	private transient Membership membership;
	
	protected void boot() {
		super.boot();
	}
	
	public void setMembershipListener(MembershipListener listener) {
		boot();
		membListener = listener;
	}
	
	protected synchronized boolean hasAllListeners(){
		return membListener != null ;
	}

	protected synchronized void notifyAndSetMembership(Membership m) {
		boot();		
		membership = m;
		if(membListener != null)
			membListener.onMembershipChange();
	}

	protected synchronized void notifyRemoved() {
		boot();		
		membership = null;
		if(membListener != null)
			membListener.onExcluded();
	}

	public Membership getMembership() throws NotJoinedException{
		if(membership == null)
			throw new NotJoinedException("Membership does not exist.");
		return membership;
	}

	protected void setMembership(Membership m) {
		membership = m;
	}

	public MembershipID getMembershipID() throws NotJoinedException{
		if(membership == null)
			throw new NotJoinedException("Membership does not exist.");
		return membership.getMembershipID();
	}

}
