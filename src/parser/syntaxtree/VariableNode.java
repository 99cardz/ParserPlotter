package parser.syntaxtree;

import java.util.Arrays;

public class VariableNode extends SyntaxNode {
    @Override
	public void print() {
        System.out.print("x");
    }

    @Override
	public double eval(double x) {
        return x;
    }

	@Override
	public double[] evalAll(double[] values) {
		return Arrays.copyOf(values, values.length);
	}
}
