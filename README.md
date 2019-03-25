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

News
----

- 25th March, 2019 - new release 1.1.0 available, now uses JNA direct-mapping for native methods to give a modest
  performance boost
- 16th March, 2019 - new release 1.0.0 available

Release are available at [Maven Cental](https://search.maven.org/search?q=a:picam).

Installation
------------

Add the following Maven dependency to your project:

    <dependencies>
        <dependency>
            <groupId>uk.co.caprica</groupId>
            <artifactId>picam</artifactId>
            <version>1.1.0</version>
        </dependency>
    </dependencies>

Basic Usage
-----------

Using the library is simple, first you create a `CameraConfiguration` using a convenient "builder" approach:

```
CameraConfiguration config = cameraConfiguration()
    .width(1920)
    .height(1080)
    .encoding(Encoding.JPEG)
    .quality(85)
```
You can supply as much or as little configuration as you want, sensible defaults will be provided where needed.

Next, create a `Camera` with that configuration:
```
try (Camera camera = new Camera(config)) {
    camera.takePicture(new FilePictureCaptureHandler(new File("picam.jpg")));
}
```
Captured images can be directly saved to disk, or returned and processed as a
`byte[]`.

The above example used a `FilePictureCaptureHandler` that saves the captured picture directly to disk. You are
free to provide your own implementations of a `PictureCaptureHandler` to suit your own needs.

That example also created a camera, took only a single picture and automatically cleaned up the camera since `Camera`
implements `AutoCloseable`. There is no reason why you couldn't keep a camera component and take multiple pictures
before finally closing the camera yourself. It is important that whichever approach you use you close the camera when
you are finished using it to free up the camera and associated resources.

If the colouration of your captured images looks a bit "off", try setting a `delay` value on the `CameraConfiguration` - the
delay value is used to give the camera sensor time to "settle" before capturing the image. Even a delay as small as 5ms can
make a significant difference.

The `Camera` instance is obviously *not thread-safe*. You must not attempt to use the camera from multiple threads at
the same time.

Tutorials
---------

Some new tutorials are available [here](http://capricasoftware.co.uk/projects/picam/tutorials).

Status
------

The current API should be considered alpha, it is stable but nevertheless is still subject to change.

Thousands upon thousands of images have been captured with no problems.

Feedback is welcome at the [github project](https://github.com/caprica/picam).

Not all camera features or effects are implemented, the aim is to add more
support over time:

- [x] image width/height
- [x] image encoding
- [x] encoding quality (for JPEG encoding)
- [x] stereo mode
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
- [x] automatic white balance red/blue gain
- [x] image effects
- [x] colour effects
- [x] flip horizontally/vertically
- [x] rotation (90 degree steps)
- [ ] crop (region of interest)
- [ ] burst capture mode
- [ ] EXIF tags
- [x] initial capture delay (time for the sensor to 'settle')
- [ ] raw capture

Trademark Acknowledgement
-------------------------

Raspberry Pi is a trademark of the Raspberry Pi Foundation.

Demo Application
----------------

This screenshot shows a Java web application running on the Pi:

![picam-demo](https://github.com/caprica/picam/raw/master/etc/demo.png "picam-demo")
