package android.support.v4.media;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.BadParcelableException;
import android.os.Binder;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.os.RemoteException;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.annotation.RestrictTo;
import android.support.annotation.RestrictTo.Scope;
import android.support.v4.app.BundleCompat;
import android.support.v4.media.session.IMediaSession;
import android.support.v4.media.session.IMediaSession.Stub;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.MediaSessionCompat.Token;
import android.support.v4.os.ResultReceiver;
import android.support.v4.util.ArrayMap;
import android.text.TextUtils;
import android.util.Log;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map.Entry;

public final class MediaBrowserCompat {
    public static final String CUSTOM_ACTION_DOWNLOAD = "android.support.v4.media.action.DOWNLOAD";
    public static final String CUSTOM_ACTION_REMOVE_DOWNLOADED_FILE = "android.support.v4.media.action.REMOVE_DOWNLOADED_FILE";
    static final boolean DEBUG = Log.isLoggable(TAG, 3);
    public static final String EXTRA_DOWNLOAD_PROGRESS = "android.media.browse.extra.DOWNLOAD_PROGRESS";
    public static final String EXTRA_MEDIA_ID = "android.media.browse.extra.MEDIA_ID";
    public static final String EXTRA_PAGE = "android.media.browse.extra.PAGE";
    public static final String EXTRA_PAGE_SIZE = "android.media.browse.extra.PAGE_SIZE";
    static final String TAG = "MediaBrowserCompat";
    private final MediaBrowserImpl mImpl;

    private static class CallbackHandler extends Handler {
        private final WeakReference<MediaBrowserServiceCallbackImpl> mCallbackImplRef;
        private WeakReference<Messenger> mCallbacksMessengerRef;

        CallbackHandler(MediaBrowserServiceCallbackImpl callbackImpl) {
            this.mCallbackImplRef = new WeakReference(callbackImpl);
        }

        public void handleMessage(Message msg) {
            if (this.mCallbacksMessengerRef != null && this.mCallbacksMessengerRef.get() != null && this.mCallbackImplRef.get() != null) {
                Bundle data = msg.getData();
                data.setClassLoader(MediaSessionCompat.class.getClassLoader());
                MediaBrowserServiceCallbackImpl serviceCallback = (MediaBrowserServiceCallbackImpl) this.mCallbackImplRef.get();
                Messenger callbacksMessenger = (Messenger) this.mCallbacksMessengerRef.get();
                try {
                    switch (msg.what) {
                        case 1:
                            serviceCallback.onServiceConnected(callbacksMessenger, data.getString(MediaBrowserProtocol.DATA_MEDIA_ITEM_ID), (Token) data.getParcelable(MediaBrowserProtocol.DATA_MEDIA_SESSION_TOKEN), data.getBundle(MediaBrowserProtocol.DATA_ROOT_HINTS));
                            break;
                        case 2:
                            serviceCallback.onConnectionFailed(callbacksMessenger);
                            break;
                        case 3:
                            serviceCallback.onLoadChildren(callbacksMessenger, data.getString(MediaBrowserProtocol.DATA_MEDIA_ITEM_ID), data.getParcelableArrayList(MediaBrowserProtocol.DATA_MEDIA_ITEM_LIST), data.getBundle(MediaBrowserProtocol.DATA_OPTIONS));
                            break;
                        default:
                            String str = MediaBrowserCompat.TAG;
                            StringBuilder stringBuilder = new StringBuilder();
                            stringBuilder.append("Unhandled message: ");
                            stringBuilder.append(msg);
                            stringBuilder.append("\n  Client version: ");
                            stringBuilder.append(1);
                            stringBuilder.append("\n  Service version: ");
                            stringBuilder.append(msg.arg1);
                            Log.w(str, stringBuilder.toString());
                            break;
                    }
                } catch (BadParcelableException e) {
                    Log.e(MediaBrowserCompat.TAG, "Could not unparcel the data.");
                    if (msg.what == 1) {
                        serviceCallback.onConnectionFailed(callbacksMessenger);
                    }
                }
            }
        }

        void setCallbacksMessenger(Messenger callbacksMessenger) {
            this.mCallbacksMessengerRef = new WeakReference(callbacksMessenger);
        }
    }

    public static class ConnectionCallback {
        ConnectionCallbackInternal mConnectionCallbackInternal;
        final Object mConnectionCallbackObj;

        interface ConnectionCallbackInternal {
            void onConnected();

            void onConnectionFailed();

            void onConnectionSuspended();
        }

        private class StubApi21 implements ConnectionCallback {
            StubApi21() {
            }

            public void onConnected() {
                if (ConnectionCallback.this.mConnectionCallbackInternal != null) {
                    ConnectionCallback.this.mConnectionCallbackInternal.onConnected();
                }
                ConnectionCallback.this.onConnected();
            }

            public void onConnectionSuspended() {
                if (ConnectionCallback.this.mConnectionCallbackInternal != null) {
                    ConnectionCallback.this.mConnectionCallbackInternal.onConnectionSuspended();
                }
                ConnectionCallback.this.onConnectionSuspended();
            }

            public void onConnectionFailed() {
                if (ConnectionCallback.this.mConnectionCallbackInternal != null) {
                    ConnectionCallback.this.mConnectionCallbackInternal.onConnectionFailed();
                }
                ConnectionCallback.this.onConnectionFailed();
            }
        }

        public ConnectionCallback() {
            if (VERSION.SDK_INT >= 21) {
                this.mConnectionCallbackObj = MediaBrowserCompatApi21.createConnectionCallback(new StubApi21());
            } else {
                this.mConnectionCallbackObj = null;
            }
        }

        public void onConnected() {
        }

        public void onConnectionSuspended() {
        }

        public void onConnectionFailed() {
        }

        void setInternalConnectionCallback(ConnectionCallbackInternal connectionCallbackInternal) {
            this.mConnectionCallbackInternal = connectionCallbackInternal;
        }
    }

    public static abstract class CustomActionCallback {
        public void onProgressUpdate(String action, Bundle extras, Bundle data) {
        }

        public void onResult(String action, Bundle extras, Bundle resultData) {
        }

        public void onError(String action, Bundle extras, Bundle data) {
        }
    }

    public static abstract class ItemCallback {
        final Object mItemCallbackObj;

        private class StubApi23 implements ItemCallback {
            StubApi23() {
            }

            public void onItemLoaded(Parcel itemParcel) {
                if (itemParcel == null) {
                    ItemCallback.this.onItemLoaded(null);
                    return;
                }
                itemParcel.setDataPosition(0);
                MediaItem item = (MediaItem) MediaItem.CREATOR.createFromParcel(itemParcel);
                itemParcel.recycle();
                ItemCallback.this.onItemLoaded(item);
            }

            public void onError(@NonNull String itemId) {
                ItemCallback.this.onError(itemId);
            }
        }

        public ItemCallback() {
            if (VERSION.SDK_INT >= 23) {
                this.mItemCallbackObj = MediaBrowserCompatApi23.createItemCallback(new StubApi23());
            } else {
                this.mItemCallbackObj = null;
            }
        }

        public void onItemLoaded(MediaItem item) {
        }

        public void onError(@NonNull String itemId) {
        }
    }

    interface MediaBrowserImpl {
        void connect();

        void disconnect();

        @Nullable
        Bundle getExtras();

        void getItem(@NonNull String str, @NonNull ItemCallback itemCallback);

        @NonNull
        String getRoot();

        ComponentName getServiceComponent();

        @NonNull
        Token getSessionToken();

        boolean isConnected();

        void search(@NonNull String str, Bundle bundle, @NonNull SearchCallback searchCallback);

        void sendCustomAction(@NonNull String str, Bundle bundle, @Nullable CustomActionCallback customActionCallback);

        void subscribe(@NonNull String str, Bundle bundle, @NonNull SubscriptionCallback subscriptionCallback);

        void unsubscribe(@NonNull String str, SubscriptionCallback subscriptionCallback);
    }

    interface MediaBrowserServiceCallbackImpl {
        void onConnectionFailed(Messenger messenger);

        void onLoadChildren(Messenger messenger, String str, List list, Bundle bundle);

        void onServiceConnected(Messenger messenger, String str, Token token, Bundle bundle);
    }

