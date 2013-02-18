package friendlab2;

import java.util.ArrayList;

public class Attribute {
	String attribute;
	int occurrenceCount, correspondingAttributeIndex;
	ArrayList<ResultingState> resultingStates;

	public Attribute(int index, String attribute) {
		this.correspondingAttributeIndex = index;
		this.attribute = attribute;
		this.occurrenceCount = 0;
		resultingStates = new ArrayList<ResultingState>();
	}

	public void incrementCounter(String attributeValue) {
		occurrenceCount++;
		boolean stateExists = false;
		for (int i = 0; i < resultingStates.size(); i++) {
			if (attributeValue.equals(resultingStates.get(i).getState())) {
				resultingStates.get(i).incrementCount();
				stateExists = true;
				break;
			}
		}
		if (!stateExists) {
			resultingStates.add(new ResultingState(attributeValue));
			resultingStates.get(resultingStates.size() - 1).incrementCount();
		}
	}

	public ArrayList<ResultingState> getStates() {
		return resultingStates;
	}

	public int getCount() {
		return occurrenceCount;
	}

	public int getIndex() {
		return correspondingAttributeIndex;
	}

	public String getName() {
		return attribute;
	}

	public class ResultingState {
		String stateName;
		int occurrenceCount;

		public ResultingState(String stateName) {
			this.stateName = stateName;
			occurrenceCount = 0;
		}

		public String getState() {
			return stateName;
		}

		public void setName(String stateName) {
			this.stateName = stateName;
		}

		public int getCount() {
			return occurrenceCount;
		}

		public void incrementCount() {
			occurrenceCount++;
		}

	}

}
