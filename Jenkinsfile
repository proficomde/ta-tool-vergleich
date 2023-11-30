pipeline {
  agent {
    label 'Docker'
  }

  stages {
    stage('Selenium Java') {
      steps {
        script {     
          sh "id"     
          docker.image("maven:latest").inside("-v /run/podman/podman.sock:/var/run/docker.sock --privileged --entrypoint=/bin/bash") {
          dir("selenium/java") {
            sh "mvn test-compile"
            sh "mvn clean test"
            }
          }
        }
      }
    }
  }
}
