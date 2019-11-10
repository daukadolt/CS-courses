import numpy as np
import math
from scipy.special import expit
from sklearn.metrics import confusion_matrix, accuracy_score
import random
import Perceptron_Lib

in_dim = 785
out_dim = 10
num_epochs = 5

def load_data(file_name):
    data_file = np.loadtxt(file_name, delimiter=',')
    dataset = np.insert(data_file[:, np.arange(1, in_dim)]/255, 0, 1, axis=1)
    data_labels = data_file[:, 0]
    return dataset, data_labels

# Initialize weight matrix with random weights
weight = np.random.uniform(-0.05,0.05,(in_dim,out_dim))

# Load Training and Test Sets :
print("\nLoading Training Set")
train_data, train_labels = load_data('train.csv')
print("\nLoading Test Set\n")
test_data, test_labels = load_data('test.csv')

arr_train_acc = []
arr_test_acc = [] 

for i in range(1, num_epochs+1):
    
    pred_train_labels = Perceptron_Lib.get_predictions(train_data, weight)  # Test network on training set and get training accuracy
    curr_accu = accuracy_score(train_labels, pred_train_labels)

    print("Epoch " + str(i) + " :\tTraining Set Accuracy = " + str(curr_accu))

    pred_test_labels = Perceptron_Lib.get_predictions(test_data, weight)  # Test network on test set and get accuracy on test set
    test_accu = accuracy_score(test_labels, pred_test_labels)
    print("\t\tTest Set Accuracy = " + str(test_accu))

    weight = Perceptron_Lib.train(train_data, train_labels, weight)    # Train the network

    arr_train_acc.append(curr_accu)
    arr_test_acc.append(test_accu)

# Test network on test set and get test accuracy
pred_test_labels = Perceptron_Lib.get_predictions(test_data, weight)  # Test network on test set and get accuracy on test set
test_accu = accuracy_score(test_labels, pred_test_labels)

# Confusion Matrix	
print("\t\tFinal Accuracy = " + str(test_accu) + "\n\nConfusion Matrix :\n")
print(confusion_matrix(test_labels, pred_test_labels))
print("\n")