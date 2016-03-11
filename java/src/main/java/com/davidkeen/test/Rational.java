package com.davidkeen.test;

public class Rational implements Ordered<Rational> {

    public final int numerator;
    public final int denominator;

    public Rational(int numerator, int denominator) {
        if (denominator == 0) {
            throw new IllegalArgumentException("Denominator cannot be 0");
        }
        int g = gcd(Math.abs(numerator), Math.abs(denominator));
        this.numerator = numerator / g;
        this.denominator = denominator / g;
    }

    private int gcd(int a, int b) {
        if (b == 0) {
            return a;
        } else {
            return gcd(b, a % b);
        }
    }

    @Override
    public boolean equals(Object that) {
        if (that == this) {
            return true;
        }

        if (!(that instanceof Rational)) {
            return false;
        }

        Rational r = (Rational) that;
        return r.numerator == this.numerator && r.denominator == this.denominator;
    }

    @Override
    public int hashCode() {
        int result = 17;
        result = 31 * result + numerator;
        result = 31 * result + denominator;
        return result;
    }

    @Override
    public String toString() {
        return numerator + "/" + denominator;
    }

    @Override
    public int compare(Rational that) {
        return (this.numerator * that.denominator) - (this.numerator * this.denominator);
    }

    public Rational add(Rational that) {
        return new Rational(this.numerator * that.denominator + that.numerator * this.denominator,
                this.denominator * that.denominator);
    }

    public Rational times(Rational that) {
        return new Rational(this.numerator * that.numerator, this.denominator * that.denominator);
    }
}
