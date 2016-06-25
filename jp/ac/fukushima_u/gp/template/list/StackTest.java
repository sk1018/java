package jp.ac.fukushima_u.gp.template.list;

import java.util.Stack;

public class StackTest {


	public static void main(String[] args) {

		StackTest.stackStringTest();
		StackTest.stackIntTest();
		StackTest.stackClassTest();




	}

	private static void stackStringTest(){

		Stack<String> stack = new Stack<String>();

		stack.push("ABC");
		stack.push("DEF");
		stack.push("GHI");

		for(int i=0;i<3;i++){
			System.out.println(stack.pop());
		}


	}

	private static void stackIntTest(){

		Stack<Integer> stack = new Stack<Integer>();

		stack.push(123);
		stack.push(456);
		stack.push(789);

		for(int i=0;i<3;i++){
			System.out.println(stack.pop());
		}



	}


	private static void stackClassTest(){

		Stack<StackTestClass> stack = new Stack<StackTestClass>();

		//StackTestClass stc = new StackTestClass("ABC",123);

		stack.push(new StackTestClass("ABC",123));
		stack.push(new StackTestClass("DEF",456));
		stack.push(new StackTestClass("GHI",789));
		
		for(int i=0;i<3;i++){
			StackTestClass stc = stack.pop();
			System.out.println(stc.s + " " + stc.n);
		}



	}




}

class StackTestClass{
	String s;
	int n;

	StackTestClass(String s,int n){
		this.s = s;
		this.n = n;
	}
}
