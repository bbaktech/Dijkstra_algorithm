import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
// Dijkstra.java
import java.util.Scanner;

import javax.swing.JFileChooser;

public class Dijkstra {
	
	static int Max_rooters;
	
	static int DistGrph[][] = null;
	static int DistRow[] = null;
	static int source = -1,destination = -1;
	class ConTableEntry {
		boolean flg; 
		int length;
		int []ids;
		int depth;
	}
	static ConTableEntry []conTable = null;
	
	
	public static void main(String[] args) {

		Dijkstra psFrame = new Dijkstra();

	}
	
	public int nodeid(int in){
		return (in+1);
	}
	public void ReadTextfileToBuildGraph() {
		try
		{
			//Config file must contain list of (ip-address and indx_server portno,download portno) pair for all the PEER Servers/Clients
			
			JFileChooser chooser = new JFileChooser();
		    int retval = chooser.showOpenDialog(null);
            if (retval == JFileChooser.APPROVE_OPTION) {
            	
	            File selectedFile = chooser.getSelectedFile();
	            String fiename = selectedFile.getAbsolutePath();
			
				FileReader fr = new FileReader(fiename);   
				String val=new String();
				//Creation of BufferedReader Object
				BufferedReader br = new BufferedReader(fr);	
				String[] temp;
				val=br.readLine();
	
				temp = val.split(" ");        //to find size of the matrics or number of rooters
				DistGrph = new int [temp.length][temp.length];
				//Close the BufferedReader Object 
				br.close();
				//Close FileReader Object
				fr.close();
				System.out.println("<=======:Graph  Sixe Red:=======>"+temp.length);
				
				fr = new FileReader(fiename);//Read the content in config file into the FileReader object    
				val=new String();
				//Creation of BufferedReader Object
				br = new BufferedReader(fr);	
				int i = 0;			
				while((val=br.readLine())!=null)
				{
					String[] temp1;
					temp1 = val.split(" ");
					for (int dist =0; dist < temp1.length; dist++ ){
						DistGrph[i][dist] =  Integer.parseInt(temp1[dist]);
					}
					i++;
				}
				//Close the BufferedReader Object 
				br.close();
				//Close FileReader Object
				fr.close();			
				System.out.println("<=======:Graph  Table Initialized:=======>");
				Max_rooters = i;
				
				
				String imageName = "%3d" ;
				System.out.println();
				System.out.print("ID|");
				for (int j =0; j < Max_rooters; j++ ){
					System.out.print(String.format( imageName,nodeid(j)));
				}
				System.out.println();
				System.out.println("-------------------------------------------------------------------");
				for (int j =0; j < Max_rooters; j++ ){
					imageName = "%2d|" ;
					System.out.print(String.format( imageName,nodeid(j)));
					imageName = "%3d" ;
					for (int k =0; k < Max_rooters; k++ )
						System.out.print( String.format( imageName, DistGrph[j][k]));
						//System.out.print(" "+DistGrph[j][k] +"  ");
					System.out.println();
				}
				System.out.println("-------------------------------------------------------------------");
          //  }
		} 
		}catch(Exception e){
			//e.printStackTrace();
			System.out.println("Could not read File");
		}
	}
	
