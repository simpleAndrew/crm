So, the application is written using spring boot.
I'm not a master of it but it gives out of the box web server, db support and fat jar built.
Also I've used start.spring.io service which is nice if you know which components you want.

I've implemented all use cases listed. Application exposes 2 REST services: appointments and clients.
Through POST:clients/ you should be able to create clients. Then through POST:clients/<id/appointments/ you can register appointments. JSON structure samples are included in resources directory so you can start with something.

Some endpoints:
GET:clients/<id>/appointments - list all client appointments
GET:clients/<id>/appointments/last - get last visited appointment (basically, to be able to rate it)
GET:clients/<id>/appointments/next - get next upcoming appointment
POST:clients/<id>/appointments - create appointment for client.

GET:appointments/nextWeek - to get appointments for next 7 days starting tomorrow
PUT::appointments/<id>/rate?rating=<any string rating> - to rate appointment

App is built my maven and you should be able to find jar in /target dir. When you run it it starts on 8080 port (sorry - didn't change that) and runs with DB which then is preserved, so on restart it will recover information out of it.

Business tradeoffs made:
definition of week is not obvious so I went with next 7 days, starting from tomorrow
rating is part of appointment, so we can't do rating multiple times
both, client and appointment, has only minimum information
Technical tradeoffs (a lot of them):
poor test coverage - I was more in technology browsing than in implementation, so tests came later
no significant validation
no correct error handling
not nice JSON parsing - date in appointment is shown as epoch (though you still can pass it as String)
time is transformed to Date and to UTC - it changes the time a little - enough for example, but should be different in reality
poor knowledge of spring boot of mine - I'm afraid majority of things should be done better