package data


class jobs_Tosca {

	def jobs = [
		pipe_Tosca_daily: [
				name: "Tosca_daily",
				view: "Tosca",
				trigger: {
					cron('30 15 * * 1-5')
				},
				config: [
					job: [
						agent: "maven-3.6-jdk-8"
					],
					tosca: [ 
						nodes: ["/ToscaTeam/DEX_Sandbox/Execution/TestEvents/Smoketests_195_DEX_POC","39f1a120-9420-c18b-9944-7cf7b16f2405", "39f4adb2-0a53-d179-393e-0d7fb04eda40"],
						fullPathToYaml: "tools/tosca-test.yaml",
						selectionKey: "tosca_unique_id",
						testTypeId: "1",
						rootElement: "tosca_unique_id",
						shortPathToYaml: "tools/",
						yamlName: "tosca-test.yaml",
						release: "ABS Release 20.0",
						testStufe: "acceptance",
						testLevel: "4",
						entityKey: "tosca_unique_id",
						junitResultUrl: "http://dekubitus.dhcp.allianz:8080/api/v1/junitResult/",
						mavenLogUrl: "http://dekubitus.dhcp.allianz:8080/api/v1/mavenLog/",
						parallelCount: "4",
						branch: "develop",
						vendor: "azd",
					],
					git: [
						repo: '/absd-dev/absd-test-interface.git',
						branch: 'develop',
						file: 'tools/sqlyaml.jar'
					]

				],
				stages: [
					[name: 'Preperation'					, rank:   5, use: true, catchError: false, type: 'stage', steps: ['general.clean','git.checkoutFile']],
					[name: 'Tosca - Run'           			, rank:  10, use: true, catchError: false, type: 'stage', steps: ['tosca.runtoscatests']]
				]
			],
		pipe_Tosca_onDemand: [
				name: "Tosca_onDemand",
				view: "Tosca",
				config: [
					tosca: [ 
						branch: "develop",
						teststufe: "OnDemand",
						release: '${abs_release}',
						timestamp: "20200305T101900",
						nodes: "tosca_nodes",
						tribe: '${your_tribe}'
					]
				],
				parameters: {
					choiceParam('abs_release',['19.5','19.6','20.0','Other'], 'ABS Release')
					stringParam('your_tribe', '', 'your tribe')
					textParam('tosca_nodes','n39f1d7de-ce94-32e8-37e4-986a2cc036a0', 'Tosca Test Nodes or IDs')
				},		
				
				stages: [
					[name: 'Preperation'					, rank:   5, use: true, catchError: false, type: 'stage', steps: ['general.clean']],
					[name: 'Tosca - Prep'          			, rank:  10, use: true, catchError: false, type: 'stage', steps: ['tosca.prep']],
					[name: 'Tosca - Run'           			, rank:  20, use: true, catchError: false, type: 'stage', steps: ['tosca.execute']]
				]
			]
	]


	def getJobs(String url) {
		def job = [:]
		switch(url) { 
			case "https://absdbuildtest-jenkins.apps.crpcc.azd.cloud.allianz/":
				job = jobs
				break;
			case "http://rollator.dhcp.allianz:8081/":
				job = jobs
				break;
			default:
				job = jobs
				break;
		}
		return job
	}
	
}
