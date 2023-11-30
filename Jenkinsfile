pipeline {
  agent {
    label 'Docker'
  }
  tools {
    dockerTool 'alias'
  }

  stages {
    stage('Selenium Java') {
      steps {
        script {
          sh 'echo "alias docker=podman" >> .bashrc'
          
          docker.image("maven:latest").inside("-v /var/run/docker.sock:/var/run/docker.sock --rm") {
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
