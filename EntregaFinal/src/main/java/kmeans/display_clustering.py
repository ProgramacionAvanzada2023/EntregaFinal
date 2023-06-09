#import csv
import numpy as np
import matplotlib.pylab as plt
import sys

if __name__ == "__main__":
	#filename = "face.csv"
	#filename = "basic1.csv"
	filename = sys.argv[1]
	data = np.genfromtxt(filename, skip_header=1, delimiter=',')
	if (data.shape[1]>3):
		first_column = 1
	else:
		first_column = 0
	feats = data[:,first_column:-1]	
	labels = data[:,-1]
	print(feats.shape,labels.shape)
	
	for label in np.unique(labels):
		select = labels==label
		plt.scatter(feats[select,0],feats[select,1],label=str(int(label)))
	plt.title("File: "+filename)
	#plt.legend()
	plt.show(block=True)
