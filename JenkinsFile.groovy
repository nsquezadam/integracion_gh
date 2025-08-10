pipeline {
    agent any

    tools {
        jdk 'jdk-17'
        maven 'maven-3.9'
    }

    options { timestamps() }

    stages {
        stage('Checkout') {
            steps {
                git branch: 'main',
                        url: 'https://github.com/nsquezadam/integracion_gh.git',
                        credentialsId: 'nsquezadam'
            }
        }

        stage('Build & Test') {
            steps {
                bat 'mvn -B -U clean test'
            }
        }

        stage('Package') {
            steps {
                bat 'mvn -B -DskipTests package'
                archiveArtifacts artifacts: 'target/*.jar', fingerprint: true
            }
        }

        stage('Deploy & Run (local)') {
            steps {
                bat '''
          if not exist C:\\deploy\\mi-app mkdir C:\\deploy\\mi-app
          copy /Y target\\*.jar C:\\deploy\\mi-app\\app.jar
          start "" java -jar C:\\deploy\\mi-app\\app.jar
        '''
            }
        }
    }

    post {
        always { echo "Estado: ${currentBuild.currentResult}" }
    }
}
