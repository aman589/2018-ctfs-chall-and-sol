package android.support.v7.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.RectF;
import android.os.Build.VERSION;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RestrictTo;
import android.support.annotation.RestrictTo.Scope;
import android.support.v7.appcompat.R;
import android.text.Layout.Alignment;
import android.text.StaticLayout;
import android.text.StaticLayout.Builder;
import android.text.TextDirectionHeuristic;
import android.text.TextDirectionHeuristics;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.widget.TextView;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Hashtable;
import java.util.List;

class AppCompatTextViewAutoSizeHelper {
    private static final int DEFAULT_AUTO_SIZE_GRANULARITY_IN_PX = 1;
    private static final int DEFAULT_AUTO_SIZE_MAX_TEXT_SIZE_IN_SP = 112;
    private static final int DEFAULT_AUTO_SIZE_MIN_TEXT_SIZE_IN_SP = 12;
    private static final String TAG = "ACTVAutoSizeHelper";
    private static final RectF TEMP_RECTF = new RectF();
    static final float UNSET_AUTO_SIZE_UNIFORM_CONFIGURATION_VALUE = -1.0f;
    private static final int VERY_WIDE = 1048576;
    private static Hashtable<String, Method> sTextViewMethodByNameCache = new Hashtable();
    private float mAutoSizeMaxTextSizeInPx = UNSET_AUTO_SIZE_UNIFORM_CONFIGURATION_VALUE;
    private float mAutoSizeMinTextSizeInPx = UNSET_AUTO_SIZE_UNIFORM_CONFIGURATION_VALUE;
    private float mAutoSizeStepGranularityInPx = UNSET_AUTO_SIZE_UNIFORM_CONFIGURATION_VALUE;
    private int[] mAutoSizeTextSizesInPx = new int[0];
    private int mAutoSizeTextType = 0;
    private final Context mContext;
    private boolean mHasPresetAutoSizeValues = false;
    private boolean mNeedsAutoSizeText = false;
    private TextPaint mTempTextPaint;
    private final TextView mTextView;

    AppCompatTextViewAutoSizeHelper(TextView textView) {
        this.mTextView = textView;
        this.mContext = this.mTextView.getContext();
    }

    void loadFromAttributes(AttributeSet attrs, int defStyleAttr) {
        float autoSizeMinTextSizeInPx = UNSET_AUTO_SIZE_UNIFORM_CONFIGURATION_VALUE;
        float autoSizeMaxTextSizeInPx = UNSET_AUTO_SIZE_UNIFORM_CONFIGURATION_VALUE;
        float autoSizeStepGranularityInPx = UNSET_AUTO_SIZE_UNIFORM_CONFIGURATION_VALUE;
        TypedArray a = this.mContext.obtainStyledAttributes(attrs, R.styleable.AppCompatTextView, defStyleAttr, 0);
        if (a.hasValue(R.styleable.AppCompatTextView_autoSizeTextType)) {
            this.mAutoSizeTextType = a.getInt(R.styleable.AppCompatTextView_autoSizeTextType, 0);
        }
        if (a.hasValue(R.styleable.AppCompatTextView_autoSizeStepGranularity)) {
            autoSizeStepGranularityInPx = a.getDimension(R.styleable.AppCompatTextView_autoSizeStepGranularity, UNSET_AUTO_SIZE_UNIFORM_CONFIGURATION_VALUE);
        }
        if (a.hasValue(R.styleable.AppCompatTextView_autoSizeMinTextSize)) {
            autoSizeMinTextSizeInPx = a.getDimension(R.styleable.AppCompatTextView_autoSizeMinTextSize, UNSET_AUTO_SIZE_UNIFORM_CONFIGURATION_VALUE);
        }
        if (a.hasValue(R.styleable.AppCompatTextView_autoSizeMaxTextSize)) {
            autoSizeMaxTextSizeInPx = a.getDimension(R.styleable.AppCompatTextView_autoSizeMaxTextSize, UNSET_AUTO_SIZE_UNIFORM_CONFIGURATION_VALUE);
        }
        if (a.hasValue(R.styleable.AppCompatTextView_autoSizePresetSizes)) {
            int autoSizeStepSizeArrayResId = a.getResourceId(R.styleable.AppCompatTextView_autoSizePresetSizes, 0);
            if (autoSizeStepSizeArrayResId > 0) {
                TypedArray autoSizePreDefTextSizes = a.getResources().obtainTypedArray(autoSizeStepSizeArrayResId);
                setupAutoSizeUniformPresetSizes(autoSizePreDefTextSizes);
                autoSizePreDefTextSizes.recycle();
            }
        }
        a.recycle();
        if (!supportsAutoSizeText()) {
            this.mAutoSizeTextType = 0;
        } else if (this.mAutoSizeTextType == 1) {
            if (!this.mHasPresetAutoSizeValues) {
                DisplayMetrics displayMetrics = this.mContext.getResources().getDisplayMetrics();
                if (autoSizeMinTextSizeInPx == UNSET_AUTO_SIZE_UNIFORM_CONFIGURATION_VALUE) {
                    autoSizeMinTextSizeInPx = TypedValue.applyDimension(2, 12.0f, displayMetrics);
                }
                if (autoSizeMaxTextSizeInPx == UNSET_AUTO_SIZE_UNIFORM_CONFIGURATION_VALUE) {
                    autoSizeMaxTextSizeInPx = TypedValue.applyDimension(2, 112.0f, displayMetrics);
                }
                if (autoSizeStepGranularityInPx == UNSET_AUTO_SIZE_UNIFORM_CONFIGURATION_VALUE) {
                    autoSizeStepGranularityInPx = 1.0f;
                }
                validateAndSetAutoSizeTextTypeUniformConfiguration(autoSizeMinTextSizeInPx, autoSizeMaxTextSizeInPx, autoSizeStepGranularityInPx);
            }
            setupAutoSizeText();
        }
    }

