package test;

import java.awt.Container; 
import java.awt.event.MouseEvent; 
import java.awt.event.MouseListener; 
 
import javax.swing.JFrame; 
 
/**
 * ��������¼�
 * ���Կ�������˫�����ʱ����һ�εĵ���ᴥ��һ�ε����¼�
 * @author HAN
 *
 */ 
public class MouseEvent_1 extends JFrame { 
 
    /**
     * 
     */ 
    private static final long serialVersionUID = 7554087008285696671L; 
 
    public MouseEvent_1() { 
        // TODO Auto-generated constructor stub  
        Container container = getContentPane(); 
        container.addMouseListener(new MouseListener() { 
 
            @Override 
            public void mouseClicked(MouseEvent e) { 
                // TODO Auto-generated method stub  
                System.out.print("��������갴����"); 
                int i = e.getButton(); 
                if (i == MouseEvent.BUTTON1) 
                    System.out.print("����������������"); 
                if (i == MouseEvent.BUTTON2) 
                    System.out.print("������������м���"); 
                if (i == MouseEvent.BUTTON3) 
                    System.out.print("������������Ҽ���"); 
                int clickCount = e.getClickCount(); 
                System.out.println("��������Ϊ" + clickCount + "��"); 
            } 
 
            @Override 
            public void mousePressed(MouseEvent e) { 
                // TODO Auto-generated method stub  
                System.out.print("��갴�������£�"); 
                int i = e.getButton(); 
                if (i == MouseEvent.BUTTON1) 
                    System.out.println("���µ���������"); 
                if (i == MouseEvent.BUTTON2) 
                    System.out.println("���µ�������м�"); 
                if (i == MouseEvent.BUTTON3) 
                    System.out.println("���µ�������Ҽ�"); 
            } 
 
            @Override 
            public void mouseReleased(MouseEvent e) { 
                // TODO Auto-generated method stub  
                System.out.print("��갴�����ͷţ�"); 
                int i = e.getButton(); 
                if (i == MouseEvent.BUTTON1) 
                    System.out.println("�ͷŵ���������"); 
                if (i == MouseEvent.BUTTON2) 
                    System.out.println("�ͷŵ�������м�"); 
                if (i == MouseEvent.BUTTON3) 
                    System.out.println("�ͷŵ�������Ҽ�"); 
            } 
 
            @Override 
            public void mouseEntered(MouseEvent e) { 
                // TODO Auto-generated method stub  
                System.out.println("����������"); 
            } 
 
            @Override 
            public void mouseExited(MouseEvent e) { 
                // TODO Auto-generated method stub  
                System.out.println("����Ƴ����"); 
            } 
             
            
        }); 
    } 
 
    /**
     * @param args
     */ 
    public static void main(String[] args) { 
        // TODO Auto-generated method stub  
        MouseEvent_1 frame = new MouseEvent_1(); 
        frame.setTitle("MouseEvent Test"); 
        frame.setVisible(true); 
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
        frame.setBounds(0, 0, 300, 100); 
    } 
 
}