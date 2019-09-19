package com.gremlin.todo.aspect;

import com.gremlin.TrafficCoordinates;
import com.gremlin.todo.dto.ToDoDto;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Attack
public class AdvancedAttackAspect {
    private final Logger LOG = LoggerFactory.getLogger(getClass().getName());

    private final AttackAspect attackAspect;

    @Autowired
    public AdvancedAttackAspect(AttackAspect attackAspect) {
        this.attackAspect = attackAspect;
    }

    @Before("@annotation(advancedAttack)")
    public void setUpAttack(JoinPoint joinPoint, AdvancedAttack advancedAttack) throws Throwable {
        ToDoDto args = (ToDoDto) joinPoint.getArgs()[0];
        LOG.info(args.toString());
        TrafficCoordinates trafficCoordinates = attackAspect.setupAttack(advancedAttack.type(), advancedAttack.fields());
        attackAspect.executeAttack(trafficCoordinates);
    }
}
