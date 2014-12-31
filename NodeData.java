import java.util.ArrayList;


public class NodeData {
	private int NodeID;
	private int Statu = 0;
	private ArrayList<Integer> NearNodes = new ArrayList<Integer>();
	
	public NodeData(int nodeid, ArrayList<Integer> nearnodes) {
		this.NodeID = nodeid;
		this.NearNodes = nearnodes;
    }
	
	public NodeData() {
		
    }
	
	public int getNodeID() {
        return NodeID;
    }
  
    public void setStatu(int statu) {
        this.Statu = statu;
    }
    public int getStatu() {
        return Statu;
    }
  
    public void setNodeID(int nodeid) {
        this.NodeID = nodeid;
    }
    
    public ArrayList<Integer> getNearNodes() {
        return NearNodes;
    }
  
    public void setNearNodes(ArrayList<Integer> nearnodes) {
        this.NearNodes = nearnodes;
    }
    
    public int NearNodes_num() {
        return NearNodes.size();
    }
}
