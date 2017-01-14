package Common;

public interface Const {
	public static final int		MAXSTRING	= 60000;				// max of a tring (for _sbuffer)
	public static final byte	TRUE			= 1;
	public static final byte	FALSE			= 0;
	public static final int		MAXNAME		= 6;						// max+1 length of name of a symbol
	public static final int		MAXDEPTH		= 17;					// max size of chromosome
	public static final int		MAXTOUR		= 10;					// max tournement size
	public static final int		MAXATEMPT	= 100;					// max atempt for choosing a site of a chromosome
	public static final int		MAXFUNCTION	= 20;
	public static final int		MAXTERMINAL	= 40;
	public static final int		MAXNODE		= 3000;
	public static final double	VOIDVALUE	= -1523612789.21342;
	
	
	public final double PCROSS = 0.9;
	public final double PMUTATE = 0.1;
	
    public final int TOURSIZE=5;
    
    
    public final int MAX_KOZA=5;
    			
    public final int		NUMSIZE	=10;
	public final double		USIM	=0.6;
	public final double		LSIM	=0.0;
	

	public static final int NRUN=100;
	public static final int POPSIZE=500;
    public static final int NUMGEN=100;
    
	
	public static final double	INFPLUS		=  Math.exp(700);
	public static final double	INFMINUS		= -Math.exp(700);
	public static final double	HUGE_VAL		= Math.exp(700);
	
	//SGX + SC
	public static final int NUM_TRIAL = 20;
	public static final double SGXMSC_PRO = 0.3;
	public final int SUBTREE_MAXSIZE = 80;
	public final int SUBTREE_MINSIZE = 2;
	public final int TR_MAXDEPTH = 4;
	
	//AGX RDO
	public final int TREELIBSIZE = 1000;
	public final int TREELIB_MAXDEPTH = 4;
	public final double STAR = Double.NaN;
	public final boolean KOZA_MUTATION = true;
	
	
	
	// Problems	


//	public static final String PROBLEM = "Protein_Tertiary_Structure";//11 300 300
//	public static final int		NUMVAR	=9;
//	public static final int		NUMFITCASE	= 1000;
//	public static final int		NUMFITTEST	= 1000;	
	
//	public static final String PROBLEM = "ccpp";//11 250 250
//	public static final int		NUMVAR	=4;
//	public static final int		NUMFITCASE	=1000;
//	public static final int		NUMFITTEST	=1000;
	
	
//	public static final String PROBLEM = "airfoil_self_noise";//11 250 250
//	public static final int		NUMVAR	=5;
//	public static final int		NUMFITCASE	=800;
//	public static final int		NUMFITTEST	=703;
	
//	public static final String PROBLEM = "3D_spatial_network";//11 300 300
//	public static final int		NUMVAR	=3;
//	public static final int		NUMFITCASE	= 750;
//	public static final int		NUMFITTEST	= 750;
	
//	public static final String PROBLEM = "ISTANBUL_STOCK_EXCHANGE";//11 300 300
//	public static final int		NUMVAR	=7;
//	public static final int		NUMFITCASE	= 270;
//	public static final int		NUMFITTEST	= 266;
	
//	public static final String PROBLEM = "yacht_hydrodynamics";//11 300 300
//	public static final int		NUMVAR	=6;
//	public static final int		NUMFITCASE	= 160;
//	public static final int		NUMFITTEST	= 148;
	
	
//	public static final String PROBLEM = "casp";//11 250 250
//	public static final int		NUMVAR	=9;
//	public static final int		NUMFITCASE	=100;
//	public static final int		NUMFITTEST	=100;
	
//	public static final String PROBLEM = "wpbc";//11 300 300
//	public static final int		NUMVAR	=31;
//	public static final int		NUMFITCASE	= 100;
//	public static final int		NUMFITTEST	= 98;
	
//	public static final String PROBLEM = "slump_test_Compressive";//11 250 250
//	public static final int		NUMVAR	=7;
//	public static final int		NUMFITCASE	=50;
//	public static final int		NUMFITTEST	=53;
	
//	public static final String PROBLEM = "slump_test_FLOW";//11 250 250
//	public static final int		NUMVAR	=7;
//	public static final int		NUMFITCASE	=50;
//	public static final int		NUMFITTEST	=53;
	
//	public static final String PROBLEM = "slump_test_SLUMP";//11 250 250
//	public static final int		NUMVAR	=7;
//	public static final int		NUMFITCASE	=50;
//	public static final int		NUMFITTEST	=53;
	
	
//	public static final String PROBLEM = "korns-12-20";//11 250 250
//	public static final int		NUMVAR	=5;
//	public static final int		NUMFITCASE	=20;
//	public static final int		NUMFITTEST	=20;
	
//	public static final String PROBLEM = "vladislavleva-2";//11 250 250
//	public static final int		NUMVAR	=1;
//	public static final int		NUMFITCASE	=100;
//	public static final int		NUMFITTEST	=221;
	
//	public static final String PROBLEM = "vladislavleva-4";//11 250 250
//	public static final int		NUMVAR	=5;
//	public static final int		NUMFITCASE	=500;
//	public static final int		NUMFITTEST	=500;
//	
	
//	public static final String PROBLEM = "vladislavleva-6";//11 250 250
//	public static final int		NUMVAR	=2;
//	public static final int		NUMFITCASE	=30;
//	public static final int		NUMFITTEST	=93636;
	
//	public static final String PROBLEM = "vladislavleva-8";//11 250 250
//	public static final int		NUMVAR	=2;
//	public static final int		NUMFITCASE	=50;
//	public static final int		NUMFITTEST	=1089;
	
	// korn 1,2,3,4,11,12,14,15
	
		public static final String PROBLEM = "korns-4";//11 300 300
		public static final int		NUMVAR	=5;
		public static final int		NUMFITCASE	= 100;
		public static final int		NUMFITTEST	= 10;
	
	
	
//	public static final String PROBLEM = "concrete";//11 250 250
//	public static final int		NUMVAR	=8;
//	public static final int		NUMFITCASE	=500;
//	public static final int		NUMFITTEST	=530;
	

//	public static final String PROBLEM = "vladislavleva-1";//11 250 250
//	public static final int		NUMVAR	=2;
//	public static final int		NUMFITCASE	=20;
//	public static final int		NUMFITTEST	=2025;
	

	
//	public static final String PROBLEM = "vladislavleva-5";//11 250 250
//	public static final int		NUMVAR	=3;
//	public static final int		NUMFITCASE	=300;
//	public static final int		NUMFITTEST	=2640;
	

//	public static final String PROBLEM = "vladislavleva-7";//11 250 250
//	public static final int		NUMVAR	=2;
//	public static final int		NUMFITCASE	=300;
//	public static final int		NUMFITTEST	=1000;
		
//	public static final String PROBLEM = "Keijzer-10";//11 300 300
//	public static final int		NUMVAR	=2;
//	public static final int		NUMFITCASE	= 100;
//	public static final int		NUMFITTEST	= 121;
	
//	public static final String PROBLEM = "Keijzer-15";//11 300 300
//	public static final int		NUMVAR	=2;
//	public static final int		NUMFITCASE	= 20;
//	public static final int		NUMFITTEST	= 3600;	

		
	

	
	public static final double	alpha=0.05;


	public final String DATADIR = "../Data/DataTest/";
	public final String OUTDIR = "../Result/Original/";
	
//	public final String DATADIR = "../Data/DataWithNoiseXY/";	
//	public final String OUTDIR = "../Result/NoiseXY/";
}
