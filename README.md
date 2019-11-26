# Vancouver Racquet Club Ladder System

## CMPT 373 - Team Alpha
------------------------

This project was developed for the non-profit society, the Vancouver Racquets Club (VRC), as a part of CMPT373: Software Development Methods. It is licensed under the Berkeley Software Distribution (BSD) license.

The goal of the system is to modernize the weekly doubles ladder system of VRC through an easy-to-use, platform-independent user interface.

The project is split into two parts:
    1. The front-end: Website/User Interface (JavaScript)
    2. The back-end: Rest API, Business Logic and Database (Java)   
These are the two folders in the root folder of the project.

------------
### Frontend

#### Instructions (For Ubuntu 14.04):
1) Start a terminal session in the frontend directory (\<project-dir\>/frontend)


2) Install Node:

	Check Version:
    	nodejs --version

	If version is above 5.X.X go to step 3

	Delete outdated nodejs version if older version exist:
    	sudo apt-get remove --purge nodejs

	Install Node:
    	curl -sL https://deb.nodesource.com/setup_5.x | sudo -E bash -
    	sudo apt-get install nodejs
    	sudo ln -s /usr/bin/nodejs /usr/bin/node


3) Install dependencies:

    	sudo apt-get install npm
    	sudo apt-get install libvips
    	npm install

4) Start local development server (If you want to use it locally):

    	npm run dev

OR

5) Run the production build (If you want to host it online):

    	npm run build
    	
6) Your default browser will open. Wait until the command line process finishes, then refresh the page in your default browser.

#### Instructions (For OS X):
1) Start a terminal session in the frontend directory (\<project-dir\>/frontend)

2) Download dependencies:

    	brew install vips \
    	--with-webp \
    	--without-pango \
    	--without-python \
    	--without-pygobject3 \
    	--with-graphicsmagick

    	npm install

3) Start local development server (If you want to use it locally):

    	npm run dev

OR

4) Run the production build (If you want to host it online):

    	npm run build
    	
5) Your default browser will open. Wait until the command line process finishes, then refresh the page in your default browser.

#### Citations:
All webpack settings, and redux settings are from various guides online. Below is a list of online resources that were read or used.
- https://developer.mozilla.org/en/docs/Web/API/Window/localStorage
- https://github.com/lodash/lodash/wiki/FP-Guide
- https://github.com/JedWatson/classnames
- https://github.com/css-modules/css-modules
- https://github.com/peterramsing/lost/wiki/Installation
- https://github.com/reactjs/react-redux/blob/master/docs/api.md#connectmapstatetoprops-mapdispatchtoprops-mergeprops-options
- https://github.com/fmoo/react-typeahead
- https://github.com/reactjs/reselect
- http://redux-form.com/5.2.3/#/api/reduxForm?_k=cvo2xg
- https://lodash.com/docs
- https://github.com/acdlite/redux-actions
- https://github.com/reactjs/react-redux/blob/master/docs/api.md#connectmapstatetoprops-mapdispatchtoprops-mergeprops-options
- http://flowtype.org/
- https://github.com/zalmoxisus/redux-devtools-extension
- https://github.com/gaearon/redux-thunk
- https://github.com/zalmoxisus/redux-devtools-extension
- https://github.com/zippyui/react-button
- https://blog.risingstack.com/the-react-way-getting-started-tutorial/
- http://paletton.com/#uid=13L0u0kllll5wtvdqpqtghgKwdb

#### Directory Structure Explanation
frontend<br />
----config/webpack : <br />
--------contrib : <br />
--------plugin : <br />
----entry : <br />
----mock : <br />
----src : <br />
--------action : <br />
--------api : <br />
--------component : <br />
------------button : <br />
------------create-team : <br />
------------heading : <br />
------------ladder : <br />
------------login : <br />
------------signup : <br />
--------config : <br />
--------images : <br />
--------layout : <br />

-----------
### Backend

#### Build Configurations:

In order to support different runtime configurations for development/testing and production use, there are different gradle build configurations.

To switch between build configurations, see Build/Run Instructions

1) debug: for developing/testing things locally using a local in-memory database

2) production-debug: a persistent database for development purposes when developers want to experiment with large, persistent amounts of data, or experiment with making a change in a similar environment to production without risking breaking anything in production

3) production: a persistent database that the client (vrc) will use to store their actual ladder rankings.

