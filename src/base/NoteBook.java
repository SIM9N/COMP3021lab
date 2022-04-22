package base;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

public class NoteBook implements java.io.Serializable{

	private static final long serialVersionUID = 2114466124144998517L;
	private ArrayList<Folder> folders;
	
	public NoteBook() {
		folders = new ArrayList<Folder>();
	}
	
	public NoteBook(String file) {
		FileInputStream fis = null;
		ObjectInputStream ois = null;
		try {
			fis = new FileInputStream("file.ser");
			ois = new ObjectInputStream(fis);
			NoteBook n = (NoteBook)ois.readObject();
			folders = n.folders;
			ois.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public ArrayList<Folder> getFolders(){
		return folders;
	}
	
	public boolean insertNote(String folderName, Note note) {
		Folder f = null;
		for (Folder f1 : folders) {
			if (f1.getName().equals(folderName))
				f = f1;
		}
		
		if(f == null) {
			Folder newFolder = new Folder(folderName);
			folders.add(newFolder);
			f = folders.get(folders.size() - 1);
		}
		
		for (Note n : f.getNotes()) {
			if (n.equals(note)) {
//				System.out.println("Creating note " + note.getTitle() + " under folder " + folderName + " failed");
				return false;
			}
				
		}
		
		f.addNote(note);
		return true;
	}
	
	public boolean createTextNote(String folderName, String title) {
		TextNote note = new TextNote(title);
		return insertNote(folderName, note);
	}
	
	public boolean createTextNote(String folderName, String title, String content) {
		TextNote note = new TextNote(title, content);
		return insertNote(folderName, note);
	}
	
	public boolean createImageNote(String folderName, String title) {
		ImageNote note = new ImageNote(title);
		return insertNote(folderName, note);
	}
	
	public void sortFolders() {
		for (Folder folder:folders) {
			folder.sortNotes();
		}
	}
	
	public List<Note> searchNotes(String keywords){
		ArrayList<Note> output = new ArrayList<Note>();
		
		for (Folder folder: folders) {
			output.addAll(folder.searchNotes(keywords));
		}
		
		return output;
	}
	
	public boolean containFolder(String folderName) {
		for (Folder folder: folders) {
			if (folder.getName().equals(folderName))
				return true;
		}
		return false;
	}
	
	public void addFolder(String folderName) {
		if (!containFolder(folderName)) {
			Folder folder = new Folder(folderName);
			folders.add(folder);
		}
	}
	
	public boolean save(String file) {
		FileOutputStream fos = null;
		ObjectOutputStream oos = null;
		try {
			fos = new FileOutputStream("file.ser");
			oos = new ObjectOutputStream(fos);
			oos.writeObject(this);
			oos.close();
			
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
}
