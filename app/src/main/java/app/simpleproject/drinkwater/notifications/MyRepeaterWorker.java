package app.simpleproject.drinkwater.notifications;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.AudioAttributes;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.preference.PreferenceManager;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import app.simpleproject.drinkwater.MainActivity;
import app.simpleproject.drinkwater.R;

import static app.simpleproject.drinkwater.MainActivity.sp;

// import static app.simpleproject.drinkwater.MainActivity.sp;

public class MyRepeaterWorker extends Worker {
    private static final String WORK_RESULT = "work_result";
    public static final String MESSAGE_STATUS = "MainActivity";
 //   public static final String CHANNEL_ID = "drinkWaterNotificationChannel";
 public static String CHANNEL_ID = "0";
    long timeDuration;
    String start = "10:15";
    String stop = "21:15";
    String timeNow;
    Calendar startTimeCalendar;
    Calendar stopTimeCalendar;
    Calendar nowTimeCalendar;
    SharedPreferences sp;
    Uri ringURI;
    Uri soundUri;

    public MyRepeaterWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        SharedPreferences.Editor editorChannel = sp.edit();
        editorChannel.putBoolean("recreateChannel", true);
        editorChannel.apply();
    }

    @NonNull
    @Override
    public Result doWork() {
        Data taskData = getInputData();
        String taskDataString = taskData.getString(MESSAGE_STATUS);

        // рандомим фразы для уведомлений
        // ЯЗЫКИ ИМ ДОБАВИТЬ РАЗНЫЕ
        String[] arrayHead = this.getApplicationContext().getResources().getStringArray(R.array.messagesHeadArray);
        String randomHead = arrayHead[new Random().nextInt(arrayHead.length)];
        String[] arrayBody = this.getApplicationContext().getResources().getStringArray(R.array.messagesBodyArray);
        String randomBody = arrayBody[new Random().nextInt(arrayBody.length)];

        //    showNotification(randomHead, taskDataString != null ? taskDataString : "Message has been Sent");

        // берём все параметры времени
        // устанавливаем отложку на столько, на сколько потребуется, чтоб попасть в окно времени
        checkTime();

        // если сейчас меньше или равно времени завершения - запускаем
        if ((nowTimeCalendar.getTimeInMillis() <= stopTimeCalendar.getTimeInMillis() && nowTimeCalendar.getTimeInMillis() >= startTimeCalendar.getTimeInMillis())) {
            showNotification(randomHead, randomBody);
        }
        Data outputData = new Data.Builder().putString(WORK_RESULT, "Jobs Finished").build();

        rerunWork();

        return Result.success(outputData);
    }

    private void showNotification(String randomHead, String randomBody) {
        Intent notificationIntent = new Intent(getApplicationContext(), MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(),
                0, notificationIntent, 0);


        /*
        switch (sp.getString("soundResId", "defaultsound")) {
            //    ringURI = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            //    builder.setSound(ringURI);
            case "silence":
                break;
            case "shortsound":
                soundUri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://"+ getApplicationContext().getPackageName() + "/" + R.raw.s2);
                break;
            case "longsound":
                soundUri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://"+ getApplicationContext().getPackageName() + "/" + R.raw.s2);
                break;
            default:
                soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                break;
        }
        */
        long[] vibrate = new long[]{1000, 500, 300, 500, 300, 500};
        NotificationManager manager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // если тру - пересоздаём канал, иначе в 8+ не обновятся параметры уведомлений типа звука и вибро
            if (sp.getBoolean("recreateChannel", false)){
                manager.deleteNotificationChannel(CHANNEL_ID);
                CHANNEL_ID=String.valueOf(Integer.parseInt(sp.getString("channel_id","0"))+1);
                SharedPreferences.Editor editorChannel = sp.edit();
                editorChannel.putString("channel_id", CHANNEL_ID);
                editorChannel.apply();
            }
            NotificationChannel channel = new
                    // NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_DEFAULT);
                    NotificationChannel(CHANNEL_ID, "DrinkWater Notification Channel", NotificationManager.IMPORTANCE_HIGH);

        //    channel.shouldVibrate();

            if (sp.getBoolean("switchVibro", false)) {
                channel.setVibrationPattern(vibrate);
        //        channel.shouldVibrate();
                channel.enableVibration(true);
            } else {
                vibrate = new long[]{0};
                channel.setVibrationPattern(vibrate);
                channel.enableVibration(true);
            }

            channel.setLightColor(Color.parseColor("#ffffffff"));
        //    channel.shouldShowLights();
            channel.enableLights(true);

            /*
            channel.setLightColor(Color.BLUE);
            channel.enableLights(true);
       //     channel.setDescription(Utils.CHANNEL_SIREN_DESCRIPTION);
            AudioAttributes audioAttributes = new AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                    .build();
            channel.setSound(soundUri, audioAttributes);
            */

            manager.createNotificationChannel(channel);
        }
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID)
                .setContentTitle(randomHead)
                .setContentText(randomBody)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentIntent(pendingIntent)
        //        .setLights(0xffffffff, 1, 0)
        //        .setDefaults(NotificationCompat.DEFAULT_SOUND)

                .setLights(Color.parseColor("#ffffffff"),500,2000)

                .setPriority(2)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC);

        if (!sp.getBoolean("switchVibro", false)) {
            vibrate = new long[]{0};
        }
        builder.setVibrate(vibrate);

        //     if (sp.getBoolean("switchVibro", false)) {
        //    long[] vibrate = new long[]{1000, 1000, 1000, 1000, 1000};
      //      builder.setVibrate(vibrate);
     //       builder.setDefaults(NotificationCompat.DEFAULT_VIBRATE | NotificationCompat.DEFAULT_SOUND);
    //    }

        /*
        switch (sp.getString("soundResId", "defaultsound")) {
        //    ringURI = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        //    builder.setSound(ringURI);
            case "silence":
                break;
            case "shortsound":
                builder.setSound(Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE+ "://" +getApplicationContext().getPackageName()+"/"+R.raw.s2));
                break;
            case "longsound":
                builder.setSound(Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE+ "://" +getApplicationContext().getPackageName()+"/"+R.raw.s3));
                break;
            default:
                if (sp.getBoolean("switchVibro", false)) {
                    builder.setDefaults(Notification.DEFAULT_SOUND |
                            Notification.DEFAULT_VIBRATE | Notification.DEFAULT_LIGHTS);
                } else {
                    builder.setDefaults(Notification.DEFAULT_SOUND);
                }
                break;
        }
        */

        manager.notify(1, builder.build());
    }

    private void rerunWork() {
        // к этому моменту время подсчитано актуальное
        // добавил getApplicationContext(), надеюсь хуже не будет
            OneTimeWorkRequest workRequest = new OneTimeWorkRequest.Builder(MyRepeaterWorker.class)
                    // здесь время между повторяущимися КАК БЫ
                    .setInitialDelay(timeDuration, TimeUnit.MILLISECONDS)
                    .addTag("timeRepeater")
                    .build();
        final WorkManager mWorkManager = WorkManager.getInstance(getApplicationContext());
            mWorkManager.enqueue(workRequest);
    }

    private void checkTime() {
        switch (sp.getString("listInterval", "twohour")) {
            case "halfhour":
                timeDuration = 30 * 60 * 1000;
                break;
            case "hour":
                timeDuration = 60 * 60 * 1000;
                break;
            case "twohour":
                timeDuration = 120 * 60 * 1000;
                break;
            case "fourhour":
                timeDuration = 240 * 60 * 1000;
                break;
            default:
                timeDuration = 60 * 60 * 1000;
                break;
        }

        start = sp.getString("TimePrefStart", "10:15");
        stop = sp.getString("TimePrefStop", "21:15");

        // Текущее время
        Date currentDate = new Date();
        // Форматирование времени как "часы:минуты"
        DateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
        timeNow = timeFormat.format(currentDate);
        Log.d("startTime", start);
        Log.d("stopTime", stop);
        Log.d("timeText", timeNow);

        int startTimeHours = Integer.parseInt(start.substring(0, start.indexOf(":")));
        int startTimeMinutes = Integer.parseInt(start.substring(start.indexOf(":") + 1));
        int stopTimeHours = Integer.parseInt(stop.substring(0, stop.indexOf(":")));
        int stopTimeMinutes = Integer.parseInt(stop.substring(stop.indexOf(":") + 1));

        int timeNowHours = Integer.parseInt(timeNow.substring(0, timeNow.indexOf(":")));
        int timeNowMinutes = Integer.parseInt(timeNow.substring(timeNow.indexOf(":") + 1));

        //     if (время сейчас больше или равно и меньше или равно){

        // задаём время 0 в миллисекундах, чтоб вычислить остальное
        // в момент выполнения уведомления вроверяем разницу между интервалом и временем завершения,
        // если СЕЙЧАС между началом и концом
        // затем если что добавляем время до следующего начала
        // для этого можно разницу между сейчас и начало вперёд посчитать как 24 часа минус разница между сейчас и начало назад
        Calendar dateAndTime = Calendar.getInstance();
        dateAndTime.set(Calendar.HOUR_OF_DAY, 0);
        dateAndTime.set(Calendar.MINUTE, 0);
        dateAndTime.set(Calendar.SECOND, 0);
        dateAndTime.set(Calendar.MILLISECOND, 0);
        dateAndTime.set(Calendar.DAY_OF_YEAR, 1);
        dateAndTime.set(Calendar.MONTH, 1);
        dateAndTime.set(Calendar.YEAR, 1970);
        // формат даты везде общий
    //    DateFormat df = new SimpleDateFormat("dd:MM:yyyy HH:mm:ss", Locale.US);
    //    String date = df.format(dateAndTime.getTimeInMillis());

        startTimeCalendar = Calendar.getInstance();
        startTimeCalendar.set(Calendar.HOUR_OF_DAY, startTimeHours);
        startTimeCalendar.set(Calendar.MINUTE, startTimeMinutes);
        startTimeCalendar.set(Calendar.SECOND, 0);
        startTimeCalendar.set(Calendar.MILLISECOND, 0);
        startTimeCalendar.set(Calendar.DAY_OF_YEAR, 1);
        startTimeCalendar.set(Calendar.MONTH, 1);
        startTimeCalendar.set(Calendar.YEAR, 1970);
    //    String dateStart = df.format(startTimeCalendar.getTimeInMillis());

        stopTimeCalendar = Calendar.getInstance();
        stopTimeCalendar.set(Calendar.HOUR_OF_DAY, stopTimeHours);
        stopTimeCalendar.set(Calendar.MINUTE, stopTimeMinutes);
        stopTimeCalendar.set(Calendar.SECOND, 0);
        stopTimeCalendar.set(Calendar.MILLISECOND, 0);
        stopTimeCalendar.set(Calendar.DAY_OF_YEAR, 1);
        stopTimeCalendar.set(Calendar.MONTH, 1);
        stopTimeCalendar.set(Calendar.YEAR, 1970);
    //    String dateStop = df.format(stopTimeCalendar.getTimeInMillis());

        nowTimeCalendar = Calendar.getInstance();
        nowTimeCalendar.set(Calendar.HOUR_OF_DAY, timeNowHours);
        nowTimeCalendar.set(Calendar.MINUTE, timeNowMinutes);
        nowTimeCalendar.set(Calendar.SECOND, 0);
        nowTimeCalendar.set(Calendar.MILLISECOND, 0);
        nowTimeCalendar.set(Calendar.DAY_OF_YEAR, 1);
        nowTimeCalendar.set(Calendar.MONTH, 1);
        nowTimeCalendar.set(Calendar.YEAR, 1970);
    //    String dateNow = df.format(nowTimeCalendar.getTimeInMillis());

        // определим реальный интервал, который нужен до следующего запуска уведомления
        if (nowTimeCalendar.getTimeInMillis() <= stopTimeCalendar.getTimeInMillis() && nowTimeCalendar.getTimeInMillis() >= startTimeCalendar.getTimeInMillis()) {
            // если от момента создания до первого запуска больше, чем до конца срока - выполняем в конце срока (для первого дня так)
            if (timeDuration > (stopTimeCalendar.getTimeInMillis() - nowTimeCalendar.getTimeInMillis())) {
                timeDuration = (stopTimeCalendar.getTimeInMillis() - nowTimeCalendar.getTimeInMillis());
            }
        } else if (nowTimeCalendar.getTimeInMillis() < startTimeCalendar.getTimeInMillis()) {
            timeDuration = (startTimeCalendar.getTimeInMillis() - nowTimeCalendar.getTimeInMillis());
        } else if (nowTimeCalendar.getTimeInMillis() > stopTimeCalendar.getTimeInMillis()) {
            timeDuration = Integer.parseInt(String.valueOf((24 * 60 * 60 * 1000) - (nowTimeCalendar.getTimeInMillis() - startTimeCalendar.getTimeInMillis())));
        }
    }
}
