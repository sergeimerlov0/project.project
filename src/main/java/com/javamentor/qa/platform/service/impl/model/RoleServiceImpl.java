package com.javamentor.qa.platform.service.impl.model;

import com.javamentor.qa.platform.dao.abstracts.model.RoleDao;
import com.javamentor.qa.platform.models.entity.user.Role;
import com.javamentor.qa.platform.service.abstracts.model.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class RoleServiceImpl extends ReadWriteServiceImpl<Role, Long> implements RoleService {

    private final RoleDao roleDao;

    @Autowired
    public RoleServiceImpl(RoleDao roleDao) {
        super(roleDao);
        this.roleDao = roleDao;
    }

    @Override
    public Optional<Role> getByName(String name) {
        return roleDao.getByName(name);
    }
}
