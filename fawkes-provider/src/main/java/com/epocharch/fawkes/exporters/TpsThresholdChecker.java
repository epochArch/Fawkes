
/*
 * Copyright 2017 EpochArch.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.epocharch.fawkes.exporters;

import java.util.LinkedList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author Archer
 * 
 */
public class TpsThresholdChecker {

	private int threshold = 0;
	private LinkedList<Integer> hisIvkCtList = new LinkedList<Integer>();
	private AtomicInteger curIvkCount = new AtomicInteger(0);
	private int efIvkCount = 0;
	private Timer timer = new Timer(true);
	private Lock lock = new ReentrantLock();
	private int DEFAULT_COLLECT_INTERVAL = 3;
	private int DEFAULT_MAX_COLLECT_ROUND =12;
	private int DEFAULT_COLLECT_INTERVAL_UNIT_SECOND = 1000;

	public TpsThresholdChecker(int tpsThreshold) {
		if (tpsThreshold > 0) {
			threshold = tpsThreshold * DEFAULT_COLLECT_INTERVAL * DEFAULT_MAX_COLLECT_ROUND;
			initIvkCountJob();
		}
	}

	public boolean isReached() {
		boolean value = false;
		if (threshold > 0) {
			value = (curIvkCount.incrementAndGet() + efIvkCount) >= threshold;
		}
		return value;
	}

	private void initIvkCountJob() {
		final int interval = DEFAULT_COLLECT_INTERVAL * DEFAULT_COLLECT_INTERVAL_UNIT_SECOND;
		timer.schedule(new TimerTask() {

			@Override
			public void run() {
				lock.lock();
				try {
					int count = curIvkCount.getAndSet(0);
					while (hisIvkCtList.size() >= DEFAULT_MAX_COLLECT_ROUND) {
						int tmp = hisIvkCtList.removeFirst();
						efIvkCount -= tmp;
					}
					hisIvkCtList.add(count);
					efIvkCount += count;
				} finally {
					lock.unlock();
				}

			}
		}, interval, interval);

	}
}
