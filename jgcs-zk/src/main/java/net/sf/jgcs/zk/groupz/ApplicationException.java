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

import net.sf.jgcs.zk.ZKException;

/**
 * Wrapped application exception. An application exception during a group
 * call back (e.g. install or receive) can be wrapped with this class and
 * re-thrown. This will make the process leave the group, as it usually
 * signals a bug or otherwise corrupted application state. 
 * 
 * @author jop
 */
public class ApplicationException extends ZKException {
	private static final long serialVersionUID = 5735154401650469938L;

	/**
	 * Wrap an exception.
	 * 
	 * @param cause the wrapped exception
	 */
	public ApplicationException(Throwable cause) {
		super("unrecoverable application exception", cause);
	}
}
