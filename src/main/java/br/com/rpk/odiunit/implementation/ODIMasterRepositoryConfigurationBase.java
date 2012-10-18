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

public class ODIMasterRepositoryConfigurationBase implements ODIMasterRepositoryConfiguration {

	private String jdbcDriver;
	private String jdbcURL;
	private String jdbcDbUser;
	private String jdbcDbUserPassword;
	
	@Override
	public ODIMasterRepositoryConfigurationBase setJDBCDriver(String jdbcDriver) {
		this.jdbcDriver = jdbcDriver;
		return this;
	}

	@Override
	public String getJDBCDriver() {
		return this.jdbcDriver;
	}

	@Override
	public ODIMasterRepositoryConfigurationBase setJDBCUrl(String jdbcURL) {
		this.jdbcURL = jdbcURL;
		return this;
	}

	@Override
	public String getJDBCUrl() {
		return this.jdbcURL;
	}

	@Override
	public ODIMasterRepositoryConfigurationBase setDBUser(String jdbcDbUser) {
		this.jdbcDbUser = jdbcDbUser;
		return this;
	}

	@Override
	public String getDBUser() {
		return this.jdbcDbUser;
	}

	@Override
	public ODIMasterRepositoryConfigurationBase setDBUserPassword(String jdbcDbUserPassword) {
		this.jdbcDbUserPassword = jdbcDbUserPassword;
		return this;
	}

	@Override
	public String getDBUserPassword() {
		return this.jdbcDbUserPassword;
	}
}
