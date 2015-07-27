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
package info.unterrainer.java.tools.reporting.consoleprogressbar.drawablecomponents;

import info.unterrainer.java.tools.datastructures.Fader;
import info.unterrainer.java.tools.utils.NullUtils;
import info.unterrainer.java.tools.utils.StringUtils;

import java.io.PrintStream;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.ExtensionMethod;

/**
 * This progress-bar draws a bar like:
 *
 * <pre>
 * {@code
 * [  4%]
 * 
 * ...
 * 
 * [ 56%]
 * 
 * ...
 * 
 * [100%]
 * }
 * </pre>
 *
 * This bar ignores the width-parameter for the drawing of the percentage. However you may use it to regulate the sensitivity of the gauge (a higher width
 * updates the gauge more often). Widths over 100 don't make sense in this context since it will be drawn more often but the value won't change.
 * <p>
 * You may specify any other begin- , end- , percent- or empty-character you like.<br />
 * This bar is only a good choice if your console supports control characters since for this representation you have to to clear all characters on each redraw
 * using '\b' (backspace).
 * <p>
 * <table>
 * <tr>
 * <td><b>begin</b></td>
 * <td>"["</td>
 * </tr>
 * <tr>
 * <td><b>end</b></td>
 * <td>"]"</td>
 * </tr>
 * <tr>
 * <td><b>percent</b></td>
 * <td>"%"</td>
 * </tr>
 * <tr>
 * <td><b>empty</b></td>
 * <td>{@code ' '}</td>
 * </tr>
 * </table>
 */
@ExtensionMethod(NullUtils.class)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PercentGauge implements DrawableComponent {

	@Getter
	@Setter
	private String begin;
	@Getter
	@Setter
	private String end;
	@Getter
	@Setter
	private String percent;
	@Getter
	@Setter
	private Character empty;

	@Builder
	public PercentGauge(String begin, String end, String percent, Character empty) {
		super();
		this.begin = begin.or("[");
		this.end = end.or("]");
		this.percent = percent.or("%");
		this.empty = empty.or(' ');
	}

	@Override
	public void draw(PrintStream ps, Fader fader, int width, boolean drawInitialized, int value, int lastValue) {
		String s = "";

		if (drawInitialized) {
			// Delete already drawn bar using command-characters.
			s += StringUtils.repeat("\b", begin.length() + percent.length() + 3 + end.length());
		}

		int v = (int) (fader.getPercentage() * 100);

		s += begin;
		if (v < 10) {
			s += empty;
		}
		if (v < 100) {
			s += empty;
		}
		s += v;
		s += percent;
		s += end;

		ps.print(s);
		ps.flush();
	}
}
