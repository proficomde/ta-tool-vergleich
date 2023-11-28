stage ("Selenium Java") {
 	agent(
        image: 'registry.lab.proficom.de/jenkins/jnlp-maven-agent:latest',
        tags: 'maven'
    ) {
      git branch: 'main', changelog: false, poll: false, url: 'https://git.lab.proficom.de/dhorn/testautomation-tooling.git'
      dir('selenium/java') {
      	mvn test-compile
        mvn clean test
      }
    } 
}