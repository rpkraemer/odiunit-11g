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
import oracle.odi.runtime.agent.invocation.ExecutionInfo;
import oracle.odi.runtime.agent.invocation.InvocationException;
import oracle.odi.runtime.agent.invocation.RemoteRuntimeAgentInvoker;
import oracle.odi.runtime.agent.invocation.StartupParams;
import br.com.rpk.odiunit.Agent;
import br.com.rpk.odiunit.ODIScenarioExecutionResult;


public class AgentRemote11g implements Agent {

	private String url;
	private String user;
	private String password;
	private ODIScenarioInvoker11g odiScenarioInvoker;
	private ODIScenarioExecutionResult odiScenarioExecutionResult;

	/**
	 * Invokes and ODI Scenario in StandAlone Agent or JavaEE Agent<br><br>
	 * 
	 * Example StandAlone (bat or sh):<br>
	 * 		<b>Agent agentLocalStandAlone = new AgentRemote11g("http://localhost:20910/oraclediagent", "SUPERVISOR", "SUNOPSIS")</b>
	 * create an remote agent that is listening at localhost port 20910 (agent.bat or agent.sh)

	 * <br><br>
	 * Example JavaEE:
	 * 		<b>Agent agentJavaEE = new AgentRemote11g("http://odiAgent:8001/oraclediagent", "SUPERVISOR", "SUNOPSIS")</b>
	 * create an remote agent(javaEE) that is listening at odiAgent host with port 8001
	 * 
	 * @param url - URL of Agent
	 * @param user - ODI User which Agent use to connect to Repository
	 * @param password - ODI User Password which Agent use to connect to Repository
	 */
	public AgentRemote11g(String url, String user, String password) {
		this.url = url;
		this.user = user;
		this.password = password;
	}
	
	/**
	 * INTERNAL ONLY
	 * @param agent
	 * @param odiScenarioInvoker
	 */
	protected AgentRemote11g(AgentRemote11g agent, ODIScenarioInvoker11g odiScenarioInvoker) {
		this.url = agent.getURL();
		this.user = agent.getUser();
		this.password = agent.getPassword();
		this.odiScenarioInvoker = odiScenarioInvoker;
	}
	
	@Override
	public void setURL(String url) {
		this.url = url;
	}

	@Override
	public String getURL() {
		return this.url;
	}

	@Override
	public void setUser(String user) {
		this.user = user;
	}

	@Override
	public String getUser() {
		return this.user;
	}

	@Override
	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public String getPassword() {
		return this.password;
	}

	@Override
	public ODIScenarioExecutionResult invokeLogic() {
		OdiInstance odiInstance = ODI11gUtil.getODIInstance((ODIUnit11g) this.odiScenarioInvoker.getODIUnit11g()); 
		
		// Are there scenario initializing variables?
		StartupParams variables = null;
		if (this.odiScenarioInvoker.getScenarioVariables() != null) {
			variables = new StartupParams(this.odiScenarioInvoker.getScenarioVariables());
		}
		
		RemoteRuntimeAgentInvoker remoteAgent = new RemoteRuntimeAgentInvoker(this.url, this.user, this.password.toCharArray());
		ExecutionInfo executionInfo = null;
		try {	
			executionInfo = remoteAgent.
					invokeStartScenario(this.odiScenarioInvoker.getScenario(), 
							 			this.odiScenarioInvoker.getScenarioVersion(), variables, 
							 			null, this.odiScenarioInvoker.getContext(), this.odiScenarioInvoker.getScenarioLogLevel(), null, 
							 			true, this.odiScenarioInvoker.getODIUnit11g().getWorkRepositoryName());
		} catch (InvocationException e) {
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
