/*
 * Copyright 2012 NTS New Technology Systems GmbH. All Rights reserved. NTS PROPRIETARY/CONFIDENTIAL. Use is subject to
 * NTS License Agreement. Address: Doernbacher Strasse 126, A-4073 Wilhering, Austria Homepage: www.ntswincash.com
 */
package utils;

/**
 * The Class WrapParserData.
 * <p>
 * This class is a bean holding data that is used and changed during a wrap-parser run.
 * @author GEUNT
 * @since 28.11.2013
 */
public class WrapParserData {

    private StringBuilder result;
    private String        t;
    private int           index;
    private boolean       justDidNewline;
    private int           l;

    /**
     * Instantiates a new wrap parser data.
     * @param result {@link StringBuilder} the result
     * @param t {@link String} the t
     * @param index {@link int} the index
     * @param justDidNewline {@link boolean} the just did newline
     * @param l {@link int} the l
     */
    public WrapParserData(final StringBuilder result, final String t, final int index, final boolean justDidNewline,
                          final int l) {
        super();
        this.result = result;
        this.t = t;
        this.index = index;
        this.justDidNewline = justDidNewline;
        this.l = l;
    }

    /**
     * Gets the result.
     * @return the result {@link StringBuilder}
     */
    public StringBuilder getResult() {
        return result;
    }

    /**
     * Sets the result.
     * @param result the new result
     */
    public void setResult(final StringBuilder result) {
        this.result = result;
    }

    /**
     * Gets the t.
     * @return the t {@link String}
     */
    public String getT() {
        return t;
    }

    /**
     * Sets the t.
     * @param t the new t
     */
    public void setT(final String t) {
        this.t = t;
    }

    /**
     * Gets the index.
     * @return the index {@link int}
     */
    public int getIndex() {
        return index;
    }

    /**
     * Sets the index.
     * @param index the new index
     */
    public void setIndex(final int index) {
        this.index = index;
    }

    /**
     * Checks if is just did newline.
     * @return true {@link Boolean} , if is just did newline
     */
    public boolean isJustDidNewline() {
        return justDidNewline;
    }

    /**
     * Sets the just did newline.
     * @param justDidNewline the new just did newline
     */
    public void setJustDidNewline(final boolean justDidNewline) {
        this.justDidNewline = justDidNewline;
    }

    /**
     * Gets the l.
     * @return the l {@link int}
     */
    public int getL() {
        return l;
    }

    /**
     * Sets the l.
     * @param l the new l
     */
    public void setL(final int l) {
        this.l = l;
    }
}
