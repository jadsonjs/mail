package br.com.jadson.mailframe.models;

public class MailCounter {

    long counter = 1;

    public void incrementCounter(){
        counter++;
    }

    public long getCounter(){
        return counter;
    }

}
