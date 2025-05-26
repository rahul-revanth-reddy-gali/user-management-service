pipeline {
    agent {
        node {
            label ''
            customWorkspace ''
        }
    }

    tools {
        jdk 'jdk-17'  // 👈 match the name you gave above
    }

    stages {
        stage('Build') {
            steps {
                sh 'mvn clean package -DskipTests'
            }
        }

        stage('Test') {
            steps {
                sh 'mvn test'
            }
        }

        stage('Archive Artifact') {
            steps {
                archiveArtifacts artifacts: 'target/*.jar', fingerprint: true
            }
        }
    }
}
