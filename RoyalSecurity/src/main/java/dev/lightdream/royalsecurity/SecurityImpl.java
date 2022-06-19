package dev.lightdream.royalsecurity;

import dev.lightdream.ticketsystem.ISecurity;

public class SecurityImpl implements ISecurity {

    @Override
    public void sayHello() {
        System.out.println("Hello from the plugin security implementation!");
    }
}
