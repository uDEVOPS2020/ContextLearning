#!/bin/sh
# 60 sec / 5 sec = 12 * n (min) => num esecuzioni


numSamples=$((12*$1))
numUser=10

for i in $(seq 1 1 $numSamples)
do  
#   esecuzione variabile up/down
    # if (($i > $numSamples/2))
    # then
    #     ((numUser=numUser-5))
    # else
    #     ((numUser=numUser+5))
    # fi

#   esecuzione singola anomalia
#    if (($i > $numSamples/2))
#    then
#        ((numUser=150))
#    fi

    #   esecuzione singola anomalia
 #   if (($i > ($numSamples/2)+6))
 #   then
 #       ((numUser=10))
 #   fi

    echo "-------------------------------------------------------------------------------"
    echo "NUMUSER: $numUser"
    echo "Esecuzione: $i"
    echo "-------------------------------------------------------------------------------"
    locust --headless --users $numUser --spawn-rate $numUser -H http://localhost:8080 --run-time 5s --csv=esec_$i
    docker container stats --no-stream >> docker_$i.csv
    python3 perMicroRT.py $i
    python3 docker_MicroRT.py $i
    python3 combine.py $i

    find . -name "*.csv" -maxdepth 1 -type f -delete
done   

python3 combine_t.py
