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
 * [>>>>>>>>>>>>>>>]
 *  ####
 *
 *  ...
 *
 * [>>>>>>>>>>>>>>>]
 *  ############
 *
 *  ...
 *
 * [>>>>>>>>>>>>>>>]
 *  ###############
 * }
 * </pre>
 *
 * ... with an ever growing number of '#' characters.</br/>
 * You may specify any other begin- , end- , full- or legendFill-character you like. <br />
 * This bar is always working. Even if your console doesn't support control characters like the Eclipse console-implementation (before Mars (4.5)) or a pipe to
 * a file.
 * <p>
 * Default values are:
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
 * <td><b>full</b></td>
 * <td>'#'</td>
 * </tr>
 * <tr>
 * <td><b>legendFill</b></td>
 * <td>'>'</td>
 * </tr>
 * </table>
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SimpleInsertBar implements DrawableComponent {

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
	private Character legendFill;

	@Builder
	public SimpleInsertBar(String begin, String end, Character full, Character legendFill) {
		super();
		this.begin = NullUtils.defaultIfNull(begin, "[");
		this.end = NullUtils.defaultIfNull(end, "]");
		this.full = NullUtils.defaultIfNull(full, '#');
		this.legendFill = NullUtils.defaultIfNull(legendFill, '>');
	}

	@Override
	public void draw(PrintStream ps, Fader fader, int width, boolean drawInitialized, int value, int lastValue) {

		if (!drawInitialized) {
			// Draw the lead-line, the legend to the bar.
			ps.print(begin + StringUtils.repeat(legendFill + "", width) + end + "\n" + StringUtils.repeat(" ", begin.length()));
			ps.flush();
		}

		ps.print(StringUtils.repeat(full + "", value - lastValue));
		ps.flush();
	}
}