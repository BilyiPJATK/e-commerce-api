package com.example.demo.models.users;

import com.example.demo.enums.MembershipType;
import com.example.demo.models.BaseEntity;
import com.example.demo.models.rental.RentalTransaction;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "members")
@SQLDelete(sql = "UPDATE members SET is_deleted = true WHERE id=?")
@SQLRestriction("is_deleted = false")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Member extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    private LocalDate joinDate;

    @Enumerated(EnumType.STRING)
    private MembershipType membershipType;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<RentalTransaction> rentals;
}