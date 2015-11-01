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
package info.unterrainer.java.tools.reporting.consoleprogressbar;

import info.unterrainer.java.tools.datastructures.Fader;
import info.unterrainer.java.tools.reporting.consoleprogressbar.drawablecomponents.DrawableComponent;
import info.unterrainer.java.tools.reporting.consoleprogressbar.drawablecomponents.ProgressBar;
import info.unterrainer.java.tools.reporting.consoleprogressbar.drawablecomponents.SimpleInsertBar;
import info.unterrainer.java.tools.utils.NullUtils;

import java.io.PrintStream;

import javax.annotation.Nullable;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.experimental.ExtensionMethod;

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
@ExtensionMethod(NullUtils.class)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Accessors(chain = true)
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

	@Getter
	private boolean drawInitialized = false;
	private int lastNumberOfCharactersDrawn;
	@Getter
	private DrawableComponent component;

	@Builder
	public ConsoleProgressBar(@Nullable Integer width, @Nullable Double minValue, @Nullable Double maxValue, @Nullable Boolean controlCharacterSupport,
			@Nullable DrawableComponent component) {

		this.minValue = minValue.or(0.0d);
		this.maxValue = maxValue.or(1.0d);
		this.width = width.or(50);
		this.controlCharacterSupport = controlCharacterSupport.orNoNull(true).booleanValue();

		if (component == null) {
			if (this.controlCharacterSupport) {
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
			fader = new Fader(minValue.orNoNull(0.0d).doubleValue(), maxValue.orNoNull(1.0d).doubleValue());
		}
	}

	/**
	 * Updates the bar to a given value.
	 *
	 * @param v the value to set the bar to
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
	 * @param v the value
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
	 * @param p the percentage to set the bar to
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
	 * @param p the percentage
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
	 * Decides if a redraw is necessary.
	 * <p>
	 * This is the case if the last drawn value differs from the current one or if the component hasn't been drawn yet at all.
	 *
	 * @return
	 */
	public boolean isRedrawNecessary() {
		return !drawInitialized || (int) (fader.getPercentage() * width) != lastNumberOfCharactersDrawn;
	}

	/**
	 * Redraws the draw-able component of this progress bar by calling {@link #remove(PrintStream)} and then {@link #draw(PrintStream)}.
	 * <p>
	 * Please call this as frequent as possible.<br>
	 * The real remove-draw call will only be issued if there really has been a change in the graphical representation of the bar.
	 *
	 * @param ps the print-stream to draw to
	 * @return the console progress bar
	 */
	public ConsoleProgressBar redraw(@Nullable PrintStream ps) {
		if (ps != null) {
			checkFader();
			int fullNumber = (int) (fader.getPercentage() * width);

			if (isRedrawNecessary()) {
				if (drawInitialized) {
					component.remove(ps, width, lastNumberOfCharactersDrawn);
				}
				component.draw(ps, fader, width, drawInitialized, fullNumber, lastNumberOfCharactersDrawn);
				drawInitialized = true;
				lastNumberOfCharactersDrawn = fullNumber;
			}
		}
		return this;
	}

	/**
	 * This method will draw the component. It will not remove the component first and it will draw at any circumstances.<br>
	 * If you want to re-draw it (update its values visually) consider calling redraw instead.
	 *
	 * @param ps the print-stream to draw to
	 * @return the console progress bar
	 */
	public ConsoleProgressBar draw(@Nullable PrintStream ps) {
		if (ps != null) {
			checkFader();
			int fullNumber = (int) (fader.getPercentage() * width);
			component.draw(ps, fader, width, drawInitialized, fullNumber, lastNumberOfCharactersDrawn);
			drawInitialized = true;
			lastNumberOfCharactersDrawn = fullNumber;
			ps.flush();
		}
		return this;
	}

	/**
	 * This method removes the component from the print-stream if possible.
	 * <p>
	 * If you use a component that cannot do so, e.g. components designed for consoles with no control-character support, then this call will do nothing.
	 *
	 * @param ps the print-stream to draw to
	 * @return the console progress bar
	 */
	public ConsoleProgressBar remove(@Nullable PrintStream ps) {
		if (ps != null) {
			checkFader();
			if (drawInitialized) {
				component.remove(ps, width, lastNumberOfCharactersDrawn);
				ps.flush();
			}
		}
		return this;
	}
}
