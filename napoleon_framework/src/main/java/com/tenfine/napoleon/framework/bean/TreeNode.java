package com.tenfine.napoleon.framework.bean;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class TreeNode implements Serializable {

	/**
	 * serialVersionUID:TODO
	 */
	private static final long serialVersionUID = -814644945738464567L;

	// node id, which is important to load remote data
	private String id;
	// node text to show
	private String text;
	// node state, 'open' or 'closed', default is 'open'. When set to 'closed',
	// the node have children nodes and will load them from remote site
	private String state = "open";
	// Indicate whether the node is checked selected.
	private boolean checked = false;
	// custom attributes can be added to a node
	private Map<String, Object> attributes;
	// an array nodes defines some children nodes
	private List<TreeNode> children;
	// private TreeNode parent;

	public TreeNode(String id, String parentId, String name, Map<String, Object> attributes) {
		super();
		this.id = id;
		this.parentId = parentId;
		this.text = name;
		this.attributes = attributes;
	}

	private String parentId;
	private int level;
	private boolean leaf;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public boolean isChecked() {
		return checked;
	}

	public void setChecked(boolean checked) {
		this.checked = checked;
	}

	public Map<String, Object> getAttributes() {
		return attributes;
	}

	public void setAttributes(Map<String, Object> attributes) {
		this.attributes = attributes;
	}

	public List<TreeNode> getChildren() {
		return children;
	}

	public void setChildren(List<TreeNode> children) {
		this.children = children;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	// public TreeNode getParent() {
	// return parent;
	// }
	// public void setParent(TreeNode parent) {
	// this.parent = parent;
	// }
	public boolean isLeaf() {
		return leaf;
	}

	public void setLeaf(boolean leaf) {
		this.leaf = leaf;
	}

}