    public static class MediaItem implements Parcelable {
        public static final Creator<MediaItem> CREATOR = new Creator<MediaItem>() {
            public MediaItem createFromParcel(Parcel in) {
                return new MediaItem(in);
            }

            public MediaItem[] newArray(int size) {
                return new MediaItem[size];
            }
        };
        public static final int FLAG_BROWSABLE = 1;
        public static final int FLAG_PLAYABLE = 2;
        private final MediaDescriptionCompat mDescription;
        private final int mFlags;

        @RestrictTo({Scope.LIBRARY_GROUP})
        @Retention(RetentionPolicy.SOURCE)
        public @interface Flags {
        }

        public static MediaItem fromMediaItem(Object itemObj) {
            if (itemObj == null || VERSION.SDK_INT < 21) {
                return null;
            }
            return new MediaItem(MediaDescriptionCompat.fromMediaDescription(MediaItem.getDescription(itemObj)), MediaItem.getFlags(itemObj));
        }

        public static List<MediaItem> fromMediaItemList(List<?> itemList) {
            if (itemList == null || VERSION.SDK_INT < 21) {
                return null;
            }
            List<MediaItem> items = new ArrayList(itemList.size());
            for (Object itemObj : itemList) {
                items.add(fromMediaItem(itemObj));
            }
            return items;
        }

        public MediaItem(@NonNull MediaDescriptionCompat description, int flags) {
            if (description == null) {
                throw new IllegalArgumentException("description cannot be null");
            } else if (TextUtils.isEmpty(description.getMediaId())) {
                throw new IllegalArgumentException("description must have a non-empty media id");
            } else {
                this.mFlags = flags;
                this.mDescription = description;
            }
        }

        MediaItem(Parcel in) {
            this.mFlags = in.readInt();
            this.mDescription = (MediaDescriptionCompat) MediaDescriptionCompat.CREATOR.createFromParcel(in);
        }

        public int describeContents() {
            return 0;
        }

        public void writeToParcel(Parcel out, int flags) {
            out.writeInt(this.mFlags);
            this.mDescription.writeToParcel(out, flags);
        }

        public String toString() {
            StringBuilder sb = new StringBuilder("MediaItem{");
            sb.append("mFlags=");
            sb.append(this.mFlags);
            sb.append(", mDescription=");
            sb.append(this.mDescription);
            sb.append('}');
            return sb.toString();
        }

        public int getFlags() {
            return this.mFlags;
        }

        public boolean isBrowsable() {
            return (this.mFlags & 1) != 0;
        }

        public boolean isPlayable() {
            return (this.mFlags & 2) != 0;
        }

        @NonNull
        public MediaDescriptionCompat getDescription() {
            return this.mDescription;
        }

        @Nullable
        public String getMediaId() {
            return this.mDescription.getMediaId();
        }
    }

    public static abstract class SearchCallback {
        public void onSearchResult(@NonNull String query, Bundle extras, @NonNull List<MediaItem> list) {
        }

        public void onError(@NonNull String query, Bundle extras) {
        }
    }

    private static class ServiceBinderWrapper {
        private Messenger mMessenger;
        private Bundle mRootHints;

        public ServiceBinderWrapper(IBinder target, Bundle rootHints) {
            this.mMessenger = new Messenger(target);
            this.mRootHints = rootHints;
        }

        void connect(Context context, Messenger callbacksMessenger) throws RemoteException {
            Bundle data = new Bundle();
            data.putString(MediaBrowserProtocol.DATA_PACKAGE_NAME, context.getPackageName());
            data.putBundle(MediaBrowserProtocol.DATA_ROOT_HINTS, this.mRootHints);
            sendRequest(1, data, callbacksMessenger);
        }

        void disconnect(Messenger callbacksMessenger) throws RemoteException {
            sendRequest(2, null, callbacksMessenger);
        }

        void addSubscription(String parentId, IBinder callbackToken, Bundle options, Messenger callbacksMessenger) throws RemoteException {
            Bundle data = new Bundle();
            data.putString(MediaBrowserProtocol.DATA_MEDIA_ITEM_ID, parentId);
            BundleCompat.putBinder(data, MediaBrowserProtocol.DATA_CALLBACK_TOKEN, callbackToken);
            data.putBundle(MediaBrowserProtocol.DATA_OPTIONS, options);
            sendRequest(3, data, callbacksMessenger);
        }

        void removeSubscription(String parentId, IBinder callbackToken, Messenger callbacksMessenger) throws RemoteException {
            Bundle data = new Bundle();
            data.putString(MediaBrowserProtocol.DATA_MEDIA_ITEM_ID, parentId);
            BundleCompat.putBinder(data, MediaBrowserProtocol.DATA_CALLBACK_TOKEN, callbackToken);
            sendRequest(4, data, callbacksMessenger);
        }

        void getMediaItem(String mediaId, ResultReceiver receiver, Messenger callbacksMessenger) throws RemoteException {
            Bundle data = new Bundle();
            data.putString(MediaBrowserProtocol.DATA_MEDIA_ITEM_ID, mediaId);
            data.putParcelable(MediaBrowserProtocol.DATA_RESULT_RECEIVER, receiver);
            sendRequest(5, data, callbacksMessenger);
        }

        void registerCallbackMessenger(Messenger callbackMessenger) throws RemoteException {
            Bundle data = new Bundle();
            data.putBundle(MediaBrowserProtocol.DATA_ROOT_HINTS, this.mRootHints);
            sendRequest(6, data, callbackMessenger);
        }

        void unregisterCallbackMessenger(Messenger callbackMessenger) throws RemoteException {
            sendRequest(7, null, callbackMessenger);
        }

        void search(String query, Bundle extras, ResultReceiver receiver, Messenger callbacksMessenger) throws RemoteException {
            Bundle data = new Bundle();
            data.putString(MediaBrowserProtocol.DATA_SEARCH_QUERY, query);
            data.putBundle(MediaBrowserProtocol.DATA_SEARCH_EXTRAS, extras);
            data.putParcelable(MediaBrowserProtocol.DATA_RESULT_RECEIVER, receiver);
            sendRequest(8, data, callbacksMessenger);
        }

        void sendCustomAction(String action, Bundle extras, ResultReceiver receiver, Messenger callbacksMessenger) throws RemoteException {
            Bundle data = new Bundle();
            data.putString(MediaBrowserProtocol.DATA_CUSTOM_ACTION, action);
            data.putBundle(MediaBrowserProtocol.DATA_CUSTOM_ACTION_EXTRAS, extras);
            data.putParcelable(MediaBrowserProtocol.DATA_RESULT_RECEIVER, receiver);
            sendRequest(9, data, callbacksMessenger);
        }

        private void sendRequest(int what, Bundle data, Messenger cbMessenger) throws RemoteException {
            Message msg = Message.obtain();
            msg.what = what;
            msg.arg1 = 1;
            msg.setData(data);
            msg.replyTo = cbMessenger;
            this.mMessenger.send(msg);
        }
    }

    private static class Subscription {
        private final List<SubscriptionCallback> mCallbacks = new ArrayList();
        private final List<Bundle> mOptionsList = new ArrayList();

        public boolean isEmpty() {
            return this.mCallbacks.isEmpty();
        }

        public List<Bundle> getOptionsList() {
            return this.mOptionsList;
        }

        public List<SubscriptionCallback> getCallbacks() {
            return this.mCallbacks;
        }

        public SubscriptionCallback getCallback(Context context, Bundle options) {
            if (options != null) {
                options.setClassLoader(context.getClassLoader());
            }
            for (int i = 0; i < this.mOptionsList.size(); i++) {
                if (MediaBrowserCompatUtils.areSameOptions((Bundle) this.mOptionsList.get(i), options)) {
                    return (SubscriptionCallback) this.mCallbacks.get(i);
                }
            }
            return null;
        }

        public void putCallback(Context context, Bundle options, SubscriptionCallback callback) {
            if (options != null) {
                options.setClassLoader(context.getClassLoader());
            }
            for (int i = 0; i < this.mOptionsList.size(); i++) {
                if (MediaBrowserCompatUtils.areSameOptions((Bundle) this.mOptionsList.get(i), options)) {
                    this.mCallbacks.set(i, callback);
                    return;
                }
            }
            this.mCallbacks.add(callback);
            this.mOptionsList.add(options);
        }
    }

