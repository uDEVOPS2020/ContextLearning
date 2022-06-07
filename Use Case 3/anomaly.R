library(tidyverse)
library(readr)
library(anomalize)
library(patchwork)
library(gridExtra)

rFrame <- read_csv("./ex_10m_spike_30s_10-150_DT.csv",col_types = cols(DateTime = col_datetime(format = "%Y-%m-%d %H:%M:%S")))



count = as.integer(0)


l <- list()
for(i in names(rFrame)){
  count <- count +1
  if(count >1){
    p <- (rFrame %>% time_decompose(i, frequency = "12 seconds", trend="1 minutes", merge = TRUE) %>% anomalize(remainder) %>% time_recompose()) %>% plot_anomalies(ncol = 3, alpha_dots = 0.25) + ggtitle(i)
    l <- append(l,list(p))
  }
  
}

#wrap_plots(l)
grid.arrange(grobs = l, ncol = 6)
