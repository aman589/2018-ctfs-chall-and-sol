package android.support.v4.graphics;

import android.graphics.Path;
import android.support.annotation.RestrictTo;
import android.support.annotation.RestrictTo.Scope;
import android.util.Log;
import java.util.ArrayList;

@RestrictTo({Scope.LIBRARY_GROUP})
public class PathParser {
    private static final String LOGTAG = "PathParser";

    private static class ExtractFloatResult {
        int mEndPosition;
        boolean mEndWithNegOrDot;

        ExtractFloatResult() {
        }
    }

    public static class PathDataNode {
        @RestrictTo({Scope.LIBRARY_GROUP})
        public float[] mParams;
        @RestrictTo({Scope.LIBRARY_GROUP})
        public char mType;

        PathDataNode(char type, float[] params) {
            this.mType = type;
            this.mParams = params;
        }

        PathDataNode(PathDataNode n) {
            this.mType = n.mType;
            this.mParams = PathParser.copyOfRange(n.mParams, 0, n.mParams.length);
        }

        public static void nodesToPath(PathDataNode[] node, Path path) {
            float[] current = new float[6];
            char previousCommand = 'm';
            for (int i = 0; i < node.length; i++) {
                addCommand(path, current, previousCommand, node[i].mType, node[i].mParams);
                previousCommand = node[i].mType;
            }
        }

        public void interpolatePathDataNode(PathDataNode nodeFrom, PathDataNode nodeTo, float fraction) {
            for (int i = 0; i < nodeFrom.mParams.length; i++) {
                this.mParams[i] = (nodeFrom.mParams[i] * (1.0f - fraction)) + (nodeTo.mParams[i] * fraction);
            }
        }

