# Digital Banking Application (CMPT-756 Project)
Backend design for Digital Banking.

  - Microservice distributed system architecture pattern
  - Containerization of services enabled by Docker and Kubernetes
  - Infrastructure as Code cloud service from AWS CloudFormation

# Application details

  - It consists of 3 Main Entities - Customer, Account ,Transaction and db proxy service
  - 11 operations on thoses entities


Can be simulated through:
  - Minikube 
  - EKS


### Tech

Following are the requirements to run this project:

* AWS Personal Account 
* DynamoDB
* Docker
* Amazon EKS 
* Python 
* Flask 
* Gatling 

### Installation

Get started by run these few steps 


####  Clone the git repository
```sh
$ git clone https://github.com/scp-2020-sept-cmpt-756/term-project-t7-saturday.git
```
####  Build images for each service from ek8s folder
```sh
$ docker build -t <docker username>/cmpt756a1:latest a1
$ docker build -t <docker username>/cmpt756s1:latest s1
$ docker build -t <docker username>/cmpt756db:latest db
```
#### Run the images 
```sh
$ docker run --publish 30003:30003 --detach --name a1 <docker username>/cmpt756a1:latest
$ docker run --publish 30002:30002 --detach --name db <docker username>/cmpt756db:latest
$ docker run --publish 30000:30000 --detach --name s1 <docker username>/cmpt756s1:latest
```

#### Run the Cloud Formation script from IaC folder
```sh
$ aws cloudformation create-stack --stack-name db --template-body file://misc/cloudformationdynamodb.json
```

#### Start Cluster (EKS)
```sh
$ make -f eks.mak start
```

#### Change the EKS Context name and create Namespace
```sh
$ kubectl config rename-context iam-root-account@aws756.us-west-2.eksctl.io aws756
$ kubectl config use-context aws756
$ kubectl create ns cmpt756e4
$ kubectl config set-context aws756 --namespace=cmpt756e4
```

#### Install istio & Cluster Ingress
```sh
$ curl -L https://istio.io/downloadIstio | sh -
$ cd istio-1.8.0
$ export PATH=$PWD/bin:$PATH
$ istioctl install --set profile=demo
$ kubectl label namespace cmpt756e4 istio-injection=enabled
$ cd ..
$ kubectl -n istio-system get service istio-ingressgateway
```
#### Run your Docker containers
```sh
$ kubectl -n cmpt756e4 apply -f db/db.yaml
$ kubectl -n cmpt756e4 apply -f customer/customer.yaml
$ kubectl -n cmpt756e4 apply -f account/account.yaml
$ kubectl -n cmpt756e4 apply -f transaction/transaction.yaml
$ kubectl -n cmpt756e4 apply -f misc/service-gateway.yaml
```
#### Check your pods, services and deploymemnts
```sh 
$ make -f eks.mak ls
```
#### Perform operations using CURL Scripts in api.mak file.
```sh 
$ make -f api.mak <pseudo-target >
```
Note: The api.mak file requires some parameters to be set depending upon the service you are trying to run.

| api.mak psuedo-target | operation|
| ------ | ------ |
| ccustomer | create a customer |
| caccount | open an account for a specific customer |
| ctrans | perform a transaction (credit / debit) |
| apilogin | login as a specific customer  |
| apilogoff | logoff as a specific customer  |
| ucustomer | update a specific customer details |
| uaccount | update a specific customer's account details |
| raccount |Read a specific customer's account details |
| dcustomer | delete a specific customer details |
| daccount | Close an account of a specific customer |
| dtrans | Clear a transaction details (Memory Management Purpose) |



License
----

Project by SFU Grad Students 


**I hope you've enjoyed this innovative architectural and design pattern for digital banking **


   
