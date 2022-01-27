# Smapshot Mobile Application

### What is Smapshot?
Smapshot is a web-based platform with the goal of creating a virtual 3D globe by georeferencing photos. See Smapshot's [website](https://smapshot.heig-vd.ch/).

### What is Smapshot mobile application?
This proof of concept demonstrates what a Smapshot mobile application could look like. The focus is to be able to retake historical photographs from Smapshot. Rephotography allows us to compare and analyze the past with the present (human influence of areas, urbanisation, environmental changes, etc.).

* This prototype has been usability tested by eight test-users.
* This prototype is only available for Android smartphones and has only been tested on: *Xiaomi Mi 9T* and *Motorola G8*. 
* The app includes two photographs to retake in Yverdon-les-Bains, Switzerland:
    * One test photo from the office of St Roch building
    * One photo by the lake Neuchâtel, from Smapshot archives

## Permissions
The app requires three permissions:

📍 Location - to display your current location in OpenStreetMap.

📸 Camera - to take photos.

💾 Storage - to store photos captured.

## Contributors
* Christoffer Karlsson - master thesis student (geographic data)
* Alfred Hirschfeld - master thesis student (interaction design)

## Developer notes
The app is **not** modified for dark mode on Android phones. If you have enabled dark mode, the UI will look peculiar.

The app **does not have a UI that supports all screen sizes and devices.** Check [developer notes](https://github.com/Christoffer9612/smapshot-application/blob/master/developer_notes.md) for more technical information.

## APK
APK is a file format that Android uses to distribute and install apps.

*APK will be released soon for the public to try out.* 

## Thesis report
*Will be linked here when published.* 

## Demo of app
![](https://github.com/Christoffer9612/smapshot-application/blob/master/full_demo.gif)

## Conclusions and suggestions for future work 🚀
* Involve users more socially
* Use points instead of grading and include a leaderboard
* Research about making orientation angles (azimuth, tilt and roll) more user-friendly
* Due to different camera calibrations (field of view, resolution sizes), introduce a feature to crop photos
* Integrate Smapshot API more (fetch more photos and json files)
* Look into artificial intelligence, used for recognizing environments instead of having gps coordinates
