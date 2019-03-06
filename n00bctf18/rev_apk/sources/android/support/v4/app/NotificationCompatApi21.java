package android.support.v4.app;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompatBase.Action;
import android.support.v4.app.NotificationCompatBase.UnreadConversation;
import android.support.v4.app.NotificationCompatBase.UnreadConversation.Factory;
import android.support.v4.app.RemoteInputCompatBase.RemoteInput;
import android.widget.RemoteViews;
import java.util.ArrayList;
import java.util.Iterator;

@RequiresApi(21)
class NotificationCompatApi21 {
    private static final String KEY_AUTHOR = "author";
    private static final String KEY_MESSAGES = "messages";
    private static final String KEY_ON_READ = "on_read";
    private static final String KEY_ON_REPLY = "on_reply";
    private static final String KEY_PARTICIPANTS = "participants";
    private static final String KEY_REMOTE_INPUT = "remote_input";
    private static final String KEY_TEXT = "text";
    private static final String KEY_TIMESTAMP = "timestamp";

    public static class Builder implements NotificationBuilderWithBuilderAccessor, NotificationBuilderWithActions {
        private android.app.Notification.Builder b;
        private RemoteViews mBigContentView;
        private RemoteViews mContentView;
        private Bundle mExtras;
        private int mGroupAlertBehavior;
        private RemoteViews mHeadsUpContentView;

        public Builder(Context context, Notification n, CharSequence contentTitle, CharSequence contentText, CharSequence contentInfo, RemoteViews tickerView, int number, PendingIntent contentIntent, PendingIntent fullScreenIntent, Bitmap largeIcon, int progressMax, int progress, boolean progressIndeterminate, boolean showWhen, boolean useChronometer, int priority, CharSequence subText, boolean localOnly, String category, ArrayList<String> people, Bundle extras, int color, int visibility, Notification publicVersion, String groupKey, boolean groupSummary, String sortKey, RemoteViews contentView, RemoteViews bigContentView, RemoteViews headsUpContentView, int groupAlertBehavior) {
            Notification notification = n;
            Bundle bundle = extras;
            boolean z = false;
            android.app.Notification.Builder deleteIntent = new android.app.Notification.Builder(context).setWhen(notification.when).setShowWhen(showWhen).setSmallIcon(notification.icon, notification.iconLevel).setContent(notification.contentView).setTicker(notification.tickerText, tickerView).setSound(notification.sound, notification.audioStreamType).setVibrate(notification.vibrate).setLights(notification.ledARGB, notification.ledOnMS, notification.ledOffMS).setOngoing((notification.flags & 2) != 0).setOnlyAlertOnce((notification.flags & 8) != 0).setAutoCancel((notification.flags & 16) != 0).setDefaults(notification.defaults).setContentTitle(contentTitle).setContentText(contentText).setSubText(subText).setContentInfo(contentInfo).setContentIntent(contentIntent).setDeleteIntent(notification.deleteIntent);
            if ((notification.flags & 128) != 0) {
                z = true;
            }
            this.b = deleteIntent.setFullScreenIntent(fullScreenIntent, z).setLargeIcon(largeIcon).setNumber(number).setUsesChronometer(useChronometer).setPriority(priority).setProgress(progressMax, progress, progressIndeterminate).setLocalOnly(localOnly).setGroup(groupKey).setGroupSummary(groupSummary).setSortKey(sortKey).setCategory(category).setColor(color).setVisibility(visibility).setPublicVersion(publicVersion);
            this.mExtras = new Bundle();
            if (bundle != null) {
                this.mExtras.putAll(bundle);
            }
            Iterator it = people.iterator();
            while (it.hasNext()) {
                this.b.addPerson((String) it.next());
                notification = publicVersion;
                bundle = extras;
            }
            this.mContentView = contentView;
            this.mBigContentView = bigContentView;
            this.mHeadsUpContentView = headsUpContentView;
            this.mGroupAlertBehavior = groupAlertBehavior;
        }

        public void addAction(Action action) {
            NotificationCompatApi20.addAction(this.b, action);
        }

        public android.app.Notification.Builder getBuilder() {
            return this.b;
        }

