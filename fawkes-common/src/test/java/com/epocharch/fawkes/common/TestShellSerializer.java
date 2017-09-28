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

package com.epocharch.fawkes.common;

import com.epocharch.fawkes.common.dto.TransShell;
import com.epocharch.fawkes.common.serializer.ShellSerializer;
import junit.framework.Assert;
import org.junit.Test;

/**
 * Created by archer on 25/09/2017.
 */
public class TestShellSerializer {

	@Test
	public void testShellSerializer() {
		String methodId = "aaaaaaaaaaaaaa.bbbbbbbbbb_ccccc_ddddd_eeeee_fffff";
		TransShell ts = new TransShell(methodId,"ttttttt",new byte[]{1,2,3});
		ShellSerializer ser = new ShellSerializer();
		byte[] bytes= ser.toBinary(ts);
		TransShell dts = ser.toObject(bytes);
		Assert.assertTrue(dts.getMethodId().equals(methodId));
		Assert.assertTrue(dts.getType().equals(ts.getType()));
	}
}
