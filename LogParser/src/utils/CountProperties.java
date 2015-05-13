/*
 * Copyright 2012 NTS New Technology Systems GmbH. All Rights reserved. NTS PROPRIETARY/CONFIDENTIAL. Use is subject to
 * NTS License Agreement. Address: Doernbacher Strasse 126, A-4073 Wilhering, Austria Homepage: www.ntswincash.com
 */
package utils;

/**
 * The Class CountProperties.
 * <p>
 * This class contains the properties of a file or string.
 * @author GEUNT
 * @since 21.11.2013
 */
public class CountProperties {

    private final long lineCount;
    private final long wordCount;
    private final long characterCount;

    /**
     * Instantiates a new count properties.
     * @param lineCount {@link long} the line count
     * @param wordCount {@link long} the word count
     * @param characterCount {@link long} the character count
     */
    public CountProperties(final long lineCount, final long wordCount, final long characterCount) {
        super();
        this.lineCount = lineCount;
        this.wordCount = wordCount;
        this.characterCount = characterCount;
    }

    /**
     * Gets the line count.
     * @return the line count {@link long}
     */
    public long getLineCount() {
        return lineCount;
    }

    /**
     * Gets the word count.
     * @return the word count {@link long}
     */
    public long getWordCount() {
        return wordCount;
    }

    /**
     * Gets the character count.
     * @return the character count {@link long}
     */
    public long getCharacterCount() {
        return characterCount;
    }

    @Override
    public String toString() {
        return "[" + lineCount + "] lines, [" + wordCount + "] words, [" + characterCount + "] characters";
    }
}
