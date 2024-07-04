package org.student.api;

@FunctionalInterface
public interface VoidBiFunction <F, S>{

    void accept(F f, S s);

}
