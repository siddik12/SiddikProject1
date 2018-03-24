package com.rockscoder.easyphotofilters.interfaces;

/**
 * Created by Siddiqur on 3/24/2018.
 */

public interface EditImageFragmentListener {
    void onBrightnessChanged(int brightness);

    void onSaturationChanged(float saturation);

    void onContrastChanged(float contrast);

    void onEditStarted();

    void onEditCompleted();
}
