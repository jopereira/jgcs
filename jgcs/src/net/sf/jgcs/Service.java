
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
 * This class defines a Service.
 * 
 * A Service is some functionality that the channel needs to provide to the application.
 * One example is the optimistic total order. If an application creates a channel that provides
 * optimistic total order, the application will receive the message payload with out guarantees and
 * will be notified later about optimistic delivery, regular delivery, uniform delivery, etc.
 * These notifications must implement this interface.
 * 
 * All related services must be comparable with each other (e.g. uniform delivery is a stronger service than
 * regular delivery, so if the message is uniform, it's also regular and optimistic 
 * -- optimistic lower than regular lower than uniform).
 * 
 * @author <a href="mailto:nunomrc@di.fc.ul.pt">Nuno Carvalho</a>
 * @version 1.0
 */
public interface Service {
	
	/**
	 * Compares two Services of the same protocol. return 0 if the services are the same,
	 * -1 if the service has lower properties than the given service, 1 if the service has greater properties
	 * than the given service.
	 * @param service the service to compare.
	 * @return 0 - same service, 1 greater service, -1 otherwise
	 * @throws UnsupportedServiceException if the service is not comparable.
	 */
	public int compare(Service service) throws UnsupportedServiceException;

}
