#Author: Dinh-Mao Bui, Anh-Tu Nguyen
import as1_bfs
tree_1 = {'1': ['2', '3', '4'],
         '2': ['5'],
         '3': ['6', '7'],
         '5': [],
         '4': [],
         '6': [],
         '7': []}
		 
tree_2 = {'1': ['2', '3', '4'],
         '2': ['1','5', '4'],
         '3': ['1', '6', '7'],
         '5': ['2'],
         '4': ['1', '6','5'],
         '6': ['3','2'],
         '7': ['3']}

print (as1_bfs.traverse(tree_1,'1'))#the result should be ['1', '2', '3', '4', '5', '6', '7']
print (as1_bfs.traverse(tree_2,'1'))#the same as above
print (as1_bfs.pathfinder(tree_2,'2', '7'))#the result should be ['2', '1', '3', '7']

