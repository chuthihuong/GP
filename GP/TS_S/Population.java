package TS_S;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.apache.commons.math3.stat.inference.WilcoxonSignedRankTest;


import Common.MyRandom;





//import java.io.FileInputStream;
//import java.io.FileOutputStream;
//import java.io.PrintStream;
//import java.text.DecimalFormat;

public class Population extends MyRandom {
	//mang chua quan the cu va moi
	individual[]	oldpop, newpop;												
	int				poplen;			//so ca the											
	double			pcross, pmutate, pcopy;		//xac suat lai ghep va dot bien							
	long				ncross, nmutate;//thong ke so lan lai ghep va so lan dot bien
	static int				gltermcard;//so ham va terminal										
	static int				glfcard;//so ham
	static int				gltcard;//so terminal
	term[]			glterm		= new term[MAXFUNCTION + MAXTERMINAL];//mang luu ham va terminal
	term[]			glfunction	= new term[MAXFUNCTION];//mang luu ham
	term[]			glterminal	= new term[MAXTERMINAL];//mang luu terminal
	sample[]	fitcase			= new sample[NUMFITCASE];//mang luu du lieu dau vao
	sample[]	fittest			= new sample[NUMFITTEST];//mang luu du lieu dau vao
	byte		SuccPredicate	= FALSE;//danh gia thanh cong hay khong?
	int				generation, gen = 0;
	individual[]	bestcurrent;
	double[]			average;
	double[]			averageSize;
//	int[][]				popSize;
	
//	public double[] constructiveRate;
//	public double[] semanticDistance;
	public long timeTournament;

	
	
//	long NumTest=0;
//	long NumDifference=0;

	
// **************************************************
//Lop chua du lieu dau vao
	class sample {
		double x[]=new double[NUMVAR];
		double	y;
	}
//Lop chua cac toan tu va terminal 
	class term {
		String	name;
		byte	arity;
	};
// Ham tao cho quan the, dat seed	
	public Population(long seed) {
		super.Set_Seed(seed);//goi ham Set_Seed trong lop Random
		//Khoi tao cac bien dem =0
		gltermcard = 0;
		glfcard = 0;
		gltcard = 0;
		//Khoi tao cac mang chau term
		for(int i = 0; i < MAXFUNCTION + MAXTERMINAL; i++)
			glterm[i] = new term();
		for(int i = 0; i < MAXTERMINAL; i++)
			glterminal[i] = new term();
		for(int i = 0; i < MAXFUNCTION; i++)
			glfunction[i] = new term();
		//goi phuong thuc addterm de them cac ham va terminal vao mang
		AddTerm("add", (byte) 2);
		AddTerm("sub", (byte) 2);
		
		AddTerm("mul", (byte) 2);
		AddTerm("div", (byte) 2);
		
		AddTerm("sin", (byte) 1);
		AddTerm("cos", (byte) 1);
//		AddTerm("ep", (byte) 1);
//		AddTerm("log", (byte) 1);
		//AddTerm("sqrt", (byte) 1);
		//AddTerm("sqr", (byte) 1);
		for(int i=0;i<NUMVAR;i++){			
			
    		AddTerm("X"+String.valueOf(i+1), (byte) 0);
		}
		
		//this.PrintTerm();
		//hoan doi ngau nhien cac term 100 lan
		SwapTerm(100);
		//this.PrintTerm();
		//dat so luong quan the
		poplen = POPSIZE;
		//khoi tao quan the moi va cu
		oldpop = new individual[poplen];
		assert (oldpop != null);
		newpop = new individual[poplen];
		assert (newpop != null);
		for(int i = 0; i < poplen; i++) {
			oldpop[i] = new individual();
			newpop[i] = new individual();
		}
		// dat xac suat cho dot bien va lai ghep
		pcross = PCROSS;
		pmutate = PMUTATE;
		// Khoi tao bien thong ke so lan dot bien va lai ghep
		ncross = 0;
		nmutate = 0;
		// Dat so generation
		generation = NUMGEN;
		//khoi tao mang de luu cac doi tuong tot nhat trong moi the he
		bestcurrent = new individual[generation];
		assert (bestcurrent != null);
		for(int i=0; i<generation; i++)
			bestcurrent[i]=new individual();
		//mang de luu cac fitness cua bestcurrent
		average = new double[generation];			
		assert (average != null);
		
		//mang de luu avg size Population
		averageSize = new double[generation];			
		assert (averageSize != null);
		
//		//mang de luu avg size Population
//		popSize = new int[generation][poplen];			
//		assert (popSize != null);
//		
		
//		NumTest=0; NumDifference=0;
		
		
		SuccPredicate = FALSE;
		SetFitCase();//Goi phuong thuc khoi tao du lieu
		SetFitTest();
	}
	
	// Phuong thuc them cac function va terminal vao mang
	void AddTerm(String name, byte arity){
		glterm[gltermcard].arity = arity;
		glterm[gltermcard].name = name;
		gltermcard++;
		if(arity == 0) {//truong hop la terminal
			glterminal[gltcard].arity = arity;
			glterminal[gltcard].name = name;
			gltcard++;
		} else {//truongg hop la function
			glfunction[glfcard].arity = arity;
			glfunction[glfcard].name = name;
			glfcard++;
		}
	}
//in ra cac term len man hinh
	void PrintTerm(){
		int i;
		for(i = 0; i < gltermcard; i++) {
			System.out.println("--------TERM " + i + "---------");
			System.out.println("NAME:" + glterm[i].name);
			System.out.println("ARITY:" + (int) glterm[i].arity);
		}
	}
	// Hoan doi cac term times lan
	void SwapTerm(int times){
		int i, j, k;
		term temp;
		assert (gltermcard >= 2);
		for(i = 0; i < times; i++) {
			j = IRandom(0, gltermcard - 1);
			k = IRandom(0, gltermcard - 1);
			temp = glterm[j];
			glterm[j] = glterm[k];
			glterm[k] = temp;
		}
	}

