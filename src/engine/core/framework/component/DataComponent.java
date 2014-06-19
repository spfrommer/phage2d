package engine.core.framework.component;


/**
 * A Component that only carries data as a public instance variable or as a private variable with getters and setters.
 * This data will then be used by LogicComponents.
 */
public abstract class DataComponent extends Component {
	public DataComponent() {
		super();
	}
}