    @RestrictTo({Scope.LIBRARY_GROUP})
    void setAutoSizeTextTypeWithDefaults(int autoSizeTextType) {
        if (supportsAutoSizeText()) {
            switch (autoSizeTextType) {
                case 0:
                    clearAutoSizeConfiguration();
                    return;
                case 1:
                    DisplayMetrics displayMetrics = this.mContext.getResources().getDisplayMetrics();
                    validateAndSetAutoSizeTextTypeUniformConfiguration(TypedValue.applyDimension(2, 12.0f, displayMetrics), TypedValue.applyDimension(2, 112.0f, displayMetrics), 1.0f);
                    if (setupAutoSizeText()) {
                        autoSizeText();
                        return;
                    }
                    return;
                default:
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("Unknown auto-size text type: ");
                    stringBuilder.append(autoSizeTextType);
                    throw new IllegalArgumentException(stringBuilder.toString());
            }
        }
    }

    @RestrictTo({Scope.LIBRARY_GROUP})
    void setAutoSizeTextTypeUniformWithConfiguration(int autoSizeMinTextSize, int autoSizeMaxTextSize, int autoSizeStepGranularity, int unit) throws IllegalArgumentException {
        if (supportsAutoSizeText()) {
            DisplayMetrics displayMetrics = this.mContext.getResources().getDisplayMetrics();
            validateAndSetAutoSizeTextTypeUniformConfiguration(TypedValue.applyDimension(unit, (float) autoSizeMinTextSize, displayMetrics), TypedValue.applyDimension(unit, (float) autoSizeMaxTextSize, displayMetrics), TypedValue.applyDimension(unit, (float) autoSizeStepGranularity, displayMetrics));
            if (setupAutoSizeText()) {
                autoSizeText();
            }
        }
    }

