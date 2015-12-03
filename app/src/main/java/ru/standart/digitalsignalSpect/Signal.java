package ru.standart.digitalsignalSpect;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by 95tox on 30.11.2015.
 */
public class Signal {
    public double   durationSignal; //продолжительность сигнала (Т)
    public double   FriqPeriodDeskret; //частота период дескретизации ( )
    public double      numberOfGarmonic; //количество гармоник
    public double[] amplitude;       //амплитуда(А)
    public double[] freq;       //частота(f)
    public double[] phase;           //фаза гармоники(фи)
    public double   amplitudeOfRandomNoise;  //амплитуда случайного шума
    public double [] signal;
    public double [] noise;
    static public int N;

    public Signal( double durationSignal,double FriqPeriodDeskret,double numberOfGarmonic, double[]amplitude,double[]freq,double[]phase,double amplitudeOfRandomNoise)
    {
        this.durationSignal=durationSignal;
        this.FriqPeriodDeskret=FriqPeriodDeskret;
        this.numberOfGarmonic=numberOfGarmonic;
        this.amplitude=amplitude;
        this.freq=freq;
        this.phase=phase;
        this.amplitudeOfRandomNoise=amplitudeOfRandomNoise;
        signal =getSignalArray();
        noise=generateNoise();
    }


    double getPeriodDeskret()
    {
        return 1/FriqPeriodDeskret;
    }


    double[] converListToDouble(List<Double>list)
    {
        double[]arr=new double[list.size()];
        for (int i=0; i<list.size();i++)
        {
            arr[i]=list.get(i);
        }
        return arr;
    }


    public double[] getTArray()
    {
        List<Double> list=new ArrayList<>();
        double n=Math.floor(durationSignal) / getPeriodDeskret();
        N=(int)n;
        double perid=getPeriodDeskret();
        int j=0;
        for(double i=0; i < durationSignal; i+=perid)
        {
            list.add(i);
        }

        return converListToDouble(list);
    }

   public double[] getSignalArray()
    {
        double[]tArray=getTArray();
        double[] arr=new double[tArray.length];
        double signal=0;
        for(int i=0; i<N; i++)
        {
            signal=0;
            for(int j=0; j<numberOfGarmonic; j++)
            {

                signal+=amplitude[j]*Math.cos(2 * Math.PI * freq[j] *
                        tArray[i] + phase[j]);
                if(j==numberOfGarmonic-1)
                {
                    arr[i]=signal;

                }
            }
        }
        return arr;
    }

    double[] getArrFrequency()
    {
        double dF=1/durationSignal;
        // int size=(int)Math.floor(durationDescret/dF)-1;
        double[] result=new double[N];
        int i=0;
        for(int j=0; j<N; j++)
        {
            result[j]=j*dF;
        }

        return result;
    }

   public void getDPF(double[] amplSpectr, double[] phaseSpectr)
    {

        double[] im=new double[N];
        double[] re=new double[N];
        for(int i=0; i<N;i++) {
            im[i]=0;
            re[i]=0;
            for(int j=0; j<N;j++) {
                im[i]=im[i]+signal[j]*Math.sin(2*Math.PI*j*i/N);
                re[i]=re[i]+signal[j] * Math.cos(2*Math.PI*j*i/N);
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
            //   phaseSpectr[i]=phaseSpectr[i]/180*Math.PI;
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
        return result/ N;
    }

    double getMaxAmplitude()
    {
        double max=0;
        for(int i=0; i< N; i++)
        {
            double max_t= signal[i];
            for (int j=i; j<N; j++)
            {
                if(signal[j]>max_t)
                {
                    max= signal[j];
                }
            }
        }

        return max;
    }

    double getSignalEnergy()
    {
        double result=0;
        for(int i=0; i< N; i++)
        {
            result+=Math.pow(signal[i],2);
        }
        return result;
    }

  public  double[]getNoisesSignal()
    {
        double[]noiseSignal=new double[N];

        for(int i=0; i<N; i++)
        {
            noiseSignal[i]=noise[i]+ signal[i];
        }

        return noiseSignal;
    }

    double[] generateNoise()
    {
        if(noise==null) {
            noise = new double[N];
            Random random = new Random();
            for (int i = 0; i < N; i++) {
                noise[i] = -amplitudeOfRandomNoise + (amplitudeOfRandomNoise - (-amplitudeOfRandomNoise)) * random.nextDouble();
            }
        }
        return noise;
    }

  /*  double[]getCurrentPower()
    {
        double[]currPower=new double[t.length];

        for(int i=0; i<t.length; i++)
        {
            currPower[i]=Math.pow(signal[i],2);
        }

        return currPower;

    }*/

    double chastotaSreza=10;
    double r=0.5;
    double a0;
    double a1;
    double a2;
    double b0;
    double b1;
    double b2;
    double[] sigFiltr;

    public  double[] getHennigFiltr()
    {
        sigFiltr=new double[signal.length];
        a0=0.25;
        a1=0.5;
        a2=0.25;
        b0=1;
        b1=0;
        b2=0;
        for (int i=2; i< N; i++)
        {
            sigFiltr[i]=a0* signal[i]+a1* signal[i-1]+a2* signal[i-2];
        }
        return sigFiltr;
    }

    public   double[] getPorabolFiltr()
    {
        sigFiltr=new double[N];

        for(int i=2; i< N-2; i++)
        {
            sigFiltr[i]=(-3* signal[i-2]+12* signal[i-1]+17* signal[i]+12* signal[i+1]-3* signal[i+2])/35;
        }
        return sigFiltr;
    }


    public  double[] getFirstFiltr()
    {
        sigFiltr=new double[N];

        a0=1;
        a1=0;
        a2=0;
        b0=1;
        b1=-r;
        b2=0;
        for (int i=1; i< N; i++)
        {
            sigFiltr[i]=a0* signal[i]-b1*sigFiltr[i-1];
        }

        return sigFiltr;
    }

    public double[] getACHFiltr()
    {
        double[]ax=new double[N];

        for (int i=0; i< N; i++)
        {
            double acos=a0+a1*Math.cos(1 * 2 * Math.PI * i / N)+a2*Math.cos(2 * 2 * Math.PI * i /N);
            double asin=  a1*Math.sin(1 * 2 * Math.PI * i / N)+a2*Math.sin(2 * 2 * Math.PI * i / N);
            double bcos=1+b1*Math.cos(1 * 2 * Math.PI * i / N)+b2*Math.cos(2 * 2 * Math.PI * i / N);
            double bsin=  b1*Math.sin(1 * 2 * Math.PI * i / N)+b2*Math.sin(2 * 2 * Math.PI * i / N);
            ax[i]=Math.sqrt((Math.pow(acos,2)+Math.pow(asin,2))/(Math.pow(bcos,2)+Math.pow(bsin,2)));
        }
        return ax;
    }

}
