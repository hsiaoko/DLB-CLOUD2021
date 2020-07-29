package org.cloudbus.cloudsim.distributions;
import java.util.Random;
public class NormalDistr implements ContinuousDistribution  {
	
	
	private final double mean;
	private final double dev;
		
	
	public NormalDistr(double mean, double dev) {
		this.mean = mean;
		this.dev = dev;
	}
	
	@Override
	public double sample() {
		
		java.util.Random random = new java.util.Random();
	    return (this.dev*this.dev)*random.nextGaussian()+this.mean;
	}

}
