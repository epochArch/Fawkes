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

package com.epocharch.fawkes.client.router.balancer;

import com.epocharch.fawkes.client.router.PhantomRoutee;
import com.epocharch.fawkes.client.router.Routee;
import com.epocharch.fawkes.client.router.RouteeWeightComparator;

import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by archer on 19/09/2017.
 */
public class WRRBalancer implements IBalancer<Routee, PhantomRoutee> {
	private Circle<Integer, PhantomRoutee> _circle;
	private Random random = new Random();
	private int barrier = Integer.MAX_VALUE/2;
	private AtomicInteger position = new AtomicInteger(random.nextInt(barrier));

	public WRRBalancer(List<Routee> routees) {
		update(routees);
	}

	public PhantomRoutee select() {
		PhantomRoutee routee = null;
		if (_circle == null || _circle.size() == 0) {
			routee = null;
		} else if (_circle.size() == 1) {
			routee = _circle.firstVlue();
		} else {
			int key = position.getAndIncrement();
			int totalSize = _circle.size(); ///registerJMX( this );
			int realPos = key % totalSize;
			if (key > barrier) {
				position.set(0);
			}
			routee = _circle.lowerValue(realPos);
		}
		return routee;
	}

	public void update(List<Routee> routees) {
		List<PhantomRoutee> routeeList = MixupHelper.mix(routees, new RouteeWeightComparator());
		createNewCircle(routeeList);
	}

	public void createNewCircle(List<PhantomRoutee> pRoutees){
		Circle<Integer, PhantomRoutee> circle = new Circle();
		int size=0;
		for (PhantomRoutee pr : pRoutees) {
			circle.put(size++, pr);
		}
		_circle = circle;
	}

	public void clean() {
		_circle = new Circle<Integer, PhantomRoutee>();
	}
}
