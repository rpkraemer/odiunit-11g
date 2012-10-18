package br.com.rpk.odiunit.implementation;

import org.jmock.Mockery;
import org.junit.Before;
import org.junit.Test;

import br.com.rpk.odiunit.Agent;
import br.com.rpk.odiunit.ODIScenarioInvoker;
import br.com.rpk.odiunit.implementation.ODIScenarioInvoker11g;
import br.com.rpk.odiunit.implementation.ODIUnit11g;
import br.com.rpk.odiunit.implementation.ODIUnitConfiguration11g;

public class ODIScenarioInvoker11gTest {

	private Mockery mockery;
	
	@Before
	public void setUp() {
		this.mockery = new Mockery();
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void shouldNotAcceptsNon11gAgents() {
		ODIScenarioInvoker scenarioInvoker = new ODIScenarioInvoker11g(new ODIUnit11g(new ODIUnitConfiguration11g()));
		scenarioInvoker.agent(newNonODI11gAgent());
	}

	private Agent newNonODI11gAgent() {
		return this.mockery.mock(Agent.class); //Mock a Non Specific Agent
	}
}
