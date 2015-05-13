/*
 * Copyright 2012 NTS New Technology Systems GmbH. All Rights reserved. NTS PROPRIETARY/CONFIDENTIAL. Use is subject to
 * NTS License Agreement. Address: Doernbacher Strasse 126, A-4073 Wilhering, Austria Homepage: www.ntswincash.com
 */
package utils.serialization;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.LinkedList;
import java.util.Queue;

/**
 * The Class CloneOutput.
 * @author GEUNT
 */
public class CloneOutput extends ObjectOutputStream {
    private final Queue<Class<?>> classQueue = new LinkedList<Class<?>>();

    /**
     * Instantiates a new clone output.
     * @param out {@link OutputStream} the out
     * @throws IOException Signals that an I/O exception has occurred.
     */
    CloneOutput(final OutputStream out) throws IOException {
        super(out);
    }

    /*
     * (non-Javadoc)
     * @see java.io.ObjectOutputStream#annotateClass(java.lang.Class)
     */
    @Override
    protected void annotateClass(final Class<?> c) {
        classQueue.add(c);
    }

    /*
     * (non-Javadoc)
     * @see java.io.ObjectOutputStream#annotateProxyClass(java.lang.Class)
     */
    @Override
    protected void annotateProxyClass(final Class<?> c) {
        classQueue.add(c);
    }

    /**
     * Gets the class queue.
     * @return the class queue {@link Queue<Class<?>>}
     */
    protected Queue<Class<?>> getClassQueue() {
        return classQueue;
    }
}
