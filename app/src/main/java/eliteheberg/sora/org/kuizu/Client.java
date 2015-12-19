package eliteheberg.sora.org.kuizu;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Created by sora on 12/6/2015.
 */
public class Client {
    public static String SERVER = "5.135.146.96";
    public static int PORT = 2194;
    public static Socket socket;
    public static BufferedReader in;
    public static PrintWriter out;
    public static DBHelper dbHelper;

    public static String login;
    public static String pwd;
    public static int rating;

    public static String loginP2;
    public static int p2rating;
}
