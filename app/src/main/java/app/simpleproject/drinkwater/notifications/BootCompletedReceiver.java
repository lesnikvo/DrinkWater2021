package app.simpleproject.drinkwater.notifications;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import java.util.concurrent.TimeUnit;

import app.simpleproject.drinkwater.R;


public class BootCompletedReceiver extends BroadcastReceiver {
    public BootCompletedReceiver() {
    }

    public void onReceive(Context context, Intent intent) {

        // выключил, потому что он вроде никогда не исполняется
        // по крайней мере на 9 андроиде
        /*
        if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {
            Toast toast = Toast.makeText(context.getApplicationContext(),
                    context.getResources().getString(R.string.your_message), Toast.LENGTH_LONG);
            toast.show();
            Log.d("slovoDlyaLoga", context.getResources().getString(R.string.your_message));
            // ваш код здесь

            // вырубаем старый WorkManager, для нового высчитываем время и запускаем
            WorkManager.getInstance().cancelAllWorkByTag("oneTime");
            OneTimeWorkRequest workRequest = new OneTimeWorkRequest.Builder(MyRepeaterWorker.class)
                    // здесь ожидание для ПЕРВОГО запуска
                    .setInitialDelay(15, TimeUnit.SECONDS)
                    .addTag("oneTime")
                    .build();

            final WorkManager mWorkManager = WorkManager.getInstance();
            mWorkManager.enqueue(workRequest);
        }
         */
    }
}