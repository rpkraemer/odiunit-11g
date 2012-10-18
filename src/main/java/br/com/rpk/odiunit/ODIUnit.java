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

import java.util.List;

/**
 * Represents an ODI Unit instance
 * @author Robson Kraemer
 *
 */
public interface ODIUnit {

	/**
	 * 
	 * @return the ODI scenario invoker
	 */
	ODIScenarioInvoker getODIScenarioInvoker();
	
	/**
	 * 
	 * @return the Master Repository Connection
	 */
	ODIMasterRepositoryConfiguration getMasterRepositoryConfiguration();

	/**
	 * 
	 * @return the ODI instance version
	 */
	String getOdiVersion();

	/**
	 * 
	 * @return the Work Repository Name
	 */
	String getWorkRepositoryName();
	
	/**
	 * If you want to set up enviroment before execute your unit test,
	 * add the object here
	 * @param setUpStatement
	 */
	ODIUnit addSetUp(ODISetUpStatement setUpStatement);
	
	/**
	 * 
	 * @return all set ups
	 */
	List<ODISetUpStatement> getSetUpStatements();
	
	/**
	 * If you want to create a unified Java Object to encapsulate all assertions for a unit test,
	 * add the object here
	 * @param assertionStatement
	 */
	ODIUnit addAssertion(ODIAssertionStatement assertionStatement);
	
	/**
	 * 
	 * @return all assertions
	 */
	List<ODIAssertionStatement> getAssertionStatements();
	
	/**
	 * Execute all SetUps Statements
	 */
	void executeSetUps();
	
	/**
	 * Execute all Assertions Statements
	 */
	void executeAssertions();
	
	/**
	 * Clean all SetUps and Assertions defined
	 */
	void cleanSetUpsAndAssertions();

}