    @RestrictTo({Scope.LIBRARY_GROUP})
    void setAutoSizeTextTypeUniformWithPresetSizes(@NonNull int[] presetSizes, int unit) throws IllegalArgumentException {
        if (supportsAutoSizeText()) {
            int presetSizesLength = presetSizes.length;
            int i = 0;
            if (presetSizesLength > 0) {
                int[] presetSizesInPx = new int[presetSizesLength];
                if (unit == 0) {
                    presetSizesInPx = Arrays.copyOf(presetSizes, presetSizesLength);
                } else {
                    DisplayMetrics displayMetrics = this.mContext.getResources().getDisplayMetrics();
                    while (i < presetSizesLength) {
                        presetSizesInPx[i] = Math.round(TypedValue.applyDimension(unit, (float) presetSizes[i], displayMetrics));
                        i++;
                    }
                }
                this.mAutoSizeTextSizesInPx = cleanupAutoSizePresetSizes(presetSizesInPx);
                if (!setupAutoSizeUniformPresetSizesConfiguration()) {
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("None of the preset sizes is valid: ");
                    stringBuilder.append(Arrays.toString(presetSizes));
                    throw new IllegalArgumentException(stringBuilder.toString());
                }
            }
            this.mHasPresetAutoSizeValues = false;
            if (setupAutoSizeText()) {
                autoSizeText();
            }
        }
    }

    @RestrictTo({Scope.LIBRARY_GROUP})
    int getAutoSizeTextType() {
        return this.mAutoSizeTextType;
    }

    @RestrictTo({Scope.LIBRARY_GROUP})
    int getAutoSizeStepGranularity() {
        return Math.round(this.mAutoSizeStepGranularityInPx);
    }

    @RestrictTo({Scope.LIBRARY_GROUP})
    int getAutoSizeMinTextSize() {
        return Math.round(this.mAutoSizeMinTextSizeInPx);
    }

    @RestrictTo({Scope.LIBRARY_GROUP})
    int getAutoSizeMaxTextSize() {
        return Math.round(this.mAutoSizeMaxTextSizeInPx);
    }

    @RestrictTo({Scope.LIBRARY_GROUP})
    int[] getAutoSizeTextAvailableSizes() {
        return this.mAutoSizeTextSizesInPx;
    }

    private void setupAutoSizeUniformPresetSizes(TypedArray textSizes) {
        int textSizesLength = textSizes.length();
        int[] parsedSizes = new int[textSizesLength];
        if (textSizesLength > 0) {
            for (int i = 0; i < textSizesLength; i++) {
                parsedSizes[i] = textSizes.getDimensionPixelSize(i, -1);
            }
            this.mAutoSizeTextSizesInPx = cleanupAutoSizePresetSizes(parsedSizes);
            setupAutoSizeUniformPresetSizesConfiguration();
        }
    }

    private boolean setupAutoSizeUniformPresetSizesConfiguration() {
        int sizesLength = this.mAutoSizeTextSizesInPx.length;
        this.mHasPresetAutoSizeValues = sizesLength > 0;
        if (this.mHasPresetAutoSizeValues) {
            this.mAutoSizeTextType = 1;
            this.mAutoSizeMinTextSizeInPx = (float) this.mAutoSizeTextSizesInPx[0];
            this.mAutoSizeMaxTextSizeInPx = (float) this.mAutoSizeTextSizesInPx[sizesLength - 1];
            this.mAutoSizeStepGranularityInPx = UNSET_AUTO_SIZE_UNIFORM_CONFIGURATION_VALUE;
        }
        return this.mHasPresetAutoSizeValues;
    }

    private int[] cleanupAutoSizePresetSizes(int[] presetValues) {
        if (presetValuesLength == 0) {
            return presetValues;
        }
        int i;
        Arrays.sort(presetValues);
        List<Integer> uniqueValidSizes = new ArrayList();
        for (int currentPresetValue : presetValues) {
            if (currentPresetValue > 0 && Collections.binarySearch(uniqueValidSizes, Integer.valueOf(currentPresetValue)) < 0) {
                uniqueValidSizes.add(Integer.valueOf(currentPresetValue));
            }
        }
        if (presetValuesLength == uniqueValidSizes.size()) {
            return presetValues;
        }
        i = uniqueValidSizes.size();
        int[] cleanedUpSizes = new int[i];
        for (int i2 = 0; i2 < i; i2++) {
            cleanedUpSizes[i2] = ((Integer) uniqueValidSizes.get(i2)).intValue();
        }
        return cleanedUpSizes;
    }

