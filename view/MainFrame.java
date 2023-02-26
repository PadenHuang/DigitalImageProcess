package view;
/*
 * ��ʱ��¼���ļ�ѡ���� ȡ����bug(���޸���)
 * ��ʱ��¼��û����Ӷ����Բ�ֵ��λ�õ��ж�
 * ��ʱ��¼������dialogȡ�����б���Ȼ����....(���޸���)
 * ��ʱ��¼����С����ֵ�� ���ڼ�С���ȷ����������С���˷� ��Ϻ��� ͼ��̫���Ӳ���ʵ��
 *                                        �� ����б�������жϣ������򵥣�����ͼ����    ��û����      
 * ��ʱ��¼���鿴�ݶ�ͼ������ı䴰�ڴ�С���Զ�����repaint����    
 * ��ʱ��¼���ݶ�ͼЧ�����ã���ʼ�����õĲ��á�          
 * ��ʱ��¼��Prewitt�������ĸ���              
 * ��ʱ��¼��a,b��ֵ���ȷ��
 * ��ʱ��¼������HT�任���뷨������������ȥ����������ֱ�߶��ԣ�����������չȥʵ�֡�������һ������ͻ����һȦ�㡣Ȼ��һЩϸ�ڴ���
 */







import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import java.io.*;

import javax.imageio.*;

import test.TestForLinearDlg;

import algorithm.EdgeDetection;
import algorithm.GeoTransform;
import algorithm.ImageEnhancement;
import algorithm.ImageSegmentation;
import algorithm.Morphology;
import algorithm.WaveletTransform;


import java.util.*;

/**
 * ���������
 */
public class MainFrame extends JFrame implements ListSelectionListener {
	
	JMenuBar mb;
    JMenu fileMenu;
    JMenuItem openItem;
    JMenuItem saveItem;
    JMenuItem exitItem;
    
    JMenu editMenu;
    JMenuItem undoItem;
    JMenuItem redoItem;
    
    JMenu geoMenu;
    JMenuItem translationItem;
    JMenuItem horMirrorItem;
    JMenuItem verMirrorItem;
    JMenuItem scaleItem;
    JMenuItem rotateItem;
    
    JMenu ehanceMenu;
    JMenuItem grayScaleItem;
    JMenu grayTransformation;
    JMenu linearTransformation;
    JMenuItem liNotSegmentation;
    JMenuItem liSegmentation;
    JMenuItem nonLinearTransformation;
    JMenu histogramModification;
    JMenuItem histgramItem;
    JMenuItem histogramEqualizationItem;
    JMenuItem histogramSpecificationItem;
    JMenu imageSmoothing;
    JMenuItem medianFiltering; 
    JMenuItem gaussianSmoothingItem;
    JMenuItem fieldAverageItem;
    JMenu imageSharpening;
    JMenuItem laplacian;
    JMenuItem laplacianHiBoostFiltering;
    JMenuItem gaussianHiBoostFiletering;
    
    JMenu imageSegmentation;
    JMenu thresholdSeg;
    JMenuItem simpleThreshold;
    JMenuItem iterativeThreshold;
    JMenuItem otsuThreshold;
    JMenuItem dynamicThreshold;
	JMenuItem regionSegmentationMethod;
    
    JMenu edgeMenu;
    JMenu gradientMenu;
    JMenuItem horGradientItem;
    JMenuItem verGradientItem;
    JMenuItem sobelItem;
    JMenuItem cannyItem;
    JMenuItem oriItem;

    
    JMenu frequencyDomainProcessing;
    JMenuItem fftItem;
    JMenuItem decomposeItem;
    JMenuItem markItem;
    
    JMenu imgShow;//hwq:ͼ���ʾ������
    JMenu morphology;
    JMenuItem corrosionItem;
    JMenuItem swellItem;
    JMenuItem getBone;
    
    
    JToolBar tb;
    JButton openBtn;
    JButton saveBtn;
    JButton exitBtn;
    
    
    ImagePanel imagePanel;
    JScrollPane scrollPane;
    JScrollPane scrollPane1;
    ImageIcon imageIcon;
    BufferedImage image;
    
    JFileChooser chooser;
    ImagePreviewer imagePreviewer;
    ImageFileView fileView;
    JList operationList;
    DefaultListModel dlm;

   
    
    ImageFileFilter bmpFilter;
    ImageFileFilter jpgFilter;
	ImageFileFilter gifFilter;
	ImageFileFilter bothFilter;
	
	LinkedList undoList;
	LinkedList redoList;
	LinkedList allList;
	int pointer = 0;
	int newImage = 0;
	boolean okFlag = false;
	private final static int MAX_UNDO_COUNT = 10;
	private final static int MAX_REDO_COUNT = 10;
	private final static int MAX_ALL_COUNT = 50;

