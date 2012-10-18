## odi-unit-11g

A simple framework to testing ODI (Oracle Data Integrator) scenarios in Java code.

# builing and contributing

In order to build and/or contribute to this project, you have to clone this repository:

	git clone https://github.com/rpkraemer/odiunit-11g.git

after cloning, import the project into your Eclipse.

### Oracle Data Integrator required library to compile

Because this software uses some Oracle Data Integrator SDK libraries, you must add this library directly
from your ODI installation to the odi-unit-11g build path on Eclipse. 

Its location path is:

	<ODI_HOME>/oracledi.sdk/lib

The only required JAR to COMPILE this project is:

	odi-core.jar
	
### build the project

After prepare the project environment, run a Eclipse Clean/Build (necessary to prepare .class(es) of ODI library) 
and generate a JAR of odi-unit-11g executing:

	mvn install
	
#using odi-unit

After generating a JAR of this project (previous step), you can import it into your project.
Beyond odi-unit-11g JAR, you must import from your Oracle Data Integrator installation the following libraries too:	

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

<b>Important: </b> you have to add JDBC Driver used by your Master Repository and any other driver
you will want to use.

After adding this libraries, you will be able to start using odi-unit in your project.
		
# license
odi-unit is licensed under the terms of the [Apache License, Version 2.0](http://www.apache.org/licenses/LICENSE-2.0)