package info.unterrainer.java.tools.reporting;
/**************************************************************************
 * <pre>
 * 
 * Copyright (c) Unterrainer Informatik OG.
 * This source is subject to the Microsoft Public License.
 * 
 * See http://www.microsoft.com/opensource/licenses.mspx#Ms-PL.
 * All other rights reserved.
 * 
 * (In other words you may copy, use, change and redistribute it without
 * any restrictions except for not suing me because it broke something.)
 * 
 * THIS CODE AND INFORMATION IS PROVIDED "AS IS" WITHOUT WARRANTY OF ANY
 * KIND, EITHER EXPRESSED OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND/OR FITNESS FOR A PARTICULAR
 * PURPOSE.
 * 
 * </pre>
 ***************************************************************************/

import java.io.PrintStream;
import java.util.Formatter;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class implements a stopWatch.
 * <p>
 * Additionally to the normal stopWatch-functionality it may be used to debug out split-times as well. It
 * measures the split-times and keeps track of the overall times in a variable.<br>
 * Don't be afraid to stop the watch. Stopping doesn't mean you loose any value whatsoever. Think of it as a
 * real-life stopWatch where you may press the start-button at any time after previously pressing the
 * stop-button.
 * <p>
 * This class provides useful overloads that allow writing to a PrintStream in a way that your measurement
 * doesn't get compromised (the stopWatch is paused while writing to the stream). You may initialize it with a
 * PrintStream so that you can use all the overloads that take a string-argument or System.out is used as a
 * default.<br>
 * All the write-operations are performed as a printLine-call, so you don't need to close your assigned text
 * with a newline-character.
 * <p>
 * This class is automatically created using millisecond-precision. If you want to enable
 * nanoseconds-precision albeit performance impacts, though the impact of this is very small indeed, you may
 * do so after creating the stopWatch via the setIsNanoPrecision-Setter.
 * <p>
 * All public methods within this class are synchronized so you may use it concurrently within many threads.<br>
 * It has a property 'isActive' that defaults to true. When this is set to false all calls to this class are
 * aborted within a single if-statement in the called method. This is a convenience function so that you may
 * leave your logging-code in the production code.
 * 
 * @author GEUNT
 * @since 11.07.2013
 */
public class SplitStopWatch {

    /**
     * This format string displays the total time, split-time and your text separated by a pipe.
     */
    public static final String  FORMAT_STRING_TOTAL_SPLIT_OWN_MS = "| total: %10.0fms | since last split: %10.0fms | %s|[%s]";
    public static final String  FORMAT_STRING_TOTAL_SPLIT_OWN_NS = "| total: %15fms | since last split: %15fms | %s|[%s]";

    private static final String COMMAND_START                    = "START";
    private static final String COMMAND_START_NEW                = "STARTNEW";
    private static final String COMMAND_STOP                     = "STOP";
    private static final String COMMAND_SPLIT                    = "SPLIT";
    private static final String COMMAND_RESET                    = "RESET";
    private static final String COMMAND_SPLIT_AND_STOP           = "SPLITANDSTOP";

    private boolean             isActive                         = true;

    private long                startTime;
    private long                time;
    private long                totalTime;
    private boolean             isRunning;

    private boolean             isNanoPrecision                  = false;

    private final PrintStream   printStream;

    private final Logger        logger;
    private final Level         logLevel;

    private String              prefixFormatString               = FORMAT_STRING_TOTAL_SPLIT_OWN_MS;
    private boolean             displayPrefix                    = true;
    private boolean             isFlushImmediately;
    private String              indentString                     = "  ";

    private final float         NANO_CONVERSION_QUOTIENT         = 1000000f;

    /**
     * Initializes a new instance of the SplitStopwatch class. The default PrintStream is System.out.
     */
    public SplitStopWatch() {
        logger = null;
        logLevel = null;
        printStream = System.out;
    }

    /**
     * Instantiates a new split stop watch.
     * 
     * @param logger {@link Logger} the logger to use for output (default log-level is INFO)
     */
    public SplitStopWatch(final Logger logger) {
        this.logger = logger;
        logLevel = Level.INFO;
        printStream = null;
    }

    /**
     * Instantiates a new split stop watch.
     * 
     * @param logger {@link Logger} the logger to use for output (default log-level is INFO)
     * @param level {@link Level} the log-level
     */
    public SplitStopWatch(final Logger logger, final Level level) {
        this.logger = logger;
        logLevel = level;
        printStream = null;
    }

