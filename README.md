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

Link to [Scrum Board](https://miro.com/app/board/o9J_lxMVwzM=/).

## Current functionality
- [X] Displaying sensor values (tilt, roll, azimuth)
- [X] Able to capture and store photos with Custom Camera API
- [X] Displaying json-values from json-file, mimicking metadata from Smapshot beta (georeferenced photo)
- [X] Able to store azimuth, tilt and roll from new photo and display
- [X] Normalizing angles from json-file and from the sensor values (0-360 degrees)
- [X] Display the mean accuracy of the retaken photograph in percentage

## Known issues
* Specify JDK version error, solution [here](https://www.py4u.net/discuss/604849).
* How to set JDK path in Android Studio, solution [here](https://stackoverflow.com/questions/68120382/how-to-set-java-jdk-path-in-android-studio-arctic-fox).
* Can't move elements in UI (```acitivty.xml``` files) -> solution [here](https://stackoverflow.com/questions/54366352/cant-move-any-elements-in-android-studio-for-relativelayout).

## Debugging
To debug the code and follow printouts, use the Logcat terminal which can be found at the bottom of Android Studio UI next to "Build". Add print-statements using syntax: 

```Log.d("tag", "message");```

## Android Studio Links
* [How to set default font family for entire Android app](https://stackoverflow.com/questions/16404820/how-to-set-default-font-family-for-entire-android-app)
* [How to pass a value from one Activity to another](https://stackoverflow.com/questions/3510649/how-to-pass-a-value-from-one-activity-to-another-in-android)
* [Guide for creating custom camera in Android Studio](https://www.youtube.com/watch?v=_wZvds9CfuE&t=16s)
* [DESIGN - How to change fonts in TextView](https://stackoverflow.com/questions/2888508/how-to-change-the-font-on-the-textview)
* [How to track device orientation in real time](https://stackoverflow.com/questions/63442812/how-to-make-an-android-class-in-java-that-returns-device-angle?noredirect=1&lq=1)
* [How to show image using ImageView in Android (setImageResource)](https://stackoverflow.com/questions/8051069/how-to-show-image-using-imageview-in-android)
* [Transparency in image](https://stackoverflow.com/questions/5078041/how-can-i-make-an-image-transparent-on-android)
* [Button font size](https://stackoverflow.com/questions/2823808/android-button-font-size)
