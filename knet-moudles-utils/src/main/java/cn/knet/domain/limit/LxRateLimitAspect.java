package cn.knet.domain.limit;

import lombok.extern.slf4j.Slf4j;
import net.jodah.expiringmap.ExpirationPolicy;
import net.jodah.expiringmap.ExpiringMap;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.HandlerMapping;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Slf4j
@Aspect
@Component
public class LxRateLimitAspect {

    private static ExpiringMap<String, ACount> uc = ExpiringMap.builder().variableExpiration().build();


    /**
     * 切点
     * 通过扫包切入 @Pointcut("execution(public * com.ycn.springcloud.*.*(..))")
     * 带有指定注解切入 @Pointcut("@annotation(com.ycn.springcloud.annotation.LxRateLimit)")
     */
    @Pointcut("@annotation(cn.knet.domain.limit.LxRateLimit)")
    public void pointcut() {
    }

    @Around("pointcut()")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        MethodSignature signature = (MethodSignature) point.getSignature();
        Method method = signature.getMethod();
        if (method.isAnnotationPresent(LxRateLimit.class)) {
            LxRateLimit lxRateLimit = method.getAnnotation(LxRateLimit.class);
            Map<?, ?> pathVariables = (Map<?, ?>) request.getAttribute(
                    HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
            String limitKeyValue = (String) pathVariables.get(lxRateLimit.name());
            ACount uCount = uc.getOrDefault(limitKeyValue, new ACount());
            if (uCount.getNum() >= lxRateLimit.max()) { // 超过次数，不执行目标方法
                throw new RuntimeException("[" + limitKeyValue + "]访问频率过快");
            } else if (uCount.getNum() == 0) { // 第一次请求时，设置有效时间
                uCount.setNum(1);
                uc.put(limitKeyValue, uCount, ExpirationPolicy.CREATED, lxRateLimit.time(), TimeUnit.MILLISECONDS);
            } else { // 未超过次数， 记录加一
                uCount.setNum(uCount.getNum() + 1);
            }
            log.info(uCount.toString());
            log.info("[{}]访问次数：{}", limitKeyValue, uCount.getNum());
        }
        return point.proceed();
    }
}