    private void validateAndSetAutoSizeTextTypeUniformConfiguration(float autoSizeMinTextSizeInPx, float autoSizeMaxTextSizeInPx, float autoSizeStepGranularityInPx) throws IllegalArgumentException {
        StringBuilder stringBuilder;
        if (autoSizeMinTextSizeInPx <= 0.0f) {
            stringBuilder = new StringBuilder();
            stringBuilder.append("Minimum auto-size text size (");
            stringBuilder.append(autoSizeMinTextSizeInPx);
            stringBuilder.append("px) is less or equal to (0px)");
            throw new IllegalArgumentException(stringBuilder.toString());
        } else if (autoSizeMaxTextSizeInPx <= autoSizeMinTextSizeInPx) {
            stringBuilder = new StringBuilder();
            stringBuilder.append("Maximum auto-size text size (");
            stringBuilder.append(autoSizeMaxTextSizeInPx);
            stringBuilder.append("px) is less or equal to minimum auto-size ");
            stringBuilder.append("text size (");
            stringBuilder.append(autoSizeMinTextSizeInPx);
            stringBuilder.append("px)");
            throw new IllegalArgumentException(stringBuilder.toString());
        } else if (autoSizeStepGranularityInPx > 0.0f) {
            this.mAutoSizeTextType = 1;
            this.mAutoSizeMinTextSizeInPx = autoSizeMinTextSizeInPx;
            this.mAutoSizeMaxTextSizeInPx = autoSizeMaxTextSizeInPx;
            this.mAutoSizeStepGranularityInPx = autoSizeStepGranularityInPx;
            this.mHasPresetAutoSizeValues = false;
        } else {
            stringBuilder = new StringBuilder();
            stringBuilder.append("The auto-size step granularity (");
            stringBuilder.append(autoSizeStepGranularityInPx);
            stringBuilder.append("px) is less or equal to (0px)");
            throw new IllegalArgumentException(stringBuilder.toString());
        }
    }

    private boolean setupAutoSizeText() {
        int i = 0;
        if (supportsAutoSizeText() && this.mAutoSizeTextType == 1) {
            if (!this.mHasPresetAutoSizeValues || this.mAutoSizeTextSizesInPx.length == 0) {
                int autoSizeValuesLength = 1;
                float currentSize = (float) Math.round(this.mAutoSizeMinTextSizeInPx);
                while (Math.round(this.mAutoSizeStepGranularityInPx + currentSize) <= Math.round(this.mAutoSizeMaxTextSizeInPx)) {
                    autoSizeValuesLength++;
                    currentSize += this.mAutoSizeStepGranularityInPx;
                }
                int[] autoSizeTextSizesInPx = new int[autoSizeValuesLength];
                float sizeToAdd = this.mAutoSizeMinTextSizeInPx;
                while (i < autoSizeValuesLength) {
                    autoSizeTextSizesInPx[i] = Math.round(sizeToAdd);
                    sizeToAdd += this.mAutoSizeStepGranularityInPx;
                    i++;
                }
                this.mAutoSizeTextSizesInPx = cleanupAutoSizePresetSizes(autoSizeTextSizesInPx);
            }
            this.mNeedsAutoSizeText = true;
        } else {
            this.mNeedsAutoSizeText = false;
        }
        return this.mNeedsAutoSizeText;
    }

    @RestrictTo({Scope.LIBRARY_GROUP})
    void autoSizeText() {
        if (isAutoSizeEnabled()) {
            if (this.mNeedsAutoSizeText) {
                if (this.mTextView.getMeasuredHeight() > 0 && this.mTextView.getMeasuredWidth() > 0) {
                    int availableWidth;
                    if (((Boolean) invokeAndReturnWithDefault(this.mTextView, "getHorizontallyScrolling", Boolean.valueOf(false))).booleanValue()) {
                        availableWidth = 1048576;
                    } else {
                        availableWidth = (this.mTextView.getMeasuredWidth() - this.mTextView.getTotalPaddingLeft()) - this.mTextView.getTotalPaddingRight();
                    }
                    int availableHeight = (this.mTextView.getHeight() - this.mTextView.getCompoundPaddingBottom()) - this.mTextView.getCompoundPaddingTop();
                    if (availableWidth > 0 && availableHeight > 0) {
                        synchronized (TEMP_RECTF) {
                            TEMP_RECTF.setEmpty();
                            TEMP_RECTF.right = (float) availableWidth;
                            TEMP_RECTF.bottom = (float) availableHeight;
                            float optimalTextSize = (float) findLargestTextSizeWhichFits(TEMP_RECTF);
                            if (optimalTextSize != this.mTextView.getTextSize()) {
                                setTextSizeInternal(0, optimalTextSize);
                            }
                        }
                    } else {
                        return;
                    }
                }
                return;
            }
            this.mNeedsAutoSizeText = true;
        }
    }

