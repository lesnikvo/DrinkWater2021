package app.simpleproject.drinkwater.notifications;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.PowerManager;
import android.provider.Settings;

import java.util.Objects;

import app.simpleproject.drinkwater.R;

public class PowerSettings {

    // проверка и вывод окна о просьбе добавить в список неоптимизированных под энергопотребление
    public void openPowerSettings(Context context) {
        String packageName = context.getPackageName();
        PowerManager powerManager = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        // только с 23 версии апи
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!powerManager.isIgnoringBatteryOptimizations(packageName)) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage(Objects.requireNonNull(context).getResources().getString(R.string.addtolist))
                        .setPositiveButton(R.string.ok, (dialog, which) -> {
                            Intent intent = new Intent();
                            intent.setAction(Settings.ACTION_IGNORE_BATTERY_OPTIMIZATION_SETTINGS);
                            context.startActivity(intent);
                        })
                        .setNegativeButton(R.string.later, (dialog, which) -> {

                        });

                AlertDialog alert = builder.create();
                alert.show();
            }
        }
    }
}
