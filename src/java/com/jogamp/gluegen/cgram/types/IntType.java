/*
 * Copyright (c) 2003 Sun Microsystems, Inc. All Rights Reserved.
 * Copyright (c) 2010 JogAmp Community. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 *
 * - Redistribution of source code must retain the above copyright
 *   notice, this list of conditions and the following disclaimer.
 *
 * - Redistribution in binary form must reproduce the above copyright
 *   notice, this list of conditions and the following disclaimer in the
 *   documentation and/or other materials provided with the distribution.
 *
 * Neither the name of Sun Microsystems, Inc. or the names of
 * contributors may be used to endorse or promote products derived from
 * this software without specific prior written permission.
 *
 * This software is provided "AS IS," without a warranty of any kind. ALL
 * EXPRESS OR IMPLIED CONDITIONS, REPRESENTATIONS AND WARRANTIES,
 * INCLUDING ANY IMPLIED WARRANTY OF MERCHANTABILITY, FITNESS FOR A
 * PARTICULAR PURPOSE OR NON-INFRINGEMENT, ARE HEREBY EXCLUDED. SUN
 * MICROSYSTEMS, INC. ("SUN") AND ITS LICENSORS SHALL NOT BE LIABLE FOR
 * ANY DAMAGES SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING OR
 * DISTRIBUTING THIS SOFTWARE OR ITS DERIVATIVES. IN NO EVENT WILL SUN OR
 * ITS LICENSORS BE LIABLE FOR ANY LOST REVENUE, PROFIT OR DATA, OR FOR
 * DIRECT, INDIRECT, SPECIAL, CONSEQUENTIAL, INCIDENTAL OR PUNITIVE
 * DAMAGES, HOWEVER CAUSED AND REGARDLESS OF THE THEORY OF LIABILITY,
 * ARISING OUT OF THE USE OF OR INABILITY TO USE THIS SOFTWARE, EVEN IF
 * SUN HAS BEEN ADVISED OF THE POSSIBILITY OF SUCH DAMAGES.
 *
 * You acknowledge that this software is not designed or intended for use
 * in the design, construction, operation or maintenance of any nuclear
 * facility.
 *
 * Sun gratefully acknowledges that this software was originally authored
 * and developed by Kenneth Bradley Russell and Christopher John Kline.
 */
package com.jogamp.gluegen.cgram.types;

import com.jogamp.gluegen.ASTLocusTag;

public class IntType extends PrimitiveType implements Cloneable {

    private final boolean unsigned;

    public IntType(final String name, final SizeThunk size, final boolean unsigned, final int cvAttributes) {
        this(name, size, unsigned, cvAttributes, null);
    }

    public IntType(final String name, final SizeThunk size,
                   final boolean unsigned, final int cvAttributes,
                   final ASTLocusTag astLocus) {
        super(name, size, cvAttributes, astLocus);
        this.unsigned = unsigned;
    }

    /** Only for HeaderParser */
    public IntType(final String name, final SizeThunk size,
                   final boolean unsigned, final int cvAttributes,
                   final boolean isTypedef,
                   final ASTLocusTag astLocus) {
        super(name, size, cvAttributes, astLocus);
        this.unsigned = unsigned;
        if( isTypedef ) {
            setTypedef(cvAttributes);
        }
    }

    @Override
    protected int hashCodeImpl() {
      // 31 * x == (x << 5) - x
      return 31 + ( unsigned ? 1 : 0 );
    }

    @Override
    protected boolean equalsImpl(final Type arg) {
        final IntType t = (IntType) arg;
        return unsigned == t.unsigned;
    }

    @Override
    protected int hashCodeSemanticsImpl() {
      return hashCodeImpl();
    }

    @Override
    protected boolean equalSemanticsImpl(final Type arg) {
        final IntType t = (IntType) arg;
        return relaxedEqSem ||
               unsigned == t.unsigned;
    }

    @Override
    public IntType asInt() {
        return this;
    }

    /** Indicates whether this type is unsigned */
    public boolean isUnsigned() {
        return unsigned;
    }

    @Override
    public String getCName(final boolean includeCVAttrs) {
        if ( isTypedef() || !isUnsigned() ) {
            return super.getCName(includeCVAttrs);
        } else {
            return "unsigned "+super.getCName(includeCVAttrs);
        }
    }

    @Override
    public String toString() {
        return getCVAttributesString() + ( isUnsigned() && !isTypedef() ? "unsigned " : "") + getCName();
    }

    @Override
    Type newCVVariant(final int cvAttributes) {
        final Type t = new IntType(getName(), getSize(), isUnsigned(), cvAttributes, astLocus);
        if( isTypedef() ) {
            t.setTypedef(getTypedefCVAttributes());
        }
        return t;
    }
}
