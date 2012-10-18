package br.com.rpk.odiunit.implementation;

import java.sql.Connection;
import java.sql.SQLException;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import br.com.rpk.odiunit.Agent;
import br.com.rpk.odiunit.ODIAssertionStatement;
import br.com.rpk.odiunit.ODIJavaSetUp;
import br.com.rpk.odiunit.ODIScenarioInvoker;
import br.com.rpk.odiunit.ODISqlScriptSetUp;
import br.com.rpk.odiunit.ODIUnit;
import br.com.rpk.odiunit.ODIUnitConfiguration;
import br.com.rpk.odiunit.implementation.AgentLocal11g;
import br.com.rpk.odiunit.implementation.ODIUnit11g;

public class ODIUnit11gTest {

	private Mockery mockery;
	
	@Before
	public void setUp() {
		this.mockery = new Mockery();
	}
	
	@Test
	public void shouldCreateAnODIObject() {
		ODIUnitConfiguration configuration = 
		givenODIConfiguration(); odiConfigurationMustBeUsedToSetUpODIInstance(configuration);
		
		ODIUnit odi11g = new ODIUnit11g(configuration);
		ODIScenarioInvoker invoker = odi11g.getODIScenarioInvoker();
		
		Assert.assertNotNull(odi11g);
		Assert.assertNotNull(invoker);
	}
	
	@Test
	public void shouldConfigurateODIScenarioInvoker() {
		ODIUnitConfiguration configuration = 
		givenODIConfiguration(); odiConfigurationMustBeUsedToSetUpODIInstance(configuration);
		
		ODIUnit odi11g = new ODIUnit11g(configuration);
		ODIScenarioInvoker invoker = odi11g.getODIScenarioInvoker();
		
		//configuring invoker
		Agent agent = new AgentLocal11g("user", "123456"); 
		invoker.
			scenario("SCNTESTE").version("001").context("Global").
			agent(agent);
		
		mockery.assertIsSatisfied();
	}
	
	@Test
	public void shouldAddSetUpStatements() {
		ODIUnitConfiguration configuration = 
		givenODIConfiguration(); odiConfigurationMustBeUsedToSetUpODIInstance(configuration);
		
		ODIUnit odi11g = new ODIUnit11g(configuration);
		Assert.assertEquals(0, odi11g.getSetUpStatements().size());
		
		odi11g.addSetUp(new JavaSetUp()).
			   addSetUp(new SQLScriptSetUp());
		
		Assert.assertEquals(2, odi11g.getSetUpStatements().size());
		odi11g.cleanSetUpsAndAssertions();
		Assert.assertEquals(0, odi11g.getSetUpStatements().size());
	}
	
	@Test
	public void shouldAddAssertionStatements() {
		ODIUnitConfiguration configuration = 
		givenODIConfiguration(); odiConfigurationMustBeUsedToSetUpODIInstance(configuration);
		
		ODIUnit odi11g = new ODIUnit11g(configuration);
		Assert.assertEquals(0, odi11g.getAssertionStatements().size());
		
		odi11g.addAssertion(new JavaAssertion());
		
		Assert.assertEquals(1, odi11g.getAssertionStatements().size());
		odi11g.cleanSetUpsAndAssertions();
		Assert.assertEquals(0, odi11g.getAssertionStatements().size());
	}
	
	@Test
	public void shouldExecuteSetUpStatements() {
		ODIJavaSetUp javaSetUp = givenAJavaSetUp();
		shouldBeExecutedWhenAddedToODIInstanceAndCallInvokeOnAgent(javaSetUp);
		
		ODIUnitConfiguration configuration = 
		givenODIConfiguration(); odiConfigurationMustBeUsedToSetUpODIInstance(configuration);
		
		ODIUnit odi11g = new ODIUnit11g(configuration);
		
		//configure environment setup statement
		odi11g.addSetUp(javaSetUp);
		
		ODIScenarioInvoker invoker = odi11g.getODIScenarioInvoker();
		
		//configuring invoker
		Agent agent = new AgentLocal11g("user", "123456");
		invoker.scenario("SCNTESTE").version("001").context("Global").agent(agent);

		// prepare environment set ups
		odi11g.executeSetUps();

		// assert expected results are satisfied
		odi11g.executeAssertions();
		
		mockery.assertIsSatisfied();
	}
	
