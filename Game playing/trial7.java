import java.io.*;
import java.util.*;
import java.util.Map.Entry;

public class trial7 {
	
	public static int maxdepthsmall=3;
	public static int maxdepthlarge=2;
	public static ArrayList<String> getNeighbors(int i, int j, int x, int y, char[][] board,ArrayList<String> dummy) {

		int flagbottom=0;
		int flagtop=0;
		int flagleft=0;
		int flagright=0;
		int value = board[i][j];
		int count=1;
		boolean found=false;

		//board[i][j]='*';

		if(isCabin(i + 1, j, x, y))
		{
			int newvalue=board[i+1][j];
			if (value==newvalue)
			{
				dummy.add((i+1)+","+j);
				flagbottom=1;
				//board[i+1][j]='*';
				board[i][j]='*';
				found=true;
				getNeighbors(i+1, j, x, y, board,dummy);


			}
		}

		if(isCabin(i - 1, j, x, y))
		{
			int newvalue=board[i-1][j];
			if (value==newvalue)
			{
				dummy.add((i-1)+","+j);
				flagtop=1;
				//board[i-1][j]='*';
				board[i][j]='*';
				found=true;

				getNeighbors(i-1, j, x, y, board,dummy);
				//board[i-1][j]='*';
			}
		}

		if(isCabin(i, j-1, x, y))
		{
			int newvalue=board[i][j-1];
			if (value==newvalue)
			{
				dummy.add(i+","+(j-1));
				flagleft=1;
				//board[i][j-1]='*';
				board[i][j]='*';
				found=true;
				getNeighbors(i, j-1, x, y, board,dummy);
				//board[i][j-1]='*';
			}
		}

		if(isCabin(i , j+1, x, y))
		{
			int newvalue=board[i][j+1];
			if (value==newvalue)
			{
				dummy.add(i+","+(j+1));
				flagright=1;
				//board[i][j+1]='*';
				board[i][j]='*';
				found=true;
				getNeighbors(i, j+1, x, y, board,dummy);
				//board[i][j+1]='*';
			}
		}
		
		return dummy;

	}

	public static boolean isCabin(int i, int j, int x, int y) {
		boolean flag = false;
		if (i >= 0 && i < x && j >= 0 && j < y) {
			flag = true;
		}
		return flag; 
	}

	public static char[][] replaceasterisk(ArrayList<String> dummy, char board[][])
	{
		String[] rowcol=new String[2];
		int row,col=0;


		for(String elem:dummy)
		{

			rowcol=elem.split(",");
			row=Integer.parseInt(rowcol[0]);
			col=Integer.parseInt(rowcol[1]);
			board[row][col]='*';

		}
		return board;

	}

	public static Extract preprocess (String inputfile) throws FileNotFoundException
	{
		Scanner sc = new Scanner(new File(inputfile));
		Extract obj=null;
		int limit,fruits;
		float time;
		limit=sc.nextInt();
		fruits=sc.nextInt();
		time=sc.nextFloat();
		char[][] board = new char[limit][limit];;

		for(int i = 0; i < limit; i++)
		{
			if(sc.hasNextLine())
			{
				String line=sc.next();	            
				for(int j = 0; j < limit; j++)
				{
					board[i][j] = line.charAt(j);
				}
			}
		}

		obj = new Extract(limit,fruits,time,board);
		return obj;

	}

	public static char[][] gravity(char[][] replacedboard)
	{
		int count,swapvar;
		char temp;
		for(int j=0;j<replacedboard[0].length;j++)
		{
			count=replacedboard.length;
			while(count>=0)
			{
				swapvar=0;
				for(int i=0;i<replacedboard.length-1;i++)
				{
					if(Character.isDigit(replacedboard[i][j]) && replacedboard[i+1][j]=='*')
					{
						temp = replacedboard[i][j];
						replacedboard[i][j]=replacedboard[i+1][j];
						replacedboard[i+1][j]=temp;
						swapvar=1;
					}
				}
				count--;
			}
		}

		return replacedboard;
	}

	public static boolean termtest(char[][] gravityboard)
	{
		for(int i=0;i<gravityboard.length;i++)
			for(int j=0;j<gravityboard.length;j++)
				if(gravityboard[i][j]!='*')
					return false;

		return true;
	}

	public static HashSet<String> fruits(char[][] board)
	{
		HashSet<String> fruits = new HashSet<String>();
		for(int i=0;i<board.length;i++)
			for(int j=0;j<board.length;j++)
				if (board[i][j]!='*')
					fruits.add(i+","+j);
		return fruits;	
	}

	public static HashSet<String> asterisk(char[][] board)
	{
		HashSet<String> asterisk = new HashSet<String>();
		for(int i=0;i<board.length;i++)
			for(int j=0;j<board.length;j++)
				if (board[i][j]=='*')
					asterisk.add(i+","+j);
		return asterisk;	
	}

