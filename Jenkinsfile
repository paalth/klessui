pipeline {
    agent any

    environment {
      REPO_NAME = 'github.com/paalth/klessui'
    }

    stages {
        stage('Build') {
            steps {
                withCredentials([[$class: 'UsernamePasswordMultiBinding', credentialsId: 'KLESS_DEST_REGISTRY_CREDENTIALS', usernameVariable: 'KLESS_DEST_REGISTRY_USERNAME', passwordVariable: 'KLESS_DEST_REGISTRY_PASSWORD']]) {
               	    echo 'Building..'
                    sh 'printenv | sort'
                    sh 'make'
                    echo 'Build complete'
		}
            }
        }

        stage('Test') {
            steps {
                echo 'Testing..'
                echo 'Tests TBD...'
                echo 'Test complete'
            }
        }

        stage('Deploy') {
            steps {
                echo 'Deploying....'
                echo 'Deploy kless UI'
                echo 'Deploy complete'
            }
        }
    }
}

