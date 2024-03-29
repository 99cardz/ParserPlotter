package parser.syntaxtree;

public class LogNode extends UnarySyntaxNode {

    public LogNode(SyntaxNode left) {
        super(left);
    }

    @Override
	public void print() {
        System.out.print("log(");
        left.print();
        System.out.print(")");
    }

    @Override
	public double eval(double x) {
        return Math.log(left.eval(x));
    }

	@Override
	public double[] evalAll(double[] values) {

		double[] inner = left.evalAll(values);
		double[] result = new double[values.length];

		result[0] = Math.log(inner[0]);
		int len = values.length-1;
		result[len] = Math.log(inner[len]);
		for (int i = 1; i < len; i++)
			result[i] =
				inner[i-1] <= 0 && inner[i] >= 0 || inner[i] >= 0 && inner[i+1] <= 0
				? Double.NEGATIVE_INFINITY : Math.log(inner[i]);

		return result;
	}
}