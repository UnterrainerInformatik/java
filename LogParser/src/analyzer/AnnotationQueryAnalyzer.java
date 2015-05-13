package analyzer;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.jxpath.ClassFunctions;
import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.JXPathFunctionNotFoundException;
import org.apache.commons.jxpath.JXPathInvalidSyntaxException;

import utils.Files;
import utils.Strings;
import utils.file.encoding.Encoding;
import utils.serialization.Serializations;

import com.google.common.collect.Sets;
import com.nts.automation.framework.tatools.sourcecode.analysis.SourceAnalyzer;
import com.nts.automation.framework.tatools.sourcecode.analysis.datamodel.Data;
import com.nts.automation.framework.utils.datatype.string.Converter;
import com.sun.istack.internal.logging.Logger;


/**
 * The Class AnnotationQueryAnalyzer.
 * <p>
 * This program analyzes a given list of java-source-files and extracts all annotations within them and their default-
 * and actually set values. It then builds an object-data-structure for you to query using XPath 1.0.
 * @author GEUNT
 */
public final class AnnotationQueryAnalyzer {

	private static final Logger logger = Logger.getLogger(AnnotationQueryAnalyzer.class);
	
    /**
     * Instantiates a new annotation query analyzer.
     */
    private AnnotationQueryAnalyzer() {

    }

    /**
     * The main method.
     * <p>
     * Arguments:
     * <ol>
     * <li>The name of the input-file containing a list of files and directories to import and the xPath-statement to
     * process.
     * </ol>
     * @param args the arguments
     */
    public static void main(final String[] args) {
        boolean isVerbose = true;

        if (args.length < 1) {
            fail();
        }

        Mode mode = Mode.NONE;
        OptionMode optionMode = OptionMode.NONE;
        String modeData1 = "";
        String modeData2 = "";
        Operator operator = Operator.NONE;
        String inputFileName = "";

        for (String arg : args) {
            String a = arg.trim();

            if ((optionMode != OptionMode.NONE) && isOption(a)) {
                fail();
            }

            // React on modes that require additional parameters.
            switch (optionMode) {
                case OPTION_ANALYZE_SAVE_DATA:
                    modeData1 = Strings.stripQuotes(a);
                    mode = Mode.ANALYZE_SAVE_DATA;
                    optionMode = OptionMode.NONE;
                    continue;
                case OPTION_OUTPUT_META_DATA:
                    modeData1 = Strings.stripQuotes(a);
                    mode = Mode.OUTPUT_META_DATA;
                    optionMode = OptionMode.NONE;
                    continue;
                case OPTION_ANALYZE_SAVE_DATA_NAME_BY_DATE:
                    modeData1 = Strings.stripQuotes(a);
                    modeData1 = modeData1.replace("?", Strings.isoDate(new Date()));
                    mode = Mode.ANALYZE_SAVE_DATA;
                    optionMode = OptionMode.NONE;
                    continue;
                case OPTION_ANALYZE_SAVE_DATA_NAME_BY_DATETIME:
                    modeData1 = Strings.stripQuotes(a);
                    modeData1 = modeData1.replace("?", Strings.isoDateTimeForFileName(new Date()));
                    mode = Mode.ANALYZE_SAVE_DATA;
                    optionMode = OptionMode.NONE;
                    continue;
                case OPTION_EVALUATE_SAVED_DATA:
                    modeData1 = Strings.stripQuotes(a);
                    mode = Mode.EVALUATE_SAVED_DATA;
                    optionMode = OptionMode.NONE;
                    continue;
                case OPTION_GENERATE_DUMMY:
                    modeData1 = Strings.stripQuotes(a);
                    mode = Mode.GENERATE_DUMMY;
                    optionMode = OptionMode.NONE;
                    continue;
                case OPTION_CALCULATE1:
                    modeData1 = Strings.stripQuotes(a);
                    // Advance the mode to the next one for this chain of input
                    // parameters.
                    optionMode = OptionMode.OPTION_CALCULATE2;
                    continue;
                case OPTION_CALCULATE2:
                    try {
                        operator = Operator.valueOf(Strings.stripQuotes(a).toUpperCase());
                    } catch (IllegalArgumentException e) {
                        fail();
                    }
                    // Advance the mode to the next one for this chain of input
                    // parameters.
                    optionMode = OptionMode.OPTION_CALCULATE3;
                    continue;
                case OPTION_CALCULATE3:
                    modeData2 = Strings.stripQuotes(a);
                    mode = Mode.CALCULATE;
                    optionMode = OptionMode.NONE;
                    continue;
                default:
                    break;
            }

            if (isOption(a)) {
                // Set to the right mode and cut the character.
                a = a.substring(1, a.length());
                optionMode = resolveOption(a);
            } else {
                if (optionMode == OptionMode.NONE) {
                    // This isn't an option tag and we are not in a state to
                    // receive additional option-data.
                    inputFileName = Strings.stripQuotes(a);
                }
            }

            // React on modes that don't require additional parameters.
            switch (optionMode) {
                case OPTION_HELP:
                    printUsage();
                    System.exit(0);
                    break;
                case OPTION_SILENT:
                    isVerbose = false;
                    optionMode = OptionMode.NONE;
                    break;
                default:
                    break;
            }
        }

        if (optionMode != OptionMode.NONE) {
            // The option's syntax was incorrect.
            fail();
        }

        InputFile inFile;
        Data data1;
        Data data2;
        List<String> e1;
        List<String> e2;
        switch (mode) {
            case ANALYZE_SAVE_DATA:
                analyzeAndSaveData(inputFileName, modeData1, isVerbose);
                break;
            case OUTPUT_META_DATA:
                data1 = Serializations.objectDeserialize(new File(modeData1), Data.class);
                outputMetaData(data1, isVerbose);
                break;
            case EVALUATE_SAVED_DATA:
                inFile = openInputFile(inputFileName, isVerbose);
                File f = new File(modeData1);
                if (!f.exists()) {
                    out("Error opening analysis-result-file '" + modeData1 + "'. File not found.", isVerbose);
                    System.exit(1);
                }
                data1 = Serializations.objectDeserialize(new File(modeData1), Data.class);
                evaluatePrint(data1, inFile.getXPath(), isVerbose);
                break;
            case CALCULATE:
                inFile = openInputFile(inputFileName, isVerbose);
                data1 = Serializations.objectDeserialize(new File(modeData1), Data.class);
                data2 = Serializations.objectDeserialize(new File(modeData2), Data.class);
                e1 = evaluate(data1, inFile.getXPath(), isVerbose);
                e2 = evaluate(data2, inFile.getXPath(), isVerbose);
                calculate(e1, operator, e2, isVerbose);
                break;
            case GENERATE_DUMMY:
                generateDummyInputFile(modeData1, isVerbose);
                break;
            case NONE:
                // No additional option has been specified, so load, analyze and
                // evaluate.
                inFile = openInputFile(inputFileName, isVerbose);
                data1 = generateData(inFile, isVerbose);
                evaluatePrint(data1, inFile.getXPath(), isVerbose);
            default:
                break;
        }

        System.exit(0);
    }

