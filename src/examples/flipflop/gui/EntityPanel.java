package examples.flipflop.gui;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import utils.physics.Vector;
import engine.core.framework.Entity;
import engine.core.framework.component.type.TypeManager;
import engine.core.implementation.NameData;
import engine.core.implementation.physics.data.PhysicsData;
import examples.flipflop.FlipFlop;

public class EntityPanel extends JPanel {
	private Entity m_entity;
	private FlipFlop m_system;
	private Editor m_editor;
	private NameData m_name;
	private PhysicsData m_physics;

	private JTextField m_xfield;
	private JTextField m_yfield;

	public EntityPanel(Entity entity, FlipFlop system, Editor editor) {
		m_entity = entity;
		m_system = system;
		m_editor = editor;

		GridLayout grid = new GridLayout(1, 3);
		this.setLayout(grid);

		m_name = (NameData) entity.getComponent(TypeManager.typeOf(NameData.class));

		JLabel nameLabel = new JLabel(m_name.name);
		this.add(nameLabel);

		JButton delete = new JButton("delete");
		delete.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				m_system.removeEntity(m_entity);
				m_editor.loadSystem();
			}
		});
		this.add(delete);

		m_physics = (PhysicsData) entity.getComponent(TypeManager.typeOf(PhysicsData.class));

		if (m_physics != null) {
			m_xfield = new JTextField("" + (int) m_physics.getPosition().getX());

			m_yfield = new JTextField("" + (int) m_physics.getPosition().getY());
			m_xfield.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					inputPosition();
				}
			});
			this.add(m_xfield);

			m_yfield.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					inputPosition();
				}
			});
			this.add(m_yfield);
		}
	}

	/**
	 * Takes coordinates from field and puts them into Entity.
	 */
	public void inputPosition() {
		if (!m_system.getEntities().contains(m_entity))
			m_system.addEntity(m_entity);

		m_physics.setPosition(new Vector(m_physics.getPosition().getX(), Double.parseDouble(m_yfield.getText())));
		m_physics.setPosition(new Vector(Double.parseDouble(m_xfield.getText()), m_physics.getPosition().getY()));
	}

	public void zeroVelocity() {
		m_physics.setVelocity(new Vector(0, 0));
	}
}
