import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class WaveformTest {

    @Test
    public void test_case1() throws Exception {
        ArrayList<Double> waveform = new ArrayList<Double>();
        double i = 1.0;
        while (i < 10) {
            if (i == 9)
                break;
            waveform.add(i);
            i += 1.0;
        }

        SoundWaveform sound = new SoundWaveform();
        sound.setWaveform(waveform);
        sound.dft();

        for (ComplexNumber cm : sound.getSpectrum()) {
            System.out.println(cm.toString());
        }

        // ArrayList<ComplexNumber> spectrum = new ArrayList<ComplexNumber>() ;
        // 36.0+0.0i,
        // -4.000000000000002+9.65685424949238i,
        // -4.000000000000002+3.9999999999999987i,
        // -4.0+1.6568542494923788i,
        // -4.0 -2.4492935982947065E-15i,
        // -3.999999999999999 -1.656854249492381i,
        // -3.999999999999998 -4.000000000000001i,
        // -3.999999999999995 -9.65685424949238i;

    }

    //
    // Case 2:
    // Input waveform = [1,2,1,2,1,2,1,2]
    // Output spectrum = [
    // 12.0+0.0i
    // -4.688471024089327E-16-1.2246467991473537E-16i
    // -4.898587196589413E-16-2.449293598294707E-16i
    // -2.2391774257946197E-16-1.2246467991473537E-16i
    // -4.0-9.797174393178826E-16i
    // 2.2391774257946197E-16-1.2246467991473525E-16i
    // 4.898587196589413E-16-2.4492935982947054E-16i
    // 4.688471024089326E-16-1.2246467991473517E-16i
    // ]
    //
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