    /**
     * Calculate.
     * @param result1 {@link List<String>} the result1
     * @param operator {@link Operator} the operator
     * @param result2 {@link List<String>} the result2
     * @param isVerbose {@link boolean} the is verbose {@link List<String>} the result1 {@link Operator} the operator
     *        {@link List<String>} the result2 {@link boolean} the is verbose
     */
    private static void calculate(final List<String> result1, final Operator operator, final List<String> result2,
                                  final boolean isVerbose) {
        if ((result1.size() == 1) && (result2.size() == 1)) {
            // Maybe they both are scalar results.
            try {
                double d1 = Double.parseDouble(result1.get(0));
                double d2 = Double.parseDouble(result2.get(0));

                if ((operator == Operator.INTERSECTION) || (operator == Operator.XOR)) {
                    System.out
                            .println("Error: " + operator + "-operator not allowed when arguments are scalar values.");
                    fail();
                }

                // We have two scalar result-sets to calculate.
                if (calculateScalar(d1, operator, d2)) {
                    return;
                }
                System.out.println("Error: Illegal operator [" + operator + "] with scalar arguments.");
                fail();

            } catch (NumberFormatException e) {
                logger.info("They just are not scalar results. So continue.");
            }
        }

        // When we are here, we have a set-calculation.
        Set<String> s1 = new HashSet<String>(result1);
        Set<String> s2 = new HashSet<String>(result2);
        if (calculateSet(s1, operator, s2)) {
            return;
        }
        System.out.println("Error: Illegal operator [" + operator + "] with table-arguments.");
        fail();
    }

