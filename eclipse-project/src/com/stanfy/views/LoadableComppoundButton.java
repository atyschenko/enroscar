package com.stanfy.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.AttributeSet;
import android.widget.CompoundButton;

import com.stanfy.images.ImagesManagerContext;

/**
 * Compound button that can have a drawable to be drawn and scaled on the center.
 * @author Roman Mazur - Stanfy (http://www.stanfy.com)
 */
public class LoadableComppoundButton extends CompoundButton {

  /** Button resource. */
  private int buttonResource;

  /** Buttton drawable. */
  private Drawable buttonDrawable;

  /** Images manager context. */
  private ImagesManagerContext<?> imagesManagerContext;

  public LoadableComppoundButton(final Context context) {
    this(context, null);
  }

  public LoadableComppoundButton(final Context context, final AttributeSet attrs) {
    this(context, attrs, 0);
  }

  public LoadableComppoundButton(final Context context, final AttributeSet attrs, final int defStyle) {
    super(context, attrs, defStyle);

    final TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.LoadableComppoundButton);
    final Drawable d = a.getDrawable(R.styleable.LoadableComppoundButton_android_src);
    if (d != null) { setButtonDrawable(d); }
    a.recycle();
  }

  /** @param imagesManagerContext the imagesManager context to set */
  public void setImagesManagerContext(final ImagesManagerContext<?> imagesManagerContext) {
    this.imagesManagerContext = imagesManagerContext;
  }

  @Override
  public void setButtonDrawable(final int resid) {
    if (resid != 0 && resid == buttonResource) { return; }

    buttonResource = resid;

    Drawable d = null;
    if (buttonResource != 0) {
      d = getResources().getDrawable(buttonResource);
    }
    setButtonDrawable(d);
  }

  @Override
  public void setButtonDrawable(final Drawable d) {
    if (d != null) {
      if (buttonDrawable != null) {
        buttonDrawable.setCallback(null);
        unscheduleDrawable(buttonDrawable);
      }
      d.setCallback(this);
      if (d.isStateful()) { d.setState(getDrawableState()); }
      d.setVisible(getVisibility() == VISIBLE, false);
      buttonDrawable = d;
      buttonDrawable.setState(null);
    }

    refreshDrawableState();
  }

  public void setButtonDrawableUri(final Uri uri) {
    if (imagesManagerContext != null && uri != null && uri.getScheme().startsWith("http")) {
      imagesManagerContext.populateCompoundButton(this, uri.toString());
    }
  }

  @Override
  protected void drawableStateChanged() {
    super.drawableStateChanged();

    if (buttonDrawable != null) {
      final int[] myDrawableState = getDrawableState();

      // Set the state of the Drawable
      buttonDrawable.setState(myDrawableState);

      invalidate();
    }
  }

  @Override
  protected boolean verifyDrawable(final Drawable who) { return super.verifyDrawable(who) || who == buttonDrawable; }

  @Override
  protected void onDraw(final Canvas canvas) {
    super.onDraw(canvas);
    final Drawable d = buttonDrawable;
    if (d == null) { return; }
    int l = getPaddingLeft(), t = getPaddingTop();
    final int r = getPaddingRight(), b = getPaddingBottom();
    final int w = d.getIntrinsicWidth(), h = d.getIntrinsicHeight(), ww = getWidth() - l - r, hh = getHeight() - t - b;
    if (w > ww || h > hh) {
      d.setBounds(l, t, l + ww, t + hh);
    } else {
      l += (ww - w) >> 1;
      t += (hh - h) >> 1;
      d.setBounds(l, t, l + w, t + h);
    }
    d.draw(canvas);
  }

}