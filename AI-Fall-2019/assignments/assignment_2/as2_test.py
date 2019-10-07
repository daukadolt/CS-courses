#Test file
from as2_mnx import MNX
def main():
	data_list=['A', ['B', ('E', 3), ('F', 12),('G', 8)], ['C', ('H', 2), ('I', 4),('J', 6)],['D', ('K', 14), ('L', 5),('M', 2)]]
	mnm=MNX(data_list);
	res=mnm.minimax();


    #Test the result
	print (res.value)#should be 3
	print (res.solution);#should be ['A', 'B', 'E']

	
if __name__ == "__main__":
	main()
	
	
