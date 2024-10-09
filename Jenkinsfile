pipeline {
    environment {
        repository = "kimdohee58/biday-jenkins" // Docker Hub user id/repo name
        registryCredential = 'dockerhub' // Jenkins credentialsId
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
                            bat 'git pull origin master'
                        }
                    }
                }
            }
        }

//         stage('Start Build Module') {
//             steps {
//                 script {
//                     dir('biday-msa-jenkins/backend') {
//                         bat '"C:\\Program Files\\Git\\bin\\bash.exe" ./buildModule.sh'
//                     }
//                 }
//             }
//         }
//
//         stage('Login to Docker Hub') {
//             steps {
//                 withCredentials([usernamePassword(credentialsId: registryCredential, usernameVariable: 'DOCKERHUB_USERNAME', passwordVariable: 'DOCKERHUB_PASSWORD')]) {
//                     bat "echo docker login -u %DOCKERHUB_USERNAME% -p %DOCKERHUB_PASSWORD%"
//                 }
//             }
//         }

        stage('Building Docker Images') {
            steps {
                script {
                    // Closure to build images
                    def buildImages = { dirPath, imageType ->
                        dir(dirPath) {
                            def dirs = new File(dirPath).listDirectories()
                            for (dir in dirs) {
                                def dockerfile = new File(dir, 'Dockerfile')
                                if (dockerfile.exists()) {
                                    def imageName = dir.name // Subdirectory name
                                    docker.build("${repository}/${imageType}/${imageName}:${BUILD_NUMBER}", "-f ${dockerfile.path} ${dir}")
                                }
                            }
                        }
                    }

                    // Build server and service images
                    buildImages("${env.WORKSPACE}/biday-msa-jenkins/backend/server", "server")
                    buildImages("${env.WORKSPACE}/biday-msa-jenkins/backend/service", "service")
                }
            }
        }

        stage('Deploy Images') {
            steps {
                script {
                    // Closure to push images
                    def pushImages = { dirPath, imageType ->
                        dir(dirPath) {
                            def dirs = new File(dirPath).listDirectories()
                            for (dir in dirs) {
                                def dockerfile = new File(dir, 'Dockerfile')
                                if (dockerfile.exists()) {
                                    def imageName = dir.name
                                    bat "docker push ${repository}/${imageType}/${imageName}:${BUILD_NUMBER}"
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
