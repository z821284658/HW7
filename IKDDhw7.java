import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;


public class IKDDhw7
{
  // Data members
	int numNodes;
	ArrayList<NodeData> Nodes = new ArrayList<NodeData>();
	ArrayList<NodeData> Top10_Nodes = new ArrayList<NodeData>();
	int[] Top10_mainnodes = new int[10];
	
	
	
  // Constructor; loads expression data from file <fileName>. The Nodes arraylist is
  // filled with the genes from the dataset. 
  public IKDDhw7(String fileName) 
  {
    BufferedReader reader;
    int Pre_NodeID = 0;
    int Cur_NodeID = 0;
    ArrayList<Integer> NearNodes = new ArrayList<Integer>();
    
    NodeData node = new NodeData();
    String line;
    
    String[] splitLine = null;
    
    // Creates a new KMeans object by reading in all of the genes
    // and expression levels located in filename
    try 
    {
      reader = new BufferedReader(new FileReader(fileName));
      
      // Get each nodeID and their nearnodes and add to Nodes arraylist
      for(numNodes = 0; (line = reader.readLine( )) != null;numNodes++) {
    	  splitLine = line.split( "\t" );
    	  
    	  if(numNodes == 0) {
    		  NearNodes = new ArrayList<Integer>();
    		  Cur_NodeID = Integer.parseInt(splitLine[0]);
    		  NearNodes.add(Integer.parseInt(splitLine[1]));
        	  Pre_NodeID = Cur_NodeID;
        	  
    	  }else {
    		  Cur_NodeID = Integer.parseInt(splitLine[0]);
    		  if(Cur_NodeID == Pre_NodeID) {
    			  NearNodes.add(Integer.parseInt(splitLine[1]));
    		  }else {
    			  Collections.sort(NearNodes,new Comparator<Integer>() {
    				  public int compare(Integer o1, Integer o2) {
    				               return o1 - o2;
    				  }
    			  });
    			  node = new NodeData(Pre_NodeID , NearNodes);
    			  Nodes.add(node);
    			  NearNodes = new ArrayList<Integer>();
            	  NearNodes.add(Integer.parseInt(splitLine[1]));
            	  
    		  }
    		  Pre_NodeID = Cur_NodeID;
    	  }
      };
      
      // add the final nodedata to Nodes arraylist
      node = new NodeData(Pre_NodeID , NearNodes);
	  
	  Nodes.add(node);
      
      // Close
      reader.close();
	  
      // Sort Nodes by NodeID
      Collections.sort(Nodes,new Comparator<NodeData>() {
		  public int compare(NodeData o1, NodeData o2) {
		               return o1.getNodeID() - o2.getNodeID();
		  }
	  });
      
      // Count the totalnodes_num
      numNodes = Cur_NodeID + 1;
     
    }
    catch(FileNotFoundException e) 
    {
      System.out.println( "ERROR:  Unable to locate " + fileName + "." );
      System.exit( 0 ); 
    }
    catch(IOException e) 
    {
      System.out.println( "ERROR:  Unable to read from " + fileName + "." );
      System.exit( 0 );
    } 
  }
  
  // Compute all Connected Graph in database and Get the Top10 subgraph and
  // let the node which have the max degree in the subgraph be mainnode of the subgraph
  // the top10 nodes is the set containing each mainnode of Top10 subgraph
  // By Greedy method
  private void Greedy_Top10() { 
	  ArrayList<NodeData> Top10_arrset = new ArrayList<NodeData>();
	  int top = 0;
	  for(int i = 0; i < Nodes.size(); i++) {
		  
			  if(Nodes.get(i).getStatu() == 0) {
				  ArrayList<Integer> actived_arrset = Influence_function(i);
				  
				  Top10_arrset.add(new NodeData(top,actived_arrset));
				  
				  top++;
			  }
	  }
	  Collections.sort(Top10_arrset,new Comparator<NodeData>() {
			public int compare(NodeData o1, NodeData o2) {
			             return o2.NearNodes_num() - o1.NearNodes_num();
			}
		});
	  for(int i = 0; i < 10; i++) {
		  Top10_Nodes.add(Top10_arrset.get(i));
		  
	  }
	  int i = 0;
	  for(NodeData tmp: Top10_Nodes) {
		  int maxdegree_node = 0;
		  int maxdegree = 0;
		  
		  for(int node:tmp.getNearNodes()) {
			  if(Nodes.get(node).NearNodes_num() >= maxdegree) {
				  maxdegree_node = node;
				  maxdegree = Nodes.get(node).NearNodes_num();
				  
			  }
			  
		  }
		 // System.out.print(maxdegree_node+",   ");
		  Top10_mainnodes[i] = maxdegree_node;
		  i++;
	  }
	  
  }
  