    public static abstract class SubscriptionCallback {
        private final Object mSubscriptionCallbackObj;
        WeakReference<Subscription> mSubscriptionRef;
        private final IBinder mToken;

        private class StubApi21 implements SubscriptionCallback {
            StubApi21() {
            }

            public void onChildrenLoaded(@NonNull String parentId, List<?> children) {
                Subscription sub = SubscriptionCallback.this.mSubscriptionRef == null ? null : (Subscription) SubscriptionCallback.this.mSubscriptionRef.get();
                if (sub == null) {
                    SubscriptionCallback.this.onChildrenLoaded(parentId, MediaItem.fromMediaItemList(children));
                    return;
                }
                List<MediaItem> itemList = MediaItem.fromMediaItemList(children);
                List<SubscriptionCallback> callbacks = sub.getCallbacks();
                List<Bundle> optionsList = sub.getOptionsList();
                for (int i = 0; i < callbacks.size(); i++) {
                    Bundle options = (Bundle) optionsList.get(i);
                    if (options == null) {
                        SubscriptionCallback.this.onChildrenLoaded(parentId, itemList);
                    } else {
                        SubscriptionCallback.this.onChildrenLoaded(parentId, applyOptions(itemList, options), options);
                    }
                }
            }

            public void onError(@NonNull String parentId) {
                SubscriptionCallback.this.onError(parentId);
            }

            List<MediaItem> applyOptions(List<MediaItem> list, Bundle options) {
                if (list == null) {
                    return null;
                }
                int page = options.getInt(MediaBrowserCompat.EXTRA_PAGE, -1);
                int pageSize = options.getInt(MediaBrowserCompat.EXTRA_PAGE_SIZE, -1);
                if (page == -1 && pageSize == -1) {
                    return list;
                }
                int fromIndex = pageSize * page;
                int toIndex = fromIndex + pageSize;
                if (page < 0 || pageSize < 1 || fromIndex >= list.size()) {
                    return Collections.EMPTY_LIST;
                }
                if (toIndex > list.size()) {
                    toIndex = list.size();
                }
                return list.subList(fromIndex, toIndex);
            }
        }

        private class StubApi24 extends StubApi21 implements SubscriptionCallback {
            StubApi24() {
                super();
            }

            public void onChildrenLoaded(@NonNull String parentId, List<?> children, @NonNull Bundle options) {
                SubscriptionCallback.this.onChildrenLoaded(parentId, MediaItem.fromMediaItemList(children), options);
            }

            public void onError(@NonNull String parentId, @NonNull Bundle options) {
                SubscriptionCallback.this.onError(parentId, options);
            }
        }

        public SubscriptionCallback() {
            if (VERSION.SDK_INT >= 26) {
                this.mSubscriptionCallbackObj = MediaBrowserCompatApi24.createSubscriptionCallback(new StubApi24());
                this.mToken = null;
            } else if (VERSION.SDK_INT >= 21) {
                this.mSubscriptionCallbackObj = MediaBrowserCompatApi21.createSubscriptionCallback(new StubApi21());
                this.mToken = new Binder();
            } else {
                this.mSubscriptionCallbackObj = null;
                this.mToken = new Binder();
            }
        }

        public void onChildrenLoaded(@NonNull String parentId, @NonNull List<MediaItem> list) {
        }

        public void onChildrenLoaded(@NonNull String parentId, @NonNull List<MediaItem> list, @NonNull Bundle options) {
        }

        public void onError(@NonNull String parentId) {
        }

        public void onError(@NonNull String parentId, @NonNull Bundle options) {
        }

        private void setSubscription(Subscription subscription) {
            this.mSubscriptionRef = new WeakReference(subscription);
        }
    }

    private static class CustomActionResultReceiver extends ResultReceiver {
        private final String mAction;
        private final CustomActionCallback mCallback;
        private final Bundle mExtras;

        CustomActionResultReceiver(String action, Bundle extras, CustomActionCallback callback, Handler handler) {
            super(handler);
            this.mAction = action;
            this.mExtras = extras;
            this.mCallback = callback;
        }

        protected void onReceiveResult(int resultCode, Bundle resultData) {
            if (this.mCallback != null) {
                switch (resultCode) {
                    case -1:
                        this.mCallback.onError(this.mAction, this.mExtras, resultData);
                        break;
                    case 0:
                        this.mCallback.onResult(this.mAction, this.mExtras, resultData);
                        break;
                    case 1:
                        this.mCallback.onProgressUpdate(this.mAction, this.mExtras, resultData);
                        break;
                    default:
                        String str = MediaBrowserCompat.TAG;
                        StringBuilder stringBuilder = new StringBuilder();
                        stringBuilder.append("Unknown result code: ");
                        stringBuilder.append(resultCode);
                        stringBuilder.append(" (extras=");
                        stringBuilder.append(this.mExtras);
                        stringBuilder.append(", resultData=");
                        stringBuilder.append(resultData);
                        stringBuilder.append(")");
                        Log.w(str, stringBuilder.toString());
                        break;
                }
            }
        }
    }

    private static class ItemReceiver extends ResultReceiver {
        private final ItemCallback mCallback;
        private final String mMediaId;

        ItemReceiver(String mediaId, ItemCallback callback, Handler handler) {
            super(handler);
            this.mMediaId = mediaId;
            this.mCallback = callback;
        }

        protected void onReceiveResult(int resultCode, Bundle resultData) {
            if (resultData != null) {
                resultData.setClassLoader(MediaBrowserCompat.class.getClassLoader());
            }
            if (resultCode == 0 && resultData != null && resultData.containsKey(MediaBrowserServiceCompat.KEY_MEDIA_ITEM)) {
                Parcelable item = resultData.getParcelable(MediaBrowserServiceCompat.KEY_MEDIA_ITEM);
                if (item == null || (item instanceof MediaItem)) {
                    this.mCallback.onItemLoaded((MediaItem) item);
                } else {
                    this.mCallback.onError(this.mMediaId);
                }
                return;
            }
            this.mCallback.onError(this.mMediaId);
        }
    }

    @RequiresApi(21)
    static class MediaBrowserImplApi21 implements MediaBrowserImpl, MediaBrowserServiceCallbackImpl, ConnectionCallbackInternal {
        protected final Object mBrowserObj;
        protected Messenger mCallbacksMessenger;
        final Context mContext;
        protected final CallbackHandler mHandler = new CallbackHandler(this);
        private Token mMediaSessionToken;
        protected final Bundle mRootHints;
        protected ServiceBinderWrapper mServiceBinderWrapper;
        private final ArrayMap<String, Subscription> mSubscriptions = new ArrayMap();

        public MediaBrowserImplApi21(Context context, ComponentName serviceComponent, ConnectionCallback callback, Bundle rootHints) {
            this.mContext = context;
            if (rootHints == null) {
                rootHints = new Bundle();
            }
            rootHints.putInt(MediaBrowserProtocol.EXTRA_CLIENT_VERSION, 1);
            this.mRootHints = new Bundle(rootHints);
            callback.setInternalConnectionCallback(this);
            this.mBrowserObj = MediaBrowserCompatApi21.createBrowser(context, serviceComponent, callback.mConnectionCallbackObj, this.mRootHints);
        }

        public void connect() {
            MediaBrowserCompatApi21.connect(this.mBrowserObj);
        }

        public void disconnect() {
            if (!(this.mServiceBinderWrapper == null || this.mCallbacksMessenger == null)) {
                try {
                    this.mServiceBinderWrapper.unregisterCallbackMessenger(this.mCallbacksMessenger);
                } catch (RemoteException e) {
                    Log.i(MediaBrowserCompat.TAG, "Remote error unregistering client messenger.");
                }
            }
            MediaBrowserCompatApi21.disconnect(this.mBrowserObj);
        }

        public boolean isConnected() {
            return MediaBrowserCompatApi21.isConnected(this.mBrowserObj);
        }

        public ComponentName getServiceComponent() {
            return MediaBrowserCompatApi21.getServiceComponent(this.mBrowserObj);
        }

        @NonNull
        public String getRoot() {
            return MediaBrowserCompatApi21.getRoot(this.mBrowserObj);
        }

