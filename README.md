picam
=====

An easy-to-use Open Source Java library to access the [Raspberry Pi](https://www.raspberrypi.org/)
[camera module](https://www.raspberrypi.org/products/camera-module).

This library provides a direct Java API to the camera - behind the scenes the *native* MMAL
library is used.

This library does *not* require any external native processes nor does it wrap
any native executable program.

The implementation is based loosely on that used by the native `RaspiStill`
utility.

This project is unofficial and is not affiliated in any way with the Raspberry
Pi Foundation.

Basic Usage
-----------

More detailed installation and usage instructions will be provided soon.

Add the library jar file and its dependencies to your project class-path:

 * picam-<version>-SNAPSHOT.jar
 * jna-4.2.1.jar
 * jna-platform-4.2.1.jar
 * logback-classic-1.1.7.jar
 * logback-core-1.1.7.jar
 * slf4j-api-1.7.20.jar

Using the library is simple, essentially:

```
CameraConfiguration config = cameraConfiguration()
    .width(1920)
    .height(1080);

try (Camera camera = new Camera(config)) {
    camera.takePicture(new FilePictureCaptureCallback(new File("picam.png")));
}
```

Captured images can be directly saved to disk, or returned and processed as a
`byte[]`.

Status
------

The project status is currently pre-alpha and experimental.

The API is subject to change.

Feedback is welcome at the [github project](https://github.com/caprica/picam).

Not all camera features or effects are implemented, the aim is to add more
support over time:

- [x] image width/height
- [ ] image encoding (hard-coded to PNG currently)
- [ ] stereo mode
- [x] brightness
- [x] contrast
- [x] saturation
- [x] sharpness
- [x] video stabilisation
- [x] shutter speed
- [x] ISO
- [x] exposure mode
- [x] exposure metering mode
- [x] exposure compensation
- [x] dynamic range compression strength
- [x] automatic white balance modes
- [ ] automatic white balance red/blue gain
- [-] image effects (most, but not all, are working)
- [ ] image effect parameters
- [ ] colour effects
- [x] flip horizontally/vertically
- [x] rotation (90 degree steps)
- [ ] crop (region of interest)
- [ ] burst capture mode
- [ ] EXIF tags
- [x] initial capture delay (time for the sensor to 'settle')
- [ ] raw capture

Known Issues
------------

Important!

The native image encoder sometimes fails to send the final frame of image data.
I have no idea why this intermittently fails, it seems to happen roughly 10%
of the time. At the present time this will cause a hang in the encoder buffer
callback function, requiring SIGKILL to terminate the Java application.

Warning messages are logged when the native camera resources are freed up, e.g.
destroying the connection between the camera and the encoder causes a warning
to be logged stating that a port can not be disabled (because it is already
disabled).

Sometimes further warning messages are logged to the native console during
cleanup of the native resources, e.g. attempting to disable a port reports
success but the port is still enabled - it looks like a race condition in
native code.

Trademark Acknowledgement
-------------------------

Raspberry Pi is a trademark of the Raspberry Pi Foundation.
