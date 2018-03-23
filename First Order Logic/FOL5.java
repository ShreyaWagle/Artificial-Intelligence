import java.io.*;
import java.util.*;

public class FOL5 {
	
	public static Reading read(String filename) throws FileNotFoundException
	{
		Scanner sc = new Scanner(new File(filename));
		Reading obj=null;
		int query;
		query=sc.nextInt();
		sc.nextLine();
		String[] queries=new String[query];
		for(int i = 0; i < query; i++)
		{
			if(sc.hasNextLine())
			{
				queries[i]=sc.next();
			}
		}
		int orig;
		orig = sc.nextInt();
		sc.nextLine();
		String[] kb=new String[orig];
		for(int i = 0; i < orig; i++)
		{
			if(sc.hasNextLine())
			{
				kb[i]=sc.nextLine();
			}
		}
		obj = new Reading(query,queries,orig,kb);
		sc.close();
		return obj;
	}

	public static HashMap<String,ArrayList<ArrayList<MyClass>>> buildkb(int orig, String[] kb)
	{
		HashMap<String,ArrayList<ArrayList<String>>> hmap = new HashMap<>();
		for(int i=0;i<orig;i++)
		{
			String line = kb[i]; 		
			String[] split = line.split("\\|"); 
			for(int ff=0;ff<split.length;ff++)
				split[ff]=split[ff].trim();
			for(String splitted:split) 			
			{
				String pred=splitted.substring(0,splitted.indexOf('('));
				if (!hmap.containsKey(pred))
				{
					ArrayList<ArrayList<String>> linemod = new ArrayList<>();
					linemod.add(new ArrayList<>(Arrays.asList(split)));
					hmap.put(pred,linemod);
				}
				else
				{
					ArrayList<ArrayList<String>> linemod = hmap.get(pred);
					linemod.add(new ArrayList<>(Arrays.asList(split)));
					hmap.put(pred, linemod);
				}
			}
		}
		
		ArrayList<ArrayList<String>> hvalue = new ArrayList<>();
		HashMap<String,ArrayList<ArrayList<MyClass>>> hhmap= new HashMap<>();
		
		for(String hkey:hmap.keySet())
		{
			ArrayList<ArrayList<MyClass>> put2 = new ArrayList<ArrayList<MyClass>>();
			ArrayList<MyClass> put1 = new ArrayList<>();
			hvalue = new ArrayList<>(hmap.get(hkey));
			for(ArrayList<String> say1 : hvalue)
			{
				put1 = new ArrayList<>();
				for (String say2: say1)
				{
					MyClass obj1=new MyClass();
					obj1=simplify(say2);
					put1.add(obj1);	
				}
				put2.add(put1);
			}
			hhmap.put(hkey, put2);	
		}
		return hhmap;
	}
	
	public static MyClass simplify(String hvalue)
	{
		MyClass obj2=new MyClass();
		String some3[] = hvalue.split("\\|");
		for(int ff=0;ff<some3.length;ff++)
			some3[ff]=some3[ff].trim();
		

		for(String some4:some3)
			{
				obj2.pred=some4.substring(0,some4.indexOf('('));
				obj2.vars=some4.substring(some4.indexOf('(')+1,some4.indexOf(')')).split(",");
				for(int ff=0;ff<obj2.vars.length;ff++)
					obj2.vars[ff]=obj2.vars[ff].trim();
				
			}
		return obj2;
	}
	
	public static String negate(String curquery)
	{
		String negquery="";
		if(curquery.charAt(0)=='~')
			negquery=curquery.substring(1,curquery.length());
		else
			negquery="~"+curquery;
		return negquery;
	}
	
	public static HashMap<String, ArrayList<ArrayList<MyClass>>> addinkb(HashMap<String, ArrayList<ArrayList<MyClass>>> bkb, String negquery)
	{
		ArrayList<MyClass> jlt=new ArrayList<>();
		String nqpred = negquery.substring(0,negquery.indexOf('('));
		MyClass nqobj=simplify(negquery);
		jlt.add(nqobj);

		ArrayList<ArrayList<MyClass>> a1 = new ArrayList<>();
		
			if(bkb.containsKey(nqpred))
			{
				a1=bkb.get(nqpred);
				a1.add(jlt);
				bkb.put(nqpred,a1);
			}
			else
			{
				a1.add(jlt);
				bkb.put(nqpred, a1);
			}
		return bkb;
	}
	