        @Nullable
        public Bundle getExtras() {
            return MediaBrowserCompatApi21.getExtras(this.mBrowserObj);
        }

        @NonNull
        public Token getSessionToken() {
            if (this.mMediaSessionToken == null) {
                this.mMediaSessionToken = Token.fromToken(MediaBrowserCompatApi21.getSessionToken(this.mBrowserObj));
            }
            return this.mMediaSessionToken;
        }

        public void subscribe(@NonNull String parentId, Bundle options, @NonNull SubscriptionCallback callback) {
            Subscription sub = (Subscription) this.mSubscriptions.get(parentId);
            if (sub == null) {
                sub = new Subscription();
                this.mSubscriptions.put(parentId, sub);
            }
            callback.setSubscription(sub);
            Bundle copiedOptions = options == null ? null : new Bundle(options);
            sub.putCallback(this.mContext, copiedOptions, callback);
            if (this.mServiceBinderWrapper == null) {
                MediaBrowserCompatApi21.subscribe(this.mBrowserObj, parentId, callback.mSubscriptionCallbackObj);
                return;
            }
            try {
                this.mServiceBinderWrapper.addSubscription(parentId, callback.mToken, copiedOptions, this.mCallbacksMessenger);
            } catch (RemoteException e) {
                String str = MediaBrowserCompat.TAG;
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Remote error subscribing media item: ");
                stringBuilder.append(parentId);
                Log.i(str, stringBuilder.toString());
            }
        }

        public void unsubscribe(@NonNull String parentId, SubscriptionCallback callback) {
            Subscription sub = (Subscription) this.mSubscriptions.get(parentId);
            if (sub != null) {
                List<Bundle> optionsList;
                int i;
                if (this.mServiceBinderWrapper == null) {
                    if (callback == null) {
                        MediaBrowserCompatApi21.unsubscribe(this.mBrowserObj, parentId);
                    } else {
                        List<SubscriptionCallback> callbacks = sub.getCallbacks();
                        optionsList = sub.getOptionsList();
                        for (i = callbacks.size() - 1; i >= 0; i--) {
                            if (callbacks.get(i) == callback) {
                                callbacks.remove(i);
                                optionsList.remove(i);
                            }
                        }
                        if (callbacks.size() == 0) {
                            MediaBrowserCompatApi21.unsubscribe(this.mBrowserObj, parentId);
                        }
                    }
                } else if (callback == null) {
                    try {
                        this.mServiceBinderWrapper.removeSubscription(parentId, null, this.mCallbacksMessenger);
                    } catch (RemoteException e) {
                        String str = MediaBrowserCompat.TAG;
                        StringBuilder stringBuilder = new StringBuilder();
                        stringBuilder.append("removeSubscription failed with RemoteException parentId=");
                        stringBuilder.append(parentId);
                        Log.d(str, stringBuilder.toString());
                    }
                } else {
                    RemoteException ex = sub.getCallbacks();
                    optionsList = sub.getOptionsList();
                    for (i = ex.size() - 1; i >= 0; i--) {
                        if (ex.get(i) == callback) {
                            this.mServiceBinderWrapper.removeSubscription(parentId, callback.mToken, this.mCallbacksMessenger);
                            ex.remove(i);
                            optionsList.remove(i);
                        }
                    }
                }
                if (sub.isEmpty() || callback == null) {
                    this.mSubscriptions.remove(parentId);
                }
            }
        }

        public void getItem(@NonNull final String mediaId, @NonNull final ItemCallback cb) {
            if (TextUtils.isEmpty(mediaId)) {
                throw new IllegalArgumentException("mediaId is empty");
            } else if (cb == null) {
                throw new IllegalArgumentException("cb is null");
            } else if (!MediaBrowserCompatApi21.isConnected(this.mBrowserObj)) {
                Log.i(MediaBrowserCompat.TAG, "Not connected, unable to retrieve the MediaItem.");
                this.mHandler.post(new Runnable() {
                    public void run() {
                        cb.onError(mediaId);
                    }
                });
            } else if (this.mServiceBinderWrapper == null) {
                this.mHandler.post(new Runnable() {
                    public void run() {
                        cb.onError(mediaId);
                    }
                });
            } else {
                try {
                    this.mServiceBinderWrapper.getMediaItem(mediaId, new ItemReceiver(mediaId, cb, this.mHandler), this.mCallbacksMessenger);
                } catch (RemoteException e) {
                    String str = MediaBrowserCompat.TAG;
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("Remote error getting media item: ");
                    stringBuilder.append(mediaId);
                    Log.i(str, stringBuilder.toString());
                    this.mHandler.post(new Runnable() {
                        public void run() {
                            cb.onError(mediaId);
                        }
                    });
                }
            }
        }

        public void search(@NonNull final String query, final Bundle extras, @NonNull final SearchCallback callback) {
            if (!isConnected()) {
                throw new IllegalStateException("search() called while not connected");
            } else if (this.mServiceBinderWrapper == null) {
                Log.i(MediaBrowserCompat.TAG, "The connected service doesn't support search.");
                this.mHandler.post(new Runnable() {
                    public void run() {
                        callback.onError(query, extras);
                    }
                });
            } else {
                try {
                    this.mServiceBinderWrapper.search(query, extras, new SearchResultReceiver(query, extras, callback, this.mHandler), this.mCallbacksMessenger);
                } catch (RemoteException e) {
                    String str = MediaBrowserCompat.TAG;
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("Remote error searching items with query: ");
                    stringBuilder.append(query);
                    Log.i(str, stringBuilder.toString(), e);
                    this.mHandler.post(new Runnable() {
                        public void run() {
                            callback.onError(query, extras);
                        }
                    });
                }
            }
        }

        public void sendCustomAction(@NonNull final String action, final Bundle extras, @Nullable final CustomActionCallback callback) {
            if (isConnected()) {
                if (this.mServiceBinderWrapper == null) {
                    Log.i(MediaBrowserCompat.TAG, "The connected service doesn't support sendCustomAction.");
                    if (callback != null) {
                        this.mHandler.post(new Runnable() {
                            public void run() {
                                callback.onError(action, extras, null);
                            }
                        });
                    }
                }
                try {
                    this.mServiceBinderWrapper.sendCustomAction(action, extras, new CustomActionResultReceiver(action, extras, callback, this.mHandler), this.mCallbacksMessenger);
                    return;
                } catch (RemoteException e) {
                    String str = MediaBrowserCompat.TAG;
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("Remote error sending a custom action: action=");
                    stringBuilder.append(action);
                    stringBuilder.append(", extras=");
                    stringBuilder.append(extras);
                    Log.i(str, stringBuilder.toString(), e);
                    if (callback != null) {
                        this.mHandler.post(new Runnable() {
                            public void run() {
                                callback.onError(action, extras, null);
                            }
                        });
                        return;
                    }
                    return;
                }
            }
            StringBuilder stringBuilder2 = new StringBuilder();
            stringBuilder2.append("Cannot send a custom action (");
            stringBuilder2.append(action);
            stringBuilder2.append(") with ");
            stringBuilder2.append("extras ");
            stringBuilder2.append(extras);
            stringBuilder2.append(" because the browser is not connected to the ");
            stringBuilder2.append("service.");
            throw new IllegalStateException(stringBuilder2.toString());
        }

        public void onConnected() {
            Bundle extras = MediaBrowserCompatApi21.getExtras(this.mBrowserObj);
            if (extras != null) {
                IBinder serviceBinder = BundleCompat.getBinder(extras, MediaBrowserProtocol.EXTRA_MESSENGER_BINDER);
                if (serviceBinder != null) {
                    this.mServiceBinderWrapper = new ServiceBinderWrapper(serviceBinder, this.mRootHints);
                    this.mCallbacksMessenger = new Messenger(this.mHandler);
                    this.mHandler.setCallbacksMessenger(this.mCallbacksMessenger);
                    try {
                        this.mServiceBinderWrapper.registerCallbackMessenger(this.mCallbacksMessenger);
                    } catch (RemoteException e) {
                        Log.i(MediaBrowserCompat.TAG, "Remote error registering client messenger.");
                    }
                }
                IMediaSession sessionToken = Stub.asInterface(BundleCompat.getBinder(extras, MediaBrowserProtocol.EXTRA_SESSION_BINDER));
                if (sessionToken != null) {
                    this.mMediaSessionToken = Token.fromToken(MediaBrowserCompatApi21.getSessionToken(this.mBrowserObj), sessionToken);
                }
            }
        }

