/*
 * This file is part of picam.
 *
 * picam is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * picam is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with picam.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Copyright 2016-2019 Caprica Software Limited.
 */

package uk.co.caprica.picam;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Collections;

/**
 * Helper class to extract the bundled native library file and install it to a local directory.
 * <p>
 * You can use this class to install the native library to the system temporary directory (the native library will be
 * automatically deleted when your application exits) or to any other directory you may like to specify.
 * <p>
 * When installing to a specific directory, you have the option to keep or overwrite the existing file.
 * <p>
 * For example, the simplest case:
 * <pre>
 * PicamNativeLibrary.installTempLibrary();
 * </pre>
 * Installing to a specific directory:
 * <pre>
 * PicamNativeLibrary.installLibrary("/home/pi");
 * </pre>
 */
public final class PicamNativeLibrary {

    /**
     * Name of the "directory" within the bundle that contains the native library.
     */
    private static final String SOURCE_PREFIX = "/native";

    /**
     * Filename suffix for the native library.
     */
    private static final String LIBRARY_SUFFIX = ".so";

    /**
     * Flag if the native library has already been installed.
     */
    private static boolean installed;

    /**
     * Install the native library to a temporary directory.
     * <p>
     * On successful installation, the native library will be loaded.
     * <p>
     * The installed library file will be marked for deletion when the JVM exits.
     *
     * @return full path of the installed native library file
     * @throws NativeLibraryException if the native library could not be installed or loaded
     */
    public static Path installTempLibrary() throws NativeLibraryException {
        Path installPath = installLibrary(Paths.get(System.getProperty("java.io.tmpdir")), true);
        installPath.toFile().deleteOnExit();
        return installPath;
    }

    /**
     * Install the native library to a specific directory.
     * <p>
     * The native library will <strong>not</strong> be replaced if it already exists in the specified directory (only
     * filenames are compared).
     * <p>
     * On successful installation, the native library will be loaded.
     *
     * @param installDirectory full path to the directory where the native library is to be installed
     * @return full path of the installed native library file
     * @throws NativeLibraryException if the native library could not be installed or loaded
     */
    public static Path installLibrary(String installDirectory) throws NativeLibraryException {
        return installLibrary(Paths.get(installDirectory));
    }

    /**
     * Install the native library to a specific directory, optionally overwriting it if it already exists.
     * <p>
     * The native library, if it exists, will be replaced or not according to the overwrite parameter.
     * <p>
     * On successful installation, the native library will be loaded.
     * <p>
     * The classpath will be searched for the native library to install, the library is expected to exist in a "/native"
     * directory off the classpath. The native library must be the only file in that directory with a name that has a
     * ".so" suffix.
     * <p>
     * This will work for either a file-based classpath or a jar classpath.
     *
     * @param installDirectory full path to the directory where the native library is to be installed
     * @param overwrite <code>true</code> if any existing native library should be replaced; otherwise <code>false</code>
     * @return full path of the installed native library file
     * @throws NativeLibraryException if the native library could not be installed or loaded
     */
    public static Path installLibrary(String installDirectory, boolean overwrite) throws NativeLibraryException {
        return installLibrary(Paths.get(installDirectory), overwrite);
    }

    /**
     * Install the native library to a specific directory.
     * <p>
     * The native library will <strong>not</strong> be replaced if it already exists in the specified directory (only
     * filenames are compared).
     * <p>
     * On successful installation, the native library will be loaded.
     *
     * @param installDirectory full path to the directory where the native library is to be installed
     * @return full path of the installed native library file
     * @throws NativeLibraryException if the native library could not be installed or loaded
     */
    public static Path installLibrary(Path installDirectory) throws NativeLibraryException {
        return installLibrary(installDirectory, false);
    }

    /**
     * Install the native library to a specific directory, optionally overwriting it if it already exists.
     * <p>
     * The native library, if it exists, will be replaced or not according to the overwrite parameter.
     * <p>
     * On successful installation, the native library will be loaded.
     * <p>
     * The classpath will be searched for the native library to install, the library is expected to exist in a "/native"
     * directory off the classpath. The native library must be the only file in that directory with a name that has a
     * ".so" suffix.
     * <p>
     * This will work for either a file-based classpath or a jar classpath.
     *
     * @param installDirectory full path to the directory where the native library is to be installed
     * @param overwrite <code>true</code> if any existing native library should be replaced; otherwise <code>false</code>
     * @return full path of the installed native library file
     * @throws NativeLibraryException if the native library could not be installed or loaded
     */
    public static Path installLibrary(Path installDirectory, boolean overwrite) throws NativeLibraryException {
        if (!installed) {
            try {
                URL containerUrl = PicamNativeLibrary.class.getResource(SOURCE_PREFIX);
                String protocol = containerUrl.getProtocol();
                if ("file".equalsIgnoreCase(protocol)) {
                    return installLibraryFromPath(Paths.get(containerUrl.toURI()), installDirectory, overwrite);
                } else if ("jar".equalsIgnoreCase(protocol)) {
                    try (FileSystem fs = FileSystems.newFileSystem(containerUrl.toURI(), Collections.<String, Object>emptyMap())) {
                        return installLibraryFromPath(fs.getPath(SOURCE_PREFIX), installDirectory, overwrite);
                    }
                } else {
                    throw new NativeLibraryException(String.format("Unexpected scheme '%s'", protocol));
                }
            }
            catch (URISyntaxException | IOException | UnsatisfiedLinkError e) {
                throw new NativeLibraryException("Failed to install native library", e);
            }
        } else {
            throw new NativeLibraryException("Native library already installed");
        }
    }

    /**
     * Install the native library.
     *
     * @param sourcePath
     * @param installPath full path to the directory where the native library is to be installed
     * @param overwrite <code>true</code> if any existing native library should be replaced; otherwise <code>false</code>
     * @return full path of the installed native library file
     * @throws IOException if a general IO error occurred
     * @throws UnsatisfiedLinkError if the extract native library could not be loaded
     */
    private static Path installLibraryFromPath(Path sourcePath, Path installPath, boolean overwrite) throws IOException, UnsatisfiedLinkError {
        Path sourceFilePath = Files.list(sourcePath).filter(path -> path.getFileName().toString().endsWith(LIBRARY_SUFFIX)).findFirst().orElseThrow(() -> new IOException("Failed to find native library to extract"));
        Path installFilePath = installPath.resolve(sourceFilePath.getFileName().toString());
        if (overwrite || Files.notExists(installFilePath)) {
            Files.copy(sourceFilePath, installFilePath, StandardCopyOption.REPLACE_EXISTING);
            if (Files.notExists(installFilePath)) {
                throw new IOException(String.format("Failed to copy native library to '%s'", installFilePath));
            }
        }
        System.load(installFilePath.toFile().getAbsolutePath());
        installed = true;
        return installFilePath;
    }

    private PicamNativeLibrary() {
    }

}
