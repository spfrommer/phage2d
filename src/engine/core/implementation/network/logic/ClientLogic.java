package engine.core.implementation.network.logic;

import java.util.ArrayList;

import utils.physics.Vector;
import engine.core.framework.Aspect;
import engine.core.framework.component.Component;
import engine.core.framework.component.type.TypeManager;
import engine.core.implementation.physics.data.PhysicsShellData;
import engine.core.implementation.rendering.data.TextureData;
import engine.core.network.message.Message;

/**
 * Basic client logic that's used to receive updates from a server.
 * 
 * @eng.dependencies PhysicsShellData, TextureData
 */
public class ClientLogic extends NetworkSyncLogic {
	private PhysicsShellData m_shellComponent;
	private TextureData m_textureComponent;

	public ClientLogic() {
		super(new Aspect(TypeManager.getType(PhysicsShellData.class), TypeManager.getType(TextureData.class)));
	}

	@Override
	public Message getCreationMessage() {
		// There should be no creation message
		return null;
	}

	@Override
	public Message getRemovalMessage() {
		// There should be no removal message
		return null;
	}

	@Override
	public ArrayList<Message> getUpdateMessages() {
		return new ArrayList<Message>();
	}

	@Override
	public void processMessage(Message update) {
		int type = update.getParameters()[1].getIntValue();
		if (type == 0) {
			m_shellComponent.position = new Vector(update.getParameters()[2].getDoubleValue(),
					update.getParameters()[3].getDoubleValue());
		}
		if (type == 1) {
			m_shellComponent.rotation = update.getParameters()[2].getDoubleValue();
		}
		if (type == 2) {
			m_textureComponent.texture.setImageID(update.getParameters()[2].getIntValue());
		}
	}

	@Override
	public void loadDependencies() {
		super.loadDependencies();
		m_shellComponent = (PhysicsShellData) this.loadDependency(TypeManager.getType(PhysicsShellData.class));
		m_textureComponent = (TextureData) this.loadDependency(TypeManager.getType(TextureData.class));
	}

	@Override
	public Component copy() {
		return new ClientLogic();
	}
}
