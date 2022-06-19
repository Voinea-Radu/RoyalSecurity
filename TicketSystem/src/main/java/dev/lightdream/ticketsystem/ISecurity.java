package dev.lightdream.ticketsystem;

public interface ISecurity {

    default void sayHello(){
        System.out.println("Hello from the default security implementation!");
    }

}
