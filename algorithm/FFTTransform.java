package algorithm;

import javax.swing.*;
import java.awt.*;
import java.awt.image.*;
import java.util.*;

public class FFTTransform {
	
	public static final int opt = 1;		//��ѧDFT,ֱ���������м�
	public static final int DFT = 1;		//���任
	public static final int IDFT = -1;		//��任
	
	private static void fftCore(float arl[], float aim[], int length, int ex,
								float sinTbl[], float cosTbl[], float buf[]) {
		int i, j, k, w, j1, j2;
		int numb, lenb, timb;
		float xr, xi, yr, yi, nrml;
		
		if(opt == 1) {
			for(i=1; i<length; i+=2) {
				arl[i] = -arl[i];
				aim[i] = -aim[i];  
			}
		}
		
		numb = 1;
		lenb = length;
		for(i=0; i<ex; i++) {
			lenb /= 2;
			timb = 0;
			for(j=0; j<numb; j++) {
				w = 0;
				for(k=0; k<lenb; k++) {
					j1 = timb+k;
					j2 = j1+lenb;
					xr = arl[j1];
					xi = aim[j1];
					yr = arl[j2];
					yi = aim[j2];
					arl[j1] = xr+yr;
					aim[j1] = xi+yi;
					xr = xr-yr;
					xi = xi-yi;
					arl[j2] = xr*cosTbl[w]-xi*sinTbl[w];
					aim[j2] = xr*sinTbl[w]+xi*cosTbl[w];
					w += numb;
				}
				timb += (2*lenb);
			}
			numb *= 2;
		}
		
		birv(arl, length, ex, buf);
		birv(aim, length, ex, buf);
		if(opt == 1) {
			for(i=1; i<length; i+=2) {
				arl[i] = -arl[i];
				aim[i] = -aim[i];
			}
		}
		
		nrml = (float)(1.0/Math.sqrt(length));
		for(i=0; i<length; i++) {
			arl[i] *= nrml;
			aim[i] *= nrml;
		}
	}
	
	
	/*
	 * ��sin cos����
	 */
	private static void cstb(int length, int inv, float sinTbl[], float cosTbl[]) {
		int i;
		float xx, arg;
		
		xx = (float)(((-Math.PI)*2) / length);
		if(inv == IDFT) xx = -xx;
		for(i=0; i<length; i++) {
			arg = i*xx;
			sinTbl[i] = (float)Math.sin(arg);
			cosTbl[i] = (float)Math.cos(arg);
		}
	}
	
	/*
	 *
	 */
	private static void birv(float a[], int length, int ex, float b[]) {
		int i, ii, k, bit;
		
		for(i=0; i<length; i++) {
			for(k=0, ii=i, bit=0; ; bit<<=1, ii>>=1) {
				bit = (ii & 1) | bit;
				if(++k == ex) break;
			}
			b[i] = a[bit];
		}
		for(i=0; i<length; i++) {
			a[i] = b[i];
		}
	}
	
	/** һά����Ҷ���ٱ任
	 *  arl[]��		ʵ�������룩
	 *	aim[]��		�鲿�������
	 *	ex��		���ݸ��� = 2^ex
	 *	inv��		�任�������任��DFT������任��IDFT��*/
	public static void fft(float arl[], float aim[], int ex, int inv) {
		int i, length = 1;
		
		for(i=0; i<ex; i++) length *= 2;	//�������г���
		float sinTbl[] = new float[length];
		float cosTbl[] = new float[length];
		float buf[] = new float[length];
		
		cstb(length, inv, sinTbl, cosTbl);
		fftCore(arl, aim, length, ex, sinTbl, cosTbl, buf);
	}
	
	private static void rvmtx(float a[][], float b[][], int xsize, int ysize) {
		for(int j=0; j<ysize; j++) {
			for(int i=0; i<xsize; i++) {
				b[i][j] = a[j][i];
			}
		}
	}

	/** ��ά����Ҷ���ٱ任
	 *  arl[][]��		ʵ�������룩
	 *	aim[][]��		�鲿�������
	 *	inv��			�任�������任��DFT������任��IDFT��
	 *	xsize��			���
	 *  ysize��			�߶�*/
	public static void fft2d(float arl[][], float aim[][], int inv, 
							 int xsize, int ysize) {
		int i, exp;
		
		float brl[][] = new float[xsize][];
		float bim[][] = new float[xsize][];
		for(i=0; i<xsize; i++) {
			brl[i] = new float[ysize];
			bim[i] = new float[ysize];
		}
		float hsinTbl[] = new float[xsize];
		float hcosTbl[] = new float[xsize];
		float vsinTbl[] = new float[ysize];
		float vcosTbl[] = new float[ysize];
		float bufx[] = new float[xsize];
		float bufy[] = new float[ysize];
		
		cstb(xsize, inv, hsinTbl, hcosTbl);
		cstb(ysize, inv, vsinTbl, vcosTbl);
		
		exp = (int)(Math.log(xsize)/Math.log(2)); 
		for(i=0; i<ysize; i++) {
			fftCore(arl[i], aim[i], xsize, exp, hsinTbl, hcosTbl, bufx);
		}
		rvmtx(arl, brl, xsize, ysize);
		rvmtx(aim, bim, xsize, ysize);
		
		exp = (int)(Math.log(ysize)/Math.log(2)); 
		for(i=0; i<xsize; i++) {
			fftCore(brl[i], bim[i], ysize, exp, vsinTbl, vcosTbl, bufy);
		}
		rvmtx(brl, arl, ysize, xsize);
		rvmtx(bim, aim, ysize, xsize);
	}
	
	
}
