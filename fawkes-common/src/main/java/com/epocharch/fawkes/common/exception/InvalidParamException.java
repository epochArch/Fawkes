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
package com.epocharch.fawkes.common.exception;

/**
 * @author Archer Jiang
 *
 */
public class InvalidParamException extends FawkesException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 22584697336399243L;

	public InvalidParamException() {
		super();
	}

	public InvalidParamException(String message, Throwable cause) {
		super(message, cause);
	}

	public InvalidParamException(String message) {
		super(message);
	}

	public InvalidParamException(Throwable cause) {
		super(cause);
	}

}
