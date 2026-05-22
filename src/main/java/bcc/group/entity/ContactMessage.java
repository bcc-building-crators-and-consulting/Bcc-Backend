package bcc.group.entity;


import bcc.group.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "contact_messages")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@SuperBuilder
public class ContactMessage extends BaseEntity {

    @Column(nullable = false)
    private String fullName;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String phone;

    private String service;     // e.g., "Residential", "Commercial"

    @Column(columnDefinition = "TEXT", nullable = false)
    private String message;
}