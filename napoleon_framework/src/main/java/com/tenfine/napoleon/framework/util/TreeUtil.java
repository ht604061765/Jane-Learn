package com.tenfine.napoleon.framework.util;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;

import com.tenfine.napoleon.framework.bean.BaseTreePO;
import com.tenfine.napoleon.framework.bean.POCondition;
import com.tenfine.napoleon.framework.bean.TreeNode;
import com.tenfine.napoleon.framework.dao.BaseDao;

public class TreeUtil {

	@SuppressWarnings("unchecked")
	public static List<TreeNode> buildListToTree(List<TreeNode> dirs) {
		List<TreeNode> roots = findRoots(dirs);
		List<TreeNode> notRoots = (List<TreeNode>) CollectionUtils.subtract(dirs, roots);
		for (TreeNode root : roots) {
			root.setChildren(findChildren(root, notRoots));
		}
		return roots;
	}

	private static List<TreeNode> findRoots(List<TreeNode> allNodes) {
		List<TreeNode> results = new ArrayList<TreeNode>();
		for (TreeNode node : allNodes) {
			boolean isRoot = true;
			for (TreeNode comparedOne : allNodes) {
				if (comparedOne.getId().equals(node.getParentId())) {
					isRoot = false;
					break;
				}
			}
			if (isRoot) {
				node.setLevel(0);
				results.add(node);
			}
		}
		return results;
	}

	@SuppressWarnings("unchecked")
	private static List<TreeNode> findChildren(TreeNode root, List<TreeNode> allLeafNodes) {
		List<TreeNode> children = new ArrayList<TreeNode>();

		for (TreeNode comparedOne : allLeafNodes) {
			if (comparedOne.getParentId().equals(root.getId())) {
//				comparedOne.setParent(root);
				comparedOne.setLevel(root.getLevel() + 1);
				children.add(comparedOne);
			}
		}
		List<TreeNode> notChildren = (List<TreeNode>) CollectionUtils.subtract(allLeafNodes, children);
		for (TreeNode child : children) {
			List<TreeNode> tmpChildren = findChildren(child, notChildren);
			if (tmpChildren == null || tmpChildren.size() < 1) {
				child.setLeaf(true);
			} else {
				child.setLeaf(false);
			}
			child.setChildren(tmpChildren);
		}
		return children;
	}

	public static void main(String[] args) {
		List<TreeNode> allNodes = new ArrayList<TreeNode>();
		allNodes.add(new TreeNode("1", "0", "节点1",null));
		allNodes.add(new TreeNode("2", "0", "节点2",null));
		allNodes.add(new TreeNode("3", "0", "节点3",null));
		allNodes.add(new TreeNode("4", "1", "节点4",null));
		allNodes.add(new TreeNode("5", "1", "节点5",null));
		allNodes.add(new TreeNode("6", "1", "节点6",null));
		allNodes.add(new TreeNode("7", "4", "节点7",null));
		allNodes.add(new TreeNode("8", "4", "节点8",null));
		allNodes.add(new TreeNode("9", "5", "节点9",null));
		allNodes.add(new TreeNode("10", "5", "节点10",null));
		List<TreeNode> roots = TreeUtil.buildListToTree(allNodes);
		for (TreeNode n : roots) {
			System.out.println(n);
		}
	}
	
	//寻找 本节点下面是否还有子节点
	/**
     * 寻找 本节点的父节点下面是否还有其他子节点
     *
     * @param clazz 实体类
     * @param po    实体(当前节点，注意不是父节点)
     * @param dao   dao层
     * @return  是否 本节点的父节点下面是否还有其他子节点  true 则还有子节点    false  无子节点
     */
	public static  <PO extends BaseTreePO, DAO extends BaseDao> boolean findParentHaveChildren(Class<PO> clazz, PO po, DAO dao) {
		//clazz pQdmx = dao.findPo(clazz, po.getPid());
		POCondition co = new POCondition();
		if(po.getPid()!=null) {
			co.addEQ("pid", po.getPid());
			List<PO> pChildren = dao.findPoList(clazz, co);
			if(pChildren!=null && pChildren.size()!=0) {
				//父节点还有其他孩子 不能叶子节点
				return false;
			}else {
				//父节点无其他孩子 是叶子节点
				return true;
			}
		}
		return false;
	} 
}
