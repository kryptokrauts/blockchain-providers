pipeline {
    agent any
    environment {
        GITHUB_CREDS = credentials('GITHUB_CRED')
        MAVEN_OPTS="-Djava.security.egd=file:/dev/./urandom"
        CODECOV_TOKEN = credentials('ARKANE_CODECOV_TOKEN');
    }
    options {
        disableConcurrentBuilds()
        timeout(time: 15, unit: 'MINUTES')
    }
    stages {
        stage('Build') {
            steps {
                sh 'mvn -B -U clean install'
            }
            post {
                always {
                    junit '**/target/surefire-reports/*.xml'
                    jacoco(
                          execPattern: '**/target/*.exec',
                          classPattern: '**/target/classes',
                          sourcePattern: '**/src/main/java',
                          exclusionPattern: '**/src/test*'
                    )

                }
            }
        }
        stage('Reports') {
            steps {
                sh 'curl -s https://codecov.io/bash | bash -s -- -t $CODECOV_TOKEN'
            }
        }
    }
}