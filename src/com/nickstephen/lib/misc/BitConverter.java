package com.nickstephen.lib.misc;

/**
 * Java port of the C# BitConverter class. Used for converting to and from byte arrays with most other
 * data types.
 * @author Nick Stephen (a.k.a saltisgood)
 *
 */
public class BitConverter {
	
	/**
	 * Private constructor. Shouldn't ever be called, because you don't need an instance of this class (all static calls)
	 */
	private BitConverter() {}
	
	/**
	 * Parses a boolean and returns a 1-byte length byte array of its value
	 * @param x The value to be converted
	 * @return The corresponding 1-byte length byte array
	 */
	public static byte[] getBytes(Boolean x) {
		return new byte[] { (byte) (x ? 1:0)};
	}

	/**
	 * Parses a char and returns a 2-byte length byte array of its value
	 * @param c The value to be converted
	 * @return The corresponding 2-byte length byte array
	 */
	public static byte[] getBytes(char c) {
		return new byte[] { (byte) (c & 0xFF), (byte) (c >> 8 & 0xFF) };
	}
	
	/**
	 * Parses a double and returns a 8-byte length byte array of its value
	 * @param x The value to be converted
	 * @return The corresponding 8-byte length byte array
	 */
	public static byte[] getBytes(double x) {
		return getBytes(Double.doubleToRawLongBits(x));
	}
	
	/**
	 * Parses a short and returns a 2-byte length byte array of its value
	 * @param x The value to be converted
	 * @return The corresponding 2-byte length byte array
	 */
	public static byte[] getBytes(short x) {
		return new byte[] { (byte) (x >>> 8), (byte) x };
	}
	
	/**
	 * Parses an int and returns a 4-byte length byte array of its value
	 * @param x The value to be converted
	 * @return The corresponding 4-byte length byte array
	 */
	public static byte[] getBytes(int x) {
		return new byte[] { (byte) (x >>> 24), (byte) (x >>> 16), (byte) (x >>> 8), (byte) x };
	}
	
	/**
	 * Parses a long and returns an 8-byte length byte array of its value
	 * @param x The value to be converted
	 * @return The corresopnding 8-byte length byte array
	 */
	public static byte[] getBytes(long x) {
		return new byte[] { (byte) (x >>> 56), (byte) (x >>> 48), (byte) (x >>> 40), (byte) (x >>> 32),
				(byte) (x >>> 24), (byte) (x >>> 16), (byte) (x >>> 8), (byte) x };
	}
	
	/**
	 * Parses a float and returns a 4-byte length byte array of its value
	 * @param x The value to be converted
	 * @return The corresponding 4-byte length byte array
	 */
	public static byte[] getBytes(float x) {
		return getBytes(Float.floatToRawIntBits(x));
	}
	
	/**
	 * Parses a string and returns a n-byte length byte array of its value
	 * (where n is the length of the string). Equivalent to x.getBytes();
	 * @param x The value to be converted
	 * @return The corresponding n-byte length byte array
	 */
	public static byte[] getBytes(String x) {
		return x.getBytes();
	}
	
	/**
	 * Convert a variable in double bit format to a long. Should only be used
	 * for preservation of bits for simplification purposes.
	 * @param x The value to be converted
	 * @return A variable with the same bits but different format
	 */
	public static long doubleToInt64Bits(double x) {
		return Double.doubleToRawLongBits(x);
	}
	
	/**
	 * Convert a variable in long bit format to a double. Should only be used
	 * for preservation of bits for simplification purposes.
	 * @param x
	 * @return
	 */
	public static double int64BitsToDouble(long x) {
		return (double)x;
	}
	
	/**
	 * Convert a byte array to its boolean value. Arrays longer than 1 byte can
	 * be passed to the function with a non-zero starting index but the index
	 * must be at least 1 less than the size of the array else an exception will
	 * be thrown. Arrays shorter than 1 byte will also purposely throw an exception.
	 * @param bytes The array representation to convert from
	 * @param index The starting index to read from (> 0)
	 * @return The corresponding boolean value
	 * @throws Exception Thrown by a null array, negative index or other out of bounds access
	 */
	public static boolean toBoolean(byte[] bytes, int index) throws Exception {
		if (bytes.length < 1)
			throw new Exception("Length of byte array must be at least 1 index");
		else if (index + 1 > bytes.length)
			throw new Exception("Byte array must be at least 1 byte greater than the starting index");
		return bytes[index] != 0;
	}
	
	/**
	 * Convert a byte array to its char value. Arrays longer than 2 bytes can
	 * be passed to the function with a non-zero starting index but the index
	 * must be at least 2 less than the size of the array else an exception will
	 * be thrown. Arrays shorter than 2 bytes will also purposely throw an exception. 
	 * @param bytes The array representation to convert from
	 * @param index The starting index to read from (> 0)
	 * @return The corresponding char value
	 * @throws java.lang.RuntimeException Thrown by a null array, negative index or other out of bounds access
	 */
	public static char toChar(byte[] bytes, int index) throws RuntimeException {
		if (bytes.length < 2) 
			throw new RuntimeException("Length of byte array must be at least 2");
		else if (index + 2 > bytes.length)
			throw new RuntimeException("Byte array must be at least 2 bytes greater than the starting index");
		return (char) ((0xFF & bytes[index++]) << 8 | (0xFF & bytes[index]));
	}
	
