package com.example.hungryjava;

import android.util.Log;

import java.io.IOException;
import java.net.Socket;

public class Communication {

    private static final String SERVER_IP = "10.0.2.2";
    private static final int SERVER_PORT = 7777;
    private static final String TAG = "Communication";

    public void connect() {
        new Thread(() -> {
            Socket socket = null;
            try {
                socket = new Socket(SERVER_IP, SERVER_PORT);
                Log.d(TAG, "Connected to server: " + socket.getRemoteSocketAddress());
            } catch (IOException e) {
                Log.e(TAG, "Failed to connect to server: " + e.getMessage());
                e.printStackTrace(); // Still good to print the stack trace for debugging
            } finally {
                if (socket != null) {
                    try {
                        socket.close();
                    } catch (IOException e) {
                        Log.e(TAG, "Error closing socket: " + e.getMessage());
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

}