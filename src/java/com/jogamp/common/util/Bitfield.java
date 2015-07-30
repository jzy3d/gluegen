/**
 * Copyright 2015 JogAmp Community. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are
 * permitted provided that the following conditions are met:
 *
 *    1. Redistributions of source code must retain the above copyright notice, this list of
 *       conditions and the following disclaimer.
 *
 *    2. Redistributions in binary form must reproduce the above copyright notice, this list
 *       of conditions and the following disclaimer in the documentation and/or other materials
 *       provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY JogAmp Community ``AS IS'' AND ANY EXPRESS OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL JogAmp Community OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
 * ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF
 * ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * The views and conclusions contained in the software and documentation are those of the
 * authors and should not be interpreted as representing official policies, either expressed
 * or implied, of JogAmp Community.
 */
package com.jogamp.common.util;

/**
 * Simple bitfield interface for efficient storage access in O(1).
 */
public interface Bitfield {
    /**
     * Bit operation utilities (static).
     */
    public static class Util {
        /**
         * Returns the number of set bits within given 32bit integer in O(1)
         * using <i>HAKEM Bit Count</i>:
         * <pre>
         *   http://www.inwap.com/pdp10/hbaker/hakmem/hakmem.html
         *   http://home.pipeline.com/~hbaker1/hakmem/hacks.html#item169
         *   http://tekpool.wordpress.com/category/bit-count/
         * </pre>
         */
        public static final int getBitCount(final int n) {
            // Note: Original used 'unsigned int',
            // hence we use the unsigned right-shift '>>>'
            int c = n;
            c -= (n >>> 1) & 033333333333;
            c -= (n >>> 2) & 011111111111;
            return ( (c + ( c >>> 3 ) ) & 030707070707 ) % 63;
        }
    }
    /**
     * Simple {@link Bitfield} factory for returning the efficient implementation.
     */
    public static class Factory {
        /**
         * Creates am efficient {@link Bitfield} instance based on the requested {@code storageBitSize}.
         * <p>
         * Implementation returns a plain 32 bit integer field implementation for
         * {@code storageBitSize} &le; 32 bits or an 32 bit integer array implementation otherwise.
         * </p>
         */
        public static Bitfield create(final int storageBitSize) {
            if( 32 >= storageBitSize ) {
                return new jogamp.common.util.Int32Bitfield();
            } else {
                return new jogamp.common.util.Int32ArrayBitfield(storageBitSize);
            }
        }
    }

    /**
     * Returns the storage size in bit units, e.g. 32 bit for implementations using one {@code int} field.
     */
    int getStorageBitSize();

    /**
     * Returns the 32 bit integer mask w/ its lowest bit at the bit number {@code rightBitnum}.
     * @param rightBitnum bit number denoting the position of the lowest bit, restricted to [0..{@link #getStorageBitSize()}-32].
     * @throws IndexOutOfBoundsException if {@code rightBitnum} is out of bounds
     */
    int getInt32(final int rightBitnum) throws IndexOutOfBoundsException;

    /**
     * Sets the given 32 bit integer mask w/ its lowest bit at the bit number {@code rightBitnum}.
     * @param rightBitnum bit number denoting the position of the lowest bit, restricted to [0..{@link #getStorageBitSize()}-32].
     * @param mask denoting the 32 bit mask value to store
     * @throws IndexOutOfBoundsException if {@code rightBitnum} is out of bounds
     */
    void putInt32(final int rightBitnum, final int mask) throws IndexOutOfBoundsException;

    /**
     * Return <code>true</code> if the bit at position <code>bitnum</code> is set, otherwise <code>false</code>.
     * @param bitnum bit number, restricted to [0..{@link #getStorageBitSize()}-1].
     * @throws IndexOutOfBoundsException if {@code bitnum} is out of bounds
     */
    boolean get(final int bitnum) throws IndexOutOfBoundsException;

    /**
     * Set or clear the bit at position <code>bitnum</code> according to <code>bit</code>
     * and return the previous value.
     * @param bitnum bit number, restricted to [0..{@link #getStorageBitSize()}-1].
     * @throws IndexOutOfBoundsException if {@code bitnum} is out of bounds
     */
    boolean put(final int bitnum, final boolean bit) throws IndexOutOfBoundsException;

    /**
     * Set the bit at position <code>bitnum</code> according to <code>bit</code>.
     * @param bitnum bit number, restricted to [0..{@link #getStorageBitSize()}-1].
     * @throws IndexOutOfBoundsException if {@code bitnum} is out of bounds
     */
    void set(final int bitnum) throws IndexOutOfBoundsException;

    /**
     * Clear the bit at position <code>bitnum</code> according to <code>bit</code>.
     * @param bitnum bit number, restricted to [0..{@link #getStorageBitSize()}-1].
     * @throws IndexOutOfBoundsException if {@code bitnum} is out of bounds
     */
    void clear(final int bitnum) throws IndexOutOfBoundsException;

    /**
     * Returns the number of set bits within this bitfield.
     * <p>
     * Utilizes {#link {@link Bitfield.Util#getBitCount(int)}}.
     * </p>
     */
    int getBitCount();
}
