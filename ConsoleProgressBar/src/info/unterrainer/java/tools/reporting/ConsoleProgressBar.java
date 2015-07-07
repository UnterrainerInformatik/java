package info.unterrainer.java.tools.reporting;


import java.io.PrintStream;

import info.unterrainer.java.tools.datastructures.Fader;
import info.unterrainer.java.tools.utils.NullUtils;
import info.unterrainer.java.tools.utils.StringUtils;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
public class ConsoleProgressBar {

	@Getter
	private Fader fader;
	private Double minValue;
	private Double maxValue;

	@Getter
	@Setter
	private boolean consoleSupportsControlCharacters = true;

	@Getter
	@Setter
	private Integer width = 50;
	private boolean drawInitialized = false;
	private int lastNumberOfFullCharsDrawed;

	@Getter
	@Setter
	private String begin = "[";
	@Getter
	@Setter
	private String end = "]";
	@Getter
	@Setter
	private Character full = '#';
	@Getter
	@Setter
	private Character empty = '-';
	@Getter
	@Setter
	private Character legendFill = '>';

	@Builder
	public ConsoleProgressBar(Integer width, Double minValue, Double maxValue, String beginString, String endString, Character fullCharacter,
			Character emptyCharacter, Character legendFillCharacter, Boolean consoleSupportsControlCharacters) {

		begin = NullUtils.defaultIfNull(beginString, "[");
		end = NullUtils.defaultIfNull(endString, "]");
		full = NullUtils.defaultIfNull(fullCharacter, '#');
		empty = NullUtils.defaultIfNull(emptyCharacter, '-');
		legendFill = NullUtils.defaultIfNull(legendFillCharacter, '>');
		this.minValue = NullUtils.defaultIfNull(minValue, 0.0d);
		this.maxValue = NullUtils.defaultIfNull(maxValue, 1.0d);
		this.width = NullUtils.defaultIfNull(width, 50);
		this.consoleSupportsControlCharacters = NullUtils.defaultIfNull(consoleSupportsControlCharacters, true);
	}

	public ConsoleProgressBar(int width, double minValue, double maxValue) {
		this(width);
	}

	public ConsoleProgressBar(double minValue, double maxValue) {
		this();
	}

	public ConsoleProgressBar(int width) {
		this.width = width;
	}

	private void checkFader() {
		if (fader == null) {
			fader = new Fader(NullUtils.defaultIfNull(minValue, 0.0d), NullUtils.defaultIfNull(maxValue, 1.0d));
		}
	}

	public ConsoleProgressBar updateValue(double p) {
		checkFader();
		fader.setValue(p);
		return this;
	}

	public ConsoleProgressBar updateValueBy(int v) {
		checkFader();
		fader.setValue(fader.getValue() + v);
		return this;
	}

	public ConsoleProgressBar updatePercentage(int v) {
		checkFader();
		fader.setPercentage(v);
		return this;
	}

	public ConsoleProgressBar updatePercentageBy(double p) {
		checkFader();
		fader.setPercentage(fader.getPercentage() + p);
		return this;
	}

	public ConsoleProgressBar reset() {
		checkFader();
		fader.setValue(fader.getMinimalValue());
		return this;
	}

	public ConsoleProgressBar complete() {
		checkFader();
		fader.setValue(fader.getMaximalValue());
		return this;
	}

	public ConsoleProgressBar draw(PrintStream ps) {
		checkFader();
		if (consoleSupportsControlCharacters) {
			drawOnControlCharacterSupportedConsole(ps);
		} else {
			drawForFileOutput(ps);
		}
		return this;
	}

	private void drawOnControlCharacterSupportedConsole(PrintStream ps) {
		String s = "";
		int fullNumber = (int) (fader.getPercentage() * width);
		boolean draw = (fullNumber != lastNumberOfFullCharsDrawed) || !drawInitialized;

		if (draw) {
			if (drawInitialized) {
				// Delete already drawn bar using command-characters.
				s += StringUtils.repeat("\b", begin.length() + width + end.length());
			} else {
				drawInitialized = true;
			}

			s += begin;
			s += StringUtils.repeat(full + "", fullNumber);
			s += StringUtils.repeat(empty + "", width - fullNumber);
			s += end;

			ps.print(s);
			ps.flush();
		}

		lastNumberOfFullCharsDrawed = fullNumber;
	}

	private void drawForFileOutput(PrintStream ps) {

		if (!drawInitialized) {
			// Draw the lead-line, the legend to the bar.
			ps.print(begin + StringUtils.repeat(legendFill + "", width) + end + "\n" + StringUtils.repeat(" ", begin.length()));
			ps.flush();

			drawInitialized = true;
		}

		int fullNumber = (int) (fader.getPercentage() * width);
		ps.print(StringUtils.repeat(full + "", fullNumber - lastNumberOfFullCharsDrawed));
		ps.flush();

		lastNumberOfFullCharsDrawed = fullNumber;
	}
}
