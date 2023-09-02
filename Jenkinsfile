pipeline {
    //指定任务在哪个集群节点上执行
    agent any
	//声明环境变量，方便后面使用
	environment {
	    key='value'

	}

    stages {
        stage('拉取git仓库代码') {
            steps {
                checkout([$class: 'GitSCM', branches: [[name: '*/master']], extensions: [], userRemoteConfigs: [[credentialsId: 'demo3', url: 'https://github.com/floppy888/demo3.git']]])
            }
        }
		stage('通过maven构建项目') {
            steps {
                sh 'mvn clean package -DskipTests'
            }
        }
		stage('通过sonarqube做代码质量测试') {
            steps {
                echo '代码质量检测-SUCCESS'
            }
        }
		stage('通过docker制作自定义镜像') {
            steps {
                sh '''mv ./target/*.jar ./docker/
                docker build -t ${JOB_NAME} ./docker/'''
            }
        }
		stage('将自定义镜像推送到仓库中') {
            steps {
                sh '''docker tag pipeline-demo:latest 192.168.180.11:5000/pipeline-demo:latest
                docker push 192.168.180.11:5000/pipeline-demo:latest'''
            }
        }
		stage('拷贝YAML文件到K8S') {
            steps {
                sshPublisher(publishers: [sshPublisherDesc(configName: 'k8s', transfers: [sshTransfer(cleanRemote: false, excludes: '', execCommand: '', execTimeout: 120000, flatten: false, makeEmptyDirs: false, noDefaultExcludes: false, patternSeparator: '[, ]+', remoteDirectory: '', remoteDirectorySDF: false, removePrefix: '', sourceFiles: 'pipline.yaml')], usePromotionTimestamp: false, useWorkspaceInPromotion: false, verbose: false)])
            }
        }
		stage('通过K8S部署服务') {
            steps {
                sh 'ssh root@192.168.180.11 kubectl apply -f /root/deploy/pipline.yaml'
            }
        }
    }
}