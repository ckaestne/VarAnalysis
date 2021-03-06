package datamodelviewer;

import java.util.ArrayList;

import util.StringUtils;
import util.sourcetracing.SourceCodeLocation;
import varanalysis.RunFile;
import datamodel.nodes.ConcatNode;
import datamodel.nodes.DataNode;
import datamodel.nodes.LiteralNode;
import datamodel.nodes.RepeatNode;
import datamodel.nodes.SelectNode;
import datamodel.nodes.SymbolicNode;

/**
 * 
 * @author HUNG
 *
 */
public class MyTreeNode {
	
	private DataNode dataNode;
	
	private MyTreeNode parent;
	
	private MyTreeNode[] children;
	
	public MyTreeNode(DataNode dataNode, MyTreeNode parent) {
		this.dataNode = dataNode;
		this.parent = parent;
		
		ArrayList<DataNode> childNodes = new ArrayList<DataNode>();
		if (dataNode instanceof ConcatNode) {
			ConcatNode concatNode = (ConcatNode) dataNode;
			childNodes = concatNode.getChildNodes();
		}
		else if (dataNode instanceof SelectNode) {
			SelectNode selectNode = (SelectNode) dataNode;
			childNodes.add(selectNode.getNodeInTrueBranch());
			childNodes.add(selectNode.getNodeInFalseBranch());
		}
		else if (dataNode instanceof RepeatNode) {
			RepeatNode repeatNode = (RepeatNode) dataNode;
			childNodes.add(repeatNode.getChildNode());
		}
		// else do nothing
		
		children = new MyTreeNode[childNodes.size()];
		for (int i = 0; i < childNodes.size(); i++) {
			children[i] = new MyTreeNode(childNodes.get(i), this);
		}
	}
	
	public String getNodeName() {
		return dataNode.getClass().getSimpleName();
	}
	
	public String getLocation() {
		if (dataNode instanceof ConcatNode) {
			return "";
		}
		else if (dataNode instanceof SelectNode) {
			SelectNode selectNode = (SelectNode) dataNode;
			return "";
		}
		else if (dataNode instanceof RepeatNode) {
			RepeatNode repeatNode = (RepeatNode) dataNode;
			return "";
		}
		else if (dataNode instanceof LiteralNode) {
			LiteralNode literalNode = (LiteralNode) dataNode;
			SourceCodeLocation location = literalNode.getLocation().getLocationAtOffset(0);
			SourceCodeLocation absoluteLocation = new SourceCodeLocation(location.getFilePath(), location.getPosition());
			return location.getFilePath() + " @ Line " + absoluteLocation.getLine(); 
		}
		else { // if (dataNode instanceof SymbolicNode) {
			SymbolicNode symbolicNode = (SymbolicNode) dataNode;
			return "";
		}
	}
	
	public String getText() {
		if (dataNode instanceof ConcatNode) {
			return "Concat";
		}
		else if (dataNode instanceof SelectNode) {
			SelectNode selectNode = (SelectNode) dataNode;
			return "Select(" + (selectNode.getConditionString() != null ? selectNode.getConditionString().getStringValue() : "") + ")";
		}
		else if (dataNode instanceof RepeatNode) {
			RepeatNode repeatNode = (RepeatNode) dataNode;
			return "Repeat";
		}
		else if (dataNode instanceof LiteralNode) {
			LiteralNode literalNode = (LiteralNode) dataNode;
			return literalNode.getStringValue();
		}
		else { // if (dataNode instanceof SymbolicNode) {
			SymbolicNode symbolicNode = (SymbolicNode) dataNode;
			return "Symbolic";
		}
	}
	
	public MyTreeNode getParent() {
		return parent;
	}
	
	public MyTreeNode[] getChildren() {
		return children;
	}
	
	public boolean hasChildren() {
		return children.length > 0;
	}
	
}
