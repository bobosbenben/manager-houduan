package com.duobi.manager.sys.utils;

import com.duobi.manager.sys.utils.persistence.TreeEntity;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class TreeEntityUtils {

    private static Logger logger = LoggerFactory.getLogger(TreeEntity.class);

    public static List<TreeEntity> listToTree(List<TreeEntity> treeEntityList) {
        List<TreeEntity> roots = findRoots(treeEntityList);
        List<TreeEntity> notRoots = (List<TreeEntity>) CollectionUtils.subtract(treeEntityList, roots);
        for (TreeEntity root : roots) {
            root.setChildren(findChildren(root, notRoots));
        }
        return roots;
    }

    private static List<TreeEntity> findRoots(List<TreeEntity> treeEntityList) {
        List<TreeEntity> results = new ArrayList<>();
        for (TreeEntity treeEntity : treeEntityList) {
            boolean isRoot = true;
            for (TreeEntity comparedOne : treeEntityList) {
                if (treeEntity.getParentId().equals(comparedOne.getId())) {
                    isRoot = false;
                    break;
                }
            }
            if (isRoot) {
                results.add(treeEntity);
            }
        }
        return results;
    }

    @SuppressWarnings("unchecked")
    private static List<TreeEntity> findChildren(TreeEntity root, List<TreeEntity> treeEntityList) {
        List<TreeEntity> children = new ArrayList<>();

        for (TreeEntity comparedOne : treeEntityList) {
            if (comparedOne.getParentId().equals(root.getId())) {
                children.add(comparedOne);
            }
        }
        List<TreeEntity> notChildren = (List<TreeEntity>) CollectionUtils.subtract(treeEntityList, children);
        for (TreeEntity child : children) {
            //递归找孩子
            List<TreeEntity> tmpChildren = findChildren(child, notChildren);
            child.setChildren(tmpChildren);
        }
        if (children.size() == 0) return null;
        return children;
    }

}
