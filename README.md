# Vancouver Racquet Club Ladder System

## CMPT 373 - Team Alpha
------------------------

This project was developed for the non-profit society, the Vancouver Racquets Club (VRC), as a part of CMPT373: Software Development Methods, and it is provided under the terms of the Berkeley Software Distribution (BSD) license.

The goal of the system is to modernize the weekly doubles ladder system of VRC through an easy-to-use, platform-independent user interface.
The project is split into two large parts:
    1. The frontend: Website/user interface (JavaScript)
    2. The backend: Rest API, Business Logic and Database (Java)
These are the two folders in the root folder of the project

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

4) Start local development server:

    	npm run dev


5) Run the production build

    	npm run build

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

3) Start local development server:

    	npm run dev

4) Run the production build

    	npm run build

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
<Frontend directory structure explanation>

-----------
### Backend

#### Instructions:
1) Start a terminal session in the backend directory (\<project-dir\>/backend)

2) Run `gradle clean run`, or `gradlew clean run` if gradle is not installed on your machine

#### Amazon Web Services Deployment:
1) Start a terminal session in the backend directory (\<project-dir\>/backend)

2) Run `gradle war`, or `gradlew war` if gradle is not installed on your machine

3) Locate the generated WAR archive in the /build/libs directory

4) Create/login to an Amazon Web Services Account

5) Navigate to the AWS Elastic Beanstalk page, and click "Create New Application"

6) Select a Web Server Environment

7) Choose the TomCat Platform

8) When prompted to upload software, upload the WAR archive generated from gradle

9) The rest of the settings can be left at their defaults

#### Citations:
- src\main\java\ca\sfu\cmpt373\alpha\vrctextui\Menu.java: Idea based on design from Summer 2016 CMPT213 As0.
- https://sparktutorials.github.io/2015/04/03/spark-lombok-jackson-reduce-boilerplate.html for dataToJSON method in RestDriver
- src\test\java\ca\sfu\cmpt373\alpha\vrcladder\ladder\LadderTest.java:  Got way to get a sublist from http://beginnersbook.com/2013/12/how-to-get-sublist-of-an-arraylist-with-example/
- src\test\java\ca\sfu\cmpt373\alpha\vrcladder\ladder\LadderTest.java:  Conversion from List to Array obtained from http://stackoverflow.com/questions/9572795/convert-list-to-array-in-java
- src\test\java\ca\sfu\cmpt373\alpha\vrcladder\ladder\LadderMethodsTest.java: generatePermutations() implementation from: http://stackoverflow.com/questions/10305153/generating-all-possible-permutations-of-a-list-recursively

#### Directory Structure Explanation
- backend
     -gradle/wrapper : Supports gradle integration into the application. Gradle is for the purposes of switching between debug and production, and for automatically obtaining files the project is dependent upon. It is effectively for our own use, not a part of the final product.
    - src : Source code for the project
        - main : Production code and supporting files
            - resources : Database configuration information
            - webapp/WEB-INF : Configure Rest API to run on AWS
            - java/ca/sfu/cmpt373/alpha : Production code
                - vrcladder : Business logic and database
                    - exceptions : RuntimeExceptions that can occur in the program
                    - ladder : Ladder and ladder sorting data structure
                    - matchmaking : Grouping teams into MatchGroups and MatchGroup interface with database
                        - logic : Sorting MatchGroups onto courts and determines times of play
                    - persistence : Database
                    - scores : Structure for MatchGroup's scores
                    - teams : Team data structure and Team interface with database
                        - attendance : Team attendance data structure
                    - users : User data structure and User interface with database
                        - authentication : Determines if they have permission to perform an action
                        - authorization : Figures out who a User is
                        - personal : Data structures for fields of Users
                    - util : Utility support files
                - vrcrest : Rest API
                    - datatransfer :
                        - requests : Turns incoming JSON bodies into internal data structures
                        - responses : Turns internal data structures into outgoing JSON bodies
                    - routes : Methods that are automatically called by Spark with HTTP Requests
        - test : Debug code and supporting files