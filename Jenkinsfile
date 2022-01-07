pipeline {
    agent any
    environment {
        GITHUB_CREDS = credentials('GITHUB_CRED')
        MAVEN_OPTS="-Djava.security.egd=file:/dev/./urandom"
        CODECOV_TOKEN = credentials('ARKANE_CODECOV_TOKEN');
    }
    tools {
        jdk "JDK 17"
        maven 'apache-maven-3.8.4'
    }
    options {
        disableConcurrentBuilds()
        timeout(time: 15, unit: 'MINUTES')
    }
    stages {
        stage('Build') {
            steps {
                sh 'mvn -v'
                sh 'mvn -B -U -DjvmArgs="-Djdk.tls.client.protocols=\"TLSv1,TLSv1.1,TLSv1.2\"" clean deploy'
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
        stage('Record Coverage') {
            when { branch 'develop' }
            steps {
                script {
                    currentBuild.result = 'SUCCESS'
                 }
                step([$class: 'MasterCoverageAction', scmVars: [GIT_URL: env.GIT_URL]])
            }
        }
        /*
        stage('PR Coverage to Github') {
            when { allOf {not { branch 'master' }; expression { return env.CHANGE_ID != null }} }
            steps {
                script {
                    currentBuild.result = 'SUCCESS'
                 }
                step([$class: 'CompareCoverageAction', publishResultAs: 'statusCheck', scmVars: [GIT_URL: env.GIT_URL]])
            }
        }
        */
    }
}
