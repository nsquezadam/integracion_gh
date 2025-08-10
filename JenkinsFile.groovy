pipeline {
    agent any

    tools {
        jdk 'jdk-17'
        maven 'maven-3.9'
    }

    options {
        timestamps()
        buildDiscarder(logRotator(numToKeepStr: '10'))
    }

    stages {
        stage('Checkout Código') {
            steps {
                git branch: 'main',
                        url: 'https://github.com/nsquezadam/integracion_gh.git',
                        credentialsId: 'nsquezadam'
            }
        }

        stage('Compilar y Analizar Código') {
            steps {
                bat 'mvn clean compile'
                bat 'mvn spotbugs:check'
            }
        }

        stage('Pruebas Unitarias') {
            steps {
                bat 'mvn test'
            }
            post {
                always {
                    junit '**/target/surefire-reports/*.xml'
                }
            }
        }

        stage('Empaquetar Artefacto') {
            steps {
                bat 'mvn package'
            }
            post {
                success {
                    archiveArtifacts artifacts: 'target/*.jar', fingerprint: true
                }
            }
        }

        stage('Deployment') {
            steps {
                echo 'Desplegando localmente'
                // Copia el JAR generado
                bat 'if not exist C:\\deploy\\mi-app mkdir C:\\deploy\\mi-app'
                bat 'copy /Y target\\*.jar C:\\deploy\\mi-app\\'
            }
        }

        stage('Iniciar aplicación') {
            steps {
                echo 'Iniciando aplicación en segundo plano'
                bat 'start "" java -jar C:\\deploy\\mi-app\\integracion_gh-1.0-SNAPSHOT.jar'
            }
        }
    }

    post {
        always {
            echo "Pipeline finalizado con estado: ${currentBuild.currentResult}"
        }
    }
}
