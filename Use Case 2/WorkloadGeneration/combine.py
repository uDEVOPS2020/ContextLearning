import pandas as pd
import sys

s, fn = sys.argv

df_d = pd.read_csv("finals/esecD_T_"+fn+".csv")
df_e = pd.read_csv("finals/esec_T_"+fn+".csv")

concat = pd.concat([df_e, df_d], axis=1)

print(concat)

concat.to_csv("finals/final"+fn+".csv",index=False)

if fn == "1":
    concat.to_csv("finals/FINAL.csv",mode='a',index=False, header=True)
else:
    concat.to_csv("finals/FINAL.csv",mode='a',index=False, header=False)