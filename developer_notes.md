## Class diagram
*To be created...* 

## Debugging
To debug the code and follow printouts, use the Logcat terminal which can be found at the bottom of Android Studio UI next to "Build". Add print-statements using syntax: 

```Log.d("tag", "message");```

## Android Studio links
* [GEO - How to track device orientation in real time](https://stackoverflow.com/questions/63442812/how-to-make-an-android-class-in-java-that-returns-device-angle?noredirect=1&lq=1)
* [GEO - Normalise angles with modulus](https://stackoverflow.com/questions/2320986/easy-way-to-keeping-angles-between-179-and-180-degrees)
* [GEO - Integrate MapView from OpenStreetMapDroid](https://help.famoco.com/developers/media/display-map/)
* [GEO - Calculate magnetic declination](https://www.tabnine.com/code/java/methods/android.hardware.GeomagneticField/getDeclination)
* [DEV - Pass a value from one Activity to another](https://stackoverflow.com/questions/3510649/how-to-pass-a-value-from-one-activity-to-another-in-android)
* [DEV - Creating custom camera in Android Studio](https://www.youtube.com/watch?v=_wZvds9CfuE&t=16s)
* [DEV - Show image using ImageView in Android (setImageResource)](https://stackoverflow.com/questions/8051069/how-to-show-image-using-imageview-in-android)
* [DEV - Transparency in photo](https://stackoverflow.com/questions/5078041/how-can-i-make-an-image-transparent-on-android)
* [DEV - calculating aspect ratio](https://stackoverflow.com/questions/28861102/correct-way-to-calculate-best-camera-preview-size-maintaing-aspect-ratio)
* [DEV - get current location](https://www.youtube.com/watch?v=XQJiiuk8Feo)
* [DESIGN - Change fonts in TextView](https://stackoverflow.com/questions/2888508/how-to-change-the-font-on-the-textview)
* [DESIGN - Set default font family for entire Android app](https://stackoverflow.com/questions/16404820/how-to-set-default-font-family-for-entire-android-app)
* [DESIGN - Button font size](https://stackoverflow.com/questions/2823808/android-button-font-size)
* [DESIGN - Slider (SeekBar)](https://stackoverflow.com/questions/8629535/implementing-a-slider-seekbar-in-android)
* [DESIGN - Shape photos, round edges](https://www.youtube.com/watch?v=jihLJ0oVmGo)
* [DESIGN - rounded corner rectangle](https://stackoverflow.com/questions/18781902/rounded-corner-for-textview-in-android)
* [DESIGN - set opacity (dynamically)](https://stackoverflow.com/questions/2838757/how-to-set-opacity-alpha-for-view-in-android)
* [DESIGN - transition effect](https://stackoverflow.com/questions/18475826/how-to-perform-a-fade-animation-on-activity-transition)
* [DESIGN - change app icon](https://stackoverflow.com/questions/26615889/how-do-you-change-the-launcher-logo-of-an-app-in-android-studio)
* [DESIGN - animate ImageView](https://stackoverflow.com/questions/8720626/android-fade-in-and-fade-out-with-imageview)

## Known bugs

* When adding custom markers in OSMdroid, they move with the map. Solution [here](https://stackoverflow.com/questions/54811451/osmdroid-default-marker-moving-when-zooming-out-on-android-api-28#_=), added into current codebase.

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