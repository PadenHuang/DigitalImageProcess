package algorithm;


import javax.swing.*;

import util.ProcessUtil;
import util.ProcessMath;

import java.awt.*;
import java.awt.image.*;

public class GeoTransform {
	/*
	 * �������ƣ�ͼƬƽ��
	 * �㷨��������ͼƬƽ�ƺ�Ŀհ׵ط������ɫ���൱������ͼƬ,���ಿ��Ϊ��ɫ��
	 */
	public static BufferedImage translation(BufferedImage srcImage, int tx, int ty) {
		int width = srcImage.getWidth();
		int height = srcImage.getHeight();
		int destwidth = width+tx;
		int destheight = height+ty;
		int srcRGBs[] = srcImage.getRGB(0, 0, width, height, null, 0, width);
		BufferedImage destImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		for(int j=0; j<height; j++) {
			for(int i=0; i<width; i++){
					int rgb[] = new int[3];
					rgb[0]=255;
					rgb[1]=255;
					rgb[2]=255;
					destImage.setRGB(i, j, ProcessUtil.encodeColor(rgb));
			}
		}
		for(int j=ty;j<height;j++){
			for(int i=tx;i<width;i++){
				destImage.setRGB(i,j,srcRGBs[(j-ty)*width+i-tx]);
			}
		}
		return destImage;
	}
	/*
	 * �������ƣ�ˮƽ����
	 * �㷨�����������е�������飬Ȼ��ͨ��ƫ����offset�õ�ͼ����
	 */
	public static BufferedImage horMirror(BufferedImage srcImage) {
		int width = srcImage.getWidth();
		int height = srcImage.getHeight();
		int srcRGBs[] = srcImage.getRGB(0, 0, width, height, null, 0, width);
		BufferedImage destImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		for(int j=0; j<height; j++) {
			destImage.setRGB(0, height-j-1, width, 1, srcRGBs, j*width, width);
		}	
		return destImage;
	}
	/*
	 * �������ƣ���ֱ����
	 * �㷨�����������е�������飬��ÿһ�е����ضԻ�����
	 */
	public static BufferedImage verMirror(BufferedImage srcImage) {
		int width = srcImage.getWidth();
		int height = srcImage.getHeight();
		int srcRGBs[] = srcImage.getRGB(0, 0, width, height, null, 0, width);
		BufferedImage destImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		
		for(int i=0; i<width; i++) {
			for(int j=0; j<height; j++) {
				destImage.setRGB(i, j, srcRGBs[j*width+width-i-1]);
			}
		}	
		return destImage;
	}
	/*
	 * �������ƣ�ͼ������
	 * �㷨����������������㽫���ص���3����ɫ��ֵ�ļ��ϣ������������ϲ����ص㡣���㷨����
	 *           ���β�ֵ�ķ�������ֵ������ص��ÿһ����ɫ��
	 */
	public static BufferedImage scale(BufferedImage srcImage, float sx, float sy) {
		int srcWidth = srcImage.getWidth();
		int srcHeight = srcImage.getHeight();
		int srcRGBs[] = srcImage.getRGB(0, 0, srcWidth, srcHeight, null, 0, srcWidth);
		int destWidth = Math.round(srcWidth*sx);
		int destHeight = Math.round(srcHeight*sy);
		BufferedImage destImage = new BufferedImage(destWidth, destHeight, BufferedImage.TYPE_INT_RGB);
		
		int m, m1, n, n1;
		float x, y, p, q;
		int p00[] = new int[3];
		int p01[] = new int[3];
		int p10[] = new int[3];
		int p11[] = new int[3];
		int rgb[] = new int[3];
	
		for(int j=0; j<destHeight; j++) {
			y = j/sy;	m = (int)y;		q = y-m;
			m1 = m+1 >= srcHeight ? m : m+1;
			for(int i=0; i<destWidth; i++) {
				x = i/sx;	n = (int)x;		p = x-n;
				n1 = n+1 >= srcWidth ? n : n+1;
				
				ProcessUtil.decodeColor(srcRGBs[m*srcWidth+n], p00);
				ProcessUtil.decodeColor(srcRGBs[m*srcWidth+n1], p01);
				ProcessUtil.decodeColor(srcRGBs[m1*srcWidth+n], p10);
				ProcessUtil.decodeColor(srcRGBs[m1*srcWidth+n1], p11);
				
				for(int k=0; k<3; k++) {
					rgb[k] = Math.round(ProcessMath.biLinear(p00[k], p01[k], p10[k], p11[k], p, q));
				}
				destImage.setRGB(i, j, ProcessUtil.encodeColor(rgb));
			}
		}
	
		return destImage;
	}
	/*
	 * �������ƣ�ͼƬ��ת
	 * �㷨������(1)�����ߴ�ֽ�ȡƥ�䡢����ƥ��
	 *           (2)��ʹ��ԭ�����ͱ任�����ĺ���ʱ�򣬱���ע������ϵ�ı仯��������ƥ�������ϵת����ȡƥ�������ϵ������⣡
	 *           (3)�Ե���ж��β�ֵ��һ��һ��д�롣  
	 */        
	
