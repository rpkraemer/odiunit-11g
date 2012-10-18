## odi-unit-11g

A simple framework to testing ODI (Oracle Data Integrator) scenarios in Java code.

# installing

### Maven

clone this repository:

	git clone

build the project:
                
	mvn clean install

import the generated JAR into your project.

<b>Important:</b> you need to manually add Oracle Data Integrator SDK JARs to your project. The required JARs are listed below:

	bsf.jar
    bsh-2.0b2.jar
    commons-codec-1.3.jar
    commons-collections-3.2.jar
    commons-httpclient-3.1.jar
    commons-lang-2.2.jar
    commons-logging-1.1.1.jar
    eclipselink.jar
    odi-core.jar
    ojdl.jar
    oracle.ucp_11.1.0.jar
    persistence.jar
    spring-beans.jar
    spring-core.jar
    spring-dao.jar
    spring-jdbc.jar

		
# license
odi-unit is licensed under the terms of the [Apache License, Version 2.0](http://www.apache.org/licenses/LICENSE-2.0)