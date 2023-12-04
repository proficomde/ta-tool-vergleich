pipeline {
  agent {
    label 'Docker'
  }

  stages {
    stage('Selenium Java') {
      steps {
        script {     
          sh "id"     
          docker.image("maven:latest").inside("-v /var/run/docker.sock:/var/run/docker.sock -u 0:0 --privileged --network host") {
          dir("selenium/java") {
            sh "id"
            sh "mvn test-compile"
            for 
            sh "mvn clean test"
            }
          }
        }
      }
    }
  }
}