        public void onConnectionSuspended() {
            this.mServiceBinderWrapper = null;
            this.mCallbacksMessenger = null;
            this.mMediaSessionToken = null;
            this.mHandler.setCallbacksMessenger(null);
        }

        public void onConnectionFailed() {
        }

        public void onServiceConnected(Messenger callback, String root, Token session, Bundle extra) {
        }

        public void onConnectionFailed(Messenger callback) {
        }

        public void onLoadChildren(Messenger callback, String parentId, List list, Bundle options) {
            if (this.mCallbacksMessenger == callback) {
                Subscription subscription = (Subscription) this.mSubscriptions.get(parentId);
                if (subscription == null) {
                    if (MediaBrowserCompat.DEBUG) {
                        String str = MediaBrowserCompat.TAG;
                        StringBuilder stringBuilder = new StringBuilder();
                        stringBuilder.append("onLoadChildren for id that isn't subscribed id=");
                        stringBuilder.append(parentId);
                        Log.d(str, stringBuilder.toString());
                    }
                    return;
                }
                SubscriptionCallback subscriptionCallback = subscription.getCallback(this.mContext, options);
                if (subscriptionCallback != null) {
                    if (options == null) {
                        if (list == null) {
                            subscriptionCallback.onError(parentId);
                        } else {
                            subscriptionCallback.onChildrenLoaded(parentId, list);
                        }
                    } else if (list == null) {
                        subscriptionCallback.onError(parentId, options);
                    } else {
                        subscriptionCallback.onChildrenLoaded(parentId, list, options);
                    }
                }
            }
        }
    }

    static class MediaBrowserImplBase implements MediaBrowserImpl, MediaBrowserServiceCallbackImpl {
        static final int CONNECT_STATE_CONNECTED = 3;
        static final int CONNECT_STATE_CONNECTING = 2;
        static final int CONNECT_STATE_DISCONNECTED = 1;
        static final int CONNECT_STATE_DISCONNECTING = 0;
        static final int CONNECT_STATE_SUSPENDED = 4;
        final ConnectionCallback mCallback;
        Messenger mCallbacksMessenger;
        final Context mContext;
        private Bundle mExtras;
        final CallbackHandler mHandler = new CallbackHandler(this);
        private Token mMediaSessionToken;
        final Bundle mRootHints;
        private String mRootId;
        ServiceBinderWrapper mServiceBinderWrapper;
        final ComponentName mServiceComponent;
        MediaServiceConnection mServiceConnection;
        int mState = 1;
        private final ArrayMap<String, Subscription> mSubscriptions = new ArrayMap();

        private class MediaServiceConnection implements ServiceConnection {
            MediaServiceConnection() {
            }

            public void onServiceConnected(final ComponentName name, final IBinder binder) {
                postOrRun(new Runnable() {
                    public void run() {
                        if (MediaBrowserCompat.DEBUG) {
                            String str = MediaBrowserCompat.TAG;
                            StringBuilder stringBuilder = new StringBuilder();
                            stringBuilder.append("MediaServiceConnection.onServiceConnected name=");
                            stringBuilder.append(name);
                            stringBuilder.append(" binder=");
                            stringBuilder.append(binder);
                            Log.d(str, stringBuilder.toString());
                            MediaBrowserImplBase.this.dump();
                        }
                        if (MediaServiceConnection.this.isCurrent("onServiceConnected")) {
                            MediaBrowserImplBase.this.mServiceBinderWrapper = new ServiceBinderWrapper(binder, MediaBrowserImplBase.this.mRootHints);
                            MediaBrowserImplBase.this.mCallbacksMessenger = new Messenger(MediaBrowserImplBase.this.mHandler);
                            MediaBrowserImplBase.this.mHandler.setCallbacksMessenger(MediaBrowserImplBase.this.mCallbacksMessenger);
                            MediaBrowserImplBase.this.mState = 2;
                            try {
                                if (MediaBrowserCompat.DEBUG) {
                                    Log.d(MediaBrowserCompat.TAG, "ServiceCallbacks.onConnect...");
                                    MediaBrowserImplBase.this.dump();
                                }
                                MediaBrowserImplBase.this.mServiceBinderWrapper.connect(MediaBrowserImplBase.this.mContext, MediaBrowserImplBase.this.mCallbacksMessenger);
                            } catch (RemoteException e) {
                                String str2 = MediaBrowserCompat.TAG;
                                StringBuilder stringBuilder2 = new StringBuilder();
                                stringBuilder2.append("RemoteException during connect for ");
                                stringBuilder2.append(MediaBrowserImplBase.this.mServiceComponent);
                                Log.w(str2, stringBuilder2.toString());
                                if (MediaBrowserCompat.DEBUG) {
                                    Log.d(MediaBrowserCompat.TAG, "ServiceCallbacks.onConnect...");
                                    MediaBrowserImplBase.this.dump();
                                }
                            }
                        }
                    }
                });
            }

            public void onServiceDisconnected(final ComponentName name) {
                postOrRun(new Runnable() {
                    public void run() {
                        if (MediaBrowserCompat.DEBUG) {
                            String str = MediaBrowserCompat.TAG;
                            StringBuilder stringBuilder = new StringBuilder();
                            stringBuilder.append("MediaServiceConnection.onServiceDisconnected name=");
                            stringBuilder.append(name);
                            stringBuilder.append(" this=");
                            stringBuilder.append(this);
                            stringBuilder.append(" mServiceConnection=");
                            stringBuilder.append(MediaBrowserImplBase.this.mServiceConnection);
                            Log.d(str, stringBuilder.toString());
                            MediaBrowserImplBase.this.dump();
                        }
                        if (MediaServiceConnection.this.isCurrent("onServiceDisconnected")) {
                            MediaBrowserImplBase.this.mServiceBinderWrapper = null;
                            MediaBrowserImplBase.this.mCallbacksMessenger = null;
                            MediaBrowserImplBase.this.mHandler.setCallbacksMessenger(null);
                            MediaBrowserImplBase.this.mState = 4;
                            MediaBrowserImplBase.this.mCallback.onConnectionSuspended();
                        }
                    }
                });
            }

            private void postOrRun(Runnable r) {
                if (Thread.currentThread() == MediaBrowserImplBase.this.mHandler.getLooper().getThread()) {
                    r.run();
                } else {
                    MediaBrowserImplBase.this.mHandler.post(r);
                }
            }

            boolean isCurrent(String funcName) {
                if (MediaBrowserImplBase.this.mServiceConnection == this && MediaBrowserImplBase.this.mState != 0 && MediaBrowserImplBase.this.mState != 1) {
                    return true;
                }
                if (!(MediaBrowserImplBase.this.mState == 0 || MediaBrowserImplBase.this.mState == 1)) {
                    String str = MediaBrowserCompat.TAG;
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append(funcName);
                    stringBuilder.append(" for ");
                    stringBuilder.append(MediaBrowserImplBase.this.mServiceComponent);
                    stringBuilder.append(" with mServiceConnection=");
                    stringBuilder.append(MediaBrowserImplBase.this.mServiceConnection);
                    stringBuilder.append(" this=");
                    stringBuilder.append(this);
                    Log.i(str, stringBuilder.toString());
                }
                return false;
            }
        }

        public MediaBrowserImplBase(Context context, ComponentName serviceComponent, ConnectionCallback callback, Bundle rootHints) {
            if (context == null) {
                throw new IllegalArgumentException("context must not be null");
            } else if (serviceComponent == null) {
                throw new IllegalArgumentException("service component must not be null");
            } else if (callback != null) {
                this.mContext = context;
                this.mServiceComponent = serviceComponent;
                this.mCallback = callback;
                this.mRootHints = rootHints == null ? null : new Bundle(rootHints);
            } else {
                throw new IllegalArgumentException("connection callback must not be null");
            }
        }

