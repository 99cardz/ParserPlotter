package parser.syntaxtree;

public class TanNode extends UnarySyntaxNode {

    public TanNode(SyntaxNode left) {
        super(left);
    }

    @Override
	public void print() {
        System.out.print("tan(");
        left.print();
        System.out.print(")");
    }

    @Override
	public double eval(double x) {
    	return Math.tan(left.eval(x));
    }

	@Override
	public double[] evalAll(double[] values) {
		double[] inner = left.evalAll(values);
    	double[] result = new double[values.length];

    	for (int i = 0; i < result.length; i++)
    		result[i] = Math.tan(inner[i]);

    	for (int i = 1, len = result.length-1; i < len; i++) {
    		// ascending
    		if (inner[i-1] < inner[i] && inner[i] < inner[i+1]) {
        		if (result[i-1] > result[i])
        			result[i] =  Double.NEGATIVE_INFINITY;
        		if (result[i] > result[i+1])
        			result[i] =  Double.POSITIVE_INFINITY;
        	}
        	// descending
        	if (inner[i-1] > inner[i] && inner[i] > inner[i+1]) {
        		if (result[i-1] < result[i])
        			result[i] = Double.POSITIVE_INFINITY;
        		if (result[i] < result[i+1])
        			result[i] = Double.NEGATIVE_INFINITY;
        	}
    	}
        return result;
	}
}
