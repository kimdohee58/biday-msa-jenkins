pipeline {
    environment {
        repository = "kimdohee58/biday-jenkins"
        DOCKERHUB_CREDENTIALS = credentials('dockerhub')
    }
    agent any

    stages {
        stage('git pull') {
            steps {
                script {
                    dir('biday-msa-jenkins') {
                        if (!fileExists('.git')) {
                            bat 'git clone https://github.com/kimdohee58/biday-msa-jenkins.git .'
                        } else {
                            bat 'git pull origin master'
                        }
                    }
                }
            }
        }

        stage('Start buildModule') {
            steps {
                script {
                    dir('biday-msa-jenkins/backend') {
                        bat '"C:\\Program Files\\Git\\bin\\bash.exe" ./buildModule.sh'
                    }
                }
            }
        }

        stage('Building Docker images') {
            steps {
                script {
                    // 서버 이미지 빌드
                    dir('biday-msa-jenkins/backend/server') {
                        def serverDirs = []
                        bat 'for /r %i in (Dockerfile) do @echo %i' > 'serverDockerfiles.txt'
                        serverDirs = readFile('serverDockerfiles.txt').split('\n')

                        for (file in serverDirs) {
                            if (file) {
                                def imageName = file.replace('\\', '/').split('/').last()  // 서브디렉토리 이름 추출
                                docker.build("${repository}/server/${imageName}:${BUILD_NUMBER}", "-f ${file} ${file.parent}")
                            }
                        }
                    }

                    // 서비스 이미지 빌드
                    dir('biday-msa-jenkins/backend/service') {
                        def serviceDirs = []
                        bat 'for /r %i in (Dockerfile) do @echo %i' > 'serviceDockerfiles.txt'
                        serviceDirs = readFile('serviceDockerfiles.txt').split('\n')

                        for (file in serviceDirs) {
                            if (file) {
                                def imageName = file.replace('\\', '/').split('/').last()  // 서브디렉토리 이름 추출
                                docker.build("${repository}/service/${imageName}:${BUILD_NUMBER}", "-f ${file} ${file.parent}")
                            }
                        }
                    }
                }
            }
        }

        stage('Login') {
            steps {
                bat "echo %DOCKERHUB_CREDENTIALS_PSW% | docker login -u %DOCKERHUB_CREDENTIALS_USR% --password-stdin"
            }
        }

        stage('Deploy our images') {
            steps {
                script {
                    // 서버 이미지 푸시
                    dir('biday-msa-jenkins/backend/server') {
                        def serverDirs = []
                        bat 'for /r %i in (Dockerfile) do @echo %i' > 'serverDockerfiles.txt'
                        serverDirs = readFile('serverDockerfiles.txt').split('\n')

                        for (file in serverDirs) {
                            if (file) {
                                def imageName = file.replace('\\', '/').split('/').last()
                                bat "docker push ${repository}/server/${imageName}:${BUILD_NUMBER}"
                            }
                        }
                    }

                    // 서비스 이미지 푸시
                    dir('biday-msa-jenkins/backend/service') {
                        def serviceDirs = []
                        bat 'for /r %i in (Dockerfile) do @echo %i' > 'serviceDockerfiles.txt'
                        serviceDirs = readFile('serviceDockerfiles.txt').split('\n')

                        for (file in serviceDirs) {
                            if (file) {
                                def imageName = file.replace('\\', '/').split('/').last()
                                bat "docker push ${repository}/service/${imageName}:${BUILD_NUMBER}"
                            }
                        }
                    }
                }
            }
        }

        // stage('Cleaning up') {
        //     steps {
        //         bat "docker rmi ${repository}/server:${BUILD_NUMBER}"
        //         bat "docker rmi ${repository}/service:${BUILD_NUMBER}"
        //     }
        // }
    }

    post {
        always {
            echo 'I will always say Hello again!'
        }
    }
}
