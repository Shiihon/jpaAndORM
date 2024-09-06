package org.example.JPAWeekOne.JPATorsdag2908.daos;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.TypedQuery;
import org.example.JPAWeekOne.JPATorsdag2908.entities.User;
import org.example.persistence.HibernateConfig;
import org.hibernate.exception.ConstraintViolationException;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class UserDAO implements IDAO<User> {
    private EntityManagerFactory emf = HibernateConfig.getEntityManagerFactory();

    @Override
    public User getById(Integer id) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction();
            return em.find(User.class, id);
        }
    }

    @Override
    public Set<User> getAll() {
        try (EntityManager em = emf.createEntityManager()) {
            TypedQuery<User> query = em.createQuery("SELECT u FROM User u", User.class);// returnere en type query
            List<User> userList = query.getResultList();
            return userList.stream().collect(Collectors.toSet());
        }
    }

    @Override
    public void create(User user) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            em.persist(user);
            em.getTransaction().commit();
        }
    }

    @Override
    public void update(User user) {
        try (EntityManager em = emf.createEntityManager()) {
            User found = em.find(User.class, user.getId());
            if (found == null) {
                throw new EntityNotFoundException("User not found in the system!");
            }
            em.getTransaction().begin();
            if(user.getUserName()!=null){
                found.setUserName(user.getUserName());
            }
            if(user.getEmail()!=null){
                found.setEmail(user.getEmail());
            }
            if(user.getPassword()!=null){
                found.setPassword(user.getPassword());
            }
            em.getTransaction().commit();

        } catch (ConstraintViolationException e) {
            System.out.println("Constrain violation " + e.getMessage());
        }
    }

    @Override
    public void delete(User user) {
        try (EntityManager em = emf.createEntityManager()){
            em.getTransaction().begin();
            em.remove(user);
            em.getTransaction().commit();
        }
    }


    public static void main(String[] args) {
        UserDAO userDAO = new UserDAO();
       /* userDAO.create(User.builder()
                .userName("user4")
                .password("password123")
                .email("user4@email.com")
                .build()); */

        /*User user2 = userDAO.getById(2);
        System.out.println(user2);

        user2.setUserName("DinMor");
        userDAO.update(user2);

        System.out.println(user2);

        User user3 = userDAO.getById(3);
        userDAO.delete(user3);

        userDAO.getAll().forEach(System.out::println);*/

        User userUpdate = User.builder().id(2).userName("Alex").build();
        userDAO.update(userUpdate);
    }
}
