package com.example.bluetoothremote;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.telephony.RadioAccessSpecifier;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.KeyListener;
import android.util.Log;
import android.view.DragEvent;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.larswerkman.holocolorpicker.ColorPicker;
import com.larswerkman.holocolorpicker.SaturationBar;
import com.skydoves.colorpickerview.ColorEnvelope;
import com.skydoves.colorpickerview.ColorPickerView;
import com.skydoves.colorpickerview.listeners.ColorEnvelopeListener;
import com.skydoves.colorpickerview.listeners.ColorListener;

import org.w3c.dom.Text;

import java.io.OutputStream;

public class Selection_screen extends AppCompatActivity {

    RadioGroup colors;
    int[][] rgb = new int[5][3];
   ColorPicker picker;
    //ColorPickerView picker;
    SaturationBar bar;
    TextView[] images;
    TextView bright;
    RadioGroup commands;
    SeekBar brightbar;
    int brightness = 0;
    int selectedid =0;
    EditText time_txt;
    char command ='S';
    util utility = new util();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selection_screen);
        time_txt = findViewById(R.id.time_txt);
        commands = findViewById(R.id.commands);
        picker = findViewById(R.id.picker);
        colors = findViewById(R.id.colorgroup);
        bright = findViewById(R.id.brightness_text);
        brightbar = findViewById(R.id.brightnessbar);
        images = new TextView[5];
        images[0] = findViewById(R.id.colorimage1);
        images[1] = findViewById(R.id.colorimage2);
        images[2] = findViewById(R.id.colorimage3);
        images[3] = findViewById(R.id.colorimage4);
        images[4] = findViewById(R.id.colorimage5);
        bar = findViewById(R.id.saturationbar);
        selectedid = 0;
        picker.addSaturationBar(bar);
        picker.setShowOldCenterColor(false);


        //set commands
        for(int c = 0; c<4; c++)
        {

            commands.getChildAt(c).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    command  = ((RadioButton) findViewById(view.getId())).getText().charAt(0);
                    utility.write(getApplication(), makestring());
                    Log.d("msg", command+"");
                }
            });
        }
        //final CharSequence[] temp = new CharSequence[1];

        time_txt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(b)
                {if(Integer.parseInt(time_txt.getText().toString()) <20 || time_txt.getText().toString().length()>4)
                {
                    time_txt.setText("40");

                    Toast.makeText(getApplicationContext(),"Value is too low or too high!",Toast.LENGTH_SHORT).show();
                }

                }
            }
        });
                    brightbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                        @Override
                        public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                            bright.setText("Brightness: " + i);
                            brightness = i;
                        }

                        @Override
                        public void onStartTrackingTouch(SeekBar seekBar) {

                        }

                        @Override
                        public void onStopTrackingTouch(SeekBar seekBar) {

                        }
                    });

        picker.setOnColorChangedListener(new ColorPicker.OnColorChangedListener(){
            @RequiresApi(api = Build.VERSION_CODES.O)
            public void onColorChanged(int color)
            {

               Color e  = Color.valueOf(color);
               Log.d("msg", (int)(255*e.red())+" "+(int) (255*e.green())+" "+(int) (e.blue()*255));
                rgb[selectedid][2] = (int) (e.blue()*255);
                rgb[selectedid][0] = (int) (255*e.red());
                rgb[selectedid][1] = (int) (255*e.green());
                setimagescolor();
                utility.write(getApplicationContext(), makestring());

            }

        });



            for(int c = 0; c<5; c++){
                RadioButton e = (RadioButton) colors.getChildAt(0);
                   e.setChecked(true);
                    colors.getChildAt(c).setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    RadioButton c = findViewById(v.getId());
                    setcolor(c);


                }
            });
        }

        for(int c= 0; c<5; c++) {
            for (int d = 0; d < 3; d++) {
                rgb[c][d] = 0;
            }
        }


            String read = utility.read(getApplicationContext());
            int childat = 2;
            if(read!=null)
            {
                switch(read.charAt(0))
                {
                    case 'W': childat = 0; break;
                    case 'F': childat = 1; break;
                    case 'S': childat = 2; break;
                    case 'P': childat = 3; break;

                }
                command = read.charAt(0);
                brightness =  ((read.charAt(1)-'0')*100) + ((read.charAt(2)-'0')*10)+ ((read.charAt(3)-'0'));
                brightbar.setProgress(brightness);
                bright.setText("Brightness: "+brightness);

                for(int c = 0; c<5; c++)
                {
                    for(int d = 0; d<3; d++)
                    {
                        rgb[c][d]  =  (read.charAt(c*9+d*3+4)-'0')*100 + (read.charAt(c*9+d*3+5)-'0')*10 + read.charAt(c*9+d*3+6)-'0';
                    }
                }
            }


        RadioButton stat = (RadioButton) commands.getChildAt(childat);
        stat.setChecked(true);

       setimagescolor();


    }

    public void setimagescolor()
    {

        for(int c = 0; c<5; c++)
            if(rgb[c][0]==0 && rgb[c][1]==0 && rgb[c][2] ==0)
            {
                images[c].setBackgroundColor(Color.rgb(255,255,255 ));
            }
        else
            images[c].setBackgroundColor(Color.rgb(rgb[c][0], rgb[c][1], rgb[c][2] ));
    }


    public void setcolor(RadioButton id)
    {
        String c = id.getText().toString();

        selectedid = c.charAt(c.length()-1) - '0'-1;
        picker.setColor(Color.rgb(rgb[selectedid][0], rgb[selectedid][1], rgb[selectedid][2]));

      Log.d("msg", c);
    }

    public void send(View v)
    {

            String built = makestring();

            Log.d("msg", built);
            if(!utility.wrte(getApplicationContext(), built+'#'))
            {
                utility.quit(getApplicationContext());
                super.finish();
            }


    }

    public void clear(View v)
    {
        rgb[selectedid][0] = 0;
        rgb[selectedid][1] = 0;
        rgb[selectedid][2] = 0;
        setimagescolor();
    }

    public void quit(View v)
    {

        utility.quit(getApplicationContext());
        super.finish();
    }

    public String makestring()
    {

        String build="";
        if(Integer.toString(brightness).length()==3)
        {
            build+=brightness;
        }
        else if(Integer.toString(brightness).length() == 2)
        {
            build+="0"+brightness;
        }
        else if(Integer.toString(brightness).length() == 1)
        {
            build+="00"+brightness;
        }
        for(int e = 0; e<5; e++)
        {
            for(int d = 0; d<3; d++)
            {
                if(Integer.toString(rgb[e][d]).length()==3)
                {
                    build+=rgb[e][d];
                }
                else if(Integer.toString(rgb[e][d]).length() == 2)
                {
                    build+="0"+(rgb[e][d]);
                }
                else if(Integer.toString(rgb[e][d]).length() == 1)
                {
                    build+="00"+(rgb[e][d]);
                }
            }
        }

        return command+build;
    }
    @Override
    public void onBackPressed() {
        utility.quit(getApplicationContext());
      super.finish();
    }

    public void time(View v)
    {
        int timee = Integer.parseInt(time_txt.getText().toString());
        utility.wrtespec(getApplicationContext(), "T"+time_txt.getText().toString()+"#" );

    }




}