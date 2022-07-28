package by.geron.scanner.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@Slf4j
@Aspect
@Component
public class ControllerAspect extends BaseAspect {

    @Pointcut("execution(* by.geron.scanner.controller..*(..)) " +
            "&& !@annotation(by.geron.scanner.aop.ExcludeLog)")
    public void before() {

    }

    @Pointcut("execution(* by.geron.scanner.controller..*(..)) " +
            "&& !@annotation(by.geron.scanner.aop.ExcludeLog)")
    public void after() {

    }

    @Before("before()")
    public void logControllersBefore(JoinPoint joinPoint) {
        final HttpServletRequest request =
                ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        log.info(BEFORE_PATTERN_CONTROLLER,
                request.getMethod(),
                joinPoint.getSignature().toShortString(),
                request.getRequestURI(),
                getARgsWithNames(joinPoint));
    }

    @AfterReturning(pointcut = "after()", returning = "result")
    public void logControllerAfter(JoinPoint joinPoint, Object result) {
        final HttpServletRequest request =
                ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        log.info(AFTER_PATTERN_CONTROLLER,
                request.getMethod(),
                joinPoint.getSignature().toShortString(),
                request.getRequestURI(),
                getStringInstanceOf(Optional.ofNullable(result).orElse("")),
                getARgsWithNames(joinPoint));
    }

}
