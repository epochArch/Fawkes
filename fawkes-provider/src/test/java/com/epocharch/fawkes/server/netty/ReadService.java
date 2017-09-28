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

package com.epocharch.fawkes.server.netty;

import com.epocharch.fawkes.common.spi.IReadService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by archer on 13/09/2017.
 */
public class ReadService implements IReadService<String,String> {

	public String readObject(String s) {
		return s+1;
	}

	public Collection<String> readCollection(String s, Integer size) {
		List<String> list = new ArrayList<String>();
		for(int i=0;i<size;i++){
			list.add(s+i);
		}
		return list;
	}
}
