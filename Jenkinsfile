pipeline {
    agent none
    stages {
        stage('Build and Test') {
            parallel {
                stage('Config-Server') {
                    agent {
                        docker {
                            image 'eclipse-temurin:21-jdk'
                        }
                    }

                    steps {
                        dir('config-server') {
                            sh 'chmod +x mvnw' // for permission denied since the executable flags are not commited into the repo
                            sh './mvnw clean package -DskipTests' // to skip tests while building since we are building parallelly and some services depend on eureka server and config server
                        }

                        stash name: 'config-server-jar', includes: 'config-server/target/*.jar'
                    }
                }

                stage('Eureka-Server') {
                    agent {
                        docker {
                            image 'eclipse-temurin:21-jdk'
                        }
                    }

                    steps {
                        dir('eureka-server') {
                            sh 'chmod +x mvnw'
                            sh './mvnw clean package -DskipTests'
                        }

                        stash name: 'eureka-server-jar', includes: 'eureka-server/target/*.jar'

                    }
                }

                stage('Api-Gateway') {
                    agent {
                        docker {
                            image 'eclipse-temurin:21-jdk'
                        }
                    }

                    steps {
                        dir('api-gateway') {
                            sh 'chmod +x mvnw'
                            sh './mvnw clean package -DskipTests'
                        }

                        stash name: 'api-gateway-jar', includes: 'api-gateway/target/*.jar'

                    }
                }

                stage('Product-Service') {
                    agent {
                        docker {
                            image 'eclipse-temurin:21-jdk'
                        }
                    }

                    steps {
                        dir('product-service') {
                            sh 'chmod +x mvnw'
                            sh './mvnw clean package -DskipTests'
                        }

                        stash name: 'product-service-jar', includes: 'product-service/target/*.jar'

                    }
                }

                stage('User-Service') {
                    agent {
                        docker {
                            image 'eclipse-temurin:21-jdk'
                        }
                    }

                    steps {
                        dir('user-service') {
                            sh 'chmod +x mvnw'
                            sh './mvnw clean package -DskipTests'
                        }

                        stash name: 'user-service-jar', includes: 'user-service/target/*.jar'

                    }
                }

                stage('Cart-Service') {
                    agent {
                        docker {
                            image 'eclipse-temurin:21-jdk'
                        }
                    }

                    steps {
                        dir('cart-service') {
                            sh 'chmod +x mvnw'
                            sh './mvnw clean package -DskipTests'
                        }

                        stash name: 'cart-service-jar', includes: 'cart-service/target/*.jar'

                    }
                }

                stage('Order-Service') {
                    agent {
                        docker {
                            image 'eclipse-temurin:21-jdk'
                        }
                    }

                    steps {
                        dir('order-service') {
                            sh 'chmod +x mvnw'
                            sh './mvnw clean package -DskipTests'
                        }

                        stash name: 'order-service-jar', includes: 'order-service/target/*.jar'

                    }
                }
            }
        }

        stage('deploy') {
            agent any
            steps {
                unstash 'config-server-jar'
                unstash 'eureka-server-jar'
                unstash 'api-gateway-jar'
                unstash 'product-service-jar'
                unstash 'user-service-jar'
                unstash 'cart-service-jar'
                unstash 'order-service-jar'

                echo 'Build and Test stages were a success'

            }
        }
    }

    post {
        success {
            echo 'successfully executed CI/CD'
        }
        failure {
            echo 'failed to execute CI/CD'
        }
    }
}