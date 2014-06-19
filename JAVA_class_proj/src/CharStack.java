import java.util.Stack;

public class CharStack{
	
	private Stack theStack;
	
	public CharStack(){
		theStack = new Stack();
	}
	
	public boolean empty(){
		return theStack.empty();
	}
	
	public void push(char c){
		theStack.push(new Character(c));
	}
	
	public char pop(){
		Character temp = (Character)theStack.pop();
		return temp.charValue();
	}
	
	public char peek(){
		Character temp = (Character)theStack.peek();
		return temp.charValue();
	}
}