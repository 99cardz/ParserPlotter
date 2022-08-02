package parser.syntaxtree;

import java.util.Arrays;

public class VariableNode extends SyntaxNode {
    public void print() {
        System.out.print("x");
    }

    public double eval(double x) {
        return x;
    }

	public double[] evalAll(double[] values) {
		return Arrays.copyOf(values, values.length);
	}
}
