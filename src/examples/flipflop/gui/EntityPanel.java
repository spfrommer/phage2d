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

	private JTextField xfield;
	private JTextField yfield;

	public EntityPanel(Entity entity, FlipFlop system, Editor editor) {
		m_entity = entity;
		m_system = system;
		m_editor = editor;

		GridLayout grid = new GridLayout(1, 3);
		this.setLayout(grid);

		m_name = (NameData) entity.getComponent(TypeManager
				.getType(NameData.class));

		JLabel nameLabel = new JLabel(m_name.name);
		this.add(nameLabel);

		JButton delete = new JButton("delete");
		delete.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				m_system.entityRemove.add(m_entity);
				m_editor.loadSystem();
			}
		});
		this.add(delete);

		m_physics = (PhysicsData) entity.getComponent(TypeManager
				.getType(PhysicsData.class));

		if (m_physics != null) {
			xfield = new JTextField("" + (int) m_physics.getPosition().getX());

			yfield = new JTextField("" + (int) m_physics.getPosition().getY());
			xfield.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					inputPosition();
				}
			});
			this.add(xfield);

			yfield.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					inputPosition();
				}
			});
			this.add(yfield);
		}
	}

	/**
	 * Takes coordinates from field and puts them into Entity.
	 */
	public void inputPosition() {
		m_physics.setPosition(new Vector(m_physics.getPosition().getX(), Double
				.parseDouble(yfield.getText())));
		m_physics.setPosition(new Vector(Double.parseDouble(xfield.getText()),
				m_physics.getPosition().getY()));
	}
}