#### Build/Run Instructions:
1) Start a terminal session in the backend directory (\<project-dir\>/backend)

2) Run `gradle clean run -Pconfiguration=<build_configuration>`, or replace `gradle` with `gradlew` if gradle is not installed on your machine

4) Note: if you're running either of the production builds from your local machine, you must contact the team to add your IP Address to the list of authorized database IPs

#### Amazon Web Services Deployment:
1) Start a terminal session in the backend directory (\<project-dir\>/backend)

2) Run `gradle war -Pconfiguration=<build_configuration>`, or replace `gradle` with `gradlew` if gradle is not installed on your machine

3) Locate the generated WAR archive in the /build/libs directory

4) Create/login to an Amazon Web Services Account

5) Navigate to the AWS Elastic Beanstalk page, and click "Create New Application"

6) Select a Web Server Environment

7) Choose the TomCat Platform

8) When prompted to upload software, upload the WAR archive generated from gradle

9) The rest of the settings can be left at their defaults

Note that these instructions only apply for uploading the debug build to AWS. Running either of the production builds requires setting up a database, then configuring the application to communicate with said database
To setup a database on AWS for a production build, please contact a team member. 

#### Citations:
- src\main\java\ca\sfu\cmpt373\alpha\vrctextui\Menu.java: Idea based on design from Summer 2016 CMPT213 As0.
- https://sparktutorials.github.io/2015/04/03/spark-lombok-jackson-reduce-boilerplate.html for dataToJSON method in RestDriver
- src\test\java\ca\sfu\cmpt373\alpha\vrcladder\ladder\LadderTest.java:  Got way to get a sublist from http://beginnersbook.com/2013/12/how-to-get-sublist-of-an-arraylist-with-example/
- src\test\java\ca\sfu\cmpt373\alpha\vrcladder\ladder\LadderTest.java:  Conversion from List to Array obtained from http://stackoverflow.com/questions/9572795/convert-list-to-array-in-java
- src\test\java\ca\sfu\cmpt373\alpha\vrcladder\ladder\LadderMethodsTest.java: generatePermutations() implementation from: http://stackoverflow.com/questions/10305153/generating-all-possible-permutations-of-a-list-recursively
- build-configurations\debug-config.gradle and production-config.gradle: generating resource properties file from Craig Trader: http://stackoverflow.com/questions/33020069/how-to-get-version-attribute-from-a-gradle-build-to-be-included-in-runtime-swing  

#### Directory Structure Explanation
backend<br />
----gradle/wrapper : Supports gradle integration into the application. Gradle is for the purposes of switching between debug and production, and for automatically obtaining files the project is dependent upon. It is effectively for our own use, not a part of the final product.<br />
----src : Source code for the project<br />
--------main : Production code and supporting files<br />
------------resources : Database configuration information<br />
------------webapp/WEB-INF : Configure Rest API to run on AWS<br />
------------java/ca/sfu/cmpt373/alpha : Production code<br />
----------------vrcladder : Business logic and database<br />
--------------------exceptions : RuntimeExceptions that can occur in the program<br />
--------------------ladder : Ladder and ladder sorting data structure<br />
--------------------matchmaking : Grouping teams into MatchGroups and MatchGroup interface with database<br />
------------------------logic : Sorting MatchGroups onto courts and determines times of play<br />
--------------------persistence : Database<br />
--------------------scores : Structure for MatchGroup's scores<br />
--------------------teams : Team data structure and Team interface with database<br />
------------------------attendance : Team attendance data structure<br />
--------------------users : User data structure and User interface with database<br />
------------------------authentication : Determines if they have permission to perform an action<br />
------------------------authorization : Figures out who a User is<br />
------------------------personal : Data structures for fields of Users<br />
--------------------util : Utility support files<br />
----------------vrcrest : Rest API<br />
--------------------datatransfer : Translates to and from JSON<br />
------------------------requests : Turns incoming JSON bodies into internal data structures<br />
------------------------responses : Turns internal data structures into outgoing JSON bodies<br />
--------------------routes : Methods that are automatically called by Spark with HTTP Requests<br />
--------test : Debug code and supporting files<br />
------------java/ca/sfu/cmpt373/alpha/vrcladder : JUnit tests for vrcladder, folders here correspond to the folders of files being tested in backend/src/main/java/ca/sfu/cmpt373/alpha/vrcladder<br />
------------resources : Debug database configuration information<br />
