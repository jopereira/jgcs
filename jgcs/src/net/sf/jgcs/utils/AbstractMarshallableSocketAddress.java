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

package net.sf.jgcs.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;

import net.sf.jgcs.JGCSException;

public abstract class AbstractMarshallableSocketAddress implements Marshallable {
		
	public SocketAddress getSocketAddress(byte[] buffer)
	throws JGCSException {
		ByteArrayInputStream byteStream = new ByteArrayInputStream(buffer);
		ObjectInputStream in = null;
		InetAddress inetAddr = null;
		int port = -1;
		try {
			in = new ObjectInputStream(byteStream);
			byte[] addrBytes = new byte[in.readInt()];
			in.read(addrBytes);
			inetAddr = InetAddress.getByAddress(addrBytes);
			port = in.readInt();
			in.close();
		} catch (IOException e) {
			throw new JGCSException("Could not read from input stream", e);
		}
		return new InetSocketAddress(inetAddr,port);
	}
	
	public byte[] getBytes(SocketAddress addr)
	throws JGCSException {
		if( ! (addr instanceof InetSocketAddress))
			throw new JGCSException("Addrress of type "+addr.getClass().getName()+" is not supported.");
		InetSocketAddress inetAddr = (InetSocketAddress) addr;
		
		ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
		ObjectOutputStream out = null;
		try {
			out = new ObjectOutputStream(byteStream);
			byte[] addrBytes = inetAddr.getAddress().getAddress();
			out.writeInt(addrBytes.length);
			out.write(addrBytes);
			out.writeInt(inetAddr.getPort());
			out.close();
		} catch (IOException e) {
			throw new JGCSException("Could not write to output stream", e);
		}
		return byteStream.toByteArray();
	}
	
	
}
