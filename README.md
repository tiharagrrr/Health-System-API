Importing the Project:

1. Launch NetBeans IDE.
2. Open the project. Go to "File" -> "Open Project..." and navigate to the folder where you unzipped the
Health System API project. Select the folder and click "Open". NetBeans will import the project for you.
This project uses simulated data stored in memory, so you don't need to set up a separate database.

1.2 Running the Application
This project is designed to run on a local Tomcat server:
1. Right-click on the project name in the Projects window.
2. Select "Run" to start the Tomcat server.
3. NetBeans will build and deploy the project to the server.
4. Once the server is running, you can access the API using a web browser. The base URL will be
displayed by the server (in this case, http://localhost:8080/w1953194_TiharaEgodage_5COSC022W).

1.3 Accessing API Endpoints

The base URL provided by the server is where you'll make API requests. Specific features (microservices) can
be accessed by adding additional parts (endpoints) to the base URL.
As shown here, the URLs for each HTTP method for each model class should start with the pattern,
http://localhost:8080/w1953194_TiharaEgodage_5COSC022W/rest
followed by the relevent path as described in the resources classes in the code.
