/*
 * Copyright (C) 2016 Bartosz Schiller.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.infomaniak.lib.pdfview.util;

import android.content.Context;
import android.util.TypedValue;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Utility class for common operations in the PDF viewer, such as unit conversion and stream handling.
 */
public final class Util {
    private static final int DEFAULT_BUFFER_SIZE = 8192; // 8KB buffer for efficient I/O

    private Util() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * Converts density-independent pixels (dp) to actual pixels based on the device's display metrics.
     *
     * @param context The Android context to access display metrics.
     * @param dp      The value in density-independent pixels.
     * @return The equivalent value in pixels, rounded to the nearest integer.
     * @throws IllegalArgumentException if context is null.
     */
    public static int getDP(@NonNull Context context, float dp) {
        if (context == null) {
            throw new IllegalArgumentException("Context cannot be null");
        }
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                context.getResources().getDisplayMetrics());
    }

    /**
     * Converts an InputStream to a byte array.
     *
     * @param inputStream The input stream to read from.
     * @return A byte array containing the stream's data.
     * @throws IOException              if an I/O error occurs.
     * @throws IllegalArgumentException if inputStream is null.
     */
    public static byte[] toByteArray(@NonNull InputStream inputStream) throws IOException {
        if (inputStream == null) {
            throw new IllegalArgumentException("InputStream cannot be null");
        }
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
            return outputStream.toByteArray();
        }
    }
}