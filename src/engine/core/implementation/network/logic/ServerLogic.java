package engine.core.implementation.network.logic;

import java.util.ArrayList;

import utils.physics.Vector;
import engine.core.framework.Aspect;
import engine.core.framework.component.Component;
import engine.core.framework.component.type.TypeManager;
import engine.core.implementation.network.wrappers.EncoderWrapper;
import engine.core.implementation.physics.wrappers.TransformWrapper;
import engine.core.implementation.rendering.data.TextureData;
import engine.core.network.message.Message;
import engine.core.network.message.parameter.MessageParameter;

/**
 * Server logic that should be added to all server-side Entities that have to be synced across a network
 * 
 * @eng.dependencies TextureData, EncoderWrapper, TransformWrapper
 */
public class ServerLogic extends NetworkSyncLogic {
	private TextureData m_textureComponent;
	private EncoderWrapper m_encoderWrapper;
	private TransformWrapper m_transform;

	public ServerLogic() {
		super(new Aspect(TypeManager.getType(EncoderWrapper.class), TypeManager.getType(TextureData.class),
				TypeManager.getType(TransformWrapper.class)));
	}

	@Override
	public Message getCreationMessage() {
		String command = "addentity";
		MessageParameter[] parameters = new MessageParameter[1];
		parameters[0] = new MessageParameter(m_encoderWrapper.encode());

		return (new Message(command, parameters));
	}

	/**
	 * Returns the update message, null if there isn't one
	 * 
	 * @return
	 */
	private Vector m_lastPosition = new Vector(0, 0);
	private Vector m_lastVelocity = new Vector(0, 0);
	private double m_lastRotation = 0;
	private double m_lastTextureID = -1;

	double m_lastTimeStamp = System.currentTimeMillis();

	@Override
	public ArrayList<Message> getUpdateMessages() {
		ArrayList<Message> updates = new ArrayList<Message>();
		if (m_lastPosition.subtract(m_transform.getPosition()).length() > 0.01) {
			MessageParameter[] positionParameters = new MessageParameter[4];
			positionParameters[0] = new MessageParameter(getID());
			positionParameters[1] = new MessageParameter(0);
			positionParameters[2] = new MessageParameter(Math.floor(m_transform.getPosition().getX() * 10) / 10);
			positionParameters[3] = new MessageParameter(Math.floor(m_transform.getPosition().getY() * 10) / 10);
			updates.add(new Message("update", positionParameters));

			m_lastPosition = m_transform.getPosition().clone();
		}

		if (Math.abs(m_lastRotation - m_transform.getRotation()) > 0.01) {
			MessageParameter[] rotationParameters = new MessageParameter[3];
			rotationParameters[0] = new MessageParameter(getID());
			rotationParameters[1] = new MessageParameter(1);
			rotationParameters[2] = new MessageParameter(Math.floor(m_transform.getRotation() * 10) / 10);
			updates.add(new Message("update", rotationParameters));

			m_lastRotation = m_transform.getRotation();
		}
		if (m_lastTextureID == -1)
			m_lastTextureID = m_textureComponent.texture.getImageID();

		if (m_lastTextureID != m_textureComponent.texture.getImageID()) {
			MessageParameter[] textureParameters = new MessageParameter[3];
			textureParameters[0] = new MessageParameter(getID());
			textureParameters[1] = new MessageParameter(2);
			textureParameters[2] = new MessageParameter(m_textureComponent.texture.getImageID());
			updates.add(new Message("update", textureParameters));

			m_lastTextureID = m_textureComponent.texture.getImageID();
		}

		return updates;
	}

	@Override
	public Message getRemovalMessage() {
		String command = "removeentity";

		MessageParameter[] parameters = new MessageParameter[1];
		parameters[0] = new MessageParameter(getID());

		return (new Message(command, parameters));
	}

	@Override
	public void loadDependencies() {
		super.loadDependencies();
		m_encoderWrapper = (EncoderWrapper) this.loadDependency(TypeManager.getType(EncoderWrapper.class));
		m_textureComponent = (TextureData) this.loadDependency(TypeManager.getType(TextureData.class));
		m_transform = (TransformWrapper) this.loadDependency(TypeManager.getType(TransformWrapper.class));
	}

	@Override
	public void processMessage(Message update) {
		// Doesn't get any Messages from dummy terminal client
	}

	@Override
	public Component copy() {
		return new ServerLogic();
	}
}
