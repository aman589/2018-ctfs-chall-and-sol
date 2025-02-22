package android.support.constraint.solver.widgets;

import android.support.constraint.solver.Cache;
import android.support.constraint.solver.LinearSystem;
import android.support.constraint.solver.SolverVariable;
import android.support.constraint.solver.widgets.ConstraintAnchor.Strength;
import android.support.constraint.solver.widgets.ConstraintAnchor.Type;
import java.util.ArrayList;

public class ConstraintWidget {
    protected static final int ANCHOR_BASELINE = 4;
    protected static final int ANCHOR_BOTTOM = 3;
    protected static final int ANCHOR_LEFT = 0;
    protected static final int ANCHOR_RIGHT = 1;
    protected static final int ANCHOR_TOP = 2;
    private static final boolean AUTOTAG_CENTER = false;
    public static final int CHAIN_PACKED = 2;
    public static final int CHAIN_SPREAD = 0;
    public static final int CHAIN_SPREAD_INSIDE = 1;
    public static float DEFAULT_BIAS = 0.5f;
    static final int DIMENSION_HORIZONTAL = 0;
    static final int DIMENSION_VERTICAL = 1;
    protected static final int DIRECT = 2;
    public static final int GONE = 8;
    public static final int HORIZONTAL = 0;
    public static final int INVISIBLE = 4;
    public static final int MATCH_CONSTRAINT_PERCENT = 2;
    public static final int MATCH_CONSTRAINT_RATIO = 3;
    public static final int MATCH_CONSTRAINT_RATIO_RESOLVED = 4;
    public static final int MATCH_CONSTRAINT_SPREAD = 0;
    public static final int MATCH_CONSTRAINT_WRAP = 1;
    protected static final int SOLVER = 1;
    public static final int UNKNOWN = -1;
    public static final int VERTICAL = 1;
    public static final int VISIBLE = 0;
    private static final int WRAP = -2;
    protected ArrayList<ConstraintAnchor> mAnchors;
    ConstraintAnchor mBaseline;
    int mBaselineDistance;
    ConstraintWidgetGroup mBelongingGroup;
    ConstraintAnchor mBottom;
    boolean mBottomHasCentered;
    ConstraintAnchor mCenter;
    ConstraintAnchor mCenterX;
    ConstraintAnchor mCenterY;
    private float mCircleConstraintAngle;
    private Object mCompanionWidget;
    private int mContainerItemSkip;
    private String mDebugName;
    protected float mDimensionRatio;
    protected int mDimensionRatioSide;
    int mDistToBottom;
    int mDistToLeft;
    int mDistToRight;
    int mDistToTop;
    private int mDrawHeight;
    private int mDrawWidth;
    private int mDrawX;
    private int mDrawY;
    boolean mGroupsToSolver;
    int mHeight;
    float mHorizontalBiasPercent;
    boolean mHorizontalChainFixedPosition;
    int mHorizontalChainStyle;
    ConstraintWidget mHorizontalNextWidget;
    public int mHorizontalResolution;
    boolean mHorizontalWrapVisited;
    boolean mIsHeightWrapContent;
    boolean mIsWidthWrapContent;
    ConstraintAnchor mLeft;
    boolean mLeftHasCentered;
    protected ConstraintAnchor[] mListAnchors;
    protected DimensionBehaviour[] mListDimensionBehaviors;
    protected ConstraintWidget[] mListNextMatchConstraintsWidget;
    int mMatchConstraintDefaultHeight;
    int mMatchConstraintDefaultWidth;
    int mMatchConstraintMaxHeight;
    int mMatchConstraintMaxWidth;
    int mMatchConstraintMinHeight;
    int mMatchConstraintMinWidth;
    float mMatchConstraintPercentHeight;
    float mMatchConstraintPercentWidth;
    private int[] mMaxDimension;
    protected int mMinHeight;
    protected int mMinWidth;
    protected ConstraintWidget[] mNextChainWidget;
    protected int mOffsetX;
    protected int mOffsetY;
    boolean mOptimizerMeasurable;
    boolean mOptimizerMeasured;
    ConstraintWidget mParent;
    int mRelX;
    int mRelY;
    ResolutionDimension mResolutionHeight;
    ResolutionDimension mResolutionWidth;
    float mResolvedDimensionRatio;
    int mResolvedDimensionRatioSide;
    int[] mResolvedMatchConstraintDefault;
    ConstraintAnchor mRight;
    boolean mRightHasCentered;
    ConstraintAnchor mTop;
    boolean mTopHasCentered;
    private String mType;
    float mVerticalBiasPercent;
    boolean mVerticalChainFixedPosition;
    int mVerticalChainStyle;
    ConstraintWidget mVerticalNextWidget;
    public int mVerticalResolution;
    boolean mVerticalWrapVisited;
    private int mVisibility;
    float[] mWeight;
    int mWidth;
    private int mWrapHeight;
    private int mWrapWidth;
    protected int mX;
    protected int mY;

    public enum ContentAlignment {
        BEGIN,
        MIDDLE,
        END,
        TOP,
        VERTICAL_MIDDLE,
        BOTTOM,
        LEFT,
        RIGHT
    }

    public enum DimensionBehaviour {
        FIXED,
        WRAP_CONTENT,
        MATCH_CONSTRAINT,
        MATCH_PARENT
    }

    public int getMaxHeight() {
        return this.mMaxDimension[1];
    }

    public int getMaxWidth() {
        return this.mMaxDimension[0];
    }

    public void setMaxWidth(int maxWidth) {
        this.mMaxDimension[0] = maxWidth;
    }

    public void setMaxHeight(int maxHeight) {
        this.mMaxDimension[1] = maxHeight;
    }

    public boolean isSpreadWidth() {
        return this.mMatchConstraintDefaultWidth == 0 && this.mDimensionRatio == 0.0f && this.mMatchConstraintMinWidth == 0 && this.mMatchConstraintMaxWidth == 0 && this.mListDimensionBehaviors[0] == DimensionBehaviour.MATCH_CONSTRAINT;
    }

    public boolean isSpreadHeight() {
        return this.mMatchConstraintDefaultHeight == 0 && this.mDimensionRatio == 0.0f && this.mMatchConstraintMinHeight == 0 && this.mMatchConstraintMaxHeight == 0 && this.mListDimensionBehaviors[1] == DimensionBehaviour.MATCH_CONSTRAINT;
    }

    public void reset() {
        this.mLeft.reset();
        this.mTop.reset();
        this.mRight.reset();
        this.mBottom.reset();
        this.mBaseline.reset();
        this.mCenterX.reset();
        this.mCenterY.reset();
        this.mCenter.reset();
        this.mParent = null;
        this.mCircleConstraintAngle = 0.0f;
        this.mWidth = 0;
        this.mHeight = 0;
        this.mDimensionRatio = 0.0f;
        this.mDimensionRatioSide = -1;
        this.mX = 0;
        this.mY = 0;
        this.mDrawX = 0;
        this.mDrawY = 0;
        this.mDrawWidth = 0;
        this.mDrawHeight = 0;
        this.mOffsetX = 0;
        this.mOffsetY = 0;
        this.mBaselineDistance = 0;
        this.mMinWidth = 0;
        this.mMinHeight = 0;
        this.mWrapWidth = 0;
        this.mWrapHeight = 0;
        this.mHorizontalBiasPercent = DEFAULT_BIAS;
        this.mVerticalBiasPercent = DEFAULT_BIAS;
        this.mListDimensionBehaviors[0] = DimensionBehaviour.FIXED;
        this.mListDimensionBehaviors[1] = DimensionBehaviour.FIXED;
        this.mCompanionWidget = null;
        this.mContainerItemSkip = 0;
        this.mVisibility = 0;
        this.mType = null;
        this.mHorizontalWrapVisited = false;
        this.mVerticalWrapVisited = false;
        this.mHorizontalChainStyle = 0;
        this.mVerticalChainStyle = 0;
        this.mHorizontalChainFixedPosition = false;
        this.mVerticalChainFixedPosition = false;
        this.mWeight[0] = -1.0f;
        this.mWeight[1] = -1.0f;
        this.mHorizontalResolution = -1;
        this.mVerticalResolution = -1;
        this.mMaxDimension[0] = ActivityChooserViewAdapter.MAX_ACTIVITY_COUNT_UNLIMITED;
        this.mMaxDimension[1] = ActivityChooserViewAdapter.MAX_ACTIVITY_COUNT_UNLIMITED;
        this.mMatchConstraintDefaultWidth = 0;
        this.mMatchConstraintDefaultHeight = 0;
        this.mMatchConstraintPercentWidth = 1.0f;
        this.mMatchConstraintPercentHeight = 1.0f;
        this.mMatchConstraintMaxWidth = ActivityChooserViewAdapter.MAX_ACTIVITY_COUNT_UNLIMITED;
        this.mMatchConstraintMaxHeight = ActivityChooserViewAdapter.MAX_ACTIVITY_COUNT_UNLIMITED;
        this.mMatchConstraintMinWidth = 0;
        this.mMatchConstraintMinHeight = 0;
        this.mResolvedDimensionRatioSide = -1;
        this.mResolvedDimensionRatio = 1.0f;
        if (this.mResolutionWidth != null) {
            this.mResolutionWidth.reset();
        }
        if (this.mResolutionHeight != null) {
            this.mResolutionHeight.reset();
        }
        this.mBelongingGroup = null;
        this.mOptimizerMeasurable = false;
        this.mOptimizerMeasured = false;
        this.mGroupsToSolver = false;
    }

    public void resetResolutionNodes() {
        for (int i = 0; i < 6; i++) {
            this.mListAnchors[i].getResolutionNode().reset();
        }
    }

    public void updateResolutionNodes() {
        for (int i = 0; i < 6; i++) {
            this.mListAnchors[i].getResolutionNode().update();
        }
    }

    public void analyze(int optimizationLevel) {
        Optimizer.analyze(optimizationLevel, this);
    }

    public void resolve() {
    }

    public boolean isFullyResolved() {
        if (this.mLeft.getResolutionNode().state == 1 && this.mRight.getResolutionNode().state == 1 && this.mTop.getResolutionNode().state == 1 && this.mBottom.getResolutionNode().state == 1) {
            return true;
        }
        return false;
    }

    public ResolutionDimension getResolutionWidth() {
        if (this.mResolutionWidth == null) {
            this.mResolutionWidth = new ResolutionDimension();
        }
        return this.mResolutionWidth;
    }

    public ResolutionDimension getResolutionHeight() {
        if (this.mResolutionHeight == null) {
            this.mResolutionHeight = new ResolutionDimension();
        }
        return this.mResolutionHeight;
    }

    public ConstraintWidget() {
        this.mHorizontalResolution = -1;
        this.mVerticalResolution = -1;
        this.mMatchConstraintDefaultWidth = 0;
        this.mMatchConstraintDefaultHeight = 0;
        this.mResolvedMatchConstraintDefault = new int[2];
        this.mMatchConstraintMinWidth = 0;
        this.mMatchConstraintMaxWidth = 0;
        this.mMatchConstraintPercentWidth = 1.0f;
        this.mMatchConstraintMinHeight = 0;
        this.mMatchConstraintMaxHeight = 0;
        this.mMatchConstraintPercentHeight = 1.0f;
        this.mResolvedDimensionRatioSide = -1;
        this.mResolvedDimensionRatio = 1.0f;
        this.mBelongingGroup = null;
        this.mMaxDimension = new int[]{ActivityChooserViewAdapter.MAX_ACTIVITY_COUNT_UNLIMITED, ActivityChooserViewAdapter.MAX_ACTIVITY_COUNT_UNLIMITED};
        this.mCircleConstraintAngle = 0.0f;
        this.mLeft = new ConstraintAnchor(this, Type.LEFT);
        this.mTop = new ConstraintAnchor(this, Type.TOP);
        this.mRight = new ConstraintAnchor(this, Type.RIGHT);
        this.mBottom = new ConstraintAnchor(this, Type.BOTTOM);
        this.mBaseline = new ConstraintAnchor(this, Type.BASELINE);
        this.mCenterX = new ConstraintAnchor(this, Type.CENTER_X);
        this.mCenterY = new ConstraintAnchor(this, Type.CENTER_Y);
        this.mCenter = new ConstraintAnchor(this, Type.CENTER);
        this.mListAnchors = new ConstraintAnchor[]{this.mLeft, this.mRight, this.mTop, this.mBottom, this.mBaseline, this.mCenter};
        this.mAnchors = new ArrayList();
        this.mListDimensionBehaviors = new DimensionBehaviour[]{DimensionBehaviour.FIXED, DimensionBehaviour.FIXED};
        this.mParent = null;
        this.mWidth = 0;
        this.mHeight = 0;
        this.mDimensionRatio = 0.0f;
        this.mDimensionRatioSide = -1;
        this.mX = 0;
        this.mY = 0;
        this.mRelX = 0;
        this.mRelY = 0;
        this.mDrawX = 0;
        this.mDrawY = 0;
        this.mDrawWidth = 0;
        this.mDrawHeight = 0;
        this.mOffsetX = 0;
        this.mOffsetY = 0;
        this.mBaselineDistance = 0;
        this.mHorizontalBiasPercent = DEFAULT_BIAS;
        this.mVerticalBiasPercent = DEFAULT_BIAS;
        this.mContainerItemSkip = 0;
        this.mVisibility = 0;
        this.mDebugName = null;
        this.mType = null;
        this.mOptimizerMeasurable = false;
        this.mOptimizerMeasured = false;
        this.mGroupsToSolver = false;
        this.mHorizontalChainStyle = 0;
        this.mVerticalChainStyle = 0;
        this.mWeight = new float[]{-1.0f, -1.0f};
        this.mListNextMatchConstraintsWidget = new ConstraintWidget[]{null, null};
        this.mNextChainWidget = new ConstraintWidget[]{null, null};
        this.mHorizontalNextWidget = null;
        this.mVerticalNextWidget = null;
        addAnchors();
    }

