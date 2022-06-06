
## Use case 1. Test case prioritization via Learning to rank techniques. 

This folder contains: 
- **Original Dataset**. This is the dataset used as illustrative example for this use case. It is derived by running a load on a well-known open-source benchmark for microservice  architecture (MSA), named Train Ticket [1].  The application simulates a train ticket booking system, composed of 41 microservices communicating to each other via REST over HTTP. Train ticket is  polyglot (e.g., Java, golang, Node.js, etc). 

Dataset has the following columns: 

testID | HTTP status code | Response Time | URL | HTTP method | Input Classes | ...

Each row is an executed test. 

- **Training and test sets generator**. The training and test sets are generated through the csv_parsing.py script. The datasets are encoded in the format required by RankLib to perform the training and the prioritization.

- **Prioritization**. Response Time and status code are both considered to perform the prioritization. The ranking score is computed as follows: ranking_score = response_code+1/(response_time)*100. The response codes higher than or equal to 400 correspond to failed tests; at the same time, a lower  response time implies a higher  priority of the test. As result, the higher the value of the ranking score, the higher the priority of the considered test.

- **Results**. The output of the Learning to Rank algorithms are ordered lists of test IDs (column "1").


- **Code**. Python code files/scripts to: i) generate training and test sets, ii) train and execute the Lerning to Rank techniques, iii) build the ordered list of testIDs. 

*Prerequisites*: 

Python (version >3), JVM/JRE (version > 1.8). 

Libraries: 

RankLib library (Dang, V. “The Lemur Project-Wiki-RankLib.” Lemur Project,[Online]. Available at http://sourceforge.net/p/lemur/wiki/RankLib)


*Commands*: 

>  You can run the "sh ranking.sh" script to perform the ranking of the selected test dataset.

