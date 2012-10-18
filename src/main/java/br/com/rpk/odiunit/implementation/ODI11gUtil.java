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

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import oracle.odi.core.OdiInstance;
import oracle.odi.core.config.MasterRepositoryDbInfo;
import oracle.odi.core.config.OdiConfigurationException;
import oracle.odi.core.config.OdiInstanceConfig;
import oracle.odi.core.config.WorkRepositoryDbInfo;
import oracle.odi.core.repository.WorkRepository;

/**
 * Util methods used internally to retrieve ODI 11g metadata information
 * 
 * @author robsonpk
 */
public class ODI11gUtil {

	/**
	 * INTERNAL ONLY
	 * 
	 * Construct an 11g SDK OdiInstance object 
	 * used to have access to ODI metadata (master and work repos)
	 *  
	 * @param odiScenarioInvoker
	 * @return 11g SDK OdiInstance object
	 */
	public static OdiInstance getODIInstance(ODIUnit11g odiUnit11g) {
		String jdbcURL = odiUnit11g.getMasterRepositoryConfiguration().getJDBCUrl();
		String jdbcDriver = odiUnit11g.getMasterRepositoryConfiguration().getJDBCDriver();
		String jdbcDbUser = odiUnit11g.getMasterRepositoryConfiguration().getDBUser();
		String jdbcDbUserPassword = odiUnit11g.getMasterRepositoryConfiguration().getDBUserPassword();
		String workRepositoryName = odiUnit11g.getWorkRepositoryName();
		
		MasterRepositoryDbInfo masterDbInfo = new MasterRepositoryDbInfo(jdbcURL, jdbcDriver, jdbcDbUser, 
																		 jdbcDbUserPassword.toCharArray(), null);
		
		WorkRepositoryDbInfo workDbInfo = new WorkRepositoryDbInfo(workRepositoryName, null);
		OdiInstanceConfig config = new OdiInstanceConfig(masterDbInfo, workDbInfo);
		OdiInstance odiInstance;
		try {
			odiInstance = OdiInstance.createInstance(config);
		} catch (OdiConfigurationException e) {
			throw new RuntimeException("Cannot connect to ODI Master Repository: " + e.getMessage());
		}
		return odiInstance;
	}
	
	/**
	 * INTERNAL ONLY
	 * 
	 * Recuperate from ODI 11g metadata some ODI Session information
	 * Number of Inserts, Number of Updates, Number of Deletes
	 * 
	 * @param workRepository
	 * @param sessionID
	 * @return ODIScenarioExecutionResult11g object with above information
	 */
	protected static ODIScenarioExecutionResult11g recuperateFromWorkRepositoryScenarioSessionInformation(
			WorkRepository workRepository, long sessionID) {
		try {
			ODIScenarioExecutionResult11g odiScenarioExecutionResult = new ODIScenarioExecutionResult11g();
			Connection connection = workRepository.getDataSource().getConnection();
			int[] sessionInformation = getFromWorkRepositoryNumberOfInsertsUpdatesAndDeletesOfGivenSession(connection, sessionID);
		
			odiScenarioExecutionResult.setNumberOfInserts(sessionInformation[0]);
			odiScenarioExecutionResult.setNumberOfUpdates(sessionInformation[1]);
			odiScenarioExecutionResult.setNumberOfDeletes(sessionInformation[2]);

			return odiScenarioExecutionResult;
		} catch (SQLException e) {
			throw new RuntimeException("Error recuperating session information from Work Repository: " + e.getMessage());
		}
	}
	
	/**
	 * INTERNAL ONLY
	 * 
	 * Recupate ODI Session information
	 * @param connection - ODI Work Repository Connection
	 * @param sessionID - ODI Session ID
	 * @return array containing number of inserts, updates and deletes
	 */
	private static int[] getFromWorkRepositoryNumberOfInsertsUpdatesAndDeletesOfGivenSession(Connection connection, long sessionID) {
		int[] sessionInformation = new int[3];
		String sql = "SELECT NB_INS AS NUMBER_OF_INSERTS, " +
					        "NB_UPD AS NUMBER_OF_UPDATES, " +
					        "NB_DEL AS NUMBER_OF_DELETES " +
					 "FROM SNP_SESSION " +
					 "WHERE SESS_NO = ?";
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		try {
			statement = connection.prepareStatement(sql);
			statement.setLong(1, sessionID);
			resultSet = statement.executeQuery();
			if (resultSet.next()) {
				sessionInformation[0] = resultSet.getInt("NUMBER_OF_INSERTS");
				sessionInformation[1] = resultSet.getInt("NUMBER_OF_UPDATES");
				sessionInformation[2] = resultSet.getInt("NUMBER_OF_DELETES");
			}
		} catch (SQLException e) {
			throw new RuntimeException("Error recuperating Work Repository information: " + e.getMessage());
		}
		finally {
			try {
				if (resultSet != null)
					resultSet.close();
				if (statement != null)
					statement.close();
			} catch (SQLException e) {}
		}
		return sessionInformation;
	}
	
	
}
