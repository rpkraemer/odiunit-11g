package br.com.rpk.odiunit.implementation;

import java.sql.Connection;
import java.sql.DriverManager;

import br.com.rpk.odiunit.ODIMasterRepositoryConfiguration;
import br.com.rpk.odiunit.ODIScenarioExecutionResult;
import br.com.rpk.odiunit.ODIScenarioInvoker;
import br.com.rpk.odiunit.ODISqlScriptSetUp;
import br.com.rpk.odiunit.ODIUnit;
import br.com.rpk.odiunit.ODIUnitConfiguration;
import br.com.rpk.odiunit.implementation.AgentLocal11g;
import br.com.rpk.odiunit.implementation.ODIMasterRepositoryConfiguration11g;
import br.com.rpk.odiunit.implementation.ODIUnit11g;
import br.com.rpk.odiunit.implementation.ODIUnitConfiguration11g;

public class Global {

	public static void main(String[] args) {
		
		// Criamos um objeto ODIMasterRepositoryConfiguration 
		// com as informações de conexão ao repositório Master desejado
		ODIMasterRepositoryConfiguration masterConfiguration = new ODIMasterRepositoryConfiguration11g();
		masterConfiguration.
			setDBUser("ODI_MASTER_REP").
			setDBUserPassword("diego").
			setJDBCDriver("oracle.jdbc.OracleDriver").
			setJDBCUrl("jdbc:oracle:thin:@sanga:1521/XE");
		
		// Criamos um objeto ODIUnitConfiguration com as informações obtidas anteriomente do
		// repositório Master além de passarmos o nome do repositório Work que iremos utilizar
		ODIUnitConfiguration config = new ODIUnitConfiguration11g();
		config.		
			setMasterRepositoryConfiguration(masterConfiguration).
			setWorkRepositoryName("WORKREP_LOCAL");
		
		// Criamos o objeto ODIUnit, 
		// Ele irá nos fornecer os mecanismos necessários na interação do nosso teste com o ambiente ODI
		ODIUnit odi11g = new ODIUnit11g(config);
		
		// Obtemos um Invocador de cenários ODI a partir do ODIUnit, que fará a chamada do cenário sendo testado
		ODIScenarioInvoker invoker = odi11g.getODIScenarioInvoker();
		
		// Configuramos o invoker
		invoker.scenario("PACOTE_EXEMPLO").
				version("002").
				context("Local").
				agent(new AgentLocal11g("SUPERVISOR", "SUNOPSIS")); // Utilizamos Agente Local (Sem Agente)
		
		// Após configurado, chamamos o método invoke() do nosso invoker
		// isso irá disparar a execução do cenário no ODI e após o término
		// receberá um objeto com os resultados da execução
		ODIScenarioExecutionResult result = invoker.invoke();
		
		//Mostramos os resultados da execução do cenário
		System.out.println(result.getSessionID());
		System.out.println(result.getStatus());
		System.out.println(result.getMessage());
		System.out.println(result.getNumberOfInserts());
		System.out.println(result.getNumberOfUpdates());
		System.out.println(result.getNumberOfDeletes());
	}
}

class TesteSetUp implements ODISqlScriptSetUp {
	
	@Override
	public String scriptLocation() {
		return "src/test/resources/global_set_up.sql";
	}
	
	@Override
	public Connection getConnection() {
		Connection connection = null;
		try {
			Class.forName("oracle.jdbc.OracleDriver");
			connection = DriverManager.getConnection("jdbc:oracle:thin:@sanga:1521/XE", "ODI_TESTES", "diego");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return connection;
	}
}