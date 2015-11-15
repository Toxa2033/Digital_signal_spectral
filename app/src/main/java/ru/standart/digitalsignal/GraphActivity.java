package ru.standart.digitalsignal;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;


import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TextView;

import com.jjoe64.graphview.GraphView;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Random;

public class GraphActivity extends AppCompatActivity implements View.OnClickListener {

    Button withNoiseGraph;
    Button onlyNoiseGraph;
    Button normalGraph;
    Button graphPower;
    Button amplitudeAnalisButton;
    Button phaseAnalisButton;
    Button spectralPowerButton;
    GraphView graph;
    GraphView graphAnalis;
    GraphView graphGistog;
    Button heningButton;
    Button porabolButton;
    Button firstButton;
    int durSignal=0;
    double[]signals;
    double[]t;
    double[]noise;
    double amplitudeOfNoise=0;
    final String TAB_1="tag1";
    final String TAB_2="tag2";
    final String TAB_3="tag3";
    final String TAB_4="tag4";
    final String TAB_5="tag5";
    final String TAB_6="tag6";

    GraphView graphSig1;
    GraphView graphSig2;
    GraphView graphFianl;

    TabHost tabs;
    double durationDescret; //частота дескретизации
    int N;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph);
        onlyNoiseGraph=(Button)findViewById(R.id.noiseOnlyGraph);
        withNoiseGraph=(Button)findViewById(R.id.withNoiseGraph);
        normalGraph=(Button)findViewById(R.id.normalGraph);
       // graphPower=(Button)findViewById(R.id.graphPower);
        graphAnalis=(GraphView)findViewById(R.id.graphAnaliz);
        amplitudeAnalisButton=(Button)findViewById(R.id.amplitudeFrequencyButton);
        phaseAnalisButton=(Button)findViewById(R.id.phaseFrequencyButton);
      /*   graphGistog=(GraphView)findViewById(R.id.graphGistogram);
         heningButton=(Button)findViewById(R.id.hanningButton);
         porabolButton=(Button)findViewById(R.id.parobalButton);
         firstButton=(Button)findViewById(R.id.firstButton);
          graphSig1=(GraphView)findViewById(R.id.graphSig1);
          graphSig2=(GraphView)findViewById(R.id.graphSig2);
          graphFianl=(GraphView)findViewById(R.id.graphFinal);*/


         tabs=(TabHost)findViewById(R.id.tabHost);
        tabs.setup();

        TabHost.TabSpec spec = tabs.newTabSpec(TAB_1);

        spec.setContent(R.id.tab1);
        spec.setIndicator("Сигналы");
        tabs.addTab(spec);

        /*spec = tabs.newTabSpec(TAB_2);
        spec.setContent(R.id.tab2);
        spec.setIndicator("Расчеты");
        tabs.addTab(spec);*/

        spec = tabs.newTabSpec(TAB_3);
        spec.setContent(R.id.tab3);
        spec.setIndicator("Анализ");
        tabs.addTab(spec);

        /*spec = tabs.newTabSpec(TAB_4);
        spec.setContent(R.id.tab4);
        spec.setIndicator("АЧХ фильтр");
        tabs.addTab(spec);*/



        tabs.setCurrentTab(0);

        tabs.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {
                if (tabId.equals(TAB_2)) {
                 /*   TextView midPower = (TextView) findViewById(R.id.midPower);
                    TextView signalEnergy = (TextView) findViewById(R.id.signalEnergy);
                    TextView maxAmplitude = (TextView) findViewById(R.id.maxAmplitude);
                    midPower.setText(String.valueOf(getMidPower()));
                    signalEnergy.setText(String.valueOf(getSignalEnergy()));
                    maxAmplitude.setText(String.valueOf(getMaxAmplitude()));
                    TextView mathMid = (TextView) findViewById(R.id.mathMid);
                    TextView dispers = (TextView) findViewById(R.id.dispersion);
                    TextView standartDeviation = (TextView) findViewById(R.id.standartDeviation);
                    standartDeviation.setText(String.valueOf(getStandartDeviation()));
                    dispers.setText(String.valueOf(getDispers()));
                    mathMid.setText(String.valueOf(getMathMid()));*/

                }
            }
        });

        Intent intent =getIntent();
        signals=intent.getDoubleArrayExtra(MainActivity.ATTRIBUT_SIGNAL);
        t=intent.getDoubleArrayExtra(MainActivity.ATTRIBUT_t);
        durSignal=intent.getIntExtra(MainActivity.ATTRIBUT_DURR_SIGNAL, durSignal);
        amplitudeOfNoise=intent.getDoubleExtra(MainActivity.ATTRIBUT_AMPLITUDE_OF_NOISE, amplitudeOfNoise);
        durationDescret=intent.getDoubleExtra(MainActivity.ATTRIBUT_DURATION_DESCRET, durationDescret);
        N=signals.length;

        graph = (GraphView) findViewById(R.id.graph);
        graph.addSeries(createDataPoint(t, signals,Color.BLUE));
        graph.getViewport().setXAxisBoundsManual(true);
        graph.getViewport().setScrollable(true);
        graph.getViewport().setScalable(true);
        withNoiseGraph.setOnClickListener(this);
        onlyNoiseGraph.setOnClickListener(this);
        normalGraph.setOnClickListener(this);
        //graphPower.setOnClickListener(this);
        amplitudeAnalisButton.setOnClickListener(this);
        phaseAnalisButton.setOnClickListener(this);
       /* heningButton.setOnClickListener(this);
        porabolButton.setOnClickListener(this);
        firstButton.setOnClickListener(this);*/
    }

    LineGraphSeries<DataPoint> createDataPoint(double[]x, double[]y, int color)
    {
        DataPoint[] dataPoint=new DataPoint[x.length];
        for(int i=0; i<x.length;i++)
        {
            dataPoint[i]=new DataPoint(x[i],y[i]);
        }

        LineGraphSeries<DataPoint> series = new LineGraphSeries<DataPoint>(dataPoint);
        series.setColor(color);
        return series;
    }


    //массив частот

    double[] getArrFrequency()
    {
        double dF=1/durSignal;
       // int size=(int)Math.floor(durationDescret/dF)-1;
        double[] result=new double[(int)MainActivity.N];
        int i=0;
        for(int j=0; j<MainActivity.N; j++)
        {
            result[j]=j*dF;
        }

        return result;
    }

    void getDPF(double[] amplSpectr, double[] phaseSpectr, double[]signals)
    {

        double[] im=new double[N];
        double[] re=new double[N];
        for(int i=0; i<N;i++) {
            im[i]=0;
            re[i]=0;
            for(int j=0; j<N;j++) {
                im[i]=im[i]+signals[j]*Math.sin(2*Math.PI*j*i/N);
                re[i]=re[i]+signals[j] * Math.cos(2*Math.PI*j*i/N);
            }

            if(i==0)
            {
                im[i]=new BigDecimal(im[i]/N).setScale(6, RoundingMode.HALF_UP).doubleValue();
                re[i]=new BigDecimal(re[i]/N).setScale(6, RoundingMode.HALF_UP).doubleValue();

            }
            else
            {
                im[i]=new BigDecimal(2*im[i]/N).setScale(6, RoundingMode.HALF_UP).doubleValue();
                re[i]=new BigDecimal(2*re[i]/N).setScale(6, RoundingMode.HALF_UP).doubleValue();
            }
            amplSpectr[i]=Math.sqrt(Math.pow(re[i], 2) + Math.pow(im[i], 2));
            if(re[i]!=0.0) {
                phaseSpectr[i] = -Math.atan(im[i] / re[i]);
            }
            else
            {
                phaseSpectr[i]=0;
            }
            phaseSpectr[i]=phaseSpectr[i]/180*Math.PI;
        }

    }




    double getMathMid()
    {
        double[]signalWithNoise=getNoisesSignal();
        double result=0;

        for(int i=0; i<signalWithNoise.length; i++)
        {
            result+=signalWithNoise[i];
        }

        return result/signalWithNoise.length;
    }

    double getDispers()
    {
        double[]signalWithNoise=getNoisesSignal();
        double result=0;

        for(int i=0; i<signalWithNoise.length; i++)
        {
            result+=Math.pow(signalWithNoise[i],2);
        }
        result=result/signalWithNoise.length;
        result-=Math.pow(getMathMid(),2);
        return result;
    }

    double getStandartDeviation()
    {
        return new BigDecimal(Math.sqrt(getDispers())).setScale(2, RoundingMode.UP).doubleValue();

    }

    double getMidPower()
    {
        double result=0;
        result= getSignalEnergy();
        return result/signals.length;
    }

    double getMaxAmplitude()
    {
        double max=0;
        for(int i=0; i<signals.length; i++)
        {
            double max_t=signals[i];
            for (int j=i; j<signals.length; j++)
            {
                if(signals[j]>max_t)
                {
                    max=signals[j];
                }
            }
        }

        return max;
    }

    double getSignalEnergy()
    {
        double result=0;
        for(int i=0; i<signals.length; i++)
        {
            result+=Math.pow(signals[i],2);
        }
        return result;
    }

    double[]getNoisesSignal()
    {
        double[]noiseSignal=new double[t.length];

        for(int i=0; i<t.length; i++)
        {
            noiseSignal[i]=noise[i]+signals[i];
        }

        return noiseSignal;
    }

    void generateNoise()
    {
        if(noise==null) {
            noise = new double[t.length];
            Random random = new Random();
            for (int i = 0; i < t.length; i++) {
                noise[i] = -amplitudeOfNoise + (amplitudeOfNoise - (-amplitudeOfNoise)) * random.nextDouble();
            }
        }
    }

    double[]getCurrentPower()
    {
        double[]currPower=new double[t.length];

        for(int i=0; i<t.length; i++)
        {
            currPower[i]=Math.pow(signals[i],2);
        }

        return currPower;

    }

    double chastotaSreza=10;
    double r=0.5;
    int a0;
    int a1;
    int a2;
    int b0;
    double b1;
    int b2;
    double[] sigFiltr;

    double[] getHennigFiltr()
    {
        sigFiltr=new double[signals.length];
         a0=1/4;
         a1=1/2;
         a2=1/4;
         b0=1;
         b1=0;
         b2=0;
        for (int i=2; i<signals.length; i++)
        {
            sigFiltr[i]=a0*signals[i]+a1*signals[i-1]+a2*signals[i-2];
        }
        return sigFiltr;
    }

    double[] getPorabolFiltr()
    {
        sigFiltr=new double[signals.length];

        for(int i=2; i<signals.length-2; i++)
    {
        sigFiltr[i]=(-3*signals[i-2]+12*signals[i-1]+17*signals[i]+12*signals[i+1]-3*signals[i+2])/35;
    }
        return sigFiltr;
    }


    double[] getFirstFiltr()
    {
        sigFiltr=new double[signals.length];

        a0=1;
        a1=0;
        a2=0;
        b0=1;
        b1=-r;
        b2=0;
        for (int i=1; i<signals.length; i++)
        {
            sigFiltr[i]=a0*signals[i]-b1*sigFiltr[i-1];
        }

        return sigFiltr;
    }

    double[] getACHFiltr()
    {
        double[]ax=new double[signals.length];

        for (int i=0; i<signals.length; i++)
        {
            double acos=a0+a1*Math.cos(1 * 2 * Math.PI * i / MainActivity.N)+a2*Math.cos(2 * 2 * Math.PI * i / MainActivity.N);
            double asin=  a1*Math.sin(1 * 2 * Math.PI * i / MainActivity.N)+a2*Math.sin(2 * 2 * Math.PI * i / MainActivity.N);
            double bcos=1+b1*Math.cos(1 * 2 * Math.PI * i / MainActivity.N)+b2*Math.cos(2 * 2 * Math.PI * i / MainActivity.N);
            double bsin=  b1*Math.sin(1 * 2 * Math.PI * i / MainActivity.N)+b2*Math.sin(2 * 2 * Math.PI * i / MainActivity.N);
            ax[i]=Math.sqrt((Math.pow(acos,2)+Math.pow(asin,2))/(Math.pow(bcos,2)+Math.pow(bsin,2)));
        }
        return ax;
    }


    @Override
    public void onClick(View v) {
        double[]amplSpectr=new double[N];
        double[]phaseSpectr=new double[N];
        switch (v.getId())
        {
            case R.id.withNoiseGraph:
            {
                graph.removeAllSeries();
                generateNoise();
                graph.addSeries(createDataPoint(t,getNoisesSignal(),Color.BLUE));
                break;
            }

            case R.id.noiseOnlyGraph:
            {
                graph.removeAllSeries();
                generateNoise();
                graph.addSeries(createDataPoint(t,noise,Color.BLUE));
                break;
            }

            case R.id.normalGraph:
            {
                graph.removeAllSeries();
                graph.addSeries(createDataPoint(t,signals,Color.BLUE));
                break;
            }

          /*  case R.id.graphPower:
            {
                graph.removeAllSeries();
                graph.addSeries(createDataPoint(t,getCurrentPower(),Color.BLUE));
                tabs.setCurrentTab(0);
                break;
            }*/

            case R.id.amplitudeFrequencyButton:
            {
                graphAnalis.removeAllSeries();
                getDPF(amplSpectr, phaseSpectr,signals);
                graphAnalis.addSeries(createDataPoint(getArrFrequency(),amplSpectr,Color.BLUE));
                break;
            }

            case R.id.phaseFrequencyButton:
            {
                graphAnalis.removeAllSeries();
                getDPF(amplSpectr, phaseSpectr,signals);
                graphAnalis.addSeries(createDataPoint(getArrFrequency(), phaseSpectr,Color.BLUE));

            }
           /* case R.id.hanningButton:
            {
                graphGistog.removeAllSeries();
                graphAnalis.removeAllSeries();
                graph.removeAllSeries();
                graph.addSeries(createDataPoint(t, getHennigFiltr(),Color.BLUE));
                getDPF(amplSpectr, phaseSpectr, sigFiltr);
                graphGistog.addSeries(createDataPoint(getArrFrequency(),getACHFiltr(),Color.BLUE));

            }
            case R.id.parobalButton:
            {
                graphGistog.removeAllSeries();
                graphAnalis.removeAllSeries();
                graph.removeAllSeries();
                graph.addSeries(createDataPoint(t,getPorabolFiltr(),Color.BLUE));
                getDPF(amplSpectr, phaseSpectr,sigFiltr);
                graphGistog.addSeries(createDataPoint(getArrFrequency(),getACHFiltr(),Color.BLUE));
            }

            case R.id.firstButton:
            {
                graphGistog.removeAllSeries();
                graphAnalis.removeAllSeries();
                graph.removeAllSeries();
                graph.addSeries(createDataPoint(t,getFirstFiltr(),Color.BLUE));
                getDPF(amplSpectr, phaseSpectr,sigFiltr);
                graphGistog.addSeries(createDataPoint(getArrFrequency(),getACHFiltr(),Color.BLUE));
            }*/

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_graph, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
