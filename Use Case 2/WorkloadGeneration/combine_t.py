import pandas as pd
import sys
import datetime as dt

df = pd.read_csv("past_executions/ex_10m_step_10-150.csv")

time = dt.datetime.now()
temp_list = []

for i in range(len(df.index)):
    time = time+dt.timedelta(seconds=5)
    temp_list.append(time)

dft = pd.DataFrame(data=temp_list, columns=['DateTime'])

dfc = pd.concat([dft, df], axis=1)

print(dfc)

dfc.to_csv("ex_10m_step_10-150_DT.csv", index=False)