    public ConstraintWidget(int x, int y, int width, int height) {
        this.mHorizontalResolution = -1;
        this.mVerticalResolution = -1;
        this.mMatchConstraintDefaultWidth = 0;
        this.mMatchConstraintDefaultHeight = 0;
        this.mResolvedMatchConstraintDefault = new int[2];
        this.mMatchConstraintMinWidth = 0;
        this.mMatchConstraintMaxWidth = 0;
        this.mMatchConstraintPercentWidth = 1.0f;
        this.mMatchConstraintMinHeight = 0;
        this.mMatchConstraintMaxHeight = 0;
        this.mMatchConstraintPercentHeight = 1.0f;
        this.mResolvedDimensionRatioSide = -1;
        this.mResolvedDimensionRatio = 1.0f;
        this.mBelongingGroup = null;
        this.mMaxDimension = new int[]{ActivityChooserViewAdapter.MAX_ACTIVITY_COUNT_UNLIMITED, ActivityChooserViewAdapter.MAX_ACTIVITY_COUNT_UNLIMITED};
        this.mCircleConstraintAngle = 0.0f;
        this.mLeft = new ConstraintAnchor(this, Type.LEFT);
        this.mTop = new ConstraintAnchor(this, Type.TOP);
        this.mRight = new ConstraintAnchor(this, Type.RIGHT);
        this.mBottom = new ConstraintAnchor(this, Type.BOTTOM);
        this.mBaseline = new ConstraintAnchor(this, Type.BASELINE);
        this.mCenterX = new ConstraintAnchor(this, Type.CENTER_X);
        this.mCenterY = new ConstraintAnchor(this, Type.CENTER_Y);
        this.mCenter = new ConstraintAnchor(this, Type.CENTER);
        this.mListAnchors = new ConstraintAnchor[]{this.mLeft, this.mRight, this.mTop, this.mBottom, this.mBaseline, this.mCenter};
        this.mAnchors = new ArrayList();
        this.mListDimensionBehaviors = new DimensionBehaviour[]{DimensionBehaviour.FIXED, DimensionBehaviour.FIXED};
        this.mParent = null;
        this.mWidth = 0;
        this.mHeight = 0;
        this.mDimensionRatio = 0.0f;
        this.mDimensionRatioSide = -1;
        this.mX = 0;
        this.mY = 0;
        this.mRelX = 0;
        this.mRelY = 0;
        this.mDrawX = 0;
        this.mDrawY = 0;
        this.mDrawWidth = 0;
        this.mDrawHeight = 0;
        this.mOffsetX = 0;
        this.mOffsetY = 0;
        this.mBaselineDistance = 0;
        this.mHorizontalBiasPercent = DEFAULT_BIAS;
        this.mVerticalBiasPercent = DEFAULT_BIAS;
        this.mContainerItemSkip = 0;
        this.mVisibility = 0;
        this.mDebugName = null;
        this.mType = null;
        this.mOptimizerMeasurable = false;
        this.mOptimizerMeasured = false;
        this.mGroupsToSolver = false;
        this.mHorizontalChainStyle = 0;
        this.mVerticalChainStyle = 0;
        this.mWeight = new float[]{-1.0f, -1.0f};
        this.mListNextMatchConstraintsWidget = new ConstraintWidget[]{null, null};
        this.mNextChainWidget = new ConstraintWidget[]{null, null};
        this.mHorizontalNextWidget = null;
        this.mVerticalNextWidget = null;
        this.mX = x;
        this.mY = y;
        this.mWidth = width;
        this.mHeight = height;
        addAnchors();
        forceUpdateDrawPosition();
    }

    public ConstraintWidget(int width, int height) {
        this(0, 0, width, height);
    }

    public void resetSolverVariables(Cache cache) {
        this.mLeft.resetSolverVariable(cache);
        this.mTop.resetSolverVariable(cache);
        this.mRight.resetSolverVariable(cache);
        this.mBottom.resetSolverVariable(cache);
        this.mBaseline.resetSolverVariable(cache);
        this.mCenter.resetSolverVariable(cache);
        this.mCenterX.resetSolverVariable(cache);
        this.mCenterY.resetSolverVariable(cache);
    }

    private void addAnchors() {
        this.mAnchors.add(this.mLeft);
        this.mAnchors.add(this.mTop);
        this.mAnchors.add(this.mRight);
        this.mAnchors.add(this.mBottom);
        this.mAnchors.add(this.mCenterX);
        this.mAnchors.add(this.mCenterY);
        this.mAnchors.add(this.mCenter);
        this.mAnchors.add(this.mBaseline);
    }

    public boolean isRoot() {
        return this.mParent == null;
    }

    public boolean isRootContainer() {
        return (this instanceof ConstraintWidgetContainer) && (this.mParent == null || !(this.mParent instanceof ConstraintWidgetContainer));
    }

    public boolean isInsideConstraintLayout() {
        ConstraintWidget widget = getParent();
        if (widget == null) {
            return false;
        }
        while (widget != null) {
            if (widget instanceof ConstraintWidgetContainer) {
                return true;
            }
            widget = widget.getParent();
        }
        return false;
    }

    public boolean hasAncestor(ConstraintWidget widget) {
        ConstraintWidget parent = getParent();
        if (parent == widget) {
            return true;
        }
        if (parent == widget.getParent()) {
            return false;
        }
        while (parent != null) {
            if (parent == widget || parent == widget.getParent()) {
                return true;
            }
            parent = parent.getParent();
        }
        return false;
    }

    public WidgetContainer getRootWidgetContainer() {
        ConstraintWidget root = this;
        while (root.getParent() != null) {
            root = root.getParent();
        }
        if (root instanceof WidgetContainer) {
            return (WidgetContainer) root;
        }
        return null;
    }

    public ConstraintWidget getParent() {
        return this.mParent;
    }

    public void setParent(ConstraintWidget widget) {
        this.mParent = widget;
    }

    public void setWidthWrapContent(boolean widthWrapContent) {
        this.mIsWidthWrapContent = widthWrapContent;
    }

    public boolean isWidthWrapContent() {
        return this.mIsWidthWrapContent;
    }

    public void setHeightWrapContent(boolean heightWrapContent) {
        this.mIsHeightWrapContent = heightWrapContent;
    }

    public boolean isHeightWrapContent() {
        return this.mIsHeightWrapContent;
    }

    public void connectCircularConstraint(ConstraintWidget target, float angle, int radius) {
        immediateConnect(Type.CENTER, target, Type.CENTER, radius, 0);
        this.mCircleConstraintAngle = angle;
    }

    public String getType() {
        return this.mType;
    }

    public void setType(String type) {
        this.mType = type;
    }

    public void setVisibility(int visibility) {
        this.mVisibility = visibility;
    }

    public int getVisibility() {
        return this.mVisibility;
    }

    public String getDebugName() {
        return this.mDebugName;
    }

    public void setDebugName(String name) {
        this.mDebugName = name;
    }

    public void setDebugSolverName(LinearSystem system, String name) {
        this.mDebugName = name;
        SolverVariable left = system.createObjectVariable(this.mLeft);
        SolverVariable top = system.createObjectVariable(this.mTop);
        SolverVariable right = system.createObjectVariable(this.mRight);
        SolverVariable bottom = system.createObjectVariable(this.mBottom);
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(name);
        stringBuilder.append(".left");
        left.setName(stringBuilder.toString());
        stringBuilder = new StringBuilder();
        stringBuilder.append(name);
        stringBuilder.append(".top");
        top.setName(stringBuilder.toString());
        stringBuilder = new StringBuilder();
        stringBuilder.append(name);
        stringBuilder.append(".right");
        right.setName(stringBuilder.toString());
        stringBuilder = new StringBuilder();
        stringBuilder.append(name);
        stringBuilder.append(".bottom");
        bottom.setName(stringBuilder.toString());
        if (this.mBaselineDistance > 0) {
            SolverVariable baseline = system.createObjectVariable(this.mBaseline);
            StringBuilder stringBuilder2 = new StringBuilder();
            stringBuilder2.append(name);
            stringBuilder2.append(".baseline");
            baseline.setName(stringBuilder2.toString());
        }
    }

    public void createObjectVariables(LinearSystem system) {
        SolverVariable left = system.createObjectVariable(this.mLeft);
        SolverVariable top = system.createObjectVariable(this.mTop);
        SolverVariable right = system.createObjectVariable(this.mRight);
        SolverVariable bottom = system.createObjectVariable(this.mBottom);
        if (this.mBaselineDistance > 0) {
            system.createObjectVariable(this.mBaseline);
        }
    }

    public String toString() {
        StringBuilder stringBuilder;
        String stringBuilder2;
        StringBuilder stringBuilder3 = new StringBuilder();
        if (this.mType != null) {
            stringBuilder = new StringBuilder();
            stringBuilder.append("type: ");
            stringBuilder.append(this.mType);
            stringBuilder.append(" ");
            stringBuilder2 = stringBuilder.toString();
        } else {
            stringBuilder2 = "";
        }
        stringBuilder3.append(stringBuilder2);
        if (this.mDebugName != null) {
            stringBuilder = new StringBuilder();
            stringBuilder.append("id: ");
            stringBuilder.append(this.mDebugName);
            stringBuilder.append(" ");
            stringBuilder2 = stringBuilder.toString();
        } else {
            stringBuilder2 = "";
        }
        stringBuilder3.append(stringBuilder2);
        stringBuilder3.append("(");
        stringBuilder3.append(this.mX);
        stringBuilder3.append(", ");
        stringBuilder3.append(this.mY);
        stringBuilder3.append(") - (");
        stringBuilder3.append(this.mWidth);
        stringBuilder3.append(" x ");
        stringBuilder3.append(this.mHeight);
        stringBuilder3.append(") wrap: (");
        stringBuilder3.append(this.mWrapWidth);
        stringBuilder3.append(" x ");
        stringBuilder3.append(this.mWrapHeight);
        stringBuilder3.append(")");
        return stringBuilder3.toString();
    }

    int getInternalDrawX() {
        return this.mDrawX;
    }

    int getInternalDrawY() {
        return this.mDrawY;
    }

    public int getInternalDrawRight() {
        return this.mDrawX + this.mDrawWidth;
    }

    public int getInternalDrawBottom() {
        return this.mDrawY + this.mDrawHeight;
    }

    public int getX() {
        return this.mX;
    }

    public int getY() {
        return this.mY;
    }

    public int getWidth() {
        if (this.mVisibility == 8) {
            return 0;
        }
        return this.mWidth;
    }

    public int getOptimizerWrapWidth() {
        int w = this.mWidth;
        if (this.mListDimensionBehaviors[0] != DimensionBehaviour.MATCH_CONSTRAINT) {
            return w;
        }
        if (this.mMatchConstraintDefaultWidth == 1) {
            w = Math.max(this.mMatchConstraintMinWidth, w);
        } else if (this.mMatchConstraintMinWidth > 0) {
            w = this.mMatchConstraintMinWidth;
            this.mWidth = w;
        } else {
            w = 0;
        }
        if (this.mMatchConstraintMaxWidth <= 0 || this.mMatchConstraintMaxWidth >= w) {
            return w;
        }
        return this.mMatchConstraintMaxWidth;
    }

    public int getOptimizerWrapHeight() {
        int h = this.mHeight;
        if (this.mListDimensionBehaviors[1] != DimensionBehaviour.MATCH_CONSTRAINT) {
            return h;
        }
        if (this.mMatchConstraintDefaultHeight == 1) {
            h = Math.max(this.mMatchConstraintMinHeight, h);
        } else if (this.mMatchConstraintMinHeight > 0) {
            h = this.mMatchConstraintMinHeight;
            this.mHeight = h;
        } else {
            h = 0;
        }
        if (this.mMatchConstraintMaxHeight <= 0 || this.mMatchConstraintMaxHeight >= h) {
            return h;
        }
        return this.mMatchConstraintMaxHeight;
    }

    public int getWrapWidth() {
        return this.mWrapWidth;
    }

    public int getHeight() {
        if (this.mVisibility == 8) {
            return 0;
        }
        return this.mHeight;
    }

    public int getWrapHeight() {
        return this.mWrapHeight;
    }

    public int getLength(int orientation) {
        if (orientation == 0) {
            return getWidth();
        }
        if (orientation == 1) {
            return getHeight();
        }
        return 0;
    }

    public int getDrawX() {
        return this.mDrawX + this.mOffsetX;
    }

    public int getDrawY() {
        return this.mDrawY + this.mOffsetY;
    }

    public int getDrawWidth() {
        return this.mDrawWidth;
    }

    public int getDrawHeight() {
        return this.mDrawHeight;
    }

    public int getDrawBottom() {
        return getDrawY() + this.mDrawHeight;
    }

