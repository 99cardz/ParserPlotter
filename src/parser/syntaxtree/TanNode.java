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
    	double[] results = new double[values.length];

    	for (int i = 0; i < results.length; i++)
    		results[i] = Math.tan(inner[i]);

    	for (int i = 1, len = results.length-1; i < len; i++) {
    		// ascending
    		if (inner[i-1] < inner[i] && inner[i] < inner[i+1]) {
        		if (results[i-1] > results[i])
        			results[i] =  Double.NEGATIVE_INFINITY;
        		if (results[i] > results[i+1])
        			results[i] =  Double.POSITIVE_INFINITY;
        	}
        	// descending
        	if (inner[i-1] > inner[i] && inner[i] > inner[i+1]) {
        		if (results[i-1] < results[i])
        			results[i] = Double.POSITIVE_INFINITY;
        		if (results[i] < results[i+1])
        			results[i] = Double.NEGATIVE_INFINITY;
        	}
    	}
        return results;
	}
}
