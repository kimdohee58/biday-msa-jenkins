pipeline {
    environment {
        repository = "kimdohee58/biday-jenkins"
        registryCredential = 'dockerhub'
    }
    agent any

    stages {
        stage('Git Pull') {
            steps {
                script {
                    dir('biday-msa-jenkins') {
                        if (!fileExists('.git')) {
                            bat 'git clone https://github.com/kimdohee58/biday-msa-jenkins.git .'
                        } else {
                            bat 'git pull origin main'
                        }
                    }
                }
            }
        }

        stage('Start Build Module') {
            steps {
                script {
                    dir('biday-msa-jenkins/backend') {
                        bat '"C:\\Program Files\\Git\\bin\\bash.exe" ./buildModule.sh'
                    }
                }
            }
        }

        stage('Login to Docker Hub') {
            steps {
                withCredentials([usernamePassword(credentialsId: registryCredential, usernameVariable: 'DOCKERHUB_USERNAME', passwordVariable: 'DOCKERHUB_PASSWORD')]) {
                    bat "echo docker login -u %DOCKERHUB_USERNAME% -p %DOCKERHUB_PASSWORD%"
                }
            }
        }

        stage('Building Docker Images') {
            steps {
                script {
                    def buildImages = { dirPath, imageType ->
                        dir(dirPath) {
                            def output = bat(script: 'dir /b', returnStdout: true)
                            def dirs = output.readLines()
                            for (dir in dirs) {
                                if (fileExists("${dir}/Dockerfile")) {
                                    docker.build("${repository}/${imageType}/${dir}:${BUILD_NUMBER}", "-f ${dir}/Dockerfile ${dir}")
                                }
                            }
                        }
                    }

                    buildImages("${env.WORKSPACE}/biday-msa-jenkins/backend/server", "server")
                    buildImages("${env.WORKSPACE}/biday-msa-jenkins/backend/service", "service")
                }
            }
        }

        stage('Push Docker Images') {
            steps {
                script {
                    def pushImages = { dirPath, imageType ->
                        dir(dirPath) {
                            def output = bat(script: 'dir /b', returnStdout: true)
                            def dirs = output.readLines()
                            for (dir in dirs) {
                                if (fileExists("${dir}/Dockerfile")) {
                                    bat "docker tag ${repository}/${imageType}/${dir}:${BUILD_NUMBER} ${repository}:${dir}${BUILD_NUMBER}"
                                    bat "docker push ${repository}:${dir}${BUILD_NUMBER}"
                                }
                            }
                        }
                    }

                    pushImages("${env.WORKSPACE}/biday-msa-jenkins/backend/server", "server")
                    pushImages("${env.WORKSPACE}/biday-msa-jenkins/backend/service", "service")
                }
            }
        }

        stage('Delete Docker Images') {
            steps {
                script {
                    def deleteImages = { dirPath, imageType ->
                        dir(dirPath) {
                            def output = bat(script: 'dir /b', returnStdout: true)
                            def dirs = output.readLines()
                            for (dir in dirs) {
                                if (fileExists("${dir}/Dockerfile")) {
                                    bat "docker rmi ${repository}/${imageType}/${dir}:${BUILD_NUMBER}"
                                }
                            }
                        }
                    }

                    deleteImages("${env.WORKSPACE}/biday-msa-jenkins/backend/server", "server")
                    deleteImages("${env.WORKSPACE}/biday-msa-jenkins/backend/service", "service")
                }
            }
        }
    }

    post {
        always {
            echo 'I will always say Hello again!'
        }
    }
}