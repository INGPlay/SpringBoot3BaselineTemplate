package baseline.version3.springboot.pageAdmin.page.handler;

import baseline.version3.springboot.config.security.authenticationManager.AccountContext;
import baseline.version3.springboot.pageAdmin.page.domain.subPage.SubPageRequest;
import baseline.version3.springboot.pageAdmin.page.domain.subPage.SubPageResponse;
import baseline.version3.springboot.pageAdmin.page.service.SubPageService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class DynamicPageAuthorityHandler {

    private final SubPageService subPageService;
    private final AntPathMatcher antPathMatcher;

    private final List<String> staticPaths = new ArrayList<>(List.of(new String[]{
            "/framework/**",
            "/library/**",
            "/img/**",
            "/favicon.ico"
    }));

    public boolean isPageAuthorization(HttpServletRequest request, Authentication authentication) throws IOException {

        // GET 방식만 체크한다.
        if (!request.getMethod().equalsIgnoreCase("GET") ||
                staticPaths.stream().anyMatch(p -> antPathMatcher.match(p, request.getRequestURI()))
        ){
            return true;
        }

        SubPageResponse.Response subPage = subPageService.findMatchedPage(request);
        if (subPage == null) return true;

        Collection<? extends GrantedAuthority> authorities = getGrantedAuthorities(authentication.getPrincipal());
        
        // 등록해놓은 페이지에 제한사항이 없을 경우
        if (!StringUtils.hasText(Objects.requireNonNull(subPage).pageAuthorityCode())){
            return true;

        // 제한사항은 있는데, 사용자의 권한이 없을 경우
        } else if (authorities == null){
            return false;
        }

        return authorities.contains(new SimpleGrantedAuthority(getRoleString(subPage.pageAuthorityCode())));
    }

    private static Collection<? extends GrantedAuthority> getGrantedAuthorities(Object principal) {
        Collection<? extends GrantedAuthority> authorities = null;
        // 로그인 방식에 따른 처리
        if (principal instanceof AccountContext accountContext){
            authorities = accountContext.getAuthorities();

        } else if (principal instanceof OidcUser oidcUser){
            List<String> roles = (List<String>) (((Map<String, Object>) oidcUser.getAttribute("realm_access")).get("roles"));
            authorities = roles.stream().map(role -> new SimpleGrantedAuthority("ROLE_" + role)).collect(Collectors.toList());
        } else {
            assert false : "지원하지 않는 유저 토큰입니다.";
        }
        return authorities;
    }
    private String getRoleString(String roleCode){
        return "ROLE_" + roleCode;
    }
}
