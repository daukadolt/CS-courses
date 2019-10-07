from ast import literal_eval
class TreeNode:
	def __init__(self, name, value=0, parent=None):
		self.Name = name
		self.value = value
		self.parent = parent
		self.children = []

	def addChild(self, childNode):
		self.children.append(childNode)

class Tree:
	def __init__(self):
		self.root = None

	def init_tree(self, data_list):		
		self.root = TreeNode(data_list.pop(0))
		for elem in data_list:
			self.add_children(elem, self.root)

	def add_children(self, data_list, parent):		
		if type(data_list) is tuple:
			leaf_node = TreeNode(data_list[0])
			leaf_node.parent = parent
			parent.addChild(leaf_node)
			if len(data_list) == 2:
				leaf_node.value = data_list[1]
			return
		tree_node = TreeNode(data_list.pop(0))
		tree_node.parent = parent
		parent.addChild(tree_node)
		for elem in data_list:
			self.add_children(elem, tree_node)
		return