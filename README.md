# Find the best plan

For example, Microsoft Office 365 has different
plans called E1, F1, etc. Each plan has different features such as voice, email, archiving, etc. Now let's say
the user selects a set of features he/she wants.

The goal is to write code that finds the combination of plans that offers all selected features at the
lowest price. Note that in some cases, it will be just one plan, but in other cases, you will need 2 or more
plans to get all the features you want.
Note: The cost for each plan is positive ( > 0).

sample Input: plan.txt

PLAN1,100,voice,email
PLAN2,150,email,database,admin
PLAN3,125,voice,admin
PLAN4,135,database,admin

output the minimum price for a given feature set.
The app should accept 2 arguments
1st is the path to the plan file. 
2nd is the features needed (separated by commas (,) ).

### command Line execution
For example:
Java -jar app.jar c:\example1.txt email,voice,admin

Output:
225,PLAN1,PLAN3

Note: If no plans are found for the features provided, just output 0.


### Implemented solution
For further reference, please consider the following sections:

* Design and implemented a RESTFUL API using by Java Spring boot Microservice Application with Docker
* Added swagger Open API tool to generate the document 
	- Access via an example: http://localhost:8080/swagger-ui.html
* Application available on Docker Hub
	- Tag name: arafath07/plan-service:1.0
	- CMD : docker pull arafath07/plan-service:1.0
* Unit/Integration test cases covered to Controllers and Services

### How to run
#### command Line
	- mvn clean install
	- java -jar target\plan-service-1.0.jar sample.txt email,voice,admin
		sample response : Output: 225,PLAN1,PLAN3
#### swagger
	- mvn clean install (cmd)
	- java -jar target\plan-service-1.0.jar (cmd)
	- URL : http://localhost:8080/swagger-ui.html
