package com.mski.spring.jv.demo.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.proxy.HibernateProxy;

import java.util.Objects;
import java.util.UUID;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "sold_items")
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"stock_id", "transaction_id"})})
public class SoldItem {

    @Id
    @Column(name = "sold_item_id")
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "stock_id")
    private Stock stock;

    @ManyToOne
    @JoinColumn(name = "transaction_id")
    @ToString.Exclude
    private Transaction transaction;

    @Column(nullable = false)
    private Integer quantity;

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        SoldItem soldItem = (SoldItem) o;
        return getId() != null && Objects.equals(getId(), soldItem.getId());
    }

    @Override
    public final int hashCode() {
        return Objects.hash(getId());
    }
}
