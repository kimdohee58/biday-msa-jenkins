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
                            powershell 'git clone https://github.com/kimdohee58/biday-msa-jenkins.git .'
                        } else {
                            powershell 'git pull origin main'
                        }
                    }
                }
            }
        }

        stage('Start Build Module') {
            steps {
                script {
                    dir('biday-msa-jenkins/backend') {
                        powershell './buildModule.sh'
                    }
                }
            }
        }
        
        stage('Login to Docker Hub') {
            steps {
                withCredentials([usernamePassword(credentialsId: registryCredential, usernameVariable: 'DOCKERHUB_USERNAME', passwordVariable: 'DOCKERHUB_PASSWORD')]) {
                    powershell 'docker login -u $env:DOCKERHUB_USERNAME -p $env:DOCKERHUB_PASSWORD'
                }
            }
        }

        stage('Building Docker Images') {
            steps {
                script {
                    def buildImages = { dirPath, imageType ->
                        dir(dirPath) {
                            def output = powershell(script: 'Get-ChildItem -Directory', returnStdout: true)
                            def dirs = output.readLines()
                            for (dir in dirs) {
                                def dockerfilePath = "${dir}/Dockerfile"
                                if (fileExists(dockerfilePath)) {
                                    // Extracting just the directory name for the tag
                                    def imageName = dir // This is the last part of the path
                                    powershell "docker build -t biday-jenkins/${imageName}:${BUILD_NUMBER} -f ${dockerfilePath} ."
                                    echo "Built image: ${repository}/${imageName}:${BUILD_NUMBER}"
//                                     powershell "docker build -t ${repository}/${dir}:${BUILD_NUMBER} ."
// //                                     docker.build("${repository}/${imageType}/${dir}:${BUILD_NUMBER}", "-f ${dir}/Dockerfile ${dir}")
//                                     echo "Built image: ${repository}/${imageType}/${dir}:${BUILD_NUMBER}"
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
                            def output = powershell(script: 'Get-ChildItem -Directory', returnStdout: true).trim()
                            def dirs = output.readLines()
                            for (dir in dirs) {
                                // Define the image name and tag here
                                def imageName = "${repository}/${dir}:${BUILD_NUMBER}"
                                powershell "docker tag docker-jenkins/${dir} ${repository}:${dir}"
                                def pushCommand = "docker push -a ${repository}:${dir}"
                                echo "Executing push command: ${pushCommand}" // Print the push command
                                powershell pushCommand
                                echo "Pushed image: ${imageName}"
                            }
                        }
                    }

                    pushImages("${env.WORKSPACE}/biday-msa-jenkins/backend/server", "server")
                    pushImages("${env.WORKSPACE}/biday-msa-jenkins/backend/service", "service")
                }
            }
        }


//         stage('Push Docker Images') {
//             steps {
//                 script {
//                     def pushImages = { dirPath, imageType ->
//                         dir(dirPath) {
//                             def output = powershell(script: 'Get-ChildItem -Directory', returnStdout: true).trim()
//                             def dirs = output.readLines()
//                             for (dir in dirs) {
//                                 powershell "docker push ${repository}:/${dir}"
//                                 echo "Pushed image: ${repository}:/${dir}"
// //                                 powershell "docker push ${repository}/${dir}:${BUILD_NUMBER}"
// //                                 echo "Pushed image: ${repository}/${dir}:${BUILD_NUMBER}"
//                             }
//                         }
//                     }
//
//                     // Push images from the specified directories
//                     pushImages("${env.WORKSPACE}/biday-msa-jenkins/backend/server", "server")
//                     pushImages("${env.WORKSPACE}/biday-msa-jenkins/backend/service", "service")
//                 }
//             }
//         }

//         stage('Push Docker Images') {
//             steps {
//                 script {
//                     def pushImages = { dirPath, imageType ->
//                         dir(dirPath) {
//                             def output = powershell(script: 'Get-ChildItem -Directory', returnStdout: true).trim()
//                             def dirs = output.readLines()
//                             for (dir in dirs) {
//                                 def dockerfilePath = "${dir}/Dockerfile"
//                                 if (fileExists(dockerfilePath)) {
//                                     powershell "docker tag ${repository}/${dir}:${BUILD_NUMBER} kimdohee58/${repository}:${dir}"
//                                     powershell "docker push ${repository}/${dir}:${BUILD_NUMBER}"
// //                                     powershell "docker push ${repository}/${imageType}/${dir}:${BUILD_NUMBER}"
//                                     echo "docker push ${repository}/${imageType}/${dir}:${BUILD_NUMBER}"
//                                 }
//                             }
//                         }
//                     }
//
//                     pushImages("${env.WORKSPACE}/biday-msa-jenkins/backend/server", "server")
//                     pushImages("${env.WORKSPACE}/biday-msa-jenkins/backend/service", "service")
//                 }
//             }
//         }
    }

    post {
        always {
            echo 'I will always say Hello again!'
        }
    }
}
