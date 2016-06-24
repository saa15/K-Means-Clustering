/*
 Each point in the dataset has 2 attributes. 
 Each point is considered as an object with its x-y coordinates 
 and the cluster that it belongs to as the attributes.
 */
public class DataPoint {
	double x;
	double y;
	int cluster = 0;
	public int getCluster() {
		return cluster;
	}
	public void setCluster(int cluster) {
		this.cluster = cluster;
	}
	DataPoint(double x,double y){
		this.x = x;
		this.y = y;
		this.cluster = cluster;
	}
	
	public double getX() {
		return x;
	}
	public void setX(float x) {
		this.x = x;
	}
	public double getY() {
		return y;
	}
	public void setY(float y) {
		this.y = y;
	}
	
	
}
