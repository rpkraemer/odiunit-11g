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

package br.com.rpk.odiunit.implementation;

import java.util.Map;

import br.com.rpk.odiunit.Agent;
import br.com.rpk.odiunit.ODIScenarioExecutionResult;
import br.com.rpk.odiunit.ODIScenarioInvoker;
import br.com.rpk.odiunit.ODIUnit;

public class ODIScenarioInvoker11g implements ODIScenarioInvoker {

	private ODIUnit odi11g; 
	private Agent agent;
	private String scenario;
	private Map<String, String> scenarioVariables;
	private String scenarioVersion;
	private String context;
	private int scenarioLogLevel;
	
	/**
	 * INTERNAL ONLY
	 * Used to construct a ODI Scenario Invoker from ODIUnit11g class
	 * @param odi11g
	 */
	protected ODIScenarioInvoker11g(ODIUnit odi11g) {
		this.odi11g = odi11g;
		this.scenarioLogLevel = 5; // Default
	}
	
	@Override
	public ODIScenarioInvoker agent(Agent agent) {
		if (agent instanceof AgentLocal11g)
			this.agent = new AgentLocal11g((AgentLocal11g) agent, this);
		else if (agent instanceof AgentRemote11g)
			this.agent = new AgentRemote11g((AgentRemote11g) agent, this);
		else
			throw new IllegalArgumentException("Must be an ODI 11g Agent");
		return this;
	}

	@Override
	public ODIScenarioInvoker scenario(String scenario) {
		this.scenario = scenario;
		return this;
	}
	
	@Override
	public ODIScenarioInvoker scenario(String scenario, Map<String, String> variables) {
		this.scenario = scenario;
		this.scenarioVariables = variables;
		return this;
	}

	@Override
	public ODIScenarioInvoker version(String scenarioVersion) {
		this.scenarioVersion = scenarioVersion;
		return this;
	}

	@Override
	public ODIScenarioInvoker context(String context) {
		this.context = context.toUpperCase(); // sdk uses context code (upper)
		return this;
	}

	@Override
	public ODIScenarioExecutionResult invoke() {
		return this.agent.invokeLogic();
	}

	public ODIUnit getODIUnit11g() {
		return odi11g;
	}

	public String getScenario() {
		return scenario;
	}

	public Map<String, String> getScenarioVariables() {
		return scenarioVariables;
	}

	public String getScenarioVersion() {
		return scenarioVersion;
	}

	public String getContext() {
		return context;
	}
	
	public int getScenarioLogLevel() {
		return this.scenarioLogLevel;
	}

	@Override
	public ODIScenarioInvoker level(int scenarioLogLevel) {
		this.scenarioLogLevel = scenarioLogLevel;
		return this;
	}
}
