package parser.syntaxtree;

public class LogNode extends UnarySyntaxNode {

    public LogNode(SyntaxNode left) {
        super(left);
    }

    public void print() {
        System.out.print("log(");
        left.print();
        System.out.print(")");
    }

    public double eval(double x) {
        return Math.log(left.eval(x));
    }

	public double[] evalAll(double[] values) {
		
		double[] inner = left.evalAll(values);
		double[] result = new double[values.length];
		
		for (int i = 0; i < values.length; i++)
			result[i] = Math.log(inner[i]);
	
		for (int i = 1, len = values.length-1; i < len; i++) {
			// just passed 0
			if (inner[i] <= 0 && inner[i+1] >= 0)
				result[i] = Double.POSITIVE_INFINITY;
			// will pass 0
			else if (inner[i-1] <= 0 && inner[i] >= 0)
				result[i] = Double.NEGATIVE_INFINITY;
		}
		return result;
	}
}