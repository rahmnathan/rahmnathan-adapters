pipeline {
    agent {
        kubernetes {
            yaml """
kind: Pod
metadata:
  name: jenkins-agent
spec:
  containers:
  - name: jnlp
    image: rahmnathan/inbound-agent
    imagePullPolicy: Always
    tty: true
    volumeMounts:
      - name: docker
        mountPath: /var/run/docker.sock
  volumes:
    - name: docker
      hostPath:
        path: '/var/run/docker.sock'
"""
        }
    }

    tools {
        maven 'Maven'
        jdk 'Java 21'
    }

    stages {
        stage('Checkout') {
            steps {
                script {
                    git 'https://github.com/rahmnathan/rahmnathan-adapters.git'
                }
            }
        }
        stage('Set Version') {
            steps {
                script {
                    PROJECT_VERSION = sh(
                            script: "mvn help:evaluate -Dexpression=project.version -q -DforceStdout",
                            returnStdout: true
                    ).trim()
                    env.NEW_VERSION = "${PROJECT_VERSION}.${BUILD_NUMBER}"
                    sh "mvn -DnewVersion='${NEW_VERSION}' versions:set"
                }
            }
        }
        stage('Tag') {
            steps {
                script {
                    sh 'git config --global user.email "rahm.nathan@protonmail.com"'
                    sh 'git config --global user.name "rahmnathan"'
                    sshagent(credentials: ['Github-Git']) {
                        sh 'mkdir -p /home/jenkins/.ssh'
                        sh 'ssh-keyscan  github.com >> ~/.ssh/known_hosts'
                        sh "mvn -Dtag=${NEW_VERSION} scm:tag"
                    }
                }
            }
        }
        stage('Unit Test') {
            steps {
                sh 'mvn test'
            }
        }
        stage('Package & Deploy Jar to Artifactory') {
            steps {
                script {
                    server = Artifactory.server 'Artifactory'
                    rtMaven = Artifactory.newMavenBuild()
                    rtMaven.tool = 'Maven'
                    rtMaven.deployer releaseRepo: 'rahmnathan-libraries', snapshotRepo: 'rahmnathan-libraries', server: server

                    buildInfo = Artifactory.newBuildInfo()

                    rtMaven.run pom: 'pom.xml', goals: 'install -DskipTests', buildInfo: buildInfo
                }
            }
        }
    }
}