    /**
     * Initializes a new instance of the SplitStopwatch class.
     * 
     * @param printStream The printStream to write to.
     */
    public SplitStopWatch(final PrintStream printStream) {
        logger = null;
        logLevel = null;
        this.printStream = printStream;
    }

    /**
     * Initializes a new instance of the SplitStopwatch class.
     * 
     * @param printStream The printStream to write to.
     * @param isFlushImmediately If set to true the writer is immediately flushed every time a write is done.
     */
    public SplitStopWatch(final PrintStream printStream,
            final boolean isFlushImmediately) {
        logger = null;
        logLevel = null;
        this.printStream = printStream;
        this.isFlushImmediately = isFlushImmediately;
    }

    /**
     * Starts, or resumes measuring elapsed time for an interval. You may start after a stop as well.
     */
    public synchronized void start() {
        if (!isActive) {
            return;
        }
        start("");
    }

    /**
     * Starts, or resumes measuring elapsed time for an interval. You may start after a stop as well.
     * 
     * @param text The text to output on the PrintStream you specified when creating this instance or
     *            System.out as the default.
     */
    public synchronized void start(final String text) {
        if (!isActive) {
            return;
        }
        start(text, 0);
    }

    /**
     * Starts, or resumes measuring elapsed time for an interval. You may start after a stop as well.
     * 
     * @param text The text to output on the PrintStream you specified when creating this instance or
     *            System.out as the default.
     * @param indentLevel The indent level.
     */
    public synchronized void start(final String text, final int indentLevel) {
        if (!isActive) {
            return;
        }
        writeText(text, COMMAND_START, time, indentLevel);
        timerStart();
    }

    /**
     * Initializes a new instance, sets the elapsed time property to zero, and starts measuring elapsed time.
     */
    public synchronized void startNew() {
        if (!isActive) {
            return;
        }
        startNew("");
    }

    /**
     * Initializes a new instance, sets the elapsed time property to zero, and starts measuring elapsed time.
     * 
     * @param text The text to output on the PrintStream you specified when creating this instance or
     *            System.out as the default.
     */
    public synchronized void startNew(final String text) {
        if (!isActive) {
            return;
        }
        startNew(text, 0);
    }

    /**
     * Initializes a new instance, sets the elapsed time property to zero, and starts measuring elapsed time.
     * 
     * @param text The text to output on the PrintStream you specified when creating this instance or
     *            System.out as the default.
     * @param indentLevel The indent level.
     */
    public synchronized void startNew(final String text, final int indentLevel) {
        if (!isActive) {
            return;
        }
        long timeSinceLastSplit = timerElapsedTime();
        writeText(text, COMMAND_START_NEW, timeSinceLastSplit, indentLevel);
        doReset();
        timerStart();
    }

    /**
     * Stops measuring elapsed time for an interval. You may stop and restart afterward resulting in a pause
     * of the timer.
     */
    public synchronized void stop() {
        if (!isActive) {
            return;
        }
        stop("");
    }

    /**
     * Stops measuring elapsed time for an interval. You may stop and restart afterward resulting in a pause
     * of the timer.
     * 
     * @param text The text to output on the PrintStream you specified when creating this instance or
     *            System.out as the default.
     */
    public synchronized void stop(final String text) {
        if (!isActive) {
            return;
        }
        stop(text, 0);
    }

    /**
     * Stops measuring elapsed time for an interval. You may stop and restart afterward resulting in a pause
     * of the timer.
     * 
     * @param text The text to output on the PrintStream you specified when creating this instance or
     *            System.out as the default.
     * @param indentLevel The indent level.
     */
    public synchronized void stop(final String text, final int indentLevel) {
        if (!isActive) {
            return;
        }
        timerStop();
        long timeSinceLastSplit = timerElapsedTime();
        writeText(text, COMMAND_STOP, timeSinceLastSplit, indentLevel);
    }

    /**
     * Stops time interval measurement and resets the time to zero.
     */
    public synchronized void reset() {
        if (!isActive) {
            return;
        }
        reset("");
    }

    /**
     * Stops time interval measurement and resets the time to zero.
     * 
     * @param text The text to output on the PrintStream you specified when creating this instance or
     *            System.out as the default.
     */
    public synchronized void reset(final String text) {
        if (!isActive) {
            return;
        }
        reset(text, 0);
    }

    /**
     * Stops time interval measurement and resets the time to zero.
     * 
     * @param text The text to output on the PrintStream you specified when creating this instance or
     *            System.out as the default.
     * @param indentLevel The indent level.
     */
    public synchronized void reset(final String text, final int indentLevel) {
        if (!isActive) {
            return;
        }
        long timeSinceLastSplit = splitAndStop();
        writeText(text, COMMAND_RESET, timeSinceLastSplit, indentLevel);
        doReset();
    }

