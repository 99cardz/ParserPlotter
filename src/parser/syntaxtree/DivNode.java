package parser.syntaxtree;

public class DivNode extends BinarySyntaxNode {
    public DivNode(SyntaxNode left, SyntaxNode right) {
        super(left, right);
    }

    public void print() {
        System.out.print("(");
        left.print();
        System.out.print(" / ");
        right.print();
        System.out.print(")");
    }

    public double eval(double x) {
        return left.eval(x) / right.eval(x);
    }

	public double[] evalAll(double[] values) {
		double[] denoms = right.evalAll(values);
		double[] result = left.evalAll(values);
		
		for (int i = 0; i < result.length; i++)
			result[i] = Double.isInfinite(denoms[i]) ? 0 : result[i] / denoms[i];
		
		for (int i = 1, len = denoms.length - 2; i < len; i++) {
			if (denoms[i-1] <= 0 && denoms[i] >= 0 || denoms[i+1] <= 0 && denoms[i] >= 0)
				result[i] = Double.POSITIVE_INFINITY;
			if (denoms[i] <= 0 && denoms[i+1] >= 0 || denoms[i] <= 0 && denoms[i-1] >= 0)
				result[i] = Double.NEGATIVE_INFINITY;
		}
		return result;
	}
}
