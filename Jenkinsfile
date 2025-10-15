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
              - name: docker
                image: docker:28.5.1-cli-alpine3.22
                command:
                - cat
                tty: true
                volumeMounts:
                - mountPath: "/var/run/docker.sock"
                  name: docker-socket
              volumes:
              - name: docker-socket
                hostPath:
                  path: "/var/run/docker.sock"
            '''
        }
    }

    stages {
        stage('Maven Build') {
            steps {
                container('maven'){
                sh 'pwd'
                sh 'ls -al'
                sh 'mvn -v'
                // sh 'mvn clean'
                sh 'mvn package --DskipTests'
                sh 'ls -al'
                sh 'ls -al ./target'
                }
            }
        }

        environment{
            DOCKER_IMAGES_NAME = 'whwjyj/department-service'
                }
        }

        stage('Docker Image build & Push'){
            steps{
                container('docker'){
                    script{
                        def buildNumber = "${env.BUILD_NUMBER}"

                        // 파이프라인 단계에서 환경 변수를 설정하는 역할을 한다.
                        withEnv(["DOCKER_IMAGE_VERSION=${buildNumber}"]){
                            sh 'docker -v'
                            sh 'echo $DOCKER_IMAGE_NAME'
                            sh 'echo $DOCKER_IMAGE_VERSION'
                        //     sh 'docker images whwjyj/department-service'
                        //     sh 'docker build --no-cache -t whwjyj/department-service:5.0'./
                        //     sh 'docker images whwjyj/department-service'
                        // }
                    }
                }
            }
        }
    }
}
