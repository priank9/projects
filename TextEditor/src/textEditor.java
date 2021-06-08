import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.TreeMap;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultCaret;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Highlighter;

public class textEditor {

	private JFrame editorFrame, aboutFrame, searchFrame, bookmarkFrame;
	private JPanel editorPanel2, editorPanel3, searchPanel1, searchPanel2, searchPanel3, bookmarkPanel1, bookmarkPanel2, bookmarkPanel3;
	private JFileChooser fileExplorer;
	private FileNameExtensionFilter filter;
	private JMenuBar menuBar;
	private JMenu fileMenu, toolsMenu, helpMenu, wordsMenu, bookmarkMenu, searchMenu, bookmarkOldMenu, analysisMenu;
	private String[] wordHistory = new String[5];
	private ArrayList<String> bookmarkHistory = new ArrayList<String>();
	private static JMenuItem openMenuItem, saveMenuItem, saveAsMenuItem, closeMenuItem, 
	existMenuItem, aboutMenuItem, countMenuItem, uniqueMenuItem, searchNewMenuItem,
	wordHistory1, wordHistory2, wordHistory3, wordHistory4, wordHistory5,
	bookmarkNewMenuItem, bookmarkDeleteMenuItem, wordCountMenuItem;
	private JComboBox<?> bookmarkDeleteOptions;
	private static JTextArea editorTextArea, aboutTextArea;
	private JLabel notificationLabel, searchLabel, bookmarkLabel;
	private JButton okButton, cancelButton, deleteButton;
	private JTextField searchTextField;
	private static File file;
	private static ArrayList<Integer> cp = new ArrayList<Integer>();
	private static TreeMap<String, Integer> wordCount = new TreeMap<String, Integer>();
	
	
	
	
	
	public static void main(String[] args) {
		textEditor te = new textEditor();
		//main method starts off by displaying a blank text area with a menu bar with file, tools, and help selections
		te.setupView();
	}