	public static HashMap<String,ArrayList<ArrayList<MyClass>>> dcopy(HashMap<String,ArrayList<ArrayList<MyClass>>> orig)
		{
		HashMap<String,ArrayList<ArrayList<MyClass>>> copyorig=new HashMap<>();
		for(String key:orig.keySet())
		{
			ArrayList<ArrayList<MyClass>> copy1=new ArrayList<>();
			ArrayList<ArrayList<MyClass>> orig1=orig.get(key);
			for(ArrayList<MyClass> orig2: orig1)
			{
				ArrayList<MyClass> copy2= new ArrayList<>();
				for(MyClass orignode: orig2)
				{
					copy2.add(newdeepcopy(orignode));
				}
				copy1.add(copy2);
			}
			copyorig.put(key, copy1);

		}
		return copyorig;
		}
	
	public static boolean infer(HashMap<String, ArrayList<ArrayList<MyClass>>> bkb,String negquery)
	{
		int count=0;
		long starttime=System.currentTimeMillis();
		while(true && (System.currentTimeMillis()-starttime)/1000<20)
		{ 
			int ultimate=0;
			ArrayList<ArrayList<MyClass>> bigcombine = new ArrayList<>();
			for(String key:bkb.keySet())
			{
				if((System.currentTimeMillis()-starttime)/1000>20)
					break;
				String notkey=negate(key);
				if(bkb.containsKey(notkey))
				{
					ArrayList<ArrayList<MyClass>> one = bkb.get(key);
					ArrayList<ArrayList<MyClass>> two = bkb.get(notkey);
					
					for(int i=0;i<one.size() && (System.currentTimeMillis()-starttime)/1000<20;i++) 
					{
						for(int j=0;j<two.size() && (System.currentTimeMillis()-starttime)/1000<20;j++) 
						{
							ArrayList<MyClass> onewala=one.get(i);
							ArrayList<MyClass> twowala=two.get(j);
							
							for(MyClass chu1:onewala)
							{
								if((System.currentTimeMillis()-starttime)/1000>20)
									break;
								for(MyClass chu2:twowala)
								{
									if((System.currentTimeMillis()-starttime)/1000>20)
										break;
									if(chu1.pred.equals(negate(chu2.pred)))
									{
										HashMap<String,String>unifymap=unify(chu1.vars,chu2.vars);
										if (!unifymap.isEmpty())
										{ 
										ArrayList<MyClass> combine = new ArrayList<>();
										for(int x=0;x<onewala.size() && (System.currentTimeMillis()-starttime)/1000<20;x++)
										{
											if(onewala.get(x).pred!=chu1.pred || !Arrays.equals(onewala.get(x).vars, chu1.vars))
											{
												combine.add(onewala.get(x));
											}
										}
										for(int x=0;x<twowala.size() && (System.currentTimeMillis()-starttime)/1000<20;x++)
										{
											if(twowala.get(x).pred!=chu2.pred || !Arrays.equals(twowala.get(x).vars, chu2.vars))
											{
												combine.add(twowala.get(x));
											}
										}
										
										if(combine.size()==0)
											return true;
										
										ArrayList<MyClass> dp = new ArrayList<>();
										for(MyClass in:combine)
										{
											if((System.currentTimeMillis()-starttime)/1000>20)
												break;
											MyClass combdc = newdeepcopy(in);
											MyClass subs= substitute(combdc, unifymap);
											
											dp.add(subs);
											if(dp.size()==0)
												return true;
										}
										bigcombine.add(dp);
										}	
									}
								}
							}
//							
						}
					}
				}
			} // pahila for loop
//			count++; //to avoid true
			
			//tempkb and origkb compare
			//add to kb
			int cc = 0;
//			System.out.println("This is bigcombine length "+bigcombine.size());
//			myprintlol(bigcombine);
			for(ArrayList<MyClass> here:bigcombine)
			{
				if((System.currentTimeMillis()-starttime)/1000>20)
					break;
				boolean check=false;
				MyClass uchal = here.get(0);
				if(bkb.containsKey(uchal.pred))
				{
					ArrayList<ArrayList<MyClass>> dummy = new ArrayList<>();
					dummy=bkb.get(uchal.pred);
					for(ArrayList<MyClass> aat:dummy)
					{
						if((System.currentTimeMillis()-starttime)/1000>20)
							break;
						if (here.size() == aat.size())
						{
						int ithecount=0;
						for(MyClass nodewala:here)
						{
							if((System.currentTimeMillis()-starttime)/1000>20)
								break;
							for(MyClass kbwala:aat)
							{
								if((System.currentTimeMillis()-starttime)/1000>20)
									break;
								if(kbwala.pred.equals(nodewala.pred))
								{
									boolean flag=false;
									for(int y=0;y<kbwala.vars.length && (System.currentTimeMillis()-starttime)/1000<20;y++)
									{
										if(!kbwala.vars[y].equals(nodewala.vars[y]))
										{
											flag=true;
											break;
										}
									}
								
								if(!flag)
									ithecount++;
								}
							}
						}
						
//						System.out.println("ithecount "+ithecount);
//						System.out.println("here size "+here.size());
						if (ithecount==here.size())
						{
							cc++;
							check=true;
							break;
						}
						}
						
					} // ha for break hotoy
					
					if(check==false)
						
					{
						ultimate++;
						//how to work on this flag???
						//ultimate wala
						//that means sentence not previously in kb
						for(int i=0;i<here.size() && (System.currentTimeMillis()-starttime)/1000<20;i++)
						{
							//add to kb
							MyClass addnode=here.get(i);
							ArrayList<ArrayList<MyClass>> say= new ArrayList<>();
							say=bkb.get(addnode.pred);
							say.add(here);
							bkb.put(addnode.pred, say);
						
							
						}
					}
					
				}
			}
		if(ultimate==0)
			break;
			
	}//while true
		return false;

		} 
	
