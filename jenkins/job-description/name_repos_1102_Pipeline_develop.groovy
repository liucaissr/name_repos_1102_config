def job_config = [
    job: [
        name: "name_repos_1102_Pipeline_develop",
        agent: "maven"
    ],
    git: [
        server: 'github.developer.allianz.io',
        credentialsId: 'git-token-credentials',
        protocol: 'https',
        branch: 'develop'
    ],
    ghe: [
        //uncomment next line to enable msg/title validation to be "AITC-123: msg"
        //commitMsgPattern: '[A-Z]{2,10}-[0-9]+:.*',
        commitMsgPattern: '.*',
        apiUrl: 'https://github.developer.allianz.io/api/v3',
        GitGroup: 'liucaissr',
        RepoName: 'name_repos_1102',
        checks: [
            // [name: "di/base-branch-validation", description: "requested (or skipped if previous failed)"],
            [name: 'scm/commit-validation', description: 'requested (or skipped if previous failed)', type: 'commit_validation'],
            [name: 'scm/pr-validation', description: 'requested (or skipped if previous failed)', type: 'prname_validation'],
            // [name: "scm/branchname-validation",description: "requested (or skipped if previous failed)", type: "branchname_validation"],
            // [name: "di/target-branch-validation",description: "requested (or skipped if previous failed)", type: "commit_validation"],
            // [name: "di/checkout-and-merge",description: "requested (or skipped if previous failed)", type: "commit_validation"],
            // [name: "di/merge-ready",description: "requested (or skipped if previous failed)", type: "commit_validation"]
        ]
    ]
]


def gConfig = job_config

def scripts = """

def lib = library identifier: 'BizDevOps_JSL@develop', retriever: modernSCM(
  [\$class: 'GitSCMSource',
   remote: 'https://github.developer.allianz.io/JEQP/BizDevOps-JSL.git',
   credentialsId: 'git-token-credentials'])

def customLib = library identifier: 'name_repos_1102_JSL@develop', retriever: modernSCM(
  [\$class: 'GitSCMSource',
   remote: 'https://github.developer.allianz.io/liucaissr/name_repos_1102_lib.git',
   credentialsId: 'git-token-credentials'])

def config = ${gConfig.inspect()}

def jslGeneral    = lib.de.allianz.bdo.pipeline.JSLGeneral.new()
def jslGit        = lib.de.allianz.bdo.pipeline.JSLGit.new()
def jslGhe        = lib.de.allianz.bdo.pipeline.JSLGhe.new()

def jslCustom     = customLib.de.allianz.NameRepos1102.new()

def manual_commit_sha

// for questions about this job ask mario akermann/tobias pfeifer from team pipeline

pipeline {
    agent { label '${job_config.job.agent}' }
    stages {
        stage('Prepare') {
            steps {
                echo "prepare"
                script {
                    jslGeneral.clean()
                    jslAppGeneral.prep(config, commit_sha)
                    jslAppGeneral.build(config, commit_sha, commits_url, pr_title)
                }
            }
        }
        stage('Checkout') {
            steps {
                echo "checkout"
                script {
                   jslGit.checkout(config, "liucaissr", "name_repos_1102", job_config.git.branch)
                }
            }
        }
        stage('Build') {
            steps {
                echo "Build"
                script {
                      jslCustom.build()
                }
            }
        }
        stage('Component Tests') {
            steps {
                echo "Component Tests"
                script {
                    jslCustom.componentTest()
                }
            }
        }
        stage('Integration Tests') {
            steps {
                echo "Integration Tests"
                script {
                     jslCustom.integrationTest()
                }
            }
        }
        stage('UAT Tests') {
            steps {
                echo "UAT Tests"
                script {
                        jslCustom.uatTest()
                }
            }
        }
        stage('Acceptance Tests') {
            steps {
                echo "Acceptance Tests"
                script {
                        jslCustom.acceptanceTest()
                }
            }
        }
        stage('Publish Artifacts') {
            steps {
                echo "Publish Artifacts"
                script {
                        jslCustom.publishArtifacts()
                }
            }
        }
        stage('Publish Results') {
            steps {
                echo "Publish Results"
                script {
                        junit allowEmptyResults: true, testResults: '**/surefire-reports/TEST-*.xml'
                }
            }
        }
    }
}

"""

def job = pipelineJob("${job_config.job.name}")

job.with {
       properties {
        triggers {
            genericTrigger {
                genericVariables {
                    genericVariable {
                        key('action')
                        value("\$.action")
                        expressionType('JSONPath') //Optional, defaults to JSONPath
                        regexpFilter('') //Optional, defaults to empty string
                        defaultValue('') //Optional, defaults to empty string
                    }
                    genericVariable {
                        key('commit_sha')
                        value("\$.pull_request.head.sha")
                        expressionType('JSONPath') //Optional, defaults to JSONPath
                        regexpFilter('') //Optional, defaults to empty string
                        defaultValue('') //Optional, defaults to empty string
                    }
                    genericVariable {
                        key('pr_number')
                        value("\$.number")
                        expressionType('JSONPath') //Optional, defaults to JSONPath
                        regexpFilter('') //Optional, defaults to empty string
                        defaultValue('') //Optional, defaults to empty string
                    }
                    genericVariable {
                        key('pr_title')
                        value("\$.pull_request.title")
                        expressionType('JSONPath') //Optional, defaults to JSONPath
                        regexpFilter('') //Optional, defaults to empty string
                        defaultValue('') //Optional, defaults to empty string
                    }
                    genericVariable {
                        key('commits_url')
                        value("\$.pull_request.commits_url")
                        expressionType('JSONPath') //Optional, defaults to JSONPath
                        regexpFilter('') //Optional, defaults to empty string
                        defaultValue('') //Optional, defaults to empty string
                    }
                    genericVariable {
                        key('srcBranch')
                        value("\$.pull_request.head.ref")
                        expressionType('JSONPath') //Optional, defaults to JSONPath
                        regexpFilter('') //Optional, defaults to empty string
                        defaultValue('') //Optional, defaults to empty string
                    }
                    genericVariable {
                        key('baseBranch')
                        value("\$.pull_request.base.ref")
                        expressionType('JSONPath') //Optional, defaults to JSONPath
                        regexpFilter('') //Optional, defaults to empty string
                        defaultValue('') //Optional, defaults to empty string
                    }
                }

                printContributedVariables(true)
                printPostContent(true)
                silentResponse(false)
                regexpFilterText("\$action")
                regexpFilterExpression("^(opened|reopened|synchronize)\$")
                token('4X2t52oEe6wGSkQtIdKrW2DLISDtRxht')
            }
        }
       }

    // authenticationToken('bBBIt5YOI2ecXqMccgIHxglwkapa31E3')

    definition {
        cps {
            script(scripts)
                sandbox()
        }
    }
}
