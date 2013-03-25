
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
			// FIXME: A single callback cookie for all listeners? Not good.
			for(MessageListener listener : listeners){				
				currentCookie = listener.onMessage(msg);
				if(currentCookie != null)
					cookie = currentCookie;
			}
			return cookie;
		}		
	}
	
	private List<MessageListener>listeners;
	private MyListener myListener;

	/**
	 * Creates a new Adapter instance. Note that the same message instance to all listeners.
	 * @param session the DataSession where the Messages will be delivered.
	 * @throws ClosedSessionException if the given DataSession is closed.
	 */
	public MultipleMessageListeners(DataSession session) throws ClosedSessionException {
		super();
		myListener = new MyListener();
		session.setMessageListener(myListener);
		listeners = new ArrayList<MessageListener>();
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
