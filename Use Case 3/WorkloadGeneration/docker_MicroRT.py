import pandas as pd
import sys

s, fn = sys.argv

df = pd.read_csv("docker_"+fn+".csv",delim_whitespace=True)
df_cmp = pd.read_csv("esec_"+fn+"_stats.csv")

df = df[["ID","NAME","USAGE"]]

df["ID"] = df["ID"].str.replace("docker-compose-manifests_ts-","")
df["ID"] = df["ID"].str.replace("docker-compose-manifests_","")
df["ID"] = df["ID"].str.replace("_1","")
df["ID"] = df["ID"].str.replace("-","")
df["NAME"] = df["NAME"].str.replace("%","")
df["USAGE"] = df["USAGE"].str.replace("%","")

# print(df)

new_df = df.T.iloc[[1]].rename(columns = df["ID"]).reset_index().join(df.T.iloc[[2]].rename(columns = df["ID"]).reset_index(), lsuffix='_cpu', rsuffix='_mem')
new_df = new_df.drop(["index_cpu","index_mem"], axis=1)
new_df = new_df.sort_index(axis=1)



listCol = df_cmp.T.iloc[[1]]
# print(listCol)

for column in new_df:
    if new_df[column].name.replace("_cpu","").replace("_mem","") not in listCol.values: 
        # print(column)
        # print(new_df[column])
        new_df = new_df.drop([column], axis=1)

# print(new_df)

new_df.to_csv("finals/esecD_T_"+fn+".csv",index=False)
new_df.to_csv("finals/esecD_T.csv",mode='a',index=False, header=False)