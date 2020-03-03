package devoir2;

public class Main {

	public static void main(String[] args) {
		double r = 2 / (new AgeModel().expectedParenthoodSpan(14, 50));
		Simulation simu = new Simulation(2.5, 0.01, 100.0, r, 0.9);
		simu.simulate(20, 1000);
	}

}
