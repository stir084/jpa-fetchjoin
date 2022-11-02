package com.example.jpafetchjoin.repository;

public class Memo {
   /* 1차 캐시 2차 캐시에 모두 들어가지 않는 statelessSession.createQuery를 이용하거나 DTO를 이용해값을 받는다..
    https://blog.termian.dev/posts/jpql-join-fetch-with-condition/

            1L 캐시 혹은 2L 캐시와 연관이 있나보긴하네

    컬렉션 캐싱을 완전히 비활성화

    https://hibernate.atlassian.net/browse/HHH-7863
    https://hibernate.atlassian.net/browse/HHH-2003

    JPQL: "SELECT master FROM Master master JOIN FETCH master.details details WHERE details.description=:description"을 작성하고 ":description"에 값을 전달하면 결과적으로 Master의 인스턴스가 몇 개 있습니다. 세부 정보는 WHERE 절과 일치하는 세부 정보만 갖게 됩니다.

    이제 다른 JPQL: "SELECT master FROM Master master JOIN FETCH master.details WHERE master.id=:id"를 작성하고 이전 쿼리에서 얻은 마스터의 기본 키인 인스턴스를 ":id"로 사용하면 Hibernate에 의해 반환된 Master는 두 번째 쿼리가 세부 사항을 전혀 필터링하지 않는다는 사실에도 불구하고 첫 번째 쿼리에 있었던 세부 사항만 가질 것입니다.


    http://java-persistence-performance.blogspot.com/2012/04/objects-vs-data-and-filtering-join.html
    이는 JPA가 객체를 다루기 때문에 Department 객체는 특정 부서를 대표하고 특정 ID를 갖기 때문입니다. 특정 부서에 대해 두 개의 쿼리를 실행하면 동일한 동일한(==) 인스턴스가 반환됩니다(두 쿼리가 동일한 EntityManager를 사용하는 경우).

   .*/
}
