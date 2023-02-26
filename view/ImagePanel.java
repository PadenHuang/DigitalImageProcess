package view;

import javax.swing.*;
import java.awt.*;
import java.awt.image.*;

/**
 * ͼƬ�ļ�������
 */
public class ImagePanel extends JComponent {
    protected float scale = 1;          // ���ű���
    protected Image image = null;       // ��Ҫ��ʾ��ͼ��
    
    public ImagePanel(Image image) {
        setImage(image);
    }
    
    public void setImage(Image image) {
        this.image = image;
        setSize(getPreferredSize());
    }
    
    /** �������ű���*/
    public void setScale(float scale) {
        this.scale = scale;
        setSize(getPreferredSize());
    }
    
    /** ��ȡ��Ҫ��ʾͼ�����ź�ĳߴ�*/
    protected Dimension getImageSize() {
        if(image != null) {
            return new Dimension(Math.round(image.getWidth(null)*scale), Math.round(image.getHeight(null)*scale));
        }
        else return new Dimension(0, 0);
    }
    
    /** ��ȡ�ؼ���ѡ�ߴ磬����û�б߿�������getImageSize()*/
    public Dimension getPreferredSize() {
        return getImageSize();    
    }
    
    
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        if(image == null) return ;
        Dimension destDim = getImageSize();
        g.drawImage(image, 0, 0, destDim.width, destDim.height,
                    0, 0, image.getWidth(null), image.getHeight(null), null);
    }
}