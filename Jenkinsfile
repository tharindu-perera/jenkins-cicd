#!groovy
import groovy.json.JsonOutput

def author = ""
def DEPLOY_QA = 'qa'
def buildCause = ""
def BUILD_USER = ""
def SLACK_ACCESS_KEY = ""
def jen = ""

//TODO chnageset  ,  changelog, try catch bloc , send test summary, sonar summary ,
pipeline {
//    try{
    agent any
    options {
//        skipDefaultCheckout()
        buildDiscarder(logRotator(numToKeepStr: '1'))
    }

    // =============== stages====================
    stages {

        stage('preparation') {
            steps {
                script {
                    echo ">>getBuildUser>>>>>"
                    echo " git branch  ${env.GIT_BRANCH}  "
                    echo "${currentBuild.getBuildCauses()}"
                    echo "${currentBuild.buildCauses}" // same as currentBuild.getBuildCauses()
                    echo "${currentBuild.getBuildCauses('hudson.model.Cause$UserCause')}"
                    echo "${currentBuild.getBuildCauses('hudson.triggers.TimerTrigger$TimerTriggerCause')}"
                    // started by commit
                    echo "${currentBuild.getBuildCauses('jenkins.branch.BranchEventCause')}"
// started by timer
                    echo "${currentBuild.getBuildCauses('hudson.triggers.TimerTrigger$TimerTriggerCause')}"
// started by user
                    echo "${currentBuild.getBuildCauses('hudson.model.Cause$UserIdCause')}"
                    echo ">>getBuildUser> ENd >>>>"
                    // get build cause (time triggered vs. SCM change)
                    BUILD_USER = currentBuild.getBuildCauses()[0].shortDescription
                    echo "Current build was caused by: ${BUILD_USER}\n"

                }
            }
        }

        stage('build & test') {
            steps {
                notifySlack("qd","wed","wed")
                sh "./gradlew clean build test"
                step $class: 'JUnitResultArchiver', testResults: '**/TEST-*.xml'
            }

            post {
                failure {
                    echo 'build & test error'
                    slackSend channel: 'error',
                            color: 'good',
                            message: "*${currentBuild.currentResult}:* Job ${env.JOB_NAME} build ${env.BUILD_NUMBER} by ${BUILD_USER}\n More info at: ${env.BUILD_URL}"

                }
            }
        }

        stage('getting approval for qa release') {
            when { branch 'develop' }
            steps {
                echo 'getting approval for qa release'
                slackSend channel: 'qa-release-approval',
                        color: 'good',
                        message: "*${currentBuild.currentResult}:* Job ${env.JOB_NAME} build ${env.BUILD_NUMBER} by ${BUILD_USER}\n More info at: ${env.BUILD_URL}"

            }
        }

        stage('inform  build status to slack ') {
            steps {
                echo 'inform  build status to slack'
            }
        }
    }

    post { // these post steps will get executed at the end of build
        always {
            echo ' post outside stages always '
            sh "echo ${currentBuild.result}"
        }
        failure {
            echo ' post outside stages failure '
            sh "echo ${currentBuild.result}"
        }

    }
//    } catch (e) {
//       echo 'Error in pipeline'
//        slackSend channel: 'general',
//                color: 'good',
//                message: "Error in pipelin [${e.toString()}]"
//
//    }

}
def notifySlack() {
     withCredentials([string(credentialsId: 'slack-token', variable: 'st'), string(credentialsId: 'jen', variable: 'jenn')]) {
         script {
            sh "curl --location --request POST '$st' " +
                    "--header 'Content-Type: application/json' \n" +
                    "--data-raw '{\n" +
                    "  \"channel\": \"general\",\n" +
                    "  \"text\": \"Hello, world\"\n" +
                    "}'"
        }
    }

}
