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

/**
 * Define configuration information of a ODI Master Repository
 * @author Robson Kraemer
 *
 */
public interface ODIMasterRepositoryConfiguration {

	/**
	 * JDBC Driver used in Master Repository
	 * @param jdbcDriver
	 */
	ODIMasterRepositoryConfiguration setJDBCDriver(String jdbcDriver);
	
	/**
	 * 
	 * @return the JDBC Driver used in Master Repository
	 */
	String getJDBCDriver();
	
	/**
	 * JDBC URL used in Master Repository
	 * @param jdbcURL
	 */
	ODIMasterRepositoryConfiguration setJDBCUrl(String jdbcURL);
	
	/**
	 * 
	 * @return the JDBC URL used in Master Repository
	 */
	String getJDBCUrl();
	
	/**
	 * Database User used in Master Repository
	 * @param dbUser
	 */
	ODIMasterRepositoryConfiguration setDBUser(String dbUser);
	
	/**
	 * 
	 * @return the Database user used in Master Repository
	 */
	String getDBUser();
	
	/**
	 * Database User Password used in Master Repository
	 * @param dbUserPassword
	 */
	ODIMasterRepositoryConfiguration setDBUserPassword(String dbUserPassword);
	
	/**
	 * 
	 * @return the Database User Password used in Master Repository
	 */
	String getDBUserPassword();
}
