package org.example.JPAWeekOne.JPAFredagsopg3008.DAO;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.example.JPAWeekOne.JPAFredagsopg3008.entity.Location;
import org.example.persistence.HibernateConfig;
import org.junit.jupiter.api.*;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class LocationDAOTest {
    private static EntityManagerFactory emf;
    private static LocationDAO dao;
    private Location l1, l2, l3;

    @BeforeAll
    static void setUpBeforeClass() {
        emf = HibernateConfig.getEntityManagerFactoryForTest();
        dao = new LocationDAO(emf);
    }

    @BeforeEach
    void setUp() {
        try (EntityManager em = emf.createEntityManager()) {
            l1 = new Location(1.6, 5.6, "Kongenshave 24, 2200 KBH V");
            l2 = new Location(2.2, 5.6, "Sommerfuglevej 21, 2610 Rødovre");
            l3 = new Location(8.1, 9.6, "Sigurds Alle 2, 2100 KBH N");

            em.getTransaction().begin();
            em.createQuery("DELETE FROM Location").executeUpdate();
            em.persist(l1);
            em.persist(l2);
            em.persist(l3);
            em.getTransaction().commit();
        }
    }

    @AfterAll
    static void tearDown() {
        emf.close();
    }

    @Test
    void create() {
        Location location = Location.builder().address("Hejrevej 45, 1200 KBH Ø").latitude(5.5).longitude(6.6).build();
        Location created = dao.create(location);
        boolean expected = created.getId() != null;
        Assertions.assertTrue(expected);
    }

    @Test
    void update() {
        Location newLocation = l1;
        l1.setAddress("VictorsMorsVej 2, 2200 KBH N");
        Location actual = dao.update(newLocation);
        String expected = "VictorsMorsVej 2, 2200 KBH N";
        Assertions.assertEquals(expected, actual.getAddress());
    }

    @Test
    void delete() {
        int numberOf = dao.getAll().size();
        dao.delete(l1);
        int expected = numberOf - 1;
        int actual = dao.getAll().size();
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void getById() {
        Location location = dao.getById(l1.getId());
        Long actual = location.getId();
        Long expected = l1.getId();
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void getAll() {
        int actual = 3;
        int expected = dao.getAll().size();
        Assertions.assertEquals(expected, actual);
    }
}