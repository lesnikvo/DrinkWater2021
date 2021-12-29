package app.simpleproject.drinkwater.fragments;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import app.simpleproject.drinkwater.BoxAdapter;
import app.simpleproject.drinkwater.BoxAdapterSmall;
import app.simpleproject.drinkwater.DBHelper;
import app.simpleproject.drinkwater.MainActivity;
import app.simpleproject.drinkwater.Product;
import app.simpleproject.drinkwater.R;
import app.simpleproject.drinkwater.notifications.MyRepeaterWorker;
import app.simpleproject.drinkwater.notifications.PowerSettings;

import static app.simpleproject.drinkwater.MainActivity.sp;
import static java.util.Calendar.HOUR_OF_DAY;
import static java.util.Calendar.MINUTE;

public class HomeFragment extends Fragment {

    static View view;
    static boolean switchIcons = true;
    static public BoxAdapter boxAdapter;
    static public BoxAdapterSmall boxAdapterSmall;
    static ArrayList<Product> products = new ArrayList<Product>();
    static GridView gridView;
    int normaWater;
    int sportWater;
    boolean sportActivate;
    boolean b;
    int progressBaraMax;
    static ProgressBar progressBar;
    int progressBaraNow;
    static FloatingActionButton floatingSport;
    static FloatingActionButton floatingBack;
    FloatingActionButton floatingAdd;
    static TextView volumeWater;
    static TextView volumeWater2;
    Product product;
    FrameLayout frameConfigBackground;
    FrameLayout frameWaterConfigure;
    ImageView imageConfig;
    String volume;
    TextView textVolumeConfig;
    int firstVolume;
    int volumePref;
    SeekBar seekBar;
    int j = 0;
    FrameLayout frameWater;
    FrameLayout frameBack;
    TextView textViewChoiceWater;
    TextView textView_bear_one;
    TextView textView_black_tea_cup;
    TextView textView_coffee;
    TextView textView_green_tea;
    TextView textView_milk_cup;
    TextView textView_milk_profile;
    TextView textView_orange_juice;
    TextView textView_soda_medium;
    TextView textView_water_bottle;
    TextView textView_water_cup;
    TextView textView_wine;
    // элементы из нижней менюшки
    LinearLayout water_cup, black_tea_cup, coffee, green_tea, milk_cup, soda_medium,
            orange_juice, water_bottle, milk_profile, wine, bear_one;
    Boolean underLine = false;
    FrameLayout frameConfigNizWater;
    int volumeForNizWater;
    String nazvanieWater;
    ImageView imageSoSmall;
    EditText textVolumeNizWater;
    String month_string;
    String currentMins;
    int nomerflaga;
    String name;
    int numberOfId=0;
    int flag;
    String obyem;
    int lastItemForBackSpace;
    DBHelper dbHelper;
    SQLiteDatabase database;
    ContentValues contentValues;
    Cursor cursor;
    String ml;
    String subMonth;
    Button buttonViewOkConfig;
    Button buttonViewCancelConfig;
    ImageView imageCopyWater;
    ImageView imageDeleteWater;
    TextView textProgressConfig13;
    TextView textProgressConfig12;
    TextView textProgressConfig23;
    TextView textProgressConfig11;
    FrameLayout frameBackDark12;
    FrameLayout frameBackDark23;
    FrameLayout frameBackDark11;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_home, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // в зависимости от выбранного варианта выставляем большие либо маленькие основные значки для GridView
        // фолс, т.к. ещё нечего пересоздавать
        changeSizeOfBox(getContext(), false);

        progressBar = view.findViewById(R.id.progressBar);
        volumeWater = view.findViewById(R.id.volumeWater);
        volumeWater2 = view.findViewById(R.id.volumeWater2);

        floatingSport = view.findViewById(R.id.floatingSport);
        floatingBack = view.findViewById(R.id.floatingBack);
        floatingAdd = view.findViewById(R.id.floatingAdd);

        // оранжевый фон для кнопок
        floatingAdd.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#f37f21")));
        floatingBack.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#f37f21")));
        floatingSport.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#f37f21")));

