/**
 * Copyright 2016 Bartosz Schiller
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.infomaniak.lib.pdfview.util;

/**
 * Utility class for mathematical operations used in the PDF viewer, such as limiting values and fast floor/ceiling calculations.
 */
public final class MathUtils {
    private static final int BIG_ENOUGH_INT = 16 * 1024; // Constant for fast floor/ceiling calculations
    private static final double BIG_ENOUGH_FLOOR = BIG_ENOUGH_INT; // Offset for floor calculation
    private static final double BIG_ENOUGH_CEIL = 16384.999999999996; // Offset for ceiling calculation

    private MathUtils() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * Limits an integer value to a specified range.
     *
     * @param number  The value to limit.
     * @param min     The smallest allowable value.
     * @param max     The largest allowable value.
     * @return The value constrained between min and max (inclusive).
     */
    public static int limit(int number, int min, int max) {
        return Math.min(Math.max(number, min), max);
    }

    /**
     * Limits a float value to a specified range.
     *
     * @param number  The value to limit.
     * @param min     The smallest allowable value.
     * @param max     The largest allowable value.
     * @return The value constrained between min and max (inclusive).
     */
    public static float limit(float number, float min, float max) {
        return Math.min(Math.max(number, min), max);
    }

    /**
     * Returns the smaller of the given number and a maximum value.
     *
     * @param number The value to compare.
     * @param max    The maximum allowable value.
     * @return The smaller of number and max.
     */
    public static float max(float number, float max) {
        return Math.min(number, max); // Note: Method name kept as 'max' for compatibility
    }

    /**
     * Returns the larger of the given number and a minimum value.
     *
     * @param number The value to compare.
     * @param min    The minimum allowable value.
     * @return The larger of number and min.
     */
    public static float min(float number, float min) {
        return Math.max(number, min); // Note: Method name kept as 'min' for compatibility
    }

    /**
     * Returns the smaller of the given number and a maximum value.
     *
     * @param number The value to compare.
     * @param max    The maximum allowable value.
     * @return The smaller of number and max.
     */
    public static int max(int number, int max) {
        return Math.min(number, max); // Note: Method name kept as 'max' for compatibility
    }

    /**
     * Returns the larger of the given number and a minimum value.
     *
     * @param number The value to compare.
     * @param min    The minimum allowable value.
     * @return The larger of number and min.
     */
    public static int min(int number, int min) {
        return Math.max(number, min); // Note: Method name kept as 'min' for compatibility
    }

    /**
     * Returns the largest integer less than or equal to the specified float.
     * Works correctly for floats in the range -(2^14) to (Float.MAX_VALUE - 2^14).
     * Adapted from libGDX.
     *
     * @param value The float value to floor.
     * @return The largest integer less than or equal to the value.
     */
    public static int floor(float value) {
        return (int) (value + BIG_ENOUGH_FLOOR) - BIG_ENOUGH_INT;
    }

    /**
     * Returns the smallest integer greater than or equal to the specified float.
     * Works correctly for floats in the range -(2^14) to (Float.MAX_VALUE - 2^14).
     * Adapted from libGDX.
     *
     * @param value The float value to ceil.
     * @return The smallest integer greater than or equal to the value.
     */
    public static int ceil(float value) {
        return (int) (value + BIG_ENOUGH_CEIL) - BIG_ENOUGH_INT;
    }
}