package info.unterrainer.java.tools.reporting.consoleprogressbar.drawablecomponents;

import java.io.PrintStream;

import info.unterrainer.java.tools.datastructures.Fader;
import info.unterrainer.java.tools.utils.NullUtils;
import info.unterrainer.java.tools.utils.StringUtils;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * This progress-bar draws a bar like:
 *
 * <pre>
 * {@code
 * [ 56%]
 * }
 * </pre>
 *
 * This bar ignores the width-parameter.
 * <p>
 * You may specify any other begin- ('['), end- (']'), percent- ('%') or empty-character (' ') you like.<br />
 * This bar is only a good choice if your console supports control characters since for this representation you have to to clear all characters on each redraw
 * using '\b' (backspace).
 */
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
		this.begin = NullUtils.defaultIfNull(begin, "[");
		this.end = NullUtils.defaultIfNull(end, "]");
		this.percent = NullUtils.defaultIfNull(percent, "%");
		this.empty = NullUtils.defaultIfNull(empty, ' ');
	}

	@Override
	public void draw(PrintStream ps, Fader fader, int width, boolean drawInitialized, int value) {
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
