/*
 * JGroups implementation of JGCS - Group Communication Service
 * Copyright (C) 2006 Nuno Carvalho, Universidade de Lisboa
 * Copyright (C) 2013 José Orlando Pereira
 * 
 * http://github.com/jopereira/jgcs
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
 */
 
package net.sf.jgcs.jgroups;

import net.sf.jgcs.GroupException;
import net.sf.jgcs.Protocol;
import net.sf.jgcs.ProtocolFactory;

/**
 * Protocol configuration in JGroups. A protocol is configured with an
 * XML file describing the stack, loaded from the class path. See JGroups
 * configuration for available alternatives and how to configure your
 * own stacks.
 */
public class JGroupsProtocolFactory implements ProtocolFactory {

	private static final long serialVersionUID = 2L;
	
	private String configuration;

	/** 
	 * Use the default JGroups protocol configuration.
	 */
	public JGroupsProtocolFactory() {
	}

	/** 
	 * Use an alternate JGroups protocol configuration.
	 */
	public JGroupsProtocolFactory(String config) {
		this.configuration = config;
	}

	/**
	 * Get the name of the protocol configuration file.
	 * @return the configuration file, or null for the default
	 */
	public String getConfiguration() {
		return configuration;
	}

	/**
	 * Set the name of the protocol configuration file.
	 * @param configuration the configuration file, or null for the default
	 */
	public void setConfiguration(String configuration) {
		this.configuration = configuration;
	}	
	
	@Override
	public Protocol createProtocol() throws GroupException{
		return new JGroupsProtocol(configuration);
	}
}
