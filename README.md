# Vancouver Racquet Club Ladder System

## CMPT 373 - Team Alpha
------------------------

This project is split up by the individual components, the frontend and the backend. The frontend is written in JavaScript and the backend is written in Java. This project is under the Berkeley Software Distribution (BSD) license and was developed for the Vancouver Racquet Club (VRC).

------------
### Frontend

#### Instructions (For Ubuntu 14.04):
1) Start a terminal session in the frontend directory (<project-dir>/frontend)


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
1) Start a terminal session in the frontend directory (<project-dir>/frontend)

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

-----------
### Backend

#### Instructions:
1) Start a terminal session in the backend directory (<project-dir>/backend)
2) Run "gradle clean run", or "gradlew clean run" if gradle is not installed on your machine

#### Citations:
- src\main\java\ca\sfu\cmpt373\alpha\vrctextui\Menu.java: Idea based on design from Summer 2016 CMPT213 As0.
- https://sparktutorials.github.io/2015/04/03/spark-lombok-jackson-reduce-boilerplate.html for dataToJSON method in RestDriver
