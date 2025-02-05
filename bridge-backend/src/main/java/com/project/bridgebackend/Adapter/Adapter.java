package com.project.bridgebackend.Adapter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Adapter {
    public static void main(String[] args) {
        Process pros;
        try {
            pros = Runtime.getRuntime().exec("Python3 /Users/mariozurolo/AI-Bridge/Reccomendation-System/Adapter/Adapter.py");
            BufferedReader in = new BufferedReader(new InputStreamReader(pros.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                System.out.println(line);
            }
            in.close();
            pros.waitFor();
        }catch (IOException e){
            e.printStackTrace();
        }catch (InterruptedException e){
            e.printStackTrace();
        }
    }
}
