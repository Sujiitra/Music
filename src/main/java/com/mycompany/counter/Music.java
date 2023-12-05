/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.counter;

import java.io.File;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

/**
 *
 * @author SM
 */
class Music {
    private int count = 0;
    private int ecount = 0;
    private int ocount =0;
    
    private final Object lock = new Object();
     private static final String[] THREAD2_TONES = {"./Sounds/do.wav", "./Sounds/mi.wav", "./Sounds/sol.wav", "./Sounds/si.wav", "./Sounds/do-octave.wav"};
    private static final String[] THREAD1_TONES = {"./Sounds/re.wav", "./Sounds/fa.wav", "./Sounds/la.wav", "./Sounds/do-octave.wav"};
    private static final String finishTone = "./Sounds/do-octave.wav";
public static void main(String[] args) {
        Music counter = new Music();

        // Creating odd thread
        Thread oddThread = new Thread(() -> {
            counter.printOdd();
        }, "OddThread");

        // Creating even thread
        Thread evenThread = new Thread(() -> {
            counter.printEven();
        }, "EvenThread");

        // Starting both threads
        oddThread.start();
        evenThread.start();
    }
    public void printOdd() {
        synchronized (lock) {
            while (count < 9) {
                if (count % 2 == 1 && !(THREAD1_TONES[ocount].equalsIgnoreCase(finishTone))) {
                    System.out.println(Thread.currentThread().getName() + ": " + count +" "+ THREAD1_TONES[ocount]);
                    play(THREAD1_TONES[ocount++],1,null);
                    count++;
                    lock.notify(); // Notify the waiting thread (even thread)
                }else if (count % 2 == 1 && THREAD1_TONES[ocount].equalsIgnoreCase(finishTone))
                {
                    System.out.println(Thread.currentThread().getName() + ": " + count +" "+ THREAD1_TONES[ocount]);
                   play(THREAD1_TONES[ocount],1,null);
                   count=8;
                   lock.notify();
                } 
                else {
                    try {
                        lock.wait(); // Wait for the even thread to notify
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
            }
            
        }
    }

    public void printEven() {
        synchronized (lock) {
            while (count <= 8) {
                if (count % 2 == 0 && !(THREAD2_TONES[ecount].equalsIgnoreCase(finishTone))) {
                    System.out.println(Thread.currentThread().getName() + ": " + count+" "+ THREAD2_TONES[ecount]);
                    play(THREAD2_TONES[ecount++],1,null);
                    count++;
                    lock.notify(); // Notify the waiting thread (odd thread)
                } 
                else if (count % 2 == 0 && THREAD2_TONES[ecount].equalsIgnoreCase(finishTone))
                {
                    System.out.println(Thread.currentThread().getName() + ": " + count +" "+ THREAD2_TONES[ecount]);
                    
                   play(THREAD2_TONES[ecount],0,null);
                    count=9;
                    lock.notify();
                }
                else {
                    try {
                        lock.wait(); // Wait for the odd thread to notify
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
            }
            play(THREAD2_TONES[ecount],0,null);
        }
    }
    
    public synchronized void play(String filePath,double isleeptime, String sPlaytime) {

       

        try {

            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(filePath).getAbsoluteFile());
            Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            //System.out.println("T1-Started Playing: "+filePath+" "+isleeptime+" "+sPlaytime);
            //if(sPlaytime.equalsIgnoreCase("I"))
           // {
                clip.start();
                Thread.sleep((long) (1000*isleeptime));
               // wait();
                
                
            //}
           // System.out.println("T1-stopped Playing: "+filePath+" "+isleeptime+" "+sPlaytime);
        } catch (Exception e) {
            System.out.println("Error with playing sound.");
            e.printStackTrace();
        }

    }
}


    

