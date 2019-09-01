#Author: Dinh-Mao Bui, Anh-Tu Nguyen
#Rule of traversing: Alphabetical ordered, then left to right, 
#Points: 2
def traverse(tree, init):    	
	queue = [init]
	traversed = []
	explored = set()
	while queue:
		'''
			Student fixes the loopy path from here to the end of this function
		'''
		node = queue.pop(0)
		if node in explored:
			continue
		else:
			explored.add(node)
		traversed.append(node)
		leaves = [leaf for leaf in tree[node] if leaf not in explored and leaf not in queue]
		# leaves.sort()										  # alternative
		# leaves = sorted(leaves, key=lambda leaf: int(leaf)) # ways of sorting
		leaves.sort(key=int) # sorting alphabetically using Python's built-in in-place sorting algorithm to fulfill alphabetical rule of traversal
		# print(leaves)

		for leaf in leaves:
			queue.append(leaf)
	return traversed

#Points: 3
def pathfinder(tree, init, goal):
	traversed = []
	queue = [[init]]
	if init == goal:
		return "No kidding, pls"
	explored = set()
	while queue:
		'''
			You implement the path finder from here
		'''
		currentSequence = queue.pop(0)
		lastNode = currentSequence[len(currentSequence)-1]
		if lastNode in explored:
			continue
		explored.add(lastNode)
		leaves = [leaf for leaf in tree[lastNode] if leaf not in explored]
		leaves.sort(key=int)
		for leaf in leaves:
			extendedSequence = [newNode for newNode in currentSequence]
			extendedSequence.append(leaf)
			if leaf == goal:
				return extendedSequence
			queue.append(extendedSequence)
	return "No such path exists"
 
