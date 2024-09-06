package org.example.JPAWeekOne.JPAFredagsopg3008.DAO;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityNotFoundException;
import org.example.JPAWeekOne.JPAFredagsopg3008.entity.Location;
import org.hibernate.exception.ConstraintViolationException;

import java.util.Set;
import java.util.stream.Collectors;

public class LocationDAO implements IDAO<Location> {
    private EntityManagerFactory emf;

    public LocationDAO(EntityManagerFactory emf) {
        this.emf = emf;
    }

    @Override
    public Location create(Location location) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            em.persist(location);
            em.getTransaction().commit();
            return location;

        } catch (ConstraintViolationException e) {
            System.out.println("Constraint violation" + e.getMessage());
        }
        return null;
    }

    @Override
    public Location update(Location location) {
        try (EntityManager em = emf.createEntityManager()) {
            Location found = em.find(Location.class, location.getId());
            if (found == null) {
                throw new EntityNotFoundException("The location is not present");
            }
            em.getTransaction().begin();
            if (location.getLongitude() != null) {
                found.setLongitude(location.getLongitude());
            }
            if (location.getLatitude() != null) {
                found.setLatitude(location.getLatitude());
            }
            if (location.getAddress() != null) {
                found.setAddress(location.getAddress());
            }
            em.getTransaction().commit();
            return found;
        } catch (ConstraintViolationException e) {
            System.out.println("Constraint violation" + e.getMessage());
        }
        return null;
    }

    @Override
    public void delete(Location location) {
        try (EntityManager em = emf.createEntityManager()) {
        em.getTransaction().begin();
        em.remove(location);
        em.getTransaction().commit();
        }
    }

    @Override
    public Location getById(Long id) {
        try(EntityManager em = emf.createEntityManager()) {
            return em.find(Location.class, id);
        }
    }

    @Override
    public Set<Location> getAll() {
        try (EntityManager em = emf.createEntityManager()) {
            return em.createQuery("SELECT l FROM Location l", Location.class)
                    .getResultStream()
                    .collect(Collectors.toSet());
        }
    }
}
