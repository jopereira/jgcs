/**
 * APPIA implementation of JGCS - Group Communication Service
 * Copyright 2006 University of Lisbon
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License. 
 *
 * Initial developer(s): Nuno Carvalho.
 * 
 *  * Contact
 * 	Address:
 * 		LASIGE, Departamento de Informatica, Bloco C6
 * 		Faculdade de Ciencias, Universidade de Lisboa
 * 		Campo Grande, 1749-016 Lisboa
 * 		Portugal
 * 	Email:
 * 		jgcs@lasige.di.fc.ul.pt
 */
 
package net.sf.jgcs.utils;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class Mailbox<T> {

	private ConcurrentLinkedQueue<T> mailbox =new ConcurrentLinkedQueue<T>();
	private final ReentrantLock lock = new ReentrantLock();
	private final Condition isEmpty = lock.newCondition();

	public Mailbox() {}

	public void add(T item){
		lock.lock();
		try{
			mailbox.add(item);
			isEmpty.signalAll();
		}
		finally{
			lock.unlock();
		}
	}
	
	public T removeNext(){
		lock.lock();
		try{
			while(mailbox.isEmpty())
				try {
					isEmpty.await();
				} catch (InterruptedException e) {
					return null;
				}
			return mailbox.remove();
		}
		finally{
			lock.unlock();
		}
	}

	public T removeNext(long millis){
		lock.lock();
		try{
			while(mailbox.isEmpty())
				try {
					isEmpty.await(millis,TimeUnit.MILLISECONDS);
				} catch (InterruptedException e) {
					return null;
				}
				if (!mailbox.isEmpty()) 
					return mailbox.remove();
				else 
					return null;		
		}
		finally{
			lock.unlock();
		}
	}

}
