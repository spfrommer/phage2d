package engine.core.network.message.parameter;

public enum ParameterType {
	INT(0), DOUBLE(1), STRING(2), NULL(3) /*End of Transmision*/;

	private final int val;

	private ParameterType(int v) {
		val = v;
	}

	public int intValue() {
		return val;
	}
}
