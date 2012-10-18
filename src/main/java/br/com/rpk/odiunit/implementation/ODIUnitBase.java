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

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import br.com.rpk.odiunit.ODIAssertionStatement;
import br.com.rpk.odiunit.ODIJavaSetUp;
import br.com.rpk.odiunit.ODIMasterRepositoryConfiguration;
import br.com.rpk.odiunit.ODISetUpStatement;
import br.com.rpk.odiunit.ODISqlScriptSetUp;
import br.com.rpk.odiunit.ODIUnit;
import br.com.rpk.odiunit.ODIUnitConfiguration;

public abstract class ODIUnitBase implements ODIUnit {
	
	private final ODIMasterRepositoryConfiguration masterRepositoryConfiguration;
	private final String odiVersion;
	private final String workRepositoryName;
	private List<ODISetUpStatement> setUpStatements = new ArrayList<ODISetUpStatement>();
	private List<ODIAssertionStatement> assertionStatements = new ArrayList<ODIAssertionStatement>();
	
	public ODIUnitBase(ODIUnitConfiguration odiConfiguration) {
		this.masterRepositoryConfiguration = odiConfiguration.getMasterRepositoryConfiguration();
		this.odiVersion = odiConfiguration.getVersion();
		this.workRepositoryName = odiConfiguration.getWorkRepositoryName();
	}
	
	@Override
	public ODIMasterRepositoryConfiguration getMasterRepositoryConfiguration() {
		return this.masterRepositoryConfiguration;
	}

	@Override
	public String getOdiVersion() {
		return this.odiVersion;
	}

	@Override
	public String getWorkRepositoryName() {
		return this.workRepositoryName;
	}
	
	@Override
	public ODIUnit addSetUp(ODISetUpStatement setUpStatement) {
		this.setUpStatements.add(setUpStatement);
		return this;
	}
	
	@Override
	public List<ODISetUpStatement> getSetUpStatements() {
		return Collections.unmodifiableList(this.setUpStatements);
	}
	
	@Override
	public List<ODIAssertionStatement> getAssertionStatements() {
		return Collections.unmodifiableList(this.assertionStatements);
	}
	
	@Override
	public ODIUnit addAssertion(ODIAssertionStatement assertionStatement) {
		this.assertionStatements.add(assertionStatement);
		return this;
	}
	
	@Override
	public void cleanSetUpsAndAssertions() {
		this.setUpStatements.clear();
		this.assertionStatements.clear();
	}
	
	@Override
	public void executeAssertions() {
		for (ODIAssertionStatement statement : this.assertionStatements)
			statement.assertions();
	}
	
	@Override
	public void executeSetUps() {
		for (ODISetUpStatement statement : this.setUpStatements) {
			if (statement instanceof ODIJavaSetUp)
				((ODIJavaSetUp) statement).execute();
			else if (statement instanceof ODISqlScriptSetUp) {
				ODISqlScriptSetUp script = (ODISqlScriptSetUp) statement;
				try {
					executeScript(script);
				} catch (IOException e) {
					throw new RuntimeException("Error in assertions execution: " + e.getMessage());
				} catch (SQLException e) {
					throw new RuntimeException("Error in assertions execution: " + e.getMessage());
				}
			}
		}
	}

	private void executeScript(ODISqlScriptSetUp script) throws IOException, SQLException {
		String scriptLocation = script.scriptLocation();
		BufferedReader reader = new BufferedReader(new FileReader(scriptLocation));
		String sql = "", line = "";
		while ((line = reader.readLine()) != null)
			sql += line;
		reader.close();
		
		String[] statements = sql.split(";");
		Connection connection = script.getConnection();
		PreparedStatement preparedStatement = null;
		for (String statementSql : statements) {
			if (!statementSql.isEmpty()) {
				preparedStatement = connection.prepareStatement(statementSql);
				preparedStatement.executeUpdate();
			}
		}
		if (preparedStatement != null) {
			preparedStatement.close();
		}
	}
}