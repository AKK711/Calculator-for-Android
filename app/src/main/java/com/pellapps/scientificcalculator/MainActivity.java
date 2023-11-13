package com.pellapps.scientificcalculator;



import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;


import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {
        private CalculatorDatabaseHelper db;
        private AdView mAdView;
        Button b1,b2,b3,b4,b5,b6,b7,b8,b9,b0,bdot,bh;
        Button bmplus,bmmin,bequal,bpi,bplus,bmin,bln,bdiv,bmul,binv,bsqrt,bsquare,bfact,blog,btan,bsin,bcos,bb1,bmode,bac,be;
        EditText tvmain;

        TextView tvsec;

        View layout,l0;
        View ll1,ll2,ll3,ll4,ll5,ll6,ll7;

        int fun = 0;  //0=rad 1=deg
        int clickcount = 0;

        double MAX_VALUE = 9223372036854775807.0;

        ArrayList <Double> listmin = new ArrayList<Double>();

        ArrayList <Double> list = new ArrayList<Double>();
        ImageButton bc;
        static Double pi = 3.1415926535897932384626433832795028841971693993751058209749445923078164062862089986280348253421170679;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //mobileads
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        // object of database helper class
        db= new CalculatorDatabaseHelper(this);


        // set mapping
        layout = findViewById(R.id.layout);
        l0 = findViewById(R.id.l0);

        ll1 = findViewById(R.id.ll1);
        ll2 = findViewById(R.id.ll2);
        ll3 = findViewById(R.id.ll3);
        ll4 = findViewById(R.id.ll4);
        ll5 = findViewById(R.id.ll5);
        ll6 = findViewById(R.id.ll6);
        ll7 = findViewById(R.id.ll7);


        b1 = findViewById(R.id.b1);
        b2 = findViewById(R.id.b2);
        b3 = findViewById(R.id.b3);
        b4 = findViewById(R.id.b4);
        b5 = findViewById(R.id.b5);
        b6 = findViewById(R.id.b6);
        b7 = findViewById(R.id.b7);
        b8 = findViewById(R.id.b8);
        b9 = findViewById(R.id.b9);
        b0 = findViewById(R.id.b0);
        bh = findViewById(R.id.bh);
        bh.setTextColor(getResources().getColor(R.color.blue));

        bdot = findViewById(R.id.bdot);
        bpi = findViewById(R.id.bpi);
        bmplus = findViewById(R.id.bmplus);
        bmmin = findViewById(R.id.bmmin);
        bequal = findViewById(R.id.bequal);
        bplus = findViewById(R.id.bplus);
        bmin = findViewById(R.id.bmin);
        bmul = findViewById(R.id.bmul);
        bdiv = findViewById(R.id.bdiv);
        binv = findViewById(R.id.binv);
        bsqrt = findViewById(R.id.bsqrt);
        bsquare = findViewById(R.id.bsqaure);
        bfact = findViewById(R.id.bfact);
        bln = findViewById(R.id.bln);
        blog = findViewById(R.id.blog);
        btan = findViewById(R.id.btan);
        bsin = findViewById(R.id.bsin);
        bcos = findViewById(R.id.bcos);
        bb1 = findViewById(R.id.bb1);
        bc = findViewById(R.id.bc);
        bac = findViewById(R.id.bac);
        bmode = findViewById(R.id.bmode);
        be = findViewById(R.id.be);

        tvmain = findViewById(R.id.tvmain);
        tvmain.setShowSoftInputOnFocus(false);

        tvsec = findViewById(R.id.tvsec);
        tvsec.setMovementMethod(new ScrollingMovementMethod());
        tvsec.setShowSoftInputOnFocus(false);



        tvmain.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // do nothing
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String val = tvmain.getText().toString();
                if(val.contains("tan90")) tvsec.setText("Domain Error");
                else {
                    try {
                        String replacedstr = val.replace("÷", "/").replace("×", "*").replace("√", "sqrt").replace("sin^(-1)", "asin").replace("cos^(-1)", "acos").replace("tan^(-1)", "atan");

                        double result = eval(replacedstr, fun);

                        //check if number contains only 1 zero after decimal point
                        tvsec.setTextColor(getResources().getColor(R.color.grey));

                        if (result > MAX_VALUE) {
                            BigDecimal bigDecimalInput = new BigDecimal(result);
                            String scientificNotation = bigDecimalInput.toEngineeringString();
                            tvsec.setText(String.format("%s", scientificNotation));
                        } else if (result % 1 == 0) {
                            tvsec.setText(String.format("%s", (long) result));

                        } else {
                            tvsec.setText(String.format("%s", result));

                        }
                    } catch (Exception e) {
                        System.out.println("Error: " + e.getMessage());
                    }
                }

        }

            @Override
            public void afterTextChanged(Editable s) {
                // do nothing
            }
        });

        //check if system night mode is on
        if(isNightModeOn())
        {
            //m+ and m- button color
            if(getResources().getConfiguration().orientation != Configuration.ORIENTATION_LANDSCAPE)
            {
                bmplus.setTextColor(getResources().getColor(R.color.white));
                bmmin.setTextColor(getResources().getColor(R.color.white));

                bmplus.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.func_dark));
                bmmin.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.func_dark));
            }

            //change status bar color
            getWindow().setStatusBarColor(getResources().getColor(R.color.color_tv_main_dark));
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_IMMERSIVE);

            //change layout color
            l0.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.color_tv_main_dark));

            //check if device is in landscape mode
            if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)
            {
                ll1.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.layout_dark));
                ll2.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.layout_dark));
                ll3.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.layout_dark));
                ll4.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.layout_dark));

            }
            else {
                ll1.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.layout_dark));
                ll2.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.layout_dark));
                ll3.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.layout_dark));
                ll4.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.layout_dark));
                ll5.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.layout_dark));
                ll6.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.layout_dark));
                ll7.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.layout_dark));
            }

            layout.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.color_tv_main_dark));

            tvmain.setTextColor(getResources().getColor(R.color.white));
            tvsec.setTextColor(getResources().getColor(R.color.white));
            tvmain.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.color_tv_main_dark));
            tvsec.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.color_tv_main_dark));

            b0.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.black));
            b1.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.black));
            b2.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.black));
            b3.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.black));
            b4.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.black));
            b5.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.black));
            b6.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.black));
            b7.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.black));
            b8.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.black));
            b9.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.black));
            bdot.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.black));
            bpi.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.func_dark));
            bequal.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.equals_dark));
            bplus.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.func_dark));
            bmin.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.func_dark));
            bmul.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.func_dark));
            bdiv.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.func_dark));
            binv.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.func_dark));
            bsqrt.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.func_dark));
            bsquare.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.func_dark));
            bfact.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.func_dark));
            bln.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.func_dark));
            blog.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.func_dark));
            btan.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.func_dark));
            bsin.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.func_dark));
            bcos.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.func_dark));
            bb1.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.func_dark));
            bc.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.text_dark));
            bac.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.ac_dark));
            bmode.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.func_dark));
            bh.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.black));
            be.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.black));

            b0.setTextColor(getResources().getColor(R.color.text_dark));
            b1.setTextColor(getResources().getColor(R.color.text_dark));
            b2.setTextColor(getResources().getColor(R.color.text_dark));
            b3.setTextColor(getResources().getColor(R.color.text_dark));
            b4.setTextColor(getResources().getColor(R.color.text_dark));
            b5.setTextColor(getResources().getColor(R.color.text_dark));
            b6.setTextColor(getResources().getColor(R.color.text_dark));
            b7.setTextColor(getResources().getColor(R.color.text_dark));
            b8.setTextColor(getResources().getColor(R.color.text_dark));
            b9.setTextColor(getResources().getColor(R.color.text_dark));
            bh.setTextColor(getResources().getColor(R.color.teal_200));
            be.setTextColor(getResources().getColor(R.color.white));
            bdot.setTextColor(getResources().getColor(R.color.text_dark));
            bpi.setTextColor(getResources().getColor(R.color.white));
            bequal.setTextColor(getResources().getColor(R.color.white));
            bplus.setTextColor(getResources().getColor(R.color.white));
            bmin.setTextColor(getResources().getColor(R.color.white));
            bmul.setTextColor(getResources().getColor(R.color.white));
            bdiv.setTextColor(getResources().getColor(R.color.white));
            binv.setTextColor(getResources().getColor(R.color.white));
            bsqrt.setTextColor(getResources().getColor(R.color.white));
            bsquare.setTextColor(getResources().getColor(R.color.white));
            bfact.setTextColor(getResources().getColor(R.color.white));
            bln.setTextColor(getResources().getColor(R.color.white));
            blog.setTextColor(getResources().getColor(R.color.white));
            btan.setTextColor(getResources().getColor(R.color.white));
            bsin.setTextColor(getResources().getColor(R.color.white));
            bcos.setTextColor(getResources().getColor(R.color.white));
            bb1.setTextColor(getResources().getColor(R.color.white));
            bac.setTextColor(getResources().getColor(R.color.white));
            bmode.setTextColor(getResources().getColor(R.color.white));
        }

        //onclick listeners

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int cursorPos = tvmain.getSelectionStart();
                tvmain.getText().insert(cursorPos, "1");
                tvmain.setSelection(cursorPos + 1);
            }

        });

        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int cursorPos = tvmain.getSelectionStart();
                tvmain.getText().insert(cursorPos, "2");
                tvmain.setSelection(cursorPos + 1);
            }
        });
        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int cursorPos = tvmain.getSelectionStart();
                tvmain.getText().insert(cursorPos, "3");
                tvmain.setSelection(cursorPos + 1);
            }
        });
        b4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int cursorPos = tvmain.getSelectionStart();
                tvmain.getText().insert(cursorPos, "4");
                tvmain.setSelection(cursorPos + 1);
            }
        });
        b5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int cursorPos = tvmain.getSelectionStart();
                tvmain.getText().insert(cursorPos, "5");
                tvmain.setSelection(cursorPos + 1);
            }
        });
        b6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int cursorPos = tvmain.getSelectionStart();
                tvmain.getText().insert(cursorPos, "6");
                tvmain.setSelection(cursorPos + 1);
            }
        });
        b7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int cursorPos = tvmain.getSelectionStart();
                tvmain.getText().insert(cursorPos, "7");
                tvmain.setSelection(cursorPos + 1);
            }
        });
        b8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int cursorPos = tvmain.getSelectionStart();
                tvmain.getText().insert(cursorPos, "8");
                tvmain.setSelection(cursorPos + 1);
            }
        });
        b9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int cursorPos = tvmain.getSelectionStart();
                tvmain.getText().insert(cursorPos, "9");
                tvmain.setSelection(cursorPos + 1);
            }
        });
        b0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int cursorPos = tvmain.getSelectionStart();
                tvmain.getText().insert(cursorPos, "0");
                tvmain.setSelection(cursorPos + 1);
            }
        });


        bdot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String val = (String) tvmain.getText().toString();
                if(val.endsWith("."))
                {
                    System.out.println(". already present");
                }
                else
                {
                    int cursorPos = tvmain.getSelectionStart();
                    tvmain.getText().insert(cursorPos, ".");
                    tvmain.setSelection(cursorPos + 1);
                }
            }
        });

        bac.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (tvmain.getText().length() != 0)
                {
                    tvmain.setText("");
                    tvsec.setText("");
                }
                else {
                    System.out.println("No values to clear");
                }

            }

        });

        bc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String val = tvmain.getText().toString();
                int cursorPos = tvmain.getSelectionStart();
                if(val.length() >=1)
                {
                    try {
                        tvmain.getText().delete(cursorPos - 1, cursorPos);
                    }
                    catch (Exception e)
                    {
                        System.out.println("Error: " + e.getMessage());
                    }
                }
                else {
                    System.out.println("No values to clear");
                }

            }
        });

        bplus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int cursorPos = tvmain.getSelectionStart();
                tvmain.getText().insert(cursorPos, "+");
                tvmain.setSelection(cursorPos + 1);
            }
        });

        bmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int cursorPos = tvmain.getSelectionStart();
                tvmain.getText().insert(cursorPos, "-");
                tvmain.setSelection(cursorPos + 1);
            }
        });

        bmul.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int cursorPos = tvmain.getSelectionStart();
                tvmain.getText().insert(cursorPos, "×");
                tvmain.setSelection(cursorPos + 1);
            }
        });

        bdiv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int cursorPos = tvmain.getSelectionStart();
                tvmain.getText().insert(cursorPos, "÷");
                tvmain.setSelection(cursorPos + 1);
            }
        });

        bsqrt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int cursorPos = tvmain.getSelectionStart();
                tvmain.getText().insert(cursorPos, "√");
                tvmain.setSelection(cursorPos + 1);
            }
        });


        bb1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String str = tvmain.getText().toString();
                int openingBrackets = countOccurrences(str, "(");
                int closingBrackets = countOccurrences(str, ")");
                if (openingBrackets == closingBrackets || str.endsWith("(")) {
                    // insert opening bracket
                    int cursorPos = tvmain.getSelectionStart();
                    tvmain.getText().insert(cursorPos, "(");
                    tvmain.setSelection(cursorPos + 1);
                } else if (openingBrackets > closingBrackets && !str.endsWith("(")) {
                    // insert closing bracket
                    int cursorPos = tvmain.getSelectionStart();
                    tvmain.getText().insert(cursorPos, ")");
                    tvmain.setSelection(cursorPos + 1);
                }
            }
        });

        bmode.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View view) {
                ++clickcount;
                fun = clickcount % 2;
                String val = tvmain.getText().toString();
                String replacedstr = val.replace("÷", "/").replace("×", "*").replace("√", "sqrt").replace("sin^(-1)", "asin").replace("cos^(-1)", "acos").replace("tan^(-1)", "atan");
                boolean b = val.contains("sin") || val.contains("cos") || val.contains("tan");

                if(fun == 0)
                {
                    bmode.setText(R.string.Rad);
                    try {
                        if (b) {
                            double result = eval(replacedstr, fun);
                            if(val.contains("tan90")) tvsec.setText("Domain Error");
                            else tvsec.setText(String.format("%s", result));
                        }
                    }
                    catch (Exception e)
                    {
                        System.out.println("Error: " + e.getMessage());
                    }

                }
                else
                {
                    bmode.setText(R.string.Deg);
                    try {
                        if (b) {
                            double result = eval(replacedstr, fun);
                            if(val.contains("tan90")) tvsec.setText("Domain Error");
                            else tvsec.setText(String.format("%s", result));
                        }
                    }
                    catch (Exception e)
                    {
                        System.out.println("Error: " + e.getMessage());
                    }
                }
            }
        });

        bpi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int cursorPos = tvmain.getSelectionStart();
                tvmain.getText().insert(cursorPos, "π");
                tvmain.setSelection(cursorPos + 1);
            }
        });

        bsin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int cursorPos = tvmain.getSelectionStart();
                tvmain.getText().insert(cursorPos, "sin");
                tvmain.setSelection(cursorPos + 3);
            }
        });

        bcos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int cursorPos = tvmain.getSelectionStart();
                tvmain.getText().insert(cursorPos, "cos");
                tvmain.setSelection(cursorPos + 3);
            }
        });

        btan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int cursorPos = tvmain.getSelectionStart();
                tvmain.getText().insert(cursorPos, "tan");
                tvmain.setSelection(cursorPos + 3);
            }
        });

        binv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int cursorPos = tvmain.getSelectionStart();
                tvmain.getText().insert(cursorPos, "^(-1)");
                tvmain.setSelection(cursorPos + 5);
            }
        });

        bln.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int cursorPos = tvmain.getSelectionStart();
                tvmain.getText().insert(cursorPos, "ln");
                tvmain.setSelection(cursorPos + 2);
            }
        });

        blog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int cursorPos = tvmain.getSelectionStart();
                tvmain.getText().insert(cursorPos, "log");
                tvmain.setSelection(cursorPos + 3);
            }
        });

        bfact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int cursorPos = tvmain.getSelectionStart();
                tvmain.getText().insert(cursorPos, "!");
                tvmain.setSelection(cursorPos + 1);
            }
        });

        bsquare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int cursorPos = tvmain.getSelectionStart();
                tvmain.getText().insert(cursorPos, "^");
                tvmain.setSelection(cursorPos + 1);
            }
        });

        be.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int cursorPos = tvmain.getSelectionStart();
                tvmain.getText().insert(cursorPos, "e");
                tvmain.setSelection(cursorPos + 1);
            }
        });


        bequal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String val = tvmain.getText().toString();

                if(!list.isEmpty() || !listmin.isEmpty())
                {
                    list.add(0.0);
                    listmin.add(0.0);
                    double sumplus =0;
                    double summin =0;

                    try {
                        for (int i = 0; i < list.size(); i++) {
                            sumplus = sumplus + list.get(i);
                        }
                        for (int i = 0; i < listmin.size(); i++) {
                            summin = summin - listmin.get(i);
                        }
                        double result = (-summin) + sumplus;
                        list.clear();
                        listmin.clear();
                        if (result % 1 == 0) {
                            tvmain.setText(String.format("%s", (long) result));
                        } else {
                            tvmain.setText(String.format("%s", result));
                        }
                    } catch (Exception e) {
                        Toast toast = Toast.makeText(getApplicationContext(), "Invalid Expression", Toast.LENGTH_SHORT);
                        toast.show();
                    }

                }

                if(val.contains("tan90")) {
                    tvmain.setText("Domain Error");
                }
                else if(val.length()>=1) {
                    try {
                        String replacedstr = val.replace("÷", "/").replace("×", "*").replace("√", "sqrt").replace("sin^(-1)", "asin").replace("cos^(-1)", "acos").replace("tan^(-1)", "atan");
                        double result = eval(replacedstr, fun);

                        // check if result is too large
                        if(result > MAX_VALUE ) {
                            DecimalFormat decimalFormat = new DecimalFormat("0.#####E0");
                            tvmain.setText(decimalFormat.format(result));
                            db.insertHistory(val, decimalFormat.format(result));
                        } else if (result % 1 == 0) {
                            tvmain.setText(String.format("%s", (long) result));
                            db.insertHistory(val, String.format("%s", (long) result));
                        } else {
                            tvmain.setText(String.format("%s", result));
                            db.insertHistory(val, String.format("%s", result));
                        }

                        tvmain.setSelection(tvmain.getText().length());
                        if(!isNightModeOn()){
                            tvsec.setTextColor(getResources().getColor(R.color.black));
                        }
                        else{
                            tvsec.setTextColor(getResources().getColor(R.color.white));
                        }
                        tvsec.setText(val);
                    } catch (Exception e) {
                        Toast toast = Toast.makeText(getApplicationContext(), "Invalid Expression", Toast.LENGTH_SHORT);
                        System.out.println("Invalid Expression");
                        toast.show();
                    }
                }
                else {
                    System.out.println("Empty Expression");
                }

            }
        });

        if(getResources().getConfiguration().orientation != Configuration.ORIENTATION_LANDSCAPE) {

            bmplus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    String val = tvmain.getText().toString();
                    String replacedstr = val.replace("÷", "/").replace("×", "*").replace("√", "sqrt").replace("sin^(-1)", "asin").replace("cos^(-1)", "acos").replace("tan^(-1)", "atan");;

                    try {
                        double result = eval(replacedstr, fun);
                        list.add(result);
                        tvmain.setText("");
                    } catch (Exception e) {
                        System.out.println("Invalid Expression");
                    }
                }
            });

            bmmin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    String val = tvmain.getText().toString();
                    String replacedstr = val.replace("÷", "/").replace("×", "*").replace("√", "sqrt").replace("sin^(-1)", "asin").replace("cos^(-1)", "acos").replace("tan^(-1)", "atan");;

                    try {
                        double result = eval(replacedstr, fun);
                        listmin.add((-1)*(result));
                        tvmain.setText("");
                    } catch (Exception e) {
                        System.out.println("Invalid Expression");
                    }

                }
            });

        }

        bh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, History.class);
                startActivity(intent);
            }
        });

    }

    // night mode
    private boolean isNightModeOn() {
        int mode = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        return mode == Configuration.UI_MODE_NIGHT_YES;
    }

    // count occurrences of substring in string
    public int countOccurrences(String str, String substr) {
        return str.length() - str.replace(substr, "").length();
    }

    public static double factorial(double x)
    { try {
        if (x == 0) {
            return 1;
        } else {
            return x * factorial(x - 1);
        }
    }
    catch (Exception e) {
        System.out.println("Unexpected error in factorial");
    }
    return 0;
    }

    // eval function
    public static double eval (final String str, int fun)
    {
        return new Object() {
            int pos = -1, ch;

            void nextChar() {
                ch = (++pos < str.length()) ? str.charAt(pos) : -1;
            }

            boolean eat(int charToEat) {
                while (ch == ' ') nextChar();
                if (ch == charToEat) {
                    nextChar();
                    return true;
                }
                return false;
            }

            double parse() {
                nextChar();
                double x = parseExpression();
                if (pos < str.length()) throw new RuntimeException("Unexpected: " + (char)ch);
                return x;
            }

            // Grammar:
            // expression = term | expression `+` term | expression `-` term
            // term = factor | term `*` factor | term `/` factor
            // factor = `+` factor | `-` factor | `(` expression `)`
            //        | number | functionName factor | factor `^` factor

            double parseExpression() {
                double x = parseTerm();
                for (;;) {
                    if      (eat('+')) x += parseTerm(); // addition
                    else if (eat('-')) x -= parseTerm(); // subtraction
                    else return x;
                }
            }

            double parseTerm() {
                double x = parseFactor();
                for (;;) {
                    if      (eat('*')) x *= parseFactor(); // multiplication
                    else if (eat('/')) x /= parseFactor(); // division
                    else return x;
                }
            }

            double parseFactor() {
                if (eat('+')) return parseFactor(); // unary plus
                if (eat('-')) return -parseFactor(); // unary minus

                double x;
                int startPos = this.pos;
                if (eat('(')) { // parentheses
                    x = parseExpression();
                    eat(')');
                } else if ((ch >= '0' && ch <= '9') || ch == '.') { // numbers
                    while ((ch >= '0' && ch <= '9') || ch == '.') nextChar();
                    x = Double.parseDouble(str.substring(startPos, this.pos));
                } else if (ch == 'π') { // pi
                    nextChar();
                    if ((ch >= '0' && ch <= '9') || ch == '.') {
                        // If there is a number following π, parse it as a factor
                        startPos = this.pos;
                        while ((ch >= '0' && ch <= '9') || ch == '.') nextChar();
                        double factor = Double.parseDouble(str.substring(startPos, this.pos));
                        x = factor * Math.PI;
                    } else {
                        // Otherwise, treat π as 1π
                        x = Math.PI;
                    }
                }
                else if (ch == 'e') {
                    nextChar();
                    if ((ch >= '0' && ch <= '9') || ch == '.') {
                        // If there is a number following e, parse it as a factor
                        startPos = this.pos;
                        while ((ch >= '0' && ch <= '9') || ch == '.') nextChar();
                        double factor = Double.parseDouble(str.substring(startPos, this.pos));
                        x = factor * Math.E;
                    } else {
                        // Otherwise, treat e as 1e
                        x = Math.E;
                    }
                }
                else if (ch >= 'a' && ch <= 'z') { // functions
                    while (ch >= 'a' && ch <= 'z') nextChar();
                    String func = str.substring(startPos, this.pos);
                    x = parseFactor();
                    switch (func) {

                        case "sqrt":
                            x = Math.sqrt(x);
                            break;
                        case "sin":
                            if(fun==0) {
                                x = Math.sin(Math.toRadians(x));
                            }
                            else
                            {
                                x = Math.sin(x);
                            }
                            break;
                        case "cos":
                            if(fun==0) {
                                x = Math.cos(Math.toRadians(x));
                            }
                            else
                            {
                                x = Math.cos(x);
                            }
                            if(x==6.123233995736766E-17)
                            {
                                x=0;
                            }
                            break;
                        case "tan":

                            if(fun==0) {
                                x = Math.tan(Math.toRadians(x));
                            }
                            else
                            {
                                x = Math.tan(x);
                            }
                            break;
                        case "asin":
                            if(fun==0) {
                                x = Math.asin(x)*(180/Math.PI);
                            }
                            else
                            {
                                x = Math.asin(x);
                            }
                            break;
                        case "acos":
                            if(fun==0) {
                                x = Math.acos(x)*(180/Math.PI);
                            }
                            else
                            {
                                x = Math.acos(x);
                            }
                            break;
                        case "atan":
                            if(fun==0) {
                                x = Math.atan(x)*(180/Math.PI);
                            }
                            else
                            {
                                x = Math.atan(x);
                            }
                            break;
                        case "log":
                            x = Math.log10(x);
                            break;
                        case "ln":
                            x = Math.log(x);
                            break;
                        default:
                            throw new RuntimeException("Unknown function: " + func);
                    }
                } else {
                    throw new RuntimeException("Unexpected: " + (char)ch);
                }
                if(eat('e')) x = x * Math.E; // exponential

                if (eat('^')) x = Math.pow(x, parseFactor()); // exponentiation

                if(eat('π'))  x = x * Math.PI;  // pi

                if(eat('!')) {
                    if(x<1001) x = factorial(x);
                    else{
                        return 0;
                    }
                } // factorial

                return x;
            }
        }.parse();

    }

}