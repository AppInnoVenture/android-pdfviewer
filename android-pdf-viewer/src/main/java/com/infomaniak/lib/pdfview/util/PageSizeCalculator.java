/**
 * Copyright 2017 Bartosz Schiller
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

import com.shockwave.pdfium.util.Size;
import com.shockwave.pdfium.util.SizeF;

/**
 * Utility class for calculating optimal page sizes for PDF rendering based on a fitting policy.
 * This class determines how pages should be scaled to fit within a view, supporting width, height,
 * or both dimensions fitting policies.
 */
public class PageSizeCalculator {

    private final Size originalMaxWidthPageSize;
    private final Size originalMaxHeightPageSize;
    private final Size viewSize;
    private final FitPolicy fitPolicy;
    private final boolean fitEachPage;
    private SizeF optimalMaxWidthPageSize;
    private SizeF optimalMaxHeightPageSize;
    private float widthRatio;
    private float heightRatio;

    /**
     * Constructs a PageSizeCalculator with the specified fitting policy and page sizes.
     *
     * @param fitPolicy                Policy to determine how pages are scaled (WIDTH, HEIGHT, or BOTH).
     * @param originalMaxWidthPageSize Original size of the widest page.
     * @param originalMaxHeightPageSize Original size of the tallest page.
     * @param viewSize                 Size of the view where pages are rendered.
     * @param fitEachPage              If true, each page is scaled to fit the view; otherwise, uses calculated ratios.
     * @throws IllegalArgumentException if any input parameter is null.
     */
    public PageSizeCalculator(FitPolicy fitPolicy, Size originalMaxWidthPageSize,
                              Size originalMaxHeightPageSize, Size viewSize, boolean fitEachPage) {
        if (fitPolicy == null || originalMaxWidthPageSize == null ||
            originalMaxHeightPageSize == null || viewSize == null) {
            throw new IllegalArgumentException("Parameters cannot be null");
        }
        this.fitPolicy = fitPolicy;
        this.originalMaxWidthPageSize = originalMaxWidthPageSize;
        this.originalMaxHeightPageSize = originalMaxHeightPageSize;
        this.viewSize = viewSize;
        this.fitEachPage = fitEachPage;
        calculateMaxPages();
    }

    /**
     * Calculates the optimal size for a given page based on the fitting policy.
     *
     * @param pageSize The original size of the page to be scaled.
     * @return A SizeF object representing the scaled dimensions, or (0,0) if pageSize is invalid.
     */
    public SizeF calculate(Size pageSize) {
        if (pageSize == null || pageSize.getWidth() <= 0 || pageSize.getHeight() <= 0) {
            return new SizeF(0, 0);
        }
        float maxWidth = fitEachPage ? viewSize.getWidth() : pageSize.getWidth() * widthRatio;
        float maxHeight = fitEachPage ? viewSize.getHeight() : pageSize.getHeight() * heightRatio;
        switch (fitPolicy) {
            case HEIGHT:
                return fitHeight(pageSize, maxHeight);
            case BOTH:
                return fitBoth(pageSize, maxWidth, maxHeight);
            case WIDTH:
            default:
                return fitWidth(pageSize, maxWidth);
        }
    }

    /**
     * Gets the optimal size for the widest page after scaling.
     *
     * @return The scaled size of the widest page.
     */
    public SizeF getOptimalMaxWidthPageSize() {
        return optimalMaxWidthPageSize;
    }

    /**
     * Gets the optimal size for the tallest page after scaling.
     *
     * @return The scaled size of the tallest page.
     */
    public SizeF getOptimalMaxHeightPageSize() {
        return optimalMaxHeightPageSize;
    }

    /**
     * Calculates the optimal sizes and scaling ratios for the maximum width and height pages
     * based on the fitting policy and view size.
     */
    private void calculateMaxPages() {
        switch (fitPolicy) {
            case HEIGHT:
                // Fit the tallest page to the view height and derive width ratio
                optimalMaxHeightPageSize = fitHeight(originalMaxHeightPageSize, viewSize.getHeight());
                heightRatio = optimalMaxHeightPageSize.getHeight() / originalMaxHeightPageSize.getHeight();
                optimalMaxWidthPageSize = fitHeight(originalMaxWidthPageSize,
                        originalMaxWidthPageSize.getHeight() * heightRatio);
                widthRatio = optimalMaxWidthPageSize.getWidth() / originalMaxWidthPageSize.getWidth();
                break;
            case BOTH:
                // Fit the widest page to both view dimensions
                optimalMaxWidthPageSize = fitBoth(originalMaxWidthPageSize,
                        viewSize.getWidth(), viewSize.getHeight());
                widthRatio = optimalMaxWidthPageSize.getWidth() / originalMaxWidthPageSize.getWidth();
                // Apply the width ratio to the tallest page
                optimalMaxHeightPageSize = fitBoth(originalMaxHeightPageSize,
                        originalMaxHeightPageSize.getWidth() * widthRatio, viewSize.getHeight());
                heightRatio = optimalMaxHeightPageSize.getHeight() / originalMaxHeightPageSize.getHeight();
                break;
            case WIDTH:
            default:
                // Fit the widest page to the view width and derive height ratio
                optimalMaxWidthPageSize = fitWidth(originalMaxWidthPageSize, viewSize.getWidth());
                widthRatio = optimalMaxWidthPageSize.getWidth() / originalMaxWidthPageSize.getWidth();
                optimalMaxHeightPageSize = fitWidth(originalMaxHeightPageSize,
                        originalMaxHeightPageSize.getWidth() * widthRatio);
                heightRatio = optimalMaxHeightPageSize.getHeight() / originalMaxHeightPageSize.getHeight();
                break;
        }
    }

    /**
     * Scales a page to fit a specified width while maintaining aspect ratio.
     *
     * @param pageSize The original page size.
     * @param maxWidth The target width to fit.
     * @return A SizeF object with the scaled dimensions.
     */
    private SizeF fitWidth(Size pageSize, float maxWidth) {
        float ratio = (float) pageSize.getWidth() / pageSize.getHeight();
        float width = maxWidth;
        float height = width / ratio;
        return new SizeF(width, height);
    }

    /**
     * Scales a page to fit a specified height while maintaining aspect ratio.
     *
     * @param pageSize The original page size.
     * @param maxHeight The target height to fit.
     * @return A SizeF object with the scaled dimensions.
     */
    private SizeF fitHeight(Size pageSize, float maxHeight) {
        float ratio = (float) pageSize.getHeight() / pageSize.getWidth();
        float height = maxHeight;
        float width = height / ratio;
        return new SizeF(width, height);
    }

    /**
     * Scales a page to fit both a specified width and height, using the smaller scaling factor
     * to ensure the entire page fits within the view.
     *
     * @param pageSize The original page size.
     * @param maxWidth The target width to fit.
     * @param maxHeight The target height to fit.
     * @return A SizeF object with the scaled dimensions.
     */
    private SizeF fitBoth(Size pageSize, float maxWidth, float maxHeight) {
        float ratio = (float) pageSize.getWidth() / pageSize.getHeight();
        float width = maxWidth;
        float height = width / ratio;
        if (height > maxHeight) {
            height = maxHeight;
            width = height * ratio;
        }
        return new SizeF(width, height);
    }
}