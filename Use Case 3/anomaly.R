library(tidyverse)
library(readr)
library(anomalize)
library(patchwork)
library(gridExtra)

args = commandArgs(trailingOnly=TRUE)

rFrame <- read_csv(args[1],col_types = cols(DateTime = col_datetime(format = "%Y-%m-%d %H:%M:%S")))

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
#grid.arrange(grobs = l, ncol = 6)

g <- arrangeGrob(grobs = l, ncol = 6)
ggsave(file="anomalies.jpg", g, width = 20, height = 10)