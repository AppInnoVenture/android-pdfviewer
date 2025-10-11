package com.infomaniak.lib.pdfview.util;

import android.view.MotionEvent;
import android.view.View;
import android.view.ViewParent;

import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.infomaniak.lib.pdfview.PDFView;

/**
 * Utility class for handling touch events in the PDF viewer, managing touch priority between views.
 */
public final class TouchUtils {
    public static final int DIRECTION_SCROLLING_LEFT = -1;
    public static final int DIRECTION_SCROLLING_RIGHT = 1;
    public static final int DIRECTION_SCROLLING_TOP = -1;
    public static final int DIRECTION_SCROLLING_BOTTOM = 1;

    private TouchUtils() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * Manages touch event priority for a view, controlling whether parent views (e.g., RecyclerView or ViewPager2)
     * should intercept touch events based on scrolling or zooming behavior.
     *
     * @param event                   The MotionEvent to process.
     * @param view                    The view receiving the touch event.
     * @param pointerCount            The minimum number of pointers for multi-touch gestures.
     * @param shouldOverrideTouchPriority If true, overrides default touch priority logic.
     * @param isZooming               If true, indicates a zooming gesture is active.
     * @throws IllegalArgumentException if event or view is null.
     */
    public static void handleTouchPriority(@NonNull MotionEvent event, @NonNull View view,
                                          int pointerCount, boolean shouldOverrideTouchPriority,
                                          boolean isZooming) {
        if (event == null || view == null) {
            throw new IllegalArgumentException("Event and view cannot be null");
        }

        ViewParent viewToDisableTouch = getViewToDisableTouch(view);
        if (viewToDisableTouch == null) {
            return;
        }

        // Check if the view can scroll in both directions
        boolean canScrollHorizontally = view.canScrollHorizontally(DIRECTION_SCROLLING_RIGHT) &&
                view.canScrollHorizontally(DIRECTION_SCROLLING_LEFT);
        boolean canScrollVertically = view.canScrollVertically(DIRECTION_SCROLLING_TOP) &&
                view.canScrollVertically(DIRECTION_SCROLLING_BOTTOM);

        if (shouldOverrideTouchPriority) {
            viewToDisableTouch.requestDisallowInterceptTouchEvent(false);
            ViewParent viewPager = getViewPager(view);
            if (viewPager != null) {
                viewPager.requestDisallowInterceptTouchEvent(true);
            }
        } else if (event.getPointerCount() >= pointerCount || canScrollHorizontally || canScrollVertically) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_UP:
                    viewToDisableTouch.requestDisallowInterceptTouchEvent(false);
                    break;
                case MotionEvent.ACTION_DOWN:
                case MotionEvent.ACTION_MOVE:
                    if (isZooming || canScrollHorizontally || canScrollVertically) {
                        viewToDisableTouch.requestDisallowInterceptTouchEvent(true);
                    }
                    break;
            }
        }
    }

    /**
     * Finds the nearest RecyclerView parent of the given view to manage touch interception.
     *
     * @param startingView The view to start searching from.
     * @return The RecyclerView parent, or null if none found.
     */
    private static ViewParent getViewToDisableTouch(View startingView) {
        ViewParent parent = startingView.getParent();
        while (parent != null && !(parent instanceof RecyclerView)) {
            parent = parent.getParent();
        }
        return parent;
    }

    /**
     * Finds the nearest ViewPager2 parent of the given view to manage touch interception.
     *
     * @param startingView The view to start searching from.
     * @return The ViewPager2 parent, or null if none found.
     */
    private static ViewParent getViewPager(View startingView) {
        ViewParent parent = startingView.getParent();
        while (parent != null && !(parent instanceof ViewPager2)) {
            parent = parent.getParent();
        }
        return parent;
    }
}