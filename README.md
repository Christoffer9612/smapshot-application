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

## Permissions
The mobile application requires three permissions:
* Location - to display your current location and photos to retake nearby.
* Camera - to take photos with your camera.
* Storage - to store photos captured.

## Contributors
* Christoffer Karlsson - master thesis student
* Alfred Hirschfeld - master thesis student

See developer notes [here](https://github.com/Christoffer9612/smapshot-application/blob/master/developer_notes.md).

## Developer notes
If you want to further improve or contribute to the project, check developer_notes.md for more detailed information.

## APK
APK stands for Android Package Kit, a file format that Android uses to distribute and install apps. APKs contain all the elements that an app needs to install correctly on your device. 

*Will be released in the future for the public to try out.* 

## Example
![](https://github.com/Christoffer9612/smapshot-application/blob/master/transparency_demo.gif)
