package by.geron.scanner.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Slf4j
@Aspect
@Component
public class ServiceLogAspect extends BaseAspect {

    @Pointcut("execution(* by.geron.scanner.service..*(..)) " +
            "&& !@annotation(by.geron.scanner.aop.ExcludeLog)")
    public void before() {

    }

    @Pointcut("execution(* by.geron.scanner.service..*(..)) " +
            "&& !@annotation(by.geron.scanner.aop.ExcludeLog)")
    public void after() {

    }

    @Before("before()")
    public void logServiceBefore(JoinPoint joinPoint) {
        log.info(BEFORE_PATTERN_SERVICE,
                joinPoint.getSignature().toShortString(),
                getARgsWithNames(joinPoint));
    }

    @AfterReturning(pointcut = "after()", returning = "result")
    public void logServiceAfter(JoinPoint joinPoint, Object result) {
        log.info(AFTER_PATTERN_SERVICE,
                joinPoint.getSignature().toShortString(),
                getStringInstanceOf(Optional.ofNullable(result).orElse("")),
                getARgsWithNames(joinPoint));
    }

}
