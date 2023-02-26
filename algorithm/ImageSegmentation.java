package algorithm;

import java.awt.image.BufferedImage;

public class ImageSegmentation {
	
	/*
	 * �������ƣ�����ֵ�ָ�
	 */
	public static BufferedImage threshold(BufferedImage srcImage,int t) {
		int width = srcImage.getWidth(); 
		int height = srcImage.getHeight();
		int srcRGBs[] = ImageEnhancement.grayScale(srcImage).getRGB(0, 0, width, height, null, 0, width);
		BufferedImage destImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		
		for(int i =0; i<srcRGBs.length; i++){
			if((srcRGBs[i]&0x000000ff)<=t)
			{
				srcRGBs[i] = 0xff000000;
			}else{
				srcRGBs[i] = 0xffffffff;
			}
		     	
		}
		
		destImage.setRGB(0, 0, width, height, srcRGBs, 0, width);
		return destImage;
		
		
	}
	
	/*
	 * �������ƣ�������ֵ�ָ�
	 */
	
	public static BufferedImage iterative(BufferedImage srcImage, int t0, int t1){
		int width = srcImage.getWidth(); 
		int height = srcImage.getHeight();
		int t2 = 0, temp1 = 0, temp2 = 0;
		int count1 = 0, count2 = 0;
		boolean flag = true;
		int srcRGBs[] = ImageEnhancement.grayScale(srcImage).getRGB(0, 0, width, height, null, 0, width);
		BufferedImage destImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		
		while(flag){
			for(int i =0; i<srcRGBs.length; i++){
				if((srcRGBs[i]&0x000000ff)<t1){
					temp1 = temp1+(srcRGBs[i]&0x000000ff);
					count1++;
				}
				else{
					temp2 = temp2+(srcRGBs[i]&0x000000ff);
					count2++;
				}
			}
			int u1 = Math.round(temp1/count1);
			int u2 = Math.round(temp2/count2);
			if(Math.abs((u2-u1)/2-t1)<=t0){
				flag = false;
			}else{				
				t1 = t2;
			}
		}
		
		for(int i =0; i<srcRGBs.length; i++){
			if((srcRGBs[i]&0x000000ff)<=t1)
			{
				srcRGBs[i] = 0xff000000;
			}else{
				srcRGBs[i] = 0xffffffff;
			}
		     	
		}
		
		destImage.setRGB(0, 0, width, height, srcRGBs, 0, width);
		return destImage;
		
		
		
	}

	/*
	 * �������ƣ�Ostu��
	 */
	public static BufferedImage otsu(BufferedImage srcImage) {
		//A:�õ�������Ҫ������,���ȶ�����������
		int histInfo[] = null;//ֱ��ͼ����
		int UT = 0;           //ȫͼƽ���Ҷ�ֵ��T
		int Uj1 = 0;          //��һ�������ƽ���Ҷ�ֵ��1
		int Uj2 = 0;          //�ڶ��������ƽ���Ҷ�ֵ��2
		double Wj1 = 0.0;     //��һ������ĸ��ʦ�1
		double Wj2 = 0.0;     //�ڶ�������ĸ��ʦ�2
		int threshold = 0;    //����ȷ������ֵ
		
		int temp = 0;         //�м����
		int temp1 = 0;        //�м����
		double temp2 = 0;     //�м����
		double temp3 = 0;     //�м����
		int width = srcImage.getWidth();
		int height = srcImage.getHeight();
		//�٣�������ǰд���ĺ�����ֱ��ͼ�����丳ֵ
		histInfo = ImageEnhancement.getHistInfo(srcImage, histInfo);
		//�ڣ�����ƽ���Ҷ�ֵ
		for(int i=0; i<histInfo.length; i++){
			temp = temp+histInfo[i]*i;
		}
	    UT = temp/(width*height);
		//B:�����������ֵ
	    temp = 0;
		for(int i=0; i<histInfo.length; i++){
			for(int j=0; j<i; j++){
				temp = temp+histInfo[j]*j;
				temp1 = temp1+histInfo[j];
			}
			Uj1 = temp1==0? 0:temp/temp1;
			Wj1 = temp1/width*height;
			temp2 = Wj1*(Uj1-UT)*(Uj1-UT);
			temp = temp1 = 0;
			for(int j=i; j<histInfo.length; j++){
				temp = temp+histInfo[j]*j;
				temp1 = temp1+histInfo[j];
			}
			Uj2 = temp1==0? 0:temp/temp1;
			Wj2 = temp1/width*height;
			temp2 = temp2 + Wj2*(Uj2-UT)*(Uj2-UT);
			if(temp3<temp2){
				temp3 = temp2;
				threshold = i;
			}
		}
		//C:������ֵ�ָ�
		return threshold(srcImage, threshold);
		
	}

	/*
	 * �������ƣ���̬��ֵ�ָ
	 */
	public static BufferedImage dynamic(BufferedImage srcImage) {
		//A����ͼ��Ϊ����Ϊ4�ı�����ֱ�Ӷ��������
		int width = srcImage.getWidth();
		int height = srcImage.getHeight();
		int destRGBs[] = new int[(width-width%4)*(height-height%4)];
		int srcRGBs[] = srcImage.getRGB(0, 0, width, height, null, 0, width);
		for(int j=0; j<height-height%4; j++){
			for(int i=0; i<width-width%4; i++){
				destRGBs[j*(width-width%4)+i] = srcRGBs[j*width+i];
				}
		}
		//B:�ֳ�16��Сͼ��,���ҽ���otsu��ֵ�ָ�
		for(int j=0; j<height-height%4; j=j+(height-height%4)/4){
			for(int i=0; i<width-width%4; i=i+(width-width%4)/4){
				BufferedImage tempImage = new BufferedImage((width-width%4)/4, (height-height%4)/4, BufferedImage.TYPE_INT_RGB);
				int tempRGBs[] = new int[(width-width%4)*(height-height%4)/16];
				for(int n=0; n<(height-height%4)/4; n++){
					for(int m=0; m<(width-width%4)/4; m++){
						tempRGBs[n*(width-width%4)/4+m] = destRGBs[(n+j)*(width-width%4)+i+m];
					}
				}
				tempImage.setRGB(0, 0, (width-width%4)/4, (height-height%4)/4, tempRGBs, 0, (width-width%4)/4);
				tempImage = otsu(tempImage);
				tempRGBs =tempImage.getRGB(0, 0, (width-width%4)/4, (height-height%4)/4, null, 0, (width-width%4)/4);
				for(int n=0; n<(height-height%4)/4; n++){
					for(int m=0; m<(width-width%4)/4; m++){
				destRGBs[(n+j)*(width-width%4)+i+m] = tempRGBs[n*(width-width%4)/4+m];
					}
				}
			}
		}
		//C:������ͼ��
		BufferedImage destImage = new BufferedImage((width-width%4), (height-height%4), BufferedImage.TYPE_INT_RGB);
		destImage.setRGB(0, 0, width-width%4, height-height%4, destRGBs, 0, width-width%4);
		return destImage;
	}
	

}
