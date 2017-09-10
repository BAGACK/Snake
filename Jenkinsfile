pipeline {
   agent any
   tools {
      maven 'maven3.5.0'
      jdk 'jdk8'
   }
   stages {
      stage ('Initialization') {
          steps {
              echo 'Preparing for build'
          }
      }
      
      stage ('Build') {
          steps {
              sh 'mvn clean install' 
              archiveArtifacts artifacts: '**/target/*.jar', fingerprint: true
          }
          post {
              failure {
                  mail to: 'mep_eisen@web.de', subject: 'build of Snake failed', body: 'build of Snake failed'
              }
          }
      }
      
      stage ('Upload') {
        steps {
        	script {
        		env.BUILDTYPE = readPomVersion().endsWith("-SNAPSHOT") ? "snapshots" : "releases";
        	}
            sh "/srv/hudson/upload_game.sh ${env.BUILDTYPE} MGSnake-Plugin/target 1 Snake MGSnake-Plugin"
      	}
      }
      
      stage ('Deploy') {
          when {
              expression {
                  currentBuild.result == null || currentBuild.result == 'SUCCESS'
              }
          }
          steps {
              sh 'mvn -Deisenschmiede.deployment=true -Dmaven.test.skip=true -DskipTests deploy'
          }
          post {
              failure {
                  mail to: 'mep_eisen@web.de', subject: 'deploy of Snake failed', body: 'deploy of Snake failed'
              }
          }
      }
   }
}


def readPomVersion() {
	// Reference: http://stackoverflow.com/a/26514030/1851299
	sh "mvn --quiet --non-recursive -Dexec.executable='echo' -Dexec.args='\${project.version}' org.codehaus.mojo:exec-maven-plugin:1.3.1:exec > pom.project.version.txt"
	pomProjectVersion = readFile('pom.project.version.txt').trim()
	sh "rm -f pom.project.version.txt"
	echo "Current POM version: ${pomProjectVersion}"
	return pomProjectVersion
}