	public static void printBoard(char[][] board){
		for(int i=0;i<board.length;i++)
		{
			for(int j=0;j<board.length;j++)
				System.out.print(board[i][j]);
			System.out.println();
		}
	}

	public static Return1 mmab(char[][] gravityboard,int depth)
	{
		int alpha = Integer.MIN_VALUE;
		int beta = Integer.MAX_VALUE;

		char[][] supercopygravityboard=new char[gravityboard.length][gravityboard.length];
		for(int i=0;i<gravityboard.length;i++)
			for(int j=0;j<gravityboard.length;j++)
				supercopygravityboard[i][j]=gravityboard[i][j];
		
		
		char[][] copygravityboard=new char[gravityboard.length][gravityboard.length];
		for(int i=0;i<gravityboard.length;i++)
			for(int j=0;j<gravityboard.length;j++)
				copygravityboard[i][j]=gravityboard[i][j];

		String bestmove = null;
		int scoreret = 0;
		char[][] beststate=new char[gravityboard.length][gravityboard.length];
		int count=0;
		TreeMap<Integer,ArrayList<String>> tmap=new TreeMap<>(Collections.reverseOrder());
		HashMap<String, ArrayList<String>> hmap = new HashMap<>();

		for(int i=0;i<gravityboard.length;i++)
		{
			for(int j=0;j<gravityboard.length;j++)
			{
				//scoreret=0;
				if(gravityboard[i][j]!='*')
				{
					ArrayList<String> dummy1 = new ArrayList<>();
					dummy1.add(i+","+j);
//					System.out.println("dummy1"+dummy1);
					getNeighbors(i, j, gravityboard.length, gravityboard.length, gravityboard,dummy1);
					count=dummy1.size()*dummy1.size();
					hmap.put(i+","+j,dummy1);
//					System.out.println("hmap"+hmap);
					if (tmap.containsKey(count))
					{
						ArrayList<String> temp= tmap.get(count);
						temp.add(i+","+j);
						tmap.put(count,temp);
					}
					else
					{
						ArrayList<String> temp=new ArrayList<>();
						temp.add(i+","+j);
						tmap.put(count, temp);
					}
				}

			}
		}//preprocess ka for


//		System.out.println("tmap "+tmap);
		for(Entry<Integer, ArrayList<String>> entryy : tmap.entrySet())
		{
			ArrayList<String> ssw = entryy.getValue();
//			System.out.println("ssw "+ssw);
			for(String ssw1:ssw)
			{
				//		
				for(int ii=0;ii<gravityboard.length;ii++)
					for(int jj=0;jj<gravityboard.length;jj++)
						gravityboard[ii][jj]=copygravityboard[ii][jj];

				char[][]newboard1=new char[gravityboard.length][gravityboard.length];

				char[][]newboard2=new char[gravityboard.length][gravityboard.length];

				ArrayList<String> inner = new ArrayList<>();
				int count1=entryy.getKey();
				
				ArrayList<String> connected = hmap.get(ssw1);
				newboard1= replaceasterisk(connected, gravityboard);
				newboard2=gravity(newboard1);

//				System.out.println("newboard2 mmab---");
//				System.out.println("mmab "+count1);
//				System.out.println("current score mmab "+count1);
				//printBoard(newboard2);
				
				int util=minfn(newboard2,alpha,beta,count1,depth+1);

				//print util here
				//System.out.println("util mmab "+util);

				if (util>alpha)
				{
					alpha=util;
					bestmove=ssw1;
					//System.out.println("bestmove"+bestmove);
					beststate=newboard2;	
				}
			
			}
		}
		String[] bmrowcol=bestmove.split(",");
		ArrayList<String> something=new ArrayList<>();
//		something.add(bestmove);

		ArrayList<String> somethingg=new ArrayList<>();

		somethingg=getNeighbors(Integer.parseInt(bmrowcol[0]),Integer.parseInt(bmrowcol[1]), supercopygravityboard.length, supercopygravityboard.length, supercopygravityboard, something);
		somethingg.add(bestmove);
		//System.out.println("something"+somethingg);
		char[][] opboard1=replaceasterisk(somethingg, supercopygravityboard);
		char[][] opboard2=gravity(opboard1);
		
//		System.out.println();
		
		//ithe final board print maartiy
		printBoard(opboard2);
		
		Return1 objj=new Return1(bestmove, beststate, scoreret,opboard2);
		
		//ithe final bestmove print maartiy
		System.out.println(format(objj.bestmove));
		
		return objj;
	}

