#Semantic Tournament Selection for Genetic Programming based on Statistical Analysis of Error


This code provides a Java implementation of statistics tournament selection methods as described in "Semantic Tournament Selection for Genetic
Programming based on Statistical Analysis of Error"  by Thi Huong Chu et al.

# Methods:
+ GP/GP: Standard Tournament Selection,
+ GP/TS-R: Statistics tournament selection with random,
+ GP/TS-S: Statistics test tournament selection with size,
+ GP/TS-P: Statistics tournament selection with probability,l
+ GP/RDO: Crossover- random desired operator (RDO),
+ GP/TS-RDO:The method which combines TS-S with RDO.

# Evolutionary Parameter Values:

You can set evolutionary parameter values in file GP/Common/Const.java. Here are some basic parameters you can set in the file:
+ POPSIZE:  Population size,
+ NUMGEN: Generations, 
+ TOURSIZE: Tournament size,
+ PCROSS: Crossover probability,
+ PMUTATE: Mutation probability.

# Prolem Paramater values:
You set up the information of the problem in file GP/Common/Const.java:
+ PROBLEM: The name of the problem,
+ NUMVAR: The number of features,
+ NUMFITCASE: The number of samples in training data,
+ NUMFITTEST: The number of samples in testing data.
