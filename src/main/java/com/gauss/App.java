package com.gauss;

import com.gauss.Model.Menu;
import java.util.Scanner;

public class App
{
    public static void main( String[] args ) {
        //iniciando sistema.
        Scanner sc = new Scanner(System.in);
        Menu menu = new Menu(sc);
        menu.exibirMenu();
    }
}
