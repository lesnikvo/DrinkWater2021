package app.simpleproject.drinkwater.fragments;

import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import android.os.PowerManager;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.material.switchmaterial.SwitchMaterial;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import app.simpleproject.drinkwater.R;
import app.simpleproject.drinkwater.notifications.MyRepeaterWorker;
import app.simpleproject.drinkwater.notifications.PowerSettings;

import static app.simpleproject.drinkwater.MainActivity.sp;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NotificationsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NotificationsFragment extends Fragment {

    TextView textTimeStart, textTimeStop, textIntervalHead, textInterval, textTimeStartHead, textTimeStopHead, textSoundHead, textSound;
    SwitchMaterial switchNotif, switchVibro;
    CardView cardInterval, cardStart, cardStop, cardSound;

    Boolean bTime = true;
    private Calendar dateAndTime = Calendar.getInstance();

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    //   private static final String ARG_PARAM1 = "param1";
    //   private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    //   private String mParam1;
    //   private String mParam2;

    public NotificationsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PendingOrdersFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static NotificationsFragment newInstance(String param1, String param2) {
        NotificationsFragment fragment = new NotificationsFragment();
        //    Bundle args = new Bundle();
        //    args.putString(ARG_PARAM1, param1);
        //    args.putString(ARG_PARAM2, param2);
        //    fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //    if (getArguments() != null) {
        //        mParam1 = getArguments().getString(ARG_PARAM1);
        //        mParam2 = getArguments().getString(ARG_PARAM2);
        //    }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_notifications, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        LinearLayout autoApp = view.findViewById(R.id.autoApp);
        LinearLayout headphonesApp = view.findViewById(R.id.headphonesApp);
        LinearLayout drinkwaterApp = view.findViewById(R.id.drinkwaterApp);

        autoApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAppRating(0);
            }
        });

        headphonesApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAppRating(1);
            }
        });

        drinkwaterApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAppRating(2);
            }
        });

        String halfhour = Objects.requireNonNull(getActivity()).getResources().getString(R.string.halfhour);
        String hour = getActivity().getResources().getString(R.string.hour);
        String twohour = getActivity().getResources().getString(R.string.twohour);
        String fourhour = getActivity().getResources().getString(R.string.fourhour);
        String[] mInterval = {halfhour, hour, twohour, fourhour};

        String silence = getActivity().getResources().getString(R.string.silence);
        String defaultsound = getActivity().getResources().getString(R.string.defaultsound);
        String shortsound = getActivity().getResources().getString(R.string.shortsound);
        String longsound = getActivity().getResources().getString(R.string.longsound);
        String[] mSound = {silence, defaultsound, shortsound, longsound};

        switchNotif = view.findViewById(R.id.switchNotif);
        switchVibro = view.findViewById(R.id.switchVibro);
        textIntervalHead = view.findViewById(R.id.textIntervalHead);
        textInterval = view.findViewById(R.id.textInterval);
        textTimeStartHead = view.findViewById(R.id.textTimeStartHead);
        textTimeStart = view.findViewById(R.id.textTimeStart);
        textTimeStopHead = view.findViewById(R.id.textTimeStopHead);
        textTimeStop = view.findViewById(R.id.textTimeStop);
        textSoundHead = view.findViewById(R.id.textSoundHead);
        textSound = view.findViewById(R.id.textSound);

        cardInterval = view.findViewById(R.id.cardInterval);
        cardStart = view.findViewById(R.id.cardStart);
        cardStop = view.findViewById(R.id.cardStop);
        cardSound = view.findViewById(R.id.cardSound);

        switchNotif.setChecked(sp.getBoolean("switchNotif", false));
        if (switchNotif.isChecked()) {
            switchVibro.setEnabled(true);
            switchVibro.setChecked(sp.getBoolean("switchVibro", false));
            switchVibro.setTextColor(Color.parseColor(("#000000")));
            textIntervalHead.setTextColor(Color.parseColor("#000000"));
            textInterval.setTextColor(Color.parseColor("#000000"));
            textTimeStartHead.setTextColor(Color.parseColor("#000000"));
            textTimeStart.setTextColor(Color.parseColor("#000000"));
            textTimeStopHead.setTextColor(Color.parseColor("#000000"));
            textTimeStop.setTextColor(Color.parseColor("#000000"));
            textSoundHead.setTextColor(Color.parseColor("#000000"));
            textSound.setTextColor(Color.parseColor("#000000"));
        } else {
     //       switchVibro.setEnabled(false);
     //       switchVibro.setChecked(false);
     //       SharedPreferences.Editor editorVibro = sp.edit();
    //        editorVibro.putBoolean("switchVibro", false);
    //        editorVibro.apply();
            switchVibro.setTextColor(Color.parseColor(("#B0BEC5")));
            textIntervalHead.setTextColor(Color.parseColor("#B0BEC5"));
            textInterval.setTextColor(Color.parseColor("#B0BEC5"));
            textTimeStartHead.setTextColor(Color.parseColor("#B0BEC5"));
            textTimeStart.setTextColor(Color.parseColor("#B0BEC5"));
            textTimeStopHead.setTextColor(Color.parseColor("#B0BEC5"));
            textTimeStop.setTextColor(Color.parseColor("#B0BEC5"));
            textSoundHead.setTextColor(Color.parseColor("#B0BEC5"));
            textSound.setTextColor(Color.parseColor("#B0BEC5"));
        }

        switch (sp.getString("listInterval", textInterval.getText().toString())) {
            case "halfhour":
                textInterval.setText(R.string.halfhour);
                break;
            case "hour":
                textInterval.setText(R.string.hour);
                break;
            case "twohour":
                textInterval.setText(R.string.twohour);
                break;
            case "fourhour":
                textInterval.setText(R.string.fourhour);
                break;
            default:
                textInterval.setText(sp.getString("listInterval", textInterval.getText().toString()));
                break;
        }

        textTimeStart.setText(sp.getString("TimePrefStart", "10:15"));
        textTimeStop.setText(sp.getString("TimePrefStop", "21:15"));

        switch (sp.getString("soundResId", textSound.getText().toString())) {
            case "silence":
                textSound.setText(R.string.silence);
                break;
            case "defaultsound":
                textSound.setText(R.string.defaultsound);
                break;
            case "shortsound":
                textSound.setText(R.string.shortsound);
                break;
            case "longsound":
                textSound.setText(R.string.longsound);
                break;
            default:
                textSound.setText(sp.getString("soundResId", textSound.getText().toString()));
                break;
        }

        switchVibro.setOnCheckedChangeListener((compoundButton, b) -> {
            SharedPreferences.Editor editorVibro = sp.edit();
            editorVibro.putBoolean("switchVibro", switchVibro.isChecked());
            editorVibro.apply();
        });

        cardInterval.setOnClickListener(view1 -> {

            if (switchNotif.isChecked()) {
                Toast.makeText(getContext(), R.string.recreate,Toast.LENGTH_LONG).show();

                AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
                builder.setItems(mInterval, (dialogInterface, i) -> {
                    boolean upd = false;
                    String s = textInterval.getText().toString();
                    textInterval.setText(mInterval[i]);
                    if (!s.equals(mInterval[i])) {
                        upd = true;
                    }
                    SharedPreferences.Editor editorInterval = sp.edit();
                    switch (i) {
                        case 0:
                            editorInterval.putString("listInterval", "halfhour");
                            if (upd) {
                                setNotification(30 * 60 * 1000);
                            }
                            break;
                        case 1:
                            editorInterval.putString("listInterval", "hour");
                            if (upd) {
                                setNotification(60 * 60 * 1000);
                            }
                            break;
                        case 2:
                            editorInterval.putString("listInterval", "twohour");
                            if (upd) {
                                setNotification(120 * 60 * 1000);
                            }
                            break;
                        case 3:
                            editorInterval.putString("listInterval", "fourhour");
                            if (upd) {
                                setNotification(240 * 60 * 1000);
                            }
                            break;
                    }
                    editorInterval.apply();
                })
                        .setCancelable(true);

                AlertDialog alert = builder.create();
                alert.show();
            }
        });

        cardStart.setOnClickListener(view12 -> {
            if (switchNotif.isChecked()) {
                Toast.makeText(getContext(), R.string.recreate,Toast.LENGTH_LONG).show();
                bTime = true;
                dateAndTime = Calendar.getInstance();
                setTime();
            }
        });

        cardStop.setOnClickListener(view13 -> {
            if (switchNotif.isChecked()) {
                Toast.makeText(getContext(), R.string.recreate,Toast.LENGTH_LONG).show();
                bTime = false;
                dateAndTime = Calendar.getInstance();
                setTime();
            }
        });

        cardSound.setOnClickListener(view14 -> {
            if (switchNotif.isChecked()) {
                AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
                builder.setItems(mSound, (dialogInterface, i) -> {
                    textSound.setText(mSound[i]);
                    SharedPreferences.Editor editorSound = sp.edit();
                    switch (i) {
                        case 0:
                            editorSound.putString("soundResId", "silence");
                            break;
                        case 1:
                            editorSound.putString("soundResId", "defaultsound");
                            break;
                        case 2:
                            editorSound.putString("soundResId", "shortsound");
                            break;
                        case 3:
                            editorSound.putString("soundResId", "longsound");
                            break;
                    }
                    editorSound.apply();
                })
                        .setCancelable(true);

                AlertDialog alert = builder.create();
                alert.show();
            }
        });

        //    private MainActivity mainActivity;
        switchNotif.setOnCheckedChangeListener((compoundButton, b) -> {
            if (switchNotif.isChecked()) {
                switchVibro.setEnabled(true);
                switchVibro.setTextColor(Color.parseColor(("#000000")));
                textIntervalHead.setTextColor(Color.parseColor("#000000"));
                textInterval.setTextColor(Color.parseColor("#000000"));
                textTimeStartHead.setTextColor(Color.parseColor("#000000"));
                textTimeStart.setTextColor(Color.parseColor("#000000"));
                textTimeStopHead.setTextColor(Color.parseColor("#000000"));
                textTimeStop.setTextColor(Color.parseColor("#000000"));
                textSoundHead.setTextColor(Color.parseColor("#000000"));
                textSound.setTextColor(Color.parseColor("#000000"));

                setNotification(1111111);

            } else {
            //    switchVibro.setEnabled(false);
            //    switchVibro.setChecked(false);
            //    SharedPreferences.Editor editorVibro = sp.edit();
           //     editorVibro.putBoolean("switchVibro", false);
            //    editorVibro.apply();
                switchVibro.setTextColor(Color.parseColor(("#B0BEC5")));
                textIntervalHead.setTextColor(Color.parseColor("#B0BEC5"));
                textInterval.setTextColor(Color.parseColor("#B0BEC5"));
                textTimeStartHead.setTextColor(Color.parseColor("#B0BEC5"));
                textTimeStart.setTextColor(Color.parseColor("#B0BEC5"));
                textTimeStopHead.setTextColor(Color.parseColor("#B0BEC5"));
                textTimeStop.setTextColor(Color.parseColor("#B0BEC5"));
                textSoundHead.setTextColor(Color.parseColor("#B0BEC5"));
                textSound.setTextColor(Color.parseColor("#B0BEC5"));

                cancelNotification();

            }
            SharedPreferences.Editor editorNotif = sp.edit();
            editorNotif.putBoolean("switchNotif", switchNotif.isChecked());
            editorNotif.apply();
        });
    }

    public void openAppRating(int appPos) {
        String packageName;
        switch (appPos){
            case 0:
                packageName = "site.simpleproject.regionauto";
                break;
            case 1:
                packageName = "site.simpleproject.checkphones";
                break;
            default:
                packageName = "app.simpleproject.drinkwater";
                break;
        }
        try {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + packageName)));
        } catch (android.content.ActivityNotFoundException asd) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + packageName)));
        }
    }

    // установка начальных даты и времени
    private void setInitialDateTime() {
        DateFormat df = new SimpleDateFormat("HH:mm", Locale.US);
        String date = df.format(dateAndTime.getTimeInMillis());

        checkOrderOfTimes(bTime, date);
        /*
        if (bTime) {
            //    textTimeStart.setText(DateUtils.formatDateTime(getContext(),
            //            dateAndTime.getTimeInMillis(),
            //            DateUtils.FORMAT_SHOW_TIME));
            DateFormat df = new SimpleDateFormat("HH:mm", Locale.US);
            String date = df.format(dateAndTime.getTimeInMillis());
            //    textTimeStart.setText(date);
            //    SharedPreferences.Editor editorTimeStart = sp.edit();
            //    editorTimeStart.putString("TimePrefStart", date);
            //    editorTimeStart.apply();
            checkOrderOfTimes(bTime, date);
            //            NotifBuild();
        } else {
            //    textTimeStop.setText(DateUtils.formatDateTime(getContext(),
            //            dateAndTime.getTimeInMillis(),
            //            DateUtils.FORMAT_SHOW_TIME));
            DateFormat df = new SimpleDateFormat("HH:mm", Locale.US);
            String date = df.format(dateAndTime.getTimeInMillis());
            //    textTimeStop.setText(date);
            //    SharedPreferences.Editor editorTimeStart = sp.edit();
            //    editorTimeStart.putString("TimePrefStop", date);
            //    editorTimeStart.apply();
            checkOrderOfTimes(bTime, date);
            //            NotifBuild();
        }
         */
    }

    // отображаем диалоговое окно для выбора времени
    public void setTime() {
        new TimePickerDialog(getContext(), t,
                dateAndTime.get(Calendar.HOUR_OF_DAY),
                dateAndTime.get(Calendar.MINUTE), true)
                .show();
    }

    // установка обработчика выбора времени
    TimePickerDialog.OnTimeSetListener t = (view, hourOfDay, minute) -> {
        dateAndTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
        dateAndTime.set(Calendar.MINUTE, minute);
        setInitialDateTime();
    };

    private void checkOrderOfTimes(boolean startTime, String timeString) {
        // передали сюда timeString, если startTime тру, значит передали время старта - время остановки из преференций берём
        int hours = Integer.parseInt(timeString.substring(0, timeString.indexOf(":")));
        int mins = Integer.parseInt(timeString.substring(timeString.indexOf(":") + 1));
        if (startTime) {
            //    int startTimeHours = hours;
            //    int startTimeMinutes = mins;
            String stop = sp.getString("TimePrefStop", "21:15");
            int stopTimeHours = Integer.parseInt(stop.substring(0, stop.indexOf(":")));
            int stopTimeMinutes = Integer.parseInt(stop.substring(stop.indexOf(":") + 1));

            if (hours > stopTimeHours) {
                Toast.makeText(getContext(), "Время старта должно быть меньше времени завершения", Toast.LENGTH_SHORT).show();
                //    textTimeStart.setText(sp.getString("TimePrefStart", "10:15"));
            } else if ((hours == stopTimeHours) && (mins >= stopTimeMinutes)) {
                Toast.makeText(getContext(), "Время старта должно быть меньше времени завершения", Toast.LENGTH_SHORT).show();
                //        textTimeStart.setText(sp.getString("TimePrefStart", "10:15"));
            } else {
                textTimeStart.setText(timeString);
                SharedPreferences.Editor editorTimeStart = sp.edit();
                editorTimeStart.putString("TimePrefStart", timeString);
                editorTimeStart.apply();

                setNotification(Long.parseLong(textInterval.getText().toString()));
            }
        } else {
            //    int stopTimeHours = hours;
            //    int stopTimeMinutes = mins;
            String start = sp.getString("TimePrefStart", "10:15");
            int startTimeHours = Integer.parseInt(start.substring(0, start.indexOf(":")));
            int startTimeMinutes = Integer.parseInt(start.substring(start.indexOf(":") + 1));

            if (startTimeHours > hours) {
                Toast.makeText(getContext(), "Время старта должно быть меньше времени завершения", Toast.LENGTH_SHORT).show();
                //    textTimeStop.setText(sp.getString("TimePrefStop", "21:15"));
            } else if ((startTimeHours == hours) && (startTimeMinutes >= mins)) {
                Toast.makeText(getContext(), "Время старта должно быть меньше времени завершения", Toast.LENGTH_SHORT).show();
                //    textTimeStop.setText(sp.getString("TimePrefStop", "21:15"));
            } else {
                textTimeStop.setText(timeString);
                SharedPreferences.Editor editorTimeStart = sp.edit();
                editorTimeStart.putString("TimePrefStop", timeString);
                editorTimeStart.apply();

                setNotification(Long.parseLong(textInterval.getText().toString()));
            }
        }
    }

    private void setNotification(long timeDuration) {
        String start = sp.getString("TimePrefStart", "10:15");
        String stop = sp.getString("TimePrefStop", "21:15");
        // Текущее время
        Date currentDate = new Date();
        // Форматирование времени как "часы:минуты"
        DateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
        String timeNow = timeFormat.format(currentDate);

        int startTimeHours = Integer.parseInt(start.substring(0, start.indexOf(":")));
        int startTimeMinutes = Integer.parseInt(start.substring(start.indexOf(":") + 1));
        int stopTimeHours = Integer.parseInt(stop.substring(0, stop.indexOf(":")));
        int stopTimeMinutes = Integer.parseInt(stop.substring(stop.indexOf(":") + 1));
        int timeNowHours = Integer.parseInt(timeNow.substring(0, timeNow.indexOf(":")));
        int timeNowMinutes = Integer.parseInt(timeNow.substring(timeNow.indexOf(":") + 1));

        // приводим к "общему знаменателю" все "времена" в миллисекунды, для полученя разницы во времени (между ними)
        Calendar dateAndTime = Calendar.getInstance();
        dateAndTime.set(Calendar.HOUR_OF_DAY, 0);
        dateAndTime.set(Calendar.MINUTE, 0);
        dateAndTime.set(Calendar.SECOND, 0);
        dateAndTime.set(Calendar.MILLISECOND, 0);
        dateAndTime.set(Calendar.DAY_OF_YEAR, 1);
        dateAndTime.set(Calendar.MONTH, 1);
        dateAndTime.set(Calendar.YEAR, 1970);

        Calendar startTimeCalendar = Calendar.getInstance();
        startTimeCalendar.set(Calendar.HOUR_OF_DAY, startTimeHours);
        startTimeCalendar.set(Calendar.MINUTE, startTimeMinutes);
        startTimeCalendar.set(Calendar.SECOND, 0);
        startTimeCalendar.set(Calendar.MILLISECOND, 0);
        startTimeCalendar.set(Calendar.DAY_OF_YEAR, 1);
        startTimeCalendar.set(Calendar.MONTH, 1);
        startTimeCalendar.set(Calendar.YEAR, 1970);

        Calendar stopTimeCalendar = Calendar.getInstance();
        stopTimeCalendar.set(Calendar.HOUR_OF_DAY, stopTimeHours);
        stopTimeCalendar.set(Calendar.MINUTE, stopTimeMinutes);
        stopTimeCalendar.set(Calendar.SECOND, 0);
        stopTimeCalendar.set(Calendar.MILLISECOND, 0);
        stopTimeCalendar.set(Calendar.DAY_OF_YEAR, 1);
        stopTimeCalendar.set(Calendar.MONTH, 1);
        stopTimeCalendar.set(Calendar.YEAR, 1970);

        Calendar nowTimeCalendar = Calendar.getInstance();
        nowTimeCalendar.set(Calendar.HOUR_OF_DAY, timeNowHours);
        nowTimeCalendar.set(Calendar.MINUTE, timeNowMinutes);
        nowTimeCalendar.set(Calendar.SECOND, 0);
        nowTimeCalendar.set(Calendar.MILLISECOND, 0);
        nowTimeCalendar.set(Calendar.DAY_OF_YEAR, 1);
        nowTimeCalendar.set(Calendar.MONTH, 1);
        nowTimeCalendar.set(Calendar.YEAR, 1970);

        WorkManager.getInstance(Objects.requireNonNull(getContext())).cancelAllWorkByTag("oneTime");
        WorkManager.getInstance(Objects.requireNonNull(getContext())).cancelAllWorkByTag("timeRepeater");

        if (nowTimeCalendar.getTimeInMillis() <= stopTimeCalendar.getTimeInMillis() && nowTimeCalendar.getTimeInMillis() >= startTimeCalendar.getTimeInMillis()) {
            // если от момента создания до первого запуска больше, чем до конца срока - выполняем в конце срока (для первого дня так)
            if (timeDuration > (stopTimeCalendar.getTimeInMillis() - nowTimeCalendar.getTimeInMillis())) {
                timeDuration = (stopTimeCalendar.getTimeInMillis() - nowTimeCalendar.getTimeInMillis());
            }
            //  } else if ((nowTimeCalendar.getTimeInMillis() > stopTimeCalendar.getTimeInMillis()) || (nowTimeCalendar.getTimeInMillis() < startTimeCalendar.getTimeInMillis())) {
            //      timeDuration = Integer.parseInt(String.valueOf((24 * 60 * 60 * 1000) - (nowTimeCalendar.getTimeInMillis() - startTimeCalendar.getTimeInMillis())));
        } else if (nowTimeCalendar.getTimeInMillis() < startTimeCalendar.getTimeInMillis()) {
            timeDuration = (startTimeCalendar.getTimeInMillis() - nowTimeCalendar.getTimeInMillis());
        } else if (nowTimeCalendar.getTimeInMillis() > stopTimeCalendar.getTimeInMillis()) {
            timeDuration = Integer.parseInt(String.valueOf((24 * 60 * 60 * 1000) - (nowTimeCalendar.getTimeInMillis() - startTimeCalendar.getTimeInMillis())));
        }
        OneTimeWorkRequest workRequest = new OneTimeWorkRequest.Builder(MyRepeaterWorker.class)
                // здесь ожидание для ПЕРВОГО запуска
        //        .setInitialDelay(timeDuration, TimeUnit.MILLISECONDS)
                .setInitialDelay(5000, TimeUnit.MILLISECONDS)
                .addTag("oneTime")
                .build();

        WorkManager mWorkManager = WorkManager.getInstance(Objects.requireNonNull(getContext()));
        mWorkManager.enqueue(workRequest);

        PowerSettings powerSettings = new PowerSettings();
        powerSettings.openPowerSettings(Objects.requireNonNull(getContext()));

    //    openPowerSettings(Objects.requireNonNull(getContext()));

    }

    private void cancelNotification() {
        WorkManager.getInstance(Objects.requireNonNull(getContext())).cancelAllWorkByTag("oneTime");
        WorkManager.getInstance(Objects.requireNonNull(getContext())).cancelAllWorkByTag("timeRepeater");
    }

    /*
    public void openPowerSettings(Context context) {
        String packageName = context.getPackageName();
        PowerManager powerManager = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        // только с 23 версии апи
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!powerManager.isIgnoringBatteryOptimizations(packageName)) {
                AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
                builder.setMessage(Objects.requireNonNull(getActivity()).getResources().getString(R.string.addtolist))
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
     */

}