/*
 * Copyright 2012 NTS New Technology Systems GmbH. All Rights reserved. NTS PROPRIETARY/CONFIDENTIAL. Use is subject to
 * NTS License Agreement. Address: Doernbacher Strasse 126, A-4073 Wilhering, Austria Homepage: www.ntswincash.com
 */
package info.unterrainer.java.tools.utils.serialization;

import java.io.IOException;
import java.io.InputStream;
import java.io.InvalidClassException;
import java.io.ObjectInputStream;
import java.io.ObjectStreamClass;

/**
 * The Class CloneInput.
 * @author GEUNT
 */
public class CloneInput extends ObjectInputStream {
    private final CloneOutput output;

    /**
     * Instantiates a new clone input.
     * @param in {@link InputStream} the in
     * @param output {@link CloneOutput} the output
     * @throws IOException Signals that an I/O exception has occurred.
     */
    CloneInput(final InputStream in, final CloneOutput output) throws IOException {
        super(in);
        this.output = output;
    }

    /*
     * (non-Javadoc)
     * @see java.io.ObjectInputStream#resolveClass(java.io.ObjectStreamClass)
     */
    @Override
    protected Class<?> resolveClass(final ObjectStreamClass osc) throws IOException, ClassNotFoundException {
        Class<?> c = output.getClassQueue().poll();
        String expected = osc.getName();
        String found = (c == null) ? null : c.getName();
        if (!expected.equals(found)) {
            throw new InvalidClassException("Classes desynchronized: " + "found " + found + " when expecting "
                    + expected);
        }
        return c;
    }

    /*
     * (non-Javadoc)
     * @see java.io.ObjectInputStream#resolveProxyClass(java.lang.String[])
     */
    @Override
    protected Class<?> resolveProxyClass(final String[] interfaceNames) throws IOException, ClassNotFoundException {
        return output.getClassQueue().poll();
    }
}