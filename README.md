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
* Gradle version: to be specified

### Known issues
* Specify JDK version error, solution [here](https://www.py4u.net/discuss/604849).
* How to set JDK path in Android Studio, solution [here](https://stackoverflow.com/questions/68120382/how-to-set-java-jdk-path-in-android-studio-arctic-fox).

## Current functionality
- [X] Custom Camera API
- [X] Capture & store photos
- [X] Displaying sensor values when opening camera: tilt, roll and azimuth
- [X] Displaying sensor values from old photos (tilt, roll and azimuth)
- [X] Normalizing angles (0-360 degrees)
- [X] Able to store azimuth, tilt and roll when capturing new photo and compare with old
- [X] Calculating results of parameters & mean accuracy in %
- [X] Instructions on how to position the device to achieve greater accuracy when retaking a photo
- [X] Transparency mode in camera: displaying old photo on top of camera
- [X] Select between two photos to retake.

## Contributors
* Christoffer Karlsson - master thesis student
* Alfred Hirschfeld - master thesis student

## Debugging
To debug the code and follow printouts, use the Logcat terminal which can be found at the bottom of Android Studio UI next to "Build". Add print-statements using syntax: 

```Log.d("tag", "message");```

## Android Studio links
* [GEO - How to track device orientation in real time](https://stackoverflow.com/questions/63442812/how-to-make-an-android-class-in-java-that-returns-device-angle?noredirect=1&lq=1)
* [GEO - Normalise angles with modulus](https://stackoverflow.com/questions/2320986/easy-way-to-keeping-angles-between-179-and-180-degrees)
* [GEO - Calculate magnetic declination](https://www.tabnine.com/code/java/methods/android.hardware.GeomagneticField/getDeclination)
* [DEV - Pass a value from one Activity to another](https://stackoverflow.com/questions/3510649/how-to-pass-a-value-from-one-activity-to-another-in-android)
* [DEV - Creating custom camera in Android Studio](https://www.youtube.com/watch?v=_wZvds9CfuE&t=16s)
* [DEV - Show image using ImageView in Android (setImageResource)](https://stackoverflow.com/questions/8051069/how-to-show-image-using-imageview-in-android)
* [DEV - Transparency in photo](https://stackoverflow.com/questions/5078041/how-can-i-make-an-image-transparent-on-android)
* [DESIGN - Change fonts in TextView](https://stackoverflow.com/questions/2888508/how-to-change-the-font-on-the-textview)
* [DESIGN - Set default font family for entire Android app](https://stackoverflow.com/questions/16404820/how-to-set-default-font-family-for-entire-android-app)
* [DESIGN - Button font size](https://stackoverflow.com/questions/2823808/android-button-font-size)
* [DESIGN - Slider (SeekBar)](https://stackoverflow.com/questions/8629535/implementing-a-slider-seekbar-in-android)
* [DESIGN - Shape photos, round edges](https://www.youtube.com/watch?v=jihLJ0oVmGo)

## APK
APK stands for Android Package Kit, a file format that Android uses to distribute and install apps. APKs contain all the elements that an app needs to install correctly on your device. 

*Will be released in the future for the public to try out.* 
