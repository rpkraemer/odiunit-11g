/*
 * Copyright 2012 Robson Kraemer
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

package br.com.rpk.odiunit;

/**
 * Represents an ODI Agent
 * @author Robson Kraemer
 *
 */
public interface Agent {

	/**
	 * Pass the Agent URL 
	 * @param url
	 */
	void setURL(String url);
	
	/**
	 * 
	 * @return the Agent URL
	 */
	String getURL();
	
	/**
	 * Pass the Agent User
	 * @param user
	 */
	void setUser(String user);
	
	/**
	 * 
	 * @return the Agent user
	 */
	String getUser();
	
	/**
	 * Pass the Agent User Password
	 * @param password
	 */
	void setPassword(String password);
	
	/**
	 * 
	 * @return the Agent User Password
	 */
	String getPassword();
	
	/**
	 * Put Agent's ODI Scenario Invocation logic inside this method
	 * @return the object containing the execution results
	 */
	ODIScenarioExecutionResult invokeLogic();
	
}
