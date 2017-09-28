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
package com.epocharch.fawkes.client.router;

import com.epocharch.fawkes.client.router.Routee;

import java.util.Comparator;

/**
 * @author Archer
 * 
 */
public class RouteeWeightComparator implements Comparator<Routee> {

	@Override
	public int compare(Routee o1, Routee o2) {
		int v = 0;
		if (o1 != null && o2 != null) {
			if(o1.getWeight()-o2.getWeight()>=0){
				v=-1;
			}else{
				v=1;
			}
		}
		return v;
	}
}
