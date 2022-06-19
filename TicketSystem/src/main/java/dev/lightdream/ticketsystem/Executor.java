package dev.lightdream.ticketsystem;

public class Executor {

    public static void main(String[] args) {
        new Main().onEnable(new ISecurity() {
        });
    }
}
