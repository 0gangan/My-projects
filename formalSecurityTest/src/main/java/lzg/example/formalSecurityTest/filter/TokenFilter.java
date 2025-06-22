package lzg.example.formalSecurityTest.filter;

import com.alibaba.fastjson.JSON;
import lzg.example.formalSecurityTest.config.DemoConfiguration;
import lzg.example.formalSecurityTest.constant.RedisKey;
import lzg.example.formalSecurityTest.domain.SecurityUser;
import lzg.example.formalSecurityTest.utils.JwtUtil;
import lzg.example.formalSecurityTest.utils.R;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
//import jakarta.servlet.ServletRequest;
//import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
//import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.CollectionUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
@Component
public class TokenFilter extends OncePerRequestFilter {
    private Logger log = LoggerFactory.getLogger(getClass());
    private AntPathMatcher pathMatcher = new AntPathMatcher();

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private DemoConfiguration.Security security;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = request.getHeader(JwtUtil.TOKEN_HEADER);

        log.info("intercept " + request.getRequestURI());
        log.info("header token " + request.getHeader("token"));
        log.info("header Content-Type " + request.getHeader("Content-Type"));
        log.info("method "+ request.getMethod());
        System.out.println("intercept " + request.getRequestURI());
        System.out.println("header token " + request.getHeader("token"));
        System.out.println("header Content-Type " + request.getHeader("Content-Type"));
        System.out.println("method "+ request.getMethod());
        // token=1用于swagger页面调用API
        /*if (!StringUtils.hasText(token) || "1".equals(token)) {
            filterChain.doFilter(request, response);
            return;
        }*/
        // 判断是否是放行请求
        if (isFilterRequest(request)) {
            filterChain.doFilter(request, response);
            log.info("filter request no" );
            System.out.println("filter request no");
            return;
        }
        Claims claims = null;
        try {
            claims = JwtUtil.parseToken(token);
        } catch (Exception e) {
            log.error(e.getMessage());
            log.info("parseToken no" );
            fallback("token解析失败(非法token)！", response);
            return;
        }
        String username = claims.getSubject();
        String cache = (String) redisTemplate.opsForValue().get(String.format(RedisKey.AUTH_TOKEN_KEY, username));
        if (cache == null) {
            fallback("token失效，请重新登录！", response);
            return;
        }
        SecurityUser user = JSON.parseObject(cache, SecurityUser.class);
        log.info(JSON.toJSONString(user, true));
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        // 放行
        filterChain.doFilter(request, response);
    }

    private void fallback(String message, HttpServletResponse response) {
        response.setCharacterEncoding("UTF-8");
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        PrintWriter writer = null;
        try {
            if (message == null) {
                message = "403 Forbidden";
            }
            R res = R.error(403, message);
            writer = response.getWriter();
            writer.append(JSON.toJSONString(res));
        } catch (IOException e) {
            log.error(e.getMessage());
        } finally {
            if (writer != null) {
                writer.close();
            }
        }
    }

    private boolean isFilterRequest(HttpServletRequest request) {
        String contextPath = request.getContextPath();
        String filterPath = request.getRequestURI();
        List<String> permitAllPathList = security.getPermitAllPath();
        if (CollectionUtils.isEmpty(permitAllPathList)) {
            return false;
        }
        for (String path : permitAllPathList) {
            String pattern = contextPath + path;
            pattern = pattern.replaceAll("/+", "/");
            if (pathMatcher.match(pattern, filterPath)) {
                return true;
            }
        }

        return false;
    }
}