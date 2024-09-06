package org.example.JPAWeekOne.JPAFredagsopg3008.DAO;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.example.JPAWeekOne.JPAFredagsopg3008.entity.Package;
import org.example.persistence.HibernateConfig;
import org.hibernate.exception.ConstraintViolationException;
import org.junit.jupiter.api.*;

class PackageDAOTest {
    private static EntityManagerFactory emf;
    private static EntityManager em;
    private static PackageDAO packageDAO;

    private Package p1, p2;


    @BeforeAll
    static void setUpAll() {
        HibernateConfig.setTestMode(true);
        emf = HibernateConfig.getEntityManagerFactory();
        packageDAO = new PackageDAO(emf);
    }

    @AfterAll
    static void tearDownAll() {
        HibernateConfig.setTestMode(false);
        if (emf != null) {
            emf.close();
        }
    }

    @BeforeEach
    void setUp() {
        try (EntityManager em = emf.createEntityManager()) {
            p1 = new Package("25654TO52", "Laurits", "Elvis", Package.DeliveryStatus.PENDING);
            p2 = new Package("2222hHUE2", "Camilla", "Harold", Package.DeliveryStatus.IN_TRANSIT);
            p1.setId(1L);
            p2.setId(2L);
            em.getTransaction().begin();
            em.createQuery("DELETE FROM Package").executeUpdate();
            em.merge(p1); // instead of persist use merge to handle specifik id's
            em.merge(p2);
            em.getTransaction().commit();
        } catch (ConstraintViolationException e) {
            System.out.println("Constraint violation " + e.getMessage());
        }
    }

    @AfterAll
    static void tearDown() {
        emf.close();
    }

    @Test
    void getPackageById() {
        Package p = packageDAO.getById(p1.getId());
        Long actual = p.getId();
        Long expected = 1L;
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void getAllPackages() {
    }

    @Test
    void getPackageByTrackingNumber() {
    }

    @Test
    void createPackage() {
    }

    @Test
    void updatePackage() {
    }

    @Test
    void deletePackage() {
    }

    @Test
    void main() {
    }
}