    public int getDrawRight() {
        return getDrawX() + this.mDrawWidth;
    }

    protected int getRootX() {
        return this.mX + this.mOffsetX;
    }

    protected int getRootY() {
        return this.mY + this.mOffsetY;
    }

    public int getMinWidth() {
        return this.mMinWidth;
    }

    public int getMinHeight() {
        return this.mMinHeight;
    }

    public int getLeft() {
        return getX();
    }

    public int getTop() {
        return getY();
    }

    public int getRight() {
        return getX() + this.mWidth;
    }

    public int getBottom() {
        return getY() + this.mHeight;
    }

    public float getHorizontalBiasPercent() {
        return this.mHorizontalBiasPercent;
    }

    public float getVerticalBiasPercent() {
        return this.mVerticalBiasPercent;
    }

    public float getBiasPercent(int orientation) {
        if (orientation == 0) {
            return this.mHorizontalBiasPercent;
        }
        if (orientation == 1) {
            return this.mVerticalBiasPercent;
        }
        return -1.0f;
    }

    public boolean hasBaseline() {
        return this.mBaselineDistance > 0;
    }

    public int getBaselineDistance() {
        return this.mBaselineDistance;
    }

    public Object getCompanionWidget() {
        return this.mCompanionWidget;
    }

    public ArrayList<ConstraintAnchor> getAnchors() {
        return this.mAnchors;
    }

    public void setX(int x) {
        this.mX = x;
    }

    public void setY(int y) {
        this.mY = y;
    }

    public void setOrigin(int x, int y) {
        this.mX = x;
        this.mY = y;
    }

    public void setOffset(int x, int y) {
        this.mOffsetX = x;
        this.mOffsetY = y;
    }

    public void setGoneMargin(Type type, int goneMargin) {
        switch (type) {
            case LEFT:
                this.mLeft.mGoneMargin = goneMargin;
                return;
            case TOP:
                this.mTop.mGoneMargin = goneMargin;
                return;
            case RIGHT:
                this.mRight.mGoneMargin = goneMargin;
                return;
            case BOTTOM:
                this.mBottom.mGoneMargin = goneMargin;
                return;
            default:
                return;
        }
    }

    public void updateDrawPosition() {
        int left = this.mX;
        int top = this.mY;
        int right = this.mX + this.mWidth;
        int bottom = this.mY + this.mHeight;
        this.mDrawX = left;
        this.mDrawY = top;
        this.mDrawWidth = right - left;
        this.mDrawHeight = bottom - top;
    }

    public void forceUpdateDrawPosition() {
        int left = this.mX;
        int top = this.mY;
        int right = this.mX + this.mWidth;
        int bottom = this.mY + this.mHeight;
        this.mDrawX = left;
        this.mDrawY = top;
        this.mDrawWidth = right - left;
        this.mDrawHeight = bottom - top;
    }

    public void setDrawOrigin(int x, int y) {
        this.mDrawX = x - this.mOffsetX;
        this.mDrawY = y - this.mOffsetY;
        this.mX = this.mDrawX;
        this.mY = this.mDrawY;
    }

    public void setDrawX(int x) {
        this.mDrawX = x - this.mOffsetX;
        this.mX = this.mDrawX;
    }

    public void setDrawY(int y) {
        this.mDrawY = y - this.mOffsetY;
        this.mY = this.mDrawY;
    }

    public void setDrawWidth(int drawWidth) {
        this.mDrawWidth = drawWidth;
    }

    public void setDrawHeight(int drawHeight) {
        this.mDrawHeight = drawHeight;
    }

    public void setWidth(int w) {
        this.mWidth = w;
        if (this.mWidth < this.mMinWidth) {
            this.mWidth = this.mMinWidth;
        }
    }

    public void setHeight(int h) {
        this.mHeight = h;
        if (this.mHeight < this.mMinHeight) {
            this.mHeight = this.mMinHeight;
        }
    }

    public void setLength(int length, int orientation) {
        if (orientation == 0) {
            setWidth(length);
        } else if (orientation == 1) {
            setHeight(length);
        }
    }

    public void setHorizontalMatchStyle(int horizontalMatchStyle, int min, int max, float percent) {
        this.mMatchConstraintDefaultWidth = horizontalMatchStyle;
        this.mMatchConstraintMinWidth = min;
        this.mMatchConstraintMaxWidth = max;
        this.mMatchConstraintPercentWidth = percent;
        if (percent < 1.0f && this.mMatchConstraintDefaultWidth == 0) {
            this.mMatchConstraintDefaultWidth = 2;
        }
    }

    public void setVerticalMatchStyle(int verticalMatchStyle, int min, int max, float percent) {
        this.mMatchConstraintDefaultHeight = verticalMatchStyle;
        this.mMatchConstraintMinHeight = min;
        this.mMatchConstraintMaxHeight = max;
        this.mMatchConstraintPercentHeight = percent;
        if (percent < 1.0f && this.mMatchConstraintDefaultHeight == 0) {
            this.mMatchConstraintDefaultHeight = 2;
        }
    }

    public void setDimensionRatio(String ratio) {
        if (ratio == null || ratio.length() == 0) {
            this.mDimensionRatio = 0.0f;
            return;
        }
        int dimensionRatioSide = -1;
        float dimensionRatio = 0.0f;
        int len = ratio.length();
        int commaIndex = ratio.indexOf(44);
        if (commaIndex <= 0 || commaIndex >= len - 1) {
            commaIndex = 0;
        } else {
            String dimension = ratio.substring(null, commaIndex);
            if (dimension.equalsIgnoreCase("W")) {
                dimensionRatioSide = 0;
            } else if (dimension.equalsIgnoreCase("H")) {
                dimensionRatioSide = 1;
            }
            commaIndex++;
        }
        int colonIndex = ratio.indexOf(58);
        if (colonIndex < 0 || colonIndex >= len - 1) {
            String r = ratio.substring(commaIndex);
            if (r.length() > 0) {
                try {
                    dimensionRatio = Float.parseFloat(r);
                } catch (NumberFormatException e) {
                }
            }
        } else {
            String nominator = ratio.substring(commaIndex, colonIndex);
            String denominator = ratio.substring(colonIndex + 1);
            if (nominator.length() > 0 && denominator.length() > 0) {
                try {
                    float nominatorValue = Float.parseFloat(nominator);
                    float denominatorValue = Float.parseFloat(denominator);
                    if (nominatorValue > 0.0f && denominatorValue > 0.0f) {
                        dimensionRatio = dimensionRatioSide == 1 ? Math.abs(denominatorValue / nominatorValue) : Math.abs(nominatorValue / denominatorValue);
                    }
                } catch (NumberFormatException e2) {
                }
            }
        }
        if (dimensionRatio > 0.0f) {
            this.mDimensionRatio = dimensionRatio;
            this.mDimensionRatioSide = dimensionRatioSide;
        }
    }

    public void setDimensionRatio(float ratio, int dimensionRatioSide) {
        this.mDimensionRatio = ratio;
        this.mDimensionRatioSide = dimensionRatioSide;
    }

    public float getDimensionRatio() {
        return this.mDimensionRatio;
    }

    public int getDimensionRatioSide() {
        return this.mDimensionRatioSide;
    }

    public void setHorizontalBiasPercent(float horizontalBiasPercent) {
        this.mHorizontalBiasPercent = horizontalBiasPercent;
    }

    public void setVerticalBiasPercent(float verticalBiasPercent) {
        this.mVerticalBiasPercent = verticalBiasPercent;
    }

    public void setMinWidth(int w) {
        if (w < 0) {
            this.mMinWidth = 0;
        } else {
            this.mMinWidth = w;
        }
    }

    public void setMinHeight(int h) {
        if (h < 0) {
            this.mMinHeight = 0;
        } else {
            this.mMinHeight = h;
        }
    }

    public void setWrapWidth(int w) {
        this.mWrapWidth = w;
    }

    public void setWrapHeight(int h) {
        this.mWrapHeight = h;
    }

    public void setDimension(int w, int h) {
        this.mWidth = w;
        if (this.mWidth < this.mMinWidth) {
            this.mWidth = this.mMinWidth;
        }
        this.mHeight = h;
        if (this.mHeight < this.mMinHeight) {
            this.mHeight = this.mMinHeight;
        }
    }

    public void setFrame(int left, int top, int right, int bottom) {
        int w = right - left;
        int h = bottom - top;
        this.mX = left;
        this.mY = top;
        if (this.mVisibility == 8) {
            this.mWidth = 0;
            this.mHeight = 0;
            return;
        }
        if (this.mListDimensionBehaviors[0] == DimensionBehaviour.FIXED && w < this.mWidth) {
            w = this.mWidth;
        }
        if (this.mListDimensionBehaviors[1] == DimensionBehaviour.FIXED && h < this.mHeight) {
            h = this.mHeight;
        }
        this.mWidth = w;
        this.mHeight = h;
        if (this.mHeight < this.mMinHeight) {
            this.mHeight = this.mMinHeight;
        }
        if (this.mWidth < this.mMinWidth) {
            this.mWidth = this.mMinWidth;
        }
        this.mOptimizerMeasured = true;
    }

    public void setFrame(int start, int end, int orientation) {
        if (orientation == 0) {
            setHorizontalDimension(start, end);
        } else if (orientation == 1) {
            setVerticalDimension(start, end);
        }
        this.mOptimizerMeasured = true;
    }

    public void setHorizontalDimension(int left, int right) {
        this.mX = left;
        this.mWidth = right - left;
        if (this.mWidth < this.mMinWidth) {
            this.mWidth = this.mMinWidth;
        }
    }

    public void setVerticalDimension(int top, int bottom) {
        this.mY = top;
        this.mHeight = bottom - top;
        if (this.mHeight < this.mMinHeight) {
            this.mHeight = this.mMinHeight;
        }
    }

    int getRelativePositioning(int orientation) {
        if (orientation == 0) {
            return this.mRelX;
        }
        if (orientation == 1) {
            return this.mRelY;
        }
        return 0;
    }

    void setRelativePositioning(int offset, int orientation) {
        if (orientation == 0) {
            this.mRelX = offset;
        } else if (orientation == 1) {
            this.mRelY = offset;
        }
    }

    public void setBaselineDistance(int baseline) {
        this.mBaselineDistance = baseline;
    }

    public void setCompanionWidget(Object companion) {
        this.mCompanionWidget = companion;
    }

    public void setContainerItemSkip(int skip) {
        if (skip >= 0) {
            this.mContainerItemSkip = skip;
        } else {
            this.mContainerItemSkip = 0;
        }
    }

    public int getContainerItemSkip() {
        return this.mContainerItemSkip;
    }

    public void setHorizontalWeight(float horizontalWeight) {
        this.mWeight[0] = horizontalWeight;
    }

    public void setVerticalWeight(float verticalWeight) {
        this.mWeight[1] = verticalWeight;
    }

    public void setHorizontalChainStyle(int horizontalChainStyle) {
        this.mHorizontalChainStyle = horizontalChainStyle;
    }

    public int getHorizontalChainStyle() {
        return this.mHorizontalChainStyle;
    }

    public void setVerticalChainStyle(int verticalChainStyle) {
        this.mVerticalChainStyle = verticalChainStyle;
    }

    public int getVerticalChainStyle() {
        return this.mVerticalChainStyle;
    }

    public boolean allowedInBarrier() {
        return this.mVisibility != 8;
    }

    public void connectedTo(ConstraintWidget source) {
    }

    public void immediateConnect(Type startType, ConstraintWidget target, Type endType, int margin, int goneMargin) {
        ConstraintAnchor startAnchor = getAnchor(startType);
        startAnchor.connect(target.getAnchor(endType), margin, goneMargin, Strength.STRONG, 0, true);
    }

    public void connect(ConstraintAnchor from, ConstraintAnchor to, int margin, int creator) {
        connect(from, to, margin, Strength.STRONG, creator);
    }

    public void connect(ConstraintAnchor from, ConstraintAnchor to, int margin) {
        connect(from, to, margin, Strength.STRONG, 0);
    }

    public void connect(ConstraintAnchor from, ConstraintAnchor to, int margin, Strength strength, int creator) {
        if (from.getOwner() == this) {
            connect(from.getType(), to.getOwner(), to.getType(), margin, strength, creator);
        }
    }

    public void connect(Type constraintFrom, ConstraintWidget target, Type constraintTo, int margin) {
        connect(constraintFrom, target, constraintTo, margin, Strength.STRONG);
    }

    public void connect(Type constraintFrom, ConstraintWidget target, Type constraintTo) {
        connect(constraintFrom, target, constraintTo, 0, Strength.STRONG);
    }

    public void connect(Type constraintFrom, ConstraintWidget target, Type constraintTo, int margin, Strength strength) {
        connect(constraintFrom, target, constraintTo, margin, strength, 0);
    }

