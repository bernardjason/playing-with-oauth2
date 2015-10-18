

You should be able to clone the project, then execute

mvn jetty:run

From your browser visit http://127.0.0.1:8080/application

which is a sample demo website that wants to call 3 of your apis

click on top menu "Authorise" will direct you to the demo oauth2 server to permit access to some or all 3 of the apis you provide. You then go back to demo website with a token which is then used to get an access_token to call the apis.

Will add more notes soon.
