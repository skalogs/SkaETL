pipeline {
    agent any
    tools {
        maven 'apache-maven-3.5.2'
    }
    stages {
        stage('Build') {
            steps {
                withCredentials([usernamePassword(credentialsId: 'docker', passwordVariable: 'DOCKER_PASSWORD', usernameVariable: 'DOCKER_USERNAME')]) {
                  sh "docker login -u ${DOCKER_USERNAME} -p ${DOCKER_PASSWORD}"
                }
                sh 'mvn -Dmaven.test.failure.ignore=true clean install'
            }
            post {
                success {
                    junit 'target/surefire-reports/**/*.xml'
                }
            }
        }

        stage('Push Docker Image') {
            steps {
                sh 'mvn dockerfile:push'
            }
        }
    }
}