	@Test
	public void shouldExecuteSQLScriptSetUp() throws SQLException {
		ODIUnitConfiguration configuration = 
		givenODIConfiguration(); odiConfigurationMustBeUsedToSetUpODIInstance(configuration);
		
		ODIUnit odi11g = new ODIUnit11g(configuration);
		
		ODISqlScriptSetUp scriptSetUp = givenASqlScriptSetUp();
		odi11g.addSetUp(scriptSetUp);
		whenInvokeMethodToMakeEnvironmentSetUp_ShouldSQLScriptBeExecuted(scriptSetUp);
		odi11g.executeSetUps();
		
		Assert.assertEquals(1, odi11g.getSetUpStatements().size());
		mockery.assertIsSatisfied();
	}
	
	@Test (expected = RuntimeException.class)
	public void shouldThrowsExceptionWhenTryToExecuteInexistentScriptSetUp() throws SQLException {
		ODIUnitConfiguration configuration = 
		givenODIConfiguration(); odiConfigurationMustBeUsedToSetUpODIInstance(configuration);
		
		ODIUnit odi11g = new ODIUnit11g(configuration);
		
		ODISqlScriptSetUp scriptSetUp = givenAInvalidSqlScriptSetUp();
		odi11g.addSetUp(scriptSetUp);
		whenInvokeMethodToMakeEnvironmentSetUp_ShouldSQLScriptThrowException(scriptSetUp);
		
		odi11g.executeSetUps();
		mockery.assertIsSatisfied();
	}

	private ODISqlScriptSetUp givenASqlScriptSetUp() {
		return mockery.mock(ODISqlScriptSetUp.class);
	}
	
	private ODISqlScriptSetUp givenAInvalidSqlScriptSetUp() {
		return mockery.mock(ODISqlScriptSetUp.class);
	}

	private void whenInvokeMethodToMakeEnvironmentSetUp_ShouldSQLScriptBeExecuted(final ODISqlScriptSetUp scriptSetUp) throws SQLException {
		final Connection connection = mockery.mock(Connection.class);
		mockery.checking(new Expectations() {{
			// should invoke twice connection.preparedStatement
			exactly(2).of(connection).prepareStatement(with(any(String.class)));
			oneOf (scriptSetUp).scriptLocation(); will(returnValue("src/test/resources/customers_set_up.sql"));
			// getConnection will return mocked connection
			oneOf (scriptSetUp).getConnection(); will(returnValue(connection));
		}});
	}

	private void whenInvokeMethodToMakeEnvironmentSetUp_ShouldSQLScriptThrowException(final ODISqlScriptSetUp scriptSetUp) throws SQLException {
		mockery.checking(new Expectations() {{
			oneOf (scriptSetUp).scriptLocation(); will(returnValue("invalid/location/inexisting_file.sql"));
		}});
	}
	
	private void shouldBeExecutedWhenAddedToODIInstanceAndCallInvokeOnAgent(final ODIJavaSetUp javaSetUp) {
		mockery.checking(new Expectations() {{
			oneOf (javaSetUp).execute();
		}});
	}

	private ODIJavaSetUp givenAJavaSetUp() {
		return mockery.mock(ODIJavaSetUp.class);
	}

	/**
	 * Configure expectations of ODIConfiguration object
	 * when it be called by ODI Instance constructor
	 */
	private void odiConfigurationMustBeUsedToSetUpODIInstance(final ODIUnitConfiguration configuration) {
		mockery.checking(new Expectations() {{
			oneOf (configuration).getMasterRepositoryConfiguration();
			oneOf (configuration).getVersion(); will(returnValue("11g"));
			oneOf (configuration).getWorkRepositoryName(); will(returnValue("WORKREP_LOCAL"));
		}});
	}
		
	/**
	 * 
	 * @return mock for ODIConfiguration
	 */
	private ODIUnitConfiguration givenODIConfiguration() {
		return mockery.mock(ODIUnitConfiguration.class);
	}
}

class JavaSetUp implements ODIJavaSetUp {
	
	public void execute() {
		System.out.println("I'm executing unit tests set up");
	}
}

class SQLScriptSetUp implements ODISqlScriptSetUp {
	
	public String scriptLocation() {
		return "location/to/script/script.sql";
	}
	
	public Connection getConnection() {
		return new Mockery().mock(Connection.class);
	}
}

class JavaAssertion implements ODIAssertionStatement {
	
	public void assertions() {
		System.out.println("I'm doing assertions of unit test");
	}
}
