/*
 * Copyright 2012 NTS New Technology Systems GmbH. All Rights reserved. NTS PROPRIETARY/CONFIDENTIAL. Use is subject to
 * NTS License Agreement. Address: Doernbacher Strasse 126, A-4073 Wilhering, Austria Homepage: www.ntswincash.com
 */
package utils;

import java.io.Serializable;
import java.util.Map.Entry;

/**
 * This class is a tuple containing two variables of possibly two different types. This is a convenience class that
 * should ease the burden when requiring multiple return values.
 * <p>
 * It implements {@link Serializable}, so it is serializable as long as the parameters are serializable.
 * @param <A> the type of the first parameter.
 * @param <B> the type of the second parameter.
 * @author GEUNT
 */
public final class Tuple2<A, B> implements Serializable, Entry<A, B> {

    private static final long serialVersionUID = 907647270469007515L;

    private A                 a;
    private B                 b;

    /**
     * The Constructor.
     */
    public Tuple2() {
    }

    /**
     * The Constructor.
     * @param a the a
     * @param b the b
     */
    public Tuple2(final A a, final B b) {
        this.a = a;
        this.b = b;
    }

    /**
     * Gets the first parameter of this tuple.
     * @return The first parameter.
     */
    public A getA() {
        return a;
    }

    /**
     * Sets the first parameter of this tuple.
     * @param a The first parameter.
     */
    public void setA(final A a) {
        this.a = a;
    }

    /**
     * Gets the second parameter of this tuple.
     * @return The second parameter.
     */
    public B getB() {
        return b;
    }

    /**
     * Sets the second parameter of this tuple.
     * @param b the b
     */
    public void setB(final B b) {
        this.b = b;
    }
    
    @Override
    public String toString() {
        String aString = (a != null) ? a.toString() : null;
        String bString = (b != null) ? b.toString() : null;
        return "Tuple2: [a=" + aString + ", b=" + bString + "]";
    }

    @Override
    public A getKey() {
        return this.getA();
    }

    @Override
    public B getValue() {
        return this.getB();
    }

    @Override
    public B setValue(final B value) {
        B old = this.getValue();
        this.setB(value);
        return old;
    }
}