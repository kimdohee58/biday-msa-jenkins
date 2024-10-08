pipeline {
    environment {
        // repository = "kimdohee58/biday-jenkins" // 주석 처리된 부분
        imagename = "kimdohee58/biday-jenkins" // 현재 도커허브의 사용자 id/repo 이름
        registryCredential = 'dockerhub' // Jenkins에서 만든 credentialsId
        dockerImage = ''
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

        stage('Login') {
            steps {
                withCredentials([usernamePassword(credentialsId: registryCredential, usernameVariable: 'DOCKERHUB_USERNAME', passwordVariable: 'DOCKERHUB_PASSWORD')]) {
                    bat "echo ${DOCKERHUB_PASSWORD} | docker login -u ${DOCKERHUB_USERNAME} --password-stdin"
                }
            }
        }

        // 주석 처리된 Docker 이미지 빌드 및 푸시 단계
        /*
        stage('Building Docker images') {
            steps {
                script {
                    // 서버 이미지 빌드
                    dir('biday-msa-jenkins/backend/server') {
                        def serverDirs = new File("${env.WORKSPACE}/biday-msa-jenkins/backend/server").listFiles().findAll { it.name == 'Dockerfile' }
                        for (file in serverDirs) {
                            def imageName = file.parent.replace('\\', '/').split('/').last()  // 서브디렉토리 이름 추출
                            docker.build("${repository}/server/${imageName}:${BUILD_NUMBER}", "-f ${file} ${file.parent}")
                        }
                    }

                    // 서비스 이미지 빌드
                    dir('biday-msa-jenkins/backend/service') {
                        def serviceDirs = new File("${env.WORKSPACE}/biday-msa-jenkins/backend/service").listFiles().findAll { it.name == 'Dockerfile' }
                        for (file in serviceDirs) {
                            def imageName = file.parent.replace('\\', '/').split('/').last()  // 서브디렉토리 이름 추출
                            docker.build("${repository}/service/${imageName}:${BUILD_NUMBER}", "-f ${file} ${file.parent}")
                        }
                    }
                }
            }
        }

        stage('Deploy our images') {
            steps {
                script {
                    // 서버 이미지 푸시
                    dir('biday-msa-jenkins/backend/server') {
                        def serverDirs = new File("${env.WORKSPACE}/biday-msa-jenkins/backend/server").listFiles().findAll { it.name == 'Dockerfile' }
                        for (file in serverDirs) {
                            def imageName = file.parent.replace('\\', '/').split('/').last()
                            bat "docker push ${repository}/server/${imageName}:${BUILD_NUMBER}"
                        }
                    }

                    // 서비스 이미지 푸시
                    dir('biday-msa-jenkins/backend/service') {
                        def serviceDirs = new File("${env.WORKSPACE}/biday-msa-jenkins/backend/service").listFiles().findAll { it.name == 'Dockerfile' }
                        for (file in serviceDirs) {
                            def imageName = file.parent.replace('\\', '/').split('/').last()
                            bat "docker push ${repository}/service/${imageName}:${BUILD_NUMBER}"
                        }
                    }
                }
            }
        }
        */
    }

    post {
        always {
            echo 'I will always say Hello again!'
        }
    }
}