	//Xay dung lop Individual
	class individual {
		node		chrom;	// thuoc tinh chrom cua individual
		int			size, height;	// size and heigh cua individual
		double		branch;			// nhanh cua individual
		double		fitness;	
		double    oldfitness;
		byte evaluated;
		double[] semanticTraining;
		double[] errorVector;
		// fitness cua indinvidual
//ham tao cua lop individual
       public individual() {
			size = 0;
			height = 0;
			branch = 0;
			fitness = 0;
			oldfitness=0;
			evaluated=FALSE;
			chrom = new node();
			semanticTraining = new double[NUMFITCASE];
			errorVector=new double[NUMFITCASE];
		}
//ham copyIndividual
		public void CopyIndividual(individual t, byte copychrom){
			if(copychrom == TRUE)//truong hop copy den vung nho khac 
				this.chrom = chrom.CopyNode(t.chrom);
			else 
				//truong hop trung vung nho
				this.chrom = t.chrom;			

			this.size = t.size;
			this.height = t.height;
			this.fitness = t.fitness;
			this.branch = t.branch;
			this.oldfitness=t.oldfitness;
			this.evaluated=t.evaluated;
			
			this.semanticTraining=t.semanticTraining.clone();
			this.errorVector=t.errorVector.clone();
			
			
		}
		//phuong thuc xoa individual
		void DeleteChrom(node t){
			node q, p;
			
			if(t!=null)
			{
			if(t.children==null)
				t=null;
			else
			{
				p=t.children;
				while(p!=null)
				{
					q=p.sibling;
					DeleteChrom(p);
					p=q;
				}
				t=null;
			}
			}
			t=null;
		}
//hien thi mot individual ra man hinh
		void DisplayIndividual(){
			System.out.println("-----------------------------");
			System.out.println("Genotype Structure:");
			//go phuong thuc bien doi cay thanh mot xau
			this.chrom.DisplaySTree(this.chrom);
			System.out.println("Size:" + this.size);
			System.out.println("Height:" + this.height);
			System.out.println("Branching:" + this.branch);
			System.out.println("Fitness:" + this.fitness);
		}
		
		void DisplayIndividualChrom(){
			this.chrom.DisplaySTree(this.chrom);			
		}
		
		public boolean isBetterThan(individual individual)
		{
			if (ComputeRF(this) < ComputeRF(individual))
				return true;
			else
				return false;
		}
		
		public double getSemanticDistance(individual individual)
		{
			double sd = 0.0;
			
			for(int i = 0; i < this.semanticTraining.length; i++)
			{
				if(Math.abs(this.semanticTraining[i]) > 10000
						|| Math.abs(individual.semanticTraining[i]) > 10000)
					continue;
				
				sd += Math.abs(this.semanticTraining[i]-individual.semanticTraining[i]);
			}
			
			return sd/NUMFITCASE;
		}
		
		
	}
	
	
//phuong thuc phat trien cay voi do cao toi da ma maxdepth
	node GrowthTreeGen(int maxdepth){
		int i;
		byte a;
		node t, p;
		if(maxdepth == 0) // to the limit then choose a terminal
		{
			i = IRandom(0, gltcard - 1);
			t = new node(glterminal[i].name, VOIDVALUE);
			return t;
		} 
		else // choosing from function and terminal
		 {
			i = IRandom(0, gltermcard - 1);
			t = new node(glterm[i].name, VOIDVALUE);
			if(glterm[i].arity > 0) // if it is function
			{
				t.children = GrowthTreeGen(maxdepth - 1);
				p = t.children;
				for(a = 1; a < glterm[i].arity; a++) {
					p.sibling = GrowthTreeGen(maxdepth - 1);
					p = p.sibling;
				}
				p.sibling = null;
			}
			return t;
		}
	}
//phuong thuc phat trien cay day du
node FullTreeGen(int maxdepth){
		int i;
		byte a;
		node t, p;
		if(maxdepth == 0) // to the limit then choose a terminal
		{
			i = IRandom(0, gltcard - 1);
			t = new node(glterminal[i].name, VOIDVALUE);
			return t;
		} 
		else // choosing from function
		{
			i = IRandom(0, glfcard - 1);
			t = new node(glfunction[i].name, VOIDVALUE);
			t.children = FullTreeGen(maxdepth - 1);
			p = t.children;
			for(a = 1; a < glfunction[i].arity; a++) {
				p.sibling = FullTreeGen(maxdepth - 1);
				p = p.sibling;
			}
			p.sibling = null;
			return t;
		}
	}
//Khoi tao quan the dau tien
	void RampedInit(int depth, double percentage)
	{
		int i, j;
		byte k;
		node t, p;
		for(i = 0; i < poplen; i++) {
			// choose randomly from the function set
			j = IRandom(0, glfcard - 1);
			t = new node(glfunction[j].name, VOIDVALUE);
			if(Next_Double() < percentage)// Growth
			{
				t.children = GrowthTreeGen(depth - 1);
				p = t.children;
				for(k = 1; k < glfunction[j].arity; k++) {
					p.sibling = GrowthTreeGen(depth - 1);
					p = p.sibling;
				}
				p.sibling = null;
			} 
			else 
			{
				t.children = FullTreeGen(depth - 1);
				p = t.children;
				for(k = 1; k < glfunction[j].arity; k++) {
					p.sibling = FullTreeGen(depth - 1);
					p = p.sibling;
				}
				p.sibling = null;
			}
			oldpop[i].chrom = t;
			oldpop[i].size = t.GetSizeNode(t, TRUE);
			oldpop[i].height = t.GetHeightNode();
			oldpop[i].branch = t.GetAVGNode(t);
		}
	}
//
	void AdjustFitness(){
		int i;
		for(i = 0; i < poplen; i++)
			oldpop[i].fitness = 1 / (1 + oldpop[i].fitness);
	}
//cho fitness be hon 1
	void NormalizeFitness(){
		double sum = 0;
		int i;
		for(i = 0; i < poplen; i++)
			sum += oldpop[i].fitness;
		for(i = 0; i < poplen; i++)
		{
			oldpop[i].fitness = oldpop[i].fitness / sum;
		//	System.out.print(oldpop[i].fitness+" ");
	}
	}


int StaticTourSelectSmall(int size){
	int i, j,j0, pos = 0;
	double p_value;
	double[] _sum;
	assert (size <= MAXTOUR);
	
	pos=IRandom(0, poplen - 1);	
	
	for(i = 1; i < size; i++) {
		_sum=new double[2];
		j = IRandom(0, poplen - 1);
		p_value=StaticTest(oldpop[pos],oldpop[j],_sum);
//		NumTest += 1;
		//System.out.print("\n"+j+"="+oldpop[j].fitness);			
		if(p_value<alpha){
//			NumDifference+=1;
			if(_sum[1]<_sum[0]){					
				pos = j;
			}				
		}
		else{				
			if(oldpop[j].size<oldpop[pos].size){					
				pos = j;
			}				
		}
	}
	//System.out.print("pos="+oldpop[pos].fitness+"\n");
	return pos;		
	
}
	
