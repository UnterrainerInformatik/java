package info.unterrainer.java.tools.reporting.consoleprogressbar;

import info.unterrainer.java.tools.reporting.consoleprogressbar.drawablecomponents.PercentGauge;
import info.unterrainer.java.tools.reporting.consoleprogressbar.drawablecomponents.ProgressBar;
import info.unterrainer.java.tools.reporting.consoleprogressbar.drawablecomponents.SimpleInsertBar;

import org.junit.Ignore;
import org.junit.Test;

public class ConsoleProgressBarTest {

	@Test
	@Ignore
	public void SimpleInsertBarTest() throws InterruptedException {
		ConsoleProgressBar bar = ConsoleProgressBar
				.builder()
				.minValue(0d)
				.maxValue(100d)
				.component(SimpleInsertBar.builder().prefix("prefix: ").build())
				.build();
		for (int i = 0; i <= 100; i++) {
			bar.updateValue(i);
			bar.redraw(System.out);
			Thread.sleep(30);
		}
		bar.complete().redraw(System.out);
	}

	@Test
	@Ignore
	public void ProgressBarTest() throws InterruptedException {
		ConsoleProgressBar bar = ConsoleProgressBar.builder().minValue(0d).maxValue(100d).component(ProgressBar.builder().prefix("prefix: ").build()).build();
		for (int i = 0; i <= 100; i++) {
			bar.updateValue(i);
			bar.redraw(System.out);
			Thread.sleep(30);
		}
		bar.complete().redraw(System.out);
	}

	@Test
	@Ignore
	public void PercentGaugeTest() throws InterruptedException {
		ConsoleProgressBar bar = ConsoleProgressBar.builder().minValue(0d).maxValue(100d).component(PercentGauge.builder().prefix("prefix: ").build()).build();
		for (int i = 0; i <= 100; i++) {
			bar.updateValue(i);
			bar.redraw(System.out);
			Thread.sleep(30);
		}
		bar.complete().redraw(System.out);
	}
}
