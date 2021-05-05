import java.util.ArrayList;
import java.util.List;

public class ComplexNumber {
    /**
     * The real, Re(z), part of the <code>ComplexNumber</code>.
     */
    private double real;

    /**
     * The imaginary, Im(z), part of the <code>ComplexNumber</code>.
     */
    private double imaginary;

    /**
     * Constructs a new <code>ComplexNumber</code> object with both real and imaginary parts 0
     * (z = 0 + 0i).
     */
    public ComplexNumber() {
        real = 0.0;
        imaginary = 0.0;
    }

    /**
     * Constructs a new <code>ComplexNumber</code> object.
     * 
     * @param real
     *            the real part, Re(z), of the complex number
     * @param imaginary
     *            the imaginary part, Im(z), of the complex number
     */
    public ComplexNumber(double real, double imaginary) {
        this.real = real;
        this.imaginary = imaginary;
    }

    /**
     * Sets the value of current complex number to the passed complex number.
     * 
     * @param z
     *            the complex number
     */
    public void set(ComplexNumber z) {
        this.real = z.real;
        this.imaginary = z.imaginary;
    }

    /**
     * The real part of <code>ComplexNumber</code>
     * 
     * @return the real part of the complex number
     */
    public double getRe() {
        // return Double.valueOf(String.format("%.2f", this.real));
        return this.real;
    }

    /**
     * The imaginary part of <code>ComplexNumber</code>
     * 
     * @return the imaginary part of the complex number
     */
    public double getIm() {
        // return Double.valueOf(String.format("%.2f", this.imaginary));
        return this.imaginary;
    }

    /**
     * The modulus, magnitude or the absolute value of current complex number.
     * 
     * @return the magnitude or modulus of current complex number
     */
    public double mod() {
        return Math.sqrt(Math.pow(this.real, 2) + Math.pow(this.imaginary, 2));
    }

    /**
     * @return the complex number in x + yi format
     */
    @Override
    public String toString() {
        String re = this.getRe() + "";
        String im = "";
        if (this.getIm() < 0) {
            // im = String.format("%.2f", this.getIm()) + "i";
            im = this.getIm() + "i";
        } else {
            // im = "+" + String.format("%.2f", this.getIm()) + "i";
            im = "+" + this.getIm() + "i";
        }
        return re + im;
    }

    /**
     * Checks if the passed <code>ComplexNumber</code> is equal to the current.
     * 
     * @param z
     *            the complex number to be checked
     * @return true if they are equal, false otherwise
     */
    @Override
    public final boolean equals(Object z) {
        if (!(z instanceof ComplexNumber))
            return false;
        ComplexNumber a = (ComplexNumber) z;
        return (real == a.real) && (imaginary == a.imaginary);
    }

    // TODO
    // Fill in the operations between complex numbers used for DFT/IDFT/FFT/IFFT.

    /**
     * Description: <br/>
     * Add 2 complex numbers together in order to return a new complex number.
     * 
     * @author Yun Zhou
     * @param currentNumber
     *            current complex number
     * @param toAddNumber
     *            the complex number to be added
     * @return the addition of 2 input complexNumber which is a new complexNumber
     */
    public static ComplexNumber add(ComplexNumber currentNumber, ComplexNumber toAddNumber) {
        double a = currentNumber.getRe();
        double b = currentNumber.getIm();
        double c = toAddNumber.getRe();
        double d = toAddNumber.getIm();

        double n_real = a + c;
        double n_img = b + d;
        // assert n_real != 0;
        return new ComplexNumber(n_real, n_img);
    }

    public static ComplexNumber subtract(ComplexNumber currentNumber,
            ComplexNumber toSubtractComplexNumber) {
        double a = currentNumber.getRe();
        double b = currentNumber.getIm();
        double c = toSubtractComplexNumber.getRe();
        double d = toSubtractComplexNumber.getIm();

        double n_real = a - c;
        double n_img = b - d;
        return new ComplexNumber(n_real, n_img);
    }

    public static ComplexNumber multiply(ComplexNumber currentNumber,
            ComplexNumber toMultiplyComplexNumber) {

        double a = currentNumber.getRe();
        double b = currentNumber.getIm();
        double c = toMultiplyComplexNumber.getRe();
        double d = toMultiplyComplexNumber.getIm();

        double n_real = a * c - b * d;
        double n_img = b * c + a * d;
        return new ComplexNumber(n_real, n_img);
    }

    public static ComplexNumber divide(ComplexNumber currentNumber,
            ComplexNumber toDivideComplexNumber) {
        double a = currentNumber.getRe();
        double b = currentNumber.getIm();
        double c = toDivideComplexNumber.getRe();
        double d = toDivideComplexNumber.getIm();

        double n_real = (a * c + b * d) / (c * c + d * d);
        double n_img = (b * c - a * d) / (c * c + d * d);
        return new ComplexNumber(n_real, n_img);
    }

    /**
     * Description: <br/>
     * Return the new exponential <code>complexNumber</code> of the current
     * <code>complexNumer</code> object.
     * 
     * @author Yun Zhou
     * @param number
     *            to be calculate
     * @return the new exponential <code>complexNumber</code> of the current
     *         <code>complexNumer</code> object.
     */
    public static ComplexNumber exp(ComplexNumber number) {

        double a = number.getRe();
        double b = number.getIm();

        double n_real = Math.exp(a) * Math.cos(b);
        double n_img = Math.exp(a) * Math.sin(b);
        return new ComplexNumber(n_real, n_img);
    }

    /**
     * Description: <br/>
     * Method for converting <code>double</code> List to <code>ComplexNumber</code> List.
     * 
     * @author Yun Zhou
     * @param doubleList
     *            the list to be converted
     * @return the complexNumber list
     */
    public static ArrayList<ComplexNumber> convertToComplexNumberList(List<Double> doubleList) {
        ArrayList<ComplexNumber> toReturn = new ArrayList<ComplexNumber>();
        for (Double double_number : doubleList) {
            toReturn.add(new ComplexNumber(double_number, 0));
        }
        return toReturn;
    }

    /**
     * Description: <br/>
     * Convert the double number to the Complex number. (e.g. 20 = 20 + 0*i)
     * 
     * @author Yun Zhou
     * @param number
     *            number to be converted
     * @return complexNumber object
     */
    public static ComplexNumber convertToComplexNumber(double number) {
        return new ComplexNumber(number, 0);
    }
}