package info.unterrainer.java.tools.reporting.consoleprogressbar.drawablecomponents;

import info.unterrainer.java.tools.datastructures.Fader;

import java.io.PrintStream;

public interface DrawableComponent {

	/**
	 * Draws the graphical component of the progress bar.<br/>
	 * No need to flush the stream after writing. This is done after calling this method.
	 *
	 * @param ps the print-stream to draw to
	 * @param fader the fader that holds all exact values regarding the bar
	 * @param width the width of the bar in characters
	 * @param drawInitialized false, if this is the first call to the draw-method, true otherwise
	 * @param value the number of characters that have the status 'filled'
	 * @param lastValue the number of character drawn by the last call to draw
	 */
	void draw(PrintStream ps, Fader fader, int width, boolean drawInitialized, int value, int lastValue);

	/**
	 * Removes the graphical component of the progress bar from the output stream.<br/>
	 * No need to flush the stream after writing. This is done after calling this method.
	 *
	 * @param ps the print-stream to draw to
	 * @param width the width of the bar in characters
	 * @param lastValue the number of character drawn by the last call to draw
	 */
	void remove(PrintStream ps, int width, int lastValue);
}
