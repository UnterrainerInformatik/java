/*
 * Copyright 2012 NTS New Technology Systems GmbH. All Rights reserved. NTS PROPRIETARY/CONFIDENTIAL. Use is subject to
 * NTS License Agreement. Address: Doernbacher Strasse 126, A-4073 Wilhering, Austria Homepage: www.ntswincash.com
 */
package utils;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * The Class Strings.
 * <p>
 * This class contains a collection of static helper-methods used in conjunction with strings.
 * @author GEUNT
 */
public final class Strings {

    /**
     * Instantiates a new Strings. Here in order to hide the public constructor since this is a static utility class.
     */
    private Strings() {
    }

    public static boolean isNullOrEmpty(String string) {
    	return string == null || string.equals("");
    }
    
    /**
     * Returns the index of the first non-numeric non-space character starting with the given starting-position.
     * @param startingIndex {@link int} the starting index
     * @param str {@link String} the string
     * @return the index {@link int}
     */
    public static int indexOfFirstNonNumericNonSpaceCharacterFrom(final int startingIndex, final String str) {
        List<Character> numbers = Arrays.asList(new Character[] { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' });
        if ((str == null) || (str.length() == 0) || (startingIndex >= str.length())) {
            return -1;
        }

        int pos = startingIndex;
        while (pos < str.length()) {
            char c = str.charAt(pos);
            if ((c != ' ') && !numbers.contains(c)) {
                return pos;
            }
            pos++;
        }
        return str.length();
    }

    /**
     * Centers a given text to the given width and returns the number of prefix-characters and postfix-characters you
     * would need in order to center your String.
     * @param text {@link String} the text to calculate the centered-pre- and postfix for
     * @param lineWidth {@link int} the line width
     * @return the tuple2 {@link Tuple2<Integer,Integer>} containing the first length of the prefix (A) and the length
     *         of the postfix (B) in order to center the given text (as in
     *         {@code String result = Strings.repeat(" ", result.getA()) + text + Strings.repeat(" ", result.getB());}
     */
    public static Tuple2<Integer, Integer> getCenterPaddings(final String text, final int lineWidth) {
        int firstHalf = (lineWidth / 2) - 1 - (text.length() / 2);
        int secondHalf = lineWidth - firstHalf - text.length();

        return new Tuple2<Integer, Integer>(firstHalf, secondHalf);
    }

    /**
     * Centers a given String using the given line-width and a space as padding-character for pre- and postfix.
     * @param text {@link String} the text to center
     * @param lineWidth {@link int} the line width
     * @return the string {@link String}
     */
    public static String center(final String text, final int lineWidth) {
        return center(text, lineWidth, ' ', ' ');
    }

    /**
     * Centers a given String using the given line-width and the given padding-character as pre- and postfix.
     * @param text {@link String} the text to center
     * @param lineWidth {@link int} the line width
     * @param paddingCharacter {@link char} the padding character
     * @return the string {@link String}
     */
    public static String center(final String text, final int lineWidth, final char paddingCharacter) {
        return center(text, lineWidth, paddingCharacter, paddingCharacter);
    }

    /**
     * Centers a given String using the given line-width and the given padding-characters.
     * @param text {@link String} the text to center
     * @param lineWidth {@link int} the line width
     * @param preFixPaddingCharacter {@link char} the pre fix padding character
     * @param postFixPaddingCharacter {@link char} the post fix padding character
     * @return the string {@link String}
     */
    public static String center(final String text, final int lineWidth, final char preFixPaddingCharacter,
                                final char postFixPaddingCharacter) {
        Tuple2<Integer, Integer> c = Strings.getCenterPaddings(text, lineWidth);

        return Strings.repeat(preFixPaddingCharacter + "", c.getA()) + text
                + Strings.repeat(postFixPaddingCharacter + "", c.getB());
    }

    /**
     * Returns a string consisting of a specific number of concatenated copies of an input string. For example,
     * repeat("hey", 3) returns the string "heyheyhey".
     * @param string {@link String} any non-null string
     * @param count {@link int} the number of times to repeat it; a nonnegative integer
     * @return the string {@link String} containing string repeated count times (the empty string if count is zero)
     */
    public static String repeat(final String string, final int count) {
    	String result = "";
    	for(int i = 0; i < count; i++) {
    		result += string;
    	}
        return result;
    }
    
    /**
     * Capitalizes a given string. Returns the same string but with an upper-case first letter.
     * @param str {@link String} the string to capitalize
     * @return the string {@link String}
     */
    public static String capitalize(final String str) {
        if ((str == null) || ((str.length()) == 0)) {
            return str;
        }
        return Character.toUpperCase(str.charAt(0)) + str.substring(1).toString();
    }

    /**
     * Uncapitalizes a given string. Returns the same string but with a lower-case first letter.
     * @param str {@link String} the string to capitalize
     * @return the string {@link String}
     */
    public static String uncapitalize(final String str) {
        if ((str == null) || ((str.length()) == 0)) {
            return str;
        }
        return Character.toLowerCase(str.charAt(0)) + str.substring(1).toString();
    }

    /**
     * Converts a string into title-case.
     * <p>
     * {@code "this Is a title-cased character-Sequence."} will become<br/>
     * {@code "This Is A Title-cased Character-sequence."} with forceLowerCase = true for example and<br/>
     * {@code "This Is A Title-cased Character-Sequence."} with forceLowerCase = false (the difference is the 's' in '-
     * sequence').
     * @param str {@link String} the string to convert
     * @param forceLowerCase {@link boolean} if false the casing of in-word characters is kept
     * @return the converted string {@link String}
     */
    public static String titleCase(final String str, final boolean forceLowerCase) {
        if (str == null) {
            return null;
        }

        final StringBuilder result = new StringBuilder(str.length());
        for (final String word : str.split(" ")) {
            if (!word.isEmpty()) {
                if (!forceLowerCase) {
                    result.append(capitalize(word));
                } else {
                    result.append(Character.toUpperCase(word.charAt(0)));
                    result.append(word.substring(1).toLowerCase());
                }
            }
            if (!(result.length() == str.length())) {
                result.append(" ");
            }
        }

        return result.toString();
    }

    /**
     * Replaces all occurrences of non-alphanumeric characters within a string with the given replacement-string.
     * <p>
     * So every occurrence of a character other than a upper- or lower-case character, a number will get replaced.
     * @param str {@link String} the string to manipulate
     * @param replacement {@link String} the replacement
     * @return the result-string {@link String}
     */
    public static String replaceNonAlphanumeric(final String str, final String replacement) {
        return str.replaceAll("[^a-zA-Z0-9]", replacement);
    }

    /**
     * Replaces all occurrences of non-alpha characters (like {@like #replaceNonAlphanumeric(String)} but replacing
     * numbers as well) within a string with the given replacement-string.
     * <p>
     * So every occurrence of a character other than a upper- or lower-case character will get replaced.
     * @param str {@link String} the string to manipulate
     * @param replacement {@link String} the replacement
     * @return the result-string {@link String}
     */
    public static String replaceNonAlpha(final String str, final String replacement) {
        return str.replaceAll("[^a-zA-Z]", replacement);
    }

    /**
     * Replaces anything between parenthesis '()', '[]' or '{}' with a replacement-string.
     * @param str {@link String} the string to modify
     * @param replacement {@link String} the replacement
     * @return the string {@link String}
     */
    public static String replaceAnythingBetweenParenthesis(final String str, final String replacement) {
        return str.replaceAll("\\[.*?\\]", replacement).replaceAll("\\(.*?\\)", replacement)
                .replaceAll("\\{.*?\\}", replacement);
    }

    /**
     * Replaces all occurrences of non-alpha or space characters within a string with the given replacement-string.
     * <p>
     * So every occurrence of a character other than a upper- or lower-case character or a space will get replaced.
     * @param str {@link String} the string to manipulate
     * @param replacement {@link String} the replacement
     * @return the result-string {@link String}
     */
    public static String replaceNonAlphaSpace(final String str, final String replacement) {
        return str.replaceAll("[^a-zA-Z ]", replacement);
    }

    /**
     * Replaces all occurrences of non-alphanumeric or non-underscore characters within a string with the given
     * replacement-string.
     * <p>
     * So every occurrence of a character other than a upper- or lower-case character, a number or an underscore will
     * get replaced.
     * @param str {@link String} the string to manipulate
     * @param replacement {@link String} the replacement
     * @return the result-string {@link String}
     */
    public static String replaceNonAlphanumericUnderscore(final String str, final String replacement) {
        return str.replaceAll("[^a-zA-Z0-9_]", replacement);
    }

    /**
     * Replaces all occurrences of non-alpha or space or underscore characters within a string with the given
     * replacement-string.
     * <p>
     * So every occurrence of a character other than a upper- or lower-case character or a space or an underscore will
     * get replaced.
     * @param str {@link String} the string to manipulate
     * @param replacement {@link String} the replacement
     * @return the result-string {@link String}
     */
    public static String replaceNonAlphaSpaceUnderscore(final String str, final String replacement) {
        return str.replaceAll("[^a-zA-Z _]", replacement);
    }

    /**
     * Converts a given string to Pascal-case.
     * <p>
     * {@code "this Is a cased character-Sequence."} will become<br/>
     * {@code "ThisIsACasedCharacter-sequence."} with forceLowerCase = true for example and<br/>
     * {@code "ThisIsACasedCharacter-Sequence."} with forceLowerCase = false (the difference is the 's' in '-
     * sequence').
     * @param str {@link String} the string to convert
     * @param forceLowerCase {@link boolean} if false the casing of in-word characters is kept
     * @return the string {@link String}
     */
    public static String pascalCase(final String str, final boolean forceLowerCase) {
        return titleCase(str, forceLowerCase).replace(" ", "");
    }

    /**
     * Converts a given string to Camel-case.
     * <p>
     * {@code "this Is a cased character-Sequence."} will become<br/>
     * {@code "thisIsACasedCharacter-sequence."} with forceLowerCase = true for example and<br/>
     * {@code "thisIsACasedCharacter-Sequence."} with forceLowerCase = false (the difference is the 's' in '-
     * sequence').
     * @param str {@link String} the string to convert
     * @param forceLowerCase {@link boolean} if false the casing of in-word characters is kept
     * @return the string {@link String}
     */
    public static String camelCase(final String str, final boolean forceLowerCase) {
        return uncapitalize(pascalCase(str, forceLowerCase));
    }

    /**
     * Strips all space-characters after each underscore-character in a string.
     * @param str {@link String} the string to modify
     * @return the string {@link String}
     */
    public static String stripSpacesAfterUnderscores(final String str) {
        String result = str;
        while (result.indexOf("_ ") > -1) {
            result = result.replace("_ ", "_");
        }
        return result;
    }

    /**
     * Wraps the given string to the given length by inserting new-line characters.
     * @param text {@link String} the text to wrap
     * @param maxLineLength {@link int} the maximal line-length the text should have
     * @return the string {@link String} the wrapped text
     */
    public static String wrap(final String text, final int maxLineLength) {
        return wrap(text, maxLineLength, true, true, true, true, "\n");
    }

    /**
     * Wraps a given string to the given length by inserting variable new-line characters.
     * @param text {@link String} the text to wrap
     * @param maxLineLength {@link int} the maximal line-length the text should have
     * @param isWrapAtWhitespaceOnly {@link boolean} if false, then it warps in mid-word as well
     * @param isKeepExistingNewlines {@link boolean} if true, then it keeps existing newline characters
     * @param isDeleteSpacesAfterNewline {@link boolean} if true, then all white-spaces after a new-line are deleted
     * @param isDeleteSpacesBeforeNewline {@link boolean} if true, then all white-spaces before a new-line are deleted
     * @param insertAsNewline {@link String} the string that gets inserted as a new-line delimiter
     * @return the string {@link String} the wrapped text
     */
    public static String wrap(final String text, final int maxLineLength, final boolean isWrapAtWhitespaceOnly,
                              final boolean isKeepExistingNewlines, final boolean isDeleteSpacesAfterNewline,
                              final boolean isDeleteSpacesBeforeNewline, final String insertAsNewline) {

        WrapParserData pd = new WrapParserData(new StringBuilder(), unifyLineBreaks(text, isKeepExistingNewlines), 0,
                false, maxLineLength);

        while (!pd.getT().equals("")) {

            if (pd.getT().length() < pd.getL()) {
                pd.getResult().append(pd.getT());
                break;
            }

            // Find the next whitespace character other than the current one.
            pd.setIndex(indexOfNextWhitespace(pd.getT()));
            if ((pd.getIndex() == -1) || ((pd.getIndex() + 1) > pd.getL())) {
                doBreak(isWrapAtWhitespaceOnly, isDeleteSpacesBeforeNewline, isDeleteSpacesAfterNewline,
                        insertAsNewline, maxLineLength, pd);
            } else {
                whiteSpaceFound(isDeleteSpacesBeforeNewline, isDeleteSpacesAfterNewline, insertAsNewline,
                        maxLineLength, pd);
            }
        }
        return pd.getResult().toString();
    }

    private static void doBreak(final boolean isWrapAtWhitespaceOnly, final boolean isDeleteSpacesBeforeNewline,
                                final boolean isDeleteSpacesAfterNewline, final String insertAsNewline,
                                final int maxLineLength, final WrapParserData pd) {
        // None was found or found too late. Break!
        if (!isWrapAtWhitespaceOnly || pd.isJustDidNewline()) {
            // Hard break.
            pd.getResult().append(pd.getT().substring(0, pd.getL()));
            pd.setT(pd.getT().substring(pd.getL()));
        } else {
            // Soft break.
            if (pd.getIndex() == pd.getL()) {
                String part = pd.getT().substring(0, pd.getIndex() + 1);
                if (Strings.indexOfLastWhitespace(part) == (part.length() - 1)) {
                    pd.getResult().append(part.substring(0, part.length() - 1));
                    pd.setT(pd.getT().substring(part.length() - 1));
                }
            }
        }

        if (isDeleteSpacesAfterNewline) {
            pd.setResult(new StringBuilder(Strings.trimSpacesEnd(pd.getResult().toString())));
        }
        pd.getResult().append(insertAsNewline);

        if (isDeleteSpacesBeforeNewline) {
            pd.setT(Strings.trimSpacesBeginning(pd.getT()));
        }

        // Reset the counter.
        pd.setJustDidNewline(true);
        pd.setL(maxLineLength);
    }

    private static void whiteSpaceFound(final boolean isDeleteSpacesBeforeNewline,
                                        final boolean isDeleteSpacesAfterNewline, final String insertAsNewline,
                                        final int maxLineLength, final WrapParserData pd) {
        if (pd.getT().substring(0, pd.getIndex() + 1).contains("\n")) {
            // We will write a newline that was contained in the input-string. Reset counters.
            pd.setIndex(pd.getT().indexOf('\n'));
            pd.getResult().append(pd.getT().substring(0, pd.getIndex()));

            if (isDeleteSpacesAfterNewline) {
                pd.setResult(new StringBuilder(Strings.trimSpacesEnd(pd.getResult().toString())));
            }
            pd.getResult().append(insertAsNewline);

            if (isDeleteSpacesBeforeNewline) {
                pd.setT(Strings.trimSpacesBeginning(pd.getT()));
            }

            pd.setT(pd.getT().substring(pd.getIndex() + 1));
            pd.setJustDidNewline(true);
            pd.setL(maxLineLength);
        } else {
            pd.getResult().append(pd.getT().substring(0, pd.getIndex() + 1));
            pd.setT(pd.getT().substring(pd.getIndex() + 1));
            pd.setL(pd.getL() - (pd.getIndex() + 1));
            pd.setJustDidNewline(false);
        }
    }

    private static String unifyLineBreaks(final String t, final boolean isKeepExistingNewlines) {
        String result = t.replace("\r\n", "\n");

        if (!isKeepExistingNewlines) {
            result = result.replace("\n", " ");
        }
        return result;
    }

    /**
     * Finds the index of the next occurrence of a whitespace character within a given text.
     * @param text {@link String} the text to search a whitespace character in
     * @return the int {@link int} the index of the next whitespace-character or -1 if none was found
     */
    public static int indexOfNextWhitespace(final String text) {
        int r = text.indexOf('\r');
        int n = text.indexOf('\n');
        int t = text.indexOf('\t');
        int s = text.indexOf(' ');

        if ((r == -1) && (n == -1) && (t == -1) && (s == -1)) {
            return -1;
        }

        if (r == -1) {
            r = Integer.MAX_VALUE;
        }
        if (n == -1) {
            n = Integer.MAX_VALUE;
        }
        if (t == -1) {
            t = Integer.MAX_VALUE;
        }
        if (s == -1) {
            s = Integer.MAX_VALUE;
        }

        return Math.min(Math.min(r, n), Math.min(t, s));
    }

    /**
     * Finds the index of the last occurrence of a whitespace character within a given text.
     * @param text {@link String} the text to search a whitespace character in
     * @return the int {@link int} the index of the last whitespace-character or -1 if none was found
     */
    public static int indexOfLastWhitespace(final String text) {
        int r = text.lastIndexOf('\r');
        int n = text.lastIndexOf('\n');
        int t = text.lastIndexOf('\t');
        int s = text.lastIndexOf(' ');

        if ((r == -1) && (n == -1) && (t == -1) && (s == -1)) {
            return -1;
        }

        return Math.max(Math.max(r, n), Math.max(t, s));
    }

    /**
     * Checks if a given character is a whitespace character (carriage-return, newline, tab or space).
     * @param character {@link String} the character
     * @return true {@link Boolean} , if is whitespace
     */
    public static boolean isWhitespace(final String character) {
        return character.equals("\r") || character.equals("\n") || character.equals("\t") || character.equals(" ");
    }

    /**
     * Trims the beginning of a given string (removes all the whitespace-characters).
     * @param text {@link String} the text
     * @return the string {@link String}
     */
    public static String trimBeginning(final String text) {
        String result = text;
        while ((result.length() > 0) && isWhitespace(result.substring(0, 1))) {
            result = result.substring(1);
        }
        return result;
    }

    /**
     * Trims the end of a given string (removes all the whitespace-characters).
     * @param text {@link String} the text
     * @return the string {@link String}
     */
    public static String trimEnd(final String text) {
        String result = text;
        while ((result.length() > 0) && isWhitespace(result.substring(result.length() - 1, result.length()))) {
            result = result.substring(0, result.length() - 1);
        }
        return result;
    }

    /**
     * Trims the beginning of a given string (removes all the whitespace-characters).
     * @param text {@link String} the text
     * @return the string {@link String}
     */
    public static String trimSpacesBeginning(final String text) {
        String result = text;
        while ((result.length() > 0) && result.substring(0, 1).equals(" ")) {
            result = result.substring(1);
        }
        return result;
    }

    /**
     * Trims the end of a given string (removes all the whitespace-characters).
     * @param text {@link String} the text
     * @return the string {@link String}
     */
    public static String trimSpacesEnd(final String text) {
        String result = text;
        while ((result.length() > 0) && result.substring(result.length() - 1, result.length()).equals(" ")) {
            result = result.substring(0, result.length() - 1);
        }
        return result;
    }

    /**
     * Strips the leading and trailing quotes {@code (")} from a string if it starts with one and ends with one. Does
     * not trim the string first; That's up to you.
     * @param text {@link String} the text to strip the quotes from
     * @return the string {@link String} the text without the quotes or the original text, if the condition was not
     *         satisfied
     */
    public static String stripQuotes(final String text) {
        if (text == null) {
            return null;
        }

        if ((text.length() > 1) && text.startsWith("\"") && text.endsWith("\"")) {
            return text.substring(1, text.length() - 1);
        }
        return text;
    }

    /**
     * Strips the leading and trailing quotes {@code (')} from a string if it starts with one and ends with one. Does
     * not trim the string first; That's up to you.
     * @param text {@link String} the text to strip the quotes from
     * @return the string {@link String} the text without the quotes or the original text, if the condition was not
     *         satisfied
     */
    public static String stripSingleQuotes(final String text) {
        if (text == null) {
            return null;
        }

        if ((text.length() > 1) && text.startsWith("'") && text.endsWith("'")) {
            return text.substring(1, text.length() - 1);
        }
        return text;
    }

    /**
     * Makes sure that the given string ends with the given post-fix. If it doesn't, the given post-fix is appended,
     * otherwise the original string is returned.
     * @param text {@link String} the text to check for the given post-fix
     * @param postfix {@link String} the post-fix to check for
     * @return the string {@link String} that is ending with the given post-fix in any case or null, if both input
     *         parameters were null
     */
    public static String endWith(final String text, final String postfix) {

        if ((postfix == null) && (text == null)) {
            return null;
        }

        String result;

        if (postfix == null) {
            result = text;
        } else {
            if (text == null) {
                result = postfix;
            } else {
                if (text.endsWith(postfix)) {
                    result = text;
                } else {
                    result = text + postfix;
                }
            }
        }
        return result;
    }

    /**
     * Converts a fully qualified name to a short-name. Resolves all inner-classes as well and only returns the last
     * class-name-string.
     * <p>
     * Example: <code>com.nts.MyClass$InnerClassInMyClass</code> would resolve to <code>InnerClassInMyClass</code>.
     * @param fullyQualifiedName {@link String} the fully qualified name
     * @param isDeleteGenerics {@link boolean} if set to true, all generic-tag-contents will be removed from each
     *        path-part (Example: {@code java.utils.List<com.nts.Class>} would become {@code List} instead of
     *        {@code List<com.nts.Class>}
     * @return the name that got extracted from the fully qualified name {@link String}
     */
    public static String getTypeFromFqn(final String fullyQualifiedName, final boolean isDeleteGenerics) {
        return getTypeFromFqn(fullyQualifiedName, true, isDeleteGenerics);
    }

    /**
     * Converts a fully qualified name to a short-name.
     * <p>
     * Example (with resolveInnerClasses set to true): <code>com.nts.MyClass$InnerClassInMyClass</code> would resolve to
     * <code>InnerClassInMyClass</code><br>
     * Example (with resolveInnerClasses set to false): <code>com.nts.MyClass$InnerClassInMyClass</code> would resolve
     * to <code>MyClass.InnerClassInMyClass</code>
     * @param fullyQualifiedName {@link String} the fully qualified name
     * @param resolveInnerClasses {@link boolean} if set to false, the inner classes won't be resolved and the return
     *        value will contain them separated by a '.'. If true they are resolved and only the last inner-class-name
     *        is returned
     * @param isDeleteGenerics {@link boolean} if set to true, all generic-tag-contents will be removed from each
     *        path-part (Example: {@code java.utils.List<com.nts.Class>} would become {@code List} instead of
     *        {@code List<com.nts.Class>}
     * @return the name that got extracted from the fully qualified name {@link String}
     */
    public static String getTypeFromFqn(final String fullyQualifiedName, final boolean resolveInnerClasses,
                                        final boolean isDeleteGenerics) {
        if ((fullyQualifiedName == null) || fullyQualifiedName.equals("")) {
            return fullyQualifiedName;
        }

        List<String> path = parseFqn(fullyQualifiedName, isDeleteGenerics);
        if ((path != null) && (path.size() > 0)) {
            return path.get(path.size() - 1);
        }

        return fullyQualifiedName;
    }

    /**
     * Gets the path from a fully qualified name. Resolves all inner classes in the process.
     * <p>
     * Example: <code>com.nts.MyClass.Type</code> would return <code>com.nts.MyClass</code>.
     * @param fullyQualifiedName {@link String} the fully qualified name
     * @param isDeleteGenerics {@link boolean} if set to true, all generic-tag-contents will be removed from each
     *        path-part (Example: {@code java.utils.List<com.nts.Class>} would become
     * @return the path that got extracted from the fully qualified name {@link String} or an empty string if the fully
     *         qualified name was null or empty or contained no path (it contained, for example, a simple type)
     *         {@code [java, utils, List]} instead of {@code [java, utils, List<com.nts.Class>]}
     */
    public static String getPathFromFqn(final String fullyQualifiedName, final boolean isDeleteGenerics) {
        if ((fullyQualifiedName == null) || fullyQualifiedName.equals("")) {
            return "";
        }

        List<String> path = parseFqn(fullyQualifiedName, isDeleteGenerics);
        if (path.size() > 1) {
            StringBuffer result = new StringBuffer("");
            boolean isFirst = true;
            for (int i = 0; i < (path.size() - 1); i++) {
                if (isFirst) {
                    isFirst = false;
                } else {
                    result.append(".");
                }
                result.append(path.get(i));
            }
            return result.toString();
        }

        return "";
    }

    /**
     * Resolves the given fully qualified name by parsing it and then re-assembling it using the dot as delimiter.
     * @param fullyQualifiedName {@link String} the fully qualified name
     * @param isDeleteGenerics {@link boolean} if set to true, all generic-tag-contents will be removed from each
     *        path-part (Example: {@code java.utils.List<com.nts.Class>} would become {@code java.utils.List]}
     * @return the string {@link String} {@code [java, utils, List]} instead of
     */
    public static String resolveFqn(final String fullyQualifiedName, final boolean isDeleteGenerics) {
        if ((fullyQualifiedName == null) || fullyQualifiedName.equals("")) {
            return "";
        }

        List<String> path = parseFqn(fullyQualifiedName, isDeleteGenerics);
        if (path.size() > 0) {
            StringBuffer result = new StringBuffer("");
            boolean isFirst = true;
            for (int i = 0; i < path.size(); i++) {
                if (isFirst) {
                    isFirst = false;
                } else {
                    result.append(".");
                }
                result.append(path.get(i));
            }
            return result.toString();
        }

        return "";
    }

    /**
     * Parses the fully qualified name and returns a list of strings each containing a part of the path (top-level -
     * dot-separated strings).
     * @param fullyQualifiedName {@link String} the fully qualified name
     * @param isDeleteGenerics {@link boolean} if set to true, all generic-tag-contents will be removed from each
     *        path-part (Example: {@code java.utils.List<com.nts.Class>} would become
     * @return the list {@link List<String>} that contains the parts of the path or null, if there was a parsing error
     *         like more generic closing-brackets than opening-brackets or similar things {@code [java, utils, List]}
     *         instead of {@code [java, utils, List<com.nts.Class>]}
     */
    public static List<String> parseFqn(final String fullyQualifiedName, final boolean isDeleteGenerics) {
        List<String> result = new ArrayList<String>();
        char[] source = fullyQualifiedName.toCharArray();

        int pointer = -1;
        int genericBlockCount = 0;
        StringBuffer part = new StringBuffer("");
        while (true) {
            pointer++;
            Character c = charAt(source, pointer);
            if (c == null) {
                result.add(part.toString());
                break;
            }

            switch (c.charValue()) {
                case '<':
                    genericBlockCount++;
                    if (!isDeleteGenerics) {
                        part.append(c);
                    }
                    break;
                case '>':
                    genericBlockCount--;
                    if (!isDeleteGenerics) {
                        part.append(c);
                    }
                    if (genericBlockCount < 0) {
                        return null;
                    }
                    break;
                case '.':
                    if (genericBlockCount == 0) {
                        // We are at the lowest level possible. So this means the end of this part.
                        result.add(part.toString());
                        part = new StringBuffer("");
                    } else if (!isDeleteGenerics) {
                        part.append(c);
                    }
                    break;
                default:
                    if ((genericBlockCount == 0) || !isDeleteGenerics) {
                        part.append(c);
                    }
                    break;
            }
        }
        return (genericBlockCount != 0) ? null : result;
    }

    /**
     * Returns the character of the source-character-array at the given pointer-position.
     * @param source {@link char[]} the source character-array
     * @param pointer {@link int} the position-pointer
     * @return the character {@link Character} or null, if the pointer was out of bounds
     */
    public static Character charAt(final char[] source, final int pointer) {
        if ((pointer < 0) || (source.length <= pointer)) {
            return null;
        }
        return source[pointer];
    }

    /**
     * Gets the stack trace as a formatted String.
     * @param throwable {@link Throwable} the throwable
     * @return the stack trace {@link String}
     */
    public static String getStackTrace(final Throwable throwable) {
        final Writer result = new StringWriter();
        final PrintWriter printWriter = new PrintWriter(result);
        throwable.printStackTrace(printWriter);
        return result.toString();
    }

    /**
     * Prefixes the given sourceString by indentLevel - times - indentCharacters. Returns null if the sourceString is
     * null. Returns the sourceString if the indentLevel smaller or equals zero or the indentCharacters are null or
     * empty.
     * <p>
     * So, for example:
     * 
     * <pre>
     * <code>
     *  indent("test", " ", 3) => "   test";
     *  indent("test", "##", 2) => "####test";
     *  indent(null, ... => null
     *  indent("test", &lt;null or empty&gt;, x) => "test"
     * </code>
     * </pre>
     * @param sourceString {@link String} the source string to indent
     * @param indentCharacters {@link String} the indent characters to indent by
     * @param indentLevel {@link int} the indent level (indentPrefix = indentLevel x indentCharacters)
     * @return the string {@link String} the indented String
     */
    public static String indent(final String sourceString, final String indentCharacters, final int indentLevel) {

        if (sourceString == null) {
            return null;
        }

        if ((indentCharacters == null) || indentCharacters.equals("") || (indentLevel < 1)) {
            return sourceString;
        }

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < indentLevel; i++) {
            sb.append(indentCharacters);
        }
        return sb.toString() + sourceString;
    }

    /**
     * Formats a date to ISO format as stated in the ISO 8601 standard.
     * 
     * <pre>
     * <code>
     * Format:  yyyy-MM-dd
     * Example: 2013-02-16
     * </code>
     * </pre>
     * @param date {@link Date} the given date to format
     * @return the string {@link String} containing the date in ISO 8601 format
     */
    public static String isoDate(final Date date) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        return df.format(date);
    }

