package com.example.jpafetchjoin.repository;

import com.example.jpafetchjoin.MemberDto;
import com.example.jpafetchjoin.domain.Member;
import com.example.jpafetchjoin.domain.Order;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.StatelessSession;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;


import javax.persistence.*;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional
public class OrderRepositoryTest {

    @PersistenceContext
    EntityManager em;

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    MemberRepository memberRepository;

    // N+1 소개 시작
    @Test
    public void Nplus1(){ // Lazy Loading 시 N+1 발생
        List<Order> orderList = em.createQuery("select o from Order o"
                , Order.class).getResultList();
        for (Order order : orderList) {
            System.out.println(order.getMember().getName()+"님은 "+order.getItem()+"을 주문함.");
        }
    }

    @Test
    public void Nplus1_해결(){
        List<Order> orderList = em.createQuery("select o from Order o join fetch o.member"
                , Order.class).getResultList();
        for (Order order : orderList) {
            System.out.println(order.getMember().getName()+"님은 "+order.getItem()+"을 주문함.");
        }
    }
    // N+1 소개 끝

    // Fetch Join에 조건걸기 시작
    @Test
    public void Banana를주문한사람(){
        List<Order> orderList = em.createQuery("select o from Order o join fetch o.member where o.item = 'Banana'"
                , Order.class).getResultList();
        for (Order order : orderList) {
            System.out.println(order.getMember().getName()+"님은 "+order.getItem()+"을 주문함");
        }
    }

    @Test
    public void 회원명이Lee인사람(){
        List<Order> orderList = em.createQuery("select o from Order o join fetch o.member m where m.name = 'Lee'"
                , Order.class).getResultList();
        for (Order order : orderList) {
            System.out.println(order.getMember().getName()+"님은 "+order.getItem()+"을 주문함");
        }
    }

    @Test
    public void 회원명이Lee인사람2(){ //다대일에서 일대다로 조건 바꾸기
        List<Member> memberList = em.createQuery("select m from Member m join fetch m.orders where m.name = 'Lee'"
                , Member.class).getResultList();
        for (Member member : memberList) {
            List<Order> orderList = member.getOrders();
            for (Order order : orderList) {
                System.out.println(member.getName()+"님은 "+order.getItem()+"을 주문함");
            }
        }
    }

    @Test
    public void 회원명이Lee인사람3(){ //distinct로 데이터 뻥튀기 제거
        List<Member> memberList = em.createQuery("select distinct m from Member m join fetch m.orders o where m.name = 'Lee'"
                , Member.class).getResultList();
        for (Member member : memberList) {
            List<Order> orderList = member.getOrders();
            for (Order order : orderList) {
                System.out.println(member.getName()+"님은 "+order.getItem()+"을 주문함");
            }
        }
    }

    @Test
    public void 컬렉션데이터는조건에상관없이전부나온다(){
        List<Member> memberList = em.createQuery("select m from Member m join m.orders o where o.item ='Apple'", Member.class)
                .getResultList();
        for (Member member : memberList) {
            List<Order> orderList = member.getOrders();
            for (Order order : orderList) {
                System.out.println(member.getName()+"님은 "+order.getItem()+"을 주문함");
            }
        }
    }

    @Test
    public void 컬렉션조인에Apple필터링(){
        List<Member> memberList = em.createQuery("select distinct m from Member m join fetch m.orders o where o.item = 'Apple'"
                , Member.class).getResultList();

        for (Member member : memberList) {
            List<Order> orderList = member.getOrders();
            for (Order order : orderList) {
                System.out.println(member.getName()+"님은 "+order.getItem()+"을 주문함");
            }
        }
    }

    @Test
    public void 컬렉션이잘못필터링되면생기는문제(){
        List<Member> memberList = em.createQuery("select distinct m from Member m join fetch m.orders o where o.item = 'Apple'"
                , Member.class).getResultList();
        List<Member> memberList2 = em.createQuery("select distinct m from Member m join fetch m.orders"
                , Member.class).getResultList();

        for (Member member : memberList2) {
            List<Order> orderList = member.getOrders();
            for (Order order : orderList) {
                System.out.println(member.getName()+"님은 "+order.getItem()+"을 주문함");
            }
        }
    }
    // Fetch Join에 조건걸기 끝

    // Fetch Join 결과를 올바르게 받아오기 시작
    //Stateless Session 조회
    @Test
    public void StatelessSession로조회(){
        Session session = em.unwrap(Session.class);
        SessionFactory sessionFactory = session.getSessionFactory();
        List<Member> memberList = session.doReturningWork(connection -> {
            StatelessSession statelessSession = sessionFactory.openStatelessSession(connection);
            return statelessSession.createQuery("select distinct m from Member m join fetch m.orders o where o.item = 'Apple'"
                    , Member.class).getResultList();
        });

        for (Member member : memberList) {
            List<Order> orderList = member.getOrders();
            for (Order order : orderList) {
                System.out.println(member.getName()+"님은 "+order.getItem()+"을 주문함");
            }
        }
    }

    //DTO로 조회
    @Test
    public void DTO조회(){
        List<MemberDto> memberDtoList = em.createQuery(
                "select com.example.jpafetchjoin.MemberDto() " +
                        "from Member m join fetch m.orders o where o.item = 'Apple'", MemberDto.class)
                .getResultList();

        /*for (MemberDto memberDto : memberDtoList) {
            List<Order> orderList = memberDto.getOrders();
            for (Order order : orderList) {
                System.out.println(memberDto.getName()+"님은 "+order.getItem()+"을 주문함");
            }
        }*/
    }
    // Fetch Join 결과를 올바르게 받아오기 끝
}

