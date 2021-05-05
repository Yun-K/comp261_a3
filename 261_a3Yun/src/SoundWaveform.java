
// DO NOT DISTRIBUTE THIS FILE TO STUDENTS
import ecs100.UI;
import ecs100.UIButtonListener;
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

    /* represent the runing time */
    public static long IDFT_running_time, IFFT_runing_time, DFT_running_time, FFT_running_time;

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
        System.out.println("Clean up and Initialize spectrum list first\nDo the algorthim...");
        UI.println("Clean up and Initialize spectrum list first\nDo the algorthim...");
        this.spectrum = new ArrayList<ComplexNumber>();// clean up

        // TODO
        // Add your code here: you should transform from the waveform to the spectrum
        long start = System.currentTimeMillis();

        ArrayList<ComplexNumber> waveform_ComplexList = ComplexNumber
                .convertToComplexNumberList(waveform);// easy for assigning variable
        // check if need to cut the tail
        int toMinus = how_far_away_isPowerOf2(waveform_ComplexList);
        if (toMinus > 0) {// bigger than 0 means need to cut the tail
            for (int i = 0; i < toMinus; i++) {
                waveform_ComplexList.remove(waveform_ComplexList.size() - 1);
            }
        }

        /*
         * K: amplitude of the frequency K
         */
        for (int k = 0; k < waveform_ComplexList.size(); k++) {
            ComplexNumber weightedSum_cNumber = new ComplexNumber();
            for (int n = 0; n < waveform_ComplexList.size(); n++) {
                // set up & assign the Maths value
                double b = (n * k * 2 * Math.PI) / (waveform_ComplexList.size());// (k*n*2PI)/N
                double real = Math.cos(-b);
                double img = Math.sin(-b);
                // multply it do the calculation
                ComplexNumber toBeAdded = ComplexNumber.multiply(
                        waveform_ComplexList.get(n),
                        new ComplexNumber(real, img));

                // increment the value of the complexNumber
                weightedSum_cNumber = ComplexNumber.add(weightedSum_cNumber,
                        toBeAdded);
            }

            // add into list
            this.spectrum.add(weightedSum_cNumber);
        }

        long end = System.currentTimeMillis();

        this.DFT_running_time = end - start;

        UI.println("DFT completed!");
        waveform.clear();
    }

    public void idft() {
        UI.clearText();
        UI.println("IDFT in process, please wait...");

        if (this.spectrum == null || this.spectrum.isEmpty()) {
            UI.println("Please do dft first");
            return;
        }

        System.out.println("Clean up and Initialize waveform list\nDo the algorthim...");
        UI.println("Clean up and Initialize waveform list\nDo the algorthim...");
        this.waveform = new ArrayList<Double>();// clean up
        // TODO
        // Add your code here: you should transform from the spectrum to the waveform
        long start = System.currentTimeMillis();

        // implement code
        for (int n = 0; n < this.spectrum.size(); n++) {
            // double value = 0;
            ComplexNumber weightedSum = new ComplexNumber();
            for (int k = 0; k < this.spectrum.size(); k++) {
                // set up & assign the Maths value
                double b = (n * k * 2 * Math.PI) / (this.spectrum.size());// k*n*2PI/N
                double real = Math.cos(b);
                double img = Math.sin(b);

                // multply it do the calculation
                ComplexNumber toBeAdded = ComplexNumber.multiply(
                        this.spectrum.get(k),
                        new ComplexNumber(real, img));
                weightedSum = ComplexNumber.add(weightedSum, toBeAdded);

            }
            double value = weightedSum.getRe();
            value /= this.spectrum.size();

            waveform.add(value);
        }

        UI.println("IDFT completed!");
        long end = System
                .currentTimeMillis();

        this.IDFT_running_time = end - start;

        spectrum.clear();
    }

    public void fft() {// double to ComplexNumber
        UI.clearText();
        UI.println("FFT in process, please wait...");

        System.out.println("Clean up and Initialize spectrum list first\nDo the algorthim...");
        UI.println("Clean up and Initialize spectrum list first\nDo the algorthim...");
        this.spectrum = new ArrayList<ComplexNumber>();// clean up

        // TODO,write some piece of code
        // Add your code here: you should transform from the waveform to the spectrum

        long start = System.currentTimeMillis();

        ArrayList<ComplexNumber> waveform_ComplexList = ComplexNumber
                .convertToComplexNumberList(waveform);// easy for assigning variable
        // check if need to cut the tail
        int toMinus = how_far_away_isPowerOf2(waveform_ComplexList);
        if (toMinus > 0) {// bigger than 0 means need to cut the tail
            for (int i = 0; i < toMinus; i++) {
                waveform_ComplexList.remove(waveform_ComplexList.size() - 1);
            }
        }

        // do recursion and assign to the spectrum ArrayList
        this.spectrum = FFTHelper(waveform_ComplexList);// do the recursion

        long end = System.currentTimeMillis();

        FFT_running_time = end - start;
        UI.println("FFT completed!");
        waveform.clear();
    }

    /**
     * The input is an array of double, and the output is an array of complex numbers.
     *
     * @author Yun Zhou
     * @param waveformComplexList
     * @return
     */
    public ArrayList<ComplexNumber> FFTHelper(ArrayList<ComplexNumber> waveformComplexList) {
        if (waveformComplexList == null) {
            throw new NullPointerException("It's null, you haven't load the file yet");
        }
        int N = waveformComplexList.size();
        if (N == 1) {
            // can not divide only 1 instance into 2 half, so just return it
            return waveformComplexList;
        }
        ArrayList<ComplexNumber> xeven = new ArrayList<ComplexNumber>();
        ArrayList<ComplexNumber> xodd = new ArrayList<ComplexNumber>();
        for (int i = 0; i < N / 2; i++) {
            xeven.add(null);
            xodd.add(null);
        }

        for (int k = 0; k < N / 2; k++) {
            xeven.set(k, waveformComplexList.get(k * 2));
            xodd.set(k, waveformComplexList.get(1 + k * 2));
        }

        // do the recursion DFS
        ArrayList<ComplexNumber> Xeven = FFTHelper(xeven);
        ArrayList<ComplexNumber> Xodd = FFTHelper(xodd);

        // initiallize arrayList X
        ArrayList<ComplexNumber> X = new ArrayList<ComplexNumber>();
        for (int i = 0; i < N; i++) {
            X.add(new ComplexNumber());
        }

        // since the period is periodic , so each corresponding value is match, use d&c to
        // assign and calculate X from Xeven, Xodd and W[k]
        for (int k = 0; k < N / 2; k++) {
            // set up & assign the Maths value
            double img = -1 * (k * 2 * Math.PI) / N;// (2PI*k)/N
            double img1 = -1 * ((k + N / 2) * 2 * Math.PI) / N;// (2PI*(k+N/2))/N
            // Xeven(k)+Xodd(K)*W(K,N)
            ComplexNumber W_K_N = ComplexNumber.exp(new ComplexNumber(0, img));
            // Xeven(k)+Xodd(K)*W(K+N/2,N)
            ComplexNumber W_KN2_N = ComplexNumber.exp(new ComplexNumber(0, img1));
            // assert W_KN2_N.getIm() != 0;
            // ComplexNumber or1 = X.get(k);
            X.set(k,
                    ComplexNumber.add(Xeven.get(k), ComplexNumber.multiply(Xodd.get(k), W_K_N)));

            int nextPeriodIndex = k + N / 2;
            // ComplexNumber or2 = X.get(nextPeriodIndex);
            X.set(nextPeriodIndex,
                    ComplexNumber.add(Xeven.get(k), ComplexNumber.multiply(Xodd.get(k), W_KN2_N)));
        }

        return X;
    }

    public void ifft() {// ComplexNumber To Double
        UI.clearText();
        UI.println("IFFT in process, please wait...");

        System.out.println("Clean up and Initialize waveform list\nDo the algorthim...");
        UI.println("Clean up and Initialize waveform list\nDo the algorthim...");
        this.waveform = new ArrayList<Double>();// clean up
        // TODO
        // Add your code here: you should transform from the spectrum to the waveform

        long start = System.currentTimeMillis();

        // check if need to cut the tail
        if (this.spectrum.size() % 2 != 0) {
            if (this.spectrum.size() != 1) {
                // cut the last tail to make sure it is some power of 2
                this.spectrum.remove(spectrum.size() - 1);
            }
        }

        // do recursion and assign to the spectrum ArrayList
        ArrayList<ComplexNumber> temp = IFFTHelper(new ArrayList<ComplexNumber>(this.spectrum));

        // add it into the waveform list
        for (ComplexNumber complexNumber : temp) {
            this.waveform.add(complexNumber.getRe());
        }

        UI.println("IFFT completed!");

        long end = System.currentTimeMillis();
        IFFT_runing_time = end - start;
        spectrum.clear();
    }

    private ArrayList<ComplexNumber> IFFTHelper(ArrayList<ComplexNumber> spectArrayList) {
        if (spectArrayList == null) {
            throw new NullPointerException("It's null, you haven't load the file yet");
        }
        int N = spectArrayList.size();
        if (N == 1) {
            // can not divide only 1 instance into 2 half, so just return it
            return spectArrayList;
        }
        ArrayList<ComplexNumber> xeven = new ArrayList<ComplexNumber>();
        ArrayList<ComplexNumber> xodd = new ArrayList<ComplexNumber>();
        for (int i = 0; i < N / 2; i++) {
            xeven.add(null);
            xodd.add(null);
        }

        for (int k = 0; k < N / 2; k++) {
            xeven.set(k, spectArrayList.get(k * 2));
            xodd.set(k, spectArrayList.get(1 + k * 2));
        }

        // do the recursion DFS
        ArrayList<ComplexNumber> Xeven = FFTHelper(xeven);
        ArrayList<ComplexNumber> Xodd = FFTHelper(xodd);

        // initiallize arrayList X
        ArrayList<ComplexNumber> X = new ArrayList<ComplexNumber>();
        for (int i = 0; i < N; i++) {
            X.add(new ComplexNumber());
        }

        // since the period is periodic , so each corresponding value is match, use d&c to
        // assign and calculate X from Xeven, Xodd and W[k]
        for (int k = 0; k < N / 2; k++) {
            // set up & assign the Maths value
            double img = (k * 2 * Math.PI) / N;// (2PI*k)/N
            double img1 = ((k + N / 2) * 2 * Math.PI) / N;// (2PI*(k+N/2))/N
            // Xeven(k)+Xodd(K)*W(K,N)
            ComplexNumber W_K_N = ComplexNumber.exp(new ComplexNumber(0, img));
            // Xeven(k)+Xodd(K)*W(K+N/2,N)
            ComplexNumber W_KN2_N = ComplexNumber.exp(new ComplexNumber(0, img1));
            // assert W_KN2_N.getIm() != 0;
            ComplexNumber or1 = ComplexNumber.add(Xeven.get(k),
                    ComplexNumber.multiply(Xodd.get(k), W_K_N));

            X.set(k, ComplexNumber.divide(or1, ComplexNumber.convertToComplexNumber(N)));

            int nextPeriodIndex = k + N / 2;
            ComplexNumber or2 = ComplexNumber.add(Xeven.get(k),
                    ComplexNumber.multiply(Xodd.get(k), W_KN2_N));
            X.set(nextPeriodIndex,
                    ComplexNumber.divide(or2, ComplexNumber.convertToComplexNumber(N)));

        }

        return X;
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

        UI.addButton("Print Time", () -> {
            UI.println("Below are the running time:");
            UI.println("IDFT: " + IDFT_running_time);
            UI.println("DFT: " + DFT_running_time);
            UI.println("FFT: " + FFT_running_time);
            UI.println("IFFT: " + IFFT_runing_time);
            long fft_diff = FFT_running_time - DFT_running_time;
            UI.println("The time difference between FFT and DFT: " + fft_diff);
            long inverse_diff = IFFT_runing_time - IDFT_running_time;
            UI.println("The time difference between IFFT and IDFT : " + inverse_diff);

            System.out.println("Below are the running time:");
            System.out.println("IDFT: " + IDFT_running_time);
            System.out.println("DFT: " + DFT_running_time);
            System.out.println("FFT: " + FFT_running_time);
            System.out.println("IFFT: " + IFFT_runing_time);
            System.out.println("The time difference between FFT and DFT: " + fft_diff);
            System.out.println("The time difference between IFFT and IDFT : " + inverse_diff);
        });

        UI.setMouseMotionListener(wfm::doMouse);// at next method
        UI.setWindowSize(950, 630);
    }

    /**
     * Respond to mouse actions. The value of action may be "pressed", "released", "clicked",
     * "doubleclicked", "moved", or "dragged". x and y are the coordinates of where the mouse
     * action happened.
     */
    public UIMouseListener doMouse(String action, double x, double y) {

        return null;
    }

    @Override
    public void mousePerformed(String action, double x, double y) {
        // TODO Auto-generated method stub

    }

    public void printDiff() {
        // () -> {
        UI.println("Below are the running time:");
        UI.println("IDFT: " + IDFT_running_time);
        UI.println("DFT: " + DFT_running_time);
        UI.println("FFT: " + FFT_running_time);
        UI.println("IFFT: " + IFFT_runing_time);
        long fft_diff = FFT_running_time - DFT_running_time;
        UI.println("The time difference between FFT and DFT: " + fft_diff);
        long inverse_diff = IFFT_runing_time - IDFT_running_time;
        UI.println("The time difference between IFFT and IDFT : " + inverse_diff);

        System.out.println("Below are the running time:");
        System.out.println("IDFT: " + IDFT_running_time);
        System.out.println("DFT: " + DFT_running_time);
        System.out.println("FFT: " + FFT_running_time);
        System.out.println("IFFT: " + IFFT_runing_time);
        System.out.println("The time difference between FFT and DFT: " + fft_diff);
        System.out.println("The time difference between IFFT and IDFT : " + inverse_diff);
        // }
    }

    /**
     * Description: <br/>
     * This method will find how many number should the list to be subtracted to make the size
     * of the list to be some power of 2.
     * <p>
     * e.g. size==10, it will return 2 since 10 need to minus 2 to be 8 since 8 is the pow of
     * 2.
     * <p>
     * size==4, it will return 0, which means 4 is already the pow of 2.
     * 
     * @author Yun Zhou
     * @param list
     * @return
     */
    public int how_far_away_isPowerOf2(ArrayList list) {
        int toMinus = Integer.MAX_VALUE;
        for (int i = 1; i <= list.size() / 2; i++) {
            int pow2 = (int) Math.pow(2, i);
            if (pow2 == list.size()) {
                return 0;
            }
            int diff = list.size() - pow2;
            // diff can not be less that 0
            if (diff < toMinus && diff > 0) {
                toMinus = diff;
            }
        }
        // the original size of the waveform should be smaller than the size after the cut
        // times 2.
        assert 0 <= toMinus && toMinus < list.size();// double check
        assert list.size() < (list.size() - toMinus) * 2;
        return toMinus;

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
