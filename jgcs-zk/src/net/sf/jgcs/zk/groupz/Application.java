/*
   Copyright 2010-2013 José Orlando Pereira <jop@di.uminho.pt>

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
*/

package net.sf.jgcs.zk.groupz;

/**
 * Application callbacks for group communication. The application must
 * implement this interface to handle group events. Any exceptions thrown
 * within application code will remove the process from the group.
 * 
 * @author jop
 */
public interface Application {
	/**
	 * Handle a message.
	 * @param data raw message data, as sent by a process
	 * @throws GroupException an exception that might occur while trying to
	 * perform other group operations
	 */
	public void receive(byte[] data) throws GroupException;
	
	/**
	 * Handle a new view.
	 * @param vid a monotonically increasing view indentifier
	 * @param members the current members of the group, or null if the process
	 * has been excluded
	 * @throws GroupException an exception that might occur while trying to
	 * perform other group operations
	 */
	public void install(int vid, String[] members) throws GroupException;
	
	/**
	 * Prepare for a new view. A process should stop sending messages and
	 * then call blockOk() to allow the installation of a new view to proceed.
	 * @throws GroupException an exception that might occur while trying to
	 * perform other group operations
	 */
	public void block() throws GroupException;
}
