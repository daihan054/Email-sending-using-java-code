# Email Sending using microsoft exchange server and smtp
This is a basic code for sending mail via EWS and SMTP

## EWS - Microsoft Exchange Server
### Required jars
The following jars should be added in the project build path
- commons-codec-1.9.jar
- commons-lang3-3.4.jar
- commons-logging-1.2.jar
- ews-java-api-2.0.jar
- httpclient-4.4.1.jar
- httpcore-4.4.1.jar
- joda-time-2.8.jar

### Necessary credentials
- Email id which is used to open microsoft account
- That microsoft account password

## SMTP
### Required jars
The following jars should be added in the project build path
- javax.mail.jar
- smtp-1.4.5.jar

### Necessary credentials
- gmail id
- gmail id's password

### A very important point to understand
In my EmailMessenger.java code in line number 121 there has a line 

```java
message.setFrom(new InternetAddress(this.smtpFrom, "National University of Singapore")); // from , view
```
#### if we use the above line
**View before** opening the mail will be as bellow:
![renameEmail group 1 2](https://user-images.githubusercontent.com/73774433/113709691-9d521e00-9704-11eb-9663-5e611536e99d.JPG)

**View after** opening the mail will be as bellow:
![renameEmail group 1 1](https://user-images.githubusercontent.com/73774433/113709843-cd012600-9704-11eb-8ca7-0e38df74cf25.png)

#### if we don't use the above line
**View before** opening the mail will be as bellow:
![renameEmail 2 1](https://user-images.githubusercontent.com/73774433/113710262-4ef14f00-9705-11eb-9026-d47e218f8217.JPG)

**View after** opening the mail will be as bellow:
![renameEmail 2 2](https://user-images.githubusercontent.com/73774433/113710324-5e709800-9705-11eb-9b0b-a7688f4de94e.png)
