package engine.graphics.lwjgl.vector;

import java.util.Stack;

public class MatrixStack {
	Stack<Matrix4f> m_stack = new Stack<Matrix4f>();

	Matrix4f m_matrix = new Matrix4f();

	public Matrix4f getMatrix() {
		return m_matrix;
	}
	public Matrix4f peek() {
		return m_stack.peek();
	}
	public void pop() {
		m_matrix = m_stack.pop();
	}
	public void push() {
		m_stack.push(new Matrix4f(m_matrix));
	}

	public void identity() {
		peek().setIdentity();
	}
}