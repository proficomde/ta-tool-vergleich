numberOfRuns = 1

pipeline {
  agent {
    label 'Docker'
  }

  stages {
    stage ("Selenium") {
      stages {
        stage('Selenium Java') {
          steps {
            script {     
              docker.image("maven:latest").inside("-v /var/run/docker.sock:/var/run/docker.sock -u 0:0 --privileged --network host") {
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
          steps {
            script {     
              docker.image("maven:latest").inside("-v /var/run/docker.sock:/var/run/docker.sock -u 0:0 --privileged --network host") {
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
          steps {
            script {
              docker.image("node:21.3.0-bookworm").inside("-v /var/run/docker.sock:/var/run/docker.sock -u 0:0 --privileged --network host") {
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
          steps {
            script {
              docker.image("maven:latest").inside("-v /var/run/docker.sock:/var/run/docker.sock -u 0:0 --privileged --network host") {
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
      }
    }
  }
  post {
    always {
      cleanWs()
    }
  }
}