	public void ComputeConnectionTabel(){
		conTable = new ConTableEntry[Max_rooters];
		
//		Initialise the working rows with -1 cost and -1 path  from source
		for (int j = 0;j<Max_rooters;j++) {	
			ConTableEntry ce = new ConTableEntry();
			ce.flg = true;
			ce.length = -1;
			ce.ids = new int[Max_rooters];
			ce.ids[0] = source ;
			ce.depth = 1;
			for (int i = 1;i<Max_rooters;i++) ce.ids[i] = -1;
			conTable[j] = ce;
		}
		
		
		//initializing source in working row 
		int tmpsorce = source;
		conTable[tmpsorce].length = 0;
		conTable[tmpsorce].ids[0]=source;
		conTable[tmpsorce].flg = false;
		int nodedepth = 1;
		
		for (int loopcnt = 0 ; loopcnt<Max_rooters; loopcnt++) {
			
//			System.out.println("Selected row:"+tmpsorce);
			
			for (int k = 0 ;  k< Max_rooters ; k++)
			{  
				//System.out.println("conTable[k].flg"+conTable[k].flg);
				
				if (conTable[k].flg)
				{
					//System.out.println("DistGrph[tmpsorce][k]"+DistGrph[tmpsorce][k]);
					if (DistGrph[tmpsorce][k]!= -1){
						if ((conTable[k].length != -1) ) {
							
							// smaller ( selected node length+ tableentry,previous entry path) 
						    if (conTable[k].length > conTable[tmpsorce].length + DistGrph[tmpsorce][k]) {
								conTable[k].length = conTable[tmpsorce].length + DistGrph[tmpsorce][k];
								for (int idx = 0; idx< conTable[tmpsorce].depth ;idx ++)
									conTable[k].ids[idx] = conTable[tmpsorce].ids[idx];
								conTable[k].depth = conTable[tmpsorce].depth ;								
								conTable[k].ids[conTable[k].depth] = k;
								conTable[k].depth++;
								conTable[k].ids[conTable[k].depth] = -1;
							}
						}
						else 
						{  //selected node length is added to length table entry for new length
							conTable[k].length = conTable[tmpsorce].length + DistGrph[tmpsorce][k];
							
							for (int idx = 0; idx< conTable[tmpsorce].depth ;idx ++)
								conTable[k].ids[idx] = conTable[tmpsorce].ids[idx];
							conTable[k].depth = conTable[tmpsorce].depth ;	
							
							conTable[k].ids[conTable[k].depth] = k;
							conTable[k].depth++;
							conTable[k].ids[conTable[k].depth] = -1;
							
						}
					}
			
				}
			}

			for (int i = 0; i<Max_rooters; i++){
//				System.out.print(conTable[i].length+ ",");
			}
//			System.out.println();

			//initalize smallest dist
			int small = 0;
			int indx_small = 0;
			for (int i = 0; i<Max_rooters; i++){
				if (conTable[i].flg){
					if(conTable[i].length !=-1 ){
						small = conTable[i].length;
						indx_small = i;
						break;
					}
				}				
			}
			//find source for next iteration
			for (int i = 0; i<Max_rooters; i++){
				if (conTable[i].flg){
					if(conTable[i].length != -1 ){
						if (small > conTable[i].length){						
							small = conTable[i].length;
							indx_small = i;
						}
					}
				}			
			}
//			System.out.println("indx :"+indx_small + " Val:"+small );
			tmpsorce = indx_small;
			conTable[tmpsorce].flg = false;
			
		}	
	
		System.out.println("Router [" + nodeid(source) + "] "+ "Connection Table:");
		System.out.println("Distination        Interface");
		for (int i = 0; i<Max_rooters; i++){
			System.out.print("      "+  nodeid(i) + "                "+ nodeid(conTable[i].ids[1]));
			System.out.println();
		}	
	}
	
	public void PrintConnectionTabel() {	
	//	System.out.println("Enter  Id< 0 - :"); 
		System.out.println("Enter Source Rooter Id< 1 - "+ (Max_rooters)+" >:"); 
		Scanner in1 = new Scanner(System.in);                       //Takes from the user the 4Digit Peer ID as input 
		String str_source = in1.nextLine();
		source = Integer.parseInt(str_source);
		source--;
		ComputeConnectionTabel();
	}
	