	/**
	 * Convert a byte array to its double value. Arrays longer than 8 bytes can
	 * be passed to the function with a non-zero starting index but the index
	 * must be at least 8 less than the size of the array else an exception will
	 * be thrown. Arrays shorter than 8 bytes will also purposely throw an exception.
	 * @param bytes The array representation to convert from
	 * @param index The starting index to read from (> 0)
	 * @return The corresponding double value
	 * @throws Exception Thrown by a null array, negative index or other out of bounds access
	 */
	public static double toDouble(byte[] bytes, int index) throws Exception { 
		if(bytes.length < 8)
			throw new Exception("The length of the byte array must be at least 8 bytes long.");
		else if (index + 8 > bytes.length)
			throw new Exception("Byte array must be at least 8 bytes greater than the starting index");
		return Double.longBitsToDouble( toInt64(bytes, index)); 
	} 
	
	/**
	 * Convert a byte array to its short value. Arrays longer than 2 bytes can
	 * be passed to the function with a non-zero starting index but the index
	 * must be at least 2 less than the size of the array else an exception will
	 * be thrown. Arrays shorter than 2 bytes will also purposely throw an exception.
	 * @param bytes The array representation to convert from
	 * @param index The starting index to read from (> 0)
	 * @return The corresponding short value
	 * @throws java.lang.RuntimeException Thrown by a null array, negative index or other out of bounds access
	 */
	public static short toInt16(byte[] bytes, int index) throws RuntimeException {
		if(bytes.length < 2) 
			throw new RuntimeException("The length of the byte array must be at least 2 bytes long.");
		else if (index + 2 > bytes.length)
			throw new RuntimeException("Byte array must be at least 2 bytes greater than the starting index");
		return (short)( (0xff & bytes[index]) << 8 | (0xff & bytes[index + 1]) );
	} 
	
	/**
	 * Convert a byte array to its int value. Arrays longer than 4 bytes can
	 * be passed to the function with a non-zero starting index but the index
	 * must be at least 4 less than the size of the array else an exception will
	 * be thrown. Arrays shorter than 4 bytes will also purposely throw an exception.
	 * @param bytes The array representation to convert from
	 * @param index The starting index to read from (> 0)
	 * @return The corresponding int value
	 * @throws RuntimeException Thrown by a null array, negative index or other out of bounds access
	 */
	public static int toInt32(byte[] bytes, int index) throws RuntimeException {
		if(bytes.length < 4) 
			throw new RuntimeException("The length of the byte array must be at least 4 bytes long.");
		else if (index + 4 > bytes.length)
			throw new RuntimeException("Byte array must be at least 4 bytes greater than the starting index");
		return (int)( (int)(0xff & bytes[index]) << 24 | (int)(0xff & bytes[index + 1]) << 16 | (int)(0xff & bytes[index + 2]) << 8 |
				(int)(0xff & bytes[index + 3]) );
	} 
	
	/**
	 * Convert a byte array to its long value. Arrays longer than 8 bytes can
	 * be passed to the function with a non-zero starting index but the index
	 * must be at least 8 less than the size of the array else an exception will
	 * be thrown. Arrays shorter than 8 bytes will also purposely throw an exception.
	 * @param bytes The array representation to convert from
	 * @param index The starting index to read from (> 0)
	 * @return The corresponding long value
	 * @throws Exception Thrown by a null array, negative index or other out of bounds access
	 */
	public static long toInt64(byte[] bytes, int index) throws Exception { 
		if(bytes.length < 8) 
			throw new Exception("The length of the byte array must be at least 8 bytes long.");
		else if (index + 8 > bytes.length)
			throw new Exception("Byte array must be at least 8 bytes greater than the starting index");
		return (long)((long)(0xff & bytes[index]) << 56 | (long)(0xff & bytes[index + 1]) << 48 | (long)(0xff & bytes[index + 2]) << 40 | 
				(long)(0xff & bytes[index + 3]) << 32 | (long)(0xff & bytes[index + 4]) << 24 | (long)(0xff & bytes[index + 5]) << 16 | 
				(long)(0xff & bytes[index + 6]) << 8 | (long)(0xff & bytes[index + 7]) );
	} 
	
	/**
	 * Convert a byte array to its float value. Arrays longer than 4 bytes can
	 * be passed to the function with a non-zero starting index but the index
	 * must be at least 4 less than the size of the array else an exception will
	 * be thrown. Arrays shorter than 4 bytes will also purposely throw an exception.
	 * @param bytes The array representation to convert from
	 * @param index The starting index to read from (> 0)
	 * @return The corresponding float value
	 * @throws Exception Thrown by a null array, negative index or other out of bounds access
	 */
	public static float toSingle(byte[] bytes, int index) throws Exception { 
		if(bytes.length < 4) 
			throw new Exception("The length of the byte array must be at least 4 bytes long.");
		else if (index + 4 > bytes.length)
			throw new Exception("Byte array must be at least 4 bytes greater than the starting index");
		return Float.intBitsToFloat( toInt32(bytes, index)); 
	} 
	
	/**
	 * Convert a byte array to its string value. Arrays shorter than 1 byte
	 * will purposely throw an exception. Equivalent to new String(bytes);
	 * @param bytes The array representation to convert from
	 * @return The corresponding string value
	 * @throws Exception Thrown by a null array or other out of bounds access
	 */
	public static String toString(byte[] bytes) throws Exception { 
		if(bytes.length < 1) 
			throw new Exception("The byte array must have at least 1 byte."); 
		return new String(bytes); 
	}
}