    /**
     * Takes a split-time and restarts the timer.
     * 
     * @return The split time in milliseconds.
     */
    public synchronized long split() {
        if (!isActive) {
            return 0;
        }
        return split("");
    }

    /**
     * Takes a split-time and restarts the timer.
     * 
     * @param text The text to output on the PrintStream you specified when creating this instance or
     *            System.out as the default.
     * @return The split time in milliseconds.
     */
    public synchronized long split(final String text) {
        if (!isActive) {
            return 0;
        }
        return split(text, 0);
    }

    /**
     * Takes a split-time and restarts the timer.
     * 
     * @param text The text to output on the PrintStream you specified when creating this instance or
     *            System.out as the default.
     * @param indentLevel The indent level.
     * @return The split time in milliseconds.
     */
    public synchronized long split(final String text, final int indentLevel) {
        if (!isActive) {
            return 0;
        }

        // Split and stop.
        long timeSinceLastSplit = timerElapsedTime();
        timerStop();
        totalTime += timeSinceLastSplit;
        timerReset();

        writeText(text, COMMAND_SPLIT, timeSinceLastSplit, indentLevel);
        timerStart();
        return timeSinceLastSplit;
    }

    /**
     * Takes a split-time and does not restart the timer. You may restart it with start again at any time.
     * 
     * @return The split time in milliseconds.
     */
    public synchronized long splitAndStop() {
        if (!isActive) {
            return 0;
        }
        return splitAndStop("");
    }

    /**
     * Takes a split-time and does not restart the timer. You may restart it with start again at any time.
     * 
     * @param text The text to output on the PrintStream you specified when creating this instance or
     *            System.out as the default.
     * @return The split time in milliseconds.
     */
    public synchronized long splitAndStop(final String text) {
        if (!isActive) {
            return 0;
        }
        return splitAndStop(text, 0);
    }

    /**
     * Takes a split-time and does not restart the timer. You may restart it with start again at any time.
     * 
     * @param text The text to output on the PrintStream you specified when creating this instance or
     *            System.out as the default.
     * @param indentLevel The indent level.
     * @return The split time in milliseconds.
     */
    public synchronized long splitAndStop(final String text,
            final int indentLevel) {
        if (!isActive) {
            return 0;
        }
        long timeSinceLastSplit = timerElapsedTime();
        timerStop();
        totalTime += timeSinceLastSplit;
        timerReset();
        writeText(text, COMMAND_SPLIT_AND_STOP, timeSinceLastSplit, indentLevel);
        return timeSinceLastSplit;
    }

    private void doReset() {
        timerReset();
        totalTime = 0;
    }

    /**
     * Gets a flag indicating if the internal watch is running at the moment.
     * 
     * @return true if the watch is running at the moment, false otherwise.
     */
    public synchronized boolean isRunning() {
        return startTime == 0;
    }

    /**
     * Gets the time that has passed since the start of the watch or the last split-time, if one has been
     * taken, in milliseconds.
     * 
     * @return The time in milliseconds.
     */
    public synchronized long getElapsedSinceLastSplitMilliseconds() {
        return timerElapsedTime();
    }

    /**
     * Gets the time that has passed since the start of the watch.
     * 
     * @return The time in milliseconds.
     */
    public synchronized long getTotalTime() {
        return totalTime + timerElapsedTime();
    }

    /**
     * The prefix is written every time an output is made (if this default behavior isn't turned off using
     * setDisplayPrefix()).
     * 
     * @return The format string for a Formatter.
     */
    public synchronized String getPrefixFormatString() {
        return prefixFormatString;
    }

    /**
     * Sets the prefix format string.
     * 
     * @param prefixFormatString the new prefix format string
     */
    public synchronized void setPrefixFormatString(
            final String prefixFormatString) {
        this.prefixFormatString = prefixFormatString;
    }

    /**
     * A flag indicating whether a given prefix (the default one or set one) is printed in front of the actual
     * time or not.
     * 
     * @return true if the prefix is written, false otherwise.
     */
    public synchronized boolean isDisplayPrefix() {
        return displayPrefix;
    }

    /**
     * Sets the display prefix.
     * 
     * @param displayPrefix the new display prefix
     */
    public synchronized void setDisplayPrefix(final boolean displayPrefix) {
        this.displayPrefix = displayPrefix;
    }

