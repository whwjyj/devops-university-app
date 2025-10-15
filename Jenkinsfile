pipeline {
    agent {
        kubernetes {
            yaml '''
            apiVersion: v1
            kind: Pod
            metadata:
              name: jenkins-agent
            spec:
              containers:
              - name: maven
                image: maven:3.9.9-eclipse-temurin-21-alpine
                command:
                - cat
                tty: true
            '''
        }
    }

    stages {
        stage('Maven Build') {
            steps {
                sh 'pwd'
                sh 'ls -al'
                sh 'mvn clean'
            }
        }
    }
}
