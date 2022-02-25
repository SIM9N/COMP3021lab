package base;

import java.util.ArrayList;

public class NoteBook {
	private ArrayList<Folder> folders;
	
	public NoteBook() {
		folders = new ArrayList<Folder>();
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
				System.out.println("Creating note " + note.getTitle() + " under folder " + folderName + " failed");
				return false;
			}
				
		}
		
		f.addNote(note);
		return true;
	}
	
	public boolean createTextNote(String folderName, String name) {
		TextNote note = new TextNote(name);
		return insertNote(folderName, note);
	}
	
	public boolean createImageNote(String folderName, String name) {
		ImageNote note = new ImageNote(name);
		return insertNote(folderName, note);
	}
}
