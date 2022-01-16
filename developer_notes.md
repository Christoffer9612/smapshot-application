## Configuration

* Java 15
* [Android Studio Bumblebee 2021.1.1 Beta 3](https://developer.android.com/studio/preview/index.html)
  * Android Studio gradle: JDK 11. Can be found in: ```File -> Settings```
* Phone: Xiaomi Mi 9T, Motorola G8. Currently, layout of app is designed towards Xiaomi Mi 9T. There are aspect ratio issues with Motorola G8 (4:3).
  * Android version: 10

### Solved issues
* Specify JDK version error, solution [here](https://www.py4u.net/discuss/604849).
* How to set JDK path in Android Studio, solution [here](https://stackoverflow.com/questions/68120382/how-to-set-java-jdk-path-in-android-studio-arctic-fox).
* Osmdroid (OpenStreetMap) doesn't load in codefiles. Solution: open ```build.gradle``` (Module), do a minor change and change back to its original state in order for gradle to display a "Sync Now" button in the upper right corner. Press "Sync Now".

## Debugging
To debug the code and follow printouts, use the Logcat terminal which can be found at the bottom of Android Studio UI next to "Build". Add print-statements using syntax: 

```Log.d("tag", "message");```

## Known bugs
* Xiaomi Mi 9T does NOT update current location on OSMdroid. Solution: open Google Maps to load in and update your current location.

## Current functionality

#### Camera
- [X] Photos being used and stored in: **portrait mode**
- [X] Custom Camera API implemented
- [X] Capture & store photos on external memory of mobile device
- [X] Transparency mode in camera: displaying photo on top of camera view
- [X] Zoom in/out selected photo on top of the camera view by dragging
- [X] Aspect ratio of camera preview set to 4:3 (= 1.33) for all devices
- [X] Instructions on how to position the device to achieve greater accuracy when retaking a photo

#### Geographic information
- [X] Display sensor values when opening camera: azimuth, tilt and roll
- [X] Display sensor values from the test photos
- [X] Normalizing angles (0-360 degrees)
- [X] Calculating results of parameters and mean accuracy in percentage (%)
- [X] Able to store azimuth, tilt and roll when capturing new photo and compare with selected test photo

#### OpenStreetMap
- [X] OpenStreetMap to show map of photos. The app requires **internet** to load map
- [X] Displaying your current location in OpenStreetMap

#### Other
- [X] Select between two photos to retake, one test photo and another photo fetched from [Smapshot's API](https://smapshot.heig-vd.ch/api/v1/docs/)
- [X] Instructions on how to use and information about Smapshot