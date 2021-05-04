
// DO NOT DISTRIBUTE THIS FILE TO STUDENTS
import ecs100.UI;
import ecs100.UIFileChooser;
import ecs100.UIMouseListener;

import javax.sound.sampled.*;
import java.awt.*;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
/*
  getAudioInputStream
  -> getframelength,
  -> read into byteArray of 2x that many bytes
  -> convert to array of doubles in reversed pairs of bytes (signed)
  -> scale #FFFF to +/- 300

  array of doubles
   -> unscale  +/- 300  to #FFFF (
   -> convert to array of bytes (pairs little endian, signed)
   -> convert to inputStream
   -> convert to AudioInputStream
   -> write to file.
 */

public class SoundWaveform implements UIMouseListener {

    public static final double MAX_VALUE = 300;

    public static final int SAMPLE_RATE = 44100;

    /** samples in 1/100 sec */
    public static final int MAX_SAMPLES = SAMPLE_RATE / 100;

    public static final int GRAPH_LEFT = 10;

    public static final int ZERO_LINE = 310;

    /** pixels between samples */
    public static final int X_STEP = 2;

    public static final int GRAPH_WIDTH = MAX_SAMPLES * X_STEP;

    /** the displayed waveform */
    private ArrayList<Double> waveform = new ArrayList<Double>();

    /** the spectrum: length/mod of each X(k) */
    private ArrayList<ComplexNumber> spectrum = new ArrayList<ComplexNumber>();

    // private ArrayList<ComplexNumber> ;

    /**
     * Displays the waveform.
     */
    public void displayWaveform() {
        if (this.waveform == null) { // there is no data to display
            UI.println("No waveform to display");
            return;
        }
        UI.clearText();
        UI.println("Printing, please wait...");

        UI.clearGraphics();

        // draw x axis (showing where the value 0 will be)
        UI.setColor(Color.black);
        UI.drawLine(GRAPH_LEFT, ZERO_LINE, GRAPH_LEFT + GRAPH_WIDTH, ZERO_LINE);

        // plot points: blue line between each pair of values
        UI.setColor(Color.blue);

        double x = GRAPH_LEFT;
        for (int i = 1; i < this.waveform.size(); i++) {
            double y1 = ZERO_LINE - this.waveform.get(i - 1);
            double y2 = ZERO_LINE - this.waveform.get(i);
            if (i > MAX_SAMPLES) {
                UI.setColor(Color.red);
            }
            UI.drawLine(x, y1, x + X_STEP, y2);
            x = x + X_STEP;
        }

        UI.println("Printing completed!");
    }

    /**
     * Displays the spectrum. Scale to the range of +/- 300.
     */
    public void displaySpectrum() {
        if (this.spectrum == null) { // there is no data to display
            UI.println("No spectrum to display");
            return;
        }
        UI.clearText();
        UI.println("Printing, please wait...");

        UI.clearGraphics();

        // calculate the mode of each element
        ArrayList<Double> spectrumMod = new ArrayList<Double>();
        double max = 0;
        for (int i = 0; i < spectrum.size(); i++) {
            if (i == MAX_SAMPLES)
                break;

            double value = spectrum.get(i).mod();
            max = Math.max(max, value);
            spectrumMod.add(spectrum.get(i).mod());
        }

        double scaling = 300 / max;
        for (int i = 0; i < spectrumMod.size(); i++) {
            spectrumMod.set(i, spectrumMod.get(i) * scaling);
        }

        // draw x axis (showing where the value 0 will be)
        UI.setColor(Color.black);
        UI.drawLine(GRAPH_LEFT, ZERO_LINE, GRAPH_LEFT + GRAPH_WIDTH, ZERO_LINE);

        // plot points: blue line between each pair of values
        UI.setColor(Color.blue);

        double x = GRAPH_LEFT;
        for (int i = 1; i < spectrumMod.size(); i++) {
            double y1 = ZERO_LINE;
            double y2 = ZERO_LINE - spectrumMod.get(i);
            if (i > MAX_SAMPLES) {
                UI.setColor(Color.red);
            }
            UI.drawLine(x, y1, x + X_STEP, y2);
            x = x + X_STEP;
        }

        UI.println("Printing completed!");
    }

    public void dft() {//
        UI.clearText();
        UI.println("DFT in process, please wait...");

        ArrayList<ComplexNumber> corrComplexList = ComplexNumber
                .convertToComplexNumberList(waveform);
        // TODO
        // Add your code here: you should transform from the waveform to the spectrum

        /*
         * K: amplitude of the frequency K
         */
        for (int k = 0; k < this.waveform.size(); k++) {
            ComplexNumber k_cNumber = new ComplexNumber();
            for (int n = 0; n < this.waveform.size(); n++) {
                // set up & assign the Maths value
                double b = (n * k * 2 * Math.PI) / (this.waveform.size());// 2PI/N

                double real = Math.cos(-b);
                double img = Math.sin(-b);
                // multply it do the calculation
                ComplexNumber toBeAdded = ComplexNumber.multiply(
                        corrComplexList.get(n),
                        new ComplexNumber(real, img));

                // increment the value of the complexNumber
                k_cNumber = ComplexNumber.add(k_cNumber, toBeAdded);
            }

            // add into list
            this.spectrum.add(k_cNumber);
        }

        UI.println("DFT completed!");
        waveform.clear();
    }

