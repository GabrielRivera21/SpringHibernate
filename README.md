# SpringHibernateApi
##Building the Project
You need to have the <b>Gradle Plugin</b> installed in your Eclipse environment. After this, you need to click <b>File</b> -> <b>Import...</b> -> <b>Gradle Project</b> -> Select the directory where the `build.gradle` is and click on <b>Build Model</b> then click <b>Next</b> -> <b>Finish</b> and you've successfully imported the SpringHibernateApi Project into Eclipse.

## Requirements
<ul>
<li>Postgresql Database installed locally</li>
<li>Create a Database for the App</li>
<li>Set up a SendGrid Account</li>
</ul>
Put your database properties in the application.properties file
```
spring.datasource.url=jdbc:postgresql://localhost:5432/YOURDBNAME
spring.datasource.username=YOURUSER
spring.datasource.password=YOURPASS
```


## Running the SpringHibernateApi Application

_To run the application:

1. (Menu Bar) Run->Run Configurations
2. Under Java Applications, select your run configuration for this app in `Application.java`
3. Open the Arguments tab
4. In VM Arguments, provide the following information to use the default keystore provided with the sample code:

-Dkeystore.file=src/main/resources/private/.keystore -Dkeystore.pass=changeit

5. Note, this keystore is highly insecure! If you want more security, you should obtain a real SSL certificate:

http://tomcat.apache.org/tomcat-7.0-doc/ssl-howto.html

6. This keystore is not secured and should be in a more secure directory -- preferably completely outside of the app for non-test applications -- and with strict permissions on which user accounts can access it

## Accessing the Service

Note: you need to use "https" and port "8443":

`https://localhost:8443/users`

You will almost certainly see a warning about the site's certificate in your browser. This warning is being generated because the keystore includes a certificate that has not been signed by a certificate authority.

If you try to access the above URL in your browser, the server is going to generate an error that looks something like "An Authentication object was not found in the SecurityContext." If you want to use your browser to test the service, you will need to use a plug-in like Postman and an understanding of how to use it to manually construct and obtain a bearer token.

The `UsersSvcClientApiTest` shows how to programmatically access the SpringHibernate's services. You should look at the `SecuredRestBuilder` class that is used to automatically intercept requests to the UsersSvcApi methods, automatically obtain an OAuth 2.0 bearer token if needed, and add this bearer token to HTTP requests.

###Obtaining the OAuth2 Token

In order to obtain the Token in order to access the token you need to make an HTTP POST using a grant_type of password as seen in the `SecuredRestBuilder` class to the /oauth/token path.

#####Request
```
POST /oauth/token HTTP/1.1
Host: localhost:8443
Content-Type: application/x-www-form-urlencoded

username=youruser&
password=yourpass&
client_id=mobile-SpringHibernate&
client_secret=&
grant_type=password
```

#####Response
```
{
  "access_token":"7186f8b2-9bae-48b6-90c2-033a4476c0fc",
  "token_type":"bearer",
  "refresh_token":"d7fe8cda-812b-4b3e-9ce7-b15067e001e4",
  "expires_in":298653
}
```

### Accesing API Docs

To access the docs just go this url on your browser:
```
https://localhost:8443/docs
```

and you will see the API with Response and Request Models and Info.
