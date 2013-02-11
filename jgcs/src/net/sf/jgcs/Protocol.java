
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

/**
 * 
 * This interface defines a Protocol represents an instance of the toolkit 
 * used to implement the Group Communication Service (GCS). 
 * This interface must be used to create instances of DataSession and Control Session.
 * 
 * @see DataSession
 * @see ControlSession
 * @see GroupConfiguration
 * 
 * @assoc 1 Uses 1..* GroupConfiguration
 * @composed 1 Defines 1 DataSession
 * @composed 1 Defines 1 ControlSession
 * 
 * @author <a href="mailto:nunomrc@di.fc.ul.pt">Nuno Carvalho</a>
 * @version 1.0
 */
public interface Protocol {

	/**
	 * Creates e new Data Session. This session must be used to send messages and to register a listener
	 * to receive messages from the other members of the group.
	 * @param group the configuration.
	 * @return a new data session.
	 * @throws JGCSException
	 */
	public DataSession openDataSession(GroupConfiguration group) throws JGCSException;
	
	/**
	 * Creates a new Control Session. This session must be used to join a group and register a listener
	 * to receive asynchronous notifications about the other members of the group (join, leave, fail).
	 * @param group the group configuration.
	 * @return a new control session.
	 * @throws JGCSException
	 */
	public ControlSession openControlSession(GroupConfiguration group) throws JGCSException;
	
}
