package ru.standart.digitalsignalSpect;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;


import android.widget.TabHost;

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
         graphGistog=(GraphView)findViewById(R.id.graphGistogram);
         heningButton=(Button)findViewById(R.id.hanningButton);
         porabolButton=(Button)findViewById(R.id.parobalButton);
         firstButton=(Button)findViewById(R.id.firstButton);
       /*   graphSig1=(GraphView)findViewById(R.id.graphSig1);
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

        spec = tabs.newTabSpec(TAB_4);
        spec.setContent(R.id.tab4);
        spec.setIndicator("АЧХ фильтр");
        tabs.addTab(spec);



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
        t=MainActivity.s.getTArray();
        /*Intent intent =getIntent();
        signals=intent.getDoubleArrayExtra(MainActivity.ATTRIBUT_SIGNAL);
        t=intent.getDoubleArrayExtra(MainActivity.ATTRIBUT_t);
        durSignal=intent.getIntExtra(MainActivity.ATTRIBUT_DURR_SIGNAL, durSignal);
        amplitudeOfNoise=intent.getDoubleExtra(MainActivity.ATTRIBUT_AMPLITUDE_OF_NOISE, amplitudeOfNoise);
        durationDescret=intent.getDoubleExtra(MainActivity.ATTRIBUT_DURATION_DESCRET, durationDescret);
        N=signals.length;*/

        graph = (GraphView) findViewById(R.id.graph);
        graph.addSeries(createDataPoint(t, MainActivity.s.signal, Color.BLUE));
        graph.getViewport().setXAxisBoundsManual(true);
        graph.getViewport().setScrollable(true);
        graph.getViewport().setScalable(true);
        withNoiseGraph.setOnClickListener(this);
        onlyNoiseGraph.setOnClickListener(this);
        normalGraph.setOnClickListener(this);
        //graphPower.setOnClickListener(this);
        amplitudeAnalisButton.setOnClickListener(this);
        phaseAnalisButton.setOnClickListener(this);
        heningButton.setOnClickListener(this);
        porabolButton.setOnClickListener(this);
        firstButton.setOnClickListener(this);
        graph.setFocusableInTouchMode(true);
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



    @Override
    public void onClick(View v) {
        double[]amplSpectr=new double[MainActivity.s.N];
        double[]phaseSpectr=new double[MainActivity.s.N];
        switch (v.getId())
        {
            case R.id.withNoiseGraph:
            {
                graph.removeAllSeries();
                //generateNoise();
                graph.addSeries(createDataPoint(t,MainActivity.s.getNoisesSignal(),Color.BLUE));
                break;
            }

            case R.id.noiseOnlyGraph:
            {
                graph.removeAllSeries();
                //generateNoise();
                graph.addSeries(createDataPoint(t,MainActivity.s.noise,Color.BLUE));
                break;
            }

            case R.id.normalGraph:
            {
                graph.removeAllSeries();
                graph.addSeries(createDataPoint(t,MainActivity.s.signal,Color.BLUE));
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
                MainActivity.s.getDPF(amplSpectr, phaseSpectr);
                graphAnalis.addSeries(createDataPoint(MainActivity.s.getArrFrequency(),amplSpectr,Color.BLUE));
                break;
            }

            case R.id.phaseFrequencyButton:
            {
                graphAnalis.removeAllSeries();
                MainActivity.s.getDPF(amplSpectr, phaseSpectr);
                graphAnalis.addSeries(createDataPoint(MainActivity.s.getArrFrequency(), phaseSpectr,Color.BLUE));
                break;
            }
            case R.id.hanningButton:
            {
                graphGistog.removeAllSeries();
                graph.removeAllSeries();
                graph.addSeries(createDataPoint(t,MainActivity.s.signal,Color.BLUE));
                graph.addSeries(createDataPoint(t, MainActivity.s.getHennigFiltr(), Color.RED));
                graphGistog.addSeries(createDataPoint(MainActivity.s.getArrFrequency(),MainActivity.s.getACHFiltr(),Color.BLUE));
                break;
            }
            case R.id.parobalButton:
            {
                graphGistog.removeAllSeries();
                graph.removeAllSeries();
                graph.addSeries(createDataPoint(t, MainActivity.s.signal, Color.BLUE));
                graph.addSeries(createDataPoint(t, MainActivity.s.getPorabolFiltr(), Color.RED));
                graphGistog.addSeries(createDataPoint(MainActivity.s.getArrFrequency(),MainActivity.s.getACHFiltr(),Color.BLUE));
                break;
            }

            case R.id.firstButton:
            {
                graphGistog.removeAllSeries();
                graph.removeAllSeries();
                graph.addSeries(createDataPoint(t, MainActivity.s.signal, Color.BLACK));
                graph.addSeries(createDataPoint(t, MainActivity.s.getFirstFiltr(), Color.RED));
                graphGistog.addSeries(createDataPoint(MainActivity.s.getArrFrequency(),MainActivity.s.getACHFiltr(),Color.BLUE));
                break;
            }

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
