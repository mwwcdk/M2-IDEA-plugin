// Jenkinsfile (Declarative Pipeline)
pipeline {
    agent { docker 'maven:3.3.3' }
    stages {
        stage('Checkout') {
            steps {
                // 使用 HTTPS 协议（需 Token）
                git(
                    url: 'https://github.com/mwwcdk/M2-IDEA-plugin.git',
                    credentialsId: 'GitHubAccount',  // 替换为你的凭证 ID
                    branch: 'master'
                )
            }
        }
        stage('build') {
            steps {
                sh 'mvn --version'
            }
        }
    }
}