package info.unterrainer.java.tools.utils;

import org.junit.Assert;
import org.junit.Test;

public class HrfUtilsTests {

	@Test
	public void HumanReadableDuration_Boundaries() throws InterruptedException {
		test(new Long[] { 1L, HrfUtils.SECOND, HrfUtils.MINUTE, HrfUtils.HOUR, HrfUtils.DAY, HrfUtils.WEEK, HrfUtils.MONTH, HrfUtils.YEAR },
				new String[] { "1ms", "1s", "1m", "1h", "1d", "1W", "1M", "1Y" });
	}

	@Test
	public void HumanReadableDuration_OddNumbers1() throws InterruptedException {
		test(new Long[] { 2L, HrfUtils.SECOND + 1, HrfUtils.MINUTE + 1, HrfUtils.HOUR + 1, HrfUtils.DAY + 1, HrfUtils.WEEK + 1, HrfUtils.MONTH + 1,
				HrfUtils.YEAR + 1 }, new String[] { "2ms", "1s", "1m", "1h", "1d", "1W", "1M", "1Y" });
	}

	@Test
	public void HumanReadableDuration_OddNumbers2() throws InterruptedException {
		test(new Long[] { 500L, HrfUtils.SECOND + 500, HrfUtils.MINUTE + HrfUtils.MINUTE / 2, HrfUtils.HOUR + HrfUtils.HOUR / 2,
				HrfUtils.DAY + HrfUtils.DAY / 2, HrfUtils.WEEK + HrfUtils.WEEK / 2, HrfUtils.MONTH + HrfUtils.MONTH / 2, HrfUtils.YEAR + HrfUtils.YEAR / 2 },
				new String[] { "500ms", 1.5 + "s", 1.5 + "m", 1.5 + "h", 1.5 + "d", 1.5 + "W", 1.5 + "M", 1.5 + "Y" });
	}

	@Test
	public void HumanReadableDuration_OddNumbers2_Negative() throws InterruptedException {
		test(new Long[] { -500L, -HrfUtils.SECOND - 500, -HrfUtils.MINUTE - HrfUtils.MINUTE / 2, -HrfUtils.HOUR - HrfUtils.HOUR / 2,
				-HrfUtils.DAY - HrfUtils.DAY / 2, -HrfUtils.WEEK - HrfUtils.WEEK / 2, -HrfUtils.MONTH - HrfUtils.MONTH / 2,
				-HrfUtils.YEAR - HrfUtils.YEAR / 2 },
				new String[] { "-500ms", -1.5 + "s", -1.5 + "m", -1.5 + "h", -1.5 + "d", -1.5 + "W", -1.5 + "M", -1.5 + "Y" });
	}

	@Test
	public void HumanReadableDuration_Boundaries_LongUnits() throws InterruptedException {
		testLong(new Long[] { 1L, HrfUtils.SECOND, HrfUtils.MINUTE, HrfUtils.HOUR, HrfUtils.DAY, HrfUtils.WEEK, HrfUtils.MONTH, HrfUtils.YEAR },
				new String[] { "1 milliseconds", "1 seconds", "1 minutes", "1 hours", "1 days", "1 weeks", "1 months", "1 years" });
	}

	private void test(Long[] values, String[] expected) {
		for (int i = 0; i < values.length; i++) {
			String r = HrfUtils.toHumanReadableDuration(values[i]);
			Assert.assertEquals(expected[i], r);
		}
	}

	private void testLong(Long[] values, String[] expected) {
		for (int i = 0; i < values.length; i++) {
			String r = HrfUtils.toHumanReadableDurationLongUnits(values[i]);
			Assert.assertEquals(expected[i], r);
		}
	}
}
