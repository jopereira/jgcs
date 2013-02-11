
/*
 *
 * JGroups implementation of JGCS - Group Communication Service
 * Copyright (C) 2006 Nuno Carvalho, Universidade de Lisboa
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA
 * 
 * Contact
 * 	Address:
 * 		LASIGE, Departamento de Informatica, Bloco C6
 * 		Faculdade de Ciencias, Universidade de Lisboa
 * 		Campo Grande, 1749-016 Lisboa
 * 		Portugal
 * 	Email:
 * 		jgcs@lasige.di.fc.ul.pt
 * 
 */
 
package net.sf.jgcs.jgroups;

import net.sf.jgcs.Service;
import net.sf.jgcs.UnsupportedServiceException;

public class JGroupsService implements Service {

	public enum Services{
		FIFO("vsc+fifo"),
		TOTAL("vsc+total"),
		TOTAL_SERVICES("vsc+total+services"),
		CAUSAL("vsc+causal"),
		SETO_TOTAL_ORDER("seto_total_order"),
		REGULAR_TOTAL_ORDER("regular_total_order"),
		UNIFORM_TOTAL_ORDER("uniform_total_order");
		private String service_name;
		Services(String service){
			this.service_name = service;
		}
	}
	
	private Services myService;
	
	/**
	 * Service is the channel name that was given in the XML configuration file.
	 * @param service
	 */
	public JGroupsService(Services service) {
		myService = service;
	}

	public JGroupsService(String service_name){
		if(Services.CAUSAL.service_name.equals(service_name))
			myService = Services.CAUSAL;
		else if(Services.TOTAL.service_name.equals(service_name))
			myService = Services.TOTAL;
		else if(Services.TOTAL_SERVICES.service_name.equals(service_name))
			myService = Services.TOTAL_SERVICES;
		else if(Services.FIFO.service_name.equals(service_name))
			myService = Services.FIFO;
		else if(service_name.equals(Services.SETO_TOTAL_ORDER.service_name))
			myService = Services.SETO_TOTAL_ORDER;
		else if(service_name.equals(Services.REGULAR_TOTAL_ORDER.service_name))
			myService = Services.REGULAR_TOTAL_ORDER;
		else if(service_name.equals(Services.UNIFORM_TOTAL_ORDER.service_name))
			myService = Services.UNIFORM_TOTAL_ORDER;
	}
	
	public String getService() {
		return myService.service_name;
	}

	public void setService(Services service) {
		myService = service;
	}

	public int compare(Service service) throws UnsupportedServiceException {
		if(! (service instanceof JGroupsService))
			throw new UnsupportedServiceException("Service not valid: "+service.getClass().getName());
		JGroupsService as = (JGroupsService) service;
		return as.myService.compareTo(myService);
	}
	
	@Override
	public int hashCode(){
		return myService.hashCode();
	}

	@Override
	public boolean equals(Object o){
		if (o instanceof JGroupsService) {
			JGroupsService as = (JGroupsService) o;
			return (as.myService.compareTo(myService) == 0);
		}
		else
			return false;
	}
	
	@Override
	public String toString(){
		return "JGroups service: "+myService.service_name;
	}

}
