package base;

import java.util.Date;
import java.util.Objects;

public class Note implements Comparable<Note>{
	private Date date;
	private String title;

	public Note(String title) {
		this.title = title;
		date = new Date(System.currentTimeMillis());
	}
	
	public String getTitle() {return title;}
	
	public boolean equals(Note other) {
		return this.title.equals(other.title);
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(title);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Note other = (Note) obj;
		return Objects.equals(title, other.title);
	}

	@Override
	public int compareTo(Note o) {
//		System.out.println(date.compareTo(o.date));
		return date.compareTo(o.date);
	}
	
	public String toString() {
		return date.toString() + "\t" + title;
	}
}
