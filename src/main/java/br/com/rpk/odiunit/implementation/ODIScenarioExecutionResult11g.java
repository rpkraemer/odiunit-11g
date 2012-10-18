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

import br.com.rpk.odiunit.ODIScenarioExecutionResult;

public class ODIScenarioExecutionResult11g implements ODIScenarioExecutionResult {

	private Status scenarioSessionStatus;
	private String scenarioSessionMessage;
	private int numberOfInserts, numberOfUpdates, numberOfDeletes;
	private long scenarioSessionID;
	
	@Override
	public void setStatus(Status status) {
		this.scenarioSessionStatus = status;
	}

	@Override
	public Status getStatus() {
		return this.scenarioSessionStatus;
	}

	@Override
	public void setMessage(String message) {
		this.scenarioSessionMessage = message;
	}

	@Override
	public String getMessage() {
		return this.scenarioSessionMessage;
	}

	@Override
	public void setNumberOfInserts(int numberOfInserts) {
		this.numberOfInserts = numberOfInserts;
	}

	@Override
	public int getNumberOfInserts() {
		return this.numberOfInserts;
	}

	@Override
	public void setNumberOfUpdates(int numberOfUpdates) {
		this.numberOfUpdates = numberOfUpdates;
	}

	@Override
	public int getNumberOfUpdates() {
		return this.numberOfUpdates;
	}

	@Override
	public void setNumberOfDeletes(int numberOfDeletes) {
		this.numberOfDeletes = numberOfDeletes;
	}

	@Override
	public int getNumberOfDeletes() {
		return this.numberOfDeletes;
	}

	@Override
	public void setSessionID(long scenarioSessionID) {
		this.scenarioSessionID = scenarioSessionID;
	}

	@Override
	public long getSessionID() {
		return this.scenarioSessionID;
	}
}
