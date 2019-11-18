import numpy as np 
import math 
from scipy.special import expit 
import random 
 
in_dim = 785 # input dimension 
out_dim = 10 # number of classes (0-9) 
eta = 0.01 # Learning rate. You might try different rates (e.g. 0.001, 0.01, 0.1) to maximize the accuracy 
 
def Weight_update(feature, label, weight_i2o): 
 ## 
 #Update the weights for a train feature. 
  # Inputs: 
   # feature: feature vector (ndarray) of a data point with 785 dimensions. Here, the feature represents a handwritten digit  
            # of a 28x28 pixel grayscale image, which is flattened into a 785-dimensional vector (include bias) 
   # label: Actual label of the train feature  
   # weight_i2o: current weights with shape (in_dim x out_dim) from input (feature vector) to output (digit number 0-9) 
  # Return: updated weight 
 ## 
 #"*** YOUR CODE HERE ***" 
  
  
	predicted_vector = np.dot(weight_i2o.T, feature) # <--- obtaining output layer values
	feature = feature.reshape(len(feature), 1)  # <--- feature vector is of shape (785, ) which cannot be used in dot products. Reshaping it into (785, 1)
	predicted_vector = np.asarray([0 if i != np.argmax(predicted_vector) else 1 for i in range(out_dim)])  # <--- changing predicted_vector by leaving max node as 1 and rest as 0
	predicted_vector = predicted_vector.reshape(1, len(predicted_vector))  # <--- reshaping into (1, 10) so that dot product at weight_i2o update would work at line 30
	actual_vector = np.asarray([0 if i != int(label) else 1 for i in range(out_dim)])  # <--- since we are only given label as a number of form 5.0 for example, we need to set 5th index of vector of length 10 as 1 and the rest as 0s as per PDF instructions on Moodle
	actual_vector = actual_vector.reshape(1, len(actual_vector))  # <--- reshaping actual_vector just like predicted_vector

	weight_i2o += eta*np.dot(feature, (actual_vector - predicted_vector))  # <--- updating weight_i2o
	
	return weight_i2o 
     
     
 
def get_predictions(dataset, weight_i2o): 
 #""" 
 #Calculates the predicted label for each feature in dataset. 
  # Inputs: 
   # dataset: a set of feature vectors with shape   
   # weight_i2o: current weights with shape (in_dim x out_dim) 
  # Return: list (or ndarray) of predicted labels from given dataset 
 #""" 
 #"*** YOUR CODE HERE ***" 
	product = dataset.dot(weight_i2o)  # <--- obtaining output layers for all inputs
	return np.argmax(product, axis=1)  # <--- reducing each output layer into single number which is an index of the max node
  
     
  
 
def train(train_set, labels, weight_i2o): 
 #""" 
 #Train the perceptron until convergence. 
 # Inputs: 
  # train_set: training set (ndarray) with shape (number of data points x in_dim) 
  # labels: list (or ndarray) of actual labels from training set 
  # weight_i2o: 
 # Return: the weights for the entire training set 
 #""" 
	for i in range(0, train_set.shape[0]):         
		weight_i2o = Weight_update(train_set[i, :], labels[i], weight_i2o)         
	return weight_i2o