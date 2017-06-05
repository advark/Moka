/*
 * Copyright (C) 2011-2017 Yanick Poirier
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published
 * by the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * More details about the licensing can be found at https://www.gnu.org/licenses/lgpl-3.0.en.html.
 */
package moka.util;

/**
 * This class contains several static methods for retrieving information about the Java
 * virtual machine and the underlying operating system.
 * <p>
 * Note that all the memory related methods relies to the Java virtual machine memory.
 * Unless otherwise noted, they have nothing to do with the actual physical or virtual
 * memory of the computer.
 *
 * @author Yanick Poirier
 */
public class JavaVM {

    /**
     * Returns the amount of free memory in the virtual machine.
     *
     * @return an approximation of the total number of bytes currently available for
     *         object allocation.
     */
    static public long getFreeMemory() {
        return Runtime.getRuntime().freeMemory();
    }

    /**
     * Returns the total amount of memory of the virtual machine. The value returned may
     * vary over time depending on the underlying operating system.
     *
     * @return total number of bytes available for current and future allocation.
     */
    static public long getTotalMemory() {
        return Runtime.getRuntime().totalMemory();
    }

    /**
     * Returns the maximum amount of memory the virtual machine will attempt to use.
     *
     * @return
     */
    static public long getMaxMemory() {
        return Runtime.getRuntime().maxMemory();
    }

    /**
     * Launches the garbage collector.
     */
    static public void gc() {
        Runtime.getRuntime().gc();
    }

    static public String getJavaVersion() {
        return System.getProperty( "java.version" );
    }

    static public String getJavaVendor() {
        return System.getProperty( "java.vendor" );
    }

    static public String getJavaHomeDir() {
        return System.getProperty( "java.home" );
    }

    static public String getVMSpecVersion() {
        return System.getProperty( "java.vm.specification.version" );
    }

    static public String getVMVersion() {
        return System.getProperty( "java.vm.version" );
    }

    /**
     * Returns the operating system's name.
     *
     * @return
     */
    static public String getOSName() {
        return System.getProperty( "os.name" );
    }

    /**
     * Checks if the current platform is of Windows family.
     *
     * @return {@code true} is the platform is of Windows family.
     */
    static public boolean isWindows() {
        return getOSName().startsWith( "Windows" );
    }

    /**
     * Checks if the current platform is of Linux family.
     *
     * @return {@code true} is the platform is of Linux family.
     */
    static public boolean isLinux() {
        return getOSName().startsWith( "Linux" );
    }

    /**
     * Returns the operating system's version.
     *
     * @return
     */
    static public String getOSVersion() {
        return System.getProperty( "os.version" );
    }

    /**
     * Returns the operating system's architecture.
     *
     * @return
     */
    static public String getOSArch() {
        return System.getProperty( "os.arch" );
    }

    static public String getLineSeparator() {
        return System.getProperty( "line.separator" );
    }

    /**
     * Retrieves the directory where the Java VM was launched.
     *
     * @return
     */
    static public String getStartupDir() {
        return System.clearProperty( "user.dir" );
    }

    private JavaVM() {
    }

}
