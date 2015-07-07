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
 * [########-------]
 * }
 * </pre>
 *
 * You may specify any other begin- ('['), end- (']'), full- ('#') or empty-character ('-') you like.<br />
 * This bar is only a good choice if your console supports control characters since for this representation you have to to clear all characters on each redraw
 * using '\b' (backspace).
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ProgressBar implements DrawableComponent {

	@Getter
	@Setter
	private String begin;
	@Getter
	@Setter
	private String end;
	@Getter
	@Setter
	private Character full;
	@Getter
	@Setter
	private Character empty;

	@Builder
	public ProgressBar(String begin, String end, Character full, Character empty) {
		super();
		this.begin = NullUtils.defaultIfNull(begin, "[");
		this.end = NullUtils.defaultIfNull(end, "]");
		this.full = NullUtils.defaultIfNull(full, '#');
		this.empty = NullUtils.defaultIfNull(empty, '-');
	}

	@Override
	public void draw(PrintStream ps, Fader fader, int width, boolean drawInitialized, int value) {
		String s = "";

		if (drawInitialized) {
			// Delete already drawn bar using command-characters.
			s += StringUtils.repeat("\b", begin.length() + width + end.length());
		}

		s += begin;
		s += StringUtils.repeat(full + "", value);
		s += StringUtils.repeat(empty + "", width - value);
		s += end;

		ps.print(s);
		ps.flush();
	}
}
