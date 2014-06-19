import java.util.Stack;

public class DoubleStack{
	
	private Stack theStack;
	
	public DoubleStack(){
		theStack = new Stack();
	}
	
	public boolean empty(){
		return theStack.empty();
	}
	
	public void push(double d){
		theStack.push(new Double(d));
	}
	
	public double pop(){
		Double temp = (Double)theStack.pop();
		return temp.doubleValue();
	}
	
	public double peek(){
		Double temp = (Double)theStack.peek();
		return temp.doubleValue();
	}
}