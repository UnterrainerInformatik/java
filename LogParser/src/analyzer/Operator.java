/*
 * Copyright 2012 NTS New Technology Systems GmbH. All Rights reserved. NTS PROPRIETARY/CONFIDENTIAL. Use is subject to
 * NTS License Agreement. Address: Doernbacher Strasse 126, A-4073 Wilhering, Austria Homepage: www.ntswincash.com
 */
package analyzer;

/**
 * The Enumeration Operator.
 */
public enum Operator {
    NONE,
    INTERSECTION,
    LEFT,
    RIGHT,
    UNION,
    XOR;

    /**
     * Displays the name of the enumeration in lower case with the first letter capitalized (the rest is converted to
     * lower case).
     * @return {@link String} the string representation of this instance (capitalized first letter; spaces instead of
     *         underscores).
     */
    @Override
    public String toString() {
        // Capitalize the first letter.
        String s = super.toString().replace("_", " ");
        return s.substring(0, 1).toUpperCase() + s.substring(1).toLowerCase();
    }
}