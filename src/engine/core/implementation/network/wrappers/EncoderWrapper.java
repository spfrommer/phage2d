package engine.core.implementation.network.wrappers;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import engine.core.framework.component.Component;
import engine.core.framework.component.DataComponent;
import engine.core.framework.component.WrapperComponent;
import engine.core.framework.component.type.ComponentType;
import engine.core.implementation.network.base.encoding.BlankEncoder;
import engine.core.implementation.network.base.encoding.DataEncoder;

/**
 * A Wrapper that combines various Components into an Entity XML Document. Needed by ServerLogic to send Entities to the
 * ClientLogic.
 * 
 * @eng.dependencies varies
 */
public class EncoderWrapper extends WrapperComponent {
	/**
	 * Defines which DataComponents it should load when added into an Entity.
	 */
	private Map<ComponentType, DataEncoder> m_dataTypes;

	/**
	 * Contains a Map of DataComponents and the appropriate DataEncoders that need a reference to the DataComponent to
	 * encode it.
	 */
	private Map<DataComponent, DataEncoder> m_dataEncoders;

	/**
	 * Contains all the Wrapper and Logic Components that this encoded Entity should have. These don't need to be linked
	 * to any specific DataComponents because these dependencies will be rebuilt when the Entity is constructed from the
	 * XML.
	 */
	private List<BlankEncoder> m_blankEncoders;

	{
		m_dataTypes = new HashMap<ComponentType, DataEncoder>();

		m_dataEncoders = new HashMap<DataComponent, DataEncoder>();
		m_blankEncoders = new ArrayList<BlankEncoder>();
	}

	public EncoderWrapper() {
		super();
	}

	/**
	 * Defines a DataComponent that should be loaded when added to an Entity, as well as the DataEncoder to encode this
	 * Component. If the EncoderWrapper cannot find find this DataComponent, it will be omitted.
	 * 
	 * @param comp
	 * @param encoder
	 */
	public void addDataEncoder(ComponentType comp, DataEncoder encoder) {
		m_dataTypes.put(comp, encoder);
	}

	/**
	 * Adds either a Wrapper or Logic Component to be added to the XML.
	 * 
	 * @param encoder
	 */
	public void addBlankEncoder(BlankEncoder encoder) {
		m_blankEncoders.add(encoder);
	}

	/**
	 * Generates an XML of the Entity, with random DataComponents first followed by DependentComponents in whatever
	 * order the user specified.
	 * 
	 * @return
	 */
	public String encode() {
		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = null;
		try {
			docBuilder = docFactory.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}

		Document doc = docBuilder.newDocument();

		Element rootElement = doc.createElement("entity");
		doc.appendChild(rootElement);

		for (Component comp : m_dataEncoders.keySet()) {
			Element element = m_dataEncoders.get(comp).encode(comp, doc);
			rootElement.appendChild(element);
		}

		for (BlankEncoder encoder : m_blankEncoders) {
			Element element = encoder.encode(doc);
			rootElement.appendChild(element);
		}

		return docToString(doc);
	}

	@Override
	public void loadDependencies() {
		for (ComponentType type : m_dataTypes.keySet()) {
			if (this.getEntity().hasComponent(type)) {
				Component comp = this.loadDependency(type);
				m_dataEncoders.put((DataComponent) comp, m_dataTypes.get(type));
			}
		}
	}

	@Override
	public Component copy() {
		EncoderWrapper logic = new EncoderWrapper();
		for (ComponentType type : m_dataTypes.keySet()) {
			logic.addDataEncoder(type, m_dataTypes.get(type));
		}

		for (BlankEncoder encoder : m_blankEncoders)
			logic.addBlankEncoder(encoder);

		return logic;
	}

	private static String docToString(Document doc) {
		try {
			StringWriter sw = new StringWriter();
			TransformerFactory tf = TransformerFactory.newInstance();
			Transformer transformer = tf.newTransformer();
			transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
			transformer.setOutputProperty(OutputKeys.METHOD, "xml");
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");

			transformer.transform(new DOMSource(doc), new StreamResult(sw));
			return sw.toString();
		} catch (TransformerException e) {
			e.printStackTrace();
			return null;
		}
	}
}