  // Function to compute Connected Graph
  private ArrayList<Integer> Influence_function(int node) { 
	  boolean check_change = false;
	  int Pre_actived_num = 0;
	  int Cur_actived_num = 1;
	  Set<Integer> actived_set = new HashSet<>();
	  ArrayList<Integer> actived_arrset = new ArrayList<Integer>();
	  Nodes.get(node).setStatu(1);
	  actived_set.add(node);
	  
	  for(int i = 0; i < Cur_actived_num; i++) {
		if(i == 0) {
			for(int nearnode: Nodes.get(node).getNearNodes()) {
				if(Nodes.get(nearnode).getStatu() == 0) {
					Nodes.get(nearnode).setStatu(1);
					actived_set.add(nearnode);
				}
				
			}
			
		}else {
			for(int nearnode: Nodes.get(actived_arrset.get(i)).getNearNodes()) {
				if(Nodes.get(nearnode).getStatu() == 0) {
					Nodes.get(nearnode).setStatu(1);
					actived_set.add(nearnode);
				}
			}	
		}
		actived_arrset = new ArrayList(actived_set);
		Cur_actived_num = actived_arrset.size();
		if(Pre_actived_num != Cur_actived_num) {
			Collections.sort(actived_arrset,new Comparator<Integer>() {
				public int compare(Integer o1, Integer o2) {
				             return o1 - o2;
				}
			});
			
		}
		Pre_actived_num = Cur_actived_num;
	  }
	  return actived_arrset;
  }
  
  // Function to Evaluation Top10_nodes influence
  // return the influence value
  private int Evaluation_influence_Greedy_Top10(double probability) {
	  
	  double prob = probability;
	  double random_num = 0.0;
	  ArrayList<Integer> All_Actived_nodes = new ArrayList<Integer>();
	  ArrayList<Integer> Pre_Activeing_nodes = new ArrayList<Integer>();
	  ArrayList<Integer> Cur_Activeing_nodes = new ArrayList<Integer>();
	  for(NodeData tmp:Nodes) {
		  tmp.setStatu(2);
	  }
	  
	  for(int i = 0; i < 10; i++) {
		  Pre_Activeing_nodes.add(Top10_mainnodes[i]);
		  Nodes.get(Top10_mainnodes[i]).setStatu(3);
		  All_Actived_nodes.add(Top10_mainnodes[i]);
	  }
	  
	  do {
		  random_num = Math.random();
		  for(int activeing_node: Pre_Activeing_nodes) {
			  for(int may_active_node: Nodes.get(activeing_node).getNearNodes()) {
				  if(random_num >= prob) {
					  if(Nodes.get(may_active_node).getStatu() == 2) {
						  Nodes.get(may_active_node).setStatu(3);
						  Cur_Activeing_nodes.add(may_active_node);
						  All_Actived_nodes.add(may_active_node);
					  }else {
						  
					  }
					  
				  }else {
					  Nodes.get(may_active_node).setStatu(4);
					  
				  }
			  }
		  }
		  Pre_Activeing_nodes = Cur_Activeing_nodes;
		  Cur_Activeing_nodes = new ArrayList<Integer>();
	  }while(!Pre_Activeing_nodes.isEmpty());
	  
	  
	  
	  
	  
	  return All_Actived_nodes.size();
  }  
  
  
  // Main method. Run this program using the following command.
  // java IKDDhw7 <input_data_filename> <probability>/default(0.1) <Iteration_num>/default(1000)
  //
  // This program will print out the top10_influence_node found by greedy method in Independent Cascade model
  // and will also compute influence f(S) by Monte Carlo simulation, the number of iterations is 1000 as default,
  // propagation probability p = 0.1 as default
  
  public static void main( String[] astrArgs ) throws IOException 
  {
    // TODO
	  int Iteration_num = 1000;
	  double Total_Influence = 0.0;
	  double Influence = 0.0;
	  double Prob = 0.1;
	  // Check the parameter inputed by user
	  if(astrArgs.length >= 2) {
		  Prob = Double.parseDouble(astrArgs[1]); 
	  }
	  if(astrArgs.length >= 3) {
		  Iteration_num = Integer.parseInt(astrArgs[2]); 
	  }
	  
	  // loads expression data from file <fileName>
	  IKDDhw7 KM = new IKDDhw7( astrArgs[0]); 
	  
	  // Get the Top10 nodes by greedy
	  KM.Greedy_Top10();
	  
	  // Evaluation total influence f(S) by Monte Carlo simulation, the number of iterations is 1000
	  for(int i = 0; i < Iteration_num; i++) {
		 Influence = KM.Evaluation_influence_Greedy_Top10( Prob );
		 Total_Influence += Influence;
		 //System.out.println("\n"+Influence);
	  } 
	  
	  //Output result to console
	  System.out.print("Top 10 seed set: ");
	  for(int i = 0; i < 10; i++) {
		  if(i != 9) {
			  System.out.print(KM.Top10_mainnodes[i]+",");
		  }else {
			  System.out.print(KM.Top10_mainnodes[i]);
		  } 
	  } 
	  System.out.println("\nAverge influence f(S) = "+Total_Influence/1000);
	  
  }
}
