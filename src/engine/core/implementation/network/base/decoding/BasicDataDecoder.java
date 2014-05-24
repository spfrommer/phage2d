package engine.core.implementation.network.base.decoding;

import java.lang.reflect.Field;

import engine.core.framework.component.DataComponent;

/**
 * A Decoder that can only handle DataComponents that contain public primitive fields and Strings - works using
 * Reflection.
 */
public class BasicDataDecoder implements DataDecoder {

	@Override
	public void decode(DataComponent component, String[] data) {
		Class<? extends DataComponent> compClass = component.getClass();

		// fills in all public fields with data in order
		Field[] fields = compClass.getFields();
		for (int i = 0; i < fields.length; i++) {
			Field field = fields[i];
			Class<?> fieldType = field.getType();
			String typeName = fieldType.getName().toUpperCase().replace(".", "");
			FieldType type = FieldType.valueOf(typeName);
			try {
				switch (type) {
				case INT:
					field.setInt(component, Integer.parseInt(data[i]));
					break;
				case DOUBLE:
					field.setDouble(component, Double.parseDouble(data[i]));
					break;
				case BYTE:
					field.setByte(component, Byte.parseByte(data[i]));
					break;
				case SHORT:
					field.setShort(component, Short.parseShort(data[i]));
					break;
				case LONG:
					field.setLong(component, Long.parseLong(data[i]));
					break;
				case FLOAT:
					field.setFloat(component, Float.parseFloat(data[i]));
					break;
				case BOOLEAN:
					field.setBoolean(component, Boolean.parseBoolean(data[i]));
					break;
				case CHAR:
					field.setChar(component, data[i].charAt(0));
					break;
				case JAVALANGSTRING:
					field.set(component, data[i]);
					break;
				default:
					throw new RuntimeException("No type for: " + typeName);
				}
			} catch (IllegalArgumentException | IllegalAccessException e) {
				e.printStackTrace();
			}
		}
	}

	private enum FieldType {
		INT, DOUBLE, BYTE, SHORT, LONG, FLOAT, BOOLEAN, CHAR, JAVALANGSTRING
	}
}