	public static MyClass substitute(MyClass inner, HashMap<String, String> unifymap)
	{
			for(int i=0;i<inner.vars.length;i++)
			{
				if (unifymap.containsKey(inner.vars[i]))
						inner.vars[i]=unifymap.get(inner.vars[i]);
			}
		return inner;
	}
	
	public static MyClass newdeepcopy(MyClass node)
	{
		MyClass ndc = new MyClass();
		ndc.vars=new String[node.vars.length];
		
		for(int i=0;i<node.vars.length;i++)
		{
			ndc.vars[i]=node.vars[i];
		}
		ndc.pred=node.pred;
//		System.out.println(node.hashCode()+" "+ndc.hashCode());
		return ndc;
	}
	
//check 6 conditions	  ...... var const var const
    public static HashMap<String,String> unify(String[] pahila, String[] dusra)
    {
//    	System.out.println("enter");
    	HashMap<String,String> unifymap=new HashMap<>();
    	for(int s=0;s<pahila.length;s++)
    	{
    		//pahila[s]-const and dusra[s]-var
    		if(Character.isUpperCase(pahila[s].charAt(0)) && Character.isLowerCase(dusra[s].charAt(0)))
    		{
    			if(unifymap.containsKey(dusra[s]))
    			{
    				unifymap.clear();
    				break;
    			}
    			unifymap.put(dusra[s], pahila[s]);
//    			System.out.println("uni"+unifymap);
    		}
    		
    		//dusra[s]-const and pahila[s]-var
    		if(Character.isUpperCase(dusra[s].charAt(0)) && Character.isLowerCase(pahila[s].charAt(0)))
    		{
    			if(unifymap.containsKey(pahila[s]))
    			{
    				unifymap.clear();
    				break;
    			}
    			unifymap.put(pahila[s], dusra[s]);
//    			System.out.println("uni"+unifymap);
    		}
    		
    		//pahila=var dusra=var both same
    		if(Character.isLowerCase(pahila[s].charAt(0)) && Character.isLowerCase(dusra[s].charAt(0)) && pahila[s].equals(dusra[s]))
    			unifymap.put(pahila[s], dusra[s]);
             	//continue;
    				
    		//pahila=var dusra=var both diff
    		if(Character.isLowerCase(pahila[s].charAt(0)) && Character.isLowerCase(dusra[s].charAt(0)) && !pahila[s].equals(dusra[s]))
    		{
    			unifymap.clear();
    			break;
    		}
    			
    		//pahila=const dusra=const both same
    		if(Character.isUpperCase(pahila[s].charAt(0)) && Character.isUpperCase(dusra[s].charAt(0)) && pahila[s].equals(dusra[s]))
    			unifymap.put(pahila[s], dusra[s]);
             //continue;
        	
    		//pahila=const dusra=const both diff
    		if(Character.isUpperCase(pahila[s].charAt(0)) && Character.isUpperCase(dusra[s].charAt(0)) && !pahila[s].equals(dusra[s]))
    		{
    			unifymap.clear();
    			break;
    		}
    		
    	}
//    	System.out.println("uni "+unifymap);
    	return unifymap;
    }
	
