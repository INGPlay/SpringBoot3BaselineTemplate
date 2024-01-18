package baseline.version3.springboot.pageAdmin.repository.querydsl;

import baseline.version3.springboot.common.util.QueryDslNullableUtil;
import baseline.version3.springboot.pageAdmin.domain.subPage.SubPageRequest;
import baseline.version3.springboot.pageAdmin.domain.subPage.SubPageResponse;
import baseline.version3.springboot.pageAdmin.repository.entity.QPageAuthority;
import baseline.version3.springboot.pageAdmin.repository.entity.QParentPage;
import baseline.version3.springboot.pageAdmin.repository.entity.QSubPage;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class QSubPageRepository {

    private final JPAQueryFactory jpaQueryFactory;
    private final QueryDslNullableUtil queryDslNullableUtil;

    private QParentPage parentPage = QParentPage.parentPage;
    private QSubPage subPage = QSubPage.subPage;
    private QPageAuthority pageAuthority = QPageAuthority.pageAuthority;

    public List<SubPageResponse.Response> selectList(SubPageRequest.RequestDynamicQuery requestDynamicQuery){
        return jpaQueryFactory
                .select(
                        Projections.constructor(
                                SubPageResponse.Response.class,
                                subPage.subPageId,
                                subPage.subPageTitle,
                                subPage.subPageDescription,
                                subPage.subPagePath,
                                pageAuthority.pageAuthorityCode,
                                subPage.registerDate,
                                subPage.lastModifyDate
                        )
                )
                .from(subPage)
                .join(subPage.parentPage, parentPage)
                .leftJoin(subPage.pageAuthority, pageAuthority)
                .where(
                        parentPage.parentPageId.eq(requestDynamicQuery.getParentPageId())
                )
                .fetch();

    }

    public Optional<SubPageResponse.Response> selectOne(SubPageRequest.RequestDynamicQueryOne requestDynamicQueryOne){
        return Optional.ofNullable(
                jpaQueryFactory
                        .select(
                                Projections.constructor(
                                        SubPageResponse.Response.class,
                                        subPage.subPageId,
                                        subPage.subPageTitle,
                                        subPage.subPageDescription,
                                        subPage.subPagePath,
                                        pageAuthority.pageAuthorityCode,
                                        subPage.registerDate,
                                        subPage.lastModifyDate
                                )
                        )
                        .from(subPage)
                        .join(subPage.parentPage, parentPage)
                        .leftJoin(subPage.pageAuthority, pageAuthority)
                        .where(
                                parentPage.parentPageId.eq(requestDynamicQueryOne.getParentPageId()),
                                queryDslNullableUtil.eq(subPage.subPageId, requestDynamicQueryOne.getSubPageId()),
                                queryDslNullableUtil.eq(subPage.subPagePath, requestDynamicQueryOne.getSubPagePath())
                        )
                        .fetchOne()
        );
    }
}