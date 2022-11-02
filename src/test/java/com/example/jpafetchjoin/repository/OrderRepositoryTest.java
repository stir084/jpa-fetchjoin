package com.example.jpafetchjoin.repository;

import com.example.jpafetchjoin.domain.Member;
import com.example.jpafetchjoin.domain.Order;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;


import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class OrderRepositoryTest {

    @PersistenceContext
    EntityManager em;

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
    public void 테스트(){ //nplus1해결
        List<Order> orderList = orderRepository.findAllFetchJoin();
        for (Order order : orderList) {
            System.out.println(order.getMember().getName());
        }
    }

    //N+1은 느리지않다.

    @Test
    @Transactional
    public void 테스트2(){ //fetch join을 하면서 주문 아이템이 banana인 케이스
        List<Order> orderList = orderRepository.findAllFetchJoin2();
        for (Order order : orderList) {
            System.out.println(order.getItem());
        }
    }
    //페치조인에 alias 사용. 하지만 다대일이라서 상관없음. 실제로 Member에 조거

    @Test
    @Transactional
    @Rollback(false) //fetch join을 하면서 Member id가 1인 케이스 ->
    public void 테스트3(){
        List<Order> orderList = orderRepository.findAllFetchJoin3();
        for (Order order : orderList) {
            System.out.println("머냐"+order.getMember().getName());
        }
        List<Order> orderList2 = orderRepository.findAllFetchJoin4();
        for (Order order : orderList2) {
            System.out.println("짠"+order.getItem());
        }
    }
    //위에꺼 하기전에 내일 데이터 뻥튀기 예제 확인하기

    //얼리어스는 하이버네이트만 허용한다.
    //설명 1 - fetch join의 대상에 alias를 주면 안됨. 왜 안되는지는 설명을 넘기고 일단은
    //fetch join 대상에 alias를 줘서 조건을 걸고 싶은 경우에는 반대로 일대다 -> 다대일로 만들기

    //설명 2 - 근데 어쩔 수 없이 Member에도 걸고 Order에도 걸면 fetch join 대상에 alias를 주고 조건을 걸 수 밖에 없다.
    // 근데 이런 경우 보통 일대다인 경우에 일관성이 깨져버린다.
    //fetch join의 대상이 되는 entity는 항상 다 들고온다는 전제하에 개발이 되어야 한다.
    // JPQL은 SQL이 아니라 객체를 대상으로 하기 때문에 관계성을 포함해서 Collection 데이터는 어떠한 필터링을 걸더라도 전부 나와야하는게 정상이라고 함.(이게 데이터 뻥튀기인가?)
    // findAllFetchJoin()예제 참조

    //근데 일대다 fetch join에 필터링을 걸면 전부 나오지 않고 일부만 나와버린다.
    //이런 경우 어떤 문제가 생기냐면 테스트4 참조
   /// 이는 JPA가 객체를 다루기 때문에 Department 객체는 특정 부서를 대표하고 특정 ID를 갖기 때문입니다. 특정 부서에 대해 두 개의 쿼리를 실행하면 동일한 동일한(==) 인스턴스가 반환됩니다(두 쿼리가 동일한 EntityManager를 사용하는 경우).

    //  2차 캐시에서 특히 발생한다. 왜냐면 같은 부서를 2번 조회할리는 없을 테니까.

    //얼리어스는 하이버네이트만 허용한다.(실제로 알고 쓰면 조회는 가능하기 때문에 하이버네이트 측에서 안고치나보다, 이 내용은 아래에 기술 DTO 혹은 무상태세션)
     /*   추가로 일관성이 깨져도 엔티티를 변경하지 않고 딱! 조회용으로만 주의해서 사용하면 크게 문제는 없습니다.
    하지만 이 경우 정말 조심해서 조회 용도로만 사용해야합니다. (여기서 2차 캐시 등등을 사용하면 또 문제가 될 수 있기 때문에 ㅠㅠ 여기까지 가면 너무 복잡해지네요)*/


    //

    @Test
    @Transactional
    @Rollback(false)
    public void 테스트4(){
        List<Member> memberList = memberRepository.findAllFetchJoin();
        System.out.println("이걸보냄"+memberList.get(0).getId());
        //em.clear();
        //왜 동일한 1차 캐시를 사용하지?
        List<Member> memberList2 = memberRepository.findAllFetchJoin2(memberList.get(0).getId());
        for (Member member : memberList2) {
            System.out.println(member);
            List<Order> orderList = member.getOrders();
            for (Order order : orderList) {
                System.out.println("주문이거함" + order.getItem());
            }
        }

        List<Member> memberList3 = memberRepository.findAllFetchJoin3();
        for (Member member : memberList2) {
            System.out.println(member);
            List<Order> orderList = member.getOrders();
            for (Order order : orderList) {
                System.out.println("이건바껴야지" + order.getItem());
            }
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

//다대일은 상관없다
//일대다인 경우에 fetch join의 대상이 되는 entity는 항상 다 들고온다는 전제하에 개발이 되어야 한다.
//근데 alias를 통해 필터링을 걸어버리면 다들고와지지 않는데 이러면 JPA가 일관성 파악을 하지 못한다.
//다대일이 안되는 건 어차피 fetch join의 대상이 되는 일의 경우 어떠한 조건을 걸든 전부 다 들고와지기 때문에 일관성이 존재한다.
//다대일은 어차피 '다'라고해서 콜렉션 형태가 아니다.