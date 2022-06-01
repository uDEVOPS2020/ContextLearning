
import edu.cmu.tetrad.data.DataSet;
import edu.cmu.tetrad.util.Parameters;
import edu.cmu.tetrad.data.BoxDataSet;
import edu.cmu.tetrad.data.DataBox;
import edu.cmu.tetrad.data.DoubleDataBox;
import edu.cmu.tetrad.graph.Edge;
import edu.cmu.tetrad.graph.Graph;
import edu.cmu.tetrad.sem.SemEstimator;
import edu.cmu.tetrad.sem.SemIm;
import edu.cmu.tetrad.sem.SemPm;
import edu.cmu.tetrad.algcomparison.algorithm.Algorithm;
import edu.cmu.tetrad.algcomparison.algorithm.AlgorithmFactory;
import edu.cmu.tetrad.algcomparison.algorithm.continuous.dag.Lingam;
import edu.cmu.tetrad.algcomparison.score.ConditionalGaussianBicScore;
import edu.cmu.tetrad.data.DataModel;
import edu.cmu.tetrad.util.DataConvertUtils;
import edu.cmu.tetrad.util.Params;
import edu.pitt.dbmi.data.reader.ContinuousData;
import edu.pitt.dbmi.data.reader.DataColumn;
import edu.pitt.dbmi.data.reader.Delimiter;
import edu.pitt.dbmi.data.reader.tabular.TabularColumnFileReader;
import edu.pitt.dbmi.data.reader.tabular.TabularColumnReader;
import edu.pitt.dbmi.data.reader.tabular.TabularDataFileReader;
import edu.pitt.dbmi.data.reader.tabular.TabularDataReader;

import java.util.Iterator;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Sem {
	
	public static void main(String[] args) {

    
	/*****
	 * Usage: java causal_estiamte.java <path/to/filename>.csv
	 * 
	 * @param Input: The CSV filename 
	 *     
	 * @return  It gives the causal and structural equation model as a textual file as well as graphical .dot file
	 * 
	 *  Code customized from examples for using the Tetrad open source library (https://www.tetradcausal.app/) 
	 */
	
	//args[0] is the file name. 
	Path dataFile = Paths.get(args[0]);
	
    // data file settings
    final Delimiter delimiter = Delimiter.COMMA;  
    
    final boolean hasHeader = true;
    final boolean isDiscrete = false;

    TabularColumnReader columnReader = new TabularColumnFileReader(dataFile, delimiter);

    // create the column  object - describes the data in each column
    DataColumn[] dataColumns;

    // read in all columns and set all columns to continuous
    ContinuousData data = null; 
    DataModel dataModel = null;
    try {
		dataColumns = columnReader.readInDataColumns(isDiscrete);


		// setup data reader
		TabularDataReader dataReader = new TabularDataFileReader(dataFile, delimiter);

		// if this is a mixed dataset determine which columns are discrete based on number of unique values in column i.e, updates dataColumns[]
		//dataReader.determineDiscreteDataColumns(dataColumns, numberOfCategories, hasHeader);

		// actually read in the data
		data = (ContinuousData) dataReader.read(dataColumns, hasHeader, null);
		dataModel = DataConvertUtils.toDataModel(data);
	} catch (IOException e) {e.printStackTrace();}
    
    double[][] dData = data.getData();

    // Select search algorithm
    // We use Lingam by default, but other algorithms are possible (see the Tetrad library at https://www.tetradcausal.app/), such as FGES, GFCI, RFCI. 
    // which allows you to easily swap out ind. tests and scores

   Algorithm lingam = null;
   try {
	lingam = AlgorithmFactory.create(Lingam.class, null, ConditionalGaussianBicScore.class);
   } catch (IllegalAccessException | InstantiationException e) {e.printStackTrace();}

   /*******Example with FGES   
    * // For continuous data can use sem bic score
    *    Algorithm fges = AlgorithmFactory.create(Fges.class, null, SemBicScore.class);
    *
    * // For discrete  data can use sem bic score
    *	 Algorithm fges = AlgorithmFactory.create(Fges.class, null, BdeuScore.class);
    *
    * // For mixed data can use Conditional Gaussian
  	*	 Algorithm fges = AlgorithmFactory.create(Lingam.class, null, ConditionalGaussianBicScore.class);
  	* 
  	*******/
    
   // Set algorithm parameters
    Parameters parameters = new Parameters();
    
    // fges parameters
    parameters.set(Params.MAX_ITERATIONS, 2000);
    parameters.set(Params.FAST_ICA_TOLERANCE, 1E-6);
    parameters.set(Params.PENALTY_DISCOUNT, 2.0);
    parameters.set(Params.FAITHFULNESS_ASSUMED, true);
    parameters.set(Params.VERBOSE, true);
    parameters.set(Params.ADD_ORIGINAL_DATASET, true);

    // parameters for conditional gaussian
    parameters.set(Params.DISCRETIZE, false);

    // perform the search
    
    System.out.println("\n\n *** Running the algorithm... **** \n\n");

    
    Graph graph = lingam.search(dataModel, parameters);

    // output the graph
    System.out.println("\n\n *** Printing the graph ... **** \n\n");
    System.out.println(graph.toString().trim());
    System.out.flush();
	
    /***** Applying the SEM construction and fitting. *****/

    SemPm pm = new SemPm(graph);
 
    DataBox db = new DoubleDataBox(dData);
    DataSet ds = new BoxDataSet(db, graph.getNodes()) ;
  
    SemIm im = new SemIm(pm);
    SemEstimator se = new SemEstimator(ds, pm);
    
    im = se.estimate();
    
    
    System.out.println("***** Summary output ***** \n\n");
    
    System.out.println("Estimate successful: " +im.isEstimated());
    System.out.println("\n"); 
    
    Iterator<Edge> it = graph.getEdges().iterator(); 
    while (it.hasNext()) {
    	Edge e = it.next() ;
    	
    	System.out.println("Model coefficients (for the edge "+e.toString()+") "+im.getEdgeCoef(e));
    }
    
    double[] means = im.getMeans();
    
    System.out.println("\n\n");
    double[] std = new double[graph.getNodes().size()];
    for (int i = 0; i<means.length; i++) {
    	System.out.println("Means resulting from the estiamtor (for the Node "+graph.getNodes().get(i)+"): "+means[i]); 
    	std[i] = im.getStdDev(graph.getNodes().get(i), se.getEstimatedSem().getImplCovar(true));
    	System.out.println("Std resulting from the estiamtor (for the Node "+graph.getNodes().get(i)+"): "+std[i]);
    	System.out.println("\n");
    }
    
/*
    Iterator<Node> it2 = graph.getNodes().iterator(); 
    while (it2.hasNext())
    	System.out.println("Model intercepts (per node) "+im.getIntercept(it2.next()));
  */  
   
    System.out.println("\nThe model has the following p-value: "+im.getPValue()); 
    
}
}
