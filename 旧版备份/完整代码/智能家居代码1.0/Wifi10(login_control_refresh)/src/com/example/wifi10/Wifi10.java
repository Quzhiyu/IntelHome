//����ʵ��˫��ͨѶ
package com.example.wifi10;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket; 

import Func.function;
import TypeP.Type;
import android.os.Bundle;
import android.os.Message;
import android.os.Handler;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Switch;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wifi10.Login_page1;
import com.example.wifi10.Login_page1.MyHandler;
import TypeP.Type;
import Func.socket_func;
import Func.function;


public class Wifi10 extends Activity {
	
	MyHandler myHandler = null;
	private Switch swt1;
	private Switch swt2;
	private TextView name_text;
	private TextView online_text;
	private TextView state_text;
	private TextView con_device_text;
	private TextView con_state_text;
	private Button send_bn;
	public static final int SWITCH_OFF = 0;
	public static final int SWITCH_ON = 1;
	
	private String mOnText = "��";
	private String mOffText= "�ر�";
	private int mswt1 = SWITCH_OFF;
	private int mswt2 = SWITCH_OFF;
	public static byte[] id_pwd = new byte[2];
			
	
	@Override
	protected void onCreate(Bundle savedInstanceState){		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_wifi10);
		
		swt1 = (Switch)findViewById(R.id.switch1);
		swt2 = (Switch)findViewById(R.id.switch2);		
		name_text = (TextView)findViewById(R.id.name_text);
		online_text = (TextView)findViewById(R.id.online_text);
		state_text = (TextView)findViewById(R.id.rec_state_text);
		con_device_text = (TextView)findViewById(R.id.con_device_text);
		con_state_text = (TextView)findViewById(R.id.con_state_text);
		send_bn = (Button)findViewById(R.id.send_bn);
		send_bn.setOnClickListener(new send_bnListener());
		myHandler = new MyHandler();
		
		Intent intent = getIntent();
		id_pwd = intent.getByteArrayExtra("id_pwd");
    	
