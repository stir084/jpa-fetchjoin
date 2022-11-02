package com.example.jpafetchjoin.repository;

import com.example.jpafetchjoin.domain.Member;
import com.example.jpafetchjoin.domain.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class MemberRepository {

    private final EntityManager em;

    public List<Member> findAllFetchJoin() {
        return em.createQuery("select m from Member m join fetch m.orders o where o.item = 'apple'", Member.class) //join fetch Member가 아니다.
                .getResultList();
    }
    public List<Member> findAllFetchJoin2(Long id) {
        return em.createQuery("select m from Member m join fetch m.orders", Member.class)
               //.setParameter("id", id)//join fetch Member가 아니다.
                .getResultList();
    }
    public List<Member> findAllFetchJoin3() {
        return em.createQuery("select m from Member m join fetch m.orders where m.id = 1", Member.class)
                //.setParameter("id", id)//join fetch Member가 아니다.
                .getResultList();
    }
    //https://stackoverflow.com/questions/5816417/how-to-properly-express-jpql-join-fetch-with-where-clause-as-jpa-2-criteriaq/5819631#5819631
    //select m from Member m join m.orders o where o.item ='apple'
    //이거 왜 컬렉션에 조건 걸었는데 apple banana orange 다 나와??
    //select member0_.member_id as member_i1_0_, member0_.name as name2_0_
    // from Member member0_ inner join orders orders1_
    // on member0_.member_id=orders1_.member_id
    // where orders1_.item='apple'
    // inner join만
    //지연로딩으로 인해 Member부터 가져오고 Order가 필요할 떄 lazy loading이 발생해서 3개가 다 나오는 듯 하다.
    //JPQL에서 콜렉션 조인은 모든 데이터를 다 가져오나?
    //fetch join은 죄다 가져오는게 원칙인데 일부만 가져오면 안된다.
    //그럼 내부적으로 JPA가 죄다가져왔다고 판단하는데, 이때 2차 캐시고 뭐고 해서 데이터 정합성이 꺠질 수 있다.
    //난 그 꺠지는걸 구현하고싶은데 어떻게 하지 ㅠㅠㅠㅠㅠㅠ

    //select member0_.member_id as member_i1_0_, member0_.name as name2_0_
    // from Member member0_ inner join orders orders1_
    // on member0_.member_id=orders1_.member_id

    //JPQL과 Fetch join 다시 강의 듣기..
    //https://www.inflearn.com/questions/15876
    //페치조인은 다 가져오는 방식으로 사용해야한다.

    //https://loosie.tistory.com/750
    //https://stackoverflow.com/questions/5816417/how-to-properly-express-jpql-join-fetch-with-where-clause-as-jpa-2-criteriaq/5819631#5819631
    //http://java-persistence-performance.blogspot.com/2012/04/objects-vs-data-and-filtering-join.html

    //일반 SQL쿼리는 on절이 필요하지만 JPQL은 ON절 없이도 on이 자동으로 묵시적 조인이 발생한다.
    //묵시적 조인은 그냥 다른 객체 갖다 쓸 때 조인 발생하는거고
    //"또한 관계는 매핑에서 관계의 조인 열에 정의된 외래 키에 의해 항상 조인되기 때문에 JPQL에는 ON 절이 없습니다." http://java-persistence-performance.blogspot.com/2012/04/objects-vs-data-and-filtering-join.html
    //ON이없는 이유는 위와 같다.

   /* Select e from Employee e
    join e.phones p
    where p.areaCode = '613'*/
    //  return em.createQuery("select m from Member m fetch join m.orders o where m.id = 2 and o.id >= 5", Member.class) //join fetch Member가 아니다.

    ////////---------
   /* 다대일은 되는 이유?
    다대일 쿼리는 회원과 팀의 일관성을 해치지 않습니다. 그러니까 조회된 회원은 db와 동일한 일관성을 유지한 팀의 결과를 가지고 있습니다.
        근데 얘는 쿼리문이랑 나온 데이터가 일치하다이거고..

    따라서 전체 직원 엔터티( e)를 선택하면 where절이 무엇이든 실제로 해당 직원의 모든 전화를 포함해야 합니다.
    -JPQL은 SQL이 아니라 객체를 대상으로 하는데 일대다 쿼리를 실행할 경우 where절 조건이 어떻든간에 N의 데이터는 모든 데이터가 출력이 된다.

    findAllFetchJoin 얘를 보면 원래 페치조인의 결과는 jpa가 멤버 1개당 오더 3개로 총 3개의 레코드를 예상하는데
    필터링으로인해 갑자기 1개만 툭 튀어나와버리니까 일관성이 떨어지는거다.

            findAllFetchJoin3 근데 얘를 보면 다대일은 멤버 1개당 오더 3개로 총 3개의 레코드를 예상하는데
    필터링을 걸어도 3개가 나올테니 일관성이 일치한다는 것이다.
        그니까 메인 entity 곱하기 모든 콜렉션 데이터의 개수가 보장되어야 한다는 뜻..

    그럼 다대일도 다쪽을 필터링걸면*/
/*        그니까 얘는 쿼리문이랑 나온 데이터가 다르다는거네.. 쿼리문은 이미 제거된 상태인거같고 나온 데이터는 죄다 뿌려진걸로 예상하고
        그니까 inner 조인은 그냥 어떤 조건을 걸어도 전부 튀어나오니까 일관성이 있는데(나중 조회 가능)
    inner조인이 아닌 fetch join은 조건을 걸면 삭제되어버리는 특징 때문에 (나중 조회 불가)*/


 /*   추가로 일관성이 깨져도 엔티티를 변경하지 않고 딱! 조회용으로만 주의해서 사용하면 크게 문제는 없습니다.
    하지만 이 경우 정말 조심해서 조회 용도로만 사용해야합니다. (여기서 2차 캐시 등등을 사용하면 또 문제가 될 수 있기 때문에 ㅠㅠ 여기까지 가면 너무 복잡해지네요)*/
}