	public void PrintShortPathToDestination() {
		                    
		System.out.println("Enter Destination Rooter Id< 1 - "+ (Max_rooters)+" >:"); 
		Scanner in1 = new Scanner(System.in);                       //Takes from the user the 4Digit Peer ID as input 
		String str_dest = in1.nextLine();
		destination = Integer.parseInt(str_dest);
		destination--;
		System.out.print("Shortest Path from Rooter:["+nodeid(source) +"] to ["+ nodeid(destination) + "] is ");
		for (int n = 0;n<Max_rooters;n++ ) {
			if (-1 != conTable[destination].ids[n]) System.out.print(" "+ nodeid(conTable[destination].ids[n]));
		}
		System.out.println();
		System.out.println("The total cost is "+ conTable[destination].length);
	}
	public void ChangeNetworkTopology(){
		
		System.out.println("Enter Rooter Id< 1 - "+ (Max_rooters)+" > to Delete:");                       
		Scanner in1 = new Scanner(System.in);                       //Takes from the user the 4Digit Peer ID as input 
		String str_delt = in1.nextLine();
		int delid = Integer.parseInt(str_delt);
		delid--;

		for (int j =delid; j < Max_rooters-1; j++ ){
		//	System.out.print(" R["+j+"] ");
			for (int k = 0; k < Max_rooters; k++ )
				DistGrph[j][k] = DistGrph[j+1][k] ;
		}
		for (int l =delid; l < Max_rooters-1; l++ ){
			//System.out.print(" R["+j+"] ");
			for (int k = 0; k < Max_rooters; k++ )
				DistGrph[k][l] = DistGrph[k][l+1] ;
		}
		Max_rooters--;
		System.out.println("Modified Topology:");
		
		//insert
		String imageName = "%3d" ;
		System.out.println();
		System.out.print("ID|");
		for (int j =0; j < Max_rooters; j++ ){
			System.out.print(String.format( imageName,nodeid(j)));
		}
		System.out.println();
		System.out.println("-------------------------------------------------------------------");
		for (int j =0; j < Max_rooters; j++ ){
			imageName = "%2d|" ;
			System.out.print(String.format( imageName,nodeid(j)));
			imageName = "%3d" ;
			for (int k =0; k < Max_rooters; k++ )
				System.out.print( String.format( imageName, DistGrph[j][k]));
				//System.out.print(" "+DistGrph[j][k] +"  ");
			System.out.println();
		}
		System.out.println("-------------------------------------------------------------------");
		
	}
	
	public Dijkstra() {
		
		while (true){
			//  System.out.println("\n");
			System.out.println("===========================================================\n");
			System.out.println("Enter The Option :\n==================\n1. Create a Network Topology\n \n2. Build a Connection Table \n \n3. Shortest Path to Destination Router \n \n4. Modify a topology \n \n5. Exit\n");	
			System.out.println("Command:");
			Scanner in = new Scanner(System.in);
			String regmessage = in.nextLine();

			if (regmessage.equals("1")){				
				ReadTextfileToBuildGraph();                         
			}				
			if (regmessage.equals("2")){
				PrintConnectionTabel();                           
			}		
			if (regmessage.equals("3")){
				PrintShortPathToDestination();                       
			}		
			if (regmessage.equals("4")){
				ChangeNetworkTopology();
				if ((source >-1) && (source < Max_rooters)){ 
					ComputeConnectionTabel();
					if ((destination >-1) && (destination < Max_rooters)){
						System.out.print("Shortest Path from Rooter:["+nodeid(source) +"] to ["+ nodeid(destination) + "] is ");
						for (int n = 0;n<Max_rooters;n++ ) {
							if (-1 != conTable[destination].ids[n]) System.out.print(" "+ nodeid(conTable[destination].ids[n]));
						}
						System.out.println();
						System.out.println("The total cost is "+ conTable[destination].length);
					}
					else System.out.println("Destination node is not selected");
				}
				else System.out.println("Source node is not selected");
	
			}

			if (regmessage.equals("5")){				
				System.out.println("Exit CS542 project. Good Bye!.");
				System.exit(0);   		
			}	
		}
	}
}