package parser.syntaxtree;

public class CosNode extends UnarySyntaxNode {

    public CosNode(SyntaxNode left) {
        super(left);
    }

    public void print() {
        System.out.print("cos(");
        left.print();
        System.out.print(")");
    }

    public double eval(double x) {
//    	double prevY = Math.cos(left.eval(x - stride));
//    	double y = Math.cos(left.eval(x));
//    	double nextY = Math.cos(left.eval(x + stride));
//    	// round to zero if we pass it or will pass it
//    	if ((prevY < 0 && y >= 0) || (prevY > 0 && y <= 0))
//    		return 0;
//    	if ((nextY < 0 && y >= 0) || (nextY > 0 && y <= 0))
//    		return 0;
        return Math.cos(left.eval(x));
    }

	public double[] evalAll(double[] values) {
		
		double[] result = left.evalAll(values);
		
		for (int i = 0; i < values.length; i++)
			result[i] = Math.cos(result[i]);
		
		return result;
	}
}