    public void idft() {
        UI.clearText();
        UI.println("IDFT in process, please wait...");

        // TODO
        // Add your code here: you should transform from the spectrum to the waveform

        UI.println("IDFT completed!");

        spectrum.clear();
    }

    public void fft() {
        UI.clearText();
        UI.println("FFT in process, please wait...");

        // TODO,write some piece of code
        // Add your code here: you should transform from the waveform to the spectrum
        double[] temp = new double[this.waveform.size()];
        for (int i = 0; i < temp.length; i++) {
            temp[i] = waveform.get(i);
        }
        FFTHelper(temp);

        UI.println("FFT completed!");
        waveform.clear();
    }

    /**
     * The input is an array of double, and the output is an array of complex numbers.
     *
     * @author Yun Zhou
     */
    public ComplexNumber[] FFTHelper(double[] transoformList) {
        if (transoformList == null || transoformList.length % 2 != 0) {
            throw new NullPointerException("It's null, you haven't load the file yet");
        }
        int N = transoformList.length;
        if (N == 1) {
            // can not divide only 1 instance into 2 half, so just return the complexNumber
            // version of this number ( x= x + 0*i)
            return new ComplexNumber[] { new ComplexNumber(transoformList[0], 0) };
        }

        ComplexNumber[] W = new ComplexNumber[N];
        for (int k = 0; k < N; k++) {
            W[k] = ComplexNumber.exp(new ComplexNumber(0, -2 * Math.PI * k / N));
        }

        double[] xeven = new double[N / 2];
        double[] xodd = new double[N / 2];
        for (int k = 0; k < N / 2; k++) {
            xeven[k] = transoformList[k * 2];
            xodd[k] = transoformList[k * 2 + 1];
        }

        ComplexNumber[] Xeven = FFTHelper(xeven);
        ComplexNumber[] Xodd = FFTHelper(xodd);
        ComplexNumber[] X = new ComplexNumber[N];

        // since the period is periodic , so each corresponding value is match, use d&c to
        // assign and calculate X from Xeven, Xodd and W[k]
        for (int k = 0; k < N / 2 - 1; k++) {
            // Xeven(k)+Xodd(K)*W(K,N)
            ComplexNumber W_K_N = ComplexNumber.exp(new ComplexNumber(0, (-2 * Math.PI * k) / 8));
            // Xeven(k)+Xodd(K)*W(K+N/2,N)
            ComplexNumber W_KN2_N = ComplexNumber
                    .exp(new ComplexNumber(0, (-2 * Math.PI * (N / 2 + k)) / 8));

            X[k] = ComplexNumber.add(Xeven[k], ComplexNumber.multiply(Xodd[k], W_K_N));
            X[k + N / 2] = ComplexNumber.add(Xeven[k], ComplexNumber.multiply(Xodd[k], W_KN2_N));
        }

        return X;
    }

    public void ifft() {
        UI.clearText();
        UI.println("IFFT in process, please wait...");

        // TODO
        // Add your code here: you should transform from the spectrum to the waveform

        UI.println("IFFT completed!");

        spectrum.clear();
    }

    /**
     * Save the wave form to a WAV file
     */
    public void doSave() {
        WaveformLoader.doSave(waveform, WaveformLoader.scalingForSavingFile);
    }

    /**
     * Load the WAV file.
     */
    public void doLoad() {
        UI.clearText();
        UI.println("Loading...");

        waveform = WaveformLoader.doLoad();

        this.displayWaveform();

        UI.println("Loading completed!");
    }

    public static void main(String[] args) {
        SoundWaveform wfm = new SoundWaveform();
        // core
        UI.addButton("Display Waveform", wfm::displayWaveform);
        UI.addButton("Display Spectrum", wfm::displaySpectrum);
        UI.addButton("DFT", wfm::dft);
        UI.addButton("IDFT", wfm::idft);
        UI.addButton("FFT", wfm::fft);
        UI.addButton("IFFT", wfm::ifft);
        UI.addButton("Save", wfm::doSave);
        UI.addButton("Load", wfm::doLoad);
        UI.addButton("Quit", UI::quit);
        UI.setMouseMotionListener(wfm::doMouse);// at next method
        UI.setWindowSize(950, 630);
    }

    public UIMouseListener doMouse(String action, double x, double y) {

        return null;
    }

    @Override
    public void mousePerformed(String action, double x, double y) {
        // TODO Auto-generated method stub

    }

    /**
     * Get the waveform.
     *
     * @return the waveform
     */
    public ArrayList<Double> getWaveform() {
        return waveform;
    }

    /**
     * Get the spectrum.
     *
     * @return the spectrum
     */
    public ArrayList<ComplexNumber> getSpectrum() {
        return spectrum;
    }

    /**
     * Set the waveform.
     *
     * @param waveform
     *            the waveform to set
     */
    public void setWaveform(ArrayList<Double> waveform) {
        this.waveform = waveform;
    }

    /**
     * Set the spectrum.
     *
     * @param spectrum
     *            the spectrum to set
     */
    public void setSpectrum(ArrayList<ComplexNumber> spectrum) {
        this.spectrum = spectrum;
    }
}
