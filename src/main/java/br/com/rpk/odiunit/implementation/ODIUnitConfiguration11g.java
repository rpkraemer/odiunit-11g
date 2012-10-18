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

import br.com.rpk.odiunit.ODIMasterRepositoryConfiguration;
import br.com.rpk.odiunit.ODIUnitConfiguration;

public class ODIUnitConfiguration11g implements ODIUnitConfiguration {

	private ODIMasterRepositoryConfiguration masterRepositoryConfiguration;
	private String workRepositoryName;
	
	@Override
	public ODIUnitConfiguration11g setMasterRepositoryConfiguration(
			ODIMasterRepositoryConfiguration masterRepositoryConfiguration) {
		if (masterRepositoryConfiguration == null) {
			throw new IllegalArgumentException("Master Repository Configuration must not be null");
		}
		this.masterRepositoryConfiguration = masterRepositoryConfiguration;
		return this;
	}

	@Override
	public ODIUnitConfiguration11g setWorkRepositoryName(String workRepositoryName) {
		if (workRepositoryName == null || workRepositoryName.isEmpty()) {
			throw new IllegalArgumentException("Work Repository Name must not be informed");
		}
		this.workRepositoryName = workRepositoryName;
		return this;
	}

	@Override
	public String getVersion() {
		return "11g";
	}

	@Override
	public ODIMasterRepositoryConfiguration getMasterRepositoryConfiguration() {
		return this.masterRepositoryConfiguration;
	}

	@Override
	public String getWorkRepositoryName() {
		return this.workRepositoryName;
	}
}
