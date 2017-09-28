/*
 *
 *  * Copyright 2017 EpochArch.com
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  * you may not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  *     http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *
 */

/**
 * 
 */
package com.epocharch.fawkes.client.router.balancer;

import com.epocharch.fawkes.client.router.PhantomRoutee;
import com.epocharch.fawkes.client.router.Routee;

import java.util.*;

/**
 * @author Archer
 * 
 */
public	class MixupHelper {

	private static Random r = new Random();

	public static List<PhantomRoutee> mix(List<Routee> routees,Comparator comparator) {
		List<PhantomRoutee> rlist = new ArrayList();
		if (routees != null && comparator != null) {
			Collections.sort(routees, comparator);
			for (Routee m : routees) {
				if (m != null) {
					int loop = m.getWeight();
					for (int i = 0; i < loop; i++) {
						add2List(m, rlist);
					}
				}
			}
		}
		return rlist;
	}

	private static void add2List(Routee m, List<PhantomRoutee> list) {
		if (list != null) {
			int i = getPosition(list, m.getHostUrl());
			list.add(i, new PhantomRoutee(m));
		}
	}

	private static int getPosition(List<PhantomRoutee> list, String hostStr) {
		int pos = 0;
		int size = list.size();
		if(size>1){
			for(int i=0;i<(size-1);i++){
				String hs1 = list.get(i).getRoutee().getHostUrl();
				String hs2 = list.get(i+1).getRoutee().getHostUrl();
				if(hs1.equals(hs2)){
					pos = i+1;
					break;
				}
			}
		}
		return pos;
	}

}
