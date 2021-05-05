import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class WaveformTest {
    ArrayList<Double> waveForm = new ArrayList<Double>();

    SoundWaveform SOUND = new SoundWaveform();

    @Test
    public void test_case1_whole() throws Exception {
        // test_case1_dft_idft();
        test_case1_fft_ifft();
        SOUND.printDiff();
        System.out.println("------------------case 1 end=============================\n");
    }

    @Test
    public void test_case2_whole() throws Exception {
        test_case2_dft_idft();
        test_case2_fft_ifft();
        SOUND.printDiff();
        System.out.println("------------------case 2 end=============================\n");
    }

    @Test
    public void test_case3_whole() throws Exception {
        test_case3_dft_idft();
        test_case3_fft_ifft();
        SOUND.printDiff();
        System.out.println("------------------case 3 end=============================\n");
    }

    public void test_case1_dft_idft() throws Exception {
        System.out.println("\n=============Testing case1 DFT============================");
        waveForm = new ArrayList<Double>();
        double i1 = 1.0;
        while (i1 <= 8) {
            waveForm.add(i1++);
        }
        SOUND.setWaveform(waveForm);
        SOUND.dft();

        for (ComplexNumber cm : SOUND.getSpectrum()) {
            System.out.println(cm.toString());
        }
        System.out.println("============finish case1 DFT=============================\n");

        System.out.println("\n============Testing case1 IDFT=============================");
        SOUND.idft();
        for (Double double1 : SOUND.getWaveform()) {
            System.out.println(double1);
        }
        System.out.println("============finish case1 IDFT=============================\n");

    }

    public void test_case1_fft_ifft() throws Exception {
        System.out.println("\n=============Testing case1 FFT============================");
        waveForm = new ArrayList<Double>();
        double i = 1.0;
        while (i <= 8) {
            waveForm.add(i++);
        }
        SOUND.setWaveform(waveForm);
        SOUND.fft();

        for (ComplexNumber cm : SOUND.getSpectrum()) {
            System.out.println(cm.toString());
        }
        System.out.println("============finish case1 FFT=============================\n");
        System.out.println("\n============Testing case1 IFFT=============================");
        SOUND.ifft();
        for (Double double1 : SOUND.getWaveform()) {
            System.out.println(double1);
        }
        System.out.println("============finish case1 IFFT=============================\n");

    }

    public void test_case2_fft_ifft() throws Exception {

        System.out.println("\n=============Testing case2 FFT============================");
        waveForm = new ArrayList<Double>();
        waveForm.add(1.0);
        waveForm.add(2.0);
        waveForm.add(1.0);
        waveForm.add(2.0);
        waveForm.add(1.0);
        waveForm.add(2.0);
        waveForm.add(1.0);
        waveForm.add(2.0);
        SOUND.setWaveform(waveForm);
        SOUND.fft();

        for (ComplexNumber cm : SOUND.getSpectrum()) {
            System.out.println(cm.toString());
        }
        System.out.println("============finish case2 FFT=============================\n");
        System.out.println("\n============Testing case2 IFFT=============================");
        SOUND.ifft();
        for (Double double1 : SOUND.getWaveform()) {
            System.out.println(double1);
        }
        System.out.println("============finish case2 IFFT=============================\n");
    }

    public void test_case2_dft_idft() throws Exception {
        System.out.println("\n=============Testing case 2 DFT============================");
        waveForm = new ArrayList<Double>();
        waveForm.add(1.0);
        waveForm.add(2.0);
        waveForm.add(1.0);
        waveForm.add(2.0);
        waveForm.add(1.0);
        waveForm.add(2.0);
        waveForm.add(1.0);
        waveForm.add(2.0);
        SOUND.setWaveform(new ArrayList<Double>(waveForm));
        SOUND.dft();

        for (ComplexNumber cm : SOUND.getSpectrum()) {
            System.out.println(cm.toString());
        }
        System.out.println("============finish case2 DFT=============================\n");

        System.out.println("\n============Testing case2 IDFT=============================");
        SOUND.idft();
        for (Double double1 : SOUND.getWaveform()) {
            System.out.println(double1);
        }
        System.out.println("============finish case2 IDFT=============================\n");

    }

    public void test_case3_dft_idft() throws Exception {
        System.out.println("\n=============Testing case 3 DFT============================");
        waveForm = new ArrayList<Double>();
        waveForm.add(1.0);
        waveForm.add(2.0);
        waveForm.add(3.0);
        waveForm.add(4.0);
        waveForm.add(4.0);
        waveForm.add(3.0);
        waveForm.add(2.0);
        waveForm.add(1.0);
        SOUND.setWaveform(new ArrayList<Double>(waveForm));
        SOUND.dft();

        for (ComplexNumber cm : SOUND.getSpectrum()) {
            System.out.println(cm.toString());
        }
        System.out.println("============finish case3 DFT=============================\n");

        System.out.println("\n============Testing case3 IDFT=============================");
        SOUND.idft();
        for (Double double1 : SOUND.getWaveform()) {
            System.out.println(double1);
        }
        System.out.println("============finish case3 IDFT=============================\n");

    }

    public void test_case3_fft_ifft() throws Exception {
        System.out.println("\n=============Testing case 3 FFT============================");
        waveForm = new ArrayList<Double>();
        waveForm.add(1.0);
        waveForm.add(2.0);
        waveForm.add(3.0);
        waveForm.add(4.0);
        waveForm.add(4.0);
        waveForm.add(3.0);
        waveForm.add(2.0);
        waveForm.add(1.0);
        SOUND.setWaveform(new ArrayList<Double>(waveForm));
        SOUND.fft();
        for (ComplexNumber cm : SOUND.getSpectrum()) {
            System.out.println(cm.toString());
        }
        System.out.println("============finish case3 FFT=============================\n");

        System.out.println("\n============Testing case3 IFFT=============================");
        SOUND.ifft();
        for (Double double1 : SOUND.getWaveform()) {
            System.out.println(double1);
        }
        System.out.println("============finish case3 IFFT=============================\n");

    }

    // Case 3:
    // Input waveform = [1,2,3,4,4,3,2,1]
    // Output spectrum = [
    // 20.0+0.0i
    // -5.82842712474619-2.414213562373096i
    // -6.123233995736766E-16-6.123233995736766E-16i
    // -0.1715728752538097-0.4142135623730945i
    // 0.0-1.2246467991473533E-15i
    // -0.1715728752538097+0.41421356237309537i
    // 6.123233995736766E-16-6.123233995736765E-16i
    // -5.828427124746191+2.4142135623730936i
    // ]

}