	public static int maxfn(char[][] someboard,int alpha,int beta,int squarescore,int depth)
	{
		int chosendepth;
		if (someboard.length<=5)
			chosendepth=maxdepthsmall;
		else
			chosendepth=maxdepthlarge;
		//System.out.println("enter max");
		
//		System.out.println(chosendepth);

		if (depth>=chosendepth || termtest(someboard))
		{
			//System.out.println("termtest maxfn");
			return squarescore;
		}
		int v=Integer.MIN_VALUE;
		char[][] copygravityboard=new char[someboard.length][someboard.length];
		for(int i=0;i<someboard.length;i++)
			for(int j=0;j<someboard.length;j++)
				copygravityboard[i][j]=someboard[i][j];

		String bestmove = null;
		int scoreret = 0;
		char[][] beststate=new char[someboard.length][someboard.length];
		int count=0;
		TreeMap<Integer,ArrayList<String>> tmap=new TreeMap<>(Collections.reverseOrder());
		HashMap<String, ArrayList<String>> hmap = new HashMap<>();

		for(int i=0;i<someboard.length;i++)
		{
			for(int j=0;j<someboard.length;j++)
			{
				//scoreret=0;
				if(someboard[i][j]!='*')
				{
					ArrayList<String> dummy1 = new ArrayList<>();
					dummy1.add(i+","+j);
//					System.out.println("dummy1"+dummy1);
					getNeighbors(i, j, someboard.length, someboard.length, someboard,dummy1);
					count=dummy1.size()*dummy1.size();
					hmap.put(i+","+j,dummy1);
//					System.out.println("hmap"+hmap);
					if (tmap.containsKey(count))
					{
						ArrayList<String> temp = tmap.get(count);
						temp.add(i+","+j);
						tmap.put(count,temp);
					}
					else
					{
						ArrayList<String> temp=new ArrayList<>();
						temp.add(i+","+j);
						tmap.put(count, temp);
					}
				}

			}
		}//preprocess ka for


//		System.out.println("tmap "+tmap);
		for(Entry<Integer, ArrayList<String>> entryy : tmap.entrySet())
		{
			int localscore = squarescore;
			ArrayList<String> ssw = entryy.getValue();
//			System.out.println("ssw "+ssw);
			for(String ssw1:ssw)
			{
				//		
				for(int ii=0;ii<someboard.length;ii++)
					for(int jj=0;jj<someboard.length;jj++)
						someboard[ii][jj]=copygravityboard[ii][jj];

				char[][]newboard1=new char[someboard.length][someboard.length];

				char[][]newboard2=new char[someboard.length][someboard.length];

				ArrayList<String> inner = new ArrayList<>();
				int count1=entryy.getKey();

				ArrayList<String> connected = hmap.get(ssw1);
				newboard1= replaceasterisk(connected, someboard);
				newboard2=gravity(newboard1);

				localscore += count1 ;
//				System.out.println("newboard2 maxfn---");
				//System.out.println("current score min "+localscore);
				//printBoard(newboard2);
				
				

				////print localscore here
				//System.out.println("localscore maxfn "+localscore);
//				System.out.println("maxfn "+localscore);
				v=Math.max(v, minfn(newboard2,alpha,beta,localscore,depth+1));
				//System.out.println("came here");
				if (v>=beta)
					return v;
				alpha=Math.max(alpha, v);

			}
		}

		return v;
	}


