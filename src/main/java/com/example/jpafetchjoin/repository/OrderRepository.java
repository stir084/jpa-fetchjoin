package com.example.jpafetchjoin.repository;

import com.example.jpafetchjoin.domain.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class OrderRepository {

    private final EntityManager em;

/*    public void save(Order order) {
        em.persist(order);
    }*/

    public List<Order> findAll() {
        return em.createQuery("select o from Order o", Order.class)
                .getResultList();
    }

    public List<Order> findAllFetchJoin() {
        return em.createQuery("select o from Order o join fetch o.member", Order.class) //join fetch Member가 아니다.
                .getResultList();
    }
}