        public void connect() {
            if (this.mState == 0 || this.mState == 1) {
                this.mState = 2;
                this.mHandler.post(new Runnable() {
                    public void run() {
                        if (MediaBrowserImplBase.this.mState != 0) {
                            MediaBrowserImplBase.this.mState = 2;
                            StringBuilder stringBuilder;
                            if (MediaBrowserCompat.DEBUG && MediaBrowserImplBase.this.mServiceConnection != null) {
                                stringBuilder = new StringBuilder();
                                stringBuilder.append("mServiceConnection should be null. Instead it is ");
                                stringBuilder.append(MediaBrowserImplBase.this.mServiceConnection);
                                throw new RuntimeException(stringBuilder.toString());
                            } else if (MediaBrowserImplBase.this.mServiceBinderWrapper != null) {
                                stringBuilder = new StringBuilder();
                                stringBuilder.append("mServiceBinderWrapper should be null. Instead it is ");
                                stringBuilder.append(MediaBrowserImplBase.this.mServiceBinderWrapper);
                                throw new RuntimeException(stringBuilder.toString());
                            } else if (MediaBrowserImplBase.this.mCallbacksMessenger == null) {
                                Intent intent = new Intent(MediaBrowserServiceCompat.SERVICE_INTERFACE);
                                intent.setComponent(MediaBrowserImplBase.this.mServiceComponent);
                                MediaBrowserImplBase.this.mServiceConnection = new MediaServiceConnection();
                                boolean bound = false;
                                try {
                                    bound = MediaBrowserImplBase.this.mContext.bindService(intent, MediaBrowserImplBase.this.mServiceConnection, 1);
                                } catch (Exception e) {
                                    String str = MediaBrowserCompat.TAG;
                                    StringBuilder stringBuilder2 = new StringBuilder();
                                    stringBuilder2.append("Failed binding to service ");
                                    stringBuilder2.append(MediaBrowserImplBase.this.mServiceComponent);
                                    Log.e(str, stringBuilder2.toString());
                                }
                                if (!bound) {
                                    MediaBrowserImplBase.this.forceCloseConnection();
                                    MediaBrowserImplBase.this.mCallback.onConnectionFailed();
                                }
                                if (MediaBrowserCompat.DEBUG) {
                                    Log.d(MediaBrowserCompat.TAG, "connect...");
                                    MediaBrowserImplBase.this.dump();
                                }
                            } else {
                                stringBuilder = new StringBuilder();
                                stringBuilder.append("mCallbacksMessenger should be null. Instead it is ");
                                stringBuilder.append(MediaBrowserImplBase.this.mCallbacksMessenger);
                                throw new RuntimeException(stringBuilder.toString());
                            }
                        }
                    }
                });
                return;
            }
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("connect() called while neigther disconnecting nor disconnected (state=");
            stringBuilder.append(getStateLabel(this.mState));
            stringBuilder.append(")");
            throw new IllegalStateException(stringBuilder.toString());
        }

        public void disconnect() {
            this.mState = 0;
            this.mHandler.post(new Runnable() {
                public void run() {
                    if (MediaBrowserImplBase.this.mCallbacksMessenger != null) {
                        try {
                            MediaBrowserImplBase.this.mServiceBinderWrapper.disconnect(MediaBrowserImplBase.this.mCallbacksMessenger);
                        } catch (RemoteException e) {
                            String str = MediaBrowserCompat.TAG;
                            StringBuilder stringBuilder = new StringBuilder();
                            stringBuilder.append("RemoteException during connect for ");
                            stringBuilder.append(MediaBrowserImplBase.this.mServiceComponent);
                            Log.w(str, stringBuilder.toString());
                        }
                    }
                    int state = MediaBrowserImplBase.this.mState;
                    MediaBrowserImplBase.this.forceCloseConnection();
                    if (state != 0) {
                        MediaBrowserImplBase.this.mState = state;
                    }
                    if (MediaBrowserCompat.DEBUG) {
                        Log.d(MediaBrowserCompat.TAG, "disconnect...");
                        MediaBrowserImplBase.this.dump();
                    }
                }
            });
        }

        void forceCloseConnection() {
            if (this.mServiceConnection != null) {
                this.mContext.unbindService(this.mServiceConnection);
            }
            this.mState = 1;
            this.mServiceConnection = null;
            this.mServiceBinderWrapper = null;
            this.mCallbacksMessenger = null;
            this.mHandler.setCallbacksMessenger(null);
            this.mRootId = null;
            this.mMediaSessionToken = null;
        }

        public boolean isConnected() {
            return this.mState == 3;
        }

        @NonNull
        public ComponentName getServiceComponent() {
            if (isConnected()) {
                return this.mServiceComponent;
            }
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("getServiceComponent() called while not connected (state=");
            stringBuilder.append(this.mState);
            stringBuilder.append(")");
            throw new IllegalStateException(stringBuilder.toString());
        }

        @NonNull
        public String getRoot() {
            if (isConnected()) {
                return this.mRootId;
            }
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("getRoot() called while not connected(state=");
            stringBuilder.append(getStateLabel(this.mState));
            stringBuilder.append(")");
            throw new IllegalStateException(stringBuilder.toString());
        }

        @Nullable
        public Bundle getExtras() {
            if (isConnected()) {
                return this.mExtras;
            }
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("getExtras() called while not connected (state=");
            stringBuilder.append(getStateLabel(this.mState));
            stringBuilder.append(")");
            throw new IllegalStateException(stringBuilder.toString());
        }

        @NonNull
        public Token getSessionToken() {
            if (isConnected()) {
                return this.mMediaSessionToken;
            }
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("getSessionToken() called while not connected(state=");
            stringBuilder.append(this.mState);
            stringBuilder.append(")");
            throw new IllegalStateException(stringBuilder.toString());
        }

        public void subscribe(@NonNull String parentId, Bundle options, @NonNull SubscriptionCallback callback) {
            Subscription sub = (Subscription) this.mSubscriptions.get(parentId);
            if (sub == null) {
                sub = new Subscription();
                this.mSubscriptions.put(parentId, sub);
            }
            Bundle copiedOptions = options == null ? null : new Bundle(options);
            sub.putCallback(this.mContext, copiedOptions, callback);
            if (isConnected()) {
                try {
                    this.mServiceBinderWrapper.addSubscription(parentId, callback.mToken, copiedOptions, this.mCallbacksMessenger);
                } catch (RemoteException e) {
                    String str = MediaBrowserCompat.TAG;
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("addSubscription failed with RemoteException parentId=");
                    stringBuilder.append(parentId);
                    Log.d(str, stringBuilder.toString());
                }
            }
        }

        public void unsubscribe(@NonNull String parentId, SubscriptionCallback callback) {
            Subscription sub = (Subscription) this.mSubscriptions.get(parentId);
            if (sub != null) {
                if (callback == null) {
                    try {
                        if (isConnected()) {
                            this.mServiceBinderWrapper.removeSubscription(parentId, null, this.mCallbacksMessenger);
                        }
                    } catch (RemoteException e) {
                        String str = MediaBrowserCompat.TAG;
                        StringBuilder stringBuilder = new StringBuilder();
                        stringBuilder.append("removeSubscription failed with RemoteException parentId=");
                        stringBuilder.append(parentId);
                        Log.d(str, stringBuilder.toString());
                    }
                } else {
                    RemoteException ex = sub.getCallbacks();
                    List<Bundle> optionsList = sub.getOptionsList();
                    for (int i = ex.size() - 1; i >= 0; i--) {
                        if (ex.get(i) == callback) {
                            if (isConnected()) {
                                this.mServiceBinderWrapper.removeSubscription(parentId, callback.mToken, this.mCallbacksMessenger);
                            }
                            ex.remove(i);
                            optionsList.remove(i);
                        }
                    }
                }
                if (sub.isEmpty() || callback == null) {
                    this.mSubscriptions.remove(parentId);
                }
            }
        }