    /**
     * Calculate scalar.
     * @param d1 {@link double} the d1
     * @param operator {@link Operator} the operator
     * @param d2 {@link double} the d2
     * @return true ({@link boolean}), if successful {@link double} the d1 {@link Operator} the operator {@link double}
     *         the d2
     */
    private static boolean calculateScalar(final double d1, final Operator operator, final double d2) {
        switch (operator) {
            case LEFT:
                System.out.println(d1 - d2);
                return true;
            case RIGHT:
                System.out.println(d2 - d1);
                return true;
            case UNION:
                System.out.println(d1 + d2);
                return true;
            default:
                break;
        }
        return false;
    }

    /**
     * Calculate set.
     * @param s1 {@link Set<String>} the s1
     * @param operator {@link Operator} the operator
     * @param s2 {@link Set<String>} the s2
     * @return true ({@link boolean}), if successful {@link Set<String>} the s1 {@link Operator} the operator
     *         {@link Set<String>} the s2
     */
    private static boolean calculateSet(final Set<String> s1, final Operator operator, final Set<String> s2) {
        switch (operator) {
            case INTERSECTION:
                Set<String> intersection = Sets.intersection(s1, s2);
                for (String s : intersection) {
                    System.out.println(s);
                }
                return true;
            case LEFT:
                Set<String> left = Sets.difference(s1, s2);
                for (String s : left) {
                    System.out.println(s);
                }
                return true;
            case RIGHT:
                Set<String> right = Sets.difference(s2, s1);
                for (String s : right) {
                    System.out.println(s);
                }
                return true;
            case UNION:
                Set<String> union = Sets.union(s1, s2);
                for (String s : union) {
                    System.out.println(s);
                }
                return true;
            case XOR:
                Set<String> xor = Sets.symmetricDifference(s1, s2);
                for (String s : xor) {
                    System.out.println(s);
                }
                return true;
            default:
                break;
        }
        return false;
    }

    /**
     * Open input file.
     * @param inputFileName {@link String} the input file name
     * @param isVerbose {@link boolean} the is verbose
     * @return the input file {@link InputFile} {@link String} the input file name {@link boolean} the is verbose
     */
    private static InputFile openInputFile(final String inputFileName, final boolean isVerbose) {
        File f = new File(inputFileName);
        if (!f.exists()) {
            out("Error opening input-file '" + inputFileName + "'. File not found.", isVerbose);
            System.exit(1);
        }
        out("opening input-file: " + inputFileName, isVerbose);
        return Serializations.jaxBXmlDeserializer(f, InputFile.class);
    }

    /**
     * Resolve option.
     * @param option {@link String} the option
     * @return the option mode {@link OptionMode} {@link String} the option
     */
    private static OptionMode resolveOption(final String option) {
        if (option.equals("h") || option.equals("?")) {
            return OptionMode.OPTION_HELP;
        } else if (option.equals("s")) {
            return OptionMode.OPTION_SILENT;
        } else if (option.equals("d")) {
            return OptionMode.OPTION_ANALYZE_SAVE_DATA;
        } else if (option.equals("D")) {
            return OptionMode.OPTION_ANALYZE_SAVE_DATA_NAME_BY_DATE;
        } else if (option.equals("DT")) {
            return OptionMode.OPTION_ANALYZE_SAVE_DATA_NAME_BY_DATETIME;
        } else if (option.equals("g")) {
            return OptionMode.OPTION_GENERATE_DUMMY;
        } else if (option.equals("e")) {
            return OptionMode.OPTION_EVALUATE_SAVED_DATA;
        } else if (option.equals("m")) {
            return OptionMode.OPTION_OUTPUT_META_DATA;
        } else if (option.equals("c")) {
            return OptionMode.OPTION_CALCULATE1;
        }

        System.out.println("The option '" + option + "' you specified wasn't recognized.");
        fail();

        return OptionMode.NONE;
    }

