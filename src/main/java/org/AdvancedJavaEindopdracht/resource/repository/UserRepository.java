package org.AdvancedJavaEindopdracht.resource.repository;

import org.AdvancedJavaEindopdracht.resource.model.User;
import org.springframework.stereotype.Repository;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.transaction.Transactional;
import java.util.List;

@Repository
@Transactional
public class UserRepository {

    @PersistenceContext
    private EntityManager manager;

    @Transactional
    public List<User> getUsers(){
        CriteriaBuilder cb = manager.getCriteriaBuilder();
        CriteriaQuery<User> query = cb.createQuery(User.class);
        return manager.createQuery(query.select(query.from(User.class))).getResultList();
    }

    @Transactional
    public User getUser(int id){
        return manager.find(User.class, id);
    }

    @Transactional
    public User postUser(User user){
        manager.persist(user);
        return manager.find(User.class, user.getId());
    }

    @Transactional
    public void putUser(User user, int id){
        User update = manager.find(User.class, id);
        update.setName(user.getName());
        update.setEmail(user.getEmail());
        update.setProfileImagePath(user.getProfileImagePath());
        update.setPassword(user.getPassword());
        update.setApproved(user.isApproved());
    }

    @Transactional
    public void deleteUser(int id){
        User user = manager.find(User.class, id);
        manager.remove(user);
    }
}