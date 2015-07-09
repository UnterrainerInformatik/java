package info.unterrainer.java.tools.reporting.consoleprogressbar;

import java.io.PrintStream;

import info.unterrainer.java.tools.datastructures.Fader;
import info.unterrainer.java.tools.reporting.consoleprogressbar.drawablecomponents.DrawableComponent;
import info.unterrainer.java.tools.reporting.consoleprogressbar.drawablecomponents.ProgressBar;
import info.unterrainer.java.tools.reporting.consoleprogressbar.drawablecomponents.SimpleInsertBar;
import info.unterrainer.java.tools.utils.NullUtils;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * This class enables your console-applications to draw a progress-bar.
 * <p>
 * You may specify if your console supports control-characters (like standard-out) or not (like the Eclipse console-implementation (before Mars (4.5)) or a pipe
 * to a file) if you'd like to use one of the two standard {@link DrawableComponent} implementations.<br/>
 * You also may implement your own {@link DrawableComponent} and use that in your applications.
 * <p>
 * Default values are:
 * <table>
 * <tr>
 * <td><b>width</b></td>
 * <td>50</td>
 * </tr>
 * <tr>
 * <td><b>minValue</b></td>
 * <td>0.0d</td>
 * </tr>
 * <tr>
 * <td><b>maxValue</b></td>
 * <td>1.0d</td>
 * </tr>
 * <tr>
 * <td><b>controlCharacterSupport</b></td>
 * <td>true</td>
 * </tr>
 * </table>
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ConsoleProgressBar {

	@Getter
	private Fader fader;
	private Double minValue;
	private Double maxValue;

	@Getter
	@Setter
	private boolean controlCharacterSupport;

	@Getter
	@Setter
	private Integer width;

	private boolean drawInitialized = false;
	private int lastNumberOfCharactersDrawn;
	private DrawableComponent component;

	@Builder
	public ConsoleProgressBar(Integer width, Double minValue, Double maxValue, Boolean controlCharacterSupport, DrawableComponent component) {

		this.minValue = NullUtils.defaultIfNull(minValue, 0.0d);
		this.maxValue = NullUtils.defaultIfNull(maxValue, 1.0d);
		this.width = NullUtils.defaultIfNull(width, 50);
		this.controlCharacterSupport = NullUtils.defaultIfNull(controlCharacterSupport, true);

		if (component == null) {
			if (controlCharacterSupport) {
				this.component = ProgressBar.builder().build();
			} else {
				this.component = SimpleInsertBar.builder().build();
			}
		} else {
			this.component = component;
		}
	}

	private void checkFader() {
		if (fader == null) {
			fader = new Fader(NullUtils.defaultIfNull(minValue, 0.0d), NullUtils.defaultIfNull(maxValue, 1.0d));
		}
	}

	/**
	 * Updates the bar to a given value.
	 *
	 * @param v
	 *            the value to set the bar to
	 * @return the console progress bar
	 */
	public ConsoleProgressBar updateValue(double v) {
		checkFader();
		fader.setValue(v);
		return this;
	}

	/**
	 * Updates the bar by a given value.
	 *
	 * @param v
	 *            the value
	 * @return the console progress bar
	 */
	public ConsoleProgressBar updateValueBy(double v) {
		checkFader();
		fader.setValue(fader.getValue() + v);
		return this;
	}

	/**
	 * Updates the bar to a given percentage.
	 *
	 * @param p
	 *            the percentage to set the bar to
	 * @return the console progress bar
	 */
	public ConsoleProgressBar updatePercentage(double p) {
		checkFader();
		fader.setPercentage(p);
		return this;
	}

	/**
	 * Updates the bar by a given percentage.
	 *
	 * @param p
	 *            the percentage
	 * @return the console progress bar
	 */
	public ConsoleProgressBar updatePercentageBy(double p) {
		checkFader();
		fader.setPercentage(fader.getPercentage() + p);
		return this;
	}

	/**
	 * Resets the bar to its minimum value.
	 *
	 * @return the console progress bar
	 */
	public ConsoleProgressBar reset() {
		checkFader();
		fader.setValue(fader.getMinimalValue());
		return this;
	}

	/**
	 * Sets the bar to its maximum value.
	 *
	 * @return the console progress bar
	 */
	public ConsoleProgressBar complete() {
		checkFader();
		fader.setValue(fader.getMaximalValue());
		return this;
	}

	/**
	 * Draws the draw-able component of this progress bar.
	 * <p>
	 * Please call this as frequent as possible. The real draw-call will only be issued if there really has been a change in the graphical representation of the
	 * bar.
	 *
	 * @param ps
	 *            the print-stream to draw to
	 * @return the console progress bar
	 */
	public ConsoleProgressBar draw(PrintStream ps) {
		checkFader();

		int fullNumber = (int) (fader.getPercentage() * width);

		if ((fullNumber != lastNumberOfCharactersDrawn) || !drawInitialized) {
			component.draw(ps, fader, width, drawInitialized, fullNumber, lastNumberOfCharactersDrawn);
			drawInitialized = true;
			lastNumberOfCharactersDrawn = fullNumber;
		}
		return this;
	}
}
