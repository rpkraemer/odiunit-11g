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

import java.util.Map;

/**
 * Define a ODI Scenario Invoker, it's responsible for configurate and make an execution at ODI Repository
 * @author Robson Kraemer
 *
 */
public interface ODIScenarioInvoker {

	/**
	 * Pass the Agent which will execute ODI scenario
	 * @param agent
	 * @return
	 */
	ODIScenarioInvoker agent(Agent agent);
	
	/**
	 * Pass the ODI scenario to be executed
	 * @param scenario
	 * @return
	 */
	ODIScenarioInvoker scenario(String scenario);
	
	/**
	 * Pass the ODI scenario to be executed with your initialization variables
	 * @param scenario
	 * @param variables
	 * @return
	 */
	ODIScenarioInvoker scenario(String scenario, Map<String, String> variables);
	
	/**
	 * Pass the version of scenario
	 * @param scenarioVersion
	 * @return
	 */
	ODIScenarioInvoker version(String scenarioVersion);
	
	/**
	 * Pass the ODI context to execute the scenario
	 * @param context
	 * @return
	 */
	ODIScenarioInvoker context(String context);
	
	/**
	 * Invoke this scenario inside ODI. According with Master Repository Connection
	 * @return the object containing information about execution
	 */
	ODIScenarioExecutionResult invoke();
	
	/**
	 * Pass the log lever of ODI Scenario Session
	 * @param logLevel
	 * @return
	 */
	ODIScenarioInvoker level(int logLevel);
}