        // серый полупрозрачный фон
        frameConfigBackground = view.findViewById(R.id.frameConfigBackground);
        // по клику в GridView открывается для редактирования
        frameWaterConfigure = view.findViewById(R.id.frameWaterConfigure);
        // изображение внутри frameWaterConfigure
        imageConfig = view.findViewById(R.id.imageConfig);
        // изображение внутри frameWaterConfigure
        textVolumeConfig = view.findViewById(R.id.textVolumeConfig);
        // подчеркиваем
        textVolumeConfig.setPaintFlags(textVolumeConfig.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        seekBar = view.findViewById(R.id.seekBar);

        // меняем максимальный объём воды в зависимости от настроек пола/веса
        changeWaterVolume();

        // нижняя менюшка с напитками
        frameWater = view.findViewById(R.id.frameWater);
        // второй серый полупрозрачный фон
        frameBack = view.findViewById(R.id.frameBack);
        // надпись "Выберите напиток"
        textViewChoiceWater = view.findViewById(R.id.textViewChoiceWater);
        // текстовые поля объёма для каждого напитка в нижней менюшке
        textView_bear_one = view.findViewById(R.id.textView_bear_one);
        textView_black_tea_cup = view.findViewById(R.id.textView_black_tea_cup);
        textView_coffee = view.findViewById(R.id.textView_coffee);
        textView_green_tea = view.findViewById(R.id.textView_green_tea);
        textView_milk_cup = view.findViewById(R.id.textView_milk_cup);
        textView_milk_profile = view.findViewById(R.id.textView_milk_profile);
        textView_orange_juice = view.findViewById(R.id.textView_orange_juice);
        textView_soda_medium = view.findViewById(R.id.textView_soda_medium);
        textView_water_bottle = view.findViewById(R.id.textView_water_bottle);
        textView_water_cup = view.findViewById(R.id.textView_water_cup);
        textView_wine = view.findViewById(R.id.textView_wine);
        // элементы из нижней менюшки (лайауты с картинками)
        water_cup = view.findViewById(R.id.water_cup);
        black_tea_cup = view.findViewById(R.id.black_tea_cup);
        coffee = view.findViewById(R.id.coffee);
        green_tea = view.findViewById(R.id.green_tea);
        milk_cup = view.findViewById(R.id.milk_cup);
        soda_medium = view.findViewById(R.id.soda_medium);
        orange_juice = view.findViewById(R.id.orange_juice);
        water_bottle = view.findViewById(R.id.water_bottle);
        milk_profile = view.findViewById(R.id.milk_profile);
        wine = view.findViewById(R.id.wine);
        bear_one = view.findViewById(R.id.bear_one);
        // действия для всех этих напитков
        water_cup.setOnClickListener(ImageClick);
        black_tea_cup.setOnClickListener(ImageClick);
        coffee.setOnClickListener(ImageClick);
        green_tea.setOnClickListener(ImageClick);
        milk_cup.setOnClickListener(ImageClick);
        soda_medium.setOnClickListener(ImageClick);
        orange_juice.setOnClickListener(ImageClick);
        water_bottle.setOnClickListener(ImageClick);
        milk_profile.setOnClickListener(ImageClick);
        wine.setOnClickListener(ImageClick);
        bear_one.setOnClickListener(ImageClick);
        // фрэйм для редактирования элементов в нижней менюшке
        frameConfigNizWater = view.findViewById(R.id.frameConfigNizWater);
        // EditText для изменения базового объёма напитка в нижней менюшке
        textVolumeNizWater = view.findViewById(R.id.textVolumeNizWater);

        // кнопки Ок и Отмена для окошечка редактирования напитка внутри gridView
        buttonViewOkConfig = view.findViewById(R.id.buttonViewOkConfig);
        buttonViewCancelConfig = view.findViewById(R.id.buttonViewCancelConfig);
        imageCopyWater = view.findViewById(R.id.imageCopyWater);
        imageDeleteWater = view.findViewById(R.id.imageDeleteWater);

        // при редактировании напитки в gridView это числа в круглешках
        frameBackDark12 = view.findViewById(R.id.frameBackDark12);
        frameBackDark23 = view.findViewById(R.id.frameBackDark23);
        frameBackDark11 = view.findViewById(R.id.frameBackDark11);
        // при редактировании напитки в gridView это части ползунка
        textProgressConfig13 = view.findViewById(R.id.textProgressConfig13);
        textProgressConfig12 = view.findViewById(R.id.textProgressConfig12);
        textProgressConfig23 = view.findViewById(R.id.textProgressConfig23);
        textProgressConfig11 = view.findViewById(R.id.textProgressConfig11);


        buttonViewOkConfig.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                frameConfigBackground.setVisibility(View.GONE);
                frameWaterConfigure.setVisibility(View.GONE);





                dbHelper = new DBHelper(getContext());
                database = dbHelper.getWritableDatabase();
                ContentValues contentValuesUpdate = new ContentValues();

                obyem = volume;

                contentValuesUpdate.put(DBHelper.KEY_VOLUME, obyem);
                contentValuesUpdate.put(DBHelper.KEY_PROGRESSBAR, seekBar.getProgress());

                database.update(DBHelper.TABLE_CONTACTS, contentValuesUpdate, DBHelper.KEY_ID + "= ?", new String[]{product.numberId});

                contentValuesUpdate.clear();
                database.close();
                dbHelper.close();

                ml=getResources().getString(R.string.ml);

                // здесь кусок вообще хз
                product.name = volume + " " + ml;
                product.barPosition = String.valueOf(seekBar.getProgress());

                progressBaraNow=progressBaraNow-firstVolume+Integer.parseInt(volume);
                //

                SharedPreferences.Editor editorProgress1 = sp.edit();
                editorProgress1.putInt("progressBaraNow", progressBaraNow);
                editorProgress1.apply();

                progressBar.setProgress(progressBaraNow);
                volumeWater.setText(String.valueOf(progressBaraNow));



















                // скорее тру, чем фолс, потому что параметры напитка уже внутри gridView редактируются
                changeSizeOfBox(getContext(), true);

                switchIcons = sp.getBoolean("switchIcons", true);
                if (switchIcons) {
                    boxAdapter.notifyDataSetChanged();
                } else {
                    boxAdapterSmall.notifyDataSetChanged();
                }
            }
        });

        buttonViewCancelConfig.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                frameConfigBackground.setVisibility(View.GONE);
                frameWaterConfigure.setVisibility(View.GONE);
            }
        });

        // при включении проблема: скрывает всё это, даже если кликать не по фону, а по окошку
    /*    frameConfigBackground.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                frameConfigBackground.setVisibility(View.GONE);
                frameWaterConfigure.setVisibility(View.GONE);
            }
        });
        */

      //  imageCopyWater.setOnClickListener(onImageCopyWater);
      //  imageDeleteWater.setOnClickListener(onImageDeleteWater);

        // работа с БД
        dbHelper = new DBHelper(getContext());
        database = dbHelper.getWritableDatabase();
        contentValues = new ContentValues();
        cursor = database.query(DBHelper.TABLE_CONTACTS, null, null, null, null, null, null);

        // проверяем наступил ли следующий день, если наступил - сбрасываем все параметры,
        // если нет - подгружаем в gridView и параметры из SP
        Calendar c = Calendar.getInstance();
        int month_c = c.get(Calendar.MONTH) + 1;

        if (month_c < 10) {
            month_string = "0" + String.valueOf(month_c);
        } else {
            month_string = String.valueOf(month_c);
        }
        String date_string = String.valueOf(c.get(Calendar.DAY_OF_MONTH))
                + month_string + String.valueOf(c.get(Calendar.YEAR));

        // при первом запуске даты равны, значит, уравниваем их и ничего не делаем
        // позже запишем текущую дату в SP для следующих запусков
        String dateToday = sp.getString("dateToday", date_string);

        // до 301 строки в зависимости от новой/старой даты обновляем прогресс бар и параметры
        normaWater = Integer.parseInt(sp.getString("textNormaWater", "1800"));
        sportWater = Integer.parseInt(sp.getString("textSportWater", "450"));
        sportActivate = sp.getBoolean("sportActivate", false);

    //    progressBaraNow = sp.getInt("progressBaraNow", 0);
        if (sportActivate) {
            progressBaraMax = normaWater + sportWater;

            floatingSport.setImageResource(R.drawable.ic_fitness_center_white_24dp);
            floatingBack.setImageResource(R.drawable.ic_replay_white_24dp);

            sportActivate = true;
            b = false;
        } else {
            progressBaraMax = normaWater;

            floatingSport.setImageResource(R.drawable.ic_fitness_center_light_orange_24dp);
            floatingBack.setImageResource(R.drawable.ic_replay_light_orange_24dp);

            sportActivate = false;
            b = true;
        }
        progressBar.setMax(progressBaraMax);
        progressBar.setProgress(progressBaraNow); // Важно оставить

        SharedPreferences.Editor editorB0 = sp.edit();
        editorB0.putBoolean("sportActivate", sportActivate);
        editorB0.putBoolean("b", b);
        editorB0.apply();

        volumeWater2.setText(String.valueOf(progressBaraMax));
        progressBaraNow = 0;

        lastItemForBackSpace = sp.getInt("lastItemForBackSpace", 0);

        if (!dateToday.equals(date_string)) {
            // отсюда запись "dateToday" в настройки перенесли ниже
            progressBar.setProgress(progressBaraNow);
            volumeWater.setText(String.valueOf(progressBaraNow));
        } else {
            ////////////////////
            SharedPreferences.Editor editorDateToday = sp.edit();
            editorDateToday.putInt("lastItemForBackSpace", lastItemForBackSpace);
            // подгружаем из БД в массив сегодняшние напитки для gridView
            if (cursor.moveToFirst()) {
                int nameIndex = cursor.getColumnIndex(DBHelper.KEY_NAME);
                int imgIndex = cursor.getColumnIndex(DBHelper.KEY_IMG);
                int timeIndex = cursor.getColumnIndex(DBHelper.KEY_TIME);
                int volumeIndex = cursor.getColumnIndex(DBHelper.KEY_VOLUME);
                int dateIndex = cursor.getColumnIndex(DBHelper.KEY_DATE);
                int idIndex = cursor.getColumnIndex(DBHelper.KEY_ID);
                int progressIndex = cursor.getColumnIndex(DBHelper.KEY_PROGRESSBAR);
                int lastItemForBackSpaceIndex = cursor.getColumnIndex(DBHelper.KEY_LASTITEM);

                ml = getResources().getString(R.string.ml);

                do {
                    if (date_string.equals(cursor.getString(dateIndex))) {
                        products.add(new Product(cursor.getString(volumeIndex) + " " + ml, cursor.getString(timeIndex),
                                cursor.getInt(imgIndex), cursor.getString(idIndex), cursor.getString(nameIndex), cursor.getString(progressIndex), cursor.getString(idIndex), cursor.getString(dateIndex), cursor.getInt(lastItemForBackSpaceIndex)));
                        progressBaraNow = progressBaraNow + Integer.parseInt(cursor.getString(volumeIndex));
                    }
                } while (cursor.moveToNext());
            }

            progressBar.setProgress(progressBaraNow);
            volumeWater.setText(String.valueOf(progressBaraNow));

            editorDateToday.putInt("progressBaraNow", progressBaraNow);
            editorDateToday.putString("dateToday", date_string);
            editorDateToday.apply();

        }
        // первый из вероянто бесполезных вызовов этой функции
        // тру, потому что нужно в любом случае пересоздать Adapter
        changeSizeOfBox(getContext(), true);
        // 100% нужная часть
        switchIcons = sp.getBoolean("switchIcons", true);
        if (switchIcons) {
            boxAdapter.notifyDataSetChanged();
        } else {
            boxAdapterSmall.notifyDataSetChanged();
        }

        // не уверен, что здесь нужно +1, т.к. уже вообще не помню как он дальше используется
        numberOfId = cursor.getPosition() + 1;

        cursor.close();
        database.close();
        dbHelper.close();

        // открывает нижнюю менюшку с напитками
        floatingAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                j++;
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // если 1 клик??
                        if (j == 1) {
                            textView_bear_one.setText(String.valueOf(sp.getInt("bear_one", 500)));
                            textView_black_tea_cup.setText(String.valueOf(sp.getInt("black_tea_cup", 250)));
                            textView_coffee.setText(String.valueOf(sp.getInt("coffee", 300)));
                            textView_green_tea.setText(String.valueOf(sp.getInt("green_tea", 250)));
                            textView_milk_cup.setText(String.valueOf(sp.getInt("milk_cup", 250)));
                            textView_milk_profile.setText(String.valueOf(sp.getInt("milk_profile", 500)));
                            textView_orange_juice.setText(String.valueOf(sp.getInt("orange_juice", 200)));
                            textView_soda_medium.setText(String.valueOf(sp.getInt("soda_medium", 500)));
                            textView_water_bottle.setText(String.valueOf(sp.getInt("water_bottle", 500)));
                            textView_water_cup.setText(String.valueOf(sp.getInt("water_cup", 250)));
                            textView_wine.setText(String.valueOf(sp.getInt("wine", 200)));

                            frameWater.setVisibility(View.VISIBLE);
                            frameBack.setVisibility(View.VISIBLE);
                            textViewChoiceWater.setVisibility(View.VISIBLE);

                            // даблклик??
                        } else if (j == 2) {
                            // Не уверен, что нужно
                            // зачем-то название месяца (для истории в будущем?)
                            Date();
                        }
                        j = 0;
                    }
                    // поставил так мало, чтоб не успевали больше 1 клика сделать
                }, 50);
            }
        });

        // клик по серому фону при добавлении напитка
        frameBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // нижнее меню, серый фон, надпись "Выберите напиток"
                frameWater.setVisibility(View.GONE);
                frameBack.setVisibility(View.GONE);
                textViewChoiceWater.setVisibility(View.GONE);
            }
        });

        floatingSport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                normaWater = Integer.parseInt(sp.getString("textNormaWater", "1800"));
                sportWater = Integer.parseInt(sp.getString("textSportWater", "450"));
                b = sp.getBoolean("b", true);

                progressBaraNow = sp.getInt("progressBaraNow", 0);
                if (b) {
                    progressBaraMax = normaWater + sportWater;

                    floatingSport.setImageResource(R.drawable.ic_fitness_center_white_24dp);
                    floatingBack.setImageResource(R.drawable.ic_replay_white_24dp);

                    sportActivate = true;
                    b = false;
                } else {
                    progressBaraMax = normaWater;

                    floatingSport.setImageResource(R.drawable.ic_fitness_center_light_orange_24dp);
                    floatingBack.setImageResource(R.drawable.ic_replay_light_orange_24dp);

                    sportActivate = false;
                    b = true;
                }
                volumeWater2.setText(String.valueOf(progressBaraMax));

                progressBar.setMax(progressBaraMax);
                progressBar.setProgress(progressBaraNow); // Важно оставить

                SharedPreferences.Editor editorB = sp.edit();
                editorB.putBoolean("sportActivate", sportActivate);
                editorB.putBoolean("b", b);
                editorB.apply();
            }
        });

        floatingBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lastItemForBackSpace = sp.getInt("lastItemForBackSpace", 0);
                if (lastItemForBackSpace>0) {

                    String volumeBack1 = products.get(lastItemForBackSpace-1).name;

                    String[] arrayVolumeBack1 = volumeBack1.split(" ");

                    int minusVolume1 = Integer.parseInt(arrayVolumeBack1[0]);
                    progressBaraNow = sp.getInt("progressBaraNow", 0);
                    progressBaraNow = progressBaraNow - minusVolume1;

                    if (progressBaraNow<0) {
                        progressBaraNow=0;
                    }

                    progressBar.setProgress(progressBaraNow);
                    volumeWater.setText(String.valueOf(progressBaraNow));

                    // скорее всего здесь к lastItemForBackSpace-1 нужно обращаться
                    String strochka = String.valueOf(products.get(lastItemForBackSpace-1).numberId);

                    // и здесь
                    products.remove(lastItemForBackSpace-1);

                    dbHelper = new DBHelper(getContext());
                    database = dbHelper.getWritableDatabase();
                    database.delete(DBHelper.TABLE_CONTACTS,   DBHelper.KEY_ID + "= " + strochka, null);

                    database.close();
                    dbHelper.close();

                    // но при этом всё равно здесь сохранить уменьшение
                    lastItemForBackSpace--;

                    SharedPreferences.Editor editorProgress2 = sp.edit();
                    editorProgress2.putInt("lastItemForBackSpace", lastItemForBackSpace);
                    editorProgress2.putInt("progressBaraNow", progressBaraNow);
                    editorProgress2.apply();

                    changeSizeOfBox(getContext(), true);
                    if (switchIcons) {
                        boxAdapter.notifyDataSetChanged();
                    } else {
                        boxAdapterSmall.notifyDataSetChanged();
                    }
                }
            }
        });


        // обработка кликов по элементам в gridView
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> a, View v, int position, long id) {
                Object o = gridView.getItemAtPosition(position);
                product = (Product) o;
                // серый полупрозрачный фон
                frameConfigBackground.setVisibility(View.VISIBLE);
                // по клику в GridView открывается для редактирования
                frameWaterConfigure.setVisibility(View.VISIBLE);
                // изображение внутри frameWaterConfigure
                imageConfig.setImageResource(product.image);

                String sProductVolume = product.name;
                String[] volumeParts = sProductVolume.split(" ");
                // нулевая часть отвечает за объём
                volume = volumeParts[0];
                textVolumeConfig.setText(volume);

                firstVolume = Integer.parseInt(volumeParts[0]);

                switch (product.imena) {
                    case "bear_one":
                        // volumePref передадим дальше в seekBar
                        volumePref = sp.getInt("bear_one", 500);
                        break;
                    case "black_tea_cup":
                        volumePref = sp.getInt("black_tea_cup", 250);
                        break;
                    case "coffee":
                        volumePref = sp.getInt("coffee", 300);
                        break;
                    case "green_tea":
                        volumePref = sp.getInt("green_tea", 250);
                        break;
                    case "milk_cup":
                        volumePref = sp.getInt("milk_cup", 250);
                        break;
                    case "milk_profile":
                        volumePref = sp.getInt("milk_profile", 500);
                        break;
                    case "orange_juice":
                        volumePref = sp.getInt("orange_juice", 200);
                        break;
                    case "soda_medium":
                        volumePref = sp.getInt("soda_medium", 500);
                        break;
                    case "water_bottle":
                        volumePref = sp.getInt("water_bottle", 500);
                        break;
                    case "wine":
                        volumePref = sp.getInt("wine", 200);
                        break;
                    default:
                        volumePref = sp.getInt("water_cup", 250);
                        break;
                }
                seekBar.setProgress(Integer.parseInt(product.barPosition));
            }
        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                switch (progress) {
                    case 0:
                        textProgressConfig13.setTextColor(Color.parseColor("#000000"));
                        textProgressConfig12.setTextColor(Color.parseColor("#ffffff"));
                        textProgressConfig23.setTextColor(Color.parseColor("#ffffff"));
                        textProgressConfig11.setTextColor(Color.parseColor("#ffffff"));
                        volume = String.valueOf(volumePref / 3);

                        frameBackDark12.setVisibility(View.INVISIBLE);
                        frameBackDark23.setVisibility(View.INVISIBLE);
                        frameBackDark11.setVisibility(View.INVISIBLE);
                        break;
                    case 1:
                        textProgressConfig13.setTextColor(Color.parseColor("#ffffff"));
                        textProgressConfig12.setTextColor(Color.parseColor("#000000"));
                        textProgressConfig23.setTextColor(Color.parseColor("#ffffff"));
                        textProgressConfig11.setTextColor(Color.parseColor("#ffffff"));
                        volume = String.valueOf(volumePref / 2);

                        frameBackDark12.setVisibility(View.VISIBLE);
                        frameBackDark23.setVisibility(View.INVISIBLE);
                        frameBackDark11.setVisibility(View.INVISIBLE);
                        break;
                    case 2:
                        textProgressConfig13.setTextColor(Color.parseColor("#ffffff"));
                        textProgressConfig12.setTextColor(Color.parseColor("#ffffff"));
                        textProgressConfig23.setTextColor(Color.parseColor("#000000"));
                        textProgressConfig11.setTextColor(Color.parseColor("#ffffff"));
                        volume = String.valueOf(volumePref * 2 / 3);

                        frameBackDark12.setVisibility(View.VISIBLE);
                        frameBackDark23.setVisibility(View.VISIBLE);
                        frameBackDark11.setVisibility(View.INVISIBLE);
                        break;
                    case 3:
                        textProgressConfig13.setTextColor(Color.parseColor("#ffffff"));
                        textProgressConfig12.setTextColor(Color.parseColor("#ffffff"));
                        textProgressConfig23.setTextColor(Color.parseColor("#ffffff"));
                        textProgressConfig11.setTextColor(Color.parseColor("#000000"));
                        volume = String.valueOf(volumePref);

                        frameBackDark12.setVisibility(View.VISIBLE);
                        frameBackDark23.setVisibility(View.VISIBLE);
                        frameBackDark11.setVisibility(View.VISIBLE);
                        break;
                    default:
                        volume = String.valueOf(volumePref);
                        break;
                }
                textVolumeConfig.setText(volume);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

    }

    public void Date() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat month_date = new SimpleDateFormat("LLLL", Locale.getDefault());
        String month_name = month_date.format(c.getTime());
    }

    public static void changeSizeOfBox(Context c, boolean reCreate) {
        switchIcons = sp.getBoolean("switchIcons", true);
        if (switchIcons) {
            if (boxAdapter != null) {
                Log.d("asd", "big not null");
                if (reCreate) {
                    boxAdapter.isEmpty();
                    boxAdapter = new BoxAdapter(c, products);
                }
                boxAdapter.notifyDataSetChanged();
            } else {
                Log.d("asd", "big IS null");
                boxAdapter = new BoxAdapter(c, products);
                gridView = view.findViewById(R.id.gridView);
            }
            gridView.setNumColumns(3);
            gridView.setAdapter(boxAdapter);
        } else {
            if (boxAdapterSmall != null) {
                Log.d("asd", "small not null");
                if (reCreate) {
                    boxAdapterSmall.isEmpty();
                    boxAdapterSmall = new BoxAdapterSmall(c, products);
                }
                boxAdapterSmall.notifyDataSetChanged();
            } else {
                Log.d("asd", "small IS null");
                boxAdapterSmall = new BoxAdapterSmall(c, products);
                gridView = view.findViewById(R.id.gridView);
            }
            gridView.setNumColumns(4);
            gridView.setAdapter(boxAdapterSmall);
        }
    }

    public void changeWaterVolume() {
        normaWater = Integer.parseInt(sp.getString("textNormaWater", "1800"));
        sportWater = Integer.parseInt(sp.getString("textSportWater", "450"));
        sportActivate = sp.getBoolean("sportActivate", false);
        b = sp.getBoolean("b", true);

        progressBaraNow = sp.getInt("progressBaraNow", 0);
        if (sportActivate) {
            progressBaraMax = normaWater + sportWater;

            floatingSport.setImageResource(R.drawable.ic_fitness_center_white_24dp);
            floatingBack.setImageResource(R.drawable.ic_replay_white_24dp);

            sportActivate = true;
            b = false;
        } else {
            progressBaraMax = normaWater;

            floatingSport.setImageResource(R.drawable.ic_fitness_center_light_orange_24dp);
            floatingBack.setImageResource(R.drawable.ic_replay_light_orange_24dp);

            sportActivate = false;
            b = true;
        }
        progressBar.setMax(progressBaraMax);
        progressBar.setProgress(progressBaraNow); // Важно оставить

        SharedPreferences.Editor editorB1 = sp.edit();
        editorB1.putBoolean("sportActivate", sportActivate);
        editorB1.putBoolean("b", b);
        editorB1.apply();

        volumeWater2.setText(String.valueOf(progressBaraMax));
    }

    View.OnClickListener ImageClick = new View.OnClickListener() {
        @SuppressLint("NonConstantResourceId")
        @Override
        public void onClick(View v) {
            // если включен режим редактирования в нижнем меню
            if (underLine) {
                // фрэйм для редактирования элементов в нижней менюшке
                frameConfigNizWater.setVisibility(View.VISIBLE);
                switch (v.getId()) {
                    case R.id.bear_one:
                        volumeForNizWater = sp.getInt("bear_one", 500);
                        nazvanieWater = "bear_one";
                        imageSoSmall.setImageResource(R.drawable.bear_one);
                        break;
                    case R.id.black_tea_cup:
                        volumeForNizWater = sp.getInt("black_tea_cup", 250);
                        nazvanieWater = "black_tea_cup";
                        imageSoSmall.setImageResource(R.drawable.black_tea_cup);
                        break;
                    case R.id.coffee:
                        volumeForNizWater = sp.getInt("coffee", 300);
                        nazvanieWater = "coffee";
                        imageSoSmall.setImageResource(R.drawable.coffee);
                        break;
                    case R.id.green_tea:
                        volumeForNizWater = sp.getInt("green_tea", 250);
                        nazvanieWater = "green_tea";
                        imageSoSmall.setImageResource(R.drawable.green_tea);
                        break;
                    case R.id.milk_cup:
                        volumeForNizWater = sp.getInt("milk_cup", 250);
                        nazvanieWater = "milk_cup";
                        imageSoSmall.setImageResource(R.drawable.milk_cup);
                        break;
                    case R.id.milk_profile:
                        volumeForNizWater = sp.getInt("milk_profile", 500);
                        nazvanieWater = "milk_profile";
                        imageSoSmall.setImageResource(R.drawable.milk_profile);
                        break;
                    case R.id.orange_juice:
                        volumeForNizWater = sp.getInt("orange_juice", 200);
                        nazvanieWater = "orange_juice";
                        imageSoSmall.setImageResource(R.drawable.orange_juice);
                        break;
                    case R.id.soda_medium:
                        volumeForNizWater = sp.getInt("soda_medium", 500);
                        nazvanieWater = "soda_medium";
                        imageSoSmall.setImageResource(R.drawable.soda_medium);
                        break;
                    case R.id.water_bottle:
                        volumeForNizWater = sp.getInt("water_bottle", 500);
                        nazvanieWater = "water_bottle";
                        imageSoSmall.setImageResource(R.drawable.water_bottle);
                        break;
                    case R.id.wine:
                        volumeForNizWater = sp.getInt("wine", 200);
                        nazvanieWater = "wine";
                        imageSoSmall.setImageResource(R.drawable.wine);
                        break;
                    default:
                        volumeForNizWater = sp.getInt("water_cup", 250);
                        nazvanieWater = "water_cup";
                        imageSoSmall.setImageResource(R.drawable.water_cup);
                        break;
                }
                textVolumeNizWater.setText(String.valueOf(volumeForNizWater));

            } else {
                // нижнее меню, серый фон, надпись "Выберите напиток"
                frameWater.setVisibility(View.GONE);
                frameBack.setVisibility(View.GONE);
                textViewChoiceWater.setVisibility(View.GONE);

                Calendar c = Calendar.getInstance();
                //Возвращаем текущее время
                int hours_c = c.get(HOUR_OF_DAY);
                int mins_c = c.get(MINUTE);
                int month_c = c.get(Calendar.MONTH) + 1;

                if (month_c < 10) {
                    month_string = "0" + String.valueOf(month_c);
                } else {
                    month_string = String.valueOf(month_c);
                }

                String date_string = String.valueOf(c.get(Calendar.DAY_OF_MONTH))
                        + month_string + String.valueOf(c.get(Calendar.YEAR));

                if (mins_c < 10) {
                    currentMins = "0" + String.valueOf(mins_c);
                } else {
                    currentMins = String.valueOf(mins_c);
                }

                lastItemForBackSpace = sp.getInt("lastItemForBackSpace", 0);

                // если при добавлении напитка окажется, что последний добавленный напиток имел дату (был добавлен) не сегодня
                // то сбросить lastItemForBackSpace и очистить прогресс + очистить массив

                // НО не понятно, нужно ли gridView чистить при этом
                // здесь может выдать ошибку, но по идее в случае lastItemForBackSpace<=0 сразу обрубится if и не проверит null
                // хотя не уверен с 0 или 1 или с чего здесь счёт начать, чтоб (lastItemForBackSpace-1) отработало
                if (lastItemForBackSpace > 0 && !(products.get(lastItemForBackSpace - 1).date.equals(date_string))) {
                    lastItemForBackSpace = 0;
                    progressBaraNow = 0;

                    // переменная чистится, но незаписывается в SP
                    // видимо дальше по ходу добавления напитка запишется сразу обновлённое значение

                    products.clear();

                    // возможно раньше вот это предполагало очистку, но теперь оно вряд ли очистит
                    // не уверен, что здесь нужна эта строка
                    // вместо целой функции можно просто switchIcons из SP запросить
                    changeSizeOfBox(getContext(), false);
                    // наверное вот этот кусок отвечает за переподгрузку из массива в gridView
                    switchIcons = sp.getBoolean("switchIcons", true);
                    if (switchIcons) {
                        boxAdapter.notifyDataSetChanged();
                    } else {
                        boxAdapterSmall.notifyDataSetChanged();
                    }
                }

                String currentTime = String.valueOf(hours_c) + ":" + currentMins;
                ml = getResources().getString(R.string.ml);
                //////////////////////////
                switch (v.getId()) {
                    case R.id.bear_one:
                        nomerflaga = 11;
                        name = "bear_one";
                        obyem = String.valueOf(sp.getInt("bear_one", 500));
                        products.add(new Product(obyem + " " + ml, currentTime,
                                R.drawable.bear_one, String.valueOf(numberOfId), name, "3", null, date_string, lastItemForBackSpace));
                        flag = R.drawable.bear_one;
                        break;
                    case R.id.black_tea_cup:
                        nomerflaga = 22;
                        name = "black_tea_cup";
                        obyem = String.valueOf(sp.getInt("black_tea_cup", 250));
                        products.add(new Product(obyem + " " + ml, currentTime,
                                R.drawable.black_tea_cup, String.valueOf(numberOfId), name, "3", null, date_string, lastItemForBackSpace));
                        flag = R.drawable.black_tea_cup;
                        break;
                    case R.id.coffee:
                        nomerflaga = 33;
                        name = "coffee";
                        obyem = String.valueOf(sp.getInt("coffee", 300));
                        products.add(new Product(obyem + " " + ml, currentTime,
                                R.drawable.coffee, String.valueOf(numberOfId), name, "3", null, date_string, lastItemForBackSpace));
                        flag = R.drawable.coffee;
                        break;
                    case R.id.green_tea:
                        nomerflaga = 44;
                        name = "green_tea";
                        obyem = String.valueOf(sp.getInt("green_tea", 250));
                        products.add(new Product(obyem + " " + ml, currentTime,
                                R.drawable.green_tea, String.valueOf(numberOfId), name, "3", null, date_string, lastItemForBackSpace));
                        flag = R.drawable.green_tea;
                        break;
                    case R.id.milk_cup:
                        nomerflaga = 55;
                        name = "milk_cup";
                        obyem = String.valueOf(sp.getInt("milk_cup", 250));
                        products.add(new Product(obyem + " " + ml, currentTime,
                                R.drawable.milk_cup, String.valueOf(numberOfId), name, "3", null, date_string, lastItemForBackSpace));
                        flag = R.drawable.milk_cup;
                        break;
                    case R.id.milk_profile:
                        nomerflaga = 66;
                        name = "milk_profile";
                        obyem = String.valueOf(sp.getInt("milk_profile", 500));
                        products.add(new Product(obyem + " " + ml, currentTime,
                                R.drawable.milk_profile, String.valueOf(numberOfId), name, "3", null, date_string, lastItemForBackSpace));
                        flag = R.drawable.milk_profile;
                        break;
                    case R.id.orange_juice:
                        nomerflaga = 77;
                        name = "orange_juice";
                        obyem = String.valueOf(sp.getInt("orange_juice", 200));
                        products.add(new Product(obyem + " " + ml, currentTime,
                                R.drawable.orange_juice, String.valueOf(numberOfId), name, "3", null, date_string, lastItemForBackSpace));
                        flag = R.drawable.orange_juice;
                        break;
                    case R.id.soda_medium:
                        nomerflaga = 88;
                        name = "soda_medium";
                        obyem = String.valueOf(sp.getInt("soda_medium", 500));
                        products.add(new Product(obyem + " " + ml, currentTime,
                                R.drawable.soda_medium, String.valueOf(numberOfId), name, "3", null, date_string, lastItemForBackSpace));
                        flag = R.drawable.soda_medium;
                        break;
                    case R.id.water_bottle:
                        nomerflaga = 99;
                        name = "water_bottle";
                        obyem = String.valueOf(sp.getInt("water_bottle", 500));
                        products.add(new Product(obyem + " " + ml, currentTime,
                                R.drawable.water_bottle, String.valueOf(numberOfId), name, "3", null, date_string, lastItemForBackSpace));
                        flag = R.drawable.water_bottle;
                        break;
                    case R.id.wine:
                        nomerflaga = 1;
                        name = "wine";
                        obyem = String.valueOf(sp.getInt("wine", 200));
                        products.add(new Product(obyem + " " + ml, currentTime,
                                R.drawable.wine, String.valueOf(numberOfId), name, "3", null, date_string, lastItemForBackSpace));
                        flag = R.drawable.wine;
                        break;
                    default:
                        nomerflaga = 0;
                        name = "water_cup";
                        obyem = String.valueOf(sp.getInt("water_cup", 250));
                        products.add(new Product(obyem + " " + ml, currentTime,
                                R.drawable.water_cup, String.valueOf(numberOfId), name, "3", null, date_string, lastItemForBackSpace));
                        flag = R.drawable.water_cup;
                        break;
                }

                // вопросы по его значению есть
                numberOfId++;

                // опять же не уверен, здесь что нужна вся эта функция
                // здесь хз фолс или тру, по идее и без неё работало
                changeSizeOfBox(getContext(), false);
                // может только этот блок оставлю
                switchIcons = sp.getBoolean("switchIcons", true);
                if (switchIcons) {
                    boxAdapter.notifyDataSetChanged();
                } else {
                    boxAdapterSmall.notifyDataSetChanged();
                }
                // плавно перемещаем вид к последнему добавленному в gridView
                gridView.smoothScrollToPosition(gridView.getBottom());

                // при включенных уведомлениях перезапустит их для корректировки времени следующего приёма воды
                // без точных параметров времени и всё такое, пока выключу, позже надо разобраться
                /*
                boolean switchNotif = sp.getBoolean("switchNotif", false);
                if (switchNotif){
                    String TimePrefStart = sp.getString("TimePrefStart", "10:15");

                    //Выключаем старое напоминание
                    WorkManager.getInstance(Objects.requireNonNull(getContext())).cancelAllWorkByTag("oneTime");
                    WorkManager.getInstance(Objects.requireNonNull(getContext())).cancelAllWorkByTag("timeRepeater");

                    //Включаем новое напоминание
                    OneTimeWorkRequest workRequest = new OneTimeWorkRequest.Builder(MyRepeaterWorker.class)
                            // здесь ожидание для ПЕРВОГО запуска
                            //        .setInitialDelay(timeDuration, TimeUnit.MILLISECONDS)
                            .setInitialDelay(5000, TimeUnit.MILLISECONDS)
                            .addTag("oneTime")
                            .build();

                    WorkManager mWorkManager = WorkManager.getInstance(Objects.requireNonNull(getContext()));
                    mWorkManager.enqueue(workRequest);

                    // этот запрос может задолбать человека, если он не захочет / не сможет добавить в белый список
                    PowerSettings powerSettings = new PowerSettings();
                    powerSettings.openPowerSettings(Objects.requireNonNull(getContext()));
                }
                */

                dbHelper = new DBHelper(getContext());
                database = dbHelper.getWritableDatabase();
                contentValues = new ContentValues();

                contentValues.put(DBHelper.KEY_NAME, name);
                contentValues.put(DBHelper.KEY_IMG, flag);
                contentValues.put(DBHelper.KEY_TIME, currentTime);
                contentValues.put(DBHelper.KEY_VOLUME, obyem);
                contentValues.put(DBHelper.KEY_DATE, date_string);


                // не понятно почему, но везде 3 стоит
                contentValues.put(DBHelper.KEY_PROGRESSBAR, 3);


                contentValues.put(DBHelper.KEY_LASTITEM, lastItemForBackSpace);

                // не знаю, зачем здесь это делается
                // скорее всего вырезаем из строки даты месяц в зависимости от длины даты
                // но она в принципе всегда подгоняется под один формат
                if (date_string.length() == 8) {
                    subMonth = date_string.substring(2, 4);
                } else {
                    subMonth = date_string.substring(1, 3);
                }

                contentValues.put(DBHelper.KEY_MONTH, subMonth);

                progressBaraNow = progressBaraNow + Integer.parseInt(obyem);
                volumeWater.setText(String.valueOf(progressBaraNow));
                progressBar.setProgress(progressBaraNow);

                database.insert(DBHelper.TABLE_CONTACTS, null, contentValues);

                // раньше было только dbHelper.close();
                contentValues.clear();
                database.close();
                dbHelper.close();

                lastItemForBackSpace++;
                SharedPreferences.Editor editorProgress = sp.edit();
                editorProgress.putInt("progressBaraNow", progressBaraNow);
                editorProgress.putInt("lastItemForBackSpace", lastItemForBackSpace);
                editorProgress.apply();
            }
        }
    };
}