package org.example.JPAWeekOne.JPAFredagsopg3008.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Shipment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    private Package shipmentPackage;
    @ManyToOne
    private Location sourceLocation;
    @ManyToOne
    private Location destinationLocation;
    private LocalDateTime shipmentDate;

    @Builder
    public Shipment(Package shipmentPackage, Location sourceLocation, Location destinationLocation, LocalDateTime shipmentDate) {
        this.shipmentPackage = shipmentPackage;
        this.sourceLocation = sourceLocation;
        this.destinationLocation = destinationLocation;
        this.shipmentDate = shipmentDate;
    }
}
