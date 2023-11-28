stage ("Selenium Java") {
 	agent(
        image: 'registry.lab.proficom.de/jenkins/jnlp-maven-agent:latest',
        tags: 'maven'
    ) {
      checkout scm
      dir('selenium/java') {
      	mvn test-compile
        mvn clean test
      }
    } 
}