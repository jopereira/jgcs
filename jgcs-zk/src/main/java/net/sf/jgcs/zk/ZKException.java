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

package net.sf.jgcs.zk;

import net.sf.jgcs.GroupException;

/**
 * Group communication exception. Any exception within the group
 * communication protocol or the application callbacks removes
 * the process from the view.
 * 
 * @author jop
 */
public class ZKException extends GroupException {
	private static final long serialVersionUID = -3526423319319274589L;

	public ZKException(String message, Throwable cause) {
		super(message, cause);
	}

	public ZKException(String message) {
		super(message);
	}
}