    /**
     * Formats a date to ISO format with time as stated in the ISO 8601 standard.
     * 
     * <pre>
     * <code>
     * Format:  yyyy-MM-ddTHH:mm:ss.SSS
     * Example: 2013-02-16T16:59:55.998
     * </code>
     * </pre>
     * @param date {@link Date} the given date to format
     * @return the string {@link String} containing the date in ISO 8601 format
     */
    public static String isoDateTime(final Date date) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
        return df.format(date);
    }

    /**
     * Formats a date to ISO format with time as stated in the ISO 8601 standard with the modification that it doesn't
     * use the double-colon, because that's not an allowed character in DOS-fileNames.
     * 
     * <pre>
     * <code>
     * Format:  yyyy-MM-ddTHH_mm_ss.SSS
     * Example: 2013-02-16T16_59_55.998
     * </code>
     * </pre>
     * @param date {@link Date} the given date to format
     * @return the string {@link String} containing the date in ISO 8601 format
     */
    public static String isoDateTimeForFileName(final Date date) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH_mm_ss.SSS");
        return df.format(date);
    }

    /**
     * Prefixes the given sourceString by indentLevel - times - &lt;aSpaceCharacter&gt;. Returns null if the
     * sourceString is null. Returns the sourceString if the indentLevel smaller or equals zero.
     * <p>
     * So, for example:
     * 
     * <pre>
     * <code>
     *  spaceIndent("test", 3) => "   test";
     *  spaceIndent("test", 1) => " test";
     * </code>
     * </pre>
     * @param sourceString {@link String} the source string to indent
     * @param indentLevel {@link int} the indent level (indentPrefix = indentLevel x " ")
     * @return the string {@link String} the indented String
     */
    public static String spaceIndent(final String sourceString, final int indentLevel) {
        return indent(sourceString, " ", indentLevel);
    }

    /**
     * Makes sure that the given string begins with the given prefix. If it doesn't, the given prefix is prepended,
     * otherwise the original string is returned.
     * @param text {@link String} the text to check for the given prefix
     * @param prefix {@link String} the prefix to check for
     * @return the string {@link String} that is beginning with the given prefix in any case or null, if both input
     *         parameters were null
     */
    public static String startWith(final String text, final String prefix) {

        if ((prefix == null) && (text == null)) {
            return null;
        }

        String result;

        if (prefix == null) {
            result = text;
        } else {
            if (text == null) {
                result = prefix;
            } else {
                if (text.startsWith(prefix)) {
                    result = text;
                } else {
                    result = prefix + text;
                }
            }
        }
        return result;
    }

    /**
     * Returns the given value if it wasn't null and an empty string otherwise.
     * @param value {@link String} the value
     * @return the string {@link String}
     */
    public static String unNull(final String value) {
        return unNull(value, "");
    }

    /**
     * Returns the given value if it wasn't null and a specified string otherwise.
     * @param value {@link String} the value
     * @param valueIfNull {@link String} the value if null
     * @return the string {@link String}
     */
    public static String unNull(final String value, final String valueIfNull) {
        if (value == null) {
            return valueIfNull;
        }

        return value;
    }

    /**
     * Find after a given index.
     * @param data {@link String} the data
     * @param index {@link int} the index
     * @param findValue {@link String} the find value
     * @return the int {@link int} {@link String} the data {@link int} the index {@link int} the length {@link String}
     *         the find value
     */
    public static int findAfter(final String data, final int index, final String findValue) {
        String test = data.substring(index);
        Integer result = test.indexOf(findValue);
        if (result != -1) {
            result = index + result;
        }
        return result;
    }

    /**
     * Find before a given index.
     * @param data {@link String} the data
     * @param index {@link int} the index
     * @param stepLength {@link int} the length
     * @param findValue {@link String} the find value
     * @return the int {@link int}
     */
    public static int findBefore(final String data, final int index, final int stepLength, final String findValue) {
        int result = index;
        int start = index - stepLength;
        int end = index;
        if (index != -1) {
            boolean found = false;
            for (int i = 0; (i < (index / stepLength)) && !found; i++) {
                found = data.substring(start, end).contains(findValue);
                if (found) {
                    result = start + data.substring(start, end).indexOf(findValue);
                }
                start = start - stepLength;
                end = end - stepLength;
            }
        }
        return result;
    }

    /**
     * Contains check if the name contains any of the items in matches.
     * @param matches {@link List<String>} the matches
     * @param name {@link String} the name
     * @return true ({@link boolean}), if successful
     */
    public static synchronized boolean contains(final List<String> matches, final String name) {
        boolean result = false;
        for (int i = 0; (i < matches.size()) && !result; i++) {
            result = name.contains(matches.get(i));
        }
        return result;
    }

    /**
     * Chops Strings to be easy wrapable for wkhtmltopdf. For this, there's an zero-width space (& #8203;) between all
     * originally neighbouring chars.
     * @see <a href=https://code.google.com/p/wkhtmltopdf/issues/detail?id=531>Issue 531</a>
     * @param s the s
     * @return the string
     */
    public static String chopToWrap(final String s) {
        return Strings.chop(s, "&#8203;");
    }

    /**
     * Chops s with seperator.
     * @param s the s
     * @return if s="someString" and seperator="a" returns saoamaeaSataraianaga
     */
    public static String chop(final String s, final String seperator) {
        StringBuilder b = new StringBuilder();
        for (char c : s.toCharArray()) {
            b.append(c);
            b.append(seperator);
        }
        return b.toString();
    }

    public static String objectToAttribute(final Object o) {
        String ret = null;
        if (o instanceof String) {
            ret = "\"" + o + "\"";
        } else if (o instanceof Enum<?>) {
            ret = o.getClass().getSimpleName() + "." + ((Enum<?>) o).name();
        }
        return ret;
    }
}