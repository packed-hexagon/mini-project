package com.group6.accommodation.global.annotation.aspect;

import com.group6.accommodation.global.annotation.RedissonLock;
import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class RedissonLockAspect {

    private final RedissonClient redissonClient;
    private final AopForTransaction transaction;

    // 이 메소드는 @RedissonLock 애노테이션이 붙은 메소드가 호출될 때 실행됩니다.
    @Around("@annotation(com.group6.accommodation.global.annotation.RedissonLock)")
    public Object redissonLock(ProceedingJoinPoint joinPoint) throws Throwable {
        // 메소드 시그니처를 가져옵니다.
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();

        // @RedissonLock 애노테이션을 가져옵니다.
        RedissonLock annotation = method.getAnnotation(RedissonLock.class);

        // 락 키를 생성합니다. postReservation1
        String lockKey = method.getName() + ElParser.getDynamicValue(signature.getParameterNames(), joinPoint.getArgs(), annotation.key());
        log.error("Generated lock key: {}", lockKey);

        // 락 객체를 가져옵니다.
        RLock lock = redissonClient.getLock(lockKey);
        log.error("Acquired lock object for key: {}", lockKey);


        try {
            // 주어진 시간 동안 락을 시도합니다.
            boolean lockable = lock.tryLock(annotation.waitTime(), annotation.leaseTime(), TimeUnit.MILLISECONDS);
            if (!lockable) {
                return false;
            }

            // 실제 메소드를 호출합니다.
            return transaction.proceed(joinPoint);
        } catch (InterruptedException e) {
            throw e;
        } finally {
            lock.unlock();
        }
    }


}