	//setup frame and panel that displays the text area, menu bar, and the notification label
	public void setupView(){
		//add attributes to the editorFrame
		editorFrame = new JFrame();
		editorFrame.setTitle("Text Editor");
		editorFrame.setPreferredSize(new Dimension(700,850));
		editorFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		editorFrame.setLayout(new BorderLayout());

		//create menu bar that lets user choose an action
		menuBar = new JMenuBar();
		fileMenu = new JMenu("File");
		toolsMenu = new JMenu("Tools");
		helpMenu = new JMenu("Help");
		//the file on the menu bar will contain: "open", "save", "save as", "close", and "exit" options
		openMenuItem = new JMenuItem("Open...");
		saveMenuItem = new JMenuItem("Save");
		saveAsMenuItem = new JMenuItem("Save As...");
		closeMenuItem = new JMenuItem("Close");
		existMenuItem = new JMenuItem("Exist");
		//the tools on the menu bar will contain: "Words", and "Bookmark" options with sub menus
		wordsMenu = new JMenu("Words");
		countMenuItem = new JMenuItem("Count");
		analysisMenu = new JMenu("Analysis");
		wordCountMenuItem = new JMenuItem("Most Occuring Words");
		uniqueMenuItem = new JMenuItem("Unique");
		bookmarkMenu = new JMenu("Bookmark");
		searchMenu= new JMenu("Search");
		searchNewMenuItem = new JMenuItem("Search new...");
		wordHistory1 = new JMenuItem(wordHistory[0]);
		wordHistory2 = new JMenuItem(wordHistory[1]);
		wordHistory3 = new JMenuItem(wordHistory[2]);
		wordHistory4 = new JMenuItem(wordHistory[3]);
		wordHistory5 = new JMenuItem(wordHistory[4]);
		//the tools on the menu bar will contain: "New...", "Old", and "Delete..." options with sub menus
		bookmarkNewMenuItem = new JMenuItem("New...");
		bookmarkOldMenu = new JMenu("Old");
		bookmarkDeleteMenuItem = new JMenuItem("Delete...");
		//the help on the menu bar will contain: just "about" options
		aboutMenuItem = new JMenuItem("About");

		//set an action for when the user chooses the "open" selection
		openMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e){
				//when "open" is click, user will be able to choose which file to open
				fileExplorer = new JFileChooser();
				fileExplorer.setCurrentDirectory(new File(System.getProperty("user.home"))); 
				//add filter which automatically only display txt files when the user is looking for a file
				filter = new FileNameExtensionFilter("Text files", "txt"); 
				fileExplorer.setFileFilter(filter); 

				//when the file is chosen, display the content of the file in the text area
				int result = fileExplorer.showOpenDialog(editorFrame);
				if(result == JFileChooser.APPROVE_OPTION){
					//select file in from the file explorer
					file = fileExplorer.getSelectedFile();
					FileReader readFile = null;
					try {
						//read the file selected from the file explorer
						readFile = new FileReader(file);
						editorTextArea.read(readFile, file);
						//update the notification label to the name of the file
						notificationLabel.setText(file.getName().toString());
					} catch (IOException e1) {
						e1.printStackTrace();
					}	
				}
			}
		});

		//set an action for when the user chooses the "save" selection
		saveMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e){
				//in the case the file has already been opened, automatically save the file to the currently open file
				if(file != null){
					try{
						FileWriter fw = new FileWriter(file.getAbsoluteFile());
						BufferedWriter bw = new BufferedWriter(fw); 
						bw.write(editorTextArea.getText());
						bw.close();
						//update the notification label to the name of the file
						notificationLabel.setText(file.getName().toString());
					} catch (IOException e1) {
						e1.printStackTrace();
					}

					//in the case the file has not been open, the user must specify new file name to save the content of the file
					//in other words, when the user is editing the text area from a blank page saving destination must be specified
				}else{
					try{
						fileExplorer = new JFileChooser();
						fileExplorer.setCurrentDirectory(new File(System.getProperty("user.home"))); 
						filter = new FileNameExtensionFilter("Text files", "txt"); 
						fileExplorer.setFileFilter(filter); 
						fileExplorer.showSaveDialog(editorFrame);

						FileWriter fw = new FileWriter(fileExplorer.getSelectedFile());
						BufferedWriter bw = new BufferedWriter(fw); 
						bw.write(editorTextArea.getText());
						bw.close();

						file = fileExplorer.getSelectedFile();
						//update the notification label to the name of the file you saved to
						notificationLabel.setText(file.getName().toString());
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
			}
		});

		//set an action for when the user chooses the "save as" selection
		saveAsMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e){
				//when you already have a file open and you want to save it as another name, you select this option
				try{
					fileExplorer = new JFileChooser();
					fileExplorer.setCurrentDirectory(new File(System.getProperty("user.home")));
					filter = new FileNameExtensionFilter("Text files", "txt");
					fileExplorer.setFileFilter(filter);
					fileExplorer.showSaveDialog(editorFrame);

					FileWriter fw = new FileWriter(fileExplorer.getSelectedFile());
					BufferedWriter bw = new BufferedWriter(fw);
					bw.write(editorTextArea.getText());
					bw.close();

					file = fileExplorer.getSelectedFile();
					//update the notification label to the name of the file you saved to
					notificationLabel.setText(file.getName().toString());
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		});

		//set an action for when the user chooses the "close" selection
		closeMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e){
				//set the text area to become blank
				editorTextArea.setText("");
			}
		});

		//set an action for when the user chooses the "exist" selection
		existMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e){
				//close the whole system
				System.exit(0);
			}
		});

		countMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e){
				//extract the string from what is currently displayed in the text area
				String txt = editorTextArea.getText().trim();
				StringTokenizer stk = new StringTokenizer(txt);
				int i = 0;
				//count the number of words in the text area
				while(stk.hasMoreTokens()){
					stk.nextToken();
					++i;
				}
				notificationLabel.setText("Word Count: " + i);
			}
		});

		uniqueMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e){
				//extract the string from what is currently displayed in the text area
				ArrayList<String> words = new ArrayList<String>();
				String txt = editorTextArea.getText().trim();
				StringTokenizer stk = new StringTokenizer(txt);
				//input all words in the text area in to an array list
				while(stk.hasMoreTokens()){
					String word = stk.nextToken().toLowerCase().replaceAll("\\W", "");
					words.add(word);
				}
				//identify all the unique words in the array list
				Set<String> uniqueWords = new HashSet<String>(words);
				notificationLabel.setText("Unique Words: " + uniqueWords.size());
			}
		});

		searchNewMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e){
				searchFrame = new JFrame();
				searchFrame.setTitle("Input");
				searchFrame.setPreferredSize(new Dimension(400,150));
				searchFrame.setResizable(false);
				searchFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				searchFrame.setLayout(new BorderLayout());

				searchLabel = new JLabel("Enter string to search");

				//Where users can input the words they are searching for
				searchTextField = new JTextField(15);

				okButton = new JButton("ok");	

				//This button highlights all the words existing in the file


				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e){

						Highlighter h = editorTextArea.getHighlighter();
						//Make sure to remove all highlight when the search is initiated again
						h.removeAllHighlights();
						String searchString = new String(searchTextField.getText());
						int searchPosition = editorTextArea.getText().indexOf(searchString);
						try {
							//Only run when the search word exists
							if(searchString.isEmpty()){
							}else{
								//set WordCount to 0 so that every time the search is initiated, WordCount starts from 0
								int wordCount = 0;
								//highlight all searched word while it exists in the document
								while(searchPosition != -1){
									wordCount++;
									h.addHighlight(searchPosition, searchPosition + searchString.length(), DefaultHighlighter.DefaultPainter);
									searchPosition = editorTextArea.getText().indexOf(searchString, searchPosition+searchString.length());
								};
								//Change the output on output frame so that it displays which string is being searched as well as how many times it appeared in the file
								if(wordCount == 0){
									notificationLabel.setText("word not found.");
								}else{
									notificationLabel.setText(searchTextField.getText() + ": " + wordCount + " occurences.");
								}
							}
							//Do nothing if the word doesn't exist
						} catch (BadLocationException e1) {
						} 
					}
				});

				cancelButton = new JButton("cancel");
				//The cancel button closes the search frame as well as deleting tall the highlights
				cancelButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e){
						Highlighter h = editorTextArea.getHighlighter();
						h.removeAllHighlights();
						notificationLabel.setText("");
						searchFrame.dispose();
					}
				});

				searchPanel1 = new JPanel();
				searchPanel1.setBackground(Color.CYAN);
				searchPanel1.add(searchLabel);

				//Setting up JPanel which provides the textfield for the user to input file
				searchPanel2 = new JPanel();
				searchPanel2.add(searchTextField);

				//Setting up JPanel for the buttons
				searchPanel3 = new JPanel();
				searchPanel3.add(okButton);
				searchPanel3.add(cancelButton);

				//Locate the question on the top
				searchFrame.add(searchPanel1, BorderLayout.NORTH);
				//Locate the textfield in the middle
				searchFrame.add(searchPanel2, BorderLayout.CENTER);
				//Locate the buttons on the bottom 
				searchFrame.add(searchPanel3, BorderLayout.SOUTH);

				searchFrame.pack();
				searchFrame.setVisible(true);	
				//Set frame to pop up in the middle of the windows
				searchFrame.setLocationRelativeTo(null);

			}
		});

		bookmarkNewMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e){
				bookmarkFrame = new JFrame();
				bookmarkFrame.setTitle("Input");
				bookmarkFrame.setPreferredSize(new Dimension(350,120));
				bookmarkFrame.setResizable(false);
				bookmarkFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				bookmarkFrame.setLayout(new BorderLayout());

				bookmarkLabel = new JLabel("Enter tag name");

				//Where users can input the words they are searching for
				searchTextField = new JTextField(15);

				okButton = new JButton("ok");	

				//This button adds the word inputed to the history of words bookmarked
				
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e){
						//add the word to the bookmark
						String searchString = new String(searchTextField.getText());
						bookmarkHistory.add(searchString);
						//use Set to remove all duplicate searches
						Set setItems = new LinkedHashSet(bookmarkHistory);
						bookmarkHistory.clear();
						bookmarkHistory.addAll(setItems);
						Collections.sort(bookmarkHistory);
						cp.add(editorTextArea.getCaretPosition()); 
						bookmarkOldMenu.removeAll();
						int i = 0;
						//creating the list of JMenuItems that should be included in the old Menu
						while(i < bookmarkHistory.size()){
							JMenuItem bookmarkHistoryItem = new JMenuItem(bookmarkHistory.get(i));
							bookmarkHistoryItem.setActionCommand(i+"");
							String bookmarkWord = bookmarkHistory.get(i);
							// adding actionlistener to move the caret to the word position where the bookmark specifies
							bookmarkHistoryItem.addActionListener(new ActionListener() {
								public void actionPerformed(ActionEvent e){
									editorTextArea.setCaretPosition(cp.get(Integer.parseInt(e.getActionCommand())));
								}
							});
							bookmarkOldMenu.add(bookmarkHistoryItem);
							i++;
						}bookmarkFrame.dispose();
					}
				});

				cancelButton = new JButton("cancel");
				//The cancel button closes the search frame as well as deleting tall the highlights
				cancelButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e){
						bookmarkFrame.dispose();
					}
				});

				bookmarkPanel1 = new JPanel();
				bookmarkPanel1.setBackground(Color.CYAN);
				bookmarkPanel1.add(bookmarkLabel);

				//Setting up JPanel which provides the textfield for the user to input file
				bookmarkPanel2 = new JPanel();
				bookmarkPanel2.add(searchTextField);

				//Setting up JPanel for the buttons
				bookmarkPanel3 = new JPanel();
				bookmarkPanel3.add(okButton);
				bookmarkPanel3.add(cancelButton);

				//Locate the question on the top
				bookmarkFrame.add(bookmarkPanel1, BorderLayout.NORTH);
				//Locate the textfield in the middle
				bookmarkFrame.add(bookmarkPanel2, BorderLayout.CENTER);
				//Locate the buttons on the bottom 
				bookmarkFrame.add(bookmarkPanel3, BorderLayout.SOUTH);

				bookmarkFrame.pack();
				bookmarkFrame.setVisible(true);	
				//Set frame to pop up in the middle of the windows
				bookmarkFrame.setLocationRelativeTo(null);

			}
		});

		bookmarkDeleteMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e){
				bookmarkFrame = new JFrame();
				bookmarkFrame.setTitle("Input");
				bookmarkFrame.setPreferredSize(new Dimension(350,100));
				bookmarkFrame.setResizable(false);
				bookmarkFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				bookmarkFrame.setLayout(new BorderLayout());

				bookmarkLabel = new JLabel("Select bookmark to delete");

				//Where users select from the combo box which bookmark selection to delete
				bookmarkDeleteOptions = new JComboBox<Object>(bookmarkHistory.toArray());
				bookmarkDeleteOptions.setPreferredSize(new Dimension(100,20));

				deleteButton = new JButton("delete");	

				//This button deletes the word from the list of old word searches that has been made

				deleteButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e){
						bookmarkDeleteOptions.getSelectedItem();
						int i = 0;
						//the while loop goes through all the words in the array of word history and find the matching one to delete it
						while( i < bookmarkHistory.size()){
							if(bookmarkHistory.get(i) == bookmarkDeleteOptions.getSelectedItem()){
								bookmarkHistory.remove(i);
							}else{
								i++;
							}
						}
						bookmarkFrame.dispose();
					}
				});

				cancelButton = new JButton("cancel");
				//The cancel button closes the search frame as well as deleting tall the highlights
				cancelButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e){
						bookmarkFrame.dispose();
					}
				});

				bookmarkPanel1 = new JPanel();
				bookmarkPanel1.setBackground(Color.CYAN);
				bookmarkPanel1.add(bookmarkLabel);
				bookmarkPanel1.add(bookmarkDeleteOptions);

				//Setting up JPanel for the buttons
				bookmarkPanel3 = new JPanel();
				bookmarkPanel3.add(deleteButton);
				bookmarkPanel3.add(cancelButton);

				bookmarkFrame.add(bookmarkPanel1, BorderLayout.NORTH);
				//Locate the buttons on the bottom 
				bookmarkFrame.add(bookmarkPanel3, BorderLayout.SOUTH);

				bookmarkFrame.pack();
				bookmarkFrame.setVisible(true);	
				//Set frame to pop up in the middle of the windows
				bookmarkFrame.setLocationRelativeTo(null);

			}
		});

		wordHistory1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e){
				Highlighter h = editorTextArea.getHighlighter();
				//Make sure to remove all highlight when the search is initiated again
				h.removeAllHighlights();
				String searchString = new String(wordHistory[0]);
				int searchPosition = editorTextArea.getText().indexOf(searchString);
				try {
					//Only run when the search word exists
					if(searchString.isEmpty()){
					}else{
						//set WordCount to 0 so that every time the search is initiated, WordCount starts from 0
						int wordCount = 0;
						//highlight all searched word while it exists in the document
						while(searchPosition != -1){
							wordCount++;
							h.addHighlight(searchPosition, searchPosition + searchString.length(), DefaultHighlighter.DefaultPainter);
							searchPosition = editorTextArea.getText().indexOf(searchString, searchPosition+searchString.length());
						};
						//Change the output on output frame so that it displays which string is being searched as well as how many times it appeared in the file
						if(wordCount == 0){
							notificationLabel.setText("word not found.");
						}else{
							notificationLabel.setText(searchTextField.getText() + ": " + wordCount + " occurences.");
						}
					}
					//Do nothing if the word doesn't exist
				} catch (BadLocationException e1) {
				} 
			}
		});

		//set an action for when the user chooses the "about" selection
		aboutMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e){
				//setup frame and panel that displays "about me" content
				aboutFrame = new JFrame();
				aboutFrame.setTitle("About Text Editor");
				aboutFrame.setPreferredSize(new Dimension(500,450));
				aboutFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
				aboutFrame.setLayout(new BorderLayout());

				//setup what is displayed about me; my information
				aboutTextArea = new JTextArea();
				aboutTextArea.setText("This is built by Tim Paik!\n\nNice to meet you!\n\nI created this\n\nI'am currently studying"
						+ "Informations Systems Management at Heinz College, "
						+ "\nCarnegie Mellon University\n\nMy favorite"
						+ "sport is basketball\n\nemail me at tpaik@andrew.cmu.edu if you want to shoot around with me.");
				//disable the user from being able to edit "about me"
				aboutTextArea.setEditable(false);

				JPanel aboutPanel = new JPanel();
				aboutPanel.add(aboutTextArea);

				aboutFrame.add(aboutPanel, BorderLayout.CENTER);

				aboutFrame.add(aboutTextArea);
				aboutFrame.pack();
				aboutFrame.setVisible(true);
				aboutFrame.setLocationRelativeTo(null);
			}
		});
		
		wordCountMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e){
				//setup frame and panel that displays "about me" content
				aboutFrame = new JFrame();
				aboutFrame.setTitle("Word Count Analysis");
				aboutFrame.setPreferredSize(new Dimension(500,450));
				aboutFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
				aboutFrame.setLayout(new BorderLayout());
				
				String txt = editorTextArea.getText().trim();
				StringTokenizer stk = new StringTokenizer(txt);
				//input all words in the text area in to an array list

				while(stk.hasMoreTokens()){
					String word = stk.nextToken().toLowerCase().replaceAll("\\W", "");
					if(!wordCount.containsKey(word)){
						wordCount.put(word, new Integer(1));
					}else{
						int value = wordCount.get(word).intValue();
						value++;
						wordCount.put(word, new Integer(value));
					}
				}
				
				
				Iterator<Entry<String,Integer>> it = wordCount.entrySet().iterator();
				
				while(it.hasNext()){
		
					Map.Entry<String, Integer> pair = it.next();
					System.out.println(pair.getKey() + " - "+pair.getValue());
					
				}
				
				/*System.out.println(wordCount.lastKey() + " - value : "+wordCount.get(wordCount.lastKey()));*/
				System.out.println(wordCount.subMap(txt, txt));
				
				//System.out.println(wordCount.get(word));
				//setup what is displayed about me; my information
				aboutTextArea = new JTextArea();
				aboutTextArea.setText("This is built by Tim Paik!\n\nNice to meet you!\n\nI created this\n\nI'am currently studying"
						+ "Informations Systems Management at Heinz College, "
						+ "\nCarnegie Mellon University\n\nMy favorite"
						+ "sport is basketball\n\nemail me at tpaik@andrew.cmu.edu if you want to shoot around with me.");
				//disable the user from being able to edit "about me"
				aboutTextArea.setEditable(false);

				JPanel aboutPanel = new JPanel();
				aboutPanel.add(aboutTextArea);

				aboutFrame.add(aboutPanel, BorderLayout.CENTER);

				aboutFrame.add(aboutTextArea);
				aboutFrame.pack();
				aboutFrame.setVisible(true);
				aboutFrame.setLocationRelativeTo(null);
			}
		});
		
		//the the menu selections to file buttons
		fileMenu.add(openMenuItem);
		fileMenu.add(saveMenuItem);
		fileMenu.add(saveAsMenuItem);
		fileMenu.add(closeMenuItem);
		fileMenu.add(existMenuItem);
		toolsMenu.add(wordsMenu);
		wordsMenu.add(countMenuItem);
		wordsMenu.add(uniqueMenuItem);
		wordsMenu.add(searchMenu);
		bookmarkMenu.add(bookmarkNewMenuItem);
		bookmarkMenu.add(bookmarkOldMenu);
		bookmarkMenu.add(bookmarkDeleteMenuItem);
		searchMenu.add(searchNewMenuItem);
		toolsMenu.add(bookmarkMenu);
		toolsMenu.add(analysisMenu);
		analysisMenu.add(wordCountMenuItem);
		helpMenu.add(aboutMenuItem);
		
		//add these selections to the menu bar
		menuBar.add(fileMenu);
		menuBar.add(toolsMenu);
		menuBar.add(helpMenu);
		
		editorTextArea = new JTextArea(60,60);
		//editorTextArea.setEditable(true);
		editorTextArea.setLineWrap(true);
		editorTextArea.setWrapStyleWord(true);
		DefaultCaret caret = (DefaultCaret)editorTextArea.getCaret();
		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		
		JScrollPane scroll = new JScrollPane(editorTextArea);
		scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		
		notificationLabel = new JLabel();
		
		editorPanel2 = new JPanel();
		editorPanel2.add(scroll);
		
		editorPanel3 = new JPanel();
		editorPanel3.setBackground(Color.CYAN);
		editorPanel3.add(notificationLabel);
		
		editorFrame.setJMenuBar(menuBar);
		editorFrame.add(editorPanel2, BorderLayout.CENTER);
		editorFrame.add(editorPanel3, BorderLayout.SOUTH);
		
		editorFrame.pack();
		editorFrame.setVisible(true);
		editorFrame.setLocationRelativeTo(null);
	}
}