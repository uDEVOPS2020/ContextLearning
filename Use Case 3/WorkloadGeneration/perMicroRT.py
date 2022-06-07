import pandas as pd
import sys

s, fn = sys.argv
df = pd.read_csv("esec_"+fn+"_stats.csv")

#,"Name","Request Count","Failure Count","Median Response Time","Average Response Time","Min Response Time","Max Response Time","Average Content Size","Requests/s","Failures/s","50%","66%","75%","80%","90%","95%","98%","99%","99.9%","99.99%","100%"
df = df.drop(["Request Count","Failure Count","Min Response Time","Max Response Time","Type","Average Content Size","Requests/s","Failures/s","50%","66%","75%","80%","90%","95%","98%","99%","99.9%","99.99%","100%"], axis=1)
df = df.drop(df.shape[0]-1)

df_2 = pd.DataFrame()

new_df = df.T.iloc[[1]].rename(columns = df["Name"]).reset_index().join(df.T.iloc[[2]].rename(columns = df["Name"]).reset_index(), lsuffix='_median_rt', rsuffix='_avg_rt')
new_df = new_df.drop(["index_median_rt","index_avg_rt"], axis=1)

print(new_df)

new_df.to_csv("finals/esec_T_"+fn+".csv",index=False)
new_df.to_csv("finals/esec_T.csv",mode='a',index=False, header=False)