        /* JADX WARNING: Missing block: B:16:0x006d, code:
            r27 = r6;
     */
        /* JADX WARNING: Missing block: B:42:0x019e, code:
            r21 = r0;
            r22 = r1;
     */
        /* JADX WARNING: Missing block: B:43:0x01a2, code:
            r14 = r7;
     */
        /* JADX WARNING: Missing block: B:79:0x0343, code:
            r21 = r0;
            r22 = r1;
     */
        private static void addCommand(android.graphics.Path r28, float[] r29, char r30, char r31, float[] r32) {
            /*
            r10 = r28;
            r13 = r32;
            r0 = 2;
            r14 = 0;
            r1 = r29[r14];
            r15 = 1;
            r2 = r29[r15];
            r16 = 2;
            r3 = r29[r16];
            r17 = 3;
            r4 = r29[r17];
            r18 = 4;
            r5 = r29[r18];
            r19 = 5;
            r6 = r29[r19];
            switch(r31) {
                case 65: goto L_0x0034;
                case 67: goto L_0x0032;
                case 72: goto L_0x0030;
                case 76: goto L_0x002e;
                case 77: goto L_0x002e;
                case 81: goto L_0x002c;
                case 83: goto L_0x002c;
                case 84: goto L_0x002e;
                case 86: goto L_0x0030;
                case 90: goto L_0x0021;
                case 97: goto L_0x0034;
                case 99: goto L_0x0032;
                case 104: goto L_0x0030;
                case 108: goto L_0x002e;
                case 109: goto L_0x002e;
                case 113: goto L_0x002c;
                case 115: goto L_0x002c;
                case 116: goto L_0x002e;
                case 118: goto L_0x0030;
                case 122: goto L_0x0021;
                default: goto L_0x001e;
            };
        L_0x001e:
            r20 = r0;
            goto L_0x0036;
        L_0x0021:
            r28.close();
            r1 = r5;
            r2 = r6;
            r3 = r5;
            r4 = r6;
            r10.moveTo(r1, r2);
            goto L_0x001e;
        L_0x002c:
            r0 = 4;
            goto L_0x001e;
        L_0x002e:
            r0 = 2;
            goto L_0x001e;
        L_0x0030:
            r0 = 1;
            goto L_0x001e;
        L_0x0032:
            r0 = 6;
            goto L_0x001e;
        L_0x0034:
            r0 = 7;
            goto L_0x001e;
        L_0x0036:
            r7 = r30;
            r9 = r1;
            r8 = r2;
            r21 = r3;
            r22 = r4;
            r23 = r5;
            r24 = r6;
            r0 = 0;
        L_0x0043:
            r6 = r0;
            r0 = r13.length;
            if (r6 >= r0) goto L_0x0391;
        L_0x0047:
            r0 = 81;
            r1 = 67;
            r2 = 116; // 0x74 float:1.63E-43 double:5.73E-322;
            r3 = 115; // 0x73 float:1.61E-43 double:5.7E-322;
            r4 = 113; // 0x71 float:1.58E-43 double:5.6E-322;
            r5 = 99;
            r25 = 1073741824; // 0x40000000 float:2.0 double:5.304989477E-315;
            r15 = 0;
            switch(r31) {
                case 65: goto L_0x0348;
                case 67: goto L_0x0310;
                case 72: goto L_0x02fe;
                case 76: goto L_0x02e4;
                case 77: goto L_0x02b4;
                case 81: goto L_0x028a;
                case 83: goto L_0x023d;
                case 84: goto L_0x0208;
                case 86: goto L_0x01f6;
                case 97: goto L_0x01a5;
                case 99: goto L_0x016a;
                case 104: goto L_0x015b;
                case 108: goto L_0x0143;
                case 109: goto L_0x0115;
                case 113: goto L_0x00ea;
                case 115: goto L_0x00a0;
                case 116: goto L_0x0071;
                case 118: goto L_0x0060;
                default: goto L_0x0059;
            };
        L_0x0059:
            r27 = r6;
            r14 = r7;
            r12 = r8;
            r11 = r9;
            goto L_0x0389;
        L_0x0060:
            r0 = r6 + 0;
            r0 = r13[r0];
            r10.rLineTo(r15, r0);
            r0 = r6 + 0;
            r0 = r13[r0];
            r8 = r8 + r0;
        L_0x006d:
            r27 = r6;
            goto L_0x01a2;
        L_0x0071:
            r1 = 0;
            r3 = 0;
            if (r7 == r4) goto L_0x007d;
        L_0x0075:
            if (r7 == r2) goto L_0x007d;
        L_0x0077:
            if (r7 == r0) goto L_0x007d;
        L_0x0079:
            r0 = 84;
            if (r7 != r0) goto L_0x0081;
        L_0x007d:
            r1 = r9 - r21;
            r3 = r8 - r22;
        L_0x0081:
            r0 = r6 + 0;
            r0 = r13[r0];
            r2 = r6 + 1;
            r2 = r13[r2];
            r10.rQuadTo(r1, r3, r0, r2);
            r0 = r9 + r1;
            r2 = r8 + r3;
            r4 = r6 + 0;
            r4 = r13[r4];
            r9 = r9 + r4;
            r4 = r6 + 1;
            r4 = r13[r4];
            r8 = r8 + r4;
            r21 = r0;
            r22 = r2;
            goto L_0x006d;
        L_0x00a0:
            r0 = 0;
            r2 = 0;
            if (r7 == r5) goto L_0x00b1;
        L_0x00a4:
            if (r7 == r3) goto L_0x00b1;
        L_0x00a6:
            if (r7 == r1) goto L_0x00b1;
        L_0x00a8:
            r1 = 83;
            if (r7 != r1) goto L_0x00ad;
        L_0x00ac:
            goto L_0x00b1;
        L_0x00ad:
            r15 = r0;
            r25 = r2;
            goto L_0x00b8;
        L_0x00b1:
            r0 = r9 - r21;
            r1 = r8 - r22;
            r15 = r0;
            r25 = r1;
        L_0x00b8:
            r0 = r6 + 0;
            r3 = r13[r0];
            r0 = r6 + 1;
            r4 = r13[r0];
            r0 = r6 + 2;
            r5 = r13[r0];
            r0 = r6 + 3;
            r26 = r13[r0];
            r0 = r28;
            r1 = r15;
            r2 = r25;
            r27 = r6;
            r6 = r26;
            r0.rCubicTo(r1, r2, r3, r4, r5, r6);
            r6 = r27 + 0;
            r0 = r13[r6];
            r0 = r0 + r9;
            r6 = r27 + 1;
            r1 = r13[r6];
            r1 = r1 + r8;
            r6 = r27 + 2;
            r2 = r13[r6];
            r9 = r9 + r2;
            r6 = r27 + 3;
            r2 = r13[r6];
            r8 = r8 + r2;
            goto L_0x019e;
        L_0x00ea:
            r27 = r6;
            r6 = r27 + 0;
            r0 = r13[r6];
            r6 = r27 + 1;
            r1 = r13[r6];
            r6 = r27 + 2;
            r2 = r13[r6];
            r6 = r27 + 3;
            r3 = r13[r6];
            r10.rQuadTo(r0, r1, r2, r3);
            r6 = r27 + 0;
            r0 = r13[r6];
            r0 = r0 + r9;
            r6 = r27 + 1;
            r1 = r13[r6];
            r1 = r1 + r8;
            r6 = r27 + 2;
            r2 = r13[r6];
            r9 = r9 + r2;
            r6 = r27 + 3;
            r2 = r13[r6];
            r8 = r8 + r2;
            goto L_0x019e;
        L_0x0115:
            r27 = r6;
            r6 = r27 + 0;
            r0 = r13[r6];
            r9 = r9 + r0;
            r6 = r27 + 1;
            r0 = r13[r6];
            r8 = r8 + r0;
            if (r27 <= 0) goto L_0x0130;
        L_0x0123:
            r6 = r27 + 0;
            r0 = r13[r6];
            r6 = r27 + 1;
            r1 = r13[r6];
            r10.rLineTo(r0, r1);
            goto L_0x01a2;
        L_0x0130:
            r6 = r27 + 0;
            r0 = r13[r6];
            r6 = r27 + 1;
            r1 = r13[r6];
            r10.rMoveTo(r0, r1);
            r0 = r9;
            r1 = r8;
            r23 = r0;
            r24 = r1;
            goto L_0x01a2;
        L_0x0143:
            r27 = r6;
            r6 = r27 + 0;
            r0 = r13[r6];
            r6 = r27 + 1;
            r1 = r13[r6];
            r10.rLineTo(r0, r1);
            r6 = r27 + 0;
            r0 = r13[r6];
            r9 = r9 + r0;
            r6 = r27 + 1;
            r0 = r13[r6];
            r8 = r8 + r0;
            goto L_0x01a2;
        L_0x015b:
            r27 = r6;
            r6 = r27 + 0;
            r0 = r13[r6];
            r10.rLineTo(r0, r15);
            r6 = r27 + 0;
            r0 = r13[r6];
            r9 = r9 + r0;
            goto L_0x01a2;
        L_0x016a:
            r27 = r6;
            r6 = r27 + 0;
            r1 = r13[r6];
            r6 = r27 + 1;
            r2 = r13[r6];
            r6 = r27 + 2;
            r3 = r13[r6];
            r6 = r27 + 3;
            r4 = r13[r6];
            r6 = r27 + 4;
            r5 = r13[r6];
            r6 = r27 + 5;
            r6 = r13[r6];
            r0 = r28;
            r0.rCubicTo(r1, r2, r3, r4, r5, r6);
            r6 = r27 + 2;
            r0 = r13[r6];
            r0 = r0 + r9;
            r6 = r27 + 3;
            r1 = r13[r6];
            r1 = r1 + r8;
            r6 = r27 + 4;
            r2 = r13[r6];
            r9 = r9 + r2;
            r6 = r27 + 5;
            r2 = r13[r6];
            r8 = r8 + r2;
        L_0x019e:
            r21 = r0;
            r22 = r1;
        L_0x01a2:
            r14 = r7;
            goto L_0x0389;
        L_0x01a5:
            r27 = r6;
            r6 = r27 + 5;
            r0 = r13[r6];
            r3 = r0 + r9;
            r6 = r27 + 6;
            r0 = r13[r6];
            r4 = r0 + r8;
            r6 = r27 + 0;
            r5 = r13[r6];
            r6 = r27 + 1;
            r6 = r13[r6];
            r0 = r27 + 2;
            r25 = r13[r0];
            r0 = r27 + 3;
            r0 = r13[r0];
            r0 = (r0 > r15 ? 1 : (r0 == r15 ? 0 : -1));
            if (r0 == 0) goto L_0x01ca;
        L_0x01c7:
            r26 = 1;
            goto L_0x01cc;
        L_0x01ca:
            r26 = 0;
        L_0x01cc:
            r0 = r27 + 4;
            r0 = r13[r0];
            r0 = (r0 > r15 ? 1 : (r0 == r15 ? 0 : -1));
            if (r0 == 0) goto L_0x01d6;
        L_0x01d4:
            r15 = 1;
            goto L_0x01d7;
        L_0x01d6:
            r15 = 0;
        L_0x01d7:
            r0 = r28;
            r1 = r9;
            r2 = r8;
            r14 = r7;
            r7 = r25;
            r12 = r8;
            r8 = r26;
            r11 = r9;
            r9 = r15;
            drawArc(r0, r1, r2, r3, r4, r5, r6, r7, r8, r9);
            r6 = r27 + 5;
            r0 = r13[r6];
            r9 = r11 + r0;
            r6 = r27 + 6;
            r0 = r13[r6];
            r8 = r12 + r0;
            r0 = r9;
            r1 = r8;
            goto L_0x0343;
        L_0x01f6:
            r27 = r6;
            r14 = r7;
            r12 = r8;
            r11 = r9;
            r6 = r27 + 0;
            r0 = r13[r6];
            r10.lineTo(r11, r0);
            r6 = r27 + 0;
            r8 = r13[r6];
            goto L_0x0389;
        L_0x0208:
            r27 = r6;
            r14 = r7;
            r12 = r8;
            r11 = r9;
            r1 = r11;
            r3 = r12;
            if (r14 == r4) goto L_0x0219;
        L_0x0211:
            if (r14 == r2) goto L_0x0219;
        L_0x0213:
            if (r14 == r0) goto L_0x0219;
        L_0x0215:
            r0 = 84;
            if (r14 != r0) goto L_0x0221;
        L_0x0219:
            r9 = r11 * r25;
            r1 = r9 - r21;
            r8 = r12 * r25;
            r3 = r8 - r22;
        L_0x0221:
            r6 = r27 + 0;
            r0 = r13[r6];
            r6 = r27 + 1;
            r2 = r13[r6];
            r10.quadTo(r1, r3, r0, r2);
            r0 = r1;
            r2 = r3;
            r6 = r27 + 0;
            r9 = r13[r6];
            r6 = r27 + 1;
            r8 = r13[r6];
            r21 = r0;
            r22 = r2;
            goto L_0x0389;
        L_0x023d:
            r27 = r6;
            r14 = r7;
            r12 = r8;
            r11 = r9;
            r0 = r11;
            r2 = r12;
            if (r14 == r5) goto L_0x0252;
        L_0x0246:
            if (r14 == r3) goto L_0x0252;
        L_0x0248:
            if (r14 == r1) goto L_0x0252;
        L_0x024a:
            r1 = 83;
            if (r14 != r1) goto L_0x024f;
        L_0x024e:
            goto L_0x0252;
        L_0x024f:
            r9 = r0;
            r8 = r2;
            goto L_0x025a;
        L_0x0252:
            r9 = r11 * r25;
            r9 = r9 - r21;
            r8 = r12 * r25;
            r8 = r8 - r22;
        L_0x025a:
            r6 = r27 + 0;
            r3 = r13[r6];
            r6 = r27 + 1;
            r4 = r13[r6];
            r6 = r27 + 2;
            r5 = r13[r6];
            r6 = r27 + 3;
            r6 = r13[r6];
            r0 = r28;
            r1 = r9;
            r2 = r8;
            r0.cubicTo(r1, r2, r3, r4, r5, r6);
            r6 = r27 + 0;
            r0 = r13[r6];
            r6 = r27 + 1;
            r1 = r13[r6];
            r6 = r27 + 2;
            r2 = r13[r6];
            r6 = r27 + 3;
            r3 = r13[r6];
            r21 = r0;
            r22 = r1;
            r9 = r2;
            r8 = r3;
            goto L_0x0389;
        L_0x028a:
            r27 = r6;
            r14 = r7;
            r12 = r8;
            r11 = r9;
            r6 = r27 + 0;
            r0 = r13[r6];
            r6 = r27 + 1;
            r1 = r13[r6];
            r6 = r27 + 2;
            r2 = r13[r6];
            r6 = r27 + 3;
            r3 = r13[r6];
            r10.quadTo(r0, r1, r2, r3);
            r6 = r27 + 0;
            r0 = r13[r6];
            r6 = r27 + 1;
            r1 = r13[r6];
            r6 = r27 + 2;
            r9 = r13[r6];
            r6 = r27 + 3;
            r8 = r13[r6];
            goto L_0x0343;
        L_0x02b4:
            r27 = r6;
            r14 = r7;
            r12 = r8;
            r11 = r9;
            r6 = r27 + 0;
            r9 = r13[r6];
            r6 = r27 + 1;
            r8 = r13[r6];
            if (r27 <= 0) goto L_0x02d0;
        L_0x02c3:
            r6 = r27 + 0;
            r0 = r13[r6];
            r6 = r27 + 1;
            r1 = r13[r6];
            r10.lineTo(r0, r1);
            goto L_0x0389;
        L_0x02d0:
            r6 = r27 + 0;
            r0 = r13[r6];
            r6 = r27 + 1;
            r1 = r13[r6];
            r10.moveTo(r0, r1);
            r0 = r9;
            r1 = r8;
            r23 = r0;
            r24 = r1;
            goto L_0x0389;
        L_0x02e4:
            r27 = r6;
            r14 = r7;
            r12 = r8;
            r11 = r9;
            r6 = r27 + 0;
            r0 = r13[r6];
            r6 = r27 + 1;
            r1 = r13[r6];
            r10.lineTo(r0, r1);
            r6 = r27 + 0;
            r9 = r13[r6];
            r6 = r27 + 1;
            r8 = r13[r6];
            goto L_0x0389;
        L_0x02fe:
            r27 = r6;
            r14 = r7;
            r12 = r8;
            r11 = r9;
            r6 = r27 + 0;
            r0 = r13[r6];
            r10.lineTo(r0, r12);
            r6 = r27 + 0;
            r9 = r13[r6];
            goto L_0x0389;
        L_0x0310:
            r27 = r6;
            r14 = r7;
            r12 = r8;
            r11 = r9;
            r6 = r27 + 0;
            r1 = r13[r6];
            r6 = r27 + 1;
            r2 = r13[r6];
            r6 = r27 + 2;
            r3 = r13[r6];
            r6 = r27 + 3;
            r4 = r13[r6];
            r6 = r27 + 4;
            r5 = r13[r6];
            r6 = r27 + 5;
            r6 = r13[r6];
            r0 = r28;
            r0.cubicTo(r1, r2, r3, r4, r5, r6);
            r6 = r27 + 4;
            r9 = r13[r6];
            r6 = r27 + 5;
            r8 = r13[r6];
            r6 = r27 + 2;
            r0 = r13[r6];
            r6 = r27 + 3;
            r1 = r13[r6];
        L_0x0343:
            r21 = r0;
            r22 = r1;
            goto L_0x0389;
        L_0x0348:
            r27 = r6;
            r14 = r7;
            r12 = r8;
            r11 = r9;
            r6 = r27 + 5;
            r3 = r13[r6];
            r6 = r27 + 6;
            r4 = r13[r6];
            r6 = r27 + 0;
            r5 = r13[r6];
            r6 = r27 + 1;
            r6 = r13[r6];
            r0 = r27 + 2;
            r7 = r13[r0];
            r0 = r27 + 3;
            r0 = r13[r0];
            r0 = (r0 > r15 ? 1 : (r0 == r15 ? 0 : -1));
            if (r0 == 0) goto L_0x036b;
        L_0x0369:
            r8 = 1;
            goto L_0x036c;
        L_0x036b:
            r8 = 0;
        L_0x036c:
            r0 = r27 + 4;
            r0 = r13[r0];
            r0 = (r0 > r15 ? 1 : (r0 == r15 ? 0 : -1));
            if (r0 == 0) goto L_0x0376;
        L_0x0374:
            r9 = 1;
            goto L_0x0377;
        L_0x0376:
            r9 = 0;
        L_0x0377:
            r0 = r28;
            r1 = r11;
            r2 = r12;
            drawArc(r0, r1, r2, r3, r4, r5, r6, r7, r8, r9);
            r6 = r27 + 5;
            r9 = r13[r6];
            r6 = r27 + 6;
            r8 = r13[r6];
            r0 = r9;
            r1 = r8;
            goto L_0x0343;
        L_0x0389:
            r7 = r31;
            r0 = r27 + r20;
            r14 = 0;
            r15 = 1;
            goto L_0x0043;
        L_0x0391:
            r14 = r7;
            r12 = r8;
            r11 = r9;
            r1 = r11;
            r2 = 0;
            r29[r2] = r1;
            r2 = 1;
            r29[r2] = r12;
            r29[r16] = r21;
            r29[r17] = r22;
            r29[r18] = r23;
            r29[r19] = r24;
            return;
            */
            throw new UnsupportedOperationException("Method not decompiled: android.support.v4.graphics.PathParser.PathDataNode.addCommand(android.graphics.Path, float[], char, char, float[]):void");
        }

