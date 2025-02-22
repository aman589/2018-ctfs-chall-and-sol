package android.support.v7.widget;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.annotation.RestrictTo;
import android.support.annotation.RestrictTo.Scope;
import android.support.annotation.StyleRes;
import android.support.v7.view.menu.ActionMenuItemView;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.view.menu.MenuBuilder.ItemInvoker;
import android.support.v7.view.menu.MenuItemImpl;
import android.support.v7.view.menu.MenuPresenter.Callback;
import android.support.v7.view.menu.MenuView;
import android.util.AttributeSet;
import android.view.ContextThemeWrapper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewDebug.ExportedProperty;
import android.view.accessibility.AccessibilityEvent;

public class ActionMenuView extends LinearLayoutCompat implements ItemInvoker, MenuView {
    static final int GENERATED_ITEM_PADDING = 4;
    static final int MIN_CELL_SIZE = 56;
    private static final String TAG = "ActionMenuView";
    private Callback mActionMenuPresenterCallback;
    private boolean mFormatItems;
    private int mFormatItemsWidth;
    private int mGeneratedItemPadding;
    private MenuBuilder mMenu;
    MenuBuilder.Callback mMenuBuilderCallback;
    private int mMinCellSize;
    OnMenuItemClickListener mOnMenuItemClickListener;
    private Context mPopupContext;
    private int mPopupTheme;
    private ActionMenuPresenter mPresenter;
    private boolean mReserveOverflow;

    @RestrictTo({Scope.LIBRARY_GROUP})
    public interface ActionMenuChildView {
        boolean needsDividerAfter();

        boolean needsDividerBefore();
    }

    public interface OnMenuItemClickListener {
        boolean onMenuItemClick(MenuItem menuItem);
    }

    private static class ActionMenuPresenterCallback implements Callback {
        ActionMenuPresenterCallback() {
        }

        public void onCloseMenu(MenuBuilder menu, boolean allMenusAreClosing) {
        }

        public boolean onOpenSubMenu(MenuBuilder subMenu) {
            return false;
        }
    }

    public static class LayoutParams extends android.support.v7.widget.LinearLayoutCompat.LayoutParams {
        @ExportedProperty
        public int cellsUsed;
        @ExportedProperty
        public boolean expandable;
        boolean expanded;
        @ExportedProperty
        public int extraPixels;
        @ExportedProperty
        public boolean isOverflowButton;
        @ExportedProperty
        public boolean preventEdgeOffset;

        public LayoutParams(Context c, AttributeSet attrs) {
            super(c, attrs);
        }

        public LayoutParams(android.view.ViewGroup.LayoutParams other) {
            super(other);
        }

        public LayoutParams(LayoutParams other) {
            super((android.view.ViewGroup.LayoutParams) other);
            this.isOverflowButton = other.isOverflowButton;
        }

        public LayoutParams(int width, int height) {
            super(width, height);
            this.isOverflowButton = false;
        }

        LayoutParams(int width, int height, boolean isOverflowButton) {
            super(width, height);
            this.isOverflowButton = isOverflowButton;
        }
    }

    private class MenuBuilderCallback implements MenuBuilder.Callback {
        MenuBuilderCallback() {
        }

        public boolean onMenuItemSelected(MenuBuilder menu, MenuItem item) {
            return ActionMenuView.this.mOnMenuItemClickListener != null && ActionMenuView.this.mOnMenuItemClickListener.onMenuItemClick(item);
        }

        public void onMenuModeChange(MenuBuilder menu) {
            if (ActionMenuView.this.mMenuBuilderCallback != null) {
                ActionMenuView.this.mMenuBuilderCallback.onMenuModeChange(menu);
            }
        }
    }

    public ActionMenuView(Context context) {
        this(context, null);
    }

