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
import lombok.*;

import javax.annotation.Nullable;
import java.io.PrintStream;

/**
 * This progress-bar draws a bar like:
 *
 * <pre>
 * {@code
 * file a: [>>>>>>>>>>>>>>>]
 *          ####
 *
 *  ...
 *
 * file a: [>>>>>>>>>>>>>>>]
 *          ############
 *
 *  ...
 *
 * file a: [>>>>>>>>>>>>>>>]
 *          ###############
 * }
 * </pre>
 *
 * ... with an ever growing number of '#' characters.</br/> You may specify any other begin- , end- , full- or legendFill-character you like. <br />
 * This bar is always working. Even if your console doesn't support control characters like the Eclipse console-implementation (before Mars (4.5)) or a pipe to
 * a file.
 * <p>
 * Default values are:
 * <table>
 * <tr>
 * <td><b>prefix</b></td>
 * <td>"file a: "</td>
 * </tr>
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
	private String prefix;
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
	public SimpleInsertBar(@Nullable String prefix, @Nullable String begin, @Nullable String end, @Nullable Character full, @Nullable Character legendFill) {
		super();
		this.prefix = NullUtils.or(prefix, "");
		this.begin = NullUtils.or(begin, "[");
		this.end = NullUtils.or(end, "]");
		this.full = NullUtils.or(full, '#');
		this.legendFill = NullUtils.or(legendFill, '>');
	}

	@Override
	public void draw(PrintStream ps, Fader fader, int width, boolean drawInitialized, int value, int lastValue) {
		if (!drawInitialized) {
			// Draw the lead-line, the legend to the bar.
			int l = begin.length() + prefix.length();
			ps.print(prefix + begin + StringUtils.repeat(legendFill, width) + end + "\n" + StringUtils.repeat(" "
					, l));
		}

		ps.print(StringUtils.repeat(full, value - lastValue));
	}

	@Override
	public void remove(PrintStream ps, int width, int lastValue) {
	}
}
