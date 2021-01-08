pipeline {
    agent any
    tools {
        maven 'maven'
        jdk 'jdk11'
    }
    stages {
        stage ('Initialize') {
            steps {
                sh '''
                    echo "PATH = ${PATH}"
                    echo "M2_HOME = ${M2_HOME}"
                    dropdb -h localhost -U postgres app_store_test
                    psql -h localhost -c 'create database app_store_test;' -U postgres
                    psql -h localhost -d app_store_test -U postgres -p 5432 -a -q -f src/main/resources/db/migration/V1_0__appstore_create.sql
                    "curl -H 'Cache-Control: no-cache' https://raw.githubusercontent.com/fossas/fossa-cli/master/install.sh | bash"
                '''
            }
        }
        stage('Build') {
            steps {
                sh 'mvn -B -DskipTests clean package'
            }
        }
        stage('Test') {
            steps {
                sh 'mvn test'
            }
            post {
                always {
                    junit 'target/surefire-reports/*.xml'
                }
            }
        }
        stage('FOSSA') {
            steps {
                sh '''
                fossa init
                fossa analyze
                '''
            }
        }
    }
}
