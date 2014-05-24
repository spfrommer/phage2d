package engine.core.implementation.network.logic;

import java.util.ArrayList;

import utils.physics.Vector;
import engine.core.framework.Aspect;
import engine.core.framework.Entity;
import engine.core.framework.component.Component;
import engine.core.framework.component.type.TypeManager;
import engine.core.implementation.network.wrappers.EncoderWrapper;
import engine.core.implementation.physics.wrappers.PositionWrapper;
import engine.core.implementation.physics.wrappers.RotationWrapper;
import engine.core.implementation.physics.wrappers.VelocityWrapper;
import engine.core.implementation.rendering.data.TextureData;
import engine.core.network.message.Message;
import engine.core.network.message.parameter.MessageParameter;

/**
 * Server logic that should be added to all server-side Entities that have to be synced across a network
 * 
 * @eng.dependencies TextureData, EncoderWrapper, PositionWrapper, RotationWrapper, VelocityWrapper
 */
public class ServerLogic extends NetworkSyncLogic {
	private TextureData m_textureComponent;
	private EncoderWrapper m_encoderWrapper;
	private PositionWrapper m_position;
	private RotationWrapper m_rotation;
	private VelocityWrapper m_velocity;

	public ServerLogic() {
		super(new Aspect(TypeManager.getType(EncoderWrapper.class), TypeManager.getType(TextureData.class),
				TypeManager.getType(PositionWrapper.class), TypeManager.getType(RotationWrapper.class),
				TypeManager.getType(VelocityWrapper.class)));
	}

	public ServerLogic(Entity parent) {
		super(parent, new Aspect(TypeManager.getType(EncoderWrapper.class), TypeManager.getType(TextureData.class),
				TypeManager.getType(PositionWrapper.class), TypeManager.getType(RotationWrapper.class),
				TypeManager.getType(VelocityWrapper.class)));
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

	@Override
	public ArrayList<Message> getUpdateMessages() {
		ArrayList<Message> updates = new ArrayList<Message>();
		if (m_lastPosition.subtract(m_position.getPosition()).length() > 0.5) {
			MessageParameter[] positionParameters = new MessageParameter[4];
			positionParameters[0] = new MessageParameter(getID());
			positionParameters[1] = new MessageParameter(0);
			positionParameters[2] = new MessageParameter(m_position.getPosition().getX());
			positionParameters[3] = new MessageParameter(m_position.getPosition().getY());
			updates.add(new Message("update", positionParameters));

			m_lastPosition = m_position.getPosition().clone();
		}

		if (Math.abs(m_lastRotation - m_rotation.getRotation()) > 0.5) {
			MessageParameter[] rotationParameters = new MessageParameter[3];
			rotationParameters[0] = new MessageParameter(getID());
			rotationParameters[1] = new MessageParameter(1);
			rotationParameters[2] = new MessageParameter(m_rotation.getRotation());
			updates.add(new Message("update", rotationParameters));

			m_lastRotation = m_rotation.getRotation();
		}

		if (m_lastVelocity.subtract(m_velocity.getVelocity()).length() > 0.5) {
			MessageParameter[] velocityParameters = new MessageParameter[4];
			velocityParameters[0] = new MessageParameter(getID());
			velocityParameters[1] = new MessageParameter(2);
			velocityParameters[2] = new MessageParameter(m_velocity.getVelocity().getX());
			velocityParameters[3] = new MessageParameter(m_velocity.getVelocity().getY());

			updates.add(new Message("update", velocityParameters));
		}

		if (m_lastTextureID == -1)
			m_lastTextureID = m_textureComponent.texture.getImageID();

		if (m_lastTextureID != m_textureComponent.texture.getImageID()) {
			MessageParameter[] textureParameters = new MessageParameter[3];
			textureParameters[0] = new MessageParameter(getID());
			textureParameters[1] = new MessageParameter(3);
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
		m_position = (PositionWrapper) this.loadDependency(TypeManager.getType(PositionWrapper.class));
		m_rotation = (RotationWrapper) this.loadDependency(TypeManager.getType(RotationWrapper.class));
		m_velocity = (VelocityWrapper) this.loadDependency(TypeManager.getType(VelocityWrapper.class));
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
