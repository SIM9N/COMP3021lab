package base;

import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.FileChooser;
/**
 * 
 * NoteBook GUI with JAVAFX
 * 
 * COMP 3021
 * 
 * 
 * @author valerio
 *
 */
public class NoteBookWindow extends Application {

	/**
	 * TextArea containing the note
	 */
	final TextArea textAreaNote = new TextArea("");
	/**
	 * list view showing the titles of the current folder
	 */
	final ListView<String> titleslistView = new ListView<String>();
	/**
	 * 
	 * Combobox for selecting the folder
	 * 
	 */
	final ComboBox<String> foldersComboBox = new ComboBox<String>();
	/**
	 * This is our Notebook object
	 */
	NoteBook noteBook = null;
	/**
	 * current folder selected by the user
	 */
	String currentFolder = "";
	/**
	 * current search string
	 */
	String currentSearch = "";
	
	Boolean searchMode = false;
	
	Stage stage = null;
	
	File file = null;

	String currentNote = "";

	public static void main(String[] args) {
		launch(NoteBookWindow.class, args);
	}

	@Override
	public void start(Stage stage) {
		this.stage = stage;
		
		loadNoteBook();
		// Use a border pane as the root for scene
		BorderPane border = new BorderPane();
		// add top, left and center
		border.setTop(addHBox());
		border.setLeft(addVBox());
		border.setCenter(addGridPane());

		Scene scene = new Scene(border);
		stage.setScene(scene);
		stage.setTitle("NoteBook COMP 3021");
		stage.show();
	}

