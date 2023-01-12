package data


class jobs_ABSd {

	def jobs = [
		ABSd_Feature_Merge: [
			name: "ABSd_Feature_Merge",
			view: "ABSd_Feature",
			config: [
				git: [ 
					branch: "develop",
					source_branch1: "\${env.gitlabBranch}",
					source_branch: '${merge_feature}',
					comment: 'merge ${merge_feature} into develop'
				]
			],
			parameters: {
				stringParam('merge_feature','', 'feature to merge')
			},		
	
			stages: [
				 [name: 'CheckOut'             			, rank:  00, use: true, catchError: false, type: 'stage', steps: ['general.clean','git.checkout']],
				 [name: 'merge'					, rank:  10, use: true, catchError: false, type: 'stage', steps: ['git.merge']],
				 [name: 'read configs'         			, rank:  20, use: true, catchError: false, type: 'stage', steps: ['']],
				 [name: 'Build'                			, rank:  30, use: true, catchError: false, type: 'stage', steps: ['maven.build']],
				 [name: 'Komponententests - prep'	  	, rank:  40, use: true, catchError: true,  type: 'stage', steps: ['']],
				 [name: 'Komponententests - Unit'     	, rank:  50, use: true, catchError: true,  type: 'stage', steps: ['']],
				 [name: 'Komponententests - Unit-plugin', rank:  60, use: true, catchError: true,  type: 'stage', steps: ['']],
				 [name: 'cleanUp'	      			, rank:  65, use: true, catchError: true,  type: 'stage', steps: ['abs.cleandlls']],
				 [name: 'generate docs'	      			, rank:  70, use: true, catchError: true,  type: 'stage', steps: ['']],
				 [name: 'publish and delete'			, rank:  80, use: true, catchError: false,  type: 'stage', steps: ['git.publish']]
			]
		],
		ABSd_Feature_Build: [
			name: "ABSd_Feature_Build",
			view: "ABSd_Feature",
			config: [
				git: [ 
					branch: "develop",
					source_branch1: "\${env.gitlabBranch}",
					source_branch: '${test_feature}'
				]
			],
			parameters: {
				stringParam('test_feature','', 'feature to test')
			},		
	
			stages: [
				 [name: 'CheckOut'             			, rank:  00, use: true, catchError: false, type: 'stage', steps: ['general.clean','git.checkout']],
				 [name: 'merge'					, rank:  10, use: true, catchError: false, type: 'stage', steps: ['git.merge']],
				 [name: 'read configs'         			, rank:  20, use: true, catchError: false, type: 'stage', steps: ['']],
				 [name: 'Build'                			, rank:  30, use: true, catchError: false, type: 'stage', steps: ['maven.build']],
				 [name: 'Komponententests - prep'	  	, rank:  40, use: true, catchError: true,  type: 'stage', steps: ['']],
				 [name: 'Komponententests - Unit'     	, rank:  50, use: true, catchError: true,  type: 'stage', steps: ['']],
				 [name: 'Komponententests - Unit-plugin', rank:  60, use: true, catchError: true,  type: 'stage', steps: ['']],
				 [name: 'generate docs'	      			, rank:  70, use: true, catchError: true,  type: 'stage', steps: ['']]
			]
		],
		
		ABSd_Feature_Unit_Test_woMerge: [
			name: "ABSd_Feature_Unit_Test_woMerge",
			view: "ABSd_Feature",
			config: [
				git: [ 
					branch: '${test_feature}',
					source_branch1: "\${env.gitlabBranch}",
					source_branch: '${test_feature}'
				]
			],
			parameters: {
				stringParam('test_feature','', 'feature to test')
			},		
	
			stages: [
				 [name: 'CheckOut'             			, rank:  00, use: true, catchError: false, type: 'stage', steps: ['general.clean','git.checkout']],
				 [name: 'merge'					, rank:  10, use: true, catchError: false, type: 'stage', steps: ['']],
				 [name: 'read configs'         			, rank:  20, use: true, catchError: false, type: 'stage', steps: ['']],
				 [name: 'Build'                			, rank:  30, use: true, catchError: false, type: 'stage', steps: ['maven.testunit']],
				 [name: 'Komponententests - prep'	  	, rank:  40, use: true, catchError: true,  type: 'stage', steps: ['']],
				 [name: 'Komponententests - Unit'     	, rank:  50, use: true, catchError: true,  type: 'stage', steps: ['']],
				 [name: 'Komponententests - Unit-plugin', rank:  60, use: true, catchError: true,  type: 'stage', steps: ['']],
				 [name: 'generate docs'	      			, rank:  70, use: true, catchError: true,  type: 'stage', steps: ['']]
			]
		],
		
		ABSd_Pipeline_Simulation: [
			name: "ABSd_Pipeline_Simulation",
			view: "ABSd_Pipeline",
			trigger: {
				cron('H 05 * * 1-5')
			},
			config: [
				git: [ 
					branch: "develop"
				]
			],
			stages: [
				[name: 'CheckOut'             			, rank:  10, use: true, catchError: false, type: 'stage', steps: ['general.wait']],
				[name: 'read configs'         			, rank:  15, use: true, catchError: false, type: 'stage', steps: ['general.wait']],
				[name: 'Build'                			, rank:  20, use: true, catchError: false, type: 'stage', steps: ['general.wait']],
				[name: 'Komponententest - prep'	  		, rank:  30, use: true, catchError: true,  type: 'stage', steps: ['general.wait']],
				[name: 'Komponententest - Unit'     	, rank:  40, use: true, group: 'Komponententest', catchError: true,  type: 'stage', steps: ['general.wait']],
				[name: 'Komponententest - Unit-plugin' 	, rank:  50, use: true, group: 'Komponententest', catchError: true,  type: 'stage', steps: ['general.wait']],
				[name: 'generate docs'	      			, rank:  80, use: true, catchError: true,  type: 'stage', steps: ['general.wait']],
				[name: 'Package - P2'		  			, rank:  90, use: true, group: 'Package', catchError: true,  type: 'stage', steps: ['general.wait']],
				[name: 'Package - SAC'		  			, rank: 100, use: true, group: 'Package', catchError: true,  type: 'stage', steps: ['general.wait']],
				[name: 'Package - Webservices'			, rank: 110, use: true, group: 'Package', catchError: true,  type: 'stage', steps: ['general.wait']],
				[name: 'Deploy - ATQ'		  			, rank: 120, use: true, catchError: true,  type: 'stage', steps: ['general.wait']],					
				[name: 'Integrationtest - Prep'	  		, rank: 130, use: true, catchError: true,  type: 'stage', steps: ['general.wait']],					
				[name: 'Integrationtest - Sonar'        , rank: 140, use: true, group: 'Integrationtest', catchError: true,  type: 'stage', steps: ['general.wait']],
				[name: 'Integrationtest - XRay' 		, rank: 150, use: true, group: 'Integrationtest', catchError: true,  type: 'stage', steps: ['general.wait']],
				[name: 'Integrationtest - CISL'    		, rank: 160, use: true, group: 'Integrationtest', catchError: true,  type: 'stage', steps: ['general.wait']],
				[name: 'Integrationtest - Tosca'	  	, rank: 170, use: true, group: 'Integrationtest', catchError: true,  type: 'stage', steps: ['general.wait']],
				[name: 'Integrationtest - Unit'     	, rank: 132, use: true, group: 'Integrationtest', catchError: true,  type: 'stage', steps: ['general.wait']],
				[name: 'Integrationtest - Unit-plugin' 	, rank: 135, use: true, group: 'Integrationtest', catchError: true,  type: 'stage', steps: ['general.wait']],
				[name: 'Deploy - ATE'		  			, rank: 180, use: true, group: 'Deploy', catchError: true,  type: 'stage', steps: ['general.wait']],
				[name: 'Deploy - ATA'		  			, rank: 190, use: true, group: 'Deploy', catchError: true,  type: 'stage', steps: ['general.wait']],					
				[name: 'Abnahmetest - Prep'	  			, rank: 200, use: true, catchError: true,  type: 'stage', steps: ['general.wait']],
				[name: 'Abnahmetest - CISL'     		, rank: 210, use: true, group: 'Abnahmetest', catchError: true,  type: 'stage', steps: ['general.wait']],
				[name: 'Abnahmetest - Tosca'	  		, rank: 220, use: true, group: 'Abnahmetest', catchError: true,  type: 'stage', steps: ['general.wait']],
				[name: 'Abnahmetest - Unit'     		, rank: 204, use: true, group: 'Abnahmetest', catchError: true,  type: 'stage', steps: ['general.wait']],
				[name: 'Abnahmetest - Unit-plugin' 		, rank: 206, use: true, group: 'Abnahmetest', catchError: true,  type: 'stage', steps: ['general.wait']],
			]
		],
		ABSd_Pipeline_Develop: [
			name: "ABSd_Pipeline_Develop",
			view: "ABSd_Pipeline",
			trigger: {
				cron('H 6-18/2 * * 1-5')
			},
			config: [
				git: [ 
					branch: "develop"
				]
			],
			stages: [
				[name: 'CheckOut'             			, rank:  10, use: true, catchError: false, type: 'stage', steps: ['git.checkout']],
				[name: 'read configs'         			, rank:  15, use: true, catchError: false, type: 'stage', steps: ['general.wait']],
				[name: 'Build'                			, rank:  20, use: true, catchError: false, type: 'stage', steps: ['maven.build']],
				[name: 'Komponententest - prep'	  		, rank:  30, use: true, catchError: true,  type: 'stage', steps: ['general.wait']],
				[name: 'Komponententest - Unit'     	, rank:  40, use: true, group: 'Komponententest', catchError: true,  type: 'stage', steps: ['general.wait']],
				[name: 'Komponententest - Unit-plugin' 	, rank:  50, use: true, group: 'Komponententest', catchError: true,  type: 'stage', steps: ['general.wait']],
				[name: 'generate docs'	      			, rank:  80, use: true, catchError: true,  type: 'stage', steps: ['general.wait']],
				[name: 'Package - P2'		  			, rank:  90, use: true, group: 'Package', catchError: true,  type: 'stage', steps: ['general.wait']],
				[name: 'Package - SAC'		  			, rank: 100, use: true, group: 'Package', catchError: true,  type: 'stage', steps: ['general.wait']],
				[name: 'Package - Webservices'			, rank: 110, use: true, group: 'Package', catchError: true,  type: 'stage', steps: ['general.wait']],
				[name: 'Deploy - ATQ'		  			, rank: 120, use: true, catchError: true,  type: 'stage', steps: ['general.wait']],					
				[name: 'Integrationtest - Prep'	  		, rank: 130, use: true, catchError: true,  type: 'stage', steps: ['general.wait']],					
				[name: 'Integrationtest - Sonar'        , rank: 140, use: true, group: 'Integrationtest', catchError: true,  type: 'stage', steps: ['general.wait']],
				[name: 'Integrationtest - XRay' 		, rank: 150, use: true, group: 'Integrationtest', catchError: true,  type: 'stage', steps: ['general.wait']],
				[name: 'Integrationtest - CISL'    		, rank: 160, use: true, group: 'Integrationtest', catchError: true,  type: 'stage', steps: ['general.wait']],
				[name: 'Integrationtest - Tosca'	  	, rank: 170, use: true, group: 'Integrationtest', catchError: true,  type: 'stage', steps: ['general.wait']],
				[name: 'Integrationtest - Unit'     	, rank: 132, use: true, group: 'Integrationtest', catchError: true,  type: 'stage', steps: ['general.wait']],
				[name: 'Integrationtest - Unit-plugin' 	, rank: 135, use: true, group: 'Integrationtest', catchError: true,  type: 'stage', steps: ['general.wait']],
				[name: 'Deploy - ATE'		  			, rank: 180, use: true, group: 'Deploy', catchError: true,  type: 'stage', steps: ['general.wait']],
				[name: 'Deploy - ATA'		  			, rank: 190, use: true, group: 'Deploy', catchError: true,  type: 'stage', steps: ['general.wait']],					
				[name: 'Abnahmetest - Prep'	  			, rank: 200, use: true, catchError: true,  type: 'stage', steps: ['general.wait']],
				[name: 'Abnahmetest - CISL'     		, rank: 210, use: true, group: 'Abnahmetest', catchError: true,  type: 'stage', steps: ['general.wait']],
				[name: 'Abnahmetest - Tosca'	  		, rank: 220, use: true, group: 'Abnahmetest', catchError: true,  type: 'stage', steps: ['general.wait']],
				[name: 'Abnahmetest - Unit'     		, rank: 204, use: true, group: 'Abnahmetest', catchError: true,  type: 'stage', steps: ['general.wait']],
				[name: 'Abnahmetest - Unit-plugin' 		, rank: 206, use: true, group: 'Abnahmetest', catchError: true,  type: 'stage', steps: ['general.wait']],
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
