package engine.gui.condition;

import engine.gui.AnimationState;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public interface Condition {
	public boolean satisfies(AnimationState state);
	
	public static class StateCondition implements Condition {
		private String m_animation;
		public StateCondition(String animation) {
			m_animation = animation;
		}
		
		public boolean satisfies(AnimationState state) {
			return state.getState(m_animation);
		}
	}
	public static class InverseCondition implements Condition {
		private Condition m_condition;
		public InverseCondition(Condition c) {
			m_condition = c;
		}
		public boolean satisfies(AnimationState state) {
			return !m_condition.satisfies(state);
		}
	}
	public static class AndCondition implements Condition {
		private List<Condition> m_conditions = new ArrayList<Condition>();
		
		public AndCondition(Condition... conditions) {
			m_conditions.addAll(Arrays.asList(conditions));
		}
		
		public boolean satisfies(AnimationState state) {
			for (Condition c : m_conditions) {
				if (!c.satisfies(state)) return false;
			}
			return true;
		}
	}
	public static class OrCondition implements Condition {
		private List<Condition> m_conditions = new ArrayList<Condition>();
		
		public OrCondition(Condition... conditions) {
			m_conditions.addAll(Arrays.asList(conditions));
		}
		
		public boolean satisfies(AnimationState state) {
			for (Condition c : m_conditions) {
				if (c.satisfies(state)) return true;
			}
			return false;
		}
	}
	public static class ConditionParser {
		public static Condition s_parse(String condition) {
			if (condition.equals("")) {
				return new Condition() {
					public boolean satisfies(AnimationState state) { return true; }
				};
			}
			
			condition = condition.trim();
			String[] ands = condition.split("\\+");
			if (ands.length == 1) {
				String[] ors = condition.split("\\|");
				if (ors.length == 1) {
					if (condition.startsWith("!")) {
						return new InverseCondition(s_parse(condition.substring(1)));
					} else {
						return new StateCondition(condition);
					}
				} else {
					Condition[] cons = s_parse(ors);
					return new OrCondition(cons);
				}
			} else {
				Condition[] cons = s_parse(ands);
				return new AndCondition(cons);
			}
		}
		public static Condition[] s_parse(String[] conditions) {
			Condition[] con = new Condition[conditions.length];
			for (int i = 0; i < conditions.length; i++) {
				con[i] = s_parse(conditions[i]);
			}
			return con;
		}
		public static void main(String[] args) {
			Condition c = s_parse("hi + !foo");
			AnimationState state = new AnimationState();
			state.setState("hi", true);
			state.setState("foo", false);
			System.out.println(c.satisfies(state));
		}
	}
}
