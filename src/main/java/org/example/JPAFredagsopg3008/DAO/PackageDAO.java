package org.example.JPAFredagsopg3008.DAO;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.TypedQuery;
import org.example.JPAFredagsopg3008.entity.Package;
import org.example.persistence.HibernateConfig;
import org.hibernate.exception.ConstraintViolationException;

import java.util.ArrayList;
import java.util.Set;
import java.util.stream.Collectors;

public class PackageDAO {
    private static PackageDAO instance;
    private EntityManagerFactory emf = HibernateConfig.getEntityManagerFactory();

    public Package getPackageById(Object id) {
        Package found;
        try (EntityManager em = emf.createEntityManager()) {
            found = em.find(Package.class, id);
            if (found == null) {
                throw new EntityNotFoundException("Package with id " + id + " not found");
            }
        }
        return found;
    }

    public Set<Package> getAllPackages() {
        try (EntityManager em = emf.createEntityManager()) {
            TypedQuery<Package> query = em.createQuery("select p from Package p", Package.class);
            Set<Package> result = query.getResultList().stream().collect(Collectors.toSet());
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public Package getPackageByTrackingNumber(String trackingNumber) {
        Package found;
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            found = em.createQuery("SELECT p FROM Package p WHERE p.trackingNumber = :trackingNumber", Package.class)
                    .setParameter("trackingNumber", trackingNumber)
                    .getSingleResult();
            em.getTransaction().commit();
            return found;
        } catch (Exception e) {
            System.out.println("Package with trackingNumber " + trackingNumber + " not found");
            return null;
        }
    }

    public Package createPackage(Package p) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            em.persist(p);
            em.getTransaction().commit();
            return p;
        } catch (ConstraintViolationException e) {
            System.out.println("Constraint violation " + e.getMessage());
        }
        return null;
    }

    public void updatePackage(Package p) { // only delivery, reciever name and trackingnumber.
        try (EntityManager em = emf.createEntityManager()) {
            Package found = em.find(Package.class, p.getId());
            if (found == null) {
                throw new EntityNotFoundException("Package not found in the system!");
            }
            em.getTransaction().begin();

            if (p.getDeliveryStatus() != null) {
                found.setDeliveryStatus(p.getDeliveryStatus());
            }

            if (p.getTrackingNumber() != null) {
                found.setTrackingNumber(p.getTrackingNumber());
            }

            if (p.getRecieverName() != null) {
                found.setRecieverName(p.getRecieverName());
            }
            em.getTransaction().commit();

        } catch (ConstraintViolationException e) {
            System.out.println("Constrain violation " + e.getMessage());
        }
    }


    public void deletePackage(Package p) {
        try (EntityManager em = emf.createEntityManager()) {
            Package found = em.find(Package.class, p.getId());
            if (found == null) {
                throw new EntityNotFoundException("Package with id " + p.getId() + " not found");
            }
            em.getTransaction().begin();
            em.remove(found);
            em.getTransaction().commit();
        } catch (ConstraintViolationException e) {
            System.out.println("Constraint violation " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        PackageDAO dao = new PackageDAO();

     /*   // Persist packages
        ArrayList<Package> packages = new ArrayList<>();

        packages.add(new Package("TRACK1234", "Alice", "Bob", Package.DeliveryStatus.PENDING));
        packages.add(new Package("TRACK5678", "Charlie", "Diana", Package.DeliveryStatus.IN_TRANSIT));
        packages.add(new Package("TRACK9101", "Eve", "Frank", Package.DeliveryStatus.DELIVERED));
        packages.add(new Package("TRACK1121", "George", "Helen", Package.DeliveryStatus.PENDING));
        packages.add(new Package("TRACK3141", "Ivy", "Jack", Package.DeliveryStatus.IN_TRANSIT));

        for (Package p : packages) {
            dao.createPackage(p);
        }*/

        // Update package delivery status
        Package updatedPackage = Package.builder().id(4).deliveryStatus(Package.DeliveryStatus.IN_TRANSIT).build();
        dao.updatePackage(updatedPackage);

        // Find package by trackingNumber.
        Package packageByTrackingNumber = dao.getPackageByTrackingNumber("TRACK1121");
        System.out.println(packageByTrackingNumber);

       /* // Remove package from system.
        Package packageForDeleting = dao.getPackageById(2);
        dao.deletePackage(packageForDeleting);
*/
    }
}