        public void getItem(@NonNull final String mediaId, @NonNull final ItemCallback cb) {
            if (TextUtils.isEmpty(mediaId)) {
                throw new IllegalArgumentException("mediaId is empty");
            } else if (cb == null) {
                throw new IllegalArgumentException("cb is null");
            } else if (isConnected()) {
                try {
                    this.mServiceBinderWrapper.getMediaItem(mediaId, new ItemReceiver(mediaId, cb, this.mHandler), this.mCallbacksMessenger);
                } catch (RemoteException e) {
                    String str = MediaBrowserCompat.TAG;
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("Remote error getting media item: ");
                    stringBuilder.append(mediaId);
                    Log.i(str, stringBuilder.toString());
                    this.mHandler.post(new Runnable() {
                        public void run() {
                            cb.onError(mediaId);
                        }
                    });
                }
            } else {
                Log.i(MediaBrowserCompat.TAG, "Not connected, unable to retrieve the MediaItem.");
                this.mHandler.post(new Runnable() {
                    public void run() {
                        cb.onError(mediaId);
                    }
                });
            }
        }

        public void search(@NonNull final String query, final Bundle extras, @NonNull final SearchCallback callback) {
            if (isConnected()) {
                try {
                    this.mServiceBinderWrapper.search(query, extras, new SearchResultReceiver(query, extras, callback, this.mHandler), this.mCallbacksMessenger);
                    return;
                } catch (RemoteException e) {
                    String str = MediaBrowserCompat.TAG;
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("Remote error searching items with query: ");
                    stringBuilder.append(query);
                    Log.i(str, stringBuilder.toString(), e);
                    this.mHandler.post(new Runnable() {
                        public void run() {
                            callback.onError(query, extras);
                        }
                    });
                    return;
                }
            }
            StringBuilder stringBuilder2 = new StringBuilder();
            stringBuilder2.append("search() called while not connected (state=");
            stringBuilder2.append(getStateLabel(this.mState));
            stringBuilder2.append(")");
            throw new IllegalStateException(stringBuilder2.toString());
        }

        public void sendCustomAction(@NonNull final String action, final Bundle extras, @Nullable final CustomActionCallback callback) {
            if (isConnected()) {
                try {
                    this.mServiceBinderWrapper.sendCustomAction(action, extras, new CustomActionResultReceiver(action, extras, callback, this.mHandler), this.mCallbacksMessenger);
                    return;
                } catch (RemoteException e) {
                    String str = MediaBrowserCompat.TAG;
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("Remote error sending a custom action: action=");
                    stringBuilder.append(action);
                    stringBuilder.append(", extras=");
                    stringBuilder.append(extras);
                    Log.i(str, stringBuilder.toString(), e);
                    if (callback != null) {
                        this.mHandler.post(new Runnable() {
                            public void run() {
                                callback.onError(action, extras, null);
                            }
                        });
                        return;
                    }
                    return;
                }
            }
            StringBuilder stringBuilder2 = new StringBuilder();
            stringBuilder2.append("Cannot send a custom action (");
            stringBuilder2.append(action);
            stringBuilder2.append(") with ");
            stringBuilder2.append("extras ");
            stringBuilder2.append(extras);
            stringBuilder2.append(" because the browser is not connected to the ");
            stringBuilder2.append("service.");
            throw new IllegalStateException(stringBuilder2.toString());
        }

        public void onServiceConnected(Messenger callback, String root, Token session, Bundle extra) {
            if (!isCurrent(callback, "onConnect")) {
                return;
            }
            if (this.mState != 2) {
                String str = MediaBrowserCompat.TAG;
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("onConnect from service while mState=");
                stringBuilder.append(getStateLabel(this.mState));
                stringBuilder.append("... ignoring");
                Log.w(str, stringBuilder.toString());
                return;
            }
            this.mRootId = root;
            this.mMediaSessionToken = session;
            this.mExtras = extra;
            this.mState = 3;
            if (MediaBrowserCompat.DEBUG) {
                Log.d(MediaBrowserCompat.TAG, "ServiceCallbacks.onConnect...");
                dump();
            }
            this.mCallback.onConnected();
            try {
                for (Entry<String, Subscription> subscriptionEntry : this.mSubscriptions.entrySet()) {
                    String id = (String) subscriptionEntry.getKey();
                    Subscription sub = (Subscription) subscriptionEntry.getValue();
                    List<SubscriptionCallback> callbackList = sub.getCallbacks();
                    List<Bundle> optionsList = sub.getOptionsList();
                    for (int i = 0; i < callbackList.size(); i++) {
                        this.mServiceBinderWrapper.addSubscription(id, ((SubscriptionCallback) callbackList.get(i)).mToken, (Bundle) optionsList.get(i), this.mCallbacksMessenger);
                    }
                }
            } catch (RemoteException e) {
                Log.d(MediaBrowserCompat.TAG, "addSubscription failed with RemoteException.");
            }
        }

        public void onConnectionFailed(Messenger callback) {
            String str = MediaBrowserCompat.TAG;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("onConnectFailed for ");
            stringBuilder.append(this.mServiceComponent);
            Log.e(str, stringBuilder.toString());
            if (!isCurrent(callback, "onConnectFailed")) {
                return;
            }
            if (this.mState != 2) {
                str = MediaBrowserCompat.TAG;
                stringBuilder = new StringBuilder();
                stringBuilder.append("onConnect from service while mState=");
                stringBuilder.append(getStateLabel(this.mState));
                stringBuilder.append("... ignoring");
                Log.w(str, stringBuilder.toString());
                return;
            }
            forceCloseConnection();
            this.mCallback.onConnectionFailed();
        }

        public void onLoadChildren(Messenger callback, String parentId, List list, Bundle options) {
            if (isCurrent(callback, "onLoadChildren")) {
                if (MediaBrowserCompat.DEBUG) {
                    String str = MediaBrowserCompat.TAG;
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("onLoadChildren for ");
                    stringBuilder.append(this.mServiceComponent);
                    stringBuilder.append(" id=");
                    stringBuilder.append(parentId);
                    Log.d(str, stringBuilder.toString());
                }
                Subscription subscription = (Subscription) this.mSubscriptions.get(parentId);
                if (subscription == null) {
                    if (MediaBrowserCompat.DEBUG) {
                        String str2 = MediaBrowserCompat.TAG;
                        StringBuilder stringBuilder2 = new StringBuilder();
                        stringBuilder2.append("onLoadChildren for id that isn't subscribed id=");
                        stringBuilder2.append(parentId);
                        Log.d(str2, stringBuilder2.toString());
                    }
                    return;
                }
                SubscriptionCallback subscriptionCallback = subscription.getCallback(this.mContext, options);
                if (subscriptionCallback != null) {
                    if (options == null) {
                        if (list == null) {
                            subscriptionCallback.onError(parentId);
                        } else {
                            subscriptionCallback.onChildrenLoaded(parentId, list);
                        }
                    } else if (list == null) {
                        subscriptionCallback.onError(parentId, options);
                    } else {
                        subscriptionCallback.onChildrenLoaded(parentId, list, options);
                    }
                }
            }
        }

        private static String getStateLabel(int state) {
            switch (state) {
                case 0:
                    return "CONNECT_STATE_DISCONNECTING";
                case 1:
                    return "CONNECT_STATE_DISCONNECTED";
                case 2:
                    return "CONNECT_STATE_CONNECTING";
                case 3:
                    return "CONNECT_STATE_CONNECTED";
                case 4:
                    return "CONNECT_STATE_SUSPENDED";
                default:
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("UNKNOWN/");
                    stringBuilder.append(state);
                    return stringBuilder.toString();
            }
        }

        private boolean isCurrent(Messenger callback, String funcName) {
            if (this.mCallbacksMessenger == callback && this.mState != 0 && this.mState != 1) {
                return true;
            }
            if (!(this.mState == 0 || this.mState == 1)) {
                String str = MediaBrowserCompat.TAG;
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(funcName);
                stringBuilder.append(" for ");
                stringBuilder.append(this.mServiceComponent);
                stringBuilder.append(" with mCallbacksMessenger=");
                stringBuilder.append(this.mCallbacksMessenger);
                stringBuilder.append(" this=");
                stringBuilder.append(this);
                Log.i(str, stringBuilder.toString());
            }
            return false;
        }

