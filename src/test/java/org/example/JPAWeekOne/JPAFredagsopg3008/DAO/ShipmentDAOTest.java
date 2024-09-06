package org.example.JPAWeekOne.JPAFredagsopg3008.DAO;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.example.JPAWeekOne.JPAFredagsopg3008.entity.Location;
import org.example.JPAWeekOne.JPAFredagsopg3008.entity.Package;
import org.example.JPAWeekOne.JPAFredagsopg3008.entity.Shipment;
import org.example.persistence.HibernateConfig;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class ShipmentDAOTest {
    private static EntityManagerFactory emf;
    private static ShipmentDAO dao;
    private Shipment s1, s2;
    private Package p1, p2;
    private Location l1, l2, l3;


    @BeforeAll
    static void setUpBeforeClass() {
        emf = HibernateConfig.getEntityManagerFactoryForTest();
        dao = new ShipmentDAO(emf);
    }

    @BeforeEach
    void setUp() {
        try (EntityManager em = emf.createEntityManager()) {
            p1 = new Package("25654TO52", "Laurits", "Elvis", Package.DeliveryStatus.PENDING);
            p2 = new Package("2222hHUE2", "Camilla", "Harold", Package.DeliveryStatus.IN_TRANSIT);

            l1 = new Location(1.6, 5.6, "Kongenshave 24, 2200 KBH V");
            l2 = new Location(2.2, 5.6, "Sommerfuglevej 21, 2610 Rødovre");
            l3 = new Location(8.1, 9.6, "Sigurds Alle 2, 2100 KBH N");

            s1 = new Shipment(p1, l1, l2, LocalDateTime.of(2023, 02, 18,20,10));
            s2 = new Shipment(p2, l3, l1, LocalDateTime.of(2023, 10, 01,12,00));

            p1.getShipments().add(s1);
            p2.getShipments().add(s2);

            em.getTransaction().begin();
            em.createQuery("DELETE FROM Location").executeUpdate();
            em.createQuery("DELETE FROM Package").executeUpdate();
            em.createQuery("DELETE FROM Shipment").executeUpdate();
            em.persist(p1);
            em.persist(p2);
            em.persist(l1);
            em.persist(l2);
            em.persist(l3);
            em.persist(s1);
            em.persist(s2);
            em.getTransaction().commit();
        }
    }

    @Test
    void create() {
        Shipment shipment = Shipment.builder()
                .shipmentDate(LocalDateTime.of(2024, 8, 10, 20, 0))
                .destinationLocation(l1)
                .shipmentPackage(p1)
                .sourceLocation(l2)
                .build();
        Shipment created = dao.create(shipment);
        boolean expected = created.getId() != null;
        Assertions.assertTrue(expected);
    }

    @Test
    void update() {
        Shipment newShipment = s1;
        newShipment.setSourceLocation(l2);
        Shipment actual = dao.update(newShipment);
        Shipment expected = s1;
        Assertions.assertEquals(expected.getId(), actual.getId());
    }

    @Test
    void delete() {
        int numberOf = dao.getAll().size();
        dao.delete(s1);
        int expected = numberOf - 1;
        int actual = dao.getAll().size();
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void getById() {
        Shipment shipment = dao.getById(s1.getId());
        Long expected = shipment.getId();
        Long actual = s1.getId();
       Assertions.assertEquals(expected, actual);
    }

    @Test
    void getAll() {
        int actual = 2;
        int expected = dao.getAll().size();
        Assertions.assertEquals(expected, actual);
    }
}