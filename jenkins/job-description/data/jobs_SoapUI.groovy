package data

class jobs_SoapUI {

	def jobs = [
		pipe_SoapUI_Test: [
				name: "SoapUI_Test",
				view: "SoapUI",
				job: [
					agent: "maven-3.6-jdk-8"
				],
				config: [
					soapui: [
						fullPathToYaml: "testcases/tools/soapui-test.yaml",
						selectionValue: "avengers",
						selectionKey: "team",
						branch: "develop",
						vendor: "azd",
						testTypeId: "2",
						testLevel: "4",
						rootElement: "soapui_path",
						shortPathToYaml: "testcases/tools/",
						yamlName: "soapui-test.yaml",
						release: "abs 20.1",
						entityKey: "team",
						junitResultUrl: "http://dekubitus.dhcp.allianz:8080/api/v1/junitResult/",
						mavenLogUrl: "http://dekubitus.dhcp.allianz:8080/api/v1/mavenLog/",
						parallelCount: "4"
					],
					git: [
						repo: '/absd-dev/absd-test-interface.git',
						branch: 'develop',
                        dir: 'testcases'
					]
				],
				stages: [
					[name: 'Preperation'	, rank:   5, use: true, catchError: false, type: 'stage', steps: ['general.clean']],
					[name: 'SoapUI - Parallel prep'   , rank:  10, use: true, catchError: false, type: 'stage', steps: ['soapui.runsoapuitests']]
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
