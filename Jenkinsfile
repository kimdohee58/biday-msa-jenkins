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
                    // Closure to push images
                    def pushImages = { dirPath, imageType ->
                        dir(dirPath) {
                            // Windows 명령어를 사용하여 디렉토리 목록 가져오기
                            def dirs = bat(script: 'for /d %i in (*) do @echo %i', returnStdout: true).trim().split('\n')
                            for (dir in dirs) {
                                def dockerfilePath = "${dir}/Dockerfile"
                                // Dockerfile이 존재하는지 확인
                                if (fileExists(dockerfilePath)) {
//                                     def imageName = dir.trim()
//                                     bat "docker push ${repository}/${imageType}/${imageName}:${BUILD_NUMBER}"
                                    bat "docker push ${repository}/${imageType}/${dir}:${BUILD_NUMBER}"
                                }
                            }
                        }
                    }

                    // Push server and service images
                    pushImages("${env.WORKSPACE}/biday-msa-jenkins/backend/server", "server")
                    pushImages("${env.WORKSPACE}/biday-msa-jenkins/backend/service", "service")
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