        void dump() {
            Log.d(MediaBrowserCompat.TAG, "MediaBrowserCompat...");
            String str = MediaBrowserCompat.TAG;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("  mServiceComponent=");
            stringBuilder.append(this.mServiceComponent);
            Log.d(str, stringBuilder.toString());
            str = MediaBrowserCompat.TAG;
            stringBuilder = new StringBuilder();
            stringBuilder.append("  mCallback=");
            stringBuilder.append(this.mCallback);
            Log.d(str, stringBuilder.toString());
            str = MediaBrowserCompat.TAG;
            stringBuilder = new StringBuilder();
            stringBuilder.append("  mRootHints=");
            stringBuilder.append(this.mRootHints);
            Log.d(str, stringBuilder.toString());
            str = MediaBrowserCompat.TAG;
            stringBuilder = new StringBuilder();
            stringBuilder.append("  mState=");
            stringBuilder.append(getStateLabel(this.mState));
            Log.d(str, stringBuilder.toString());
            str = MediaBrowserCompat.TAG;
            stringBuilder = new StringBuilder();
            stringBuilder.append("  mServiceConnection=");
            stringBuilder.append(this.mServiceConnection);
            Log.d(str, stringBuilder.toString());
            str = MediaBrowserCompat.TAG;
            stringBuilder = new StringBuilder();
            stringBuilder.append("  mServiceBinderWrapper=");
            stringBuilder.append(this.mServiceBinderWrapper);
            Log.d(str, stringBuilder.toString());
            str = MediaBrowserCompat.TAG;
            stringBuilder = new StringBuilder();
            stringBuilder.append("  mCallbacksMessenger=");
            stringBuilder.append(this.mCallbacksMessenger);
            Log.d(str, stringBuilder.toString());
            str = MediaBrowserCompat.TAG;
            stringBuilder = new StringBuilder();
            stringBuilder.append("  mRootId=");
            stringBuilder.append(this.mRootId);
            Log.d(str, stringBuilder.toString());
            str = MediaBrowserCompat.TAG;
            stringBuilder = new StringBuilder();
            stringBuilder.append("  mMediaSessionToken=");
            stringBuilder.append(this.mMediaSessionToken);
            Log.d(str, stringBuilder.toString());
        }
    }

    private static class SearchResultReceiver extends ResultReceiver {
        private final SearchCallback mCallback;
        private final Bundle mExtras;
        private final String mQuery;

        SearchResultReceiver(String query, Bundle extras, SearchCallback callback, Handler handler) {
            super(handler);
            this.mQuery = query;
            this.mExtras = extras;
            this.mCallback = callback;
        }

        protected void onReceiveResult(int resultCode, Bundle resultData) {
            if (resultData != null) {
                resultData.setClassLoader(MediaBrowserCompat.class.getClassLoader());
            }
            if (resultCode == 0 && resultData != null && resultData.containsKey(MediaBrowserServiceCompat.KEY_SEARCH_RESULTS)) {
                Parcelable[] items = resultData.getParcelableArray(MediaBrowserServiceCompat.KEY_SEARCH_RESULTS);
                List<MediaItem> results = null;
                if (items != null) {
                    results = new ArrayList();
                    for (Parcelable item : items) {
                        results.add((MediaItem) item);
                    }
                }
                this.mCallback.onSearchResult(this.mQuery, this.mExtras, results);
                return;
            }
            this.mCallback.onError(this.mQuery, this.mExtras);
        }
    }

    @RequiresApi(23)
    static class MediaBrowserImplApi23 extends MediaBrowserImplApi21 {
        public MediaBrowserImplApi23(Context context, ComponentName serviceComponent, ConnectionCallback callback, Bundle rootHints) {
            super(context, serviceComponent, callback, rootHints);
        }

        public void getItem(@NonNull String mediaId, @NonNull ItemCallback cb) {
            if (this.mServiceBinderWrapper == null) {
                MediaBrowserCompatApi23.getItem(this.mBrowserObj, mediaId, cb.mItemCallbackObj);
            } else {
                super.getItem(mediaId, cb);
            }
        }
    }

    @RequiresApi(26)
    static class MediaBrowserImplApi24 extends MediaBrowserImplApi23 {
        public MediaBrowserImplApi24(Context context, ComponentName serviceComponent, ConnectionCallback callback, Bundle rootHints) {
            super(context, serviceComponent, callback, rootHints);
        }

        public void subscribe(@NonNull String parentId, @NonNull Bundle options, @NonNull SubscriptionCallback callback) {
            if (options == null) {
                MediaBrowserCompatApi21.subscribe(this.mBrowserObj, parentId, callback.mSubscriptionCallbackObj);
            } else {
                MediaBrowserCompatApi24.subscribe(this.mBrowserObj, parentId, options, callback.mSubscriptionCallbackObj);
            }
        }

        public void unsubscribe(@NonNull String parentId, SubscriptionCallback callback) {
            if (callback == null) {
                MediaBrowserCompatApi21.unsubscribe(this.mBrowserObj, parentId);
            } else {
                MediaBrowserCompatApi24.unsubscribe(this.mBrowserObj, parentId, callback.mSubscriptionCallbackObj);
            }
        }
    }

    public MediaBrowserCompat(Context context, ComponentName serviceComponent, ConnectionCallback callback, Bundle rootHints) {
        if (VERSION.SDK_INT >= 26) {
            this.mImpl = new MediaBrowserImplApi24(context, serviceComponent, callback, rootHints);
        } else if (VERSION.SDK_INT >= 23) {
            this.mImpl = new MediaBrowserImplApi23(context, serviceComponent, callback, rootHints);
        } else if (VERSION.SDK_INT >= 21) {
            this.mImpl = new MediaBrowserImplApi21(context, serviceComponent, callback, rootHints);
        } else {
            this.mImpl = new MediaBrowserImplBase(context, serviceComponent, callback, rootHints);
        }
    }

    public void connect() {
        this.mImpl.connect();
    }

    public void disconnect() {
        this.mImpl.disconnect();
    }

    public boolean isConnected() {
        return this.mImpl.isConnected();
    }

    @NonNull
    public ComponentName getServiceComponent() {
        return this.mImpl.getServiceComponent();
    }

    @NonNull
    public String getRoot() {
        return this.mImpl.getRoot();
    }

    @Nullable
    public Bundle getExtras() {
        return this.mImpl.getExtras();
    }

    @NonNull
    public Token getSessionToken() {
        return this.mImpl.getSessionToken();
    }

    public void subscribe(@NonNull String parentId, @NonNull SubscriptionCallback callback) {
        if (TextUtils.isEmpty(parentId)) {
            throw new IllegalArgumentException("parentId is empty");
        } else if (callback != null) {
            this.mImpl.subscribe(parentId, null, callback);
        } else {
            throw new IllegalArgumentException("callback is null");
        }
    }

    public void subscribe(@NonNull String parentId, @NonNull Bundle options, @NonNull SubscriptionCallback callback) {
        if (TextUtils.isEmpty(parentId)) {
            throw new IllegalArgumentException("parentId is empty");
        } else if (callback == null) {
            throw new IllegalArgumentException("callback is null");
        } else if (options != null) {
            this.mImpl.subscribe(parentId, options, callback);
        } else {
            throw new IllegalArgumentException("options are null");
        }
    }

    public void unsubscribe(@NonNull String parentId) {
        if (TextUtils.isEmpty(parentId)) {
            throw new IllegalArgumentException("parentId is empty");
        }
        this.mImpl.unsubscribe(parentId, null);
    }

    public void unsubscribe(@NonNull String parentId, @NonNull SubscriptionCallback callback) {
        if (TextUtils.isEmpty(parentId)) {
            throw new IllegalArgumentException("parentId is empty");
        } else if (callback != null) {
            this.mImpl.unsubscribe(parentId, callback);
        } else {
            throw new IllegalArgumentException("callback is null");
        }
    }

    public void getItem(@NonNull String mediaId, @NonNull ItemCallback cb) {
        this.mImpl.getItem(mediaId, cb);
    }

    public void search(@NonNull String query, Bundle extras, @NonNull SearchCallback callback) {
        if (TextUtils.isEmpty(query)) {
            throw new IllegalArgumentException("query cannot be empty");
        } else if (callback != null) {
            this.mImpl.search(query, extras, callback);
        } else {
            throw new IllegalArgumentException("callback cannot be null");
        }
    }

    public void sendCustomAction(@NonNull String action, Bundle extras, @Nullable CustomActionCallback callback) {
        if (TextUtils.isEmpty(action)) {
            throw new IllegalArgumentException("action cannot be empty");
        }
        this.mImpl.sendCustomAction(action, extras, callback);
    }
}