	double StaticTest(individual st1, individual st2, double[] sum){
		double sum1, sum2,t, p_value;
//		double[] tm;
//		int i;
		
		double[] sample1=new double[NUMFITCASE];
		double[] sample2=new double[NUMFITCASE];
		
	//	TTest test = new TTest(); 
		WilcoxonSignedRankTest test=new WilcoxonSignedRankTest();
		sum1=st1.oldfitness;
		sample1=st1.errorVector;
		
//		sum1=0;
//		tm=ComputeNew(st1.chrom);
//		System.out.println(sample1[NUMFITCASE]);
//		for(i = 0; i < NUMFITCASE; i++) {
//		
//			t = PVAL(tm[i]);
//			t= Math.abs(t- fitcase[i].y);// fabs
//			
//			System.out.println("sample 1:" +sample1[i]+"t="+t+ "error="+ st1.errorVector[i]);
////			sample1[i]=t;
//			sum1+=t;		
//		}
		
			
		
		sum2=st2.oldfitness;
		sample2=st2.errorVector;
//		sum2=0;
//		tm=ComputeNew(st2.chrom);
//		for(i = 0; i < NUMFITCASE; i++) {
//		
//			t = PVAL(tm[i]);
//			t= Math.abs(t- fitcase[i].y);// fabs
//			sample2[i]=t;
//			sum2+=t;		
//		}
		
//		p_value=test.pairedTTest(sample1, sample2);
		p_value=test.wilcoxonSignedRankTest(sample1, sample2, false);
		sum[0]=sum1;
		sum[1]=sum2;		
		return p_value;
	}


	// **************************************************
	//phuong thuc crossover
		byte SubTreeSwap(individual parent1, individual parent2, individual[] child,int[] cp,node[] nd)
		{
			individual temp1, temp2;// hai bien tam de luu hai parent		
			temp1 = new individual();
			temp2 = new individual();
			int count, crosspoint1, crosspoint2;
			node node1, node2, t;
			node1=new node();node2=new node();t=new node();
			double valuetemp;
			String nametemp;
			count = 0;
			//so lan thu mutation
			while(count < MAXATEMPT) {
				// copy the parents
				temp1.CopyIndividual(parent1, TRUE);
				temp2.CopyIndividual(parent2, TRUE);
				// Chon ngau nhien diem de lai ghep
				crosspoint1 = IRandom(2, temp1.size);
				crosspoint2 = IRandom(2, temp2.size);
				temp1.chrom.LocateNode(crosspoint1, temp1.chrom, null);
				
				node1=temp1.chrom._idnode; 
				
				temp2.chrom.LocateNode(crosspoint2, temp2.chrom, null);
				node2=temp2.chrom._idnode;
				
				nametemp = node1.name;
				node1.name = node2.name;
				node2.name = nametemp;
				valuetemp = node1.value;
				node1.value = node2.value;
				node2.value = valuetemp;
				// then swap the children
				t = node1.children;
				node1.children = node2.children;
				node2.children = t;
				// compute the heigh after that
				temp1.height = temp1.chrom.GetHeightNode();
				temp2.height = temp2.chrom.GetHeightNode();
				if((temp1.height <= MAXDEPTH) && (temp2.height <= MAXDEPTH)) {
					temp1.size = temp1.chrom.GetSizeNode(temp1.chrom, TRUE);
					temp1.branch=temp1.chrom.GetAVGNode(temp1.chrom);
					temp2.size=temp2.chrom.GetSizeNode(temp2.chrom, TRUE);
					temp2.branch=temp2.chrom.GetAVGNode(temp2.chrom);
					child[0]=temp1;
					child[1]=temp2;
					cp[0]=crosspoint1;
					cp[1]=crosspoint2;
					nd[0]=node1;
					nd[1]=node2;
					return TRUE;
				} else {
//					temp1.DeleteChrom();
//					temp1=null;				
//					temp2.chrom.DeleteChrom(temp1.chrom);
//					temp2.DeleteChrom();
//					temp2=null;
				}
				count++;
			}
			return FALSE;
		}

