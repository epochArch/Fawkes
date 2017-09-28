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

import com.epocharch.fawkes.client.router.balancer.Circle;
import com.epocharch.fawkes.client.router.balancer.IRelivePolicy;
import com.epocharch.fawkes.common.constants.PropKeys;
import com.epocharch.fawkes.common.utils.FawkesUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Archer
 * 
 */
public class RouteeRelivePolicy implements IRelivePolicy {

	private static Logger logger = LoggerFactory.getLogger(RouteeRelivePolicy.class);
	private static final int DEFAULT_RELIVE_INTERVAL = 100;
	private static final int DEFAULT_RELIVE_COUNT = 10;
	private int tryCount = 0;
	private int threshold = DEFAULT_RELIVE_COUNT;
	private int SCALE = 2;
	private int COUNT_LIMIT = 60000;
	private long start = 0;
	private long interval = DEFAULT_RELIVE_INTERVAL;
	private long TIME_LIMIT = 60000;
	private int tps_threshold = 5;
	private int queue_lenth = 15;
	private Circle<Long, AtomicInteger> fixedLengthQueue = new Circle<Long, AtomicInteger>();
	private Routee routee;

	public RouteeRelivePolicy(Routee routee) {
		this.routee = routee;
		this.interval = FawkesUtil.getIntProperty(PropKeys.RELIVE_INTERVAL,DEFAULT_RELIVE_INTERVAL);
		this.TIME_LIMIT = FawkesUtil.getLongProperty(PropKeys.RELIVE_INTERVAL_LIMIT, this.TIME_LIMIT);
		this.COUNT_LIMIT = FawkesUtil.getIntProperty(PropKeys.RELIVE_COUNT_LIMIT, this.COUNT_LIMIT);
	}

	@Override
	public boolean isLive() {
		boolean value = false;
		boolean vc = meetCountPolicy();
		boolean vt = meetTimePolicy();
		// boolean vtps = meetTpsPolicy();
		// if (vtps || vc || vt) {
		if (vc || vt) {
			interval = interval * SCALE;
			interval = interval < TIME_LIMIT ? interval : TIME_LIMIT;
			threshold = threshold * SCALE;
			threshold = threshold < COUNT_LIMIT ? threshold : COUNT_LIMIT;
			value = true;
			// increaseTps();
		}
		return value;
	}

	private void increaseTps() {
		long ct = Calendar.getInstance().getTimeInMillis();
		long sec = TimeUnit.MILLISECONDS.toSeconds(ct);
		AtomicInteger ai = fixedLengthQueue.get(sec);
		if (ai == null) {
			ai = new AtomicInteger(0);
			fixedLengthQueue.put(sec, ai);
		}
		ai.incrementAndGet();

	}

	private boolean meetTpsPolicy() {
		boolean b = false;
		long ct = Calendar.getInstance().getTimeInMillis();
		long sec = TimeUnit.MILLISECONDS.toSeconds(ct);
		if (fixedLengthQueue.size() > 0) {
			AtomicInteger ai = fixedLengthQueue.lowerValue(sec);
			if (ai != null && ai.get() < tps_threshold) {
				logger.warn(this.routee.getName()+" "+this.routee.getHostUrl()+ " relived by tps policy,tps=" + ai.get());
				b = true;
			}
			cleanQueue();
		}
		return b;
	}

	private void cleanQueue() {
		while (fixedLengthQueue.size() > queue_lenth) {
			fixedLengthQueue.remove(fixedLengthQueue.firstKey());
		}

	}

	private boolean meetTimePolicy() {
		boolean v = false;
		if (start == 0) {
			start = System.currentTimeMillis();
		} else {
			long tmp = System.currentTimeMillis() - start;
			if (tmp > interval) {
				logger.warn(this.routee.getName()+" "+this.routee.getHostUrl()+ " relived by time policy,interval=" + tmp);
				start = System.currentTimeMillis();
				v = true;
			}
		}
		return v;
	}

	private boolean meetCountPolicy() {
		boolean v = false;
		tryCount++;
		if (tryCount >= threshold) {
			logger.warn(this.routee.getName()+" "+this.routee.getHostUrl() + " relived by count policy,count=" + tryCount);
			tryCount = 0;
			v = true;

		}
		return v;
	}

	@Override
	public void reset() {
		tryCount = 0;
		start = 0;
		threshold = DEFAULT_RELIVE_COUNT;
		interval = DEFAULT_RELIVE_INTERVAL;
		logger.warn(this.routee.getName()+" "+this.routee.getHostUrl() + " back to normal!!!");
	}

	@Override
	public void recordFail() {

	}

}
