package android.support.graphics.drawable;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.Resources.Theme;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.PorterDuff.Mode;
import android.graphics.Rect;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Drawable.Callback;
import android.graphics.drawable.Drawable.ConstantState;
import android.os.Build.VERSION;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.graphics.drawable.Animatable2Compat.AnimationCallback;
import android.support.v4.content.res.TypedArrayUtils;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v4.util.ArrayMap;
import android.util.AttributeSet;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

public class AnimatedVectorDrawableCompat extends VectorDrawableCommon implements Animatable2Compat {
    private static final String ANIMATED_VECTOR = "animated-vector";
    private static final boolean DBG_ANIMATION_VECTOR_DRAWABLE = false;
    private static final String LOGTAG = "AnimatedVDCompat";
    private static final String TARGET = "target";
    private AnimatedVectorDrawableCompatState mAnimatedVectorState;
    private ArrayList<AnimationCallback> mAnimationCallbacks;
    private AnimatorListener mAnimatorListener;
    private ArgbEvaluator mArgbEvaluator;
    AnimatedVectorDrawableDelegateState mCachedConstantStateDelegate;
    final Callback mCallback;
    private Context mContext;

    private static class AnimatedVectorDrawableCompatState extends ConstantState {
        AnimatorSet mAnimatorSet;
        private ArrayList<Animator> mAnimators;
        int mChangingConfigurations;
        ArrayMap<Animator, String> mTargetNameMap;
        VectorDrawableCompat mVectorDrawable;

        public AnimatedVectorDrawableCompatState(Context context, AnimatedVectorDrawableCompatState copy, Callback owner, Resources res) {
            if (copy != null) {
                this.mChangingConfigurations = copy.mChangingConfigurations;
                int i = 0;
                if (copy.mVectorDrawable != null) {
                    ConstantState cs = copy.mVectorDrawable.getConstantState();
                    if (res != null) {
                        this.mVectorDrawable = (VectorDrawableCompat) cs.newDrawable(res);
                    } else {
                        this.mVectorDrawable = (VectorDrawableCompat) cs.newDrawable();
                    }
                    this.mVectorDrawable = (VectorDrawableCompat) this.mVectorDrawable.mutate();
                    this.mVectorDrawable.setCallback(owner);
                    this.mVectorDrawable.setBounds(copy.mVectorDrawable.getBounds());
                    this.mVectorDrawable.setAllowCaching(false);
                }
                if (copy.mAnimators != null) {
                    int numAnimators = copy.mAnimators.size();
                    this.mAnimators = new ArrayList(numAnimators);
                    this.mTargetNameMap = new ArrayMap(numAnimators);
                    while (i < numAnimators) {
                        Animator anim = (Animator) copy.mAnimators.get(i);
                        Animator animClone = anim.clone();
                        String targetName = (String) copy.mTargetNameMap.get(anim);
                        animClone.setTarget(this.mVectorDrawable.getTargetByName(targetName));
                        this.mAnimators.add(animClone);
                        this.mTargetNameMap.put(animClone, targetName);
                        i++;
                    }
                    setupAnimatorSet();
                }
            }
        }

        public Drawable newDrawable() {
            throw new IllegalStateException("No constant state support for SDK < 24.");
        }

        public Drawable newDrawable(Resources res) {
            throw new IllegalStateException("No constant state support for SDK < 24.");
        }

        public int getChangingConfigurations() {
            return this.mChangingConfigurations;
        }

        public void setupAnimatorSet() {
            if (this.mAnimatorSet == null) {
                this.mAnimatorSet = new AnimatorSet();
            }
            this.mAnimatorSet.playTogether(this.mAnimators);
        }
    }

    @RequiresApi(24)
    private static class AnimatedVectorDrawableDelegateState extends ConstantState {
        private final ConstantState mDelegateState;

        public AnimatedVectorDrawableDelegateState(ConstantState state) {
            this.mDelegateState = state;
        }