    public void connect(Type constraintFrom, ConstraintWidget target, Type constraintTo, int margin, Strength strength, int creator) {
        Type type = constraintFrom;
        ConstraintWidget constraintWidget = target;
        Type type2 = constraintTo;
        int i = creator;
        ConstraintAnchor left;
        ConstraintAnchor targetAnchor;
        ConstraintAnchor right;
        if (type == Type.CENTER) {
            ConstraintWidget constraintWidget2;
            Strength strength2;
            int i2;
            if (type2 == Type.CENTER) {
                ConstraintAnchor left2 = getAnchor(Type.LEFT);
                ConstraintAnchor right2 = getAnchor(Type.RIGHT);
                ConstraintAnchor top = getAnchor(Type.TOP);
                ConstraintAnchor bottom = getAnchor(Type.BOTTOM);
                boolean centerX = false;
                boolean centerY = false;
                if ((left2 == null || !left2.isConnected()) && (right2 == null || !right2.isConnected())) {
                    constraintWidget2 = target;
                    strength2 = strength;
                    i2 = creator;
                    connect(Type.LEFT, constraintWidget2, Type.LEFT, 0, strength2, i2);
                    connect(Type.RIGHT, constraintWidget2, Type.RIGHT, 0, strength2, i2);
                    centerX = true;
                }
                if ((top == null || !top.isConnected()) && (bottom == null || !bottom.isConnected())) {
                    constraintWidget2 = target;
                    strength2 = strength;
                    i2 = creator;
                    connect(Type.TOP, constraintWidget2, Type.TOP, 0, strength2, i2);
                    connect(Type.BOTTOM, constraintWidget2, Type.BOTTOM, 0, strength2, i2);
                    centerY = true;
                }
                if (centerX && centerY) {
                    getAnchor(Type.CENTER).connect(constraintWidget.getAnchor(Type.CENTER), 0, i);
                } else if (centerX) {
                    getAnchor(Type.CENTER_X).connect(constraintWidget.getAnchor(Type.CENTER_X), 0, i);
                } else if (centerY) {
                    getAnchor(Type.CENTER_Y).connect(constraintWidget.getAnchor(Type.CENTER_Y), 0, i);
                }
            } else if (type2 == Type.LEFT || type2 == Type.RIGHT) {
                connect(Type.LEFT, target, constraintTo, 0, strength, creator);
                try {
                    connect(Type.RIGHT, target, constraintTo, 0, strength, creator);
                    getAnchor(Type.CENTER).connect(target.getAnchor(constraintTo), 0, i);
                } catch (Throwable th) {
                    Throwable th2 = th;
                }
            } else if (type2 == Type.TOP || type2 == Type.BOTTOM) {
                constraintWidget2 = target;
                Type type3 = constraintTo;
                strength2 = strength;
                i2 = creator;
                connect(Type.TOP, constraintWidget2, type3, 0, strength2, i2);
                connect(Type.BOTTOM, constraintWidget2, type3, 0, strength2, i2);
                getAnchor(Type.CENTER).connect(target.getAnchor(constraintTo), 0, i);
            }
        } else if (type == Type.CENTER_X && (type2 == Type.LEFT || type2 == Type.RIGHT)) {
            left = getAnchor(Type.LEFT);
            targetAnchor = target.getAnchor(constraintTo);
            right = getAnchor(Type.RIGHT);
            left.connect(targetAnchor, 0, i);
            right.connect(targetAnchor, 0, i);
            getAnchor(Type.CENTER_X).connect(targetAnchor, 0, i);
        } else if (type == Type.CENTER_Y && (type2 == Type.TOP || type2 == Type.BOTTOM)) {
            left = target.getAnchor(constraintTo);
            getAnchor(Type.TOP).connect(left, 0, i);
            getAnchor(Type.BOTTOM).connect(left, 0, i);
            getAnchor(Type.CENTER_Y).connect(left, 0, i);
        } else if (type == Type.CENTER_X && type2 == Type.CENTER_X) {
            getAnchor(Type.LEFT).connect(constraintWidget.getAnchor(Type.LEFT), 0, i);
            getAnchor(Type.RIGHT).connect(constraintWidget.getAnchor(Type.RIGHT), 0, i);
            getAnchor(Type.CENTER_X).connect(target.getAnchor(constraintTo), 0, i);
        } else if (type == Type.CENTER_Y && type2 == Type.CENTER_Y) {
            getAnchor(Type.TOP).connect(constraintWidget.getAnchor(Type.TOP), 0, i);
            getAnchor(Type.BOTTOM).connect(constraintWidget.getAnchor(Type.BOTTOM), 0, i);
            getAnchor(Type.CENTER_Y).connect(target.getAnchor(constraintTo), 0, i);
        } else {
            left = getAnchor(constraintFrom);
            targetAnchor = target.getAnchor(constraintTo);
            if (left.isValidConnection(targetAnchor)) {
                int margin2;
                ConstraintAnchor bottom2;
                if (type == Type.BASELINE) {
                    right = getAnchor(Type.TOP);
                    bottom2 = getAnchor(Type.BOTTOM);
                    if (right != null) {
                        right.reset();
                    }
                    if (bottom2 != null) {
                        bottom2.reset();
                    }
                    margin2 = 0;
                } else {
                    ConstraintAnchor opposite;
                    if (type == Type.TOP || type == Type.BOTTOM) {
                        right = getAnchor(Type.BASELINE);
                        if (right != null) {
                            right.reset();
                        }
                        bottom2 = getAnchor(Type.CENTER);
                        if (bottom2.getTarget() != targetAnchor) {
                            bottom2.reset();
                        }
                        opposite = getAnchor(constraintFrom).getOpposite();
                        ConstraintAnchor centerY2 = getAnchor(Type.CENTER_Y);
                        if (centerY2.isConnected()) {
                            opposite.reset();
                            centerY2.reset();
                        }
                    } else if (type == Type.LEFT || type == Type.RIGHT) {
                        right = getAnchor(Type.CENTER);
                        if (right.getTarget() != targetAnchor) {
                            right.reset();
                        }
                        bottom2 = getAnchor(constraintFrom).getOpposite();
                        opposite = getAnchor(Type.CENTER_X);
                        if (opposite.isConnected()) {
                            bottom2.reset();
                            opposite.reset();
                        }
                    }
                    margin2 = margin;
                }
                left.connect(targetAnchor, margin2, strength, i);
                targetAnchor.getOwner().connectedTo(left.getOwner());
                return;
            }
        }
        Strength strength3 = strength;
    }

    public void resetAllConstraints() {
        resetAnchors();
        setVerticalBiasPercent(DEFAULT_BIAS);
        setHorizontalBiasPercent(DEFAULT_BIAS);
        if (!(this instanceof ConstraintWidgetContainer)) {
            if (getHorizontalDimensionBehaviour() == DimensionBehaviour.MATCH_CONSTRAINT) {
                if (getWidth() == getWrapWidth()) {
                    setHorizontalDimensionBehaviour(DimensionBehaviour.WRAP_CONTENT);
                } else if (getWidth() > getMinWidth()) {
                    setHorizontalDimensionBehaviour(DimensionBehaviour.FIXED);
                }
            }
            if (getVerticalDimensionBehaviour() == DimensionBehaviour.MATCH_CONSTRAINT) {
                if (getHeight() == getWrapHeight()) {
                    setVerticalDimensionBehaviour(DimensionBehaviour.WRAP_CONTENT);
                } else if (getHeight() > getMinHeight()) {
                    setVerticalDimensionBehaviour(DimensionBehaviour.FIXED);
                }
            }
        }
    }

    public void resetAnchor(ConstraintAnchor anchor) {
        if (getParent() == null || !(getParent() instanceof ConstraintWidgetContainer) || !((ConstraintWidgetContainer) getParent()).handlesInternalConstraints()) {
            ConstraintAnchor left = getAnchor(Type.LEFT);
            ConstraintAnchor right = getAnchor(Type.RIGHT);
            ConstraintAnchor top = getAnchor(Type.TOP);
            ConstraintAnchor bottom = getAnchor(Type.BOTTOM);
            ConstraintAnchor center = getAnchor(Type.CENTER);
            ConstraintAnchor centerX = getAnchor(Type.CENTER_X);
            ConstraintAnchor centerY = getAnchor(Type.CENTER_Y);
            if (anchor == center) {
                if (left.isConnected() && right.isConnected() && left.getTarget() == right.getTarget()) {
                    left.reset();
                    right.reset();
                }
                if (top.isConnected() && bottom.isConnected() && top.getTarget() == bottom.getTarget()) {
                    top.reset();
                    bottom.reset();
                }
                this.mHorizontalBiasPercent = 0.5f;
                this.mVerticalBiasPercent = 0.5f;
            } else if (anchor == centerX) {
                if (left.isConnected() && right.isConnected() && left.getTarget().getOwner() == right.getTarget().getOwner()) {
                    left.reset();
                    right.reset();
                }
                this.mHorizontalBiasPercent = 0.5f;
            } else if (anchor == centerY) {
                if (top.isConnected() && bottom.isConnected() && top.getTarget().getOwner() == bottom.getTarget().getOwner()) {
                    top.reset();
                    bottom.reset();
                }
                this.mVerticalBiasPercent = 0.5f;
            } else if (anchor == left || anchor == right) {
                if (left.isConnected() && left.getTarget() == right.getTarget()) {
                    center.reset();
                }
            } else if ((anchor == top || anchor == bottom) && top.isConnected() && top.getTarget() == bottom.getTarget()) {
                center.reset();
            }
            anchor.reset();
        }
    }

    public void resetAnchors() {
        ConstraintWidget parent = getParent();
        if (parent == null || !(parent instanceof ConstraintWidgetContainer) || !((ConstraintWidgetContainer) getParent()).handlesInternalConstraints()) {
            int mAnchorsSize = this.mAnchors.size();
            for (int i = 0; i < mAnchorsSize; i++) {
                ((ConstraintAnchor) this.mAnchors.get(i)).reset();
            }
        }
    }

    public void resetAnchors(int connectionCreator) {
        ConstraintWidget parent = getParent();
        if (parent == null || !(parent instanceof ConstraintWidgetContainer) || !((ConstraintWidgetContainer) getParent()).handlesInternalConstraints()) {
            int mAnchorsSize = this.mAnchors.size();
            for (int i = 0; i < mAnchorsSize; i++) {
                ConstraintAnchor anchor = (ConstraintAnchor) this.mAnchors.get(i);
                if (connectionCreator == anchor.getConnectionCreator()) {
                    if (anchor.isVerticalAnchor()) {
                        setVerticalBiasPercent(DEFAULT_BIAS);
                    } else {
                        setHorizontalBiasPercent(DEFAULT_BIAS);
                    }
                    anchor.reset();
                }
            }
        }
    }

    public void disconnectWidget(ConstraintWidget widget) {
        ArrayList<ConstraintAnchor> anchors = getAnchors();
        int anchorsSize = anchors.size();
        for (int i = 0; i < anchorsSize; i++) {
            ConstraintAnchor anchor = (ConstraintAnchor) anchors.get(i);
            if (anchor.isConnected() && anchor.getTarget().getOwner() == widget) {
                anchor.reset();
            }
        }
    }

    public void disconnectUnlockedWidget(ConstraintWidget widget) {
        ArrayList<ConstraintAnchor> anchors = getAnchors();
        int anchorsSize = anchors.size();
        for (int i = 0; i < anchorsSize; i++) {
            ConstraintAnchor anchor = (ConstraintAnchor) anchors.get(i);
            if (anchor.isConnected() && anchor.getTarget().getOwner() == widget && anchor.getConnectionCreator() == 2) {
                anchor.reset();
            }
        }
    }

    public ConstraintAnchor getAnchor(Type anchorType) {
        switch (anchorType) {
            case LEFT:
                return this.mLeft;
            case TOP:
                return this.mTop;
            case RIGHT:
                return this.mRight;
            case BOTTOM:
                return this.mBottom;
            case BASELINE:
                return this.mBaseline;
            case CENTER:
                return this.mCenter;
            case CENTER_X:
                return this.mCenterX;
            case CENTER_Y:
                return this.mCenterY;
            case NONE:
                return null;
            default:
                throw new AssertionError(anchorType.name());
        }
    }

    public DimensionBehaviour getHorizontalDimensionBehaviour() {
        return this.mListDimensionBehaviors[0];
    }

    public DimensionBehaviour getVerticalDimensionBehaviour() {
        return this.mListDimensionBehaviors[1];
    }

    public DimensionBehaviour getDimensionBehaviour(int orientation) {
        if (orientation == 0) {
            return getHorizontalDimensionBehaviour();
        }
        if (orientation == 1) {
            return getVerticalDimensionBehaviour();
        }
        return null;
    }

    public void setHorizontalDimensionBehaviour(DimensionBehaviour behaviour) {
        this.mListDimensionBehaviors[0] = behaviour;
        if (behaviour == DimensionBehaviour.WRAP_CONTENT) {
            setWidth(this.mWrapWidth);
        }
    }

    public void setVerticalDimensionBehaviour(DimensionBehaviour behaviour) {
        this.mListDimensionBehaviors[1] = behaviour;
        if (behaviour == DimensionBehaviour.WRAP_CONTENT) {
            setHeight(this.mWrapHeight);
        }
    }

    public boolean isInHorizontalChain() {
        if ((this.mLeft.mTarget == null || this.mLeft.mTarget.mTarget != this.mLeft) && (this.mRight.mTarget == null || this.mRight.mTarget.mTarget != this.mRight)) {
            return false;
        }
        return true;
    }

