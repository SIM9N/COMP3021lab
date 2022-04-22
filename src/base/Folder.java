package base;

import java.util.ArrayList;
import java.util.List;

public class Folder implements Comparable<Folder>, java.io.Serializable{
	
	private static final long serialVersionUID = -8451287108185475351L;
	private ArrayList<Note> notes;
	private String name;
	
	public Folder(String name) {
		this.name = name;
		notes = new ArrayList<Note>();
	}
	
	public void addNote(Note newNote) {
		notes.add(newNote);
	}
	
	public String getName() {
		return name;
	}
	
	public ArrayList<Note> getNotes(){
		return notes;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Folder other = (Folder) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

	@Override
	public String toString() {
		int nText = 0;
		int nImage = 0;
		for (Note note : notes) {
			if(note instanceof TextNote)
				nText++;
			else if(note instanceof ImageNote)
				nImage++;
		}
		return name + ":" + nText + ":" + nImage;
	}

	@Override
	public int compareTo(Folder o) {
//		System.out.println(name.compareTo(o.name));
		return name.compareTo(o.name);
	}
	
	public void sortNotes() {
		notes.sort(null);
	}
	
	public List<Note> searchNotes(String keywords){
		ArrayList<Note> output = new ArrayList<Note>();
		
		String []arrayOfKeywords = keywords.split("\\s+");
		
		ArrayList<ArrayList<String>> setsOfKeywords = new ArrayList<ArrayList<String>>();
	
		boolean requiredNewList = true;
		
		//append the keywords into 2d arrayList
		for (String word : arrayOfKeywords) {
			
			if(word.equals("or")|| word.equals("OR")) {
				requiredNewList = false;
				continue;
			}
			
			if(requiredNewList) {
				setsOfKeywords.add(new ArrayList<String>());
			}
				
			setsOfKeywords.get(setsOfKeywords.size()-1).add(word);
			
			requiredNewList = true;
		}
//		System.out.println(setOfKeywords);
		
		for (Note note : notes) {
			
			for (ArrayList<String> set: setsOfKeywords) {
				boolean foundMatches = false;
				
				for (String word : set) {
					if(note.getTitle().toLowerCase().contains(word.toLowerCase())) {
						foundMatches = true;
						break;
					}
					else if(note instanceof TextNote) {
						if(((TextNote) note).content.toLowerCase().contains(word.toLowerCase())) {
							foundMatches = true;
							break;
						}
					}
				}
				
				if (!foundMatches)
					break;
				
				if(setsOfKeywords.indexOf(set) == setsOfKeywords.size()-1) {
					output.add(note);
				}
			}
		}

		return output;
		
	}
	
	public boolean removeNote(String title) {
		   // TODO
		   // Given the title of the note, delete it from the folder.
		   // Return true if it is deleted successfully, otherwise return false. 
		for (Note note : notes) {
			if (note.getTitle().equals(title)) {
				notes.remove(note);
				return true;
			}
				
		}
		return false;
		
	}
}
