package parser.syntaxtree;

import java.util.Arrays;

public class NumberNode extends SyntaxNode {
    double value;

    public NumberNode(double value) {
        this.value = value;
    }

    @Override
	public void print() {
        System.out.print(value);
    }

    @Override
	public double eval(double x) {
        return value;
    }

	@Override
	public double[] evalAll(double[] values) {
		double[] numbers = new double[values.length];
		Arrays.fill(numbers, value);
		return numbers;
	}
}
