package com.davidkeen;

/**
 * A 'rich' interface for data that have a single, natural ordering.
 * Clients need only implement the compare method.
 */
public interface Ordered<T> {

    /**
     * Result of comparing `this` with operand `that`.
     *
     * Implement this method to determine how instances of T will be sorted.
     *
     * Returns `x` where:
     *
     *   - `x < 0` when `this < that`
     *
     *   - `x == 0` when `this == that`
     *
     *   - `x > 0` when  `this > that`
     */
    int compare(T that);

    /**
     * Returns true if `this` is less than `that`
     */
    default boolean lessThan(T that) {
        return compare(that) < 0;
    }

    /**
     * Returns true if `this` is greater than `that`.
     */
    default boolean greaterThan(T that) {
        return compare(that) > 0;
    }

    /**
     * Returns true if `this` is less than or equal to `that`.
     */
    default boolean lessThanOrEqual(T that) {
        return compare(that) <= 0;
    }

    /**
     * Returns true if `this` is greater than or equal to `that`.
     */
    default boolean greaterThanOrEqual(T that) {
        return compare(that) >= 0;
    }

    /**
     * Result of comparing `this` with operand `that`.
     */
    default int compareTo(T that) {
        return compare(that);
    }
}
