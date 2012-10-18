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
 * Define a representation of an execution result of ODI Scenario
 * @author Robson Kraemer
 *
 */
public interface ODIScenarioExecutionResult {

	public enum Status {
		DONE, ERROR, QUEUED, RUNNING,
		UNKNOWN, WAITING, WARNING
	}
	
	/**
	 * Set ODI Scenario Session ID
	 * @param sessionID
	 */
	void setSessionID(long sessionID);
	
	/**
	 * 
	 * @return the scenario session ID
	 */
	long getSessionID();
	
	/**
	 * Set Status that scenario session finished
	 * @param status
	 */
	void setStatus(Status status);
	
	/**
	 * 
	 * @return the scenario session status
	 */
	Status getStatus();
	
	/**
	 * Set finish message of scenario session
	 * @param message
	 */
	void setMessage(String message);
	
	/**
	 * 
	 * @return execution message of scenario session
	 */
	String getMessage();
	
	/**
	 * Set the number of inserted records in scenario session
	 * @param numberOfInserts
	 */
	void setNumberOfInserts(int numberOfInserts);
	
	/**
	 * 
	 * @return the number of inserted records in scenario session
	 */
	int getNumberOfInserts();
	
	/**
	 * Set the number of updated records in scenario session
	 * @param numberOfUpdates
	 */
	void setNumberOfUpdates(int numberOfUpdates);
	
	/**
	 * 
	 * @return the number of updated records in scenario session
	 */
	int getNumberOfUpdates();
	
	/**
	 * Set the number of deleted records in scenario session
	 * @param numberOfDeletes
	 */
	void setNumberOfDeletes(int numberOfDeletes);
	
	/**
	 * 
	 * @return the number of deleted records in scenario session
	 */
	int getNumberOfDeletes();
}
