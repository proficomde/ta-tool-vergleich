numberOfRuns = 10

pipeline {
  agent {
    label 'Docker'
  }

  stages {
    stage('Selenium Java') {
      steps {
        script {     
          docker.image("maven:latest").inside("-v /var/run/docker.sock:/var/run/docker.sock -u 0:0 --privileged --network host") {
            dir("selenium/java") {
              sh "mvn test-compile"
              for (int i = 0; i < numberOfRuns; i++) {
                try {
                  sh "mvn clean test"
                } catch (err) {
                  // nothing to do here
                }
              }
              archiveArtifacts allowEmptyArchive: true, artifacts: 'timings.*', followSymlinks: false
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
                try {
                  sh "npm test"
                } catch (err) {
                  // nothing to do here
                }
              }  
              archiveArtifacts allowEmptyArchive: true, artifacts: 'timings.*', followSymlinks: false            
            }
          }
        }
      }
    }
  }

}
