
public class MatchSort {
	private String[][] namesData;
	private String[][] scoresData;
	private String[] timeData;
	
	public void sortData (String[][] _namesData, String[][] _scoresData, String[] _timeData) {
		if (namesData==null) {
			namesData=_namesData;
			scoresData=_scoresData;
			timeData=_timeData;
			return;
		}
		
		for (int i=0; i<_namesData.length; i++) {
			System.out.println(i);
			for (int x=0; x<namesData.length; x++) {
				if (_namesData[i][0].equals(namesData[x][0])) {
					scoresData[x][0] = _scoresData[i][0];
					scoresData[x][1] = _scoresData[i][1];
					timeData[x] = _timeData[i];
					break;
				}
			}
		}
	}
	
	public String[][] getSortedNames() {
		return namesData;
	}

	public String[][] getSortedScores() {
		return scoresData;
	}

	public String[] getSortedTime() {
		return timeData;
	}

}