        public Drawable newDrawable() {
            AnimatedVectorDrawableCompat drawableCompat = new AnimatedVectorDrawableCompat();
            drawableCompat.mDelegateDrawable = this.mDelegateState.newDrawable();
            drawableCompat.mDelegateDrawable.setCallback(drawableCompat.mCallback);
            return drawableCompat;
        }

        public Drawable newDrawable(Resources res) {
            AnimatedVectorDrawableCompat drawableCompat = new AnimatedVectorDrawableCompat();
            drawableCompat.mDelegateDrawable = this.mDelegateState.newDrawable(res);
            drawableCompat.mDelegateDrawable.setCallback(drawableCompat.mCallback);
            return drawableCompat;
        }

        public Drawable newDrawable(Resources res, Theme theme) {
            AnimatedVectorDrawableCompat drawableCompat = new AnimatedVectorDrawableCompat();
            drawableCompat.mDelegateDrawable = this.mDelegateState.newDrawable(res, theme);
            drawableCompat.mDelegateDrawable.setCallback(drawableCompat.mCallback);
            return drawableCompat;
        }

        public boolean canApplyTheme() {
            return this.mDelegateState.canApplyTheme();
        }

        public int getChangingConfigurations() {
            return this.mDelegateState.getChangingConfigurations();
        }
    }

    AnimatedVectorDrawableCompat() {
        this(null, null, null);
    }

    private AnimatedVectorDrawableCompat(@Nullable Context context) {
        this(context, null, null);
    }

    private AnimatedVectorDrawableCompat(@Nullable Context context, @Nullable AnimatedVectorDrawableCompatState state, @Nullable Resources res) {
        this.mArgbEvaluator = null;
        this.mAnimatorListener = null;
        this.mAnimationCallbacks = null;
        this.mCallback = new Callback() {
            public void invalidateDrawable(Drawable who) {
                AnimatedVectorDrawableCompat.this.invalidateSelf();
            }

            public void scheduleDrawable(Drawable who, Runnable what, long when) {
                AnimatedVectorDrawableCompat.this.scheduleSelf(what, when);
            }

            public void unscheduleDrawable(Drawable who, Runnable what) {
                AnimatedVectorDrawableCompat.this.unscheduleSelf(what);
            }
        };
        this.mContext = context;
        if (state != null) {
            this.mAnimatedVectorState = state;
        } else {
            this.mAnimatedVectorState = new AnimatedVectorDrawableCompatState(context, state, this.mCallback, res);
        }
    }

    public Drawable mutate() {
        if (this.mDelegateDrawable != null) {
            this.mDelegateDrawable.mutate();
        }
        return this;
    }

