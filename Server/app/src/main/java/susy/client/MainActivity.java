package susy.client;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLOutput;

public class MainActivity extends AppCompatActivity {

    private int PORT = 4444;

    TextView txt;
    TextView txtname;
    TextView txttype;
    TextView txtquantity;

    public void setUI(){
        txt = (TextView) findViewById(R.id.text);
        txtname = (TextView) findViewById(R.id.name_object);
        txttype = (TextView) findViewById(R.id.type_object);
        txtquantity = (TextView) findViewById(R.id.quantity_object);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setUI();
        try {
            Server();
        } catch (IOException e) {
            Toast.makeText(this, "error", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    public void Server() throws IOException {

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {

                    System.out.println("Server String UP...");

                    Object object;
                    ServerSocket serverSocket = new ServerSocket(PORT);

                    while (true) {

                        Socket client = serverSocket.accept();

                        BufferedReader type = new BufferedReader(new InputStreamReader(client.getInputStream()));

                        int mode = type.read();

                        switch (mode){
                            case 1:
                                //String
                                String string = type.readLine();
                                setText(txt,string);
                                client.close();
                                break;

                            case 2:
                                ObjectInputStream ois = new ObjectInputStream(client.getInputStream());
                                Object obj = (Object) ois.readObject();
                                System.out.println(">>"+obj.getName());
                                System.out.println(">>"+obj.getQuantity());
                                System.out.println(">>"+obj.getType());

                                setObject(obj,txtname,txttype,txtquantity);

                                ois.close();
                                client.close();
                                break;
                        }

                    }

                } catch (IOException e) {
                    System.out.println(e);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }

    private void setText(final TextView text,final String value){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                text.setText(value);
            }
        });
    }

    private void setObject(final Object object, final TextView name, final TextView type, final TextView quantity){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                name.setText(object.getName());
                type.setText(object.getType());
                quantity.setText(String.valueOf(object.getQuantity()));
            }
        });
    }
}


