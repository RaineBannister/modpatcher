package main;

import java.io.*;
import java.util.Stack;

public class BinaryStream extends RandomAccessFile{
    private Stack<Long> gotos;
    private File stream;
    private String method;

    public BinaryStream(File stream, String method) throws FileNotFoundException {
        super(stream, method);

        this.stream = stream;
        this.method = method;

        gotos = new Stack<>();
    }

    public BinaryStream(BinaryStream copy) throws IOException {
        super(copy.getStream(), copy.getMethod());

        this.stream = copy.getStream();
        this.method = copy.getMethod();
        this.gotos = new Stack<>();
        this.gotos.addAll(copy.getGotos());

        go(copy.getFilePointer());
    }

    public int readIntLittleEndian() throws IOException {
        return this.readUnsignedByte() +
            (this.readUnsignedByte() << 8) +
            (this.readUnsignedByte() << 16) +
            (this.readUnsignedByte() << 24);
    }

    public void writeIntLittleEndian(long num) throws IOException {
        this.writeByte((int) num & 0xFF);
        this.writeByte((int) (num & 0xFF00) >> 0b1000);
        this.writeByte((int) (num & 0xFF0000) >> 0b10000);
        this.writeByte((int) (num & 0xFF000000) >> 0b100000);
    }

    public long readLongLittleEndian() throws IOException {
        long second = this.readIntLittleEndian();
        long first = this.readIntLittleEndian();
        return first << 32 + second;
    }

    public String readString() throws IOException {
        this.follow();

        StringBuilder temp = new StringBuilder();
        int look = this.readUnsignedByte();
        while(look != 0) {
            temp.append(Character.toString((char) look));
            look = this.readUnsignedByte();
        }

        this.back();
        return temp.toString();
    }

    public void writeString(String string) throws IOException {
        // write pointer to end of file
        this.writeIntLittleEndian((int) this.length());

        this.go(this.length());

        for(int i = 0; i < string.length(); i ++) {
            this.writeByte(string.codePointAt(i));
        }
        //null terminator
        this.writeByte(0);

        //keep it word aligned
        while(this.length() % 4 != 0) {
            this.writeByte(0);
        }

        this.back();
    }

    public void go(long pointer) throws IOException {
        gotos.push(this.getFilePointer());
        this.seek(pointer);
    }

    public void follow() throws IOException {
        int seek = this.readIntLittleEndian();
        gotos.push(this.getFilePointer());
        this.seek(seek);
    }

    public void back() throws IOException {
        this.seek(gotos.pop());
    }

    public Stack<Long> getGotos() {
        return gotos;
    }

    public void setGotos(Stack<Long> gotos) {
        this.gotos = gotos;
    }

    public File getStream() {
        return stream;
    }

    public void setStream(File stream) {
        this.stream = stream;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }
}