    private void clearAutoSizeConfiguration() {
        this.mAutoSizeTextType = 0;
        this.mAutoSizeMinTextSizeInPx = UNSET_AUTO_SIZE_UNIFORM_CONFIGURATION_VALUE;
        this.mAutoSizeMaxTextSizeInPx = UNSET_AUTO_SIZE_UNIFORM_CONFIGURATION_VALUE;
        this.mAutoSizeStepGranularityInPx = UNSET_AUTO_SIZE_UNIFORM_CONFIGURATION_VALUE;
        this.mAutoSizeTextSizesInPx = new int[0];
        this.mNeedsAutoSizeText = false;
    }

    @RestrictTo({Scope.LIBRARY_GROUP})
    void setTextSizeInternal(int unit, float size) {
        Resources res;
        if (this.mContext == null) {
            res = Resources.getSystem();
        } else {
            res = this.mContext.getResources();
        }
        setRawTextSize(TypedValue.applyDimension(unit, size, res.getDisplayMetrics()));
    }

    private void setRawTextSize(float size) {
        if (size != this.mTextView.getPaint().getTextSize()) {
            this.mTextView.getPaint().setTextSize(size);
            boolean isInLayout = false;
            if (VERSION.SDK_INT >= 18) {
                isInLayout = this.mTextView.isInLayout();
            }
            if (this.mTextView.getLayout() != null) {
                this.mNeedsAutoSizeText = false;
                String methodName = "nullLayouts";
                try {
                    Method method = getTextViewMethod("nullLayouts");
                    if (method != null) {
                        method.invoke(this.mTextView, new Object[0]);
                    }
                } catch (Exception ex) {
                    Log.w(TAG, "Failed to invoke TextView#nullLayouts() method", ex);
                }
                if (isInLayout) {
                    this.mTextView.forceLayout();
                } else {
                    this.mTextView.requestLayout();
                }
                this.mTextView.invalidate();
            }
        }
    }

    private int findLargestTextSizeWhichFits(RectF availableSpace) {
        int sizesCount = this.mAutoSizeTextSizesInPx.length;
        if (sizesCount != 0) {
            int bestSizeIndex = 0;
            int lowIndex = 0 + 1;
            int highIndex = sizesCount - 1;
            while (lowIndex <= highIndex) {
                int sizeToTryIndex = (lowIndex + highIndex) / 2;
                if (suggestedSizeFitsInSpace(this.mAutoSizeTextSizesInPx[sizeToTryIndex], availableSpace)) {
                    bestSizeIndex = lowIndex;
                    lowIndex = sizeToTryIndex + 1;
                } else {
                    highIndex = sizeToTryIndex - 1;
                    bestSizeIndex = highIndex;
                }
            }
            return this.mAutoSizeTextSizesInPx[bestSizeIndex];
        }
        throw new IllegalStateException("No available text sizes to choose from.");
    }

