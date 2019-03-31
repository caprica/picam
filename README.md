*You are currently looking at the development branch for picam-2.0.0, if you are looking for the previous version of
picam you should switch to the [picam-1.x branch](https://github.com/caprica/picam/tree/picam-1.x).*

**At the moment this master branch for 2.0.0 is somewhat experimental in that it uses JNI rather than JNA to access the
camera and there is a little bit more involved in getting up and running.**

*If you want to play it safe, use the [picam-1.x branch](https://github.com/caprica/picam/tree/picam-1.x) instead, if
you want to experiment, test, or help improve the JNI solution then by all means this master branch is for you!*

picam
=====

An easy-to-use Open Source Java library to access the [Raspberry Pi](https://www.raspberrypi.org/)
[camera module](https://www.raspberrypi.org/products/camera-module).

This library provides a direct Java API to the camera - behind the scenes the *native* MMAL library is used.

This library does *not* require any external native processes nor does it wrap any native executable program.

The implementation is based loosely on that used by the native `RaspiStill` utility.

This project is unofficial and is not affiliated in any way with the Raspberry Pi Foundation.

News
----

Picam 2.0.0 brings a pure JNI implementation rather than using JNA, this should give a modest but notable performance
boost compared to the JNA implementation in picam-1.x.

Version 2.0.0+ of picam brings some small API changes, you may have to make some minor adjustments to your code if you
decide to move to the new version.

Release are available at [Maven Cental](https://search.maven.org/search?q=a:picam).

Installation
------------

*Currently there is no 2.0.0 release available as it is still undergoing development and testing, you will have to build
the project yourself for the time being.*

Add the following Maven dependency to your project:

    <dependencies>
        <dependency>
            <groupId>uk.co.caprica</groupId>
            <artifactId>picam</artifactId>
            <version>2.0.0</version>
        </dependency>
    </dependencies>

No other dependencies are necessary.

Since version 2+ of picam uses JNI directly, you must also install the
[picam native library](https://github.com/caprica/picam-native).

In the final release of picam 2.x the JNI native library may be bundled with the picam jar file, but for now it is not.

Basic Usage
-----------

The first thing to do is to load the picam JNI native library, this is loaded as any other native library, one easy way
to do so is this:

```
    System.load("/home/pi/whatever-path-you-want/picam.so");
```

You could get this path from the current directory, a configuration file, a system property or whatever you want.

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
Captured images can be directly saved to disk, or returned and processed as a `byte[]`.

The above example used a `FilePictureCaptureHandler` that saves the captured picture directly to disk. You are free to
provide your own implementations of a `PictureCaptureHandler` to suit your own needs.

That example also created a camera, took only a single picture and automatically cleaned up the camera since `Camera`
implements `AutoCloseable`. There is no reason why you couldn't keep a camera component and take multiple pictures
before finally closing the camera yourself. It is important that whichever approach you use you close the camera when
you are finished using it to free up the camera and associated resources.

If the colouration of your captured pictures looks a bit "off", try setting a `delay` value when you take the picture.
The delay value is used to give the camera sensor time to "settle" before capturing the image. Even a delay as small as
5ms can make a significant difference. A longer delay for the first capture is sometimes needed.

You can specify the delay like this:

```
try (Camera camera = new Camera(config)) {
    camera.takePicture(new FilePictureCaptureHandler(new File("picam-1.jpg")), 3000);
    camera.takePicture(new FilePictureCaptureHandler(new File("picam-2.jpg")));
}
```
This example code fragment shows waiting 3 seconds (3,000 milliseconds) for the first picture, then no delay for the
second picture.

The `Camera` instance is obviously *not thread-safe*. You must not attempt to use the camera from multiple threads at
the same time.

Tutorials
---------

*Tutorials are currently for picam-1.x, new tutorials will be provided when picam-2.0.0 is released.*

Some new tutorials are available [here](http://capricasoftware.co.uk/projects/picam/tutorials).

Status
------

The current API is stable but nevertheless is still subject to change.

Hundres of thousands of images have been captured, but you might like to have a look at [this issue](https://github.com/caprica/picam/issues/9).

Feedback is welcome at the [github project](https://github.com/caprica/picam).

Most major/useful camera features or effects are implemented:

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
- [x] crop (region of interest)
- [x] initial capture delay (time for the sensor to 'settle')

Trademark Acknowledgement
-------------------------

Raspberry Pi is a trademark of the Raspberry Pi Foundation.

Demo Application
----------------

This screenshot shows a Java web application running on the Pi:

![picam-demo](https://github.com/caprica/picam/raw/master/etc/demo.png "picam-demo")
