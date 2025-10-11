/**
 * Copyright 2016 Bartosz Schiller
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.infomaniak.lib.pdfview.util;

import java.util.ArrayList;
import java.util.List;

public class ArrayUtils {

    private ArrayUtils() {
        // Prevents instantiation
    }

    /**
     * Removes duplicates from a sorted array, e.g., (0,1,2,2,3) to (0,1,2,3)
     */
    public static int[] deleteDuplicatedPages(int[] pages) {
        if (pages == null || pages.length == 0) {
            return pages == null ? new int[0] : pages;
        }

        // Count unique elements
        int uniqueCount = 1;
        for (int i = 1; i < pages.length; i++) {
            if (pages[i] != pages[i - 1]) {
                uniqueCount++;
            }
        }

        // Create result array and copy unique elements
        int[] result = new int[uniqueCount];
        result[0] = pages[0];
        int index = 1;
        for (int i = 1; i < pages.length; i++) {
            if (pages[i] != pages[i - 1]) {
                result[index++] = pages[i];
            }
        }
        return result;
    }

    /**
     * Transforms (0, 4, 4, 6, 6, 6, 3) into (0, 1, 1, 2, 2, 2, 3)
     */
    public static int[] calculateIndexesInDuplicateArray(int[] originalUserPages) {
        if (originalUserPages == null || originalUserPages.length == 0) {
            return new int[0];
        }

        int[] result = new int[originalUserPages.length];
        int index = 0;
        result[0] = 0;
        for (int i = 1; i < originalUserPages.length; i++) {
            if (originalUserPages[i] != originalUserPages[i - 1]) {
                index++;
            }
            result[i] = index;
        }
        return result;
    }

    /**
     * Converts an array to a string representation, e.g., [1,2,3]
     */
    public static String arrayToString(int[] array) {
        if (array == null || array.length == 0) {
            return "[]";
        }
        return Arrays.toString(array);
    }
}