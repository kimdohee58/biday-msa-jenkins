// https://velog.io/@revelation/Jenkins-pipeline-%EC%82%AC%EC%9A%A9%ED%95%B4%EB%B3%B4%EA%B8%B0
pipeline {
    agent any

    stages {
        stage('git pull') {
            steps {
                script {
                    dir('biday-msa-jenkins') {
                        if (!fileExists('.git')) {
                            sh 'git clone https://github.com/kimdohee58/biday-msa-jenkins.git .'
                        } else {
                            sh 'git pull'
                        }
                    }
                }
            }
        }

        stage('Start buildModule') {
            steps {
                script {
                    dir('biday-msa-jenkins/backend') {
                        // bash 대신 cmd를 사용할 경우, 명령어 수정
                        bat './buildModule.sh' // Windows에서 bat 사용
                    }
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
