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

import oracle.odi.core.OdiInstance;
import oracle.odi.runtime.agent.RuntimeAgent;
import oracle.odi.runtime.agent.invocation.ExecutionInfo;
import oracle.odi.runtime.agent.invocation.StartupParams;
import br.com.rpk.odiunit.Agent;
import br.com.rpk.odiunit.ODIScenarioExecutionResult;

/**
 * Agent Local ODI 11g
 * Use this to execute ODI Scenario with "local execution"
 * @author robsonpk
 *
 */
public class AgentLocal11g implements Agent {

	private String odiUser;
	private String odiPassword;
	private ODIScenarioInvoker11g odiScenarioInvoker;
	private ODIScenarioExecutionResult odiScenarioExecutionResult;
	
	/**
	 * @param odiUser - ODI User used by Agent to Connect to Repository
	 * @param odiPassword - ODI User Password used by Agent to Connect to Repository
	 */
	public AgentLocal11g(String odiUser, String odiPassword) {
		this.odiUser = odiUser;
		this.odiPassword = odiPassword;
	}
	
	/**
	 * INTERNAL ONLY
	 * Used to enhance Agent functionality, passing to it the current ODI Scenario Invoker
	 * @param odiUser
	 * @param odiPassword
	 * @param odiScenarioInvoker
	 */
	protected AgentLocal11g(AgentLocal11g agent, ODIScenarioInvoker11g odiScenarioInvoker) {
		this.odiUser = agent.getUser();
		this.odiPassword = agent.getPassword();
		this.odiScenarioInvoker = odiScenarioInvoker;
	}
	
	@Override
	public void setURL(String url) {
		throw new UnsupportedOperationException("This method is used only in Java EE Agent 11g");
	}

	@Override
	public String getURL() {
		throw new UnsupportedOperationException("This method is used only in Java EE Agent 11g");
	}

	@Override
	public void setUser(String user) {
		this.odiUser = user;
	}

	@Override
	public String getUser() {
		return this.odiUser;
	}

	@Override
	public void setPassword(String password) {
		this.odiPassword = password;
	}

	@Override
	public String getPassword() {
		return this.odiPassword;
	}
	
	@Override
	public ODIScenarioExecutionResult invokeLogic() {
		OdiInstance odiInstance = ODI11gUtil.getODIInstance((ODIUnit11g) this.odiScenarioInvoker.getODIUnit11g());

		// Are there scenario initializing variables?
		StartupParams variables = null;
		if (this.odiScenarioInvoker.getScenarioVariables() != null) {
			variables = new StartupParams(this.odiScenarioInvoker.getScenarioVariables());
		}
		
		RuntimeAgent localAgent = new RuntimeAgent(odiInstance, this.odiUser, this.odiPassword.toCharArray());
		ExecutionInfo executionInfo = null;
		try {
			 executionInfo = localAgent.
					startScenario(this.odiScenarioInvoker.getScenario(), 
						    	  this.odiScenarioInvoker.getScenarioVersion(),
						    	  variables, null, this.odiScenarioInvoker.getContext(), 
						    	  this.odiScenarioInvoker.getScenarioLogLevel(), null, true);
		} catch (Exception e) {
			throw new RuntimeException("Failed to execute scenario: " + e.getMessage());
		}
		this.odiScenarioExecutionResult = ODI11gUtil.
				recuperateFromWorkRepositoryScenarioSessionInformation(odiInstance.getWorkRepository(), executionInfo.getSessionId());
		this.odiScenarioExecutionResult.setSessionID(executionInfo.getSessionId());
		this.odiScenarioExecutionResult.setMessage(executionInfo.getStatusMessage());
		this.odiScenarioExecutionResult.setStatus(ODIScenarioExecutionResult.Status.valueOf(executionInfo.getSessionStatus().toString()));

		return this.odiScenarioExecutionResult;
	}
}