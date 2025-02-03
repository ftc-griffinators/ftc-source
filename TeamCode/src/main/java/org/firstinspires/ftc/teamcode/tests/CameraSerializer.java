package org.firstinspires.ftc.teamcode.tests;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class CameraSerializer {
    private File saveFile;

    public CameraSerializer() {
        setFile("/sdcard/output.csv");
    }

    public void setFile(String path){
        saveFile = new File(path);
        if (saveFile.exists()){
            throw new IllegalStateException("File already exists");
        }
    }

    public void serialize(List<List<Double>> data){
        if (saveFile == null){
            throw new IllegalStateException("No file set to save to");
        }

        try(BufferedWriter writer = new BufferedWriter(new FileWriter(saveFile, true))){
            for (int i = 0; i < data.size(); i++) {
                List<Double> frame = data.get(i);
                for (int j = 0; j < frame.size(); j++) {
                    Double value = frame.get(j);
                    if (j == frame.size() - 1) {
                        writer.write(value.toString());
                    } else {
                        writer.write(value + ",");
                    }
                }
                if (i < data.size() - 1) {
                    writer.write(":");
                }
            }
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void close(){
        try(BufferedWriter writer = new BufferedWriter(new FileWriter(saveFile, true))) {
            writer.write("END");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        saveFile = null;
    }

    public static void main(String[] args) {
        CameraSerializer serializer = new CameraSerializer();
        serializer.setFile("output.csv");
        serializer.serialize(List.of(
                List.of(1.0, 2.0, 3.0),
                List.of(4.0, 5.0, 6.0),
                List.of(7.0, 8.0, 9.0)
        ));
        serializer.serialize(List.of(
                List.of(1.0, 2.0, 3.0),
                List.of(4.0, 5.0, 6.0),
                List.of(7.0, 8.0, 9.0)
        ));
        serializer.serialize(List.of(
                List.of(1.0, 2.0, 3.0),
                List.of(4.0, 5.0, 6.0),
                List.of(7.0, 8.0, 9.0)
        ));
        serializer.close();
    }
}