require 'java'	

module JavaODIUnit
	include_package "br.com.rpk.odiunit.implementation"
end

module ODIUnit

	module Drivers
		ORACLE = "oracle.jdbc.OracleDriver"
		MYSQL = ""
		POSTGRESQL = ""
	end

	class Executor
		@@testcases = Hash.new
		def self.execute(testcase_name)
			testcase = @@testcases[testcase_name]
			testcase.execute if testcase.respond_to? :execute
		end
		
		protected
		def self.add_testcase(testcase)
			@@testcases[testcase.name] = testcase
		end
	end

	class Scenario
		attr_reader :scen_id, :scen_name, :scen_version, :scen_variables
		
		def initialize(id)
			@scen_id = id
			@scen_variables = []
		end
	
		def name(name)
			@scen_name = name
		end
		
		def version(version)
			@scen_version = version
		end
		
		def variables(variables)
			raise "Must be a hash with scenarios variables!" unless variables.is_a? Hash
			@scen_variables = variables 
		end
	end
	
	class LocalAgent
		attr_reader :odi_user, :password
		
		def initialize(credentials, testcase)
			raise "Must be a hash with agent credentials (odi_user and password)!" unless credentials.is_a? Hash
			raise "Hash must contain: odi_user, password" unless credentials.has_key? :odi_user and credentials.has_key? :password
			@odi_user, @password = credentials.values
			@scenarios_to_invoke = Hash.new # Hash containing all odi scenarios that this agent must invoke
			@testcase = testcase 			# TestCase Reference, to access its attributes
		end	
		
		def should_invoke(scenario_id, odi_context)
			scenario = @testcase.scenarios.detect { |scen| scen.scen_id == scenario_id } #first occurrence of odi scenario
			@scenarios_to_invoke[:scenario_id] = {:scenario => scenario, :context => odi_context}
		end
	end
	
	class Repositories 
		attr_reader :master_repo, :work_repo		
		
		def master(config)
			raise "Must be a hash with master repository configurations!" unless config.is_a? Hash
			raise "Hash must contain: db_user, password, driver and jdbc_url" unless config.has_key? :db_user  and 
																					 config.has_key? :password and
																					 config.has_key? :driver   and
																					 config.has_key? :jdbc_url
			@master_repo = config
		end
		
		def work(work_repo_name)
			@work_repo = work_repo_name
		end
	end

	class TestCase
		attr_reader :name, :scenarios, :odi_repositories, :agent
		
		def initialize(test_case_name, &block)
			@name = test_case_name
			@scenarios = []
			instance_eval &block
			# After testcase object was built (through block) add it to executor
			Executor.add_testcase(self)
		end
		
		def odi_repositories
			odi_repositories = Repositories.new
			yield odi_repositories
			@odi_repositories = odi_repositories
		end
		
		def scenario(identification, &block)
			scenario = Scenario.new(identification)
			yield scenario
			@scenarios << scenario
		end
		
		def using_local_agent(credentials)
			@agent = local_agent = LocalAgent.new(credentials, self)
			yield agent
		end
		
		def execute
			puts "Executing #{name}...\n"
			puts "Work Repository: " + @odi_repositories.work_repo
			puts "Master Repository Info: " + @odi_repositories.master_repo[:db_user]
			puts "\nScenarios to be invoked by Agent\n"
			@scenarios.each { |s| 
				print "\tScenario: #{s.scen_name} \n\tVersion: #{s.scen_version}\n"
				if s.scen_variables.any?
					print "\tVariables: "
					s.scen_variables.each { |k,v| print "#{k}->#{v}\ " }
					print "\n"
				end
				puts
			}
			puts "Agent: #{@agent.odi_user.downcase}##{@agent.password.downcase}"
		end
	end
end


master_config = JavaODIUnit::ODIMasterRepositoryConfiguration11g.new()
master_config.set_jdbc_driver(ODIUnit::Drivers::ORACLE)
puts master_config.jdbc_driver