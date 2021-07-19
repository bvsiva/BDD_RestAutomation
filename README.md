# BDD_RestAutomation

TL-challenge
automation challenge for TL
Objective :
Develop a BDD framework to test sample REST API as TL Challenge

Tools / libraries used/ to be installed :
1.	Java - Java 1.8 or higher
2.	Rest Assured - 4.3.0 - specified in POM.xml
3.	Cucumber - to be installed from marketplace in ecllipse/IntelliJIDEAD
4.	JUnit - 4.12 - specified in POM.xml
5.	Maven - 3.6.3 or above

Steps to start :
1.	Clone / Download the project into your local
2.	Open the Command prompt and navigat to project location
3.	Execute the following Maven command's
        mvn clean :- To clean the maven repo
        mvn install :- To install the maven requirments
        mvn test -Dcucumber.filter.tags={"@init","@Regression"} :- To execute test scenarios based on tags


