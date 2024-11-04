numberOfRuns = params.NUMBER_OF_RUNS?.isInteger() ? params.NUMBER_OF_RUNS as Integer : 10

nodeVersion = "20.15.1"
mvnVersion = "3.9.8"

DOCKER_HOME = "/var/run/docker.sock"

def buildNumber = env.BUILD_NUMBER as int
if (buildNumber >1) milestone (buildNumber -1)
milestone(buildNumber)


pipeline {
  parameters {
  booleanParam defaultValue: false, name: 'RUN_SELENIUM_JAVA'
  booleanParam defaultValue: false, name: 'RUN_SELENIUM_POP'
  booleanParam defaultValue: false, name: 'RUN_SELENIUM_JS'

  booleanParam defaultValue: false, name: 'RUN_PLAYWRIGHT_JAVA'
  booleanParam defaultValue: false, name: 'RUN_PLAYWRIGHT_JS'

  booleanParam defaultValue: false, name: 'RUN_CYPRESS_JS'

  string defaultValue: '100', name: 'NUMBER_OF_RUNS'



  }
  agent {
    label 'docker'
  }

  stages {
    stage ("Selenium") {
      stages {
        stage('Selenium Java') {
          when { expression { return params.RUN_SELENIUM_JAVA } } 
          steps {
            script {     
              docker.image("maven:${mvnVersion}").inside("-v ${DOCKER_HOME}:/var/run/docker.sock -u 0:0 --privileged --network host") {
                dir("selenium/java") {
                  sh "mvn test-compile"
                  for (int i = 0; i < numberOfRuns; i++) {
                    catchError(buildResult: 'SUCCESS', stageResult: 'UNSTABLE') {
                      sh "mvn clean test"
                    }
                  }
                  sh "cp timings.csv selenium-java.csv"
                  archiveArtifacts allowEmptyArchive: true, artifacts: 'selenium-java.csv', followSymlinks: false
                }
              }
            }
          }
        }
        stage('Selenium Java - Page Objects') {
          when { expression { return params.RUN_SELENIUM_POP } } 
          steps {
            script {     
              docker.image("maven:${mvnVersion}").inside("-v  ${DOCKER_HOME}:/var/run/docker.sock -u 0:0 --privileged --network host") {
                dir("selenium/java_PageObjectPattern") {
                  sh "mvn test-compile"
                  for (int i = 0; i < numberOfRuns; i++) {
                    catchError(buildResult: 'SUCCESS', stageResult: 'UNSTABLE') {
                      sh "mvn clean test"
                    } 
                  }
                  sh "cp timings.csv selenium-java-pop.csv"
                  archiveArtifacts allowEmptyArchive: true, artifacts: 'selenium-java-pop.csv', followSymlinks: false
                }
              }
            }
          }
        }
        stage('Selenium JavaScript') {
          when { expression { return params.RUN_SELENIUM_JS } } 
          steps {
            script {
              docker.image("node:${nodeVersion}-bookworm").inside("-v ${DOCKER_HOME}:/var/run/docker.sock -u 0:0 --privileged --network host") {
                dir("selenium/js") {
                  sh "npm ci"
                  for (int i = 0; i < numberOfRuns; i++) {
                    catchError(buildResult: 'SUCCESS', stageResult: 'UNSTABLE') {
                      sh "npm test"
                    } 
                  } 
                  sh "cp timings.csv selenium-js.csv" 
                  archiveArtifacts allowEmptyArchive: true, artifacts: 'selenium-js.csv', followSymlinks: false            
                }
              }
            }
          }
        }
      }
    }

    stage ("Playwright") {
      stages {
        stage('Playwright Java') {
          when { expression { return params.RUN_PLAYWRIGHT_JAVA } } 
          steps {
            script {
              docker.image("maven:${mvnVersion}").inside("-v ${DOCKER_HOME}:/var/run/docker.sock -u 0:0 --privileged --network host") {
                dir("playwright/java") {
                  sh 'mvn exec:java -e -D exec.mainClass=com.microsoft.playwright.CLI -D exec.args="install-deps"'
                  sh "mvn test-compile"

                  for (int i = 0; i < numberOfRuns; i++) {
                    catchError(buildResult: 'SUCCESS', stageResult: 'UNSTABLE') {
                      sh "mvn clean test"
                    } 
                  }
                  sh "cp timings.csv playwright-java.csv" 
                  archiveArtifacts allowEmptyArchive: true, artifacts: 'playwright-java.csv', followSymlinks: false            
                }
              }
            }
          }
        }
        stage('Playwright JavaScript') {
          when { expression { return params.RUN_PLAYWRIGHT_JS } } 
          steps {
            script {
              docker.image("node:${nodeVersion}-bookworm").inside("-v ${DOCKER_HOME}:/var/run/docker.sock -u 0:0 --privileged --network host") {
                dir("playwright/js") {
                  sh "npm ci"                  
                  sh "npx playwright install"
                  sh "npx playwright install-deps"
                  
                  for (int i = 0; i < numberOfRuns; i++) {
                    catchError(buildResult: 'SUCCESS', stageResult: 'UNSTABLE') {
                      sh "npm run test"
                    } 
                  } 
                  sh "cp timings.csv playwright-js.csv" 
                  archiveArtifacts allowEmptyArchive: true, artifacts: 'playwright-js.csv', followSymlinks: false            
                }
              }
            }
          }
        }
      }
    }
    stage ("Cypress") {
      stages {
        stage('Cypress JavaScript') {
          when { expression { return params.RUN_CYPRESS_JS} } 
          steps {
            script {
              docker.image("cypress/base:${nodeVersion}").inside("-v ${DOCKER_HOME}:/var/run/docker.sock -u 0:0 --privileged --network host") {
                dir("cypress/js") {
                  sh "npm ci"
                  for (int i = 0; i < numberOfRuns; i++) {
                    catchError(buildResult: 'SUCCESS', stageResult: 'UNSTABLE') {
                      sh "npm run test"
                    } 
                  } 
                  sh "cp timings.csv cypress-js.csv" 
                  archiveArtifacts allowEmptyArchive: true, artifacts: 'cypress-js.csv', followSymlinks: false            
                }
              }
            }
          }
        }
      }
    }
  }
  post {
    always {
      cleanWs()
    }
  }
}
