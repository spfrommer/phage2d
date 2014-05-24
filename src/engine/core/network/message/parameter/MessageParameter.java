package engine.core.network.message.parameter;

public class MessageParameter {
	private ParameterType m_type;

	// one or the other
	private int m_intValue = -1;
	private double m_doubleValue = -1;
	private String m_stringValue = "";

	public MessageParameter() {
		m_type = ParameterType.NULL;
	}

	public MessageParameter(int intValue) {
		m_intValue = intValue;
		m_type = ParameterType.INT;
	}

	public MessageParameter(double doubleValue) {
		setValue(doubleValue);
		m_type = ParameterType.DOUBLE;
	}

	public MessageParameter(String stringValue) {
		setValue(stringValue);
		m_type = ParameterType.STRING;
	}

	public void setValue(int number) {
		m_intValue = number;
		m_type = ParameterType.INT;
	}

	public void setValue(double number) {
		m_doubleValue = number;
		m_type = ParameterType.DOUBLE;
	}

	public void setValue(String string) {
		m_stringValue = string;
		m_type = ParameterType.STRING;
	}

	public void nullValue() {
		m_type = ParameterType.NULL;
	}

	public ParameterType getType() {
		return m_type;
	}

	public int getIntValue() {
		return m_intValue;
	}

	public double getDoubleValue() {
		return m_doubleValue;
	}

	public String getStringValue() {
		return m_stringValue;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append(m_type + ":");
		if (m_type == ParameterType.INT)
			builder.append(getIntValue());
		if (m_type == ParameterType.DOUBLE)
			builder.append(getDoubleValue());
		if (m_type == ParameterType.STRING)
			builder.append(getStringValue());
		if (m_type == ParameterType.NULL)
			builder.append("null");
		if (m_type == null)
			builder.append("No Value");

		return builder.toString();
	}
}
