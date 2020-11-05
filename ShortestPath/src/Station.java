import java.util.ArrayList;
import java.util.List;

public class Station {
	private String stationName=null;
	private List<String> stationLine=new ArrayList<String>();//վ��������·
	private List<Station> stationLink=new ArrayList<Station>();//����վ��
	public Station pre=null;//ǰ���ڵ�
	public int dist;
	
	public String getStationName() {
		return stationName;
	}
	public void setStationName(String stationName) {
		this.stationName = stationName;
	}
	public List<String> getStationLine() {
		return stationLine;
	}
	public void setStationLine(List<String> stationLine) {
		this.stationLine = stationLine;
	}
	public void addStationLine(String line) {
		stationLine.add(line);
	}
	public List<Station> getStationLink() {
		return stationLink;
	}
	public void setStationLink(List<Station> stationLink) {
		this.stationLink = stationLink;
	}
	public void addStationLink(Station station) {
		stationLink.add(station);
	}
	public int getDist() {
		return dist;
	}
	public void setDist(int dist) {
		this.dist = dist;
	}
	public Station getPre() {
		return pre;
	}
	public void setPre(Station pre) {
		this.pre = pre;
	}
}
