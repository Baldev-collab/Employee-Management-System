
pipeline {
    agent any

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Check Java and Maven') {
            steps {
                bat 'java -version'
                bat 'mvn -version'
            }
        }

        stage('Build') {
            steps {
                bat 'mvn -B clean compile'
            }
        }

        stage('Test') {
            steps {
                bat 'mvn -B test'
            }
        }

        stage('Package') {
            steps {
                bat 'mvn -B package'
            }
        }
    }

    post {
        success {
            echo 'Employee Management System build succeeded!'
        }
        failure {
            echo 'Build failed. Check Jenkins Console Output.'
        }
    }
}