    public ActionMenuView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setBaselineAligned(false);
        float density = context.getResources().getDisplayMetrics().density;
        this.mMinCellSize = (int) (56.0f * density);
        this.mGeneratedItemPadding = (int) (4.0f * density);
        this.mPopupContext = context;
        this.mPopupTheme = 0;
    }

    public void setPopupTheme(@StyleRes int resId) {
        if (this.mPopupTheme != resId) {
            this.mPopupTheme = resId;
            if (resId == 0) {
                this.mPopupContext = getContext();
            } else {
                this.mPopupContext = new ContextThemeWrapper(getContext(), resId);
            }
        }
    }

    public int getPopupTheme() {
        return this.mPopupTheme;
    }

    @RestrictTo({Scope.LIBRARY_GROUP})
    public void setPresenter(ActionMenuPresenter presenter) {
        this.mPresenter = presenter;
        this.mPresenter.setMenuView(this);
    }

    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (this.mPresenter != null) {
            this.mPresenter.updateMenuView(false);
            if (this.mPresenter.isOverflowMenuShowing()) {
                this.mPresenter.hideOverflowMenu();
                this.mPresenter.showOverflowMenu();
            }
        }
    }

    public void setOnMenuItemClickListener(OnMenuItemClickListener listener) {
        this.mOnMenuItemClickListener = listener;
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        boolean wasFormatted = this.mFormatItems;
        this.mFormatItems = MeasureSpec.getMode(widthMeasureSpec) == 1073741824;
        if (wasFormatted != this.mFormatItems) {
            this.mFormatItemsWidth = 0;
        }
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        if (!(!this.mFormatItems || this.mMenu == null || widthSize == this.mFormatItemsWidth)) {
            this.mFormatItemsWidth = widthSize;
            this.mMenu.onItemsChanged(true);
        }
        int childCount = getChildCount();
        if (!this.mFormatItems || childCount <= 0) {
            for (int i = 0; i < childCount; i++) {
                LayoutParams lp = (LayoutParams) getChildAt(i).getLayoutParams();
                lp.rightMargin = 0;
                lp.leftMargin = 0;
            }
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            return;
        }
        onMeasureExactFormat(widthMeasureSpec, heightMeasureSpec);
    }

    /* JADX WARNING: Removed duplicated region for block: B:139:0x02ae  */
    /* JADX WARNING: Removed duplicated region for block: B:148:0x02d8  */
    /* JADX WARNING: Removed duplicated region for block: B:147:0x02d6  */
    private void onMeasureExactFormat(int r42, int r43) {
        /*
        r41 = this;
        r0 = r41;
        r1 = android.view.View.MeasureSpec.getMode(r43);
        r2 = android.view.View.MeasureSpec.getSize(r42);
        r3 = android.view.View.MeasureSpec.getSize(r43);
        r4 = r41.getPaddingLeft();
        r5 = r41.getPaddingRight();
        r4 = r4 + r5;
        r5 = r41.getPaddingTop();
        r6 = r41.getPaddingBottom();
        r5 = r5 + r6;
        r6 = -2;
        r7 = r43;
        r6 = getChildMeasureSpec(r7, r5, r6);
        r2 = r2 - r4;
        r8 = r0.mMinCellSize;
        r8 = r2 / r8;
        r9 = r0.mMinCellSize;
        r9 = r2 % r9;
        r10 = 0;
        if (r8 != 0) goto L_0x0037;
    L_0x0033:
        r0.setMeasuredDimension(r2, r10);
        return;
    L_0x0037:
        r11 = r0.mMinCellSize;
        r12 = r9 / r8;
        r11 = r11 + r12;
        r12 = r8;
        r13 = 0;
        r14 = 0;
        r15 = 0;
        r16 = 0;
        r17 = 0;
        r18 = 0;
        r10 = r41.getChildCount();
        r21 = r3;
        r3 = r13;
        r13 = r16;
        r16 = r15;
        r15 = r14;
        r14 = r12;
        r12 = 0;
    L_0x0054:
        r22 = r4;
        if (r12 >= r10) goto L_0x00f0;
    L_0x0058:
        r4 = r0.getChildAt(r12);
        r7 = r4.getVisibility();
        r23 = r8;
        r8 = 8;
        if (r7 != r8) goto L_0x006c;
    L_0x0066:
        r26 = r5;
        r24 = r9;
        goto L_0x00e2;
    L_0x006c:
        r7 = r4 instanceof android.support.v7.view.menu.ActionMenuItemView;
        r13 = r13 + 1;
        if (r7 == 0) goto L_0x007f;
    L_0x0072:
        r8 = r0.mGeneratedItemPadding;
        r24 = r9;
        r9 = r0.mGeneratedItemPadding;
        r25 = r13;
        r13 = 0;
        r4.setPadding(r8, r13, r9, r13);
        goto L_0x0084;
    L_0x007f:
        r24 = r9;
        r25 = r13;
        r13 = 0;
    L_0x0084:
        r8 = r4.getLayoutParams();
        r8 = (android.support.v7.widget.ActionMenuView.LayoutParams) r8;
        r8.expanded = r13;
        r8.extraPixels = r13;
        r8.cellsUsed = r13;
        r8.expandable = r13;
        r8.leftMargin = r13;
        r8.rightMargin = r13;
        if (r7 == 0) goto L_0x00a3;
    L_0x0098:
        r9 = r4;
        r9 = (android.support.v7.view.menu.ActionMenuItemView) r9;
        r9 = r9.hasText();
        if (r9 == 0) goto L_0x00a3;
    L_0x00a1:
        r9 = 1;
        goto L_0x00a4;
    L_0x00a3:
        r9 = 0;
    L_0x00a4:
        r8.preventEdgeOffset = r9;
        r9 = r8.isOverflowButton;
        if (r9 == 0) goto L_0x00ac;
    L_0x00aa:
        r9 = 1;
        goto L_0x00ad;
    L_0x00ac:
        r9 = r14;
    L_0x00ad:
        r13 = measureChildForCells(r4, r11, r9, r6, r5);
        r15 = java.lang.Math.max(r15, r13);
        r26 = r5;
        r5 = r8.expandable;
        if (r5 == 0) goto L_0x00bd;
    L_0x00bb:
        r16 = r16 + 1;
    L_0x00bd:
        r5 = r8.isOverflowButton;
        if (r5 == 0) goto L_0x00c3;
    L_0x00c1:
        r17 = 1;
    L_0x00c3:
        r14 = r14 - r13;
        r5 = r4.getMeasuredHeight();
        r3 = java.lang.Math.max(r3, r5);
        r5 = 1;
        if (r13 != r5) goto L_0x00de;
    L_0x00cf:
        r5 = r5 << r12;
        r28 = r3;
        r27 = r4;
        r3 = (long) r5;
        r3 = r18 | r3;
        r18 = r3;
        r13 = r25;
        r3 = r28;
        goto L_0x00e2;
    L_0x00de:
        r28 = r3;
        r13 = r25;
    L_0x00e2:
        r12 = r12 + 1;
        r4 = r22;
        r8 = r23;
        r9 = r24;
        r5 = r26;
        r7 = r43;
        goto L_0x0054;
    L_0x00f0:
        r26 = r5;
        r23 = r8;
        r24 = r9;
        r4 = 2;
        if (r17 == 0) goto L_0x00fd;
    L_0x00f9:
        if (r13 != r4) goto L_0x00fd;
    L_0x00fb:
        r5 = 1;
        goto L_0x00fe;
    L_0x00fd:
        r5 = 0;
    L_0x00fe:
        r7 = 0;
    L_0x00ff:
        if (r16 <= 0) goto L_0x01c7;
    L_0x0101:
        if (r14 <= 0) goto L_0x01c7;
    L_0x0103:
        r12 = 2147483647; // 0x7fffffff float:NaN double:1.060997895E-314;
        r27 = 0;
        r25 = 0;
        r8 = r12;
        r4 = r25;
        r12 = 0;
    L_0x010e:
        r9 = r12;
        if (r9 >= r10) goto L_0x0153;
    L_0x0111:
        r12 = r0.getChildAt(r9);
        r25 = r12.getLayoutParams();
        r31 = r7;
        r7 = r25;
        r7 = (android.support.v7.widget.ActionMenuView.LayoutParams) r7;
        r32 = r12;
        r12 = r7.expandable;
        if (r12 != 0) goto L_0x0128;
    L_0x0125:
        r34 = r13;
        goto L_0x014c;
    L_0x0128:
        r12 = r7.cellsUsed;
        if (r12 >= r8) goto L_0x013c;
    L_0x012c:
        r8 = r7.cellsUsed;
        r33 = r8;
        r12 = 1;
        r8 = r12 << r9;
        r34 = r13;
        r12 = (long) r8;
        r4 = 1;
        r27 = r12;
        r8 = r33;
        goto L_0x014c;
    L_0x013c:
        r34 = r13;
        r12 = r7.cellsUsed;
        if (r12 != r8) goto L_0x014c;
    L_0x0142:
        r12 = 1;
        r13 = r12 << r9;
        r12 = (long) r13;
        r12 = r27 | r12;
        r4 = r4 + 1;
        r27 = r12;
    L_0x014c:
        r12 = r9 + 1;
        r7 = r31;
        r13 = r34;
        goto L_0x010e;
    L_0x0153:
        r31 = r7;
        r34 = r13;
        r18 = r18 | r27;
        if (r4 <= r14) goto L_0x015f;
    L_0x015b:
        r37 = r5;
        goto L_0x01cd;
    L_0x015f:
        r8 = r8 + 1;
        r7 = 0;
    L_0x0162:
        if (r7 >= r10) goto L_0x01bc;
    L_0x0164:
        r9 = r0.getChildAt(r7);
        r12 = r9.getLayoutParams();
        r12 = (android.support.v7.widget.ActionMenuView.LayoutParams) r12;
        r35 = r4;
        r13 = 1;
        r4 = r13 << r7;
        r36 = r14;
        r13 = (long) r4;
        r13 = r27 & r13;
        r29 = 0;
        r4 = (r13 > r29 ? 1 : (r13 == r29 ? 0 : -1));
        if (r4 != 0) goto L_0x018d;
    L_0x017e:
        r4 = r12.cellsUsed;
        if (r4 != r8) goto L_0x0188;
    L_0x0182:
        r4 = 1;
        r13 = r4 << r7;
        r13 = (long) r13;
        r18 = r18 | r13;
    L_0x0188:
        r37 = r5;
        r14 = r36;
        goto L_0x01b5;
    L_0x018d:
        r4 = 1;
        if (r5 == 0) goto L_0x01a7;
    L_0x0190:
        r13 = r12.preventEdgeOffset;
        if (r13 == 0) goto L_0x01a7;
    L_0x0194:
        r14 = r36;
        if (r14 != r4) goto L_0x01a4;
    L_0x0198:
        r4 = r0.mGeneratedItemPadding;
        r4 = r4 + r11;
        r13 = r0.mGeneratedItemPadding;
        r37 = r5;
        r5 = 0;
        r9.setPadding(r4, r5, r13, r5);
        goto L_0x01ab;
    L_0x01a4:
        r37 = r5;
        goto L_0x01ab;
    L_0x01a7:
        r37 = r5;
        r14 = r36;
    L_0x01ab:
        r4 = r12.cellsUsed;
        r5 = 1;
        r4 = r4 + r5;
        r12.cellsUsed = r4;
        r12.expanded = r5;
        r14 = r14 + -1;
    L_0x01b5:
        r7 = r7 + 1;
        r4 = r35;
        r5 = r37;
        goto L_0x0162;
    L_0x01bc:
        r35 = r4;
        r37 = r5;
        r7 = 1;
        r13 = r34;
        r4 = 2;
        goto L_0x00ff;
    L_0x01c7:
        r37 = r5;
        r31 = r7;
        r34 = r13;
    L_0x01cd:
        if (r17 != 0) goto L_0x01d6;
    L_0x01cf:
        r13 = r34;
        r4 = 1;
        if (r13 != r4) goto L_0x01d8;
    L_0x01d4:
        r4 = 1;
        goto L_0x01d9;
    L_0x01d6:
        r13 = r34;
    L_0x01d8:
        r4 = 0;
    L_0x01d9:
        if (r14 <= 0) goto L_0x02a8;
    L_0x01db:
        r7 = 0;
        r5 = (r18 > r7 ? 1 : (r18 == r7 ? 0 : -1));
        if (r5 == 0) goto L_0x02a8;
    L_0x01e1:
        r5 = r13 + -1;
        if (r14 < r5) goto L_0x01ef;
    L_0x01e5:
        if (r4 != 0) goto L_0x01ef;
    L_0x01e7:
        r5 = 1;
        if (r15 <= r5) goto L_0x01eb;
    L_0x01ea:
        goto L_0x01ef;
    L_0x01eb:
        r39 = r4;
        goto L_0x02aa;
    L_0x01ef:
        r5 = java.lang.Long.bitCount(r18);
        r5 = (float) r5;
        if (r4 != 0) goto L_0x0233;
    L_0x01f6:
        r7 = 1;
        r7 = r18 & r7;
        r9 = 1056964608; // 0x3f000000 float:0.5 double:5.222099017E-315;
        r27 = 0;
        r7 = (r7 > r27 ? 1 : (r7 == r27 ? 0 : -1));
        if (r7 == 0) goto L_0x0213;
    L_0x0202:
        r7 = 0;
        r8 = r0.getChildAt(r7);
        r8 = r8.getLayoutParams();
        r8 = (android.support.v7.widget.ActionMenuView.LayoutParams) r8;
        r12 = r8.preventEdgeOffset;
        if (r12 != 0) goto L_0x0214;
    L_0x0211:
        r5 = r5 - r9;
        goto L_0x0214;
    L_0x0213:
        r7 = 0;
    L_0x0214:
        r8 = r10 + -1;
        r12 = 1;
        r8 = r12 << r8;
        r7 = (long) r8;
        r7 = r18 & r7;
        r27 = 0;
        r7 = (r7 > r27 ? 1 : (r7 == r27 ? 0 : -1));
        if (r7 == 0) goto L_0x0233;
    L_0x0222:
        r7 = r10 + -1;
        r7 = r0.getChildAt(r7);
        r7 = r7.getLayoutParams();
        r7 = (android.support.v7.widget.ActionMenuView.LayoutParams) r7;
        r8 = r7.preventEdgeOffset;
        if (r8 != 0) goto L_0x0233;
    L_0x0232:
        r5 = r5 - r9;
    L_0x0233:
        r7 = 0;
        r7 = (r5 > r7 ? 1 : (r5 == r7 ? 0 : -1));
        if (r7 <= 0) goto L_0x023e;
    L_0x0238:
        r7 = r14 * r11;
        r7 = (float) r7;
        r7 = r7 / r5;
        r7 = (int) r7;
        goto L_0x023f;
    L_0x023e:
        r7 = 0;
    L_0x023f:
        r8 = 0;
    L_0x0240:
        if (r8 >= r10) goto L_0x02a2;
    L_0x0242:
        r9 = 1;
        r12 = r9 << r8;
        r39 = r4;
        r40 = r5;
        r4 = (long) r12;
        r4 = r18 & r4;
        r27 = 0;
        r4 = (r4 > r27 ? 1 : (r4 == r27 ? 0 : -1));
        if (r4 != 0) goto L_0x0253;
    L_0x0252:
        goto L_0x0273;
    L_0x0253:
        r4 = r0.getChildAt(r8);
        r5 = r4.getLayoutParams();
        r5 = (android.support.v7.widget.ActionMenuView.LayoutParams) r5;
        r9 = r4 instanceof android.support.v7.view.menu.ActionMenuItemView;
        if (r9 == 0) goto L_0x0277;
    L_0x0261:
        r5.extraPixels = r7;
        r9 = 1;
        r5.expanded = r9;
        if (r8 != 0) goto L_0x0271;
    L_0x0268:
        r9 = r5.preventEdgeOffset;
        if (r9 != 0) goto L_0x0271;
    L_0x026c:
        r9 = -r7;
        r12 = 2;
        r9 = r9 / r12;
        r5.leftMargin = r9;
    L_0x0271:
        r31 = 1;
    L_0x0273:
        r9 = 1;
        r20 = 2;
        goto L_0x029b;
    L_0x0277:
        r9 = r5.isOverflowButton;
        if (r9 == 0) goto L_0x028a;
    L_0x027b:
        r5.extraPixels = r7;
        r9 = 1;
        r5.expanded = r9;
        r12 = -r7;
        r20 = 2;
        r12 = r12 / 2;
        r5.rightMargin = r12;
        r31 = 1;
        goto L_0x029b;
    L_0x028a:
        r9 = 1;
        r20 = 2;
        if (r8 == 0) goto L_0x0293;
    L_0x028f:
        r12 = r7 / 2;
        r5.leftMargin = r12;
    L_0x0293:
        r12 = r10 + -1;
        if (r8 == r12) goto L_0x029b;
    L_0x0297:
        r12 = r7 / 2;
        r5.rightMargin = r12;
    L_0x029b:
        r8 = r8 + 1;
        r4 = r39;
        r5 = r40;
        goto L_0x0240;
    L_0x02a2:
        r39 = r4;
        r40 = r5;
        r14 = 0;
        goto L_0x02aa;
    L_0x02a8:
        r39 = r4;
    L_0x02aa:
        r4 = 1073741824; // 0x40000000 float:2.0 double:5.304989477E-315;
        if (r31 == 0) goto L_0x02d4;
    L_0x02ae:
        r38 = 0;
    L_0x02b0:
        r5 = r38;
        if (r5 >= r10) goto L_0x02d4;
    L_0x02b4:
        r7 = r0.getChildAt(r5);
        r8 = r7.getLayoutParams();
        r8 = (android.support.v7.widget.ActionMenuView.LayoutParams) r8;
        r9 = r8.expanded;
        if (r9 != 0) goto L_0x02c3;
    L_0x02c2:
        goto L_0x02d1;
    L_0x02c3:
        r9 = r8.cellsUsed;
        r9 = r9 * r11;
        r12 = r8.extraPixels;
        r9 = r9 + r12;
        r12 = android.view.View.MeasureSpec.makeMeasureSpec(r9, r4);
        r7.measure(r12, r6);
    L_0x02d1:
        r38 = r5 + 1;
        goto L_0x02b0;
    L_0x02d4:
        if (r1 == r4) goto L_0x02d8;
    L_0x02d6:
        r4 = r3;
        goto L_0x02da;
    L_0x02d8:
        r4 = r21;
    L_0x02da:
        r0.setMeasuredDimension(r2, r4);
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.support.v7.widget.ActionMenuView.onMeasureExactFormat(int, int):void");
    }

    static int measureChildForCells(View child, int cellSize, int cellsRemaining, int parentHeightMeasureSpec, int parentHeightPadding) {
        View view = child;
        int i = cellsRemaining;
        LayoutParams lp = (LayoutParams) child.getLayoutParams();
        int childHeightSpec = MeasureSpec.makeMeasureSpec(MeasureSpec.getSize(parentHeightMeasureSpec) - parentHeightPadding, MeasureSpec.getMode(parentHeightMeasureSpec));
        ActionMenuItemView itemView = view instanceof ActionMenuItemView ? (ActionMenuItemView) view : null;
        boolean expandable = false;
        boolean hasText = itemView != null && itemView.hasText();
        int cellsUsed = 0;
        if (i > 0 && (!hasText || i >= 2)) {
            child.measure(MeasureSpec.makeMeasureSpec(cellSize * i, Integer.MIN_VALUE), childHeightSpec);
            int measuredWidth = child.getMeasuredWidth();
            cellsUsed = measuredWidth / cellSize;
            if (measuredWidth % cellSize != 0) {
                cellsUsed++;
            }
            if (hasText && cellsUsed < 2) {
                cellsUsed = 2;
            }
        }
        if (!lp.isOverflowButton && hasText) {
            expandable = true;
        }
        lp.expandable = expandable;
        lp.cellsUsed = cellsUsed;
        child.measure(MeasureSpec.makeMeasureSpec(cellsUsed * cellSize, 1073741824), childHeightSpec);
        return cellsUsed;
    }

    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        if (this.mFormatItems) {
            int midVertical;
            boolean isLayoutRtl;
            int overflowWidth;
            int t;
            int size;
            int childCount = getChildCount();
            int midVertical2 = (bottom - top) / 2;
            int dividerWidth = getDividerWidth();
            int nonOverflowCount = 0;
            int widthRemaining = ((right - left) - getPaddingRight()) - getPaddingLeft();
            boolean hasOverflow = false;
            boolean isLayoutRtl2 = ViewUtils.isLayoutRtl(this);
            int widthRemaining2 = widthRemaining;
            widthRemaining = 0;
            int overflowWidth2 = 0;
            int i = 0;
            while (i < childCount) {
                View v = getChildAt(i);
                if (v.getVisibility() == 8) {
                    midVertical = midVertical2;
                    isLayoutRtl = isLayoutRtl2;
                } else {
                    LayoutParams p = (LayoutParams) v.getLayoutParams();
                    if (p.isOverflowButton) {
                        int l;
                        overflowWidth = v.getMeasuredWidth();
                        if (hasSupportDividerBeforeChildAt(i)) {
                            overflowWidth += dividerWidth;
                        }
                        overflowWidth2 = v.getMeasuredHeight();
                        if (isLayoutRtl2) {
                            isLayoutRtl = isLayoutRtl2;
                            l = getPaddingLeft() + p.leftMargin;
                            isLayoutRtl2 = l + overflowWidth;
                        } else {
                            isLayoutRtl = isLayoutRtl2;
                            isLayoutRtl2 = (getWidth() - getPaddingRight()) - p.rightMargin;
                            l = isLayoutRtl2 - overflowWidth;
                        }
                        t = midVertical2 - (overflowWidth2 / 2);
                        midVertical = midVertical2;
                        v.layout(l, t, isLayoutRtl2, t + overflowWidth2);
                        widthRemaining2 -= overflowWidth;
                        hasOverflow = true;
                        overflowWidth2 = overflowWidth;
                    } else {
                        midVertical = midVertical2;
                        isLayoutRtl = isLayoutRtl2;
                        size = (v.getMeasuredWidth() + p.leftMargin) + p.rightMargin;
                        widthRemaining += size;
                        widthRemaining2 -= size;
                        if (hasSupportDividerBeforeChildAt(i)) {
                            widthRemaining += dividerWidth;
                        }
                        nonOverflowCount++;
                    }
                }
                i++;
                isLayoutRtl2 = isLayoutRtl;
                midVertical2 = midVertical;
            }
            midVertical = midVertical2;
            isLayoutRtl = isLayoutRtl2;
            int i2 = 1;
            int spacerSize;
            int t2;
            if (childCount != 1 || hasOverflow) {
                if (hasOverflow) {
                    i2 = 0;
                }
                size = nonOverflowCount - i2;
                t = 0;
                spacerSize = Math.max(0, size > 0 ? widthRemaining2 / size : 0);
                int dividerWidth2;
                int overflowWidth3;
                if (isLayoutRtl) {
                    overflowWidth = getWidth() - getPaddingRight();
                    while (t < childCount) {
                        View v2 = getChildAt(t);
                        LayoutParams lp = (LayoutParams) v2.getLayoutParams();
                        int spacerCount = size;
                        if (v2.getVisibility() == 8) {
                            dividerWidth2 = dividerWidth;
                            overflowWidth3 = overflowWidth2;
                        } else if (lp.isOverflowButton != 0) {
                            dividerWidth2 = dividerWidth;
                            overflowWidth3 = overflowWidth2;
                        } else {
                            overflowWidth -= lp.rightMargin;
                            size = v2.getMeasuredWidth();
                            i2 = v2.getMeasuredHeight();
                            midVertical2 = midVertical - (i2 / 2);
                            dividerWidth2 = dividerWidth;
                            overflowWidth3 = overflowWidth2;
                            v2.layout(overflowWidth - size, midVertical2, overflowWidth, midVertical2 + i2);
                            overflowWidth -= (lp.leftMargin + size) + spacerSize;
                        }
                        t++;
                        size = spacerCount;
                        dividerWidth = dividerWidth2;
                        overflowWidth2 = overflowWidth3;
                    }
                    dividerWidth2 = dividerWidth;
                    overflowWidth3 = overflowWidth2;
                } else {
                    dividerWidth2 = dividerWidth;
                    overflowWidth3 = overflowWidth2;
                    size = getPaddingLeft();
                    while (t < childCount) {
                        View v3 = getChildAt(t);
                        LayoutParams lp2 = (LayoutParams) v3.getLayoutParams();
                        if (!(v3.getVisibility() == 8 || lp2.isOverflowButton)) {
                            size += lp2.leftMargin;
                            dividerWidth = v3.getMeasuredWidth();
                            overflowWidth2 = v3.getMeasuredHeight();
                            t2 = midVertical - (overflowWidth2 / 2);
                            v3.layout(size, t2, size + dividerWidth, t2 + overflowWidth2);
                            size += (lp2.rightMargin + dividerWidth) + spacerSize;
                        }
                        t++;
                    }
                }
                return;
            }
            View v4 = getChildAt(null);
            t = v4.getMeasuredWidth();
            spacerSize = v4.getMeasuredHeight();
            t2 = ((right - left) / 2) - (t / 2);
            i2 = midVertical - (spacerSize / 2);
            v4.layout(t2, i2, t2 + t, i2 + spacerSize);
            return;
        }
        super.onLayout(changed, left, top, right, bottom);
    }

    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        dismissPopupMenus();
    }

    public void setOverflowIcon(@Nullable Drawable icon) {
        getMenu();
        this.mPresenter.setOverflowIcon(icon);
    }

    @Nullable
    public Drawable getOverflowIcon() {
        getMenu();
        return this.mPresenter.getOverflowIcon();
    }

    @RestrictTo({Scope.LIBRARY_GROUP})
    public boolean isOverflowReserved() {
        return this.mReserveOverflow;
    }

    @RestrictTo({Scope.LIBRARY_GROUP})
    public void setOverflowReserved(boolean reserveOverflow) {
        this.mReserveOverflow = reserveOverflow;
    }

    protected LayoutParams generateDefaultLayoutParams() {
        LayoutParams params = new LayoutParams(-2, -2);
        params.gravity = 16;
        return params;
    }

    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new LayoutParams(getContext(), attrs);
    }

    protected LayoutParams generateLayoutParams(android.view.ViewGroup.LayoutParams p) {
        if (p == null) {
            return generateDefaultLayoutParams();
        }
        LayoutParams result = p instanceof LayoutParams ? new LayoutParams((LayoutParams) p) : new LayoutParams(p);
        if (result.gravity <= 0) {
            result.gravity = 16;
        }
        return result;
    }

    protected boolean checkLayoutParams(android.view.ViewGroup.LayoutParams p) {
        return p != null && (p instanceof LayoutParams);
    }

    @RestrictTo({Scope.LIBRARY_GROUP})
    public LayoutParams generateOverflowButtonLayoutParams() {
        LayoutParams result = generateDefaultLayoutParams();
        result.isOverflowButton = true;
        return result;
    }

    @RestrictTo({Scope.LIBRARY_GROUP})
    public boolean invokeItem(MenuItemImpl item) {
        return this.mMenu.performItemAction(item, 0);
    }

    @RestrictTo({Scope.LIBRARY_GROUP})
    public int getWindowAnimations() {
        return 0;
    }

    @RestrictTo({Scope.LIBRARY_GROUP})
    public void initialize(MenuBuilder menu) {
        this.mMenu = menu;
    }

    public Menu getMenu() {
        if (this.mMenu == null) {
            Context context = getContext();
            this.mMenu = new MenuBuilder(context);
            this.mMenu.setCallback(new MenuBuilderCallback());
            this.mPresenter = new ActionMenuPresenter(context);
            this.mPresenter.setReserveOverflow(true);
            this.mPresenter.setCallback(this.mActionMenuPresenterCallback != null ? this.mActionMenuPresenterCallback : new ActionMenuPresenterCallback());
            this.mMenu.addMenuPresenter(this.mPresenter, this.mPopupContext);
            this.mPresenter.setMenuView(this);
        }
        return this.mMenu;
    }

    @RestrictTo({Scope.LIBRARY_GROUP})
    public void setMenuCallbacks(Callback pcb, MenuBuilder.Callback mcb) {
        this.mActionMenuPresenterCallback = pcb;
        this.mMenuBuilderCallback = mcb;
    }

    @RestrictTo({Scope.LIBRARY_GROUP})
    public MenuBuilder peekMenu() {
        return this.mMenu;
    }

    public boolean showOverflowMenu() {
        return this.mPresenter != null && this.mPresenter.showOverflowMenu();
    }

    public boolean hideOverflowMenu() {
        return this.mPresenter != null && this.mPresenter.hideOverflowMenu();
    }

    public boolean isOverflowMenuShowing() {
        return this.mPresenter != null && this.mPresenter.isOverflowMenuShowing();
    }

    @RestrictTo({Scope.LIBRARY_GROUP})
    public boolean isOverflowMenuShowPending() {
        return this.mPresenter != null && this.mPresenter.isOverflowMenuShowPending();
    }

    public void dismissPopupMenus() {
        if (this.mPresenter != null) {
            this.mPresenter.dismissPopupMenus();
        }
    }

    @RestrictTo({Scope.LIBRARY_GROUP})
    protected boolean hasSupportDividerBeforeChildAt(int childIndex) {
        if (childIndex == 0) {
            return false;
        }
        View childBefore = getChildAt(childIndex - 1);
        View child = getChildAt(childIndex);
        boolean result = false;
        if (childIndex < getChildCount() && (childBefore instanceof ActionMenuChildView)) {
            result = false | ((ActionMenuChildView) childBefore).needsDividerAfter();
        }
        if (childIndex > 0 && (child instanceof ActionMenuChildView)) {
            result |= ((ActionMenuChildView) child).needsDividerBefore();
        }
        return result;
    }

    public boolean dispatchPopulateAccessibilityEvent(AccessibilityEvent event) {
        return false;
    }

    @RestrictTo({Scope.LIBRARY_GROUP})
    public void setExpandedActionViewsExclusive(boolean exclusive) {
        this.mPresenter.setExpandedActionViewsExclusive(exclusive);
    }
}
