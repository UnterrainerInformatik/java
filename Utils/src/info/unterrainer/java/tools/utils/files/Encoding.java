/*
 * Copyright 2012 NTS New Technology Systems GmbH. All Rights reserved. NTS PROPRIETARY/CONFIDENTIAL. Use is subject to
 * NTS License Agreement. Address: Doernbacher Strasse 126, A-4073 Wilhering, Austria Homepage: www.ntswincash.com
 */
package info.unterrainer.java.tools.utils.files;

import java.nio.charset.Charset;

/**
 * The Enumeration Encoding.
 */
public enum Encoding {

    UTF8("UTF-8"),
    UTF16("UTF-16"),
    UTF16LE("UTF-16, Little Endian (LE)"),
    UTF16BE("UTF-16, Big Endian (BE)"),
    UTF32("UTF-32"),
    UTF32LE("UTF-32, Little Endian (LE)"),
    UTF32BE("UTF-32, Big Endian (BE)"),
    CP437("Cp437"),
    ANSI("ANSI"),
    ISO88591("ISO-8859-1"),
    CP850("Cp850");

    /** The encoding. */
    private String encoding;

    /**
     * Instantiates a new encodings.
     * @param encoding {@link String} the encoding
     */
    Encoding(final String encoding) {
        setEncoding(encoding);
    }

    /**
     * Converts the current encoding to a charset.
     * @return the charset {@link Charset}
     */
    public Charset toCharset() {
        return Charset.forName(getEncoding());
    }

    /**
     * Gets the encoding.
     * @return the encoding {@link String}
     */
    public String getEncoding() {
        return encoding;
    }

    /**
     * Sets the encoding.
     * @param newEncoding the new encoding
     */
    private void setEncoding(final String newEncoding) {
        encoding = newEncoding;
    }

    @Override
    public String toString() {
        return getEncoding();
    }
}