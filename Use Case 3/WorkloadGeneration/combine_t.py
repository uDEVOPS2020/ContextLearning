import pandas as pd
import sys
import datetime as dt

df = pd.read_csv("finals/FINAL.csv")

df1 = df.loc[:, df.columns.str.contains("mem")]
df2 = df.loc[:, df.columns.str.contains("cpu")]

df = pd.concat([df1, df2], axis=1)

time = dt.datetime.now()
temp_list = []

for i in range(len(df.index)):
    time = time+dt.timedelta(seconds=5)
    temp_list.append(time)

dft = pd.DataFrame(data=temp_list, columns=['DateTime'])

dfc = pd.concat([dft, df], axis=1)

dfc.to_csv("dataset.csv", index=False)
