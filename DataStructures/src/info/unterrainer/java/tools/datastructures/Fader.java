package info.unterrainer.java.tools.datastructures;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
public class Fader {
	private static double A_THIRD = 1d / 3d;

	private Interval<Double> interval = new Interval<Double>(0d, 100d);

	@Getter
	private double value;
	@Getter
	private double percentage;

	@Getter
	@Setter
	private boolean inverted;

	public Fader(double minValue, double maxValue) {
		// Avoid IllegalArgumentException when the new minimal-value is higher than the default maximal-value.
		if (minValue > maxValue) {
			interval.setMax(maxValue);
			interval.setMin(minValue);
		} else {
			interval.setMin(minValue);
			interval.setMax(maxValue);
		}
	}

	public double getMinimalValue() {
		return interval.getMin();
	}

	public void setMinimalValue(Double value) {
		interval.setMin(value);

		// Ensure value is still in range..
		if (this.value < interval.getMin()) {
			this.value = interval.getMin();
		}
	}

	public double getMaximalValue() {
		return interval.getMax();
	}

	public void setMaximalValue(Double value) {
		interval.setMax(value);

		// Ensure value is still in range..
		if (this.value > interval.getMax()) {
			this.value = interval.getMax();
		}
	}

	/**
	 * Gets or sets the faders value. Automatically recalculates the percentage accordingly.
	 *
	 * @param value
	 *            the current value of the fader
	 */
	public void setValue(double value) {
		this.value = interval.clamp(value);
		percentage = getPercentageAtValue(this.value);
	}

	/**
	 * Gets or sets the percentage for the slider. A double value between 0.0 and 1.0.
	 *
	 * @param value
	 *            the value of the slider in percent
	 */
	public void setPercentage(double value) {
		this.value = getValueAtPercentage(clampPercent(value));
	}

	private double clampPercent(double value) {
		double result = value;
		if (value > 1.0d) {
			result = 1.0d;
		}
		if (value < 0.0d) {
			result = 0.0d;
		}
		return result;
	}

	/**
	 * Getter for the value at a certain percentage of this {@link Fader}.
	 *
	 * @param percentage
	 *            the percentage to get the value for
	 * @return what the value of the fader would be if it was at the given percentage
	 */
	private double getValueAtPercentage(double percentage) {
		if (inverted) {
			return interval.getMin() + (1f - percentage) * (interval.getMax() - interval.getMin());
		}
		return interval.getMin() + percentage * (interval.getMax() - interval.getMin());
	}

	/**
	 * Getter for the percentage at a certain value of this {@link Fader}.
	 *
	 * @param value
	 *            the value to get the percentage for
	 * @return what the percentage of the fader would be if it was at the given value
	 */
	private double getPercentageAtValue(double value) {
		if (inverted) {
			if (interval.getMax() - interval.getMin() == 0d) {
				return 0.0d;
			}
			return 1f - ((value - interval.getMin()) / (interval.getMax() - interval.getMin()));
		}

		if (interval.getMax() - interval.getMin() == 0d) {
			return 1.0d;
		}
		return (value - interval.getMin()) / (interval.getMax() - interval.getMin());
	}

	/**
	 * Gets or sets the quadratic value of the fader: y = x * x
	 *
	 * @return value
	 */
	public double getQuadraticValue() {
		return getValueAtPercentage(percentage * percentage);
	}

	/**
	 * Gets or sets the quadratic value of the fader: y = x * x
	 *
	 * @param value
	 */
	public void setQuadraticValue(double value) {
		percentage = Math.sqrt(getPercentageAtValue(value));
	}

	/**
	 * Gets or sets the cubic value of the fader: y = x * x * x
	 *
	 * @return value
	 */
	public double getCubicValue() {
		return getValueAtPercentage(percentage * percentage * percentage);
	}

	/**
	 * Gets or sets the cubic value of the fader: y = x * x * x
	 *
	 * @param value
	 */
	public void setCubicValue(double value) {
		percentage = Math.pow(getPercentageAtValue(value), A_THIRD);
	}

	/**
	 * Gets or sets the exponential value of the fader: y = (20^x - 1) / 20
	 *
	 * @return value
	 */
	public double getExponentialValue() {
		return getValueAtPercentage((Math.pow(20d, percentage) - 1.0) / 20.0);
	}

	/**
	 * Gets or sets the exponential value of the fader: y = (20^x - 1) / 20
	 *
	 * @param value
	 */
	public void setExponentialValue(double value) {
		percentage = Math.log((20.0 * getPercentageAtValue(value)) + 1.0) / Math.log(20.0);
	}

	/**
	 * Gets the bidirectional slow start: y = (cos((x - 1)* PI) + 1) / 2
	 *
	 * @return value
	 */
	public double getBidirectionalSlow() {
		return getValueAtPercentage((Math.cos((percentage - 1.0) * Math.PI) + 1.0) / 2.0);
	}

	/**
	 *
	 * @param value
	 */
	public void setBidirectionalSlow(double value) {
		percentage = 1.0 - (Math.acos(2.0 * getPercentageAtValue(value) - 1.0) / Math.PI);
	}

	/**
	 * Gets the bidirectional quick start: y = ((2x - 1)^3+1)/2
	 *
	 * @return value
	 */
	public double getBidirectionalQuick() {
		return getValueAtPercentage((Math.pow((2.0 * percentage - 1.0), 3.0) + 1.0) / 2.0);
	}

	/**
	 * Gets the bidirectional quick start: y = ((2x - 1)^3+1)/2
	 *
	 * @param value
	 */
	public void setBidirectionalQuick(double value) {
		percentage = (Math.pow(2.0 * getPercentageAtValue(value) - 1.0, A_THIRD) + 1.0) / 2.0;
	}

	@Override
	public String toString() {
		return interval + "[v:" + value + ",p:" + percentage + ",i:" + inverted + "]";
	}
}
