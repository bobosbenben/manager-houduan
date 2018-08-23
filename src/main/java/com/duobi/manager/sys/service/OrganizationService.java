package com.duobi.manager.sys.service;

import com.duobi.manager.sys.dao.OrganizationDao;
import com.duobi.manager.sys.entity.Organization;
import com.duobi.manager.sys.utils.UserUtils;
import com.duobi.manager.sys.utils.tree.TreeService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class OrganizationService extends TreeService<OrganizationDao, Organization, Long> {

    @Override
    @Transactional(readOnly = false)
    public void save(Organization entity) {
        super.save(entity);
        UserUtils.removeCache(UserUtils.CACHE_ORGANIZATION_LIST);
        UserUtils.removeCache(UserUtils.CACHE_ORGANIZATION_TREE);
    }

    @Override
    @Transactional(readOnly = false)
    public void save(List<Organization> entities) {
        super.save(entities);
        UserUtils.removeCache(UserUtils.CACHE_ORGANIZATION_LIST);
        UserUtils.removeCache(UserUtils.CACHE_ORGANIZATION_TREE);
    }

    @Override
    @Transactional(readOnly = false)
    public void delete(Organization entity) {
        super.delete(entity);
        UserUtils.removeCache(UserUtils.CACHE_ORGANIZATION_LIST);
        UserUtils.removeCache(UserUtils.CACHE_ORGANIZATION_TREE);
    }

    @Override
    @Transactional(readOnly = false)
    public void delete(List<Organization> entities) {
        super.delete(entities);
        UserUtils.removeCache(UserUtils.CACHE_ORGANIZATION_LIST);
        UserUtils.removeCache(UserUtils.CACHE_ORGANIZATION_TREE);
    }

    @Override
    public List<Organization> findAsTree(Organization organization) {
        List<Organization> organizationTree = UserUtils.getOrganizationTree();
        return organizationTree;
    }
}