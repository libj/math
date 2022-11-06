/* Copyright (c) 2020 Seva Safris, LibJ
 * Copyright (c) 2015-2016 Simon Klein, Google Inc.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE AUTHOR AND CONTRIBUTORS "AS IS" AND ANY
 * EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE AUTHOR OR CONTRIBUTORS BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * The views and conclusions contained in the software and documentation are
 * those of the authors and should not be interpreted as representing official
 * policies, either expressed or implied, of the Huldra and the LibJ projects.
 */

package org.libj.math;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.management.ManagementFactory;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

import org.libj.lang.OperatingSystem;
import org.libj.lang.Systems;

final class NativeMath {
  enum Mode {
    JAVA,
    NATIVE,
    CRITICAL
  }

  private static Mode mode;

  static Mode loadNative() {
    if (mode != null)
      return mode;

    if (Systems.hasProperty("org.libj.math.noNative"))
      return Mode.JAVA;

    final boolean useCritical = ManagementFactory.getRuntimeMXBean().getInputArguments().toString().indexOf("-Xcomp") > 0;

    final String arch = System.getProperty("os.arch");

    final String fileName = "libmath" + (useCritical ? "c" : "j");
    final String extension;
    final OperatingSystem operatingSystem = OperatingSystem.get();
    if (operatingSystem.isMac())
      extension = ".dylib";
    else if (operatingSystem.isUnix())
      extension = ".so";
    else if (operatingSystem.isWindows())
      extension = ".dll";
    else
      throw new UnsupportedOperationException("Unsupported operating system: " + operatingSystem);

    final String resourceName = "/" + fileName + "_" + arch + extension;
    final URL url = NativeMath.class.getResource(resourceName);
    if (url != null) {
      final File file;
      try {
        if (url.toString().startsWith("jar:file:")) {
          final Path tempPath = Files.createTempFile(fileName, extension);
          file = tempPath.toFile();
          file.deleteOnExit();
          try (final InputStream in = url.openStream()) {
            Files.copy(in, tempPath, StandardCopyOption.REPLACE_EXISTING);
          }
        }
        else if (url.toString().startsWith("file:")) {
          file = new File(url.getPath());
        }
        else {
          throw new ExceptionInInitializerError("Unsupported protocol: " + url);
        }
      }
      catch (final IOException e) {
        throw new ExceptionInInitializerError(e);
      }

      try {
        System.load(file.getAbsolutePath());
      }
      catch (final UnsatisfiedLinkError e) {
        e.printStackTrace();
        System.err.println("Starting without JNI bindings");
      }
    }
    else {
      System.err.println("Not found: " + resourceName);
      System.err.println("Starting without JNI bindings");
    }

    return mode = useCritical ? Mode.CRITICAL : Mode.NATIVE;
  }

  private NativeMath() {
  }
}