    /* JADX WARNING: Removed duplicated region for block: B:15:0x0055 A:{Catch:{ XmlPullParserException -> 0x0066, IOException -> 0x005d }} */
    /* JADX WARNING: Removed duplicated region for block: B:13:0x0048 A:{Catch:{ XmlPullParserException -> 0x0066, IOException -> 0x005d }} */
    @android.support.annotation.Nullable
    public static android.support.graphics.drawable.AnimatedVectorDrawableCompat create(@android.support.annotation.NonNull android.content.Context r6, @android.support.annotation.DrawableRes int r7) {
        /*
        r0 = android.os.Build.VERSION.SDK_INT;
        r1 = 24;
        if (r0 < r1) goto L_0x002e;
    L_0x0006:
        r0 = new android.support.graphics.drawable.AnimatedVectorDrawableCompat;
        r0.<init>(r6);
        r1 = r6.getResources();
        r2 = r6.getTheme();
        r1 = android.support.v4.content.res.ResourcesCompat.getDrawable(r1, r7, r2);
        r0.mDelegateDrawable = r1;
        r1 = r0.mDelegateDrawable;
        r2 = r0.mCallback;
        r1.setCallback(r2);
        r1 = new android.support.graphics.drawable.AnimatedVectorDrawableCompat$AnimatedVectorDrawableDelegateState;
        r2 = r0.mDelegateDrawable;
        r2 = r2.getConstantState();
        r1.<init>(r2);
        r0.mCachedConstantStateDelegate = r1;
        return r0;
    L_0x002e:
        r0 = r6.getResources();
        r1 = r0.getXml(r7);	 Catch:{ XmlPullParserException -> 0x0066, IOException -> 0x005d }
        r2 = android.util.Xml.asAttributeSet(r1);	 Catch:{ XmlPullParserException -> 0x0066, IOException -> 0x005d }
    L_0x003a:
        r3 = r1.next();	 Catch:{ XmlPullParserException -> 0x0066, IOException -> 0x005d }
        r4 = r3;
        r5 = 2;
        if (r3 == r5) goto L_0x0046;
    L_0x0042:
        r3 = 1;
        if (r4 == r3) goto L_0x0046;
    L_0x0045:
        goto L_0x003a;
    L_0x0046:
        if (r4 != r5) goto L_0x0055;
    L_0x0048:
        r3 = r6.getResources();	 Catch:{ XmlPullParserException -> 0x0066, IOException -> 0x005d }
        r5 = r6.getTheme();	 Catch:{ XmlPullParserException -> 0x0066, IOException -> 0x005d }
        r3 = createFromXmlInner(r6, r3, r1, r2, r5);	 Catch:{ XmlPullParserException -> 0x0066, IOException -> 0x005d }
        return r3;
    L_0x0055:
        r3 = new org.xmlpull.v1.XmlPullParserException;	 Catch:{ XmlPullParserException -> 0x0066, IOException -> 0x005d }
        r5 = "No start tag found";
        r3.<init>(r5);	 Catch:{ XmlPullParserException -> 0x0066, IOException -> 0x005d }
        throw r3;	 Catch:{ XmlPullParserException -> 0x0066, IOException -> 0x005d }
    L_0x005d:
        r1 = move-exception;
        r2 = "AnimatedVDCompat";
        r3 = "parser error";
        android.util.Log.e(r2, r3, r1);
        goto L_0x006f;
    L_0x0066:
        r1 = move-exception;
        r2 = "AnimatedVDCompat";
        r3 = "parser error";
        android.util.Log.e(r2, r3, r1);
    L_0x006f:
        r1 = 0;
        return r1;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.support.graphics.drawable.AnimatedVectorDrawableCompat.create(android.content.Context, int):android.support.graphics.drawable.AnimatedVectorDrawableCompat");
    }

    public static AnimatedVectorDrawableCompat createFromXmlInner(Context context, Resources r, XmlPullParser parser, AttributeSet attrs, Theme theme) throws XmlPullParserException, IOException {
        AnimatedVectorDrawableCompat drawable = new AnimatedVectorDrawableCompat(context);
        drawable.inflate(r, parser, attrs, theme);
        return drawable;
    }

    public ConstantState getConstantState() {
        if (this.mDelegateDrawable == null || VERSION.SDK_INT < 24) {
            return null;
        }
        return new AnimatedVectorDrawableDelegateState(this.mDelegateDrawable.getConstantState());
    }

    public int getChangingConfigurations() {
        if (this.mDelegateDrawable != null) {
            return this.mDelegateDrawable.getChangingConfigurations();
        }
        return super.getChangingConfigurations() | this.mAnimatedVectorState.mChangingConfigurations;
    }

    public void draw(Canvas canvas) {
        if (this.mDelegateDrawable != null) {
            this.mDelegateDrawable.draw(canvas);
            return;
        }
        this.mAnimatedVectorState.mVectorDrawable.draw(canvas);
        if (this.mAnimatedVectorState.mAnimatorSet.isStarted()) {
            invalidateSelf();
        }
    }

    protected void onBoundsChange(Rect bounds) {
        if (this.mDelegateDrawable != null) {
            this.mDelegateDrawable.setBounds(bounds);
        } else {
            this.mAnimatedVectorState.mVectorDrawable.setBounds(bounds);
        }
    }

    protected boolean onStateChange(int[] state) {
        if (this.mDelegateDrawable != null) {
            return this.mDelegateDrawable.setState(state);
        }
        return this.mAnimatedVectorState.mVectorDrawable.setState(state);
    }

    protected boolean onLevelChange(int level) {
        if (this.mDelegateDrawable != null) {
            return this.mDelegateDrawable.setLevel(level);
        }
        return this.mAnimatedVectorState.mVectorDrawable.setLevel(level);
    }

    public int getAlpha() {
        if (this.mDelegateDrawable != null) {
            return DrawableCompat.getAlpha(this.mDelegateDrawable);
        }
        return this.mAnimatedVectorState.mVectorDrawable.getAlpha();
    }

    public void setAlpha(int alpha) {
        if (this.mDelegateDrawable != null) {
            this.mDelegateDrawable.setAlpha(alpha);
        } else {
            this.mAnimatedVectorState.mVectorDrawable.setAlpha(alpha);
        }
    }

    public void setColorFilter(ColorFilter colorFilter) {
        if (this.mDelegateDrawable != null) {
            this.mDelegateDrawable.setColorFilter(colorFilter);
        } else {
            this.mAnimatedVectorState.mVectorDrawable.setColorFilter(colorFilter);
        }
    }

    public void setTint(int tint) {
        if (this.mDelegateDrawable != null) {
            DrawableCompat.setTint(this.mDelegateDrawable, tint);
        } else {
            this.mAnimatedVectorState.mVectorDrawable.setTint(tint);
        }
    }

    public void setTintList(ColorStateList tint) {
        if (this.mDelegateDrawable != null) {
            DrawableCompat.setTintList(this.mDelegateDrawable, tint);
        } else {
            this.mAnimatedVectorState.mVectorDrawable.setTintList(tint);
        }
    }

    public void setTintMode(Mode tintMode) {
        if (this.mDelegateDrawable != null) {
            DrawableCompat.setTintMode(this.mDelegateDrawable, tintMode);
        } else {
            this.mAnimatedVectorState.mVectorDrawable.setTintMode(tintMode);
        }
    }

    public boolean setVisible(boolean visible, boolean restart) {
        if (this.mDelegateDrawable != null) {
            return this.mDelegateDrawable.setVisible(visible, restart);
        }
        this.mAnimatedVectorState.mVectorDrawable.setVisible(visible, restart);
        return super.setVisible(visible, restart);
    }

    public boolean isStateful() {
        if (this.mDelegateDrawable != null) {
            return this.mDelegateDrawable.isStateful();
        }
        return this.mAnimatedVectorState.mVectorDrawable.isStateful();
    }

    public int getOpacity() {
        if (this.mDelegateDrawable != null) {
            return this.mDelegateDrawable.getOpacity();
        }
        return this.mAnimatedVectorState.mVectorDrawable.getOpacity();
    }

    public int getIntrinsicWidth() {
        if (this.mDelegateDrawable != null) {
            return this.mDelegateDrawable.getIntrinsicWidth();
        }
        return this.mAnimatedVectorState.mVectorDrawable.getIntrinsicWidth();
    }

    public int getIntrinsicHeight() {
        if (this.mDelegateDrawable != null) {
            return this.mDelegateDrawable.getIntrinsicHeight();
        }
        return this.mAnimatedVectorState.mVectorDrawable.getIntrinsicHeight();
    }

    public boolean isAutoMirrored() {
        if (this.mDelegateDrawable != null) {
            return DrawableCompat.isAutoMirrored(this.mDelegateDrawable);
        }
        return this.mAnimatedVectorState.mVectorDrawable.isAutoMirrored();
    }

    public void setAutoMirrored(boolean mirrored) {
        if (this.mDelegateDrawable != null) {
            DrawableCompat.setAutoMirrored(this.mDelegateDrawable, mirrored);
        } else {
            this.mAnimatedVectorState.mVectorDrawable.setAutoMirrored(mirrored);
        }
    }

    public void inflate(Resources res, XmlPullParser parser, AttributeSet attrs, Theme theme) throws XmlPullParserException, IOException {
        if (this.mDelegateDrawable != null) {
            DrawableCompat.inflate(this.mDelegateDrawable, res, parser, attrs, theme);
            return;
        }
        int eventType = parser.getEventType();
        int innerDepth = parser.getDepth() + 1;
        while (eventType != 1 && (parser.getDepth() >= innerDepth || eventType != 3)) {
            if (eventType == 2) {
                String tagName = parser.getName();
                TypedArray a;
                if (ANIMATED_VECTOR.equals(tagName)) {
                    a = TypedArrayUtils.obtainAttributes(res, theme, attrs, AndroidResources.STYLEABLE_ANIMATED_VECTOR_DRAWABLE);
                    int drawableRes = a.getResourceId(0, 0);
                    if (drawableRes != 0) {
                        VectorDrawableCompat vectorDrawable = VectorDrawableCompat.create(res, drawableRes, theme);
                        vectorDrawable.setAllowCaching(false);
                        vectorDrawable.setCallback(this.mCallback);
                        if (this.mAnimatedVectorState.mVectorDrawable != null) {
                            this.mAnimatedVectorState.mVectorDrawable.setCallback(null);
                        }
                        this.mAnimatedVectorState.mVectorDrawable = vectorDrawable;
                    }
                    a.recycle();
                } else if (TARGET.equals(tagName)) {
                    a = res.obtainAttributes(attrs, AndroidResources.STYLEABLE_ANIMATED_VECTOR_DRAWABLE_TARGET);
                    String target = a.getString(0);
                    int id = a.getResourceId(1, 0);
                    if (id != 0) {
                        if (this.mContext != null) {
                            setupAnimatorsForTarget(target, AnimatorInflaterCompat.loadAnimator(this.mContext, id));
                        } else {
                            a.recycle();
                            throw new IllegalStateException("Context can't be null when inflating animators");
                        }
                    }
                    a.recycle();
                } else {
                    continue;
                }
            }
            eventType = parser.next();
        }
        this.mAnimatedVectorState.setupAnimatorSet();
    }

    public void inflate(Resources res, XmlPullParser parser, AttributeSet attrs) throws XmlPullParserException, IOException {
        inflate(res, parser, attrs, null);
    }

    public void applyTheme(Theme t) {
        if (this.mDelegateDrawable != null) {
            DrawableCompat.applyTheme(this.mDelegateDrawable, t);
        }
    }

    public boolean canApplyTheme() {
        if (this.mDelegateDrawable != null) {
            return DrawableCompat.canApplyTheme(this.mDelegateDrawable);
        }
        return false;
    }

    private void setupColorAnimator(Animator animator) {
        if (animator instanceof AnimatorSet) {
            List<Animator> childAnimators = ((AnimatorSet) animator).getChildAnimations();
            if (childAnimators != null) {
                for (int i = 0; i < childAnimators.size(); i++) {
                    setupColorAnimator((Animator) childAnimators.get(i));
                }
            }
        }
        if (animator instanceof ObjectAnimator) {
            ObjectAnimator objectAnim = (ObjectAnimator) animator;
            String propertyName = objectAnim.getPropertyName();
            if ("fillColor".equals(propertyName) || "strokeColor".equals(propertyName)) {
                if (this.mArgbEvaluator == null) {
                    this.mArgbEvaluator = new ArgbEvaluator();
                }
                objectAnim.setEvaluator(this.mArgbEvaluator);
            }
        }
    }

    private void setupAnimatorsForTarget(String name, Animator animator) {
        animator.setTarget(this.mAnimatedVectorState.mVectorDrawable.getTargetByName(name));
        if (VERSION.SDK_INT < 21) {
            setupColorAnimator(animator);
        }
        if (this.mAnimatedVectorState.mAnimators == null) {
            this.mAnimatedVectorState.mAnimators = new ArrayList();
            this.mAnimatedVectorState.mTargetNameMap = new ArrayMap();
        }
        this.mAnimatedVectorState.mAnimators.add(animator);
        this.mAnimatedVectorState.mTargetNameMap.put(animator, name);
    }

    public boolean isRunning() {
        if (this.mDelegateDrawable != null) {
            return ((AnimatedVectorDrawable) this.mDelegateDrawable).isRunning();
        }
        return this.mAnimatedVectorState.mAnimatorSet.isRunning();
    }

    public void start() {
        if (this.mDelegateDrawable != null) {
            ((AnimatedVectorDrawable) this.mDelegateDrawable).start();
        } else if (!this.mAnimatedVectorState.mAnimatorSet.isStarted()) {
            this.mAnimatedVectorState.mAnimatorSet.start();
            invalidateSelf();
        }
    }

    public void stop() {
        if (this.mDelegateDrawable != null) {
            ((AnimatedVectorDrawable) this.mDelegateDrawable).stop();
        } else {
            this.mAnimatedVectorState.mAnimatorSet.end();
        }
    }

    @RequiresApi(23)
    private static boolean unregisterPlatformCallback(AnimatedVectorDrawable dr, AnimationCallback callback) {
        return dr.unregisterAnimationCallback(callback.getPlatformCallback());
    }

    public void registerAnimationCallback(@NonNull AnimationCallback callback) {
        if (this.mDelegateDrawable != null) {
            registerPlatformCallback((AnimatedVectorDrawable) this.mDelegateDrawable, callback);
        } else if (callback != null) {
            if (this.mAnimationCallbacks == null) {
                this.mAnimationCallbacks = new ArrayList();
            }
            if (!this.mAnimationCallbacks.contains(callback)) {
                this.mAnimationCallbacks.add(callback);
                if (this.mAnimatorListener == null) {
                    this.mAnimatorListener = new AnimatorListenerAdapter() {
                        public void onAnimationStart(Animator animation) {
                            ArrayList<AnimationCallback> tmpCallbacks = new ArrayList(AnimatedVectorDrawableCompat.this.mAnimationCallbacks);
                            int size = tmpCallbacks.size();
                            for (int i = 0; i < size; i++) {
                                ((AnimationCallback) tmpCallbacks.get(i)).onAnimationStart(AnimatedVectorDrawableCompat.this);
                            }
                        }

                        public void onAnimationEnd(Animator animation) {
                            ArrayList<AnimationCallback> tmpCallbacks = new ArrayList(AnimatedVectorDrawableCompat.this.mAnimationCallbacks);
                            int size = tmpCallbacks.size();
                            for (int i = 0; i < size; i++) {
                                ((AnimationCallback) tmpCallbacks.get(i)).onAnimationEnd(AnimatedVectorDrawableCompat.this);
                            }
                        }
                    };
                }
                this.mAnimatedVectorState.mAnimatorSet.addListener(this.mAnimatorListener);
            }
        }
    }

    @RequiresApi(23)
    private static void registerPlatformCallback(@NonNull AnimatedVectorDrawable avd, @NonNull AnimationCallback callback) {
        avd.registerAnimationCallback(callback.getPlatformCallback());
    }

    private void removeAnimatorSetListener() {
        if (this.mAnimatorListener != null) {
            this.mAnimatedVectorState.mAnimatorSet.removeListener(this.mAnimatorListener);
            this.mAnimatorListener = null;
        }
    }

    public boolean unregisterAnimationCallback(@NonNull AnimationCallback callback) {
        if (this.mDelegateDrawable != null) {
            unregisterPlatformCallback((AnimatedVectorDrawable) this.mDelegateDrawable, callback);
        }
        if (this.mAnimationCallbacks == null || callback == null) {
            return false;
        }
        boolean removed = this.mAnimationCallbacks.remove(callback);
        if (this.mAnimationCallbacks.size() == 0) {
            removeAnimatorSetListener();
        }
        return removed;
    }

    public void clearAnimationCallbacks() {
        if (this.mDelegateDrawable != null) {
            ((AnimatedVectorDrawable) this.mDelegateDrawable).clearAnimationCallbacks();
            return;
        }
        removeAnimatorSetListener();
        if (this.mAnimationCallbacks != null) {
            this.mAnimationCallbacks.clear();
        }
    }

    /* JADX WARNING: Missing block: B:10:0x001e, code:
            return;
     */
    public static void registerAnimationCallback(android.graphics.drawable.Drawable r2, android.support.graphics.drawable.Animatable2Compat.AnimationCallback r3) {
        /*
        if (r2 == 0) goto L_0x001e;
    L_0x0002:
        if (r3 != 0) goto L_0x0005;
    L_0x0004:
        goto L_0x001e;
    L_0x0005:
        r0 = r2 instanceof android.graphics.drawable.Animatable;
        if (r0 != 0) goto L_0x000a;
    L_0x0009:
        return;
    L_0x000a:
        r0 = android.os.Build.VERSION.SDK_INT;
        r1 = 24;
        if (r0 < r1) goto L_0x0017;
    L_0x0010:
        r0 = r2;
        r0 = (android.graphics.drawable.AnimatedVectorDrawable) r0;
        registerPlatformCallback(r0, r3);
        goto L_0x001d;
    L_0x0017:
        r0 = r2;
        r0 = (android.support.graphics.drawable.AnimatedVectorDrawableCompat) r0;
        r0.registerAnimationCallback(r3);
    L_0x001d:
        return;
    L_0x001e:
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.support.graphics.drawable.AnimatedVectorDrawableCompat.registerAnimationCallback(android.graphics.drawable.Drawable, android.support.graphics.drawable.Animatable2Compat$AnimationCallback):void");
    }

    /* JADX WARNING: Missing block: B:12:0x0021, code:
            return false;
     */
    public static boolean unregisterAnimationCallback(android.graphics.drawable.Drawable r2, android.support.graphics.drawable.Animatable2Compat.AnimationCallback r3) {
        /*
        r0 = 0;
        if (r2 == 0) goto L_0x0021;
    L_0x0003:
        if (r3 != 0) goto L_0x0006;
    L_0x0005:
        goto L_0x0021;
    L_0x0006:
        r1 = r2 instanceof android.graphics.drawable.Animatable;
        if (r1 != 0) goto L_0x000b;
    L_0x000a:
        return r0;
    L_0x000b:
        r0 = android.os.Build.VERSION.SDK_INT;
        r1 = 24;
        if (r0 < r1) goto L_0x0019;
    L_0x0011:
        r0 = r2;
        r0 = (android.graphics.drawable.AnimatedVectorDrawable) r0;
        r0 = unregisterPlatformCallback(r0, r3);
        return r0;
    L_0x0019:
        r0 = r2;
        r0 = (android.support.graphics.drawable.AnimatedVectorDrawableCompat) r0;
        r0 = r0.unregisterAnimationCallback(r3);
        return r0;
    L_0x0021:
        return r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.support.graphics.drawable.AnimatedVectorDrawableCompat.unregisterAnimationCallback(android.graphics.drawable.Drawable, android.support.graphics.drawable.Animatable2Compat$AnimationCallback):boolean");
    }

    public static void clearAnimationCallbacks(Drawable dr) {
        if (dr != null && (dr instanceof Animatable)) {
            if (VERSION.SDK_INT >= 24) {
                ((AnimatedVectorDrawable) dr).clearAnimationCallbacks();
            } else {
                ((AnimatedVectorDrawableCompat) dr).clearAnimationCallbacks();
            }
        }
    }
}
