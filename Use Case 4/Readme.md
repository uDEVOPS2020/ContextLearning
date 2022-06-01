## Use case 4 

The folder "Case 4" contains: 
- **Dataset**. This is the dataset used as illustrative example for this use case. The dataset consists of commit data for 6 (VA BENE ANCHE UNA) open source applications. It is cloned from a Just-In-Time Defect Prediction (JIT-DP) research work repository [1]. The generation procedure is as follows: PROCEDURA DA PAPER: data extraction, labelling by the SZZ algorithm.

Each subject dataset has the following columns: 
 ... |Nome metriche commit | ...  | .. ...| ... |label=Buggy| 

Each row refers to a commit. 

- *Benchmark.txt*. The file contains links to the applications under analysis.  

- **Results**. It contains the results of applying the model to the datasets. 
 
- **Code**. Python code files/scripts to: i) perform defect prediction on a dataset in the same format as the Dataset's folder files; this includes code for training the C2Vec model for JIT-DP and corse-validating results. The files are changed from the original code [1] to add the computation of feature stability. Feature stability is computed according to the algorithm described in [2]. ii) present the results. 

Code to create datasets (hence extracting GitHib metrics and labelling dataset) is available from the reference paper repository [3].  

To reproduce:  use the code files, steps i) and ii). 

*Prerequisites*: 
...

*Commands*: 

./vedi comandi paper 			 #for JIT-DP
./compute_stability			 #for features stability computation 
./rank_and_present.py			 #for ranking and presenting results, from most to least stable metrics
 
To replicate on a different subject, use the code in the repository of the JIT-DP paper we started from [3], then apply steps i) and ii). 

*Commands*:
... 
...
