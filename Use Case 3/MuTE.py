
from idtxl.multivariate_te import MultivariateTE
from idtxl.data import Data
from idtxl.visualise_graph import plot_network
import matplotlib.pyplot as plt
import pandas as pd
import sys

#print("This is the name of the script: ", sys.argv[0])
#print("Number of arguments: ", len(sys.argv))
print("Input file: ", sys.argv[1])
#print("Target column index: ", sys.argv[2])
data = pd.read_csv(sys.argv[1], error_bad_lines=False)
df = data.drop('DateTime', 1)

print("Dropping data-time column...")

print(df)
data_np = df.to_numpy()
#column = [int(sys.argv[2])]

mydata = Data(data_np, dim_order='sp')

print(mydata)

# b) Initialise analysis object and define settings
network_analysis = MultivariateTE()
settings = {'cmi_estimator': 'JidtGaussianCMI',
            'max_lag_sources': 4,
            'min_lag_sources': 1}

# c) Run analysis
results = network_analysis.analyse_network(settings=settings, data=mydata)

# d) Plot inferred network to console and via matplotlib
results.print_edge_list(weights='max_te_lag', fdr=False)
results.get_single_target(0, fdr=False)
plot_network(results=results, weights='max_te_lag', fdr=False)
plt.show()
