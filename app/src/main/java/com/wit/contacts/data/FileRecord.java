package com.wit.contacts.data;

import android.util.Log;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;

/**
 * Created by wnw on 2016/10/19.
 */

public class FileRecord {
    BufferedWriter writer;
    OutputStreamWriter outputStreamWriter;
    FileOutputStream fout;
    public FileRecord(String fileName) throws FileNotFoundException {
        fout = new FileOutputStream(fileName,true);
        outputStreamWriter = new OutputStreamWriter(fout, Charset.forName("gbk"));
        writer = new BufferedWriter(outputStreamWriter);
    }
    public void Write( String str) throws IOException {
        writer.append(str+"\r\n");
        writer.flush();
    }
    public void Close() throws IOException{
        writer.close();
    }
}

