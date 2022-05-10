package com.assignment;


import uk.ac.leedsbeckett.oop.LBUGraphics;

import javax.swing.*;
import java.awt.*;

public class MainClass {

    public static void main(String[] args) {

        JFrame frame = new JFrame();
        LBUGraphics lbuGraphics = new LBUGraphics() {
            @Override
            public void processCommand(String s) {
                if (s.equalsIgnoreCase("about")) {
                    about();
                }
            }
        };
        frame.add(lbuGraphics);
        frame.setLayout(new FlowLayout());
        frame.setSize(new Dimension(1050, 450));
        frame.setVisible(true);

    }
}
