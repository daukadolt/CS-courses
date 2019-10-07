#Assignment 2
from as2_tree import Tree
class Result:
	def __init__(self, sol=[], val=-1000):
			self.solution = sol
			self.value = val
			
class MNX:
	def __init__(self, data_list):
		self.tree = Tree()	
		self.tree.init_tree(data_list)
		self.root = self.tree.root
		self.currentNode = None
		self.successors = []		
		return
        
	def terminalTest(self, node):
		assert node is not None
		return len(node.children) == 0

	def utilityChecking(self, node):
		assert node is not None
		return node.value

	def getChildren(self, node):
		assert node is not None
		return node.children

	def minimax(self):
		memory = {}
		terminal_val = self.modified_max_v(self.root, memory)
		res=Result();

		queue = [self.root.Name]
		traversed = []
		while queue:
			currentNode = queue.pop(0)
			traversed.append(currentNode)
			if currentNode in memory:
				queue.append(memory[currentNode])

#################  Return the solution here  #################
		res.value=terminal_val #you put the best terminal value for root node here
		res.solution=traversed #you put the solution_array here
#################  Return the solution here  #################



		return res


	def max_v(self, node):		
		if self.terminalTest(node):
			return self.utilityChecking(node)		
		max_v = -1000 #we use 1000 as the initial_maximum value
		deeper_layer = self.getChildren(node)
		for deeper_node in deeper_layer:
			max_v = max(max_v, self.min_v(deeper_node))
		return max_v

	def min_v(self, node):		
		if self.terminalTest(node):
			return self.utilityChecking(node)
		min_v = 1000 #we use -1000 as the initial_minimum value
		deeper_layer = self.getChildren(node)
		for deeper_node in deeper_layer:
			min_v = min(min_v, self.max_v(deeper_node))
		return min_v

	def modified_max_v(self, node, memory):		
		if self.terminalTest(node):
			return self.utilityChecking(node)		
		max_v = -1000 #we use 1000 as the initial_maximum value
		currentSelectedNode = None
		deeper_layer = self.getChildren(node)
		for deeper_node in deeper_layer:
			deeperResult = self.modified_min_v(deeper_node, memory)
			if currentSelectedNode == None:
				max_v = deeperResult
				currentSelectedNode = deeper_node
			elif max_v<deeperResult:
				max_v = deeperResult
				currentSelectedNode = deeper_node
		memory[node.Name] = currentSelectedNode.Name
		return max_v

	def modified_min_v(self, node, memory):		
		if self.terminalTest(node):
			return self.utilityChecking(node)
		min_v = 1000 #we use -1000 as the initial_minimum value
		currentSelectedNode = None
		deeper_layer = self.getChildren(node)
		for deeper_node in deeper_layer:
			deeperResult = self.modified_max_v(deeper_node, memory)
			if currentSelectedNode == None:
				min_v = deeperResult
				currentSelectedNode = deeper_node
			elif min_v>deeperResult:
				min_v = deeperResult
				currentSelectedNode = deeper_node
		memory[node.Name] = currentSelectedNode.Name
		return min_v