    public ConstraintWidget getHorizontalChainControlWidget() {
        if (!isInHorizontalChain()) {
            return null;
        }
        ConstraintWidget found = null;
        ConstraintWidget found2 = this;
        while (found == null && found2 != null) {
            ConstraintAnchor anchor = found2.getAnchor(Type.LEFT);
            ConstraintAnchor targetAnchor = null;
            ConstraintAnchor targetOwner = anchor == null ? null : anchor.getTarget();
            ConstraintWidget target = targetOwner == null ? null : targetOwner.getOwner();
            if (target == getParent()) {
                return found2;
            }
            if (target != null) {
                targetAnchor = target.getAnchor(Type.RIGHT).getTarget();
            }
            if (targetAnchor == null || targetAnchor.getOwner() == found2) {
                found2 = target;
            } else {
                found = found2;
            }
        }
        return found;
    }

    public boolean isInVerticalChain() {
        if ((this.mTop.mTarget == null || this.mTop.mTarget.mTarget != this.mTop) && (this.mBottom.mTarget == null || this.mBottom.mTarget.mTarget != this.mBottom)) {
            return false;
        }
        return true;
    }

    public ConstraintWidget getVerticalChainControlWidget() {
        if (!isInVerticalChain()) {
            return null;
        }
        ConstraintWidget found = null;
        ConstraintWidget found2 = this;
        while (found == null && found2 != null) {
            ConstraintAnchor anchor = found2.getAnchor(Type.TOP);
            ConstraintAnchor targetAnchor = null;
            ConstraintAnchor targetOwner = anchor == null ? null : anchor.getTarget();
            ConstraintWidget target = targetOwner == null ? null : targetOwner.getOwner();
            if (target == getParent()) {
                return found2;
            }
            if (target != null) {
                targetAnchor = target.getAnchor(Type.BOTTOM).getTarget();
            }
            if (targetAnchor == null || targetAnchor.getOwner() == found2) {
                found2 = target;
            } else {
                found = found2;
            }
        }
        return found;
    }

    private boolean isChainHead(int orientation) {
        int offset = orientation * 2;
        return (this.mListAnchors[offset].mTarget == null || this.mListAnchors[offset].mTarget.mTarget == this.mListAnchors[offset] || this.mListAnchors[offset + 1].mTarget == null || this.mListAnchors[offset + 1].mTarget.mTarget != this.mListAnchors[offset + 1]) ? false : true;
    }