        private static void drawArc(Path p, float x0, float y0, float x1, float y1, float a, float b, float theta, boolean isMoreThanHalf, boolean isPositiveArc) {
            float f = x0;
            float f2 = y0;
            float f3 = x1;
            float f4 = y1;
            float f5 = a;
            float f6 = b;
            boolean z = isPositiveArc;
            double thetaD = Math.toRadians((double) theta);
            double cosTheta = Math.cos(thetaD);
            double sinTheta = Math.sin(thetaD);
            double x0p = ((((double) f) * cosTheta) + (((double) f2) * sinTheta)) / ((double) f5);
            double y0p = ((((double) (-f)) * sinTheta) + (((double) f2) * cosTheta)) / ((double) f6);
            double x1p = ((((double) f3) * cosTheta) + (((double) f4) * sinTheta)) / ((double) f5);
            double y1p = ((((double) (-f3)) * sinTheta) + (((double) f4) * cosTheta)) / ((double) f6);
            double dx = x0p - x1p;
            double dy = y0p - y1p;
            double xm = (x0p + x1p) / 2.0d;
            double ym = (y0p + y1p) / 2.0d;
            double dsq = (dx * dx) + (dy * dy);
            if (dsq == 0.0d) {
                Log.w(PathParser.LOGTAG, " Points are coincident");
                return;
            }
            double disc = (1.0d / dsq) - 0.25d;
            boolean z2;
            if (disc < 0.0d) {
                String str = PathParser.LOGTAG;
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Points are too far apart ");
                stringBuilder.append(dsq);
                Log.w(str, stringBuilder.toString());
                float adjust = (float) (Math.sqrt(dsq) / 1.99999d);
                z2 = z;
                drawArc(p, x0, y0, x1, y1, f5 * adjust, f6 * adjust, theta, isMoreThanHalf, isPositiveArc);
                return;
            }
            double cx;
            double cy;
            z2 = z;
            double s = Math.sqrt(disc);
            double sdx = s * dx;
            dsq = s * dy;
            if (isMoreThanHalf == z2) {
                cx = xm - dsq;
                cy = ym + sdx;
            } else {
                cx = xm + dsq;
                cy = ym - sdx;
            }
            double eta0 = Math.atan2(y0p - cy, x0p - cx);
            s = Math.atan2(y1p - cy, x1p - cx);
            sdx = s - eta0;
            if (z2 != (sdx >= 0.0d)) {
                if (sdx > 0.0d) {
                    sdx -= 6.283185307179586d;
                } else {
                    sdx += 6.283185307179586d;
                }
            }
            cx *= (double) f5;
            s = ((double) f6) * cy;
            s = (cx * sinTheta) + (s * cosTheta);
            arcToBezier(p, (cx * cosTheta) - (s * sinTheta), s, (double) f5, (double) f6, (double) f, (double) f2, thetaD, eta0, sdx);
        }

