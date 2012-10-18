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
 * Represents ODI configuration to build the object responsible
 * for ODI operations on Unit Tests
 * 
 * @author Robson Kraemer
 */
public interface ODIUnitConfiguration {

	/**
	 * Object containing ODI Master Repository Configuration Information
	 * @param masterRepositoryConfiguration
	 * @return
	 */
	ODIUnitConfiguration setMasterRepositoryConfiguration(ODIMasterRepositoryConfiguration masterRepositoryConfiguration);
	
	/**
	 * Set the name of Work Repository to use
	 * @param workRepositoryName
	 * @return
	 */
	ODIUnitConfiguration setWorkRepositoryName(String workRepositoryName);
	
	/**
	 * 
	 * @return ODI version
	 */
	String getVersion();
	
	/**
	 * 
	 * @return ODI Master Repository Configuration Object
	 */
	ODIMasterRepositoryConfiguration getMasterRepositoryConfiguration();
	
	/**
	 * 
	 * @return choosed ODI Work Repository Name
	 */
	String getWorkRepositoryName();
}
