
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

package net.sf.jgcs.adapters;

import java.util.ArrayList;
import java.util.List;

import net.sf.jgcs.ClosedSessionException;
import net.sf.jgcs.DataSession;
import net.sf.jgcs.Message;
import net.sf.jgcs.MessageListener;

/**
 * This adapter is used to register several listeners on the same DataSession.
 * The adapter registers it self in the DataSession and keeps a list of listeners.
 * When a message is received, the adapter delivers the message to all registered listeners.
 * @author Nuno Carvalho
 *
 */
public class MultipleMessageListeners {
	
	private class MyListener implements MessageListener {
		public Object onMessage(Message msg) {
			Object cookie = null, currentCookie = null;
			for(MessageListener listener : listeners){				
				if(conservative){
					Message m;
					try {
						m = copyMessage(msg);
						currentCookie = listener.onMessage(m);
					} catch (ClosedSessionException e) {
						e.printStackTrace();
					}
				}
				else
					currentCookie = listener.onMessage(msg);
				if(currentCookie != null)
					cookie = currentCookie;
			}
			return cookie;
		}
		
		private Message copyMessage(Message msg) throws ClosedSessionException{
			Message copy = session.createMessage();
			byte[] payload = new byte[msg.getPayload().length];
			System.arraycopy(msg.getPayload(),0,payload,0,payload.length);
			copy.setPayload(payload);
			copy.setSenderAddress(msg.getSenderAddress());
			return copy;
		}
	}
	
	private List<MessageListener>listeners;
	private MyListener myListener;
	private boolean conservative;
	private DataSession session;

	/**
	 * Creates a new Adapter instance. It needs the instance of the DataSession and a boolean
	 * that indicates if messages should be delivered on conservative mode (copies of messages to
	 * each listener) or not conservative (the same message instance to all listeners).
	 * @param session the DataSession where the Messages will be delivered.
	 * @param conservative true if messages shoud be delivared on conservative mode.
	 * @throws ClosedSessionException if the given DataSession is closed.
	 */
	public MultipleMessageListeners(DataSession session, boolean conservative) throws ClosedSessionException {
		super();
		myListener = new MyListener();
		session.setMessageListener(myListener);
		this.session = session;
		listeners = new ArrayList<MessageListener>();
		this.conservative = conservative;
	}
	
	/**
	 * Adds a message listener.
	 * @param listener the listener to add.
	 */
	public void addMessageListener(MessageListener listener){
		listeners.add(listener);
	}
	
	/**
	 * Removes a message listener.
	 * @param listener the listener to remove
	 * @return true if the listener was removed, false otherwise.
	 */
	public boolean removeMessageListener(MessageListener listener){
		return listeners.remove(listener);
	}

}