	/*
	* ������*/
    public MainFrame() {
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                exit(e);
            }
        });
        
        undoList = new LinkedList();
        redoList = new LinkedList();
        allList = new LinkedList();
        initComponents();
    }

	/*
	* ��ʼ�����*/
    private void initComponents() {
    	Container contentPane = getContentPane();
    	
    	imagePanel = new ImagePanel(image);
        scrollPane = new JScrollPane(imagePanel);
        contentPane.add(scrollPane, BorderLayout.CENTER);
        
        
        chooser = new JFileChooser();
        imagePreviewer = new ImagePreviewer(chooser);
        fileView = new ImageFileView();
	    bmpFilter = new ImageFileFilter("bmp", "BMP Image Files");
	    jpgFilter = new ImageFileFilter("jpg", "JPEG Compressed Image Files");
		gifFilter = new ImageFileFilter("gif", "GIF Image Files");
		bothFilter = new ImageFileFilter(new String[] {"bmp", "jpg", "gif"}, "BMP, JPEG and GIF Image Files");
	    chooser.addChoosableFileFilter(gifFilter);
	    chooser.addChoosableFileFilter(bmpFilter);
        chooser.addChoosableFileFilter(jpgFilter);
        chooser.addChoosableFileFilter(bothFilter);
        chooser.setAccessory(imagePreviewer);
        chooser.setFileView(fileView);
        chooser.setAcceptAllFileFilterUsed(false);
                    
		Icon openIcon = new ImageIcon("open.gif");
		Icon saveIcon = new ImageIcon("save.gif");
		Icon exitIcon = new ImageIcon("exit.gif");
		Icon undoIcon = new ImageIcon("images/undo.gif");
		Icon redoIcon = new ImageIcon("images/redo.gif");
		
		//----�����б�---------------------------------------------------------
		operationList = new JList();
		scrollPane1 = new JScrollPane(operationList);
		contentPane.add(scrollPane1, BorderLayout.EAST);
		
		final DefaultListModel dlm = new DefaultListModel();
		operationList.setModel(dlm);
		operationList.addListSelectionListener(this);
		
		

		
		//----�˵���------------------------------------------------------------
		mb = new JMenuBar();
		setJMenuBar(mb);
		//----File�˵�----------------------------------------------------------
		fileMenu = new JMenu("�ļ�(F)");
		fileMenu.setMnemonic('F');
		mb.add(fileMenu);
		
		openItem = new JMenuItem("��(O)", openIcon);
		openItem.setMnemonic('O');
		openItem.setAccelerator(KeyStroke.getKeyStroke('O', Event.CTRL_MASK));
		openItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				openFile(e);
				if(okFlag){
					newImage++;
			 	    dlm.addElement("��"+pointer+"������ͼ��"+newImage+"                            ");
			 	    pointer++;
			 	    okFlag = false;
				}
			}
		});
		
		saveItem = new JMenuItem("����(S)", saveIcon);
		saveItem.setMnemonic('S');
		saveItem.setAccelerator(KeyStroke.getKeyStroke('S', Event.CTRL_MASK));
		saveItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				saveFile(e);
			}
		});
		
		
		exitItem = new JMenuItem("�˳�(X)",exitIcon);
		exitItem.setMnemonic('X');
		exitItem.setAccelerator(KeyStroke.getKeyStroke('X', Event.CTRL_MASK));
		exitItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				exitSystem(e);
			}
		});
		
		fileMenu.add(openItem);
		fileMenu.add(saveItem);
		fileMenu.addSeparator();
		fileMenu.add(exitItem);
		
		//----Edit���༭���˵�----------------------------------------------------------
		editMenu = new JMenu("�༭(E)");
		editMenu.setMnemonic('E');
		mb.add(editMenu);
		
		undoItem = new JMenuItem("����(U)", undoIcon);
		undoItem.setMnemonic('U');
		undoItem.setAccelerator(KeyStroke.getKeyStroke('Z', Event.CTRL_MASK));
		undoItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				undo(e);
			}
		});
		
		redoItem = new JMenuItem("����(R)", redoIcon);
		redoItem.setMnemonic('R');
		redoItem.setAccelerator(KeyStroke.getKeyStroke('Y', Event.CTRL_MASK));
		redoItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				redo(e);
			}
		});
		
		editMenu.add(undoItem);
		editMenu.add(redoItem);
		
		//----Geo�˵���hwq:���α任��----------------------------------------------------------
		geoMenu = new JMenu("���α任(G)");
		geoMenu.setMnemonic('G');
		mb.add(geoMenu);
		
		translationItem =new JMenuItem("ͼƬƽ��(T)");
		translationItem.setMnemonic('T');
		translationItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e){
				translation(e);
				if(okFlag){
	        	dlm.addElement("��"+pointer+"����ͼ��"+newImage+"��ͼ��ƽ��");}
				pointer++;
				okFlag = false;
			}
		});
		
		horMirrorItem = new JMenuItem("ˮƽ����(H)");
		horMirrorItem.setMnemonic('H');
		horMirrorItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				horMirror(e);
				dlm.addElement("��"+pointer+"����ͼ��"+newImage+"��ˮƽ����");
				pointer++;
			}
		});
		
		verMirrorItem = new JMenuItem("��ֱ����(V)");
		verMirrorItem.setMnemonic('V');
		verMirrorItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				verMirror(e);
				dlm.addElement("��"+pointer+"����ͼ��"+newImage+"����ֱ����");
				pointer++;
			}
		});
		
		scaleItem = new JMenuItem("��������(S)");
		scaleItem.setMnemonic('S');
		scaleItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				scale(e);
				if(okFlag){
				dlm.addElement("��"+pointer+"����ͼ��"+newImage+"����������");
				pointer++;
				okFlag = false;
				}
			}
		});
		
		rotateItem = new JMenuItem("��ת(R)");
		rotateItem.setMnemonic('R');
		rotateItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				rotate(e);
				if(okFlag){
				dlm.addElement("��"+pointer+"����ͼ��"+newImage+"����ת");
				pointer++;
				okFlag = false;
				}
			}
		});
		
		geoMenu.add(translationItem);
		geoMenu.add(horMirrorItem);
		geoMenu.add(verMirrorItem);
		geoMenu.add(scaleItem);
		geoMenu.add(rotateItem);
		
		//----Ehance�˵���hwq:ͼ����ǿ��-------------------------------------------------------
		ehanceMenu = new JMenu("ͼ����ǿ(E)");
		ehanceMenu.setMnemonic('E');
		mb.add(ehanceMenu);
		
		
		grayTransformation = new JMenu("�Ҷȱ任(T)");
		grayTransformation.setMnemonic('T');
		
		grayScaleItem =new JMenuItem("ͼƬ�ҶȻ�(G)");
		grayScaleItem.setMnemonic('G');
		grayScaleItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e){
				graySacle(e);
				dlm.addElement("��"+pointer+"����ͼ��"+newImage+"���ҶȻ�");
				pointer++;
			}
		});
		
		linearTransformation = new JMenu("���Ա任(L)");
		linearTransformation.setMnemonic('L');
		
		liNotSegmentation = new JMenuItem("���ֶ�����(N)");
		liNotSegmentation.setMnemonic('N');
		liNotSegmentation.addActionListener(new ActionListener() {
			public void actionPerformed (ActionEvent e){
				liNotSegmentation(e);
				if(okFlag){
					dlm.addElement("��"+pointer+"����ͼ��"+newImage+"�����ֶ����Ա任");
					pointer++;
					okFlag = false;
				}
			}
		});
		
		liSegmentation = new JMenuItem("�ֶ�����(S)");
		liSegmentation.setMnemonic('S');
		liSegmentation.addActionListener(new ActionListener() {
			public  void actionPerformed (ActionEvent e){
				liSegmentation(e);
				if(okFlag){
					dlm.addElement("��"+pointer+"����ͼ��"+newImage+"���ֶ����Ա任");
					pointer++;
					okFlag = false;
				}
			}
		});
		
		nonLinearTransformation = new JMenuItem("�����Ա任(N)");
		nonLinearTransformation.setMnemonic('L');
		nonLinearTransformation.addActionListener(new ActionListener() {
			public void actionPerformed (ActionEvent e){
                nonlinearTran(e);
			}
		});
		
		histogramModification = new JMenu("ֱ��ͼ����(H)");
		histogramModification.setMnemonic('H');
		
		histgramItem = new JMenuItem("�Ҷ�ֱ��ͼ(H)");
		histgramItem.setMnemonic('H');
		histgramItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				histgram(e);
			}
		});
		
		histogramEqualizationItem = new JMenuItem("ֱ��ͼ���⻯(E)");
		histogramEqualizationItem.setMnemonic('E');
		histogramEqualizationItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e){
				equalization(e);
				dlm.addElement("��"+pointer+"����ͼ��"+newImage+"��ֱ��ͼ���⻯");
				pointer++;
			}
		});
		
		histogramSpecificationItem = new JMenuItem("ֱ��ͼ�涨��(S)");
		histogramSpecificationItem.setMnemonic('E');
		histogramSpecificationItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e){
				specification(e);
			}
		});
		
		imageSmoothing = new JMenu("ͼ��ƽ��(S)");
		imageSmoothing.setMnemonic('S');
		
		
		medianFiltering = new JMenuItem("��ֵ�˲�(F)");
		medianFiltering.setMnemonic('F');
		medianFiltering.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e){
				medianFiltering(e);
				dlm.addElement("��"+pointer+"����ͼ��"+newImage+"����ֵ�˲�");
				pointer++;
			}
		});
		
		gaussianSmoothingItem = new JMenuItem("��˹ƽ��(G)");
		gaussianSmoothingItem.setMnemonic('G');
		gaussianSmoothingItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e){
				gaussianSmoothing(e);
				dlm.addElement("��"+pointer+"����ͼ��"+newImage+"����˹ƽ��");
				pointer++;
			}
		});
		
		fieldAverageItem = new JMenuItem("����ƽ��(F)");
		fieldAverageItem.setMnemonic('F');
		fieldAverageItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e){
				fieldAverage(e);
				dlm.addElement("��"+pointer+"����ͼ��"+newImage+"������ƽ��");
				pointer++;
			}
		});
		
		imageSharpening = new JMenu("ͼ����(S)");
		imageSharpening.setMnemonic('S');
		
		laplacian = new JMenuItem("������˹(L)");
		laplacian.setMnemonic('L');
		laplacian.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e){
				laplacian(e);
				dlm.addElement("��"+pointer+"����ͼ��"+newImage+"��������˹��");
				pointer++;
			}
		});
		
		laplacianHiBoostFiltering = new JMenuItem("��˹�����˲�(H)");
		laplacianHiBoostFiltering.setMnemonic('H');
		laplacianHiBoostFiltering.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e){
				laplacianHiBoostFiltering(e);
				dlm.addElement("��"+pointer+"����ͼ��"+newImage+"��������˹�����˲�");
				pointer++;
			}
		});
		
		gaussianHiBoostFiletering = new JMenuItem("��˹�����˲�(G)");
		gaussianHiBoostFiletering.setMnemonic('G');
		gaussianHiBoostFiletering.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e){
				gaussianHiBoostFiletering(e);
				dlm.addElement("��"+pointer+"����ͼ��"+newImage+"����˹�����˲�");
				pointer++;
			}
		});
		
		
		
		ehanceMenu.add(grayTransformation);
		grayTransformation.add(grayScaleItem);
		grayTransformation.add(linearTransformation);
		linearTransformation.add(liNotSegmentation);
		linearTransformation.add(liSegmentation);
		grayTransformation.add(nonLinearTransformation);
		ehanceMenu.add(histogramModification);
		histogramModification.add(histgramItem);
		histogramModification.add(histogramEqualizationItem);
		histogramModification.add(histogramSpecificationItem);
		ehanceMenu.add(imageSmoothing);  
		imageSmoothing.add(medianFiltering);
		imageSmoothing.add(gaussianSmoothingItem);
		imageSmoothing.add(fieldAverageItem);
		ehanceMenu.add(imageSharpening);
		imageSharpening.add(laplacian);
		imageSharpening.add(laplacianHiBoostFiltering);
		imageSharpening.add(gaussianHiBoostFiletering);
		
		//----ͼ��ָ�----------------------------------------------------------
		imageSegmentation = new JMenu("ͼ��ָ�(I)");
		imageSegmentation.setMnemonic('I');
		mb.add(imageSegmentation);
		
		thresholdSeg = new JMenu("ȫ����ֵ�ָ�(T)");
		thresholdSeg.setMnemonic('T');
		
		simpleThreshold = new JMenuItem("����ֵ(S)");
		simpleThreshold.setMnemonic('S');
		simpleThreshold.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e){
				threshold(e);
				if(okFlag){
				 	dlm.addElement("��"+pointer+"����ͼ��"+newImage+"������ֵ�ָ�");
				 	pointer++;
				 	okFlag = false;
				}
				
			}
		});
		
		iterativeThreshold = new JMenuItem("������ֵ(I)");
		iterativeThreshold.setMnemonic('I');
		iterativeThreshold.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e){
				iterative(e);
				if(okFlag){
					dlm.addElement("��"+pointer+"����ͼ��"+newImage+"��������ֵ�ָ�");
					pointer++;
					okFlag = false;
				}
			}
		});
		
		otsuThreshold = new JMenuItem("M=2��Ostu��(O)");
		otsuThreshold.setMnemonic('O');
		otsuThreshold.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e){
				otsu(e);
				dlm.addElement("��"+pointer+"����ͼ��"+newImage+"��ostu��ֵ�ָ�");
				pointer++;
			}
		});
		
		dynamicThreshold = new JMenuItem("��̬��ֵ�ָ�(D)");
		dynamicThreshold.setMnemonic('D');
		dynamicThreshold.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e){
				dynamicThreshold(e);
				dlm.addElement("��"+pointer+"����ͼ��"+newImage+"����̬��ֵ�ָ�");
				pointer++;
			}
		});

		/*hwq������ָ��Ч���붯̬��ֵ�ָ�һ��*/
		regionSegmentationMethod = new JMenuItem("����ָ�(A)");
		regionSegmentationMethod.setMnemonic('A');
		regionSegmentationMethod.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e){
				regionSegmentationMethod(e);
				dlm.addElement("��"+pointer+"����ͼ��"+newImage+"������ָ�");
				pointer++;
			}
		});
		
		imageSegmentation.add(thresholdSeg);
		thresholdSeg.add(simpleThreshold);
		thresholdSeg.add(iterativeThreshold);
		thresholdSeg.add(otsuThreshold);
		imageSegmentation.add(dynamicThreshold);
		imageSegmentation.add(regionSegmentationMethod);//hwq

		//----��Ե���-------------------------------------------------------
		edgeMenu = new JMenu("��Ե���(E)");
		edgeMenu.setMnemonic('E');
		mb.add(edgeMenu);
		
		gradientMenu =  new JMenu("�ݶ�ͼ(G)");
		gradientMenu.setMnemonic('G');
		
	    horGradientItem = new JMenuItem("ˮƽ�ݶ�ͼ(H)");
	    horGradientItem.setMnemonic('H');
	    horGradientItem.addActionListener(new ActionListener() {
	    	public void actionPerformed(ActionEvent e){
	    		horGradient(e);
				dlm.addElement("��"+pointer+"����ͼ��"+newImage+"��ˮƽ�ݶ�");
				pointer++;
	    	}
	    });
	    
	    
	    verGradientItem = new JMenuItem("��ֱ�ݶ�ͼ(V)");
	    verGradientItem.setMnemonic('V');
	    verGradientItem.addActionListener(new ActionListener() {
	    	public void actionPerformed(ActionEvent e){
	    		verGradient(e);
				dlm.addElement("��"+pointer+"����ͼ��"+newImage+"����ֱ�ݶ�");
				pointer++;
	    	}
	    });
	   
	    sobelItem = new JMenuItem("Sobel�ݶ�ͼ(G)");
	    sobelItem.setMnemonic('G');
	    sobelItem.addActionListener(new ActionListener() {
	    	public void actionPerformed(ActionEvent e){
	    		sobel(e);
				dlm.addElement("��"+pointer+"����ͼ��"+newImage+"��Sobel�ݶ�");
				pointer++;
	    	}
	    });
		
		cannyItem = new JMenuItem("canny��Ե���(C)");
		cannyItem.setMnemonic('C');
		cannyItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				canny(e);
				dlm.addElement("��"+pointer+"����ͼ��"+newImage+"��canny��Ե���");
				pointer++;
				
				
			}
		});
		
		oriItem = new JMenuItem("�ݶ�ͼ(O)");
		oriItem.setMnemonic('O');
		oriItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e){
				orientation(e);
			}
		});
		/*hwq:����Ե�����ͼ��ָ���кϲ�*/
		edgeMenu.add(gradientMenu);
		imageSegmentation.add(edgeMenu);
		gradientMenu.add(horGradientItem);
		gradientMenu.add(verGradientItem);
		gradientMenu.add(sobelItem);
		edgeMenu.add(cannyItem);
		edgeMenu.add(oriItem);
		
		//----Ƶ����----------------------------------------------------------
		/*hwq*/
		/*frequencyDomainProcessing = new JMenu("Ƶ����(F)");*/
		frequencyDomainProcessing = new JMenu("ͼ��任(F)");
		frequencyDomainProcessing.setMnemonic('F');
		mb.add(frequencyDomainProcessing);
		
		fftItem = new JMenuItem("����ҶƵ��(F)");
		fftItem.setMnemonic('F');
		fftItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				fft(e);
				if(okFlag){
				 	dlm.addElement("��"+pointer+"����ͼ��"+newImage+"������Ҷ��ͨ�˲���");
				 	pointer++;
				 	okFlag = false;
					}
			}
		});
		
		decomposeItem = new JMenuItem("С���ֽ�(D)");
		decomposeItem.setMnemonic('D');
		decomposeItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				decompose(e);
				dlm.addElement("��"+pointer+"����ͼ��"+newImage+"��С���ֽ�");
				pointer++;
			}
		});
		
		markItem = new JMenuItem("С��ˮӡ(M)");
		markItem.setMnemonic('M');
		markItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				mark(e);
				dlm.addElement("��"+pointer+"����ͼ��"+newImage+"��С��ˮӡ");
				pointer++;
			}
		});
		
		frequencyDomainProcessing.add(fftItem);
		frequencyDomainProcessing.add(decomposeItem);
		frequencyDomainProcessing.add(markItem);
		
		
		//----��̬ѧ---------------------------------------------------------
		imgShow = new JMenu("ͼ���ʾ������(S)");
		imgShow.setMnemonic('S');
		morphology = new JMenu("��̬ѧ(M)");
		morphology.setMnemonic('M');
		/*mb.add(morphology);*/
		mb.add(imgShow);

		corrosionItem = new JMenuItem("��ʴ(C)");
		corrosionItem.setMnemonic('C');
		corrosionItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e){
				corrosion(e);
				dlm.addElement("��"+pointer+"����ͼ��"+newImage+"����ʴ");
				pointer++;
			}
		});
		
		swellItem = new JMenuItem("����(S)");
		swellItem.setMnemonic('S');
		swellItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e){
				swell(e);
				dlm.addElement("��"+pointer+"����ͼ��"+newImage+"������");
				pointer++;
			}
		});
		
		getBone = new JMenuItem("�Ǽ���ȡ(B)");
		getBone.setMnemonic('B');
		getBone.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e){
				getBone(e);
				dlm.addElement("��"+pointer+"����ͼ��"+newImage+"���Ǽ���ȡ");
				pointer++;
			}
		});
		
		morphology.add(corrosionItem);
		morphology.add(swellItem);
		morphology.add(getBone);
		imgShow.add(morphology);//hwq:��̬ѧ�ϲ���ͼ���ʾ������
		
		
		
		
				
		//----������------------------------------------------------------------
		tb = new JToolBar();
        contentPane.add(tb, BorderLayout.NORTH);
        //----------------------------------------------------------------------
		
		openBtn = new JButton(openIcon);
		openBtn.setToolTipText("��");
		tb.add(openBtn);
		openBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				openFile(e);
			}
		});
		
		saveBtn = new JButton(saveIcon);
		saveBtn.setToolTipText("����");
		tb.add(saveBtn);
		saveBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				saveFile(e);
			}
		});
		
		exitBtn = new JButton(exitIcon);
		exitBtn.setToolTipText("�˳�");
		tb.add(exitBtn);
		exitBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				exitSystem(e);
			}
		});
		//----------------------------------------------------------------------
    }
    












	/** �˳������¼� */
    private void exit(WindowEvent e) {
        System.exit(0);
    }
    
    void openFile(ActionEvent e) {
    	chooser.setDialogType(JFileChooser.OPEN_DIALOG);
    	if(chooser.showDialog(this, null) == JFileChooser.APPROVE_OPTION) {
    		try { image = ImageIO.read(chooser.getSelectedFile()); }
        	catch(Exception ex) { return ;}
        	imagePanel.setImage(image);
        	imagePanel.repaint();
        	undoList.clear();
        	redoList.clear();
        	saveRedoInfo(image);
        	saveAllInfo(image);
        	okFlag = true;
       
    	}
    }
    
    void exitSystem(ActionEvent e) {
    	System.exit(0);
    }
    
    void saveFile(ActionEvent e) {
    	chooser.setDialogType(JFileChooser.SAVE_DIALOG);
    	int index = chooser.showDialog(null, "�����ļ�");
    	if(index == JFileChooser.APPROVE_OPTION) {
    			image = (BufferedImage) undoList.get(0);
    			File f = chooser.getSelectedFile();
    			if(!f.exists()){
    				try {
						f.createNewFile();
					} catch (IOException e1) {
						e1.printStackTrace();
					}
    			}
    			String fileName = chooser.getName(f)+"123";
    			String writePath = chooser.getCurrentDirectory().getAbsolutePath() + fileName;
    			File filePath = new File(writePath);
    			try {
					ImageIO.write(image, "jpg", filePath);
				} catch (IOException e1) {
					e1.printStackTrace();
				}	
    	}
    }
    
    void saveUndoInfo(BufferedImage image) {
    	if(undoList.size() == MAX_UNDO_COUNT) {
    		undoList.removeLast();
    	}
    	undoList.addFirst(image);
    }
    
    void saveRedoInfo(BufferedImage image) {
    	if(redoList.size() == MAX_REDO_COUNT) {
    		redoList.removeLast();
    	}
    	redoList.addFirst(image);	
    }
    
    void saveAllInfo(BufferedImage image) {
    	if(allList.size() == MAX_ALL_COUNT) {
    		allList.removeFirst();
    	}
    	allList.addLast(image);
    }
    
    void undo(ActionEvent e) {
    	if(undoList.size() > 0) {
    	//	saveRedoInfo(image);
    		image = (BufferedImage)undoList.get(0);
    		imagePanel.setImage(image);
        	imagePanel.repaint();
        	undoList.remove(0);
    	}
    }
    
    void redo(ActionEvent e) {
    	if(redoList.size() > 0) {
    		saveUndoInfo(image);
    		image = (BufferedImage)redoList.get(0);
    		imagePanel.setImage(image);
        	imagePanel.repaint();
        	//redoList.remove(0);
    	}
    }
    
    void translation(ActionEvent e) {
    	TranslationDlg translationDlg = new TranslationDlg(this, true);
    	translationDlg.setLocationRelativeTo(this);
    	translationDlg.setImageWidth(image.getWidth());
    	translationDlg.setImageHeight(image.getHeight());
    	if(translationDlg.showModal() == JOptionPane.OK_OPTION) {
    		saveUndoInfo(image);
    		image = GeoTransform.translation(image, translationDlg.getDistance(), translationDlg.getDistance());
    		imagePanel.setImage(image);
        	imagePanel.repaint();
        	saveAllInfo(image);
        	okFlag = translationDlg.getOkFlag();
    	}	
    }
    
    void horMirror(ActionEvent e) {
    	saveUndoInfo(image);
		image = GeoTransform.horMirror(image);
		imagePanel.setImage(image);
    	imagePanel.repaint();
    	saveAllInfo(image);
    }
    
    void verMirror(ActionEvent e) {
    	saveUndoInfo(image);
		image = GeoTransform.verMirror(image);
		imagePanel.setImage(image);
    	imagePanel.repaint();
    	saveAllInfo(image);
    }
    
    void scale(ActionEvent e) {
    	ScaleDlg scaleDlg = new ScaleDlg(this, true);
    	scaleDlg.setLocationRelativeTo(this);
    	scaleDlg.setImageWidth(image.getWidth());
    	scaleDlg.setImageHeight(image.getHeight());
    	if(scaleDlg.showModal() == JOptionPane.OK_OPTION) {
    		saveUndoInfo(image);
    		image = GeoTransform.scale(image, scaleDlg.getScale(), scaleDlg.getScale());
    		imagePanel.setImage(image);
        	imagePanel.repaint();
        	saveAllInfo(image);
        	okFlag = true;
    	}	
    }
    
    void rotate(ActionEvent e) {
    	RotateDlg rotateDlg = new RotateDlg(this, true);
    	rotateDlg.setLocationRelativeTo(this);
    	if(rotateDlg.showModal() == JOptionPane.OK_OPTION) {
    		saveUndoInfo(image);
    		image = GeoTransform.rotate(image, rotateDlg.getAngle(), rotateDlg.getIsResize());
    		imagePanel.setImage(image);
        	imagePanel.repaint();
        	saveAllInfo(image);
        	okFlag = true;
    	}	
    }
    
    void graySacle(ActionEvent e) {
    		saveUndoInfo(image);
    		image = ImageEnhancement.grayScale(image);
    		imagePanel.setImage(image);
        	imagePanel.repaint();
        	saveAllInfo(image);
	}
    
    void liNotSegmentation(ActionEvent e){
    	LinearTransformationDlg litranmadlg = new LinearTransformationDlg(this, true);
    	litranmadlg.setImage(image);
    	litranmadlg.setLocationRelativeTo(this);
    	litranmadlg.show();
    	if(litranmadlg.getokOption() == 1){
    		saveUndoInfo(image);
    		image = ImageEnhancement.linearTransformation(image, Math.round(litranmadlg.getxValue()/2)-20,
    				Math.round(litranmadlg.getxValue1()/2)-20, 300-Math.round(litranmadlg.getyValue()/2)-15,
    				300-Math.round(litranmadlg.getyValue1()/2)-15);
    	}
    	imagePanel.setImage(image);
    	imagePanel.repaint();
    	saveAllInfo(image);
    	okFlag = true;
    	litranmadlg.dispose();
    	
    }
    
    void liSegmentation(ActionEvent e){
    	NotLinearTransformationDlg noLitranmadlg = new NotLinearTransformationDlg(this, true);
    	noLitranmadlg.setImage(image);
    	noLitranmadlg.setLocationRelativeTo(this);
    	noLitranmadlg.show();
    	if(noLitranmadlg.getokOption() == 1){
    		saveUndoInfo(image);
    		image = ImageEnhancement.NotSegmentationLinearTransformation(image, Math.round(noLitranmadlg.getxValue()/2)-20,
    				Math.round(noLitranmadlg.getxValue1()/2)-20, 300-Math.round(noLitranmadlg.getyValue()/2)-15,
    				300-Math.round(noLitranmadlg.getyValue1()/2)-15);
    	}
    	imagePanel.setImage(image);
    	imagePanel.repaint();
    	saveAllInfo(image);
    	okFlag = true;
    	noLitranmadlg.dispose();
    	
    }
    
    
	void nonlinearTran(ActionEvent e) {
		// TODO Auto-generated method stub
		
	}
	
    
	
    void histgram(ActionEvent e) {
		HistgramDlg histgramDlg = new HistgramDlg(this, true);
		histgramDlg.setImage(image);
    	histgramDlg.setLocationRelativeTo(this);
    	histgramDlg.show();
    	
    	//TestForLinearDlg linearDlg = new TestForLinearDlg(this, true);
    	//linearDlg.setImage(image);
    	//linearDlg.setLocationRelativeTo(this);
    	//linearDlg.show();
    }
    

	void equalization(ActionEvent e) {
		saveUndoInfo(image);
		image = ImageEnhancement.histogramEqualization(image);
		imagePanel.setImage(image);
    	imagePanel.repaint();
    	saveAllInfo(image);
	}

	void specification(ActionEvent e) {
		// TODO Auto-generated method stub
		
	}
	


	void medianFiltering(ActionEvent e) {
		saveUndoInfo(image);
		ImageEnhancement.findLine(image);
		image = ImageEnhancement.medianFiltering(image);
		imagePanel.setImage(image);
    	imagePanel.repaint();
    	saveAllInfo(image);
	}
	

	void gaussianSmoothing(ActionEvent e) {
		saveUndoInfo(image);
		image = ImageEnhancement.gaussianSmoothing(image);
		imagePanel.setImage(image);
    	imagePanel.repaint();
    	saveAllInfo(image);
	}
    
	void fieldAverage(ActionEvent e) {
		saveUndoInfo(image);
		image = ImageEnhancement.fieldAverage(image);
		imagePanel.setImage(image);
    	imagePanel.repaint();
    	saveAllInfo(image);
	}
	
    void laplacian(ActionEvent e) {
    	
    	/*saveUndoInfo(image);
    	EdgeDetector edgeDetector=new EdgeDetector();
        edgeDetector.setSourceImage(image);
        edgeDetector.setThreshold(128);
        edgeDetector.setWidGaussianKernel(5);
        edgeDetector.process();
        image = (BufferedImage) edgeDetector.getEdgeImage();
		imagePanel.setImage(image);
    	imagePanel.repaint();
    	saveAllInfo(image);*/
    	
		saveUndoInfo(image);
		image = ImageEnhancement.laplacian(image);
		imagePanel.setImage(image);
    	imagePanel.repaint();
    	saveAllInfo(image);
	
	}
    

	void gaussianHiBoostFiletering(ActionEvent e) {
		saveUndoInfo(image);
		image = ImageEnhancement.gaussianHiBoostFiletering(image);
		imagePanel.setImage(image);
    	imagePanel.repaint();
    	saveAllInfo(image);
	}

	void laplacianHiBoostFiltering(ActionEvent e) {
		saveUndoInfo(image);
		image = ImageEnhancement.laplacianHiBoostFiltering(image);
		imagePanel.setImage(image);
    	imagePanel.repaint();
    	saveAllInfo(image);
	
    	
	}

	
	public void valueChanged(ListSelectionEvent e) {
		Object source = e.getSource();
		if(source == operationList){
			saveUndoInfo(image);
			int selectNo = operationList.getSelectedIndex();
			image = (BufferedImage)allList.get(selectNo);
			imagePanel.setImage(image);
			imagePanel.repaint();
		}

		
	}
	
	public void threshold(ActionEvent e) {
		ThresholdDlg threshouldDlg = new ThresholdDlg(this, true);
		threshouldDlg.setLocationRelativeTo(this);
		threshouldDlg.show();
		if(threshouldDlg.getModelResult() == JOptionPane.OK_OPTION){
			saveUndoInfo(image);
			image = ImageSegmentation.threshold(image, threshouldDlg.getThreshold());
		    imagePanel.setImage(image);
		    imagePanel.repaint();
		    saveAllInfo(image);
		    okFlag = true;
		}
	}
	
	public void iterative(ActionEvent e) {
		IterativeDlg iterativeDlg = new IterativeDlg(this, true);
		iterativeDlg.setLocationRelativeTo(this);
		iterativeDlg.show();
		if(iterativeDlg.getModelResult() == JOptionPane.OK_OPTION){
			saveUndoInfo(image);
			image = ImageSegmentation.iterative(image, iterativeDlg.getJudgement(), iterativeDlg.getThreshold());
			imagePanel.setImage(image);
			imagePanel.repaint();
			saveAllInfo(image);
			okFlag = true;
		}
	}
	
	
	public void otsu(ActionEvent e) {
		saveUndoInfo(image);
		image = ImageSegmentation.otsu(image);
		imagePanel.setImage(image);
    	imagePanel.repaint();
    	saveAllInfo(image);
	}
	
	void dynamicThreshold(ActionEvent e) {
		saveUndoInfo(image);
		image = ImageSegmentation.dynamic(image);
		imagePanel.setImage(image);
		imagePanel.repaint();
		saveAllInfo(image);
		
	}

	void regionSegmentationMethod(ActionEvent e) {
		saveUndoInfo(image);
		image = ImageSegmentation.dynamic(image);
		imagePanel.setImage(image);
		imagePanel.repaint();
		saveAllInfo(image);

	}
	void horGradient(ActionEvent e ) {
		saveUndoInfo(image);
		image = EdgeDetection.horGradient(image);
		imagePanel.setImage(image);
		imagePanel.repaint();
		saveAllInfo(image);
	}
	
	void verGradient(ActionEvent e ) {
		saveUndoInfo(image);
		image = EdgeDetection.verGradient(image);
		imagePanel.setImage(image);
		imagePanel.repaint();
		saveAllInfo(image);
	}
	
	void sobel(ActionEvent e ) {
		saveUndoInfo(image);
		image = EdgeDetection.sobel(image);
		imagePanel.setImage(image);
		imagePanel.repaint();
		saveAllInfo(image);
	}

	void canny(ActionEvent e) {
		saveUndoInfo(image);
		image = EdgeDetection.canny(image);
		imagePanel.setImage(image);
        imagePanel.repaint();
		saveAllInfo(image);
	}
	
	void orientation(ActionEvent e) {

	   
		
	
		//A:�õ���Ҫ������
		//1.1�ݶȷ�������
		int orient[];
		orient = EdgeDetection.orientation((BufferedImage) redoList.getFirst());
		//for(int i=0; i<orient.length ; i+=25){
		//	System.out.println(orient[i]);
		//}
		//2.1����һ���Ƕȱ��������ڻ�ͼ�������ߵ���ʼ����յ�
		double angle;
		//3.1��Ե���ж�����Ҫ����canny��Ե��⡣�õ���Եͼ
		BufferedImage tempImage = image;
		//3.2��Ե�����������
		int tempRGBs[] = tempImage.getRGB(0, 0, image.getWidth(), image.getHeight(), null, 0, image.getWidth());
		int width = image.getWidth();
		int height = image.getHeight();
		
		//B����imagePanel���л�ͼ
		//1.1 �����ͼ��graphics
		Graphics g = imagePanel.getGraphics();
		//2.1 �ж���Щ����л�ͼ����ͼԭ�򣬶Ա�Եͼÿһ��25*25�ķ�����м�������������ںڵ㣨��Ե��������25*25�ķ����н��л���ͷ����
		for(int j=0; j<height-height%25; j+=25){
			for(int i=0; i<width-width%25; i+=25){ //25*25�����ƶ�
				for(int n=0; n<25; n++){
					for(int m=0; m<25; m++){ //�����еĵ������Ѱ
						if(tempRGBs[(n+j)*width+m+i] == 0xff000000){//����Ǻ�ɫ����ʶ���������ڱ�Ե�����л�ͼ
							//������ڱ�Ե�㣬������������
							m = n = 25;
							//����ͷ��һ���������ͷ��ʼ��ͽ������x�����y����
							int x2, x1, y2, y1;
							//����ͷ�ڶ������õ���ǰ����ݶȽǶ�Angle
							angle = orient[(n+j)*width+m+i] * Math.PI/180;
							System.out.println(angle);
							System.out.println(orient[(n+j)*width+m+i]);
							//����ͷ��������ͨ���ݶȽǶȵõ���ͷ��ʼ��ͽ����㡣���Ƚ����ݶȽǶ�Ϊ�����Ļ���
							if(angle>=0){ //�ж��Ƿ�Ϊ����
								if(angle<=1* Math.PI & angle>=3/4* Math.PI){
			        				g.setColor(Color.blue);
			        			    x1 = i;
			        			    y1 = (int)(j+12-12*Math.tan(Math.PI-angle));
			        			    x2 = i + 24;
			        			    y2 = (int)(j+12+12*Math.tan(Math.PI-angle));
			        			    g.drawLine(x1, y1, x2, y2);
			        			    g.setColor(Color.red);
			        			    g.fillOval(x2-3, y2-3,6, 6);//��ͷָ��Ϊһ��СԲ������Ǽ�ͷ�ļ�����Ҫ����������㡣�鷳��
			        			}
			        			if(angle<3/4* Math.PI & angle>=1/2* Math.PI){
			        				g.setColor(Color.blue);
			        				x1 = (int)(i+12-12*Math.tan(angle-1/2*Math.PI));
			        				y1 = j;
			        				x2 = (int)(i+12+12*Math.tan(angle-1/2*Math.PI));
			        				y2 = j + 24;
			        			    g.drawLine(x1, y1, x2, y2);
			        			    g.setColor(Color.red);
			        			    g.fillOval(x2-3, y2-3,6, 6);		
			        			}
			        			if(angle<1/2* Math.PI & angle>=1/4* Math.PI){
			        				g.setColor(Color.blue);
			        				x1 = (int)(i+12+12*Math.tan(1/2*Math.PI-angle));
			        				y1 = j;
			        				x2 = (int)(i+12-12*Math.tan(1/2*Math.PI-angle));
			        				y2 = j + 24;
			        			    g.drawLine(x1, y1, x2, y2);
			        			    g.setColor(Color.red);
			        			    g.fillOval(x2-3, y2-3,6, 6);
			        			}
			        			if(angle<1/4* Math.PI & angle>=0* Math.PI){
			        				g.setColor(Color.blue);
			        				x1 = i +24;
			        				y1 = (int)(j+12-12*Math.tan(angle));
			        				x2 = i;
			        				y2 = (int)(j+12+12*Math.tan(angle));
			        			    g.drawLine(x1, y1, x2, y2);
			        			    g.setColor(Color.red);
			        			    g.fillOval(x2-3, y2-3,6, 6);
			        			}
							}else{//���Ϊ����������������һ����������angleҪ����һ���У��Ҽ�ͷ�����෴
								angle = angle + Math.PI;
			        			if(angle<=1* Math.PI & angle>=3/4* Math.PI){
			        				g.setColor(Color.blue);
			        			    x1 = 5*i;
			        			    y1 = (int)(5*j+12-12*Math.tan(Math.PI-angle));
			        			    x2 = 5*i + 24;
			        			    y2 = (int)(5*j+12+12*Math.tan(Math.PI-angle));
			        			    g.drawLine(x1, y1, x2, y2);
			        			    g.setColor(Color.red);
			        			    g.fillOval(x1-3, y1-3,6, 6);
			        			}
			        			if(angle<3/4* Math.PI & angle>=1/2* Math.PI){
			        				g.setColor(Color.blue);
			        				x1 = (int)(5*i+12-12*Math.tan(angle-1/2*Math.PI));
			        				y1 = 5*j;
			        				x2 = (int)(5*i+12+12*Math.tan(angle-1/2*Math.PI));
			        				y2 = 5*j + 24;
			        			    g.drawLine(x1, y1, x2, y2);
			        			    g.setColor(Color.red);
			        			    g.fillOval(x1-3, y1-3,6, 6);		
			        			}
			        			if(angle<1/2* Math.PI & angle>=1/4* Math.PI){
			        				g.setColor(Color.blue);
			        				x1 = (int)(5*i+12+12*Math.tan(1/2*Math.PI-angle));
			        				y1 = 5*j;
			        				x2 = (int)(5*i+12-12*Math.tan(1/2*Math.PI-angle));
			        				y2 = 5*j + 24;
			        			    g.drawLine(x1, y1, x2, y2);
			        			    g.setColor(Color.red);
			        			    g.fillOval(x1-3, y1-3,6, 6);
			        			}
			        			if(angle<1/4* Math.PI & angle>0* Math.PI){
			        				g.setColor(Color.blue);
			        				x1 = 5*i +24;
			        				y1 = (int)(5*j+12-12*Math.tan(angle));
			        				x2 = 5*i;
			        				y2 = (int)(5*j+12+12*Math.tan(angle));
			        			    g.drawLine(x1, y1, x2, y2);
			        			    g.setColor(Color.red);
			        			    g.fillOval(x1-3, y1-3,6, 6);
			        			}
							}
						}
					}
				}
			}
		}
		saveAllInfo(image);
		
	}
	
    void fft(ActionEvent e) {
    	FFTFilterDlg fftDlg = new FFTFilterDlg(this, true);
    	fftDlg.setImage(image);
    	fftDlg.setLocationRelativeTo(this);
    	if(fftDlg.showModal() == JOptionPane.OK_OPTION) {
    		saveUndoInfo(image);
    		image = fftDlg.getDestImage();
    		imagePanel.setImage(image);
        	imagePanel.repaint();
        	saveAllInfo(image);
        	okFlag = true;
    	}		
    }
    
    void decompose(ActionEvent e) {
    	saveUndoInfo(image);
		image = WaveletTransform.decompose(image);
		imagePanel.setImage(image);
    	imagePanel.repaint();	
    	saveAllInfo(image);
    }
    
    void mark(ActionEvent e) {
    	saveUndoInfo(image);
    	String word = JOptionPane.showInputDialog("����������˵�Ļ�",null);
    	String word1 = "�ν��ΰ���Ƽ";
		image = WaveletTransform.waterMark(image,word1);
		imagePanel.setImage(image);
    	imagePanel.repaint();
    	saveAllInfo(image);
    }
    
    void corrosion(ActionEvent e){
    	saveUndoInfo(image);
		image = Morphology.corrosion(image);
		imagePanel.setImage(image);
    	imagePanel.repaint();
    	saveAllInfo(image);
    }
    
    void swell(ActionEvent e){
    	
    }
	
    
    void getBone(ActionEvent e) {
    	saveUndoInfo(image);
		image = Morphology.getBone(image);
		imagePanel.setImage(image);
    	imagePanel.repaint();
    	saveAllInfo(image);
    }

	
	
	
}
