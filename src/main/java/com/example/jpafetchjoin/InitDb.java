package com.example.jpafetchjoin;

import com.example.jpafetchjoin.domain.Member;
import com.example.jpafetchjoin.domain.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;

@Component
@RequiredArgsConstructor
public class InitDb {

    private final InitService initService;

    @PostConstruct
    public void init() {
        initService.dbInit1();
        initService.dbInit2();
        initService.dbInit3();
    }

    @Component
    @Transactional
    @RequiredArgsConstructor
    static class InitService {

        private final EntityManager em;

        public void dbInit1() {
            Member member = createMember("Kim");
            em.persist(member);

            Order order = createOrder("Apple", member);
            em.persist(order);
            order = createOrder("Banana", member);
            em.persist(order);
            order = createOrder("Orange", member);
            em.persist(order);
        }

        public void dbInit2() {
            Member member = createMember("Lee");
            em.persist(member);

            Order order = createOrder("Apple", member);
            em.persist(order);
            order = createOrder("Banana", member);
            em.persist(order);
            order = createOrder("Orange", member);
            em.persist(order);
        }

        public void dbInit3() {
            Member member = createMember("Park");
            em.persist(member);

            Order order = createOrder("Apple", member);
            em.persist(order);
            order = createOrder("Banana", member);
            em.persist(order);
            order = createOrder("Orange", member);
            em.persist(order);
        }


        private Member createMember(String name) {
            Member member = new Member();
            member.setName(name);
            return member;
        }

        private Order createOrder(String item, Member member) {
            Order order = new Order();
            order.setItem(item);
            order.addMember(member);
            return order;
        }
    }
}