	//dot bien
	byte ReplaceSubTree(individual child,individual[] copychild, int maxdepth, byte type,int[] mt,node[] nm)
	{
		individual temp = new individual();
		int replacepoint, count;
		node node1=new node(), parentnode1=new node(), t=new node(), p=new node();
		count = 0;
		
		while(count < MAXATEMPT) {
			temp=new individual();
			temp.CopyIndividual(child, TRUE);
			replacepoint = IRandom(2, temp.size);
			temp.chrom.LocateNode(replacepoint, temp.chrom, null);
			
			node1=temp.chrom._idnode;
			parentnode1=temp.chrom._idnodeparent;
			
			if(type == TRUE) // growth
			  t = GrowthTreeGen(maxdepth);
			else t = FullTreeGen(maxdepth);
			nm[0]=t;
		
			p = parentnode1.children;
			if(p == node1) // first child
			parentnode1.children = t;
			else {
				while(p.sibling != node1)
					p = p.sibling;
				p.sibling = t;
			}
			t.sibling = node1.sibling;
//			node1.DeleteNode(node1);
//			temp.DeleteChrom(node1);
			node1=null;
			temp.height = temp.chrom.GetHeightNode();
			
			if(temp.height <= MAXDEPTH) {
//				child.chrom.DeleteNode(child.chrom);
				child.chrom=null;
				child.chrom=temp.chrom;
//				if(status==0)
//				{
//				//  System.out.print("\n------------------------mutation---\n");
//				//  child.chrom.DisplaySTree(temp.chrom);
//				}
				child.height=temp.height;
				child.size=child.chrom.GetSizeNode(child.chrom, TRUE);
				temp.branch=child.chrom.GetAVGNode(child.chrom);
				//copychild[0].CopyIndividual(child, TRUE);
				copychild[0]=child;
				mt[0]=replacepoint;
				return TRUE;
			}
//			temp.chrom.DeleteNode(temp.chrom);
			temp=null;
			count++;
		}
		return FALSE;
	}

//phuong thuc doc du lieu
	void SetFitCase(){
		String csvFile = DATADIR + PROBLEM +".training.in";;
		BufferedReader br = null;
		String line = "";
		String cvsSplitBy = " ";
		
		int i,j;
		
		try {
			 
			br = new BufferedReader(new FileReader(csvFile));
			line = br.readLine();
			i=0;
			while (((line = br.readLine()) != null)&&(i<NUMFITCASE)) {
	 
			        // use comma as separator)
				String[] dataline = line.split(cvsSplitBy);
				fitcase[i]=new sample();
				for(j=0;j<NUMVAR;j++)
				{
					   fitcase[i].x[j]= Double.parseDouble(dataline[j]);
				}
				fitcase[i].y= Double.parseDouble(dataline[NUMVAR]);
				i=i+1;
	 
			}
	 
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		
	}
void PrintFitCase()
{
	int i;
	for(i=0;i<NUMFITCASE;i++)
		System.out.print(fitcase[i].x+"|"+fitcase[i].y+"\n");
}
	// ********************************************************************
void SetFitTest(){
	String csvFile = DATADIR + PROBLEM +".testing.in";
	BufferedReader br = null;
	String line = "";
	String cvsSplitBy = " ";
	
	int i,j;
	
	try {
		 
		br = new BufferedReader(new FileReader(csvFile));
		line = br.readLine();
		
		i=0;
		while (((line = br.readLine()) != null)&&(i<NUMFITTEST)) {
 
		        // use comma as separator)
			String[] dataline = line.split(cvsSplitBy);
			fittest[i]=new sample();
			for(j=0;j<NUMVAR;j++)
			{
				   fittest[i].x[j]= Double.parseDouble(dataline[j]);
			}
			fittest[i].y= Double.parseDouble(dataline[NUMVAR]);
			i=i+1;
 
		}
 
	} catch (FileNotFoundException e) {
		e.printStackTrace();
	} catch (IOException e) {
		e.printStackTrace();
	} finally {
		if (br != null) {
			try {
				br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	
}
double GenerateER(String name)
{
 if(name.equals("ERC"))
   return -1+2*Next_Double();
return VOIDVALUE;
}

//phuong thuc compute cho mot node
double[] ComputeNew(node st){
	node p;
	double[] l;
	double[] r;
	double[] result=new double[NUMFITCASE];
	
	for(int j=0;j<NUMVAR;j++){		
		
		if(st.name.equals("X"+String.valueOf(j+1))){
			for(int i=0; i<NUMFITCASE; i++)
			{
				result[i]=fitcase[i].x[j];
			}
			return result;
		}
	}
	
	if(st.name.equals("1")){
		for(int i=0; i<NUMFITCASE; i++)
		{
			result[i]=1;
		}
		return result;
	}
	else
		if(st.name.equals("ERC")) {
			if(st.value==VOIDVALUE) //if it has not been initialized then initialized and return the value
			    st.value=GenerateER(st.name);      
		//	  st.att[i]=st.value;
			for(int i=0; i<NUMFITCASE; i++)
			{
				result[i]=st.value; 
			}
			return result;			  
		}	
	else // st.name="EXP"
	{
		p = st.children;
		if(st.name.equals("add")) {
			l = ComputeNew(p);
			r = ComputeNew(p.sibling);
			for(int i=0; i<NUMFITCASE; i++)
			{
				result[i]=PVAL(l[i] + r[i]);
			}
			return result;
		} else if(st.name.equals("sub")) {
			l = ComputeNew(p);
			r = ComputeNew(p.sibling);
			for(int i=0; i<NUMFITCASE; i++)
			{
				result[i]=PVAL(l[i] - r[i]);
			}
			return result;
		} else if(st.name.equals("mul")) {
			l = ComputeNew(p);
			r = ComputeNew(p.sibling);
			for(int i=0; i<NUMFITCASE; i++)
			{
				result[i]=PVAL(l[i] * r[i]);
			}
			return result;
		} else if(st.name.equals("div")) {
			l = ComputeNew(p);
			r = ComputeNew(p.sibling);
			for(int i=0; i<NUMFITCASE; i++)
			{
				if(r[i] == 0) 
					{result[i]= 1;}
				else {result[i]= PVAL(l[i]/r[i]);}				
			}
			return result;
		
		} else if(st.name.equals("sin")) {
			l = ComputeNew(p);
			for(int i=0; i<NUMFITCASE; i++)
			{
				result[i]= Math.sin(l[i]);				
			}
			return result;
			
			
		} else if(st.name.equals("cos")) {
			l = ComputeNew(p);
			for(int i=0; i<NUMFITCASE; i++)
			{
				result[i]= Math.cos(l[i]);				
			}
			return result;
			
		} else if(st.name.equals("sqrt")) {
			l = ComputeNew(p);
			for(int i=0; i<NUMFITCASE; i++)
			{
				result[i]= Math.sqrt(Math.abs(l[i]));			
			}
			return result;			
		} else if(st.name.equals("sqr")) {
			l = ComputeNew(p);
			for(int i=0; i<NUMFITCASE; i++)
			{
				result[i]= PVAL(l[i]*l[i]);			
			}
			return result;
			
		} else if(st.name.equals("ep")) {
			l = ComputeNew(p);
			for(int i=0; i<NUMFITCASE; i++)
			{
				result[i]= PVAL(Math.exp(l[i]));	
			}
			return result;			
		}  else if(st.name.equals("divE")) {
			l = ComputeNew(p);
			r = ComputeNew(p.sibling);
			for(int i=0; i<NUMFITCASE; i++)
			{
				result[i]= PVAL(l[i]/Math.sqrt(1+ r[i]*r[i]));				
			}
			return result;			
			} 
		else {
			l = ComputeNew(p);
			for(int i=0; i<NUMFITCASE; i++)
			{
				result[i]= PVAL(Math.exp(l[i]));
				if(l[i] == 0) result[i]= 0;
				else result[i]= PVAL(Math.log(Math.abs(l[i])));// fido fabs
			}
			return result;	
			
		}
	}
}

double[] ComputeTest(node st){
	node p;
	double[] l;
	double[] r;
	double[] result=new double[NUMFITTEST];
	
	for(int j=0;j<NUMVAR;j++){		
		
		if(st.name.equals("X"+String.valueOf(j+1))){
			for(int i=0; i<NUMFITTEST; i++)
			{
				result[i]=fittest[i].x[j];
			}
			return result;
		}
	}
	
	if(st.name.equals("1")){
		for(int i=0; i<NUMFITTEST; i++)
		{
			result[i]=1;
		}
		return result;
	}
	else
		if(st.name.equals("ERC")) {
			if(st.value==VOIDVALUE) //if it has not been initialized then initialized and return the value
			    st.value=GenerateER(st.name);      
		//	  st.att[i]=st.value;
			for(int i=0; i<NUMFITTEST; i++)
			{
				result[i]=st.value; 
			}
			return result;			  
		}	
	else // st.name="EXP"
	{
		p = st.children;
		if(st.name.equals("add")) {
			l = ComputeTest(p);
			r = ComputeTest(p.sibling);
			for(int i=0; i<NUMFITTEST; i++)
			{
				result[i]=PVAL(l[i] + r[i]);
			}
			return result;
		} else if(st.name.equals("sub")) {
			l = ComputeTest(p);
			r = ComputeTest(p.sibling);
			for(int i=0; i<NUMFITTEST; i++)
			{
				result[i]=PVAL(l[i] - r[i]);
			}
			return result;
		} else if(st.name.equals("mul")) {
			l = ComputeTest(p);
			r = ComputeTest(p.sibling);
			for(int i=0; i<NUMFITTEST; i++)
			{
				result[i]=PVAL(l[i] * r[i]);
			}
			return result;
		} else if(st.name.equals("div")) {
			l = ComputeTest(p);
			r = ComputeTest(p.sibling);
			for(int i=0; i<NUMFITTEST; i++)
			{
				if(r[i] == 0) result[i]= 1;
				else result[i]= PVAL(l[i]/r[i]);				
			}
			return result;
		
		} else if(st.name.equals("sin")) {
			l = ComputeTest(p);
			for(int i=0; i<NUMFITTEST; i++)
			{
				result[i]= Math.sin(l[i]);				
			}
			return result;
			
			
		} else if(st.name.equals("cos")) {
			l = ComputeTest(p);
			for(int i=0; i<NUMFITTEST; i++)
			{
				result[i]= Math.cos(l[i]);				
			}
			return result;
			
		} else if(st.name.equals("sqrt")) {
			l = ComputeTest(p);
			for(int i=0; i<NUMFITTEST; i++)
			{
				result[i]= Math.sqrt(Math.abs(l[i]));			
			}
			return result;			
		} else if(st.name.equals("sqr")) {
			l = ComputeTest(p);
			for(int i=0; i<NUMFITTEST; i++)
			{
				result[i]= PVAL(l[i]*l[i]);			
			}
			return result;
			
		} else if(st.name.equals("ep")) {
			l = ComputeTest(p);
			for(int i=0; i<NUMFITTEST; i++)
			{
				result[i]= PVAL(Math.exp(l[i]));	
			}
			return result;			
		} else if(st.name.equals("divE")) {
			l = ComputeTest(p);
			r = ComputeTest(p.sibling);
			for(int i=0; i<NUMFITTEST; i++)
			{
				result[i]= PVAL(l[i]/Math.sqrt(1+r[i]*r[i]));				
			}
			return result;			
		}
		
		else {
			l = ComputeTest(p);
			for(int i=0; i<NUMFITTEST; i++)
			{
				result[i]= PVAL(Math.exp(l[i]));
				if(l[i] == 0) result[i]= 0;
				else result[i]= PVAL(Math.log(Math.abs(l[i])));// fido fabs
			}
			return result;	
			
		}
	}
}


	// *********************************************
	//phuong thuc tinh fitness cho mot individual
	double ComputeRF(individual st){
		double sum = 0,t;
		double[] tm;
		int i;
		int hit;
		hit = 0;
		
		tm=ComputeNew(st.chrom);
		st.semanticTraining=tm;
		
		for(i = 0; i < NUMFITCASE; i++) {		
			t = PVAL(tm[i]);
			t= Math.abs(t- fitcase[i].y);// fabs
			if(t <= 0.01) hit++;
			sum += t;
			st.errorVector[i]=t;
		}
		if(hit == NUMFITCASE) SuccPredicate = TRUE;
		return sum/NUMFITCASE;
	}
	
	double ComputeFT(individual st){
		double sum = 0, ft;
		int i;
		double[] tm;
		tm=ComputeTest(st.chrom);
		for(i = 0; i < NUMFITTEST; i++) {
			ft = PVAL(tm[i]);
			ft= Math.abs(ft- fittest[i].y);// fabs			
			sum += ft;
		}		
		return sum/NUMFITTEST;
	}
	
	
	
//phuong thuc tinh fitness cho tat ca cac individual trong mot quan the
	void ComputeFitness(){
		int i, pos;
		// individual t;
		double min, sum = 0, sumSize = 0, tm;
		// First Compute Raw fitness
		for(i = 0; i < poplen; i++)
		{
			if(oldpop[i].evaluated==FALSE)
			{   tm=ComputeRF(oldpop[i]);
				oldpop[i].fitness = tm;
				oldpop[i].oldfitness = tm;
				oldpop[i].evaluated=TRUE;
			}
			else
			{
				oldpop[i].fitness=oldpop[i].oldfitness;
			}
			
			//oldpop[i].DisplayIndividual();
			//System.out.println(oldpop[i].fitness);
		}
		//tim individual co fitness be nhat
		min = oldpop[0].fitness;
		pos = 0;
		sum = oldpop[0].fitness;
		sumSize = oldpop[0].size;
		
		for(i = 1; i < poplen; i++) {
			if(oldpop[i].fitness < min) {
				min = oldpop[i].fitness;
				pos = i;
			}
			sum += oldpop[i].fitness;
			sumSize += oldpop[i].size;
//			popSize[gen][i]= oldpop[i].size;
		}
		// copy the best and average
		bestcurrent[gen] = new individual();
		bestcurrent[gen].CopyIndividual(oldpop[pos], TRUE);
		average[gen] = sum /poplen;
		averageSize[gen] = sumSize /poplen;
		// Third Compute Adjusted fitness
		AdjustFitness();
		// Finally Compute nomarlized fitness
 		NormalizeFitness();
	}
	//in cac population ra file
//	void PrintToFile()
//	{
//		FileInputStream instream = null;
//		PrintStream outstream = null;
//		PrintStream console = System.out;
//		String temp=""; 
//		try {
//			outstream = new PrintStream(new FileOutputStream("c:/result/" + gen + ".txt"));
//			System.setOut(outstream);
//		} catch(Exception e) {
//			System.err.println("Error Occurred.");
//		}
//		
//		for(int ii = 0; ii < poplen; ii++) {			
//			temp="";
//			temp=oldpop[ii].chrom.TreeToStringN(oldpop[ii].chrom);
//			System.out.printf("%s", temp);			
//				System.out.println();
////			}
//		}
//		outstream.close();
//		System.setOut(console);
//	
//	}
//	
//phuong thuc tien hoa
	void Evolution(){
		int i, j, l, k;
		//individual[] temp;
		byte _flippc=0,_flippm=0;
//		String _temp1="", _temp2="";
		int[] _cp;
		node[] _nd,_nm;
		// Khoi tao quan the dau tien 
		RampedInit(6, 0.5);
		
		ncross = 0;
		
//		constructiveRate = new double[NUMGEN];
//		semanticDistance = new double[NUMGEN];
		
		gen = 0;
		while(gen < generation) {
			
			ComputeFitness();

			// for constructive rate and semantic distance
			int g_ncross = 0;
			int g_numOfBetter = 0;
			double g_sd = 0;
			
			l = 0;			
			while(l < poplen) {				
//							
				i = StaticTourSelectSmall(TOURSIZE);
				j = StaticTourSelectSmall(TOURSIZE);
//					
			    _flippc=Flip(pcross);
//			    System.out.printf("pcross:%d", _flippc);
//			    System.out.println();
				if(_flippc == 1) 
				{					
					individual[] i_temp=new individual[2];
					_cp=new int[2];
					_nd=new node[2];
					if(SubTreeSwap(oldpop[i], oldpop[j], i_temp,_cp,_nd) == TRUE)
					{
						newpop[l].CopyIndividual(i_temp[0],TRUE);
						newpop[l+1].CopyIndividual(i_temp[1],TRUE);
						
						newpop[l].evaluated=FALSE;
						newpop[l+1].evaluated=FALSE;
						ncross++;
						
						
						i_temp=null;
						
						if(newpop[l].isBetterThan(oldpop[i]))
							g_numOfBetter += 1;
						if(newpop[l+1].isBetterThan(oldpop[j]))
							g_numOfBetter += 1;
						
						// semantic distance
						g_sd += newpop[l].getSemanticDistance(oldpop[i]);
						g_sd += newpop[l+1].getSemanticDistance(oldpop[j]);
						
						g_ncross++;
					}
					else
					{						
						newpop[l].CopyIndividual(oldpop[i], TRUE);
						newpop[l + 1].CopyIndividual(oldpop[j],TRUE);
//						System.out.printf("%s","reproduction");
//						System.out.println();
//						_temp1="";
//						_temp1=newpop[l].chrom.TreeToStringN(newpop[l].chrom);
//						System.out.printf("%s", _temp1);			
//						System.out.println();
//						_temp2="";
//						_temp2=newpop[l+1].chrom.TreeToStringN(newpop[l+1].chrom);
//						System.out.printf("%s", _temp2);			
//						System.out.println();
						i_temp=null;
					} 
				} 
				else
				{
					newpop[l].CopyIndividual(oldpop[i], TRUE);
					newpop[l + 1].CopyIndividual(oldpop[j],TRUE);
//					System.out.printf("%s","reproduction");
//					System.out.println();
//					_temp1="";
//					_temp1=newpop[l].chrom.TreeToStringN(newpop[l].chrom);
//					System.out.printf("%s", _temp1);			
//					System.out.println();
//					_temp2="";
//					_temp2=newpop[l+1].chrom.TreeToStringN(newpop[l+1].chrom);
//					System.out.printf("%s", _temp2);			
//					System.out.println();					
				}
				// mutation test
				_flippm=Flip(pmutate);
//				System.out.printf("pmutation:%d", _flippm);
//			    System.out.println();
				if(_flippm == 1) {					
					individual[] m_individual=new individual[1];
					int[] _mt=new int[1];
					_nm=new node[1];
					
					if(this.ReplaceSubTree(newpop[l], m_individual, 15, TRUE,_mt,_nm)==TRUE)
					{
					newpop[l]=new individual();
					newpop[l].CopyIndividual(m_individual[0], TRUE);
					
					newpop[l].evaluated=FALSE;
					
//					System.out.printf("%s","mutation "+l+" replacepoint:"+_mt[0]);
//					System.out.println();
//					_temp1="";
//					_temp1=newpop[l].chrom.TreeToStringN(_nm[0]);
//					System.out.printf("%s", _temp1);			
//					System.out.println();
//					_temp1="";
//					_temp1=newpop[l].chrom.TreeToStringN(newpop[l].chrom);
//					System.out.printf("%s", _temp1);			
//					System.out.println();					
					nmutate++;
					m_individual=null;
				
					}
				}
				
				
				if(Flip(pmutate) == 1) {
					individual[] m_individual1=new individual[1];
					int[] _mt=new int[1];
					_nm=new node[1];
					
					if(this.ReplaceSubTree(newpop[l + 1], m_individual1, 15, TRUE,_mt,_nm)==TRUE)
					{
					newpop[l+1]=new individual();
					newpop[l+1].CopyIndividual(m_individual1[0], TRUE);	
					
					newpop[l+1].evaluated=FALSE;
//					System.out.printf("%s","mutation "+(l+1)+" replacepoint:"+_mt[0]);
//					System.out.println();
//					_temp2="";
//					_temp2=newpop[l+1].chrom.TreeToStringN(_nm[0]);
//					System.out.printf("%s", _temp2);			
//					System.out.println();
//					_temp2="";
//					_temp2=newpop[l+1].chrom.TreeToStringN(newpop[l+1].chrom);
//					System.out.printf("%s", _temp2);			
//					System.out.println();
					nmutate++;
					m_individual1=null;
				
					}
					
					
				}
				l += 2;
			}

//			constructiveRate[gen] = g_numOfBetter / (2.0 * g_ncross);
//			semanticDistance[gen] = g_sd / (2.0 * g_ncross);
			
			gen++;
			for(k = 0; k < poplen; k++)
			{
				oldpop[k]=null;
				oldpop[k]=new individual();
				oldpop[k].CopyIndividual(newpop[k], TRUE);
			}
			
			
		}
		
	}

	
	//phuong thuc tien hoa
    void EvolutionWithElitism(){
		  	int i, j, l, k;
			//individual[] temp;
			byte _flippc=0,_flippm=0;
//			String _temp1="", _temp2="";
			int[] _cp;
			node[] _nd,_nm;
			// Khoi tao quan the dau tien 
			RampedInit(6, 0.5);
			//RampedInitOffLine(6, 0.5,15);
			
//			constructiveRate = new double[NUMGEN];
//			semanticDistance = new double[NUMGEN];
			timeTournament=0;			
			long timeTS=0;
			
			gen = 0;
			while(gen < generation) {		
					
				
				ComputeFitness();
				
//				int g_ncross = 0;
//				double g_sd = 0;
				
//				PrintToFile();
	//----------------------
//				FileInputStream instream = null;
//				PrintStream outstream = null;
//				PrintStream console = System.out;
//				try {
//					outstream = new PrintStream(new FileOutputStream("c:/result/" + "detail_"+ gen + ".txt"));
//					System.setOut(outstream);
//				} catch(Exception e) {
//					System.err.println("Error Occurred.");
//				}
//				for(i = 0; i < poplen; i++) {
//					System.out.printf("%3.5f  ",oldpop[i].fitness);
//					if((i+1) % 10 == 0) {
//						System.out.println();
//					}
//				}
//				for(int ii = 0; ii < poplen; ii++) {			
//					_temp1="";
//					_temp1=oldpop[ii].chrom.TreeToStringN(oldpop[ii].chrom);
//					System.out.printf("%s",ii+" "+ _temp1);			
//					System.out.println();
//				}
				
				// copy anh tot nhat sang the he sau	 	
				newpop[0].CopyIndividual(bestcurrent[gen], TRUE);
				// dot bien anh thu 2
			  
				l=1;	
				
				long starttime = System.currentTimeMillis();				
				i = StaticTourSelectSmall(TOURSIZE);				
				starttime = System.currentTimeMillis() - starttime;
				
				timeTS+=starttime;
				
				
			    newpop[l].CopyIndividual(oldpop[i], TRUE);
				individual[] m_individual0=new individual[1];
				int[] _mt0=new int[1];
				_nm=new node[1];
				this.ReplaceSubTree(newpop[l], m_individual0, 15, TRUE,_mt0,_nm);
				newpop[l]=new individual();
				newpop[l].CopyIndividual(m_individual0[0], TRUE);			
				newpop[l].evaluated=FALSE;		
//				
//				m_individual0=null;
	//------------------------		
				l = 2;			
				while(l < poplen) {
				//	System.out.println("Generation "+ String.valueOf(gen)+":");
										
					starttime = System.currentTimeMillis();
					
					i = StaticTourSelectSmall(TOURSIZE);
					j = StaticTourSelectSmall(TOURSIZE);
					
					starttime = System.currentTimeMillis() - starttime;					
					timeTS+=starttime;
//					
//						
				    _flippc=Flip(pcross);
//				    System.out.printf("pcross:%d", _flippc);
//				    System.out.println();
					if(_flippc == 1) {					
						individual[] i_temp=new individual[2];
						_cp=new int[2];
						_nd=new node[2];
				//	
				//		if(SubTreeSwapWithTTest(oldpop[i], oldpop[j], i_temp,_cp,_nd) == TRUE)
						
						if(SubTreeSwap(oldpop[i], oldpop[j], i_temp,_cp,_nd) == TRUE)
						{
							newpop[l].CopyIndividual(i_temp[0],TRUE);
							newpop[l+1].CopyIndividual(i_temp[1],TRUE);
							
							newpop[l].evaluated=FALSE;
							newpop[l+1].evaluated=FALSE;
//							
							ncross++;
							i_temp=null;
							
							
//							// semantic distance
//							g_sd += newpop[l].getSemanticDistance(oldpop[i]);
//							g_sd += newpop[l+1].getSemanticDistance(oldpop[j]);							
//							g_ncross++;
							
//							g_sd += oldpop[i].getSemanticDistance(oldpop[j]);//												
//							g_ncross++;
						}
						else
						{						
							newpop[l].CopyIndividual(oldpop[i], TRUE);
							newpop[l + 1].CopyIndividual(oldpop[j],TRUE);
//							System.out.printf("%s","reproduction");
//							System.out.println();
//							_temp1="";
//							_temp1=newpop[l].chrom.TreeToStringN(newpop[l].chrom);
//							System.out.printf("%s", _temp1);			
//							System.out.println();
//							_temp2="";
//							_temp2=newpop[l+1].chrom.TreeToStringN(newpop[l+1].chrom);
//							System.out.printf("%s", _temp2);			
//							System.out.println();
							i_temp=null;
						} 
					} 
					else
					{
						newpop[l].CopyIndividual(oldpop[i], TRUE);
						newpop[l + 1].CopyIndividual(oldpop[j],TRUE);
//						System.out.printf("%s","reproduction");
//						System.out.println();
//						_temp1="";
//						_temp1=newpop[l].chrom.TreeToStringN(newpop[l].chrom);
//						System.out.printf("%s", _temp1);			
//						System.out.println();
//						_temp2="";
//						_temp2=newpop[l+1].chrom.TreeToStringN(newpop[l+1].chrom);
//						System.out.printf("%s", _temp2);			
//						System.out.println();					
					}
					// mutation test
					_flippm=Flip(pmutate);
//					System.out.printf("pmutation:%d", _flippm);
//				    System.out.println();
					if(_flippm == 1) {					
						individual[] m_individual=new individual[1];
						int[] _mt=new int[1];
						_nm=new node[1];
						this.ReplaceSubTree(newpop[l], m_individual, 15, TRUE,_mt,_nm);
						newpop[l]=new individual();
						newpop[l].CopyIndividual(m_individual[0], TRUE);
						
						newpop[l].evaluated=FALSE;
						
//						System.out.printf("%s","mutation "+l+" replacepoint:"+_mt[0]);
//						System.out.println();
//						_temp1="";
//						_temp1=newpop[l].chrom.TreeToStringN(_nm[0]);
//						System.out.printf("%s", _temp1);			
//						System.out.println();
//						_temp1="";
//						_temp1=newpop[l].chrom.TreeToStringN(newpop[l].chrom);
//						System.out.printf("%s", _temp1);			
//						System.out.println();					
						nmutate++;
						m_individual=null;
					}
					else
					{
//						_temp1="";
//						_temp1=newpop[l].chrom.TreeToStringN(newpop[l].chrom);
//						System.out.printf("%s", _temp1);			
//						System.out.println();
					}
					
					if(Flip(pmutate) == 1) {
						individual[] m_individual1=new individual[1];
						int[] _mt=new int[1];
						_nm=new node[1];
						this.ReplaceSubTree(newpop[l + 1], m_individual1, 15, TRUE,_mt,_nm);
						newpop[l+1]=new individual();
						newpop[l+1].CopyIndividual(m_individual1[0], TRUE);	
						
						newpop[l+1].evaluated=FALSE;
//						System.out.printf("%s","mutation "+(l+1)+" replacepoint:"+_mt[0]);
//						System.out.println();
//						_temp2="";
//						_temp2=newpop[l+1].chrom.TreeToStringN(_nm[0]);
//						System.out.printf("%s", _temp2);			
//						System.out.println();
//						_temp2="";
//						_temp2=newpop[l+1].chrom.TreeToStringN(newpop[l+1].chrom);
//						System.out.printf("%s", _temp2);			
//						System.out.println();
						nmutate++;
						m_individual1=null;
					}
					else
					{
//						_temp2="";
//						_temp2=newpop[l+1].chrom.TreeToStringN(newpop[l+1].chrom);
//						System.out.printf("%s", _temp2);			
//						System.out.println();
					}
					l += 2;
				}
//				for(int ii = 0; ii < poplen; ii++) {			
//					_temp1="";
//					_temp1=oldpop[ii].chrom.TreeToStringN(newpop[ii].chrom);
//					System.out.printf("%s",ii+" "+ _temp1);			
//					System.out.println();
//				}
//				outstream.close();
//				System.setOut(console);
				// Thống kê tỷ lệ tTest
				
			
//				semanticDistance[gen] = g_sd / (2.0 * g_ncross);
				timeTournament = timeTS;
				
				
				gen++;
				for(k = 0; k < poplen; k++)
				{
					oldpop[k]=null;
					oldpop[k]=new individual();
					oldpop[k].CopyIndividual(newpop[k], TRUE);
				}
			}
		}
	//
	
}
