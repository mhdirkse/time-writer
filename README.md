# time-writer
Calculate your official project hours from your private notes. Do you have to write each day how many hours you spent
on each project? And does it take you much time to make up these figures? Then this tool is for you. During your working
day, enter little notes about what you are doing. At the end of the day, shuffle these notes and let the tool calculate
the time difference between these notes. Let the tool add some time differences to get your official hours.

This is my plan, but it is not yet done. This version is only the backend part of registering users and managing notes.
No calculations with notes added yet.

There will be a strict distinction between the backend, programmed with Java and Spring Boot, and the front end, which
will be programmed with Angular and TypeScript. The backend you have here is a REST interface with the following
functions:

* POST to `/login`: Login, fill the body of the HTTP request with your credentials.
It is a JSON like `{"username":"admin", "password":"admin"}`.
* GET to `/logout`: Logout.
* POST to `/api/users`: Add user. Put the credentials in the body of the HTTP request.
Modifying a user this way is an error. You can do this without
being logged in.
* PUT to `/api/users/<user id>`: Modify user. The id, the username and the new password
are in the body of the HTTP request. Only allowed if the user being modified
is logged in, or if user `admin` is logged in.
* DELETE to `/api/users/<user id>`: Delete user. Only allowed if the deleted
user is logged in, or if user `admin` is logged in.
* GET to `/api/timenotes/<start time>/<end time>`. Get all notes for the logged-in user
with timestamp between start and end time, which are seconds since Epoch. Only allowed
is someone is logged in.
* POST to `/api/timenotes`. Add time note. The body needs to have properties `userId`,
`timestamp` and `message`. The `userId` must be the id of the logged-in user. Only
allowed if someone is logged in.
* PUT to `/api/timenotes/<id>`. Modify time note with given id. The body needs to have
the same properties as for creating notes and also the `id` property must be present.
* DELETE to `/api/timenotes/<id>`. Delete time note. Only allowed if the user of the note
is the logged-in user.

# Usage
* Clone this project to your local PC. You need Java 8 and Maven before you can
proceed.
* Make a Google project and add a Cloud SQL database to it. Name it "users". Initialize your database
using the `db/dbInit.sql` script.
* Under `time-writer-with-db`, add a file `credentials.properties`. The
contents should be like:
```
googleProject=<google project id>
googleRegion=<region of Cloud SQL database>
dbpassword=<root password of Cloud SQL database>
```
* You may need to modify the connection URL in applicationp.properties
to match your Google project.
* Build from the root directory of your checkout using `mvn clean install`.
* Enter `server/time-writer-with-db`. Deploy using `mvn appengine:deploy`.
* You can also run this project on your local PC. Go to `server/time-writer-base`.
Do `mvn spring-boot:run`. There is an H2 database that you can access on URL
`/h2-console`.
* You can test using curl. Note that logging in is done with cookies. Use the -c
option to create a cookie file and -b to add cookies to a request. Use -X GET/POST/PUT/DELETE
to choose the kind of request. Use `-H "Content-Type: application/json"` with every request.

