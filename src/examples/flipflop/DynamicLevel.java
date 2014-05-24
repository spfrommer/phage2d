package examples.flipflop;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import utils.physics.Vector;
import engine.core.framework.Entity;
import engine.core.framework.component.type.TypeManager;
import engine.core.implementation.physics.logic.handler.ListenerCollisionHandlerLogic;

public class DynamicLevel implements Level {
	private List<Entity> m_balls;
	private List<Entity> m_portals;
	private List<Entity> m_platforms;

	public DynamicLevel() {
	}

	public void load(String name) {
		m_balls = new ArrayList<Entity>();
		m_portals = new ArrayList<Entity>();
		m_platforms = new ArrayList<Entity>();

		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = null;
		Document doc = null;
		try {
			dBuilder = dbFactory.newDocumentBuilder();
			doc = dBuilder.parse(new ByteArrayInputStream(name.getBytes()));
		} catch (ParserConfigurationException | SAXException | IOException e) {
			e.printStackTrace();
		}
		doc.getDocumentElement().normalize();

		NodeList ballList = doc.getElementsByTagName("ball");
		for (int temp = 0; temp < ballList.getLength(); temp++) {
			Node nNode = ballList.item(temp);

			if (nNode.getNodeType() == Node.ELEMENT_NODE) {
				Element eElement = (Element) nNode;
				Entity ball = EntityFactory.makeBall(new Vector(Double
						.parseDouble(eElement.getAttribute("x")), Double
						.parseDouble(eElement.getAttribute("y"))));
				m_balls.add(ball);
			}
		}

		NodeList portalList = doc.getElementsByTagName("portal");
		for (int temp = 0; temp < portalList.getLength(); temp++) {
			Node nNode = portalList.item(temp);

			if (nNode.getNodeType() == Node.ELEMENT_NODE) {
				Element eElement = (Element) nNode;
				Entity portal = EntityFactory.makePortal(new Vector(Double
						.parseDouble(eElement.getAttribute("x")), Double
						.parseDouble(eElement.getAttribute("y"))));
				m_portals.add(portal);
			}
		}

		NodeList platformList = doc.getElementsByTagName("platform");
		for (int temp = 0; temp < platformList.getLength(); temp++) {
			Node nNode = platformList.item(temp);

			if (nNode.getNodeType() == Node.ELEMENT_NODE) {
				Element eElement = (Element) nNode;
				Entity platform = EntityFactory.makePlatform(
						new Vector(Double.parseDouble(eElement
								.getAttribute("x")), Double
								.parseDouble(eElement.getAttribute("y"))),
						Double.parseDouble(eElement.getAttribute("width")),
						Double.parseDouble(eElement.getAttribute("height")));
				m_platforms.add(platform);
			}
		}
	}

	@Override
	public List<Entity> getBalls() {
		return m_balls;
	}

	@Override
	public List<Entity> getPortals(PortalManager manager) {
		for (Entity portal : m_portals) {
			ListenerCollisionHandlerLogic handler = (ListenerCollisionHandlerLogic) portal
					.getComponent(TypeManager
							.getType(ListenerCollisionHandlerLogic.class));
			handler.addListener(manager);
		}
		return m_portals;
	}

	@Override
	public List<Entity> getPlatforms() {
		return m_platforms;
	}

	public static void main(String[] args) {
		DynamicLevel level = new DynamicLevel();
		level.load(LevelReader.read("level1.lvl"));
	}
}
