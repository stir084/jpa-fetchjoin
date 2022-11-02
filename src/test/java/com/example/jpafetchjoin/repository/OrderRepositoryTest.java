package com.example.jpafetchjoin.repository;

import com.example.jpafetchjoin.domain.Member;
import com.example.jpafetchjoin.domain.Order;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;


import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class OrderRepositoryTest {

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    MemberRepository memberRepository;

    @Test
    @Transactional
    public void Nplus1(){
        List<Order> orderList = orderRepository.findAll();
        for (Order order : orderList) {
            //System.out.println(order.getItem());
            //System.out.println(order.getMember().getName());
            System.out.println(order.getMember().getName());
        }
    }

    @Test
    @Transactional
    public void 테스트(){
        List<Order> orderList = orderRepository.findAllFetchJoin();
        for (Order order : orderList) {
            System.out.println(order.getMember().getName());
        }
    }

    @Test
    @Transactional
    public void 테스트2(){
        List<Order> orderList = orderRepository.findAllFetchJoin2();
        for (Order order : orderList) {
            System.out.println(order.getItem());
        }
    }

    @Test
    @Transactional
    @Rollback(false)
    public void 테스트3(){
        List<Order> orderList = orderRepository.findAllFetchJoin3();
        System.out.println(orderList.size());
        for (Order order : orderList) {
            order.getMember().setName("hello");
        }
    }

    @Test
    @Transactional
    @Rollback(false)
    public void 테스트4(){
        List<Member> memberList = memberRepository.findAllFetchJoin();
        System.out.println(memberList.size());
        for (Member member : memberList) {
            System.out.println(member.getName());
            List<Order> orderList = member.getOrders();
            for (Order order : orderList) {
                System.out.println("주문" + order.getItem());
            }
            //member.setName("boom");
        }
    }
/*
query.where(team.name.eq("AAA")

        .and(member.age > 20));
*/

    //fetch join은 기본적으로 from 대상이 되는 조건을 죄다 들고온다. 그리고 JPA가 내부적으로 그걸 다 들고온걸로 알고 설계되어있다.
    //그래서 DB와의 정합성이 일치하다고 판단한다.
    //그렇기때문에 from 대상은 where 조건이 가능한데, 만약에 fetch join의 필터링을 걸어버리면 JPA는 다 들고온걸로 판단한 것과 다르게 되어있어
    //set같은 더티체킹 시 이상하게 삭제, 삽입이 될 수도 있다.

    //member id가 2인 entity 가져오기 - 틀린 예제는 fetch join에 조건이 걸림

    //올바른 예졔는 from entity에 조건이 걸림.

    //member id2, item이 apple인 경우 조건을 2개나 걸어야한다.
    //올바르지 않은 예제 1 - 쿼리 2번 날리기 - 쓸 가치도 없다.
    //네이티브 쿼리 2
    //올바른 쿼리 1 - 값 타입DTO로 받아오기

}
