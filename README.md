# Smapshot Mobile Application
This master's thesis project focus on creating a prototype mobile application with the feature of being able to retake historical photographs in order to compare the past with the present. A part from developing the mobile application, this thesis work also includes carrying out a design process and geospatial research. In other words, this consists of following three topics:
- Mobile application development
- Geospatial research
- Design process

See Smapshot's website [here](https://smapshot.heig-vd.ch/).

## Configuration environment

* Java 15
* [Android Studio Bumblebee 2021.1.1 Beta 3](https://developer.android.com/studio/preview/index.html)
  * Android Studio gradle: JDK 11. Can be found in: ```File -> Settings```
* Phone: Xiaomi Mi 9T, Motorola G8
  * Android version: 10

### Known config issues
* Specify JDK version error, solution [here](https://www.py4u.net/discuss/604849).
* How to set JDK path in Android Studio, solution [here](https://stackoverflow.com/questions/68120382/how-to-set-java-jdk-path-in-android-studio-arctic-fox).
* Can't move elements in UI (```acitivty.xml``` files) -> solution [here](https://stackoverflow.com/questions/54366352/cant-move-any-elements-in-android-studio-for-relativelayout).

## Authors & contributors
* Christoffer Karlsson - master thesis student
* Alfred Hirschfeld - master thesis student

## Agile workflow

### Design Process
- [X] Iteration 1 - lofi mockup prototype, basic mobile app, config and set up.
- [ ] Iteration 2 - hifi design, extended functionality in mobile app.
- [ ] Iteration 3 - TBA

### Scrum board
Link to [scrum board](https://miro.com/app/board/o9J_lxMVwzM=/), working in weekly sprints.

## Current functionality
- [X] Able to **capture** and **store** photos with Custom Camera API
- [X] **Displaying** sensor values when opening camera: tilt, roll and azimuth
- [X] Fetching json-values from json-file, mimicking metadata from Smapshot beta (georeferenced photo)
- [X] Able to store azimuth, tilt and roll when capturing new photo
- [X] Display results of parameters & mean accuracy in percentage (comparing old values with new)
- [X] Normalizing angles from json-file and from the sensor values (0-360 degrees)
- [X] Transparency mode included in camera, support for helping user to retake old photo

## Debugging
To debug the code and follow printouts, use the Logcat terminal which can be found at the bottom of Android Studio UI next to "Build". Add print-statements using syntax: 

```Log.d("tag", "message");```

## Android Studio links
* [GEO - How to track device orientation in real time](https://stackoverflow.com/questions/63442812/how-to-make-an-android-class-in-java-that-returns-device-angle?noredirect=1&lq=1)
* [DEV - How to pass a value from one Activity to another](https://stackoverflow.com/questions/3510649/how-to-pass-a-value-from-one-activity-to-another-in-android)
* [DEV - Guide for creating custom camera in Android Studio](https://www.youtube.com/watch?v=_wZvds9CfuE&t=16s)
* [DEV - How to show image using ImageView in Android (setImageResource)](https://stackoverflow.com/questions/8051069/how-to-show-image-using-imageview-in-android)
* [DEV - Transparency in image](https://stackoverflow.com/questions/5078041/how-can-i-make-an-image-transparent-on-android)
* [DESIGN - How to change fonts in TextView](https://stackoverflow.com/questions/2888508/how-to-change-the-font-on-the-textview)
* [DESIGN - How to set default font family for entire Android app](https://stackoverflow.com/questions/16404820/how-to-set-default-font-family-for-entire-android-app)
* [DESIGN - Button font size](https://stackoverflow.com/questions/2823808/android-button-font-size)
* [DESIGN - Slider (SeekBar)](https://stackoverflow.com/questions/8629535/implementing-a-slider-seekbar-in-android)

## APK
Android Package Kit - file format that Android uses to distribute and install apps. APKs contain all the elements that an app needs to install correctly on your device. *Will be released in the future.* 

## Issues
* Aspect ratio (4:3) of different Android cameras and resolution of photos (width, height). E.g: **Motorola G8 vs. Xiaomi Mi 9T**.
* Research about tilt (pitch) from Smapshot (reverse?). E.g: 4 degrees = 356 degrees. 
