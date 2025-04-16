# springboot-postgresql-restapi-ssl
This is a demo project for Spring Boot API using SSL (MTLS) with PostgreSQL

### API Details ###
**Update employee resource** <br>
/api/v1/employees/{id} - *PUT* <br><br>
**Delete employee resource** <br>
/api/v1/employees/{id} - *DELETE* <br><br>
**Create a new phone number for an employee** <br>
/api/v1/employees/phone - *POST*<br><br>
**Update a phone number for an employee**<br>
/api/v1/employees/phone/{id} - *PUT*<br><br>
**Delete a phone number for an employee**<br>
/api/v1/employees/phone/{id} - *DELETE*

### Important Point ###
**Create Self-signed certificate or procure the certificate from certificate authority**
keytool -genkey -alias *https_cert_server* -storetype PKCS12 -keyalg RSA -keysize 2048 -keystore *ssl-keystore-server.p12* -validity 365
keytool -genkey -alias *https_cert_client* -storetype PKCS12 -keyalg RSA -keysize 2048 -keystore *ssl-keystore-client.p12* -validity 365

**Export private key from Client Keystore (to be used in Postman)**
openssl pkcs12 -in *ssl-keystore-client.p12* -nodes -nocerts -out *https_cert_client.p8.pem*

**Export the certificates and create truststore (JKS)**
keytool -importcert -file *"localhost-client.crt"* -keystore *ssl-truststore-server.jks* -alias *"server"*
keytool -importcert -file *"localhost-server.crt"* -keystore *ssl-truststore-client.jks* -alias *"client"*

**Postman Settings for MTLS**
![Postman Settings](https://photos.app.goo.gl/Kb7igHB13FbHRhWC8)

**Install Docker and execute below commands to install PostgreSQL DB**
- docker pull postgres
- docker run -d --name mypostgres -p 5432:5432 -e POSTGRES_PASSWORD=*yourpassword* postgres
- docker exec -it mypostgres bash
- psql -h localhost -U postgres -W
- CREATE DATABASE employees;
- \c employees
- Import Postman project (postman/RestAPI-SSL.postman_collection.json) in Postman App