    /* JADX WARNING: Missing block: B:18:0x0077, code:
            return false;
     */
    private boolean suggestedSizeFitsInSpace(int r9, android.graphics.RectF r10) {
        /*
        r8 = this;
        r0 = r8.mTextView;
        r0 = r0.getText();
        r1 = android.os.Build.VERSION.SDK_INT;
        r2 = -1;
        r3 = 16;
        if (r1 < r3) goto L_0x0014;
    L_0x000d:
        r1 = r8.mTextView;
        r1 = r1.getMaxLines();
        goto L_0x0015;
    L_0x0014:
        r1 = -1;
    L_0x0015:
        r3 = r8.mTempTextPaint;
        if (r3 != 0) goto L_0x0021;
    L_0x0019:
        r3 = new android.text.TextPaint;
        r3.<init>();
        r8.mTempTextPaint = r3;
        goto L_0x0026;
    L_0x0021:
        r3 = r8.mTempTextPaint;
        r3.reset();
    L_0x0026:
        r3 = r8.mTempTextPaint;
        r4 = r8.mTextView;
        r4 = r4.getPaint();
        r3.set(r4);
        r3 = r8.mTempTextPaint;
        r4 = (float) r9;
        r3.setTextSize(r4);
        r3 = r8.mTextView;
        r4 = "getLayoutAlignment";
        r5 = android.text.Layout.Alignment.ALIGN_NORMAL;
        r3 = r8.invokeAndReturnWithDefault(r3, r4, r5);
        r3 = (android.text.Layout.Alignment) r3;
        r4 = android.os.Build.VERSION.SDK_INT;
        r5 = 23;
        if (r4 < r5) goto L_0x0054;
    L_0x0049:
        r4 = r10.right;
        r4 = java.lang.Math.round(r4);
        r4 = r8.createStaticLayoutForMeasuring(r0, r3, r4, r1);
        goto L_0x005e;
    L_0x0054:
        r4 = r10.right;
        r4 = java.lang.Math.round(r4);
        r4 = r8.createStaticLayoutForMeasuringPre23(r0, r3, r4);
    L_0x005e:
        r5 = 0;
        r6 = 1;
        if (r1 == r2) goto L_0x0078;
    L_0x0062:
        r2 = r4.getLineCount();
        if (r2 > r1) goto L_0x0077;
    L_0x0068:
        r2 = r4.getLineCount();
        r2 = r2 - r6;
        r2 = r4.getLineEnd(r2);
        r7 = r0.length();
        if (r2 == r7) goto L_0x0078;
    L_0x0077:
        return r5;
    L_0x0078:
        r2 = r4.getHeight();
        r2 = (float) r2;
        r7 = r10.bottom;
        r2 = (r2 > r7 ? 1 : (r2 == r7 ? 0 : -1));
        if (r2 <= 0) goto L_0x0084;
    L_0x0083:
        return r5;
    L_0x0084:
        return r6;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.support.v7.widget.AppCompatTextViewAutoSizeHelper.suggestedSizeFitsInSpace(int, android.graphics.RectF):boolean");
    }

    @TargetApi(23)
    private StaticLayout createStaticLayoutForMeasuring(CharSequence text, Alignment alignment, int availableWidth, int maxLines) {
        int i;
        TextDirectionHeuristic textDirectionHeuristic = (TextDirectionHeuristic) invokeAndReturnWithDefault(this.mTextView, "getTextDirectionHeuristic", TextDirectionHeuristics.FIRSTSTRONG_LTR);
        Builder hyphenationFrequency = Builder.obtain(text, 0, text.length(), this.mTempTextPaint, availableWidth).setAlignment(alignment).setLineSpacing(this.mTextView.getLineSpacingExtra(), this.mTextView.getLineSpacingMultiplier()).setIncludePad(this.mTextView.getIncludeFontPadding()).setBreakStrategy(this.mTextView.getBreakStrategy()).setHyphenationFrequency(this.mTextView.getHyphenationFrequency());
        if (maxLines == -1) {
            i = ActivityChooserViewAdapter.MAX_ACTIVITY_COUNT_UNLIMITED;
        } else {
            i = maxLines;
        }
        return hyphenationFrequency.setMaxLines(i).setTextDirection(textDirectionHeuristic).build();
    }

    @TargetApi(14)
    private StaticLayout createStaticLayoutForMeasuringPre23(CharSequence text, Alignment alignment, int availableWidth) {
        float lineSpacingMultiplier;
        float lineSpacingAdd;
        boolean includePad;
        if (VERSION.SDK_INT >= 16) {
            lineSpacingMultiplier = this.mTextView.getLineSpacingMultiplier();
            lineSpacingAdd = this.mTextView.getLineSpacingExtra();
            includePad = this.mTextView.getIncludeFontPadding();
        } else {
            lineSpacingMultiplier = ((Float) invokeAndReturnWithDefault(this.mTextView, "getLineSpacingMultiplier", Float.valueOf(1.0f))).floatValue();
            lineSpacingAdd = ((Float) invokeAndReturnWithDefault(this.mTextView, "getLineSpacingExtra", Float.valueOf(0.0f))).floatValue();
            includePad = ((Boolean) invokeAndReturnWithDefault(this.mTextView, "getIncludeFontPadding", Boolean.valueOf(true))).booleanValue();
        }
        return new StaticLayout(text, this.mTempTextPaint, availableWidth, alignment, lineSpacingMultiplier, lineSpacingAdd, includePad);
    }

    /* JADX WARNING: Failed to extract finally block: empty outs */
    private <T> T invokeAndReturnWithDefault(@android.support.annotation.NonNull java.lang.Object r7, @android.support.annotation.NonNull java.lang.String r8, @android.support.annotation.NonNull T r9) {
        /*
        r6 = this;
        r0 = 0;
        r1 = 0;
        r2 = r1;
        r3 = r6.getTextViewMethod(r8);	 Catch:{ Exception -> 0x0016 }
        r1 = new java.lang.Object[r1];	 Catch:{ Exception -> 0x0016 }
        r1 = r3.invoke(r7, r1);	 Catch:{ Exception -> 0x0016 }
        r0 = r1;
        if (r0 != 0) goto L_0x0038;
    L_0x0010:
        if (r2 == 0) goto L_0x0038;
    L_0x0012:
        r0 = r9;
        goto L_0x0038;
    L_0x0014:
        r1 = move-exception;
        goto L_0x0039;
    L_0x0016:
        r1 = move-exception;
        r2 = 1;
        r3 = "ACTVAutoSizeHelper";
        r4 = new java.lang.StringBuilder;	 Catch:{ all -> 0x0014 }
        r4.<init>();	 Catch:{ all -> 0x0014 }
        r5 = "Failed to invoke TextView#";
        r4.append(r5);	 Catch:{ all -> 0x0014 }
        r4.append(r8);	 Catch:{ all -> 0x0014 }
        r5 = "() method";
        r4.append(r5);	 Catch:{ all -> 0x0014 }
        r4 = r4.toString();	 Catch:{ all -> 0x0014 }
        android.util.Log.w(r3, r4, r1);	 Catch:{ all -> 0x0014 }
        if (r0 != 0) goto L_0x0038;
    L_0x0035:
        if (r2 == 0) goto L_0x0038;
    L_0x0037:
        goto L_0x0012;
    L_0x0038:
        return r0;
    L_0x0039:
        if (r0 != 0) goto L_0x003e;
    L_0x003b:
        if (r2 == 0) goto L_0x003e;
    L_0x003d:
        r0 = r9;
    L_0x003e:
        throw r1;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.support.v7.widget.AppCompatTextViewAutoSizeHelper.invokeAndReturnWithDefault(java.lang.Object, java.lang.String, java.lang.Object):T");
    }

    @Nullable
    private Method getTextViewMethod(@NonNull String methodName) {
        try {
            Method method = (Method) sTextViewMethodByNameCache.get(methodName);
            if (method == null) {
                method = TextView.class.getDeclaredMethod(methodName, new Class[0]);
                if (method != null) {
                    method.setAccessible(true);
                    sTextViewMethodByNameCache.put(methodName, method);
                }
            }
            return method;
        } catch (Exception ex) {
            String str = TAG;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Failed to retrieve TextView#");
            stringBuilder.append(methodName);
            stringBuilder.append("() method");
            Log.w(str, stringBuilder.toString(), ex);
            return null;
        }
    }

    @RestrictTo({Scope.LIBRARY_GROUP})
    boolean isAutoSizeEnabled() {
        return supportsAutoSizeText() && this.mAutoSizeTextType != 0;
    }

    private boolean supportsAutoSizeText() {
        return !(this.mTextView instanceof AppCompatEditText);
    }
}