	/**
	 * This create the top section
	 * 
	 * @return
	 */
	private HBox addHBox() {

		HBox hbox = new HBox();
		hbox.setPadding(new Insets(15, 12, 15, 12));
		hbox.setSpacing(10); // Gap between nodes

		Button buttonLoad = new Button("Load From File");
		buttonLoad.setPrefSize(100, 20);
		buttonLoad.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {
				//TODO load a NoteBook from file
				
				FileChooser fileChooser = new FileChooser();
				fileChooser.setTitle("Please Choose An File Which Contains a NoteBook Object");
				
				FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Serialized Object File (*.ser)", "*.ser");
				fileChooser.getExtensionFilters().add(extFilter);
				
				file = fileChooser.showOpenDialog(stage);
				
				if (file != null) {
					loadNoteBook(file);
				}
			}
		});
		
		Button buttonSave = new Button("Save to File");
		buttonSave.setPrefSize(100, 20);
		buttonSave.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {
				//TODO load a NoteBook from file
				
				if (file != null) {
					noteBook.save(file.getName());
					
					Alert alert = new Alert(AlertType.INFORMATION);
					alert.setTitle("Successfully saved");
					alert.setContentText("You file has been saved to file " 
					+ file.getName());
					alert.showAndWait().ifPresent(rs -> {
					    if (rs == ButtonType.OK) {
					        System.out.println("Pressed OK.");
					    }
					});
				}
			}
		});
		Label searchLabel = new Label("Search :");
		searchLabel.setPrefSize(50, 20);
		
		TextField searchKey = new TextField();
		searchKey.setPrefSize(150, 20);
		
		Button searchBtn = new Button("Search");
		searchBtn.setPrefSize(100, 20);
		searchBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				currentSearch = searchKey.getText();
				textAreaNote.setText("");
				searchMode = true;
				List<Note> notes = noteBook.searchNotes(currentSearch);
				ArrayList<String> list = new ArrayList<String>();
				for (Note n : notes) {
					list.add(n.getTitle());
				}
				updateListView(list);
			}
		});
		
		Button clearSearchBtn = new Button("Clear Search");
		clearSearchBtn.setPrefSize(100, 20);
		clearSearchBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				currentSearch = "";
				searchKey.setText("");
				textAreaNote.setText("");
				searchMode = false;
				ArrayList<String> list = new ArrayList<String>();
				
				Folder folder = null;
				for (Folder f : noteBook.getFolders()) {
					if (f.getName().equals(currentFolder) )
						folder = f;
				}
				if (folder != null){
					for (Note n : folder.getNotes()) {
						if (n instanceof TextNote)
							list.add(n.getTitle());
					}
				}
				updateListView(list);
			}			
		});

		hbox.getChildren().addAll(buttonLoad, buttonSave, searchLabel, searchKey, searchBtn, clearSearchBtn);

		return hbox;
	}

	/**
	 * this create the section on the left
	 * 
	 * @return
	 */
	private VBox addVBox() {

		VBox vbox = new VBox();
		vbox.setPadding(new Insets(10)); // Set all sides to 10
		vbox.setSpacing(8); // Gap between nodes
		
		HBox hbox = new HBox();
		hbox.setPadding(new Insets(0, 0, 0, 0));
		hbox.setSpacing(8); // Gap between nodes

		// TODO: This line is a fake folder list. We should display the folders in noteBook variable! Replace this with your implementation
		ArrayList<String> foldersName = new ArrayList<String>();
		for (Folder f : noteBook.getFolders()) {
			foldersName.add(f.getName());
		}
		foldersComboBox.getItems().addAll(foldersName);

		foldersComboBox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Object>() {
			@Override
			public void changed(ObservableValue ov, Object t, Object t1) {
				currentFolder = t1.toString();
				// this contains the name of the folder selected
				// TODO update listview
				searchMode = false;
				ArrayList<String> list = new ArrayList<String>();
				
				Folder folder = null;
				for (Folder f : noteBook.getFolders()) {
					if (f.getName().equals(currentFolder) )
						folder = f;
				}
				if (folder != null){
					for (Note n : folder.getNotes()) {
						if (n instanceof TextNote)
							list.add(n.getTitle());
					}
				}
				updateListView(list);

			}

		});

		foldersComboBox.setValue("-----");
		
		Button addFolderBtn = new Button("Add a Folder");
		addFolderBtn.setPrefSize(100, 20);
		addFolderBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				TextInputDialog dialog = new TextInputDialog("Add a Folder");
				dialog.setTitle("Input");
				dialog.setHeaderText("Add a new folder for your notebook:");
			    dialog.setContentText("Please enter the name you want to create:");
			    
			    //Traditional way to get the response value.
			    Optional<String> result = dialog.showAndWait();
			    if (result.isPresent()){
			    	
			    	String folderName = result.get();
			    	
			        if (folderName.isEmpty()) {
						Alert alert = new Alert(AlertType.WARNING);
						alert.setTitle("Warrning");
						alert.setContentText("Please input an valid folder name");
						alert.showAndWait().ifPresent(rs -> {
						    if (rs == ButtonType.OK) {
						        System.out.println("Pressed OK.");
						    }
						});
						return;
			        }
			        
			        if (noteBook.containFolder(folderName)) {
						Alert alert = new Alert(AlertType.WARNING);
						alert.setTitle("Warrning");
						alert.setContentText("You already have a folder named with " + folderName);
						alert.showAndWait().ifPresent(rs -> {
						    if (rs == ButtonType.OK) {
						        System.out.println("Pressed OK.");
						    }
						});
			        }
			        else {
			        	noteBook.addFolder(folderName);
			        	foldersComboBox.getItems().clear();
			    		ArrayList<String> foldersName = new ArrayList<String>();
			    		for (Folder f : noteBook.getFolders()) {
			    			foldersName.add(f.getName());
//			    			System.out.println(f.getName());
			    		}
			    		foldersComboBox.getItems().addAll(foldersName);
			        }
			    }
			    
			}			
		});
		
		Button addNoteBtn = new Button("Add a Note");
		addNoteBtn.setPrefSize(100, 20);
		addNoteBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				TextInputDialog dialog = new TextInputDialog("Add a Note");
				dialog.setTitle("Input");
				dialog.setHeaderText("Add a new note for current folder");
			    dialog.setContentText("Please enter the name of your note:");
			    
			    Optional<String> result = dialog.showAndWait();
			    if (result.isPresent()){
			    	String noteName = result.get();
			    	String currentFolder = foldersComboBox.getValue();
			    	
			        if (noteName.isEmpty()) {
						Alert alert = new Alert(AlertType.WARNING);
						alert.setTitle("Warrning");
						alert.setContentText("Please input an valid note name");
						alert.showAndWait().ifPresent(rs -> {
						    if (rs == ButtonType.OK) {
						        System.out.println("Pressed OK.");
						    }
						});
						return;
			        }
			        
			        if (currentFolder.isEmpty()) {
						Alert alert = new Alert(AlertType.WARNING);
						alert.setTitle("Warrning");
						alert.setContentText("Please choose a folder first!");
						alert.showAndWait().ifPresent(rs -> {
						    if (rs == ButtonType.OK) {
						        System.out.println("Pressed OK.");
						    }
						});
			        }
			        else {
			        	noteBook.createTextNote(currentFolder, noteName);
			        	ArrayList<String> list = new ArrayList<String>();
						Folder folder = null;
						for (Folder f : noteBook.getFolders()) {
							if (f.getName().equals(currentFolder) )
								folder = f;
						}
						if (folder != null){
							for (Note n : folder.getNotes()) {
								if (n instanceof TextNote)
									list.add(n.getTitle());
							}
						}
						updateListView(list);
			        }
			        
			        
			    	
			    }
			}			
		});

		titleslistView.setPrefHeight(100);

		titleslistView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Object>() {
			@Override
			public void changed(ObservableValue ov, Object t, Object t1) {
				if (t1 == null)
					return;
				
				String title = t1.toString();
				currentNote = title;
//				System.out.print(title);
				
				if (searchMode) {
					for (Folder f : noteBook.getFolders()) {
						for (Note n : f.getNotes()) {
							if (n.getTitle().equals(title) & n instanceof TextNote) {
								String content = ((TextNote)n).getContent();
								textAreaNote.setText(content);
							}
						}
					}
					return;
				}
				// This is the selected title
				// TODO load the content of the selected note in
				// textAreNote
				Folder folder = null;
				for (Folder f : noteBook.getFolders()) {
					if (f.getName().equals(currentFolder) )
						folder = f;
				}
				
				TextNote textNote = null;
				
				if(folder != null){
					for (Note n : folder.getNotes()) {
					if (n.getTitle().equals(title) & n instanceof TextNote)
						textNote = (TextNote)n;
					}
				}
				else
					System.out.println("null folder");
				
				String content = "";
				if(textNote != null)
					content = textNote.getContent();
				else
					System.out.println("null textNote");
				textAreaNote.setText(content);

			}
		});
		vbox.getChildren().add(new Label("Choose folder: "));
		vbox.getChildren().add(foldersComboBox);
	
		hbox.getChildren().add(foldersComboBox);
		hbox.getChildren().add(addFolderBtn);
		
		vbox.getChildren().add(hbox);
		vbox.getChildren().add(new Label("Choose note title"));
		vbox.getChildren().add(titleslistView);
		vbox.getChildren().add(addNoteBtn);

		return vbox;
	}

	private void updateListView(ArrayList<String> list) {
		

		// TODO populate the list object with all the TextNote titles of the
		// currentFolder


		ObservableList<String> combox2 = FXCollections.observableArrayList(list);
		titleslistView.setItems(combox2);
		textAreaNote.setText("");
	}

	/*
	 * Creates a grid for the center region with four columns and three rows
	 */
	private GridPane addGridPane() {

		GridPane grid = new GridPane();
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(10, 10, 10, 10));
		
		HBox hbox = new HBox();
		hbox.setPadding(new Insets(0, 0, 0, 0));
		hbox.setSpacing(8); // Gap between nodes
		
		ImageView saveView = new ImageView(new Image(new File("save.png").toURI().toString()));
		saveView.setFitWidth(18);
		saveView.setPreserveRatio(true);
		
		ImageView deleteView = new ImageView(new Image(new File("delete.png").toURI().toString()));
		deleteView.setFitWidth(18);
		deleteView.setPreserveRatio(true);

		
		Button saveNoteBtn = new Button("Save Note");
		saveNoteBtn.setPrefSize(100, 20);
		saveNoteBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				if (currentFolder.isEmpty() || currentNote.isEmpty()) {
					Alert alert = new Alert(Alert.AlertType.WARNING);
					alert.setTitle("Warning");
					alert.setContentText("Please select a folder and a note");
					alert.showAndWait().ifPresent(rs -> {
						if(rs == ButtonType.OK) {
							System.out.println("Pressed OK.");
						}
					});
				}
				else {
					for (Folder folder: noteBook.getFolders()) {
						if (folder.getName().equals(currentFolder)) {
							for (Note note: folder.getNotes()) {
								if (note instanceof TextNote) 
									if (note.getTitle().equals(currentNote)){
										TextNote textnote = (TextNote)note;
										textnote.content = textAreaNote.getText();
									}
							}
						}
					}
				}
			}
		});
		
		Button deleteNoteBtn = new Button("Delete Note");
		deleteNoteBtn.setPrefSize(100, 20);
		deleteNoteBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				boolean successfull = false;
				if (currentFolder.isEmpty() || currentNote.isEmpty()) {
					Alert alert = new Alert(Alert.AlertType.WARNING);
					alert.setTitle("Warning");
					alert.setContentText("Please select a folder and a note");
					alert.showAndWait().ifPresent(rs -> {
						if(rs == ButtonType.OK) {
							System.out.println("Pressed OK.");
						}
					});
				}else {
					for (Folder folder: noteBook.getFolders()) {
						if (folder.getName().equals(currentFolder)) {
							 successfull = folder.removeNote(currentNote);
	               			 titleslistView.getItems().remove(currentNote); 
						}
					}
				}
				
				if (successfull) {
					currentNote = "";
		        	ArrayList<String> list = new ArrayList<String>();
					Folder folder = null;
					for (Folder f : noteBook.getFolders()) {
						if (f.getName().equals(currentFolder) )
							folder = f;
					}
					if (folder != null){
						for (Note n : folder.getNotes()) {
							if (n instanceof TextNote)
								list.add(n.getTitle());
						}
					}
					updateListView(list);
					Alert alert = new Alert(Alert.AlertType.INFORMATION);
					alert.setTitle("Succeed!");
					alert.setContentText("Your note has been successfully removed");
					alert.showAndWait().ifPresent(rs -> {
						if (rs == ButtonType.OK) {
							System.out.println("Pressed OK.");
						}
					});
				}
			}
		});
		
		hbox.getChildren().add(saveView);
		hbox.getChildren().add(saveNoteBtn);
		hbox.getChildren().add(deleteView);
		hbox.getChildren().add(deleteNoteBtn);
		
		textAreaNote.setMaxSize(450, 400);
		textAreaNote.setWrapText(true);
		textAreaNote.setPrefWidth(450);
		textAreaNote.setPrefHeight(400);
		textAreaNote.setEditable(true);
		// 0 0 is the position in the grid
		grid.add(hbox, 0, 0);
		grid.add(textAreaNote, 0, 1);

		return grid;
	}

		
	private void loadNoteBook(File file) {
		FileInputStream fis = null;
		ObjectInputStream ois = null;
		NoteBook nb = null;
		try {
			fis = new FileInputStream(file);
			ois = new ObjectInputStream(fis);
			nb = (NoteBook)ois.readObject();
			ois.close();
			noteBook.getFolders().clear();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		for (Folder f : nb.getFolders()) {
			for (Note n : f.getNotes()) {
				if (n instanceof TextNote) {
					noteBook.createTextNote(f.getName(), ((TextNote) n).getTitle(),((TextNote) n).getContent());
//					System.out.println(f.getName() + " : " + ((TextNote) n).getTitle() + " : " + ((TextNote) n).getContent());
				}
			}
		}
		
    	foldersComboBox.getItems().clear();
		ArrayList<String> foldersName = new ArrayList<String>();
		for (Folder folder : noteBook.getFolders()) {
			foldersName.add(folder.getName());
//			System.out.println(f.getName());
		}
		foldersComboBox.getItems().addAll(foldersName);
		
	}
	private void loadNoteBook() {
		NoteBook nb = new NoteBook();
		nb.createTextNote("COMP3021", "COMP3021 syllabus", "Be able to implement object-oriented concepts in Java.");
		nb.createTextNote("COMP3021", "course information",
				"Introduction to Java Programming. Fundamentals include language syntax, object-oriented programming, inheritance, interface, polymorphism, exception handling, multithreading and lambdas.");
		nb.createTextNote("COMP3021", "Lab requirement",
				"Each lab has 2 credits, 1 for attendence and the other is based the completeness of your lab.");

		nb.createTextNote("Books", "The Throwback Special: A Novel",
				"Here is the absorbing story of twenty-two men who gather every fall to painstakingly reenact what ESPN called “the most shocking play in NFL history” and the Washington Redskins dubbed the “Throwback Special”: the November 1985 play in which the Redskins’ Joe Theismann had his leg horribly broken by Lawrence Taylor of the New York Giants live on Monday Night Football. With wit and great empathy, Chris Bachelder introduces us to Charles, a psychologist whose expertise is in high demand; George, a garrulous public librarian; Fat Michael, envied and despised by the others for being exquisitely fit; Jeff, a recently divorced man who has become a theorist of marriage; and many more. Over the course of a weekend, the men reveal their secret hopes, fears, and passions as they choose roles, spend a long night of the soul preparing for the play, and finally enact their bizarre ritual for what may be the last time. Along the way, mishaps, misunderstandings, and grievances pile up, and the comforting traditions holding the group together threaten to give way. The Throwback Special is a moving and comic tale filled with pitch-perfect observations about manhood, marriage, middle age, and the rituals we all enact as part of being alive.");
		nb.createTextNote("Books", "Another Brooklyn: A Novel",
				"The acclaimed New York Times bestselling and National Book Award–winning author of Brown Girl Dreaming delivers her first adult novel in twenty years. Running into a long-ago friend sets memory from the 1970s in motion for August, transporting her to a time and a place where friendship was everything—until it wasn’t. For August and her girls, sharing confidences as they ambled through neighborhood streets, Brooklyn was a place where they believed that they were beautiful, talented, brilliant—a part of a future that belonged to them. But beneath the hopeful veneer, there was another Brooklyn, a dangerous place where grown men reached for innocent girls in dark hallways, where ghosts haunted the night, where mothers disappeared. A world where madness was just a sunset away and fathers found hope in religion. Like Louise Meriwether’s Daddy Was a Number Runner and Dorothy Allison’s Bastard Out of Carolina, Jacqueline Woodson’s Another Brooklyn heartbreakingly illuminates the formative time when childhood gives way to adulthood—the promise and peril of growing up—and exquisitely renders a powerful, indelible, and fleeting friendship that united four young lives.");

		nb.createTextNote("Holiday", "Vietnam",
				"What I should Bring? When I should go? Ask Romina if she wants to come");
		nb.createTextNote("Holiday", "Los Angeles", "Peter said he wants to go next Agugust");
		nb.createTextNote("Holiday", "Christmas", "Possible destinations : Home, New York or Rome");
		noteBook = nb;

	}

}
