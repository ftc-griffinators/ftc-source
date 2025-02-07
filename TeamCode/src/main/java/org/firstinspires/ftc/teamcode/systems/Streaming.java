package org.firstinspires.ftc.teamcode.systems;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

/*
 * streams the current view camera
 * the socket uses UDP protocol
 * this is supposed to run CONCURRENTLY
 * so it does not block the main Java thread
 *
 * go to http://limelight.local:5800
 * or http://10.217.82.11:5800
 * to view the video
 */
public class Streaming
{
    /* this is where the stream is supposed to go */
    private static final int BUFFER_SIZE = 65536; // 64KB
    private static DatagramSocket socket;
    private static boolean isStreaming = false;

    public static void startStreaming() throws SocketException, UnknownHostException
    {
        socket = new DatagramSocket();
        InetAddress limelightAddress = InetAddress.getByName("10.217.82.11");

        if (isStreaming)
            return;
        isStreaming = true;
        Thread stream = new Thread(() -> {
            byte[] buf = new byte[BUFFER_SIZE];
            DatagramPacket packet = new DatagramPacket(buf, buf.length, limelightAddress, 5800);

            while (isStreaming)
            {
                try
                {
                    socket.receive(packet);
                    socket.send(packet);
                }
                catch (IOException e)
                {
                    /* TODO: better network logging */
                    e.printStackTrace();
                    break;
                }
            }
        });

        stream.start();
    }

    public static void stopStream()
    {
        isStreaming = false;
        if (socket != null && !socket.isClosed())
            socket.close();
    }
}