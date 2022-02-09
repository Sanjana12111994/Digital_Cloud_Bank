# Using Gatling for Coverage Testing and Load Testing

## Description
This directory holds the Gatling scala scripts used to simulate load to test the services defined in this project's scope.

- Firstly, we perform a coverage test that injects a single user to touch all (but one) of the defined service. 

- Second, a scenario is simulated that touches a handful of the services for a duration of 30 minutes with 50 connections/users. This results in roughly 20 API calls / second.

## Running the simulation

You can run the coverage test and the load test using the same script. Navigate to the,
- "./gatling-charts-highcharts-bundle-3.4.2/user-files/simulations/computerdatabase"

directory. 

Replace the IGW variable with the IP/DNS-port corresponding to your cluster's External IP. For a local minikube cluster this could be 
- 127.0.0.1
For an Amazon EKS cluster this could be 
- afc033ba1409146a1bf7531ecd5842ad-2083102065.us-west-1.elb.amazonaws.com

Inside the "BasicSimulation.scala" file follow the instructions to uncomment the line of code according to the test you want to simulate.
To run the test, use the following command,
- on MacOS
```sh
 $ gatling.sh 
```
- on Windows
```sh
 $ gatling.bat 
```