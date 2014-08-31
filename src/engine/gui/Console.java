package engine.gui;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.Locale;

import bsh.ConsoleInterface;
import bsh.Interpreter;
import engine.graphics.Color;
import engine.graphics.Renderer;
import engine.graphics.font.Font;
import engine.inputs.InputEvent;
import engine.inputs.keyboard.Key;
import engine.inputs.keyboard.KeyEvent;
import engine.inputs.keyboard.Keyboard;
import engine.inputs.keyboard.TypeModel;
import engine.inputs.keyboard.TypeModel.TypeListener;

public class Console extends GUI implements ConsoleInterface, TypeListener {
	private ArrayList<String> m_history = new ArrayList<String>();
	private StringBuilder m_text = new StringBuilder();
	private boolean m_show = true;
	private int m_maxLines = 30;
	private int m_cursorIndex = 0;
	private int m_historyIndex = -1;

	private Font m_font;
	private TypeModel m_typeModel;
	// They will read the lines with this
	private ConsoleReader m_in;

	// They will write the lines to this
	private ConsolePrintStream m_out;
	private Interpreter m_interpreter;

	public Console(Font font) {
		m_font = font;
		m_typeModel = new TypeModel();
		m_typeModel.addListener(this);
		try {
			m_in = new ConsoleReader();
			m_out = new ConsolePrintStream();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void setKeyboard(Keyboard keyboard) {
		m_keyboard = keyboard;
		m_typeModel.setKeyboard(keyboard);
		for (Widget w : getChildren()) {
			w.setKeyboard(keyboard);
		}
	}

	public Interpreter getInterpreter() {
		return m_interpreter;
	}

	@Override
	public void onInputEvent(InputEvent e) {
		if (e instanceof KeyEvent) {
			KeyEvent event = (KeyEvent) e;
			m_typeModel.process(event);
		}
	}

	@Override
	public void specialKey(Key key) {
		if (!m_show)
			return;
		if (key.getName().equals("RETURN")) {
			m_in.characterTyped('\n');
		} else if (key.getName().equals("BACK")) {
			m_in.backspace();
		} else if (key.getName().equals("LEFT")) {
			// Still room to go to index zero
			if (m_cursorIndex > 0)
				m_cursorIndex--;
		} else if (key.getName().equals("RIGHT")) {
			// Still room to move over one
			// Farthest right is string length
			if (m_cursorIndex < (m_in.getText().length()))
				m_cursorIndex++;
		} else if (key.getName().equals("UP")) {
			if (m_historyIndex < m_history.size() && m_history.size() > 0)
				m_in.previewLine(getHistory(++m_historyIndex));
		} else if (key.getName().equals("DOWN")) {
			if (m_historyIndex > -1)
				m_in.previewLine(getHistory(--m_historyIndex));
		}
	}

	@Override
	public void typed(char c) {
		if (c == '~') {
			m_show = !m_show;
			return;
		}
		if (m_show)
			m_in.characterTyped(c);
	}

	@Override
	public void renderWidget(Renderer renderer) {
		if (m_show) {
			String text = m_text.toString();
			String[] lines = text.split("\n");

			StringBuilder display = new StringBuilder();
			for (int i = Math.max(lines.length - m_maxLines, 0); i < lines.length; i++) {
				display.append(lines[i]);
				if (i != lines.length - 1)
					display.append('\n');
			}
			String currentLine = m_in.getText();

			String before = currentLine.substring(0, m_cursorIndex);
			String after = currentLine.substring(m_cursorIndex);
			currentLine = before + "|" + after;

			display.append(currentLine);

			renderer.setFont(m_font);
			//Draw multiline string
			String output = display.toString();
			String[] outputLines = output.split("\n");
			for (int i = 0; i < outputLines.length; i++) {
				int ypos = m_font.getHeight() * i + m_font.getAscent();
				
				String line  = outputLines[i];
				renderer.setColor(Color.WHITE);
				renderer.drawString(line, 0, ypos);
			}
		}
	}

	private String getHistory(int ago) {
		if (ago == -1)
			return m_in.getLine();
		else
			return m_history.get(ago);
	}

	public void start() {
		m_interpreter = new Interpreter(this);
		Thread thread = new Thread(m_interpreter);
		thread.start();
	}

	@Override
	public void error(Object o) {
		getErr().println(o);
	}

	@Override
	public PrintStream getErr() {
		return m_out;
	}

	@Override
	public Reader getIn() {
		return m_in;
	}

	@Override
	public PrintStream getOut() {
		return m_out;
	}

	@Override
	public void print(Object o) {
		getOut().print(o);
	}

	@Override
	public void println(Object o) {
		getOut().println(o);
	}

	public class ConsoleReader extends Reader {
		PipedOutputStream m_output = new PipedOutputStream();
		Reader m_reader;
		StringBuilder m_currentLine = new StringBuilder();
		String m_preview;

		public ConsoleReader() {
			try {
				InputStream input = new PipedInputStream(m_output);
				m_reader = new InputStreamReader(input);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		public void backspace() {
			setToPreview();
			if (m_currentLine.length() != 0) {
				m_currentLine.deleteCharAt(m_cursorIndex - 1);
				m_cursorIndex--;
			}
		}

		public void characterTyped(char c) {
			setToPreview();
			if (c == '\n') {
				flushToQueue();
				println(m_currentLine.toString());
				m_cursorIndex = 0;
				// Add the line to the last history
				m_history.add(0, getLine());
				m_currentLine = new StringBuilder();
			} else {
				m_currentLine.insert(m_cursorIndex, c);
				m_cursorIndex++;
			}
		}

		public void setCurrentLine(String line) {
			m_currentLine = new StringBuilder();
			m_currentLine.append(line);
			m_cursorIndex = m_currentLine.length();
		}

		public void flushToQueue() {
			PrintWriter writer = new PrintWriter(m_output);
			if (getLine().length() == 0)
				m_currentLine.append(';');
			writer.write(getLine());
			writer.flush();
		}

		public void setToPreview() {
			if (m_preview != null) {
				m_historyIndex = -1;
				// setCurrentLine() without the cursorIndex set
				m_currentLine = new StringBuilder();
				m_currentLine.append(m_preview);

				m_preview = null;
			}
		}

		public boolean isPreviewing() {
			return m_preview != null;
		}

		public String getPreview() {
			return m_preview;
		}

		public void previewLine(String line) {
			m_preview = line;
			// Put cursor at the end of the line
			m_cursorIndex = line.length();
		}

		public String getLine() {
			return m_currentLine.toString();
		}

		public String getText() {
			if (m_in.isPreviewing())
				return m_in.getPreview();
			else
				return m_in.getLine();
		}

		public int read() throws IOException {
			int read = m_reader.read();
			return read;
		}

		@Override
		public int read(char[] cbuf, int off, int len) throws IOException {
			int chars = m_reader.read(cbuf, off, len);
			return chars;
		}

		// Do nothing
		@Override
		public void close() throws IOException {
		}
	}

	public class ConsolePrintStream extends PrintStream {
		private Formatter m_formatter;

		public ConsolePrintStream() throws FileNotFoundException {
			super(new OutputStream() {
				@Override
				public void write(int b) throws IOException {
				}
			});
		}

		@Override
		public void print(boolean b) {
			write(b ? "true" : "false");
		}

		@Override
		public void print(char c) {
			write(String.valueOf(c));
		}

		@Override
		public void print(int i) {
			write(String.valueOf(i));
		}

		@Override
		public void print(long l) {
			write(String.valueOf(l));
		}

		@Override
		public void print(float f) {
			write(String.valueOf(f));
		}

		@Override
		public void print(double d) {
			write(String.valueOf(d));
		}

		@Override
		public void print(char s[]) {
			write(s);
		}

		@Override
		public void print(String s) {
			if (s == null) {
				s = "null";
			}
			write(s);
		}

		@Override
		public void print(Object obj) {
			write(String.valueOf(obj));
		}

		/* Methods that do terminate lines */

		@Override
		public void println() {
			newLine();
		}

		@Override
		public void println(boolean x) {
			synchronized (this) {
				print(x);
				newLine();
			}
		}

		@Override
		public void println(char x) {
			synchronized (this) {
				print(x);
				newLine();
			}
		}

		@Override
		public void println(int x) {
			synchronized (this) {
				print(x);
				newLine();
			}
		}

		@Override
		public void println(long x) {
			synchronized (this) {
				print(x);
				newLine();
			}
		}

		@Override
		public void println(float x) {
			synchronized (this) {
				print(x);
				newLine();
			}
		}

		@Override
		public void println(double x) {
			synchronized (this) {
				print(x);
				newLine();
			}
		}

		@Override
		public void println(char x[]) {
			synchronized (this) {
				print(x);
				newLine();
			}
		}

		@Override
		public void println(String x) {
			synchronized (this) {
				print(x);
				newLine();
			}
		}

		@Override
		public void println(Object x) {
			String s = String.valueOf(x);
			synchronized (this) {
				print(s);
				newLine();
			}
		}

		@Override
		public PrintStream printf(String format, Object... args) {
			return format(format, args);
		}

		@Override
		public PrintStream printf(Locale l, String format, Object... args) {
			return format(l, format, args);
		}

		@Override
		public PrintStream format(String format, Object... args) {
			synchronized (this) {
				if ((m_formatter == null) || (m_formatter.locale() != Locale.getDefault()))
					m_formatter = new Formatter((Appendable) this);
				m_formatter.format(Locale.getDefault(), format, args);
			}
			return this;
		}

		@Override
		public PrintStream format(Locale l, String format, Object... args) {
			synchronized (this) {
				if ((m_formatter == null) || (m_formatter.locale() != l))
					m_formatter = new Formatter(this, l);
				m_formatter.format(l, format, args);
			}
			return this;
		}

		@Override
		public PrintStream append(CharSequence csq) {
			if (csq == null)
				print("null");
			else
				print(csq.toString());
			return this;
		}

		@Override
		public PrintStream append(CharSequence csq, int start, int end) {
			CharSequence cs = (csq == null ? "null" : csq);
			write(cs.subSequence(start, end).toString());
			return this;
		}

		@Override
		public PrintStream append(char c) {
			print(c);
			return this;
		}

		public void write(boolean b) {
			write(Boolean.toString(b));
		}

		public void write(char c) {
			write(Character.toString(c));
		}

		public void write(char[] s) {
			write(new String(s));
		}

		public void write(double d) {
			write(Double.toString(d));
		}

		public void write(float f) {
			write(Float.toString(f));
		}

		public void write(int i) {
			write(Integer.toString(i));
		}

		public void write(long l) {
			write(Long.toString(l));
		}

		public void write(Object object) {
			write(object.toString());
		}

		public void write(String string) {
			m_text.append(string);
		}

		public void newLine() {
			m_text.append('\n');
		}
	}
}