    /* JADX WARNING: Removed duplicated region for block: B:155:0x02dd  */
    /* JADX WARNING: Removed duplicated region for block: B:154:0x02d2  */
    /* JADX WARNING: Removed duplicated region for block: B:159:0x02ee  */
    /* JADX WARNING: Removed duplicated region for block: B:158:0x02e3  */
    /* JADX WARNING: Removed duplicated region for block: B:162:0x032b  */
    /* JADX WARNING: Removed duplicated region for block: B:168:0x0357  */
    /* JADX WARNING: Removed duplicated region for block: B:113:0x01df  */
    /* JADX WARNING: Removed duplicated region for block: B:125:0x024f  */
    /* JADX WARNING: Removed duplicated region for block: B:116:0x01e9  */
    /* JADX WARNING: Removed duplicated region for block: B:129:0x0263  */
    /* JADX WARNING: Removed duplicated region for block: B:128:0x0262 A:{RETURN} */
    /* JADX WARNING: Removed duplicated region for block: B:103:0x01c1  */
    /* JADX WARNING: Removed duplicated region for block: B:97:0x01b4  */
    /* JADX WARNING: Removed duplicated region for block: B:107:0x01cc  */
    /* JADX WARNING: Removed duplicated region for block: B:113:0x01df  */
    /* JADX WARNING: Removed duplicated region for block: B:116:0x01e9  */
    /* JADX WARNING: Removed duplicated region for block: B:125:0x024f  */
    /* JADX WARNING: Removed duplicated region for block: B:128:0x0262 A:{RETURN} */
    /* JADX WARNING: Removed duplicated region for block: B:129:0x0263  */
    /* JADX WARNING: Removed duplicated region for block: B:97:0x01b4  */
    /* JADX WARNING: Removed duplicated region for block: B:103:0x01c1  */
    /* JADX WARNING: Removed duplicated region for block: B:107:0x01cc  */
    /* JADX WARNING: Removed duplicated region for block: B:113:0x01df  */
    /* JADX WARNING: Removed duplicated region for block: B:125:0x024f  */
    /* JADX WARNING: Removed duplicated region for block: B:116:0x01e9  */
    /* JADX WARNING: Removed duplicated region for block: B:129:0x0263  */
    /* JADX WARNING: Removed duplicated region for block: B:128:0x0262 A:{RETURN} */
    /* JADX WARNING: Removed duplicated region for block: B:103:0x01c1  */
    /* JADX WARNING: Removed duplicated region for block: B:97:0x01b4  */
    /* JADX WARNING: Removed duplicated region for block: B:107:0x01cc  */
    /* JADX WARNING: Removed duplicated region for block: B:113:0x01df  */
    /* JADX WARNING: Removed duplicated region for block: B:116:0x01e9  */
    /* JADX WARNING: Removed duplicated region for block: B:125:0x024f  */
    /* JADX WARNING: Removed duplicated region for block: B:128:0x0262 A:{RETURN} */
    /* JADX WARNING: Removed duplicated region for block: B:129:0x0263  */
    /* JADX WARNING: Removed duplicated region for block: B:97:0x01b4  */
    /* JADX WARNING: Removed duplicated region for block: B:103:0x01c1  */
    /* JADX WARNING: Removed duplicated region for block: B:107:0x01cc  */
    /* JADX WARNING: Removed duplicated region for block: B:113:0x01df  */
    /* JADX WARNING: Removed duplicated region for block: B:125:0x024f  */
    /* JADX WARNING: Removed duplicated region for block: B:116:0x01e9  */
    /* JADX WARNING: Removed duplicated region for block: B:129:0x0263  */
    /* JADX WARNING: Removed duplicated region for block: B:128:0x0262 A:{RETURN} */
    /* JADX WARNING: Missing block: B:100:0x01bb, code:
            if (r15.mResolvedDimensionRatioSide == -1) goto L_0x01bf;
     */
    public void addToSolver(android.support.constraint.solver.LinearSystem r53) {
        /*
        r52 = this;
        r15 = r52;
        r10 = r53;
        r0 = r15.mLeft;
        r36 = r10.createObjectVariable(r0);
        r0 = r15.mRight;
        r2 = r10.createObjectVariable(r0);
        r0 = r15.mTop;
        r1 = r10.createObjectVariable(r0);
        r0 = r15.mBottom;
        r0 = r10.createObjectVariable(r0);
        r3 = r15.mBaseline;
        r13 = r10.createObjectVariable(r3);
        r3 = 0;
        r4 = 0;
        r5 = 0;
        r6 = 0;
        r7 = r15.mParent;
        r8 = 8;
        r12 = 0;
        r11 = 1;
        if (r7 == 0) goto L_0x00b6;
    L_0x002e:
        r7 = r15.mParent;
        if (r7 == 0) goto L_0x003e;
    L_0x0032:
        r7 = r15.mParent;
        r7 = r7.mListDimensionBehaviors;
        r7 = r7[r12];
        r9 = android.support.constraint.solver.widgets.ConstraintWidget.DimensionBehaviour.WRAP_CONTENT;
        if (r7 != r9) goto L_0x003e;
    L_0x003c:
        r7 = 1;
        goto L_0x003f;
    L_0x003e:
        r7 = 0;
    L_0x003f:
        r5 = r7;
        r7 = r15.mParent;
        if (r7 == 0) goto L_0x0050;
    L_0x0044:
        r7 = r15.mParent;
        r7 = r7.mListDimensionBehaviors;
        r7 = r7[r11];
        r9 = android.support.constraint.solver.widgets.ConstraintWidget.DimensionBehaviour.WRAP_CONTENT;
        if (r7 != r9) goto L_0x0050;
    L_0x004e:
        r7 = 1;
        goto L_0x0051;
    L_0x0050:
        r7 = 0;
    L_0x0051:
        r6 = r7;
        r7 = r15.isChainHead(r12);
        if (r7 == 0) goto L_0x0061;
    L_0x0058:
        r7 = r15.mParent;
        r7 = (android.support.constraint.solver.widgets.ConstraintWidgetContainer) r7;
        r7.addChain(r15, r12);
        r3 = 1;
        goto L_0x0065;
    L_0x0061:
        r3 = r52.isInHorizontalChain();
    L_0x0065:
        r7 = r15.isChainHead(r11);
        if (r7 == 0) goto L_0x0074;
    L_0x006b:
        r7 = r15.mParent;
        r7 = (android.support.constraint.solver.widgets.ConstraintWidgetContainer) r7;
        r7.addChain(r15, r11);
        r4 = 1;
        goto L_0x0078;
    L_0x0074:
        r4 = r52.isInVerticalChain();
    L_0x0078:
        if (r5 == 0) goto L_0x0095;
    L_0x007a:
        r7 = r15.mVisibility;
        if (r7 == r8) goto L_0x0095;
    L_0x007e:
        r7 = r15.mLeft;
        r7 = r7.mTarget;
        if (r7 != 0) goto L_0x0095;
    L_0x0084:
        r7 = r15.mRight;
        r7 = r7.mTarget;
        if (r7 != 0) goto L_0x0095;
    L_0x008a:
        r7 = r15.mParent;
        r7 = r7.mRight;
        r7 = r10.createObjectVariable(r7);
        r10.addGreaterThan(r7, r2, r12, r11);
    L_0x0095:
        if (r6 == 0) goto L_0x00b6;
    L_0x0097:
        r7 = r15.mVisibility;
        if (r7 == r8) goto L_0x00b6;
    L_0x009b:
        r7 = r15.mTop;
        r7 = r7.mTarget;
        if (r7 != 0) goto L_0x00b6;
    L_0x00a1:
        r7 = r15.mBottom;
        r7 = r7.mTarget;
        if (r7 != 0) goto L_0x00b6;
    L_0x00a7:
        r7 = r15.mBaseline;
        if (r7 != 0) goto L_0x00b6;
    L_0x00ab:
        r7 = r15.mParent;
        r7 = r7.mBottom;
        r7 = r10.createObjectVariable(r7);
        r10.addGreaterThan(r7, r0, r12, r11);
    L_0x00b6:
        r37 = r3;
        r38 = r4;
        r9 = r5;
        r7 = r6;
        r3 = r15.mWidth;
        r4 = r15.mMinWidth;
        if (r3 >= r4) goto L_0x00c4;
    L_0x00c2:
        r3 = r15.mMinWidth;
    L_0x00c4:
        r4 = r15.mHeight;
        r5 = r15.mMinHeight;
        if (r4 >= r5) goto L_0x00cc;
    L_0x00ca:
        r4 = r15.mMinHeight;
    L_0x00cc:
        r5 = r15.mListDimensionBehaviors;
        r5 = r5[r12];
        r6 = android.support.constraint.solver.widgets.ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT;
        if (r5 == r6) goto L_0x00d6;
    L_0x00d4:
        r5 = 1;
        goto L_0x00d7;
    L_0x00d6:
        r5 = 0;
    L_0x00d7:
        r6 = r15.mListDimensionBehaviors;
        r6 = r6[r11];
        r14 = android.support.constraint.solver.widgets.ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT;
        if (r6 == r14) goto L_0x00e1;
    L_0x00df:
        r6 = 1;
        goto L_0x00e2;
    L_0x00e1:
        r6 = 0;
    L_0x00e2:
        r14 = 0;
        r11 = r15.mDimensionRatioSide;
        r15.mResolvedDimensionRatioSide = r11;
        r11 = r15.mDimensionRatio;
        r15.mResolvedDimensionRatio = r11;
        r11 = r15.mMatchConstraintDefaultWidth;
        r12 = r15.mMatchConstraintDefaultHeight;
        r8 = r15.mDimensionRatio;
        r16 = 0;
        r42 = r2;
        r8 = (r8 > r16 ? 1 : (r8 == r16 ? 0 : -1));
        if (r8 <= 0) goto L_0x019c;
    L_0x00f9:
        r8 = r15.mVisibility;
        r2 = 8;
        if (r8 == r2) goto L_0x019c;
    L_0x00ff:
        r14 = 1;
        r2 = r15.mListDimensionBehaviors;
        r8 = 0;
        r2 = r2[r8];
        r8 = android.support.constraint.solver.widgets.ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT;
        if (r2 != r8) goto L_0x010c;
    L_0x0109:
        if (r11 != 0) goto L_0x010c;
    L_0x010b:
        r11 = 3;
    L_0x010c:
        r2 = r15.mListDimensionBehaviors;
        r8 = 1;
        r2 = r2[r8];
        r8 = android.support.constraint.solver.widgets.ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT;
        if (r2 != r8) goto L_0x0118;
    L_0x0115:
        if (r12 != 0) goto L_0x0118;
    L_0x0117:
        r12 = 3;
    L_0x0118:
        r2 = r15.mListDimensionBehaviors;
        r8 = 0;
        r2 = r2[r8];
        r8 = android.support.constraint.solver.widgets.ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT;
        r44 = r0;
        r0 = 3;
        if (r2 != r8) goto L_0x0136;
    L_0x0124:
        r2 = r15.mListDimensionBehaviors;
        r8 = 1;
        r2 = r2[r8];
        r8 = android.support.constraint.solver.widgets.ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT;
        if (r2 != r8) goto L_0x0136;
    L_0x012d:
        if (r11 != r0) goto L_0x0136;
    L_0x012f:
        if (r12 != r0) goto L_0x0136;
    L_0x0131:
        r15.setupDimensionRatio(r9, r7, r5, r6);
        goto L_0x019e;
    L_0x0136:
        r2 = r15.mListDimensionBehaviors;
        r8 = 0;
        r2 = r2[r8];
        r8 = android.support.constraint.solver.widgets.ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT;
        if (r2 != r8) goto L_0x0161;
    L_0x013f:
        if (r11 != r0) goto L_0x0161;
    L_0x0141:
        r0 = 0;
        r15.mResolvedDimensionRatioSide = r0;
        r0 = r15.mResolvedDimensionRatio;
        r2 = r15.mHeight;
        r2 = (float) r2;
        r0 = r0 * r2;
        r0 = (int) r0;
        r2 = r15.mListDimensionBehaviors;
        r3 = 1;
        r2 = r2[r3];
        r3 = android.support.constraint.solver.widgets.ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT;
        if (r2 == r3) goto L_0x015e;
    L_0x0155:
        r2 = 4;
        r14 = 0;
        r47 = r0;
        r41 = r2;
        r48 = r4;
        goto L_0x01a4;
    L_0x015e:
        r47 = r0;
        goto L_0x01a0;
    L_0x0161:
        r2 = r15.mListDimensionBehaviors;
        r8 = 1;
        r2 = r2[r8];
        r8 = android.support.constraint.solver.widgets.ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT;
        if (r2 != r8) goto L_0x019e;
    L_0x016a:
        if (r12 != r0) goto L_0x019e;
    L_0x016c:
        r0 = 1;
        r15.mResolvedDimensionRatioSide = r0;
        r0 = r15.mDimensionRatioSide;
        r2 = -1;
        if (r0 != r2) goto L_0x017b;
    L_0x0174:
        r0 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;
        r2 = r15.mResolvedDimensionRatio;
        r0 = r0 / r2;
        r15.mResolvedDimensionRatio = r0;
    L_0x017b:
        r0 = r15.mResolvedDimensionRatio;
        r2 = r15.mWidth;
        r2 = (float) r2;
        r0 = r0 * r2;
        r0 = (int) r0;
        r2 = r15.mListDimensionBehaviors;
        r4 = 0;
        r2 = r2[r4];
        r4 = android.support.constraint.solver.widgets.ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT;
        if (r2 == r4) goto L_0x0197;
    L_0x018c:
        r2 = 4;
        r14 = 0;
        r48 = r0;
        r45 = r2;
        r47 = r3;
        r41 = r11;
        goto L_0x01a6;
    L_0x0197:
        r48 = r0;
        r47 = r3;
        goto L_0x01a2;
    L_0x019c:
        r44 = r0;
    L_0x019e:
        r47 = r3;
    L_0x01a0:
        r48 = r4;
    L_0x01a2:
        r41 = r11;
    L_0x01a4:
        r45 = r12;
    L_0x01a6:
        r46 = r14;
        r0 = r15.mResolvedMatchConstraintDefault;
        r2 = 0;
        r0[r2] = r41;
        r0 = r15.mResolvedMatchConstraintDefault;
        r2 = 1;
        r0[r2] = r45;
        if (r46 == 0) goto L_0x01c1;
    L_0x01b4:
        r0 = r15.mResolvedDimensionRatioSide;
        if (r0 == 0) goto L_0x01be;
    L_0x01b8:
        r0 = r15.mResolvedDimensionRatioSide;
        r12 = -1;
        if (r0 != r12) goto L_0x01c2;
    L_0x01bd:
        goto L_0x01bf;
    L_0x01be:
        r12 = -1;
    L_0x01bf:
        r14 = 1;
        goto L_0x01c3;
    L_0x01c1:
        r12 = -1;
    L_0x01c2:
        r14 = 0;
    L_0x01c3:
        r0 = r15.mListDimensionBehaviors;
        r3 = 0;
        r0 = r0[r3];
        r3 = android.support.constraint.solver.widgets.ConstraintWidget.DimensionBehaviour.WRAP_CONTENT;
        if (r0 != r3) goto L_0x01d2;
    L_0x01cc:
        r0 = r15 instanceof android.support.constraint.solver.widgets.ConstraintWidgetContainer;
        if (r0 == 0) goto L_0x01d2;
    L_0x01d0:
        r0 = 1;
        goto L_0x01d3;
    L_0x01d2:
        r0 = 0;
    L_0x01d3:
        r39 = r6;
        r6 = r0;
        r0 = 1;
        r3 = r15.mCenter;
        r3 = r3.isConnected();
        if (r3 == 0) goto L_0x01e0;
    L_0x01df:
        r0 = 0;
    L_0x01e0:
        r22 = r0;
        r0 = r15.mHorizontalResolution;
        r11 = 2;
        r23 = 0;
        if (r0 == r11) goto L_0x024f;
    L_0x01e9:
        r0 = r15.mParent;
        if (r0 == 0) goto L_0x01f7;
    L_0x01ed:
        r0 = r15.mParent;
        r0 = r0.mRight;
        r0 = r10.createObjectVariable(r0);
        r4 = r0;
        goto L_0x01f9;
    L_0x01f7:
        r4 = r23;
    L_0x01f9:
        r0 = r15.mParent;
        if (r0 == 0) goto L_0x0207;
    L_0x01fd:
        r0 = r15.mParent;
        r0 = r0.mLeft;
        r0 = r10.createObjectVariable(r0);
        r3 = r0;
        goto L_0x0209;
    L_0x0207:
        r3 = r23;
    L_0x0209:
        r0 = r15.mListDimensionBehaviors;
        r16 = 0;
        r0 = r0[r16];
        r40 = r5;
        r5 = r0;
        r0 = r15.mLeft;
        r43 = r7;
        r7 = r0;
        r8 = r15.mRight;
        r0 = r15.mX;
        r49 = r9;
        r9 = r0;
        r0 = r15.mMinWidth;
        r11 = r0;
        r0 = r15.mMaxDimension;
        r0 = r0[r16];
        r16 = -1;
        r24 = 0;
        r12 = r0;
        r0 = r15.mHorizontalBiasPercent;
        r50 = r13;
        r13 = r0;
        r0 = r15.mMatchConstraintMinWidth;
        r17 = r0;
        r0 = r15.mMatchConstraintMaxWidth;
        r18 = r0;
        r0 = r15.mMatchConstraintPercentWidth;
        r19 = r0;
        r0 = r52;
        r51 = r1;
        r1 = r53;
        r2 = r49;
        r10 = r47;
        r15 = r37;
        r16 = r41;
        r20 = r22;
        r0.applyConstraints(r1, r2, r3, r4, r5, r6, r7, r8, r9, r10, r11, r12, r13, r14, r15, r16, r17, r18, r19, r20);
        goto L_0x025b;
    L_0x024f:
        r51 = r1;
        r40 = r5;
        r43 = r7;
        r49 = r9;
        r50 = r13;
        r24 = 0;
    L_0x025b:
        r7 = r52;
        r0 = r7.mVerticalResolution;
        r1 = 2;
        if (r0 != r1) goto L_0x0263;
    L_0x0262:
        return;
    L_0x0263:
        r0 = r7.mListDimensionBehaviors;
        r1 = 1;
        r0 = r0[r1];
        r2 = android.support.constraint.solver.widgets.ConstraintWidget.DimensionBehaviour.WRAP_CONTENT;
        if (r0 != r2) goto L_0x0273;
    L_0x026c:
        r0 = r7 instanceof android.support.constraint.solver.widgets.ConstraintWidgetContainer;
        if (r0 == 0) goto L_0x0273;
    L_0x0270:
        r21 = 1;
        goto L_0x0275;
    L_0x0273:
        r21 = 0;
    L_0x0275:
        if (r46 == 0) goto L_0x0283;
    L_0x0277:
        r0 = r7.mResolvedDimensionRatioSide;
        if (r0 == r1) goto L_0x0280;
    L_0x027b:
        r0 = r7.mResolvedDimensionRatioSide;
        r2 = -1;
        if (r0 != r2) goto L_0x0283;
    L_0x0280:
        r29 = 1;
        goto L_0x0285;
    L_0x0283:
        r29 = 0;
    L_0x0285:
        r0 = r7.mBaselineDistance;
        if (r0 <= 0) goto L_0x02c6;
    L_0x0289:
        r0 = r7.mBaseline;
        r0 = r0.getResolutionNode();
        r0 = r0.state;
        if (r0 != r1) goto L_0x02a3;
    L_0x0293:
        r0 = r7.mBaseline;
        r0 = r0.getResolutionNode();
        r8 = r53;
        r0.addResolvedValue(r8);
        r10 = r50;
        r9 = r51;
        goto L_0x02cc;
    L_0x02a3:
        r8 = r53;
        r0 = r52.getBaselineDistance();
        r2 = 6;
        r10 = r50;
        r9 = r51;
        r8.addEquality(r10, r9, r0, r2);
        r0 = r7.mBaseline;
        r0 = r0.mTarget;
        if (r0 == 0) goto L_0x02cc;
    L_0x02b7:
        r0 = r7.mBaseline;
        r0 = r0.mTarget;
        r0 = r8.createObjectVariable(r0);
        r3 = 0;
        r8.addEquality(r10, r0, r3, r2);
        r0 = 0;
        r11 = r0;
        goto L_0x02ce;
    L_0x02c6:
        r10 = r50;
        r9 = r51;
        r8 = r53;
    L_0x02cc:
        r11 = r22;
    L_0x02ce:
        r0 = r7.mParent;
        if (r0 == 0) goto L_0x02dd;
    L_0x02d2:
        r0 = r7.mParent;
        r0 = r0.mBottom;
        r0 = r8.createObjectVariable(r0);
        r19 = r0;
        goto L_0x02df;
    L_0x02dd:
        r19 = r23;
    L_0x02df:
        r0 = r7.mParent;
        if (r0 == 0) goto L_0x02ee;
    L_0x02e3:
        r0 = r7.mParent;
        r0 = r0.mTop;
        r0 = r8.createObjectVariable(r0);
        r18 = r0;
        goto L_0x02f0;
    L_0x02ee:
        r18 = r23;
    L_0x02f0:
        r0 = r7.mListDimensionBehaviors;
        r20 = r0[r1];
        r0 = r7.mTop;
        r22 = r0;
        r0 = r7.mBottom;
        r23 = r0;
        r0 = r7.mY;
        r24 = r0;
        r0 = r7.mMinHeight;
        r26 = r0;
        r0 = r7.mMaxDimension;
        r27 = r0[r1];
        r0 = r7.mVerticalBiasPercent;
        r28 = r0;
        r0 = r7.mMatchConstraintMinHeight;
        r32 = r0;
        r0 = r7.mMatchConstraintMaxHeight;
        r33 = r0;
        r0 = r7.mMatchConstraintPercentHeight;
        r34 = r0;
        r15 = r52;
        r16 = r53;
        r17 = r43;
        r25 = r48;
        r30 = r38;
        r31 = r45;
        r35 = r11;
        r15.applyConstraints(r16, r17, r18, r19, r20, r21, r22, r23, r24, r25, r26, r27, r28, r29, r30, r31, r32, r33, r34, r35);
        if (r46 == 0) goto L_0x034f;
    L_0x032b:
        r12 = 6;
        r0 = r7.mResolvedDimensionRatioSide;
        if (r0 != r1) goto L_0x0340;
    L_0x0330:
        r5 = r7.mResolvedDimensionRatio;
        r0 = r53;
        r1 = r44;
        r2 = r9;
        r3 = r42;
        r4 = r36;
        r6 = r12;
        r0.addRatio(r1, r2, r3, r4, r5, r6);
        goto L_0x034f;
    L_0x0340:
        r5 = r7.mResolvedDimensionRatio;
        r0 = r53;
        r1 = r42;
        r2 = r36;
        r3 = r44;
        r4 = r9;
        r6 = r12;
        r0.addRatio(r1, r2, r3, r4, r5, r6);
    L_0x034f:
        r0 = r7.mCenter;
        r0 = r0.isConnected();
        if (r0 == 0) goto L_0x0375;
    L_0x0357:
        r0 = r7.mCenter;
        r0 = r0.getTarget();
        r0 = r0.getOwner();
        r1 = r7.mCircleConstraintAngle;
        r2 = 1119092736; // 0x42b40000 float:90.0 double:5.529052754E-315;
        r1 = r1 + r2;
        r1 = (double) r1;
        r1 = java.lang.Math.toRadians(r1);
        r1 = (float) r1;
        r2 = r7.mCenter;
        r2 = r2.getMargin();
        r8.addCenterPoint(r7, r0, r1, r2);
    L_0x0375:
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.support.constraint.solver.widgets.ConstraintWidget.addToSolver(android.support.constraint.solver.LinearSystem):void");
    }

    public void setupDimensionRatio(boolean hparentWrapContent, boolean vparentWrapContent, boolean horizontalDimensionFixed, boolean verticalDimensionFixed) {
        if (this.mResolvedDimensionRatioSide == -1) {
            if (horizontalDimensionFixed && !verticalDimensionFixed) {
                this.mResolvedDimensionRatioSide = 0;
            } else if (!horizontalDimensionFixed && verticalDimensionFixed) {
                this.mResolvedDimensionRatioSide = 1;
                if (this.mDimensionRatioSide == -1) {
                    this.mResolvedDimensionRatio = 1.0f / this.mResolvedDimensionRatio;
                }
            }
        }
        if (this.mResolvedDimensionRatioSide == 0 && (!this.mTop.isConnected() || !this.mBottom.isConnected())) {
            this.mResolvedDimensionRatioSide = 1;
        } else if (this.mResolvedDimensionRatioSide == 1 && !(this.mLeft.isConnected() && this.mRight.isConnected())) {
            this.mResolvedDimensionRatioSide = 0;
        }
        if (this.mResolvedDimensionRatioSide == -1 && !(this.mTop.isConnected() && this.mBottom.isConnected() && this.mLeft.isConnected() && this.mRight.isConnected())) {
            if (this.mTop.isConnected() && this.mBottom.isConnected()) {
                this.mResolvedDimensionRatioSide = 0;
            } else if (this.mLeft.isConnected() && this.mRight.isConnected()) {
                this.mResolvedDimensionRatio = 1.0f / this.mResolvedDimensionRatio;
                this.mResolvedDimensionRatioSide = 1;
            }
        }
        if (this.mResolvedDimensionRatioSide == -1) {
            if (hparentWrapContent && !vparentWrapContent) {
                this.mResolvedDimensionRatioSide = 0;
            } else if (!hparentWrapContent && vparentWrapContent) {
                this.mResolvedDimensionRatio = 1.0f / this.mResolvedDimensionRatio;
                this.mResolvedDimensionRatioSide = 1;
            }
        }
        if (this.mResolvedDimensionRatioSide == -1) {
            if (this.mMatchConstraintMinWidth > 0 && this.mMatchConstraintMinHeight == 0) {
                this.mResolvedDimensionRatioSide = 0;
            } else if (this.mMatchConstraintMinWidth == 0 && this.mMatchConstraintMinHeight > 0) {
                this.mResolvedDimensionRatio = 1.0f / this.mResolvedDimensionRatio;
                this.mResolvedDimensionRatioSide = 1;
            }
        }
        if (this.mResolvedDimensionRatioSide == -1 && hparentWrapContent && vparentWrapContent) {
            this.mResolvedDimensionRatio = 1.0f / this.mResolvedDimensionRatio;
            this.mResolvedDimensionRatioSide = 1;
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:189:0x03bb  */
    /* JADX WARNING: Removed duplicated region for block: B:188:0x03b4  */
    /* JADX WARNING: Removed duplicated region for block: B:170:0x0365  */
    /* JADX WARNING: Removed duplicated region for block: B:163:0x0326  */
    /* JADX WARNING: Removed duplicated region for block: B:172:0x0371  */
    /* JADX WARNING: Removed duplicated region for block: B:185:0x03a0  */
    /* JADX WARNING: Removed duplicated region for block: B:184:0x039b  */
    /* JADX WARNING: Removed duplicated region for block: B:188:0x03b4  */
    /* JADX WARNING: Removed duplicated region for block: B:189:0x03bb  */
    /* JADX WARNING: Removed duplicated region for block: B:189:0x03bb  */
    /* JADX WARNING: Removed duplicated region for block: B:188:0x03b4  */
    /* JADX WARNING: Removed duplicated region for block: B:188:0x03b4  */
    /* JADX WARNING: Removed duplicated region for block: B:189:0x03bb  */
    /* JADX WARNING: Removed duplicated region for block: B:63:0x011a  */
    /* JADX WARNING: Removed duplicated region for block: B:53:0x00ee  */
    /* JADX WARNING: Removed duplicated region for block: B:191:0x03be  */
    /* JADX WARNING: Removed duplicated region for block: B:104:0x0215  */
    /* JADX WARNING: Removed duplicated region for block: B:53:0x00ee  */
    /* JADX WARNING: Removed duplicated region for block: B:63:0x011a  */
    /* JADX WARNING: Removed duplicated region for block: B:104:0x0215  */
    /* JADX WARNING: Removed duplicated region for block: B:191:0x03be  */
    private void applyConstraints(android.support.constraint.solver.LinearSystem r43, boolean r44, android.support.constraint.solver.SolverVariable r45, android.support.constraint.solver.SolverVariable r46, android.support.constraint.solver.widgets.ConstraintWidget.DimensionBehaviour r47, boolean r48, android.support.constraint.solver.widgets.ConstraintAnchor r49, android.support.constraint.solver.widgets.ConstraintAnchor r50, int r51, int r52, int r53, int r54, float r55, boolean r56, boolean r57, int r58, int r59, int r60, float r61, boolean r62) {
        /*
        r42 = this;
        r0 = r42;
        r10 = r43;
        r11 = r45;
        r12 = r46;
        r13 = r49;
        r14 = r50;
        r9 = r53;
        r8 = r54;
        r7 = r10.createObjectVariable(r13);
        r6 = r10.createObjectVariable(r14);
        r1 = r49.getTarget();
        r5 = r10.createObjectVariable(r1);
        r1 = r50.getTarget();
        r4 = r10.createObjectVariable(r1);
        r1 = r10.graphOptimizer;
        r2 = 1;
        if (r1 == 0) goto L_0x006b;
    L_0x002d:
        r1 = r49.getResolutionNode();
        r1 = r1.state;
        if (r1 != r2) goto L_0x006b;
    L_0x0035:
        r1 = r50.getResolutionNode();
        r1 = r1.state;
        if (r1 != r2) goto L_0x006b;
    L_0x003d:
        r1 = android.support.constraint.solver.LinearSystem.getMetrics();
        if (r1 == 0) goto L_0x0051;
    L_0x0043:
        r1 = android.support.constraint.solver.LinearSystem.getMetrics();
        r18 = r4;
        r3 = r1.resolvedWidgets;
        r15 = 1;
        r3 = r3 + r15;
        r1.resolvedWidgets = r3;
        goto L_0x0053;
    L_0x0051:
        r18 = r4;
    L_0x0053:
        r1 = r49.getResolutionNode();
        r1.addResolvedValue(r10);
        r1 = r50.getResolutionNode();
        r1.addResolvedValue(r10);
        if (r57 != 0) goto L_0x006a;
    L_0x0063:
        if (r44 == 0) goto L_0x006a;
    L_0x0065:
        r1 = 6;
        r2 = 0;
        r10.addGreaterThan(r12, r6, r2, r1);
    L_0x006a:
        return;
    L_0x006b:
        r18 = r4;
        r1 = android.support.constraint.solver.LinearSystem.getMetrics();
        if (r1 == 0) goto L_0x007e;
    L_0x0073:
        r1 = android.support.constraint.solver.LinearSystem.getMetrics();
        r3 = r1.nonresolvedWidgets;
        r15 = 1;
        r3 = r3 + r15;
        r1.nonresolvedWidgets = r3;
    L_0x007e:
        r15 = r49.isConnected();
        r16 = r50.isConnected();
        r1 = r0.mCenter;
        r20 = r1.isConnected();
        r1 = 0;
        r3 = 0;
        if (r15 == 0) goto L_0x0092;
    L_0x0090:
        r3 = r3 + 1;
    L_0x0092:
        if (r16 == 0) goto L_0x0096;
    L_0x0094:
        r3 = r3 + 1;
    L_0x0096:
        if (r20 == 0) goto L_0x009a;
    L_0x0098:
        r3 = r3 + 1;
    L_0x009a:
        r4 = r3;
        if (r56 == 0) goto L_0x009f;
    L_0x009d:
        r3 = 3;
        goto L_0x00a1;
    L_0x009f:
        r3 = r58;
    L_0x00a1:
        r21 = android.support.constraint.solver.widgets.ConstraintWidget.AnonymousClass1.$SwitchMap$android$support$constraint$solver$widgets$ConstraintWidget$DimensionBehaviour;
        r22 = r47.ordinal();
        r21 = r21[r22];
        r2 = 4;
        switch(r21) {
            case 1: goto L_0x00b7;
            case 2: goto L_0x00b5;
            case 3: goto L_0x00b3;
            case 4: goto L_0x00ae;
            default: goto L_0x00ad;
        };
    L_0x00ad:
        goto L_0x00b9;
    L_0x00ae:
        r1 = 1;
        if (r3 != r2) goto L_0x00b9;
    L_0x00b1:
        r1 = 0;
        goto L_0x00b9;
    L_0x00b3:
        r1 = 0;
        goto L_0x00b9;
    L_0x00b5:
        r1 = 0;
        goto L_0x00b9;
    L_0x00b7:
        r1 = 0;
    L_0x00b9:
        r2 = r0.mVisibility;
        r25 = r1;
        r1 = 8;
        if (r2 != r1) goto L_0x00c6;
    L_0x00c1:
        r1 = 0;
        r2 = 0;
        r25 = r2;
        goto L_0x00c8;
    L_0x00c6:
        r1 = r52;
    L_0x00c8:
        if (r62 == 0) goto L_0x00e9;
    L_0x00ca:
        if (r15 != 0) goto L_0x00d8;
    L_0x00cc:
        if (r16 != 0) goto L_0x00d8;
    L_0x00ce:
        if (r20 != 0) goto L_0x00d8;
    L_0x00d0:
        r2 = r51;
        r10.addEquality(r7, r2);
        r26 = r4;
        goto L_0x00eb;
    L_0x00d8:
        r2 = r51;
        if (r15 == 0) goto L_0x00e9;
    L_0x00dc:
        if (r16 != 0) goto L_0x00e9;
    L_0x00de:
        r2 = r49.getMargin();
        r26 = r4;
        r4 = 6;
        r10.addEquality(r7, r5, r2, r4);
        goto L_0x00eb;
    L_0x00e9:
        r26 = r4;
    L_0x00eb:
        r4 = 3;
        if (r25 != 0) goto L_0x011a;
    L_0x00ee:
        if (r48 == 0) goto L_0x0105;
    L_0x00f0:
        r2 = 0;
        r10.addEquality(r6, r7, r2, r4);
        if (r9 <= 0) goto L_0x00fb;
    L_0x00f6:
        r2 = 6;
        r10.addGreaterThan(r6, r7, r9, r2);
        goto L_0x00fc;
    L_0x00fb:
        r2 = 6;
    L_0x00fc:
        r4 = 2147483647; // 0x7fffffff float:NaN double:1.060997895E-314;
        if (r8 >= r4) goto L_0x0109;
    L_0x0101:
        r10.addLowerThan(r6, r7, r8, r2);
        goto L_0x0109;
    L_0x0105:
        r2 = 6;
        r10.addEquality(r6, r7, r1, r2);
    L_0x0109:
        r17 = r59;
        r0 = r1;
        r33 = r3;
        r36 = r5;
        r13 = r6;
        r34 = r18;
        r14 = r26;
        r6 = 6;
        r18 = r60;
        goto L_0x0213;
    L_0x011a:
        r2 = -2;
        r4 = r59;
        if (r4 != r2) goto L_0x0120;
    L_0x011f:
        r4 = r1;
    L_0x0120:
        r29 = r5;
        r5 = r60;
        if (r5 != r2) goto L_0x0128;
    L_0x0126:
        r2 = r1;
        r5 = r2;
    L_0x0128:
        if (r4 <= 0) goto L_0x0133;
    L_0x012a:
        r2 = 6;
        r10.addGreaterThan(r6, r7, r4, r2);
        r1 = java.lang.Math.max(r1, r4);
        goto L_0x0134;
    L_0x0133:
        r2 = 6;
    L_0x0134:
        if (r5 <= 0) goto L_0x013d;
    L_0x0136:
        r10.addLowerThan(r6, r7, r5, r2);
        r1 = java.lang.Math.min(r1, r5);
    L_0x013d:
        r2 = 1;
        if (r3 != r2) goto L_0x0162;
    L_0x0140:
        if (r44 == 0) goto L_0x0155;
    L_0x0142:
        r2 = 6;
        r10.addEquality(r6, r7, r1, r2);
    L_0x0146:
        r8 = r1;
        r33 = r3;
        r35 = r4;
        r0 = r5;
        r13 = r6;
        r34 = r18;
        r14 = r26;
        r36 = r29;
        goto L_0x01f1;
    L_0x0155:
        r2 = 6;
        if (r57 == 0) goto L_0x015d;
    L_0x0158:
        r2 = 4;
        r10.addEquality(r6, r7, r1, r2);
        goto L_0x0146;
    L_0x015d:
        r2 = 1;
        r10.addEquality(r6, r7, r1, r2);
        goto L_0x0146;
    L_0x0162:
        r2 = 2;
        if (r3 != r2) goto L_0x01e4;
    L_0x0165:
        r17 = 0;
        r19 = 0;
        r2 = r49.getType();
        r30 = r1;
        r1 = android.support.constraint.solver.widgets.ConstraintAnchor.Type.TOP;
        if (r2 == r1) goto L_0x0199;
    L_0x0173:
        r1 = r49.getType();
        r2 = android.support.constraint.solver.widgets.ConstraintAnchor.Type.BOTTOM;
        if (r1 != r2) goto L_0x017c;
    L_0x017b:
        goto L_0x0199;
    L_0x017c:
        r1 = r0.mParent;
        r2 = android.support.constraint.solver.widgets.ConstraintAnchor.Type.LEFT;
        r1 = r1.getAnchor(r2);
        r1 = r10.createObjectVariable(r1);
        r2 = r0.mParent;
        r31 = r1;
        r1 = android.support.constraint.solver.widgets.ConstraintAnchor.Type.RIGHT;
        r1 = r2.getAnchor(r1);
        r1 = r10.createObjectVariable(r1);
        r17 = r1;
        goto L_0x01b7;
    L_0x0199:
        r1 = r0.mParent;
        r2 = android.support.constraint.solver.widgets.ConstraintAnchor.Type.TOP;
        r1 = r1.getAnchor(r2);
        r1 = r10.createObjectVariable(r1);
        r2 = r0.mParent;
        r32 = r1;
        r1 = android.support.constraint.solver.widgets.ConstraintAnchor.Type.BOTTOM;
        r1 = r2.getAnchor(r1);
        r1 = r10.createObjectVariable(r1);
        r17 = r1;
        r31 = r32;
    L_0x01b7:
        r1 = r43.createRow();
        r2 = r30;
        r8 = r2;
        r14 = 2;
        r19 = 6;
        r22 = 0;
        r2 = r6;
        r33 = r3;
        r3 = r7;
        r35 = r4;
        r34 = r18;
        r14 = r26;
        r13 = 3;
        r4 = r17;
        r0 = r5;
        r13 = r29;
        r5 = r31;
        r36 = r13;
        r13 = r6;
        r6 = r61;
        r1 = r1.createRowDimensionRatio(r2, r3, r4, r5, r6);
        r10.addConstraint(r1);
        r25 = 0;
        goto L_0x01f1;
    L_0x01e4:
        r8 = r1;
        r33 = r3;
        r35 = r4;
        r0 = r5;
        r13 = r6;
        r34 = r18;
        r14 = r26;
        r36 = r29;
    L_0x01f1:
        if (r25 == 0) goto L_0x020b;
    L_0x01f3:
        r1 = 2;
        if (r14 == r1) goto L_0x020b;
    L_0x01f6:
        if (r56 != 0) goto L_0x020b;
    L_0x01f8:
        r25 = 0;
        r4 = r35;
        r1 = java.lang.Math.max(r4, r8);
        if (r0 <= 0) goto L_0x0206;
    L_0x0202:
        r1 = java.lang.Math.min(r0, r1);
    L_0x0206:
        r6 = 6;
        r10.addEquality(r13, r7, r1, r6);
        goto L_0x020e;
    L_0x020b:
        r4 = r35;
        r6 = 6;
    L_0x020e:
        r18 = r0;
        r17 = r4;
        r0 = r8;
    L_0x0213:
        if (r62 == 0) goto L_0x03be;
    L_0x0215:
        if (r57 == 0) goto L_0x022b;
    L_0x0217:
        r37 = r0;
        r2 = r12;
        r38 = r14;
        r28 = r33;
        r6 = r34;
        r0 = r36;
        r1 = r49;
        r3 = r50;
        r4 = 0;
        r5 = 6;
        r14 = r7;
        goto L_0x03d0;
    L_0x022b:
        r1 = 5;
        if (r15 != 0) goto L_0x024b;
    L_0x022e:
        if (r16 != 0) goto L_0x024b;
    L_0x0230:
        if (r20 != 0) goto L_0x024b;
    L_0x0232:
        if (r44 == 0) goto L_0x0238;
    L_0x0234:
        r8 = 0;
        r10.addGreaterThan(r12, r13, r8, r1);
    L_0x0238:
        r37 = r0;
        r38 = r14;
        r28 = r33;
        r6 = r34;
    L_0x0240:
        r0 = r36;
        r1 = r49;
        r3 = r50;
        r5 = 6;
        r12 = 0;
        r14 = r7;
        goto L_0x03b2;
    L_0x024b:
        r8 = 0;
        if (r15 == 0) goto L_0x0256;
    L_0x024e:
        if (r16 != 0) goto L_0x0256;
    L_0x0250:
        if (r44 == 0) goto L_0x0238;
    L_0x0252:
        r10.addGreaterThan(r12, r13, r8, r1);
        goto L_0x0238;
    L_0x0256:
        if (r15 != 0) goto L_0x0271;
    L_0x0258:
        if (r16 == 0) goto L_0x0271;
    L_0x025a:
        r2 = r50.getMargin();
        r2 = -r2;
        r5 = r34;
        r10.addEquality(r13, r5, r2, r6);
        if (r44 == 0) goto L_0x0269;
    L_0x0266:
        r10.addGreaterThan(r7, r11, r8, r1);
    L_0x0269:
        r37 = r0;
        r6 = r5;
        r38 = r14;
        r28 = r33;
        goto L_0x0240;
    L_0x0271:
        r5 = r34;
        if (r15 == 0) goto L_0x03a2;
    L_0x0275:
        if (r16 == 0) goto L_0x03a2;
    L_0x0277:
        r1 = 0;
        r2 = 0;
        r3 = 5;
        if (r25 == 0) goto L_0x030b;
    L_0x027c:
        if (r44 == 0) goto L_0x0283;
    L_0x027e:
        if (r9 != 0) goto L_0x0283;
    L_0x0280:
        r10.addGreaterThan(r13, r7, r8, r6);
    L_0x0283:
        r4 = r33;
        if (r4 != 0) goto L_0x02be;
    L_0x0287:
        r19 = 6;
        if (r18 > 0) goto L_0x0295;
    L_0x028b:
        if (r17 <= 0) goto L_0x028e;
    L_0x028d:
        goto L_0x0295;
    L_0x028e:
        r41 = r19;
        r19 = r1;
        r1 = r41;
        goto L_0x0299;
    L_0x0295:
        r19 = 4;
        r1 = 1;
        goto L_0x028e;
    L_0x0299:
        r6 = r49.getMargin();
        r37 = r0;
        r0 = r36;
        r10.addEquality(r7, r0, r6, r1);
        r6 = r50.getMargin();
        r6 = -r6;
        r10.addEquality(r13, r5, r6, r1);
        if (r18 > 0) goto L_0x02b0;
    L_0x02ae:
        if (r17 <= 0) goto L_0x02b2;
    L_0x02b0:
        r1 = 1;
        r2 = r1;
    L_0x02b2:
        r22 = r3;
        r38 = r14;
        r39 = r19;
        r14 = r42;
        r19 = r2;
        goto L_0x031c;
    L_0x02be:
        r37 = r0;
        r0 = r36;
        r6 = 1;
        if (r4 != r6) goto L_0x02d3;
    L_0x02c5:
        r2 = 1;
        r1 = 1;
        r3 = 6;
        r39 = r1;
        r19 = r2;
        r22 = r3;
        r38 = r14;
        r14 = r42;
        goto L_0x031c;
    L_0x02d3:
        r6 = 3;
        if (r4 != r6) goto L_0x0300;
    L_0x02d6:
        r2 = 1;
        r1 = 1;
        r6 = 4;
        if (r56 != 0) goto L_0x02ea;
    L_0x02db:
        r38 = r14;
        r14 = r42;
        r8 = r14.mResolvedDimensionRatioSide;
        r39 = r1;
        r1 = -1;
        if (r8 == r1) goto L_0x02f0;
    L_0x02e6:
        if (r18 > 0) goto L_0x02f0;
    L_0x02e8:
        r6 = 6;
        goto L_0x02f0;
    L_0x02ea:
        r39 = r1;
        r38 = r14;
        r14 = r42;
    L_0x02f0:
        r1 = r49.getMargin();
        r10.addEquality(r7, r0, r1, r6);
        r1 = r50.getMargin();
        r1 = -r1;
        r10.addEquality(r13, r5, r1, r6);
        goto L_0x0318;
    L_0x0300:
        r38 = r14;
        r14 = r42;
        r39 = r1;
        r19 = r2;
        r22 = r3;
        goto L_0x031c;
    L_0x030b:
        r37 = r0;
        r38 = r14;
        r4 = r33;
        r0 = r36;
        r14 = r42;
        r2 = 1;
        r39 = r1;
    L_0x0318:
        r19 = r2;
        r22 = r3;
    L_0x031c:
        r23 = 5;
        r24 = 5;
        r26 = r44;
        r27 = r44;
        if (r19 == 0) goto L_0x0365;
    L_0x0326:
        r6 = r49.getMargin();
        r8 = r50.getMargin();
        r1 = r43;
        r2 = r7;
        r3 = r0;
        r28 = r4;
        r4 = r6;
        r6 = r5;
        r5 = r55;
        r40 = r6;
        r14 = 6;
        r14 = r7;
        r7 = r13;
        r12 = 0;
        r9 = r22;
        r1.addCentering(r2, r3, r4, r5, r6, r7, r8, r9);
        r1 = r49;
        r2 = r1.mTarget;
        r2 = r2.mOwner;
        r2 = r2 instanceof android.support.constraint.solver.widgets.Barrier;
        r3 = r50;
        r4 = r3.mTarget;
        r4 = r4.mOwner;
        r4 = r4 instanceof android.support.constraint.solver.widgets.Barrier;
        if (r2 == 0) goto L_0x035c;
    L_0x0355:
        if (r4 != 0) goto L_0x035c;
    L_0x0357:
        r24 = 6;
        r27 = 1;
        goto L_0x036f;
    L_0x035c:
        if (r2 != 0) goto L_0x036f;
    L_0x035e:
        if (r4 == 0) goto L_0x036f;
    L_0x0360:
        r23 = 6;
        r26 = 1;
        goto L_0x036f;
    L_0x0365:
        r28 = r4;
        r40 = r5;
        r14 = r7;
        r1 = r49;
        r3 = r50;
        r12 = 0;
    L_0x036f:
        if (r39 == 0) goto L_0x0375;
    L_0x0371:
        r23 = 6;
        r24 = 6;
    L_0x0375:
        r2 = r23;
        r4 = r24;
        if (r25 != 0) goto L_0x037d;
    L_0x037b:
        if (r26 != 0) goto L_0x037f;
    L_0x037d:
        if (r39 == 0) goto L_0x0386;
    L_0x037f:
        r5 = r49.getMargin();
        r10.addGreaterThan(r14, r0, r5, r2);
    L_0x0386:
        if (r25 != 0) goto L_0x038a;
    L_0x0388:
        if (r27 != 0) goto L_0x038c;
    L_0x038a:
        if (r39 == 0) goto L_0x0397;
    L_0x038c:
        r5 = r50.getMargin();
        r5 = -r5;
        r6 = r40;
        r10.addLowerThan(r13, r6, r5, r4);
        goto L_0x0399;
    L_0x0397:
        r6 = r40;
    L_0x0399:
        if (r44 == 0) goto L_0x03a0;
    L_0x039b:
        r5 = 6;
        r10.addGreaterThan(r14, r11, r12, r5);
        goto L_0x03b2;
    L_0x03a0:
        r5 = 6;
        goto L_0x03b2;
    L_0x03a2:
        r37 = r0;
        r6 = r5;
        r38 = r14;
        r28 = r33;
        r0 = r36;
        r1 = r49;
        r3 = r50;
        r5 = 6;
        r12 = 0;
        r14 = r7;
    L_0x03b2:
        if (r44 == 0) goto L_0x03bb;
    L_0x03b4:
        r2 = r46;
        r4 = 0;
        r10.addGreaterThan(r2, r13, r4, r5);
        goto L_0x03bd;
    L_0x03bb:
        r2 = r46;
    L_0x03bd:
        return;
    L_0x03be:
        r37 = r0;
        r2 = r12;
        r38 = r14;
        r28 = r33;
        r6 = r34;
        r0 = r36;
        r1 = r49;
        r3 = r50;
        r4 = 0;
        r5 = 6;
        r14 = r7;
    L_0x03d0:
        r7 = r38;
        r8 = 2;
        if (r7 >= r8) goto L_0x03dd;
    L_0x03d5:
        if (r44 == 0) goto L_0x03dd;
    L_0x03d7:
        r10.addGreaterThan(r14, r11, r4, r5);
        r10.addGreaterThan(r2, r13, r4, r5);
    L_0x03dd:
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.support.constraint.solver.widgets.ConstraintWidget.applyConstraints(android.support.constraint.solver.LinearSystem, boolean, android.support.constraint.solver.SolverVariable, android.support.constraint.solver.SolverVariable, android.support.constraint.solver.widgets.ConstraintWidget$DimensionBehaviour, boolean, android.support.constraint.solver.widgets.ConstraintAnchor, android.support.constraint.solver.widgets.ConstraintAnchor, int, int, int, int, float, boolean, boolean, int, int, int, float, boolean):void");
    }

    public void updateFromSolver(LinearSystem system) {
        int left = system.getObjectVariableValue(this.mLeft);
        int top = system.getObjectVariableValue(this.mTop);
        int right = system.getObjectVariableValue(this.mRight);
        int bottom = system.getObjectVariableValue(this.mBottom);
        int h = bottom - top;
        if (right - left < 0 || h < 0 || left == Integer.MIN_VALUE || left == ActivityChooserViewAdapter.MAX_ACTIVITY_COUNT_UNLIMITED || top == Integer.MIN_VALUE || top == ActivityChooserViewAdapter.MAX_ACTIVITY_COUNT_UNLIMITED || right == Integer.MIN_VALUE || right == ActivityChooserViewAdapter.MAX_ACTIVITY_COUNT_UNLIMITED || bottom == Integer.MIN_VALUE || bottom == ActivityChooserViewAdapter.MAX_ACTIVITY_COUNT_UNLIMITED) {
            left = 0;
            top = 0;
            right = 0;
            bottom = 0;
        }
        setFrame(left, top, right, bottom);
    }
}
