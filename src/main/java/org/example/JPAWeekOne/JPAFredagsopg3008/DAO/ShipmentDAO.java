package org.example.JPAWeekOne.JPAFredagsopg3008.DAO;

import jakarta.persistence.*;
import org.example.JPAWeekOne.JPAFredagsopg3008.entity.Location;
import org.example.JPAWeekOne.JPAFredagsopg3008.entity.Package;
import org.example.JPAWeekOne.JPAFredagsopg3008.entity.Shipment;
import org.hibernate.exception.ConstraintViolationException;

import java.util.Set;
import java.util.stream.Collectors;

public class ShipmentDAO implements IDAO<Shipment> {
    private EntityManagerFactory emf;

    public ShipmentDAO(EntityManagerFactory emf) {
        this.emf = emf;
    }

    @Override
    public Shipment create(Shipment shipment) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            Package foundP = em.find(Package.class, shipment.getShipmentPackage().getId());
            Location foundLD = em.find(Location.class, shipment.getDestinationLocation().getId());
            Location foundLS = em.find(Location.class, shipment.getSourceLocation().getId());

            if (foundP == null) {
                em.persist(foundP);
            }
            if (foundLD == null) {
                em.persist(foundLD);
            }
            if (foundLS == null) {
                em.persist(foundLS);
            }
            em.persist(shipment);
            return shipment;

        } catch (PersistenceException e) {
            System.out.println("PersistenceException: failed to create shipment" + e);
            return null;
        }
    }

    @Override
    public Shipment update(Shipment shipment) {
        try (EntityManager em = emf.createEntityManager()) {
            Shipment found = em.find(Shipment.class, shipment.getId());
            em.getTransaction().begin();

            if (shipment.getShipmentPackage() != null) {
                found.setShipmentPackage(shipment.getShipmentPackage());
            }
            if (shipment.getDestinationLocation() != null) {
                found.setDestinationLocation(shipment.getDestinationLocation());
            }
            if (shipment.getSourceLocation() != null) {
                found.setSourceLocation(shipment.getSourceLocation());
            }
            if (shipment.getShipmentDate() != null) {
                found.setShipmentDate(shipment.getShipmentDate());
            }
            em.getTransaction().commit();
            return found;

        } catch (ConstraintViolationException e) {
            System.out.println("Constrain violation " + e.getMessage());
        }
        return null;
    }


    @Override
    public void delete(Shipment shipment) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            em.remove(shipment);
            em.getTransaction().commit();
        }
    }

    @Override
    public Shipment getById(Long id) {
        try(EntityManager em = emf.createEntityManager()) {
            return em.find(Shipment.class, id);
        }
    }

    @Override
    public Set<Shipment> getAll() {
        try(EntityManager em = emf.createEntityManager()) {
            return em.createQuery("SELECT s FROM Shipment s", Shipment.class)
                    .getResultStream()
                    .collect(Collectors.toSet());
        }
    }
}
