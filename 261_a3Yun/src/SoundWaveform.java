
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

public class SoundWaveform {

    public static final double MAX_VALUE = 300;

    public static final int SAMPLE_RATE = 44100;

    public static final int MAX_SAMPLES = SAMPLE_RATE / 100; // samples in 1/100 sec

    public static final int GRAPH_LEFT = 10;

    public static final int ZERO_LINE = 310;

    public static final int X_STEP = 2; // pixels between samples

    public static final int GRAPH_WIDTH = MAX_SAMPLES * X_STEP;

    private ArrayList<Double> waveform = new ArrayList<Double>(); // the displayed waveform

    private ArrayList<ComplexNumber> spectrum = new ArrayList<ComplexNumber>(); // the
                                                                                // spectrum:
                                                                                // length/mod
                                                                                // of each X(k)

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

    public void dft() {
        UI.clearText();
        UI.println("DFT in process, please wait...");

        // TODO
        // Add your code here: you should transform from the waveform to the spectrum

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

        // TODO
        // Add your code here: you should transform from the waveform to the spectrum

        UI.println("FFT completed!");
        waveform.clear();
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

    UIMouseListener doMouse(String a, double d1, double d2) {

        return null;
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
        UI.setMouseMotionListener(wfm::doMouse);
        UI.setWindowSize(950, 630);
    }
}
