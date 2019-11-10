import numpy as np
import math
from scipy.special import expit
import random


in_dim = 785 # input dimension
hidden_dim = 100 # hidden layer size
out_dim = 10 # number of classes (0-9)
eta = 0.1 # Learning rate. You might try different rates (e.g. 0.001, 0.01, 0.1) to maximize the accuracy

# matrix to store the activation h1...hk 
hl_input = np.zeros((1,hidden_dim+1))
hl_input[0,0] = 1

def weight_update(feature, label, weight_i2h, weight_h2o): 
    # """
	# Update the weights for a train feature.
		# Inputs:
			# feature: feature vector (ndarray) of a data point with 785 dimensions. Here, the feature represents a handwritten digit 
			         # of a 28x28 pixel grayscale image, which is flattened into a 785-dimensional vector (include bias)
			# label: Actual label of the train feature 
			# weight_i2h: current weights with shape (in_dim x hidden_dim) from input (feature vector) to hidden layer
			# weight_h2o: current weights with shape (hidden_dim x out_dim) from hidden layer to output (digit number 0-9)
		# Return: updated weight
	# """
	######## feed forward ##################
	#"""compute activations at hidden layer"""
    scores_hl = np.dot(feature.reshape(1, in_dim), weight_i2h)
    sig_hl = expit(scores_hl)
    hl_input[0,1:] = sig_hl
	
	#"""compute activations at output layer"""
    scores_ol = np.dot(hl_input, weight_h2o)
    sig_ol = expit(scores_ol)    
	
	####### Update weights ##########
	#"*** YOUR CODE HERE ***"
	
   
      
    return weight_i2h, weight_h2o

def get_predictions(dataset, weight_i2h, weight_h2o):
	# """
	# Calculates the predicted label for each feature in dataset.
		# Inputs:
			# dataset: a set of feature vectors with shape  
			# weight_i2h: current weights with shape (in_dim x hidden_dim) from input (feature vector) to hidden layer
			# weight_h2o: current weights with shape (hidden_dim x out_dim) from hidden layer to output (digit number 0-9)
		# Return: list (or ndarray) of predicted labels from given dataset
	# """
	# """
	# Hint: follow the feed forward step above (from lines 28-35) to compute activations at output layer. Then, find the label
	# that returns highest value of activation.
	# """
	# "*** YOUR CODE HERE ***"
   
   
   

def train(train_set, labels, weight_i2h, weight_h2o):
    for i in range(0, train_set.shape[0]):        
        weight_i2h, weight_h2o = weight_update(train_set[i, :], labels[i], weight_i2h, weight_h2o)        
    return weight_i2h, weight_h2o