	public static void myprintarr(ArrayList<MyClass> combine)
	{
//		for(ArrayList<MyClass> d:combine)
//		{
			for(MyClass dd:combine)
			{
				System.out.print(" "+dd.pred);
				for(String ddd:dd.vars)
					System.out.print("  "+ddd);
			}
			System.out.println();

//		}
	}
		
	
	public static ArrayList<MyClass> res(ArrayList<MyClass> idhar1,String querypred)
	{
		//query pred bcoz negate of negate of querypred needs to be hunted
		ArrayList<MyClass> newlist=new ArrayList<>();
		for(MyClass kuch1:idhar1)
		{
			if(!kuch1.pred.equals(querypred))
				newlist.add(kuch1);
		}
//		for(MyClass tp:newlist)
//		System.out.println("newlist"+tp.pred);
		return newlist;
		
	}
	
	/*public static HashMap<String,String> unify(String idhar3, String nqvi)
	{
		HashMap<String,String> unifymap=new HashMap<>();
		
		//var-const
		if('a'<=idhar3.charAt(0) && idhar3.charAt(0)<='z' && nqvi.length()>1 && nqvi.charAt(0)<='a') 
			unifymap.put(idhar3, nqvi);
		
		
		//const-var
		else if(idhar3.length()>1 && nqvi.charAt(0)<='a' && 'a'<=nqvi.charAt(0) && nqvi.charAt(0)<='z') 
			unifymap.put(idhar3,nqvi);

			
		//same
		else if(idhar3.equals(nqvi))
			unifymap.put(idhar3,nqvi);
			
		return unifymap;
	}*/
	
	public static void output(String results[], String filename) throws IOException
	{
		BufferedWriter bw = new BufferedWriter(new FileWriter(filename));
		for(int i=0;i<results.length;i++)
		{
			bw.write(results[i]+"\n");
		}
		bw.close();
	}

	
	public static void main (String args[]) throws IOException
	{
		Reading obj = read("src/input.txt");
		HashMap<String, ArrayList<ArrayList<MyClass>>> bkb = buildkb(obj.orig,obj.kb);
//		myprint(bkb);
//		myprint(superbkb);

		String curquery;
		String negquery;
		String[] results=new String[obj.query];
		for(int i =0;i<obj.query;i++)
		{
			HashMap<String, ArrayList<ArrayList<MyClass>>> superbkb = dcopy(bkb);
//			System.out.println(superbkb.hashCode()+" "+bkb.hashCode());
			curquery=obj.queries[i];
			negquery=negate(curquery);
//			if (superbkb.containsKey(curquery.substring(0,curquery.indexOf('('))) || superbkb.containsKey(negquery.substring(0,negquery.indexOf('('))))
			addinkb(superbkb,negquery);
//			System.out.println("after adding neg query:  "+superbkb);
//			System.out.println("curquery:  "+curquery);
			//System.out.println(infer(superbkb,negquery));
			boolean result=infer(superbkb, negquery);
//			output(result, "output.txt");
			  // so that only 1 query i will handle now
			if(result)
				results[i]="TRUE";
			else
				results[i]="FALSE";
			
			//infer(negquery);
		}
		output(results, "output.txt");
		
//		myprint(bkb);
	}
}

class MyClass
{
	String pred;
	String[] vars;
}

class Reading
{
	int query; String[] queries; int orig; String[] kb;
	
	public Reading(int query,String[] queries,int orig,String[] kb)
	{
		this.query=query;
		this.queries=queries;
		this.orig=orig;
		this.kb=kb;
	}
}