/**
 * Equals and hashCode are hard! This example is taken from the excellent book "Effective Java" by Joshua Bloch.
 * If you write java (or even if you don't) you should buy it.
 */
public final class PhoneNumber {
    private final short areaCode;
    private final short prefix;
    private final short lineNumber;

    public PhoneNumber(int areaCode, int prefix, int lineNumber) {
        rangeCheck(areaCode, 999, "areaCode");
        rangeCheck(prefix, 999, "prefix");
        rangeCheck(lineNumber, 9999, "lineNumber");

        this.areaCode = (short) areaCode;
        this.prefix = (short) prefix;
        this.lineNumber = (short) lineNumber;
    }

    private static void rangeCheck(int arg, int max, String name) {
        if (arg < 0 || arg > max) {
            throw new IllegalArgumentException(name + ": " + arg);
        }
    }

    @Override
    public boolean equals(Object o) {

        // Use the == operator to check if the argument is a reference to this object.
        if (o == this) return true;

        // Use the instanceof operator to check if the argument has the correct type.
        if (!(o instanceof PhoneNumber)) return false;

        // Cast the argument to the correct type.
        PhoneNumber pn = (PhoneNumber) o;

        // For each "significant" field in the class, check if that field of 
        // the argument matches the corresponding field of this object.
        // When you are finished ask yourself three questions:
        // 1. Is it symmetric?
        // 2. Is it transitive?
        // 3. Is it consistent?
        return pn.lineNumber == lineNumber
            && pn.prefix == prefix
            && pn.areaCode == areaCode;
    }   

    // Always override hashcode if you override equals!
    @Override
    public int hashCode() {

        // Store a constant nonzero value in an int variable called result
        int result = 17;

        // 1. For each significant field in f in your object (see equals), do the following
        // a. Compute an int hash code c for the field:
        // i.   If the field is a boolean, compute (f ? 1 : 0)
        // ii.  If the field is a byte, char, short or int, compute (int) f
        // iii. if the field is a long, compute (int) (f ^ (f >>> 32))
        // iv.  If the field is a float, compute Float.floatToIntBits(f)
        // v.   If the field is a double, compute Double.doubleToLongBits(f) and then has the resulting long as in iii
        // vi.  If the field is an object reference and this class's equals method compares the field by recursively invoking
        //      equals, recursively invoke hashCode on the field. If a more complex comparison is required, compute a
        //      "canonical representation" for this field and invoke hashCode on the canonical representation. If the value of
        //      the field is null, return 0 (or some other constant, but 0 is traditional).
        // vii. If the field is an array, treat it as if each element were a separate field. That is, compute a hash code
        //      for each significant element by applying these rules recursively, and combine these values per step b.
        //      If every element in an array field is significant, you can use one of the Arrays.hashCode methods added in 1.5.
        // b. Combine the hash code c computed in a into a result.
        // When you are finished ask yourself (and write tests to confirm) whether equal instances have equal hashCodes.
        result = 31 * result + areaCode;
        result = 31 * result + prefix;
        result = 31 * result + lineNumber;
        return result;
    }
}