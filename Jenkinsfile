pipeline {
  agent {
    label 'Docker'
  }

  stages {
    stage('Selenium Java') {
      script {
        docker.image("maven:latest").inside("-v /var/run/docker.sock:/var/run/docker.sock:ro --rm") {
          dir("selenium/java") {
            sh "mvn test-compile"
            sh "mvn clean test"
          }
        }
      }
    }
  }
}
