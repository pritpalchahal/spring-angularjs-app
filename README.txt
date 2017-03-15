This application uses AngularJS for front-end and Spring-boot for back-end operations.

To run this application, Maven and Spring-boot command line tools need to be installed.

Execute the following command from terminal from within the top level directory:
	C:\PingApplication>mvn spring-boot:run
	
then open localhost:8080 in the web browser to use the application.

If it ask for username and password, use the following :
	username: user
	password: password

Application has only been tested on Windows 10 OS. 

Resolve maven dependency automatically: mvn dependency:resolve

Resolve maven dependency from .jar:

mvn install:install-file -Dfile=C:\Libraries\mysql-connector-java-6.0.2.jar 
-DgroupId=mysql -DartifactId=mysql-connector-java -Dversion=6.0.2 -Dpackaging=jar

Update mysql root password:
$ mysql -u root -p
mysql> USE mysql;
mysql> UPDATE user SET password=PASSWORD("NEW_PASSWORD") WHERE User='root';
mysql> FLUSH PRIVILEGES;
mysql> quit

Tools versions:
Spring framework: 1.4.1.RELEASE
Apache Maven: 3.3.9
Java: 1.8.0_121
MySql: 5.6.35
AngularJS: Angular1

OS: Windows 10 x64
