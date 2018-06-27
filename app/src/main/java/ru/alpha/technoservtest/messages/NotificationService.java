package ru.alpha.technoservtest.messages;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import ru.alpha.technoservtest.R;
import ru.alpha.technoservtest.messages.data.Message;
import ru.alpha.technoservtest.messages.interactor.MessagesInteractor;
import ru.alpha.technoservtest.messages.network.MessagesService;
import ru.alpha.technoservtest.messages.network.MessagesServiceProxy;

/**
 * Created by Aleksandr Sobol
 * asobol@golamago.com
 * on 26/06/2018.
 */
public class NotificationService extends Service {

    public static final String START_FOREGROUND_MESSAGES_SERVICE = "start";
    public static final String STOP_FOREGROUND_MESSAGES_SERVICE = "stop";
    public static final String MESSAGES_CHANNEL_ID = "messages_channel";

    public static final String MESSAGE_EXTRA = "message_extra";

    public static final int NOTIFICATION_ID = 100;

    private MessagesInteractor interactor;
    private NotificationManager notificationManager;
    private MessagesService messagesService;
    private Disposable messagesDisposable;

    public static void startService(Context context) {
        if (!isNotificationServiceRunning(context)) {
            Intent intent = new Intent(context, NotificationService.class);
            intent.setAction(START_FOREGROUND_MESSAGES_SERVICE);
            context.startService(intent);
        }
    }

    public static void stopService(Context context) {
        if (isNotificationServiceRunning(context)) {
            Intent intent = new Intent(context, NotificationService.class);
            intent.setAction(STOP_FOREGROUND_MESSAGES_SERVICE);
            context.startService(intent);
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String aciton = intent.getAction();

        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        NotificationChannel channel =
                new NotificationChannel(MESSAGES_CHANNEL_ID, "messages", NotificationManager.IMPORTANCE_DEFAULT);
        notificationManager.createNotificationChannel(channel);
        messagesService = new MessagesServiceProxy();
        interactor = MessagesInteractor.getMessagesInteractor(messagesService);

        if (START_FOREGROUND_MESSAGES_SERVICE.equals(aciton)) {
            if (messagesDisposable != null) {
                if (!messagesDisposable.isDisposed()) {
                    messagesDisposable.dispose();
                }
            }

            messagesDisposable = Observable.interval(2, TimeUnit.MINUTES)
                    .flatMap(unit -> interactor.getMessage())
                    .filter(message -> message.getStartDate() <= System.currentTimeMillis())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(message -> {
                        Notification notification = buildNotification(this, message);
                        Log.d(NotificationService.class.getSimpleName(), "message: " + message);
                        notificationManager.notify(message.getId(), notification);
                    }, error -> Log.e("MESSAGE_SERVICE", error.getLocalizedMessage(), error));

            startForeground(NOTIFICATION_ID, new Notification());
        } else if (STOP_FOREGROUND_MESSAGES_SERVICE.equals(aciton)) {
            stopForeground(true);
            stopSelf();
            if (messagesDisposable != null) {
                messagesDisposable.dispose();
            }
        }

        return Service.START_NOT_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private static boolean isNotificationServiceRunning(Context context) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (NotificationService.class.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    private Notification buildNotification(Context context, Message message) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, MESSAGES_CHANNEL_ID)
                .setContentTitle(message.getSubject())
                .setContentText(message.getText())
                .setSmallIcon(R.drawable.ic_markunread_mailbox)
                .setVisibility(Notification.VISIBILITY_PUBLIC)
                .setTimeoutAfter(message.getEndDate() - System.currentTimeMillis());

        Intent resultIntent = new Intent(this, MessagesActivity.class);
        resultIntent.putExtra(MESSAGE_EXTRA, message.getText());
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(MessagesActivity.class);
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        builder.setContentIntent(resultPendingIntent);
        return builder.build();
    }
}
