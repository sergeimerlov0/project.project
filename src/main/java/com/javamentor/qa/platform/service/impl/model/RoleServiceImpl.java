package com.javamentor.qa.platform.service.impl.model;

import com.javamentor.qa.platform.dao.abstracts.model.ReadWriteDao;
import com.javamentor.qa.platform.dao.abstracts.model.RoleDao;
import com.javamentor.qa.platform.models.entity.user.Role;
import com.javamentor.qa.platform.service.abstracts.model.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;

@Service
public class RoleServiceImpl extends ReadWriteServiceImpl<Role, Long> implements RoleService {
    public RoleServiceImpl(ReadWriteDao<Role, Long> readWriteDao) {
        super(readWriteDao);
    }

    @Autowired
    private RoleDao roleDao;

    @Override
    @Transactional
    public Optional<Role> getRoleByName(String name) {
        return roleDao.getRoleByName(name);
    }
}