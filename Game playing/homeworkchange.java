
import java.io.*;
import java.util.*;
import java.lang.*;

public class homework{

	public static void preprocess(String inputfile) throws FileNotFoundException {
	    Scanner sc = new Scanner(new File(inputfile));
	    
	    //by default write fail
	    BufferedWriter bw = null;
		FileWriter fw = null;

		try {

			fw = new FileWriter("output.txt");
			bw = new BufferedWriter(fw);
			
			bw.write("FAIL");
			
		} catch (IOException e) {

			e.printStackTrace();

		} finally {

			try {

				if (bw != null)
					bw.close();

				if (fw != null)
					fw.close();

			} catch (IOException ex) {

				ex.printStackTrace();
			}
		}

	    String algo="";
	    algo = sc.nextLine();
	    int count = 0;
	    String[] values=null;
	    int n=Integer.valueOf(sc.nextLine());
	    int p=Integer.valueOf(sc.nextLine());
	    int matrix[][]=new int[n][n];
	    
	    //read input
	    for(int i = 0; i < n; i++)
	    {
	        if(sc.hasNextLine())
	        {
	            String line=sc.nextLine();	            
	            for(int j = 0; j < n; j++)
	            {
	                matrix[i][j] = Character.getNumericValue(line.charAt(j));
	            }
	        }
	    }
	    
	    //storing singlevalue for sorting convenience
	    int singlevalue;
	    TreeSet<Integer> statespace=new TreeSet<Integer>();
	    for(int i=0;i<n;i++)
	    {
	        for(int j=0;j<n;j++)
	        {
	            if (matrix[i][j]==0)
	            {
	            	singlevalue=(i*n)+j;
	            statespace.add(singlevalue);
	            }
	        }
	    }
	    
	   int treecount=0;
	   
	   HashMap<Integer,Integer> trees = new HashMap<Integer,Integer>();
	   int totaltreecount=0;
	   int[] cumulative=new int[n];
	   int cumu=0;
	   
	   //keep track of trees
	   for(int i=0;i<n;i++)
	    {
		   cumulative[i]=cumu;
		   treecount=0;
	        for(int j=0;j<n;j++)
	        {
	            if (matrix[i][j]==2)
	            {
	            	cumulative[i]=cumu+1;
	            	cumu++;
	            	treecount++;
	            	totaltreecount++;
	            }
	        }
        	trees.put(i, treecount); 
	    }

	   TreeSet<Integer> marked = new TreeSet<Integer>();

		   boolean answer = dfs(n,p,matrix,statespace,marked,trees,totaltreecount,cumulative);
	   
	   //writing to a file for OK
	   if (answer==true)
	   {
		    bw = null;
			 fw = null;

			try {

				fw = new FileWriter("output.txt");
				bw = new BufferedWriter(fw);
				
				bw.write("OK");
				bw.write("\n");
				for (int change:marked)
					matrix[change/n][change%n]=1;
				for (int i=0;i<n;i++){
					for(int j=0;j<n;j++)
						bw.write(Integer.toString(matrix[i][j]));
					bw.write("\n");
				}
				
			} catch (IOException e) {

				e.printStackTrace();

			} finally {

				try {

					if (bw != null)
						bw.close();

					if (fw != null)
						fw.close();

				} catch (IOException ex) {

					ex.printStackTrace();
				}
			}

	   }
	   
	   //writing to a file for FAIL
	   else 
	   {
		    bw = null;
			 fw = null;

			try {

				String content = "FAIL";

				fw = new FileWriter("output.txt");
				bw = new BufferedWriter(fw);
				bw.write(content);
				
			} catch (IOException e) {

				e.printStackTrace();

			} finally {

				try {

					if (bw != null)
						bw.close();

					if (fw != null)
						fw.close();

				} catch (IOException ex) {

					ex.printStackTrace();
				}
			}

	   }
	   
	   
	   }
	
public static boolean dfs(int n, int p, int matrix[][], TreeSet<Integer> statespace, TreeSet<Integer> marked, HashMap trees,int totaltreecount, int[] cumulative)
{
	//if all lizards placed
	if (marked.size() == p)
		return true;
	
	//if lizards are more than emptyspaces
	if (p>n+totaltreecount)
		return false;
	
	//filling lizards in nursery
	for (int position : statespace)
	{		
		int lizardsleft=p-marked.size()-1;
		
		//optimize by keeping lizards less than what can fit in remaining
		if (position/n!=0)
		if (((n-(position/n))+(totaltreecount-cumulative[position/n]))<lizardsleft)
		{
			return false;
		}
		
		TreeSet<Integer> eliminate= new TreeSet<Integer>();
		
		marked.add(position); 
		
		//retrieving row,col from singlevalue
		int row=position/n; 
		int col=position%n; 
		int singlevalue=0;
		
		//top
		for (int top=row-1;top>=0;top--)
		{
			if (matrix[top][col]==2)
				break;
			singlevalue=(top*n)+col;
			eliminate.add(singlevalue);
		}
		
		//bottom
		for (int bottom=row+1;bottom<n;bottom++)
		{
			if (matrix[bottom][col]==2)
				break;
				
			singlevalue=(bottom*n)+col;
			eliminate.add(singlevalue);
		}
		
		//right
		for (int right=col+1;right<n;right++)
		{
			if (matrix[row][right]==2)
				break;
			singlevalue=(row*n)+right;
			eliminate.add(singlevalue);
		}
		
		//left
		for (int left=col-1;left>=0;left--)
		{
			if (matrix[row][left]==2)
				break;
				
			singlevalue=(row*n)+left;
			eliminate.add(singlevalue);
		}
		
		//topright
		for(int topright1=row-1,topright2=col+1;topright1>=0 && topright2<n;topright1--,topright2++)
		{
			if (matrix[topright1][topright2]==2)
				break;
			
			singlevalue=(topright1*n)+topright2;
			eliminate.add(singlevalue);
		}
		
		//topleft
		for(int topleft1=row-1,topleft2=col-1;topleft1>=0 && topleft2>=0;topleft1--,topleft2--)
		{
			if (matrix[topleft1][topleft2]==2)
				break;			
			singlevalue=(topleft1*n)+topleft2;
			eliminate.add(singlevalue);
		}
		
		//bottomright
		for (int bottomright1=row+1,bottomright2=col+1;bottomright1<n && bottomright2<n;bottomright1++,bottomright2++)
		{
			if (matrix[bottomright1][bottomright2]==2)
				break;			
			singlevalue=(bottomright1*n)+bottomright2;
			eliminate.add(singlevalue);
			}
		
		//bottomleft
		for (int bottomleft1=row+1,bottomleft2=col-1;bottomleft1<n && bottomleft2>=0;bottomleft1++,bottomleft2--)
		{
			if (matrix[bottomleft1][bottomleft2]==2)
				break;
			singlevalue=(bottomleft1*n)+bottomleft2;
			eliminate.add(singlevalue);
		}
		
		eliminate.addAll(marked);
		
				
		TreeSet<Integer> statespaceMutate = new TreeSet<Integer>(statespace);
		statespaceMutate.removeAll(eliminate);
		
		//recursion
		boolean result = dfs(n,p,matrix,statespaceMutate,marked,trees,totaltreecount,cumulative);
		
		//expand further downwards
		if (result==true)
			return true;
		//backtrack
		else
			marked.remove(position);
	}

	return false;	
}	
	   	
public static void main(String args[]) throws IOException
{
	preprocess("src/input.txt");
}
}


