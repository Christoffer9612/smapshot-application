# smapshot-application
This project focus on creating a mobile application prototype with the feature of re-taking photographs. Part of our master's thesis work alongside carrying out a design process and geospatial research. See Smapshot project [here](https://smapshot.heig-vd.ch/).

## Configuration

* Java 15
* [Android Studio Bumblebee 2021.1.1 Canary 12](https://developer.android.com/studio/preview/index.html)
  * Android Studio gradle: JDK 11. Can be found in: ```File -> Settings```
* Phone: Xiaomi Mi 9T
  * Android version: 10

## Authors
* Christoffer Karlsson
* Alfred Hirschfeld

## Current functionality
- [X] Displaying sensor values (tilt, roll, azimuth)
- [X] Able to capture and store photos with Custom Camera API
- [X] Displaying json-values from json-file, mimicking metadata from Smapshot

## Help
* Can't move elements in UI (```acitivty.xml``` files) -> solution [here](https://stackoverflow.com/questions/54366352/cant-move-any-elements-in-android-studio-for-relativelayout).

## Known issues
* Specify JDK version error, solution [here](https://www.py4u.net/discuss/604849).
* How to set JDK path in Android Studio, solution [here](https://stackoverflow.com/questions/68120382/how-to-set-java-jdk-path-in-android-studio-arctic-fox).
