import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Scanner;

public class AllMethod {
	final int MaxNum=1000;
	final int MaxValue=100000;
	
	public List<Station> stations=new ArrayList<Station>();
	public List<Line> lines=new ArrayList<Line>();
	public List<Station> shortestPath=new ArrayList<Station>();
	public File file=null;
	public Station startStation=new Station();
	public Station endStation=new Station();
	
	public AllMethod() {
		readFile();//读取文件
		dataInit();//数据初始化
		printLines();//输出所有地铁线路
		inputStation();//输入起始站及终点站
		buildPath(endStation);//计算最短路线
		printShortestLine();//输出最短路径
	}
	
	public void readFile() {
		file=new File("station.txt");
	}
	
	public void dataInit() {
		try {
			InputStreamReader  read=new InputStreamReader(new FileInputStream(file),"UTF-8");
			BufferedReader reader=new BufferedReader(read);
			String stationName=null;
			
			while((stationName=reader.readLine())!=null) {//一行为一条线路
				int j;
				Station lastStation=null;
				String line_station[]=stationName.split(" ");
				Line line=new Line();
				line.setLineName(line_station[0]);//线路名字为第一个读入的字符串
				for(int i=1;i<line_station.length;i++) {//从第二个读入的字符串之后，都是站点名
					line.addStation(line_station[i]);//将站点加入线路
					for(j=0;j<stations.size();j++) {
						if(stations.get(j).getStationName().equals(line_station[i])) {//判断车站是否已存在
							stations.get(j).addStationLine(line.getLineName());
							if(i!=1) {
								stations.get(j).addStationLink(lastStation);
								lastStation.addStationLink(stations.get(j));
							}
							lastStation=stations.get(j);
							break;
						}
					}
					if(j==stations.size()) {//车站不存在
						Station station=new Station();
						station.addStationLine(line_station[0]);
						station.setStationName(line_station[i]);
						station.dist=MaxValue;//?
						if(i!=1) {
							station.addStationLink(lastStation);
							lastStation.addStationLink(station);
						}
						stations.add(station);
						lastStation=station;
					}
				}
				lines.add(line);
			}
		}catch(UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}catch(FileNotFoundException e2) {
			e2.printStackTrace();
		}catch(IOException e3) {
			e3.printStackTrace();
		}
	}
	
	public void printLines() {
		for(int i=0;i<lines.size();i++) {
			Line line=lines.get(i);
			System.out.print(line.getLineName());
			for(int j=0;j<line.getStations().size();j++) {
				System.out.print("\t"+line.getStations().get(j));
			}
			System.out.println();
		}
	}
	
	public void inputStation() {
		Scanner input=new Scanner(System.in);
		int index;
		String start=null;
		String end=null;
		System.out.println("\n站点间最短路径查询\n");
		do {
			System.out.print("请输入起始站：");
			start=input.nextLine().replace(" ", "");
			//startStation.setStationName(input.nextLine().replace(" ", ""));
		} while (!((index=isExist(start))<MaxNum));
		startStation=stations.get(index);
		bfsShortestPath(startStation);
		do {
			System.out.print("请输入终点站：");
			end=input.nextLine().replace(" ", "");
			//endStation.setStationName(input.nextLine().replace(" ", ""));
		} while (!((index=isExist(end))<MaxNum));
		endStation=stations.get(index);
		//System.out.println("起始站："+start);
		//System.out.println("终点站："+end);
		
		
	}
	
	public int isExist(String stationSE) {
		for(int i=0;i<stations.size();i++)
		{
			if(stations.get(i).getStationName().equals(stationSE)) {
				return i;
			}
		}
		System.out.println("站点不存在！请重新输入");
		return MaxNum;
	}
	
	public void bfsShortestPath(Station station) {
		Queue<Station> queStation=new LinkedList<>();
		station.dist=0;
		queStation.offer(station);
		
		while(!queStation.isEmpty()) {
			Station s=queStation.poll();
			for(int i=0;i<s.getStationLink().size();i++) {
				if(s.getStationLink().get(i).getDist()==MaxValue) {
					s.getStationLink().get(i).setDist(s.dist+1);
					queStation.offer(s.getStationLink().get(i));
					s.getStationLink().get(i).setPre(s);
				}
			}
		}
	}
	
	public void buildPath(Station theEnd) {
		if(theEnd.getPre()!=null) {
			buildPath(theEnd.getPre());
		}
		shortestPath.add(theEnd);
	}
	
	public void printShortestLine() {
		String changeLine=getSameLine(shortestPath.get(0), shortestPath.get(1));
		System.out.println("\n"+changeLine+":");
		for(int i=0;i<shortestPath.size();i++) {
			if(i>=2) {
				Station station1,station2;
				station1=shortestPath.get(i);
				station2=shortestPath.get(i-1);
				if(!getSameLine(station1, station2).equals(changeLine)) {
					changeLine=getSameLine(shortestPath.get(i), shortestPath.get(i-1));
					System.out.println("\n换乘  "+changeLine+":");
					System.out.print(shortestPath.get(i).getStationName());
					continue;
				}
			}
			if(i!=0) {
				System.out.print(" --> ");
			}
			System.out.print(shortestPath.get(i).getStationName());
		}
	}
	
	private String getSameLine(Station station1,Station station2) {
		for(int i=0;i<station1.getStationLine().size();i++) {
			for(int j=0;j<station2.getStationLine().size();j++) {
				if(station1.getStationLine().get(i).equals(station2.getStationLine().get(j))) {
					return station1.getStationLine().get(i);
				}
			}
		}
		
		return null;
	}
}
