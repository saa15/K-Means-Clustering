import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Random;
import java.util.Scanner;

public class KMeans {
	static ArrayList<DataPoint> dataset = null;
	static DataPoint means[] = null;

	public static void main(String[] args) throws IOException {
		DecimalFormat df = new DecimalFormat("0.00");
		DataPoint dp = null;
		dataset = new ArrayList<DataPoint>();
		File f = new File(args[1]);
		BufferedReader br = new BufferedReader(new FileReader(f));
		br.readLine();
		String singleLine = br.readLine();

		// Creating a data set from the file
		while (singleLine != null) {
			String[] points = singleLine.split("	");
			dp = new DataPoint(Float.parseFloat(points[1]), Float.parseFloat(points[2]));
			dataset.add(dp);
			singleLine = br.readLine();
		}
		/*System.out.println("Enter K value:");
		Scanner sc = new Scanner(System.in);*/

		// Initializing means - Choose a randomPoint
		means = new DataPoint[Integer.parseInt(args[0])];
		//means = new DataPoint[sc.nextInt()];
		int[] initPoints = new int[means.length];
		for(int init=0;init<initPoints.length;init++){
			initPoints[init] = -1;
		}
		Random r = new Random();
		for (int i = 0; i < means.length; i++) {
			int randomint = r.nextInt(dataset.size());
			while(randomContains(initPoints,randomint))
				randomint = r.nextInt(dataset.size());
			initPoints[i] = randomint;
			means[i] = dataset.get(randomint);
		}
		
		for (int p = 0; p < dataset.size(); p++) {
			boolean flag = false;
			int i = 0;
			for (i = 0; i < dataset.size(); i++) {
				flag = findCluster(dataset.get(i), means);
			}
			//System.out.println(flag);
			if(flag==true)
				updateMeans();
			else if(flag==false)
				break;
		}
		//System.out.println("p value="+p);
		StringBuilder sb = new StringBuilder();
		sb.append("SSE = "+df.format(calculateSSE())+"\n");
		sb.append(printClusters());
		System.out.println(sb.toString());
		PrintWriter writer = new PrintWriter(args[2], "UTF-8");
		writer.println(sb.toString());
		writer.close();
	}

	private static double calculateSSE() {
		double sse = 0;
		for(DataPoint dp:dataset){
			double dist = findDistance(dp,means[dp.getCluster()]);
			sse = sse + dist*dist;
		}
		return sse;
	}

	private static boolean randomContains(int[] initPoints, int randomint) {
		boolean flag = false;
		for(int x:initPoints){
			if(x==randomint)
				flag = true;
		}
		return flag;
	}

	private static String printClusters() {
		int i = 0;
		int k = 0;
		LinkedList[] clusters = new LinkedList[means.length];
		for (DataPoint dp : dataset) {
			k = dp.getCluster();
			if(clusters[k]==null)
				clusters[k] = new LinkedList();
			 clusters[k].add(i);
			 i++;
		}
		i=1;
		StringBuilder sb = new StringBuilder();
		for(LinkedList l:clusters){
			sb.append(i+"  ");
			Iterator it = l.iterator();
			while(it.hasNext()){
				sb.append(((Integer)it.next()+1));
				if(it.hasNext())
					sb.append(",");
			}
			i++;
			sb.append("\n");
		}
		return sb.toString();
	}

	private static void updateMeans() {
		int k = 0;
		double sumsX[] = new double[means.length];
		double sumsY[] = new double[means.length];
		int[] count = new int[means.length];
		DataPoint dp = null;
		for (int i = 0; i < dataset.size(); i++) {
			dp = dataset.get(i);
			k = dp.getCluster(); // cluster no
			sumsX[k] = sumsX[k] + dp.getX();
			sumsY[k] = sumsY[k] + dp.getY();
			count[k]++;
		}
		for (int j = 0; j < means.length; j++) {
			means[j] = new DataPoint((sumsX[j] / count[j]), (sumsY[j] / count[j]));
		}

	}

	private static boolean findCluster(DataPoint dataPoint, DataPoint[] means) {
		boolean flag = false;
		double dist = Double.MAX_VALUE;
		for (int i = 0; i < means.length; i++) {
			double clust_dist = findDistance(dataPoint, means[i]);
			if (dist > clust_dist){
				flag = true;
				dist = clust_dist;
				dataPoint.setCluster(i);
			}
		}	
		return flag;
	}

	private static double findDistance(DataPoint dataPoint, DataPoint means2) {
		double x = Math.pow(dataPoint.getX() - means2.getX(), 2);
		double y = Math.pow(dataPoint.getY() - means2.getY(), 2);
		return Math.sqrt(x + y);
	}
}