    /**
     * Out.
     * @param message {@link String} the message
     * @param isVerbose {@link boolean} the is verbose {@link String} the message {@link boolean} the is verbose
     */
    private static void out(final String message, final boolean isVerbose) {
        if (isVerbose) {
            System.out.println(message);
        }
    }

    /**
     * Checks if is option.
     * @param str {@link String} the str
     * @return true {@link Boolean} , if is option {@link String} the str
     */
    private static boolean isOption(final String str) {
        return str.startsWith("/") || str.startsWith("-");
    }

    /**
     * Fail.
     */
    private static void fail() {
        printUsage();
        System.exit(1);
    }

    /**
     * Generate data.
     * @param inFile {@link InputFile} the in file
     * @param isVerbose {@link boolean} the is verbose
     * @return the data {@link Data} {@link InputFile} the in file {@link boolean} the is verbose
     */
    private static Data generateData(final InputFile inFile, final boolean isVerbose) {
        SourceAnalyzer a = new SourceAnalyzer();

        out("importing source-files...", isVerbose);
        try {
            for (String fn : inFile.getSourceFiles()) {
                File f = new File(fn);
                if (f.exists() && f.isFile()) {
                    a.addFile(fn);
                }
            }
        } catch (FileNotFoundException e) {
            out(Strings.getStackTrace(e), isVerbose);
        }

        out("importing flat source-directories...", isVerbose);
        for (String fn : inFile.getSourceDirsFlat()) {
            File f = new File(fn);
            if (f.exists() && f.isDirectory()) {
                a.addSingleDirectory(fn, Encoding.UTF8);
            }
        }

        out("importing recursive source-directories...", isVerbose);
        for (String fn : inFile.getSourceDirsRecursive()) {
            File f = new File(fn);
            if (f.exists() && f.isDirectory()) {
                a.addDirectoryTree(fn, Encoding.UTF8);
            }
        }

        return a.resolveAnnotations();
    }

    /**
     * Generate dummy input file.
     * @param outputFilePath {@link String} the output file path
     * @param isVerbose {@link boolean} the is verbose {@link String} the output file path {@link boolean} the is
     *        verbose
     */
    private static void generateDummyInputFile(final String outputFilePath, final boolean isVerbose) {
        InputFile inputFile = new InputFile();

        List<String> lines = new ArrayList<String>();
        lines.add("filePathAndName1");
        lines.add("filePathAndName2");
        lines.add("...");
        inputFile.setSourceFiles(lines);

        lines = new ArrayList<String>();
        lines.add("flat-import-directory1");
        lines.add("flat-import-directory2");
        lines.add("...");
        inputFile.setSourceDirsFlat(lines);

        lines = new ArrayList<String>();
        lines.add("recursive-import-directory1");
        lines.add("recursive-import-directory2");
        lines.add("...");
        inputFile.setSourceDirsRecursive(lines);

        // The < and > character in the next string forces the xmlJaxbSerializer
        // to user a CData-element (when
        // set to true in the call next to that one).
        inputFile.setXPath("\n\t\t/xPathString[<your_query>]\n\t");
        Serializations.jaxBXmlTransformerSerialize(inputFile, new File(outputFilePath), "XPath");

        out("Example input-file saved.", isVerbose);
    }

    /**
     * Analyze and save data.
     * @param inputFileName {@link String} the input file name
     * @param outputFilePath {@link String} the output file path
     * @param isVerbose {@link boolean} the is verbose {@link String} the input file name {@link String} the output file
     *        path {@link boolean} the is verbose
     */
    private static void analyzeAndSaveData(final String inputFileName, final String outputFilePath,
                                           final boolean isVerbose) {
        InputFile inFile = openInputFile(inputFileName, isVerbose);
        Data data = generateData(inFile, isVerbose);
        data.getMetaData().put("createdOn", Strings.isoDateTime(new Date()));
        data.getMetaData().put("inputFile", Files.readFileToString(new File(inputFileName), Encoding.UTF8));
        Serializations.objectSerialize(data, new File(outputFilePath));
        out("Analysis data saved.", isVerbose);
    }

    /**
     * Output meta data.
     * @param data {@link Data} the data
     * @param isVerbose {@link boolean} the is verbose {@link Data} the data {@link boolean} the is verbose
     */
    private static void outputMetaData(final Data data, final boolean isVerbose) {
        out("printing meta-data map:", isVerbose);
        for (Map.Entry<String, String> e : data.getMetaData().entrySet()) {
            System.out.println(e.getKey() + ": '" + e.getValue() + "'");
        }
    }

