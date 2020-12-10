## Installation: The Official Conference Planning Managing System Program 2020 (GOTY) 

This document gives instructions to install the program with all dependencies. 

***Note: the Twilio text notification feature only sends to verified caller IDs so you won't be able to see if it's functioning or not, so you likely won't plan on using the Twilio feature. If so, comment out `TwilioEventNotificationGateway.java` and all instances where it is initialized, and you can skip to Step 2.***   

### All systems (Linux, Mac OS X, and Windows)

1. Twilio requires `node.js v10.12 or above`. Make sure you have it [installed](https://nodejs.org/en). 

#### For Mac users use:
```
brew tap twilio/brew && brew install twilio
```
#### For Windows/Linux users:

```
npm install twilio-cli -g
```

**Note: If prompted for token and accountSID, see the final constants `ACCOUNT_SID` and `AUTH_TOKEN` at the top in ConferencePlanningSystem.**

2. Since we are not using a build automation system, download the following and add to classpath (File -> Project Structure -> Libraries):
* You will need [zxing](https://www.callicoder.com/qr-code-reader-scanner-in-java-using-zxing/). Scroll down to `zxing core-3.3.0.jar` and `zxing javase-3.3.0.jar` and download both.
* You will also need [twilio libraries](https://www.twilio.com/docs/libraries/java#using-without-a-build-automation-tool) for `v8.3.0`.
* Download [javacord-3.x.x-shaded.jar](https://github.com/Javacord/Javacord/releases/latest) and add to classpath.
* Include `JUnit4` to classpath




