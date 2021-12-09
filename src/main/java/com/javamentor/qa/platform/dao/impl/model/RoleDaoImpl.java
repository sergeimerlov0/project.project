package com.javamentor.qa.platform.dao.impl.model;

import com.javamentor.qa.platform.dao.abstracts.model.RoleDao;
import com.javamentor.qa.platform.dao.util.SingleResultUtil;
import com.javamentor.qa.platform.models.entity.user.Role;
import com.javamentor.qa.platform.models.entity.user.User;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.lang.reflect.ParameterizedType;
import java.util.Optional;

@Repository
public class RoleDaoImpl extends ReadWriteDaoImpl<Role, Long> implements RoleDao {

    @PersistenceContext
    private EntityManager entityManager;

    private final Class<User> clazz = (Class<User>) ((ParameterizedType) getClass().getGenericSuperclass())
            .getActualTypeArguments()[0];

    public Optional<Role> getByName(String name) {
        String hql = "FROM " + clazz.getName() + " WHERE email = :email";
        TypedQuery<Role> query = (TypedQuery<Role>) entityManager.createQuery(hql).setParameter("name", name);
        return SingleResultUtil.getSingleResultOrNull(query);
    }
}