    /**
     * Evaluate print.
     * @param data {@link Data} the data
     * @param xPath {@link String} the x path
     * @param isVerbose {@link boolean} the is verbose {@link Data} the data {@link String} the x path {@link boolean}
     *        the is verbose
     */
    private static void evaluatePrint(final Data data, final String xPath, final boolean isVerbose) {
        List<String> result = evaluate(data, xPath, isVerbose);
        out("RESULT IS: ", isVerbose);
        for (String s : result) {
            System.out.println(s);
        }
    }

    /**
     * Evaluate.
     * @param data {@link Data} the data
     * @param xPath {@link String} the x path
     * @param isVerbose {@link boolean} the is verbose
     * @return the list {@link List<String>} {@link Data} the data {@link String} the x path {@link boolean} the is
     *         verbose
     */
    private static List<String> evaluate(final Data data, final String xPath, final boolean isVerbose) {

        out("XPATH TO EVALUATE:\n" + xPath, isVerbose);

        JXPathContext c = JXPathContext.newContext(data);
        c.setFunctions(new ClassFunctions(Converter.class, "f"));
        // Return null instead of throwing an exception if nothing is found.
        c.setLenient(true);

        List<String> result = new ArrayList<String>();
        Iterator<?> iter = c.iterate(xPath);
        try {
            while (iter.hasNext()) {
                Object d = iter.next();
                result.add(d.toString());
            }
        } catch (NoClassDefFoundError e) {
            logger.severe("Invalid XPath expression. A function that was called "
                    + "could not be found within the imported classes.\n" + Strings.getStackTrace(e));
        } catch (JXPathFunctionNotFoundException e) {
            logger.severe("Invalid XPath expression. Function was not found.\n" + Strings.getStackTrace(e));
        } catch (JXPathInvalidSyntaxException e) {
            logger.severe("Invalid XPath expression. Invalid syntax.\n" + Strings.getStackTrace(e));
        }
        return result;
    }