        public Notification build() {
            this.b.setExtras(this.mExtras);
            Notification notification = this.b.build();
            if (this.mContentView != null) {
                notification.contentView = this.mContentView;
            }
            if (this.mBigContentView != null) {
                notification.bigContentView = this.mBigContentView;
            }
            if (this.mHeadsUpContentView != null) {
                notification.headsUpContentView = this.mHeadsUpContentView;
            }
            if (this.mGroupAlertBehavior != 0) {
                if (!(notification.getGroup() == null || (notification.flags & 512) == 0 || this.mGroupAlertBehavior != 2)) {
                    removeSoundAndVibration(notification);
                }
                if (notification.getGroup() != null && (notification.flags & 512) == 0 && this.mGroupAlertBehavior == 1) {
                    removeSoundAndVibration(notification);
                }
            }
            return notification;
        }

        private void removeSoundAndVibration(Notification notification) {
            notification.sound = null;
            notification.vibrate = null;
            notification.defaults &= -2;
            notification.defaults &= -3;
        }
    }

    NotificationCompatApi21() {
    }

    static Bundle getBundleForUnreadConversation(UnreadConversation uc) {
        if (uc == null) {
            return null;
        }
        Bundle b = new Bundle();
        String author = null;
        int i = 0;
        if (uc.getParticipants() != null && uc.getParticipants().length > 1) {
            author = uc.getParticipants()[0];
        }
        Parcelable[] messages = new Parcelable[uc.getMessages().length];
        while (i < messages.length) {
            Bundle m = new Bundle();
            m.putString(KEY_TEXT, uc.getMessages()[i]);
            m.putString(KEY_AUTHOR, author);
            messages[i] = m;
            i++;
        }
        b.putParcelableArray(KEY_MESSAGES, messages);
        RemoteInput remoteInput = uc.getRemoteInput();
        if (remoteInput != null) {
            b.putParcelable(KEY_REMOTE_INPUT, fromCompatRemoteInput(remoteInput));
        }
        b.putParcelable(KEY_ON_REPLY, uc.getReplyPendingIntent());
        b.putParcelable(KEY_ON_READ, uc.getReadPendingIntent());
        b.putStringArray(KEY_PARTICIPANTS, uc.getParticipants());
        b.putLong(KEY_TIMESTAMP, uc.getLatestTimestamp());
        return b;
    }

    static UnreadConversation getUnreadConversationFromBundle(Bundle b, Factory factory, RemoteInput.Factory remoteInputFactory) {
        Bundle bundle = b;
        RemoteInput remoteInput = null;
        if (bundle == null) {
            return null;
        }
        Parcelable[] parcelableMessages = bundle.getParcelableArray(KEY_MESSAGES);
        String[] messages = null;
        if (parcelableMessages != null) {
            String[] tmp = new String[parcelableMessages.length];
            boolean success = true;
            for (int i = 0; i < tmp.length; i++) {
                if (!(parcelableMessages[i] instanceof Bundle)) {
                    success = false;
                    break;
                }
                tmp[i] = ((Bundle) parcelableMessages[i]).getString(KEY_TEXT);
                if (tmp[i] == null) {
                    success = false;
                    break;
                }
            }
            if (!success) {
                return null;
            }
            messages = tmp;
        }
        PendingIntent onRead = (PendingIntent) bundle.getParcelable(KEY_ON_READ);
        PendingIntent onReply = (PendingIntent) bundle.getParcelable(KEY_ON_REPLY);
        android.app.RemoteInput remoteInput2 = (android.app.RemoteInput) bundle.getParcelable(KEY_REMOTE_INPUT);
        String[] participants = bundle.getStringArray(KEY_PARTICIPANTS);
        if (participants == null || participants.length != 1) {
            return null;
        }
        if (remoteInput2 != null) {
            remoteInput = toCompatRemoteInput(remoteInput2, remoteInputFactory);
        } else {
            RemoteInput.Factory factory2 = remoteInputFactory;
        }
        return factory.build(messages, remoteInput, onReply, onRead, participants, bundle.getLong(KEY_TIMESTAMP));
    }

    private static android.app.RemoteInput fromCompatRemoteInput(RemoteInput src) {
        return new android.app.RemoteInput.Builder(src.getResultKey()).setLabel(src.getLabel()).setChoices(src.getChoices()).setAllowFreeFormInput(src.getAllowFreeFormInput()).addExtras(src.getExtras()).build();
    }

    private static RemoteInput toCompatRemoteInput(android.app.RemoteInput remoteInput, RemoteInput.Factory factory) {
        return factory.build(remoteInput.getResultKey(), remoteInput.getLabel(), remoteInput.getChoices(), remoteInput.getAllowFreeFormInput(), remoteInput.getExtras(), null);
    }
}
