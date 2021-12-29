package com.javamentor.qa.platform.service.impl.model;

import com.javamentor.qa.platform.dao.abstracts.model.ReadWriteDao;
import com.javamentor.qa.platform.models.entity.user.Role;
import com.javamentor.qa.platform.service.abstracts.model.RoleService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class RoleServiceImpl extends ReadWriteServiceImpl<Role, Long> implements RoleService {

    public RoleServiceImpl(ReadWriteDao<Role, Long> readWriteDao) {
        super(readWriteDao);
    }


}
