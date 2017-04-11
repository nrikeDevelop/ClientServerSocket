package susy.client;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;

public class MainActivity extends AppCompatActivity {

    Socket client;
    PrintWriter printWriter;

    EditText ip;
    EditText port;
    EditText message;
    EditText name;
    EditText type;
    EditText quantity;

    Button buttonSendString;
    Button buttonSendObject;

    public void setUI(){
        ip = (EditText) findViewById(R.id.ip_edit_text);
        port = (EditText) findViewById(R.id.port_edit_text);
        message = (EditText) findViewById(R.id.message_edit_text);
        name = (EditText) findViewById(R.id.object_name_text);
        type = (EditText) findViewById(R.id.object_type_text);
        quantity = (EditText) findViewById(R.id.object_quantity_text);

        buttonSendString = (Button) findViewById(R.id.bt_send_string);
        buttonSendObject = (Button) findViewById(R.id.bt_send_object);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setUI();

        buttonSendString.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(checkEditTextString()){
                    sendStringData(ip.getText().toString(),
                            Integer.parseInt(port.getText().toString()),
                            message.getText().toString());
                }else{
                    Toast.makeText(MainActivity.this, "Introduce all data configuration-string", Toast.LENGTH_SHORT).show();
                }
            }
        });

        buttonSendObject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkEditTextObject()){

                    Object obj = new Object(
                            name.getText().toString(),
                            type.getText().toString(),
                            Integer.parseInt(quantity.getText().toString())
                    );
                    sendObjectData(ip.getText().toString(),
                            Integer.parseInt(port.getText().toString()),
                            obj
                    );

                }else{
                    Toast.makeText(MainActivity.this, "Introduce all data configuration-object", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    public void sendStringData(final String ip , final int port, final String message){
        new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    client = new Socket(ip,port);

                    printWriter = new PrintWriter(client.getOutputStream());
                    printWriter.write(1);
                    printWriter.flush();

                    printWriter.write(message);
                    printWriter.flush();
                    printWriter.close();
                    client.close();

                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }).start();
    }

    private boolean checkEditTextString(){

        if (message.getText().toString().equals("")||
                ip.getText().toString().equals("")||
                port.getText().toString().equals("")){
            return false;
        }else{
            return true;
        }
    }

    public void sendObjectData(final String ip, final int port, final Object object){
        new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    client = new Socket(ip, port);

                    printWriter = new PrintWriter(client.getOutputStream());
                    printWriter.write(2);
                    printWriter.flush();

                   // ObjectInputStream objectInputStream = new ObjectInputStream(client.getInputStream());
                    ObjectOutputStream objectOutputStream = new ObjectOutputStream(client.getOutputStream());
                    objectOutputStream.writeObject(object);
                    objectOutputStream.flush();
                    objectOutputStream.close();
                    client.close();

                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }).start();
    }

    private boolean checkEditTextObject(){
        if (name.getText().toString().equals("")||
                type.getText().toString().equals("")||
                quantity.getText().toString().equals("")||
                ip.getText().toString().equals("")||
                port.getText().toString().equals("")){
            return false;
        }else{
            return true;
        }
    }


}
