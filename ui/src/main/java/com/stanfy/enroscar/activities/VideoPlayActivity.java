package com.stanfy.enroscar.activities;

import com.stanfy.enroscar.fragments.VideoPlayFragment;

import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.widget.MediaController;
import android.widget.VideoView;

/**
 * Activity to play video. It does not contain action bar.
 * @author Roman Mazur (Stanfy - http://www.stanfy.com)
 * @author Michael Pustovit - video controls (Stanfy - http://www.stanfy.com)
 */
public class VideoPlayActivity extends OneFragmentActivity {

  /** Fragment. */
  private VideoPlayFragment fragment;

  @Override
  protected int getFragmentContainerId() { return android.R.id.content; }
  @Override
  protected int getLayoutId() { return 0; }

  @Override
  protected Fragment createFragment(final Bundle savedInstanceState) {
    final Uri uri = getIntent().getData();
    if (uri == null) {
      throw new IllegalArgumentException("Video URI is not specified");
    }
    fragment = VideoPlayFragment.create(uri);
    return fragment;
  }

  @Override
  public void onConfigurationChanged(final Configuration newConfig) {
    super.onConfigurationChanged(newConfig);
    if (fragment != null) { fragment.invalidateView(); }
  }

  /**
   * @return the fragment video view
   */
  public VideoView getVideoView() {
    final VideoPlayFragment videoPlayFragment = (VideoPlayFragment) getFragment();

    if (videoPlayFragment == null) {
      throw new IllegalStateException("Fragment hasn't been created yet.");
    }

    return videoPlayFragment.getVideoView();
  }

  /**
   * @return the fragment media controller
   */
  public MediaController getVideoController() {
    final VideoPlayFragment videoPlayFragment = (VideoPlayFragment) getFragment();

    if (videoPlayFragment == null) {
      throw new IllegalStateException("Fragment hasn't been created yet.");
    }

    return videoPlayFragment.getMediaController();
  }
}
