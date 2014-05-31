package examples.flipflop.gui;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import utils.physics.Vector;
import engine.core.framework.Entity;
import engine.core.framework.component.type.TypeManager;
import engine.core.implementation.NameData;
import examples.flipflop.EntityFactory;
import examples.flipflop.FlipFlop;

public class Editor extends JFrame {
	private FlipFlop m_source;

	private ArrayList<EntityPanel> m_panels;

	{
		m_panels = new ArrayList<EntityPanel>();
	}

	public Editor(FlipFlop source) {
		super("Editor");
		m_source = source;

		JMenuBar bar = new JMenuBar();
		JMenu entity = new JMenu("Add Entity");
		JMenuItem ball = new JMenuItem("Ball");
		ball.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				m_source.addEntity((EntityFactory.makeBall(new Vector(0, 0))));
				loadSystem();
			}
		});
		JMenuItem portal = new JMenuItem("Portal");
		portal.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				m_source.addEntity(EntityFactory.makePortal(new Vector(0, 0), m_source.getPortalManager()));
				loadSystem();
			}
		});
		JMenuItem platform = new JMenuItem("Platform");
		platform.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				m_source.addEntity((EntityFactory.makePlatform(new Vector(0, 0), 50, 50)));
				loadSystem();
			}
		});
		JButton reset = new JButton("Reset");
		reset.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				for (EntityPanel panel : m_panels) {
					panel.inputPosition();
					panel.zeroVelocity();
				}
			}
		});
		entity.add(ball);
		entity.add(portal);
		entity.add(platform);
		bar.add(entity);
		bar.add(reset);
		this.setJMenuBar(bar);

		this.setSize(250, 800);
	}

	public void loadSystem() {
		this.getContentPane().removeAll();
		m_panels.clear();
		List<Entity> entities = m_source.getEntities();
		GridLayout layout = new GridLayout(entities.size(), 1);
		this.setLayout(layout);
		for (int i = 0; i < entities.size(); i++) {
			Entity e = entities.get(i);
			NameData name = (NameData) e.getComponent(TypeManager.getType(NameData.class));
			if (name.name.equals("portal") || name.name.equals("platform") || name.name.equals("ball")) {
				EntityPanel panel = new EntityPanel(e, m_source, this);
				m_panels.add(panel);
				this.add(panel);
			}
		}
		this.revalidate();
	}
}
