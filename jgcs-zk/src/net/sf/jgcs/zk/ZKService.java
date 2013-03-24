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

import net.sf.jgcs.Service;
import net.sf.jgcs.UnsupportedServiceException;

/**
 * Any service as long as it is... the default service. 
 * @author jop
 */
public class ZKService implements Service {
	@Override
	public int compare(Service service) throws UnsupportedServiceException {
		return 0;
	}

}