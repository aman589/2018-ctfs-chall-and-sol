package android.support.v7.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.Drawable;
import android.os.Build.VERSION;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.annotation.RestrictTo;
import android.support.annotation.RestrictTo.Scope;
import android.support.v4.view.TintableBackgroundView;
import android.support.v4.widget.AutoSizeableTextView;
import android.support.v7.appcompat.R;
import android.util.AttributeSet;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.Button;

public class AppCompatButton extends Button implements TintableBackgroundView, AutoSizeableTextView {
    private final AppCompatBackgroundHelper mBackgroundTintHelper;
    private final AppCompatTextHelper mTextHelper;

    public AppCompatButton(Context context) {
        this(context, null);
    }

    public AppCompatButton(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.buttonStyle);
    }

    public AppCompatButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(TintContextWrapper.wrap(context), attrs, defStyleAttr);
        this.mBackgroundTintHelper = new AppCompatBackgroundHelper(this);
        this.mBackgroundTintHelper.loadFromAttributes(attrs, defStyleAttr);
        this.mTextHelper = AppCompatTextHelper.create(this);
        this.mTextHelper.loadFromAttributes(attrs, defStyleAttr);
        this.mTextHelper.applyCompoundDrawablesTints();
    }

    public void setBackgroundResource(@DrawableRes int resId) {
        super.setBackgroundResource(resId);
        if (this.mBackgroundTintHelper != null) {
            this.mBackgroundTintHelper.onSetBackgroundResource(resId);
        }
    }

    public void setBackgroundDrawable(Drawable background) {
        super.setBackgroundDrawable(background);
        if (this.mBackgroundTintHelper != null) {
            this.mBackgroundTintHelper.onSetBackgroundDrawable(background);
        }
    }

    @RestrictTo({Scope.LIBRARY_GROUP})
    public void setSupportBackgroundTintList(@Nullable ColorStateList tint) {
        if (this.mBackgroundTintHelper != null) {
            this.mBackgroundTintHelper.setSupportBackgroundTintList(tint);
        }
    }

    @Nullable
    @RestrictTo({Scope.LIBRARY_GROUP})
    public ColorStateList getSupportBackgroundTintList() {
        return this.mBackgroundTintHelper != null ? this.mBackgroundTintHelper.getSupportBackgroundTintList() : null;
    }

    @RestrictTo({Scope.LIBRARY_GROUP})
    public void setSupportBackgroundTintMode(@Nullable Mode tintMode) {
        if (this.mBackgroundTintHelper != null) {
            this.mBackgroundTintHelper.setSupportBackgroundTintMode(tintMode);
        }
    }

    @Nullable
    @RestrictTo({Scope.LIBRARY_GROUP})
    public Mode getSupportBackgroundTintMode() {
        return this.mBackgroundTintHelper != null ? this.mBackgroundTintHelper.getSupportBackgroundTintMode() : null;
    }

    protected void drawableStateChanged() {
        super.drawableStateChanged();
        if (this.mBackgroundTintHelper != null) {
            this.mBackgroundTintHelper.applySupportBackgroundTint();
        }
        if (this.mTextHelper != null) {
            this.mTextHelper.applyCompoundDrawablesTints();
        }
    }

    public void setTextAppearance(Context context, int resId) {
        super.setTextAppearance(context, resId);
        if (this.mTextHelper != null) {
            this.mTextHelper.onSetTextAppearance(context, resId);
        }
    }

    public void onInitializeAccessibilityEvent(AccessibilityEvent event) {
        super.onInitializeAccessibilityEvent(event);
        event.setClassName(Button.class.getName());
    }

    @RequiresApi(14)
    public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo info) {
        super.onInitializeAccessibilityNodeInfo(info);
        info.setClassName(Button.class.getName());
    }

    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (this.mTextHelper != null) {
            this.mTextHelper.onLayout(changed, left, top, right, bottom);
        }
    }

    public void setTextSize(int unit, float size) {
        if (VERSION.SDK_INT >= 26) {
            super.setTextSize(unit, size);
        } else if (this.mTextHelper != null) {
            this.mTextHelper.setTextSize(unit, size);
        }
    }

    protected void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter) {
        super.onTextChanged(text, start, lengthBefore, lengthAfter);
        if (this.mTextHelper != null && VERSION.SDK_INT < 26 && this.mTextHelper.isAutoSizeEnabled()) {
            this.mTextHelper.autoSizeText();
        }
    }

    @RestrictTo({Scope.LIBRARY_GROUP})
    public void setAutoSizeTextTypeWithDefaults(int autoSizeTextType) {
        if (VERSION.SDK_INT >= 26) {
            super.setAutoSizeTextTypeWithDefaults(autoSizeTextType);
        } else if (this.mTextHelper != null) {
            this.mTextHelper.setAutoSizeTextTypeWithDefaults(autoSizeTextType);
        }
    }

    @RestrictTo({Scope.LIBRARY_GROUP})
    public void setAutoSizeTextTypeUniformWithConfiguration(int autoSizeMinTextSize, int autoSizeMaxTextSize, int autoSizeStepGranularity, int unit) throws IllegalArgumentException {
        if (VERSION.SDK_INT >= 26) {
            super.setAutoSizeTextTypeUniformWithConfiguration(autoSizeMinTextSize, autoSizeMaxTextSize, autoSizeStepGranularity, unit);
        } else if (this.mTextHelper != null) {
            this.mTextHelper.setAutoSizeTextTypeUniformWithConfiguration(autoSizeMinTextSize, autoSizeMaxTextSize, autoSizeStepGranularity, unit);
        }
    }

    @RestrictTo({Scope.LIBRARY_GROUP})
    public void setAutoSizeTextTypeUniformWithPresetSizes(@NonNull int[] presetSizes, int unit) throws IllegalArgumentException {
        if (VERSION.SDK_INT >= 26) {
            super.setAutoSizeTextTypeUniformWithPresetSizes(presetSizes, unit);
        } else if (this.mTextHelper != null) {
            this.mTextHelper.setAutoSizeTextTypeUniformWithPresetSizes(presetSizes, unit);
        }
    }

    @RestrictTo({Scope.LIBRARY_GROUP})
    public int getAutoSizeTextType() {
        int i = 0;
        if (VERSION.SDK_INT >= 26) {
            if (super.getAutoSizeTextType() == 1) {
                i = 1;
            }
            return i;
        } else if (this.mTextHelper != null) {
            return this.mTextHelper.getAutoSizeTextType();
        } else {
            return 0;
        }
    }

    @RestrictTo({Scope.LIBRARY_GROUP})
    public int getAutoSizeStepGranularity() {
        if (VERSION.SDK_INT >= 26) {
            return super.getAutoSizeStepGranularity();
        }
        if (this.mTextHelper != null) {
            return this.mTextHelper.getAutoSizeStepGranularity();
        }
        return -1;
    }

    @RestrictTo({Scope.LIBRARY_GROUP})
    public int getAutoSizeMinTextSize() {
        if (VERSION.SDK_INT >= 26) {
            return super.getAutoSizeMinTextSize();
        }
        if (this.mTextHelper != null) {
            return this.mTextHelper.getAutoSizeMinTextSize();
        }
        return -1;
    }

    @RestrictTo({Scope.LIBRARY_GROUP})
    public int getAutoSizeMaxTextSize() {
        if (VERSION.SDK_INT >= 26) {
            return super.getAutoSizeMaxTextSize();
        }
        if (this.mTextHelper != null) {
            return this.mTextHelper.getAutoSizeMaxTextSize();
        }
        return -1;
    }

    @RestrictTo({Scope.LIBRARY_GROUP})
    public int[] getAutoSizeTextAvailableSizes() {
        if (VERSION.SDK_INT >= 26) {
            return super.getAutoSizeTextAvailableSizes();
        }
        if (this.mTextHelper != null) {
            return this.mTextHelper.getAutoSizeTextAvailableSizes();
        }
        return new int[0];
    }

    public void setSupportAllCaps(boolean allCaps) {
        if (this.mTextHelper != null) {
            this.mTextHelper.setAllCaps(allCaps);
        }
    }
}