        private static void arcToBezier(Path p, double cx, double cy, double a, double b, double e1x, double e1y, double theta, double start, double sweep) {
            double eta1 = a;
            int numSegments = (int) Math.ceil(Math.abs((sweep * 4.0d) / 3.141592653589793d));
            double eta12 = start;
            double cosTheta = Math.cos(theta);
            double sinTheta = Math.sin(theta);
            double cosEta1 = Math.cos(eta12);
            double sinEta1 = Math.sin(eta12);
            double anglePerSegment = sweep / ((double) numSegments);
            int i = 0;
            double ep1y = (((-eta1) * sinTheta) * sinEta1) + ((b * cosTheta) * cosEta1);
            double e1y2 = e1y;
            double ep1x = (((-eta1) * cosTheta) * sinEta1) - ((b * sinTheta) * cosEta1);
            double e1x2 = e1x;
            while (true) {
                int i2 = i;
                int numSegments2;
                double cosTheta2;
                double sinTheta2;
                if (i2 < numSegments) {
                    double eta2 = eta12 + anglePerSegment;
                    double sinEta2 = Math.sin(eta2);
                    double cosEta2 = Math.cos(eta2);
                    double anglePerSegment2 = anglePerSegment;
                    numSegments2 = numSegments;
                    double e2y = (cy + ((eta1 * sinTheta) * cosEta2)) + ((b * cosTheta) * sinEta2);
                    double ep2x = (((-eta1) * cosTheta) * sinEta2) - ((b * sinTheta) * cosEta2);
                    int i3 = i2;
                    double e2x = (cx + ((eta1 * cosTheta) * cosEta2)) - ((b * sinTheta) * sinEta2);
                    double ep2y = (((-eta1) * sinTheta) * sinEta2) + ((b * cosTheta) * cosEta2);
                    double tanDiff2 = Math.tan((eta2 - eta12) / 2.0d);
                    double alpha = (Math.sin(eta2 - eta12) * (Math.sqrt(((tanDiff2 * 3.0d) * tanDiff2) + 4.0d) - 1.0d)) / 3.0d;
                    eta1 = e1x2 + (alpha * ep1x);
                    double q1y = e1y2 + (alpha * ep1y);
                    eta12 = e2x - (alpha * ep2x);
                    cosTheta2 = cosTheta;
                    cosTheta = e2y - (alpha * ep2y);
                    sinTheta2 = sinTheta;
                    p.rLineTo(0.0f, 0.0f);
                    q1y = e2x;
                    double e2y2 = e2y;
                    p.cubicTo((float) eta1, (float) q1y, (float) eta12, (float) cosTheta, (float) q1y, (float) e2y2);
                    e1x2 = q1y;
                    e1y2 = e2y2;
                    ep1x = ep2x;
                    ep1y = ep2y;
                    i = i3 + 1;
                    eta12 = eta2;
                    anglePerSegment = anglePerSegment2;
                    numSegments = numSegments2;
                    cosTheta = cosTheta2;
                    sinTheta = sinTheta2;
                    eta1 = a;
                } else {
                    numSegments2 = numSegments;
                    double d = eta12;
                    cosTheta2 = cosTheta;
                    sinTheta2 = sinTheta;
                    sinTheta = p;
                    return;
                }
            }
        }
    }

