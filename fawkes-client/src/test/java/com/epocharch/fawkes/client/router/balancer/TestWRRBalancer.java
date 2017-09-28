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

import com.epocharch.fawkes.client.router.Routee;
import junit.framework.TestCase;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by archer on 22/09/2017.
 */
public class TestWRRBalancer{

	@Test
	public void testRr(){
		List<Routee> list = new ArrayList<Routee>();
		int size = 10;
		for(int i=0;i<size;i++){
			Routee r1 = new Routee("s1","10.161.18."+i);
			list.add(r1);
		}
		WRRBalancer wrr = new WRRBalancer(list);
		loop(wrr);
	}
	@Test
	public void testWrr(){
		Random r = new Random();
		List<Routee> list = new ArrayList<Routee>();
		int size = 10;
		for(int i=0;i<size;i++){
			Routee r1 = new Routee("s1","10.161.18."+i);
			int w = r.nextInt(6);
			r1.setWeight(w==0?1:w);
			list.add(r1);
		}
		WRRBalancer wrr = new WRRBalancer(list);
		loop(wrr);
	}

	private void loop(WRRBalancer balancer){
		int loop = 9999999;
		for(int i=0;i<loop;i++){
			System.out.println(balancer.select());
		}
	}
}
