package com.example.jpafetchjoin.repository;

import com.example.jpafetchjoin.domain.Member;
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

    public List<Order> findAllFetchJoin2() {
        return em.createQuery("select o from Order o join fetch o.member where o.item = 'banana'", Order.class) //join fetch Member가 아니다.
                .getResultList();
    }

    public List<Order> findAllFetchJoin3() {
        return em.createQuery("select o from Order o join fetch o.member m where o.item = 'banana'", Order.class) //join fetch Member가 아니다.
                .getResultList(); //on은 m을 주든 o를 주든 다 안된다.
    }

    public List<Order> findAllFetchJoin4() {
        return em.createQuery("select o from Order o join fetch o.member", Order.class) //join fetch Member가 아니다.
                .getResultList(); //on은 m을 주든 o를 주든 다 안된다.
    }
    public void delete(Order order){
        em.remove(order);
       /* elect m from Member m join fetch m.orders o where o.item ='apple'", Member.class) //join fetch Member가 아니다.
                .getResultList();*/
    }
}