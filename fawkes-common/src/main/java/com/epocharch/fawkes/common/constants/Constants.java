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

package com.epocharch.fawkes.common.constants;

/**
 * Created by archer on 25/08/2017.
 */
public class Constants {

	public static final String PROTOCOL_PROFIX_HTTP = "http";
	public static final String PROTOCOL_PROFIX_AKKATCP = "akka.tcp";
	public static final String URL_PATTERN = "fawkesServices";
	public static final int DEFAULT_RELIVE_THRESHOLD = 50;
	public static final String LOG_PROFIX = "Fawkes said: ";
	public static final String UNKONW_DOMAIN = "UnknowDomain";
	public static final String STRING_SEPARATOR = ",";
	public static final int ROUTE_PRIORITY_PRIMARY = 10;
	public static final int ROUTE_PRIORITY_DEFAULT = 5;
	public static final int ROUTE_PRIORITY_BACKUP = 1;
	public static final int ROUTE_PRIORITY_NONE = 0;

	public static final String GLOBAL_ID = "glbId";
	public static final String REQUEST_ID = "reqId";
	public static final String TXN_ID = "txnId";
	public static final String REQUEST_HOP = "reqHop";
	public static final String REQUEST_PARENT_ID = "parentId";

	public static final String NAMESPACE_FAWKES="ns_fawkes";
	public static final String CONFIG_FILE_NAME = "fawkes-common.properties";
	public static final String METHOD_IS_VOID_KEY = "METHOD_IS_VOID_KEY";
	public static final String METHOD_ARGUMENTS_KEY = "METHOD_ARGUMENTS_KEY";

	public static final int APPID_MAX_LENGTH = 18;
	public static final int VALUE_LENGTH_LIMIT = 150;

	public static final String BALANCER_NAME_ROUNDROBIN = "rr";
	public static final String BALANCER_NAME_WEIGHTED_ROUNDROBIN = "wrr";
	public static final String BALANCER_NAME_WRR_GRAY = "wrr_gray";
	public static final String BALANCER_NAME_CONSISTENTHASH = "ch";
	public static final String  TIMEOUT = "to";
	public static final int  DEFAULT_TIMEOUT = 5000;
	public static final String  READ_TIMEOUT = "rto";
	public static final int  DEFAULT_READ_TIMEOUT = 2000;
	public static final String INVOKE_TIME = "invokeTime";
	public static final String TOKEN_GRAY = "token.gray";
	public static final String TOKEN_MAX = "token.max";
	public static final String CLIENT_VERSION = "clt.vsn";
	public static final String CAMPS_NAME = "clt.camp";
	public static final String NON_GROUP = "NoGroup";
	public static final String AKKA_PROPERTIES="akkaProperties";
}


