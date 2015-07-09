package info.unterrainer.java.tools.reporting.consoleprogressbar.drawablecomponents;

import java.io.PrintStream;

import info.unterrainer.java.tools.datastructures.Fader;

public interface DrawableComponent {

	/**
	 * Draws the graphical component of the progress bar.<br/>
	 * Only gets called if the value has changed from the last draw-call.
	 *
	 * @param ps the print-stream to draw to
	 * @param fader the fader that holds all exact values regarding the bar
	 * @param width the width of the bar in characters
	 * @param drawInitialized false, if this is the first call to the draw-method, true otherwise
	 * @param value the number of characters that have the status 'filled'
	 * @param lastValue the number of character drawn by the last call to draw
	 */
	void draw(PrintStream ps, Fader fader, int width, boolean drawInitialized, int value, int lastValue);
}