    public static BufferedImage rotate(BufferedImage srcImage, float af, boolean isResize) {
			int i, j;
			int m, m1, n, n1;
			float x, y, p, q;
			int r, g, b;
			int p00[] = new int[3];
			int p01[] = new int[3];
			int p10[] = new int[3];
			int p11[] = new int[3];
			int tmp[] = new int[3];
			int rgb[] = new int[3];
			
			int srcWidth = srcImage.getWidth();
			int srcHeight = srcImage.getHeight();
			int srcRGBs[] = srcImage.getRGB(0, 0, srcWidth, srcHeight, null, 0, srcWidth);
		
			float sinAF = (float)Math.sin(af);
			float cosAF = (float)Math.cos(af);
			
			/** ����ת��ͼ��ĳߴ� **/
			float x0, y0;
			float ptX[] = new float[3], ptY[] = new float[3];
			float minX, maxX, minY, maxY;
			if(isResize) {
				minX = 0; 		maxX = 0; 
				minY = 0; 		maxY = 0;
				ptX[0] = srcWidth-1;	ptY[0] = 0;
				ptX[1] = 0;				ptY[1] = srcHeight-1;
				ptX[2] = srcWidth-1;	ptY[2] = srcHeight-1;
				for(i=0; i<3; i++) {
					x = ptY[i]*sinAF+ptX[i]*cosAF;
					if(x < minX) minX = x;
					if(x > maxX) maxX = x;
					y = ptY[i]*cosAF-ptX[i]*sinAF;
					if(y < minY) minY = y;
					if(y > maxY) maxY = y;
				}	
				x0 = minX;		y0 = minY;
			}
			else {
				minX = 0; 		maxX = srcWidth-1; 
				minY = 0; 		maxY = srcHeight-1;
				ptX[0] = srcWidth/2;		ptY[0] = srcHeight/2;
				x = ptY[0]*sinAF+ptX[0]*cosAF;
				y = ptY[0]*cosAF-ptX[0]*sinAF;	
				x0 = x-srcWidth/2;
				y0 = y-srcHeight/2;
			}
			int destWidth = Math.round(maxX-minX+1);
			int destHeight = Math.round(maxY-minY+1);
			BufferedImage destImage = new BufferedImage(destWidth, destHeight, BufferedImage.TYPE_INT_RGB);
			
			/** ��תͼ�� **/
			for(j=0; j<destHeight; j++) {
				for(i=0; i<destWidth; i++) {
					y = (i+x0)*sinAF + (j+y0)*cosAF;
					x = (i+x0)*cosAF - (j+y0)*sinAF; 
					m = (int)y;		n = (int)x;
					p = x-n;		q = y-m;		
		
					if(m >= 0 && m < srcHeight && n >= 0 && n < srcWidth) {
						m1 = m+1 >= srcHeight ? m : m+1;
						n1 = n+1 >= srcWidth ? n : n+1;
						ProcessUtil.decodeColor(srcRGBs[m*srcWidth+n], p00);
						ProcessUtil.decodeColor(srcRGBs[m*srcWidth+n1], p01);
						ProcessUtil.decodeColor(srcRGBs[m1*srcWidth+n], p10);
						ProcessUtil.decodeColor(srcRGBs[m1*srcWidth+n1], p11);
		
						for(int k=0; k<3; k++) {
							rgb[k] = Math.round(ProcessMath.biLinear(p00[k], p01[k], p10[k], p11[k], p, q));
						}
						
					}
					else { for(int k=0; k<3; k++) rgb[k] = 255; }
					
					destImage.setRGB(i, j, ProcessUtil.encodeColor(rgb));
				}
			}
			
			return destImage;
		}
    /*
     *��ʱ�㷨�� �����Լ���Ƭred to blue
     */
    public static BufferedImage redtoblue(BufferedImage srcImage){
		int width = srcImage.getWidth();
		int height = srcImage.getHeight();
		int srcRGBs[] = srcImage.getRGB(0, 0, width, height, null, 0, width);
		BufferedImage destImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		for(int i=0; i<height; i++){
			for(int j=0;j<width;j++){
				
			int rgb[] = new int[3];
			ProcessUtil.decodeColor(srcRGBs[i*width+j], rgb);
			if(!((i>169&j>61&i<294&j<355)|(i>323&i<452&j<300&j>173))){
			if(rgb[0]<130&&rgb[0]>80&&rgb[1]<90&&rgb[1]>55&&rgb[2]>35&&rgb[2]<65){
				rgb[0] = 51;
				rgb[1] =102;
				rgb[2] = 255;
				srcRGBs[i*width+j] = ProcessUtil.encodeColor(rgb);
			}}
			}
		}
		destImage.setRGB(0, 0, width, height, srcRGBs, 0, width);
		return destImage;
    }
}