    static float[] copyOfRange(float[] original, int start, int end) {
        if (start <= end) {
            int originalLength = original.length;
            if (start < 0 || start > originalLength) {
                throw new ArrayIndexOutOfBoundsException();
            }
            int resultLength = end - start;
            float[] result = new float[resultLength];
            System.arraycopy(original, start, result, 0, Math.min(resultLength, originalLength - start));
            return result;
        }
        throw new IllegalArgumentException();
    }

    public static Path createPathFromPathData(String pathData) {
        Path path = new Path();
        PathDataNode[] nodes = createNodesFromPathData(pathData);
        if (nodes == null) {
            return null;
        }
        try {
            PathDataNode.nodesToPath(nodes, path);
            return path;
        } catch (RuntimeException e) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Error in parsing ");
            stringBuilder.append(pathData);
            throw new RuntimeException(stringBuilder.toString(), e);
        }
    }

    public static PathDataNode[] createNodesFromPathData(String pathData) {
        if (pathData == null) {
            return null;
        }
        int start = 0;
        int end = 1;
        ArrayList<PathDataNode> list = new ArrayList();
        while (end < pathData.length()) {
            end = nextStart(pathData, end);
            String s = pathData.substring(start, end).trim();
            if (s.length() > 0) {
                addNode(list, s.charAt(0), getFloats(s));
            }
            start = end;
            end++;
        }
        if (end - start == 1 && start < pathData.length()) {
            addNode(list, pathData.charAt(start), new float[0]);
        }
        return (PathDataNode[]) list.toArray(new PathDataNode[list.size()]);
    }

    public static PathDataNode[] deepCopyNodes(PathDataNode[] source) {
        if (source == null) {
            return null;
        }
        PathDataNode[] copy = new PathDataNode[source.length];
        for (int i = 0; i < source.length; i++) {
            copy[i] = new PathDataNode(source[i]);
        }
        return copy;
    }

    /* JADX WARNING: Missing block: B:17:0x002c, code:
            return false;
     */
    public static boolean canMorph(android.support.v4.graphics.PathParser.PathDataNode[] r4, android.support.v4.graphics.PathParser.PathDataNode[] r5) {
        /*
        r0 = 0;
        if (r4 == 0) goto L_0x002c;
    L_0x0003:
        if (r5 != 0) goto L_0x0006;
    L_0x0005:
        goto L_0x002c;
    L_0x0006:
        r1 = r4.length;
        r2 = r5.length;
        if (r1 == r2) goto L_0x000b;
    L_0x000a:
        return r0;
    L_0x000b:
        r1 = 0;
    L_0x000c:
        r2 = r4.length;
        if (r1 >= r2) goto L_0x002a;
    L_0x000f:
        r2 = r4[r1];
        r2 = r2.mType;
        r3 = r5[r1];
        r3 = r3.mType;
        if (r2 != r3) goto L_0x0029;
    L_0x0019:
        r2 = r4[r1];
        r2 = r2.mParams;
        r2 = r2.length;
        r3 = r5[r1];
        r3 = r3.mParams;
        r3 = r3.length;
        if (r2 == r3) goto L_0x0026;
    L_0x0025:
        goto L_0x0029;
    L_0x0026:
        r1 = r1 + 1;
        goto L_0x000c;
    L_0x0029:
        return r0;
    L_0x002a:
        r0 = 1;
        return r0;
    L_0x002c:
        return r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.support.v4.graphics.PathParser.canMorph(android.support.v4.graphics.PathParser$PathDataNode[], android.support.v4.graphics.PathParser$PathDataNode[]):boolean");
    }

    public static void updateNodes(PathDataNode[] target, PathDataNode[] source) {
        for (int i = 0; i < source.length; i++) {
            target[i].mType = source[i].mType;
            for (int j = 0; j < source[i].mParams.length; j++) {
                target[i].mParams[j] = source[i].mParams[j];
            }
        }
    }

    private static int nextStart(String s, int end) {
        while (end < s.length()) {
            char c = s.charAt(end);
            if (((c - 65) * (c - 90) <= 0 || (c - 97) * (c - 122) <= 0) && c != 'e' && c != 'E') {
                return end;
            }
            end++;
        }
        return end;
    }

    private static void addNode(ArrayList<PathDataNode> list, char cmd, float[] val) {
        list.add(new PathDataNode(cmd, val));
    }

    private static float[] getFloats(String s) {
        if (s.charAt(0) == 'z' || s.charAt(0) == 'Z') {
            return new float[0];
        }
        try {
            float[] results = new float[s.length()];
            int count = 0;
            int startPosition = 1;
            ExtractFloatResult result = new ExtractFloatResult();
            int totalLength = s.length();
            while (startPosition < totalLength) {
                extract(s, startPosition, result);
                int endPosition = result.mEndPosition;
                if (startPosition < endPosition) {
                    int count2 = count + 1;
                    results[count] = Float.parseFloat(s.substring(startPosition, endPosition));
                    count = count2;
                }
                if (result.mEndWithNegOrDot) {
                    startPosition = endPosition;
                } else {
                    startPosition = endPosition + 1;
                }
            }
            return copyOfRange(results, 0, count);
        } catch (NumberFormatException e) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("error in parsing \"");
            stringBuilder.append(s);
            stringBuilder.append("\"");
            throw new RuntimeException(stringBuilder.toString(), e);
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:20:0x003b A:{LOOP_END, LOOP:0: B:1:0x0007->B:20:0x003b} */
    /* JADX WARNING: Removed duplicated region for block: B:24:0x003e A:{SYNTHETIC} */
    private static void extract(java.lang.String r7, int r8, android.support.v4.graphics.PathParser.ExtractFloatResult r9) {
        /*
        r0 = r8;
        r1 = 0;
        r2 = 0;
        r9.mEndWithNegOrDot = r2;
        r3 = 0;
    L_0x0007:
        r4 = r7.length();
        if (r0 >= r4) goto L_0x003e;
    L_0x000d:
        r4 = r2;
        r2 = 0;
        r5 = r7.charAt(r0);
        r6 = 32;
        if (r5 == r6) goto L_0x0036;
    L_0x0017:
        r6 = 69;
        if (r5 == r6) goto L_0x0034;
    L_0x001b:
        r6 = 101; // 0x65 float:1.42E-43 double:5.0E-322;
        if (r5 == r6) goto L_0x0034;
    L_0x001f:
        r6 = 1;
        switch(r5) {
            case 44: goto L_0x0036;
            case 45: goto L_0x002c;
            case 46: goto L_0x0024;
            default: goto L_0x0023;
        };
    L_0x0023:
        goto L_0x0038;
    L_0x0024:
        if (r3 != 0) goto L_0x0028;
    L_0x0026:
        r3 = 1;
        goto L_0x0038;
    L_0x0028:
        r1 = 1;
        r9.mEndWithNegOrDot = r6;
        goto L_0x0038;
    L_0x002c:
        if (r0 == r8) goto L_0x0038;
    L_0x002e:
        if (r4 != 0) goto L_0x0038;
    L_0x0030:
        r1 = 1;
        r9.mEndWithNegOrDot = r6;
        goto L_0x0038;
    L_0x0034:
        r2 = 1;
        goto L_0x0038;
    L_0x0036:
        r1 = 1;
    L_0x0038:
        if (r1 == 0) goto L_0x003b;
    L_0x003a:
        goto L_0x003e;
    L_0x003b:
        r0 = r0 + 1;
        goto L_0x0007;
    L_0x003e:
        r9.mEndPosition = r0;
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.support.v4.graphics.PathParser.extract(java.lang.String, int, android.support.v4.graphics.PathParser$ExtractFloatResult):void");
    }
}
