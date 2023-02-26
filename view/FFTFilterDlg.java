/*
 * FFTFilterDlg.java
 *
 * Created on 2005��2��24��, ����1:51
 */

/**
 *
 * @author  Bluewater
 */
package view;
 
import javax.swing.*;

import algorithm.FFTTransform;

import util.ProcessUtil;

import java.awt.*;
import java.awt.image.*;
import java.awt.event.*;

/*
*����Ҷ�任�˲�����-����Ҷ��ͨ�˲�
*  */
public class FFTFilterDlg extends javax.swing.JDialog {

    /** Creates new form FFTFilterDlg */
    public FFTFilterDlg(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
    }

    
    private void initComponents() {
    	imagePanel = new FFTImagePane(fftImage);
        scrollPane = new javax.swing.JScrollPane(imagePanel);
        jPanel1 = new javax.swing.JPanel();
        innerSlider = new javax.swing.JSlider();
        outterSlider = new javax.swing.JSlider();
        cancelBtn = new javax.swing.JButton();
        okBtn = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        
        getContentPane().add(scrollPane, java.awt.BorderLayout.CENTER);
        
        jPanel1.setLayout(new java.awt.GridBagLayout());
        java.awt.GridBagConstraints gridBagConstraints1;
        
        gridBagConstraints1 = new java.awt.GridBagConstraints();
        gridBagConstraints1.gridx = 1;
        gridBagConstraints1.gridy = 0;
        gridBagConstraints1.insets = new java.awt.Insets(15, 3, 3, 10);
        jPanel1.add(innerSlider, gridBagConstraints1);
        innerSlider.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                innerChanged(evt);
            }
        });
        
        gridBagConstraints1 = new java.awt.GridBagConstraints();
        gridBagConstraints1.gridx = 1;
        gridBagConstraints1.gridy = 1;
        gridBagConstraints1.insets = new java.awt.Insets(3, 3, 15, 10);
        jPanel1.add(outterSlider, gridBagConstraints1);
        outterSlider.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                outterChanged(evt);
            }
        });
        
        cancelBtn.setText("ȡ��(C)");
        cancelBtn.setMnemonic('C');
        gridBagConstraints1 = new java.awt.GridBagConstraints();
        gridBagConstraints1.gridx = 2;
        gridBagConstraints1.gridy = 1;
        gridBagConstraints1.insets = new java.awt.Insets(0, 0, 10, 15);
        jPanel1.add(cancelBtn, gridBagConstraints1);
        cancelBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cancelPerformed(e);
			}
		});
        
        okBtn.setText("ȷ��(O)");
        okBtn.setMnemonic('O');
        gridBagConstraints1 = new java.awt.GridBagConstraints();
        gridBagConstraints1.gridx = 2;
        gridBagConstraints1.gridy = 0;
        gridBagConstraints1.insets = new java.awt.Insets(12, 0, 0, 15);
        jPanel1.add(okBtn, gridBagConstraints1);
        okBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				okPerformed(e);
			}
		});
        
        jLabel2.setText("�ڰ뾶a");
        gridBagConstraints1 = new java.awt.GridBagConstraints();
        gridBagConstraints1.gridx = 0;
        gridBagConstraints1.gridy = 0;
        gridBagConstraints1.insets = new java.awt.Insets(15, 15, 0, 3);
        jPanel1.add(jLabel2, gridBagConstraints1);
        
        jLabel3.setText("��뾶b");
        gridBagConstraints1 = new java.awt.GridBagConstraints();
        gridBagConstraints1.gridx = 0;
        gridBagConstraints1.gridy = 1;
        gridBagConstraints1.insets = new java.awt.Insets(3, 15, 15, 3);
        jPanel1.add(jLabel3, gridBagConstraints1);
        
        getContentPane().add(jPanel1, java.awt.BorderLayout.SOUTH);
        
        setSize(new Dimension(640, 480));
        setTitle("����Ҷ��ͨ�˲�");
    }

    /** Closes the dialog */
    private void closeDialog(java.awt.event.WindowEvent evt) {
        setVisible(false);
        dispose();
    }

    private void okPerformed(java.awt.event.ActionEvent evt) {
        modResult = JOptionPane.OK_OPTION;
        destImage = filterImage();
		dispose();
    }

    private void cancelPerformed(java.awt.event.ActionEvent evt) {
        modResult = JOptionPane.CANCEL_OPTION;
		dispose();
    }
    
    private void innerChanged(javax.swing.event.ChangeEvent evt) {
    	int a = innerSlider.getValue();
    	imagePanel.setA(a);
    }
    
    private void outterChanged(javax.swing.event.ChangeEvent evt) {
    	int b = outterSlider.getValue();
    	imagePanel.setB(b);		
    }
    
    public int showModal() {
    	show();
    	return modResult;
    }
    
    public int modResult() {
    	return modResult;
    }
    
    public BufferedImage getDestImage() {
    	return destImage;
    }
    
    public void setImage(BufferedImage image) {
    	srcImage = image;
    	fftImage = createFFTImage();
    	
    	int a = 0;
    	int b = (int)Math.round(Math.sqrt(xsize*xsize/4+ysize*ysize/4))+2;
    	innerSlider.setMaximum(b);
    	innerSlider.setValue(a);
    	outterSlider.setMaximum(b);
    	outterSlider.setValue(b);
    	imagePanel.setImage(fftImage);
    	imagePanel.setA(a);
    	imagePanel.setB(b);
    }
    
    private BufferedImage createFFTImage() {
		int i, j;
		int width = srcImage.getWidth();
		int height = srcImage.getHeight();
		xsize = 1;
		//����Ҷ�������2�ı����������ʱ���ȡ����ͼƬ
		for(i=width/2; i>0; i/=2) xsize *= 2;
		xsize = xsize > 1 ? xsize : 0;
		ysize = 1;
		for(i=height/2; i>0; i/=2) ysize *= 2;
		ysize = ysize > 1 ? ysize : 0;
		BufferedImage fftImage = new BufferedImage(xsize, ysize, BufferedImage.TYPE_INT_RGB);
		
		//�õ�����ĵ�
		int srcRGBs[] = srcImage.getRGB(0, 0, xsize, ysize, null, 0, xsize);
		//ʵ������
		arl = new float[ysize][];
	    //�鲿����
		aim = new float[ysize][];
		float amp[][] = new float[ysize][];
		for(i=0; i<ysize; i++) {
			arl[i] = new float[xsize];
			aim[i] = new float[xsize];
			//����ֵ����
			amp[i] = new float[xsize];
		}
		
		int rgb[] = new int[3];
		float yhs[] = new float[3];
		for(i=0; i<ysize; i++) {
			for(j=0; j<xsize; j++){
				ProcessUtil.convertRGBToYHS(srcRGBs[i*xsize+j], yhs);
				//�ҶȻ�
				arl[i][j] = yhs[0];
				aim[i][j] = 0;
			}
		}
		
		FFTTransform.fft2d(arl, aim, FFTTransform.DFT, xsize, ysize);
		
		//�������
		float min = Float.POSITIVE_INFINITY;
		//�������
		float max = Float.NEGATIVE_INFINITY;
		for(i=0; i<ysize; i++) {
			for(j=0; j<xsize; j++) {
				//ȡ��������
				float norm = arl[i][j]*arl[i][j]+aim[i][j]*aim[i][j];
				if(norm != 0.0) norm = (float)Math.log(norm)/2;
				else norm = 0.0f;
				amp[i][j] = norm;
				if(amp[i][j] > max) max = amp[i][j];
				if(amp[i][j] < min) min = amp[i][j];
			}
		}
		
		for(i=0; i<ysize; i++) {
			for(j=0; j<xsize; j++) {
				//����ɻҶ�ֵ
				rgb[0] = (int)((amp[i][j]-min)*255/(max-min));
				rgb[1] = rgb[2] = rgb[0];
				fftImage.setRGB(j, i, ProcessUtil.encodeColor(rgb));
			}
		}
		
		return fftImage;
	}
	
	private BufferedImage filterImage() {
		float a = innerSlider.getValue();
		float b = outterSlider.getValue();
		int width = srcImage.getWidth();
		int height = srcImage.getHeight();
		int srcRGBs[] = srcImage.getRGB(0, 0, width, height, null, 0, width);
		BufferedImage destImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		destImage.setRGB(0, 0, width, height, srcRGBs, 0, width);
		
		for(int i=0; i<ysize; i++) {
			for(int j=0; j<xsize; j++) {
				float r = (float)Math.sqrt((j-xsize/2)*(j-xsize/2)+(i-ysize/2)*(i-ysize/2));
				if(r < a || r > b) {
					arl[i][j] = 0;
					aim[i][j] = 0;
				}
			}
		}
		
		FFTTransform.fft2d(arl, aim, FFTTransform.IDFT, xsize, ysize);
		
		float max = 0, min = 500;
		for(int i=0; i<ysize; i++) {
			for(int j=0; j<xsize; j++) {
				if(max < arl[i][j]) max = arl[i][j];
				if(min > arl[i][j]) min = arl[i][j];
			}
		}
		
		float yhs[] = new float[3];
		for(int i=0; i<ysize; i++) {
			for(int j=0; j<xsize; j++) {
				ProcessUtil.convertRGBToYHS(srcRGBs[i*width+j], yhs);
				yhs[0] = (arl[i][j]-min)*255/(max-min);
				destImage.setRGB(j, i, ProcessUtil.convertYHSToRGB(yhs));	
			}
		}
		
		return destImage;
	}


    // Variables declaration - do not modify
    private javax.swing.JScrollPane scrollPane;
    FFTImagePane imagePanel;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JSlider innerSlider;
    private javax.swing.JSlider outterSlider;
    private javax.swing.JButton cancelBtn;
    private javax.swing.JButton okBtn;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    // End of variables declaration
    
    private int modResult = JOptionPane.CANCEL_OPTION;
    private BufferedImage srcImage;
    private BufferedImage destImage;
    private BufferedImage fftImage;
    private int xsize, ysize;
    float arl[][], aim[][];

}

/**
 * ����Ҷ�任�˲�����-����Ҷ��ͨ�˲�-����������
 */
class FFTImagePane extends ImagePanel {
	int a, b;
	public FFTImagePane(Image image) {
        super(image);
    }
    
    public void setA(int a) {
    	this.a = a;
    	repaint();
    }
    
    public void setB(int b) {
    	this.b = b;
    	repaint();
    }
    
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        int x0 = image.getWidth(null)/2;
        int y0 = image.getHeight(null)/2;
        
        g.setClip(0, 0, 2*x0, 2*y0);
        if(a > 0) {
        	g.setColor(Color.red);
        	g.drawOval(x0-a, y0-a, 2*a, 2*a);
        }
        
        if(b > 0) {
        	g.setColor(Color.green);
        	g.drawOval(x0-b, y0-b, 2*b, 2*b);
        }
    }
}
