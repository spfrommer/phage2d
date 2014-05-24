package engine.core.implementation.network.base.encoding;

import java.lang.reflect.Field;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import engine.core.framework.component.Component;

/**
 * Uses reflection to encode a DataComponent with primitive fields.
 */
public class BasicDataEncoder implements DataEncoder {
	@Override
	public Element encode(Component component, Document doc) {
		Element compElement = doc.createElement(component.getClass().getName());

		Field[] fields = component.getClass().getFields();
		for (Field field : fields) {
			Element fieldElement = doc.createElement(field.getName());
			Class<?> fieldType = field.getType();
			String typeName = fieldType.getName().toUpperCase().replace(".", "");
			FieldType type = FieldType.valueOf(typeName);
			try {
				switch (type) {
				case INT:
					fieldElement.appendChild(doc.createTextNode("" + field.getInt(component)));
					break;
				case DOUBLE:
					fieldElement.appendChild(doc.createTextNode("" + field.getDouble(component)));
					break;
				case BYTE:
					fieldElement.appendChild(doc.createTextNode("" + field.getByte(component)));
					break;
				case SHORT:
					fieldElement.appendChild(doc.createTextNode("" + field.getShort(component)));
					break;
				case LONG:
					fieldElement.appendChild(doc.createTextNode("" + field.getLong(component)));
					break;
				case FLOAT:
					fieldElement.appendChild(doc.createTextNode("" + field.getFloat(component)));
					break;
				case BOOLEAN:
					fieldElement.appendChild(doc.createTextNode("" + field.getBoolean(component)));
					break;
				case CHAR:
					fieldElement.appendChild(doc.createTextNode("" + field.getChar(component)));
					break;
				case JAVALANGSTRING:
					fieldElement.appendChild(doc.createTextNode("" + field.get(component)));
					break;
				default:
					throw new RuntimeException("No type for: " + typeName);
				}
			} catch (IllegalArgumentException | IllegalAccessException e) {
				e.printStackTrace();
			}
			compElement.appendChild(fieldElement);
		}
		return compElement;
	}

	private enum FieldType {
		INT, DOUBLE, BYTE, SHORT, LONG, FLOAT, BOOLEAN, CHAR, JAVALANGSTRING
	}
}
