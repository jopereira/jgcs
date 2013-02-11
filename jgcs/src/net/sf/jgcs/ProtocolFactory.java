
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
 * This class defines a ProtocolFactory
 * 
 * This factory must be used to create instances of Protocols. It should be stateless and represents
 * one toolkit.
 * @assoc 1 Creates 1..* Protocol
 * 
 * @author <a href="mailto:nunomrc@di.fc.ul.pt">Nuno Carvalho</a>
 * @version 1.0
 */
public interface ProtocolFactory {

	/**
	 * Creates a new Protocol that represents a toolkit.
	 * @return a new protocol.
	 * @throws JGCSException
	 */
	public Protocol createProtocol() throws JGCSException;
	
}
