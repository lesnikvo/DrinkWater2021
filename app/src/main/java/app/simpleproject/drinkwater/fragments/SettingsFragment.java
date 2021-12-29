package app.simpleproject.drinkwater.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.text.InputFilter;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.switchmaterial.SwitchMaterial;

import java.util.Objects;

import app.simpleproject.drinkwater.R;

import static app.simpleproject.drinkwater.MainActivity.sp;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SettingsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SettingsFragment extends Fragment {

    TextView textPol, textNormaHead, textNormaMera, textTreningHead, textTreningBefore, textTreningMera, textVes, textNorma, textTrening;
    CardView cardPol, cardVes, cardNorma, cardTrening, cardResolution, cardTerms;
    SwitchMaterial switchIcons, switchAutoCalc;
    String[] mPol;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SettingsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DeliveredOrdersFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SettingsFragment newInstance(String param1, String param2) {
        SettingsFragment fragment = new SettingsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
/*
        switchIcons = getActivity().findViewById(R.id.switchIcons);
        textPol = getActivity().findViewById(R.id.textPol);
        textNormaHead = getActivity().findViewById(R.id.textNormaHead);
        textNorma = getActivity().findViewById(R.id.textNorma);
        textNormaMera = getActivity().findViewById(R.id.textNormaMera);
        textTreningBefore = getActivity().findViewById(R.id.textTreningBefore);
        textTreningMera = getActivity().findViewById(R.id.textTreningMera);
        textTreningHead = getActivity().findViewById(R.id.textTreningHead);
        textTrening = getActivity().findViewById(R.id.textTrening);
        switchAutoCalc = getActivity().findViewById(R.id.switchAutoCalc);
        cardPol = getActivity().findViewById(R.id.cardPol);
        cardVes = getActivity().findViewById(R.id.cardVes);
        textVes = getActivity().findViewById(R.id.textVes);
        cardNorma = getActivity().findViewById(R.id.cardNorma);
        cardTrening = getActivity().findViewById(R.id.cardTrening);
        cardResolution = getActivity().findViewById(R.id.cardResolution);

        String female = getActivity().getResources().getString(R.string.text_Pol_F);
        String male = getActivity().getResources().getString(R.string.text_Pol_M);
        mPol = new String[]{female, male};

        cardResolution.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://sites.google.com/view/simpleproject-app"));
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://simpleproject.app/privacy-policy/"));
                startActivity(browserIntent);
            }
        });

        switch (sp.getString("listPol", textPol.getText().toString())) {
            case "female":
                textPol.setText(R.string.text_Pol_F);
                break;
            case "male":
                textPol.setText(R.string.text_Pol_M);
                break;
            default:
                textPol.setText(sp.getString("listPol", textPol.getText().toString()));
                break;
        }

        switchIcons.setChecked(sp.getBoolean("switchIcons", true));
        textVes.setText(sp.getString("textVes", textVes.getText().toString()));
        switchAutoCalc.setChecked(sp.getBoolean("switchAutoCalc", false));
        textNorma.setText(sp.getString("textNormaWater", textNorma.getText().toString()));
        textTrening.setText(sp.getString("textSportWater", textTrening.getText().toString()));

        if (switchAutoCalc.isChecked()) {
            textNormaHead.setTextColor(Color.parseColor(("#000000")));
            textNorma.setTextColor(Color.parseColor("#000000"));
            textNormaMera.setTextColor(Color.parseColor("#000000"));
            textTreningHead.setTextColor(Color.parseColor("#000000"));
            textTreningBefore.setTextColor(Color.parseColor("#000000"));
            textTrening.setTextColor(Color.parseColor("#000000"));
            textTreningMera.setTextColor(Color.parseColor("#000000"));
        } else {
            if (sp.getString("listPol", "female").equals("female")) {
                textNorma.setText(String.valueOf(Integer.parseInt(textVes.getText().toString()) * 30));
                textTrening.setText(String.valueOf(Integer.parseInt(textVes.getText().toString()) * 30 / 4));
            } else {
                textNorma.setText(String.valueOf(Integer.parseInt(textVes.getText().toString()) * 33));
                textTrening.setText(String.valueOf(Integer.parseInt(textVes.getText().toString()) * 33 / 4));
            }
        }
        SharedPreferences.Editor editorNormaAndTrening = sp.edit();
        editorNormaAndTrening.putString("textNormaWater", textNorma.getText().toString());
        editorNormaAndTrening.putString("textSportWater", textTrening.getText().toString());
        editorNormaAndTrening.apply();

        textNormaHead.setTextColor(Color.parseColor(("#B0BEC5")));
        textNorma.setTextColor(Color.parseColor("#B0BEC5"));
        textNormaMera.setTextColor(Color.parseColor("#B0BEC5"));
        textTreningHead.setTextColor(Color.parseColor("#B0BEC5"));
        textTreningBefore.setTextColor(Color.parseColor("#B0BEC5"));
        textTrening.setTextColor(Color.parseColor("#B0BEC5"));
        textTreningMera.setTextColor(Color.parseColor("#B0BEC5"));

        switchIcons.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                SharedPreferences.Editor editorIcons = sp.edit();
                editorIcons.putBoolean("switchIcons", switchIcons.isChecked());
                editorIcons.apply();
            }
        });

        cardPol.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
                builder.setItems(mPol, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        textPol.setText(mPol[i]);
                        SharedPreferences.Editor editorPol = sp.edit();
                        switch (i) {
                            case 0:
                                editorPol.putString("listPol", "female");
                                if (!switchAutoCalc.isChecked()) {
                                    textNorma.setText(String.valueOf(Integer.parseInt(textVes.getText().toString()) * 30));
                                    textTrening.setText(String.valueOf(Integer.parseInt(textVes.getText().toString()) * 30 / 4));
                                }
                                break;
                            case 1:
                                editorPol.putString("listPol", "male");
                                if (!switchAutoCalc.isChecked()) {
                                    textNorma.setText(String.valueOf(Integer.parseInt(textVes.getText().toString()) * 33));
                                    textTrening.setText(String.valueOf(Integer.parseInt(textVes.getText().toString()) * 33 / 4));
                                }
                                break;
                        }

                        editorPol.putString("textNormaWater", textNorma.getText().toString());
                        editorPol.putString("textSportWater", textTrening.getText().toString());
                        editorPol.apply();
                    }
                })
                        .setCancelable(true);

                AlertDialog alert = builder.create();
                alert.show();
            }
        });

        cardVes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final EditText input = new EditText(getContext());

                InputFilter[] filterArray = new InputFilter[1];
                filterArray[0] = new InputFilter.LengthFilter(3);
                input.setFilters(filterArray);

                input.setHint("60");
                input.setHintTextColor(Color.parseColor("#B0BEC5"));
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT);
                input.setLayoutParams(lp);
                input.setInputType(InputType.TYPE_CLASS_NUMBER);
                input.setFocusable(true);

                InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);

                AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
                builder.setTitle(R.string.dialog_ves)
                        .setView(input)
                        .setCancelable(true)
                        .setPositiveButton(R.string.dialog_ok,
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();

                                        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                                        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
                                        if (input.length() == 0) {
                                            input.setText(R.string.text_Ves);
                                            textVes.setText(input.getText().toString());
                                        } else if (Integer.parseInt(input.getText().toString()) <= 0) {
                                            input.setText(R.string.text_Ves);
                                            textVes.setText(input.getText().toString());
                                        } else {
                                            textVes.setText(input.getText().toString());
                                        }
                                        SharedPreferences.Editor editorVes = sp.edit();
                                        editorVes.putString("textVes", textVes.getText().toString());
                                        editorVes.apply();
                                        if (!switchAutoCalc.isChecked()) {

                                            if (sp.getString("listPol", textPol.getText().toString()).equals("female")) {
                                                textNorma.setText(String.valueOf(Integer.parseInt(input.getText().toString()) * 30));
                                                textTrening.setText(String.valueOf(Integer.parseInt(input.getText().toString()) * 30 / 4));

                                            } else {
                                                textNorma.setText(String.valueOf(Integer.parseInt(input.getText().toString()) * 33));
                                                textTrening.setText(String.valueOf(Integer.parseInt(input.getText().toString()) * 33 / 4));
                                            }
                                            SharedPreferences.Editor editorNormaAndTrening2 = sp.edit();
                                            editorNormaAndTrening2.putString("textNormaWater", textNorma.getText().toString());
                                            editorNormaAndTrening2.putString("textSportWater", textTrening.getText().toString());
                                            editorNormaAndTrening2.apply();

                                        }
                                    }
                                })
                        .setNegativeButton(R.string.dialog_cancel,
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();

                                        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                                        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);

                                    }
                                });
                AlertDialog alert = builder.create();
                alert.show();
            }
        });

        cardNorma.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (switchAutoCalc.isChecked()) {

                    final EditText input = new EditText(getContext());

                    InputFilter[] filterArray = new InputFilter[1];
                    filterArray[0] = new InputFilter.LengthFilter(4);
                    input.setFilters(filterArray);

                    input.setHint("1800");
                    input.setHintTextColor(Color.parseColor("#B0BEC5"));
                    LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.MATCH_PARENT);
                    input.setLayoutParams(lp);
                    input.setInputType(InputType.TYPE_CLASS_NUMBER);
                    input.setFocusable(true);

                    InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);

                    AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
                    builder.setTitle(R.string.dialog_norma)
                            .setView(input)
                            .setCancelable(true)
                            .setPositiveButton(R.string.dialog_ok,
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            dialog.cancel();

                                            InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                                            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);

                                            if (input.length() == 0) {
                                                input.setText(R.string.text_Norma);
                                                textNorma.setText(input.getText().toString());
                                            } else if (Integer.parseInt(input.getText().toString()) <= 0) {
                                                input.setText(R.string.text_Norma);
                                                textNorma.setText(input.getText().toString());
                                            } else {
                                                textNorma.setText(input.getText().toString());
                                            }
                                            SharedPreferences.Editor editorNorma = sp.edit();
                                            editorNorma.putString("textNormaWater", textNorma.getText().toString());
                                            editorNorma.apply();
                                        }
                                    })
                            .setNegativeButton(R.string.dialog_cancel,
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            dialog.cancel();

                                            InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                                            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);

                                        }
                                    });
                    AlertDialog alert = builder.create();
                    alert.show();
                }
            }
        });

        cardTrening.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (switchAutoCalc.isChecked()) {

                    final EditText input = new EditText(getContext());

                    InputFilter[] filterArray = new InputFilter[1];
                    filterArray[0] = new InputFilter.LengthFilter(3);
                    input.setFilters(filterArray);

                    input.setHint("450");
                    input.setHintTextColor(Color.parseColor("#B0BEC5"));
                    LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.MATCH_PARENT);
                    input.setLayoutParams(lp);
                    input.setInputType(InputType.TYPE_CLASS_NUMBER);
                    input.setFocusable(true);

                    InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);

                    AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
                    builder.setTitle(R.string.dialog_trening)
                            .setView(input)
                            .setCancelable(true)
                            .setPositiveButton(R.string.dialog_ok,
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            dialog.cancel();

                                            InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                                            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);

                                            if (input.length() == 0) {
                                                input.setText(R.string.text_Trening);
                                                textTrening.setText(input.getText().toString());
                                            } else if (Integer.parseInt(input.getText().toString()) <= 0) {
                                                input.setText(R.string.text_Trening);
                                                textTrening.setText(input.getText().toString());
                                            } else {
                                                textTrening.setText(input.getText().toString());
                                            }
                                            SharedPreferences.Editor editorSportWater = sp.edit();
                                            editorSportWater.putString("textSportWater", textTrening.getText().toString());
                                            editorSportWater.apply();
                                        }
                                    })
                            .setNegativeButton(R.string.dialog_cancel,
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            dialog.cancel();

                                            InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                                            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);

                                        }
                                    });
                    AlertDialog alert = builder.create();
                    alert.show();
                }
            }
        });

        switchAutoCalc.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (switchAutoCalc.isChecked()) {
                    textNormaHead.setTextColor(Color.parseColor(("#000000")));
                    textNorma.setTextColor(Color.parseColor("#000000"));
                    textNormaMera.setTextColor(Color.parseColor("#000000"));
                    textTreningHead.setTextColor(Color.parseColor("#000000"));
                    textTreningBefore.setTextColor(Color.parseColor("#000000"));
                    textTrening.setTextColor(Color.parseColor("#000000"));
                    textTreningMera.setTextColor(Color.parseColor("#000000"));
                } else {
                    textNormaHead.setTextColor(Color.parseColor(("#B0BEC5")));
                    textNorma.setTextColor(Color.parseColor("#B0BEC5"));
                    textNormaMera.setTextColor(Color.parseColor("#B0BEC5"));
                    textTreningHead.setTextColor(Color.parseColor("#B0BEC5"));
                    textTreningBefore.setTextColor(Color.parseColor("#B0BEC5"));
                    textTrening.setTextColor(Color.parseColor("#B0BEC5"));
                    textTreningMera.setTextColor(Color.parseColor("#B0BEC5"));
                }
                SharedPreferences.Editor editorAutoCalc = sp.edit();
                editorAutoCalc.putBoolean("switchAutoCalc", switchAutoCalc.isChecked());
                editorAutoCalc.apply();
            }
        });
*/

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        switchIcons = view.findViewById(R.id.switchIcons);
        textPol = view.findViewById(R.id.textPol);
        textNormaHead = view.findViewById(R.id.textNormaHead);
        textNorma = view.findViewById(R.id.textNorma);
        textNormaMera = view.findViewById(R.id.textNormaMera);
        textTreningBefore = view.findViewById(R.id.textTreningBefore);
        textTreningMera = view.findViewById(R.id.textTreningMera);
        textTreningHead = view.findViewById(R.id.textTreningHead);
        textTrening = view.findViewById(R.id.textTrening);
        switchAutoCalc = view.findViewById(R.id.switchAutoCalc);
        cardPol = view.findViewById(R.id.cardPol);
        cardVes = view.findViewById(R.id.cardVes);
        textVes = view.findViewById(R.id.textVes);
        cardNorma = view.findViewById(R.id.cardNorma);
        cardTrening = view.findViewById(R.id.cardTrening);
        cardResolution = view.findViewById(R.id.cardResolution);
        cardTerms = view.findViewById(R.id.cardTerms);

        String female = Objects.requireNonNull(getActivity()).getResources().getString(R.string.text_Pol_F);
        String male = Objects.requireNonNull(getActivity()).getResources().getString(R.string.text_Pol_M);
        mPol = new String[]{female, male};

        cardResolution.setOnClickListener(view1 -> {
            //        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://sites.google.com/view/simpleproject-app"));
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://sites.google.com/view/simple-project/reminder-to-drink-water-on-time/"));
            startActivity(browserIntent);
        });

        cardTerms.setOnClickListener(v -> {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://sites.google.com/view/simple-project/reminder-to-drink-water-on-time/"));
            startActivity(browserIntent);
        });

        switch (sp.getString("listPol", textPol.getText().toString())) {
            case "female":
                textPol.setText(R.string.text_Pol_F);
                break;
            case "male":
                textPol.setText(R.string.text_Pol_M);
                break;
            default:
                textPol.setText(sp.getString("listPol", textPol.getText().toString()));
                break;
        }

        switchIcons.setChecked(sp.getBoolean("switchIcons", true));
        textVes.setText(sp.getString("textVes", textVes.getText().toString()));
        switchAutoCalc.setChecked(sp.getBoolean("switchAutoCalc", false));
        textNorma.setText(sp.getString("textNormaWater", textNorma.getText().toString()));
        textTrening.setText(sp.getString("textSportWater", textTrening.getText().toString()));

        if (switchAutoCalc.isChecked()) {
            textNormaHead.setTextColor(Color.parseColor(("#000000")));
            textNorma.setTextColor(Color.parseColor("#000000"));
            textNormaMera.setTextColor(Color.parseColor("#000000"));
            textTreningHead.setTextColor(Color.parseColor("#000000"));
            textTreningBefore.setTextColor(Color.parseColor("#000000"));
            textTrening.setTextColor(Color.parseColor("#000000"));
            textTreningMera.setTextColor(Color.parseColor("#000000"));
        } else {

            textNormaHead.setTextColor(Color.parseColor(("#B0BEC5")));
            textNorma.setTextColor(Color.parseColor("#B0BEC5"));
            textNormaMera.setTextColor(Color.parseColor("#B0BEC5"));
            textTreningHead.setTextColor(Color.parseColor("#B0BEC5"));
            textTreningBefore.setTextColor(Color.parseColor("#B0BEC5"));
            textTrening.setTextColor(Color.parseColor("#B0BEC5"));
            textTreningMera.setTextColor(Color.parseColor("#B0BEC5"));


            if (sp.getString("listPol", "female").equals("female")) {
                textNorma.setText(String.valueOf(Integer.parseInt(textVes.getText().toString()) * 30));
                textTrening.setText(String.valueOf(Integer.parseInt(textVes.getText().toString()) * 30 / 4));
            } else {
                textNorma.setText(String.valueOf(Integer.parseInt(textVes.getText().toString()) * 33));
                textTrening.setText(String.valueOf(Integer.parseInt(textVes.getText().toString()) * 33 / 4));
            }
        }
        SharedPreferences.Editor editorNormaAndTrening = sp.edit();
        editorNormaAndTrening.putString("textNormaWater", textNorma.getText().toString());
        editorNormaAndTrening.putString("textSportWater", textTrening.getText().toString());
        editorNormaAndTrening.apply();



        switchIcons.setOnCheckedChangeListener((compoundButton, b) -> {
            SharedPreferences.Editor editorIcons = sp.edit();
            editorIcons.putBoolean("switchIcons", switchIcons.isChecked());
            editorIcons.apply();
        });

        cardPol.setOnClickListener(view12 -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
            builder.setItems(mPol, (dialogInterface, i) -> {
                textPol.setText(mPol[i]);
                SharedPreferences.Editor editorPol = sp.edit();
                switch (i) {
                    case 0:
                        editorPol.putString("listPol", "female");
                        if (!switchAutoCalc.isChecked()) {
                            textNorma.setText(String.valueOf(Integer.parseInt(textVes.getText().toString()) * 30));
                            textTrening.setText(String.valueOf(Integer.parseInt(textVes.getText().toString()) * 30 / 4));
                        }
                        break;
                    case 1:
                        editorPol.putString("listPol", "male");
                        if (!switchAutoCalc.isChecked()) {
                            textNorma.setText(String.valueOf(Integer.parseInt(textVes.getText().toString()) * 33));
                            textTrening.setText(String.valueOf(Integer.parseInt(textVes.getText().toString()) * 33 / 4));
                        }
                        break;
                }

                editorPol.putString("textNormaWater", textNorma.getText().toString());
                editorPol.putString("textSportWater", textTrening.getText().toString());
                editorPol.apply();
            })
                    .setCancelable(true);

            AlertDialog alert = builder.create();
            alert.show();
        });

        cardVes.setOnClickListener(view13 -> {

            final EditText input = new EditText(getContext());

            InputFilter[] filterArray = new InputFilter[1];
            filterArray[0] = new InputFilter.LengthFilter(3);
            input.setFilters(filterArray);

            input.setHint("60");
            input.setHintTextColor(Color.parseColor("#B0BEC5"));
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT);
            input.setLayoutParams(lp);

            input.setInputType(InputType.TYPE_CLASS_NUMBER);
            input.requestFocus();
            showKeyboardFrom(Objects.requireNonNull(getContext()));

            AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
            builder.setTitle(R.string.dialog_ves)
                    .setView(input)
                    .setCancelable(true)
                    .setPositiveButton(R.string.dialog_ok,
                            (dialog, id) -> {
                                hideKeyboardFrom(input.getContext(), input);
                                dialog.cancel();

                                if (input.length() == 0) {
                                    input.setText(R.string.text_Ves);
                                    textVes.setText(input.getText().toString());
                                } else if (Integer.parseInt(input.getText().toString()) <= 0) {
                                    input.setText(R.string.text_Ves);
                                    textVes.setText(input.getText().toString());
                                } else {
                                    textVes.setText(input.getText().toString());
                                }
                                SharedPreferences.Editor editorVes = sp.edit();
                                editorVes.putString("textVes", textVes.getText().toString());
                                editorVes.apply();
                                if (!switchAutoCalc.isChecked()) {

                                    if (sp.getString("listPol", textPol.getText().toString()).equals("female")) {
                                        textNorma.setText(String.valueOf(Integer.parseInt(input.getText().toString()) * 30));
                                        textTrening.setText(String.valueOf(Integer.parseInt(input.getText().toString()) * 30 / 4));

                                    } else {
                                        textNorma.setText(String.valueOf(Integer.parseInt(input.getText().toString()) * 33));
                                        textTrening.setText(String.valueOf(Integer.parseInt(input.getText().toString()) * 33 / 4));
                                    }
                                    SharedPreferences.Editor editorNormaAndTrening2 = sp.edit();
                                    editorNormaAndTrening2.putString("textNormaWater", textNorma.getText().toString());
                                    editorNormaAndTrening2.putString("textSportWater", textTrening.getText().toString());
                                    editorNormaAndTrening2.apply();

                                }
                                input.clearFocus();
                            })
                    .setNegativeButton(R.string.dialog_cancel,
                            (dialog, id) -> {
                                hideKeyboardFrom(input.getContext(), input);

                                dialog.cancel();
                            //    InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                            //    imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
                                //        imm.hideSoftInputFromWindow(input.getWindowToken(), 0);

                                input.clearFocus();
                            });
            AlertDialog alert = builder.create();
            alert.show();
        });

        cardNorma.setOnClickListener(view14 -> {

            if (switchAutoCalc.isChecked()) {

                final EditText input = new EditText(getContext());

                InputFilter[] filterArray = new InputFilter[1];
                filterArray[0] = new InputFilter.LengthFilter(4);
                input.setFilters(filterArray);

                input.setHint("1800");
                input.setHintTextColor(Color.parseColor("#B0BEC5"));
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT);
                input.setLayoutParams(lp);
                input.setInputType(InputType.TYPE_CLASS_NUMBER);
                input.requestFocus();
                showKeyboardFrom(Objects.requireNonNull(getContext()));

                AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
                builder.setTitle(R.string.dialog_norma)
                        .setView(input)
                        .setCancelable(true)
                        .setPositiveButton(R.string.dialog_ok,
                                (dialog, id) -> {
                                    hideKeyboardFrom(Objects.requireNonNull(getContext()), input);

                                    dialog.cancel();

                                //    InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                                //    imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);


                                    if (input.length() == 0) {
                                        input.setText(R.string.text_Norma);
                                        textNorma.setText(input.getText().toString());
                                    } else if (Integer.parseInt(input.getText().toString()) <= 0) {
                                        input.setText(R.string.text_Norma);
                                        textNorma.setText(input.getText().toString());
                                    } else {
                                        textNorma.setText(input.getText().toString());
                                    }
                                    SharedPreferences.Editor editorNorma = sp.edit();
                                    editorNorma.putString("textNormaWater", textNorma.getText().toString());
                                    editorNorma.apply();

                                    input.clearFocus();
                                })
                        .setNegativeButton(R.string.dialog_cancel,
                                (dialog, id) -> {
                                    hideKeyboardFrom(Objects.requireNonNull(getContext()), input);

                                    dialog.cancel();

                                //    InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                                //    imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);

                                    input.clearFocus();
                                });
                AlertDialog alert = builder.create();
                alert.show();
            }
        });

        cardTrening.setOnClickListener(view15 -> {

            if (switchAutoCalc.isChecked()) {

                final EditText input = new EditText(getContext());

                InputFilter[] filterArray = new InputFilter[1];
                filterArray[0] = new InputFilter.LengthFilter(3);
                input.setFilters(filterArray);

                input.setHint("450");
                input.setHintTextColor(Color.parseColor("#B0BEC5"));
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT);
                input.setLayoutParams(lp);

                input.setInputType(InputType.TYPE_CLASS_NUMBER);
                input.requestFocus();
                showKeyboardFrom(Objects.requireNonNull(getContext()));

            //    InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            //    imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);

                AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
                builder.setTitle(R.string.dialog_trening)
                        .setView(input)
                        .setCancelable(true)
                        .setPositiveButton(R.string.dialog_ok,
                                (dialog, id) -> {
                                    dialog.cancel();

                                    InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                                    imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);

                                    if (input.length() == 0) {
                                        input.setText(R.string.text_Trening);
                                        textTrening.setText(input.getText().toString());
                                    } else if (Integer.parseInt(input.getText().toString()) <= 0) {
                                        input.setText(R.string.text_Trening);
                                        textTrening.setText(input.getText().toString());
                                    } else {
                                        textTrening.setText(input.getText().toString());
                                    }
                                    SharedPreferences.Editor editorSportWater = sp.edit();
                                    editorSportWater.putString("textSportWater", textTrening.getText().toString());
                                    editorSportWater.apply();

                                    input.clearFocus();
                                })
                        .setNegativeButton(R.string.dialog_cancel,
                                (dialog, id) -> {

                                    dialog.cancel();

                                //    InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                                //    imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
                                    input.clearFocus();
                                });
                AlertDialog alert = builder.create();
                alert.show();
            }
        });

        switchAutoCalc.setOnCheckedChangeListener((compoundButton, b) -> {
            if (switchAutoCalc.isChecked()) {
                textNormaHead.setTextColor(Color.parseColor(("#000000")));
                textNorma.setTextColor(Color.parseColor("#000000"));
                textNormaMera.setTextColor(Color.parseColor("#000000"));
                textTreningHead.setTextColor(Color.parseColor("#000000"));
                textTreningBefore.setTextColor(Color.parseColor("#000000"));
                textTrening.setTextColor(Color.parseColor("#000000"));
                textTreningMera.setTextColor(Color.parseColor("#000000"));
            } else {
                textNormaHead.setTextColor(Color.parseColor(("#B0BEC5")));
                textNorma.setTextColor(Color.parseColor("#B0BEC5"));
                textNormaMera.setTextColor(Color.parseColor("#B0BEC5"));
                textTreningHead.setTextColor(Color.parseColor("#B0BEC5"));
                textTreningBefore.setTextColor(Color.parseColor("#B0BEC5"));
                textTrening.setTextColor(Color.parseColor("#B0BEC5"));
                textTreningMera.setTextColor(Color.parseColor("#B0BEC5"));

                if (sp.getString("listPol", "female").equals("female")) {
                    textNorma.setText(String.valueOf(Integer.parseInt(textVes.getText().toString()) * 30));
                    textTrening.setText(String.valueOf(Integer.parseInt(textVes.getText().toString()) * 30 / 4));
                } else {
                    textNorma.setText(String.valueOf(Integer.parseInt(textVes.getText().toString()) * 33));
                    textTrening.setText(String.valueOf(Integer.parseInt(textVes.getText().toString()) * 33 / 4));
                }
            }
            SharedPreferences.Editor editorNormaAndTrening1 = sp.edit();
            editorNormaAndTrening1.putString("textNormaWater", textNorma.getText().toString());
            editorNormaAndTrening1.putString("textSportWater", textTrening.getText().toString());
            editorNormaAndTrening1.apply();

            SharedPreferences.Editor editorAutoCalc = sp.edit();
            editorAutoCalc.putBoolean("switchAutoCalc", switchAutoCalc.isChecked());
            editorAutoCalc.apply();
        });
    }

    public static void showKeyboardFrom(Context context){
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
    }

    public static void hideKeyboardFrom(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    //    imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
    }
}