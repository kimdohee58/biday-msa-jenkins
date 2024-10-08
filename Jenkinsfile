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
                        def serverDirs = findFiles(glob: '**/Dockerfile')
                        for (file in serverDirs) {
                            def imageName = file.parent.replace('\\', '/').split('/').last()  // 서브디렉토리 이름 추출
                            docker.build("${repository}/server/${imageName}:${BUILD_NUMBER}", "-f ${file} ${file.parent}")
                        }
                    }

                    // 서비스 이미지 빌드
                    dir('biday-msa-jenkins/backend/service') {
                        def serviceDirs = findFiles(glob: '**/Dockerfile')
                        for (file in serviceDirs) {
                            def imageName = file.parent.replace('\\', '/').split('/').last()  // 서브디렉토리 이름 추출
                            docker.build("${repository}/service/${imageName}:${BUILD_NUMBER}", "-f ${file} ${file.parent}")
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
                        def serverDirs = findFiles(glob: '**/Dockerfile')
                        for (file in serverDirs) {
                            def imageName = file.parent.replace('\\', '/').split('/').last()
                            bat "docker push ${repository}/server/${imageName}:${BUILD_NUMBER}"
                        }
                    }

                    // 서비스 이미지 푸시
                    dir('biday-msa-jenkins/backend/service') {
                        def serviceDirs = findFiles(glob: '**/Dockerfile')
                        for (file in serviceDirs) {
                            def imageName = file.parent.replace('\\', '/').split('/').last()
                            bat "docker push ${repository}/service/${imageName}:${BUILD_NUMBER}"
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






// https://velog.io/@dohyunkim12/Jenkins-Syntax-Scripted-vs-Declarative
/*
node {
    def version = "${params.majorVersion}.${params.minorVersion}.${params.hotfixVersion}"
    def branch = "master"
    def dockerImgRemote = "${params.dockerImageRepo}"
    def dockerUser = "${params.dockerUser}"
    def dockerPassword = "${params.dockerPassword}"
    def publishUrl = "http://${params.publishHost}:${params.publishPort}${params.publishDir}"
    def repoUser = "${params.repoUser}"
    def repoPassword = "${params.repoPassword}"


    switch(distributionType){
        case 'integrated':
            BuildJar(version, branch)
            UploadJar(version, publishUrl, repoUser, repoPassword)
            BuildImg(version)
            UploadImg(version, dockerImgRemote, dockerUser, dockerPassword)
            break
        case 'build-jar':
            BuildJar(version, branch)
            break
        case 'build-and-upload-jar':
            BuildJar(version, branch)
            UploadJar(version, publishUrl, repoUser, repoPassword)
            break
        case 'build-img':
            BuildJar(version, branch)
            BuildImg(version)
            break
        case 'build-and-upload-img':
            BuildJar(version, branch)
            BuildImg(version)
            BuildImg(version)
            UploadImg(version, dockerImgRemote, dockerUser, dockerPassword)
            break
        default:
            break
    }
}

void BuildJar(version, branch){
    stage('Build Jar') {
        echo 'Git fetching...'
        sh 'git fetch --all'
//        sh 'git reset --hard origin/master'
        sh "git reset --hard origin/${branch}"
//        sh "git pull origin ${branch}"

        echo 'Getting Commit ID...'
        commitId = sh(returnStdout: true, script: "git log | head -1 | cut -b 7-15")
        commitId = commitId.substring(1)

        echo 'Building...'
        sh 'chmod +x ./gradlew'
        sh "./gradlew clean build jenkins -PbuildVersion=${version} -PcommitId=${commitId}"
    }
}

void UploadJar(version, publishUrl, repoUser, repoPassword) {
    stage('Upload Jar') {
        sh "./gradlew publish -PbuildVersion=${version} -PpublishUrl=${publishUrl} -PrepoUser=${repoUser} -PrepoPassword=${repoPassword}"
    }
}

void BuildImg(version) {
    stage('Build Docker img') {
        echo 'Building img...'
        sh "sudo docker build --tag super-app-server:${version} --build-arg version=${version} ."
    }
}

void UploadImg(version, dockerImgRemote, dockerUser, dockerPassword) {
    stage('Upload Docker img') {
        //////////////////////////////////////// testing docker img (img run)
        sh "sudo docker ps -a"
//        sh 'sudo docker stop $(sudo docker ps -a -q)'
//        sh 'sudo docker rm $(sudo docker ps -a -q)'
        sh "sudo docker run --name super-app-server-test -d -p 8888:8888 super-app-server:${version}"
        sh "sudo docker ps | grep super-app-server"
        sh 'sudo docker stop $(sudo docker ps -a -q)'
        sh 'sudo docker rm $(sudo docker ps -a -q)'
        ////////////////////////////////////////

        echo 'Uploading img...'
        sh "sudo docker login -u ${dockerUser} -p ${dockerPassword}"
        sh "sudo docker tag super-app-server:${version} ${dockerImgRemote}:${version}"
        sh "sudo docker tag super-app-server:${version} ${dockerImgRemote}:latest"
        sh "sudo docker push dohyunkim12/super-app-server:${version}"
        sh "sudo docker push dohyunkim12/super-app-server:latest"
    }
} */
