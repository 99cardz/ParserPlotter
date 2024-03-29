package parser.syntaxtree;

public class DivNode extends BinarySyntaxNode {
    public DivNode(SyntaxNode left, SyntaxNode right) {
        super(left, right);
    }

    @Override
	public void print() {
        System.out.print("(");
        left.print();
        System.out.print(" / ");
        right.print();
        System.out.print(")");
    }

    @Override
	public double eval(double x) {
        return left.eval(x) / right.eval(x);
    }

	@Override
	public double[] evalAll(double[] values) {
		double[] d = right.evalAll(values); // denominators
		double[] n = left.evalAll(values); // numerators
		double[] result = new double[values.length];

		for (int i = 0; i < result.length; i++)
			result[i] = Double.isInfinite(d[i]) ? 0 : n[i] / d[i];

		for (int i = 1, len = d.length - 1; i < len; i++) {
			// only adjust the limit to infinity if the result is changing
			// this will exclude functions like x/x where x=0 is just undefined
			// and doesn't go to infinity
			if (result[i-1] != result[i] && result[i] != result[i+1]) {
				// ensuring the neighboring numerators are either above or below 0
				// makes sure that cases where 0/infinity don't get a result of infinity

				// numerator above 0
				if (n[i-1] > 0 && n[i] > 0 && n[i+1] > 0) {
					// denominator is ascending
					if (d[i-1] < d[i] && d[i] < d[i+1]) {
						// denominator will pass 0
						if (d[i-1] < 0 && d[i] < 0 && d[i+1] >= 0)
							result[i] = Double.NEGATIVE_INFINITY;
						// denominator just passed 0
						else if (d[i-1] <= 0 && d[i] > 0 && d[i+1] > 0)
							result[i] = Double.POSITIVE_INFINITY;
					}
					// denominator is descending
					else if (d[i-1] > d[i] && d[i] > d[i+1]) {
						// denominator will pass 0
						if (d[i] >= 0 && d[i+1] <= 0)
							result[i] = Double.POSITIVE_INFINITY;
						// denominator just passed 0
						else if (d[i-1] >= 0 && d[i] <= 0)
							result[i] = Double.NEGATIVE_INFINITY;
					}
				}
				// numerator blow 0
				else if (n[i-1] < 0 && n[i] < 0 && n[i+1] < 0) {
					// denominator is ascending
					if (d[i-1] < d[i] && d[i] < d[i+1]) {
						// denominator will pass 0
						if (d[i] <= 0 && d[i+1] >= 0)
							result[i] = Double.POSITIVE_INFINITY;
						// denominator just passed 0
						else if (d[i-1] <= 0 && d[i] >= 0)
							result[i] = Double.NEGATIVE_INFINITY;
					}
					// denominator is descending
					else if (d[i-1] > d[i] && d[i] > d[i+1]) {
						// denominator will pass 0
						if (d[i] >= 0 && d[i+1] <= 0)
							result[i] = Double.NEGATIVE_INFINITY;
						// denominator just passed 0
						else if (d[i-1] >= 0 && d[i] <= 0)
							result[i] = Double.POSITIVE_INFINITY;
					}
				}
			}
		}
		return result;
	}
}
