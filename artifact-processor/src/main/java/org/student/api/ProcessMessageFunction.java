package org.student.api;

@FunctionalInterface
public interface ProcessMessageFunction<F, S, T>{

    void accept(F f, S s, T topic);

}
