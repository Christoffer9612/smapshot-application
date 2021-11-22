# Smapshot Mobile Application

### What is Smapshot?
See Smapshot's website [here](https://smapshot.heig-vd.ch/).

### What is Smapshot mobile application?
This codebase is the work of two master thesis students where the focus is on developing a mobile application prototype with the feature of retaking historical photographs. Rephotography allows us to compare the past with the present. A part from development, this thesis also includes carrying out a user-centered design process (UCD) and geospatial research.

For now, this work is only available for Android and not iOS.

## Configuration

* Java 15
* [Android Studio Bumblebee 2021.1.1 Beta 3](https://developer.android.com/studio/preview/index.html)
  * Android Studio gradle: JDK 11. Can be found in: ```File -> Settings```
* Phone: Xiaomi Mi 9T, Motorola G8. Currently, layout of app is designed towards Xiaomi Mi 9T. There are aspect ratio issues with Motorola G8 (4:3).
  * Android version: 10

### Known issues
* Specify JDK version error, solution [here](https://www.py4u.net/discuss/604849).
* How to set JDK path in Android Studio, solution [here](https://stackoverflow.com/questions/68120382/how-to-set-java-jdk-path-in-android-studio-arctic-fox).
* Osmdroid (OpenStreetMap) doesn't load in codefiles. Solution: open ```build.gradle``` (Module), do a minor change and change back to its original state in order for gradle to display a "Sync Now" button in the upper right corner. Press "Sync Now".

## Current functionality
- [X] Adapted for being used in: **portrait mode**
- [X] Custom Camera API
- [X] Capture & store photos
- [X] Displaying sensor values when opening camera: tilt, roll and azimuth
- [X] Displaying sensor values from old photos (tilt, roll and azimuth)
- [X] Normalizing angles (0-360 degrees)
- [X] Able to store azimuth, tilt and roll when capturing new photo and compare with old
- [X] Calculating results of parameters & mean accuracy in %
- [X] Instructions on how to position the device to achieve greater accuracy when retaking a photo
- [X] Transparency mode in camera: displaying old photo on top of camera
- [X] Select between two photos to retake
- [X] Tutorial page with instructions on how to use
- [X] Zoom in/out old photo on camera view
- [X] Aspect ratio of camera preview set to 4:3 (1.33) for all devices
- [X] OpenStreetMap to view map of photos. The app requires wifi to load map.
- [X] Displaying location in OpenStreetMap.

## Contributors
* Christoffer Karlsson - master thesis student
* Alfred Hirschfeld - master thesis student

See developer notes [here](https://github.com/Christoffer9612/smapshot-application/blob/master/developer_notes.md).

## Debugging
To debug the code and follow printouts, use the Logcat terminal which can be found at the bottom of Android Studio UI next to "Build". Add print-statements using syntax: 

```Log.d("tag", "message");```

## APK
APK stands for Android Package Kit, a file format that Android uses to distribute and install apps. APKs contain all the elements that an app needs to install correctly on your device. 

*Will be released in the future for the public to try out.* 

## Example
![](https://github.com/Christoffer9612/smapshot-application/blob/master/transparency_demo.gif)