    /**
     * Prints the usage.
     */
    private static void printUsage() {
        final int leftSpace = 61;
        final int maxLineLength = 76;
        String nl = "\n                  ";

        String s = "Using this tool you may analyze the annotations within a number of java-files. "
                + "It analyzes these java-files you may specify within a XML-file (you may specify "
                + "any number of single files, directories that will be read flat or directories "
                + "that will be read recursively) and you may evaluate the analysis-result using "
                + "a XPath 1.0 expression (defined in this XML-file as well). You may display the "
                + "result of the evaluation or just save the dump of the analysis without "
                + "evaluation, evaluate the XPath on a previously generated analysis-dump, and " + "so on...\n\n"
                + "USAGE:\n" + "  AnnotationQueryAnalyzer <input-filename> [options]\n" + "or\n"
                + "  AnnotationQueryAnalyzer [options] <input-filename>\n" + "   "
                + Strings.wrap("where input-filename is mandatory and should be the XML-file"
                        + " containing the files to import and the XPath to evaluate.\n", maxLineLength, true, true,
                        false, true, "\n   ")
                + "Its structure should be as follows:\n"
                + "\n"
                + "  <?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
                + "  <inputFile>\n"
                + "    <sourceDirsFlat>flat-import-directory1</sourceDirsFlat>\n"
                + "    <sourceDirsFlat>...</sourceDirsFlat>\n"
                + "    <sourceDirsRecursive>recursive-import-directory1</sourceDirsRecursive>\n"
                + "    <sourceDirsRecursive>...</sourceDirsRecursive>\n"
                + "    <sourceFiles>filePathAndName1</sourceFiles>\n"
                + "    <sourceFiles>...</sourceFiles>\n"
                + "    <XPath><![CDATA[\n"
                + "        /xPathString[<your_query>]\n"
                + "    ]]></XPath>\n"
                + "  </inputFile>\n"
                + "\n"
                + "OPTIONS:\n"
                + "  <none>          "
                + Strings.wrap("analyze the source-files given within the input-file and evaluate "
                        + "the XPath given in the input-file on the base of the data just "
                        + "analyzed. Doesn't save an analysis-result.\n", leftSpace, true, true, false, true, nl)
                + "  -h or -?        show this message.\n"
                + "  -s              "
                + Strings.wrap("silent-mode. Just display the appropriate results without any "
                        + "additional text. Useful when piping to a file.\n", leftSpace, true, true, false, true, nl)
                + "  -d file         "
                + Strings.wrap("analyze the source-files given given in within the input-file and "
                        + "dump a snapshot of the analysis-result. Does not evaluate the " + "given XPath string.\n",
                        leftSpace, true, true, false, true, nl)
                + "  -D dir\\?.<ext>  "
                + Strings.wrap("same as -d but generates the output-file-name by itself. It will "
                        + "have the format \"yyyy-MM-dd\", will replace the question mark in "
                        + "your parameter (\"D:\\temp\\?.dat\" would become \"D:\\temp\\2012"
                        + "-05-21.dat\") and it will overwrite existing files of the same name.\n", leftSpace, true,
                        true, false, true, nl)
                + "  -DT dir\\?.<ext> "
                + Strings.wrap("same as -d but generates the output-file-name by itself. It will have "
                        + "the format \"yyyy-MM-dd'T'HH_mm_ss.SSS\", will replace the question "
                        + "mark in your parameter (\"D:\\temp\\?.dat\" would become \"D:\\temp\\"
                        + "2012-05-21'T'14_48_23.938.dat\") and will overwrite existing files of " + "the same name.",
                        leftSpace, true, true, false, true, nl)
                + "  -e file         "
                + Strings.wrap("evaluate analysis-result-file. Evaluates the XPath in the input-file "
                        + "using the data from the given analysis-result-file instead of "
                        + "generating such a result off the given files within the input-file " + "first.\n",
                        leftSpace, true, true, false, true, nl)
                + "  -c file         "
                + Strings.wrap("calculate a simple set-operation using two dumps, you previously saved. "
                        + "The XPath in inFile is applied to both of them and the result is compared. "
                        + "Use it like this: -c <dumpA> <operation> <dumpB>. The operation is "
                        + "one of the following: [INTERSECTION|UNION|LEFT|RIGHT|XOR] where INTERSECTION "
                        + "are all elements of the first list that appear in the second one as well, "
                        + "UNION are all of the first and all of the second list without double "
                        + "entries, LEFT are all of the first dump without elements appearing in "
                        + "the intersection and RIGHT are all of the second dump without elements "
                        + "appearing in the intersection. XOR are all the values without the set "
                        + "of the intersection. When using a XPath that calculates a number instead "
                        + "of a resultSet, UNION equals A+B, LEFT equals A-B and RIGHT equals B-A. "
                        + "Intersection is invalid in that case.\n", leftSpace, true, true, false, true, nl)
                + "  -m  file        "
                + Strings.wrap("takes your previously generated analysis-result-file (generated using "
                        + "the -d option) and loads it and its meta-data. Then displays the "
                        + "contained meta-data on the console.\n", leftSpace, true, true, false, true, nl)
                + "\n"
                + "  -g file         generate an example-input-file and save it.\n"
                + "\n"
                + "EXAMPLES:\n"
                + "  AnnotationQueryAnalyzer infile.xml\n"
                + "  AnnotationQueryAnalyzer infile.xml -d dump.dat -s\n"
                + "  AnnotationQueryAnalyzer infile.xml -s -DT C:\\temp\\save\\?.img\n"
                + "  AnnotationQueryAnalyzer infile.xml -D C:\\temp\\?.dat\n"
                + "  AnnotationQueryAnalyzer infile.xml -c dump1.dat UNION dump2.dat -s\n"
                + "  AnnotationQueryAnalyzer infile.xml -c dump1.dat LEFT dump2.dat -s\n"
                + "  AnnotationQueryAnalyzer -m C:\\temp\\2013-20-02.dat\n"
                + "  AnnotationQueryAnalyzer -s infile.xml -e dump.dat\n"
                + "  AnnotationQueryAnalyzer -g example.xml\n"
                + "\n"
                + "INFO:\n"
                + "  The order of input-filename or option values doesn't matter.\n"
                + "  All write-operations of this command overwrite existing files without prompt.\n";

        System.out.println(s);
    }
}