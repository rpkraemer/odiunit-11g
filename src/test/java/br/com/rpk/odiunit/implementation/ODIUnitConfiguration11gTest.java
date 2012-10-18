package br.com.rpk.odiunit.implementation;

import org.jmock.Mockery;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import br.com.rpk.odiunit.ODIMasterRepositoryConfiguration;
import br.com.rpk.odiunit.ODIUnitConfiguration;
import br.com.rpk.odiunit.implementation.ODIUnitConfiguration11g;

public class ODIUnitConfiguration11gTest {

	private Mockery mockery;
	
	@Before
	public void setUp() {
		this.mockery = new Mockery();
	}
	
	@Test
	public void shouldCreateAnConfigurationObject() {
		ODIMasterRepositoryConfiguration masterConfiguration = createMasterRepositoryConfiguration();
		ODIUnitConfiguration configuration = new ODIUnitConfiguration11g().
												setMasterRepositoryConfiguration(masterConfiguration).
												setWorkRepositoryName("WORKREPO");
		Assert.assertNotNull(configuration);
		Assert.assertEquals("11g", configuration.getVersion());
		Assert.assertNotNull(configuration.getMasterRepositoryConfiguration()); // should not be null
		Assert.assertEquals(masterConfiguration, configuration.getMasterRepositoryConfiguration());
		Assert.assertEquals("WORKREPO", configuration.getWorkRepositoryName());
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void shouldRiseExceptionWhenMasterRepositoryConfigurationIsNull() {
		ODIMasterRepositoryConfiguration masterConfiguration = null;
		@SuppressWarnings("unused")
		ODIUnitConfiguration configuration = new ODIUnitConfiguration11g().
											setMasterRepositoryConfiguration(masterConfiguration).
											setWorkRepositoryName("WORKREPO");
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void shouldRiseExceptionWhenWorkRepositoryNameIsEmpty() {
		ODIMasterRepositoryConfiguration masterConfiguration = createMasterRepositoryConfiguration();
		@SuppressWarnings("unused")
		ODIUnitConfiguration configuration = new ODIUnitConfiguration11g().
											setMasterRepositoryConfiguration(masterConfiguration).
											setWorkRepositoryName("");
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void shouldRiseExceptionWhenWorkRepositoryNameIsNull() {
		ODIMasterRepositoryConfiguration masterConfiguration = createMasterRepositoryConfiguration();
		@SuppressWarnings("unused")
		ODIUnitConfiguration configuration = new ODIUnitConfiguration11g().
											setMasterRepositoryConfiguration(masterConfiguration).
											setWorkRepositoryName(null);
	}

	private ODIMasterRepositoryConfiguration createMasterRepositoryConfiguration() {
		return this.mockery.mock(ODIMasterRepositoryConfiguration.class);
	}
}
