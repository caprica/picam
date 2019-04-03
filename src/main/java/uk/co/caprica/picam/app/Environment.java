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

package uk.co.caprica.picam.app;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Helper class to dump out some environment diagnostics information.
 */
public final class Environment {

    private static final String[] ENV_KEYS = {
        "java.home",
        "java.version",
        "java.runtime.name",
        "java.runtime.version",
        "java.vm.info",
        "java.vm.name",
        "java.vm.version",
        "os.version"
    };

    /**
     * Dump some environment parameters to the standard output.
     */
    public static void dumpEnvironment() {
        System.out.println("Environment:");
        for (String key : ENV_KEYS) {
            System.out.printf(" %-20s: %s%n", key, System.getProperty(key));
        }
        System.out.printf(" %-20s: %s%n", "/etc/issue", issue());
        System.out.println();
    }

    private static String issue() {
        try {
            return String.join("\n", Files.readAllLines(Paths.get("/etc/issue"))).trim();
        }
        catch (IOException e) {
            return e.getMessage();
        }
    }

    private Environment()  {
    }

    public static void main(String[] args) {
        dumpEnvironment();
    }

}