    /**
     * A flag indicating if the stream is flushed immediately after each write.
     * 
     * @return true if the stream is flushed immediately after each write, false otherwise.
     */
    public synchronized boolean isFlushImmediately() {
        return isFlushImmediately;
    }

    /**
     * Sets the flush immediately.
     * 
     * @param isFlushImmediately the new flush immediately
     */
    public synchronized void setFlushImmediately(
            final boolean isFlushImmediately) {
        this.isFlushImmediately = isFlushImmediately;
    }

    /**
     * The string which is used when outputting one indent. The default value are three spaces per indent, so
     * this string consists of three spaces per default.
     * 
     * @return The indentString currently used.
     */
    public synchronized String getIndentString() {
        return indentString;
    }

    /**
     * Sets the indent string.
     * 
     * @param indentString the new indent string
     */
    public synchronized void setIndentString(final String indentString) {
        this.indentString = indentString;
    }

    /**
     * A flag indicating the precision of the means of measurement used. This may have a small performance
     * impact.
     * 
     * @return true if nanosecond-precision is used, false if millisecond-precision is used.
     */
    public synchronized boolean isNanoPrecision() {
        return isNanoPrecision;
    }

    /**
     * Sets the nano precision.
     * 
     * @param isNanoPrecision the new nano precision
     */
    public synchronized void setNanoPrecision(final boolean isNanoPrecision) {
        this.isNanoPrecision = isNanoPrecision;
    }

    /**
     * When this is set to false all calls to this class are aborted within a single if-statement in the
     * called method. This is a convenience function so that you may leave your logging-code in the production
     * code.
     * 
     * @return true, or false.
     */
    public boolean isActive() {
        return isActive;
    }

    /**
     * Sets the active.
     * 
     * @param isActive the new active
     */
    public void setActive(final boolean isActive) {
        this.isActive = isActive;
    }

    /**
     * Gets the actual Time.
     * 
     * @param isNanoPrecision True, if nanosecond-precision is used, false otherwise.
     * @return The actual time.
     */
    private long getActualTime(final boolean isNanoPrecision) {
        if (isNanoPrecision) {
            return System.nanoTime();
        }
        return System.currentTimeMillis();
    }

    /**
     * Resets the timer and all according values.
     */
    private void timerReset() {
        startTime = 0;
        time = 0;
        isRunning = false;
    }

    /**
     * Starts the timer.
     */
    private void timerStart() {
        if (!isRunning) {
            startTime = getActualTime(isNanoPrecision);
            isRunning = true;
        }
    }

    /**
     * Stops the timer and doesn't reset the according values.
     */
    private void timerStop() {
        if (isRunning) {
            time += getActualTime(isNanoPrecision) - startTime;
            startTime = 0;
            isRunning = false;
        }
    }

    /**
     * Gets the elapsed time since the last start of the timer.
     * 
     * @return The time since the last start of the timer.
     */
    private long timerElapsedTime() {
        if (isRunning) {
            long now = getActualTime(isNanoPrecision);
            return time + now - startTime;
        }
        return time;
    }

    /**
     * Writes the given text to the underlying PrintStream.
     * 
     * @param text The text to be written.
     * @param command The name of the command that triggered the output.
     * @param elapsedTimeSinceLastSplit The time elapsed since the last split or start.
     * @param indentLevel The level to indent to.
     */
    private void writeText(final String text, final String command,
            final long elapsedTimeSinceLastSplit, final int indentLevel) {
        String indent = "";
        if (indentLevel > 0) {
            for (int i = 0; i < indentLevel; i++) {
                indent += indentString;
            }
        }

        if (displayPrefix) {
            Formatter formatter = new Formatter();
            float total = getTotalTime();
            float elapsed = elapsedTimeSinceLastSplit;
            if (isNanoPrecision) {
                total /= NANO_CONVERSION_QUOTIENT;
                elapsed /= NANO_CONVERSION_QUOTIENT;
            }
            String fs = prefixFormatString;
            if (isNanoPrecision && fs.equals(FORMAT_STRING_TOTAL_SPLIT_OWN_MS)) {
                fs = FORMAT_STRING_TOTAL_SPLIT_OWN_NS;
            }
            formatter.format(fs, total, elapsed, text, command);
            String output = formatter.out().toString();
            println(indent + output);
            formatter.close();
        } else {
            println(indent + text);
        }
        if (isFlushImmediately) {
            flush();
        }
    }

    private void println(final String text) {
        if (printStream != null) {
            printStream.println(text);
            return;
        }

        if (logger != null) {
            logger.log(logLevel, text);
            return;
        }
    }

    private void flush() {
        if (printStream != null) {
            printStream.flush();
        }
    }
}