pipeline {
  agent {
    label 'Docker'
  }

  stages {
    stage('Selenium Java') {
      steps {
        script {     
          sh "id"     
          docker.image("maven:latest").inside("-u 0:0 -v /run/podman/podman.sock:/var/run/docker.sock --privileged") {
          dir("selenium/java") {
            sh "id"
            sh "mvn test-compile"
            sh "mvn clean test"
            }
          }
        }
      }
    }
  }
}
