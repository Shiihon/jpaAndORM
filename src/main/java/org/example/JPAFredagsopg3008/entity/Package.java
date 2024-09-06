package org.example.JPAFredagsopg3008.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "packages")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class Package {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "tracking_number", nullable = false)
    private String trackingNumber;

    @Column(name = "sender_name", nullable = false)
    private String senderName;

    @Column(name = "reciever_name", nullable = false)
    private String recieverName;

    @Enumerated(EnumType.STRING)
    @Column(name = "delivery_status", nullable = false)
    private DeliveryStatus deliveryStatus;

    @Column(name = "last_updated")
    private LocalDateTime lastUpdated;

    public enum DeliveryStatus {
        PENDING,
        IN_TRANSIT,
        DELIVERED;
    }

    @Builder//spørg hvorfor den laves.
    public Package(String trackingNumber, String senderName, String recieverName, DeliveryStatus deliveryStatus) {
        this.trackingNumber = trackingNumber;
        this.senderName = senderName;
        this.recieverName = recieverName;
        this.deliveryStatus = deliveryStatus;
    }

    @PrePersist
    public void prePersist() {
        this.lastUpdated = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        this.lastUpdated = LocalDateTime.now();
    }
}
