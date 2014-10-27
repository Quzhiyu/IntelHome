package TypeP;

public class Type {
	//�ź����ͣ���¼,ע�ᣬˢ�£����ƣ�
	public final static int T_LOAD = 0x10;
	public final static int T_REGISTER = 0x11;
	public final static int T_REFRESH = 0x12;
	public final static int T_CONTROL = 0x13;
	
	//���������ظ��ͻ��˱���
	public final static int REC_OK = 0x01;//����ȷ��
	public final static int LOGIN_ERROR = 0x00;//��¼ʧ��
	
	/*
	 * ״̬��Ϣ
	 */
	public final static int S_ONLINE = 0x01;
	public final static int S_OUTLINE = 0x00;
	/*
	 * ��½������
	 */
	public final static int L_SUCCESS = (byte)0x01;
	public final static int LOAD_ACK_OK	= L_SUCCESS;
	
	public final static int L_ERROR = (byte)0x00;
	public final static int LOAD_ACK_ERROR = L_ERROR;
	/*
	 * ˢ�·�����
	 */
	public final static int R_SUCCESS = (byte)0x01;
	public final static int REFRESH_ACK_OK = R_SUCCESS;
	public final static int R_ERROR = (byte)0x00;
	public final static int REFRESH_ACK_ERROR = R_ERROR;
	//������
	public final static int C_OPEN = (byte)0x01;
	public final static int C_CLOSE = (byte)0x00;
}