	public static int minfn(char[][] someboard,int alpha,int beta,int squarescore,int depth)
	{
//		System.out.println("herejj it should be 4 "+squarescore);
		//System.out.println("enter max");
		int chosendepth;
		if (someboard.length<=5)
			chosendepth=maxdepthsmall;
		else
			chosendepth=maxdepthlarge;
//		System.out.println(chosendepth);
		if (depth>=chosendepth || termtest(someboard))
		{
//			System.out.println("termtest minfn");
			return squarescore;
		}
		int v=Integer.MAX_VALUE;
		char[][] copygravityboard=new char[someboard.length][someboard.length];
		for(int i=0;i<someboard.length;i++)
			for(int j=0;j<someboard.length;j++)
				copygravityboard[i][j]=someboard[i][j];

		int count=0;
		TreeMap<Integer,ArrayList<String>> tmap=new TreeMap<>(Collections.reverseOrder());
		HashMap<String, ArrayList<String>> hmap = new HashMap<>();

		for(int i=0;i<someboard.length;i++)
		{
			for(int j=0;j<someboard.length;j++)
			{
				//scoreret=0;
				if(someboard[i][j]!='*')
				{
					ArrayList<String> dummy1 = new ArrayList<>();
					dummy1.add(i+","+j);
//					System.out.println("dummy1"+dummy1);
					getNeighbors(i, j, someboard.length, someboard.length, someboard,dummy1);
					count=dummy1.size()*dummy1.size();
					hmap.put(i+","+j,dummy1);
//					System.out.println("hmap"+hmap);
					if (tmap.containsKey(count))
					{
						ArrayList<String> temp=tmap.get(count);
						temp.add(i+","+j);
						tmap.put(count,temp);
					}
					else
					{
						ArrayList<String> temp=new ArrayList<>();
						temp.add(i+","+j);
						tmap.put(count, temp);
					}
				}

			}
		}//preprocess ka for


		//System.out.println("tmap "+tmap);
		for(Entry<Integer, ArrayList<String>> entryy : tmap.entrySet())
		{
			int localscore = squarescore;
//			System.out.println("copy of sq sc " + localscore);
			ArrayList<String> ssw = entryy.getValue();
//			System.out.println("ssw "+ssw);
			for(String ssw1:ssw)
			{
				//		
				for(int ii=0;ii<someboard.length;ii++)
					for(int jj=0;jj<someboard.length;jj++)
						someboard[ii][jj]=copygravityboard[ii][jj];

				char[][]newboard1=new char[someboard.length][someboard.length];

				char[][]newboard2=new char[someboard.length][someboard.length];

				ArrayList<String> inner = new ArrayList<>();
				int count1=entryy.getKey();

				ArrayList<String> connected = hmap.get(ssw1);
				newboard1= replaceasterisk(connected, someboard);
				newboard2=gravity(newboard1);
//				System.out.println("ye aa raha hai "+localscore);
//				System.out.println("Itna subtract karna hai "+count1);
				
				localscore -= count1 ;
//				System.out.println("newboard2 minfn---");
//				System.out.println("minfn "+localscore);
				//System.out.println("current score min "+localscore);
				//printBoard(newboard2);

				
				
				////print localscore here
				//System.out.println("localscore maxfn "+localscore);
				
				v=Math.min(v, maxfn(newboard2,alpha,beta,localscore,depth+1));
				if (v<=alpha)
					return v;
				beta=Math.min(beta, v);

			}
		}

		return v;
	}

	//to be tested yet
		public static String format(String chosen)
		{
			String chosenelem[]=chosen.split(",");
			String chosenrow=chosenelem[0];
			int chosenrow1=Integer.parseInt(chosenrow)+1;
			chosenrow=Integer.toString(chosenrow1);
			String chosencol=chosenelem[1];
			HashMap<String,String> formatting = new HashMap<String,String>();
			formatting.put("0", "A");formatting.put("1", "B");formatting.put("2", "C");formatting.put("3", "D");formatting.put("4", "E");formatting.put("5", "F");formatting.put("6", "G");formatting.put("7", "H");formatting.put("8", "I");formatting.put("9", "J");formatting.put("10", "K");formatting.put("11", "L");formatting.put("12", "M");formatting.put("13", "N");formatting.put("14", "O");formatting.put("15", "P");formatting.put("16", "Q");formatting.put("17", "R");formatting.put("18", "S");formatting.put("19", "T");formatting.put("20", "U");formatting.put("21", "V");formatting.put("22", "W");formatting.put("23", "X");formatting.put("24", "Y");formatting.put("25", "Z");

			String ans=formatting.get(chosencol)+""+chosenrow;
			return ans;

		}
		
		//to be tested
		public static void outputBoard(Return1 objj, String filename) throws IOException
		{
			BufferedWriter bw = new BufferedWriter(new FileWriter(filename));
			bw.write(format(objj.bestmove)+"\n");
			for (int i=0;i<objj.opboard2.length; i++)
			{
				for (int j=0;j<objj.opboard2.length; j++)
				{
					bw.write(objj.opboard2[i][j]);
				}
				bw.write("\n");
			}
			bw.close();
		}


	public static void main(String args[]) throws IOException
	{
		Extract obj=preprocess("ab2.txt");
		HashSet<String> dummy = new HashSet<String>();
		
//		for(int i=0;i<obj.board.length;i++)
//			for(int j=0;j<obj.board.length;j++)
//				System.out.println(obj.board[i][j]);

		Return1 objj=mmab(obj.board,0);
		outputBoard(objj,"output.txt");
		
	}


	}


class Extract
{
	int limit;
	int fruits;
	float time;
	char[][] board;

	public Extract(int limit, int fruits, float time, char[][] board)
	{
		this.limit=limit;
		this.fruits=fruits;
		this.time=time;
		this.board=board;
	}
}
class Return1
{
	String bestmove;
	char[][] beststate;
	int scoreret;
	char[][] opboard2;
	Return1(String bestmove,char[][] beststate,int scoreret,char[][] opboard2)
	{
		this.bestmove=bestmove;
		this.beststate=beststate;
		this.scoreret=scoreret;
		this.opboard2=opboard2;
	}
}
