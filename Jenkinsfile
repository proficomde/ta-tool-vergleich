stage ("Selenium Java") {
 	agent(
        image: 'registry.lab.proficom.de/jenkins/jnlp-maven-agent:latest',
        tags: 'maven'
    ) {
      git branch: 'main', changelog: false, poll: false, url: 'https://git.lab.proficom.de/dhorn/testautomation-tooling.git'
      dir('selenium/java') {
      	sh 'mvn test-compile'
        sh 'mvn clean test'
      }
    } 
}