package com.davidkeen.test;

import org.junit.Test;

import static org.junit.Assert.*;

public class RationalTest {

    private final Rational half = new Rational(1, 2);
    private final Rational third = new Rational(1, 3);
    private final Rational twoThirds = new Rational(2, 3);
    private final Rational twoFourths = new Rational(2, 4);

    @Test(expected = IllegalArgumentException.class)
    public void divideByZero() throws Exception {
        new Rational(1, 0);
    }

    @Test
    public void testEquals() throws Exception {
        assertEquals(twoFourths, twoFourths);
        assertEquals(half, twoFourths);
        assertEquals(twoFourths, half);
        assertFalse(half.equals(twoThirds));
    }

    @Test
    public void testHashCode() throws Exception {
        assertEquals(twoFourths.hashCode(), new Rational(2, 4).hashCode());
        assertEquals(half.hashCode(), twoFourths.hashCode());
        assertFalse(half.hashCode() == twoThirds.hashCode());
    }

    @Test
    public void testString() throws Exception {
        assertEquals("2/5", new Rational(2, 5).toString());
        assertEquals("1/2", new Rational(3, 6).toString());
    }

    @Test
    public void compare() throws Exception {
        assertFalse(half.lessThan(third));
        assertTrue(third.lessThan(half));

        assertFalse(third.greaterThan(half));
        assertTrue(half.greaterThan(third));

        assertTrue(half.lessThanOrEqual(half));
        assertTrue(half.lessThanOrEqual(twoFourths));

        assertTrue(third.greaterThanOrEqual(third));
        assertTrue(twoFourths.greaterThanOrEqual(half));
    }

    @Test
    public void compareTo() throws Exception {
        assertTrue(third.compareTo(half) < 0);

        assertTrue(half.compareTo(half) == 0);
        assertTrue(half.compareTo(twoFourths) == 0);

        assertTrue(half.compareTo(third) > 0);
    }

    @Test
    public void add() throws Exception {
        Rational sevenSixths = half.add(twoThirds);

        assertEquals(7, sevenSixths.numerator);
        assertEquals(6, sevenSixths.denominator);
    }

    @Test
    public void times() throws Exception {
        Rational twoSixths = half.times(twoThirds);

        assertEquals(1, twoSixths.numerator);
        assertEquals(3, twoSixths.denominator);
    }
}