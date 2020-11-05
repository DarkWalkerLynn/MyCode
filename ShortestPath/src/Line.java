import java.util.ArrayList;
import java.util.List;

public class Line {
	private String lineName=null;
	private List<String> stations=new ArrayList<String>();
	
	public String getLineName() {
		return lineName;
	}
	public void setLineName(String lineName) {
		this.lineName = lineName;
	}
	public List<String> getStations() {
		return stations;
	}
	public void setStations(List<String> stations) {
		this.stations = stations;
	}
	public void addStation(String stationName) {
		stations.add(stationName);
	}
}