		swt1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
			@Override
			public void onCheckedChanged(CompoundButton buttonView,boolean isChecked){
				if(swt1.isChecked()){
					
					System.out.println("change1!!open!!!");
					new Thread(){
						@Override
						public void run(){
							control_func(0x01,Type.C_OPEN);
						}
					}.start();
				}
				if(!swt1.isChecked()){
					
					System.out.println("change1!!close!!!");
					new Thread(){
						@Override
						public void run(){
							control_func(0x01,0x00);
						}
					}.start();
				}
			}
		});
		swt2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
			@Override
			public void onCheckedChanged(CompoundButton buttonView,boolean isChecked){
				if(swt2.isChecked()){
					
					System.out.println("change2!!!!!");
					new Thread(){
						@Override
						public void run(){
							control_func(0x02,0x01);
						}
					}.start();
				}
				if(!swt2.isChecked()){
					
					System.out.println("change2!!close!!!");
					new Thread(){
						@Override
						public void run(){
							control_func(0x02,0x00);
						}
					}.start();
				}
			}
		});
		new Thread(){
			@Override
			public void run(){
				refresh_func();
			}
		}.start();
		
	}
	class MyHandler extends Handler{
		@Override
		public void handleMessage(Message msg){
			super.handleMessage(msg);
			state_text.setText(msg.obj.toString());
			System.out.println(msg.obj.toString());
		}
	}
	
    public void msg_sender(int num,String str){//�����������,num�������ִ���״̬������ʱ��ʾԭ��str��ʾ���ֻ���
    	Message msg = new Message();
    	msg.what = num;
    	msg.obj= str;
    	myHandler.sendMessage(msg);
    }
    
	class send_bnListener implements OnClickListener{
		@Override
		public void onClick(View v){
			if(con_device_text.getText().length()!=0){
				if(con_state_text.getText().length()!=0){
					new Thread(){
						@Override
						public void run(){
							control_func((int)function.stringTochar(con_device_text.getText().toString()),
											 (int)function.stringTochar(con_state_text.getText().toString()));
						}
					}.start();
				}
			}
		}
	}
	
	void refresh_func(){
		socket_output(get_refresh_package());//���ˢ��ָ���������
		System.out.println("ˢ��ָ��ͳɹ�");
		byte[] line_get = new byte[100]; 
		line_get = socket_input();
			
		if(line_get[2]==Type.R_SUCCESS){
			String socket_str = "ˢ�³ɹ�";
			this.msg_sender(Type.R_SUCCESS,socket_str);
			judge_refresh_message(line_get);
			System.out.println("shuaxin");
		}
		else{
			String socket_str = "ˢ��ʧ��";
			online_text.setText("������");
			this.msg_sender(Type.R_ERROR,socket_str);
		}
		//�жϽ��յ������ݣ�Ȼ��ʹ��public void setChecked(boolean checked)�޸�״̬
	}
	
    public byte[] get_refresh_package(){//���Ҫ���͵�ˢ��ָ��
    	//Intent intent = getIntent();
    	//byte[] id_pwd = intent.getByteArrayExtra("id_pwd");
		byte D_length = 0x04;
		byte[] refresh_data = new byte[4]; //���ݴ��
		
				refresh_data[0]=D_length;
				refresh_data[1]=Type.T_REFRESH;
				refresh_data[2]=id_pwd[0];
				refresh_data[3]=id_pwd[1];

		System.out.println("login_data is ok!!!");
		return refresh_data;
	}
	
	void control_func(int dev_num,int state_num){
		socket_output(get_control_package(dev_num,state_num));
		System.out.println("����ָ��ͳɹ�");
		byte[] line_get = new byte[100];
		line_get = socket_input();
		
		if(line_get[2]==Type.L_SUCCESS){
			String socket_str = "���Ƴɹ�";
			this.msg_sender(Type.L_ERROR,socket_str);
		}
		else{
			String socket_str = "����ʧ��";
			this.msg_sender(Type.L_ERROR,socket_str);
		}
	}
	
	public byte[] get_control_package(int dev_num,int state_num){//���Ҫ���͵Ŀ���ָ��
    	//Intent intent = getIntent();
    	//byte[] id_pwd = intent.getByteArrayExtra("id_pwd");
		byte D_length = 0x07;
		byte[] control_data = new byte[10]; //���ݴ��
		
				control_data[0] = D_length;
				control_data[1] = Type.T_CONTROL;
				control_data[2] = id_pwd[0];
				control_data[3] = id_pwd[1];
				control_data[4] = 0x01;
				control_data[5] = (byte)dev_num;
				control_data[6] = (byte)state_num;
				//control_data[5]= function.stringTochar(con_device_text.getText().toString());
				//control_data[6]= function.stringTochar(con_state_text.getText().toString());
				
				System.out.println("login_data is ok!!!");
		return control_data;
	}
       
	void socket_output(byte[] output){
		try{
			socket_func.login_os.write(output);
		}
		catch(IOException e){
			e.printStackTrace();
			String socket_error_str = "ˢ��ʧ�ܣ������µ�¼";
			this.msg_sender(Type.R_ERROR,socket_error_str);
		}
	}
	
	byte[] socket_input(){
		byte[] state_data = new byte[100];
		state_data[0] = 0x00;//���������ȳ�ʼ��
		try{
		socket_func.login_is.read(state_data);
		}
		catch(IOException e){
			e.printStackTrace();
			String socket_error_str = "ˢ��ʧ�ܣ������µ�½";
			this.msg_sender(Type.R_ERROR,socket_error_str);
		}
		return state_data;
	}
	void judge_refresh_message(byte[] refresh_msg){
		System.out.println(refresh_msg.toString());
		int i = refresh_msg[5];
		
		//name_text.setText();
		if(refresh_msg[4]==0x01){
			online_text.setText("����");
		}
		else{
			online_text.setText("������");
		}
		//refresh_msg[7]?
		if(refresh_msg[7]==Type.C_OPEN){
			swt1.setChecked(true);
		}
		else{
			swt1.setChecked(false);
		}
			
		if(refresh_msg[7]==Type.C_OPEN){
			swt2.setChecked(true);
		}
		else{
			swt2.setChecked(false);
		}
			
		/*if(refresh_msg[9]==0x01){
			swt2.setChecked(true);
		}*/
		
/*		if(i-1>0){
			for(int j=0;j<i-1;j++){
				TableRow tablerow = new TableRow(this);
				final TextView obj = new TextView(this);
				if(refresh_msg[6+i]){
					
				}
			}
		}*/

		
	}
}
	