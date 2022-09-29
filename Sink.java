package Sim;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;


public class Sink extends Node{

    public Sink(int network, int node) {
        super(network, node);
    }
    public static void toFile(String fileName, double time) {
        try {

            PrintWriter writer = new PrintWriter(fileName, StandardCharsets.UTF_8);
            writer.println("CBR: " +time);

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }






    }
